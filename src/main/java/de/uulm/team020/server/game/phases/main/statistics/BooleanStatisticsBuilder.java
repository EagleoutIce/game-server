package de.uulm.team020.server.game.phases.main.statistics;

import de.uulm.team020.server.core.datatypes.NttsClientConnection;

/**
 * Variant of the {@link StatisticsBuilder} which uses Boolean-Values for keys
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class BooleanStatisticsBuilder extends StatisticsBuilder<Boolean> {

    /**
     * Construct a new IntegerStatisticsBuilder
     * 
     * @param type The type of the statistic to build
     */
    protected BooleanStatisticsBuilder(StatisticTypeEnum type) {
        super(type);
        // Set defaults:
        this.playerOneValue = false;
        this.playerTwoValue = false;
    }

    /**
     * Update the statistics value same es
     * {@link StatisticsBuilder#setValue(NttsClientConnection, Object)}
     * 
     * @param connection The connection to increment the value for
     * @param newValue   The value to be used for set
     * 
     * @return The old Value
     */
    @Override
    public Boolean updateValue(NttsClientConnection connection, Boolean newValue) {
        if (connection == null) { // If this case there is no reason to update
            return false;
        }
        return setValue(connection, newValue);
    }

    @Override
    protected String getString(Boolean value) {
        return value == null ? "" : value.toString();
    }

}