package de.uulm.team020.server.core.dummies.story.helper;

import java.util.Arrays;
import java.util.stream.Collectors;

import de.uulm.team020.server.core.dummies.story.StoryChapterType;

/**
 * This is just a little sub-datatype which is only to be valid inside of a
 * Story-Author. The Author uses this data type to hold information about a
 * line. It may be used otherwise.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/13/2020
 * 
 * @since 1.1
 */
public class StoryLine {

    private final StoryChapterType type;
    private final String[] arguments;

    public StoryLine(final StoryChapterType type, final String... arguments) {
        this.type = type;
        this.arguments = arguments;
    }

    /**
     * @return the type
     */
    public StoryChapterType getType() {
        return type;
    }

    /**
     * @return the arguments
     */
    public String[] getArguments() {
        return arguments;
    }

    /**
     * Produce the command embedded into this line
     * 
     * @return The fully escaped String
     */
    public String buildCommand() {
        return buildCommand(this);
    }

    /**
     * Build a command
     * 
     * @param type      Type of the command
     * @param arguments Argument to pass to, will be escaped if necessary
     * 
     * @return The fully escaped String
     */
    public static String buildCommand(StoryChapterType type, String... arguments) {
        return type + (arguments.length > 0
                ? " " + Arrays.stream(arguments).map(StoryTokenizer::escape).collect(Collectors.joining(" "))
                : "");
    }

    /**
     * Build a command
     * 
     * @param line The line to build the command from
     * 
     * @return The fully escaped String
     */
    public static String buildCommand(StoryLine line) {
        return buildCommand(line.getType(), line.getArguments());
    }

    @Override
    public String toString() {
        return buildCommand();
    }
}