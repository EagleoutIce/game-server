package de.uulm.team020.server.core.dummies.story;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Pair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.InternalResources;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.parser.expander.Expandables;
import de.uulm.team020.parser.expander.Expander;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.helper.StoryLine;
import de.uulm.team020.server.core.dummies.story.helper.StoryLineProducer;
import de.uulm.team020.server.core.dummies.story.helper.StoryTokenizer;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.game.phases.choice.DraftingProposal;
import de.uulm.team020.validation.GameDataGson;

/**
 * This class should ease the writing of "stories" whereas stories describe
 * server-client communications using a dummy-language that will inject random
 * data if possible.
 * <p>
 * A Storyboard will create a fresh controller, modify it with the given rules
 * and return it and all the clients with the modified state. It can be seen as
 * a glorified factory.
 *
 * @author Florian Sihler
 * @version 1.4, 05/08/2020
 */
public class StoryBoard {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Story");

    private static final String STORY_TXT = "Story";
    private static final String SPACE_COMMA_SPLIT = ",\\s*";
    private static final String FOR_TXT = " for: ";

    public static final String INTERNAL = "INTERNAL";
    public static final String RAW_JSON = "RAW-JSON";
    private static final String VALUE_TXT = "value";
    private static final String RANDOM_TXT = "random";

    private final Story story;
    private int lineCount;

    private final StoryTokenizer tokenizer;

    private boolean doBuffer = false;
    private final Queue<String> lineBuffer = new LinkedList<>();

    private int stepDelayInMs = 0;

    /* Iteration addon */
    private final List<String> collectBuffer = new LinkedList<>(); // used for ifs to -- they are not to overlap!
    private final List<String> collectElseBuffer = new LinkedList<>(); // used for ifs

    private boolean inIterCollect = false;
    private boolean breakCurrentIteration = false;
    private int iterReiterationCount = 0; // count of the repetition
    private int iterHoldOff = 0; // when active used to allow nested iterations
    private int inCurrentIteration = 0;

    /* If addon */
    private boolean inIfCollect = false;
    private boolean inElseCollect = false;
    private boolean currentIfState = false;
    private int ifHoldOff = 0; // when active used to allow nested iterations
    private int inCurrentIf = 0; // this field might not be used but it is accessible by an expandable

    /* Collect addon */
    private boolean inJoinerCollect = false;
    private String currentJoiner = "";
    private String currentJoinVariable = "";

    /* Expander */
    private Expander storyExpander;
    private boolean doExpand = true;

    /* Sleepless */
    private long sleeplessThreshold = 0;

    /* Performer */
    private final Map<String, IStoryPerformer> performers = new HashMap<>();

    /* gameOver cleanup */
    private final AtomicBoolean gameRunning = new AtomicBoolean();

    /**
     * Construct a new StoryBoard with a default configuration
     * 
     * @throws IOException If the loading of the internal configuration defaults
     *                     fails.
     * 
     * @see #StoryBoard(int)
     */
    public StoryBoard() throws IOException {
        this(0);
    }

    /**
     * Construct a new StoryBoard with a default configuration
     * 
     * @param delayInMs time in ms to wait between every step
     * 
     * @throws IOException If the loading of the internal configuration defaults
     *                     fails.
     */
    public StoryBoard(final int delayInMs) throws IOException {
        this(new Story(), delayInMs);
    }

    /**
     * Construct a new StoryBoard
     *
     * @param configuration the configuration to be used
     * 
     * @see #StoryBoard(Configuration, int)
     */
    public StoryBoard(final Configuration configuration) {
        this(configuration, 0);
    }

    /**
     * Construct a new StoryBoard
     *
     * @param configuration the configuration to be used
     * @param delayInMs     time in ms to wait between every step
     */
    public StoryBoard(final Configuration configuration, final int delayInMs) {
        this(new Story(configuration), delayInMs);
    }

    /**
     * Construct a new "kickstart"-StoryBoard
     *
     * @param controller The controller that should host the story
     * @param clients    the clients that should take part
     * 
     * @see #StoryBoard(DummyNttsController, List, int)
     */
    public StoryBoard(final DummyNttsController controller, final List<DummyClient> clients) {
        this(controller, clients, 0);
    }

    /**
     * Construct a new "kickstart"-StoryBoard
     *
     * @param controller The controller that should host the story
     * @param clients    the clients that should take part
     * @param delayInMs  time in ms to wait between every step
     */
    public StoryBoard(final DummyNttsController controller, final List<DummyClient> clients, final int delayInMs) {
        this(new Story(controller, clients), delayInMs);
    }

    /**
     * Construct the StoryBoard based on a new story
     *
     * @param story the story to be used
     * 
     * @see #StoryBoard(Story, int)
     */
    public StoryBoard(final Story story) {
        this(story, 0);
    }

    /**
     * Construct the StoryBoard based on a new story
     *
     * @param story     the story to be used
     * @param delayInMs time in ms to wait between every step
     */
    public StoryBoard(final Story story, final int delayInMs) {
        this.story = story;
        lineCount = 0;
        setDelay(delayInMs);
        setupExpander();
        setupGameOver();
        tokenizer = new StoryTokenizer();
    }

    // Handle Expander

    // This method expands to the value of the given expandable-key
    private String expandToOption(final Expander expander, final String option) {
        return expander.expand("${call:" + expander.expand("${" + option + "}") + "}");
    }

    private String expandToNot(final Expander expander, final String option) {
        final String wouldBe = expander.expand("${" + expander.expand("${" + option + "}") + "}");
        switch (wouldBe) {
            case "true":
            case "1":
            case "enable":
            case "wahr":
                return "0";
            default:
                return "1";
        }
    }

    private String call2(final Expander expander, final String option) {
        return expander.expand("${call:" + expander.expand(option) + "}");
    }

    private void setupExpander() {
        final Expandables coreExpandables = new Expandables();
        coreExpandables.registerExpansion("date", Expandables::expandDate);
        coreExpandables.registerExpansion("time", Expandables::expandTime);
        coreExpandables.registerExpansion("level", Expandables::expandLevel);
        coreExpandables.registerExpansion("system", Expandables::expandSystem);
        // data access
        coreExpandables.registerReflection("story", this.story);
        coreExpandables.registerReflection("storyBoard", this);
        coreExpandables.registerReflection("matchconfig", this.story.getConfiguration().getMatchconfig());
        coreExpandables.registerExpansion("call", Expandables::expandFull);
        coreExpandables.registerExpansion("call2", (final String i1, final String option, final int i3,
                final Expander expander) -> this.call2(expander, option));
        coreExpandables.registerConstant("empty", "");

        // generic usable(s)
        coreExpandables.registerExpansion("line", (i1, i2, i3, i4) -> Integer.toString(this.lineCount));
        coreExpandables.registerExpansion("playerOneName",
                (i1, i2, i3, i4) -> story.getController().getClientManager().getPlayerOne().getClientName());
        coreExpandables.registerExpansion("playerTwoName",
                (i1, i2, i3, i4) -> story.getController().getClientManager().getPlayerTwo().getClientName());
        coreExpandables.registerExpansion("playerOneId",
                (i1, i2, i3, i4) -> story.getController().getClientManager().getPlayerOne().getClientId().toString());
        coreExpandables.registerExpansion("playerTwoId",
                (i1, i2, i3, i4) -> story.getController().getClientManager().getPlayerTwo().getClientId().toString());

        // useful expandables:
        coreExpandables.registerExpansion("#playerCharacters",
                (final String i1, final String i2, final int i3, final Expander i4) -> Long
                        .toString(this.story.getController().getMainGamePhaseController().getGameFieldController()
                                .playerCharactersInCurrentRound()));
        coreExpandables.registerExpansion("gameOver",
                (final String i1, final String i2, final int i3, final Expander i4) -> Boolean.toString(
                        this.story.getController().getMainGamePhaseController().getGameFieldController().isGameOver()));
        coreExpandables.registerExpansion("eval", (final String i1, final String option, final int i3,
                final Expander expander) -> expandToOption(expander, option));
        coreExpandables.registerExpansion("not", (final String i1, final String option, final int i3,
                final Expander expander) -> expandToNot(expander, option));
        this.storyExpander = new Expander(coreExpandables);
    }

    /**
     * Will enable expansions using the {@link Expander} (default)
     *
     * @return The modified StoryBoard for chaining
     * 
     * @see #dontExpand()
     */
    public StoryBoard doExpand() {
        this.doExpand = true;
        return this;
    }

    /**
     * Will disable expansions using the {@link Expander}
     *
     * @return The modified StoryBoard for chaining
     * 
     * @see #doExpand()
     */
    public StoryBoard dontExpand() {
        this.doExpand = false;
        return this;
    }

    // Handle GameOver

    private void setupGameOver() {
        gameRunning.set(true);
        this.story.getController().setGameOverConsumer(c -> gameRunning.set(false));
    }

    /**
     * Set the routine that is to be called if there is an game over you may not
     * need it and you probably don't even care, but if you have cleanup-stuff to do
     * if there is a global game-over shutdown, feel free to register yourself here
     * 
     * @param callback The routine the gameOver occurred on, the callback will get
     *                 the connection which initiated the gameOver. If there was
     *                 none, this will be null.
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard onGameOver(final Consumer<NttsClientConnection> callback) {
        this.story.getController().setGameOverConsumer(c -> {
            synchronized (gameRunning) {
                gameRunning.set(false);
                gameRunning.notifyAll();
            }
            callback.accept(c);
        });
        return this;
    }

    // Handle Stories

    /**
     * Changes the delay to be waited after a step (in ms). The timeout set here is
     * <i>not</i> affected by the {@link #sleepless()}-threshold.
     * <p>
     * Use 0 to disable the timeout (this is the default)
     * 
     * @param ms Positive (&gt;= 0) value of time after a step (in ms)
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard setDelay(final int ms) {
        if (ms < 0) {
            throw new StoryException("The delay between steps must be >= 0ms (was: " + ms + ")");
        }
        this.stepDelayInMs = ms;
        return this;
    }

    /**
     * Readable extension for an existing Story
     *
     * @param story the story to extend
     *
     * @return a story board
     */
    public static StoryBoard extendStory(final Story story) {
        return new StoryBoard(story);
    }

    private boolean isBlankOrComment(final String line) {
        return line == null || line.length() < 1 || line.charAt(0) == '#' || line.isBlank()
                || line.strip().startsWith("#");
    }

    private void assertNumOfArgs(final StoryChapterType type, final int n, final String line, final String fileName) {
        if (type.getNumOfArgs() != n)
            throw new StoryException("The type: " + type + " needs exactly " + type.getNumOfArgs() + " Argument"
                    + (type.getNumOfArgs() == 1 ? "" : "s") + " but" + (n < type.getNumOfArgs() ? " only" : "")
                    + " got " + n + " in line " + lineCount + " (" + fileName + "). Which was: " + line + ".\n"
                    + type.getHelp());
    }

    // Buffer the lines and do not execute them directly

    /**
     * Changes the mode of the story board to be buffered, this means lines will not
     * be executed directly, but only if enforced by calling {@link #build()} or if
     * changing the mode with {@link #unbuffered()}.
     *
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard buffered() {
        this.doBuffer = true;
        return this;
    }

    /**
     * Changes the mode of the story board to be unbuffered, which is the default.
     * This means lines will be executed directly. All buffered lines will be
     * executed _now_ so the buffer will be cleared;
     *
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard unbuffered() {
        this.doBuffer = false;
        while (!lineBuffer.isEmpty()) {
            executeStoryStep("Buffer", lineBuffer.remove());
        }
        return this;
    }

    // Sleeping

    /**
     * Sets the board to execute sleep-commands as known.
     * 
     * @return This storyboard for chaining
     * @see #sleepless()
     * @see #sleepless(long)
     */
    public StoryBoard awake() {
        this.sleeplessThreshold = 0;
        return this;
    }

    /**
     * Sets the story-board into sleepless mode, from now own all sleep commands
     * will be ignored <i>on execution</i>. This will not apply to buffered lines if
     * they are executed in {@link #awake()}-mode.
     * <p>
     * Being sleepless doesn't change the execution-speed as set by
     * {@link #StoryBoard(int)}, {@link #StoryBoard(Story, int)}, ... or
     * {@link #build(int)}.
     * 
     * @return This storyboard for chaining
     * @see #awake()
     * @see #sleepless(long)
     */
    public StoryBoard sleepless() {
        return this.sleepless(-1);
    }

    /**
     * Sets the story-board into sleepless mode, from now own all sleep commands
     * will be ignored <i>on execution</i>. This will not apply to buffered lines if
     * they are executed in {@link #awake()}-mode.
     * <p>
     * Being sleepless doesn't change the execution-speed as set by
     * {@link #StoryBoard(int)}, {@link #StoryBoard(Story, int)}, ... or
     * {@link #build(int)}.
     * 
     * This method differs from {@link #sleepless()} that you can specify the
     * maximum length to ignore, e.g. ignore all sleeps shorter than 250ms, ...
     * 
     * @param threshold Maximum time to ignore in ms. The maximum is inclusive.
     * 
     * @return This storyboard for chaining
     * @see #sleepless()
     * @see #awake()
     */
    public StoryBoard sleepless(final long threshold) {
        this.sleeplessThreshold = threshold;
        return this;
    }

    // Performers

    /**
     * Add an performer to be called if there is an performable event for the
     * client.
     * <p>
     * <i>Important:</i> There can be only one performer for a client name at a
     * time, this method will overwrite a previous one, if it existed.
     * 
     * @param clientName The client name to trigger for
     * @param performer  The performer to be called
     * @return This storyboard for chaining
     */
    public StoryBoard addPerformer(final String clientName, final IStoryPerformer performer) {
        this.performers.put(Objects.requireNonNull(clientName), Objects.requireNonNull(performer));
        return this;
    }

    /**
     * Remove an performer for a client, if there is none for this name, this will
     * throw a regular {@link StoryException}. This should avoid any logic errors in
     * de-registering Performers. Please keep in mind, that a performer will be
     * called on execution time, and is not to be considered if the story is
     * buffered.
     * 
     * @param clientName The client name to remove the performer for
     * @return This storyboard for chaining
     */
    public StoryBoard removePerformer(final String clientName) {
        if (this.performers.remove(clientName) == null) {
            throw new StoryException("The performer to be removed for '" + clientName + "' was not present");
        }
        return this;
    }

    // Direct Story-Actions

    /**
     * Just a readable way of register a new client in java. This will, as all other
     * commands, respect buffering, iterations,...
     * 
     * @param name Name of the client
     * @param role role of the client
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard hello(final String name, final RoleEnum role) {
        return execute("Hello", StoryLineProducer.hello(name, role));
    }

    /**
     * Just a readable way of sending a dos-attack in java. This will, as all other
     * commands, respect buffering, iterations,...
     * 
     * @param name        Name of the client, can be random
     * @param messageType The type of the Message, for random use
     * @param amount      Number of Attacks
     * @param delay       Number of ms between two attacks
     * 
     * @return The modified StoryBoard for chaining
     * @see #dos(String, int, int)
     */
    public StoryBoard dos(final String name, final MessageTypeEnum messageType, final int amount, final int delay) {
        return execute("Dos", StoryLineProducer.dos(name, messageType, amount, delay));
    }

    /**
     * Just a readable way of sending a random-type dos-attack in java. This will,
     * as all other commands, respect buffering, iterations,...
     * 
     * @param name   Name of the client, can be random
     * @param amount Number of Attacks
     * @param delay  Number of ms between two attacks
     * 
     * @return The modified StoryBoard for chaining
     * @see #dos(String, MessageTypeEnum, int, int)
     */
    public StoryBoard dos(final String name, final int amount, final int delay) {
        return execute("Dos", StoryLineProducer.dos(name, amount, delay));
    }

    /**
     * Just a readable way to allow errors (execution time)
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard allowErrors() {
        return execute("AllowErrors", StoryLineProducer.allowErrors());
    }

    /**
     * Just a readable way to forbid errors (execution time). This is the default
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard forbidErrors() {
        return execute("ForbidErrors", StoryLineProducer.forbidErrors());
    }

    /**
     * Perform an inject routine
     * 
     * @param injectType Type of the inject
     * @param target     The target for the inject
     * @param value      The value to inject for the given target
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard inject(final Injects injectType, final String target, final String value) {
        return execute("Inject", StoryLineProducer.inject(injectType, target, value));
    }

    /**
     * Execute a preset
     *
     * @param name the name of the preset you desire
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard preset(final String name) {
        return execute("Preset", StoryLineProducer.preset(name));
    }

    /**
     * Execute a preset
     *
     * @param name the name of the preset you desire
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard preset(final Presets name) {
        return execute("Preset", StoryLineProducer.preset(name));
    }

    /**
     * Let a client crash
     *
     * @param name the name of the client to crash
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard crash(final String name) {
        return execute("Crash", StoryLineProducer.crash(name));
    }

    /**
     * Let a client reconnect
     *
     * @param oldName The name of the client that shall reconnect
     * @param newName The name the story shall refer to the new client that
     *                reconnects
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard reconnect(final String oldName, final String newName) {
        return execute("Reconnect", StoryLineProducer.reconnect(oldName, newName));
    }

    /**
     * Let a client request a pause
     *
     * @param name The name of the client that wants the pause
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard pause(final String name) {
        return execute("Pause", StoryLineProducer.pause(name));
    }

    /**
     * Let a client resume
     *
     * @param name The name of the client that wants to resume
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard resume(final String name) {
        return execute("Resume", StoryLineProducer.resume(name));
    }

    /**
     * Let a client send the retire operation
     *
     * @param name The name of the client that wants to retire
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard retire(final String name) {
        return execute("Retire", StoryLineProducer.retire(name));
    }

    /**
     * Let the currently active client send the retire operation
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard retire() {
        return execute("Retire (random)", StoryLineProducer.retire());
    }

    /**
     * Let a client move
     *
     * @param name   The name of the client that wants to move
     * @param target The target of the movement operation
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard move(final String name, final Point target) {
        return execute("Move", StoryLineProducer.move(name, target));
    }

    /**
     * Let a client move
     *
     * @param name   The name of the client that wants to move
     * @param target The relative target of the movement operation
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard moveRelative(final String name, final Point target) {
        return execute("Move (relative)", StoryLineProducer.moveRelative(name, target));
    }

    /**
     * Let a client gamble
     *
     * @param name   The name of the client that wants to gamble
     * @param target The target of the movement operation
     * @param stake  The amount of chips to place
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard gamble(final String name, final Point target, final int stake) {
        return execute("Gamble", StoryLineProducer.gamble(name, target, stake));
    }

    /**
     * Let a client send a meta-information request
     *
     * @param name The name of the client that wants to request the meta information
     * @param keys The wanted keys
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard meta(final String name, final MetaKeyEnum... keys) {
        return execute("Meta", StoryLineProducer.meta(name, keys));
    }

    /**
     * Just sleep for a certain time (on execution, if not sleepless)
     *
     * @param lengthInMs Time to sleep in ms
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard sleep(final int lengthInMs) {
        return execute("Sleep", StoryLineProducer.sleep(lengthInMs));
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard set(final String variable, final String value) {
        return execute("Set (string)", StoryLineProducer.set(variable, value));
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard set(final String variable, final int value) {
        return execute("Set (int)", StoryLineProducer.set(variable, value));
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * 
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard set(final String variable, final boolean value) {
        return execute("Set (boolean)", StoryLineProducer.set(variable, value));
    }

    // Story Builder steps:

    /**
     * Execute a single line for the story
     *
     * @param line The line to be executed
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard execute(final String line) {
        lineCount = 0;
        executeStoryStep("", line);
        return this;
    }

    /**
     * Execute a single line for the story
     *
     * @param type      The type of the action to execute
     * @param arguments The arguments for the command, will be escaped as needed
     * @return The modified StoryBoard for chaining
     * @see #execute(String, StoryChapterType, String...)
     */
    public StoryBoard execute(final StoryChapterType type, final String... arguments) {
        return execute("", type, arguments);
    }

    /**
     * Execute a single line for the story
     *
     * @param name Name for debug reference
     * @param line The line that shall be executed, probably produced by the
     *             {@link StoryLineProducer}
     * 
     * @return The modified StoryBoard for chaining
     * 
     * @since 1.1
     */
    public StoryBoard execute(final String name, final StoryLine line) {
        lineCount = 0;
        executeStoryStep(name, line.buildCommand());
        return this;
    }

    /**
     * Execute a single line for the story
     *
     * @param name      Name for debug reference
     * @param type      The type of the action to execute
     * @param arguments The arguments for the command, will be escaped as needed
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard execute(final String name, final StoryChapterType type, final String... arguments) {
        lineCount = 0;
        executeStoryStep(name, StoryLine.buildCommand(type, arguments));
        return this;
    }

    /**
     * Execute an array of lines for the story
     *
     * @param lines the lines to be executed
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard execute(final String... lines) {
        lineCount = 0;
        for (final String line : lines) {
            executeStoryStep("", line);
        }
        return this;
    }

    /**
     * Executes an internal File
     *
     * @param path the path to the file desired
     *
     * @return The modified StoryBoard for chaining
     * 
     * @throws IOException If the reading fails
     *                     ({@link InternalResources#getFileInputStream(String)})
     * 
     * @see #execute(InputStream, String)
     */
    public StoryBoard executeInternalFile(final String path) throws IOException {
        return execute(
                Objects.requireNonNull(InternalResources.getFileInputStream(path), "File not found: '" + path + "'"),
                "File: " + path);
    }

    /**
     * Execute an lines from an inputStream
     *
     * @param is the stream to read from
     * @return The modified StoryBoard for chaining
     * 
     * @see #execute(InputStream, String)
     */
    public StoryBoard execute(final InputStream is) {
        return execute(is, "InputStream: No name provided");
    }

    /**
     * Execute all lines from an inputStream
     *
     * @param is   the stream to read from
     * @param name the name for the Stream
     *
     * @return The modified StoryBoard for chaining
     * @see #execute(Stream, String)
     */
    public StoryBoard execute(final InputStream is, final String name) {
        final InputStreamReader isReader = new InputStreamReader(
                Objects.requireNonNull(is, "Stream invalid for: " + name));
        final Stream<String> lines = new BufferedReader(isReader).lines();
        return execute(lines, name);
    }

    /**
     * Execute a stream of lines
     *
     * @param lines lines to compute
     *
     * @return The modified StoryBoard for chaining
     * 
     * @see #execute(Stream, String)
     */
    public StoryBoard execute(final Stream<String> lines) {
        return execute(lines, "Stream: No name provided");
    }

    /**
     * Execute a stream of lines
     *
     * @param lines lines to compute
     * @param name  the name of the stream
     *
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard execute(final Stream<String> lines, final String name) {
        lineCount = 0;
        lines.sequential().forEach(l -> executeStoryStep(name, l));
        return this;
    }

    /**
     * Appends the client to the story
     *
     * @param client the desired client
     *
     * @return The modified StoryBoard for chaining
     */
    public StoryBoard addClient(final DummyClient client) {
        story.addClient(client);
        return this;
    }

    /**
     * Returns the Story out of all chained lines. In contrast to the meaning of
     * 'build' this just is a break in the story. If you keep the storyBoard you can
     * extend with the 'execute'-calls
     *
     * @return The current Story-State
     */
    public Story build() {
        final boolean bufferSave = this.doBuffer;
        this.doBuffer = false;
        while (!lineBuffer.isEmpty()) {
            executeStoryStep("Buffer", lineBuffer.remove());
        }
        this.doBuffer = bufferSave;
        return this.story;
    }

    /**
     * Returns the Story out of all chained lines. In contrast to the meaning of
     * 'build' this just is a break in the story. If you keep the storyBoard you can
     * extend with the 'execute'-calls
     *
     * @param delayInMs Timeout be waited just for this build step. The time is only
     *                  of interest if you use a {@link #buffered()} Builder and
     *                  gets reset afterwards. Please note, that the
     *                  timeout-modification will be performed even if the Board is
     *                  NOT buffered (for consistency reasons). This just means you
     *                  will get an {@link StoryException} if you supply a negative
     *                  value, even though it might have no effect after all.
     *
     * @return The current Story-State
     */
    public Story build(final int delayInMs) {
        if (!doBuffer)
            magpie.writeWarning("Calling build with another delay, but Story isn't buffered.", STORY_TXT);
        final int oldDelay = delayInMs;
        setDelay(delayInMs);
        final Story createdStory = build();
        setDelay(oldDelay);
        return createdStory;
    }

    private void executeStoryStep(final String fileName, final String line) {
        if (doBuffer) { // if we buffer we do not execute Story-steps
            magpie.writeDebug("Passed to Buffer: " + line, STORY_TXT);
            lineBuffer.add(line);
            return;
        }

        lineCount++;
        if (isBlankOrComment(line)) {
            if (fileName.isEmpty()) {
                magpie.writeDebug("Skipping line " + line + ", as it is comment/empty.", STORY_TXT);
            } else {
                magpie.writeDebug("Skipping line no " + lineCount + " in (" + fileName
                        + ") as it appears to be comment/empty (" + line + ")", STORY_TXT);
            }
            return;
        }
        try {
            magpie.writeDebug("Executing: " + line + ". In: " + fileName + ". Delay: " + stepDelayInMs + ". Escalate: "
                    + story.canEscalate(), STORY_TXT);
            processSingleStoryStep(fileName, line);
            if (stepDelayInMs > 0) { // JMP
                Thread.sleep(stepDelayInMs);
            }
        } catch (IllegalArgumentException | InterruptedException ex) {
            magpie.writeException(ex, STORY_TXT);
            Thread.currentThread().interrupt();
            throw new StoryException("The desired Action in line '" + (doExpand ? storyExpander.expand(line) : line)
                    + "' (" + line + ") isn't valid! Please choose one of: "
                    + Arrays.toString(StoryChapterType.values()) + ". Failed for: " + ex.getMessage() + " ("
                    + ex.getClass() + "). " + fileName + "; Line: " + lineCount);
        }
    }

    /**
     * Inject configuration for a target player (denoted by name) or the server
     * (denoted by '<server>')
     * 
     * @param key    Key to run the injection on, isn't really limited but more of a
     *               helper
     * @param target The target for the config-injection, only important for some
     *               operations
     * @param value  The value to pass on
     * @throws IOException
     */
    private void injectConfig(final String key, final String target, final String value) throws IOException {
        switch (key) {
            case "NEXT-PROPOSAL":
                injectNextProposal(value, target);
                return;
            case "RANDOM-RESULT":
                story.getController().getRandomController().injectRandomResult(value, target);
                return;
            case "SAFE-ORDER":
                injectSafeEnumerationOrder(value, target);
                return;
            case "NPC-PICK":
                injectNpcEquipment(value, target);
                return;
            case "START-POSITIONS":
                injectStartPositions(value, target);
                return;
            case "NEXT-ROUND-ORDER":
                injectNextRoundOrder(value, target);
                break;
            case "MAJOR":
                injectMajor(value, target);
                return;
            case "SCENARIO":
                injectScenario(value, target);
                break;
            case "MATCHCONFIG":
                injectMatchconfig(value, target);
                break;
            case "CHARACTERS":
                injectCharacters(value, target);
                break;
            case "SERVER-CONFIG":
                injectServerConfig(value, target);
                break;
            default:
                throw new StoryException(
                        "The key '" + key + "' is unknown for config injections. (supplied with value: '" + value
                                + "'). Happened in line: " + lineCount);
        }
    }

    private void injectNextProposal(final String value, final String target) {
        // expected Pattern: list (comma separated) of 6 valid gadget/character names,
        // therefore characters should not be named as gadgets for this injection
        final String[] names = value.split(SPACE_COMMA_SPLIT);
        final DraftingProposal proposal = new DraftingProposal();
        for (final String name : names) {
            final ImmutablePair<UUID, GadgetEnum> data = story.decodeGadgetCharacter(name);
            if (data.getValue() != null) {
                proposal.addGadget(data.getValue());
            } else if (data.getKey() != null) {
                proposal.addCharacter(data.getKey());
            }
        }
        // enforce it:
        story.getController().getDraftingPhaseController().enforceNextProposal(proposal, target);
    }

    private void injectMajor(final String value, final String target) {
        switch (target.toUpperCase()) {
            case "TIMEOUT-MULTIPLIER":
                try {
                    final int iVal = Integer.parseInt(value);
                    story.getConfiguration().changeMultiplier(iVal);
                } catch (final NumberFormatException ex) {
                    throw new StoryException(
                            "Key for the TIMEOUT-MULTIPLIER has to be a valid integer. " + value + " is not.");
                }
                break;
            case "MAX-STRIKES":
                try {
                    final int iVal = Integer.parseInt(value);
                    story.getConfiguration().getMatchconfig().setMaxStrikes(iVal);
                } catch (final NumberFormatException ex) {
                    throw new StoryException(
                            "Key for the MAX-STRIKES has to be a valid integer. " + value + " is not.");
                }
                break;
            default:
                throw new StoryException(
                        "The target '" + target + "' is unknown for major config injections. (supplied with value: '"
                                + value + "'). Happened in line: " + lineCount);
        }
    }

    private void injectScenario(final String value, final String target) throws IOException {
        final Configuration configuration = story.getConfiguration();
        Scenario scenario = null;
        switch (target.toUpperCase()) {
            case INTERNAL:
                scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson(value), Scenario.class);
                break;
            case RAW_JSON:
                scenario = GameDataGson.fromJson(value, Scenario.class);
                break;
            default:
                throw new StoryException(target + " is currently not allowed to be used for the scenario-loader");
        }
        if (scenario != null) {
            configuration._unlock();
            story.getConfiguration().setScenario(scenario);
            configuration._lock();
        } else {
            throw new StoryException("The scenario you requested in line " + lineCount + " was null");
        }
    }

    private void injectMatchconfig(final String value, final String target) throws IOException {
        final Configuration configuration = story.getConfiguration();
        Matchconfig matchconfig = null;
        switch (target.toUpperCase()) {
            case INTERNAL:
                matchconfig = GameDataGson.fromJson(GameDataGson.loadInternalJson(value), Matchconfig.class);
                break;
            case RAW_JSON:
                matchconfig = GameDataGson.fromJson(value, Matchconfig.class);
                break;
            default:
                throw new StoryException(target + " is currently not allowed to be used for the matchconfig-loader");
        }
        if (matchconfig != null) {
            configuration._unlock();
            story.getConfiguration().setMatchconfig(matchconfig);
            configuration._lock();
            // UPDATE the expander :)
            storyExpander.getOwned().updateReflection("matchconfig", this.story.getConfiguration().getMatchconfig());
        } else {
            throw new StoryException("The matchconfig you requested in line " + lineCount + " was null");
        }
    }

    private void injectCharacters(final String value, final String target) throws IOException {
        final Configuration configuration = story.getConfiguration();
        CharacterInformation[] characters = null; // use same uuid too!
        CharacterDescription[] internalCharacters = null; // not with internals
        switch (target.toUpperCase()) {
            case INTERNAL:
                internalCharacters = GameDataGson.fromJson(GameDataGson.loadInternalJson(value),
                        CharacterDescription[].class);
                break;
            case RAW_JSON:
                characters = GameDataGson.fromJson(value, CharacterInformation[].class);
                break;
            default:
                throw new StoryException(target + " is currently not allowed to be used for the characters-loader");
        }
        if (internalCharacters != null) {
            configuration._unlock();
            story.getConfiguration().populateCharacters(internalCharacters); // direct? why? xD
            configuration._lock();
        } else if (characters != null) {
            configuration._unlock();
            story.getConfiguration().setCharacters(characters);
            configuration._lock();
        } else {
            throw new StoryException("The characters you requested in line " + lineCount + " were null");
        }
    }

    private void injectServerConfig(final String value, final String target) throws IOException {
        final Configuration configuration = story.getConfiguration();
        ServerConfiguration serverConfiguration;
        switch (target.toUpperCase()) {
            case INTERNAL:
                serverConfiguration = GameDataGson.fromJson(GameDataGson.loadInternalJson(value),
                        ServerConfiguration.class);
                break;
            case RAW_JSON:
                serverConfiguration = GameDataGson.fromJson(value, ServerConfiguration.class);
                break;
            default:
                throw new StoryException(target + " is currently not allowed to be used for the characters-loader");
        }
        if (serverConfiguration != null) {
            configuration._unlock(); // just to be sure
            story.getConfiguration().setServerConfiguration(serverConfiguration); // direct? why? xD
            configuration._lock();
        } else {
            throw new StoryException("The server-config you requested in line " + lineCount + " were null");
        }
    }

    private void injectSafeEnumerationOrder(final String value, final String target) {
        // expected Pattern: list (comma separated) of all numbers, safe will be
        // traversed in y down x right order.
        if (!target.equalsIgnoreCase(VALUE_TXT)) {
            throw new StoryException(
                    "To inject the safe-enumeration order the target 'value' must be passed, not: " + target);
        }
        final String[] names = value.split(SPACE_COMMA_SPLIT);
        final int[] targetInts = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            try {
                targetInts[i] = Integer.parseInt(names[i]);
            } catch (final NumberFormatException ex) {
                throw new StoryException("To inject the safe-enumeration the value passed for index: " + i
                        + " is no valid number (" + names[i] + "). Parsed for now: " + Arrays.toString(targetInts));
            }
        }
        // enforce it:
        story.getController().getMainGamePhaseController().injectSafeConstructionOrder(targetInts);
    }

    // includes the choosing of the npc(s) as well
    private void injectNpcEquipment(final String value, final String target) {
        // expected Pattern: list (comma separated) of all numbers, safe will be
        // traversed in y down x right order.
        if (!target.equalsIgnoreCase(VALUE_TXT)) {
            throw new StoryException("To inject the npc equipments the target 'value' must be passed, not: " + target);
        }
        final List<Pair<UUID, List<GadgetEnum>>> translation = story.translateTokens(value.split(SPACE_COMMA_SPLIT));
        // enforce it:
        story.getController().getMainGamePhaseController().injectNpcPick(translation);
    }

    private void injectStartPositions(final String value, final String target) {
        // expected Pattern: list (comma separated) of all numbers, safe will be
        // traversed in y down x right order.
        if (!target.equalsIgnoreCase(VALUE_TXT)) {
            throw new StoryException("To inject the start positions the target 'value' must be passed, not: " + target);
        }
        final String[] values = value.split(SPACE_COMMA_SPLIT);
        if (values.length % 2 != 0) {
            throw new StoryException(
                    "The parameters to inject must be equal for injecting the start position. Fails for: " + value);
        }

        final Map<String, Point> targetMap = new HashMap<>();

        for (int i = 0; i < values.length; i += 2) {
            final String[] pointData = values[i + 1].split("/");
            if (pointData.length != 2) {
                throw new StoryException("The point-data on index: " + (i + 1) + " is faulty in: " + value
                        + " as not valid format '<1>/<2>'");
            }
            try {
                final Point goal = new Point(Integer.parseInt(pointData[0]), Integer.parseInt(pointData[1]));
                targetMap.put(values[i], goal);
            } catch (final NumberFormatException ex) {
                throw new StoryException("The point-data on index: " + (i + 1) + " is faulty in: " + value
                        + " as not valid (integer) format '<1>/<2>'");
            }
        }
        // enforce it:
        story.getController().getMainGamePhaseController().injectCharacterPlacement(targetMap);
    }

    private void injectNextRoundOrder(final String value, final String target) {
        if (!target.equalsIgnoreCase(VALUE_TXT)) {
            throw new StoryException("To inject the start positions the target 'value' must be passed, not: " + target);
        }
        // just split by names and pass to injection protocol
        final String[] names = value.split(SPACE_COMMA_SPLIT);
        story.getController().getMainGamePhaseController().injectNextRoundOrder(List.of(names));
    }

    private Story processSingleStoryStep(final String fileName, final String line) throws InterruptedException {
        if (!gameRunning.get()) {
            magpie.writeError(
                    "The game has ended already and there are no further messages to send right now. Stopping: "
                            + fileName + ", Line: " + line + " (" + lineCount + ").",
                    "Process");
            // throw new StoryHasEndedException("The game has ended already and there are no
            // further messages to send right now. Stopping: "+ fileName + ", Line: " + line
            // + " (" + lineCount + ").")
        }
        final String expandedLine = doExpand ? storyExpander.expand(line) : line; // use expansion
        final String[] tokens = tokenizer.tokenize(expandedLine);
        if (tokens.length == 0) {
            throw new StoryException(
                    "The line seems to have slipped through the checks and has no tokens. Happened in line: "
                            + lineCount);
        }

        StoryChapterType type = null;
        try {
            type = StoryChapterType.valueOf(tokens[0].toUpperCase());
        } catch (final IllegalArgumentException ex) {
            // line doesn't start with a valid token
            magpie.writeWarning("Line: " + line + " does not start with valid story command -- getting suspicious",
                    STORY_TXT);
        }
        if (readBufferScopes(type, fileName, line)) {
            return this.story;
        }
        // we don't want a null type in the main parse part
        if (type == null) {
            throw new IllegalArgumentException("Type: " + type + " from line: " + line + " not valid");
        }

        // Ensure array-access
        assertNumOfArgs(type, tokens.length - 1, expandedLine, fileName);

        return executeSingleStoryStepPerformerWithGuard(type, line, fileName, tokens);
    }

    // true if escape, false if hold
    private boolean readBufferScopes(final StoryChapterType type, final String fileName, final String line) {
        if (inIterCollect) {
            return readBufferIter(type, fileName, line);
        } else if (inIfCollect) {
            return readBufferIf(type, fileName, line);
        } else if (inJoinerCollect) {
            return readBufferJoiner(type, line);
        }
        return false;
    }

    private boolean readBufferJoiner(final StoryChapterType type, final String line) {
        final boolean stillBuffer = type != StoryChapterType.COLLECT_END;
        if (stillBuffer) {
            collectBuffer.add(line);
        }
        return stillBuffer;
    }

    private boolean readBufferIter(final StoryChapterType type, final String fileName, final String line) {
        boolean stillBuffer = true;
        switch (type) {
            case RETI:
                // end and execute iteration
                iterHoldOff -= 1;
                stillBuffer = iterHoldOff > 0;
                break;
            case ITER:
                iterHoldOff += 1;
                break;
            case IF:
            case IF_NOT:
                ifHoldOff += 1;
                break;
            case FI:
                ifHoldOff -= 1;
                break;
            default:
                break;
        }
        if (ifHoldOff < 0) {
            throw new StoryException("Unbalanced IF-FI block in: " + line + FOR_TXT + lineCount + " in " + fileName);
        }

        // Buffer the line and continue
        if (stillBuffer) {
            collectBuffer.add(line);
            return true;
        } else if (ifHoldOff > 0) {
            throw new StoryException("Unbalanced IF-FI block in: " + line + FOR_TXT + lineCount + " in " + fileName);
        }
        return false;
    }

    private boolean readBufferIf(final StoryChapterType type, final String fileName, final String line) {
        boolean stillBuffer = true;
        switch (type) {
            case RETI:
                // end and execute iteration
                iterHoldOff -= 1;
                break;
            case ITER:
                iterHoldOff += 1;
                break;
            case IF:
                ifHoldOff += 1;
                break;
            case ELSE:
                inElseCollect = ifHoldOff == 1; // only if in the current if scope and valid
                // shall not be recorded if used for break:
                if (inElseCollect) {
                    return true;
                } else {
                    break;
                }
            case FI:
                ifHoldOff -= 1;
                stillBuffer = ifHoldOff > 0;
                break;
            default:
                break;
        }
        if (iterHoldOff < 0) {
            throw new StoryException(
                    "Unbalanced ITER-RETI block in: " + line + FOR_TXT + lineCount + " in " + fileName);
        }

        // Buffer the line and continue
        if (stillBuffer) {
            if (inElseCollect) { // for else
                collectElseBuffer.add(line);
            } else { // normal
                collectBuffer.add(line);
            }
            return true;
        } else if (iterHoldOff > 0) {
            throw new StoryException(
                    "Unbalanced ITER-RETI block in: " + line + FOR_TXT + lineCount + " in " + fileName);
        }
        return false;
    }

    private Story executeSingleStoryStepPerformerWithGuard(final StoryChapterType type, final String line,
            final String fileName, final String[] tokens) throws InterruptedException {

        if (type.canBePerformed()) { // should be performable
            if (type.getNumOfArgs() > 0) { // should have enough args
                if (this.performers.containsKey(tokens[1])) { // is there a performer ?
                    magpie.writeDebug("Calling performer for '" + tokens[1] + "' in line: '" + line + "'", STORY_TXT);
                    final boolean feedback = this.performers.get(tokens[1]).perform(
                            new StoryLineData(lineCount, fileName, line, this.storyExpander),
                            story.getClient(tokens[1]), type, tokens);
                    if (feedback) {// all operations have been performed
                        return this.story;
                    }
                }
            } else {
                throw new ThisShouldNotHappenException("There should be no performable operation with no arguments");
            }
        }

        // may be same, we don't care, used for let
        final String[] unexpandedTokens = doExpand ? tokenizer.tokenize(line) : tokens;

        return executeSingleStoryStepHelper(type, tokens, unexpandedTokens);
    }

    // Uses the type and doesn't guard
    private Story executeSingleStoryStepHelper(final StoryChapterType type, final String[] tokens,
            final String[] unexpandedTokens) throws InterruptedException {
        switch (type) {
            case HELLO:
                return story.newClientHello(tokens[1], RoleEnum.valueOf(tokens[2].toUpperCase()));
            case SLEEP:
                doSleep(tokens[1]);
                return story;
            case META:
                return story.requestMetaInformation(tokens[1], tokens[2].split(SPACE_COMMA_SPLIT));
            case PAUSE:
                return story.requestGamePause(tokens[1]);
            case RESUME:
                return story.resumeGamePause(tokens[1]);
            case TIMEOUT:
                return story.simulateTimeoutFor(tokens[1]);
            case ITEM:
                return processItem(tokens);
            case EQUIP:
                if (tokens[2].equals(RANDOM_TXT)) {
                    return story.sendEquipmentChoice(tokens[1]);
                } else {
                    final List<Pair<UUID, List<GadgetEnum>>> translation = story
                            .translateTokens(tokens[2].split(SPACE_COMMA_SPLIT));
                    return story.sendEquipmentChoice(tokens[1], translation);
                }
            case CRASH:
                return story.simulateCrashFor(tokens[1]);
            case RECONNECT:
                return story.simulateReconnect(tokens[1], tokens[2]);
            case DELETE:
                return story.deleteClient(tokens[1]);
            case RENAME:
                return story.renameClient(tokens[1], tokens[2]);
            case LEAVE:
                return story.leaveTheGame(tokens[1]);
            case OPERATION:
                return story.sendOperation(tokens[1], tokens[2], tokens[3]);
            case ITER:
                inIterCollect = true;
                iterHoldOff = 1;
                iterReiterationCount = Integer.parseInt(tokens[1].replace(":", ""));
                return this.story;
            case RETI:
                // Start executing buffer if there is one
                doReti();
                return this.story;
            case BREAK_EQ:
                if (inCurrentIteration <= 0) {
                    throw new StoryException("Tried to break outside of iteration in: " + Arrays.toString(tokens));
                }
                breakCurrentIteration = Objects.equals(tokens[1], tokens[2]);
                return this.story;
            case SET:
                this.storyExpander.getOwned().assignConstant(tokens[1], tokens[2]);
                return this.story;
            case LET:
                this.storyExpander.getOwned().assignConstant(tokens[1], unexpandedTokens[2]);
                return this.story;
            case CALL:
                return callLetFunc(tokens);
            case UNSET:
                this.storyExpander.getOwned().removeExpandable(tokens[1]);
                return this.story;
            case ASSURE_DEFAULT:
                if (this.storyExpander.getOwned().registerConstant(tokens[1], tokens[2])) {
                    magpie.writeWarning("You should set the field '" + tokens[1] + "' using 'SET " + tokens[1]
                            + " <value>'. It now uses the default value: " + tokens[2], STORY_TXT);
                }
                return this.story;
            case PRESET:
                doPreset(tokens[1]);
                return this.story;
            case DOS:
                final int times = Integer.parseInt(tokens[3]);
                final int delay = Integer.parseInt(tokens[4]);
                return story.sendDosAttack(tokens[1], tokens[2], times, delay);
            case PRINT:
                System.out.println(tokens[1]);
                return this.story;
            case ALLOW_ERRORS:
                return story.allowExceptions();
            case FORBID_ERRORS:
                return story.forbidExceptions();
            case CONFIG_INJECT:
                return processInject(tokens);
            case SERVER_CONFIG:
                story.getConfiguration().getServerConfig().xOptionInject(tokens[1], tokens[2]);
                return story;
            /* Math baby */
            case ADD:
                return computeMath(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a + b);
            case SUB:
                return computeMath(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a - b);
            case MUL:
                return computeMath(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a * b);
            case DIV:
                return computeMath(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a / b);
            case MOD:
                return computeMath(tokens, tokens[1], tokens[2], tokens[3], Math::floorMod);
            /* Special Sets */
            case SET_EQ:
                return computeStringSet(tokens, tokens[1], tokens[2], tokens[3], Objects::equals);
            case SET_NEQ:
                return computeStringSet(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> !Objects.equals(a, b));
            case SET_GEQ:
                return computeSet(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a >= b);
            case SET_LEQ:
                return computeSet(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a <= b);
            case SET_GT:
                return computeSet(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a > b);
            case SET_LT:
                return computeSet(tokens, tokens[1], tokens[2], tokens[3], (a, b) -> a < b);
            case SET_PRESENT:
                execute(StoryChapterType.SET, tokens[2],
                        storyExpander.getOwned().getServants().containsKey(tokens[1]) ? "1" : "0");
                return this.story;
            /** conditionals */
            case IF:
                return performIf(isTrue(tokens[1]));
            case IF_NOT:
                return performIf(!isTrue(tokens[1]));
            case FI:
                // Start executing buffer if there is one
                doFi();
                return this.story;
            /* collect */
            case COLLECT_START:
                this.inJoinerCollect = true;
                this.currentJoiner = tokens[1];
                this.currentJoinVariable = tokens[2];
                return this.story;
            case COLLECT_END:
                doJoinCollect();
                return this.story;
            default:
            case ELSE: // <- shall not appear
                break;
        }

        throw new StoryException("The type: " + type + " has no story-event attached in Line: " + lineCount
                + ". Tokens were: " + Arrays.toString(tokens) + "." + type.getHelp());
    }

    private Story processInject(final String[] tokens) {
        // target a player so probably make 2-3 options
        try {
            injectConfig(tokens[1].toUpperCase(), tokens[2], tokens[3]);
        } catch (final IOException ex) {
            magpie.writeException(ex, STORY_TXT);
            if (story.canEscalate())
                throw new StoryException(ex.getMessage());
        }
        return story;
    }

    private Story processItem(final String[] tokens) {
        switch (tokens[2]) {
            case RANDOM_TXT:
                return story.sendItemChoice(tokens[1], true, true);
            case "random-gadget":
                return story.sendItemChoice(tokens[1], true, false);
            case "random-character":
                return story.sendItemChoice(tokens[1], false, true);
            default:
                final ImmutablePair<UUID, GadgetEnum> data = story.decodeGadgetCharacter(tokens[2]);
                return story.sendItemChoice(tokens[1], data.getValue(), data.getKey());
        }
    }

    private Story callLetFunc(final String[] tokens) {
        // First: we will expand the key once so we are able to get the source-code
        final String name = (tokens[1].startsWith("${") && tokens[1].endsWith("}")) ? tokens[1]
                : "${" + tokens[1] + "}";
        final String functionSource = expand(name);
        // Note: we might want to add this set to the tokenizer so it is not locked to
        // this string
        final String[] data = functionSource.split("\\\\n");

        for (int i = 0; i < data.length; i++) {
            // execute me baby
            executeStoryStep("Line: " + i + ", in function: " + tokens[1], data[i]);
        }
        return this.story;
    }

    private Story computeMath(final String[] tokens, final String a, final String b, final String c,
            final IntBinaryOperator math) {
        try {
            final int iA = Integer.parseInt(a);
            final int iB = Integer.parseInt(b);
            final int result = math.applyAsInt(iA, iB);
            execute(StoryChapterType.SET, c, String.valueOf(result));
        } catch (final NumberFormatException ex) {
            throw new StoryException("Error when computing math in line: " + lineCount + " with tokens: "
                    + Arrays.toString(tokens) + " because of: " + ex.getMessage());
        }
        return this.story;
    }

    private Story computeSet(final String[] tokens, final String a, final String b, final String c,
            final BiPredicate<Integer, Integer> pred) {
        try {
            final int iA = Integer.parseInt(a);
            final int iB = Integer.parseInt(b);
            final boolean result = pred.test(iA, iB);
            execute(StoryChapterType.SET, c, result ? "1" : "0");
        } catch (final NumberFormatException ex) {
            throw new StoryException("Error when computing set-eq in line: " + lineCount + " with tokens: "
                    + Arrays.toString(tokens) + " because of: " + ex.getMessage());
        }
        return this.story;
    }

    private Story computeStringSet(final String[] tokens, final String a, final String b, final String c,
            final BiPredicate<String, String> pred) {
        try {
            final boolean result = pred.test(a, b);
            execute(StoryChapterType.SET, c, result ? "1" : "0");
        } catch (final NumberFormatException ex) {
            throw new StoryException("Error when computing string set-eq in line: " + lineCount + " using the tokens: "
                    + Arrays.toString(tokens) + " error message is: " + ex.getMessage());
        }
        return this.story;
    }

    private static final Set<String> TRUTH_VALUES = Set.of("true", "1", "wahr", "enable");

    private Story performIf(final boolean value) {
        currentIfState = value;
        inIfCollect = true;
        ifHoldOff = 1;
        return this.story;
    }

    /**
     * Will validate if the given txt is a True-Value.
     * 
     * @param txt The text to check.
     * 
     * @return True if the text is a truth-value, false otherwise.
     */
    public boolean isTrue(final String txt) {
        return TRUTH_VALUES.contains(txt.replace(":", ""));
    }

    private void doSleep(final String length) throws InterruptedException {
        if (sleeplessThreshold >= 0) { // there are sleeps to cover
            final long threshold = Integer.parseInt(length);
            if (threshold < 0) {
                throw new StoryException(
                        "Sleeping-Time has to be positive! The value you supplied: " + threshold + " isn't.");
            }
            if (threshold > sleeplessThreshold) {
                Thread.sleep(threshold);
            }
        }
    }

    private void doPreset(String name) {
        name = name.toLowerCase();
        if (!name.matches("[a-z0-9-]+"))
            throw new StoryException(
                    "The preset-name '" + name + "' has to match the pattern: '[a-z0-9-]+' which it does not.");
        try {
            executeInternalFile("story-presets/" + name + ".story-preset");
        } catch (NullPointerException | IOException ex) {
            throw new StoryException(
                    "Preset '" + name + "' not found. (" + ex.getClass() + " : " + ex.getMessage() + ")");
        }
    }

    private void doJoinCollect() {
        inJoinerCollect = false;
        final int repetitionStart = lineCount;
        // local iteration buffer
        final String finalLine = collectBuffer.stream().map(String::trim)
                .collect(Collectors.joining(this.currentJoiner));
        this.storyExpander.getOwned().assignConstant(this.currentJoinVariable, StoryTokenizer.escape(finalLine));
        this.collectBuffer.clear(); // clear the buffer as we can drop it for lower level
        lineCount = 0;

        this.currentJoiner = "";
        this.currentJoinVariable = "";
        lineCount = repetitionStart;
    }

    private void doReti() {
        inIterCollect = false;
        final int repetitionStart = lineCount;
        // local iteration buffer
        final int iterationCount = iterReiterationCount;
        final List<String> scopeBuffer = new LinkedList<>(collectBuffer);
        this.collectBuffer.clear(); // clear the buffer as we can drop it for lower level
        lineCount = 0;
        inCurrentIteration += 1;
        for (int i = 0; iterationCount < 0 || i < iterationCount; i++) {
            execute(StoryChapterType.SET, "@i", String.valueOf(i));
            for (final String iterLine : scopeBuffer) {
                executeStoryStep("Repetition", iterLine);
                if (breakCurrentIteration)
                    break;

            }
            if (breakCurrentIteration)
                break;
        }
        inCurrentIteration -= 1;
        breakCurrentIteration = false;
        lineCount = repetitionStart;
    }

    private void doFi() {
        inIfCollect = false;
        inElseCollect = false;
        final int ifStart = lineCount;
        lineCount = 0;
        inCurrentIf += 1;
        if (currentIfState) {
            final List<String> scopeBuffer = new LinkedList<>(collectBuffer);
            this.collectBuffer.clear();
            this.collectElseBuffer.clear();
            for (final String ifLine : scopeBuffer) {
                executeStoryStep("If", ifLine);
            }
        } else {
            final List<String> scopeBuffer = new LinkedList<>(collectElseBuffer);

            this.collectBuffer.clear();
            this.collectElseBuffer.clear();
            // skip to the else on the outer level, not very effective, yet working
            for (final String elseLine : scopeBuffer) {
                executeStoryStep("Else", elseLine);
            }
        }

        inCurrentIf -= 1;
        lineCount = ifStart;
    }

    /**
     * Uses the story-constructed expander to expand the string. This will work even
     * if expansion has been disabled via {@link #dontExpand()}.
     * 
     * @param string The string to expand
     * 
     * @return The expanded string
     */
    public String expand(final String string) {
        return this.storyExpander.expand(string);
    }

    /**
     * Uses the story-constructed expander to expand the string. This will work even
     * if expansion has been disabled via {@link #dontExpand()}. In contrast to
     * {@link #expand(String)} this variant adds the {@code $&#123; ... &#125;}
     * markers so the text will be interpreted as a variable.
     * 
     * @param string The variable to expand
     * 
     * @return The expanded string
     * 
     * @see #expand(String)
     * 
     * @since 1.1
     */
    public String expandVariable(final String string) {
        return expand("${" + string + "}");
    }

    /**
     * Uses the story-constructed expander to expand the string. This will work even
     * if expansion has been disabled via {@link #dontExpand()}. In contrast to
     * {@link #expand(String)} this variant adds the {@code $&#123; ... &#125;}
     * markers so the text will be interpreted as a variable.
     * 
     * @param string  The variable to expand
     * @param unknown The string if not present
     * 
     * @return The expanded string
     * 
     * @see #expand(String, String)
     * @see #expandVariable(String)
     * 
     * @since 1.2
     */
    public String expandVariable(final String string, final String unknown) {
        return expand("${" + string + "}", unknown);
    }

    /**
     * Uses the story-constructed expander to expand the string. This will work even
     * if expansion has been disabled via {@link #dontExpand()}.
     * 
     * @param string  The string to expand
     * @param unknown The string if not present
     * @return The expanded string
     * 
     * @since 1.2
     */
    public String expand(final String string, final String unknown) {
        return this.storyExpander.expand(string, unknown);
    }

    // Some simple getters

    public int getLineCounter() {
        return lineCount;
    }

    public boolean isInRepetition() {
        return inIterCollect;
    }

    /**
     * Will just call {@link Story#eraseAllMemories()} to clear the message-buffers
     * of all clients.
     */
    public void eraseAllClientMemories() {
        story.eraseAllMemories();
    }

    /**
     * @return the gameRunning
     */
    public AtomicBoolean isGameRunning() {
        return gameRunning;
    }

    /**
     * This might be used by the {@link BackstoryBuilder} to retrieve the story
     * 
     * @return The current story
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} and the
     *              {@link AbstractStoryProtagonist} to retrieve the story.
     */
    @StoryMethod
    protected Story getStory() {
        return this.story;
    }

}
