package de.uulm.team020.server.configuration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.timer.TimeoutController;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryMethod;

/**
 * Configuration for the team020 Server, will gather all Constants and Settings
 * that shall be used.
 * 
 * @author Florian Sihler
 * @version 1.1, 04/04/2020
 */
public final class Configuration implements Serializable {

    private static final long serialVersionUID = -8232164549751892480L;

    private static final String CONFIG_TXT = "Config";

    /** Logging-instance */
    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    private boolean locked = true;

    /**
     * Create a new Configuration using default server settings
     * 
     */
    public Configuration() {
        this.timeoutController = new TimeoutController(this);
        try {
            this.serverConfiguration = ServerConfiguration
                    .fromInternalFile("defaults/configuration/default-internal-config.json");
        } catch (IOException ex) {
            // this shouldn't happen, so...
            magpie.writeException(ex, CONFIG_TXT);
            throw new ConfigurationException(ex.getMessage());
        }
    }

    /**
     * Create a new Configuration
     * 
     * @param serverConfiguration The configuration to use for the ServerSettings
     */
    public Configuration(ServerConfiguration serverConfiguration) {
        this.timeoutController = new TimeoutController(this);
        this.serverConfiguration = serverConfiguration;
    }

    /**
     * Create a deep-copy of another configuration. This will copy the
     * {@link #SESSION_ID} as well! Furthermore, the Timeoutcontroller will be
     * cleared on the new instance as you should not copy timeouts (they would occur
     * double).
     * 
     * @param configuration The configuration you want to copy
     */
    public Configuration(final Configuration configuration) {
        this.loadDefaults = configuration.loadDefaults;
        this.characters = Arrays.copyOf(configuration.characters, configuration.characters.length);
        this.characterDescriptions = Arrays.copyOf(configuration.characterDescriptions,
                configuration.characterDescriptions.length);
        this.scenario = new Scenario(configuration.scenario);
        this.matchconfig = new Matchconfig(configuration.matchconfig);
        this.port = configuration.port;
        this.SESSION_ID = configuration.SESSION_ID;
        this.strictMode = configuration.strictMode;
        this.freshnessThresholdInS = configuration.freshnessThresholdInS;
        this.timeoutController = new TimeoutController(this);
        this.serverConfiguration = new ServerConfiguration(configuration.serverConfiguration);
        this.gamePhase = configuration.gamePhase;
        this.gameEnded = configuration.gameEnded;
    }

    /**
     * This will create a new Configuration Object built from command line
     * arguments.
     * 
     * @param args Command line arguments desired - they must be separated correctly
     *             ^^
     * 
     * @return The built configuration
     */
    public static Configuration buildFromArgs(String... args) {
        Configuration configuration = new Configuration();

        List<String> errors = CommandLineParser.parse(configuration, args);
        if (!errors.isEmpty()) {
            throw new ConfigurationException("Command line args for build not valid! See: " + errors);
        }
        return configuration;
    }

    /** Should defaults be loaded if none supplied? */
    private boolean loadDefaults = false;

    /**
     * Name of the server-instance, will be used by
     * {@link CommandLineParser#parse(Configuration, String[])} to create a
     * reasonable help-line.
     */
    public static final String SERVER_NAME = "server020";

    /** Version */
    public static final int VERSION = 1100;
    /** Is this Version deemed to be stable? */
    public static final boolean IS_STABLE = true;

    /**
     * List of supplied Characters, already populated with UUIDs
     */
    private CharacterInformation[] characters = null;
    private CharacterDescription[] characterDescriptions = null;

    /**
     * Scenario to be played
     */
    private Scenario scenario = null;

    /**
     * Matchconfig to use
     */
    private Matchconfig matchconfig = null;

    /**
     * The port-number to use
     */
    private int port = 7007;

    /** the uuid for this session */
    private UUID SESSION_ID = UUID.randomUUID();

    /** should the strict-mode be enabled? */
    private boolean strictMode = false;

    /** Threshold for incoming messages */
    private int freshnessThresholdInS = -1;

    /** Controls timeout calls and resumes */
    private transient TimeoutController timeoutController;

    private ServerConfiguration serverConfiguration;

    private int matchconfigMultiplier = 1000; // should be altered for Tests only

    private boolean gameEnded = false;

    /**
     * contains the phase the server is currently in, can be used to change the
     * behaviour of the program on a global scale
     */
    private GamePhaseEnum gamePhase = GamePhaseEnum.getFirst();

    public void reset() {
        loadDefaults = false;
        characters = null;
        scenario = null;
        matchconfig = null;
        port = 7007;
        // Start a new Session
        SESSION_ID = UUID.randomUUID();
        gamePhase = GamePhaseEnum.getFirst();
        magpie.writeInfo("Configurations have been reset, now running with sessionId: " + SESSION_ID, CONFIG_TXT);
        timeoutController.stop();
        timeoutController = new TimeoutController(this);
        gameEnded = false;
        _lock();
    }

    public Configuration newSession() {
        SESSION_ID = UUID.randomUUID();
        gamePhase = GamePhaseEnum.getFirst();
        timeoutController.stop();
        timeoutController = new TimeoutController(this);
        gameEnded = false;
        magpie.writeInfo("New Session request, now running with sessionId: " + SESSION_ID, CONFIG_TXT);
        return this;
    }

    /**
     * Loads {@link #characters} from a set of Character-Descriptions. This will
     * inflate the amount of characters if there are too less.
     * 
     * @param characterDescriptions the array of characters
     */
    public void populateCharacters(CharacterDescription[] characterDescriptions) {
        if (locked && this.characters != null) {
            throw new ConfigurationException("You can't change the characters!");
        }
        this.characterDescriptions = characterDescriptions;
        List<CharacterDescription> descriptions = new ArrayList<>(Arrays.asList(characterDescriptions));

        // So every player is allowed to choose his characters, there have to be at
        // least 9
        // so we will throw an error if there are less (8 chars + 1 NPC).
        int amountOfCharacters = getServerConfig().minimumCharacters();

        if (this.characterDescriptions.length < amountOfCharacters) {
            magpie.writeInfo("Cloning Characters", "clone-char");
            descriptions = cloneToGetMinimumAmountOfCharactersIfAllowedElseThrowException(this.characterDescriptions,
                    amountOfCharacters);
        }
        final int size = descriptions.size();
        magpie.writeInfo("Populating #characters with '" + size + "' Characters", CONFIG_TXT);
        this.characters = new CharacterInformation[size];
        for (int i = 0; i < size; i++) {
            this.characters[i] = new CharacterInformation(descriptions.get(i), UUID.randomUUID());
            magpie.writeDebug(String.format("Assigning Char-UUID for '%s' with '%s'", this.characters[i].getName(),
                    this.characters[i].getId()), CONFIG_TXT);
        }
    }

    private List<CharacterDescription> cloneToGetMinimumAmountOfCharactersIfAllowedElseThrowException(
            CharacterDescription[] characterDescriptions, int amountOfCharacters) {
        List<CharacterDescription> descriptions;
        final String message = "The amount of characters (" + characterDescriptions.length
                + ") is lower than the wanted amount: " + amountOfCharacters;
        if (!getServerConfig().allowDuplicateCharacters()) {
            throw new ConfigurationException(message);
        } else {
            descriptions = cloneToGetMinimumAmountOfCharacters(characterDescriptions, amountOfCharacters, message);
        }
        return descriptions;
    }

    private List<CharacterDescription> cloneToGetMinimumAmountOfCharacters(CharacterDescription[] characterDescriptions,
            int amountOfCharacters, final String message) {
        List<CharacterDescription> descriptions;
        magpie.writeWarning(message, CONFIG_TXT);
        // inflate descriptions to produce clones - as this server wants so they will
        // get numbers attached
        descriptions = new ArrayList<>(amountOfCharacters);
        int cloneCounter = 0;
        int inflationSourceIter = 0;
        while (descriptions.size() < amountOfCharacters) {
            // Get the next one to clone and use for inflation:
            CharacterDescription got = characterDescriptions[inflationSourceIter];
            descriptions.add(new CharacterDescription(got.getName() + "-" + cloneCounter, got));
            magpie.writeWarning("Producing Clone: " + descriptions.get(descriptions.size() - 1), "clone-char");
            // next
            inflationSourceIter = inflationSourceIter + 1;
            if (inflationSourceIter >= characterDescriptions.length) {
                inflationSourceIter = 0;
                cloneCounter += 1;
            }
        }
        return descriptions;
    }

    /**
     * Do not use if you don't know what this does - and you probably don't, even if
     * you think you do...
     * 
     * @param characters The characters to use
     * 
     * @StoryMethod Allows the direct character information, including the uuid for
     *              the {@link StoryBoard}
     */
    @StoryMethod
    public void setCharacters(CharacterInformation[] characters) {
        if (locked)
            throw new ConfigurationException("You can't change the characters!");
        magpie.writeWarning("Character set! (" + Arrays.toString(characters) + ")", CONFIG_TXT);
        this.characters = characters;
    }

    /**
     * Assigns the scenario to be played
     * 
     * @param scenario the scenario to play
     */
    public void setScenario(Scenario scenario) {
        if (locked && this.scenario != null)
            throw new ConfigurationException("You can't change the scenario!");
        this.scenario = scenario;
    }

    /**
     * Assigns the matchconfig to be played
     * 
     * @param matchconfig the scenario to play
     */
    public void setMatchconfig(Matchconfig matchconfig) {
        if (locked && this.matchconfig != null)
            throw new ConfigurationException("You can't change the Matchconfiguration!");
        this.matchconfig = matchconfig;
    }

    /**
     * @return true if defaults should be loaded by the {@link CommandLineParser},
     *         used as a register.
     */
    public boolean loadDefaults() {
        return this.loadDefaults;
    }

    public void setLoadDefaults() {
        this.loadDefaults = true;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public CharacterInformation[] getCharacters() {
        return this.characters;
    }

    public CharacterDescription[] getCharacterDescriptions() {
        return this.characterDescriptions;
    }

    public Scenario getScenario() {
        return this.scenario;
    }

    public Matchconfig getMatchconfig() {
        return this.matchconfig;
    }

    public int getPort() {
        return this.port;
    }

    public UUID getSessionId() {
        return this.SESSION_ID;
    }

    public boolean isStrictMode() {
        return this.strictMode;
    }

    public void setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
    }

    public GamePhaseEnum getGamePhase() {
        return this.gamePhase;
    }

    /**
     * Switch to the next server-phase
     *
     * @return True if the game is over and the Phase is the end-phase, false
     *         otherwise
     */
    public boolean shiftNextPhase() {
        this.gamePhase = this.gamePhase.getNextServer();
        magpie.writeInfo("Server now in phase: " + this.gamePhase, CONFIG_TXT);
        return gamePhase == GamePhaseEnum.END;
    }

    /**
     * Set a specific Server-Phase
     *
     * @param thePhase the next Phase to shift to
     * 
     * @return True if the game is over and the Phase is the end-phase, false
     *         otherwise
     */
    public boolean shiftPhase(GamePhaseEnum thePhase) {
        magpie.writeInfo(
                "Server jumped " + (thePhase.isBefore(this.gamePhase) ? "again " : "") + "in phase: " + thePhase,
                CONFIG_TXT);
        this.gamePhase = thePhase;
        return this.gamePhase == GamePhaseEnum.END;
    }

    public void setFreshnessThresholdInS(int freshnessThresholdInS) {
        this.freshnessThresholdInS = freshnessThresholdInS;
    }

    public int getFreshnessThresholdInS() {
        return this.freshnessThresholdInS;
    }

    public TimeoutController getTimeoutController() {
        return this.timeoutController;
    }

    public void setServerConfiguration(ServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    public ServerConfiguration getServerConfig() {
        return this.serverConfiguration;
    }

    /**
     * Decode a character name by its uuid
     * 
     * @param id The requested uuid
     * 
     * @return The name of the character, or null if not found
     */
    public String getCharacterName(UUID id) {
        CharacterInformation information = getCharacter(id);
        return information == null ? null : information.getName();
    }

    /**
     * Decode a character id by its name
     * 
     * @param name The name of the character
     * 
     * @return The uuid of the character, or null if not found
     */
    public UUID getCharacterId(String name) {
        CharacterInformation information = getCharacter(name);
        return information == null ? null : information.getId();
    }

    /**
     * Decode a character by its name
     * 
     * @param name The name of the character
     * 
     * @return The character, or null if not found
     */
    public CharacterInformation getCharacter(String name) {
        for (CharacterInformation characterInformation : characters) {
            if (characterInformation.getName().equals(name))
                return characterInformation;
        }
        return null;
    }

    /**
     * Decode a character by its (uu)id
     * 
     * @param id The id of the character
     * 
     * @return The character, or null if not found
     */
    public CharacterInformation getCharacter(UUID id) {
        for (CharacterInformation characterInformation : characters) {
            if (characterInformation.getId().equals(id))
                return characterInformation;
        }
        return null;
    }

    /**
     * Do not use this method if you don't know <i>exactly</i> what it does, it
     * shall be used for testing only and allows and easy time-replacement without
     * having to recreate a whole object and injecting it into the controller
     * 
     * @param newMultiplier The new Multiplier desired
     */
    public void changeMultiplier(int newMultiplier) {
        this.matchconfigMultiplier = newMultiplier;
    }

    public int getMatchconfigMultiplier() {
        return this.matchconfigMultiplier;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    /**
     * Locks the config, do not use if you don't know, what this does.
     * 
     * @StoryMethod Lock all Configuration features - default.
     */
    @StoryMethod
    public void _lock() {
        locked = true;
    }

    /**
     * Unlocks the config, do not use if you don't know, what this does.
     * 
     * @StoryMethod Unlock all Configuration features. Never use without
     *              {@link #_lock()}.
     */
    @StoryMethod
    public void _unlock() {
        magpie.writeWarning("Config has been unlocked!", CONFIG_TXT);
        locked = false;
    }
}
