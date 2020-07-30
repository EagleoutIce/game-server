package de.uulm.team020.server.client.helper;

/**
 * Exception to be thrown whenever you wanted for force ownership-transfer but
 * you are not allowed to.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class IllegalOwnException extends RuntimeException {

    private static final long serialVersionUID = 2442372159995264282L;

    /**
     * Thrown, if you wanted to own but you are not allowed to
     * 
     * @param message message explaining the problem
     */
    public IllegalOwnException(final String message) {
        super(message);
    }

}