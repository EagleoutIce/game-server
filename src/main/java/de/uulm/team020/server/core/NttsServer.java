package de.uulm.team020.server.core;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;

/**
 * The Main Server handling incoming Connections using the WebbSocket-Protocol
 * by TooTallNate. It tries to be as simple as possible, all logic concerning
 * the Messaging will be outsourced to make this instance as lightweight as
 * possible.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/22/2020
 */
public final class NttsServer extends WebSocketServer {

    private static final String SERVER_TEXT = "Server";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private NttsController controller;
    protected boolean isReady;

    public boolean isReady() {
        return this.isReady();
    }

    /**
     * Creates a WebSocketServer that will attempt to bind/listen on the given
     * <var>address</var>.
     * 
     * @param address    The address to listen to
     * @param controller the controller which handles messages
     */
    public NttsServer(InetSocketAddress address, NttsController controller) {
        super(address);
        this.controller = controller;
        this.isReady = false;
    }

    public NttsServer updateController(NttsController newController) {
        this.controller = newController;
        return this;
    }

    /**
     * Called after the websocket connection has been closed.
     *
     * @param conn   The <code>WebSocket</code> instance this event is occurring on.
     * @param code   The codes can be looked up here:
     *               {@link org.java_websocket.framing.CloseFrame}
     * @param reason Additional information string
     * @param remote Returns whether or not the closing of the connection was
     *               initiated by the remote host.
     **/
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        controller.handleCloseFor(conn, code, reason, remote);
    }

    /**
     * Called when errors occurs. If an error causes the websocket connection to
     * fail {@link #onClose(WebSocket, int, String, boolean)} will be called
     * additionally.<br>
     * This method will be called primarily because of IO or protocol errors.<br>
     * If the given exception is an RuntimeException that probably means that you
     * encountered a bug.<br>
     * 
     * @param conn Can be null if there error does not belong to one specific
     *             websocket. For example if the servers port could not be bound.
     * @param ex   The exception causing this error
     **/
    @Override
    public void onError(WebSocket conn, Exception ex) {
        magpie.writeError("Connection failed for: " + conn.getRemoteSocketAddress(), SERVER_TEXT);
        magpie.writeException(ex, SERVER_TEXT);
        // Delegate to controller
    }

    /**
     * Callback for string messages received from the remote host
     * 
     * @param conn    The <code>WebSocket</code> instance this event is occurring
     *                on.
     * @param message The UTF-8 decoded message that was received.
     * 
     * @see WebSocketServer#onMessage(WebSocket, java.nio.ByteBuffer)
     **/
    @Override
    public void onMessage(WebSocket conn, String message) {
        controller.handleCallFor(conn, message);
    }

    /**
     * Called after an opening handshake has been performed and the given websocket
     * is ready to be written on.
     * 
     * @param conn      The <code>WebSocket</code> instance this event is occurring
     *                  on.
     * @param handshake The handshake of the websocket instance
     */
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        controller.handleOpenFor(conn);
    }

    /**
     * Called when the server started up successfully.
     *
     * If any error occurred, onError is called instead.
     */
    @Override
    public void onStart() {
        this.isReady = true;
    }

    /**
     * Decodes the socket close-status.
     * 
     * @param code The code to represent as text
     * 
     * @return Textual representation of the close-status.
     */
    public static String closeStatusDecode(int code) {
        switch (code) {
            case CloseFrame.NEVER_CONNECTED:
                return "[NEVER_CONNECTED] The connection to the client was never established.";
            case CloseFrame.BUGGYCLOSE:
                return "[BUGGYCLOSE] There was an unwanted bug in the closing routine, you should never read this message.";
            case CloseFrame.FLASHPOLICY:
                return "[FLASHPOLICY] The connection got flushed and then closed.";
            case CloseFrame.NORMAL:
                return "[NORMAL] Purpose of the connection has been fulfilled, everything went fine!";
            case CloseFrame.GOING_AWAY:
                return "[GOING_AWAY] Closer has decided to switch to another Socket which does not have to be hosted by this Server.";
            case CloseFrame.REFUSE:
                return "[REFUSE] Closer has received Data it cannot accept.";
            case CloseFrame.NOCODE:
                return "[NOCODE] No status code linked. This should be only used internally.";
            case CloseFrame.ABNORMAL_CLOSE:
                return "[ABNORMAL_CLOSE] The Closer ended the connection without handling the close routine, it probably crashed or has been killed by the user.";
            case CloseFrame.NO_UTF8:
                return "[NO_UTF8] The message was not in valid UTF-8 format.";
            case CloseFrame.POLICY_VALIDATION:
                return "[POLICY_VALIDATION] Someone (probably we as the server) performed a policy validation - no further details supplied.";
            case CloseFrame.TOOBIG:
                return "[TOOBIG] The Message the Closer received was too big for it to handle.";
            case CloseFrame.EXTENSION:
                return "[EXTENSION] First of all - this should not occur for a server. Second: There was an extension expected to be transmitted, which was not fulfilled.";
            case CloseFrame.UNEXPECTED_CONDITION:
                return "[UNEXPECTED_CONDITION] The Server caught an unexpected condition.";
            case CloseFrame.SERVICE_RESTART:
                return "[SERVICE_RESTART] The Service restarted, try again in 5 - 30 seconds.";
            case CloseFrame.TRY_AGAIN_LATER:
                return "[TRY_AGAIN_LATER] The Service experiences and overload and will not serve the request.";
            case CloseFrame.BAD_GATEWAY:
                return "[BAD_GATEWAY] Similar to HTTP:502, simply should not occur :D.";
            case CloseFrame.TLS_ERROR:
                return "[TLS_ERROR] Should never occur as we are the server, and not providing tls.";
            default:
                return "Unknown error code (" + code + ")";
        }
    }

}