package de.uulm.team020.server.client.console.story;

import java.util.Objects;

import de.uulm.team020.networking.core.MessageContainer;

/**
 * Data class to be returned by the {@link ConsoleStoryProcessor}.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class StoryFeedback {

    private MessageContainer container;

    /**
     * Construct a new Story-Feedback holding a container
     * 
     * @param container The container to host
     */
    public StoryFeedback(MessageContainer container) {
        this.container = Objects.requireNonNull(container);
    }

    /**
     * Construct a new Story-Feedback holding nothing
     */
    public StoryFeedback() {
        this.container = null;
    }

    public boolean hasContainer() {
        return this.container != null;
    }

    public MessageContainer getContainer() {
        return this.container;
    }

}