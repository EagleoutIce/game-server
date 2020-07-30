package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * This class uses the {@link ConsoleReader} to provide dialogs which include
 * semantic-checks. So you may specify rules on which to accept the given input.
 * You are also allowed to write further messages to the user explaining why the
 * input was not valid. You may also change the way the data hops back to the
 * user after the prompt succeeded.
 * <p>
 * While a dialog is running, the
 * {@link ConsoleReader#setFeedbackConsumer(java.util.function.UnaryOperator)}
 * method will get infected.
 * <p>
 * Please note, that <i>any</i> dialog allows {@code :help} to display all
 * possible values -- {@code :help} will always result in a re prompt.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public abstract class ConsoleDialog {

    /**
     * The help token
     */
    public static final String HELP = ":help";

    protected final ConsoleReader reader;
    protected final ConsoleWriter writer;

    protected String possibleInputs;

    protected IDialogInputValidator inputValidator;

    protected UnaryOperator<String> feedbackProcessor;

    /**
     * Construct a new one, this will automatically acquire the current
     * {@link ConsoleReader} and {@link ConsoleWriter}.
     */
    public ConsoleDialog() {
        reader = ConsoleReader.getReader();
        writer = ConsoleWriter.getWriter();
        setValidator(IDialogInputValidator::trueValidator);
        setFeedbackProcessor(reader::defaultFeedbackProcessor);
    }

    /**
     * Change the validator, so the dialog may check for the constraints you want
     * 
     * @param inputValidator The new validator to use
     */
    protected final void setValidator(final IDialogInputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    /**
     * Set how Data shall be formatted
     * 
     * @param feedbackProcessor The processor to be called for feedback-reveal
     */
    protected void setFeedbackProcessor(final UnaryOperator<String> feedbackProcessor) {
        this.feedbackProcessor = feedbackProcessor;
    }

    /**
     * Set how help shall be presented
     * 
     * @param help Test to be displayed on {@code :help}
     */
    protected void setHelp(final String help) {
        this.possibleInputs = help;
    }

    /**
     * May prompt for help, may redistribute downwards
     * 
     * @param input The input given
     * @return true if help was presented, false otherwise
     */
    protected final boolean mayPromptHelp(String input) {
        input = input.trim();
        if (!input.toLowerCase().startsWith(HELP)) {
            return false;
        }
        // present help -- are there other tokens given?
        String[] tokens = input.split("\\s+");
        // may prompt specific help
        if (tokens.length > 1 && specificHelp(input, tokens)) {
            return true;
        }
        // prompt this help
        writer.renderln(possibleInputs);
        return true;

    }

    /**
     * May be overwritten by subclasses to provide detailed help about data
     * 
     * @param input  The full input
     * @param tokens Tokens which have been input - including help
     * 
     * @return True if help was prompted, false otherwise
     */
    protected boolean specificHelp(String input, String[] tokens) {
        return false;
    }

    /**
     * Prompt the user for a certain value -- will automatically re-prompt if not
     * valid.
     * 
     * @return The input-string which succeeds the patterns
     * 
     * @throws IOException If the reading/acquisition fails
     * 
     * @see #promptInput(String)
     */
    protected String promptInput() throws IOException {
        return promptInput(null);
    }

    /**
     * Used by the dialog to display all errors
     * 
     * @param errors The errors to display
     * @param input  The input of the user may not be used if overwritten
     */
    protected void mayPrintErrors(final List<String> errors, final String input) {
        if (errors.isEmpty()) {
            return; // nothing to display
        }
        // just to be clean
        for (String string : errors) {
            writer.renderln(string);
        }
    }

    /**
     * Prompt the user for a certain value -- will automatically re-prompt if not
     * valid
     * 
     * @param prompt Prompt to be displayed. Use null if none desired
     * 
     * @return The input-string which succeeds the patterns
     * 
     * @throws IOException If the reading/acquisition fails
     */
    protected String promptInput(final String prompt) throws IOException {
        // Store the feedback consumer
        final UnaryOperator<String> oldFeedbackConsumer = reader.getFeedbackProcessor();
        reader.setFeedbackConsumer(feedbackProcessor);
        final List<String> lastInputs = new LinkedList<>();
        int prompts = 0;
        String input;
        while (true) {
            reader.writePromptText(prompt);
            input = reader.prompt();
            reader.mayProcess(input);
            // only validate if no help to present
            if (!mayPromptHelp(input)) {
                // validate
                final Optional<List<String>> problems = inputValidator.validate(lastInputs, input, prompts);

                // No problems? => opt out
                if (!problems.isPresent()) {
                    break;
                }
                // print problems
                mayPrintErrors(problems.get(), input);
            }

            lastInputs.add(input);
            prompts++;
        }
        reader.setFeedbackConsumer(oldFeedbackConsumer);
        return input;
    }

}