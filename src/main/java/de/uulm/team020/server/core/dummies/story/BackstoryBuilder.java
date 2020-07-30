package de.uulm.team020.server.core.dummies.story;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.ReplayRecordKeeper;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.story.exceptions.BackstoryException;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;

/**
 * This class <i>cheats</i>. It may be used to produce a {@link StoryBoard}
 * which might be impossible to create due to the currently limited set of story
 * operations or because the states are simply invalid from a game logic
 * perspective (or they would be created by passing invalid steps e.g. a player
 * moving through a wall). But hey - we write a backstory, and everyone is
 * tuning his or her backstory to get what they desire, and if you <i>want</i>
 * this invalid state (for testing reasons of course) you shall have it. <b>Even
 * though you call "build" to get the constructed StoryBoard all the writing is
 * done exactly when you call it (synchronized). This ensures, that at least the
 * current State of the Builder is <i>valid</i>.</b>
 * 
 * <p>
 * This Builder creates a already modified {@link StoryBoard} with no
 * modifications concerning it's current build-mode (e.g. it will not put the
 * Board in {@link StoryBoard#sleepless()}-mode if you don't want it).
 * Furthermore it will inject the underlying {@link NttsController} (harshly)
 * but tries to keep all phase-protocol handlers intact -- it will perform
 * <i>no</i> fake re-creations (e.g. if you say you performed the illegal move
 * the Backstory-Builder just sets your position, without checking for
 * collisions and <i>without</i> sending any message to the owning client.
 * Changes in the backstory just "happen" as if they were always to be true).
 * <p>
 * You can only construct this builder once, after that it is invalid to avoid
 * interference with the {@link Story}/the {@link StoryBoard} as they will take
 * it with the real server logic from there. But if you know <i>what</i> your
 * doing you might use {@link #unlock()} to allow further executions. There will
 * be no additional support for this feature so it might have some restrictions.
 * <p>
 * Although you <i>can</i>, (probably) you should not pass the
 * {@link StoryBoard} to start with if you do <i>not</i> know what your doing.
 * You might have performed some actions, which will get overwritten/invalidated
 * by a Backstory-Build procedure.
 * <p>
 * <b>Advanced construction example:</b> You might want to use a modified
 * configuration. This is possible, and fairly easy to accomplish:
 * 
 * <pre>
 * BackstoryBuilder builder = new BackstoryBuilder(new StoryBoard(Configuration.buildFromArgs("--defaults")));
 * </pre>
 * 
 * Of course, instead of {@code "--defaults"} you can pass other options -- but
 * please keep in mind that the command-line paths should not be used - so in
 * this case you might still want to pre-build your {@link StoryBoard} with some
 * injects.
 * <p>
 * The Builder allows you to use the Drafting-Phase controller, but you really
 * do not have to [it depends on the point you exit]. There are several ways
 * that allow you to skip to the main phase if this is what you desire. Let's
 * look at the steps necessary to get to the main phase.
 * 
 * <ol>
 * <li>Allocate clients that shall play a role in the backstory and/or the story
 * to be produced by this one via
 * {@link #addClient(String, RoleEnum, GameRoleEnum)}</li>
 * <li>Start the drafting phase: {@link #startDraftingPhase()}</li>
 * <li>Allocate the equipments for both players, which can be done with
 * {@link #setPlayerEquipments(ImmutablePair)} or any other overload of this
 * method.</li>
 * <li>Change to preparation of the main game phase via:
 * {@link #prepareMainPhase()}. This does <i>not</i> start the turn-cycle.</li>
 * <li>Start the main game phase with {@link #startMainPhase()}.</li>
 * <li>Have fun and may end the game with: {@link #gameOver()}.</li>
 * </ol>
 * 
 * The builder itself will do it's best to let the changes appear "normal". I've
 * created this build with different builder-steps at first, so that the
 * drafting-phase builder drops to a separat main phase builder, but at there
 * are not that much method drafting-phase only and most of it is main phase i
 * decided, that i do not have the time to perform this separation.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/06/2020
 * @since 1.0
 */
public class BackstoryBuilder {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Story");

    // Some Metadata
    private final String name;
    private boolean finished;
    private boolean locked;

    // This is the only thing all other derive from
    // The other attributes are merely more than "shortcuts"
    private final StoryBoard storyBoard;
    private final Story story;
    private final DummyNttsController controller;

    private final DraftingPhaseController draftingPhaseController;
    private final MainGamePhaseController mainGamePhaseController;

    private final Configuration configuration;
    private final ServerConfiguration serverConfiguration;

    // Guards to simply check if there is no re-injection where it would break the
    // logic from
    // a semantical perspective (e.g. re-injecting a already injected
    // configuration...)
    private boolean injectedEquipmentPlayerOne = false;
    private boolean injectedEquipmentPlayerTwo = false;

    /**
     * Produce a new backstory-builder with default Story-Board and a random name.
     * Please see the general class docs for more information
     * 
     * @throws IOException See {@link StoryBoard#StoryBoard()} for detailed
     *                     information
     * 
     * @see #BackstoryBuilder(StoryBoard, String)
     */
    public BackstoryBuilder() throws IOException {
        this(new StoryBoard());
    }

    /**
     * Produce a new backstory-builder with a random name. Please see the general
     * class docs for more information
     * 
     * @param origins The story-board to derive from
     * 
     * @see #BackstoryBuilder(StoryBoard, String)
     */
    public BackstoryBuilder(final StoryBoard origins) {
        // To be honest, if there are more than ~20, i would be freaking impressed ^^
        this(origins, RandomHelper.randomString(16));
    }

    /**
     * Produce a new backstory-builder with a random name using the given
     * Configuration. Please see the general class docs for more information.
     * 
     * @param configuration The story to start from.
     * 
     * @see #BackstoryBuilder(StoryBoard, String)
     */
    public BackstoryBuilder(final Configuration configuration) {
        // To be honest, if there are more than ~20, i would be freaking impressed ^^
        this(new StoryBoard(configuration), RandomHelper.randomString(16));
    }

    /**
     * Produce a new backstory-builder with no default Story-Board. Please see the
     * general class docs for more information
     * 
     * @param name The name, which will be used to refer the Backstory-Builder. If
     *             you ever have multiple builders, it would be wise (even though it
     *             is optional) to give them unique names.
     * 
     * @throws IOException See {@link StoryBoard#StoryBoard()} for detailed
     *                     information
     * 
     * @see #BackstoryBuilder(StoryBoard, String)
     */
    public BackstoryBuilder(final String name) throws IOException {
        // To be honest, if there are more than ~20, i would be freaking impressed ^^
        this(new StoryBoard(), name);
    }

    /**
     * Produce a new backstory-builder with a given name using the given
     * Configuration. Please see the general class docs for more information.
     *
     * @param configuration The story to start from.
     * @param name          The name, which will be used to refer the
     *                      Backstory-Builder. If you ever have multiple builders,
     *                      it would be wise (even though it is optional) to give
     *                      them unique names.
     *
     * @see #BackstoryBuilder(StoryBoard, String)
     */
    public BackstoryBuilder(final Configuration configuration, final String name) {
        this(new StoryBoard(configuration), name);
    }

    /**
     * Produce a new backstory-builder. Please see the general class docs for more
     * information
     * 
     * @param origins The story-board to derive from
     * @param name    The name, which will be used to refer the Backstory-Builder.
     *                If you ever have multiple builders, it would be wise (even
     *                though it is optional) to give them unique names.
     */
    public BackstoryBuilder(final StoryBoard origins, final String name) {
        this.storyBoard = origins;
        this.name = name;
        magpie.writeInfo("New Backstory-Builder, named: " + name, name);

        magpie.writeInfo("Started Backstory-Builder", name);

        // assign the other
        this.story = this.storyBoard.getStory();
        this.controller = this.story.getController();

        this.configuration = this.controller.getConfiguration();
        this.serverConfiguration = this.configuration.getServerConfig();

        this.draftingPhaseController = this.controller.getDraftingPhaseController();
        this.mainGamePhaseController = this.controller.getMainGamePhaseController();

        this.finished = false;

        this.lock();
    }

    // Guard

    private void assertRunning() {
        if (this.locked && this.finished) {
            throw new BackstoryException(
                    "The backstory: " + name + " was already build and is not to be modified anymore!");
        }
    }

    private void assertMain() {
        if (!this.controller.mainGamePhaseEntered()) {
            throw new BackstoryException(
                    "The call for this is only working if the main-phase was encountered via prepareMainPhase.");
        }
    }

    private void assertRunningMain() {
        assertRunning();
        assertMain();
    }

    private DummyClient assertIsPlayer(final String name) {
        final DummyClient client = story.getClient(name);
        final NttsClientConnection connection = client.getConnection();
        if (connection == null || !connection.getGameRole().isPlayer()) {
            throw new BackstoryException("The client identified by: " + name + " which is: " + client + " and has: "
                    + connection + " as connection is no player");
        }
        return client;
    }

    /**
     * Will unlock the builder so you are able to perform changes after accessing
     * the story for other manipulations.
     * 
     * @see #lock()
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder unlock() {
        locked = false;
        magpie.writeInfo("Backstory builder was unlocked", name);
        return this;
    }

    /**
     * Will lock the builder so you are (no longer) able to perform changes after
     * accessing the story for other manipulations. This is the default and there is
     * no need to call this method if you haven't called {@link #unlock()} earlier.
     * 
     * @see #unlock()
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder lock() {
        locked = true;
        magpie.writeInfo("Backstory builder was locked", name);
        return this;
    }

    /**
     * Denotes, if the builder is locked by {@link #lock()} (which is the default)
     * or not.
     * 
     * @return True if locked, false otherwise.
     */
    public boolean isLocked() {
        return this.locked;
    }

    // Builder to construct the final story/board

    /**
     * Get the underlying story-board. <i>Once</i>.
     * 
     * @return The built Story-Board including the story and all modifications.
     * 
     * @see #buildStory()
     */
    public StoryBoard build() {
        assertRunning();
        this.finished = true;
        return this.storyBoard;
    }

    /**
     * You might not need the {@link StoryBoard}. If you are just interested in the
     * written Story you can call this method as a shortcut for
     * {@link StoryBoard#build()}.
     * 
     * @return The built Story with all modifications.
     */
    public Story buildStory() {
        assertRunning();
        this.finished = true;
        return this.storyBoard.build();
    }

    /**
     * You might not need the {@link StoryBoard}. If you are just interested in the
     * written Story you can call this method as a shortcut for
     * {@link StoryBoard#build(int)}.
     *
     * @param delayInMs See {@link StoryBoard#build(int)}.
     * 
     * @return The built Story with all modifications.
     */
    public Story buildStory(final int delayInMs) {
        assertRunning();
        this.finished = true;
        return this.storyBoard.build(delayInMs);
    }

    // Client management

    /**
     * This will <i>not</i> call the
     * {@link StoryBoard#hello(String, RoleEnum)}-routine. There will be <i>no</i>
     * hello-hello-reply message exchange. The client will be injected but still be
     * available via the {@link Story#getClient(String)}-feature afterwards.
     * <p>
     * Add a character to your backstory ;)
     * 
     * @param name     Name of the client desired
     * @param initRole The role the client registers with -- should correspond with
     *                 the given game role (e.g. do not set a spectator to player
     *                 one if you do not want this -- and you probably don't).
     * @param gameRole The role this client should take - if there is already client
     *                 taking that role, it will be overwritten. The
     *                 {@link ReplayRecordKeeper} might be effected by this.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder addClient(final String name, final RoleEnum initRole, final GameRoleEnum gameRole) {
        assertRunning();
        // Create the corresponding DummyClient:
        final DummyClient newDummy = new DummyClient(controller);
        // Add the client to the story
        story.addClient(newDummy);
        // Assign it with a Ntts-Connection simulating the correct appearance
        final NttsClientConnection connection = new NttsClientConnection(newDummy, UUID.randomUUID(), name, initRole);
        // Set the attachment for the server
        newDummy.setAttachment(connection);
        // inject the client manager so it will identify the connection correctly
        controller.getClientManager().injectConnection(connection, gameRole);
        // and out
        return this;
    }

    // phase management

    /**
     * This will change the current phase of the server to the drafting phase, even
     * if you can, you only should call this if:
     * <ul>
     * <li>There are enough players.</li>
     * <li>You are not already in the drafting-phase.</li>
     * <li>You are not already in a later phase like the main-game phase.</li>
     * </ul>
     * 
     * Will not broadcast game-started.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder startDraftingPhase() {
        return startDraftingPhase(false);
    }

    /**
     * This will change the current phase of the server to the drafting phase, even
     * if you can, you only should call this if:
     * <ul>
     * <li>There are enough players.</li>
     * <li>You are not already in the drafting-phase.</li>
     * <li>You are not already in a later phase like the main-game phase.</li>
     * </ul>
     * 
     * @param sendGameStarted Shall the dummy-clients get the game-started message?
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder startDraftingPhase(final boolean sendGameStarted) {
        assertRunning();
        if (sendGameStarted) {
            controller.getMessageSender().sendGameStartedMessage();
        }
        configuration.shiftPhase(GamePhaseEnum.GAME_START);
        // Start drafting
        draftingPhaseController.startPhase(false);
        return this;
    }

    /**
     * This will change the current phase of the server to the main phase, even if
     * you can, you only should call this if:
     * <ul>
     * <li>There are enough players.</li>
     * <li>You have already been in the drafting-phase.</li>
     * <li>You are not already in the main-game phase or any (chronologically) later
     * one.</li>
     * </ul>
     * Please note, that phase-processing does <b>not</b> start with this step - it
     * will allow you to hack the field, character and round data directly.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder prepareMainPhase() {
        assertRunning();

        // shift the data to main:
        this.mainGamePhaseController.setDraftingPhaseData(draftingPhaseController.getPlayerOneEquipment(),
                draftingPhaseController.getPlayerTwoEquipment(), draftingPhaseController.getCharacterPool(),
                draftingPhaseController.getGadgetPool());
        // end the drafting phase
        this.draftingPhaseController.endPhase();
        // engage preparation:
        configuration.shiftNextPhase();
        // we do NOT start the main game phase here as this would mean
        // operation requests would start running and we would not be able to
        // control npc etc. positions. So we avoid
        // calling this: 'this.mainGamePhaseController.startPhase()'
        // so we have jet entered the phase as the main controller does not really
        // no the concept of "no phase".
        controller.setMainGamePhaseEntered();
        return this;
    }

    /**
     * This will start the main game phase logic, which means the 'nextRound' calls
     * will occur. <b>Only call after {@link #prepareMainPhase()}, once.</b>.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder startMainPhase() {
        assertRunningMain();
        mainGamePhaseController.startPhase();
        return this;
    }

    // equipment stuff for drafting phase

    /**
     * This will set the equipment choices but decode the character name to a given
     * equipment
     * 
     * @param equipment The equipment that shall be decoded
     * 
     * @return The built drafting equipment
     */
    public DraftingEquipment decodeEquipment(final Map<String, List<GadgetEnum>> equipment) {
        final DraftingEquipment eq = new DraftingEquipment();
        for (final Entry<String, List<GadgetEnum>> decode : equipment.entrySet()) {
            // Decode the character Id:
            final Optional<UUID> mayCharacterId = story.validCharacter(decode.getKey());
            if (!mayCharacterId.isPresent()) {
                throw new BackstoryException(
                        "The character: " + decode.getKey() + " does not exist with the configuration");
            }
            eq.put(mayCharacterId.get(), decode.getValue());
        }
        return eq;
    }

    /**
     * Sometimes you do not need to set the equipment. This is a stripped-down call
     * to {@link #constructValidEquipments(Map, int, Map, int)}.
     * 
     * @param maxPlayerOne Maximum characters playerOne should own.
     * @param maxPlayerTwo Maximum characters playerTwo should own.
     * 
     * @return Populated Pair: (playerOneEquipment, playerTwoEquipment)
     */
    public ImmutablePair<DraftingEquipment, DraftingEquipment> constructValidEquipments(final int maxPlayerOne,
            final int maxPlayerTwo) {
        return constructValidEquipments(Map.of(), maxPlayerOne, Map.of(), maxPlayerTwo);
    }

    /**
     * Sometimes you do not need to set all the equipment and to get all the
     * equipment wanted, so this will build the equipments for p1 and p2 at random,
     * yet you may choose one character which player one will possess for sure. This
     * is a stripped-down call to
     * {@link #constructValidEquipments(Map, int, Map, int)}.
     * 
     * @param ensureCharacter Character which player one gets for sure
     * @param ensuredGadgets  Gadgets that the given character will get
     * @param maxPlayerOne    Maximum characters playerOne should own. Should be at
     *                        least one.
     * @param maxPlayerTwo    Maximum characters playerTwo should own.
     * 
     * @return Populated Pair: (playerOneEquipment, playerTwoEquipment)
     */
    public ImmutablePair<DraftingEquipment, DraftingEquipment> constructValidEquipments(final String ensureCharacter,
            final List<GadgetEnum> ensuredGadgets, final int maxPlayerOne, final int maxPlayerTwo) {
        return constructValidEquipments(Map.of(ensureCharacter, ensuredGadgets), maxPlayerOne, Map.of(), maxPlayerTwo);
    }

    /**
     * Sometimes you do not need to set all the equipment and to get all the
     * equipment wanted, so this will build the equipments for p1 and p2 at random,
     * yet you may choose one character which player one will possess for sure and
     * another one, which will be owned by player two. This is a stripped-down call
     * to {@link #constructValidEquipments(Map, int, Map, int)}.
     * 
     * @param ensureCharacterOne Character which player one gets for sure
     * @param ensuredGadgetsOne  Gadgets that the given character will get
     * @param ensureCharacterTwo Character which player one gets for sure
     * @param ensuredGadgetsTwo  Gadgets that the given character will get
     * @param maxPlayerOne       Maximum characters playerOne should own. Should be
     *                           at least one.
     * @param maxPlayerTwo       Maximum characters playerTwo should own. Should be
     *                           at least one.
     * 
     * @return Populated Pair: (playerOneEquipment, playerTwoEquipment)
     */
    public ImmutablePair<DraftingEquipment, DraftingEquipment> constructValidEquipments(final String ensureCharacterOne,
            final List<GadgetEnum> ensuredGadgetsOne, final String ensureCharacterTwo,
            final List<GadgetEnum> ensuredGadgetsTwo, final int maxPlayerOne, final int maxPlayerTwo) {
        return constructValidEquipments(Map.of(ensureCharacterOne, ensuredGadgetsOne), maxPlayerOne,
                Map.of(ensureCharacterTwo, ensuredGadgetsTwo), maxPlayerTwo);
    }

    /**
     * Sometimes you do not need to set all the equipment and to get all the
     * equipment wanted, so this will build the equipments for p1 and p2 based on
     * your requested minima in a random fashion trying to not invalidate any
     * constraints (so that the configuration, besides whatever you hae done ;), is
     * valid).
     * 
     * @param minPlayerOne Things you want playerOne to have
     * @param maxPlayerOne Maximum characters playerOne should own. If this is lower
     *                     than the injected size, the other one will be ignored
     * @param minPlayerTwo Things you want playerTwo to have
     * @param maxPlayerTwo Maximum characters playerTwo should own. If this is lower
     *                     than the injected size, the other one will be ignored
     * 
     * @return Populated Pair: (playerOneEquipment, playerTwoEquipment)
     */
    public ImmutablePair<DraftingEquipment, DraftingEquipment> constructValidEquipments(
            final Map<String, List<GadgetEnum>> minPlayerOne, final int maxPlayerOne,
            final Map<String, List<GadgetEnum>> minPlayerTwo, final int maxPlayerTwo) {
        assertRunning();
        final List<Entry<String, List<GadgetEnum>>> playerOneChoices = new ArrayList<>(minPlayerOne.entrySet());
        final List<Entry<String, List<GadgetEnum>>> playerTwoChoices = new ArrayList<>(minPlayerTwo.entrySet());
        // populate some stuff for player one
        final List<UUID> possibleCharacters = Arrays.stream(configuration.getCharacters()).filter(
                c -> !minPlayerOne.keySet().contains(c.getName()) && !minPlayerTwo.keySet().contains(c.getName()))
                .map(CharacterInformation::getId).collect(Collectors.toCollection(LinkedList::new));
        final Stream<GadgetEnum> gadgetStream = List.of(GadgetEnum.values()).stream()
                .filter(g -> g != GadgetEnum.COCKTAIL && g != GadgetEnum.DIAMOND_COLLAR)
                .filter(g -> minPlayerOne.entrySet().stream().noneMatch(e -> e.getValue().contains(g)))
                .filter(g -> minPlayerTwo.entrySet().stream().noneMatch(e -> e.getValue().contains(g)));
        final List<GadgetEnum> possibleGadgets = gadgetStream.collect(Collectors.toCollection(LinkedList::new));
        final DraftingEquipment playerOneEquipment = populateByEnforced(playerOneChoices, maxPlayerOne,
                possibleCharacters, possibleGadgets);
        final DraftingEquipment playerTwoEquipment = populateByEnforced(playerTwoChoices, maxPlayerTwo,
                possibleCharacters, possibleGadgets);

        return ImmutablePair.of(playerOneEquipment, playerTwoEquipment);
    }

    private DraftingEquipment populateByEnforced(final List<Entry<String, List<GadgetEnum>>> playerChoices,
            final int max, final List<UUID> possibleCharacters, final List<GadgetEnum> possibleGadgets) {
        final DraftingEquipment constructEquipment = new DraftingEquipment();
        for (int i = 0; i < max; i++) {
            if (i < playerChoices.size()) {
                // use from "enforced"
                final Entry<String, List<GadgetEnum>> enforce = playerChoices.get(i);
                // try to decode name:
                final Optional<UUID> mayCharacterId = story.validCharacter(enforce.getKey());
                if (!mayCharacterId.isPresent()) {
                    throw new BackstoryException(
                            "The character: " + enforce.getKey() + " does not exist with the configuration");
                }
                final UUID id = mayCharacterId.get();
                // remove from possible
                possibleCharacters.remove(id);
                possibleGadgets.removeAll(enforce.getValue());
                // add to the equipment
                constructEquipment.put(mayCharacterId.get(), enforce.getValue());
            } else {
                // populate at random
                final UUID rndId = RandomHelper.rndPick(possibleCharacters);
                if (rndId == null) {
                    break; // there are no characters left
                }
                possibleCharacters.remove(rndId);

                // get some gadgets? maybe -- we make it easy, as we don't care we will just
                // pick one
                final GadgetEnum randomGadget = RandomHelper.rndPick(possibleGadgets);
                if (randomGadget == null) {
                    // no gadget
                    constructEquipment.put(rndId, new LinkedList<>());
                } else {
                    final List<GadgetEnum> gadgets = new LinkedList<>();
                    possibleGadgets.remove(randomGadget);
                    gadgets.add(randomGadget);
                    constructEquipment.put(rndId, gadgets);
                }
            }
        }

        return constructEquipment;
    }

    /**
     * Sets the equipment for the given player -- this will cause no further
     * equipment - sets to be changed, so the request-item message dialog will
     * definitely get damaged. You are able to equip items which are not equip-able
     * in drafting and to equip items which are already used.
     * 
     * @param playerName Name to inject the equipment for -- this will fail if there
     *                   is none by
     *                   {@link #addClient(String, RoleEnum, GameRoleEnum)} or if
     *                   the given one cannot have equipment from a technical
     *                   perspective.
     * @param equipment  The equipment the player shall have. As this needs the UUID
     *                   you might use {@link #decodeEquipment(Map)} first.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipment(final String playerName, final DraftingEquipment equipment) {
        assertRunning();
        final DummyClient got = assertIsPlayer(playerName);
        final NttsClientConnection connection = got.getConnection();
        switch (connection.getGameRole()) {
            case PLAYER_ONE:
                if (injectedEquipmentPlayerOne) {
                    throw new BackstoryException("You already have injected an Equipment for player one: "
                            + draftingPhaseController.getPlayerOneEquipment());
                }
                draftingPhaseController.injectPlayerOneEquipment(equipment);
                injectedEquipmentPlayerOne = true;
                break;
            case PLAYER_TWO:
                if (injectedEquipmentPlayerTwo) {
                    throw new BackstoryException("You already have injected an Equipment for player two: "
                            + draftingPhaseController.getPlayerTwoEquipment());
                }
                draftingPhaseController.injectPlayerTwoEquipment(equipment);
                injectedEquipmentPlayerTwo = true;
                break;
            default:
                throw new ThisShouldNotHappenException(
                        "Set player equipment passed with non-player: " + playerName + " got: " + got);
        }
        // clear up the pools - as we do not check them on injection this will just set
        // the correct data for the npc-creation
        draftingPhaseController.getCharacterPool().removeIf(equipment.keySet()::contains);
        draftingPhaseController.getGadgetPool().removeIf(g -> equipment.values().stream().anyMatch(l -> l.contains(g)));
        return this;
    }

    /**
     * Sets the equipment for both players -- this will cause no further equipment -
     * sets to be changed, so the request-item message dialog will definitely get
     * damaged. This will use {@link #constructValidEquipments(int, int)} to
     * populate the equipments and then redirect the result to
     * {@link #setPlayerEquipments(ImmutablePair)} for the population-process, so it
     * is merely more than a shortcut.
     * 
     * @param maxPlayerOne Maximum characters playerOne should own.
     * @param maxPlayerTwo Maximum characters playerTwo should own.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipments(final int maxPlayerOne, final int maxPlayerTwo) {
        return setPlayerEquipments(constructValidEquipments(maxPlayerOne, maxPlayerTwo));
    }

    /**
     * Sets the equipment for both players -- this will cause no further equipment -
     * sets to be changed, so the request-item message dialog will definitely get
     * damaged. This will use
     * {@link #constructValidEquipments(String, List, int, int)} to populate the
     * equipments and then redirect the result to
     * {@link #setPlayerEquipments(ImmutablePair)} for the population-process, so it
     * is merely more than a shortcut.
     * 
     * @param ensureCharacter Character which player one gets for sure
     * @param ensuredGadgets  Gadgets that the given character will get
     * @param maxPlayerOne    Maximum characters playerOne should own.
     * @param maxPlayerTwo    Maximum characters playerTwo should own.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipments(final String ensureCharacter, final List<GadgetEnum> ensuredGadgets,
            final int maxPlayerOne, final int maxPlayerTwo) {
        return setPlayerEquipments(
                constructValidEquipments(ensureCharacter, ensuredGadgets, maxPlayerOne, maxPlayerTwo));
    }

    /**
     * Sets the equipment for both players -- this will cause no further equipment -
     * sets to be changed, so the request-item message dialog will definitely get
     * damaged. This will use {@link #constructValidEquipments(Map, int, Map, int)}
     * to populate the equipments and then redirect the result to
     * {@link #setPlayerEquipments(ImmutablePair)} for the population-process, so it
     * is merely more than a shortcut.
     * 
     * @param ensureCharacterOne Character which player one gets for sure
     * @param ensuredGadgetsOne  Gadgets that the given character will get
     * @param ensureCharacterTwo Character which player one gets for sure
     * @param ensuredGadgetsTwo  Gadgets that the given character will get
     * @param maxPlayerOne       Maximum characters playerOne should own. Should be
     *                           at least one.
     * @param maxPlayerTwo       Maximum characters playerTwo should own. Should be
     *                           at least one.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipments(final String ensureCharacterOne,
            final List<GadgetEnum> ensuredGadgetsOne, final String ensureCharacterTwo,
            final List<GadgetEnum> ensuredGadgetsTwo, final int maxPlayerOne, final int maxPlayerTwo) {
        return setPlayerEquipments(constructValidEquipments(Map.of(ensureCharacterOne, ensuredGadgetsOne), maxPlayerOne,
                Map.of(ensureCharacterTwo, ensuredGadgetsTwo), maxPlayerTwo));
    }

    /**
     * Sets the equipment for both players -- this will cause no further equipment -
     * sets to be changed, so the request-item message dialog will definitely get
     * damaged. This will use {@link #constructValidEquipments(Map, int, Map, int)}
     * to populate the equipments and then redirect the result to
     * {@link #setPlayerEquipments(ImmutablePair)} for the population-process, so it
     * is merely more than a shortcut.
     * 
     * @param minPlayerOne Things you want playerOne to have
     * @param maxPlayerOne Maximum characters playerOne should own. If this is lower
     *                     than the injected size, the other one will be ignored
     * @param minPlayerTwo Things you want playerTwo to have
     * @param maxPlayerTwo Maximum characters playerTwo should own. If this is lower
     *                     than the injected size, the other one will be ignored
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipments(final Map<String, List<GadgetEnum>> minPlayerOne,
            final int maxPlayerOne, final Map<String, List<GadgetEnum>> minPlayerTwo, final int maxPlayerTwo) {
        return setPlayerEquipments(constructValidEquipments(minPlayerOne, maxPlayerOne, minPlayerTwo, maxPlayerTwo));
    }

    /**
     * Sets the equipment for both players -- this will cause no further equipment -
     * sets to be changed, so the request-item message dialog will definitely get
     * damaged. You are able to equip items which are not equip-able in drafting and
     * to equip items which are already used.
     * 
     * @param equipments Equipments to be placed, formatted like:
     *                   (playerOneEquipment, playerTwoEquipment), probably built by
     *                   {@link #constructValidEquipments(Map, int, Map, int)} or
     *                   any of its overloads.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setPlayerEquipments(final ImmutablePair<DraftingEquipment, DraftingEquipment> equipments) {
        assertRunning();
        if (injectedEquipmentPlayerOne) {
            throw new BackstoryException("You already have injected an Equipment for player one: "
                    + draftingPhaseController.getPlayerOneEquipment());
        }
        draftingPhaseController.injectPlayerOneEquipment(equipments.getKey());
        injectedEquipmentPlayerOne = true;
        if (injectedEquipmentPlayerTwo) {
            throw new BackstoryException("You already have injected an Equipment for player two: "
                    + draftingPhaseController.getPlayerTwoEquipment());
        }
        draftingPhaseController.injectPlayerTwoEquipment(equipments.getValue());
        injectedEquipmentPlayerTwo = true;
        // clear up the pools - as we do not check them on injection this will just set
        // the correct data for the npc-creation
        draftingPhaseController.getCharacterPool().removeIf(equipments.getKey().keySet()::contains);
        draftingPhaseController.getGadgetPool()
                .removeIf(g -> equipments.getKey().values().stream().anyMatch(l -> l.contains(g)));
        draftingPhaseController.getCharacterPool().removeIf(equipments.getValue().keySet()::contains);
        draftingPhaseController.getGadgetPool()
                .removeIf(g -> equipments.getValue().values().stream().anyMatch(l -> l.contains(g)));
        // (done that just so i can pretend to be lazy, even if it means more lines :P
        // who needs methods for readability?)
        return this;
    }

    // main game phase modifications

    /**
     * Gives you the character going by this name in the current story - the stats
     * of the character are the ones he currently has and if you change them you
     * will change the actual state of the character -- even if the backstory has
     * ended, so this is more a convenience function than it is directly linked with
     * the building process. It will use the {@link MainGamePhaseController} to
     * access any character in the list of all characters. This means, if they are
     * present on (any valid) field you can access the cat via {@code "<cat>"} and
     * the janitor with {@code "<janitor>"}, all other characters use the names they
     * have been given with the description or the inflation by the
     * {@link Configuration#populateCharacters(de.uulm.team020.datatypes.CharacterDescription[])}-call.
     * <p>
     * This may be used even if the builder is locked down due to a
     * {@link #build()}-call.
     * 
     * @param characterName The name of the character you like
     * @return The Character - this may only characters in the game
     */
    public Character getCharacter(final String characterName) {
        assertMain();
        // get me the main game field controller:
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        // give us this character
        final Optional<Character> character = gfFieldController.decodeCharacterByName(characterName);
        if (!character.isPresent()) {
            throw new BackstoryException("The character you wanted: " + characterName
                    + " is not part of the currently active characters: " + gfFieldController.getAllCharacters());
        }
        return character.get();
    }

    /**
     * Can be used like the injection mechanism of the {@link StoryBoard} but does
     * not want <i>all</i> characters. If you supply an insufficient amount of
     * characters, the builder will fill the remaining slots with characters you
     * have not used. The injections can handle invalid configurations (e.g. if you
     * write three times the same character).
     * 
     * @param characters Names of the characters desired. As in
     *                   {@link #getCharacter(String)} you can use {@code "<cat>"}
     *                   to get the cat and {@code "<janitor>"} for the janitor.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder injectNextRoundOrder(final String... characters) {
        assertRunningMain();
        // get me the main game field controller:
        final List<String> selectedCharacters = new LinkedList<>(Arrays.asList(characters));
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        final List<String> allCharacterNames = gfFieldController.getAllCharacters().stream().map(Character::getName)
                .collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(allCharacterNames);
        // remove all given
        allCharacterNames.removeAll(selectedCharacters);
        selectedCharacters.addAll(allCharacterNames);
        gfFieldController.injectNextRoundOrder(selectedCharacters);
        return this;
    }

    /**
     * This allows you to change the round number - the value should be greater than
     * one, this allows you to summon the janitor at will. Please note, that you
     * have to get the correct round number to summon the janitor.
     * 
     * @param round Next round number
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder setRoundNumber(final int round) {
        assertRunningMain();
        mainGamePhaseController.getGameFieldController().injectRoundNumber(round);
        return this;
    }

    /**
     * This allows you to summon the janitor in any round
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder summonJanitor() {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();

        if (gfFieldController.inCleanup()) {
            throw new BackstoryException("The janitor is already on the field: " + getCharacter("<janitor>"));
        }
        mainGamePhaseController.getGameFieldController().summonJanitor();
        return this;
    }

    /**
     * This will give a gadget to a given character -- this works even if the gadget
     * is owned by another character - in this case it will get cloned.Will not be
     * added again if the character already has the gadget.
     * 
     * @param characterName Name of the character to modify
     * @param gadget        gadget he shall get
     * @return This builder to allow chaining
     */
    public BackstoryBuilder giftGadgetToCharacter(final String characterName, final GadgetEnum gadget) {
        return giftGadgetToCharacter(characterName, Gadget.constructGadget(gadget));
    }

    /**
     * This will give a gadget to a given character -- this works even if the gadget
     * is owned by another character - in this case it will get cloned. Will not be
     * added again if the character already has the gadget.
     * 
     * @param characterName Name of the character to modify
     * @param gadget        Gadget he shall get
     * @return This builder to allow chaining
     */
    public BackstoryBuilder giftGadgetToCharacter(final String characterName, final Gadget gadget) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.addGadget(gadget);
        return this;
    }

    /**
     * This will change the usages of a gadget if the character holds it. Otherwise
     * it fails.
     * 
     * @param characterName Name of the character to modify
     * @param gadget        Gadget to modify
     * @param usages        Number of usages to update
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateUsagesOfGadgetFor(final String characterName, final GadgetEnum gadget,
            final int usages) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        final Optional<Gadget> mayGadget = character.getGadgetType(gadget);
        if (!mayGadget.isPresent()) {
            throw new BackstoryException(
                    "You wanted to update the usages of character: " + characterName + " for the gadget: " + gadget
                            + " with: " + usages + ". But: " + character + " does not have this gadget.");
        }
        mayGadget.get().setUsages(usages);
        return this;
    }

    /**
     * This will update a gadget -- if the character does not has this gadget he
     * will get it.
     * 
     * @param characterName Name of the character to modify
     * @param gadget        The gadget to use as update
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateGadget(final String characterName, final Gadget gadget) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        final Optional<Gadget> mayGadget = character.getGadgetType(gadget.getGadget());
        if (mayGadget.isPresent()) {
            // remove it!
            character.removeGadget(mayGadget.get());
        }
        character.addGadget(gadget);
        return this;
    }

    /**
     * This will remove a gadget -- if the character does not has this gadget this
     * will cause no error as it is "removed" as well.
     * 
     * @param characterName Name of the character to modify
     * @param gadget        The gadget to remove
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder removeGadget(final String characterName, final GadgetEnum gadget) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        final Optional<Gadget> mayGadget = character.getGadgetType(gadget);
        if (mayGadget.isPresent()) {
            // remove it!
            character.removeGadget(mayGadget.get());
        }
        return this;
    }

    /**
     * This will change the position of a character - you <i>can</i> supply invalid
     * fields, but what purpose would that serve(r :P)?
     * <p>
     * You can access the cat via {@code "<cat>"} and the janitor with
     * {@code "<janitor>"} (if he is already summoned, this fails otherwise).
     * 
     * @param characterName Name of the character to modify
     * @param newPosition   The new position
     * 
     * @return This builder to allow chaining
     * 
     * @see #moveCharacter(String, Point)
     */
    public BackstoryBuilder placeCharacter(final String characterName, final Point newPosition) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setCoordinates(newPosition);
        return this;
    }

    /**
     * You may not be able to prevent the spawn-algorithm from using fields without
     * changing the algorithms' awareness-routine (or injecting a sequence which
     * would break further spawns). Yet you are able to move characters if they stay
     * on a field you want to be free.
     * <p>
     * This routine will check if there is a character on the given point, and if so
     * move it to the invalide position (-1,-1) so it does not interfere at any
     * cost. You may re-{@link #placeCharacter(String, Point) place} the character
     * but i don't think it would be necessary for most cases as you do not care
     * about it.
     * 
     * @param point The point you want to check for a character
     * 
     * @return This builder to allow chaining
     * 
     * @see #assureFieldFree(Point, Point)
     */
    public BackstoryBuilder assureFieldFree(final Point point) {
        return assureFieldFree(point, new Point(-1, -1));
    }

    /**
     * You may not be able to prevent the spawn-algorithm from using fields without
     * changing the algorithms' awareness-routine (or injecting a sequence which
     * would break further spawns). Yet you are able to move characters if they stay
     * on a field you want to be free.
     * <p>
     * This routine will check if there is a character on the given point, and if so
     * move it to the given position so it does not interfere in any way. You may
     * re-{@link #placeCharacter(String, Point) place} the character but i don't
     * think it would be necessary for most cases as you do not care about it.
     * 
     * @param point    The point you want to check for a character
     * @param dumpSpot The spot to move the character to if it interferes
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder assureFieldFree(final Point point, final Point dumpSpot) {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        final Optional<Character> mayCharacter = gfFieldController.decodeCharacterByPosition(point);
        if (mayCharacter.isPresent()) {
            // reposition
            mayCharacter.get().setCoordinates(dumpSpot);
        }
        return this;
    }

    /**
     * You may not be able to prevent the spawn-algorithm from using fields without
     * changing the algorithms' awareness-routine (or injecting a sequence which
     * would break further spawns). Yet you are able to move characters if they stay
     * on a field you want to be free.
     * <p>
     * This routine will check if there is a character on the given points, and if
     * so move it to the invalide position (-1,-1) so it does not interfere at any
     * cost. You may re-{@link #placeCharacter(String, Point) place} the character
     * but i don't think it would be necessary for most cases as you do not care
     * about it.
     * 
     * @param points The points you want to assure to be free
     * 
     * @return This builder to allow chaining
     * 
     * @see #assureFieldFree(Point, Point)
     */
    public BackstoryBuilder assureFieldsFree(final Point... points) {
        for (int i = 0; i < points.length; i++) {
            assureFieldFree(points[i], new Point(-1, -1));
        }
        return this;
    }

    /**
     * You may not be able to prevent the spawn-algorithm from using fields without
     * changing the algorithms' awareness-routine (or injecting a sequence which
     * would break further spawns). Yet you are able to move characters if they stay
     * on a field you want to be free.
     * <p>
     * This routine will check if there is a character on the given points, and if
     * so move it to the invalide position (-1,-1) so it does not interfere at any
     * cost. You may re-{@link #placeCharacter(String, Point) place} the character
     * but i don't think it would be necessary for most cases as you do not care
     * about it.
     * 
     * @param points The points you want to assure to be free
     * 
     * @return This builder to allow chaining
     * 
     * @see #assureFieldFree(Point, Point)
     */
    public BackstoryBuilder assureFieldsFree(final Collection<Point> points) {
        for (Point point : points) {
            assureFieldFree(point, new Point(-1, -1));
        }
        return this;
    }

    /**
     * This will change the position of a character based on its current one - you
     * <i>can</i> create invalid fields, but what purpose would that serve(r :P)?
     * This will use the {@link Point#move(Point)}-method.
     * <p>
     * You can access the cat via {@code "<cat>"} and the janitor with
     * {@code "<janitor>"} (if he is already summoned, this fails otherwise).
     * 
     * @param characterName Name of the character to modify
     * @param shiftVector   The vector to be used to shift the current position (of
     *                      the character) by.
     * 
     * @return This builder to allow chaining
     * 
     * @see #placeCharacter(String, Point)
     */
    public BackstoryBuilder moveCharacter(final String characterName, final Point shiftVector) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setCoordinates(character.getCoordinates().move(shiftVector));
        return this;
    }

    /**
     * This will swap the positions of two characters. Could be the same characters
     * (but why).
     * <p>
     * You can access the cat via {@code "<cat>"} and the janitor with
     * {@code "<janitor>"} (if he is already summoned, this fails otherwise).
     * 
     * @param characterOneName Name of the first character to use for swap
     * @param characterTwoName Name of the second character to use for swap
     * 
     * @return This builder to allow chaining
     * 
     * @see #placeCharacter(String, Point)
     */
    public BackstoryBuilder swapCharacters(final String characterOneName, final String characterTwoName) {
        assertRunningMain();
        final Character characterOne = getCharacter(characterOneName);
        final Character characterTwo = getCharacter(characterTwoName);
        final Point characterOneCoordinates = new Point(characterOne.getCoordinates());
        characterOne.setCoordinates(characterTwo.getCoordinates());
        characterTwo.setCoordinates(characterOneCoordinates);
        return this;
    }

    /**
     * This will move the the ownership from its current faction to the faction of
     * player one. If the character is already a member of player ones' faction -
     * this will change nothing.
     * <p>
     * Please note, that if the ownership is changed whilst this character is active
     * there will be no provider-change so you might have to invalidate the turn for
     * this character.
     * <p>
     * You can <i>not</i> access the cat or the janitor as they are not to be
     * controlled by any faction.
     * 
     * @param characterName The character to change the faction from
     * 
     * @return This builder to allow chaining
     * 
     * @see #transferOwnershipToPlayerTwo(String)
     * @see #transferOwnershipToNeutral(String)
     */
    public BackstoryBuilder transferOwnershipToPlayerOne(final String characterName) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        // identify the current ownership of the character
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        final Faction p2Faction = gfFieldController.getPlayerTwoFaction();
        final Faction ntrFaction = gfFieldController.getNeutralFaction();
        // jay - i could check on remove directly, but this seems to make more sense
        // from a semantical perspective
        if (p2Faction.contains(character)) {
            p2Faction.remove(character);
            gfFieldController.getPlayerOneFaction().add(character);
        } else if (ntrFaction.contains(character)) {
            ntrFaction.remove(character);
            gfFieldController.getPlayerOneFaction().add(character);
        }
        return this;
    }

    /**
     * This will move the the ownership from its current faction to the faction of
     * player two. If the character is already a member of player twos' faction -
     * this will change nothing.
     * <p>
     * Please note, that if the ownership is changed whilst this character is active
     * there will be no provider-change so you might have to invalidate the turn for
     * this character.
     * <p>
     * You can <i>not</i> access the cat or the janitor as they are not to be
     * controlled by any faction.
     * 
     * @param characterName The character to change the faction from
     * 
     * @return This builder to allow chaining
     * 
     * @see #transferOwnershipToPlayerOne(String)
     * @see #transferOwnershipToNeutral(String)
     */
    public BackstoryBuilder transferOwnershipToPlayerTwo(final String characterName) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        // identify the current ownership of the character
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        final Faction p1Faction = gfFieldController.getPlayerOneFaction();
        final Faction ntrFaction = gfFieldController.getNeutralFaction();
        if (p1Faction.contains(character)) {
            p1Faction.remove(character);
            gfFieldController.getPlayerTwoFaction().add(character);
        } else if (ntrFaction.contains(character)) {
            ntrFaction.remove(character);
            gfFieldController.getPlayerTwoFaction().add(character);
        }
        return this;
    }

    /**
     * This will move the the ownership from its current faction to the neutral
     * faction. If the character is already a member of the neutral faction - this
     * will change nothing.
     * <p>
     * Please note, that if the ownership is changed whilst this character is active
     * there will be no provider-change so you might have to invalidate the turn for
     * this character.
     * <p>
     * You can <i>not</i> access the cat or the janitor as they are not to be
     * controlled by any faction.
     * 
     * @param characterName The character to change the faction from
     * 
     * @return This builder to allow chaining
     * 
     * @see #transferOwnershipToPlayerOne(String)
     * @see #transferOwnershipToPlayerTwo(String)
     */
    public BackstoryBuilder transferOwnershipToNeutral(final String characterName) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        // identify the current ownership of the character
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        final Faction p1Faction = gfFieldController.getPlayerOneFaction();
        final Faction p2Faction = gfFieldController.getPlayerTwoFaction();
        if (p1Faction.contains(character)) {
            p1Faction.remove(character);
            gfFieldController.getNeutralFaction().add(character);
        } else if (p2Faction.contains(character)) {
            p2Faction.remove(character);
            gfFieldController.getNeutralFaction().add(character);
        }
        return this;
    }

    /**
     * This will make the character end its turn - forcefully. There will be no
     * extra retire-status or whatever, it will be just the turn of the next
     * character. If this character was not active an exception will be thrown.
     * <p>
     * You can access the cat via {@code "<cat>"} and the janitor with
     * {@code "<janitor>"} (if he is already summoned, this fails otherwise).
     * 
     * @param characterName Name of the character to invalidate the turn for.
     * 
     * @return This builder to allow chaining
     * 
     * @see #invalidateTurn()
     */
    public BackstoryBuilder invalidateTurnFor(final String characterName) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        if (!Objects.equals(gfFieldController.getActiveCharacter(), character)) {
            throw new BackstoryException("The character you wanted to invalidate the turn for: " + characterName
                    + " but this is not the active character: " + gfFieldController.getActiveCharacter());
        }
        character.retire();
        gfFieldController.performNextTurn();
        return this;
    }

    /**
     * This will make the currently active character end its turn - forcefully.
     * There will be no extra retire-status or whatever, it will be just the turn of
     * the next character.
     * <p>
     * You can access the cat via {@code "<cat>"} and the janitor with
     * {@code "<janitor>"} (if he is already summoned, this fails otherwise).
     * 
     * @return This builder to allow chaining
     * 
     * @see #invalidateTurnFor(String)
     */
    public BackstoryBuilder invalidateTurn() {
        assertRunningMain();
        final Character currentlyActive = mainGamePhaseController.getGameFieldController().getActiveCharacter();
        if (Objects.isNull(currentlyActive)) {
            mainGamePhaseController.getGameFieldController().performNextTurn();
            return this;
        } else {
            return invalidateTurnFor(currentlyActive.getName());
        }
    }

    /**
     * This will change a field on the scenario - this will update the safe,
     * bar-table,... lists by the {@link GameFieldController} and invalidate the
     * pathfinder if it changes the fields being walkable.
     * 
     * @param position The position of the field you want to change, if it is not on
     *                 the map, this will cause an exception to be thrown.
     * @param newField the field that should be set there.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder changeField(final Point position, final Field newField) {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        if (!position.isOnField(gfFieldController.getMap())) {
            throw new BackstoryException("The field you wanted to change: " + position + " to: " + newField
                    + " cannot be changed, as it is not on the field: " + gfFieldController.getMap());
        }
        // Get the field:
        final Field old = gfFieldController.getMap().getSpecificField(position);
        // may remove the field dependent on the type?
        gfFieldController.mayRemoveListData(position, old.getState());
        // update the field:
        gfFieldController.getMap().setSpecificField(position, newField);
        gfFieldController.mayAddListData(position, newField.getState());
        if (old.isWalkable() != newField.isWalkable()) {
            gfFieldController.invalidatePathfinder();
        }
        return this;
    }

    /**
     * This will enforce the end of the game via
     * {@link MainGamePhaseController#processGameOver()}. Messags will be sent!
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder gameOver() {
        assertRunningMain();
        mainGamePhaseController.processGameOver();
        return this;
    }

    /**
     * This will update the hp of the character - could be used for cat and janitor
     * (in theory), but as they do not care about that it would be useless. The
     * {@link GameFieldStatisticsProvider} will not record this.
     * 
     * @param characterName Name of the character to update the hp for.
     * @param newHp         The new hp, if they would kill the charakter - this will
     *                      not kill him so you might not use this call for that.
     *                      The value will get clamped by
     *                      {@link Character#setHp(int)}.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateHpFor(final String characterName, final int newHp) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setHp(newHp);
        return this;
    }

    /**
     * This will update the mp of the character - could be used for cat and janitor
     * (in theory), but as they do not care about that it would be useless.
     * 
     * @param characterName Name of the character to update the mp for.
     * @param newMp         The new mp.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateMpFor(final String characterName, final int newMp) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setMp(newMp);
        return this;
    }

    /**
     * This will update the ap of the character - could be used for cat and janitor
     * (in theory), but as they do not care about that it would be useless.
     * 
     * @param characterName Name of the character to update the ap for.
     * @param newAp         The new ap.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateApFor(final String characterName, final int newAp) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setAp(newAp);
        return this;
    }

    /**
     * This will update the chips of the character - could be used for cat and
     * janitor (in theory), but as they do not care about that it would be useless.
     * 
     * @param characterName Name of the character to update the chips for.
     * @param newChips      The new chips.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateChipsFor(final String characterName, final int newChips) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setChips(newChips);
        return this;
    }

    /**
     * This will update the ip of the character - could be used for cat and janitor
     * (in theory), but as they do not care about that it would be useless. The
     * {@link GameFieldStatisticsProvider} will not record this, nor will this
     * trigger any gadgets active on ip-gain.
     * 
     * @param characterName Name of the character to update the ip for.
     * @param newIp         The new ip.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder updateIpFor(final String characterName, final int newIp) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.setIp(newIp);
        return this;
    }

    /**
     * This will add the property to the given character -- will not fail if the
     * character does already possess this property or the property would be
     * (semantically) impossible.
     * 
     * @param characterName Name of the character to update the properties for.
     * @param property      The property to add
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder addPropertyFor(final String characterName, PropertyEnum property) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.addProperty(property);
        return this;
    }

    /**
     * This will add all the property to the given character -- will not fail if the
     * character does already possess this property or the property would be
     * (semantically) impossible.
     * 
     * @param characterName Name of the character to update the properties for.
     * @param properties    The properties to add
     * 
     * @return This builder to allow chaining
     * 
     * @see #addPropertyFor(String, PropertyEnum)
     */
    public BackstoryBuilder addPropertiesFor(final String characterName, PropertyEnum... properties) {
        for (PropertyEnum property : properties) {
            addPropertyFor(characterName, property);
        }
        return this;
    }

    /**
     * This will remove the property from the given character -- will not fail if
     * the character does not possess this property or the property is not
     * technically removable (e.g. constant properties, they will be removed as
     * well).
     * 
     * @param characterName Name of the character to update the property for.
     * @param property      The property to remove
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder removePropertyFor(final String characterName, PropertyEnum property) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        character.removeProperty(property);
        return this;
    }

    /**
     * This will remove all the properties from the given character -- will not fail
     * if the character does not possess any of the properties or the property are
     * not technically removable (e.g. constant properties, they will be removed as
     * well).
     * 
     * @param characterName Name of the character to update the property for.
     * @param properties    The properties to remove
     * 
     * @return This builder to allow chaining
     * 
     * @see #removePropertyFor(String, PropertyEnum)
     */
    public BackstoryBuilder removePropertiesFor(final String characterName, PropertyEnum... properties) {
        for (PropertyEnum property : properties) {
            removePropertyFor(characterName, property);
        }
        return this;
    }

    /**
     * This will remove all properties from the given character -- will not fail if
     * the character does not possess any property (or it is not removable).
     * 
     * @param characterName Name of the character to update the property for.
     * 
     * @return This builder to allow chaining
     * 
     * @see #removePropertiesFor(String, PropertyEnum...)
     */
    public BackstoryBuilder removeAllPropertiesFor(final String characterName) {
        return removePropertiesFor(characterName, PropertyEnum.values());
    }

    /**
     * This will update the matchconfig by using Reflection -- this means you can do
     * <i>everything</i> (wrong). Only use valid fields and mind what you do :P
     * <p>
     * There are some fields which might have no effect (anymore) after the server
     * was started.
     * 
     * @param key   Field that should be changed
     * @param value The value you want to set it to
     * 
     * @return This builder to allow chaining
     */
    @SuppressWarnings("java:S3011")
    public BackstoryBuilder matchconfigUpdate(final String key, final Object value) {
        assertRunning();
        final Matchconfig matchconfig = this.configuration.getMatchconfig();
        try {
            java.lang.reflect.Field field = Matchconfig.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(matchconfig, value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new BackstoryException(
                    "Error when setting matchconfig: " + ex.getMessage() + " (" + ex.getClass() + ")");
        }
        return this;
    }

    /**
     * This will update the server config by using Reflection -- this means you can
     * do <i>everything</i> (wrong). Only use valid fields and mind what you do :P
     * <p>
     * There are some fields which might have no effect (anymore) after the server
     * was started.
     * 
     * @param key   Field that should be changed
     * @param value The value you want to set it to
     * 
     * @return This builder to allow chaining
     */
    @SuppressWarnings("java:S3011")
    public BackstoryBuilder serverConfigUpdate(final String key, final Object value) {
        assertRunning();
        try {
            java.lang.reflect.Field field = ServerConfiguration.class.getDeclaredField(key);
            field.setAccessible(true);
            field.set(serverConfiguration, value);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new BackstoryException(
                    "Error when setting server-configuration: " + ex.getMessage() + " (" + ex.getClass() + ")");
        }
        return this;
    }

    /**
     * This will enforce the {@link GameFieldController} to start the next Round -
     * no matter the current round order.
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder enforceNextRound() {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();

        gfFieldController.nextRound();

        return this;
    }

    /**
     * Use this to set the currently active character - this may break the routines
     * if another character will do something in the moment. This furthermore should
     * only be used for players - you cannot use this for npc(s). Use
     * {@link #injectNextRoundOrder(String...)} for this.
     * 
     * @param playerName    Player that shall take over (has to be player one or
     *                      player two)
     * @param characterName The Character that shall be active now (this will not
     *                      give him any mp/ap nor will it clear his retired-status,
     *                      you may use
     *                      {@link #enforceCurrentlyActive(String, String, boolean)}
     *                      for that.)
     * 
     * @return This builder to allow chaining
     * 
     * @see #enforceCurrentlyActive(String, String, boolean)
     */
    public BackstoryBuilder enforceCurrentlyActive(String playerName, String characterName) {
        return enforceCurrentlyActive(playerName, characterName, false);
    }

    /**
     * Use this to set the currently active character - this may break the routines
     * if another character will do something in the moment. This furthermore should
     * only be used for players - you cannot use this for npc(s). Use
     * {@link #injectNextRoundOrder(String...)} for this.
     * 
     * @param playerName    Player that shall take over (has to be player one or
     *                      player two)
     * @param characterName The character that shall be active now
     * @param reset         Shall the character be cleared of the retired-state and
     *                      gain its ap/mp?
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder enforceCurrentlyActive(String playerName, String characterName, boolean reset) {
        assertRunningMain();
        final Character character = getCharacter(characterName);
        final DummyClient client = assertIsPlayer(playerName);
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        gfFieldController.setActiveCharacter(character);
        gfFieldController.setActivePlayer(true);
        mainGamePhaseController.setPlayerWaiting(client.getClientId());
        if (reset) {
            character.resetMpAp();
        }
        return this;
    }

    /**
     * This will cause the npc provider to perform the operations for the given NPC
     * character. It may work with non-npc characters too, but this is up to the
     * {@link de.uulm.team020.server.game.phases.main.helper.NpcMovementProvider
     * NpcMovementProvider}. This will reset the ap and mp + clear the retired
     * status.
     * 
     * @param npcCharacterName Character that shall be moved
     * 
     * @return This builder to allow chaining
     * 
     * @see #npcAction(String, boolean)
     */
    public BackstoryBuilder npcAction(String npcCharacterName) {
        return npcAction(npcCharacterName, true);
    }

    /**
     * This will cause the npc provider to perform the operations for all of the
     * given NPC character. It may work with non-npc characters too, but this is up
     * to the
     * {@link de.uulm.team020.server.game.phases.main.helper.NpcMovementProvider
     * NpcMovementProvider}. This will reset the ap and mp + clear the retired
     * status so they will make their move
     * 
     * @param npcCharacterName Character that shall be moved
     * 
     * @return This builder to allow chaining
     * 
     * @see #npcAction(String)
     * @see #npcAction(String, boolean)
     */
    public BackstoryBuilder npcAction(String... npcCharacterName) {
        for (String npcName : npcCharacterName) {
            npcAction(npcName);
        }
        return this;
    }

    /**
     * This will cause the npc provider to perform the operations for all of the
     * given NPC character. It may work with non-npc characters too, but this is up
     * to the
     * {@link de.uulm.team020.server.game.phases.main.helper.NpcMovementProvider
     * NpcMovementProvider}.
     * 
     * @param reset            True, if the characters status (ap, mp, ...) shall be
     *                         reset before or not.
     * @param npcCharacterName Character that shall be moved
     * 
     * @return This builder to allow chaining
     * 
     * @see #npcAction(String)
     * @see #npcAction(String, boolean)
     */
    public BackstoryBuilder npcAction(boolean reset, String... npcCharacterName) {
        for (String npcName : npcCharacterName) {
            npcAction(npcName, reset);
        }
        return this;
    }

    /**
     * This will cause the npc provider to perform the operations for the given NPC
     * character. It may work with non-npc characters too, but this is up to the
     * {@link de.uulm.team020.server.game.phases.main.helper.NpcMovementProvider
     * NpcMovementProvider}.
     * 
     * @param npcCharacterName Character that shall be moved
     * @param reset            True, if the characters status (ap, mp, ...) shall be
     *                         reset before or not.
     * 
     * @return This builder to allow chaining
     * 
     * @see #npcAction(String)
     */
    public BackstoryBuilder npcAction(String npcCharacterName, boolean reset) {
        assertRunningMain();
        final Character character = getCharacter(npcCharacterName);
        if (reset) {
            character.resetMpAp();
        }
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        gfFieldController.npcAction(character);
        return this;
    }

    /**
     * This will cause the cat to make a move, no matter the current round order.
     * 
     * @return This builder to allow chaining
     * 
     * @see #catAction(int)
     */
    public BackstoryBuilder catAction() {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        gfFieldController.getActionController().catAction();
        return this;
    }

    /**
     * This will cause the cat to make multiple moves, no matter the current round
     * order.
     * 
     * @param n How many times should the cat make its' move? Has to be &gt;= 1
     * 
     * @return This builder to allow chaining
     * 
     * @see #catAction()
     */
    public BackstoryBuilder catAction(final int n) {
        if (n <= 0) {
            throw new BackstoryException(
                    "You cannot make: " + n + " consecutive cat-actions as the number has to be >= 1.");
        }
        for (int i = 0; i < n; i++) {
            catAction();
        }
        return this;
    }

    /**
     * This will cause the janitor to make a move, no matter the current round
     * order. If the janitor was not summoned, this will fail!
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder janitorAction() {
        assertRunningMain();
        final GameFieldController gfFieldController = mainGamePhaseController.getGameFieldController();
        if (!gfFieldController.getJanitor().getCoordinates().isOnField(gfFieldController.getMap())) {
            throw new BackstoryException(
                    "The janitor can not make a move as it was not summoned: " + gfFieldController.getJanitor());
        }
        gfFieldController.getActionController().janitorAction();
        return this;
    }

    /**
     * This will cause the janitor to make multiple a move, no matter the current
     * round order. If the janitor was not summoned, this will fail (once)! It
     * basically performs {@link #janitorAction()} 'n'-times
     * 
     * @param n How many times should the janitor make its' move? Has to be &gt;= 1
     * 
     * @return This builder to allow chaining
     */
    public BackstoryBuilder janitorAction(final int n) {
        if (n <= 0) {
            throw new BackstoryException(
                    "You cannot make: " + n + " consecutive janitor-actions as the number has to be >= 1.");
        }
        for (int i = 0; i < n; i++) {
            janitorAction();
        }
        return this;
    }
}