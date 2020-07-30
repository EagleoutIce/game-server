package de.uulm.team020.server.core.dummies.story.exceptions.operation;

import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;

/**
 * Thrown if there is an error executing a Story-Operation
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StoryOperationException extends StoryException {

    private static final long serialVersionUID = 5805565466557307859L;

    private final StoryOperationExceptionType type;

    public StoryOperationException(StoryOperationExceptionType type, String message) {
        super(message);
        this.type = type;
    }

    public StoryOperationExceptionType getType() {
        return this.type;
    }

}