package de.uulm.team020.server.core;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.BooleanSupplier;

import org.java_websocket.WebSocket;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.framing.CloseFrame;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.MessageTargetPair;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.datatypes.IMessageHandler;

/**
 * Will be controlled by the main {@link NttsController} to handle the sending
 * of some messages. It acts as a shortcut for handling the clients provided by
 * the {@link NttsClientManager} and send them the Messages the
 * {@link de.uulm.team020.server.core.datatypes.HandlerReport HandlerReport}
 * wants it to.
 * <p>
 * Even though it could be run as a static instance, it will be hold by the
 * {@link NttsController}, to provide extensibility. The underlying
 * WebSocket-Protocol is responsible for buffering the data.
 * 
 * @author Florian Sihler
 * @version 1.3, 04/29/2020
 */
public class NttsMessageSender {

    private static final String TRIED_TO_SEND = "Tried to send '";

    /** Basically a link to the operating {@link NttsController} */
    protected NttsController controller;

    protected static IMagpie magpie = Magpie.createMagpieSafe("Server");

    protected final ReplayRecordKeeper recordKeeper;

    /**
     * Construct a new {@link NttsMessageSender} which will be a convenience wrapper
     * to easily send messages
     * 
     * @param controller the controller which maintains the server this one should
     *                   operate on
     */
    public NttsMessageSender(NttsController controller) {
        this.controller = controller;
        this.recordKeeper = controller.getClientManager().getRecordKeeper();
    }

    /**
     * Sends a Message to a Client, identified by its UUID
     * 
     * @param target  the UUID to the target-client
     * @param message the message-data to send
     * 
     * @return False if it was not possible to send the message, true otherwise.
     * 
     * @see #sendMessage(WebSocket, String)
     */
    public boolean sendMessage(UUID target, MessageContainer message) {
        if (target == null) {
            magpie.writeWarning(TRIED_TO_SEND + message + "' to UUID [" + target + "]", "Send");
            return false;
        }
        NttsClientConnection connection = controller.getClientManager().getConnectionByUUID(target);
        if (connection == null) {
            magpie.writeWarning(
                    TRIED_TO_SEND + message + "' to UUID [" + target + "]. No attached connection was found.", "Send");
            return false;
        }
        magpie.writeDebug("Sending: " + message + ". To: " + connection.getClientName(), "Send");
        return sendMessage(connection.getConnection(), message.setClientId(target));
    }

    /**
     * Sends a Message to the socket
     * 
     * @param conn    the socket to send the Message to
     * @param message the message-data in container-format to send
     * 
     * @return False if it was not possible to send the message, true otherwise.
     * @see #sendMessage(WebSocket, String)
     */
    public boolean sendMessage(WebSocket conn, MessageContainer message) {
        return sendMessage(conn, message.toJson());
    }

    /**
     * Sends a Message to the socket
     * 
     * @param conn        the socket to send the Message to
     * @param messageData the message-data to send
     * 
     * @return False if it was not possible to send the message, true otherwise.
     */
    public boolean sendMessage(WebSocket conn, String messageData) {
        if (conn == null || conn.isClosed()) {
            magpie.writeWarning(TRIED_TO_SEND + messageData + "', but the connection was closed already", "Send");
            return false;
        }
        return connectionWriteOut(conn, messageData);
    }

    /**
     * Can be overwritten for testing-purposes.
     * 
     * @param conn        the connection to write to
     * @param messageData the data to send
     * 
     * @return True if there was no exception in sending the message, false if the
     *         sending failed. All other exceptions should escalate
     */
    protected boolean connectionWriteOut(WebSocket conn, String messageData) {
        try {
            recordKeeper.keepRecord(conn, messageData);
            conn.send(messageData);
        } catch (WebsocketNotConnectedException ex) {
            magpie.writeError("Server Connection to client lost,  ", "send");
            magpie.writeException(ex, "send");
            return false;
        }
        return true;
    }

    /**
     * Sends a Message a set of targets denoted by {@link MessageTarget}. This will
     * set the clientId of the targets correctly
     * 
     * @param target      the target-filters
     * @param messageData the message-data to send
     * 
     * @return False if it was not possible to send the message, true otherwise.
     */
    public boolean sendMessage(MessageTarget target, MessageContainer messageData) {
        if (target.isUnicast()) {
            return sendMessage(target.getUnicastTarget(), messageData);
        }
        if (target.isBroadcast()) { // avoid filtering, which is not necessary in this case
            controller.broadcast(messageData);
            return true;
        } else {
            return sendMessageToMessageTarget(target, messageData);
        }
    }

    private boolean sendMessageToMessageTarget(MessageTarget target, MessageContainer messageData) {
        Set<NttsClientConnection> targets = controller.getClientManager().getConnectionsByFilter(target);

        boolean successful = true;
        for (NttsClientConnection connection : targets) {
            if (!sendMessage(connection.getConnection(), messageData.setClientId(connection.getClientId())))
                successful = false;
        }
        return successful;
    }

    /**
     * This is basically a broadcast with one key difference: it will set the
     * clientId to the Id of every targetClient which is hit by the broadcast
     * Message.
     *
     * @param message  the message to send
     * @param excludes exclude this in broadcast#
     *
     * @return True if every sending was successful via
     *         {@link #sendMessage(WebSocket, String)}
     */
    public boolean broadCastWithTargetAssignment(MessageContainer message, List<NttsClientConnection> excludes) {
        Set<NttsClientConnection> connections = controller.getClientManager().getAllConnections();
        boolean successful = true;
        for (NttsClientConnection connection : connections) {
            if (excludes.contains(connection)) // do not broadcast to him
                continue;
            if (!sendMessage(connection.getConnection(), message.setClientId(connection.getClientId()))) {
                successful = false;
            }
        }
        return successful;
    }

    /**
     * This is basically a broadcast with one key difference: it will set the
     * clientId to the Id of every targetClient which is hit by the broadcast
     * Message.
     *
     * @param message the message to send
     *
     * @return True if every sending was successful via
     *         {@link #sendMessage(WebSocket, String)}
     */
    public boolean broadCastWithTargetAssignment(MessageContainer message) {
        Set<NttsClientConnection> connections = controller.getClientManager().getAllConnections();
        boolean successful = true;
        for (NttsClientConnection connection : connections) {
            if (!sendMessage(connection.getConnection(), message.setClientId(connection.getClientId()))) {
                successful = false;
            }
        }
        return successful;
    }

    /**
     * sends the game-start. Please note that this does not include setting the
     * correct GamePhase via
     * {@link de.uulm.team020.server.configuration.Configuration#shiftPhase(GamePhaseEnum)}.
     * This has to be done separately.
     *
     * @return The feedback of {@link #sendMessage(MessageTarget, MessageContainer)}
     */
    public boolean sendGameStartedMessage() {
        // collect the Data for a GameStartedMessage
        NttsClientConnection pOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection pTwo = controller.getClientManager().getPlayerTwo();
        GameStartedMessage gameStartedMessage = new GameStartedMessage(null, pOne.getClientId(), pTwo.getClientId(),
                pOne.getClientName(), pTwo.getClientName(), controller.getConfiguration().getSessionId());
        return broadCastWithTargetAssignment(gameStartedMessage, Collections.emptyList());
    }

    /**
     * Handles the {@link HandlerReport HandlerReports} returned by an
     * {@link IMessageHandler#apply(NttsController, WebSocket, String)}-call.
     * 
     * @param connection The connection the Messages have been received from
     * @param report     The report to handle
     */
    public void handleReport(final NttsClientConnection connection, final HandlerReport report) {
        handleReports(connection.getConnection(), new HandlerReport[] { report });
    }

    /**
     * Handles the {@link HandlerReport HandlerReports} returned by an
     * {@link IMessageHandler#apply(NttsController, WebSocket, String)}-call.
     * 
     * @param connection The connection the Messages have been received from
     * @param reports    The reports to handle
     */
    public void handleReports(final NttsClientConnection connection, final HandlerReport[] reports) {
        handleReports(connection.getConnection(), reports);
    }

    /**
     * Handles the {@link HandlerReport HandlerReports} returned by an
     * {@link IMessageHandler#apply(NttsController, WebSocket, String)}-call.
     * 
     * @param conn    the connection the Messages have been received from
     * @param reports the reports to handle
     */
    public void handleReports(final WebSocket conn, final HandlerReport[] reports) {
        boolean closeConnection = false;
        final List<BooleanSupplier> afterFunctionCalls = new LinkedList<>();

        for (final HandlerReport report : reports) {
            if (report == null)
                continue;
            closeConnection = sendAllMessagesEmbeddedInHandlerReports(closeConnection, report);
            final BooleanSupplier funcCall = report.getAfterFunctionCall();
            if (funcCall != null)
                afterFunctionCalls.add(funcCall);
        }

        // Execute all function calls
        for (final BooleanSupplier call : afterFunctionCalls) {
            call.getAsBoolean(); // maybe validate call?
        }

        if (closeConnection) {
            conn.close(CloseFrame.NORMAL);
        }
    }

    private boolean sendAllMessagesEmbeddedInHandlerReports(boolean closeConnection, final HandlerReport report) {
        if (report.hasMessagesToSend()) {
            final List<MessageTargetPair> messages = report.getMessageRequests();
            if (report.doesRequestClose())
                closeConnection = true;
            for (final MessageTargetPair m : messages) {
                this.sendMessage(m.getTargets(), m.getMessage());
            }
        }
        return closeConnection;
    }

}