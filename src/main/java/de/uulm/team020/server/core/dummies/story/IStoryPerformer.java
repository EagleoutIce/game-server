package de.uulm.team020.server.core.dummies.story;

import de.uulm.team020.server.core.dummies.DummyClient;

/**
 * This interface is used by the {@link StoryBoard} to allow hooks to be called
 * in case of a story execution. this means you can perform replay-operations on
 * client-level.
 * <p>
 * Let's say your story is
 * 
 * <pre>
 *HELLO dieter PLAYER
 *# ...
 *ITEM dieter bowler_blade
 * </pre>
 * 
 * This interface will be used to call your client logic so you can perform all
 * necessary actions to perform the operation 'choosing bowler_blade' yourself.
 *
 * 
 * Note that you can only perform upon actions tagged with
 * {@link StoryChapterType#canBePerformed()} (so the value is {@code true}).
 * 
 * @author Florian Sihler
 * @version 1.0, 04/13/2020
 */
@FunctionalInterface
public interface IStoryPerformer {

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
    boolean perform(final StoryLineData lineData, final DummyClient targetClient, final StoryChapterType type,
            final String[] tokens);
}