package de.uulm.team020.server.client.helper;

/**
 * This enum contains all the keys the digger shall server
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public enum DiggerKeyEnum {
    SERVER("Gives information about the current server connection.");

    private final String description;

    DiggerKeyEnum(String description) {
        this.description = description;
    }

    /**
     * Description of this command
     * 
     * @return String describing the signature and the semantics of the command.
     */
    public String getDescription() {
        return this.description;
    }

}