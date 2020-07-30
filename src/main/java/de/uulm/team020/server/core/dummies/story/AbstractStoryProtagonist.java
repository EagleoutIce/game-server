package de.uulm.team020.server.core.dummies.story;

import de.uulm.team020.server.core.NttsServer;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;

/**
 * This class was built to support the testing of your own client-logic. you do
 * not need to use it - it will not be treated differently by the
 * {@link StoryBoard} or the {@link BackstoryBuilder}. It may be used to easily
 * bind the life-cycle of your test-client to the storys'.
 * <p>
 * <b>To use
 * {@link #perform(StoryLineData, DummyClient, StoryChapterType, String[])} you
 * must add this client to the list of Performers after you have registered
 * him!</b> Example:
 * 
 * <pre>
 * AbstractStoryProtagonist protagonist = ...;
// Register this protagonist with the name 'saphira' ...
// If you do not want to enforce hello-calls you may use
// {@link DummyClient#fakeName(String)} before registering it
// with the same name on hello.
storyBoard.addPerformer("saphira", protagonist::perform);
 * </pre>
 * 
 * @author Florian Sihler
 * @version 1.0, 05/14/2020
 * 
 * @since 1.1
 */
public abstract class AbstractStoryProtagonist {

    protected final DummyNttsController controller;
    protected final DummyClient client;

    /**
     * Build a new protagonist which may be used to bind your life-cycle to the one
     * of a story. Please not that this method should not be called -- it is to be
     * used if you do not like performer or something similar.
     * 
     * @param controller The controller this protagonist is to be connected to.
     */
    protected AbstractStoryProtagonist(final DummyNttsController controller) {
        this.controller = controller;
        this.client = new DummyClient(controller, this::onMessage, this::onClose);
    }

    /**
     * Build a new protagonist which may be used to bind your life-cycle to the one
     * of a story. The client will automatically be appended to the Story-clients.
     * You may request him with the name you used on Hello.
     * 
     * @param board The {@link StoryBoard} this protagonist is to be connected to.
     */
    public AbstractStoryProtagonist(final StoryBoard board) {
        this(board.getStory());
        board.addClient(this.getClient());
    }

    /**
     * Build a new protagonist which may be used to bind your life-cycle to the one
     * of a story. The client will automatically be appended to the Story-clients.
     * You may request him with the name you used on Hello.
     * 
     * @param story The {@link Story} this protagonist is to be connected to.
     */
    public AbstractStoryProtagonist(final Story story) {
        this(story.getController());
        story.addClient(this.getClient());
    }

    /**
     * Will be called, whenever a message is received from the server.
     * 
     * @param message The message received from the server.
     */
    protected abstract void onMessage(String message);

    /**
     * Will be called, when the connection got closed
     * 
     * @param code    Close code, may be decoded by
     *                {@link NttsServer#closeStatusDecode(int)}. The code will be
     *                {@link DummyClient#ARTIFICIAL_CLOSE}, if the close was
     *                enforced by the testing-framework with no specified reason.
     * @param message The message passed with it
     */
    protected abstract void onClose(final int code, final String message);

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
    protected abstract boolean perform(final StoryLineData lineData, final DummyClient targetClient,
            final StoryChapterType type, final String[] tokens);

    /**
     * Sends the Message to the server -- this will not send the message, if the
     * connection is already closed.
     * 
     * @param message The message to send.
     */
    public void sendMessage(final String message) {
        this.client.send(message);
    }

    /**
     * @return The effective logic-controller the protagonist is connected to
     */
    public DummyNttsController getController() {
        return controller;
    }

    /**
     * @return The client which represents this Protagonist on the test-level.
     */
    public DummyClient getClient() {
        return client;
    }

    /**
     * Clear the buffer of the underlying client. {@link #clearMessages()}
     */
    public void eraseMemory() {
        clearMessages();
    }

    /**
     * Clear the buffer of the underlying client.
     */
    public void clearMessages() {
        this.client.clearMessages();
    }

    /**
     * Get the buffer of the underlying client.
     * 
     * @return The embedded message buffer
     */
    public DummySendMessagesBuffer getMessages() {
        return this.client.getMessages();
    }

    /**
     * Get the connection the server holds for this client. This might be useless,
     * if the client did not register yet.
     * 
     * @return The attached connection
     */
    public NttsClientConnection getConnection() {
        return this.client.getConnection();
    }
}