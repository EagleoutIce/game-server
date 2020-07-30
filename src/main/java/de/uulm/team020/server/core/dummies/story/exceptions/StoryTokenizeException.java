package de.uulm.team020.server.core.dummies.story.exceptions;

/**
 * Thrown when there is (probably) a problem with the story-tokenize process
 *
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StoryTokenizeException extends StoryException {

    private static final long serialVersionUID = 1674910486620257092L;

    public StoryTokenizeException(String message) {
        super(message);
    }

}
