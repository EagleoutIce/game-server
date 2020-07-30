package de.uulm.team020.server.core.phases;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.server.game.phases.main.helper.CharacterActionGuard;

public class CharacterActionGuardTest {

    private static Set<Arguments> generateValidForPoint(Point p) {
        Set<Arguments> args = new HashSet<>(); // avoids duplicates :D

        for (Point pn : p.getNeighbours()) {
            args.add(Arguments.arguments(p, pn, true));
            for (Point pnn : pn.getNeighbours()) {
                args.add(Arguments.arguments(p, pnn, true));
            }
        }

        return args;
    }

    public static Stream<Arguments> generate_neighbourOfNeighbour() {
        Set<Arguments> args = new HashSet<>(); // avoids duplicates :D
        args.addAll(generateValidForPoint(new Point(0, 0)));
        args.addAll(generateValidForPoint(new Point(42, 13)));
        args.addAll(generateValidForPoint(new Point(-6, -15)));
        args.addAll(Set.of(Arguments.arguments(new Point(0, 0), new Point(42, 13), false),
                Arguments.arguments(new Point(3, 6), new Point(6, 6), false),
                Arguments.arguments(new Point(-2, 0), new Point(2, 4), false),
                Arguments.arguments(new Point(3, 1), new Point(3, 4), false),
                Arguments.arguments(new Point(-6, -7), new Point(-3, -6), false)));
        return args.stream();
    }

    @ParameterizedTest
    @Tag("Util")
    @Order(1)
    @DisplayName("[Guard] Neighbour of neighbour")
    @MethodSource("generate_neighbourOfNeighbour")
    public void test_neighbourOfNeighbour(Point a, Point b, boolean isNeighbour) {
        Assertions.assertEquals(isNeighbour, CharacterActionGuard.isNeighbourOfNeighbour(a, b),
                "Shall be as stated for, a: " + a + " and b: " + b);
    }

}