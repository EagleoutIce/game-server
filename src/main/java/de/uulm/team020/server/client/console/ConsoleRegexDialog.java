package de.uulm.team020.server.client.console;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This dialog may be used to only allow (string) values that suffice a given
 * Regex-Pattern
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public class ConsoleRegexDialog extends ConsoleDialog {

    private final Pattern regexPattern;
    private Matcher currentMatcher;

    /**
     * This constructs a new dialog
     * 
     * @param pattern The pattern to use for validation
     */
    public ConsoleRegexDialog(final Pattern pattern) {
        super();
        this.regexPattern = pattern;
        // process possible values
        setHelp("Accepts all Data matching: " + pattern.toString());
        setValidator(this::regexValidator);
    }

    /**
     * This constructs a new dialog
     * 
     * @param patternString The pattern to use for validation
     */
    public ConsoleRegexDialog(final String patternString) {
        this(Pattern.compile(patternString));
    }

    Optional<List<String>> regexValidator(final List<String> lastInputs, final String input, final int rePromptCount) {
        currentMatcher = regexPattern.matcher(input);
        if (input != null && currentMatcher.matches()) {
            return Optional.empty();
        } else {
            return Optional.of(List.of(
                    "@|red The input: '" + input + "' does not match the pattern: " + regexPattern.toString() + ".|@"));
        }
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @param prompt Text to be capsuled with the console reader prompt setting
     * 
     * @return The value the user entered as parsed regex
     * 
     * @throws IOException If there was an error reading
     */
    public Matcher prompt(final String prompt) throws IOException {
        promptInput(prompt);
        return currentMatcher;
    }

    /**
     * Will prompt the user and return the given constant if valid
     * 
     * @return The value the user entered as parsed regex
     * 
     * @throws IOException If there was an error reading
     * 
     * @see #prompt(String)
     */
    public Matcher prompt() throws IOException {
        promptInput();
        return currentMatcher;
    }
}