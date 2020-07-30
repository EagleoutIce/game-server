package de.uulm.team020.server.addons.semantics;

/**
 * Brother of the iValidType from GameData
 * 
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
public enum ISemanticsType {
    /** Object suffices the semantics */
    SUFFICES(true),
    /** There was no check */
    SEMANTICS_DISABLED(true),
    /** Object does not suffice the semantics */
    DOES_NOT_SUFFICE(false),
    /** Can be thrown even if checking is disable, e.g. if field is null etc. */
    CRITICAL_PROBLEM(false);

    private boolean valid;

    ISemanticsType(boolean valid) {
        this.valid = valid;
    }

    /**
     * @return A boolean representation if the Message is valid
     */
    public boolean get() {
        return valid;
    }
}