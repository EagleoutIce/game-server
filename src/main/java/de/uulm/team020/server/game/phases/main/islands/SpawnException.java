package de.uulm.team020.server.game.phases.main.islands;

/**
 * To be thrown when spawning fails.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/20/2020
 */
public class SpawnException extends RuntimeException {

    private static final long serialVersionUID = 3871492491204085355L;

    /**
     * Build a spawn exception thrown by the {@link IslandSpawner} and the
     * {@link de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner} in
     * case the spawn routine fails.
     * 
     * @param message The message to pack with it.
     */
    public SpawnException(String message) {
        super(message);
    }

}