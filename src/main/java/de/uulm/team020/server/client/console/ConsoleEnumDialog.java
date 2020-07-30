package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * This dialog may be used to only allow values included in an enum
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public class ConsoleEnumDialog<E extends Enum<E>> extends ConsoleDialog {

    private final Class<E> baseEnum;
    private final boolean caseSensitive;

    /**
     * This constructs a new dialog, which is case sensitive
     * 
     * @param baseEnum The enum on which values to listen to
     * 
     * @see #ConsoleEnumDialog(Class, boolean)
     */
    public ConsoleEnumDialog(Class<E> baseEnum) {
        this(baseEnum, true);
    }

    /**
     * This constructs a new dialog
     * 
     * @param baseEnum      The enum on which values to listen to
     * @param caseSensitive Should the check be case sensitive?
     */
    public ConsoleEnumDialog(Class<E> baseEnum, boolean caseSensitive) {
        super();
        this.baseEnum = baseEnum;
        this.caseSensitive = caseSensitive;

        // process possible values
        final String enumData = Arrays.stream(baseEnum.getEnumConstants()).map(Enum::toString)
                .collect(Collectors.joining(", "));
        setHelp("Possible values: @|italic " + enumData + "|@");
        setValidator(this::enumValidator);
    }

    Optional<List<String>> enumValidator(final List<String> lastInputs, final String input, final int rePromptCount) {
        final String enumGuess = caseSensitive ? input : input.toUpperCase();
        try {
            Enum.valueOf(baseEnum, enumGuess);
            return Optional.empty();
        } catch (IllegalArgumentException ex) {
            return Optional.of(List.of("@|red Input: '" + input + "' is not a valid enum constant of: "
                    + baseEnum.getSimpleName() + " (" + (caseSensitive ? "case sensitive" : "case insensitive")
                    + ").\nWrite '" + HELP + "' for more information.|@"));
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
    public E prompt(final String prompt) throws IOException {
        return Enum.valueOf(baseEnum, promptInput(prompt));
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @return The value the user entered
     * 
     * @throws IOException If there was an error reading
     * 
     * @see #prompt()
     */
    public E prompt() throws IOException {
        return Enum.valueOf(baseEnum, promptInput());
    }
}