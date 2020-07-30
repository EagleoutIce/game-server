package de.uulm.team020.server.core.handlers;

import org.java_websocket.WebSocket;

import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;

/**
 * Holds handlers mostly for error handling which are not bound to one specific
 * use case
 * 
 * @author Florian Sihler
 * @version 1.0, 04/08/2020
 */
public class GenericHandlers {

    /** Hide default constructor */
    private GenericHandlers() {
    }

    public static HandlerReport handleNotAllowedToSend(NttsController controller, WebSocket conn, String message,
            MessageTypeEnum type) throws HandlerException {
        return handleNotAllowedToSend(controller, conn, message,
                "No client is allowed to send this message (" + type + ")");
    }

    public static HandlerReport handleNotAllowedToSend(NttsController controller, WebSocket conn, String message,
            String text) throws HandlerException {
        throw new IllegalMessageException(text);
    }

}