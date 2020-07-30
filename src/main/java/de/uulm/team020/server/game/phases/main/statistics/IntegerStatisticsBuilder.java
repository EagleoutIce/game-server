package de.uulm.team020.server.game.phases.main.statistics;

import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Variant of the {@link StatisticsBuilder} which uses Integer-Values for keys
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class IntegerStatisticsBuilder extends StatisticsBuilder<Integer> {

    /**
     * Construct a new IntegerStatisticsBuilder
     * 
     * @param type The type of the statistic to build
     */
    protected IntegerStatisticsBuilder(StatisticTypeEnum type) {
        super(type);
        // Set defaults:
        this.playerOneValue = 0;
        this.playerTwoValue = 0;
    }

    /**
     * Update the statistics value by using increment
     * 
     * @param connection  The connection to increment the value for
     * @param updateValue The value to be used for increment
     * 
     * @return The old Value
     */
    @Override
    public Integer updateValue(NttsClientConnection connection, Integer updateValue) {
        if (connection == null) { // If this case there is no reason to update
            return -1;
        }
        return updateValue(connection.getGameRole(), updateValue);
    }

    /**
     * Update the statistics value by using increment. Usually this is done via
     * {@link #updateValue(NttsClientConnection, Integer)} but in some certain cases
     * as dealing damage the opposite player's connection cannot be accessed.
     *
     * @param role        The player to increment the value for
     * @param updateValue The value to be used for increment
     * @return The old value
     */
    public Integer updateValue(GameRoleEnum role, Integer updateValue) {
        Integer oldValue;
        if (role == GameRoleEnum.PLAYER_ONE) {
            oldValue = playerOneValue;
            playerOneValue += updateValue;
        } else if (role == GameRoleEnum.PLAYER_TWO) {
            oldValue = playerTwoValue;
            playerTwoValue += updateValue;
        } else {
            throw new ThisShouldNotHappenException(
                    "You cannot update the value: " + updateValue + " as statistic-value for: " + role);
        }
        return oldValue;
    }

    @Override
    protected String getString(Integer value) {
        return value == null ? "" : value.toString();
    }

}