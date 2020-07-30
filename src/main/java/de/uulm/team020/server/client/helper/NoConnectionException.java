package de.uulm.team020.server.client.helper;

/**
 * Thrown if there is a problem with the connection (so there is no connection)
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class NoConnectionException extends GenericNetworkingException {

    private static final long serialVersionUID = -8528028291448851677L;

    /**
     * Construct a new exception. To be done when there is no connection with the
     * server.
     *
     * @param seconds How long did the client wait (in seconds)
     */
    public NoConnectionException(int seconds) {
        super("Wasn't able to establish a connection with the server. Waited:" + seconds + "s.");
    }
}
