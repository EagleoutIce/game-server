package de.uulm.team020.server.core.dummies.story;

/**
 * Just a container for getting the Inject-Targets.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/26/2020
 */
public enum Injects {
    /**
     * &lt;name&gt; &lt;proposal&gt;. Set the next proposal order to be given a
     * player in the drafting phase.
     */
    NEXT_PROPOSAL("next-proposal",
            "<name> <proposal>. Set the next proposal order to be given a player in the drafting phase."),
    /**
     * value &lt;number-list&gt;. Currently there is only value as target. Set the
     * order for the safes to be enumerated, will be performed line by line.
     */
    SAFE_ORDER("safe-order",
            "value <number-list>. Currently there is only value as target. Set the order for the safes to be enumerated, will be performed line by line."),
    /**
     * value &lt;equipment-list&gt;. Currently there is only value as target. Set
     * the present npc and set their equipment choices.
     */
    NPC_PICK("npc-pick",
            "value <equipment-list>. Currently there is only value as target. Set the present npc and set their  equipment choices. "),
    /**
     * value &lt;character,x/y&gt;. Currently there is only value as target. Sets
     * the spawning positions for all characters.
     */
    START_POSITIONS("start-positions",
            "value <character,x/y>. Currently there is only value as target. Sets the spawning positions for all characters."),
    /**
     * &lt;randomOperation&gt; &lt;name:result1;result2;...&gt;. Set the outcome of
     * random operations.
     */
    RANDOM_RESULT("random-result",
            "<randomOperation> <name:result1;result2;result3>. Set the outcome of random operations."),
    /**
     * value &lt;character-list&gt;. Currently there is only value as target. Sets
     * the spawning positions for all characters. Set the character order for the
     * next round.
     */
    NEXT_ROUND_ORDER("next-round-order",
            "value <character-list>. Currently there is only value as target. Sets the spawning positions for all characters. Set the character order for the next round."),
    /**
     * &lt;major-key&gt; &lt;value&gt;. Sets a major key.
     */
    MAJOR("major", "<major-key> <value>. Sets a major key."),
    /**
     * &lt;internal/raw-json&gt; &lt;path/json-data&gt;. Sets the scenario for this
     * configuration.
     */
    SCENARIO("scenario", "<internal/raw-json> <path/json-data>. Sets the scenario for this configuration."),
    /**
     * &lt;internal/raw-json&gt; &lt;path/json-data&gt;. Sets the matchconfig for
     * this configuration.
     */
    MATCHCONFIG("matchconfig", "<internal/raw-json> <path/json-data>. Sets the matchconfig for this configuration."),
    /**
     * &lt;internal/raw-json&gt; &lt;path/json-data&gt;. Sets the characters for
     * this configuration.
     */
    CHARACTERS("characters", "<internal/raw-json> <path/json-data>. Sets the characters for this configuration."),
    /**
     * &lt;internal/raw-json&gt; &lt;path/json-data&gt;. Set the next proposal order
     * to be given a player in the drafting phase.
     */
    SERVER_CONFIG("server-config", "<internal/raw-json> <path/json-data>. Set the server config.");

    private String description;
    private String key;

    Injects(String key, String description) {
        this.key = key;
        this.description = description;
    }

    /**
     * Get the description for this inject
     * 
     * @return The description for this injection
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * The key to be used for the stor
     * 
     * @return The key as string
     */
    public String getKey() {
        return this.key;
    }
}