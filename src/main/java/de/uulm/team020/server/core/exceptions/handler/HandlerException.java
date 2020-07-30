package de.uulm.team020.server.core.exceptions.handler;

import de.uulm.team020.networking.core.ErrorTypeEnum;

/**
 * Exception that will be thrown if there's something with a Handler.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class HandlerException extends Exception {

    private static final long serialVersionUID = -3310578299796452961L;

    private ErrorTypeEnum errorType;

    /**
     * Build a new handler exceptions. Those exceptions will be transformed to a
     * Network-Message which is then sent to the client.
     * 
     * @param message   The message to pack with it
     * @param errorType The error reason which is transformed to the
     *                  {@link de.uulm.team020.networking.messages.ErrorMessage}
     */
    public HandlerException(String message, ErrorTypeEnum errorType) {
        super(message);
        this.errorType = errorType;
    }

    public ErrorTypeEnum getError() {
        return this.errorType;
    }
}