package de.uulm.team020.server.game.phases.main.statistics;

import java.util.Objects;

import de.uulm.team020.datatypes.StatisticsEntry;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Used to build a statistic and update its values. It uses the
 * {@link StatisticTypeEnum}
 * 
 * @param <T> Determines the type of the embedded Values
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public abstract class StatisticsBuilder<T> {

    /**
     * Type of the statistic
     */
    protected StatisticTypeEnum type;

    /**
     * Value of player one
     */
    protected T playerOneValue;
    /**
     * Value of player two
     */
    protected T playerTwoValue;

    /**
     * Construct a new StatisticsBuilder
     * 
     * @param type The type of the Statistic to be built with this builder
     */
    protected StatisticsBuilder(StatisticTypeEnum type) {
        this.type = type;
    }

    /**
     * Set the value, will overwrite the old one, if it is present
     * 
     * @param connection The connection the statistic is for
     * @param newValue   The new value for the statistic
     * 
     * @return The old value of the setting
     */
    public T setValue(NttsClientConnection connection, T newValue) {
        final GameRoleEnum role = connection.getGameRole();
        T oldValue;
        if (role == GameRoleEnum.PLAYER_ONE) {
            oldValue = playerOneValue;
            playerOneValue = newValue;
        } else if (role == GameRoleEnum.PLAYER_TWO) {
            oldValue = playerTwoValue;
            playerTwoValue = newValue;
        } else {
            throw new ThisShouldNotHappenException(
                    "You cannot set the value: " + newValue + " as statistic-value for: " + connection);
        }
        return oldValue;
    }

    /**
     * This can work exactly like {@link #setValue(NttsClientConnection, Object)}
     * but can be used for 'update' the value in terms of adding it to the current
     * one. E.G. if it is an Integer-Builder this can be used to add the values.
     * 
     * @param connection  The connection the statistic is for
     * @param updateValue The value to update for the statistic
     * 
     * @return The old value of the setting
     */
    public abstract T updateValue(NttsClientConnection connection, T updateValue);

    /**
     * Returns the currently connected Value for this connection
     * 
     * @param connection The connection to request the data for
     * 
     * @return The value requested
     */
    public T getValue(NttsClientConnection connection) {
        final GameRoleEnum role = connection.getGameRole();
        if (role == GameRoleEnum.PLAYER_ONE) {
            return playerOneValue;
        } else if (role == GameRoleEnum.PLAYER_TWO) {
            return playerTwoValue;
        } else {
            throw new ThisShouldNotHappenException("You cannot request the statistic-value for: " + connection);
        }
    }

    public StatisticsEntry build() {
        return new StatisticsEntry(type.getTitle(), type.getDescription(), getString(playerOneValue),
                getString(playerTwoValue));
    }

    /**
     * Get the String-Representation of the embedded Value - deal with 'null'!
     * 
     * @param value The value to convert
     * 
     * @return The string-variant
     */
    protected abstract String getString(T value);

    public T getPlayerOneValue() {
        return this.playerOneValue;
    }

    public T getPlayerTwoValue() {
        return this.playerTwoValue;
    }

    /**
     * Checks if both players have the same Value
     * 
     * @return True if the equal for both types succeeds, false otherwise
     */
    public boolean sameValues() {
        return Objects.equals(this.playerOneValue, this.playerTwoValue);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("StatisticsBuilder [playerOneValue=").append(getString(playerOneValue))
                .append(", playerTwoValue=").append(getString(playerTwoValue)).append(", type=").append(type)
                .append("]");
        return builder.toString();
    }
}