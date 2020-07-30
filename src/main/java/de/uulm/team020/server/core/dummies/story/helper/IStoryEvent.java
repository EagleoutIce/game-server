package de.uulm.team020.server.core.dummies.story.helper;

import de.uulm.team020.server.core.dummies.story.Story;

/**
 * Executes a line of events in a story
 *
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
@FunctionalInterface
public interface IStoryEvent {
    /**
     * Called every line to add to the story
     *
     * @param story  the story
     * @param tokens tokens in the line, first will always be the executing keyword
     */
    void execute(Story story, String[] tokens);
}
