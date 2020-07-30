package de.uulm.team020.server.core.dummies.story.helper;

import de.uulm.team020.server.core.NttsServer;
import de.uulm.team020.server.core.dummies.DummyClient;

/**
 * Used to set a handler for the close-routine of a {@link DummyClient}.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/14/2020
 * 
 * @since 1.1
 */
public interface ICloseConsumer {
    /**
     * To be called on close
     * 
     * @param code    Close code, may be decoded by
     *                {@link NttsServer#closeStatusDecode(int)}. The code will be
     *                {@link DummyClient#ARTIFICIAL_CLOSE}, if the close was
     *                enforced by the testing-framework with no specified reason.
     * @param message The message passed with it
     */
    void onClose(final int code, final String message);

}