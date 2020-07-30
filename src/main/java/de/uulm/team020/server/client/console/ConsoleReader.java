package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;
import de.uulm.team020.server.core.dummies.story.helper.StoryTokenizer;

/**
 * This is the base class to be used on reading from the terminal -- it may be
 * acquired via the {@link AbstractScreenOwner}-Mechanism but not by the debug
 * utilities -- you must accept, that you are responsible for multi-threaded
 * accesses as this one barely performs as a wrapper for the JAnsi-toolset.
 * <p>
 * This class operates as a singleton and as a singleton only -- there is no
 * support for feeding multiple terminals at once.
 * <p>
 * This class will use a scanner (as it allows deleting) to perform reading
 * operations it may be extended by dialog classes.The goal is to retrieve data
 * from the user.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class ConsoleReader {

    private static final ConsoleReader singleton = new ConsoleReader();
    private final ConsoleWriter writer;
    private final java.util.Scanner reader;

    private String prePrompt;
    private String postPrompt;
    private UnaryOperator<String> feedbackProcessor;

    private ConsoleReader() {
        writer = ConsoleWriter.getWriter();
        // new BufferedReader(new InputStreamReader
        reader = new Scanner(System.in);
        setPrompt("@|magenta ", "> |@");
        setFeedbackConsumer(this::defaultFeedbackProcessor);
    }

    /**
     * Get the console reader :D
     * 
     * @return The console reader operating on this terminal
     */
    public static ConsoleReader getReader() {
        return singleton;
    }

    /**
     * Default processor to be used on feedback change with
     * {@link #setFeedbackConsumer(UnaryOperator)}
     * 
     * @param input The data read
     * 
     * @return Formatted data - change noting
     */
    public String defaultFeedbackProcessor(final String input) {
        return input;
    }

    /**
     * Example processor to show highlighting of story operations via
     * {@link #setFeedbackConsumer(UnaryOperator)}. This one was coded sloppy and
     * just as an example
     * 
     * @param input The data read
     * 
     * @return Formatted data - change noting
     */
    public String storyFeedbackProcessor(final String input) {
        final StoryTokenizer tokenizer = new StoryTokenizer();
        final Set<String> validChapters = Set.of(StoryChapterType.values()).stream().map(StoryChapterType::toString)
                .collect(Collectors.toSet());
        String[] tokens = tokenizer.tokenize(input);
        StringBuilder builder = new StringBuilder();
        for (String string : tokens) {
            if (validChapters.contains(string)) {
                builder.append("@|cyan ").append(string).append("|@ ");
            } else {
                builder.append(string).append(" ");
            }
        }
        return builder.toString();
    }

    /**
     * Set Data to be displayed before and after a prompt-request
     * 
     * @param pre  Text to be displayed before the prompt
     * @param post Text to be displayed after the prompt
     */
    public void setPrompt(final String pre, final String post) {
        prePrompt = pre;
        postPrompt = post;
    }

    /**
     * Set how Data shall be formatted
     * 
     * @param feedbackProcessor The processor to be called for feedback-reveal
     */
    public void setFeedbackConsumer(final UnaryOperator<String> feedbackProcessor) {
        this.feedbackProcessor = feedbackProcessor;
    }

    /**
     * This will prompt the user with a given question by you. The user will not get
     * re-prompted.
     * 
     * @param prompt The text to be capsuled with {@link #setPrompt(String, String)}
     * 
     * @return The String the user entered (this may be driven from a file so it
     *         will not be cleared)
     * 
     * @throws IOException If anything fails with the underlying {@link #reader}
     */
    public String prompt(final String prompt) throws IOException {
        return prompt(prompt, false);
    }

    /**
     * This will prompt the user with a given question by you. The user may get
     * re-prompted.
     * 
     * @param prompt  The text to be capsuled with
     *                {@link #setPrompt(String, String)}
     * @param noEmpty Shall the user get re-prompted if the input is empty?
     * 
     * @return The String the user entered (this may be driven from a file so it
     *         will not be cleared)
     * 
     * @throws IOException If anything fails with the underlying {@link #reader}
     */
    public String prompt(final String prompt, final boolean noEmpty) throws IOException {
        String input;
        do {
            writePromptText(prompt);
            input = prompt();
        } while (noEmpty && input.isEmpty());
        mayProcess(input);
        writer.reset();
        return input;
    }

    /**
     * Capsules the string with the prompt data. Will write nothing if null
     * 
     * @param prompt The prompt string to be rendered
     */
    protected void writePromptText(final String prompt) {
        if (Objects.nonNull(prompt)) {
            writer.render(prePrompt + prompt + postPrompt);
        }
    }

    /**
     * This will prompt the user at the current position. The user may get
     * re-prompted.
     * 
     * @param noEmpty Shall the user get re-prompted if the input is empty?
     * 
     * @return The String the user entered (this may be driven from a file so it
     *         will not be cleared)
     * 
     * @throws IOException If anything fails with the underlying {@link #reader}
     * 
     * @see #prompt(String, boolean)
     */
    public String prompt(final boolean noEmpty) throws IOException {
        String input;
        do {
            input = prompt();
        } while (noEmpty && input.isEmpty());
        mayProcess(input);
        return input;
    }

    protected void mayProcess(String input) {
        if (feedbackProcessor != null) {
            writer.renderln(feedbackProcessor.apply(input));
        }
    }

    protected String prompt() {
        return reader.nextLine();
        // writer.print(writer.buildFrom().cursorDownLine()).flush()
        // return input
    }

    /**
     * Retrieve the feedback processor
     * 
     * @return the feedbackProcessor
     */
    public UnaryOperator<String> getFeedbackProcessor() {
        return feedbackProcessor;
    }

}