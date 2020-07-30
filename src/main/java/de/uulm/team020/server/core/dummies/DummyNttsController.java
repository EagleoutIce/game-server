package de.uulm.team020.server.core.dummies;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.NttsMessageSender;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.validation.GameDataGson;

/**
 * This Sender will be used by the {@link NttsController} if it is configured to
 * run in dummy mode. There is only one difference to the:
 * {@link NttsMessageSender} is, that Message will not be send via the websocket
 * - they will be written into a Queue in {@link DummyClient} that you can read
 * from via {@link DummyClient#getMessages()} or
 * {@link DummyClient#getMessages(int)}.
 *
 *
 * @author Florian Sihler
 * @version 1.0, 03/31/2020
 */
public class DummyNttsController extends NttsController {

    private static final int MAXIMUM_WAIT_TIME_S = 42;

    private static final String STORY_CALL_FOR_TXT = "Story-CallFor";

    private BiConsumer<WebSocket, String> messageConsumer;

    /**
     * Use the dummy NttsController for handling messages
     *
     * @param configuration the configuration to use
     */
    public DummyNttsController(Configuration configuration) {
        super(configuration, true);
        messageSender = new DummyNttsMessageSender(this);
        initController();

        // init may modified handlers
        callController.initDefaultHandlers();
    }

    @Override
    public void start() {
        // we have no network init here by starting the server
        configuration.shiftPhase(GamePhaseEnum.WAIT_FOR_PLAYERS);
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        // No need to stop the server
        magpie.writeInfo("Server did shutdown.", SERVER_TEXT);
    }

    /**
     * Register a consumer, that will be called whenever there is an incoming
     * message This can be used for testing
     * 
     * @param messageConsumer The method to be called. Has to be non-blocking
     */
    public void setMessageLogger(BiConsumer<WebSocket, String> messageConsumer) {
        this.messageConsumer = messageConsumer;
    }

    // just to inject
    @Override
    public void handleCallFor(final WebSocket conn, final String message) {
        magpie.writeDebug("Got Message from '" + conn.getRemoteSocketAddress() + "' (" + message.trim() + ")", "Sever");

        if (messageConsumer != null) {
            messageConsumer.accept(conn, message);
        }

        Future<Boolean> futureStoryExecution = executorService.submit(() -> {
            callController.handleMessageCall(conn, message);
            return true;
        });

        try {
            futureStoryExecution.get(MAXIMUM_WAIT_TIME_S, TimeUnit.SECONDS);
        } catch (Exception ex) {
            magpie.writeError("Critical execution on story call for message: " + message, STORY_CALL_FOR_TXT);
            magpie.writeException(ex, STORY_CALL_FOR_TXT);
            throw new StoryException(STORY_CALL_FOR_TXT, ex);
        }
    }

    /**
     * Simulate that the Controller received a Message - beside the logging of the
     * 'receiving' the underlying {@link NttsController} will act as if this message
     * was sent by a regular connected client and answer appropriately.
     *
     * @param socket  the socket this message should be send with, you should use it
     *                to identify the 'same' sender.
     * @param message the message the sender wants to send.
     */
    public void simulateMessageReceive(DummyClient socket, String message) {
        handleCallFor(socket, message);
    }

    /**
     * Simulate that the Controller received a Message - beside the logging of the
     * 'receiving' the underlying {@link NttsController} will act as if this message
     * was sent by a regular connected client and answer appropriately.
     *
     * @param socket  the socket this message should be send with, you should use it
     *                to identify the 'same' sender.
     * @param message the message the sender wants to send.
     * 
     * @see #simulateMessageReceive(DummyClient, String)
     */
    public void simulateMessageReceive(DummyClient socket, MessageContainer message) {
        simulateMessageReceive(socket, message.toJson());
    }

    /**
     * Simulate the close of the connection.
     * <p>
     * Please note it's up to you to throw the client away as it is invalidated from
     * this point on.
     *
     * @param socket the client which wants to close the connection. With the
     *               current implementation, you have to call this manually if the
     *               Server closes the connection to you, as this case is not and
     *               should not be handled by the implementation.
     * @param code   the close-code, which can be found with
     *               {@link org.java_websocket.framing.CloseFrame CloseFrame}
     *               constants.
     * @param reason the reason as text.
     * @param remote was the close initiated by a remote host.
     */
    public void simulateClose(DummyClient socket, int code, String reason, boolean remote) {
        socket.setClosed(); // close it :D it will not receive any more messages now (or at least it should)
        handleCloseFor(socket, code, reason, remote);
    }

    /**
     * Simulate the close of the connection.
     * <p>
     * Please note it's up to you to throw the client away as it is invalidated from
     * this point on.
     *
     * @param socket the client which wants to close the connection. With the
     *               current implementation, you have to call this manually if the
     *               Server closes the connection to you, as this case is not and
     *               should not be handled by the implementation.
     * @param remote was the close initiated by a remote host.
     * 
     * @see #simulateClose(DummyClient, int, String, boolean)
     */
    public void simulateNormalClose(DummyClient socket, boolean remote) {
        simulateClose(socket, CloseFrame.NORMAL, "", remote);
    }

    /**
     * Simulate the close of the connection via a crash/quit where the
     * WebSocket-Connection was able to send the close-message.
     * <p>
     * Please note it's up to you to throw the client away as it is invalidated from
     * this point on.
     * 
     * @param socket the client which closed the connection
     *
     * @see #simulateClose(DummyClient, int, String, boolean)
     */
    public void simulateCrashClose(DummyClient socket) {
        simulateClose(socket, CloseFrame.ABNORMAL_CLOSE, "", true);
    }

    /**
     * Simulate a connection close as the client is not responding to a ping-pong
     * <p>
     * Please note it's up to you to throw the client away as it is invalidated from
     * this point on.
     * 
     * @param socket the client which experiences the timeout
     *
     * @see #simulateClose(DummyClient, int, String, boolean)
     */
    public void simulateTimeoutClose(DummyClient socket) {
        simulateClose(socket, CloseFrame.ABNORMAL_CLOSE,
                "The connection was closed because the other endpoint did not respond with a pong in time. For more information check: https://github.com/TooTallNate/Java-WebSocket/wiki/Lost-connection-detection",
                true);
    }

    /**
     * Sets the configuration defaults for matchconfig, scenario and characters and
     * <i>locks them</i>!
     * 
     * @throws IOException If the loading of the internal configuration defaults
     *                     fails.
     */
    public void setConfigDefaults() throws IOException {
        setConfigDefaults(this.configuration);
    }

    /**
     * Sets the configuration defaults for matchconfig, scenario and characters and
     * <i>locks them</i>!
     * 
     * @param configuration the config to inject
     * 
     * @return The modified Configuration - as the one you have passed
     * 
     * @throws IOException If the loading of the internal configuration defaults
     *                     fails.
     */
    public static Configuration setConfigDefaults(Configuration configuration) throws IOException {
        // supply the default Matchconfig, Scenario and CharacterDescriptions
        String characters = GameDataGson.loadInternalJson("defaults/json/default-characters.json");
        configuration.populateCharacters(GameDataGson.fromJson(characters, CharacterDescription[].class));

        String scenario = GameDataGson.loadInternalJson("defaults/json/default-scenario.scenario");
        configuration.setScenario(GameDataGson.fromJson(scenario, Scenario.class));

        String matchconfig = GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match");
        configuration.setMatchconfig(GameDataGson.fromJson(matchconfig, Matchconfig.class));

        return configuration;
    }
}