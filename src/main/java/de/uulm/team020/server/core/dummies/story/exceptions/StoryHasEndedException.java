package de.uulm.team020.server.core.dummies.story.exceptions;

/**
 * Thrown when there is (probably) a problem with the story.
 *
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StoryHasEndedException extends StoryException {

    private static final long serialVersionUID = 1674910486620257092L;

    public StoryHasEndedException(String message) {
        super(message);
    }

}
