package de.uulm.team020.server.core.exceptions;

import de.uulm.team020.server.core.datatypes.FilterBuilder;

/**
 * Exception that will be thrown if there's a problem with the
 * {@link de.uulm.team020.server.core.datatypes.FilterBuilder}
 * 
 * @author Florian Sihler
 * @version 1.0, 03/25/2020
 */
public class FilterException extends RuntimeException {

    private static final long serialVersionUID = 2581325561574555857L;

    /**
     * Build a new filter exception to be thrown by the {@link FilterBuilder}
     * 
     * @param message The message to pack with it
     */
    public FilterException(String message) {
        super(message);
    }

}