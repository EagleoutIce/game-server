package de.uulm.team020.server.client.console.story;

import java.util.List;
import java.util.stream.Collectors;

import de.uulm.team020.parser.expander.Expander;
import de.uulm.team020.server.client.console.ConsoleStoryDialog;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;

/**
 * Holds all possible actions for a client story. These differ (somewhat) from
 * {@link StoryChapterType} as they will be used by the live-running view of the
 * client. This is to make clear, that the client implementation is not meant to
 * server as a live-coding environment for writing stories -- stories were and
 * always will be meant to be used in testing and testing only. This
 * implementation serves as a bridge and strips away all of the
 * math-turing-completeness the Stories do offer. The help-texts are written for
 * the {@link ConsoleStoryDialog}-{@link Expander}.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public enum ConsoleStoryChapterType {
    PRINT(List.of("text"), "Will feed-back the ${param:text} to you has no practical meaning"), //
    HELLO(List.of("role"),
            "Register this client with the name (${name}) you set on start. The role may be one of: AI, PLAYER, SPECTATOR. This client will not restrict your actions."),
    META(List.of("key1,key2,..."),
            "Request all the Meta-Information you have given as keys. So for example write '${key:meta} Player.Names' to refresh the player information for ${key:info}.\nThe client will update there informations always whenever there is an update to be expected...\nIf you want to fetch all server information write '${key:meta} server'"),
    INFO(List.of("element"),
            "The ${key:info}-Type allows you to get more information about an ${param:element}. So you may write '${key:info} server' to get more information about the server you are connected to. Write '@|magenta :help dig|@' for information about the keys."),
    CLEAR(List.of(), "Clear the screen");

    private final int numOfArgs;
    private final String description;
    private final List<String> signature;

    ConsoleStoryChapterType(List<String> signature, String description) {
        this.numOfArgs = signature.size();
        this.signature = signature;
        this.description = description;
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
     * Universal agreement on how to format chapter-types inside of the console
     * 
     * @param type The type to format
     * 
     * @return Ansi-formatted string
     */
    public static String renderChapterType(ConsoleStoryChapterType type) {
        return "@|cyan " + type.toString() + "|@";
    }

    /**
     * Renders the signature including the help of this type
     * 
     * @param expander the expander to use to expand the help-text
     * 
     * @return Signature of this type
     */
    public String renderHelp(final Expander expander) {
        return renderChapterType(this) + " @|italic "
                + this.signature.stream().map(x -> "<" + x + ">").collect(Collectors.joining(" ")) + "|@\n"
                + expander.expand(description);
    }
}
