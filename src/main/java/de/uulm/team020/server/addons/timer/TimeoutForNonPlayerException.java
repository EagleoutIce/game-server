package de.uulm.team020.server.addons.timer;

/**
 * Executed if there is to be a timeout-register for a non-player
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public class TimeoutForNonPlayerException extends RuntimeException {

    private static final long serialVersionUID = -2948459357821572417L;

    public TimeoutForNonPlayerException(String message) {
        super(message);
    }

}