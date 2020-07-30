package de.uulm.team020.server.core.exceptions.handler;

import de.uulm.team020.networking.core.ErrorTypeEnum;

/**
 * Exception that will be thrown if there's something with a Handler.
 * <p>
 * Reason: There are already two players
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class AlreadyServingException extends HandlerException {

    private static final long serialVersionUID = -4814730436260537119L;

    /**
     * Build a new already serving exception this will be sent to the player as an
     * Error message
     * 
     * @param message The text to pass with it.
     */
    public AlreadyServingException(String message) {
        super(message, ErrorTypeEnum.ALREADY_SERVING);
    }
}