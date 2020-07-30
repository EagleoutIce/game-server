package de.uulm.team020.server.core.handlers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameLeftMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.networking.messages.ReconnectMessage;
import de.uulm.team020.networking.messages.RequestGameOperationMessage;
import de.uulm.team020.networking.messages.RequestGamePauseMessage;
import de.uulm.team020.server.addons.rules.StrictModeController;
import de.uulm.team020.server.addons.rules.StrictModeTypeEnum;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.NttsClientManager;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.MessageTargetPair;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.handler.AlreadyServingException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.core.exceptions.handler.NameNotAvailableException;
import de.uulm.team020.server.core.exceptions.management.ClientIdAlreadyPresentException;
import de.uulm.team020.server.core.exceptions.management.PlayerSlotsFullException;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Holds all session Handlers, e.g. to construct the
 * {@link NttsClientConnection} on first contact.
 * 
 * @author Florian Sihler
 * @version 1.1, 04/02/2020
 */
public class SessionHandlers {

    private static final String HANDLER_TXT = "Handler";

    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    /** Hide default constructor */
    private SessionHandlers() {
    }

    /**
     * Ensures the correct attachment
     * 
     * @param conn the connection to check
     * @return the attachment, exception otherwise
     */
    private static NttsClientConnection assertNttsClientConnection(WebSocket conn) throws HandlerException {
        NttsClientConnection connection = conn.getAttachment();
        if (connection == null)
            throw new IllegalMessageException("The first message has to be HELLO, there is no connection attached...");
        return connection;
    }

    public static HandlerReport handleGameLeave(NttsController controller, WebSocket conn, String message)
            throws HandlerException {
        NttsClientConnection connection = assertNttsClientConnection(conn);
        controller.getAuthor().leave(connection.getClientName());

        magpie.writeInfo("Received leave: " + message, HANDLER_TXT);

        UUID clientId = connection.getClientId();

        // maybe with filter?
        if (connection.getClientRole() == RoleEnum.SPECTATOR) {
            GameLeftMessage gameLeftMessage = new GameLeftMessage(clientId, clientId, "You left as a spectator");
            return new HandlerReport(true, new LinkedList<>(),
                    List.of(new MessageTargetPair(gameLeftMessage, new MessageTarget(clientId))), true);
        }

        // Note: broadcasts will always set the correct clientId
        GameLeftMessage gameLeftMessage = new GameLeftMessage(clientId, clientId, "Left as a player");
        HandlerReport report = new HandlerReport(true, new LinkedList<>(),
                List.of(new MessageTargetPair(gameLeftMessage, new MessageTarget())), true);

        connection.setCurrentlyConnected(false);

        return report.setAfterFunctionCall(() -> controller.gameOver(connection, VictoryEnum.VICTORY_BY_LEAVE));
    }

    /**
     * Attaches a new NttsClientConnection to the socket
     * 
     * @param controller The controller that called this handler
     * @param conn       The connection that triggered this handler
     * @param message    The message that triggered this handler
     * 
     * @throws HandlerException If there is an error that should be dealt with by
     *                          the main controller
     * 
     * @return The report informing about Messages to send and after functions to
     *         invoke
     */
    public static HandlerReport handleHelloAndCreateNttsClientConnection(NttsController controller, WebSocket conn,
            String message) throws HandlerException {
        // if the connection already has a valid attachment, we won't assign a new one,
        // this means,
        // there already has been a hello-message by this client
        if (conn.getAttachment() != null) {
            UUID servedByClientId = ((NttsClientConnection) conn.getAttachment()).getClientId();
            ErrorMessage error = new ErrorMessage(servedByClientId, ErrorTypeEnum.ILLEGAL_MESSAGE,
                    "You have already sent a Hello Message!");
            return new HandlerReport("Hello Message duplicate.", error, new MessageTarget(servedByClientId), true);
        }
        final Configuration configuration = controller.getConfiguration();

        // As the filter has to handle the correct type-assignment we just cast:
        HelloMessage helloMessage = GameDataGson.fromJson(message, HelloMessage.class);
        controller.getAuthor().hello(helloMessage.getName(), helloMessage.getRole());

        NttsClientConnection connection = new NttsClientConnection(conn, helloMessage);
        conn.setAttachment(connection);

        NttsClientManager clientManager = Objects.requireNonNull(controller.getClientManager());

        if (helloMessage.getClientId() != null) {
            StrictModeController.mayCriticize(StrictModeTypeEnum.FIELD_IS_NOT_NULL,
                    "Your Hello Message had a clientId assigned");
        }

        if (clientManager.hasName(helloMessage.getName())) {
            throw new NameNotAvailableException(
                    "The requested name (" + connection.getClientName() + ") is already taken!");
        }

        try { // rethrow this as an already-serving message!
            clientManager.registerConnection(connection);
        } catch (ClientIdAlreadyPresentException ex) {
            throw new IllegalMessageException(ex.getMessage());
        } catch (PlayerSlotsFullException ex) {
            throw new AlreadyServingException(ex.getMessage());
        }

        if (configuration.getGamePhase().isAfterOrEqual(GamePhaseEnum.MAIN_GAME_END)) {
            throw new IllegalMessageException(
                    "The main Game has already ended - there is no reason to join it anymore.");
        }

        // We Answer with a HelloReplyMessage
        HelloReplyMessage helloReplyMessage = new HelloReplyMessage(connection.getClientId(),
                configuration.getSessionId(), configuration.getScenario(), configuration.getMatchconfig(),
                configuration.getCharacters());

        if (configuration.getGamePhase().isBefore(GamePhaseEnum.GAME_START)) {
            magpie.writeInfo("Sending the Hello-Reply to greet the client", HANDLER_TXT);
            // Here we request the game-start independent of any change by supplying an
            // 'after-func-call
            return new HandlerReport(helloReplyMessage, new MessageTarget(connection.getClientId()))
                    .setAfterFunctionCall(controller::startGameIfReady);
        } else {
            magpie.writeInfo("Sending the Hello-Reply and Game-Started message to greet the client", HANDLER_TXT);
            // the game has started already, we append the game-started message for the
            // hello-reply
            NttsClientConnection pOne = controller.getClientManager().getPlayerOne();
            NttsClientConnection pTwo = controller.getClientManager().getPlayerTwo();
            GameStartedMessage gameStartedMessage = new GameStartedMessage(connection.getClientId(), pOne.getClientId(),
                    pTwo.getClientId(), pOne.getClientName(), pTwo.getClientName(),
                    controller.getConfiguration().getSessionId());
            return new HandlerReport(
                    List.of(new MessageTargetPair(helloReplyMessage, new MessageTarget(connection.getClientId())),
                            new MessageTargetPair(gameStartedMessage, new MessageTarget(connection.getClientId()))));
        }
    }

    public static HandlerReport handlePauseRequest(NttsController controller, WebSocket conn, String message)
            throws HandlerException {

        RequestGamePauseMessage requestGamePauseMessage = GameDataGson.fromJson(message, RequestGamePauseMessage.class);
        Configuration configuration = controller.getConfiguration();
        NttsClientConnection connection = assertNttsClientConnection(conn);

        if (configuration.getGamePhase().isBefore(GamePhaseEnum.GAME_START)) {
            // TODO: maybe just ignore?
            throw new IllegalMessageException("You are not allowed to request a pause before the game started");
        }

        if (connection.getClientRole() != RoleEnum.PLAYER) {
            throw new IllegalMessageException(
                    "The role '" + connection.getClientRole() + "' isn't allowed to send pause requests.");
        }

        if (requestGamePauseMessage.getGamePause()) {
            controller.getAuthor().pause(connection.getClientName());
            // The client wants to pause the game
            // First of all we check if there isn't already a pause
            if (configuration.getGamePhase().equals(GamePhaseEnum.GAME_PAUSED)
                    || configuration.getGamePhase().equals(GamePhaseEnum.GAME_FORCE_PAUSED)) {
                // currently not standardized,
                // maybe just ignore?
                magpie.writeInfo(
                        "There was a pause-request while a pause was running, the server will just ignore it for now",
                        HANDLER_TXT);
                return null;
            }

            // here a pause would be possible
            if (configuration.getGamePhase() == GamePhaseEnum.ITEM_ASSIGNMENT) {
                // In this case we are holding our belly laughing - and pause the current game
                // turn timers instantly as i do not want to wait to the end
                DraftingPhaseController draftingPhaseController = controller.getDraftingPhaseController();
                magpie.writeInfo("Drafting Phase paused for: " + draftingPhaseController.pause(), HANDLER_TXT);
            } else if (configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_PLAY
                    || configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_READY) {
                // in this case we hold the laughing even harder - nd pause again
                // turn timers are still to be paused...
                MainGamePhaseController mainGamePhaseController = controller.getMainGamePhaseController();
                magpie.writeInfo("Main Game Phase paused for: " + mainGamePhaseController.pause(), HANDLER_TXT);
            }

            GamePauseMessage gamePauseMessage = new GamePauseMessage(null, true, false);
            controller.bufferGamePhase();
            configuration.shiftPhase(GamePhaseEnum.GAME_PAUSED);

            configuration.getTimeoutController().schedulePlayerPause(connection,
                    () -> controller.resumeByPauseEnd(connection));

            controller.getPauseController().resetResumes();

            return new HandlerReport(gamePauseMessage, new MessageTarget());

        } else {
            controller.getAuthor().resume(connection.getClientName());
            // the client wants to avoid a pause, for now, we will just may release the
            // pause
            // checking will be performed by the PauseController...
            if (configuration.getGamePhase().equals(GamePhaseEnum.GAME_PAUSED)) {
                return new HandlerReport().setAfterFunctionCall(() -> controller.resumeByPauseEnd(connection));

            } else {
                // What to do if there is a pause release request with no pause?
                magpie.writeInfo(
                        "There was a resume-request, without any pause, the server will ignore this request for now.",
                        HANDLER_TXT);
            }
        }

        return null;
    }

    private static void welcomeBackPlayer(NttsController controller, NttsClientConnection connection, WebSocket socket)
            throws HandlerException {
        if (connection.isCurrentlyConnected())
            throw new IllegalMessageException(
                    "The clientId you stated, belongs to a player currently connected, you are not allowed to take that session over");

        magpie.writeInfo("The Player " + connection.getClientName() + " was re-acknowledged by new socket: "
                + socket.getRemoteSocketAddress(), HANDLER_TXT);

        connection.setCurrentlyConnected(true);
        connection.setConnection(socket);
        socket.setAttachment(connection);

        Configuration configuration = controller.getConfiguration();
        configuration.getTimeoutController().cancelTimeout(connection);
    }

    public static HandlerReport handleReconnectRequest(NttsController controller, WebSocket conn, String message)
            throws HandlerException {
        Configuration configuration = controller.getConfiguration();
        if (configuration.getGamePhase() != GamePhaseEnum.GAME_FORCE_PAUSED) {
            if (configuration.getServerConfig().unexpectedReconnect()) {
                magpie.writeError("There was an unexpected reconnect-call: " + message
                        + ". But the server-configuration allowed to ignore that", HANDLER_TXT);
            } else {
                throw new IllegalMessageException(
                        "The game is not waiting for someone to reconnect. Maybe the session was already closed?");
            }
        }

        ReconnectMessage reconnectMessage = GameDataGson.fromJson(message, ReconnectMessage.class);

        if (!Objects.equals(reconnectMessage.getSessionId(), configuration.getSessionId())) {
            throw new IllegalMessageException(
                    "The sessionId you requested (requested: " + reconnectMessage.getSessionId() + "; is: "
                            + configuration.getSessionId() + ") is not served by this server.");
        }

        NttsClientManager clientManager = controller.getClientManager();

        NttsClientConnection connection = clientManager.getConnectionByUUID(reconnectMessage.getClientId());

        if (connection == null || Objects.equals(connection, NttsClientConnection.VOID_CONNECTION))
            throw new IllegalMessageException(
                    "The clientId you stated is not registered on this server. Maybe you've exceeded the timeout?");

        controller.getAuthor().reconnect(connection.getClientName());

        if (Objects.equals(connection, clientManager.getPlayerOne())
                || Objects.equals(connection, clientManager.getPlayerTwo())) {
            welcomeBackPlayer(controller, connection, conn);
        } else {
            throw new IllegalMessageException("The clientId you've tried is not registered as a player.");
        }

        // Rebuild GameStarted Message
        NttsClientConnection pOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection pTwo = controller.getClientManager().getPlayerTwo();
        GameStartedMessage gameStartedMessage = new GameStartedMessage(connection.getClientId(), pOne.getClientId(),
                pTwo.getClientId(), pOne.getClientName(), pTwo.getClientName(), configuration.getSessionId());

        List<MessageTargetPair> sendList = new ArrayList<>(3);
        sendList.add(new MessageTargetPair(gameStartedMessage, new MessageTarget(connection.getClientId())));

        // the game phase we returned to, is it a game-phase. As this has to work in a
        // nested pause asswell, we will check if we just entered the phase on a global
        // level and will not check for any nested problems
        if (controller.mainGamePhaseEntered()) {
            magpie.writeDebug("Resending current game status as we are already in game phase", HANDLER_TXT);
            final GameFieldController gameFieldController = controller.getMainGamePhaseController()
                    .getGameFieldController();
            // identify player one and or player two - the case 'no player' is guarded above
            boolean playerOne = connection.getGameRole() == GameRoleEnum.PLAYER_ONE;
            GameStatusMessage gameStatusMessage = new GameStatusMessage(connection.getClientId(), null, null,
                    gameFieldController.getState(playerOne), false);
            sendList.add(new MessageTargetPair(gameStatusMessage, new MessageTarget(connection.getClientId())));
            // Maybe this client needs to re-get his operation-request? lets check...
            Optional<RequestGameOperationMessage> mayRequest = gameFieldController.getOperationRequest(connection);
            if (mayRequest.isPresent()) { // there is a request to resend
                sendList.add(new MessageTargetPair(mayRequest.get(), new MessageTarget(connection.getClientId())));
            }
        }

        if (controller.getBufferedGamePhase() == GamePhaseEnum.GAME_PAUSED) {
            GamePauseMessage gamePauseMessage = new GamePauseMessage(connection.getClientId(), true, false);
            sendList.add(new MessageTargetPair(gamePauseMessage, new MessageTarget(connection.getClientId())));
        }

        return new HandlerReport(sendList).setAfterFunctionCall(() -> controller.resumeByReconnect(connection));
    }

}