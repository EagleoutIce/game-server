package de.uulm.team020.server.core.dummies;

import org.java_websocket.WebSocket;

import de.uulm.team020.server.core.NttsMessageSender;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * This Sender will be used by the {@link DummyNttsController} if it is
 * configured to run in dummy mode. There is only one difference to the:
 * {@link NttsMessageSender} is, that Message will not be send via the websocket
 * - they will be written into a Queue that you can read from.
 * <p>
 * Use {@link DummyClient#getMessages(int)} to receive the current buffer and
 * {@link DummyClient#clearMessages()} to clear it. You should not use the
 * direct send commands given by {@link NttsMessageSender} if you want to test
 * the server, use the {@link DummyNttsController}.
 *
 * @author Florian Sihler
 * @version 1.0, 03/31/2020
 */
public class DummyNttsMessageSender extends NttsMessageSender {

    /**
     * Construct a new {@link NttsMessageSender} which will be a convenience wrapper
     * to easily send Messages
     *
     * @param controller the controller which maintains the server this one should
     *                   operate on
     */
    public DummyNttsMessageSender(DummyNttsController controller) {
        super(controller);
    }

    /**
     * Can be overwritten for testing-purposes.
     *
     * @param conn        the connection to write to
     * @param messageData the data to send
     * 
     * @return Will always return true as it just writes to the buffer, may throw an Exception if not used in Dummy mode.
     */
    @Override
    protected boolean connectionWriteOut(WebSocket conn, String messageData) {
        // If the cast to a dummy client fails we have a problem so lets check it here:
        if (!(conn instanceof DummyClient)) {
            throw new ThisShouldNotHappenException("DummyNttsMessageSender is only to be used with Dummy clients!");
        }
        recordKeeper.keepRecord(conn, messageData);
        // Now the cast should work without any problems and we do not have to cast
        // elsewhere :)
        ((DummyClient) conn).gotMessages.add(messageData);
        return true;
    }

}
