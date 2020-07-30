package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner;
import de.uulm.team020.validation.GameDataGson;

/**
 * Test the positioner
 */
public class GameFieldPositionerTest {

    /**
     * Just to be used for an easy construction
     */
    private static class DummyCharacter extends Character {

        private static final long serialVersionUID = -1841595445337869969L;

        public DummyCharacter(int x, int y) {
            super(new CharacterInformation(new CharacterDescription(RandomHelper.randomString(12),
                    "Ignored on construction so whatever", GenderEnum.DIVERSE, List.of()), UUID.randomUUID()),
                    List.of());
            setCoordinates(new Point(x, y));
        }
    }

    private static final String ONE = "scenarios/freeWalls.scenario";
    private static final String TWO = "scenarios/freeWalls2.scenario";
    private static final String TRE = "scenarios/freeWalls3.scenario";
    private static final String FOR = "scenarios/freeWalls4.scenario";

    public static Stream<Arguments> generate_closestFreeField() {
        return Stream.of(Arguments.arguments(ONE, new Point(0, 0), Set.of(new Point(1, 0), new Point(1, 1))),
                Arguments.arguments(ONE, new Point(1, 0), Set.of(new Point(1, 0))),
                Arguments.arguments(ONE, new Point(3, 0), Set.of(new Point(3, 0))),
                Arguments.arguments(ONE, new Point(2, 1),
                        Set.of(new Point(1, 0), new Point(1, 1), new Point(3, 0), new Point(3, 1), new Point(1, 2))),
                Arguments.arguments(ONE, new Point(0, 5), Set.of(new Point(0, 4))),
                Arguments.arguments(TWO, new Point(0, 5), Set.of(new Point(0, 4))),
                Arguments.arguments(TWO, new Point(2, 2), Set.of(new Point(1, 0), new Point(3, 0), new Point(0, 4))),
                Arguments.arguments(TWO, new Point(2, 1), Set.of(new Point(1, 0), new Point(3, 0))),
                Arguments.arguments(TWO, new Point(2, 3), Set.of(new Point(0, 4), new Point(2, 5), new Point(3, 5))),
                Arguments.arguments(TWO, new Point(3, 3), Set.of(new Point(2, 5), new Point(3, 5))),
                Arguments.arguments(TRE, new Point(3, 3), Set.of(new Point(0, 4))),
                Arguments.arguments(TRE, new Point(0, 4), Set.of(new Point(0, 4))),
                Arguments.arguments(TRE, new Point(3, 5), Set.of(new Point(0, 4))),
                Arguments.arguments(TRE, new Point(0, 0), Set.of(new Point(0, 4))),
                Arguments.arguments(TRE, new Point(1, 1), Set.of(new Point(0, 4))),
                Arguments.arguments(FOR, new Point(3, 3), null), Arguments.arguments(FOR, new Point(0, 4), null),
                Arguments.arguments(FOR, new Point(3, 5), null), Arguments.arguments(FOR, new Point(0, 0), null),
                Arguments.arguments(FOR, new Point(1, 1), null));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(1)
    @DisplayName("[Field] Get closest free field")
    @MethodSource("generate_closestFreeField")
    public void test_closestFreeField(final String use, final Point seed, final Set<Point> oneOf) throws IOException {
        final Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson(use), Scenario.class);
        final Matchconfig matchconfig = GameDataGson
                .fromJson(GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match"), Matchconfig.class);
        final FieldMap map = new FieldMap(GameFieldController.populateFieldMap(scenario, new LinkedList<>(),
                new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), matchconfig));

        Set<Character> allCharacters = Set.of(new DummyCharacter(0, 0), new DummyCharacter(0, 5),
                new DummyCharacter(2, 1), new DummyCharacter(2, 2));

        // try multiple times
        for (int i = 0; i < 12; i++) {
            Optional<Point> mayGot = GameFieldPositioner.getClosestFreeField(allCharacters, map, seed, null, true);
            if (oneOf != null) {
                Assertions.assertTrue(mayGot.isPresent(), "Must have result for: " + seed);
                Point got = mayGot.get();
                Assertions.assertTrue(oneOf.contains(got),
                        "Must be one of: " + oneOf + " but not: " + got + " for seed: " + seed);
            } else {
                Assertions.assertFalse(mayGot.isPresent(), "Must have NO result for: " + seed + " but: " + mayGot);
            }
        }
    }

    public static Stream<Arguments> generate_closestFreeFieldNoFade() {
        return Stream.of(Arguments.arguments(ONE, new Point(0, 0), Set.of(new Point(1, 0), new Point(1, 1))),
                Arguments.arguments(TRE, new Point(1, 1), null), Arguments.arguments(FOR, new Point(3, 3), null),
                Arguments.arguments(FOR, new Point(0, 4), null), Arguments.arguments(FOR, new Point(3, 5), null),
                Arguments.arguments(FOR, new Point(0, 0), null), Arguments.arguments(FOR, new Point(1, 1), null));
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(1)
    @DisplayName("[Field] Get closest free field without fading")
    @MethodSource("generate_closestFreeFieldNoFade")
    public void test_closestFreeFieldNoFade(final String use, final Point seed, final Set<Point> oneOf)
            throws IOException {
        final Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson(use), Scenario.class);
        final Matchconfig matchconfig = GameDataGson
                .fromJson(GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match"), Matchconfig.class);
        final FieldMap map = new FieldMap(GameFieldController.populateFieldMap(scenario, new LinkedList<>(),
                new LinkedList<>(), new LinkedList<>(), new LinkedList<>(), matchconfig));

        Set<Character> allCharacters = Set.of(new DummyCharacter(0, 0), new DummyCharacter(0, 5),
                new DummyCharacter(2, 1), new DummyCharacter(2, 2));
        // try multiple times
        for (int i = 0; i < 12; i++) {
            Optional<Point> mayGot = GameFieldPositioner.getClosestFreeField(allCharacters, map, seed, null, false);
            if (oneOf != null) {
                Assertions.assertTrue(mayGot.isPresent(), "Must have result for: " + seed);
                Point got = mayGot.get();
                Assertions.assertTrue(oneOf.contains(got),
                        "Must be one of: " + oneOf + " but not: " + got + " for seed: " + seed);
            } else {
                Assertions.assertFalse(mayGot.isPresent(), "Must have NO result for: " + seed + " but: " + mayGot);
            }
        }

    }
}