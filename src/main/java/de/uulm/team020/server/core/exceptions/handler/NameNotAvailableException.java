package de.uulm.team020.server.core.exceptions.handler;

import de.uulm.team020.networking.core.ErrorTypeEnum;

/**
 * Exception that will be thrown if there's something with a Handler.
 * <p>
 * Reason: Name is already taken.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class NameNotAvailableException extends HandlerException {

    private static final long serialVersionUID = 5692162166830810314L;

    /**
     * Build a message indicating that the name the client wanted to have is already
     * taken
     * 
     * @param message Text to pack with it, explaining the reason
     */
    public NameNotAvailableException(String message) {
        super(message, ErrorTypeEnum.NAME_NOT_AVAILABLE);
    }
}