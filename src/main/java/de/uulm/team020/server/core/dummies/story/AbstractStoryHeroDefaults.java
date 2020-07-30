package de.uulm.team020.server.core.dummies.story;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;

/**
 * Basically the same class as {@link AbstractStoryHero}, but this one will
 * provide default implementations for all performer-methods.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/14/2020
 * 
 * @since 1.1
 */
public abstract class AbstractStoryHeroDefaults extends AbstractStoryHero {

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story
     * 
     * @param controller The controller this hero is to be connected to.
     */
    public AbstractStoryHeroDefaults(DummyNttsController controller) {
        super(controller);
    }

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story. The client will automatically be appended to the Story-clients. You
     * may request him with the name you used on Hello.
     * 
     * @param board The {@link StoryBoard} this hero is to be connected to.
     */
    public AbstractStoryHeroDefaults(StoryBoard board) {
        super(board);

    }

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story. The client will automatically be appended to the Story-clients. You
     * may request him with the name you used on Hello.
     * 
     * @param story The {@link Story} this hero is to be connected to.
     */
    public AbstractStoryHeroDefaults(Story story) {
        super(story);
    }

    /**
     * Called, when a client shall "crash" you probably might not <i>want</i> to
     * register any event to this performance - as you are nt able to do this in a
     * real-case scenario.
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean crash(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client experiences a timeout
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean timeout(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client should leave the game
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean leave(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client wants to resume a pause
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean resume(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client wants a pause
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean pause(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client wants to reconnect -- please keep in mind that this
     * would mean to drop all the logic - so you <i>might</i> considering dropping
     * your whole logic to be sure you re-register with the correct name and data.
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean reconnect(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client wants to register with the given data
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @param name         The name to use on registration -- can be parsed from the
     *                     tokens as well.
     * @param role         The role to use on registration -- can be parsed from the
     *                     tokens as well.
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean hello(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens,
            String name, RoleEnum role) {
        return false;
    }

    /**
     * Called, when a client wants to select an item -- you may use the story-utils
     * to decode it.
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean item(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client wants to equip items -- you may use the story-utils to
     * decode it.
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean equip(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client should request a meta-information request.
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean meta(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

    /**
     * Called, when a client should perform an operation
     * 
     * @param lineData     The data object representing the line to be executed in a
     *                     current file
     * @param targetClient The client which is to perform this action -- probably
     *                     will be this one
     * @param tokens       The tokens read in this line
     * 
     * @return In short: Returns a flag indicating if you have already sent the
     *         message (true). See
     *         {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])}
     *         for more information.
     */
    protected boolean operation(final StoryLineData lineData, final DummyClient targetClient, final String[] tokens) {
        return false;
    }

}