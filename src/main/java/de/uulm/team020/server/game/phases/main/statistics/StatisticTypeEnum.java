package de.uulm.team020.server.game.phases.main.statistics;

/**
 * Used to build a statistic and update its values
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public enum StatisticTypeEnum {
    /** Amount of IP points the players have gained over the whole game-phase. */
    IP_POINTS_GAINED("IP-Points gained", "Amount of IP points the players have gained over the whole game-phase."),
    /**
     * Total number of fields moved on, this excludes if the character was moved by
     * another one.
     */
    FIELDS_MOVED("Total fields moved on",
            "Total number of fields moved on, this excludes if the character was moved by another one."),
    /** The player, that gifted the diamond collar to the cat. */
    GIFTED_DIAMOND_COLLAR("Has gifted the diamond collar", "The player, that gifted the diamond collar to the cat."),
    /** The total number of cocktails the player has sipped. */
    SIPPED_COCKTAILS("Number of cocktails sipped", "The total number of cocktails the player has sipped."),
    /** The total number of cocktails the player has casted on the other faction. */
    CASTED_COCKTAILS("Number of cocktails casted",
            "The total number of cocktails the player has casted on the other faction."),
    /** The total number of movements performed on a field the cat was on. */
    STEPPED_ON_CAT("Movements on the cat", "The total number of movements performed on a field the cat was on."),
    /** The total number of poisoned cocktails the player did consume. */
    DRUNK_POISONED_COCKTAILS("Number of poisoned cocktails",
            "The total number of poisoned cocktails the player did consume."),
    /** Total HP lost by all players in the faction. */
    RECEIVED_DAMAGE("Total damage received", "Total HP lost by all players in the faction.");

    private String title;
    private String description;

    StatisticTypeEnum(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }
}