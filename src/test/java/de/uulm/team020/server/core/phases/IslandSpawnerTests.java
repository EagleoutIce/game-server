package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.helper.pathfinding.Pathfinder;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.islands.Island;
import de.uulm.team020.server.game.phases.main.islands.IslandClassifier;
import de.uulm.team020.server.game.phases.main.islands.IslandSpawner;
import de.uulm.team020.validation.GameDataGson;

/**
 * Test the spawner
 */
@Tag("Phases")
@Tag("Phase-Main")
@TestMethodOrder(OrderAnnotation.class)
public class IslandSpawnerTests {

    /**
     * Just to be used for an easy construction
     */
    private static class DummyCharacter extends Character {

        private static final long serialVersionUID = -1841595445337869969L;

        public DummyCharacter(String name) {
            super(new CharacterInformation(new CharacterDescription(name, "Ignored on construction so whatever",
                    GenderEnum.DIVERSE, List.of()), UUID.randomUUID()), List.of());
        }
    }

    @RepeatedTest(45) // Just to be sure, as it makes the test more worth it -
    // even though ths should
    // work each and every time :D
    @Tag("Spawn")
    @Order(1)
    @DisplayName("[Simple Isles] Test simple spawn on field")
    public void test_oneFieldSpawning() throws IOException {

        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("scenarios/classifier/cross.scenario"),
                Scenario.class);
        Matchconfig matchconfig = GameDataGson
                .fromJson(GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match"), Matchconfig.class);
        LinkedList<Point> safePositions = new LinkedList<>();
        LinkedList<Point> barTablePositions = new LinkedList<>();
        LinkedList<Point> barSeatPositions = new LinkedList<>();
        FieldMap map = new FieldMap(GameFieldController.populateFieldMap(scenario, safePositions, barTablePositions,
                barSeatPositions, new LinkedList<>(), matchconfig));
        IslandClassifier classifier = new IslandClassifier(map);
        Island[] islands = classifier.discoverWorld().toArray(Island[]::new);

        IslandSpawner spawner = new IslandSpawner(islands, safePositions);

        // Let's make factions
        Faction theDieters = new Faction("The Dieters");
        theDieters.add(new DummyCharacter("walter"));
        theDieters.add(new DummyCharacter("dieter"));
        theDieters.add(new DummyCharacter("klaus"));
        Faction theNiceGuys = new Faction("The nice guys");
        theNiceGuys.add(new DummyCharacter("kevin"));
        theNiceGuys.add(new DummyCharacter("olaf"));
        theNiceGuys.add(new DummyCharacter("klaus"));
        Faction theNeutrals = new Faction("The neutrals");
        theNeutrals.add(new DummyCharacter("the swappable"));
        theNeutrals.add(new DummyCharacter("the swappable2 :/"));

        boolean flip = RandomHelper.flip();
        if (flip) {
            theDieters.add(new DummyCharacter("tschefferson"));
        } else {
            theNiceGuys.add(new DummyCharacter("tschefferson"));
        }

        Set<Character> allCharacters = new HashSet<>(theDieters);
        allCharacters.addAll(theNiceGuys);
        allCharacters.addAll(theNeutrals);

        List<Boolean> shuffleMeSpawnOrder = new LinkedList<>(List.of(true, true, true, flip, false, false, false));
        // We defenitly don't have to guard that
        Collections.shuffle(shuffleMeSpawnOrder);
        Boolean[] spawnOrder = shuffleMeSpawnOrder.toArray(Boolean[]::new);
        Optional<int[]> isles = spawner.findSpawningIsles(spawnOrder);
        Assertions.assertTrue(isles.isPresent(), "There has to be a configuration to spawn them, but is not?");
        spawner.spawnPlayerCharacters(isles.get(), spawnOrder, theDieters, theNiceGuys, allCharacters);
        spawner.spawnCharactersFavourPlayers(theNeutrals, allCharacters);
        // validate first constraint: every character is placed on a free field
        for (Character character : allCharacters) {
            final Point point = character.getCoordinates();
            Assertions.assertTrue(point.isOnField(map.getField()),
                    "The character has to be spawned on a point on the field!");
            final Field field = map.getSpecificField(point);
            Assertions.assertTrue(field.isWalkable(),
                    "The character has to be spawned on a walkable field, which is not the case for: " + field
                            + " with: " + character);
        }

        // next constraint: any character can reach every safe!
        Pathfinder<Field> pathfinder = new Pathfinder<>(map);
        assureAllReachable(pathfinder, theDieters, safePositions);
        assureAllReachable(pathfinder, theNiceGuys, safePositions);
    }

    private void assureAllReachable(Pathfinder<?> pathfinder, Faction faction, List<Point> safePoints) {
        for (Point safePoint : safePoints) {
            Assertions.assertTrue(assureHasAtLeastOne(pathfinder, faction, safePoint),
                    "There has to be at least one character in the faction: " + faction
                            + " that is able to reach the safe on: " + safePoint);
        }
    }

    private boolean assureHasAtLeastOne(Pathfinder<?> pathfinder, Faction faction, Point safePoint) {
        return faction.stream().anyMatch(c -> pathfinder.connected(c.getCoordinates(), safePoint, false));
    }

}