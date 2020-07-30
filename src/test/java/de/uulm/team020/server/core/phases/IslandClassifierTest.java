package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.islands.Island;
import de.uulm.team020.server.game.phases.main.islands.IslandClassifier;
import de.uulm.team020.validation.GameDataGson;

/**
 * Test the classifier
 */
@Tag("Phases")
@Tag("Phase-Main")
@TestMethodOrder(OrderAnnotation.class)
public class IslandClassifierTest {

    private static final Field[][] field1 = new Field[][] { //
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(1) } };

    private static final Field[][] field2 = new Field[][] { //
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] {},
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE) },
            new Field[] { new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE),
                    new Field(FieldStateEnum.FREE), new Field(FieldStateEnum.FREE), new Field(1) } };

    @Test
    @Tag("Classification")
    @Order(1)
    @DisplayName("[OneField] Classify one Field")
    public void test_oneFieldClassification() throws IOException {
        FieldMap map = new FieldMap(field1);
        IslandClassifier classifier = new IslandClassifier(map);
        List<Island> islands = classifier.discoverWorld();

        // Field is 5x5 with one safe => 24 fields and one safe at (4,4)

        // General
        Assertions.assertEquals(1, islands.size(), "There should be exactly one island, but: " + islands);
        Island island = islands.get(0);
        // Safe
        Assertions.assertEquals(1, island.getSafePositions().size(), "There should be exactly one safe");
        Point safePoint = island.getSafePositions().toArray(Point[]::new)[0];
        Assertions.assertEquals(new Point(4, 4), safePoint, "Safe has to be at (4,4)");
        // Points
        Set<Point> points = island.getPoints();
        Assertions.assertEquals(24, points.size(), "There should be 24 points mapped, but: " + points);

        points.forEach(
                p -> Assertions.assertTrue(p.isOnField(field1), "Every point should be on the field, but: " + p));

        // Assert that every free point from the training board appears in the island:
        for (int y = 0; y < field1.length; y++) {
            for (int x = 0; x < field1[y].length; x++) {
                if (field1[y][x].getState() == FieldStateEnum.SAFE)
                    continue;
                final Point p = new Point(x, y);
                Assertions.assertTrue(points.contains(p), "Point: " + p + " should be in the collected, but isn't!");
            }
        }
    }

    @Test
    @Tag("Classification")
    @Order(2)
    @DisplayName("[OneField] Classify one Field with break and one safe")
    public void test_oneFieldClassificationWithBreak() throws IOException {
        FieldMap map = new FieldMap(field2);
        IslandClassifier classifier = new IslandClassifier(map);
        List<Island> islands = classifier.discoverWorld();

        // Field is 5x5 with one safe and split => 10 fields possible with one safe at
        // (4,4) => 9

        // General
        Assertions.assertEquals(1, islands.size(), "There should be exactly one island, but: " + islands);
        Island island = islands.get(0);
        // Safe
        Assertions.assertEquals(1, island.getSafePositions().size(), "There should be exactly one safe");
        Point safePoint = island.getSafePositions().toArray(Point[]::new)[0];
        Assertions.assertEquals(new Point(4, 4), safePoint, "Safe has to be at (4,4)");
        // Points
        Set<Point> points = island.getPoints();
        Assertions.assertEquals(9, points.size(), "There should be 9 points mapped, but: " + points);

        points.forEach(
                p -> Assertions.assertTrue(p.isOnField(field2), "Every point should be on the field, but: " + p));

        // Assert that every free point from the training board appears in the island:
        for (int y = 3; y < field2.length; y++) {
            for (int x = 0; x < field2[y].length; x++) {
                if (field2[y][x].getState() == FieldStateEnum.SAFE)
                    continue;
                final Point p = new Point(x, y);
                Assertions.assertTrue(points.contains(p), "Point: " + p + " should be in the collected, but isn't!");
            }
        }
    }

    @Test
    @Tag("Classification")
    @Order(3)
    @DisplayName("[OneField] Classify a scenario")
    public void test_classificationForScenario() throws IOException {
        // Get the scenario:
        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("scenarios/classifier/cross.scenario"),
                Scenario.class);
        Matchconfig matchconfig = GameDataGson
                .fromJson(GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match"), Matchconfig.class);
        List<Point> safePositions = new LinkedList<>();
        List<Point> barTablePositions = new LinkedList<>();
        List<Point> barSeatPositions = new LinkedList<>();
        FieldMap map = new FieldMap(GameFieldController.populateFieldMap(scenario, safePositions, barTablePositions,
                barSeatPositions, new LinkedList<>(), matchconfig));
        IslandClassifier classifier = new IslandClassifier(map);
        List<Island> islands = classifier.discoverWorld();
        Assertions.assertEquals(6, islands.size(), "There should be exactly six islands, but: " + islands);
        // we want 6 exclusive islands
        // independent of calc order:
        // first one (pretty-help: 0)
        islandAssertionsHelper(islands, Set.of(new Point(4, 7)),
                Set.of(new Point(6, 5), new Point(2, 2), new Point(6, 6), new Point(2, 3), new Point(2, 4),
                        new Point(2, 5), new Point(2, 6), new Point(5, 2), new Point(5, 3), new Point(3, 2),
                        new Point(5, 4), new Point(3, 3), new Point(5, 5), new Point(1, 2), new Point(3, 4),
                        new Point(5, 6), new Point(1, 3), new Point(3, 5), new Point(1, 4), new Point(3, 6),
                        new Point(1, 5), new Point(1, 6), new Point(6, 2), new Point(6, 3), new Point(6, 4)));
        // first one (pretty-help: 1)
        islandAssertionsHelper(islands, Set.of(new Point(3, 8)), Set.of(new Point(1, 8), new Point(2, 8)));
        // first one (pretty-help: 2) -- center tile
        islandAssertionsHelper(islands, Set.of(new Point(4, 7), new Point(5, 8), new Point(3, 8), new Point(4, 9)),
                Set.of(new Point(4, 8)));
        // first one (pretty-help: 3) -- right out
        islandAssertionsHelper(islands, Set.of(new Point(5, 8)), Set.of(new Point(6, 8)));
        // first one (pretty-help: 4) -- right out
        islandAssertionsHelper(islands, Set.of(new Point(4, 9), new Point(4, 15)),
                Set.of(new Point(6, 10), new Point(6, 11), new Point(6, 12), new Point(6, 13), new Point(2, 10),
                        new Point(6, 14), new Point(2, 11), new Point(2, 12), new Point(2, 13), new Point(2, 14),
                        new Point(5, 10), new Point(5, 11), new Point(3, 10), new Point(5, 12), new Point(3, 11),
                        new Point(5, 13), new Point(1, 10), new Point(3, 12), new Point(5, 14), new Point(1, 11),
                        new Point(3, 13), new Point(1, 12), new Point(3, 14), new Point(1, 13), new Point(1, 14)));
        // first one (pretty-help: 5) -- right out
        islandAssertionsHelper(islands, Set.of(new Point(4, 15)),
                Set.of(new Point(1, 16), new Point(0, 16), new Point(8, 14), new Point(8, 15), new Point(7, 16),
                        new Point(6, 16), new Point(5, 16), new Point(4, 16), new Point(3, 16), new Point(2, 16)));
    }

    private void islandAssertionsHelper(List<Island> islands, Set<Point> safeSignature, Set<Point> wanted) {
        // order independent, as hash
        Set<Island> island0 = islands.stream().filter(i -> i.getSafePositions().equals(safeSignature))
                .collect(Collectors.toSet());

        Assertions.assertEquals(1, island0.size(), "There should be only one island0 with this safe-id");
        Island island = island0.toArray(Island[]::new)[0];

        // Assert same but can differ in order
        Set<Point> got = island.getPoints();
        Assertions.assertEquals(wanted.size(), got.size(),
                "Should have same size, but: " + wanted + " and " + got + ".");
        Assertions.assertTrue(wanted.containsAll(got),
                "Wanted should contain all got, but: " + wanted + " and " + got + ".");
        Assertions.assertTrue(got.containsAll(wanted),
                "Got should contain all wanted, but: " + wanted + " and " + got + ".");
    }

}