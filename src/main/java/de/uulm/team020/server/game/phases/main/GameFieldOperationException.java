package de.uulm.team020.server.game.phases.main;

/**
 * Will be thrown when there is any problem with the operation performed in the
 * field, that does not work from a process-point-perspective.
 */
public class GameFieldOperationException extends Exception {

    private static final long serialVersionUID = 2666913115400842471L;

    /**
     * Konstrukt the new operation exception
     * 
     * @param message Message to be passed for debugging
     */
    public GameFieldOperationException(String message) {
        super(message);
    }

}