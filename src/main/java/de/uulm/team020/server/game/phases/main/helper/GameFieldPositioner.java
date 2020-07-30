package de.uulm.team020.server.game.phases.main.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ConfigurationException;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.islands.Island;
import de.uulm.team020.server.game.phases.main.islands.IslandClassifier;
import de.uulm.team020.server.game.phases.main.islands.IslandSpawner;
import de.uulm.team020.server.game.phases.main.islands.SpawnException;

/**
 * Helps the {@link GameFieldController} by providing the place-functions. They
 * are used to setup the main game board when switching phases.
 *
 * @author Florian Sihler
 * @version 1.1, 05/03/2020
 */
public class GameFieldPositioner {

    private static final String SERVER_TEXT = "Server";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private GameFieldController controller;
    private Set<Character> characters;
    private Configuration configuration;

    private List<Point> safePositions;

    private Faction playerOneFaction;
    private Faction playerTwoFaction;
    private Faction neutralFaction;

    public GameFieldPositioner(GameFieldController controller, Configuration configuration, Set<Character> characters,
            List<Point> safePositions, Faction playerOneFaction, Faction playerTwoFaction, Faction neutralFaction) {
        this.controller = controller;
        this.characters = characters;
        this.configuration = configuration;
        this.safePositions = safePositions;
        this.playerOneFaction = playerOneFaction;
        this.playerTwoFaction = playerTwoFaction;
        this.neutralFaction = neutralFaction;
    }

    /**
     * Places all characters on the given game-field so that the following
     * constraints are fulfilled:
     * <ul>
     * <li>Every safe is reachable by at least one character of p1 _and_ p2</li>
     * <li>Every therefore created 'isle' is populated with a 'reasonable' amount of
     * spacing</li>
     * </ul>
     * If those constraints are not fulfill-able for <i>any</i> reason it depends on
     * the server-configuration if an error is to be thrown or if just a warning is
     * issued.
     *
     * @param characterPlacements placement enforcements - if they are to be
     *                            desired.
     */
    public void positionCharacters(Map<String, Point> characterPlacements) {
        magpie.writeInfo("Started placement for " + characters.size() + " Characters. Starting by calculating islands.",
                "Pos.");
        // Classify the island
        controller.setClassifier(new IslandClassifier(controller.getMap()));

        // enforce if needed:
        if (characterPlacements != null) {
            positionCharactersEnforced(characterPlacements);
            return;
        }

        if (configuration.getServerConfig().useIslandsForSpawn()) {
            controller.setIslands(controller.getClassifier().discoverWorld().toArray(Island[]::new));
            if (controller.getIslands() == null || controller.getIslands().length == 0) {
                Island everything = new Island();
                everything.addPoints(controller.getClassifier().getValidSpawns());
                // no islands found => no safe
                controller.setIslands(new Island[] { everything });
            }

            // Summon p1 and p2 in a random order
            // So first: add their character-counts to the list:
            final Island[] islands = controller.getIslands();
            IslandSpawner spawner = new IslandSpawner(islands, safePositions);
            Boolean[] spawnOrder = getSpawnOrder();
            Optional<int[]> isles = spawner.findSpawningIsles(spawnOrder,
                    configuration.getServerConfig().islandSpawnerMayMorePlayers());
            if (!isles.isPresent()) {
                magpie.writeError("No combination was found on spawning!", "Spawn");
                throw new SpawnException("No valid configuration found for: " + Arrays.toString(islands)
                        + ". As the server doesn't allow this, this will result in a end of execution. If you do not want to check for safes, disable the safe-utility via the server-config. ");
            } else {
                spawner.spawnPlayerCharacters(isles.get(), spawnOrder, playerOneFaction, playerTwoFaction, characters);
            }

            // Spawn all neutral-factions
            spawner.spawnCharactersFavourPlayers(neutralFaction, characters);
            // is there a good island to use?:
            Set<Character> allPlayers = new HashSet<>(playerOneFaction);
            allPlayers.addAll(playerTwoFaction);
            int maxIsland = spawner.getMaxPlayerIsland(/* faction1 & faction2: */ allPlayers, characters);
            if (maxIsland >= 0) {
                spawner.spawnCharacter(maxIsland, controller.getCat(),
                        characters.stream().map(Character::getCoordinates).collect(Collectors.toSet()));
            } else {
                spawner.spawnCharacters(Set.of(controller.getCat()), characters);
            }
        } else {
            // don't care for anything ;)
            List<Point> possibles = new ArrayList<>(controller.getClassifier().getValidSpawns());
            Set<Point> occupied = new HashSet<>(characters.size());
            for (Character character : characters) {
                occupied.add(IslandSpawner.spawnCharacter(possibles, character, occupied));
            }
            // spawn the cat
            IslandSpawner.spawnCharacter(possibles, controller.getCat(), occupied);
        }

    }

    private void positionCharactersEnforced(Map<String, Point> characterPlacements) {
        // enforce!
        if (characterPlacements.size() != characters.size()) {
            throw new ConfigurationException("Injected placements with: " + characterPlacements
                    + " but the characters: " + characters + " do not match in size.");
        }
        magpie.writeWarning("Injecting character starting positions with: " + characterPlacements, "Pos.");
        for (Character character : characters) {
            final Point got = characterPlacements.get(character.getName());
            if (got == null) {
                throw new ConfigurationException(
                        "No mapping for character: " + character.getName() + " on forced placements: "
                                + characterPlacements + " and characters: " + characters + " - this is critical!");
            }
            character.setCoordinates(got);
        }

        // still perform classification if desired:
        if (configuration.getServerConfig().useIslandsForSpawn()) {
            controller.setIslands(controller.getClassifier().discoverWorld().toArray(Island[]::new));
        }
    }

    private Boolean[] getSpawnOrder() {
        ArrayList<Boolean> playerOrder = new ArrayList<>(playerOneFaction.size() + playerTwoFaction.size());
        // stupid fillings:
        playerOneFaction.forEach(p -> playerOrder.add(true)); // p1
        playerTwoFaction.forEach(p -> playerOrder.add(false));// p2
        // This should not be called on injection so, we're fine
        Collections.shuffle(playerOrder);
        return playerOrder.toArray(Boolean[]::new);
    }

    /**
     * Will find the closest free Field to a given position. The method will first
     * check all free neighbours - if there are none it will check the neighbours of
     * them and so on - this allows a character to fade through walls (if the server
     * config allows it). Please note, that it is up to you to check if the target
     * field contains a gadget and to collect it.
     * <p>
     * This method will only pick fields with no characters on them.
     *
     * @param controller The {@link GameFieldController} to get the data from
     * @param seed       The position to start checking from -- if the position
     *                   itself is free, the method will return the seed.
     *
     * @return The free position if found.
     *
     * @see #getClosestFreeField(Set, FieldMap, Point, RandomController, boolean)
     */
    public static Optional<Point> getClosestFreeField(final GameFieldController controller, final Point seed) {
        return getClosestFreeField(controller.getAllCharacters(), controller.getMap(), seed,
                controller.getRandomController(),
                controller.getConfiguration().getServerConfig().closestFreeFieldFadesThroughWall());
    }

    /**
     * Will find the closest free Field to a given position. The method will first
     * check all free neighbours - if there are none it will check the neighbours of
     * them and so on - this allows a character to fade through walls, if set.
     * Please note, that it is up to you to check if the target field contains a
     * gadget and to collect it.
     * <p>
     * This method will only pick fields with no characters on them. As this gets
     * integrated with the {@link RandomController} the call will be guarded as
     * point request
     *
     * @param allCharacters    The characters to take care of
     * @param map              The field map to map the points to
     * @param seed             The position to start checking from -- if the
     *                         position itself is free, the method will return the
     *                         seed.
     * @param fade             Should fading through walls be allowed?
     * @param randomController the {@link RandomController} to use; may be
     *                         {@code null} (in Tests)
     *
     * @return The free position if found.
     */
    public static Optional<Point> getClosestFreeField(final Set<Character> allCharacters, final FieldMap map,
            final Point seed, final RandomController randomController, boolean fade) {
        if (randomController == null) {
            return getClosestFreeFieldRaw(allCharacters, map, seed, fade);
        } else {
            final Point closesFreePoint = randomController.requestPoint(RandomController.GLOBAL,
                    () -> getClosestFreeFieldRaw(allCharacters, map, seed, fade).orElseGet(null), seed,
                    fade ? RandomOperation.CLOSEST_FREE_FIELD_FADE : RandomOperation.CLOSEST_FREE_FIELD);
            return Optional.ofNullable(closesFreePoint);
        }
    }

    private static Optional<Point> getClosestFreeFieldRaw(final Set<Character> allCharacters, final FieldMap map,
            final Point seed, boolean fade) {
        // get character positions
        final Set<Point> blockedPoints = allCharacters.stream().map(Character::getCoordinates)
                .collect(Collectors.toCollection(HashSet::new));
        // add all field positions with gadget
        for (int y = 0; y < map.getField().length; y++) {
            for (int x = 0; x < map.getField()[y].length; x++) {
                if (map.getField()[y][x].getGadget() != null) {
                    blockedPoints.add(new Point(x, y));
                }
            }
        }
        if (isValidAsPoint(map, blockedPoints, seed)) {
            return Optional.of(seed);
        }
        // just a dirty way to check already visited
        Set<Point> visited = new HashSet<>(8);
        // collect for all Neighbours
        List<Point> neighbours = Arrays.asList(seed.getNeighbours());
        // check in random order!
        Collections.shuffle(neighbours);
        // Init Queue with a random start:
        Queue<Point> travel = new LinkedList<>(neighbours);
        while (!travel.isEmpty()) {
            final Point check = travel.remove();
            if (visited.contains(check) || !check.isOnField(map))
                continue; // already seen
            if (isValidAsPoint(map, blockedPoints, check)) {
                return Optional.of(check);
            }

            visited.add(check);

            // enqueue all neighbours in random order
            neighbours = new ArrayList<>(Arrays.asList(check.getNeighbours()));
            neighbours.removeAll(visited);
            if (!fade) {
                // remove all fields which are no valid points as they are not walkable.
                neighbours.removeIf(n -> !n.isOnField(map) || !map.getSpecificField(n).isWalkable());
            }
            Collections.shuffle(neighbours);
            travel.addAll(neighbours);
        }
        return Optional.empty();
    }

    private static boolean isValidAsPoint(final FieldMap map, final Set<Point> blockedPoints, final Point check) {
        return !blockedPoints.contains(check) && check.isOnField(map) && map.getSpecificField(check).isWalkable();
    }

}