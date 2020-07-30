package de.uulm.team020.server.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import de.uulm.team020.datatypes.IAmJson;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.core.dummies.authors.CompactStoryAuthor;
import de.uulm.team020.server.core.dummies.authors.DefaultStoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.validation.GameDataGson;

/**
 * This is the configuration special for this server, that allows to do
 * configurations beyond what is standardized. It uses json to comply with the
 * other configurations.
 *
 * @author Florian Sihler
 * @version 1.3, 04/21/2020
 */
public final class ServerConfiguration implements IAmJson {

    private static final long serialVersionUID = -7476972602391625725L;

    public static final Set<String> TRUTH_VALUES = Set.of("true", "1", "wahr", "enable");

    private static final String SERVER_CONFIG_TXT = "ServerConfig";
    private static final IMagpie magpie = Magpie.createMagpieSafe("Server");
    private final List<GadgetEnum> forbiddenGadgets = Collections.emptyList();

    // Character & Gadget Management
    private boolean allowDuplicateCharacters;
    // Number of characters
    private int minimumCharacters;
    // Author configuration
    private long storyAuthorSleepThresholdMs;
    // Server-main configuration
    private String timezone;
    private int timeoutDetectionTime;
    private boolean unexpectedReconnect;
    private boolean sendMetaOnConnectionOpen;
    private int numberOfNpc;
    private int gadgetNpcMinimum;
    private int gadgetNpcMaximum;
    private int npcMinDelay;
    private int npcMaxDelay;
    private double npcHasRightKeyOnSpyChance;
    private boolean useIslandsForSpawn;
    private boolean islandSpawnerMayMorePlayers;

    private boolean escalateOnTooLateOperation;
    private boolean swapIfMoveOnJanitor;
    private boolean closestFreeFieldFadesThroughWall;
    private boolean closestCharacterByWalk;
    private boolean catMayJump;
    private boolean offerReplay;
    private boolean matchconfigDistanceEuclidean;
    private boolean flapsAndSealsThroughWall;
    private boolean moledieIsDirectAttack;
    private boolean bowlerBladeLineOfSightInterruptedByCatOrJanitor;
    private boolean laserCompactIsDirectAttack;
    private boolean jetpackAllowedOnIsWalkable;
    private boolean liveView;
    private boolean resumeByBoth;
    private boolean allMpApOnBeginOfTurn;
    private boolean gameStatusOnTurnStart;
    private boolean receiveInPause;
    private boolean sendEmptyOperationListOnStart;
    private boolean npcHasAtLeastOneKey;
    private boolean clearExfiltrationOnNextRound;
    private boolean fogHitsWalledFields;

    private boolean catAndJanitorHaveUuid;
    private boolean catAndJanitorAreActiveCharacter;

    private boolean forgetResumesOnCrash;
    // npc logic
    private boolean npcShouldMove;
    private int npcSecretMaySkip;
    // ai logic and client logic
    private int aiTurnDelay;
    private int gameStatusDelay;

    // Semantics
    private boolean performSemantics;
    private int minimumSplitsForTile; // Num of connected free tiles for a free to be considered a split
    private int minimumSafeNeighbours;
    private boolean allSafesConnected;

    // Logging
    private boolean magpieToConsole;
    private boolean magpieShouldUseRainbow;
    private int storyAuthorVerbosity;

    // Basically breaks everything :D
    private boolean runAsClient;

    // commandline
    private boolean dropToCommandLine;
    // will map "key" => (Description, Consumer), i know this is horrible, but i do
    // not care.
    private transient Map<String, ImmutablePair<String, IXConfigConsumer>> xConsumers;

    /**
     * Create a new Configuration
     */
    public ServerConfiguration() {
        initXConsumer();
    }

    /**
     * Copy-Constructor performing some funny copy operation
     *
     * @param other the old server configuration to copy
     */
    public ServerConfiguration(final ServerConfiguration other) {
        this();
        this.allowDuplicateCharacters = other.allowDuplicateCharacters;
        this.minimumCharacters = other.minimumCharacters;
        this.forbiddenGadgets.addAll(other.forbiddenGadgets);
        this.storyAuthorSleepThresholdMs = other.storyAuthorSleepThresholdMs;
        this.timeoutDetectionTime = other.timeoutDetectionTime;
        this.unexpectedReconnect = other.unexpectedReconnect;
        this.performSemantics = other.performSemantics;
        this.minimumSplitsForTile = other.minimumSplitsForTile;
        this.numberOfNpc = other.numberOfNpc;
        this.minimumSafeNeighbours = other.minimumSafeNeighbours;
        this.allSafesConnected = other.allSafesConnected;
        this.sendMetaOnConnectionOpen = other.sendMetaOnConnectionOpen;
        this.gadgetNpcMinimum = other.gadgetNpcMinimum;
        this.gadgetNpcMaximum = other.gadgetNpcMaximum;
        this.useIslandsForSpawn = other.useIslandsForSpawn;
        this.npcMinDelay = other.npcMinDelay;
        this.npcMaxDelay = other.npcMaxDelay;
        this.npcHasRightKeyOnSpyChance = other.npcHasRightKeyOnSpyChance;
        this.swapIfMoveOnJanitor = other.swapIfMoveOnJanitor;
        this.closestFreeFieldFadesThroughWall = other.closestFreeFieldFadesThroughWall;
        this.npcShouldMove = other.npcShouldMove;
        this.closestCharacterByWalk = other.closestCharacterByWalk;
        this.npcSecretMaySkip = other.npcSecretMaySkip;
        this.catMayJump = other.catMayJump;
        this.escalateOnTooLateOperation = other.escalateOnTooLateOperation;
        this.offerReplay = other.offerReplay;
        this.dropToCommandLine = other.dropToCommandLine;
        this.flapsAndSealsThroughWall = other.flapsAndSealsThroughWall;
        this.matchconfigDistanceEuclidean = other.matchconfigDistanceEuclidean;
        this.moledieIsDirectAttack = other.moledieIsDirectAttack;
        this.bowlerBladeLineOfSightInterruptedByCatOrJanitor = other.bowlerBladeLineOfSightInterruptedByCatOrJanitor;
        this.laserCompactIsDirectAttack = other.laserCompactIsDirectAttack;
        this.jetpackAllowedOnIsWalkable = other.jetpackAllowedOnIsWalkable;
        this.islandSpawnerMayMorePlayers = other.islandSpawnerMayMorePlayers;
        this.liveView = other.liveView;
        this.resumeByBoth = other.resumeByBoth;
        this.forgetResumesOnCrash = other.forgetResumesOnCrash;
        this.runAsClient = other.runAsClient;
        this.sendEmptyOperationListOnStart = other.sendEmptyOperationListOnStart;
        this.magpieToConsole = other.magpieToConsole;
        this.timezone = other.timezone;
        this.allMpApOnBeginOfTurn = other.allMpApOnBeginOfTurn;
        this.gameStatusOnTurnStart = other.gameStatusOnTurnStart;
        this.receiveInPause = other.receiveInPause;
        this.npcHasAtLeastOneKey = other.npcHasAtLeastOneKey;
        this.magpieShouldUseRainbow = other.magpieShouldUseRainbow;
        this.clearExfiltrationOnNextRound = other.clearExfiltrationOnNextRound;
        this.fogHitsWalledFields = other.fogHitsWalledFields;
        this.storyAuthorVerbosity = other.storyAuthorVerbosity;
        this.aiTurnDelay = other.aiTurnDelay;
        this.gameStatusDelay = other.gameStatusDelay;
        this.catAndJanitorHaveUuid = other.catAndJanitorHaveUuid;
        this.catAndJanitorAreActiveCharacter = other.catAndJanitorAreActiveCharacter;
    }

    /**
     * Create a configuration based on an internal file
     *
     * @param path The path to load the configuration from
     * @return The new configuration
     * @throws IOException If there is an error reading the configuration
     */
    public static ServerConfiguration fromInternalFile(String path) throws IOException {
        return fromJson(GameDataGson.loadInternalJson(path));
    }

    /**
     * Create a configuration based on json data
     *
     * @param json The json data to load
     * @return The new configuration
     */
    public static ServerConfiguration fromJson(String json) {
        return GameDataGson.fromJson(json, ServerConfiguration.class);
    }

    public boolean allowDuplicateCharacters() {
        return this.allowDuplicateCharacters;
    }

    public int minimumCharacters() {
        return this.minimumCharacters;
    }

    public List<GadgetEnum> forbiddenGadgets() {
        return this.forbiddenGadgets;
    }

    public long storyAuthorSleepThresholdMs() {
        return this.storyAuthorSleepThresholdMs;
    }

    public int timeoutDetectionTime() {
        return this.timeoutDetectionTime;
    }

    public boolean unexpectedReconnect() {
        return this.unexpectedReconnect;
    }

    public boolean performSemantics() {
        return this.performSemantics;
    }

    /**
     * @return Inclusive minimum for free-neighbours for a free tile to be
     *         considered a splitter
     */
    public int minimumSplitsForTile() {
        return this.minimumSplitsForTile;
    }

    public int numberOfNpc() {
        return this.numberOfNpc;
    }

    public int gadgetNpcMinimum() {
        return this.gadgetNpcMinimum;
    }

    public int gadgetNpcMaximum() {
        return this.gadgetNpcMaximum;
    }

    /**
     * Minimum number of milliseconds a npc should wait
     *
     * @return Min wait time in ms
     */
    public int getNpcMinDelay() {
        return this.npcMinDelay;
    }

    /**
     * Maximum number of milliseconds a npc should wait
     *
     * @return Max wait time in ms
     */
    public int getNpcMaxDelay() {
        return this.npcMaxDelay;
    }

    /**
     * This sets the maximum offset a npc might present, if this is &lt;= 1 the npc
     * will always offer the next number, if the chance triggers. Otherwise it might
     * provide up to 'n' consecutive safe keys whereas 'n' is the set value.
     *
     * @return Max number of skip on secret discovery
     */
    public int npcSecretMaySkip() {
        return this.npcSecretMaySkip;
    }

    /**
     * Determines, if an npc should at least offer one key if the chance
     * {@link #npcHasRightKeyOnSpyChance()} triggers. Otherwise he may gift '0' new
     * keys.
     * 
     * @return Should npcs grant at least one key?
     */
    public boolean npcHasAtLeastOneKey() {
        return this.npcHasAtLeastOneKey;
    }

    public int minimumSafeNeighbours() {
        return this.minimumSafeNeighbours;
    }

    public boolean allSafesConnected() {
        return this.allSafesConnected;
    }

    public boolean sendMetaOnConnectionOpen() {
        return this.sendMetaOnConnectionOpen;
    }

    public boolean useIslandsForSpawn() {
        return this.useIslandsForSpawn;
    }

    public double npcHasRightKeyOnSpyChance() {
        return this.npcHasRightKeyOnSpyChance;
    }

    public boolean swapIfMoveOnJanitor() {
        return this.swapIfMoveOnJanitor;
    }

    public boolean moledieIsDirectAttack() {
        return this.moledieIsDirectAttack;
    }

    /**
     * Shall the cat be allowed to jump if there is no one else on the current
     * field? This will only be available if {@link #useIslandsForSpawn()} is
     * enabled, otherwise this feature can not be used from a technical perspective.
     *
     * @return True if the cat is allowed to jump, false otherwise
     */
    public boolean catMayJump() {
        return this.catMayJump;
    }

    public boolean escalateOnTooLateOperation() {
        return this.escalateOnTooLateOperation;
    }

    /**
     * This option does control, if the island spawner may allow less than 4 or more
     * than 8 characters. Probably not :D
     * 
     * @return True if allowed, false if only allow 4 to 8-Players
     */
    public boolean islandSpawnerMayMorePlayers() {
        return this.islandSpawnerMayMorePlayers;
    }

    public boolean closestFreeFieldFadesThroughWall() {
        return this.closestFreeFieldFadesThroughWall;
    }

    /**
     * Determines if the property flaps and seals allows to open a safe through one
     * field of wall
     *
     * @return True if permitted, false if prohibited
     */
    public boolean flapsAndSealsThroughWall() {
        return this.flapsAndSealsThroughWall;
    }

    /**
     * Shall the server offer the replay-feature?
     *
     * @return True if the replay-feature shall be available, false otherwise
     */
    public boolean offerReplay() {
        return this.offerReplay;
    }

    /**
     * Will determine if the next character (e.g.) when tossing the moledie shall be
     * determined by walk first - or not. If not, euclidean distance measuring will
     * apply.
     *
     * @return True if walk-measurements shall be performed.
     */
    public boolean closestCharacterByWalk() {
        return this.closestCharacterByWalk;
    }

    public boolean npcShouldMove() {
        return this.npcShouldMove;
    }

    /**
     * Will the server drop to the command line to await an explicit quit or not?
     *
     * @return True if drop to command line, false if otherwise
     */
    public boolean dropToCommandLine() {
        return this.dropToCommandLine;
    }

    /**
     * Shall the distance calculation be euclidean based or king distance based.
     *
     * @return True if euclidean should be used
     * @see Point#euclideanMetric(Point, Point)
     * @see Point#kingMetric(Point, Point)
     */
    public boolean matchconfigDistanceEuclidean() {
        return matchconfigDistanceEuclidean;
    }

    /**
     * Should the server present a live-view while running?
     * 
     * @return True if live View is desired false otherwise
     */
    public boolean liveView() {
        return this.liveView;
    }

    /**
     * Should the Server run in restricted client mode?
     * 
     * @return True if client, false if normal (server)
     */
    public boolean runAsClient() {
        return this.runAsClient;
    }

    /**
     * Shall the cat or janitor interrupt the line of sight when using bowler blade?
     * This is important because the sight line that is used for
     * {@link GadgetEnum#BOWLER_BLADE} calculations is slightly different from the
     * normal ones as characters can interrupt the sight line.
     *
     * @return True if cat or janitor interrupt the line of sight
     */
    public boolean bowlerBladeLineOfSightInterruptedByCatOrJanitor() {
        return bowlerBladeLineOfSightInterruptedByCatOrJanitor;
    }

    /**
     * Is using the {@link GadgetEnum#LASER_COMPACT} gadget a direct attack?
     *
     * @return True if is direct attack, false otherwise
     */
    public boolean laserCompactIsDirectAttack() {
        return laserCompactIsDirectAttack;
    }

    /**
     * Whether jetpack cannot only be used on free but also on walkable fields.
     * 
     * @return True if walkable allowed, false otherwise
     */
    public boolean jetpackAllowedOnIsWalkable() {
        return jetpackAllowedOnIsWalkable;
    }

    /**
     * Informs, whether both players have to accept the resume or if it is enough if
     * one player requires to resume
     * 
     * @return True if two resumes are necessary to continue
     */
    public boolean resumeByBoth() {
        return this.resumeByBoth;
    }

    /**
     * Should MP and AP granted on begin of turn or on begin of round?
     * 
     * @return True begin of turn, false: begin of round
     */
    public boolean allMpApOnBeginOfTurn() {
        return this.allMpApOnBeginOfTurn;
    }

    /**
     * Should the game status be send on start of a Turn
     * 
     * @return True if status on round starts, false otherwise.
     */
    public boolean gameStatusOnTurnStart() {
        return this.gameStatusOnTurnStart;
    }

    /**
     * Holds, if the resume-request of a client should be forgotten if the client
     * disconnects/crashes.
     * 
     * @return True if it should be forgotten, false otherwise.
     */
    public boolean forgetResumesOnCrash() {
        return this.forgetResumesOnCrash;
    }

    /**
     * Do you want 'null' as the first operations to be happened, or an empty list?
     * 
     * @return True if empty list, false if null
     */
    public boolean sendEmptyOperationListOnStart() {
        return this.sendEmptyOperationListOnStart;
    }

    /**
     * Determines if exfiltrated players are allow to play again on the next turn
     * 
     * @return True if the exfiltration status will be removed, false if it stays.
     */
    public boolean clearExfiltrationOnNextRound() {
        return this.clearExfiltrationOnNextRound;
    }

    /**
     * Should magpie-output be mirrored to console? This will only take effect if
     * {@link #runAsClient} is false.
     * 
     * @return True if to be mirrored, false otherwise
     */
    public boolean magpieToConsole() {
        return this.magpieToConsole;
    }

    /**
     * Should the fog tin gadget cover walled fields (in case of a rocket pen
     * cleaning them)
     * 
     * @return True if walls are no walls against the fog.
     */
    public boolean fogHitsWalledFields() {
        return this.fogHitsWalledFields;
    }

    /**
     * Flag that determines if {@link Magpie#useRainbowForAll(boolean)} should be
     * set to true or false.
     * 
     * @return The value to be set to {@link Magpie#useRainbowForAll(boolean)}.
     */
    public boolean magpieShouldUseRainbow() {
        return this.magpieShouldUseRainbow;
    }

    /**
     * Timezone to be used
     * 
     * @return The timezone id to be used
     */
    public String getTimezone() {
        return this.timezone;
    }

    /**
     * Should it be allowed for messages to arrive while a game pause?
     * 
     * @return True if it should be allowed, false otherwise.
     */
    public boolean receiveInPause() {
        return this.receiveInPause;
    }

    /**
     * @return the storyAuthorVerbosity
     */
    public int storyAuthorVerbosity() {
        return storyAuthorVerbosity;
    }

    public int aiTurnDelay() {
        return this.aiTurnDelay;
    }

    public int gameStatusDelay() {
        return this.gameStatusDelay;
    }

    public boolean catAndJanitorHaveUuid() {
        return this.catAndJanitorHaveUuid;
    }

    public boolean catAndJanitorAreActiveCharacter() {
        return this.catAndJanitorAreActiveCharacter;
    }

    /**
     * Produce a new story author based on the configured verbosity
     * 
     * @param name          the name that the story author should use
     * @param configuration the configuration that will be dumped on write out (just
     *                      as a reference to get server configuration)
     * 
     * @return A brand new Story-Author Object
     */
    public StoryAuthor produceStoryAuthorBasedOnVerbosity(final String name, final Configuration configuration) {
        switch (this.storyAuthorVerbosity) {
            case 1:
                return new CompactStoryAuthor(name, configuration);
            default:
            case 0:
                return new DefaultStoryAuthor(name, configuration);

        }
    }

    private void initXConsumer() {
        xConsumers = new HashMap<>();
        xConsumers.put("allowDuplicates", ImmutablePair.of(
                "Supply a boolean identifying if the server allows duplicate characters or not - currently it is "
                        + "against the population rules to allow this, but it may be in the future.",
                this::setXAllowDuplicates));
        xConsumers.put("useIslands",
                ImmutablePair.of("Supply a boolean to identify if the island-classifier should be used for spawning",
                        this::setXUseIsles));
        xConsumers.put("interactive",
                ImmutablePair.of("Supply a boolean to set if the server should drop to an interactive shell or not.",
                        this::setXInteractive));
        xConsumers.put("replay", ImmutablePair.of("Supply a boolean to set if the server should offer a replay or not.",
                this::setXReplay));
        xConsumers.put("live", ImmutablePair.of(
                "Supply a boolean to set if the server should present live field data in the main game phase or not.",
                this::setXUseLive));
        xConsumers.put("client", ImmutablePair.of("Supply a boolean to set if the server should run as client instead.",
                this::setXRunAsClient));
        xConsumers.put("log", ImmutablePair.of(
                "Supply a boolean to set if the server should mirror magpie to stdout? Do not combine with a positive 'client' option.",
                this::setXMirrorMagpie));
        xConsumers.put("timezone",
                ImmutablePair.of("Supply a valid timezone-id to set the timezone for the server", this::setXTimeZone));
        xConsumers.put("mpApPerRound", ImmutablePair.of(
                "Supply a boolean to set if the server should grant mpAp on round start (true) or on turn start (false, default).",
                this::setXApMp));
        xConsumers.put("gameStatusPerTurn",
                ImmutablePair.of(
                        "Supply a boolean to set if the server should send a game status whenever a round starts.",
                        this::setXTurnStartGS));
        xConsumers.put("resumeByBoth", ImmutablePair
                .of("Supply a boolean to set if the pause has to be resumed by both.", this::setXResumeBoth));
        xConsumers.put("rainbow",
                ImmutablePair.of(
                        "Supply a boolean to set if magpie, when mirrored to the console, use the rainbow formatter.",
                        this::setXRainbow));
        xConsumers.put("author", ImmutablePair.of(
                "Supply an Integer that determines the verbosity of the story author. 0 means to use the default implementation, 1 means that the compact author should be used. There may be support for higher numbers.",
                this::setXAuthorVerbosity));

    }

    /**
     * Helper to allow direct modifications of certain values determined by their
     * string. There will be <i>no</i> reflection access for that, any variable
     * access will be mapped and decoded on a hard-coded way as this allows
     * key-abstraction and value-pruning.
     *
     * @param kvPairs pairs caught on the commandline
     * @return List of errors, if they occurred. Can be used by the CLP
     */
    public List<String> xOptionInject(String... kvPairs) {
        if (kvPairs == null || kvPairs.length == 0)
            return Collections.emptyList(); // nothing passed
        magpie.writeInfo("X-Parser got: " + Arrays.toString(kvPairs), "xConfig");
        List<String> errors = new ArrayList<>(2);
        if (kvPairs.length % 2 != 0) {
            errors.add(
                    "The supplied keys for 'x' have to be balanced, which they are not in:" + Arrays.toString(kvPairs));
            for (Entry<String, ImmutablePair<String, IXConfigConsumer>> entries : xConsumers.entrySet()) {
                errors.add("    " + entries.getKey() + ": " + entries.getValue().getKey());
            }
            return errors;
        }
        // key based iteration with look-ahead
        for (int i = 0; i < kvPairs.length; i += 2) {
            if (!xConsumers.containsKey(kvPairs[i])) {
                errors.add("The key '" + kvPairs[i] + "' is unknown!");
                continue;
            }
            final String value = kvPairs[i + 1];
            if (value == null || value.isEmpty()) {
                errors.add("The key '" + kvPairs[i] + "' needs a value, but did not got one");
            } else {
                // increment by got - i know don't modify inside loop, but this is a simple
                // iteration
                errors.addAll(xConsumers.get(kvPairs[i]).getValue().apply(kvPairs[i], value));
            }
        }

        if (!errors.isEmpty()) {
            errors.add("X-Options for the server configuration. 'True' is considered everything in " + TRUTH_VALUES);
            // Add Help
            for (Entry<String, ImmutablePair<String, IXConfigConsumer>> entries : xConsumers.entrySet()) {
                errors.add("    " + entries.getKey() + ": " + entries.getValue().getKey());
            }
        }
        return errors;
    }

    private List<String> setXAllowDuplicates(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val);
        allowDuplicateCharacters = tVal;
        magpie.writeInfo("Setting 'allowDuplicateCharacters' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXUseIsles(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        useIslandsForSpawn = tVal;
        magpie.writeInfo("Setting 'useIslandsForSpawn' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXInteractive(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        dropToCommandLine = tVal;
        magpie.writeInfo("Setting 'dropToCommandLine' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXReplay(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        this.offerReplay = tVal;
        magpie.writeInfo("Setting 'offerReplay' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXUseLive(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        liveView = tVal;
        magpie.writeInfo("Setting 'liveView' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXRunAsClient(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        runAsClient = tVal;
        magpie.writeInfo("Setting 'runAsClient' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXMirrorMagpie(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        magpieToConsole = tVal;
        magpie.writeInfo("Setting 'magpieToConsole' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXRainbow(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        magpieShouldUseRainbow = tVal;
        magpie.writeInfo("Setting 'magpieShouldUseRainbow' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXTimeZone(String key, String val) {
        timezone = val;
        magpie.writeInfo("Setting 'timezone' to " + val, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXApMp(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        allMpApOnBeginOfTurn = tVal;
        magpie.writeInfo("Setting 'allMpApOnBeginOfTurn' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXTurnStartGS(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        gameStatusOnTurnStart = tVal;
        magpie.writeInfo("Setting 'gameStatusOnTurnStart' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXResumeBoth(String key, String val) {
        final boolean tVal = TRUTH_VALUES.contains(val.toLowerCase());
        resumeByBoth = tVal;
        magpie.writeInfo("Setting 'resumeByBoth' to " + tVal, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    private List<String> setXAuthorVerbosity(String key, String val) {
        int verbosityTarget;
        try {
            verbosityTarget = Integer.parseInt(val);
        } catch (NumberFormatException ex) {
            return List.of("The verbosity you entered: '" + val + "' is not a valid integer!");
        }
        storyAuthorVerbosity = verbosityTarget;
        magpie.writeInfo("Setting 'storyAuthorVerbosity' to " + verbosityTarget, SERVER_CONFIG_TXT);
        return Collections.emptyList();
    }

    /**
     * The x-consumers handled on commandline
     * 
     * @return the xConsumers
     */
    public Map<String, ImmutablePair<String, IXConfigConsumer>> getXConsumers() {
        return xConsumers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allMpApOnBeginOfTurn, allSafesConnected, allowDuplicateCharacters,
                bowlerBladeLineOfSightInterruptedByCatOrJanitor, catMayJump, closestCharacterByWalk,
                closestFreeFieldFadesThroughWall, dropToCommandLine, escalateOnTooLateOperation,
                flapsAndSealsThroughWall, forbiddenGadgets, forgetResumesOnCrash, gadgetNpcMaximum, gadgetNpcMinimum,
                gameStatusOnTurnStart, islandSpawnerMayMorePlayers, jetpackAllowedOnIsWalkable,
                laserCompactIsDirectAttack, liveView, magpieToConsole, matchconfigDistanceEuclidean, minimumCharacters,
                minimumSafeNeighbours, minimumSplitsForTile, moledieIsDirectAttack, npcHasRightKeyOnSpyChance,
                npcMaxDelay, npcMinDelay, npcSecretMaySkip, npcShouldMove, numberOfNpc, offerReplay, performSemantics,
                resumeByBoth, runAsClient, sendEmptyOperationListOnStart, sendMetaOnConnectionOpen,
                storyAuthorSleepThresholdMs, swapIfMoveOnJanitor, timeoutDetectionTime, timezone, unexpectedReconnect,
                useIslandsForSpawn, receiveInPause, npcHasAtLeastOneKey, magpieShouldUseRainbow,
                clearExfiltrationOnNextRound, storyAuthorVerbosity, aiTurnDelay, gameStatusDelay, catAndJanitorHaveUuid,
                catAndJanitorAreActiveCharacter);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServerConfiguration)) {
            return false;
        }
        ServerConfiguration other = (ServerConfiguration) obj;
        return allMpApOnBeginOfTurn == other.allMpApOnBeginOfTurn && allSafesConnected == other.allSafesConnected
                && allowDuplicateCharacters == other.allowDuplicateCharacters
                && bowlerBladeLineOfSightInterruptedByCatOrJanitor == other.bowlerBladeLineOfSightInterruptedByCatOrJanitor
                && catMayJump == other.catMayJump && closestCharacterByWalk == other.closestCharacterByWalk
                && closestFreeFieldFadesThroughWall == other.closestFreeFieldFadesThroughWall
                && dropToCommandLine == other.dropToCommandLine
                && escalateOnTooLateOperation == other.escalateOnTooLateOperation
                && flapsAndSealsThroughWall == other.flapsAndSealsThroughWall
                && Objects.equals(forbiddenGadgets, other.forbiddenGadgets)
                && forgetResumesOnCrash == other.forgetResumesOnCrash && gadgetNpcMaximum == other.gadgetNpcMaximum
                && gadgetNpcMinimum == other.gadgetNpcMinimum && gameStatusOnTurnStart == other.gameStatusOnTurnStart
                && islandSpawnerMayMorePlayers == other.islandSpawnerMayMorePlayers
                && jetpackAllowedOnIsWalkable == other.jetpackAllowedOnIsWalkable
                && laserCompactIsDirectAttack == other.laserCompactIsDirectAttack && liveView == other.liveView
                && magpieToConsole == other.magpieToConsole
                && matchconfigDistanceEuclidean == other.matchconfigDistanceEuclidean
                && minimumCharacters == other.minimumCharacters && minimumSafeNeighbours == other.minimumSafeNeighbours
                && minimumSplitsForTile == other.minimumSplitsForTile
                && moledieIsDirectAttack == other.moledieIsDirectAttack
                && Double.doubleToLongBits(npcHasRightKeyOnSpyChance) == Double
                        .doubleToLongBits(other.npcHasRightKeyOnSpyChance)
                && npcMaxDelay == other.npcMaxDelay && npcMinDelay == other.npcMinDelay
                && npcSecretMaySkip == other.npcSecretMaySkip && npcShouldMove == other.npcShouldMove
                && numberOfNpc == other.numberOfNpc && offerReplay == other.offerReplay
                && performSemantics == other.performSemantics && resumeByBoth == other.resumeByBoth
                && runAsClient == other.runAsClient
                && sendEmptyOperationListOnStart == other.sendEmptyOperationListOnStart
                && sendMetaOnConnectionOpen == other.sendMetaOnConnectionOpen
                && storyAuthorSleepThresholdMs == other.storyAuthorSleepThresholdMs
                && swapIfMoveOnJanitor == other.swapIfMoveOnJanitor
                && timeoutDetectionTime == other.timeoutDetectionTime && Objects.equals(timezone, other.timezone)
                && unexpectedReconnect == other.unexpectedReconnect && useIslandsForSpawn == other.useIslandsForSpawn
                && receiveInPause == other.receiveInPause && npcHasAtLeastOneKey == other.npcHasAtLeastOneKey
                && magpieShouldUseRainbow == other.magpieShouldUseRainbow
                && clearExfiltrationOnNextRound == other.clearExfiltrationOnNextRound
                && storyAuthorVerbosity == other.storyAuthorVerbosity && aiTurnDelay == other.aiTurnDelay
                && gameStatusDelay == other.gameStatusDelay && catAndJanitorHaveUuid == other.catAndJanitorHaveUuid
                && catAndJanitorAreActiveCharacter == other.catAndJanitorAreActiveCharacter;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ServerConfiguration [\n    allMpApOnBeginOfTurn=").append(allMpApOnBeginOfTurn)
                .append(", \n    allSafesConnected=").append(allSafesConnected)
                .append(", \n    allowDuplicateCharacters=").append(allowDuplicateCharacters)
                .append(", \n    bowlerBladeLineOfSightInterruptedByCatOrJanitor=")
                .append(bowlerBladeLineOfSightInterruptedByCatOrJanitor).append(", \n    catMayJump=")
                .append(catMayJump).append(", \n    closestCharacterByWalk=").append(closestCharacterByWalk)
                .append(", \n    closestFreeFieldFadesThroughWall=").append(closestFreeFieldFadesThroughWall)
                .append(", \n    dropToCommandLine=").append(dropToCommandLine)
                .append(", \n    escalateOnTooLateOperation=").append(escalateOnTooLateOperation)
                .append(", \n    flapsAndSealsThroughWall=").append(flapsAndSealsThroughWall)
                .append(", \n    forbiddenGadgets=").append(forbiddenGadgets).append(", \n    forgetResumesOnCrash=")
                .append(forgetResumesOnCrash).append(", \n    gadgetNpcMaximum=").append(gadgetNpcMaximum)
                .append(", \n    gadgetNpcMinimum=").append(gadgetNpcMinimum).append(", \n    gameStatusOnTurnStart=")
                .append(gameStatusOnTurnStart).append(", \n    islandSpawnerMayMorePlayers=")
                .append(islandSpawnerMayMorePlayers).append(", \n    jetpackAllowedOnIsWalkable=")
                .append(jetpackAllowedOnIsWalkable).append(", \n    laserCompactIsDirectAttack=")
                .append(laserCompactIsDirectAttack).append(", \n    liveView=").append(liveView)
                .append(", \n    magpieToConsole=").append(magpieToConsole)
                .append(", \n    matchconfigDistanceEuclidean=").append(matchconfigDistanceEuclidean)
                .append(", \n    minimumCharacters=").append(minimumCharacters).append(", \n    minimumSafeNeighbours=")
                .append(minimumSafeNeighbours).append(", \n    minimumSplitsForTile=").append(minimumSplitsForTile)
                .append(", \n    moledieIsDirectAttack=").append(moledieIsDirectAttack)
                .append(", \n    npcHasRightKeyOnSpyChance=").append(npcHasRightKeyOnSpyChance)
                .append(", \n    npcMaxDelay=").append(npcMaxDelay).append(", \n    npcMinDelay=").append(npcMinDelay)
                .append(", \n    npcSecretMaySkip=").append(npcSecretMaySkip).append(", \n    npcShouldMove=")
                .append(npcShouldMove).append(", \n    numberOfNpc=").append(numberOfNpc).append(", \n    offerReplay=")
                .append(offerReplay).append(", \n    performSemantics=").append(performSemantics)
                .append(", \n    resumeByBoth=").append(resumeByBoth).append(", \n    runAsClient=").append(runAsClient)
                .append(", \n    sendEmptyOperationListOnStart=").append(sendEmptyOperationListOnStart)
                .append(", \n    sendMetaOnConnectionOpen=").append(sendMetaOnConnectionOpen)
                .append(", \n    storyAuthorSleepThresholdMs=").append(storyAuthorSleepThresholdMs)
                .append(", \n    storyAuthorVerbosity=").append(storyAuthorVerbosity)
                .append(", \n    swapIfMoveOnJanitor=").append(swapIfMoveOnJanitor)
                .append(", \n    timeoutDetectionTime=").append(timeoutDetectionTime).append(", \n    timezone=")
                .append(timezone).append(", \n    unexpectedReconnect=").append(unexpectedReconnect)
                .append(", \n    receiveInPause=").append(receiveInPause).append(", \n    useIslandsForSpawn=")
                .append(useIslandsForSpawn).append(", \n    npcHasAtLeastOneKey=").append(npcHasAtLeastOneKey)
                .append(", \n    clearExfiltrationOnNextRound=").append(clearExfiltrationOnNextRound)
                .append(", \n    gameStatusDelay=").append(gameStatusDelay).append(", \n    aiTurnDelay=")
                .append(aiTurnDelay).append(", \n    catAndJanitorHaveUuid=").append(catAndJanitorHaveUuid)
                .append(", \n    catAndJanitorAreActiveCharacter=").append(catAndJanitorAreActiveCharacter)
                .append(", \n    magpieShouldUseRainbow=").append(magpieShouldUseRainbow).append("]");
        return builder.toString();
    }

}
