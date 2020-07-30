package de.uulm.team020.server.core.dummies.story.exceptions;

/**
 * Thrown when there is (probably) a problem with the story and the arguments
 * you have given.
 *
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 */
public class StoryArgumentException extends StoryException {

    private static final long serialVersionUID = -8726833052244235346L;

    public StoryArgumentException(String message) {
        super(message);
    }

}
