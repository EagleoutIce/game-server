package de.uulm.team020.server.game.phases.main.islands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.game.phases.main.Faction;

/**
 * Helper utility to outsource the somewhat heavy and stupid spawning-code. This
 * code will use backtracking to find a spawning-order so that both players are
 * able to access every safe (from a theoretical perspective). It is to be used
 * as addon for the {@link IslandClassifier}.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/20/2020
 */
public class IslandSpawner {

    // Local magpie instance
    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");

    private static final String ISLE_TXT = "isleSpawn";

    private Island[] islands;
    private List<Point> safePositions;
    private Boolean[] playerSpawn;

    /**
     * Construct a new spawner
     * 
     * @param islands       The islands he should use
     * @param safePositions Positions of all the safes - every player <i>must</i>
     *                      get a glimpse at every one of them
     */
    public IslandSpawner(Island[] islands, List<Point> safePositions) {
        this.islands = islands;
        this.safePositions = safePositions;
    }

    /**
     * This method will use backtracking to find valid indices for every character
     * to get an island to spawn in. If no configuration is possible this will yield
     * an Exception.
     * 
     * @param playerSpawn This Array will contain a boolean for every character to
     *                    Spawn. True means the character is from PlayerOne, false
     *                    means it is under the control of player Two. If the Array
     *                    is smaller than four or larger than eight an exception is
     *                    thrown as this violates the rules.
     * 
     * @return List of indices matching the list of passed characters to spawn them
     *         correctly (so that the get all safes contract is fulfilled). Empty
     *         optional, if this goal was not to be met.
     */
    public Optional<int[]> findSpawningIsles(Boolean[] playerSpawn) {
        return findSpawningIsles(playerSpawn, false);
    }

    /**
     * This method will use backtracking to find valid indices for every character
     * to get an island to spawn in. If no configuration is possible this will yield
     * an Exception.
     * 
     * @param playerSpawn This Array will contain a boolean for every character to
     *                    Spawn. True means the character is from PlayerOne, false
     *                    means it is under the control of player Two. If the Array
     *                    is smaller than four or larger than eight an exception is
     *                    thrown as this violates the rules.
     * @param mayExceed   Pass true, only if you want to allow the
     *                    position-algorithm to spawn more than 8 or less than four
     *                    players
     * 
     * @return List of indices matching the list of passed characters to spawn them
     *         correctly (so that the get all safes contract is fulfilled). Empty
     *         optional, if this goal was not to be met.
     */
    public Optional<int[]> findSpawningIsles(Boolean[] playerSpawn, boolean mayExceed) {
        // Just a simple guard to prevent-misusage this guard will trigger if the
        // char-count is out of range
        if (playerSpawn.length < 4 || playerSpawn.length > 8) {
            throw new ThisShouldNotHappenException("Island-spawner expects 4-8 players, not: " + playerSpawn.length);
        }
        magpie.writeInfo("Spawning for: " + Arrays.toString(playerSpawn), ISLE_TXT);
        this.playerSpawn = playerSpawn; // assign spawn

        // Every body has to to get all safe-positions so we init the full array.
        // They will be copied on the lower levels so this will be fine
        Optional<int[]> got = findSpawnWithBacktrack(new int[playerSpawn.length], // int array to use on construction
                new ArrayList<Point>(safePositions), new ArrayList<Point>(safePositions), // safes to cover
                0); // Incrementing index counter

        magpie.writeInfo("Got: " + got, ISLE_TXT);

        return got;
    }

    /***
     * This method will set the initial position for all player-characters whilst
     * respecting others. Currently this will ignore the distance to other
     * characters.
     * 
     * @param islePositions    Position-Order to summon the player-characters
     * @param playerSpawnOrder The order to spawn the player-characters-in
     * @param playerOneFaction The members of the player-one-team
     * @param playerTwoFaction The members of the player-two-team
     * @param allCharacters    All-characters - this has to be passed to make sure
     *                         no character will be place on a field of another.
     */
    public void spawnPlayerCharacters(int[] islePositions, Boolean[] playerSpawnOrder, Faction playerOneFaction,
            Faction playerTwoFaction, Set<Character> allCharacters) {
        if (islePositions.length != playerSpawnOrder.length) {
            throw new SpawnException(
                    "isle-order mismatch for: " + islePositions.length + " and " + playerSpawnOrder.length);
        }

        Iterator<Character> p1It = playerOneFaction.iterator();
        Iterator<Character> p2It = playerTwoFaction.iterator();

        for (int i = 0; i < playerSpawnOrder.length; i++) {
            // find all non occupied points in this list
            final Set<Point> occupiedPoints = allCharacters.stream().map(Character::getCoordinates)
                    .collect(Collectors.toSet());
            final int isleGoal = islePositions[i];
            if (playerSpawnOrder[i].booleanValue()) { // is p1
                if (p1It.hasNext()) { // populate!
                    spawnCharacter(isleGoal, p1It.next(), occupiedPoints);
                } else {
                    throw new SpawnException("Requested another player one char in: " + i + " but there is none");
                }
            } else { // is p2
                if (p2It.hasNext()) { // populate!
                    spawnCharacter(isleGoal, p2It.next(), occupiedPoints);
                } else {
                    throw new SpawnException("Requested another player two char in: " + i + " but there is none");
                }
            }
        }
    }

    /**
     * This method will set the initial position for arbitrary-player-characters
     * whilst respecting others. Currently this will ignore the distance to other
     * characters.
     * 
     * @param spawnThem     All characters to spawn.
     * @param allCharacters All-characters - this has to be passed to make sure no
     *                      character will be place on a field of another.
     */
    public void spawnCharacters(Set<Character> spawnThem, Set<Character> allCharacters) {
        for (Character posChar : spawnThem) {
            final Set<Point> occupiedPoints = allCharacters.stream().map(Character::getCoordinates)
                    .collect(Collectors.toSet());
            List<Integer> possibles = new ArrayList<>(2);
            for (int i = 0; i < islands.length; i++) {
                final Island island = islands[i];
                if (island.getPoints().stream().anyMatch(p -> !occupiedPoints.contains(p))) {
                    possibles.add(i);
                }

            }
            if (possibles.isEmpty()) {
                throw new SpawnException("Unable to position the character: " + posChar.getName()
                        + " as there is no island-space left. Maybe toss to unbounded fields in the future?");
            }
            int viablePool = RandomHelper.rndPick(possibles).intValue();
            spawnCharacter(viablePool, posChar, occupiedPoints);

        }
    }

    /**
     * This method will set the initial position for arbitrary-player-characters
     * whilst respecting others. Currently this will ignore the distance to other
     * characters.
     * <p>
     * In contrast to {@link #spawnCharacters(Set, Set)} this will prefer the island
     * with the most players on it - if there are one. To be more precise: It will
     * use islands if they contain both players.
     * 
     * @param spawnThem     All characters to spawn.
     * @param allCharacters All-characters - this has to be passed to make sure no
     *                      character will be place on a field of another.
     */
    public void spawnCharactersFavourPlayers(Set<Character> spawnThem, Set<Character> allCharacters) {
        for (Character posChar : spawnThem) {
            final Set<Point> occupiedPoints = allCharacters.stream().map(Character::getCoordinates)
                    .collect(Collectors.toSet());
            int viablePool = getCharacterSpawnIsland(occupiedPoints, posChar.getName());
            spawnCharacter(viablePool, posChar, occupiedPoints);

        }
    }

    /**
     * Is used by {@link #spawnCharactersFavourPlayers(Set, Set)} to retrieve a
     * random island containing at least one of the occupied points - but also
     * offering at least one free field to spawn on
     * 
     * @param occupiedPoints     Points which are occupied and to be checked for
     * @param characterDebugName The name of the character that desires this spawn
     *                           for debugging - will be used in case of exceptions
     * 
     * @return The island-number picked.
     */
    public int getCharacterSpawnIsland(Set<Point> occupiedPoints, String characterDebugName) {

        List<Integer> possibles = new ArrayList<>(2);
        for (int i = 0; i < islands.length; i++) {
            final Island island = islands[i];
            // has at least one free field and one obtained by a character
            if (island.getPoints().stream().anyMatch(p -> !occupiedPoints.contains(p))
                    && island.getPoints().stream().anyMatch(occupiedPoints::contains)) {
                possibles.add(i);
            }

        }
        if (possibles.isEmpty()) {
            throw new SpawnException("Unable to position the character: " + characterDebugName
                    + " as there is no island-space left. Maybe toss to unbounded fields in the future?");
        }
        return RandomHelper.rndPick(possibles).intValue();
    }

    /**
     * Returns the island number with the maximum amount of players and at least one
     * free field
     * 
     * @param placedCharacters The characters to check for
     * @param allCharacters    The bulk of characters to validate, that the scenario
     *                         is free
     * 
     * @return Island with the maximum number of players on it, will be valid if
     *         there is at least on island with a free field
     */
    public int getMaxPlayerIsland(Set<Character> placedCharacters, Set<Character> allCharacters) {
        int maxI = -1;
        long maxCount = -1;
        Set<Point> occupiedPoints = allCharacters.stream().map(Character::getCoordinates).collect(Collectors.toSet());
        for (int i = 0; i < islands.length; i++) {
            final Island island = islands[i];
            long count = placedCharacters.stream().filter(c -> island.getPoints().contains(c.getCoordinates())).count();
            // check if there is at least one free point
            if (island.getPoints().stream().anyMatch(p -> !occupiedPoints.contains(p))
                    && (maxCount < 0 || count > maxCount) // direct assign or greater
            ) {
                maxI = i;
                maxCount = count;
            }
        }
        return maxI;
    }

    /**
     * This will spawn a character on any random tile of the given island
     * 
     * @param isleNum        The island desired, will throw a {@link SpawnException}
     *                       if not valid
     * @param character      The character to spawn
     * @param occupiedPoints All Points occupied by other characters.
     * 
     * @return The point the character was spawned on
     */
    public Point spawnCharacter(int isleNum, Character character, Set<Point> occupiedPoints) {
        if (isleNum < 0 || isleNum >= this.islands.length) {
            throw new SpawnException("The provided isle-num: " + isleNum + " is out of Bounds");
        }

        final Island island = islands[isleNum];
        Point goal = RandomHelper.rndPick(
                island.getPoints().stream().filter(p -> !occupiedPoints.contains(p)).collect(Collectors.toList()));
        character.setCoordinates(goal);
        return goal;
    }

    /**
     * This will spawn a character on any tile of the given points
     * 
     * @param possibles      All possible points, it is up to you to feed them with
     *                       useful information
     * @param character      The character to spawn
     * @param occupiedPoints All Points occupied by other characters.
     * 
     * @return The point the character was spawned on
     */
    public static Point spawnCharacter(List<Point> possibles, Character character, Set<Point> occupiedPoints) {
        Point goal = provideSpawn(possibles, occupiedPoints);
        character.setCoordinates(goal);
        return goal;
    }

    /**
     * This will spawn the janitor on any tile of the given points
     * 
     * @param possibles        All possible points, it is up to you to feed them
     *                         with useful information
     * @param character        The character to spawn
     * @param occupiedPoints   All Points occupied by other characters.
     * @param randomController The random controller to use
     * 
     * @return The point the character was spawned on
     */
    public static Point spawnJanitor(List<Point> possibles, Character character, Set<Point> occupiedPoints,
            RandomController randomController) {
        Point goal = randomController.requestPoint(RandomController.GLOBAL,
                () -> provideSpawn(possibles, occupiedPoints), character.getCoordinates(),
                RandomOperation.JANITOR_SUMMON_TARGET);
        character.setCoordinates(goal);
        return goal;
    }

    /**
     * This will get a random spawn point, mostly for the janitor
     * 
     * @param possibles      All possible points, it is up to you to feed them with
     *                       useful information
     * @param occupiedPoints All Points occupied by other characters.
     * 
     * @return The point that can be spawned on
     */
    public static Point provideSpawn(List<Point> possibles, Set<Point> occupiedPoints) {
        return RandomHelper
                .rndPick(possibles.stream().filter(p -> !occupiedPoints.contains(p)).collect(Collectors.toList()));

    }

    /**
     * This method will be called for every recursion - it is the head of it and the
     * head of the backtracking. It just checks if the backtracking came to a leaf
     * and if so, validates that the configuration is correct.
     * 
     * @param isle                  Current integers which map to the Island to use
     *                              in this picking-round.
     * @param playerOneSafesToCover All safes that player one has left to cover.
     * @param playerTwoSafesToCover All safes that player two has left to cover.
     * @param idx                   Int idx the index of the current recursion. This
     *                              is the index used to access the current value of
     *                              isle and the {@link #playerSpawn}.
     * @return List of indices matching the list of passed characters to spawn them
     *         correctly (so that the get all safes contract is fulfilled). Empty
     *         optional, if this goal was not to be met.
     */
    private Optional<int[]> findSpawnWithBacktrack(int[] isle, List<Point> playerOneSafesToCover,
            List<Point> playerTwoSafesToCover, int idx) {
        // Check if the calculation can be stopped:
        if (idx < 0 || idx >= isle.length) {
            // If we are at a leave in calculation, have all safes been covered for BOTH?
            if (!playerOneSafesToCover.isEmpty() || !playerTwoSafesToCover.isEmpty()) {
                magpie.writeDebug("Spawn died on final level with... p1: " + playerOneSafesToCover + ", p2: "
                        + playerTwoSafesToCover + ". isles: " + Arrays.toString(isle), ISLE_TXT);
                return Optional.empty();
            }
            // Otherwise this mustn't be null
            return Optional.of(isle);
        }
        // dig into the next recursion...
        return findSpawnWithBackTrackHelper(isle, playerOneSafesToCover, playerTwoSafesToCover, idx);
    }

    /**
     * This is the real backtracking-part, also this one could be simplified and
     * merged with {@link #findSpawnSummonRandom(int[], List, List, int, List)} this
     * stays as legacy code and to provide easy changes on this logic-instance.
     * 
     * @param isle                  Current integers which map to the Island to use
     *                              in this picking-round.
     * @param playerOneSafesToCover All safes that player one has left to cover.
     * @param playerTwoSafesToCover All safes that player two has left to cover.
     * @param idx                   Int idx the index of the current recursion. This
     *                              is the index used to access the current value of
     *                              isle and the {@link #playerSpawn}.
     * 
     * @return List of indices matching the list of passed characters to spawn them
     *         correctly (so that the get all safes contract is fulfilled). Empty
     *         optional, if this goal was not to be met.
     */
    private Optional<int[]> findSpawnWithBackTrackHelper(int[] isle, List<Point> playerOneSafesToCover,
            List<Point> playerTwoSafesToCover, int idx) {
        // Is this a backtrack-step for player one?
        if (playerSpawn[idx].booleanValue()) { // Spawn player one
            // Does this player have safes left to cover?
            if (playerOneSafesToCover.isEmpty()) {
                magpie.writeInfo("As all covered, using random isle to server player one on: " + idx, "findBT");
                // All covered? So we just select a random, but jet possible, island. Possible
                // means in this case, that the island has still free-fields which are not
                // occupied by a character.
                List<Integer> possibles = getFreeIslands(isle, idx);
                return findSpawnSummonRandom(isle, playerOneSafesToCover, playerTwoSafesToCover, idx, possibles);
            }
            // Select a random, but still valid tile - engage backtracking
            return findSpawnSummonChoice(isle, playerOneSafesToCover, playerTwoSafesToCover, idx, true);
        } else { // Spawn player two
            // Does this player have safes left to cover?
            if (playerTwoSafesToCover.isEmpty()) {
                magpie.writeInfo("As all covered, using random isle to server player two on: " + idx, "findBT");
                // All covered? So we just select a random, but jet possible, island. Possible
                // means in this case, that the island has still free-fields which are not
                // occupied by a character.
                List<Integer> possibles = getFreeIslands(isle, idx);
                return findSpawnSummonRandom(isle, playerOneSafesToCover, playerTwoSafesToCover, idx, possibles);
            }
            // Select a random, but still valid tile - engage backtracking
            return findSpawnSummonChoice(isle, playerOneSafesToCover, playerTwoSafesToCover, idx, false);
        }
    }

    /**
     * Select a random jet possible island.
     * 
     * @param isle                  Current integers which map to the Island to use
     *                              in this picking-round.
     * @param playerOneSafesToCover All safes that player one has left to cover.
     * @param playerTwoSafesToCover All safes that player two has left to cover.
     * @param idx                   Int idx the index of the current recursion. This
     *                              is the index used to access the current value of
     *                              isle and the {@link #playerSpawn}.
     * @param possibles             All possible islands
     * @return Result of the recursive call
     *         {@link #findSpawnWithBacktrack(int[], List, List, int)}
     */
    private Optional<int[]> findSpawnSummonRandom(int[] isle, List<Point> playerOneSafesToCover,
            List<Point> playerTwoSafesToCover, int idx, List<Integer> possibles) {
        int[] copyI = Arrays.copyOf(isle, isle.length);

        copyI[idx] = RandomHelper.rndPick(possibles).intValue(); // Set the random island to use
        // Dig deeper
        return findSpawnWithBacktrack(copyI, playerOneSafesToCover, playerTwoSafesToCover, idx + 1);
    }

    /**
     * Backtrack-Step branching for every possible island configuration, propagating
     * the one working. The branch-order is <i>not</i> optimized for speed, as if it
     * would be possible, there are just ~4 branches to consider at max and if it
     * doesn't work the optimization would have no benefit and would be even worse
     * as adding more complexity.
     * 
     * @param isle                  Current integers which map to the Island to use
     *                              in this picking-round.
     * @param playerOneSafesToCover All safes that player one has left to cover.
     * @param playerTwoSafesToCover All safes that player two has left to cover.
     * @param idx                   Int idx the index of the current recursion. This
     *                              is the index used to access the current value of
     *                              isle and the {@link #playerSpawn}.
     * @param p1                    Is it to be computed for player one, or player
     *                              Two?
     * @return List of indices matching the list of passed characters to spawn them
     *         correctly (so that the get all safes contract is fulfilled). Empty
     *         optional, if this goal was not to be met.
     */
    private Optional<int[]> findSpawnSummonChoice(int[] isle, List<Point> playerOneSafesToCover,
            List<Point> playerTwoSafesToCover, int idx, boolean p1) {
        List<Point> playerUse = p1 ? playerOneSafesToCover : playerTwoSafesToCover;
        List<Integer> possibles = getPossibleIslands(playerUse, isle, idx, true);
        // get all islands matching at least one of the missing numbers
        for (Integer integer : possibles) {
            final int i = integer.intValue();
            final Island island = islands[i];
            int[] copyI = Arrays.copyOf(isle, isle.length);
            copyI[idx] = i; // Set i-th island to use

            // copy all safes they to not have in common:
            List<Point> playerCopy = new LinkedList<>();
            playerUse.stream().filter(p -> !island.getSafePositions().contains(p)).forEach(playerCopy::add);
            // add all points that are not covered by this isle
            // even if its empty we will pass to slaves as long as not finished

            // dig one level deeper:
            Optional<int[]> gotDeeper = findSpawnWithBacktrack(copyI, p1 ? playerCopy : playerOneSafesToCover,
                    p1 ? playerTwoSafesToCover : playerCopy, idx + 1);
            if (gotDeeper.isPresent())
                return gotDeeper; // found a result
        }
        magpie.writeDebug("Branch died on idx: " + idx + " with playerSpawn: " + Arrays.toString(playerSpawn)
                + ", isles: " + Arrays.toString(isle) + ", coverage p1: " + playerOneSafesToCover + " coverage p2: "
                + playerTwoSafesToCover + ".", "Backtrack");
        return Optional.empty(); // nothing to do here
    }

    /**
     * This method just helps picking all island-indices which are helping and valid
     * for the safe-target. This method does always check if the island is full - in
     * that case it will never be added. To compute the uses it will use
     * {@link #countIsleUses(int[], int, int)}.
     * 
     * @param playerSafes List of safes that the player needs
     * @param isle        Current integers which map to the Island to use in this
     *                    picking-round.
     * @param idx         Int idx the index of the current recursion. This is the
     *                    index used to access the current value of isle and the
     *                    {@link #playerSpawn}.
     * @param checkSafes  Flag indicating if this should check for matching islands
     *                    based on the safe-pool. If not this will return all
     *                    islands which have still room for at least another
     *                    character. This variant is used when summoning characters
     *                    on random positions as it will return all island which
     *                    have still space. In this case, the {@code playerSafes}
     *                    will be ignored. So use
     *                    {@link #getFreeIslands(int[], int)} instead if you like.
     * 
     * @return Possibly empty list of all islands matching the given constraints
     */
    public List<Integer> getPossibleIslands(List<Point> playerSafes, int[] isle, int idx, boolean checkSafes) {
        List<Integer> possibles = new ArrayList<>(2);
        for (int i = 0; i < islands.length; i++) {
            final Island island = islands[i];
            if (checkSafes && Collections.disjoint(island.getSafePositions(), playerSafes))
                continue;
            // check: has fields left:
            int used = countIsleUses(isle, idx, i);
            if (used < island.getPoints().size())
                possibles.add(i); // it is possible!
        }
        return possibles;
    }

    /**
     * This method just helps picking all island-indices still having space using
     * the isle-configuration. To compute the uses it will use
     * {@link #countIsleUses(int[], int, int)}.
     * 
     * @param isle Current integers which map to the Island to use in this
     *             picking-round.
     * @param idx  Int idx the index of the current recursion. This is the index
     *             used to access the current value of isle and the
     *             {@link #playerSpawn}.
     * 
     * @return Possibly empty list of all islands matching the given constraints
     */
    public List<Integer> getFreeIslands(int[] isle, int idx) {
        return getPossibleIslands(Collections.emptyList(), isle, idx, false);
    }

    /**
     * Just a simple helper method which calculates the characters currently
     * occupying an island.
     * 
     * @param isles   The isles-array to check which characters occupy which isle.
     * @param maxIdx  The maximum Index the isles-array was populated. This avoids
     *                errors if the '0' island has only limited fields but would be
     *                the last one to choose.
     * @param isleNum The isle that should be counted for
     * 
     * @return The amount of player occupying this island.
     */
    private int countIsleUses(int[] isles, int maxIdx, int isleNum) {
        int counter = 0;
        // may use stream?
        for (int i = 0; i < isles.length && i < maxIdx; i++) {
            if (isles[i] == isleNum)
                counter++;
        }
        return counter;
    }

}