package de.uulm.team020.server.game.phases.main.statistics;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;

/**
 * Just a simple static helper method to calculate the victory
 * 
 * @author Florian Sihler
 * @version 1.0, 04/30/2020
 */
public class VictoryAnnouncer {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");

    // Hide the default one
    private VictoryAnnouncer() {
    }

    /**
     * Will determine the winner of a game given a set of statistics
     * 
     * @param statisticsProvider The statistics to generate the winner from
     * @param playerOne          The data for player one
     * @param playerTwo          The data for player two
     * @param randomController   The random-controller to enforce winner if
     *                           determined with the random-option. May be null
     * 
     * @return Information for the winner - this is a pair of its uuid and the
     *         reason for the announcement
     */
    public static ImmutablePair<UUID, VictoryEnum> announceVictor(GameFieldStatisticsProvider statisticsProvider,
            NttsClientConnection playerOne, NttsClientConnection playerTwo, RandomController randomController) {
        // Determine Winner:
        NttsClientConnection winner = null;
        VictoryEnum victoryReason = null;
        // Get the statistics
        IntegerStatisticsBuilder gotIp = statisticsProvider.getIpPoints();
        BooleanStatisticsBuilder giftCat = statisticsProvider.getGiftedDiamondCollar();
        IntegerStatisticsBuilder sippedCocktails = statisticsProvider.getCocktailsSipped();
        IntegerStatisticsBuilder castedCocktails = statisticsProvider.getCocktailsCasted();
        IntegerStatisticsBuilder damageReceived = statisticsProvider.getDamageReceived();
        // 1) most ip
        if (!gotIp.sameValues()) { // There is a winner by ip
            victoryReason = VictoryEnum.VICTORY_BY_IP;
            winner = winHigh(gotIp, playerOne, playerTwo);
        }
        // 2) gifted diamond collar
        else if (!giftCat.sameValues()) {
            victoryReason = VictoryEnum.VICTORY_BY_COLLAR;
            // should never be both true
            winner = giftCat.getPlayerOneValue().booleanValue() ? playerOne : playerTwo;
        }
        // 3) amount of sipped cocktails
        else if (!sippedCocktails.sameValues()) {
            victoryReason = VictoryEnum.VICTORY_BY_DRINKING;
            winner = winHigh(sippedCocktails, playerOne, playerTwo);
        }
        // 4) amount of casted cocktails
        else if (!castedCocktails.sameValues()) {
            victoryReason = VictoryEnum.VICTORY_BY_SPILLING;
            winner = winHigh(castedCocktails, playerOne, playerTwo);
        }
        // 5) amount of damage received
        else if (!damageReceived.sameValues()) {
            victoryReason = VictoryEnum.VICTORY_BY_HP;
            // win on lower!
            winner = winLow(damageReceived, playerOne, playerTwo);
        }
        // 6) no further constraint => random
        else { // Victory by Random
            victoryReason = VictoryEnum.VICTORY_BY_RANDOMNESS;
            winner = decideWinnerAtRandom(playerOne, playerTwo, randomController);
        }

        magpie.writeInfo("Winner was determined by: " + victoryReason + " to be: " + winner.getClientName(), "Win");
        return new ImmutablePair<>(winner.getClientId(), victoryReason);
    }

    private static NttsClientConnection decideWinnerAtRandom(NttsClientConnection playerOne,
            NttsClientConnection playerTwo, RandomController randomController) {
        NttsClientConnection winner;
        if (randomController == null) {
            winner = RandomHelper.flip() ? playerOne : playerTwo;
        } else {
            winner = randomController.requestFlip(RandomController.GLOBAL, 0.5D, RandomOperation.WINS_ON_RANDOM)
                    ? playerOne
                    : playerTwo;
        }
        return winner;
    }

    private static NttsClientConnection winHigh(IntegerStatisticsBuilder stats, NttsClientConnection playerOne,
            NttsClientConnection playerTwo) {
        return stats.getPlayerOneValue() > stats.getPlayerTwoValue() ? playerOne : playerTwo;
    }

    private static NttsClientConnection winLow(IntegerStatisticsBuilder stats, NttsClientConnection playerOne,
            NttsClientConnection playerTwo) {
        return stats.getPlayerOneValue() < stats.getPlayerTwoValue() ? playerOne : playerTwo;
    }

}