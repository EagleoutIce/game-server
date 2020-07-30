package de.uulm.team020.server.client.console;

import java.util.List;
import java.util.Optional;

/**
 * This interface is used by the {@link ConsoleDialog} class to provide a
 * default way of validating input
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
@FunctionalInterface
public interface IDialogInputValidator {

    /**
     * Validates a given input against custom constraints
     * 
     * @param lastInputs    List of possible last inputs, may only be useful if
     *                      rePromptCount &gt; 0
     * @param input         The current input this validation was called for
     * @param rePromptCount The number of times to user messed up and was
     *                      re-prompted
     * @return A List containing all errors -- if the input was not valid. If you do
     *         not want to prompt the errors to the user, just return an empty list.
     */
    Optional<List<String>> validate(final List<String> lastInputs, final String input, final int rePromptCount);

    /**
     * This one may be used if an {@link IDialogInputValidator} is requested, but
     * not validation is desired.
     * 
     * @param lastInputs    ignored
     * @param input         ignored
     * @param rePromptCount ignored
     * @return Empty optional -- always succeeds according to the specification
     */
    static Optional<List<String>> trueValidator(final List<String> lastInputs, final String input,
            final int rePromptCount) {
        return Optional.empty();
    }

}