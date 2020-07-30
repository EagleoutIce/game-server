package de.uulm.team020.server.configuration;

/**
 * Exception that will be thrown if there's something wrong with
 * {@link Configuration}.
 * <p>
 * If you catch this exception this usually means an error with the code as this
 * should not occur at Runtime!
 * 
 * @author Florian Sihler
 * @version 1.0, 03/19/2020
 */
public class ConfigurationException extends RuntimeException {
    private static final long serialVersionUID = 3623952655698965982L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(Throwable cause) {
        super(cause);
    }
}