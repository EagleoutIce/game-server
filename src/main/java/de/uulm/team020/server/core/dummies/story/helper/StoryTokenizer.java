package de.uulm.team020.server.core.dummies.story.helper;

import java.util.ArrayList;
import java.util.List;

import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryTokenizeException;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Tokenizer used by the {@link StoryBoard} to split a String to an Array of
 * Tokens
 * 
 * @author Florian Sihler
 * @version 1.0c, 07/07/2020
 */
public class StoryTokenizer {

    /**
     * Construct a new tokenizer
     */
    public StoryTokenizer() {
        // Nothing to be done here
    }

    private enum ParserState {
        READ_NORMAL, READ_QUOTED, READ_SKIP_SPACES // Allows easy-multiple spaces to split
    }

    /**
     * Tokenizes a string. This will raise expansion-levels by one
     * 
     * @param string The String to tokenize
     * 
     * @return All detected tokens
     * 
     * @see #tokenize(String, boolean)
     */
    public String[] tokenize(String string) {
        return tokenize(string, true);
    }

    /**
     * Tokenizes a string
     * 
     * @param string   The String to tokenize
     * @param unescape Shall nested, escaped quotes should be 'unescaped?' This will
     *                 only effect 'one' level
     * 
     * @return All detected tokens
     */
    public String[] tokenize(String string, boolean unescape) {
        // collect all tokens in a list
        List<String> tokens = new ArrayList<>(3);
        // constructing the current token to be added to tokens
        int curTokenStart = 0;
        int curTokenIndex = 0;
        // State determines the 'end' we are looking for
        ParserState state = ParserState.READ_SKIP_SPACES;

        // is this character escaped?
        // As this tokenizer allows 'multiple' escapes
        // The String "\\\\"" will see the '"' as escaped
        // but in the second level - this allows to carry nested
        // escaped strings - which allows easier readability
        boolean isEscaped = false;
        boolean printQuote = false; // should a quote be printed or gobbled?
        // just for giving context in case of unbalanced quotes
        int lastQuote = 0;

        // Changed from array as considered slower
        final int length = string.length();
        for (int i = 0; i < length; i++) {
            final char c = string.charAt(i);

            if (state == ParserState.READ_SKIP_SPACES) {
                // Skipping white-spaces
                if (Character.isWhitespace(c)) {
                    curTokenStart = i + 1; // reset token start
                    continue;
                } else {
                    // reached a character, pretend it's friendly
                    state = ParserState.READ_NORMAL;
                }
            }

            // If we are escaped by '\' we will just add the
            // this holds true even if we have '\\'.
            if (isEscaped) {
                isEscaped = false;
                continue; // next token
            }

            // We are terminating on a space and do consume
            // escaped strings
            if (state == ParserState.READ_NORMAL) {
                switch (c) {
                    case '"':
                        // String started
                        state = ParserState.READ_QUOTED;
                        lastQuote = i;
                        if (i - curTokenStart > 0) {
                            // if we have string-starts inside of an already existing token
                            // we do not want to gobble them!
                            // There is no need to escape them, they will guard themselves
                            printQuote = true;
                        } else {
                            // as we are at start: increment index:
                            curTokenStart = i + 1;
                            printQuote = false;
                        }
                        continue;
                    case '\\':
                        // We have an escape-sequence
                        isEscaped = true;
                        break; // Append
                    default:
                }
                // We are in read-normal => space breaks
                if (Character.isWhitespace(c)) {
                    // Build current-Tokens:
                    addToCurrentToken(tokens, string, curTokenIndex, curTokenStart, i);
                    curTokenStart = i + 1; // reset token start
                    curTokenIndex += 1;
                    state = ParserState.READ_SKIP_SPACES;
                }
                // Just add it, there is no reason in change the increment
            } else if (state == ParserState.READ_QUOTED) {
                switch (c) {
                    // End escape:
                    case '"':
                        state = ParserState.READ_NORMAL;
                        // There is no need to cash here, we will
                        // buffer on space and buffer the last,
                        // if there is no space -- this allows
                        // to detect unbalanced ticks as well
                        if (printQuote) {
                            printQuote = false;
                        } else { // Do not print quote by wrapping around
                            addToCurrentToken(tokens, string, curTokenIndex, curTokenStart, i);
                            curTokenStart = i + 1; // reset token start
                        }
                        continue;
                    case '\\':
                        // We have an escape-sequence
                        isEscaped = true;
                        // if we are in printQuote-mode we do not unescape, no matter what
                        if (!printQuote && unescape && i < length - 1) { // there is a next character
                            char nxtChar = string.charAt(i + 1);
                            if (nxtChar == '"' || nxtChar == '\\') {
                                // only keep if no string
                                // We will dump the current string without updating the current index
                                addToCurrentToken(tokens, string, curTokenIndex, curTokenStart, i);
                                curTokenStart = i + 1; // reset token start by skipping the next one
                            }
                        }
                        // If this is not the case we are at the end and have an escaped-sq
                        // hanging around mid-air, but as we are in quoted mode this is forbidden
                        // So we could throw the error here as well....
                        break; // Append
                    default:
                }
                // Just append as it is a non-ending quote
            } else {
                throw new ThisShouldNotHappenException("Tokenizer in invalid state: " + state);
            }
        }

        // End-addition
        mayCollectLastToken(string, tokens, curTokenStart, curTokenIndex, state, lastQuote, length);
        return tokens.toArray(String[]::new);
    }

    private void mayCollectLastToken(String string, List<String> tokens, int curTokenStart, int curTokenIndex,
            ParserState state, int lastQuote, final int length) {
        if (state == ParserState.READ_NORMAL) {
            addToCurrentToken(tokens, string, curTokenIndex, curTokenStart, length);
        } else if (state == ParserState.READ_QUOTED) {
            throw new StoryTokenizeException(
                    "The String started on column: " + lastQuote + " was not terminated in: " + string);
        }
    }

    private void addToCurrentToken(List<String> tokens, final String string, final int idx, final int from,
            final int to) {
        if (tokens.size() <= idx) { // new
            tokens.add(string.substring(from, to));
        } else { // append
            tokens.set(idx, tokens.get(idx) + string.substring(from, to));
        }
    }

    /**
     * Escapes a String - to be clear, this escapes the string for the tokenizer and
     * does not perform any other escape-mechanism. If the string contains
     * problematic spaces, there will be quotes around the string
     * 
     * @param string The string to escape
     * @return The escaped string
     */
    public static String escape(final String string) {
        final int length = string.length();
        StringBuilder builder = new StringBuilder(length);

        int escapeLevel = 0;
        boolean hasUnguardedSpace = false;
        boolean inString = false;

        for (int i = 0; i < length; i++) {
            final char c = string.charAt(i);
            switch (c) {
                case '\\':
                    escapeLevel++;
                    continue;
                case '"':
                    builder.append('\\');
                    if (escapeLevel == 0) {
                        inString = !inString; // toggle string
                    }
                    break;
                default:
            }
            // is it problematic?
            if (!inString && Character.isWhitespace(c)) {
                hasUnguardedSpace = true;
            }

            if (escapeLevel == 0) {
                builder.append(c);
            } else {
                escapeLevel *= 2; // double length to perform escape
                for (; escapeLevel > 0; escapeLevel--) {
                    builder.append('\\');
                }
                builder.append(c);
            }

        }

        return guardWithSpaceIfNeeded(string, builder, hasUnguardedSpace);
    }

    private static String guardWithSpaceIfNeeded(final String string, StringBuilder builder,
            boolean hasUnguardedSpace) {
        if (hasUnguardedSpace) {
            return "\"" + builder.append("\"").toString();
        } else {
            return string;// There is no need for further escapes
        }
    }
}