package de.uulm.team020.server.client.controller;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.everit.json.schema.Schema;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameLeftMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.RequestEquipmentChoiceMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.blueprints.AbstractNetworkHandler;
import de.uulm.team020.server.client.data.MetaInformationBuffer;
import de.uulm.team020.server.client.data.UserSessionData;
import de.uulm.team020.server.client.helper.DiggerKeyEnum;
import de.uulm.team020.server.client.helper.InformationDigger;
import de.uulm.team020.server.client.helper.NoConnectionException;
import de.uulm.team020.server.client.view.ConnectionRite;
import de.uulm.team020.server.client.view.GameRite;
import de.uulm.team020.server.client.view.LoginRite;
import de.uulm.team020.server.client.view.SessionRecoveryRite;
import de.uulm.team020.validation.GameDataGson;
import de.uulm.team020.validation.ValidationReport;
import de.uulm.team020.validation.Validator;

/**
 * Main controller instance which is used to center all of the client logic to
 * one spot
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class ServerClientController {

    /** Logging-Reference, will be shared by all Server(+Client)-components */
    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    private static final String HANDLER_TXT = "Handler";

    private static final int MAX_WAIT_INTERVAL = 5;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private UserSessionController sessionController;
    private UserSessionData sessionData;
    private MetaInformationBuffer metaInformationBuffer;

    private final ReentrantLock readyLock;
    private boolean isReady;

    private ServerClientSocket clientSocket;
    private AbstractNetworkHandler handler;

    private CharacterInformation[] characterInformations;
    private final InformationDigger digger;

    private List<MessageContainer> errorBuffer;

    public ServerClientController() {
        super();
        magpie.writeInfo("Acquire Data...", "<init>");
        sessionData = new UserSessionData();
        readyLock = new ReentrantLock();
        this.handler = new ServerClientNetworkHandler(this);
        digger = new InformationDigger();
        metaInformationBuffer = new MetaInformationBuffer(this);
        errorBuffer = new LinkedList<>();
        setupDigger();
    }

    private void setupDigger() {
        digger.insertDigger(DiggerKeyEnum.SERVER, metaInformationBuffer);
    }

    /**
     * Start the client -- this may take over your current shell session
     * 
     * @throws IOException If it fails to acquire the Terminal-Screen
     */
    public void start() throws IOException {
        // Let the user login with a given name
        LoginRite login = new LoginRite(this);
        login.run();
        magpie.writeInfo("User logged in as: " + sessionData.getUserName(), "start");

        // search for recovery information registered for this user
        // uuuh so sloppy

        sessionController = new UserSessionController(sessionData.getUserName());
        boolean hasToConnect = true;
        if (sessionController.hasRecoveryData()) {
            SessionRecoveryRite recovery = new SessionRecoveryRite(this, sessionController);
            recovery.run();
            hasToConnect = !recovery.performedRecovery();
        }

        if (hasToConnect) {
            // connection
            ConnectionRite connection = new ConnectionRite(this, sessionController);
            connection.run();
        }

        clientSocket = new ServerClientSocket(this, sessionData.getServerTarget());
        try {
            setupNetworkConnection();
        } catch (NoConnectionException | InterruptedException ex) {
            magpie.writeException(ex, "Setup");
            ConsoleWriter.getWriter().renderln(
                    "@|red It was not possible to establish a connection to the server. Maybe it is not available?|@");
            // We are done here
            System.exit(1);
        }
        if (!hasToConnect) {
            ConsoleWriter.getWriter().renderln("@|bold,red recovery not implemented, but would recover here|@");
        }

        // Now we drop to the story dialog
        GameRite game = new GameRite(this, sessionController);
        game.run();
    }

    private void setupNetworkConnection() throws InterruptedException {
        this.clientSocket.connect(); // we will not use connectBlocking as i want to see the times for now, this can
        // and probably shall be changed in the future
        synchronized (readyLock) {
            for (int attempts = 1; !isReady && attempts <= MAX_WAIT_INTERVAL; attempts++) {
                final String connectionData = "Waiting for a connection... Refreshing in 1s (" + attempts + "/"
                        + MAX_WAIT_INTERVAL + ")";
                magpie.writeWarning(connectionData, "init");
                ConsoleWriter.getWriter().renderln("@|italic " + connectionData + "|@");
                readyLock.wait(1000); // wait for it to be ready
            }
            if (!isReady) {
                throw new NoConnectionException(MAX_WAIT_INTERVAL);
            } else {
                ConsoleWriter.getWriter()
                        .renderln("@|green Connection established with: '" + sessionData.getServerTarget() + "'|@");
            }
        }

    }

    public void handleMessage(String message) {
        executorService.execute(() -> {
            magpie.writeInfo("Received: " + message, HANDLER_TXT);

            // cast to container
            MessageContainer container = GameDataGson.getContainer(message);
            if (container == null) {
                magpie.writeWarning("The message wasn't valid", HANDLER_TXT);
                return;
            }

            if (sessionData.getClientId() != null
                    && !Objects.equals(container.getClientId(), sessionData.getClientId())) {
                magpie.writeError("The received message is not for this client. Ignored...", HANDLER_TXT);
                return;
            }

            Schema schema = container.getType().getLinkedSchema();
            if (schema != null) {
                ValidationReport report = Validator.validateObject(message, schema);
                if (!report.isValid()) {
                    magpie.writeWarning("The Message had schema-errors:", HANDLER_TXT);
                    report.getReasons().forEach(reason -> magpie.writeWarning(" - " + reason, HANDLER_TXT));
                    return;
                }
            }
            // now we can call the appropriate handler
            switch (container.getType()) {
                case HELLO_REPLY:
                    handler.helloReply(GameDataGson.fromJson(message, HelloReplyMessage.class));
                    break;
                case GAME_STARTED:
                    handler.gameStarted(GameDataGson.fromJson(message, GameStartedMessage.class));
                    break;
                case REQUEST_ITEM_CHOICE:
                    handler.requestItemChoice(GameDataGson.fromJson(message, RequestItemChoiceMessage.class));
                    break;
                case REQUEST_EQUIPMENT_CHOICE:
                    handler.requestEquipmentChoice(GameDataGson.fromJson(message, RequestEquipmentChoiceMessage.class));
                    break;
                case GAME_STATUS:
                    handler.gameStatus(GameDataGson.fromJson(message, GameStatusMessage.class));
                    break;
                case GAME_LEFT:
                    handler.gameLeft(GameDataGson.fromJson(message, GameLeftMessage.class));
                    break;
                case META_INFORMATION:
                    handler.metaInformation(GameDataGson.fromJson(message, MetaInformationMessage.class));
                    break;
                case ERROR:
                    handler.error(GameDataGson.fromJson(message, ErrorMessage.class));
                    break;
                default:
                    magpie.writeError("Received message of type: " + container.getType() + ". There is no handler.",
                            HANDLER_TXT);
            }
        });
    }

    public void awake() {
        synchronized (readyLock) {
            isReady = true;
            readyLock.notifyAll();
        }
    }

    public UserSessionData getSessionData() {
        return this.sessionData;
    }

    public void setSessionData(UUID clientId, UUID sessionId) {
        this.sessionData.setClientId(clientId);
        this.sessionData.setSessionId(sessionId);
        this.sessionData.setCreationDate(new Date());
        try {
            this.sessionController.storeData(this.sessionData);
        } catch (IOException ex) {
            magpie.writeException(ex, "session");
            ConsoleWriter.getWriter().renderln("@|red Unable to saves recovery data for: " + ex.getMessage() + "|@");
        }
    }

    public void onClose(int code, String reason, boolean remote) {
        magpie.writeInfo("Closed for: " + code + " with: " + reason + " from remote: " + remote, "close");
        final ConsoleWriter writer = ConsoleWriter.getWriter();
        writer.renderln("@|red,bold Connection to the server was closed.\nCode: " + code + ", reason: " + reason
                + ", closed by " + (remote ? "remote" : "us") + "|@");
        // shutdown executer service
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            magpie.writeException(ex, "close");
        }
        if (!errorBuffer.isEmpty()) {
            writer.renderln("Here is a list of all errors received:");
            for (MessageContainer errorMessage : errorBuffer) {
                writer.renderln(" - " + errorMessage);
            }
        }
        System.exit(1);
    }

    public void setScenario(Scenario level) {
        // Right now we will ignore this and just dump
    }

    public void setCharacterInformation(CharacterInformation[] characterInformations) {
        this.characterInformations = characterInformations;
    }

    /**
     * Send a message
     * 
     * @param message The message to send
     */
    public void send(MessageContainer message) {
        clientSocket.send(message.toJson());
    }

    public UUID getClientId() {
        return sessionData.getClientId();
    }

    public InformationDigger getDigger() {
        return this.digger;
    }

    /**
     * @return the metaInformationBuffer
     */
    public MetaInformationBuffer getMetaInformationBuffer() {
        return metaInformationBuffer;
    }

    /**
     * @return the sessionController
     */
    public UserSessionController getSessionController() {
        return sessionController;
    }

    /**
     * Update the session data. Probably for recovery.
     * 
     * @param data The data to use
     */
    public void updateSessionData(UserSessionData data) {
        this.sessionData = data;
    }

    /**
     * @return the errorBuffer
     */
    public List<MessageContainer> getErrorBuffer() {
        return errorBuffer;
    }
}