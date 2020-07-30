package de.uulm.team020.server.addons.semantics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.pathfinding.Pathfinder;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.ServerConfiguration;

/**
 * This class uses a set of desired rules to validate if a map is considered to
 * be playable.
 * <p>
 * This Semantics-class can use Settings from the Server-Configuration
 * (prefixed:C) but is not allowed to use further information. Currently it
 * validates:
 * <ul>
 * <li>All Safes are to be reached by an valid starting-point, there can be
 * multiple starting points in a map. A starting Point/Isle is every free tile
 * that can reach a safe. Any Isle that does not suffices this requirement is
 * considered 'unreachable'.</li>
 * <li>There are at least (8 + C::NPC-Count + 3)*# 'FREE'-Fields, whereas '#'
 * has to be &gt;= 2. There will be a warning if '#' is below 4.</li>
 * <li>Minimum (Tiles/4) free Tiles have at least C::Minimum-Tiles connected
 * free tiles, so that the level cannot block characters.</li>
 * </ul>
 * 
 * The whole semantic-checking can be disabled.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/17/2020
 */
public class ScenarioSemantics {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    private final ServerConfiguration configuration;

    // Will map safe-point => num of free neighbours
    private Map<Point, Integer> safeCoordinates;
    // Other registers
    private int freeFields;
    private int freeSplits;
    private int totalFields;

    /**
     * Construct a new scenario semantics checker
     * 
     * @param configuration The configuration to use
     */
    public ScenarioSemantics(ServerConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Performs the check-up
     * 
     * @param scenario The scenario to check
     * @return The report, stating if the scenario did hold up to the expectations
     */
    public SemanticsReport check(Scenario scenario) {
        if (!configuration.performSemantics())
            return new SemanticsReport("", ISemanticsType.SEMANTICS_DISABLED);
        populateDataFields(scenario);
        List<String> problems = new ArrayList<>(2);
        checkTotalFreeFields(problems);

        checkEnoughFreeSplits(problems);

        checkSafesSufficeConstraints(scenario, problems);

        // Maybe implemented critical
        return problems.isEmpty() ? new SemanticsReport()
                : new SemanticsReport(problems, ISemanticsType.DOES_NOT_SUFFICE);
    }

    private void checkSafesSufficeConstraints(Scenario scenario, List<String> problems) {
        final Set<Entry<Point, Integer>> entries = safeCoordinates.entrySet();

        for (Entry<Point, Integer> entry : entries) {
            if (entry.getValue() < configuration.minimumSafeNeighbours())
                problems.add("The safe at: " + entry.getKey() + " has: " + entry.getValue()
                        + " free fields as neighbours, which is below the desired minimum of: "
                        + configuration.minimumSafeNeighbours());
        }

        if (configuration.allSafesConnected()) {
            problems.addAll(validateSafes(scenario));
        }
    }

    private void checkEnoughFreeSplits(List<String> problems) {
        final int freeSplitWanted = totalFields / 4;
        if (freeSplits < freeSplitWanted)
            problems.add("The number of free splits (" + freeSplits + ") is lower than the wanted minimum: "
                    + freeSplitWanted + ". A Split is a free tile minimum of " + configuration.minimumSplitsForTile()
                    + " free neighbour tiles. ");
    }

    private void checkTotalFreeFields(List<String> problems) {
        final int totalFreeFieldMinimum = (8 + configuration.numberOfNpc() + 3) * 2;
        final int totalFreeFieldWarning = (8 + configuration.numberOfNpc() + 3) * 4;
        if (freeFields < totalFreeFieldMinimum) {
            problems.add("The number of free fields (" + freeFields + ") is lower than the wanted minimum of: "
                    + totalFreeFieldMinimum);
        } else if (freeFields < totalFreeFieldWarning) {
            magpie.writeWarning("The number of free fields (" + freeFields + ") is lower than the warning-margin: "
                    + totalFreeFieldWarning, "Check");
        }
    }

    private List<String> validateSafes(Scenario scenario) {
        List<String> problems = new ArrayList<>(2);
        Pathfinder<FieldStateEnum> pathfinder = new Pathfinder<>(scenario, true);
        final Point[] safePoints = safeCoordinates.keySet().toArray(Point[]::new);
        for (int i = 0; i < safePoints.length; i++) {
            final Point test = safePoints[i];
            for (int j = i + 1; j < safePoints.length; j++) {
                if (!pathfinder.connected(test, safePoints[j], false)) {
                    problems.add("The safes at " + test + " and " + safePoints[j]
                            + " are not connected. But they are required to.");
                }
            }
        }
        return problems;
    }

    // First check, will populate safe-slots as well
    protected void populateDataFields(Scenario scenario) {
        FieldStateEnum[][] field = scenario.getScenario();
        safeCoordinates = new HashMap<>();
        freeFields = freeSplits = totalFields = 0;

        for (int y = 0; y < field.length; y++) {
            for (int x = 0; x < field[y].length; x++, totalFields++) {
                Point p = new Point(x, y);
                if (field[y][x] == FieldStateEnum.FREE) {
                    freeFields += 1;
                    if (countNeighbours(field, p) >= configuration.minimumSplitsForTile()) {
                        freeSplits += 1;
                    }
                } else if (field[y][x] == FieldStateEnum.SAFE) {
                    safeCoordinates.putIfAbsent(p, 0);
                }
            }
        }
    }

    private static final Point[] NEIGHBOURS = new Point[] { new Point(-1, -1), new Point(0, -1), new Point(1, -1),
            new Point(-1, 0), new Point(1, 0), new Point(-1, 1), new Point(0, 1), new Point(1, 1) };

    // Calculation is quite costly this way, but to be honest,
    // it should be more correct, than effective
    private int countNeighbours(FieldStateEnum[][] field, Point point) {
        // check for every possible neighbor, if it is on the field
        // and if so, increment the counter
        int total = 0;

        for (int i = 0; i < NEIGHBOURS.length; i++) {
            Point check = new Point(point, NEIGHBOURS[i]);
            // Is it a valid, walkable field?
            if (check.isOnField(field)) {
                final FieldStateEnum f = field[check.getY()][check.getX()];
                if (f.isWalkable()) {
                    total++;
                } else if (f == FieldStateEnum.SAFE) {
                    if (safeCoordinates.containsKey(check)) {
                        safeCoordinates.put(check, safeCoordinates.get(check) + 1); // urgh - horrible :D
                    } else {
                        safeCoordinates.put(check, 1);
                    }
                }
            }
        }

        return total;
    }

}