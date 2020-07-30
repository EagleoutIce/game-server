package de.uulm.team020.server.core.dummies;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import javax.net.ssl.SSLSession;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.enums.Opcode;
import org.java_websocket.enums.ReadyState;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.framing.Framedata;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.helper.ICloseConsumer;

/**
 * Used by the {@link DummyNttsController} to handle 'fake' websocket sends.
 * Currently i do not tend to supply meaning to any of this utils.
 * <p>
 * I've implemented close handling, equals and the 'barebones'. Please note,
 * this does not mean this is a full replacement.
 *
 * @author Florian Sihler
 * @version 1.4, 05/14/2020
 */
public class DummyClient implements WebSocket {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Story");

    public static final int ARTIFICIAL_CLOSE = -42;

    private boolean isClosed = false;
    private final UUID internalReference = UUID.randomUUID();
    private Object attachment;
    private String fakeName = null;

    private ICloseConsumer closeConsumer;

    /**
     * Holds all received messages
     */
    protected DummySendMessagesBuffer gotMessages;

    private DummyNttsController controller;

    /**
     * Build a new dummy client which is connected to the given controller
     * ("server").
     * 
     * @param controller The server-controller to connect this client to
     */
    public DummyClient(final DummyNttsController controller) {
        init(controller, new DummySendMessagesBuffer(this), null);
    }

    /**
     * Build a new dummy client which is connected to the given controller
     * ("server"), furthermore it allows to register a handler when receiving
     * messages.
     * 
     * @param controller The server-controller to connect this client to
     * @param onChange   Consumer which will be called, whenever this clients
     *                   receives a message
     */
    public DummyClient(final DummyNttsController controller, final Consumer<String> onChange) {
        init(controller, new DummySendMessagesBuffer(this, onChange), null);
    }

    /**
     * Build a new dummy client which is connected to the given controller
     * ("server"), furthermore it allows to register a handler when receiving
     * messages.
     * 
     * @param controller The server-controller to connect this client to
     * @param onChange   Consumer which will be called, whenever this clients
     *                   receives a message
     * @param onClose    Consumer which will be called, whenever this connection
     *                   gets closed.
     * 
     * @since 1.1
     */
    public DummyClient(final DummyNttsController controller, final Consumer<String> onChange,
            final ICloseConsumer onClose) {
        init(controller, new DummySendMessagesBuffer(this, onChange), onClose);
    }

    private void init(final DummyNttsController controller, final DummySendMessagesBuffer buffer,
            final ICloseConsumer onClose) {
        this.controller = controller;
        gotMessages = buffer;
        this.closeConsumer = onClose;
        controller.handleOpenFor(this); // hey ho
        if (controller.getConfiguration().getServerConfig().sendMetaOnConnectionOpen()) {
            assureMessages(1, 250); // initial meta if desired
        }
    }

    /**
     * Simple delegate for the size of the message-buffer
     * 
     * @return Value of {@link DummySendMessagesBuffer#size()}
     */
    public int getMessageSize() {
        synchronized (this.gotMessages) {
            return this.gotMessages.size();
        }
    }

    /**
     * Sets or changes the consumer to be called on change, set this to 'null' to
     * remove any present consumer
     * 
     * @param onChange the consumer to be called
     * 
     * @return The Message-Buffer for chaining, if desired
     */
    public DummySendMessagesBuffer setConsumer(final Consumer<String> onChange) {
        return this.gotMessages.setConsumer(onChange);
    }

    /**
     * Sets or changes the consumer to be called on close, set this to 'null' to
     * remove any present consumer
     * 
     * @param onClose the consumer to be called on close
     * 
     * @since 1.1
     */
    public void setCloseConsumer(final ICloseConsumer onClose) {
        this.closeConsumer = onClose;
    }

    /**
     * This method will return the clientId of the underlying controller, just to
     * have fun :D
     * 
     * @return The clientId if already assigned, null otherwise
     */
    public UUID getClientId() {
        final NttsClientConnection connection = getAttachment();
        return connection == null ? null : connection.getClientId();
    }

    /**
     * Set a new fake name - do not use this method if you do not now what a fake
     * name is used for. This does not alter the name on server side.
     * 
     * @param name The fake name you want to use
     */
    public void fakeName(final String name) {
        this.fakeName = name;
    }

    /**
     * Returns the fakeName if set, or the real name. Do not call, if you do not
     * know, what a fake name is.
     * 
     * @return the evaluation name
     * @see #fakeName(String)
     */
    public String getName() {
        if (this.fakeName == null) {
            return getConnection() == null ? null : getConnection().getClientName();
        } else {
            return this.fakeName;
        }
    }

    /**
     * Just a dummy-variant for not having to do direct conversions outside.
     * 
     * @return The connection if present.
     */
    public NttsClientConnection getConnection() {
        return this.getAttachment();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DummyClient))
            return false;
        final DummyClient that = (DummyClient) o;
        return Objects.equals(internalReference, that.internalReference);
    }

    /**
     * Set closed if there is an artificial reason, like a crash This will
     * <i>not</i> call the close-handler!
     */
    public void setClosed() {
        this.isClosed = true;
        if (this.closeConsumer != null) {
            this.closeConsumer.onClose(DummyClient.ARTIFICIAL_CLOSE, "Closed artificially by the story.");
        }
    }

    /**
     * sends the closing handshake. may be send in response to an other handshake.
     *
     * @param code    the closing code
     * @param message the closing message
     */
    @Override
    public void close(final int code, final String message) {
        isClosed = true;
        // "send" the close message to be used with the Dummy client
        controller.handleCloseFor(this, code, message, true);
        if (this.closeConsumer != null) {
            this.closeConsumer.onClose(code, message);
        }
    }

    /**
     * sends the closing handshake. may be send in response to an other handshake.
     *
     * @param code the closing code
     */
    @Override
    public void close(final int code) {
        close(code, "");
    }

    /**
     * Convenience function which behaves like close(CloseFrame.NORMAL)
     */
    @Override
    public void close() {
        close(CloseFrame.NORMAL);
    }

    /**
     * This will close the connection immediately without a proper close handshake.
     * The code and the message therefore won't be transferred over the wire also
     * they will be forwarded to onClose/onWebsocketClose.
     *
     * @param code    the closing code
     * @param message the closing message
     **/
    @Override
    public void closeConnection(final int code, final String message) {
        this.isClosed = true;
    }

    /**
     * Send Text data to the other end.
     *
     * @param text the text data to send
     */
    @Override
    public void send(final String text) {
        if (isClosed) {
            throw new StoryException("Cannot send: '" + text + "' as socket is already closed.");
        }
        controller.simulateMessageReceive(this, text);
    }

    /**
     * Send Binary data (plain bytes) to the other end.
     *
     * @param bytes the binary data to send
     * @throws IllegalArgumentException the data is null
     */
    @Override
    public void send(final ByteBuffer bytes) {
        send(new String(bytes.array(), StandardCharsets.UTF_8));
    }

    /**
     * Send Binary data (plain bytes) to the other end.
     *
     * @param bytes the byte array to send
     * @throws IllegalArgumentException the data is null
     */
    @Override
    public void send(final byte[] bytes) {
        send(new String(bytes, StandardCharsets.UTF_8));
    }

    /**
     * Send a frame to the other end
     *
     * @param frameData the frame to send to the other end
     */
    @Override
    public void sendFrame(final Framedata frameData) {
        send(frameData.getPayloadData());
    }

    /**
     * Send a collection of frames to the other end
     *
     * @param frames the frames to send to the other end
     */
    @Override
    public void sendFrame(final Collection<Framedata> frames) {
        frames.forEach(f -> send(f.getPayloadData()));
    }

    /**
     * Send a ping to the other end
     */
    @Override
    public void sendPing() {
        // Not to be implemented
    }

    /**
     * Allows to send continuous/fragmented frames conveniently. <br>
     * For more into on this frame type see
     * http://tools.ietf.org/html/rfc6455#section-5.4<br>
     * <p>
     * If the first frame you send is also the last then it is not a fragmented
     * frame and will received via onMessage instead of onFragmented even though it
     * was send by this method.
     *
     * @param op     This is only important for the first frame in the sequence.
     *               Opcode.TEXT, Opcode.BINARY are allowed.
     * @param buffer The buffer which contains the payload. It may have no bytes
     *               remaining.
     * @param fin    The finishing flag for the controller
     **/
    @Override
    public void sendFragmentedFrame(final Opcode op, final ByteBuffer buffer, final boolean fin) {
        // Not to be implemented
    }

    /**
     * Checks if the websocket has buffered data
     *
     * @return has the websocket buffered data
     */
    @Override
    public boolean hasBufferedData() {
        return false;
    }

    /**
     * Returns the address of the endpoint this socket is connected to,
     * or{@code null} if it is unconnected.
     *
     * @return never returns null
     */
    @Override
    public InetSocketAddress getRemoteSocketAddress() {
        return new InetSocketAddress(0);
    }

    /**
     * Returns the address of the endpoint this socket is bound to.
     *
     * @return never returns null
     */
    @Override
    public InetSocketAddress getLocalSocketAddress() {
        return new InetSocketAddress(0);
    }

    /**
     * Is the websocket in the state OPEN
     *
     * @return state equals ReadyState.OPEN
     */
    @Override
    public boolean isOpen() {
        return !isClosed;
    }

    /**
     * Is the websocket in the state CLOSING
     *
     * @return state equals ReadyState.CLOSING
     */
    @Override
    public boolean isClosing() {
        return false; // no real transfer
    }

    /**
     * Returns true when no further frames may be submitted<br>
     * This happens before the socket connection is closed.
     *
     * @return true when no further frames may be submitted
     */
    @Override
    public boolean isFlushAndClose() {
        return isClosed();
    }

    /**
     * Is the websocket in the state CLOSED
     *
     * @return state equals ReadyState.CLOSED
     */
    @Override
    public boolean isClosed() {
        return this.isClosed;
    }

    /**
     * Getter for the draft
     *
     * @return the used draft
     */
    @Override
    public Draft getDraft() {
        return new Draft_6455();
    }

    /**
     * Retrieve the WebSocket 'ReadyState'. This represents the state of the
     * connection. It returns a numerical value, as per W3C WebSockets specs.
     *
     * @return Returns '0 = CONNECTING', '1 = OPEN', '2 = CLOSING' or '3 = CLOSED'
     */
    @Override
    public ReadyState getReadyState() {
        return null;
    }

    /**
     * Returns the HTTP Request-URI as defined by
     * http://tools.ietf.org/html/rfc2616#section-5.1.2<br>
     * If the opening handshake has not yet happened it will return null.
     *
     * @return Returns the decoded path component of this URI.
     **/
    @Override
    public String getResourceDescriptor() {
        return null;
    }

    /**
     * Setter for an attachment on the socket connection. The attachment may be of
     * any type.
     *
     * @param attachment The object to be attached to the user
     * @since 1.3.7
     **/
    @Override
    public <T> void setAttachment(final T attachment) {
        this.attachment = attachment;
    }

    /**
     * Getter for the connection attachment.
     *
     * @return Returns the user attachment
     * @since 1.3.7
     **/
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttachment() {
        return (T) this.attachment;
    }

    /**
     * Does this websocket use an encrypted (wss/ssl) or unencrypted (ws) connection
     *
     * @return true, if the websocket does use wss and therefore has a SSLSession
     * @since 1.4.1
     */
    @Override
    public boolean hasSSLSupport() {
        return false;
    }

    /**
     * Returns the ssl session of websocket, if ssl/wss is used for this instance.
     *
     * @return the ssl session of this websocket instance
     * @throws IllegalArgumentException the underlying channel does not use ssl (use
     *                                  hasSSLSupport() to check)
     * @since 1.4.1
     */
    @Override
    public SSLSession getSSLSession() throws IllegalArgumentException {
        return null;
    }

    /**
     * Note: this might not return anything, as the server might haven't finished
     * handling the message. If you want to wait, either wait some time, or use
     * {@link #getMessages(int)}.
     *
     * @return The send buffer
     */
    public DummySendMessagesBuffer getMessages() {
        synchronized (this.gotMessages) {
            return gotMessages;
        }
    }

    /**
     * Will lock the current thread, until 'n' messages occurred or the (total)
     * Timeout has passed.
     *
     * @param n           number of Messages
     * @param timeoutInMs timeout to wait for (has to be &gt;= n)
     *
     * @return True, if the amount of messages was received in total, false
     *         otherwise
     */
    public boolean waitForNewMessages(final int n, final int timeoutInMs) {
        final int s = gotMessages.size();
        int counter = 0;
        while (counter < n) {
            synchronized (gotMessages) {
                try {
                    gotMessages.wait(timeoutInMs / n);
                } catch (final InterruptedException ex) {
                    magpie.writeException(ex, "Get");
                }
            }
            counter += 1;
        }
        return n == (gotMessages.size() - s);
    }

    /**
     * Will lock the current thread, until a message of the type 'type' arrives, or
     * the timeout triggers.
     *
     * @param type        The type of the message awaited
     * @param tries       Number of messages allowed in-between
     * @param timeoutInMs Timeout to wait for (has to be &gt;= n)
     *
     * @return True, if the amount of messages was received in total, false
     *         otherwise. Currently quite expensive, might be optimized later.
     */
    public boolean awaitForMessageType(final MessageTypeEnum type, final int tries, final int timeoutInMs) {
        return awaitForMessageType(type, tries, timeoutInMs, 0);
    }

    /**
     * Will lock the current thread, until a message of the type 'type' arrives, or
     * the timeout triggers.
     *
     * @param type        The type of the message awaited
     * @param tries       Number of messages allowed in-between
     * @param timeoutInMs Timeout to wait for (has to be &gt;= n)
     * @param offset      Start of the search-buffer to find the new message type
     *
     * @return True, if the amount of messages was received in total, false
     *         otherwise. Currently quite expensive, might be optimized later.
     */
    public boolean awaitForMessageType(final MessageTypeEnum type, final int tries, final int timeoutInMs,
            final int offset) {
        if (gotMessages.containsType(type, offset)) // already got
            return true;
        int counter = 0;
        while (counter < tries) {
            synchronized (gotMessages) {
                try {
                    gotMessages.wait(timeoutInMs / tries);
                } catch (final InterruptedException ex) {
                    magpie.writeException(ex, "Get");
                }
            }
            if (gotMessages.containsType(type, offset)) {
                return true;
            }
            counter += 1;
        }
        return gotMessages.containsType(type, offset);
    }

    /**
     * Same as waitForNewMessages, but will use the Total message count
     *
     * @param n           number of Messages
     * @param timeoutInMs timeout to wait for (has to be &gt;= n)
     *
     * @return True, if the amount of messages was received in total, false
     *         otherwise
     */
    public DummySendMessagesBuffer assureMessages(final int n, final int timeoutInMs) {
        final int s = gotMessages.size();
        if (s < n)
            waitForNewMessages(n - s, timeoutInMs);
        return getMessages();
    }

    /**
     * Will return the got Messages for this client after a given timeout, or after
     * the buffer changes
     *
     * @param timeoutInMs timeout to wait for
     *
     * @return the buffer of send messages
     */
    public DummySendMessagesBuffer getMessages(final int timeoutInMs) {
        // We make this loop to explicitly state, that there should be NO condition to
        // wait for if it fails
        while (true) {
            synchronized (gotMessages) {
                try {
                    gotMessages.wait(timeoutInMs);
                } catch (final InterruptedException ex) {
                    magpie.writeException(ex, "Get");
                }
            }
            break;
        }
        return gotMessages;
    }

    /**
     * Clear the message-buffer
     */
    public void clearMessages() {
        gotMessages.clear();
    }

    @Override
    public int hashCode() {
        return Objects.hash(internalReference);
    }

    @Override
    public String toString() {
        return "DummyClient (" + ((NttsClientConnection) attachment).getClientName() + ") [gotMessages=" + gotMessages
                + ", internalReference=" + internalReference + ", isClosed=" + isClosed + "]";
    }

}
