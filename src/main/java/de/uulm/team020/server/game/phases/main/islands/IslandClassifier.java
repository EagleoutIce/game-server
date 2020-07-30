package de.uulm.team020.server.game.phases.main.islands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.game.phases.main.Faction;

/**
 * This classifier performs BFS-operations launching from every safe to create
 * islands. Any island is identified by the set of safe-points it can reach and
 * consists of the set of all points identifying free fields, which are
 * reachable by those safes. As it would be not useful to provide the extra
 * calculation overhead of identifying connections via A* (using the underlying
 * pathfinder) as this could lead orphans undetected, this Implementation will
 * rely on the BFS routine.
 * <p>
 * This classifier will work, even if a safe is part of multiple islands,
 * whereas they are not connected.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/19/2020
 */
public class IslandClassifier {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");

    private Field[][] field;
    List<Point> validSpawns;

    /**
     * Construct a new Island-Classifier
     * 
     * @param map The map to use
     */
    public IslandClassifier(FieldMap map) {
        this.field = map.getField();
        this.validSpawns = new ArrayList<>(map.getMaxWidth() * map.getMaxHeight() / 2);
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                if (field[y][x].isWalkable()) {
                    validSpawns.add(new Point(x, y));
                }
            }
        }
    }

    public List<Point> getValidSpawns() {
        return this.validSpawns;
    }

    /**
     * Sorry for the naming of this method, it just sounds nice :D. So, what does
     * this? This method classifies all islands to be found using BFS in a
     * <i>synchronized</i> way.
     * <p>
     * <b>Important:</b> If there is no safe in this map, this discovery will return
     * an empty list!
     * 
     * @return All islands to be found
     */
    public List<Island> discoverWorld() {
        // At the start all free points are valid Starts
        Point[] copyValidSpawns = this.validSpawns.toArray(Point[]::new);

        List<Island> islands = new ArrayList<>(2); // we assume one, but well...
        for (Point point : copyValidSpawns) {
            if (islands.stream().anyMatch(i -> i.getPoints().contains(point))) {
                continue; // skip included
            }
            islands.add(discover(point));
        }
        // join all islands having the same safes as targets
        for (Island island : islands) {
            // every island having the same safe points whilst being different
            islands.stream().filter(i -> sameIslandOrSameSafeSafeKeys(i, island))
                    .forEach(i -> mergeIslandsInFirstAndClearTheOther(island, i));
        }
        // filter out all with no safe-targets
        islands.removeIf(i -> i.getSafePositions().isEmpty());
        magpie.writeInfo("Discovered islands: " + islands, "Classifier");
        return islands;
    }

    private void mergeIslandsInFirstAndClearTheOther(Island island, Island i) {
        island.getPoints().addAll(i.getPoints());
        i.getSafePositions().clear();
    }

    private boolean sameIslandOrSameSafeSafeKeys(Island i, Island island) {
        return !i.getSafePositions().isEmpty() && !i.equals(island)
                && i.getSafePositions().equals(island.getSafePositions());
    }

    /**
     * Performs the BFS-discovery from the given field. The implementation doesn't
     * mean to be memory efficient, nor does it want to impress anyone by using
     * light-speed, the only goal of it is to be convenient/easy to use whilst
     * fulfilling its duty... so fingers crossed
     * 
     * @param startingPoint The (free)-field to start from.
     * @return The Island starting with this field, including all safes got
     */
    private Island discover(Point startingPoint) {
        // this will allow us to store the one we've already visited too
        Island result = new Island();
        result.addPoint(startingPoint);

        // The queue of points to visit
        Queue<Point> queue = new LinkedList<>();
        queue.add(startingPoint);

        while (!queue.isEmpty()) {
            // traverse!
            final Point current = queue.remove();
            Point[] neighbours = current.getNeighbours();
            // Is it a valid candidate?
            for (int i = 0; i < neighbours.length; i++) {
                final Point pn = neighbours[i];
                if (!pn.isOnField(field)) // if out of bounds, ignore!
                    continue;
                addFieldToWalkablesOrSafesDependingOnState(result, queue, pn);
            }
        }
        return result;
    }

    private void addFieldToWalkablesOrSafesDependingOnState(Island result, Queue<Point> queue, final Point pn) {
        final FieldStateEnum state = field[pn.getY()][pn.getX()].getState();
        if (state.isWalkable() && result.getPoints().add(pn)) {
            // will return true only if wasn't in there already
            queue.add(pn);
        } else if (state == FieldStateEnum.SAFE) {
            result.getSafePositions().add(pn); // Try to add, duplicate prevented by set?
        }
    }

    private static final String FORMAT_RESET = "\033[0m";
    private static final String FORMAT_BLOCKED = "\033[30m";
    private static final String FORMAT_SAFE = FORMAT_RESET;
    private static final String FORMAT_ROULETTE = "\033[38;5;21m";
    private static final String FORMAT_UNBOUNDED = "\033[38;5;251m";
    private static final String[] FORMAT_ISLANDS = new String[] { "\033[38;5;106m", "\033[38;5;108m", "\033[38;5;125m",
            "\033[38;5;202m", "\033[38;5;209m", "\033[38;5;223m", "\033[38;5;39m", "\033[38;5;65m", "\033[38;5;196m", };
    private static final String SYMBOL = "██";

    /**
     * Just some presentation-dump method to visualize the generated Islands. As it
     * wants to pick nice colors it only works fine for the length of
     * {@link #FORMAT_ISLANDS}, after that it wills start to repeat the colors. This
     * method is FAR away from efficient as it is not used at runtime and just there
     * to provide visualizations.
     * 
     * @param islesToPrint The islands that should be considered while rendering the
     *                     underlying map.
     * 
     * @return A string with newlines and Formatting information that hopefully
     *         looks nice when printed with an ANSI-compatible Terminal.
     * 
     * @see #prettyPrint(Island[], Set, Faction, Faction)
     */
    public String prettyPrint(Island[] islesToPrint) {
        return prettyPrint(islesToPrint, Collections.emptySet(), null, null);
    }

    /**
     * Just some presentation-dump method to visualize the generated Islands. As it
     * wants to pick nice colors it only works fine for the length of
     * {@link #FORMAT_ISLANDS}, after that it wills start to repeat the colors. This
     * method is FAR away from efficient as it is not used at runtime and just there
     * to provide visualizations. This method will, in contrast to
     * {@link #prettyPrint(Island[])} print character-placement's as well!
     * 
     * @param islesToPrint The islands that should be considered while rendering the
     *                     underlying map.
     * @param characters   Characters to print if they are on the field
     * @param p1           Information of the player for p1 to set them correctly
     * @param p2           Information of the player for p2 to set them correctly
     * 
     * @return A string with newlines and Formatting information that hopefully
     *         looks nice when printed with an ANSI-compatible Terminal.
     */
    public String prettyPrint(Island[] islesToPrint, Set<Character> characters, Faction p1, Faction p2) {
        StringBuilder builder = new StringBuilder(FORMAT_RESET);
        // We will perform array-based operations anyway, so lets safe the access:
        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++) {
                builder.append(prettyPrint(x, y, field, islesToPrint, characters, p1, p2));
            }
            builder.append("\n").append(FORMAT_RESET);
        }
        return builder.toString();
    }

    /**
     * Just prints the legend for the {@link #prettyPrint(Island[])}
     * 
     * @return Formatted String
     */
    public static String prettyLegend() {
        StringBuilder builder = new StringBuilder(FORMAT_RESET);
        builder.append("Safe: ").append(FORMAT_SAFE).append("##").append(FORMAT_RESET).append(", ");
        builder.append("Unbounded: ").append(FORMAT_UNBOUNDED).append(SYMBOL).append(FORMAT_RESET).append(", ");
        builder.append("Blocked: ").append(FORMAT_BLOCKED).append(SYMBOL).append("\n").append(FORMAT_RESET);
        for (int i = 0; i < FORMAT_ISLANDS.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(i).append(": ").append(FORMAT_ISLANDS[i]).append(SYMBOL).append(FORMAT_RESET);
        }
        return builder.append(".\n").toString();
    }

    private StringBuilder prettyPrint(int x, int y, Field[][] field, Island[] islesToPrint, Set<Character> characters,
            Faction p1, Faction p2) {
        StringBuilder builder = new StringBuilder();
        final FieldStateEnum state = field[y][x].getState();
        if (state.isWalkable()) {
            appendWalkableByIslandFormat(x, y, islesToPrint, characters, p1, p2, builder);
        } else if (state == FieldStateEnum.SAFE) {
            appendSafeFormat(x, y, field, builder);
        } else if (state == FieldStateEnum.ROULETTE_TABLE) {
            appendRouletteFormat(x, y, field, builder);
        } else {
            appendBlockedFormat(x, y, characters, p1, p2, builder);
        }
        return builder;
    }

    private void appendBlockedFormat(int x, int y, Set<Character> characters, Faction p1, Faction p2,
            StringBuilder builder) {
        builder.append(FORMAT_BLOCKED).append(hasPlayerOnField(x, y, characters, p1, p2));
    }

    private void appendRouletteFormat(int x, int y, Field[][] field, StringBuilder builder) {
        builder.append(FORMAT_ROULETTE);
        if (field[y][x].getChipAmount() < 10) {
            builder.append(">");// padding
        }
        builder.append(field[y][x].getChipAmount()).append(FORMAT_RESET);
    }

    private void appendSafeFormat(int x, int y, Field[][] field, StringBuilder builder) {
        builder.append(FORMAT_SAFE);
        if (field[y][x].getSafeIndex() < 10) {
            builder.append(" ");// padding
        }
        builder.append(field[y][x].getSafeIndex()).append(FORMAT_RESET);
    }

    private void appendWalkableByIslandFormat(int x, int y, Island[] islesToPrint, Set<Character> characters,
            Faction p1, Faction p2, StringBuilder builder) {
        int idx = prettyFind(x, y, islesToPrint);
        if (idx >= 0) {
            builder.append(FORMAT_ISLANDS[idx]);
        } else {
            builder.append(FORMAT_UNBOUNDED);
        }
        builder.append(hasPlayerOnField(x, y, characters, p1, p2));
    }

    /**
     * Searches for a given index in the islesToPrint-Stack serving 'pretty find'
     * 
     * @param x            X-coordinate of the point
     * @param y            Y-coordinate of the point
     * @param islesToPrint Array to search
     * @return The index of the Island that contains the point, -1 if none.
     */
    private int prettyFind(int x, int y, Island[] islesToPrint) {
        final Point target = new Point(x, y);
        for (int i = 0; i < islesToPrint.length; i++) {
            if (islesToPrint[i].getPoints().contains(target)) {
                return i;
            }
        }
        return -1;
    }

    private String hasPlayerOnField(int x, int y, Set<Character> characters, Faction p1, Faction p2) {
        final Point point = new Point(x, y);
        for (Character character : characters) {
            if (character.getCoordinates().equals(point)) {
                if (p1 != null && p1.contains(character))
                    return "▓1";
                else if (p2 != null && p2.contains(character))
                    return "▓2";
                else if (character.getName().equals("<cat>")) // just for the visuals
                    return "▓C";
                else if (character.getName().equals("<janitor>")) // just for the visuals
                    return "▓J";
                else {
                    return "▓N";
                }
            }
        }
        return SYMBOL;
    }
}