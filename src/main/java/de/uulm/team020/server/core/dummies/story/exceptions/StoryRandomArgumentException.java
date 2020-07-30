package de.uulm.team020.server.core.dummies.story.exceptions;

import de.uulm.team020.server.addons.random.RandomController;

/**
 * Thrown when the {@link RandomController} encounters an invalid argument
 *
 * @author Florian Sihler
 * @version 1.0, 06/07/2020
 */
public class StoryRandomArgumentException extends StoryException {

    private static final long serialVersionUID = -8726833052244235346L;

    /**
     * Build a new random-argument exception
     * 
     * @param message The message to ship with it
     */
    public StoryRandomArgumentException(String message) {
        super(message);
    }

}
