package de.uulm.team020.server.core.phases;

import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;
import de.uulm.team020.server.game.phases.main.statistics.VictoryAnnouncer;

/**
 * Just checks if the victor-validation works
 */
public class VictoryAnnouncerTests {

    public static NttsClientConnection PLAYER_ONE = NttsClientConnection.buildFromData(null, UUID.randomUUID(),
            "player-one", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
    public static NttsClientConnection PLAYER_TWO = NttsClientConnection.buildFromData(null, UUID.randomUUID(),
            "player-cat", RoleEnum.AI, GameRoleEnum.PLAYER_TWO);

    public static Stream<Arguments> generate_victoryConfigurations() {
        return Stream.of(
                Arguments.arguments(5, 4, false, false, 0, 0, 0, 0, 0, 0, VictoryEnum.VICTORY_BY_IP, PLAYER_ONE),
                Arguments.arguments(5, 4, false, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_IP, PLAYER_ONE),
                Arguments.arguments(4, 7, false, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_IP, PLAYER_TWO),
                Arguments.arguments(4, 0, false, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_IP, PLAYER_ONE),
                Arguments.arguments(0, 6, false, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_IP, PLAYER_TWO),
                Arguments.arguments(4, 4, false, true, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_COLLAR, PLAYER_TWO),
                Arguments.arguments(4, 4, true, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_COLLAR, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 5, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_DRINKING, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 2, 3, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_DRINKING, PLAYER_TWO),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 7, -2, 7, VictoryEnum.VICTORY_BY_SPILLING, PLAYER_TWO),
                Arguments.arguments(4, 4, false, false, 5, 5, 7, 6, -2, 7, VictoryEnum.VICTORY_BY_SPILLING, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 4, -2, 7, VictoryEnum.VICTORY_BY_SPILLING, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 5, 5, 3, 2, -2, 7, VictoryEnum.VICTORY_BY_SPILLING, PLAYER_ONE),
                Arguments.arguments(5, 5, false, false, 5, 5, 6, 12, -2, 7, VictoryEnum.VICTORY_BY_SPILLING,
                        PLAYER_TWO),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 6, -2, 7, VictoryEnum.VICTORY_BY_HP, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 6, 3, 7, VictoryEnum.VICTORY_BY_HP, PLAYER_ONE),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 6, 8, 7, VictoryEnum.VICTORY_BY_HP, PLAYER_TWO),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 6, 6, 3, VictoryEnum.VICTORY_BY_HP, PLAYER_TWO),
                Arguments.arguments(4, 4, false, false, 5, 5, 6, 6, 6, 6, VictoryEnum.VICTORY_BY_RANDOMNESS, null),
                Arguments.arguments(0, 0, false, false, 5, 5, 6, 6, 6, 6, VictoryEnum.VICTORY_BY_RANDOMNESS, null),
                Arguments.arguments(36, 36, false, false, 5, 5, 6, 6, 6, 6, VictoryEnum.VICTORY_BY_RANDOMNESS, null),
                Arguments.arguments(3, 3, false, false, 5, 5, 6, 6, 13, 13, VictoryEnum.VICTORY_BY_RANDOMNESS, null),
                Arguments.arguments(4, 4, false, false, 5, 5, 42, 42, 6, 6, VictoryEnum.VICTORY_BY_RANDOMNESS, null));
    }

    @ParameterizedTest
    @Tag("Core")
    @Order(1)
    @DisplayName("[Phase] Test messages on a crash for current player in midst of a current round")
    @MethodSource("generate_victoryConfigurations")
    public void test_victoryConfigurations(int ipA, int ipB, boolean diamondA, boolean diamondB, int sippedA,
            int sippedB, int spilledA, int spilledB, int hpA, int hpB, VictoryEnum expectedType,
            NttsClientConnection expectedWinner) {

        // setup the statistics
        GameFieldStatisticsProvider statisticsProvider = new GameFieldStatisticsProvider();
        // ip
        statisticsProvider.getIpPoints().setValue(PLAYER_ONE, ipA);
        statisticsProvider.getIpPoints().setValue(PLAYER_TWO, ipB);
        // diamond
        statisticsProvider.getGiftedDiamondCollar().setValue(PLAYER_ONE, diamondA);
        statisticsProvider.getGiftedDiamondCollar().setValue(PLAYER_TWO, diamondB);
        // cocktails
        statisticsProvider.getCocktailsSipped().setValue(PLAYER_ONE, sippedA);
        statisticsProvider.getCocktailsSipped().setValue(PLAYER_TWO, sippedB);
        // casts
        statisticsProvider.getCocktailsCasted().setValue(PLAYER_ONE, spilledA);
        statisticsProvider.getCocktailsCasted().setValue(PLAYER_TWO, spilledB);
        // hp
        statisticsProvider.getDamageReceived().setValue(PLAYER_ONE, hpA);
        statisticsProvider.getDamageReceived().setValue(PLAYER_TWO, hpB);

        ImmutablePair<UUID, VictoryEnum> winner = VictoryAnnouncer.announceVictor(statisticsProvider, PLAYER_ONE,
                PLAYER_TWO, null);

        Assertions.assertNotNull(winner, "Winner shall never be null!");
        Assertions.assertNotNull(winner.getKey(), "Winner uuid shall never be null!");
        Assertions.assertNotNull(winner.getValue(), "Winner type shall never be null!");

        Assertions.assertEquals(expectedType, winner.getValue(),
                "Shall be the expected Type for: " + statisticsProvider);
        if (expectedType != VictoryEnum.VICTORY_BY_RANDOMNESS) { // if random we just check if one of p1 or p2
            Assertions.assertEquals(expectedWinner.getClientId(), winner.getKey(),
                    "Shall be the expected Winner for: " + statisticsProvider);
        } else {
            Assertions.assertTrue(
                    Objects.equals(winner.getKey(), PLAYER_ONE.getClientId())
                            || Objects.equals(winner.getKey(), PLAYER_TWO.getClientId()),
                    "Winner must be always p1 or p2, not: " + winner.getKey() + " for, p1: " + PLAYER_ONE + " and p2: "
                            + PLAYER_TWO);
        }
    }

}