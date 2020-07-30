package de.uulm.team020.server.game.phases;

import org.java_websocket.WebSocket;

import de.uulm.team020.server.addons.strikes.StrikeController;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.NttsClientManager;
import de.uulm.team020.server.core.NttsMessageSender;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;

/**
 * This is the main blueprint to be used by any phase controller. This splits
 * the server-phases into (at least):
 * <ul>
 * <li>drafting phase</li>
 * <li>main game phase</li>
 * </ul>
 * 
 * The session-controller will work differently, as it is integrated into the
 * main network controller.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
public abstract class AbstractPhaseController {

    protected final NttsClientManager clientManager;
    protected final Configuration configuration;
    protected final NttsMessageSender messageSender;
    protected final StoryAuthor author;

    protected StrikeController strikeController;

    protected boolean active;

    /**
     * Construct a new phase controller and supply it with the necessary data
     * 
     * @param configuration    The configuration to be used
     * @param messageSender    The handler for sending the messages
     * @param clientManager    The manager overseeing client Management
     * @param author           The author to be used
     * @param strikeController The strike controller to maintain
     */
    public AbstractPhaseController(final Configuration configuration, final NttsMessageSender messageSender,
            final NttsClientManager clientManager, final StoryAuthor author, final StrikeController strikeController) {
        this.clientManager = clientManager;
        this.configuration = configuration;
        this.messageSender = messageSender;
        this.author = author;
        this.strikeController = strikeController;

        active = false;
    }

    /**
     * Called to start the phase, it is inactive on construction
     */
    public abstract void startPhase();

    /**
     * Called to end a phase, this can implicitly
     */
    public abstract void endPhase();

    /**
     * Called to pause the phase - this means that all timeouts are to be paused
     * 
     * @return Can be used as a success-flag, true means success, false failure, but
     *         this doesn't has to be
     */
    public abstract boolean pause();

    /**
     * Called to resume a paused phase - this means that all timeouts are to be
     * resumed
     * 
     * @return Can be used as a success-flag, true means success, false failure, but
     *         this doesn't has to be
     */
    public abstract boolean resume();

    /**
     * Ensures the correct attachment
     * 
     * @param conn the connection to check
     * 
     * @throws HandlerException if the cast fails for any non-connection reason
     * 
     * @return the attachment, exception otherwise
     */
    protected static NttsClientConnection assertNttsClientConnection(final WebSocket conn) throws HandlerException {
        final NttsClientConnection connection = conn.getAttachment();
        if (connection == null)
            throw new IllegalMessageException("The first message has to be HELLO, there is no connection attached...");
        return connection;
    }
}