package de.uulm.team020.server.core.dummies.story.exceptions;

import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;

/**
 * Thrown when there is a problem when building a Backstory via the:
 * {@link BackstoryBuilder}.
 *
 * @author Florian Sihler
 * @version 1.0, 05/06/2020
 */
public class BackstoryException extends StoryException {

    private static final long serialVersionUID = 1674910486620257092L;

    public BackstoryException(String message) {
        super(message);
    }

}
