package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This dialog may be used as a simple dialog option
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class ConsoleSimpleDialog extends ConsoleDialog {

    private final Predicate<String> validation;

    /**
     * This constructs a new dialog
     * 
     * @param validation The method to be called for validation
     * @param help       The help text to be displayed
     */
    public ConsoleSimpleDialog(final Predicate<String> validation, String help) {
        super();
        this.validation = validation;
        // process possible values
        setHelp(help);
        setValidator(this::simpleValidator);
    }

    Optional<List<String>> simpleValidator(final List<String> lastInputs, final String input, final int rePromptCount) {
        if (validation.test(input)) {
            return Optional.empty();
        } else {
            return Optional.of(List.of("@|red You input: '" + input
                    + "' does not satisfy the constraints. Type ':help'  for more information.|@"));
        }
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @param prompt Text to be capsuled with the console reader prompt setting
     * 
     * @return The value the user entered
     * 
     * @throws IOException If there was an error reading
     */
    public String prompt(final String prompt) throws IOException {
        return promptInput(prompt);
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @return The value the user entered
     * 
     * @throws IOException If there was an error reading
     * 
     * @see #prompt(String)
     */
    public String prompt() throws IOException {
        return promptInput();
    }
}