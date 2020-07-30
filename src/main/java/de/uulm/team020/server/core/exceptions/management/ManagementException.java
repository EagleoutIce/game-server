package de.uulm.team020.server.core.exceptions.management;

/**
 * Exception that will be thrown if there's something wrong with
 * {@link de.uulm.team020.server.core.NttsClientManager NttsClientManager}.
 * 
 * @author Florian Sihler
 * @version 1.1, 06/21/2020
 */
public class ManagementException extends Exception {

    private static final long serialVersionUID = 3623952655698965982L;

    /**
     * Build a new management exception
     * 
     * @param message The message to pack with it
     */
    public ManagementException(String message) {
        super(message);
    }

}