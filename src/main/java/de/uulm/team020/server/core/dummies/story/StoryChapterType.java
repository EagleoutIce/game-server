package de.uulm.team020.server.core.dummies.story;

/**
 * Holds all possible actions for a story
 *
 * @author Florian Sihler
 * @version 1.2, 06/28/2020
 */
public enum StoryChapterType {
    /**
     * &lt;name&gt; &lt;role&gt;. Will create the client and send the right hello
     * message.
     */
    HELLO(2, false, "<name> <role>. Will create the client and send the right hello message."),
    /**
     * &lt;time&gt;. Sleeps for &lt;time&gt;ms-blocks the thread.
     */
    SLEEP(1, false, "<time>. Sleeps for <time>ms-blocks the thread."),
    /**
     * &lt;name&gt;. Simulates a timeout for the client registered with
     * &lt;name&gt;.
     */
    TIMEOUT(1, true, "<name>. Simulates a timeout for the client registered with <name>."),
    /**
     * &lt;name&gt;. Simulates a crash for the client registered with &lt;name&gt;.
     */
    CRASH(1, true, "<name>. Simulates a crash for the client registered with <name>."),
    /**
     * &lt;oldname&gt; &lt;newname&gt;. Reconnects a new client, named
     * &lt;newname&gt; to the connection of &lt;oldname&gt;. This will only work if
     * &lt;oldname&gt; crashed before, furthermore the name of the client at the
     * server will stay the same, this only allows you to keep the old, dead
     * connection for testing.
     */
    RECONNECT(2, true,
            "<oldname> <newname>. Reconnects a new client, named <newname> to the connection of <oldname>. This will only work if <oldname> crashed before, furthermore the name of the client at the server will stay the same, this only allows you to keep the old, dead connection for testing."),
    /**
     * &lt;name&gt;. The client with &lt;name&gt; will initiate the leave-rite.
     */
    LEAVE(1, true, "<name>. The client with <name> will initiate the leave-rite."),
    /**
     * &lt;name&gt; &lt;information&gt;. Requests Meta-Information, you can write
     * information comma-separated for multiple.
     */
    META(2, true,
            "<name> <information>. Requests Meta-Information, you can write information comma-separated for multiple."),
    /**
     * &lt;name&gt;. The client with &lt;name&gt; will send a RequestGamePause
     * message.
     */
    PAUSE(1, true, "<name>. The client with <name> will send a RequestGamePause message."),
    /**
     * &lt;name&gt;. The client with &lt;name&gt; sends a resume request via
     * RequestGamePause.
     */
    RESUME(1, true, "<name>. The client with <name> sends a resume request via RequestGamePause."),
    /**
     * &lt;name&gt;. Removes the client with &lt;name&gt; from the list.
     */
    DELETE(1, false, "<name>. Removes the client with <name> from the list."),
    /**
     * &lt;oldname&gt; &lt;newname&gt;. Renames the client with &lt;oldname&gt; to
     * &lt;newname&gt;.
     */
    RENAME(2, false, "<old name> <new name>. Renames the client with <old name> to <new name>."),
    /**
     * &lt;name&gt; &lt;item:name, random, random-gadget, random-character&gt;.
     * Chooses the Item denoted by &lt;item&gt;, if it is present.
     */
    ITEM(2, true,
            "<name> <item:name, random, random-gadget, random-character>. Chooses the Item denoted by <item>, if it is present."),
    /**
     * &lt;name&gt; &lt;random,equipment:char,item1,item2,char2,item3,char3,...&gt;.
     * Sets the equipment.
     */
    EQUIP(2, true, "<name> <random,equipment:char,item1,item2,char2,item3,char3,...>. Sets the equipment."),
    /**
     * &lt;name&gt; &lt;operation-type&gt; &lt;operation-arguments&gt;. Sends an
     * operation-request, probably will be split.
     */
    OPERATION(3, true,
            "<name> <operation-type> <operation-arguments>. Sends an operation-request, probably will be split."),
    /* Iteration addon */
    /**
     * &lt;times&gt;. Will collect all lines until {@link #RETI} and repeat them
     * &lt;times&gt;-times. Can be nested, the current round counter will be stored
     * in the expandable $&#123;&#64;i&#125; going by the name of &#64;i.
     * (<i>Iteration Addon</i>)
     */
    ITER(1, false,
            "<times>. Will collect all lines until 'RETI' and repeat them <times>-times. Can be nested, the current round counter will be stored in the expandable @i."),
    /**
     * . Will end the buffering for an {@link #ITER} command and execute the
     * iteration. (<i>Iteration Addon</i>)
     */
    RETI(0, false, "Will end the buffering for an 'ITER' command and execute the iteration."),
    /**
     * &lt;a&gt; &lt;b&gt;. Will break a running iteration of {@link #ITER}, if
     * &lt;a&gt; equals &lt;b&gt;. (<i>Iteration Addon</i>)
     */
    BREAK_EQ(2, false, "<a> <b>. Will break a running iteration of 'ITER', if <a> equals <b>."),
    /* Preset/Expander addon */
    /**
     * &lt;key&gt; &lt;value&gt;. Change the value of an expansion to value, will
     * create a new one if it is missing. (<i>Expander Addon</i>)
     */
    SET(2, false, "<key> <value>. Change the value of an expansion to value, will create a new one if it is missing."),
    /**
     * &lt;key&gt; &lt;value&gt;. Change the value of an expansion to value, will
     * create a new one if it is missing. This differs to set in a way that it will
     * use the unexpanded value on assignment time. (<i>Expander Addon</i>)
     */
    LET(2, false,
            "<key> <value>. Change the value of an expansion to value, will create a new one if it is missing. This differs to set in a way that it will use the unexpanded value on assignment time."),
    /**
     * &lt;name&gt;. Will call the function with the given name. The function may
     * contain the separator '\n' to identify multiple lines. (<i>Expander
     * Addon</i>)
     */
    CALL(1, false,
            "<name>. Will call the function with the given name. The function may contain the separator '\\n' to identify multiple lines."),
    /**
     * &lt;key&gt;. Will unregister the expandable which was registerd for this key.
     * Will not fail if there was none before. (<i>Expander Addon</i>)
     */
    UNSET(1, false,
            "<key>. Will unregister the expandable which was registerd for this key. Will not fail if there was none before."),
    /**
     * &lt;key&gt; &lt;value&gt;. Works like set, but does not overwrite an already
     * existing expansion. (<i>Expander Addon</i>)
     */
    ASSURE_DEFAULT(2, false, "<key> <value>. Works like set, but does not overwrite an already existing expansion."),
    /**
     * &lt;name&gt;. Will load and execute/buffer the preset. (<i>Expander
     * Addon</i>)
     */
    PRESET(1, false, "<name>. Will load and execute/buffer the preset."),
    /* Special Sets */
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; &lt;= &lt;b&gt;).
     * Will be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_LEQ(3, false, "<a> <b> <c>. Set <c> = (<a> <= <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; &gt;= &lt;b&gt;).
     * Will be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_GEQ(3, false, "<a> <b> <c>. Set <c> = (<a> >= <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; == &lt;b&gt;). Will
     * be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_EQ(3, false, "<a> <b> <c>. Set <c> = (<a> == <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; != &lt;b&gt;). Will
     * be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_NEQ(3, false, "<a> <b> <c>. Set <c> = (<a> != <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; &lt; &lt;b&gt;).
     * Will be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_LT(3, false, "<a> <b> <c>. Set <c> = (<a> < <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Set &lt;c&gt; = (&lt;a&gt; &gt; &lt;b&gt;).
     * Will be 1 if true, 0 if false. (<i>Conditionals Addon</i>)
     */
    SET_GT(3, false, "<a> <b> <c>. Set <c> = (<a> > <b>). Will be 1 if true, 0 if false."),
    /**
     * &lt;a&gt; &lt;b&gt;. Set &lt;b&gt; = &lt;a&gt; is defined. Will be 1 if true,
     * 0 if false. (<i>Conditionals Addon</i>)
     * <p>
     * Note that this is not necessary in any way but it is easier to read than a
     * raw string comparison
     */
    SET_PRESENT(2, false, "<a> <b>. Set <b> = '<a> is defined'. Will be 1 if true, 0 if false."),
    /* Penetration handlers */
    /**
     * &lt;name:random&gt; &lt;type:random&gt; &lt;number&gt; &lt;delayMs&gt;.
     * Penetrate the server with a rapid message-attack. (<i>Penetration Addon</i>)
     */
    DOS(4, false, "<name:random> <type:random> <number> <delayMs>. Penetrate the server with a rapid message-attack."),
    /* Debug helpers */
    /**
     * &lt;text&gt;. Will just print the expanded text to the std-out and might be
     * used for testing. (<i>Debug Addon</i>)
     */
    PRINT(1, false, "<text>. Will just print the expanded text to the std-out and might be used for testing."),
    /** Conditionals addon */
    /**
     * &lt;a&gt;. Will execute the if-block if the expandable expands to a
     * 'true'-value (e.g. 1, true, 'wahr', ...) -- see
     * {@link StoryBoard#isTrue(String)}. (<i>Conditionals Addon</i>)
     */
    IF(1, false,
            "<a>. Will execute the if-block if the expandable expands to a 'true'-value (e.g. 1, true, 'wahr', ...)."),
    /**
     * &lt;a&gt;. Will execute the if-block if the expandable expands to something
     * else than a 'true'-value (e.g. 1, true, 'wahr', ...) -- see the negated
     * {@link StoryBoard#isTrue(String)}. (<i>Conditionals Addon</i>)
     */
    IF_NOT(1, false,
            "<a>. Will execute the if-block if the expandable expands to a 'false'/none-true-value (not: 1, true, 'wahr', ...)."),
    /**
     * . Will execute the else-block if the corresponding
     * {@link #IF}/{@link #IF_NOT}-block does not hold. (<i>Conditionals Addon</i>)
     */
    ELSE(0, false, "Will execute the else-block if the corresponding if-block does not hold."),
    /**
     * . Will end the corresponding {@link #IF}/{@link #IF_NOT}/{@link #ELSE}-block.
     * (<i>Conditionals Addon</i>)
     */
    FI(0, false, "Will end the corresponding IF or ELSE block."),
    /* Server-Injects */
    /**
     * &lt;key&gt; &lt;target&gt; &lt;value&gt;. Allows to set some values the
     * server will use to manipulate the game. See {@link Injects} for possible
     * keys. (<i>Injections Addon</i>)
     */
    CONFIG_INJECT(3, false,
            "<key> <target> <value>. Allows to set some values the server will use to manipulate the game."),
    /**
     * &lt;key&gt; &lt;value&gt;. Set the server-config-data. (<i>Injections
     * Addon</i>)
     */
    SERVER_CONFIG(2, false, "<key> <value>. Set the server-config-data."),
    /**
     * . Story will allow some exceptions now. Exceptions which will prevent the
     * story from running won't run. See {@link #FORBID_ERRORS}.
     */
    ALLOW_ERRORS(0, false,
            "Story will allow some exceptions now. Exceptions which will prevent the story from running won't run."),
    /**
     * . Story wont allow any exceptions now. See {@link #ALLOW_ERRORS}.
     */
    FORBID_ERRORS(0, false, "Story wont allow any exceptions now."),
    /* Math */
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Calculates &lt;a&gt; + &lt;b&gt; and stores
     * result in expandable &lt;c&gt;. (<i>Math Addon</i>)
     */
    ADD(3, false, "<a> <b> <c>. Calculates <a> + <b> and stores result in expandable <c>."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Calculates &lt;a&gt; - &lt;b&gt; and stores
     * result in expandable &lt;c&gt;. (<i>Math Addon</i>)
     */
    SUB(3, false, "<a> <b> <c>. Calculates <a> - <b> and stores result in expandable <c>."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Calculates &lt;a&gt; * &lt;b&gt; and stores
     * result in expandable &lt;c&gt;. (<i>Math Addon</i>)
     */
    MUL(3, false, "<a> <b> <c>. Calculates <a> * <b> and stores result in expandable <c>."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Calculates &lt;a&gt; / &lt;b&gt; and stores
     * result in expandable &lt;c&gt;. (<i>Math Addon</i>)
     */
    DIV(3, false, "<a> <b> <c>. Calculates <a> / <b> and stores result in expandable <c>."),
    /**
     * &lt;joiner&gt; &lt;variable&gt;.Will start the collection of lines until a
     * end sequence ({@link #COLLECT_END}) is found. The joiner will be used to
     * combine the given lines. Collections should never be nested, usually they can
     * be used to write one statement across several lines.Will start the collection
     * of lines until a end sequence is found. The joiner will be used to combine
     * the given lines. Collections should never be nested, usually they can be used
     * to write one statement across several lines. (<i>Collectors Addon</i>)
     */
    COLLECT_START(2, false,
            "<joiner> <variable>. Will start the collection of lines until a end sequence is found. The joiner will be used to combine the given lines. Collections should never be nested, usually they can be used to write one statement across several lines."),
    /**
     * . Will end the collection process. This will cause the story to join all
     * collected lines by the given token and store them in the given variable.
     * (<i>Collectors Addon</i>)
     */
    COLLECT_END(0, false,
            ". Will end the collection process. This will cause the story to join all collected lines by the given token and store them in the given variable."),
    /**
     * &lt;a&gt; &lt;b&gt; &lt;c&gt;. Calculates &lt;a&gt; % &lt;b&gt; and stores
     * result in expandable &lt;c&gt;. This will depend on
     * {@link Math#floorMod(int, int)}. (<i>Math Addon</i>)
     */
    MOD(3, false, "<a> <b> <c>. Calculates <a> % <b> and stores result in expandable <c>.");

    private final int numOfArgs;
    private final String description;
    private final boolean canBePerformed;

    StoryChapterType(int numOfArgs, boolean canBePerformed, String description) {
        this.numOfArgs = numOfArgs;
        this.description = description;
        this.canBePerformed = canBePerformed;
    }

    /**
     * Number of Arguments this chapter consumes
     * 
     * @return Integer referencing the number of arguments.
     */
    public int getNumOfArgs() {
        return this.numOfArgs;
    }

    /**
     * Description of this command
     * 
     * @return String describing the signature and the semantics of the command.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Flag which identifies if a performer is able to register on this chapter type
     * 
     * @return True if you can register a performer for this chapter type, false
     *         otherwise.
     */
    public boolean canBePerformed() {
        return this.canBePerformed;
    }

    /**
     * Format the {@link #description} including the name of the command
     * 
     * @return String, which explains the command (including its usage).
     */
    public String getHelp() {
        return "Usage: " + this + " " + getDescription();
    }
}
