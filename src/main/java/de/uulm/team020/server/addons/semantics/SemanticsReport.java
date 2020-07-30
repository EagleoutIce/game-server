package de.uulm.team020.server.addons.semantics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;

/**
 * The twin of an validation report
 * 
 * @author Florian Sihler
 * @version 1.0, 04/17/2020
 */
public class SemanticsReport implements Serializable {

    private static final long serialVersionUID = 7995191481856180680L;

    private static final String SEMANTICS_TXT = "Semantics";

    /** Logging-Instance */
    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    /** Set of all Reasons why the document is invalid */
    private List<String> reasons = new ArrayList<>(1);

    /**
     * Flag indicating, if the Object is Valid (is {@link ISemanticsType#SUFFICES}
     * if so)
     */
    private final ISemanticsType isValid;

    /**
     * Construct a new Semantics-Report
     * 
     * @param reason  textual representation
     * @param isValid is the Object valid?
     */
    public SemanticsReport(final String reason, final ISemanticsType isValid) {
        this.reasons.add(reason);
        this.isValid = isValid;
        magpie.writeDebug("Building new " + this, SEMANTICS_TXT);
    }

    /**
     * Construct a new Semantics-Report
     * 
     * @param reasons textual representation of all reasons
     * @param isValid is the Object valid?
     */
    public SemanticsReport(final List<String> reasons, final ISemanticsType isValid) {
        this.reasons = reasons;
        this.isValid = isValid;
        magpie.writeDebug("Building new " + this, SEMANTICS_TXT);
    }

    /**
     * Constructs a successful report, with no message/reason attached
     */
    public SemanticsReport() {
        this("", ISemanticsType.SUFFICES);
    }

    /**
     * @return The first reason, if available - returns 'null' otherwise
     */
    public String getReason() {
        if (!this.reasons.isEmpty())
            return this.reasons.get(0);
        return null;
    }

    /**
     * @return All reasons
     */
    public List<String> getReasons() {
        return this.reasons;
    }

    /**
     * @return Is the construct valid?
     */
    public ISemanticsType getIsValid() {
        return isValid;
    }

    /**
     * @return True if the construct is valid, false otherwise.
     */
    public boolean isValid() {
        return isValid.get();
    }

    @Override
    public String toString() {
        return "SemanticsReport [isValid=" + isValid + ", reasons=" + reasons + "]";
    }
}