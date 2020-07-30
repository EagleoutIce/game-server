package de.uulm.team020.server.addons.rules;

/**
 * Holds all the types possible to be thrown for a {@link StrictModeException}.
 * 
 * @author Florian Sihler
 * @version 1.1, 03/28/2020
 */
public enum StrictModeTypeEnum {
    UNKNOWN_TYPE_EXTENSION("The file-extension given is not the one defined in the standard."),
    FIELD_IS_NOT_NULL("A field that should be null, is not"),
    MISSING_NULL_FIELD("A field that was to be nulled explicitly has not been nulled.");

    private String description;

    StrictModeTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}