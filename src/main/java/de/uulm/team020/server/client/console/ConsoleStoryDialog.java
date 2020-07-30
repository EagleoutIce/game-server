package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.parser.expander.Expandables;
import de.uulm.team020.parser.expander.Expander;
import de.uulm.team020.server.client.console.story.ConsoleStoryChapterType;
import de.uulm.team020.server.client.console.story.ConsoleStoryProcessor;
import de.uulm.team020.server.client.console.story.StoryFeedback;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.helper.InformationDigger;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.helper.StoryTokenizer;

/**
 * This dialog may be used to only allow valid Story-Lines. Please note, that
 * the Syntax varies for the client as it does OMIT the client-name and does not
 * allow to send commands which are not sendable by a client. Furthermore the
 * "random" keys are disabled for now as they to not seem to make any sense (for
 * someone who wants to play).
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public class ConsoleStoryDialog extends ConsoleDialog {
    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    private final String name;
    private final Expander utilityExpander;
    private final ConsoleStoryProcessor processor;
    private final Set<ConsoleStoryChapterType> validTypes;

    private Optional<StoryFeedback> parsedContainer;

    /**
     * This constructs a new dialog
     * 
     * @param controller The controller this one obeys to
     * @param name       The name of this client which will be appended to all
     *                   commands
     */
    public ConsoleStoryDialog(final ServerClientController controller, final String name) {
        super();
        this.name = name;
        magpie.writeInfo("Created Story-Dialog for: " + this.name, "init");
        validTypes = Set.of(ConsoleStoryChapterType.values());
        setHelp("You may enter the story syntax with restricted/other Chapter-Types.\nWhat does this mean? You may use any of the following story-commands: @|italic "
                + validTypes
                + "|@.\nSome signatures have changed as the dialog will insert data like your client-name automatically.\nIf you want specific information you may write @|magenta,italic :help <command>|@.");
        setValidator(this::storyValidator);
        setFeedbackProcessor(this::storyFeedbackProcessor);

        // setup the expander
        Expandables coreExpandables = new Expandables();
        coreExpandables.registerExpansion("date", Expandables::expandDate);
        coreExpandables.registerExpansion("time", Expandables::expandTime);
        coreExpandables.registerExpansion("level", Expandables::expandLevel);
        coreExpandables.registerExpansion("system", Expandables::expandSystem);
        // data access
        coreExpandables.registerExpansion("call", Expandables::expandFull);
        coreExpandables.registerConstant("empty", "");

        // generic usable(s)

        coreExpandables.registerConstant("name", name);
        coreExpandables.registerExpansion("eval",
                (String i1, String option, int i3, Expander expander) -> expandToOption(expander, option));
        coreExpandables.registerExpansion("param",
                (String i1, String option, int i3, Expander expander) -> expandToParam(option));
        coreExpandables.registerExpansion("key",
                (String i1, String option, int i3, Expander expander) -> expandToKey(expander, option));
        coreExpandables.registerExpansion("num",
                (String i1, String option, int i3, Expander expander) -> expandToNumber(option));
        coreExpandables.registerExpansion("not",
                (String i1, String option, int i3, Expander expander) -> expandToNot(expander, option));
        this.utilityExpander = new Expander(coreExpandables);
        this.processor = new ConsoleStoryProcessor(name, controller, utilityExpander);
    }

    private Optional<List<String>> storyValidator(final List<String> lastInputs, final String input,
            final int rePromptCount) {
        // So... we are lying some commands like the print command are executed here
        // as they are considered to be internals
        // cast it:
        parsedContainer = processor.process(input);
        if (!parsedContainer.isPresent()) { // Failed
            return Optional.of(List.of());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Is overwritten by subclasses to provide detailed help about data
     * 
     * @param input  The full input
     * @param tokens Tokens which have been input - including help
     * 
     * @return True if help was prompted, false otherwise
     */
    @Override
    protected boolean specificHelp(String input, String[] tokens) {
        if (tokens == null || tokens.length != 2) {
            return false;
        }
        // dig help as bonus
        if (isDiggerToken(tokens[1])) {
            InformationDigger.printFullHelp();
            return true;
        }
        try {
            ConsoleStoryChapterType chapterType = ConsoleStoryChapterType.valueOf(tokens[1]);
            if (!validTypes.contains(chapterType)) {
                writer.renderln("@|red This chapter type may not be used. Choose one of: " + validTypes + "|@");
            } else {
                writer.renderln(getHelpText(chapterType));
            }
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private boolean isDiggerToken(String token) {
        return token.equalsIgnoreCase("dig") || token.equalsIgnoreCase("digger");
    }

    private String getHelpText(ConsoleStoryChapterType type) {
        return type.renderHelp(this.utilityExpander);
    }

    /**
     * "real" story processor to be used on feedback change. This is still sloppy :D
     * 
     * @param input The data read
     * 
     * @return Formatted data
     */
    public String storyFeedbackProcessor(final String input) {
        final StoryTokenizer tokenizer = new StoryTokenizer();
        final Set<String> validChapters = validTypes.stream().map(ConsoleStoryChapterType::toString)
                .collect(Collectors.toSet());
        String[] tokens;
        try {
            tokens = tokenizer.tokenize(input);
        } catch (StoryException ex) {
            return input;
        }
        StringBuilder builder = new StringBuilder();
        for (String string : tokens) {
            if (validChapters.contains(string.toUpperCase())) {
                builder.append("@|cyan ").append(string).append("|@ ");
            } else if (isDiggerToken(string)) {
                builder.append("@|blue ").append(string).append("|@ ");
            } else {
                String value = StoryTokenizer.escape(string);
                if (value.startsWith("\"")) {
                    builder.append("@|yellow " + value + "|@");
                } else {
                    builder.append(value);
                }
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @param prompt Text to be capsuled with the console reader prompt setting
     * 
     * @return The feedback of the processor
     * 
     * @throws IOException If there was an error reading
     */
    public Optional<StoryFeedback> prompt(final String prompt) throws IOException {
        promptInput(prompt);
        return parsedContainer;
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @return The feedback of the processor
     * 
     * @throws IOException If there was an error reading
     * 
     * @see #prompt(String)
     */
    public Optional<StoryFeedback> prompt() throws IOException {
        promptInput();
        return parsedContainer;
    }

    // Expansion-Helpers
    private String expandToOption(Expander expander, String option) {
        return expander.expand("${call:" + expander.expand("${" + option + "}") + "}");
    }

    private String expandToParam(String option) {
        return "@|italic <" + option + ">|@";
    }

    private String expandToKey(Expander expander, String option) {
        return ConsoleStoryChapterType.renderChapterType(ConsoleStoryChapterType.valueOf(option.toUpperCase()));
    }

    private String expandToNumber(String option) {
        return "@|yellow " + option + "|@";
    }

    private String expandToNot(Expander expander, String option) {
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
}