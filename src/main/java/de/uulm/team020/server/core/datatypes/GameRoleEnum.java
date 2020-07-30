package de.uulm.team020.server.core.datatypes;

/**
 * Used to easily identify the in-game-role of the client
 *
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public enum GameRoleEnum {
    /**
     * Client is player one
     */
    PLAYER_ONE(true),
    /**
     * Client is player two
     */
    PLAYER_TWO(true),
    /**
     * Client is a spectator
     */
    SPECTATOR(false);

    private boolean isPlayer;

    GameRoleEnum(boolean isPlayer) {
        this.isPlayer = isPlayer;
    }

    /**
     * Just returns if the client is a player or not -- this method will in fact
     * return false if and only if the client is registered as a spectator.
     * 
     * @return True if the client is a player, false if it is a spectator.
     */
    public boolean isPlayer() {
        return this.isPlayer;
    }
}
