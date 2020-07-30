package de.uulm.team020.server.addons.random.enums;

import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner;

/**
 * Holds all possible targets for a random operation that may be injected into
 * the {@link RandomController}.
 * 
 * 
 * @author Florian Sihler
 * @version 1.1, 07/03/2020
 * 
 * @since 1.1
 */
public enum RandomOperation {

    /**
     * Will be called whenever there is a check for an operation to succeed. There
     * may be multiple calls for this in one operation so logging may be rather
     * verbose.
     */
    OPERATION_SUCCESS(RandomOperationType.BOOLEAN),
    /**
     * Requested by
     * {@link GameFieldPositioner#getClosestFreeField(de.uulm.team020.server.game.phases.main.GameFieldController, de.uulm.team020.datatypes.util.Point)}
     * or its base:
     * {@link GameFieldPositioner#getClosestFreeField(java.util.Set, de.uulm.team020.datatypes.FieldMap, de.uulm.team020.datatypes.util.Point, RandomController, boolean)}
     * to retrieve the field without performing any point based recalculation
     */
    CLOSEST_FREE_FIELD(RandomOperationType.POINT),
    /**
     * Requested by
     * {@link GameFieldPositioner#getClosestFreeField(de.uulm.team020.server.game.phases.main.GameFieldController, de.uulm.team020.datatypes.util.Point)}
     * or its base:
     * {@link GameFieldPositioner#getClosestFreeField(java.util.Set, de.uulm.team020.datatypes.FieldMap, de.uulm.team020.datatypes.util.Point, RandomController, boolean)}
     * to retrieve the field without performing any point based recalculation. This
     * one will get used instead of {@link #CLOSEST_FREE_FIELD} if fade is enabled.
     * It is not necessary to split them, but it improves readability
     */
    CLOSEST_FREE_FIELD_FADE(RandomOperationType.POINT),
    /**
     * Determines the result of a flip when in a gamble - the result will still be
     * victim to the inverted operation. So 'win' may mean loose (if the roulette
     * table is inverted).
     */
    GAMBLE_WIN(RandomOperationType.BOOLEAN),
    /**
     * Determines if an npc has the correct key -- the name bound to this will be
     * the one of the NPC that <i>gets spied on</i> not the one that is spied.
     */
    NPC_HAS_RIGHT_KEY(RandomOperationType.BOOLEAN),
    /**
     * This will be issued every round a wiretap is active and holds the name
     * '{@value RandomController#GLOBAL}' as there may only be one. This can be
     * changed easily if there are multiple owners possible.
     */
    WIRETAP_SHOULD_BREAK(RandomOperationType.BOOLEAN),
    /**
     * Called whenever there is a check for the honey-trap feature, name will be the
     * name of the character targeted This will only be called if the character does
     * have the honey-trap-trait
     */
    HONEY_TRAP_TRIGGERS(RandomOperationType.BOOLEAN),
    /**
     * Called whenever there is to be a new target picked by the honey trap -- the
     * name is the character-name of the character that owns the honey-trap
     * property.
     */
    HONEY_TRAP_NEW_TARGET(RandomOperationType.CHARACTER),
    /**
     * Declares who the winner shall be if determined random. True refers to player
     * one, false to player two. The name should be
     * '{@value RandomController#GLOBAL}'.
     */
    WINS_ON_RANDOM(RandomOperationType.BOOLEAN),
    /**
     * Enforces the (random) movement of an npc. The name is the npc that should
     * move, valid labels are up, left, right and down.
     */
    NPC_MOVEMENT(RandomOperationType.POINT),
    /**
     * Target for the moledie operation
     */
    NPC_MOLEDIE_TARGET(RandomOperationType.POINT),
    /**
     * Bar seat to pick on an exfiltration
     */
    BAR_SEAT_ON_EXFILTRATION(RandomOperationType.POINT),
    /**
     * Number of keys the npc will gift. The name refers to the spied npc.
     */
    NPC_AMOUNT_OF_SAFE_KEYS(RandomOperationType.RANGE),
    /**
     * Amount of ms the npc should wait between two turns. The name refers to the
     * npc character
     */
    NPC_WAIT_IN_MS(RandomOperationType.RANGE),
    /**
     * Target of the cat movement, name of the cat is determined by the name:
     * '{@value RandomController#GLOBAL}'.
     */
    CAT_WALK_TARGET(RandomOperationType.POINT),
    /**
     * Determines the point the janitor will spawn on. The name should be
     * '{@value RandomController#GLOBAL}'.
     */
    JANITOR_SUMMON_TARGET(RandomOperationType.POINT),
    /**
     * Determines the result of the first toss from resetMpAp -- name refers to the
     * name of the target character.
     */
    CHARACTER_MP_AP_GAIN(RandomOperationType.BOOLEAN),
    /**
     * Determines the result of the second toss from resetMpAp -- name refers to the
     * name of the target character.
     */
    CHARACTER_MP_AP_LOSS(RandomOperationType.BOOLEAN),
    /**
     * The number of chips to supply to any roulette table. The name should be
     * '{@value RandomController#GLOBAL}'.
     */
    ROULETTE_INITIAL_CHIPS(RandomOperationType.RANGE);

    private final RandomOperationType type;

    RandomOperation(RandomOperationType type) {
        this.type = type;
    }

    /**
     * @return the type
     */
    public RandomOperationType getType() {
        return type;
    }

}