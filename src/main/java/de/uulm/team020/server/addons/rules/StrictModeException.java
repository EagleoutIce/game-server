package de.uulm.team020.server.addons.rules;

/**
 * Thrown by {@link StrictModeController} if the
 * {@link de.uulm.team020.server.configuration.CommandLineParser} has detected
 * the <code>--strict</code> command-line option.
 * <p>
 * As it should not be caught as this possible can not be prevented.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/26/2020
 */
public class StrictModeException extends RuntimeException {

    private static final long serialVersionUID = 8000101773456630471L;

    private StrictModeTypeEnum type;

    public StrictModeException(StrictModeTypeEnum type, String message) {
        super(message);
        this.type = type;
    }

    public StrictModeTypeEnum getType() {
        return this.type;
    }

}