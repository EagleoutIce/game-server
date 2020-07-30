package de.uulm.team020.server.core.dummies.story.exceptions;

/**
 * Thrown when there is (probably) a problem with the story.
 *
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public class StoryException extends RuntimeException {

    private static final long serialVersionUID = 7899522228998981090L;

    /**
     * Create a new Story Exception
     * 
     * @param message The message to pack with the exception to give more
     *                information about the reason
     */
    public StoryException(final String message) {
        super(message);
    }

    /**
     * Create a new Story Exception
     * 
     * @param message The message to pack with the exception to give more
     *                information about the reason
     * @param cause   The original cause of this exception
     */
    public StoryException(final String message, final Throwable cause) {
        super(message, cause, false, true);
    }

}
