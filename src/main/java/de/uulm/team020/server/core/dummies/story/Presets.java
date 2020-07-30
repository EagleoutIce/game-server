package de.uulm.team020.server.core.dummies.story;

/**
 * Just a container for getting the presets.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
public enum Presets {
    /** Skipping a drafting phase using existing players. */
    SKIP_DRAFTING("skip-drafting", "Skipping a drafting phase using existing players."),
    /** Just throw yourself into the main-game, with generated players. */
    COMPLETE_DRAFTING("complete-drafting", "Just throw yourself into the main-game, with generated players."),
    /** Normal drafting, but with fixed character amount. */
    COMPLETE_DRAFTING_CHARACTERS("complete-drafting-characters", "Normal drafting, but with fixed character amount."),
    /** Play a full game with retirements only and a shortened round-spawn. */
    SKIP_MAIN("skip-main", "Skip the main game with retirements only. Adapts to max rounds."),
    /** Play a full game with retirements only and a shortened round-spawn. */
    FULL_GAME("full-game", "Skip a full game with retirements only. Adapts to max rounds.");

    private String description;
    private String name;

    Presets(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return this.name;
    }
}