package de.uulm.team020.server.game.phases.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Cocktail;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.WiretapWithEarplugs;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Pair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.helper.pathfinding.Pathfinder;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.RequestGameOperationMessage;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ConfigurationException;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.main.helper.GameFieldActionController;
import de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner;
import de.uulm.team020.server.game.phases.main.islands.Island;
import de.uulm.team020.server.game.phases.main.islands.IslandClassifier;
import de.uulm.team020.server.game.phases.main.islands.IslandSpawner;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;

/**
 * This class handles all actions &amp; initialization-actions for the main
 * Game-Board.
 *
 * @author Florian Sihler
 * @version 1.0b, 06/08/2020
 */
public class GameFieldController {

    /** Name of the cat */
    public static final String CAT_NAME = "<cat>";
    /** Name of the janitor */
    public static final String JANITOR_NAME = "<janitor>";

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");

    private static final String ROUND_TXT = "Round";

    private Queue<List<String>> currentRoundOrderInjects;

    private final FieldMap map;
    private int currentRound;
    private List<Character> currentRoundOrder;
    private Character currentlyActiveCharacter;
    private Boolean currentlyActivePlayer;
    private boolean isGameOver = false;

    private boolean inCleanup;

    private List<Point> safePositions;
    private List<Point> barTablePositions;
    private List<Point> barSeatPositions;
    private List<Point> fireplacePositions;

    private Character cat;
    private Character janitor;

    private Set<Character> characters;

    private Faction playerOneFaction;
    private Faction playerTwoFaction;
    private Faction neutralFaction;

    // just in case the client assumes order we will keep with the set mode
    private Set<Integer> playerOneSafeCombinations;
    private Set<Integer> playerTwoSafeCombinations;
    private Set<Integer> neutralSafeCombinations;

    private Pathfinder<Field> pathfinder;

    private final Configuration configuration;

    private final GameFieldPositioner positioner;
    private final GameFieldActionController actionController;
    private GameFieldStatisticsProvider statisticsProvider;
    // Will perform character-movements and actions
    private CharacterActionProcessor processor;

    private IslandClassifier classifier;
    private Island[] islands;

    private final StoryAuthor author;
    private final RandomController randomController;

    private Consumer<List<BaseOperation>> gameStatusPublisher;

    // ========================================================================
    // Constructor
    // ========================================================================

    /**
     * Initialize a new GameFieldController. It will take the Scenario, available
     * characters,... from the supplied Configuration
     *
     * @param configuration      The configuration to use
     * @param playerOneEquipment Equipment of the first player as set by the
     *                           drafting-controller
     * @param playerTwoEquipment of the second player as set by the
     *                           drafting-controller
     * @param author             The author to use on the travel
     * @param characterPool      (remaining) possible NPC
     * @param gadgetPool         (remaining) possible Gadgets
     * @param randomController   The controller to be consulted in case of random
     *                           values
     *
     * @see #GameFieldController(Configuration, DraftingEquipment,
     *      DraftingEquipment, StoryAuthor, RandomController, List, List, int[],
     *      List, Map)
     */
    public GameFieldController(final Configuration configuration, final DraftingEquipment playerOneEquipment,
            final DraftingEquipment playerTwoEquipment, final StoryAuthor author, final List<UUID> characterPool,
            final List<GadgetEnum> gadgetPool, final RandomController randomController) {
        this(configuration, playerOneEquipment, playerTwoEquipment, author, randomController, characterPool, gadgetPool,
                null, null, null);
    }

    /**
     * Initialize a new GameFieldController. It will take the Scenario, available
     * characters,... from the supplied Configuration
     *
     * @param configuration       The configuration to use
     * @param playerOneEquipment  Equipment of the first player as set by the
     *                            drafting-controller
     * @param playerTwoEquipment  of the second player as set by the
     *                            drafting-controller
     * @param author              The author to use on the travel
     * @param characterPool       (remaining) possible NPC
     * @param gadgetPool          (remaining) possible Gadgets
     * @param safeInjectionOrder  Safe inject to use - null for none, is the default
     * @param npcPicks            The NPC force to choose, if they are not there
     *                            anymore, this will issue an exception, otherwise
     *                            they will not be validated for any constraint.
     *                            This shall be null if non desired, which is the
     *                            default.
     * @param characterPlacements Placement-Information for the generated characters
     *                            - this needs to have name-information about the
     *                            characters, leave null if not desired (which is
     *                            the default)
     */
    @SuppressWarnings("java:S107")
    public GameFieldController(final Configuration configuration, final DraftingEquipment playerOneEquipment,
            final DraftingEquipment playerTwoEquipment, final StoryAuthor author,
            final RandomController randomController, final List<UUID> characterPool, final List<GadgetEnum> gadgetPool,
            final int[] safeInjectionOrder, final List<Pair<UUID, List<GadgetEnum>>> npcPicks,
            final Map<String, Point> characterPlacements) {
        this.configuration = configuration;
        this.author = author;
        this.randomController = randomController;
        init();
        // Populate the field map
        this.map = new FieldMap(populateFieldMap(configuration.getScenario(), this.safePositions,
                this.barTablePositions, this.barSeatPositions, this.fireplacePositions, safeInjectionOrder,
                configuration.getMatchconfig(), this.author, this.randomController));
        populateCharacters(playerOneEquipment, playerTwoEquipment);
        initNPC(characterPool, gadgetPool, npcPicks);
        author.setNpcSetup(neutralFaction);
        magpie.writeInfo("Faction-Dump: " + dumpFactions(), "init");
        // instruct the slaves
        this.positioner = new GameFieldPositioner(this, configuration, characters, safePositions, playerOneFaction,
                playerTwoFaction, neutralFaction);
        this.actionController = new GameFieldActionController(this, characters, playerOneFaction, playerTwoFaction);
        // position characters
        this.positioner.positionCharacters(characterPlacements);
        // proceed with initialization
        postInit();
    }

    private void init() {
        this.safePositions = new LinkedList<>();
        this.barTablePositions = new LinkedList<>();
        this.barSeatPositions = new LinkedList<>();
        this.fireplacePositions = new LinkedList<>();
        this.currentRoundOrderInjects = new LinkedList<>();
        this.characters = new HashSet<>();
        this.playerOneFaction = new Faction("PlayerOne");
        this.playerTwoFaction = new Faction("PlayerTwo");
        this.neutralFaction = new Faction("Neutral");
        this.cat = new Character(UUID.randomUUID(), CAT_NAME, new Point(-1, -1), -2, -2, -2, -2, -2,
                Collections.emptyList(), Collections.emptyList());

        this.janitor = new Character(UUID.randomUUID(), JANITOR_NAME, new Point(-1, -1), -1, -1, -1, -1, -1,
                Collections.emptyList(), Collections.emptyList());
        this.statisticsProvider = new GameFieldStatisticsProvider();
    }

    private void postInit() {
        author.setGameFieldSetup(characters);
        this.processor = new CharacterActionProcessor(this);
        final int numOfSafes = safePositions.size();
        playerOneSafeCombinations = new HashSet<>(numOfSafes);
        playerTwoSafeCombinations = new HashSet<>(numOfSafes);
        neutralSafeCombinations = new HashSet<>(numOfSafes);
        this.pathfinder = new Pathfinder<>(map, true);
        currentRound = 0;
        this.inCleanup = false; // not in cleanup by start
    }

    // ========================================================================
    // Round control
    // ========================================================================

    /**
     * Start next round (init cocktails, ...)
     *
     * @return The new round-number
     */
    public int nextRound() {
        currentRound += 1;
        this.currentlyActiveCharacter = null;
        this.author.nextRound(currentRound);
        magpie.writeInfo("Round No. " + currentRound + " (cleanup-target: "
                + configuration.getMatchconfig().getRoundLimit() + ")", ROUND_TXT);
        // First we will check if the janitor is allowed to enter - if so, he will be
        // spawned!
        mayJanitorEntry();
        // Reset all cocktails
        placeCocktails();
        // Check if the wiretap with earplugs gadget is active, and if: check if it
        // breaks
        checkWiretapWithEarplugs();
        // Update the counter for all foggy-fields if they are not foggy anymore, remove
        // he fog
        checkFoggyFields();
        // if the clear exfiltration server-configuration flag is set this will remove
        // their exfiltration status
        mayClearExfiltrationStatus();
        // Heal all characters on round start
        healOnRoundStart();
        // will remove clammy clothes if neighbour:
        fireplaceOnRoundStart();
        // may be victim to injections
        shuffleRoundOrder();

        // This will be done on start of each turn now:
        if (configuration.getServerConfig().allMpApOnBeginOfTurn()) {
            for (final Character tmpCharacter : this.currentRoundOrder) {
                tmpCharacter.resetMpAp(//
                        () -> randomController.requestFlip(tmpCharacter.getName(), 0.5D,
                                RandomOperation.CHARACTER_MP_AP_GAIN),
                        () -> randomController.requestFlip(tmpCharacter.getName(), 0.5D,
                                RandomOperation.CHARACTER_MP_AP_LOSS));
            }
        }

        return currentRound;
    }

    private void mayJanitorEntry() {
        // validate if it is time for the janitor to enter the game field
        final int roundMaximum = configuration.getMatchconfig().getRoundLimit();
        if (currentRound != roundMaximum) {
            return; // either we are before or already after janitor entry
        } else if (this.janitor.getCoordinates().isOnField(this.map)) {
            magpie.writeError("Janitor was already summoned for: " + this.janitor
                    + " will not re-summon, this might be critical!", ROUND_TXT);
            return;
        }
        // remove all npc
        magpie.writeInfo("In cleanup mode", "Cleanup");
        summonJanitor();
    }

    /**
     * Summon the janitor - no matter the round :P
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to summon the janitor.
     */
    @StoryMethod
    public void summonJanitor() {
        for (final Character character : neutralFaction) {
            magpie.writeDebug("Removing NPC: " + character, "Cleanup");
            characters.remove(character);
        }
        neutralFaction = new Faction("Neutral-Cleaned");

        // summon the mighty one - the godly - the master of masters: the janitor
        final List<Point> possibles = new ArrayList<>(classifier.getValidSpawns());
        characters.add(this.janitor);
        IslandSpawner.spawnJanitor(possibles, this.janitor,
                characters.stream().map(Character::getCoordinates).collect(Collectors.toSet()), randomController);
        inCleanup = true;
        magpie.writeInfo("Spawned Janitor: " + this.janitor.getCoordinates(), ROUND_TXT);
    }

    /**
     * Will clear the exfiltration status is configured.
     * ({@link ServerConfiguration#clearExfiltrationOnNextRound()})
     */
    private void mayClearExfiltrationStatus() {
        if (!configuration.getServerConfig().clearExfiltrationOnNextRound()) {
            return;
        }
        for (Character character : this.characters) {
            if (character.isExfiltrated()) {
                // if requested, clear the exfiltration flag
                character.clearExfiltration();
            }
        }
    }

    private void healOnRoundStart() {
        for (final Point point : barSeatPositions) {
            final Optional<Character> mayCharacter = decodeCharacterByPosition(point);
            if (mayCharacter.isPresent()) { // there is someone on this field
                final Character character = mayCharacter.get();
                if (!character.isExfiltrated()) { // only heal if not exfiltrated
                    character.healMax();
                }
            }
        }
    }

    private void fireplaceOnRoundStart() {
        for (final Character character : characters) {
            if (!character.isExfiltrated()
                    && fireplacePositions.stream().anyMatch(f -> f.isNeighbour(character.getCoordinates()))) {
                // character is neighbour of fireplace!
                character.removeProperty(PropertyEnum.CLAMMY_CLOTHES);
            }
        }
    }

    private void shuffleRoundOrder() {
        if (currentRoundOrderInjects != null && !currentRoundOrderInjects.isEmpty()) {
            final List<String> injectNames = currentRoundOrderInjects.remove();
            if (injectNames.size() != this.characters.size()) {
                throw new ConfigurationException("The round order inject: " + injectNames
                        + " differs in length with the actual characters: " + characters + " there is no injection.");
            }
            magpie.writeWarning("Injecting round order in round: " + currentRound + " with inject: " + injectNames,
                    "Inject");
            this.currentRoundOrder = new ArrayList<>();
            for (final String name : injectNames) {
                final Optional<Character> mayCharacter = decodeCharacterByName(name);
                if (!mayCharacter.isPresent()) {
                    throw new ConfigurationException("The character desired in inject: " + injectNames + " namely: "
                            + name + " is not present in the character-list: " + this.characters);
                }
                currentRoundOrder.add(mayCharacter.get());
            }
        } else {
            this.currentRoundOrder = new ArrayList<>(this.characters);
            // Guarded on injection
            Collections.shuffle(this.currentRoundOrder);

        }
        // report to the story-author
        author.setNextRoundOrder(currentRoundOrder.stream().map(Character::getName).collect(Collectors.toList()));
    }

    /**
     * Collect and return all Gadgets
     *
     * @return List of all gadgets in the game, some may be broken and removed in
     *         the next round
     */
    public List<Gadget> getAllGadgets() {
        final List<Gadget> gadgets = new ArrayList<>(16);
        playerOneFaction.stream().map(Character::getGadgets).forEach(gadgets::addAll);
        playerTwoFaction.stream().map(Character::getGadgets).forEach(gadgets::addAll);
        neutralFaction.stream().map(Character::getGadgets).forEach(gadgets::addAll);
        return gadgets;
    }

    /**
     * Inject and predict round-order, will queue for further rounds
     *
     * @param names The names ot inject for the next round
     */
    public void injectNextRoundOrder(final List<String> names) {
        currentRoundOrderInjects.add(names);
    }

    /**
     * Build the new state-object for a player
     *
     * @param p1 Should it be the state of player one (true) or the state of player
     *           two (false)
     *
     * @return The full state to be used with GameStatus
     */
    public State getState(final boolean p1) {
        return new State(currentRound, map, p1 ? playerOneSafeCombinations : playerTwoSafeCombinations,
                getOnlyPlayerCharacters(), cat.getCoordinates(), janitor.getCoordinates());
    }

    /**
     * Build the new state-object for a spectator
     *
     * @return The full state to be used with GameStatus
     */
    public State getSpectatorState() {
        return new State(currentRound, map, null, getOnlyPlayerCharacters(), cat.getCoordinates(),
                janitor.getCoordinates());
    }

    private Set<Character> getOnlyPlayerCharacters() {
        return characters.stream().filter(c -> !c.equals(this.cat) && !c.equals(this.janitor))
                .collect(Collectors.toSet());
    }

    /**
     * Use this to get a possible colorized version of the map identifying all the
     * islands. The main reason for this is to be used for presentations - it is
     * targeted for terminals which understand the ANSI-Escape-codes, ...
     *
     * @return The formatted string. If classification was not used, this will
     *         return the default string conversion for FieldMap...
     */
    public String getIslandMask() {
        if (this.islands == null)
            return map.toString(); // no islands
        return classifier.prettyPrint(islands, characters, playerOneFaction, playerTwoFaction);
    }

    // Field Data

    /**
     * Will convert a scenario into a field-map whilst assigning incrementing
     * numbers to the safes (in random order).
     *
     * @param scenario    The scenario to be used
     * @param matchconfig The matchconfig to use for field population
     *
     * @return The populated Field
     */
    public static Field[][] populateFieldMap(final Scenario scenario, final Matchconfig matchconfig) {
        return populateFieldMap(scenario, new LinkedList<>(), new LinkedList<>(), new LinkedList<>(),
                new LinkedList<>(), null, matchconfig, null, null);
    }

    /**
     * Will convert a scenario into a field-map whilst assigning incrementing
     * numbers to the safes (in random order). This method <i>uses</i> <b>side
     * effects</b> to populate the supplied List of foundSafePositions with all
     * found coordinates. This prevents other utils from reiterating over and over
     * just to get those safes
     *
     * @param scenario           The scenario to be used
     * @param foundSafePositions <i>Mutable</i> list of safes positions - shall be
     *                           empty, but mustn't - will not be cleared though.
     * @param foundBarTables     <i>Mutable</i> list of barTable positions - shall
     *                           be empty, but mustn't - will not be cleared though.
     * @param foundBarSeats      <i>Mutable</i> list of barSeat positions - shall be
     *                           empty, but mustn't - will not be cleared though.
     * @param foundFireplaces    <i>Mutable</i> list of fireplace positions - shall
     *                           be empty, but mustn't - will not be cleared though.
     * @param matchconfig        The matchconfig to use for field population
     *
     * @return The populated Field
     */
    public static Field[][] populateFieldMap(final Scenario scenario, final List<Point> foundSafePositions,
            final List<Point> foundBarTables, final List<Point> foundBarSeats, final List<Point> foundFireplaces,
            final Matchconfig matchconfig) {
        return populateFieldMap(scenario, foundSafePositions, foundBarTables, foundBarSeats, foundFireplaces, null,
                matchconfig, null, null);
    }

    /**
     * Will convert a scenario into a field-map whilst assigning incrementing
     * numbers to the safes (in random order). This method <i>uses</i> <b>side
     * effects</b> to populate the supplied List of foundSafePositions with all
     * found coordinates. This prevents other utils from reiterating over and over
     * just to get those safes
     *
     * @param scenario           The scenario to be used
     * @param foundSafePositions <i>Mutable</i> list of safes positions - shall be
     *                           empty, but mustn't - will not be cleared though.
     * @param foundBarTables     <i>Mutable</i> list of barTable positions - shall
     *                           be empty, but mustn't - will not be cleared though.
     * @param foundBarSeats      <i>Mutable</i> list of barSeat positions - shall be
     *                           empty, but mustn't - will not be cleared though.
     * @param foundFireplaces    <i>Mutable</i> list of fireplace positions - shall
     *                           be empty, but mustn't - will not be cleared though.
     * @param safePositions      Set null if safe-order should be randomized,
     *                           otherwise this will set the array numbers to this
     *                           set. This will fail if the length of the passed
     *                           array is less than the amount of safes found. There
     *                           will be an error as well if it is too long! There
     *                           will be no check if the the safe-positions are
     *                           distinct, this is not the task of an injection...
     * @param matchconfig        The matchconfig to use for field population
     * @param author             The author to use for writing the tale, if you want
     *                           none, make null.
     * @param randomController   The random controller to use, may be null if none
     *
     * @return The populated Field
     */
    public static Field[][] populateFieldMap(final Scenario scenario, final List<Point> foundSafePositions,
            final List<Point> foundBarTables, final List<Point> foundBarSeats, final List<Point> foundFireplaces,
            final int[] safePositions, final Matchconfig matchconfig, final StoryAuthor author,
            final RandomController randomController) {
        magpie.writeInfo("Init for scenario: " + scenario, "Pop.");
        final FieldStateEnum[][] base = scenario.getField();
        final Field[][] field = new Field[base.length][];
        // Iterate over the scenario and append the fields,
        // whilst populating the FieldMap:
        for (int y = 0; y < base.length; y++) {
            final int xl = base[y].length;
            // Create the line:
            field[y] = new Field[xl];
            for (int x = 0; x < xl; x++) {
                populateSingleField(foundSafePositions, foundBarTables, foundBarSeats, foundFireplaces, matchconfig,
                        randomController, base, field, y, x);
            }
        }

        // to dump the coordinates to the author in the correct order we need to have
        // the collection before the
        // shuffle to ensure order
        final List<Point> oldSafePositions = populateSafeNumbers(safePositions, foundSafePositions, field);

        // construct the author-dump - if we've got an enforcement, we'll reuse it
        if (author != null)
            populateFieldMapAuthorDump(safePositions, oldSafePositions, field, author);
        return field;
    }

    private static void populateSingleField(final List<Point> foundSafePositions, final List<Point> foundBarTables,
            final List<Point> foundBarSeats, final List<Point> foundFireplaces, final Matchconfig matchconfig,
            final RandomController randomController, final FieldStateEnum[][] base, final Field[][] field, int y,
            int x) {
        // Populate the line:
        final FieldStateEnum type = base[y][x];
        if (type == FieldStateEnum.SAFE) {
            // register with 1 for now to generate later => buffer
            field[y][x] = new Field(1);
            foundSafePositions.add(new Point(x, y));
            return;
        }
        // for non safes:
        if (type == FieldStateEnum.BAR_TABLE) {
            foundBarTables.add(new Point(x, y));
        } else if (type == FieldStateEnum.BAR_SEAT) {
            foundBarSeats.add(new Point(x, y));
        } else if (type == FieldStateEnum.FIREPLACE) {
            foundFireplaces.add(new Point(x, y));
        }
        field[y][x] = new Field(base[y][x]);

        // if roulette: assign a random chip count in interval, which is inclusive!
        if (type == FieldStateEnum.ROULETTE_TABLE) {
            if (randomController == null) {
                field[y][x].setChipAmount(
                        RandomHelper.rndInt(matchconfig.getMinChipsRoulette(), matchconfig.getMaxChipsRoulette() + 1));
            } else {
                field[y][x].setChipAmount(
                        randomController.requestRange(RandomController.GLOBAL, matchconfig.getMinChipsRoulette(),
                                matchconfig.getMaxChipsRoulette() + 1, RandomOperation.ROULETTE_INITIAL_CHIPS));
            }
        }
    }

    private static List<Point> populateSafeNumbers(final int[] safePositions, final List<Point> foundSafePositions,
            final Field[][] field) {
        List<Point> oldSafePositions = null;
        if (safePositions == null) {
            oldSafePositions = new ArrayList<>(foundSafePositions);
            // Guarded on injection :)
            Collections.shuffle(foundSafePositions);
            // assign safe numbers randomly on creation
        } else if (safePositions.length != foundSafePositions.size()) {
            throw new ConfigurationException("You passed the safePositions: " + Arrays.toString(safePositions)
                    + " but they are not length-matching for the found safe-Positions: " + foundSafePositions);
        } else {
            magpie.writeWarning("Injected safe order to be: " + Arrays.toString(safePositions) + " for safes: "
                    + foundSafePositions, "Pop.");
        }
        int idx = 1;

        for (final Point point : foundSafePositions) {
            if (safePositions == null) {
                field[point.getY()][point.getX()].setSafeIndex(idx++);
            } else {
                field[point.getY()][point.getX()].setSafeIndex(safePositions[idx - 1]);
                idx += 1;
            }
        }
        return oldSafePositions;
    }

    private static void populateFieldMapAuthorDump(final int[] safePositions, final List<Point> oldSafePositions,
            final Field[][] field, final StoryAuthor author) {
        final int[] safeTarget = safePositions == null ? new int[oldSafePositions.size()] : safePositions;
        if (oldSafePositions != null) {
            final int oSPLen = oldSafePositions.size();
            for (int i = 0; i < oSPLen; i++) {
                // assign the given numbers in order
                final Point point = oldSafePositions.get(i);
                safeTarget[i] = field[point.getY()][point.getX()].getSafeIndex();
            }
        }
        author.setSafeOrder(safeTarget);
    }

    // Character Data, NOT placement
    private void populateCharacters(final DraftingEquipment playerOneEquipment,
            final DraftingEquipment playerTwoEquipment) {
        // Start with player one
        magpie.writeInfo("Populating player one", "Pop.");
        populateCharacters(playerOneEquipment, true);
        // And now player two
        magpie.writeInfo("Populating player two", "Pop.");
        populateCharacters(playerTwoEquipment, false);
    }

    // will _not_ place the characters
    private void populateCharacters(final DraftingEquipment playerEquipment, final boolean p1) {
        // populate for this player
        for (final Entry<UUID, List<GadgetEnum>> entry : playerEquipment.entrySet()) {
            final Character character = populateCharacter(entry);
            characters.add(character);
            if (p1) {
                playerOneFaction.add(character);
            } else {
                playerTwoFaction.add(character);
            }
        }
    }

    private Character populateCharacter(final Entry<UUID, List<GadgetEnum>> entry) {
        final CharacterInformation characterInformation = configuration.getCharacter(entry.getKey());
        final List<Gadget> gadgets = entry.getValue().stream().map(Gadget::constructGadget)
                .collect(Collectors.toCollection(LinkedList::new));
        final Character character = new Character(characterInformation, gadgets);
        if (character.getGadgetType(GadgetEnum.MOLEDIE).isPresent()) {
            character.getMoledie();
        }
        return character;
    }

    // populate the npc
    /**
     * Will populate an npc by picking random gadgets for him. The amount of gadgets
     * will be bound by min, max AND the gadgetPool, if the numbers do not add up
     * together (e.g. more gadgets required than present) this method will issue an
     * {@link IllegalArgumentException}...
     *
     * @param id         Id of the character to populate
     * @param gadgetPool Remaining Gadgets to pick from
     * @param min        The minimum number of Gadgets wanted
     * @param max        The maximum number of Gadgets wanted
     */
    private Character populateNpcCharacter(final UUID id, final List<GadgetEnum> gadgetPool, final int min,
            final int max) {
        // acquire character
        final CharacterInformation characterInformation = configuration.getCharacter(id);
        // pick at least min and at max maximum of possible or maximum of wanted

        final int pickCount = RandomHelper.rndInt(min, Math.min(gadgetPool.size(), max) + 1);

        if (pickCount < 0) {
            throw new IllegalArgumentException("PickCount was: " + pickCount + " and therefore negative => illegal!");
        }

        final List<Gadget> gadgets = new LinkedList<>();
        for (int i = 0; i < pickCount; i++) {
            // pick a Random Gadget:
            final GadgetEnum pick = RandomHelper.rndPick(gadgetPool);
            // remove the picked Gadget
            gadgetPool.remove(pick);
            // Add the Gadget
            gadgets.add(Gadget.constructGadget(pick));
        }
        return new Character(characterInformation, gadgets);
    }

    private void initNPC(final List<UUID> characterPool, final List<GadgetEnum> gadgetPool,
            final List<Pair<UUID, List<GadgetEnum>>> npcPicks) {
        // add the cat:
        characters.add(this.cat);
        if (npcPicks == null) {
            // if there is no pick enforced just do it randomly
            initNPCRandom(characterPool, gadgetPool);
            return;
        }
        // otherwise, iter - check for existence and use decode
        final int maxNpc = configuration.getServerConfig().numberOfNpc();
        if (npcPicks.size() != maxNpc) {
            throw new StoryException("The amount of npc you supplied for enforcement: " + npcPicks
                    + " differs from configured one: " + maxNpc);
        }
        final Iterator<Pair<UUID, List<GadgetEnum>>> it = npcPicks.iterator();

        for (int i = 0; i < maxNpc; i++) {
            injectNextNpcRandom(maxNpc, it, i);
        }

    }

    private void injectNextNpcRandom(final int maxNpc, final Iterator<Pair<UUID, List<GadgetEnum>>> it, int i) {
        if (!it.hasNext())
            throw new ConfigurationException(
                    "The configuration wants " + maxNpc + " Npc, but this fails for num: " + i);
        final Pair<UUID, List<GadgetEnum>> current = it.next();
        final CharacterInformation characterInformation = configuration.getCharacter(current.getKey());
        final List<Gadget> picksDecoded = current.getValue().stream().map(Gadget::constructGadget)
                .collect(Collectors.toList());
        magpie.writeWarning("Injecting NPC-Character: " + characterInformation + " using the gadgets: " + picksDecoded,
                "Pop.");
        // populate
        final Character character = new Character(characterInformation, picksDecoded);
        // Add it to the sets
        characters.add(character);
        neutralFaction.add(character);
    }

    private void initNPCRandom(final List<UUID> characterPool, final List<GadgetEnum> gadgetPool) {
        // shuffle, pick n beginning; Guarded on injection
        Collections.shuffle(characterPool);
        final Iterator<UUID> it = characterPool.iterator();
        final int maxNpc = configuration.getServerConfig().numberOfNpc();
        final int minGadgets = configuration.getServerConfig().gadgetNpcMinimum();
        final int maxGadgets = configuration.getServerConfig().gadgetNpcMaximum();
        for (int i = 0; i < maxNpc; i++) {
            generateNextNPCRandom(characterPool, gadgetPool, it, maxNpc, minGadgets, maxGadgets, i);
        }
    }

    private void generateNextNPCRandom(final List<UUID> characterPool, final List<GadgetEnum> gadgetPool,
            final Iterator<UUID> it, final int maxNpc, final int minGadgets, final int maxGadgets, int i) {
        if (!it.hasNext())
            throw new ConfigurationException("The configuration wants " + maxNpc + " Npc, but this fails for num: " + i
                    + ". Had: characterPool: " + characterPool + " gadget Pool: " + gadgetPool);
        final UUID current = it.next();
        final Character character = populateNpcCharacter(current, gadgetPool, minGadgets, maxGadgets);
        // Add it to the sets
        characters.add(character);
        neutralFaction.add(character);
    }

    /**
     * Just create a dump of the current factions and who is where. Does not use
     * newlines.
     *
     * @return A string containing the dumping-information
     */
    public String dumpFactions() {
        final StringBuilder builder = new StringBuilder();
        builder.append(dumpFaction(playerOneFaction)).append(", ");
        builder.append(dumpFaction(playerTwoFaction)).append(", ");
        builder.append(dumpFaction(neutralFaction));
        return builder.toString();
    }

    private StringBuilder dumpFaction(final Faction factionToDump) {
        final StringBuilder builder = new StringBuilder(factionToDump.getName());
        builder.append(" (members=");
        for (final Character character : factionToDump) {
            builder.append(character.getName()).append(character.getGadgets()).append(" ");
        }
        return builder.append(")");
    }

    // Calculate placement for the characters

    // round data:
    private void placeCocktails() {
        // for all barTables
        for (final Point point : barTablePositions) {
            final Field field = map.getSpecificField(point);
            if (field.getState() != FieldStateEnum.BAR_TABLE) {
                throw new ThisShouldNotHappenException(
                        "Expected: " + point + " to be a BarTable, but is: " + field.getState());
            }

            if (field.getGadget() == null) {
                // place new Cocktail
                field.setGadget(new Cocktail(false));
            }
        }
    }

    /**
     * This will check if any {@link GadgetEnum#WIRETAP_WITH_EARPLUGS} gadget is
     * active and then flip the fail chance . If this results in failing, the
     * gadget's {@link WiretapWithEarplugs#getWorking()} will be set ot false. But
     * the active victim will stay set.
     */
    private void checkWiretapWithEarplugs() {
        final Optional<Gadget> mayWiretap = getAllGadgets().stream()
                .filter(g -> g.getGadget() == GadgetEnum.WIRETAP_WITH_EARPLUGS).findAny();

        if (mayWiretap.isEmpty()) {
            return;
        }

        final Gadget gadget = mayWiretap.get();
        if (!(gadget instanceof WiretapWithEarplugs)) {
            throw new ThisShouldNotHappenException(
                    "Gadget of type WIRETAP_WITH_EARPLUGS cannot be casted to WiretapWithEarplugs because was of "
                            + "type " + gadget.getClass() + ".");
        }
        final WiretapWithEarplugs wiretapWithEarplugs = (WiretapWithEarplugs) gadget;
        if (activeWiretapShouldBreak(wiretapWithEarplugs)) { // break
            magpie.writeDebug("Wiretap with earplugs broke.", ROUND_TXT);
            wiretapWithEarplugs.setWorking(false);
        }
    }

    private boolean activeWiretapShouldBreak(final WiretapWithEarplugs wiretapWithEarplugs) {
        return wiretapWithEarplugs.getWorking() && wiretapWithEarplugs.getActiveOn() != null
                && randomController.requestFlip(RandomController.GLOBAL,
                        configuration.getMatchconfig().getWiretapWithEarplugsFailChance(),
                        RandomOperation.WIRETAP_SHOULD_BREAK);
    }

    /**
     * This will check if any foggy field has no more rounds to be foggy and will
     * remove the foggy state. Otherwise the round counter will be decremented by
     * one.
     */
    private void checkFoggyFields() {
        final Field[][] fields = map.getField();
        for (final Field[] row : fields) {
            checkFoggyFieldsInRow(row);
        }
    }

    private void checkFoggyFieldsInRow(final Field[] row) {
        for (final Field field : row) {
            checkFoggyField(field);
        }
    }

    private void checkFoggyField(final Field field) {
        if (!field.isFoggy()) {
            return;
        }
        if (field.getFoggyRoundsRemaining() <= 0) {
            magpie.writeDebug("Field " + field + " is not foggy anymore.", ROUND_TXT);
            field.setFoggy(false);
        } else {
            field.setFoggyRoundsRemaining(field.getFoggyRoundsRemaining() - 1);
        }
    }

    /**
     * Set the new active character
     * 
     * @param newActive The new active character
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to set the currently active
     *              character.
     */
    @StoryMethod
    public void setActiveCharacter(final Character newActive) {
        this.currentlyActiveCharacter = newActive;
    }

    /**
     * @return Character currently active
     */
    public Character getActiveCharacter() {
        return this.currentlyActiveCharacter;
    }

    /**
     * Set the currently active player
     * 
     * @param value The value to update
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to set the active player.
     */
    @StoryMethod
    public void setActivePlayer(final Boolean value) {
        this.currentlyActivePlayer = value;
    }

    /**
     * @return True if player one is active, false if player two, only valid if
     *         there is a player active at the moment -- null otherwise
     */
    public Boolean getActivePlayer() {
        return this.currentlyActivePlayer;
    }

    /**
     * Perform the next step in a turn - if it is an npc the server will deal for
     * him.
     *
     * @return false if it is to be waited for an player move, true if it is an npc
     */
    public boolean performNextTurn() {
        // all exfiltrated?
        if (!configuration.getServerConfig().clearExfiltrationOnNextRound()
                && this.characters.stream().allMatch(Character::isExfiltrated)) {
            gameOver(Collections.emptyList()); // shouldn't happen theoretically
        }

        // game already ended?
        if (isGameOver) {
            return false;
        }

        roundLoopHelper();

        // find the owner of the character
        if (currentlyActiveCharacter.equals(this.cat)) {
            actionController.catAction();
            return true;
        } else if (currentlyActiveCharacter.equals(this.janitor)) {
            actionController.janitorAction();
            return true;
        }
        // reset MP-AP of character
        if (!configuration.getServerConfig().allMpApOnBeginOfTurn()) {
            currentlyActiveCharacter.resetMpAp(//
                    () -> randomController.requestFlip(currentlyActiveCharacter.getName(), 0.5D,
                            RandomOperation.CHARACTER_MP_AP_GAIN),
                    () -> randomController.requestFlip(currentlyActiveCharacter.getName(), 0.5D,
                            RandomOperation.CHARACTER_MP_AP_LOSS));
        }
        if (configuration.getServerConfig().gameStatusOnTurnStart()) {
            mayPublishStatus(configuration.getServerConfig().sendEmptyOperationListOnStart() ? List.of() : null);
        }

        // may heal on plage-mask
        // Note, that setHp will set the limit to 100 automatically
        // If not liked, you may use ::removeHp with a negative value.
        currentlyActiveCharacter.getGadgetType(GadgetEnum.ANTI_PLAGUE_MASK)
                .ifPresent(ignored -> currentlyActiveCharacter.setHp(currentlyActiveCharacter.getHp() + 10));

        author.comment("Turn for: " + currentlyActiveCharacter);

        if (neutralFaction.contains(currentlyActiveCharacter)) {
            npcAction(currentlyActiveCharacter);
            return true;
        }

        if (playerOneFaction.contains(currentlyActiveCharacter)) {
            currentlyActivePlayer = true;
        } else if (playerTwoFaction.contains(currentlyActiveCharacter)) {
            currentlyActivePlayer = false;
        } else {
            throw new ThisShouldNotHappenException(
                    "Got character: " + currentlyActiveCharacter + " which is neither neutral, p1 nor p2.");
        }

        magpie.writeInfo("Turn for: " + currentlyActiveCharacter, ROUND_TXT);

        return false;
    }

    private void roundLoopHelper() {
        do {
            // next round?
            if (currentRoundOrder.isEmpty()) {
                magpie.writeInfo("Round over as performNextTurn has no-one to do stuff left.", ROUND_TXT);
                nextRound();
            }

            currentlyActiveCharacter = currentRoundOrder.remove(0);
        } while (currentlyActiveCharacter.isExfiltrated() || !this.characters.contains(currentlyActiveCharacter));
        // skip if not in characters anymore or cannot do a move
    }

    public void npcAction(final Character npcCharacter) {
        // Clear the active characters
        this.currentlyActivePlayer = null;

        // check for winner etc...
        // perform npc Movements until done:
        int iteration = 0;
        while (npcCharacter.hasActionsLeft(map)) {
            mayPublishStatus(actionController.npcAction(npcCharacter, iteration++));
        }
    }

    // Field operations

    /**
     * Finds the underlying character by its id to be decoded on message receivable
     *
     * @param characterId The uuid of the character to be decoded
     * @return The character if found (as optional)
     */
    public Optional<Character> decodeCharacterById(final UUID characterId) {
        return characters.stream().filter(c -> Objects.equals(c.getCharacterId(), characterId)).findAny();
    }

    /**
     * Finds the underlying character by its position
     *
     * @param point The point that the character is to be found for
     * @return The character if found (as optional)
     */
    public Optional<Character> decodeCharacterByPosition(final Point point) {
        return characters.stream().filter(c -> Objects.equals(c.getCoordinates(), point)).findAny();
    }

    /**
     * Finds the underlying character by its name
     *
     * @param name The name that the character is to be found for
     * @return The character if found (as optional)
     */
    public Optional<Character> decodeCharacterByName(final String name) {
        return characters.stream().filter(c -> Objects.equals(c.getName(), name)).findAny();
    }

    /**
     * Finds the game role for a player owning a given character.
     *
     * @param character The character that belongs to the player who's role is to be
     *                  found
     * @return The role of the player owning the given {@code character}
     */
    public Optional<GameRoleEnum> decodePlayerGameRoleByCharacter(final Character character) {
        if (playerOneFaction.contains(character)) {
            return Optional.of(GameRoleEnum.PLAYER_ONE);
        } else if (playerTwoFaction.contains(character)) {
            return Optional.of(GameRoleEnum.PLAYER_TWO);
        } else {
            return Optional.empty();
        }
    }

    public void invalidatePathfinder() {
        this.pathfinder.invalidateCache();
    }

    /**
     * Will get you the closest character to the given point combining the methods
     * as the {@link ServerConfiguration#closestCharacterByWalk()}-Setting commands
     * it. It may rely on
     * {@link GameFieldController#getClosestCharacterByWalk(Point, boolean)} and
     * will rely on {@link #getClosestCharacterByEuclidean(Point)} if the first
     * fails or is disabled.
     * 
     * @param seed          The point to start searching from.
     * @param checkStartEnd Responds to the setting in
     *                      {@link Pathfinder#findPath(Point, Point, boolean)} will
     *                      only be used if ServerConfiguration permits the usage of
     *                      the pathfinder.
     * 
     * @return The closest character, if there is one reachable.
     * 
     * @see #getClosestCharacterByWalk(Point, boolean)
     * @see #getClosestCharacterByEuclidean(Point)
     */
    public Optional<Character> getClosestCharacter(final Point seed, final boolean checkStartEnd) {
        // find closest by walk:
        final Optional<Character> mayTarget;
        if (configuration.getServerConfig().closestCharacterByWalk()) {
            mayTarget = getClosestCharacterByWalk(seed, checkStartEnd);
        } else {
            // disable finding
            mayTarget = Optional.empty();
        }
        if (mayTarget.isPresent()) {
            magpie.writeDebug("Using walker to get closest-target: " + seed + " with: " + mayTarget, ROUND_TXT);
            return mayTarget;
        } else {
            // get by euclidean
            final Optional<Character> mayEcTarget = getClosestCharacterByEuclidean(seed);
            magpie.writeDebug("Using euclidean to get closest-target: " + seed + " with: " + mayEcTarget, ROUND_TXT);
            return mayEcTarget;
        }
    }

    /**
     * Will get you the closest character to the given point. This method will use a
     * grounded pathfinder and therefore will find the closest reachable character
     * and the closest one by walking-distance. If there is a character on the given
     * seed, this character <i>will</i> be returned, as long as the field qualifies
     * via the {@code checkStartEnd}-option.
     * <p>
     * For euclidean distance see {@link #getClosestCharacterByEuclidean(Point)}.
     * 
     * @param seed          The point to start searching from.
     * @param checkStartEnd Responds to the setting in
     *                      {@link Pathfinder#findPath(Point, Point, boolean)}
     * 
     * @return The closest character, if there is one reachable.
     * 
     * @see #getClosestCharacterByEuclidean(Point)
     */
    public Optional<Character> getClosestCharacterByWalk(final Point seed, final boolean checkStartEnd) {
        int shortest = -1;
        Character target = null;
        // check distance to every character
        for (final Character character : this.characters) {
            if (getActionProcessor().getGuard().isCatOrJanitor(character)) {
                // shouldn't remove himself or the cat!
                continue;
            }
            // we are spot on
            if (seed.equals(character.getCoordinates())) {
                return Optional.of(character);
            }
            final Path targetPath = pathfinder.findPath(seed, character.getCoordinates(), checkStartEnd);

            if (shortest < 0 || targetPath.size() < shortest) { // update minima
                shortest = targetPath.size();
                target = character;
            }
        }
        return Optional.ofNullable(target);
    }

    /**
     * Will get you the closest character to the given point. This method will use a
     * grounded pathfinder and therefore will find the closest reachable character
     * and the closest one by walking-distance. If there is a character on the given
     * seed, this character <i>will</i> be returned.
     * <p>
     * For walking distance see {@link #getClosestCharacterByWalk(Point, boolean)}.
     * <p>
     * This method by be a victim to rounding errors.
     * 
     * @param seed The point to start searching from.
     * 
     * @return The closest character, if there is one reachable.
     * 
     * @see #getClosestCharacterByWalk(Point, boolean)
     */
    public Optional<Character> getClosestCharacterByEuclidean(final Point seed) {
        double shortest = -1;
        Character target = null;
        // check distance to every character
        for (final Character character : this.characters) {
            if (getActionProcessor().getGuard().isCatOrJanitor(character)) {
                // shouldn't remove himself or the cat!
                continue;
            }
            // Standing right here :D
            if (Objects.equals(seed, character.getCoordinates())) {
                return Optional.of(character);
            }
            final double thisDist = Point.getDistance(seed, character.getCoordinates());
            if (shortest < 0 || thisDist < shortest) { // update minima
                shortest = thisDist;
                target = character;
            }
        }
        return Optional.ofNullable(target);
    }

    /**
     * This gets all possible moledie targets from a given {@code seed} in
     * {@link Matchconfig#getMoledieRange()}. The passed {@code seed} is expected to
     * be valid
     * <p>
     * Note: This is based on (or equals) the implementation of Florian Sihler - it
     * just got moved.
     *
     * @param seed Seed to start searching from
     * @return Valid targets (if existing)
     */
    public Optional<List<Point>> getPossibleMoledieTargets(final Point seed) {
        final int range = configuration.getMatchconfig().getMoledieRange();

        // may check if range is <= 0 ?
        // we will acquire all points in the given radius:
        List<Point> possible;
        // the radius might be calculated according to the server-config setting:
        if (configuration.getServerConfig().matchconfigDistanceEuclidean()) {
            possible = seed.getCircle(range, Point::euclideanMetric);
        } else {
            possible = seed.getCircle(range, Point::kingMetric);
        }
        // the requirement specification enforces us to throw it at any random tile
        // so we will give them what they want - we will choose a random one from all
        // possibles
        final FieldMap field = map;
        possible.removeIf(p -> !p.isOnField(field) || !field.getSpecificField(p).isWalkable());
        // remove all wich are not visible - this is expensive and can be changed at
        // will - this will return at least the seed, as otherwise the gadget could not
        // have been thrown, we will guard it anyways
        possible.removeIf(p -> !seed.getLine(p).isLineOfSight(field));
        // remove all wich the janitor or cat are positioned on because this is illegal
        possible.removeIf(p -> {
            final Optional<Character> mayCharacter = decodeCharacterByPosition(p);
            return mayCharacter.isPresent() && getActionProcessor().getGuard().isCatOrJanitor(mayCharacter.get());
        });

        if (possible.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(possible);
    }

    /**
     * Checks if there is an operation request for this connection, and if so it
     * will return the <i>newly</i> produced one.
     *
     * @param connection The connection to check for won't throw an exception if the
     *                   connection is no player, but it will never return a request
     *                   for those...
     * @return A newly request, if it is present.
     */
    public Optional<RequestGameOperationMessage> getOperationRequest(final NttsClientConnection connection) {
        if (currentlyActivePlayer == null)
            return Optional.empty(); // no waiting
        if ((currentlyActivePlayer.booleanValue() && connection.getGameRole() == GameRoleEnum.PLAYER_ONE)
                || (!currentlyActivePlayer.booleanValue() && connection.getGameRole() == GameRoleEnum.PLAYER_TWO)) {
            /* waiting for p1 or waiting for player two */
            return Optional.of(new RequestGameOperationMessage(connection.getClientId(),
                    currentlyActiveCharacter.getCharacterId()));

        }
        return Optional.empty(); // no waiting
    }

    /**
     * Try to publish the game status -- will only do something if
     * {@link #setGameStatusPublisher(Consumer)} set a valid target.
     * 
     * @param operations The operations that have happened
     */
    public void mayPublishStatus(final List<BaseOperation> operations) {
        if (gameStatusPublisher == null) {
            magpie.writeWarning("Loosing game-status: " + operations + " as no publisher is registered", "Publish");
        } else {
            gameStatusPublisher.accept(operations);
        }
    }

    /**
     * Signal a gameOver
     *
     * @param operations Operations that caused the game over
     */
    public void gameOver(final List<BaseOperation> operations) {
        this.isGameOver = true;
        // signal the game end - the mainGamePhase-Controller shall determine the winner
        mayPublishStatus(operations);
    }

    /**
     * This may update the data-buffers for this controller
     * 
     * @param position The position the field is on
     * @param state    The state to update from
     * 
     * @return The result of the removal operation, false if none done
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to update cache.
     */
    @StoryMethod
    public boolean mayRemoveListData(final Point position, final FieldStateEnum state) {
        switch (state) {
            case SAFE:
                return this.safePositions.remove(position);
            case BAR_SEAT:
                return this.barSeatPositions.remove(position);
            case BAR_TABLE:
                return this.barTablePositions.remove(position);
            case FIREPLACE:
                return this.fireplacePositions.remove(position);
            default:
                return false;
        }
    }

    /**
     * This may update the data-buffers for this controller
     * 
     * @param position The position the field is on
     * @param state    The state to update from
     * 
     * @return The result of the add operation, false if none done
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to update cache.
     */
    @StoryMethod
    public boolean mayAddListData(final Point position, final FieldStateEnum state) {
        switch (state) {
            case SAFE:
                return this.safePositions.add(position);
            case BAR_SEAT:
                return this.barSeatPositions.add(position);
            case BAR_TABLE:
                return this.barTablePositions.add(position);
            case FIREPLACE:
                return this.fireplacePositions.add(position);
            default:
                return false;
        }
    }

    /**
     * The underlying action controller
     * 
     * @return The action controller
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to perform actions for
     *              cat/janitor.
     */
    @StoryMethod
    public GameFieldActionController getActionController() {
        return this.actionController;
    }

    public String dumpCurrentRoundOrder() {
        return this.currentRoundOrder == null ? "<none>"
                : this.currentRoundOrder.stream().map(Character::getName).collect(Collectors.toList()).toString();
    }

    /**
     * Inject the round number
     * 
     * @param num The new Round number
     * 
     * @StoryMethod Used to inject by {@link BackstoryBuilder}.
     */
    @StoryMethod
    public void injectRoundNumber(final int num) {
        this.currentRound = num;
    }

    /**
     * Signal a game-over without try to publish any status
     */
    public void setGameOver() {
        this.isGameOver = true;
    }

    /**
     * Checks if the janitor is active
     * 
     * @return true if the janitor is active, false otherwise
     */
    public boolean inCleanup() {
        return this.inCleanup;
    }

    /**
     * Update the classifier to be used by this controller
     * 
     * @param classifier The (new) classifier
     */
    public void setClassifier(final IslandClassifier classifier) {
        this.classifier = classifier;
    }

    /**
     * Get the classifier used by this controller
     * 
     * @return The classifier used by this controller
     */
    public IslandClassifier getClassifier() {
        return this.classifier;
    }

    /**
     * Set the islands this controller should use, they will probably be set by the
     * {@link #setClassifier(IslandClassifier)}-classifier.
     * 
     * @param islands The islands to set
     */
    public void setIslands(final Island[] islands) {
        this.islands = islands;
    }

    /**
     * Get the islands this game field controller is using
     * 
     * @return The Islands this controller is using
     */
    public Island[] getIslands() {
        return this.islands;
    }

    /**
     * Get the configuration that this controller is using.
     * 
     * @return The {@link Configuration} used.
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Get the cat the game field controller is handling
     * 
     * @return The cat-character
     */
    public Character getCat() {
        return this.cat;
    }

    /**
     * Get the janitor the game field controller is handling. This does not imply,
     * the janitor is placed on the board. Check {@link #inCleanup()} for that.
     * 
     * @return The janitor-character
     */
    public Character getJanitor() {
        return this.janitor;
    }

    /**
     * Get the map/board this game field controller is handling
     * 
     * @return The underlying board.
     */
    public FieldMap getMap() {
        return this.map;
    }

    /**
     * Returns the pathfinder this game field controller is operating on -- it is
     * configured with defaults values and you may create your own.
     * 
     * @return The embedded pathfinder.
     */
    public Pathfinder<Field> getPathfinder() {
        return this.pathfinder;
    }

    /**
     * Get the statistics provider this game field controller is operating on.
     * 
     * @return The statistics provider.
     */
    public GameFieldStatisticsProvider getStatisticsProvider() {
        return this.statisticsProvider;
    }

    /**
     * Get the action processor this game field controller is operating on.
     * 
     * @return The action processor.
     */
    public CharacterActionProcessor getActionProcessor() {
        return this.processor;
    }

    /**
     * Get all safe combinations received by player one.
     * 
     * @return Player ones' safes combinations.
     */
    public Set<Integer> getPlayerOneSafeCombinations() {
        return this.playerOneSafeCombinations;
    }

    /**
     * Get all safe combinations received by player two.
     * 
     * @return Player twos' safe combinations.
     */
    public Set<Integer> getPlayerTwoSafeCombinations() {
        return this.playerTwoSafeCombinations;
    }

    /**
     * Get all safe combinations received by neutral players.
     * 
     * @return Neutral safe combinations.
     */
    public Set<Integer> getNeutralSafeCombinations() {
        return this.neutralSafeCombinations;
    }

    /**
     * Get List of all safe positions
     * 
     * @return List of all safe positions
     */
    public List<Point> getSafePositions() {
        return this.safePositions;
    }

    /**
     * Get the maximum safe number present - this may be calculated from the length
     * of the safe-positions in {@link #getSafePositions()}.
     * 
     * 
     * @return The maximum safe number.
     */
    public int getMaxSafeNumber() {
        return safePositions.size();
    }

    /**
     * Get a set of all characters present.
     * 
     * @return Set of all characters.
     */
    public Set<Character> getAllCharacters() {
        return this.characters;
    }

    /**
     * Get all bar seat positions
     * 
     * @return Position of all bar seats
     */
    public List<Point> getBarSeatPositions() {
        return barSeatPositions;
    }

    /**
     * The underlying story author
     * 
     * @return The story author used by this game field controller.
     */
    public StoryAuthor getAuthor() {
        return this.author;
    }

    /**
     * Get the faction of player one
     * 
     * @return Faction of player one
     */
    public Faction getPlayerOneFaction() {
        return this.playerOneFaction;
    }

    /**
     * Get the faction of player two
     * 
     * @return Faction of player two
     */
    public Faction getPlayerTwoFaction() {
        return this.playerTwoFaction;
    }

    /**
     * Get the faction of neutral characters
     * 
     * @return Faction of neutral characters
     */
    public Faction getNeutralFaction() {
        return this.neutralFaction;
    }

    /**
     * Returns the game-over-flag
     * 
     * @return True if the game was registered as over, false otherwise
     */
    public boolean isGameOver() {
        return this.isGameOver;
    }

    /**
     * Set the publisher to send gameStatus updates to. This will replace previous
     * ones!
     * 
     * @param gameStatusPublisher The publisher to use.
     */
    public void setGameStatusPublisher(final Consumer<List<BaseOperation>> gameStatusPublisher) {
        this.gameStatusPublisher = gameStatusPublisher;
    }

    /**
     * Will report the current round number
     *
     * @return The current round number
     */
    public int getCurrentRoundNumber() {
        return this.currentRound;
    }

    /**
     * @return the randomController
     */
    public RandomController getRandomController() {
        return randomController;
    }

    /**
     * Used to get the plain number of players in the current round to be able to
     * skip one :D
     * 
     * @return Number of characters controlled by players in the current round
     * 
     * @StoryMethod Used to calculate the correct player numbers by the
     *              {@link StoryBoard}.
     */
    @StoryMethod
    public long playerCharactersInCurrentRound() {
        return this.characters.stream()
                .filter(c -> this.playerOneFaction.contains(c) || this.playerTwoFaction.contains(c)).count();
    }
}