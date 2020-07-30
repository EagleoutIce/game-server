package de.uulm.team020.server.game.phases.choice;

/**
 * To be thrown if there was an illegal choice attempt by one of the players.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/10/2020
 */
public class DraftingChoiceException extends Exception {

    private static final long serialVersionUID = -9114958684656590255L;

    public DraftingChoiceException(String name, String subject, int max_amount) {
        super("Maximum amount of " + subject + " reached for " + name + ". Maximum is: " + max_amount + ".");
    }

}