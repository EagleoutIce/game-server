package de.uulm.team020.server.core.dummies.story;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;

/**
 * This class extends the functionality of the {@link AbstractStoryProtagonist}
 * in a way, that it does offer direct-methods for performing operations so you
 * can transfer them to your logic in an easy way.
 * <p>
 * <b>To use the performer you must add this client to the list of Performers
 * after you have registered him!</b> Example:
 * 
 * <pre>
 * AbstractStoryHero hero = ...;
// Register this hero with the name 'saphira' ...
// If you do not want to enforce hello-calls you may use
// {@link DummyClient#fakeName(String)} before registering it
// with the same name on hello.
storyBoard.addPerformer("saphira", hero::perform);
 * </pre>
 * 
 * See the {@link AbstractStoryHeroDefaults} for a (maybe) easier
 * implementation.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/14/2020
 * 
 * @since 1.1
 */
public abstract class AbstractStoryHero extends AbstractStoryProtagonist {

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story
     * 
     * @param controller The controller this hero is to be connected to.
     */
    public AbstractStoryHero(DummyNttsController controller) {
        super(controller);
    }

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story. The client will automatically be appended to the Story-clients. You
     * may request him with the name you used on Hello.
     * 
     * @param board The {@link StoryBoard} this hero is to be connected to.
     */
    public AbstractStoryHero(StoryBoard board) {
        super(board);

    }

    /**
     * Build a new hero which may be used to bind your life-cycle to the one of a
     * story. The client will automatically be appended to the Story-clients. You
     * may request him with the name you used on Hello.
     * 
     * @param story The {@link Story} this hero is to be connected to.
     */
    public AbstractStoryHero(Story story) {
        super(story);
    }

    /**
     * This is the routine to be called on every story-step. To protect this method
     * from doing crap, it should not access the story - in any way. It shouldn't
     * change the story-board or any configuration coming with it.
     * 
     * @param lineData     Data-object that can be used for further investigation,
     *                     it's likely you won't need it to perform the operation.
     * @param targetClient The client that was targeted by the story operation, you
     *                     can use it to retrieve test-time data to validate
     *                     settings or to use this method for multiple clients. If
     *                     you do so, please make sure, the send routine will target
     *                     the correct one. <b>Important</b>: As you can not
     *                     register a handler for HELLO, this should never be null.
     * @param type         The type of the story action that occurred, you will not
     *                     get a call for every one. E.g. there is no sense in
     *                     performing a step on a 'SET' or 'ITER' call. Furthermore
     *                     you are not very likely to handle every other message
     *                     too. You can use this information to register a handler
     *                     for only 'ITEM'-calls etc.
     * @param tokens       This array contains all tokens as chosen by the
     *                     story-parser. Most of the times you won't need them or at
     *                     least will not need the first two. This is, as the tokens
     *                     will include the type-token (you will get the polished
     *                     version with the 'type'-parameter) and the name of the
     *                     client desired. To make a simple example. In
     *                     {@code ITEM hans-peter moledie} the tokens will read
     *                     <code>{"ITEM", "hans-peter", "moledie"}</code>.
     * @return It is very important you return the right value. If you return
     *         {@code true} the story-board will NOT send the request. This comes in
     *         handy, if you pass the data to your logic and your logic will send
     *         the request and the story would send the message again - which
     *         wouldn't server any greater good. So in short: Returns a flag
     *         indicating if the performer has already sent the message.
     */
    @Override
    protected boolean perform(StoryLineData lineData, DummyClient targetClient, StoryChapterType type,
            String[] tokens) {
        switch (type) {
            case CRASH:
                return crash(lineData, targetClient, tokens);
            case EQUIP:
                return equip(lineData, targetClient, tokens);
            case HELLO:
                return hello(lineData, targetClient, tokens, tokens[1], RoleEnum.valueOf(tokens[2].toUpperCase()));
            case ITEM:
                return item(lineData, targetClient, tokens);
            case LEAVE:
                return leave(lineData, targetClient, tokens);
            case META:
                return meta(lineData, targetClient, tokens);
            case OPERATION:
                return operation(lineData, targetClient, tokens);
            case PAUSE:
                return pause(lineData, targetClient, tokens);
            case RECONNECT:
                return reconnect(lineData, targetClient, tokens);
            case RESUME:
                return resume(lineData, targetClient, tokens);
            case TIMEOUT:
                return timeout(lineData, targetClient, tokens);
            default:
                return false;
        }
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
    protected abstract boolean crash(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean timeout(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean leave(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean resume(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean pause(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean reconnect(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean hello(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens, String name, RoleEnum role);

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
    protected abstract boolean item(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean equip(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean meta(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);

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
    protected abstract boolean operation(final StoryLineData lineData, final DummyClient targetClient,
            final String[] tokens);
}