package de.uulm.team020.server.core.exceptions.management;

import java.util.UUID;

/**
 * Exception that will be thrown if the clientId desired is already registered.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public class ClientIdAlreadyPresentException extends ManagementException {

    private static final long serialVersionUID = 328717592975507285L;

    /**
     * Build a new Exception, noting that the client Id is already present
     * 
     * @param id The id for the client that is present
     */
    public ClientIdAlreadyPresentException(UUID id) {
        super("There is already a client registered using this clientId (" + id + ")");
    }
}