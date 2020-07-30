package de.uulm.team020.server.client.helper;

/**
 * Thrown if there is any problem with the connection
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class GenericNetworkingException extends RuntimeException {

    private static final long serialVersionUID = -7131605913846543735L;

    /**
     * Construct a new exception
     * 
     * @param message The message to add to the exception
     */
    public GenericNetworkingException(String message) {
        super(message);
    }

}
