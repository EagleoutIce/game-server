package de.uulm.team020.server.game.phases.main;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import org.java_websocket.WebSocket;

import de.uulm.team020.ServerMain;
import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.GambleAction;
import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.Statistics;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Pair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.RequestGameOperationMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.strikes.StrikeController;
import de.uulm.team020.server.addons.timer.TimeoutController;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.NttsClientManager;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.NttsMessageSender;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.dummies.story.helper.StoryLineProducer;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.AbstractPhaseController;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.main.islands.IslandClassifier;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;
import de.uulm.team020.server.game.phases.main.statistics.VictoryAnnouncer;
import de.uulm.team020.validation.GameDataGson;

/**
 * Main controller to handle logic in the main game phase. It will take over
 * from the
 * {@link de.uulm.team020.server.game.phases.choice.DraftingPhaseController
 * DraftingPhaseController} and uses the selected equipment to initiate the main
 * game-field
 * 
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
public class MainGamePhaseController extends AbstractPhaseController {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");
    private static final String HANDLER_TXT = "Handler";

    private GameFieldController gameFieldController;
    private ServerConfiguration serverConfiguration;

    private Queue<List<String>> roundOrderInjectBuffer;

    private List<Pair<UUID, List<GadgetEnum>>> npcPicks;
    // name => startposition
    private Map<String, Point> characterPlacements;
    private int[] safeInjection;

    private NttsClientConnection playerOne;
    private NttsClientConnection playerTwo;

    private TimeoutController turnTimeout;
    private RandomController randomController;

    // cleanup consumer
    private Consumer<NttsClientConnection> onGameOverCleanup;

    // current round
    private UUID activePlayerWaiting;
    private Character characterWaiting;

    /**
     * Construct the main game phase controller. All the data is controlled by the
     * NttsController
     * 
     * @param configuration    The configuration to use
     * @param messageSender    The message-sender to use
     * @param clientManager    The client-manager to use
     * @param author           The story-author writing this session
     * @param strikeController The strike controller to use
     * @param service          The main executor service
     */
    public MainGamePhaseController(Configuration configuration, NttsMessageSender messageSender,
            NttsClientManager clientManager, StoryAuthor author, StrikeController strikeController,
            final ExecutorService service, RandomController randomController) {
        super(configuration, messageSender, clientManager, author, strikeController);
        this.turnTimeout = new TimeoutController(configuration, service);
        this.roundOrderInjectBuffer = new LinkedList<>();
        this.randomController = randomController;
        this.serverConfiguration = configuration.getServerConfig();
    }

    /**
     * This has to be called before starting the phase via {@link #startPhase()} as
     * this passes all the data on, that the main game-field controller needs.
     * 
     * @param playerOneEquipment Equipment of the player one
     * @param playerTwoEquipment Equipment of the player two
     * @param characterPool      Pool of the characters to pick the NPC from.
     *                           Currently this does not respect duplicates
     * @param gadgetPool         POol of the gadgets left that the NPC can equip
     */
    public void setDraftingPhaseData(DraftingEquipment playerOneEquipment, DraftingEquipment playerTwoEquipment,
            List<UUID> characterPool, List<GadgetEnum> gadgetPool) {
        this.gameFieldController = new GameFieldController(configuration, playerOneEquipment, playerTwoEquipment,
                author, randomController, characterPool, gadgetPool, safeInjection, npcPicks, characterPlacements);
        this.gameFieldController.setGameStatusPublisher(this::publishGameStatus);

        for (List<String> roundInject : this.roundOrderInjectBuffer) {
            this.gameFieldController.injectNextRoundOrder(roundInject);
        }
        playerOne = clientManager.getPlayerOne();
        playerTwo = clientManager.getPlayerTwo();
    }

    /**
     * Start the phase -- will init the
     * {@link GameFieldController#nextRound()}-sequence and start with the operation
     * request.
     */
    @Override
    public void startPhase() {
        Objects.requireNonNull(gameFieldController,
                "You cannot start the main game phase without data from the drafting-phase!"); // use setter
        active = true;
        configuration.shiftPhase(GamePhaseEnum.MAIN_GAME_PLAY);
        gameFieldController.nextRound();
        publishGameStatus(serverConfiguration.sendEmptyOperationListOnStart() ? List.of() : null);

        requestNextOperation();
    }

    /**
     * End the phase, end the game
     */
    @Override
    public void endPhase() {
        this.active = false;
        this.turnTimeout.stop();
        clientManager.getRecordKeeper().gameEnded();
        synchronized (ServerMain.endServerRoutine) {
            ServerMain.endServerRoutine.notifyAll();
        }
    }

    /**
     * Sets the state to paused so that no further picks will be handled - this
     * affects both players
     * 
     * @return Value determined by {@link TimeoutController#pauseAllRunning()}
     */
    @Override
    public boolean pause() {
        active = false;
        return this.turnTimeout.pauseAllRunning();
    }

    /**
     * Sets the state to resumed so that further operations will be handled - this
     * affects both players. Call this variant if there was a reconnect. There will
     * be no message-sending this have to be done separately to ensure the message
     * order on pause-resume.
     * 
     * @return Value determined by {@link TimeoutController#resumeAllRunning()}
     */
    public boolean resume() {
        active = true;
        if (configuration.getGamePhase() != GamePhaseEnum.MAIN_GAME_PLAY
                && configuration.getGamePhase() != GamePhaseEnum.MAIN_GAME_READY) {
            throw new ThisShouldNotHappenException(
                    "Main game phase controller was resumed outside of the main game phase :D");
        }

        // may resume messages
        boolean worked = this.turnTimeout.resumeAllRunning();
        // do we wait on a player?
        if (Objects.isNull(this.activePlayerWaiting) && Objects.isNull(characterWaiting)) {
            // request the NEXT operation
            requestNextOperation();
        }
        return worked;

    }

    /**
     * Publish a new GameStatus mapping the calculated changes by the
     * {@link GameFieldController}. The controller will call this one too if eg.
     * there is a npc-movement
     * 
     * @param operationsHappened Operations to record
     */
    public void publishGameStatus(List<BaseOperation> operationsHappened) {
        final Character aC = gameFieldController.getActiveCharacter();
        UUID activeCharacter = aC == null ? null : aC.getCharacterId();
        final boolean gameOver = gameFieldController.isGameOver();

        activeCharacter = maySetToNullIfCatOrJanitorAndNotWanted(aC, activeCharacter);

        // This will allow a finer control:
        GameStatusMessage playerOneStatusMessage = new GameStatusMessage(playerOne.getClientId(), activeCharacter,
                operationsHappened, gameFieldController.getState(true), gameOver);
        GameStatusMessage playerTwoStatusMessage = new GameStatusMessage(playerTwo.getClientId(), activeCharacter,
                operationsHappened, gameFieldController.getState(false), gameOver);
        GameStatusMessage spectatorStatusMessage = new GameStatusMessage(null, activeCharacter, operationsHappened,
                gameFieldController.getSpectatorState(), gameOver);

        // Send:
        messageSender.sendMessage(playerOne.getClientId(), playerOneStatusMessage);
        messageSender.sendMessage(playerTwo.getClientId(), playerTwoStatusMessage);
        messageSender.sendMessage(new MessageTarget(false, false, false, true), spectatorStatusMessage);

        if (operationsHaveHappened(operationsHappened)) {
            enforceGameStatusDelay();
        }

        if (serverConfiguration.liveView()) {
            liveViewField();
        }

        if (gameOver) {
            // System.out.println("Reached GameOver")
            processGameOver();
        }
    }

    private UUID maySetToNullIfCatOrJanitorAndNotWanted(final Character aC, UUID activeCharacter) {
        if (aC != null && activeCharacter != null
                && gameFieldController.getActionProcessor().getGuard().isCatOrJanitor(aC)) {
            activeCharacter = serverConfiguration.catAndJanitorAreActiveCharacter() ? activeCharacter : null;
        }
        return activeCharacter;
    }

    private boolean operationsHaveHappened(List<BaseOperation> operationsHappened) {
        return serverConfiguration.gameStatusDelay() > 0 && operationsHappened != null && !operationsHappened.isEmpty();
    }

    private void enforceGameStatusDelay() {
        try {
            Thread.sleep(serverConfiguration.gameStatusDelay());
        } catch (InterruptedException ex) {
            magpie.writeException(ex, "Pause-Status");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * May be used to print the current state of the field to std-out by providing
     * auto-clear. This shall not be used if the system is not in interactive mode,
     * but it may be enforced by calling this method.
     */
    public void liveViewField() {
        System.out.println("\033[2J\033[0;0H" + IslandClassifier.prettyLegend());
        System.out.println("Current Round: " + gameFieldController.getCurrentRoundNumber() + " (Janitor: "
                + configuration.getMatchconfig().getRoundLimit() + ")");
        System.out.println(gameFieldController.getIslandMask());
        System.out.println("Turn:" + gameFieldController.getActiveCharacter());
        System.out.println("Current round order: " + gameFieldController.dumpCurrentRoundOrder());
    }

    public void requestNextOperation() {
        if (gameFieldController.isGameOver()) {
            magpie.writeWarning("Requested another Operation after an GameOver", "Round");
            if (configuration.getGamePhase().isBefore(GamePhaseEnum.MAIN_GAME_END)) {
                // Just to be sure end gets executed
                publishGameStatus(Collections.emptyList());
            }
            return;
        }

        // only if the current character does not have any stuff left todo we will skip
        // for the next one
        if (Objects.isNull(gameFieldController.getActiveCharacter())
                || !gameFieldController.getActiveCharacter().hasActionsLeft(gameFieldController.getMap())) {
            while (gameFieldController.performNextTurn() || gameFieldController.getActivePlayer() == null)
                ; // perform turns until first player turn
        }

        if (gameFieldController.isGameOver()) {
            magpie.writeWarning("Game Over reached on next op request", HANDLER_TXT);
            return;
        }

        final Boolean isPlayerOneTurn = gameFieldController.getActivePlayer();
        final Character character = gameFieldController.getActiveCharacter();
        if (isPlayerOneTurn == null) {
            throw new ThisShouldNotHappenException("Operation request for non player-character: " + character);
        }
        NttsClientConnection target;
        if (isPlayerOneTurn.booleanValue()) {
            target = playerOne;
        } else {
            target = playerTwo;
        }
        magpie.writeInfo(
                "Send operation request to: " + target.getClientName() + " for character: " + character.getName(),
                "Operation");
        // cancel all other timeouts, just to be sure, we will also handle that there is
        // only one
        turnTimeout.cancelAllRunning();

        if (!active) {
            activePlayerWaiting = null;
            characterWaiting = null;
            return;
        } else {
            // handle a new timeout
            turnTimeout.schedulePlayerTurn(target, () -> this.handleNoOperationFromPlayer(target));
            activePlayerWaiting = target.getClientId();
            characterWaiting = character;
        }
        RequestGameOperationMessage requestGameOperationMessage = new RequestGameOperationMessage(target.getClientId(),
                character.getCharacterId());
        messageSender.sendMessage(target.getClientId(), requestGameOperationMessage);
    }

    private void handleNoOperationFromPlayer(final NttsClientConnection target) {
        if (!active) // just to be safe guarding
            return;
        magpie.writeInfo("No operation from: " + target + " where it was requested for: "
                + gameFieldController.getActiveCharacter(), HANDLER_TXT);
        messageSender.handleReport(target, strikeController.strike(target, "No item-choice from the client."));

        final UUID characterId = gameFieldController.getActiveCharacter().getCharacterId();
        try {
            // log retirement
            author.retire(target.getClientName());
            publishGameStatus(List.of(gameFieldController.getActionProcessor().processRetire(characterId)));
        } catch (HandlerException ex) {
            magpie.writeException(ex, "Round");
            throw new ThisShouldNotHappenException(ex.getMessage());
        }
        requestNextOperation();
    }

    /**
     * Handles a game operation while the game is running
     * 
     * @param controller The main controller the operation request occurred on
     * @param conn       The connection that send the message
     * @param message    The message (as json string) that was transferred,
     *                   depending on the filter settings the message should be a
     *                   {@link GameOperationMessage}.
     * @return The handler report indicating 'how to answer'
     * 
     * @throws HandlerException If the client violated a rule this means the client
     *                          will get an error message based on the explicit
     *                          exception.
     */
    @SuppressWarnings("java:S1172")
    public HandlerReport handleOperation(NttsController controller, WebSocket conn, String message)
            throws HandlerException {
        NttsClientConnection connection = assertNttsClientConnection(conn);

        GameOperationMessage operationMessage = MessageContainer.getMessage(message);
        if (characterWaiting == null || !connection.getClientId().equals(activePlayerWaiting)) {
            // TODO: threshold sending message right round about timeout time - or at least
            // allow if there was a timeout for that player - in coop with timeout
            final String errorText = "There was no operation for you: " + connection.getClientName()
                    + ", so you are not allowed to send the request";
            if (serverConfiguration.escalateOnTooLateOperation()) {
                throw new IllegalMessageException(errorText);
            } else {
                magpie.writeError(errorText, HANDLER_TXT);
                return null; // nothing to be done, but no close reason
            }
        }
        Operation operation = operationMessage.getOperation();
        if (operation == null) {
            throw new IllegalMessageException("The operation you supplied was null in: " + message);
        }

        if (!Objects.equals(operation.getCharacterId(), characterWaiting.getCharacterId())) {
            final String errorText = "The characterId you supplied: " + operation.getCharacterId()
                    + " is not the one waiting for";
            if (serverConfiguration.escalateOnTooLateOperation()) {
                throw new IllegalMessageException(errorText);
            } else {
                magpie.writeError(errorText, HANDLER_TXT);
            }

        }

        List<BaseOperation> operationList = new LinkedList<>();
        // will throw exception if illegal and will perform if legal
        handleOperationWithGuard(operation, connection, message, operationList);
        strikeController.reset(connection);

        if (isAi(connection)) {
            enforceAiDelay();
        }
        // Game-Status-reveal -- if game over this will register automatically
        publishGameStatus(operationList);

        if (active && !gameFieldController.isGameOver())
            requestNextOperation();
        return null;
    }

    private void enforceAiDelay() {
        // No delay
        if (serverConfiguration.aiTurnDelay() <= 0) {
            return;
        }
        // enforce delay on ai turn
        try {
            Thread.sleep(serverConfiguration.aiTurnDelay());
        } catch (InterruptedException ex) {
            magpie.writeException(ex, "Ai-Wait");
            Thread.currentThread().interrupt();
        }
    }

    private boolean isAi(NttsClientConnection connection) {
        return connection.getClientRole() != null && connection.getClientRole().equals(RoleEnum.AI);
    }

    private void handleOperationWithGuard(Operation operation, NttsClientConnection connection, String message,
            List<BaseOperation> operationStack) throws HandlerException {
        final CharacterActionProcessor processor = gameFieldController.getActionProcessor();
        switch (operation.getType()) {
            case CAT_ACTION:
            case JANITOR_ACTION:
            case EXFILTRATION:
                throw new IllegalMessageException(
                        "You are not allowed to send an operation of type: " + operation.getType());
            case RETIRE:
                // retire will always work
                operationStack.add(processor.processRetire(operation));
                author.retire(connection.getClientName());
                break;
            case MOVEMENT:
                operationStack.add(processor.processMovement(connection, operation, message));
                author.movement(connection.getClientName(), operation.getTarget());
                break;
            case GAMBLE_ACTION:
                // we have to re-parse the object this may be changed?
                GambleAction gambleAction = GameDataGson.fromJson(message, "operation", GambleAction.class);
                operationStack.add(processor.processGambleAction(gambleAction, message));
                author.storyLine(StoryLineProducer.gamble(connection.getClientName(), gambleAction.getTarget(),
                        gambleAction.getStake()));
                break;
            case GADGET_ACTION:
                // logging is done inside
                operationStack.addAll(processor.processGadgetAction(connection, operation, message));
                break;
            case PROPERTY_ACTION:
                // logging is done inside
                operationStack.addAll(processor.processPropertyAction(connection, operation, message));
                break;
            case SPY_ACTION:
                operationStack.add(processor.processSpyAction(connection, operation, message));
                author.storyLine(StoryLineProducer.spy(connection.getClientName(), operation.getTarget()));
                break;
            default:
                throw new IllegalMessageException("The type: " + operation.getType() + " is currently not supported.");
        }
    }

    /**
     * This will handle the game over - and switch to the next phase, enable
     * replay-requests in the last cleanup-phase
     */
    public void processGameOver() {
        if (this.configuration.isGameEnded()) { // already ended by game over - ignore
            return;
        }
        this.configuration.setGameEnded(true);
        configuration.shiftPhase(GamePhaseEnum.MAIN_GAME_END);
        activePlayerWaiting = null;
        characterWaiting = null;

        // every character gets ip by chips:
        final int factorChips = configuration.getMatchconfig().getChipsToIpFactor();
        GameFieldStatisticsProvider provider = gameFieldController.getStatisticsProvider();
        gameFieldController.getPlayerOneFaction().forEach(c -> provider.addIP(playerOne, c.getChips() * factorChips));
        gameFieldController.getPlayerTwoFaction().forEach(c -> provider.addIP(playerTwo, c.getChips() * factorChips));
        ImmutablePair<UUID, VictoryEnum> winner = VictoryAnnouncer
                .announceVictor(gameFieldController.getStatisticsProvider(), playerOne, playerTwo, randomController);

        Statistics statistics = gameFieldController.getStatisticsProvider().build();

        StatisticsMessage statisticsMessage = new StatisticsMessage(null, statistics, winner.getKey(),
                winner.getValue(), serverConfiguration.offerReplay());

        messageSender.broadCastWithTargetAssignment(statisticsMessage);

        // Author logging
        author.winner(
                winner.getKey() == playerOne.getClientId() ? playerOne.getClientName() : playerTwo.getClientName(),
                winner.getValue());
        author.statistics(statistics);

        configuration.shiftPhase(GamePhaseEnum.END);

        if (Objects.nonNull(onGameOverCleanup)) {
            onGameOverCleanup.accept(null);
        }

        this.endPhase();
    }

    /**
     * Inject th order of safes on construction
     * 
     * @param order Order of safes
     * 
     * @StoryMethod Used by the {@link StoryBoard} to set the safe-numbers for
     *              construction
     */
    @StoryMethod
    public void injectSafeConstructionOrder(int[] order) {
        this.safeInjection = order;
    }

    /**
     * Inject the equipment and characters of the npc
     * 
     * @param npcPicks NPC-Picks
     * 
     * @StoryMethod Used by the {@link StoryBoard} to set the npc picks.
     */
    @StoryMethod
    public void injectNpcPick(List<Pair<UUID, List<GadgetEnum>>> npcPicks) {
        this.npcPicks = npcPicks;
    }

    /**
     * Inject placements for the characters
     * 
     * @param characterPlacements Positions of the characters
     * 
     * @StoryMethod Used by the {@link StoryBoard} to set the character placements
     */
    @StoryMethod
    public void injectCharacterPlacement(Map<String, Point> characterPlacements) {
        this.characterPlacements = characterPlacements;
    }

    /**
     * Inject the round order for the next round - will stack
     * 
     * @param names Order of the next round
     * 
     * @StoryMethod Used by the {@link StoryBoard} and the {@link BackstoryBuilder}
     *              to set the turn order for the next round (will stack).
     */
    @StoryMethod
    public void injectNextRoundOrder(List<String> names) {
        roundOrderInjectBuffer.add(names);
    }

    /**
     * Set the cleanup consumer on game over
     * 
     * @param onGameOverCleanup the onGameOverCleanup to set
     */
    public void setOnGameOverCleanup(Consumer<NttsClientConnection> onGameOverCleanup) {
        this.onGameOverCleanup = onGameOverCleanup;
    }

    /**
     * Get the game field controller maintained by this Main-GamePhase controller.
     * 
     * @return The underlying game field controller
     */
    public GameFieldController getGameFieldController() {
        return this.gameFieldController;
    }

    /**
     * Get the {@link UUID} of the Player the main game phase controller is waiting
     * for.
     * 
     * @return UUID of the player to be waited on.
     */
    public UUID getPlayerWaiting() {
        return this.activePlayerWaiting;
    }

    /**
     * Set the currently waiting player (don't use)
     * 
     * @param waiting The player to wait for now
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to set the player to wait
     *              for.
     */
    @StoryMethod
    public void setPlayerWaiting(UUID waiting) {
        this.activePlayerWaiting = waiting;
    }

    /**
     * Get the UUID of the character the main game phase controller is waiting on
     * 
     * @return The UUID of the character the controller is waiting on, <i>or</i>
     *         null if there is currently none to be waited on.
     */
    public UUID getCharacterIdWaiting() {
        return characterWaiting == null ? null : characterWaiting.getCharacterId();
    }

}