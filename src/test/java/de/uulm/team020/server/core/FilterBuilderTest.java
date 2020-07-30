package de.uulm.team020.server.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.FilterBuilder;
import de.uulm.team020.server.core.datatypes.IMessageFilter;

/**
 * This class is designed to test the functionality of the filter builder by providing a default test set to check all the filters with :)
 */
public class FilterBuilderTest {
    
    public static final Configuration DEFAULT_CONFIGURATION = Configuration.buildFromArgs("--defaults");

    /**
     * This will populate the default configuration but directly set it into the requested phase so that the check may be performed as wanted.
     * In fact this will spawn a new configuration for every call allowing the execution to be multi-threaded.
     */
    private static Configuration buildConfigInPhase(GamePhaseEnum phase) {
        Configuration config = Configuration.buildFromArgs("--defaults");
        config.shiftPhase(phase);
        return config;
    }



    // The test set, the object is used to easily handle Test-Items
    private static class TestData {
        private final Configuration configuration;
        private final RoleEnum role;
        private final UUID playerId;
        private final int strikeCount;
        private final boolean isConnected;

        public TestData(Configuration configuration, RoleEnum role, UUID playerId, int strikeCount,
                boolean isConnected) {
            this.configuration = configuration;
            this.role = role;
            this.playerId = playerId;
            this.strikeCount = strikeCount;
            this.isConnected = isConnected;
        }

        public TestData(GamePhaseEnum phase, RoleEnum role, UUID playerId, int strikeCount,
                boolean isConnected) {
            this.configuration = buildConfigInPhase(phase);
            this.role = role;
            this.playerId = playerId;
            this.strikeCount = strikeCount;
            this.isConnected = isConnected;
        }

        public String apply(IMessageFilter filter) {
            return filter.filter(this.getConfiguration(), this.getRole(), this.getPlayerId(), this.getStrikeCount(), this.isConnected());
        }

        public Configuration getConfiguration() {
            return configuration;
        }

        public RoleEnum getRole() {
            return role;
        }

        public UUID getPlayerId() {
            return playerId;
        }

        public int getStrikeCount() {
            return strikeCount;
        }

        public boolean isConnected() {
            return isConnected;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("TestData [configuration=").append(configuration).append(", isConnected=")
                    .append(isConnected).append(", playerId=").append(playerId).append(", role=").append(role)
                    .append(", strikeCount=").append(strikeCount).append("]");
            return builder.toString();
        }

    }

    private static boolean[] invertBooleanArray(boolean[] data) {
        boolean[] output = new boolean[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = !data[i];
        }
        return output;
    }

    // This will add the negated variant too
    private static List<Arguments> createTestSetFor(final FilterBuilder filterBuilder, final boolean... expected) {
        List<Arguments> data = createTestSetFor(filterBuilder.build(), expected);//
            data.addAll(createTestSetFor(new FilterBuilder(filterBuilder).negate().build(), invertBooleanArray(expected)));
            return data;
    }

    private static List<Arguments> createTestSetFor(final IMessageFilter filter, final boolean... expected) {
        List<Arguments> tests = new ArrayList<>(expected.length);
        for (int i = 0; i < expected.length && i < testDataSet.length; i++) {
            tests.add(Arguments.arguments(filter, testDataSet[i], expected[i]));
        }
        return tests;
    }

    private static List<Arguments> createTestSetFor(final FilterBuilder filterBuilder, final IMessageFilter expected) {
        List<Arguments> data = createTestSetFor(filterBuilder.build(), expected, false);//
        data.addAll(createTestSetFor(new FilterBuilder(filterBuilder).negate().build(), expected, true));
        return data;
    }

    private static List<Arguments> createTestSetFor(final IMessageFilter filter, final IMessageFilter expected, boolean negate) {
        List<Arguments> tests = new ArrayList<>(testDataSet.length);
        for (int i = 0; i < testDataSet.length; i++) {
            tests.add(Arguments.arguments(filter, testDataSet[i], testDataSet[i].apply(expected).isEmpty() ^ negate));
        }
        return tests;
    }
    
    protected static TestData[] testDataSet = {
        // strikes and connection
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 0, true),                // 0
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 1, true),                // 1
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 2, true),                // 2
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 3, true),                // 3
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 4, true),                // 4
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 5, true),                // 5
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 0, false),               // 6
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 1, false),               // 7
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 2, false),               // 8
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 3, false),               // 9
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 4, false),               // 10
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.SPECTATOR, UUID.randomUUID(), 5, false),               // 11
        // role
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.AI, UUID.randomUUID(), 4, true),                       // 12
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.PLAYER, UUID.randomUUID(), 4, true),                   // 13
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.PLAYER, UUID.randomUUID(), 2, false),                  // 14
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.PLAYER, UUID.randomUUID(), 2, true),                   // 15
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.AI, UUID.randomUUID(), 1, false),                      // 16
        new TestData(DEFAULT_CONFIGURATION, RoleEnum.AI, UUID.randomUUID(), 0, true),                       // 17
        // Phase
        new TestData(GamePhaseEnum.GAME_FORCE_PAUSED, RoleEnum.AI, UUID.randomUUID(), 0, true),             // 18
        new TestData(GamePhaseEnum.MAIN_GAME_END, RoleEnum.PLAYER, UUID.randomUUID(), 9, true),             // 19
        new TestData(GamePhaseEnum.MAIN_GAME_PLAY, RoleEnum.SPECTATOR, UUID.randomUUID(), -1, false),       // 20
        new TestData(GamePhaseEnum.END, RoleEnum.PLAYER, UUID.randomUUID(), 3, true),                       // 21
        new TestData(GamePhaseEnum.GAME_START, RoleEnum.PLAYER, UUID.randomUUID(), 9, true),                // 22
    };




    // Tests

    private static Stream<Arguments> generate_allTests() {
        // once i was halfway in i wanted an easier way to create those tests :D the hard coded ones do not cover all, the others should
        List<Arguments> allTests = new ArrayList<>(60);
        // general
        allTests.addAll(createTestSetFor(FilterBuilder.trueFilter(), true, true, true, true, true, true, true, true, true, true, true, true,/*Role:*/  true, true, true, true, true, true));
        allTests.addAll(createTestSetFor(new FilterBuilder(), true, true, true, true, true, true, true, true, true, true, true, true,/*Role:*/ true, true, true, true, true, true));
        // strikes
        allTests.addAll(createTestSetFor(new FilterBuilder().shouldBeStrikes(new Integer[] {1}), false, true, false, false, false, false, false, true, false, false, false, false,/*Role:*/ false, false, false, false, true, false));
        allTests.addAll(createTestSetFor(new FilterBuilder().betweenStrikeCounts(2, 5), false, false, true, true, true, true, false, false, true, true, true, true,/*Role:*/ true, true, true, true, false, false));
        allTests.addAll(createTestSetFor(new FilterBuilder().betweenStrikeCounts(0, 0), true, false, false, false, false, false, true, false, false, false, false, false,/*Role:*/ false, false, false, false, false, true));
        // connection
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected(), true, true, true, true, true, true, false, false, false, false, false, false,/*Role:*/ true, true, false, true, false, true));
        // connection and strikes
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().shouldBeStrikes(new Integer[] {1}), false, true, false, false, false, false, false, false, false, false, false,/*Role:*/ false, false, false, false, false, false));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().betweenStrikeCounts(2, 5), false, false, true, true, true, true, false, false, false, false, false, false,/*Role:*/ true, true, false, true, false, false));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().betweenStrikeCounts(0, 0), true, false, false, false, false, false, false, false, false, false, false, false,/*Role:*/ false, false, false, false, false, true));
        // behaviour changes here
        allTests.addAll(createTestSetFor(new FilterBuilder().shouldBeStrikes(new Integer[] {1}), (c, r, p, s, i) -> {return s == 1 ? "" : "Strike was not one: " + s;}));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().betweenStrikeCounts(2, 5), (c, r, p, s, i) -> {return (i && s >= 2 && s <= 5) ? "" : "Not connected (i: " + i + " or strike not in between (2,5): " + s;}));
        allTests.addAll(createTestSetFor(FilterBuilder.trueFilter(), (c, r, p, s, i) -> { return ""; }, false));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().inPause(), (c, r, p, s, i) -> { return (i && (c.getGamePhase() == GamePhaseEnum.GAME_PAUSED || c.getGamePhase() == GamePhaseEnum.GAME_FORCE_PAUSED)) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeDisconnected().inPause(), (c, r, p, s, i) -> { return (!i && (c.getGamePhase() == GamePhaseEnum.GAME_PAUSED || c.getGamePhase() == GamePhaseEnum.GAME_FORCE_PAUSED)) ? "" : "!con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().inPause().negatePhases(), (c, r, p, s, i) -> { return (c.getGamePhase() != GamePhaseEnum.GAME_PAUSED && c.getGamePhase() != GamePhaseEnum.GAME_FORCE_PAUSED) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().inPause().negatePhases(), (c, r, p, s, i) -> { return (i && c.getGamePhase() != GamePhaseEnum.GAME_PAUSED && c.getGamePhase() != GamePhaseEnum.GAME_FORCE_PAUSED) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().shouldBeInGamePhases(GamePhaseEnum.GAME_START, GamePhaseEnum.MAIN_GAME_END).orInPause(), (c, r, p, s, i) -> { return (i && ((c.getGamePhase().equals(GamePhaseEnum.GAME_START) || c.getGamePhase().equals(GamePhaseEnum.MAIN_GAME_END))) || (c.getGamePhase() == GamePhaseEnum.GAME_PAUSED || c.getGamePhase() == GamePhaseEnum.GAME_FORCE_PAUSED)) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeConnected().betweenGamePhases(GamePhaseEnum.GAME_START, GamePhaseEnum.MAIN_GAME_END), (c, r, p, s, i) -> { return (i && (c.getGamePhase().isAfterOrEqual(GamePhaseEnum.GAME_START) && (c.getGamePhase().isBefore(GamePhaseEnum.MAIN_GAME_END) || c.getGamePhase().equals(GamePhaseEnum.MAIN_GAME_END)))) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().hasToBeDisconnected().shouldBeInGamePhases(GamePhaseEnum.GAME_START, GamePhaseEnum.MAIN_GAME_END).orInPause(), (c, r, p, s, i) -> { return (!i && (c.getGamePhase().equals(GamePhaseEnum.GAME_START) || c.getGamePhase().equals(GamePhaseEnum.MAIN_GAME_END) || c.getGamePhase() == GamePhaseEnum.GAME_PAUSED || c.getGamePhase() == GamePhaseEnum.GAME_FORCE_PAUSED) ) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().betweenGamePhases(GamePhaseEnum.GAME_START, GamePhaseEnum.MAIN_GAME_END), (c, r, p, s, i) -> { return ((c.getGamePhase().isAfterOrEqual(GamePhaseEnum.GAME_START) && (c.getGamePhase().isBefore(GamePhaseEnum.MAIN_GAME_END) || c.getGamePhase().equals(GamePhaseEnum.MAIN_GAME_END)))) ? "" : "con: " + i + " phase: " + c.getGamePhase() ; }));
        // specific role checks
        allTests.addAll(createTestSetFor(new FilterBuilder().shouldBeRole(RoleEnum.SPECTATOR), (c, r, p, s, i) -> { return (r == RoleEnum.SPECTATOR) ? "" : "role (spectator): " + r ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().shouldNotBeRole(RoleEnum.AI, RoleEnum.PLAYER), (c, r, p, s, i) -> { return (r == RoleEnum.SPECTATOR) ? "" : "role (spectator,2): " + r ; }));
        allTests.addAll(createTestSetFor(new FilterBuilder().shouldBeRole(RoleEnum.AI, RoleEnum.PLAYER), (c, r, p, s, i) -> { return (r == RoleEnum.AI || r == RoleEnum.PLAYER) ? "" : "role (!spectator): " + r ; }));
        return allTests.stream();
    }


    @ParameterizedTest
    @Order(1)
    @MethodSource("generate_allTests")
    @DisplayName("[FilterBuilder] mainTest-Set")
    public void test_mainFilters(final IMessageFilter filter, final TestData data, final boolean expected) {
        String got = data.apply(filter);
        if(expected) {
            Assertions.assertTrue(got.isEmpty(), "Should be empty for: " + data + " but was: " + got);
        } else {
            Assertions.assertFalse(got.isEmpty(), "Should not be empty for: " + data + " but was: " + got);
        }
    }




}