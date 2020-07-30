package de.uulm.team020.server.client.console.story;

import java.util.Arrays;
import java.util.Optional;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.parser.expander.Expander;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.helper.DiggerKeyEnum;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.helper.StoryTokenizer;

/**
 * This acts somewhat similar to the {@link StoryBoard} as it is parsing
 * Commands. But there is one difference: it will only parse one line.
 * Furthermore it may enable a simplistic and stripped-down version of
 * Syntax-Highlighting.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class ConsoleStoryProcessor {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    private final String name;
    private final StoryTokenizer tokenizer;
    private final Expander expander;

    private final ServerClientController controller;

    /**
     * Construct a new processor
     * 
     * @param clientName Name of the client this one serves
     * @param controller The controller this one is bound to
     * @param expander   The expander to use
     */
    public ConsoleStoryProcessor(String clientName, final ServerClientController controller, Expander expander) {
        this.name = clientName;
        this.tokenizer = new StoryTokenizer();
        this.expander = expander;
        this.controller = controller;
    }

    private boolean assertNumOfArgs(final ConsoleStoryChapterType type, final int n, final String line) {
        if (type.getNumOfArgs() != n) {
            ConsoleWriter.getWriter()
                    .renderln("The type: " + type + " needs exactly " + type.getNumOfArgs() + " Argument"
                            + (type.getNumOfArgs() == 1 ? "" : "s") + " but" + (n < type.getNumOfArgs() ? " only" : "")
                            + " got " + n + ". Which was: " + line + ".\n" + expander.expand(type.getDescription()));
            return false;
        }
        return true;
    }

    /**
     * Processes a story-line event (and tokenize it). If the command produces a
     * message it will be returned;
     * 
     * @param line The entered line
     * 
     * @return The Message, if there is one, will return empty if there was an error
     */
    public Optional<StoryFeedback> process(String line) {
        line = expander.expand(line);
        // Tokenize the line
        String[] tokens;
        try {
            tokens = tokenizer.tokenize(line);
        } catch (StoryException ex) {
            ConsoleWriter.getWriter().renderln("@|red Error Parsing:|@ " + ex.getMessage());
            return Optional.empty();
        }
        if (tokens.length == 0) {
            return Optional.empty();
        }
        final ConsoleStoryChapterType type;
        try {
            type = ConsoleStoryChapterType.valueOf(tokens[0].toUpperCase());
            if (!assertNumOfArgs(type, tokens.length - 1, line)) {
                return Optional.empty();
            }
        } catch (IllegalArgumentException ex) {
            magpie.writeException(ex, "storyProcess");
            ConsoleWriter.getWriter().renderln("@|red The key|@ '@|red,bold " + tokens[0] + "|@' @|red is not known|@");
            return Optional.empty();
        }
        // Process data
        switch (type) {
            case PRINT:
                ConsoleWriter.getWriter().renderln(tokens[1]);
                return Optional.of(new StoryFeedback()); // everything worked fine
            case CLEAR:
                ConsoleWriter.getWriter().clearScreen();
                return Optional.of(new StoryFeedback()); // everything worked fine
            case INFO:
                return digForInfo(tokens[1]);
            case HELLO:
                return hello(tokens[1]);
            case META:
                return meta(tokens[1]);
            default:
                return Optional.empty();
        }
    }

    private Optional<StoryFeedback> hello(String role) {
        try {
            return Optional.of(new StoryFeedback(new HelloMessage(name, RoleEnum.valueOf(role.toUpperCase()))));
        } catch (IllegalArgumentException ex) {
            magpie.writeException(ex, "parse");
            ConsoleWriter.getWriter().renderln("@|red The role: '" + role + "' is not valid. Choose one of: "
                    + Arrays.toString(RoleEnum.values()) + ".|@");
        }
        return Optional.empty();
    }

    private Optional<StoryFeedback> meta(String keys) {
        keys = keys.trim();
        // jay: hardcode da shit :D
        if (keys.equalsIgnoreCase("server")) {
            keys = "Player.Names,Player.Count,Spectator.Names,Spectator.Count";
        }
        try {
            return Optional.of(new StoryFeedback(
                    new RequestMetaInformationMessage(controller.getClientId(), keys.split(",\\s*"))));
        } catch (IllegalArgumentException ex) {
            magpie.writeException(ex, "parse");
            ConsoleWriter.getWriter().renderln("@|red The keys: '" + keys + "' are not in a valid format.|@");
        }
        return Optional.empty();
    }

    public Optional<StoryFeedback> digForInfo(String key) {
        // Search for the key
        try {
            DiggerKeyEnum diggerKey = DiggerKeyEnum.valueOf(key.toUpperCase());
            if (controller.getDigger().dig(diggerKey)) {
                return Optional.of(new StoryFeedback()); // everything worked fine
            } else {
                // dig failed
                return Optional.empty();
            }

        } catch (IllegalArgumentException ex) {
            magpie.writeException(ex, "dig");
            ConsoleWriter.getWriter().renderln(
                    "@|red The key '" + key + "' is no valid digger key. Write ':help dig' for more information|@");
            return Optional.empty();
        }
    }

}