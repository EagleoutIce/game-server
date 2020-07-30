package de.uulm.team020.server.core.exceptions.management;

/**
 * Exception that will be thrown if there are no more player-slots
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public class PlayerSlotsFullException extends ManagementException {

    private static final long serialVersionUID = 328717592975507285L;

    /**
     * Build a new exception, which indicates that both player slots are occupied
     * 
     * @param p1Name Name of the first player (already present)
     * @param p2Name Name of the second player (already present)
     */
    public PlayerSlotsFullException(String p1Name, String p2Name) {
        super("Player slots blocked by P1 (" + p1Name + ") and P2 (" + p2Name + ")");
    }
}