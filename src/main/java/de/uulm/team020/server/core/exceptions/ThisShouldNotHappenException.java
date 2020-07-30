package de.uulm.team020.server.core.exceptions;

/**
 * Will be thrown if there is something that shouldn't happen
 * 
 * @author Florian Sihler
 * @version 1.0, 04/09/2020
 */
public class ThisShouldNotHappenException extends RuntimeException {

    private static final long serialVersionUID = -1525782994008151853L;

    /**
     * Build a new this should not happen exception
     * 
     * @param message The message to explain the misery
     */
    public ThisShouldNotHappenException(String message) {
        super("If you can read this error, there was a severe logic error and a point was reached that shouldn't be reached from a logical perspective. "
                + message);
    }

}