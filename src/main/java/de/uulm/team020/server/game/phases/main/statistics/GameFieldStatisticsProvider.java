package de.uulm.team020.server.game.phases.main.statistics;

import java.util.Objects;

import de.uulm.team020.datatypes.Statistics;
import de.uulm.team020.datatypes.StatisticsEntry;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;

/**
 * Will be used to track several statistics that will be used to determine the
 * winner AND to offer some funny statistics.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class GameFieldStatisticsProvider {

    private final IntegerStatisticsBuilder ipPoints;
    private final IntegerStatisticsBuilder fieldsMoved;
    private final IntegerStatisticsBuilder cocktailsSipped;
    private final IntegerStatisticsBuilder cocktailsCasted;
    private final IntegerStatisticsBuilder damageReceived;
    private final IntegerStatisticsBuilder steppedOnCat;
    private final IntegerStatisticsBuilder poisonedCocktails;
    private final BooleanStatisticsBuilder giftedDiamondCollar;

    public GameFieldStatisticsProvider() {
        ipPoints = new IntegerStatisticsBuilder(StatisticTypeEnum.IP_POINTS_GAINED);
        fieldsMoved = new IntegerStatisticsBuilder(StatisticTypeEnum.FIELDS_MOVED);
        cocktailsSipped = new IntegerStatisticsBuilder(StatisticTypeEnum.SIPPED_COCKTAILS);
        cocktailsCasted = new IntegerStatisticsBuilder(StatisticTypeEnum.CASTED_COCKTAILS);
        damageReceived = new IntegerStatisticsBuilder(StatisticTypeEnum.RECEIVED_DAMAGE);
        giftedDiamondCollar = new BooleanStatisticsBuilder(StatisticTypeEnum.GIFTED_DIAMOND_COLLAR);
        steppedOnCat = new IntegerStatisticsBuilder(StatisticTypeEnum.STEPPED_ON_CAT);
        poisonedCocktails = new IntegerStatisticsBuilder(StatisticTypeEnum.DRUNK_POISONED_COCKTAILS);
    }

    public void addIP(NttsClientConnection connection, int addValue) {
        ipPoints.updateValue(connection, addValue);
    }

    public void addIP(GameRoleEnum roleEnum, int addValue) {
        ipPoints.updateValue(roleEnum, addValue);
    }

    public void removeIP(NttsClientConnection connection, int removeValue) {
        ipPoints.updateValue(connection, (-1) * removeValue);
    }

    public void removeIP(GameRoleEnum roleEnum, int removeValue) {
        ipPoints.updateValue(roleEnum, removeValue);
    }

    public void addFieldsMovedOn(NttsClientConnection connection, int addValue) {
        fieldsMoved.updateValue(connection, addValue);
    }

    public void sippedCocktail(NttsClientConnection connection) {
        cocktailsSipped.updateValue(connection, 1);
    }

    public void sippedPoisonedCocktail(NttsClientConnection connection) {
        poisonedCocktails.updateValue(connection, 1);
    }

    public void castedCocktail(NttsClientConnection connection) {
        cocktailsCasted.updateValue(connection, 1);
    }

    public void steppedOnCat(NttsClientConnection connection) {
        steppedOnCat.updateValue(connection, 1);
    }

    public void receivedDamage(NttsClientConnection connection, int amount) {
        damageReceived.updateValue(connection, amount);
    }

    /**
     * Adds the passed {@code amount} to the target player's statistics (identified
     * by {@link GameRoleEnum}). Updating the statistics this way instead of the
     * normal one by using {@link #receivedDamage(NttsClientConnection, int)} is
     * necessary because damage is dealt to the opposite player which's connection
     * is not accessible during dealing damage.
     *
     * @param role   The player's role who received damage
     * @param amount The amount of damage received
     */
    public void receivedDamage(GameRoleEnum role, int amount) {
        damageReceived.updateValue(role, amount);
    }

    /**
     * Adds the passed {@code amount} to the target player's statistics (identified
     * by {@link GameRoleEnum}). Updating the statistics this way instead of the
     * normal one by using {@link #receivedDamage(NttsClientConnection, int)} is
     * necessary because if the wiretap with earplugs gadget is involved, ips are
     * given to the opposite player whichs connection is not accessible during
     * adding ips.
     *
     * @param role   The player's role who received ip
     * @param amount The amount of ip received
     */
    public void receivedIp(GameRoleEnum role, int amount) {
        ipPoints.updateValue(role, amount);
    }

    public void giftedDiamondCollar(NttsClientConnection connection) {
        if (Objects.nonNull(connection)) {
            giftedDiamondCollar.updateValue(connection, true);
        }
    }

    /**
     * @return the ipPoints
     */
    public IntegerStatisticsBuilder getIpPoints() {
        return ipPoints;
    }

    /**
     * @return the fieldsMoved
     */
    public IntegerStatisticsBuilder getFieldsMoved() {
        return fieldsMoved;
    }

    /**
     * @return the cocktailsSipped
     */
    public IntegerStatisticsBuilder getCocktailsSipped() {
        return cocktailsSipped;
    }

    /**
     * @return the cocktailsCasted
     */
    public IntegerStatisticsBuilder getCocktailsCasted() {
        return cocktailsCasted;
    }

    /**
     * @return the damageReceived
     */
    public IntegerStatisticsBuilder getDamageReceived() {
        return damageReceived;
    }

    /**
     * @return the giftedDiamondCollar
     */
    public BooleanStatisticsBuilder getGiftedDiamondCollar() {
        return giftedDiamondCollar;
    }

    /**
     * Construct the statistic report
     * 
     * @return The statistic Report
     */
    public Statistics build() {
        StatisticsEntry[] entries = new StatisticsEntry[] { ipPoints.build(), fieldsMoved.build(),
                cocktailsSipped.build(), cocktailsCasted.build(), damageReceived.build(), giftedDiamondCollar.build(),
                steppedOnCat.build(), poisonedCocktails.build() };
        return new Statistics(entries);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GameFieldStatisticsProvider [cocktailsCasted=").append(cocktailsCasted)
                .append(", cocktailsSipped=").append(cocktailsSipped).append(", damageReceived=").append(damageReceived)
                .append(", fieldsMoved=").append(fieldsMoved).append(", giftedDiamondCollar=")
                .append(giftedDiamondCollar).append(", ipPoints=").append(ipPoints).append(", poisonedCocktails=")
                .append(poisonedCocktails).append(", steppedOnCat=").append(steppedOnCat).append("]");
        return builder.toString();
    }

}