package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * This dialog may be used to only allow correct boolean values
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public class ConsoleBooleanDialog extends ConsoleDialog {

    private final List<String> trueValues = List.of("true", "yes", "y", "ja", "j", "ok");
    private final List<String> falseValues = List.of("false", "no", "n", "nein");
    private boolean currentState;

    /**
     * This constructs a new dialog
     */
    public ConsoleBooleanDialog() {
        super();
        // process possible values
        setHelp("Accepts one of the following: " + trueValues + " for true or: " + falseValues + " for false.");
        setValidator(this::booleanValidator);
    }

    Optional<List<String>> booleanValidator(final List<String> lastInputs, final String input,
            final int rePromptCount) {
        String lowerInput = input.toLowerCase();
        if (trueValues.contains(lowerInput)) {
            currentState = true;
            return Optional.empty();
        } else if (falseValues.contains(lowerInput)) {
            currentState = false;
            return Optional.empty();
        } else {
            return Optional.of(List.of("@|red The input: '" + input
                    + "' is no truth-value. You may type ':help' for more information.|@"));
        }
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @param prompt The prompt to present
     * 
     * @return The value the user entered as parsed regex
     * 
     * @throws IOException If there was an error reading
     */
    public boolean prompt(final String prompt) throws IOException {
        promptInput(prompt);
        return currentState;
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @return The value the user entered as boolean
     * 
     * @throws IOException If there was an error reading
     * 
     * @see #prompt(String)
     */
    public boolean prompt() throws IOException {
        promptInput();
        return currentState;
    }
}