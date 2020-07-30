package de.uulm.team020.server.core.exceptions;

/**
 * Will be thrown whenever there is an (asserted) expectation that doesn't get
 * fulfilled
 * 
 * @author Florian Sihler
 * @version 1.0, 04/15/2020
 */
public class ExpectationException extends RuntimeException {

    private static final long serialVersionUID = 32028672277468774L;

    /**
     * Build a new expectation exception
     * 
     * @param message The message to pack with it
     */
    public ExpectationException(String message) {
        super(message);
    }

}