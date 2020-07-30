package de.uulm.team020.server.core.dummies.story.exceptions;

/**
 * Thrown when there is a problem when using a comfort-function of the story
 * which is not implemented perfectly.
 *
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class StorySillyException extends StoryException {

    private static final long serialVersionUID = 3415664162833661895L;

    /**
     * Build a new Story Silly exception
     * 
     * @param message
     */
    public StorySillyException(String message) {
        super(message);
    }

}
