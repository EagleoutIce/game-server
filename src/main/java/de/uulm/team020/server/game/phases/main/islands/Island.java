package de.uulm.team020.server.game.phases.main.islands;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import de.uulm.team020.datatypes.util.Point;

/**
 * Represents an Island as built by the {@link IslandClassifier}
 * 
 * @author Florian Sihler
 * @version 1.0, 04/19/2020
 */
public class Island {

    private Set<Point> safePositions;
    private Set<Point> points;

    /**
     * Construct a new Island - your island, your rules!
     */
    public Island() {
        this.safePositions = new HashSet<>();
        this.points = new HashSet<>();
    }

    /**
     * Convenient (deep-)copy of an island
     * 
     * @param old The island to copy
     */
    public Island(final Island old) {
        this();
        old.safePositions.stream().map(Point::new).forEach(safePositions::add);
        old.points.stream().map(Point::new).forEach(points::add);
    }

    /**
     * @return the safePositions
     */
    public Set<Point> getSafePositions() {
        return safePositions;
    }

    /**
     * @param safePosition the safePosition to add
     */
    public void addSafePosition(Point safePosition) {
        this.safePositions.add(safePosition);
    }

    /**
     * @param safePositions the safePositions to add
     */
    public void addSafePosition(Collection<Point> safePositions) {
        this.safePositions.addAll(safePositions);
    }

    /**
     * @return the points
     */
    public Set<Point> getPoints() {
        return points;
    }

    /**
     * @param point the point to add
     */
    public void addPoint(Point point) {
        this.points.add(point);
    }

    /**
     * @param points the points to add
     */
    public void addPoints(Collection<Point> points) {
        this.points.addAll(points);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Island [").append("safePositions=").append(safePositions).append(", numOfPoints=")
                .append(points.size()).append("]");
        return builder.toString();
    }
}