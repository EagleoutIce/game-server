package de.uulm.team020.server.core;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.java_websocket.WebSocket;

import de.uulm.team020.ServerMain;
import de.uulm.team020.datatypes.Statistics;
import de.uulm.team020.datatypes.StatisticsEntry;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.strikes.StrikeController;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.IMessageFilter;
import de.uulm.team020.server.core.datatypes.IMessageHandler;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.handlers.FreshnessPolicy;
import de.uulm.team020.server.core.handlers.InformationHandlers;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;

/**
 * Handles a Connection using the {@link NttsServer}, the goal is to wrap around
 * the Server implementation and allow the registration of handlers for specific
 * Messages. It will take the Configuration as set in {@link Configuration}.
 * This means:
 * <ol>
 * <li>You can register a handler-function of {@link IMessageHandler} with
 * {@link CallController#registerHandler(MessageTypeEnum, IMessageHandler)} it
 * will get executed whenever the underlying {@link NttsServer} receives a
 * Message with the desired Type. If you need the Handler only for specific
 * senders etc. you can register it via
 * {@link CallController#registerHandler(MessageTypeEnum, IMessageHandler, IMessageFilter)}.</li>
 * 
 * <li>Whenever the {@link NttsServer} receives a Message it will Parse the Data
 * present in {@link MessageContainer} and creates a virtual filter-packet with
 * the Metadata present in {@link NttsClientConnection}.</li>
 * 
 * <li>The server will now check every registered Handler for
 * {@link MessageTypeEnum}, if it has a filter it will only be called if the
 * {@link IMessageFilter#filter(Configuration, RoleEnum, UUID, int, boolean)}
 * call evaluates to <code>true</code></li>
 * </ol>
 * 
 * @author Florian Sihler
 * @version 1.2, 04/01/2020
 */
public class NttsController {

    protected static final String SERVER_TEXT = "Server";
    private static final String HANDLER_TEXT = "Handler";
    private static final String PHASE_TEXT = "Phase";
    protected static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private NttsServer server;

    protected final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final NttsClientManager clientManager;

    protected final Configuration configuration;
    protected GamePhaseEnum bufferedGamePhase;
    protected NttsMessageSender messageSender;

    private final boolean useDummy;

    protected final FreshnessPolicy freshnessPolicy;

    protected StrikeController strikeController;
    protected CloseController closeController;
    protected CallController callController;
    protected PauseController pauseController;

    protected DraftingPhaseController draftingPhaseController;
    protected MainGamePhaseController mainGamePhaseController;
    protected RandomController randomController;

    private boolean mainGamePhaseEntered = false;

    private final StoryAuthor author;

    private Consumer<NttsClientConnection> onGameOverCleanup;

    /**
     * Construct the Controller handling everything from the server to message
     * receiving and sending
     *
     * @param configuration the configuration to use
     */
    public NttsController(final Configuration configuration) {
        this(configuration, false);
    }

    /**
     * Construct the Controller handling everything from the server to message
     * receiving and sending
     *
     * @param configuration The configuration to use.
     * 
     * @param useDummy      True if the server should not register as a 'server' and
     *                      use the test-set instead this mode is deemed to be used
     *                      in testing to avoid integration tests.
     */
    protected NttsController(final Configuration configuration, final boolean useDummy) {
        configuration.shiftNextPhase();

        this.configuration = configuration;
        this.useDummy = useDummy;

        this.freshnessPolicy = new FreshnessPolicy(TimeUnit.SECONDS.toMillis(configuration.getFreshnessThresholdInS()));
        this.clientManager = new NttsClientManager(configuration);
        this.strikeController = new StrikeController(configuration);
        // get desired author verbosity
        this.author = configuration.getServerConfig().produceStoryAuthorBasedOnVerbosity(Configuration.SERVER_NAME,
                this.configuration);
        // This line will be useless if the author doesn't record sleeps, but it is
        // useful otherwise
        this.author.setSleepThreshold(configuration.getServerConfig().storyAuthorSleepThresholdMs());

        // everything below a second shouldn't be vital in real-time

        this.closeController = new CloseController(this);
        this.callController = new CallController(this);
        this.pauseController = new PauseController(this);
        this.randomController = new RandomController(this.author);

        if (useDummy) {
            magpie.writeInfo(
                    "New NttsController running in 'dummymode' using sessionId: " + configuration.getSessionId(),
                    "Construct");
        } else {
            magpie.writeInfo("New NttsController listening on " + configuration.getPort() + " using sessionId: "
                    + configuration.getSessionId(), "Construct");
            this.messageSender = new NttsMessageSender(this);
            final InetSocketAddress address = new InetSocketAddress(configuration.getPort());
            this.server = new NttsServer(address, this);
            server.setConnectionLostTimeout(configuration.getServerConfig().timeoutDetectionTime());
            initController();
            // load handlers
            callController.initDefaultHandlers();
        }

        magpie.writeDebug("Using Server-Configuration:" + this.configuration.getServerConfig(), "Dump");

        configuration.shiftNextPhase();
    }

    /**
     * To be called after all data, by dummy or main controller, has been set.
     */
    protected void initController() {
        this.draftingPhaseController = new DraftingPhaseController(this, this.strikeController, this.executorService);
        this.mainGamePhaseController = new MainGamePhaseController(this.getConfiguration(), this.messageSender,
                this.clientManager, this.author, this.strikeController, this.executorService, randomController);
        mainGamePhaseController.setOnGameOverCleanup(this.onGameOverCleanup);
    }

    /**
     * Delegate for {@link NttsServer#start()}, starts the server and therefore the
     * main routine, won't block the main thread!
     */
    public void start() {
        server.start(); // Start the Server!!
        // Increment the Game-Phase
        configuration.shiftNextPhase();
    }

    /**
     * Stops the underlying server
     * 
     * @throws InterruptedException If timeout wait gets interrupted
     * @throws IOException          If there is an error reading the socket.
     * 
     */
    public void stop() throws IOException, InterruptedException {
        server.stop(1);
        magpie.writeInfo("Server did shutdown.", SERVER_TEXT);
    }

    /**
     * Method to be called by underlying
     * {@link NttsServer#onMessage(WebSocket, String)}
     * 
     * @param conn    the connection the message came from
     * @param message the message supplied
     */
    public void handleCallFor(final WebSocket conn, final String message) {
        magpie.writeDebug("Got Message from '" + conn.getRemoteSocketAddress() + "' (" + message.trim() + ")", "Sever");
        // Somewhat of a synchronization to avoid overload by Client
        executorService.execute(() -> callController.handleMessageCall(conn, message));
        // End of main handling routine
        // lock for sync
    }

    /**
     * Just handles the opening of a connection, without hello-rite
     * 
     * @param conn The connection that said hello
     */
    public void handleOpenFor(final WebSocket conn) {
        magpie.writeInfo("Registered new connection from: " + conn.getRemoteSocketAddress()
                + ". The client will be acknowledged on sending a HELLO-/RECONNECT-Message", SERVER_TEXT);
        if (configuration.getServerConfig().sendMetaOnConnectionOpen()) {
            magpie.writeDebug("Sending Meta-Message to greet the new funny-guy", SERVER_TEXT);
            messageSender.sendMessage(conn, InformationHandlers.generateGreeting(this));
        } else {
            magpie.writeInfo(
                    "Sending no Meta-Message to greet the new funny-guy, as it is disabled in the server-config",
                    SERVER_TEXT);
        }
    }

    /**
     * Method to be called by underlying
     * {@link NttsServer#onClose(WebSocket, int, String, boolean)}
     * 
     * @param conn   The connection the message came from
     * @param code   Code determining close reason
     * @param reason Textual representation of the reason
     * @param remote Was the close initiated by the remote instance?
     */
    public void handleCloseFor(final WebSocket conn, final int code, final String reason, final boolean remote) {
        // Somewhat of a synchronization to avoid overload by Client
        if (useDummy) {
            this.closeController.closeHandlerSchedule(conn, code, reason, remote);
        } else {
            executorService.execute(() -> this.closeController.closeHandlerSchedule(conn, code, reason, remote));
        }
    }

    // Handles game-over logic if the main game play did not start.. maybe
    // group? this might be considered dirty
    /**
     * Handles game over if there is no main game field controller
     * 
     * @param connection  The connection that triggered the game over
     * @param closeReason The reason the victor will be announced
     * 
     * @return True if the game-end was triggered, false if it already has been
     */
    public boolean gameOver(NttsClientConnection connection, VictoryEnum closeReason) {
        if (this.configuration.isGameEnded()) // already ended by game over - ignore
            return false;
        this.configuration.setGameEnded(true);

        draftingPhaseController.endPhase();

        // ship-out statistics

        Statistics statistics = new Statistics(new StatisticsEntry[0]);
        final GameFieldController gfController = mainGamePhaseController.getGameFieldController();
        if (Objects.nonNull(gfController)) {
            GameFieldStatisticsProvider statisticsProvider = gfController.getStatisticsProvider();
            final int factorChips = configuration.getMatchconfig().getChipsToIpFactor();

            gfController.getPlayerOneFaction().forEach(
                    c -> statisticsProvider.addIP(this.clientManager.getPlayerOne(), c.getChips() * factorChips));
            gfController.getPlayerTwoFaction().forEach(
                    c -> statisticsProvider.addIP(this.clientManager.getPlayerTwo(), c.getChips() * factorChips));
            if (Objects.nonNull(statisticsProvider)) {
                statistics = statisticsProvider.build();
            }
        }

        // get other connection, as it will win:
        NttsClientConnection winner = connection.equals(clientManager.getPlayerOne()) ? clientManager.getPlayerTwo()
                : clientManager.getPlayerOne();
        // This guard validates, that the winner is still in game and has not left
        // as well
        StatisticsMessage statisticsMessage = new StatisticsMessage(null, statistics, winner.getClientId(), closeReason,
                configuration.getServerConfig().offerReplay());
        messageSender.broadCastWithTargetAssignment(statisticsMessage);

        // Author logging
        author.winner(winner.getClientName(), closeReason);
        author.statistics(statistics);

        mainGamePhaseController.endPhase();
        if (Objects.nonNull(onGameOverCleanup)) {
            onGameOverCleanup.accept(connection);
        }
        configuration.shiftPhase(GamePhaseEnum.END);

        synchronized (ServerMain.endServerRoutine) {
            ServerMain.endServerRoutine.notifyAll();
        }
        return true; // succeeds for after function caller
    }

    /**
     * Calls {@link NttsServer#broadcast(String)} to send this message to every
     * connected socket
     * 
     * @param message the message to send
     */
    public void broadcast(final MessageContainer message) {
        messageSender.broadCastWithTargetAssignment(message, Collections.emptyList());
    }

    /**
     * Calls {@link NttsServer#broadcast(String)} to send this message to every
     * connected socket
     *
     * @param message  the message to send
     * @param excludes exclude this in broadcast
     *
     */
    public void broadcast(final MessageContainer message, final List<NttsClientConnection> excludes) {
        messageSender.broadCastWithTargetAssignment(message, excludes);
    }

    /**
     * @return True, if the Server (already) started up successfully. False,
     *         otherwise
     */
    public boolean serverIsReady() {
        return this.server.isReady();
    }

    /**
     * @return The linked client manager
     */
    public NttsClientManager getClientManager() {
        return this.clientManager;
    }

    /**
     * @return The linked message sender
     */
    public NttsMessageSender getMessageSender() {
        return this.messageSender;
    }

    /**
     * Sets the phase to {@link GamePhaseEnum#GAME_START} and sends the
     * {@link de.uulm.team020.networking.messages.GameStartedMessage
     * GameStartedMessage} if the phase was
     * {@link GamePhaseEnum#WAIT_FOR_GAME_START}.
     *
     * @return True if the phase did switch, false otherwise
     */
    public boolean startGameIfReady() {
        if (configuration.getGamePhase() != GamePhaseEnum.WAIT_FOR_GAME_START)
            return false;
        // Now we will send a GameStarted-Message to all players
        messageSender.sendGameStartedMessage();
        configuration.shiftPhase(GamePhaseEnum.GAME_START);

        // Start drafting
        draftingPhaseController.startPhase();

        return true;
    }

    /**
     * May be used to resume after the end of a pause -- this will respect the
     * Server-config
     * 
     * @param connection The connection that requested the resume
     * 
     * @return True if performed, false otherwise
     */
    public boolean resumeByPauseEnd(final NttsClientConnection connection) {
        return pauseController.resumeByPauseEnd(connection);
    }

    /**
     * This will perform the transition from drafting to main phase -- but only if
     * all constraints are met.
     * 
     * @return True if the transition was performed, false otherwise
     */
    public boolean shiftToMainIfReady() {
        if (configuration.getGamePhase() != GamePhaseEnum.ITEM_ASSIGNMENT)
            return false; // Not in fitting phase
        final DraftingEquipment playerOneEquipment = draftingPhaseController.getPlayerOneEquipment();
        final DraftingEquipment playerTwoEquipment = draftingPhaseController.getPlayerTwoEquipment();
        if (playerOneEquipment != null && playerTwoEquipment != null) {
            mainGamePhaseController.setDraftingPhaseData(playerOneEquipment, playerTwoEquipment,
                    draftingPhaseController.getCharacterPool(), draftingPhaseController.getGadgetPool());
            // Both have equipped
            goFromDraftingToMainPhase(); // switch
            return true;
        }
        return false;
    }

    /**
     * Returns to the buffered state, if both players have reconnected and sends the
     * end of the pause via a GamePauseMessage(false, true)
     *
     * @param connection The connection that wants the reconnect
     * 
     * @return True if the phase did switch, false otherwise
     */
    public boolean resumeByReconnect(final NttsClientConnection connection) {
        if (configuration.getGamePhase() != GamePhaseEnum.GAME_FORCE_PAUSED)
            return false; // Not in fitting phase

        // Check that both players are connected
        if (clientManager.getPlayerOne().isCurrentlyConnected()
                && clientManager.getPlayerTwo().isCurrentlyConnected()) {

            final GamePauseMessage gamePauseMessage = new GamePauseMessage(null, false, true);
            broadcast(gamePauseMessage, List.of(connection));

            configuration.shiftPhase(bufferedGamePhase);

            // Now we shall un pause the current phase handlers
            if (configuration.getGamePhase() == GamePhaseEnum.ITEM_ASSIGNMENT) {
                magpie.writeInfo("Drafting Phase resumed for: " + draftingPhaseController.resume(connection),
                        HANDLER_TEXT);
            } else if (configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_PLAY
                    || configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_READY) {
                // Resume main game
                magpie.writeInfo("Main Game Phase resumed for: " + mainGamePhaseController.resume(), HANDLER_TEXT);
            }

            return true;
        } else {
            // inform the connection about the enforced pause
            final GamePauseMessage gamePauseMessage = new GamePauseMessage(connection.getClientId(), true, true);
            this.messageSender.sendMessage(connection.getConnection(), gamePauseMessage.toJson());
        }
        return false; // still waiting for players
    }

    /**
     * @return The configuration used by the controller
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    public void bufferGamePhase() {
        this.bufferedGamePhase = configuration.getGamePhase();
    }

    public GamePhaseEnum getBufferedGamePhase() {
        return this.bufferedGamePhase;
    }

    public DraftingPhaseController getDraftingPhaseController() {
        return this.draftingPhaseController;
    }

    public MainGamePhaseController getMainGamePhaseController() {
        return this.mainGamePhaseController;
    }

    public PauseController getPauseController() {
        return this.pauseController;
    }

    public void goFromDraftingToMainPhase() {
        magpie.writeInfo("Switching from drafting to main phase", PHASE_TEXT);
        this.draftingPhaseController.endPhase();
        magpie.writeInfo("Stopped drafting phase", PHASE_TEXT);
        configuration.shiftNextPhase();
        this.mainGamePhaseController.startPhase();
        mainGamePhaseEntered = true;
        magpie.writeInfo("Main phase started", PHASE_TEXT);
    }

    public StoryAuthor getAuthor() {
        return this.author;
    }

    public boolean getUseDummy() {
        return this.useDummy;
    }

    public boolean mainGamePhaseEntered() {
        return this.mainGamePhaseEntered;
    }

    public StrikeController getStrikeController() {
        return strikeController;
    }

    /**
     * Simulate main-phase entering :D
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to simulate the
     *              main-game-phase.
     */
    @StoryMethod
    public void setMainGamePhaseEntered() {
        this.mainGamePhaseEntered = true;
    }

    /**
     * @return the randomController
     */
    public RandomController getRandomController() {
        return randomController;
    }

    public void setGameOverConsumer(Consumer<NttsClientConnection> cleanup) {
        this.onGameOverCleanup = cleanup;
        if (mainGamePhaseController != null) {
            mainGamePhaseController.setOnGameOverCleanup(cleanup);
        }
    }

}