package de.uulm.team020.server.core.exceptions.handler;

import de.uulm.team020.networking.core.ErrorTypeEnum;

/**
 * Exception that will be thrown if there's something with a Handler.
 * <p>
 * Reason: Message hat an illegal format/was not allowed!
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class IllegalMessageException extends HandlerException {

    private static final long serialVersionUID = 5692162166830810314L;

    /**
     * Build an illegal message which corresponds to the illegal message violation
     * as noted in the standard.
     * 
     * @param message The text explaining what went wrong
     */
    public IllegalMessageException(String message) {
        super(message, ErrorTypeEnum.ILLEGAL_MESSAGE);
    }
}