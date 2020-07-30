package de.uulm.team020.server.core.datatypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.PhaseComparator;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.exceptions.FilterException;

/**
 * Construct a Filter based on joining constraints
 *
 * @author Florian Sihler
 * @version 1.1b, 06/21/2020
 */
public class FilterBuilder {

    private static final String ALREADY_GAME_PHASES = "You already have restricted GamePhases";
    private static final String SET_GAME_PHASES = "This message will only be accepted if the GamePhase (is: ";
    private static final String TRUE_STRING = "";
    // negation
    private boolean negate = false;

    // Phase Bounds
    private boolean gotGamePhaseBounds = false;
    private GamePhaseEnum startPhase = null;
    private GamePhaseEnum endPhase = null;
    private Set<GamePhaseEnum> phaseShouldBeIn = null;

    // role
    private boolean gotRoleBounds = false;
    private Set<RoleEnum> roleShouldBe;

    // strike count
    private boolean gotStrikeBounds;
    private int lowerStrikeBound;
    private int upperStrikeBound;
    private Set<Integer> strikeShouldBe = null;

    // connection
    private boolean gotConnectionSpecification = false;
    private boolean shouldBeConnected = false;

    // constants
    private static final Set<GamePhaseEnum> PAUSES = Set.of(GamePhaseEnum.GAME_PAUSED, GamePhaseEnum.GAME_FORCE_PAUSED);

    private void filterFalseAssert(final boolean check, final String message) {
        if (check) {
            throw new FilterException(message);
        }
    }

    /**
     * Construct a new FilterBuilder. Please note, that calling {@link #build()}
     * will return this filter and will <b>not</b> return a copy.
     */
    public FilterBuilder() {
        lowerStrikeBound = 0;
        upperStrikeBound = 0;
    }

    /**
     * Convenient copy constructor, may be used to create one with separate rules
     * 
     * @param old The old filter builder to copy
     */
    public FilterBuilder(final FilterBuilder old) {
        this.negate = old.negate;
        this.gotGamePhaseBounds = old.gotGamePhaseBounds;
        this.startPhase = old.startPhase;
        this.endPhase = old.endPhase;
        this.phaseShouldBeIn = old.phaseShouldBeIn == null ? null : new HashSet<>(old.phaseShouldBeIn);
        this.gotRoleBounds = old.gotRoleBounds;
        this.roleShouldBe = old.roleShouldBe == null ? null : new HashSet<>(old.roleShouldBe);
        this.gotStrikeBounds = old.gotStrikeBounds;
        this.lowerStrikeBound = old.lowerStrikeBound;
        this.upperStrikeBound = old.upperStrikeBound;
        this.strikeShouldBe = old.strikeShouldBe == null ? null : new HashSet<>(old.strikeShouldBe);
        this.gotConnectionSpecification = old.gotConnectionSpecification;
        this.shouldBeConnected = old.shouldBeConnected;
    }

    /**
     * Assert, that the call will occur between the two game phases
     *
     * @param inclusiveStart the inclusive GamePhase-start
     * @param inclusiveEnd   the inclusive GamePhase-end
     * @return this builder for chaining
     */
    public FilterBuilder betweenGamePhases(final GamePhaseEnum inclusiveStart, final GamePhaseEnum inclusiveEnd) {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.startPhase = inclusiveStart;
        this.endPhase = inclusiveEnd;
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur for this gamePhases
     *
     * @param phases the phases the call should appear in
     * @return this builder for chaining
     */
    public FilterBuilder shouldBeInGamePhases(final GamePhaseEnum... phases) {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.phaseShouldBeIn = new HashSet<>(Arrays.asList(phases));
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur only in this gamePhase
     *
     * @param phase the phase the call should appear in
     * @return this builder for chaining
     */
    public FilterBuilder shouldBeInGamePhase(final GamePhaseEnum phase) {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.phaseShouldBeIn = new HashSet<>(Arrays.asList(phase));
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur not in this gamePhase
     *
     * @param phase the phase the call should appear in
     * @return this builder for chaining
     */
    public FilterBuilder shouldNotBeInGamePhase(final GamePhaseEnum phase) {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.phaseShouldBeIn = Arrays.stream(GamePhaseEnum.values()).filter(e -> !e.equals(phase))
                .collect(Collectors.toCollection(HashSet::new));
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur in a enforced or not enforced pause
     *
     * @return this builder for chaining
     */
    public FilterBuilder inPause() {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.phaseShouldBeIn = new HashSet<>(PAUSES);
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur in a enforced or not enforced pause. It
     * differs from {@link #inPause()} as it will not forbid being called multiple
     * times -- it will append the game phases to existing ones (if there are any)
     *
     * @return this builder for chaining
     */
    public FilterBuilder orInPause() {
        if (this.phaseShouldBeIn != null) {
            this.phaseShouldBeIn.addAll(PAUSES);
        } else {
            this.phaseShouldBeIn = new HashSet<>(PAUSES);
        }
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur in a enforced or not enforced pause
     *
     * @return this builder for chaining
     */
    public FilterBuilder notInPause() {
        filterFalseAssert(gotGamePhaseBounds, ALREADY_GAME_PHASES);
        this.phaseShouldBeIn = Arrays.stream(GamePhaseEnum.values()).filter(r -> !PAUSES.contains(r))
                .collect(Collectors.toCollection(HashSet::new));
        this.gotGamePhaseBounds = true;
        return this;
    }

    /**
     * Negates the filter expression, filter will exactly trigger when the pattern
     * does not match
     *
     * @return this builder for chaining
     */
    public FilterBuilder negate() {
        negate = true;
        return this;
    }

    /**
     * Negates the filter expression for matching phases. This will execute at time
     * of calling so if you call this without having any phases set this will do
     * nothing.
     *
     * @return this builder for chaining
     */
    public FilterBuilder negatePhases() {
        if (this.phaseShouldBeIn != null) {
            final Set<GamePhaseEnum> invertedPhases = Arrays.stream(GamePhaseEnum.values())
                    .filter(p -> !this.phaseShouldBeIn.contains(p)).collect(Collectors.toCollection(HashSet::new));
            this.phaseShouldBeIn = invertedPhases;
        }
        return this;
    }

    /**
     * Asserts, that the client has one of the given roles
     *
     * @param roles roles to be desired
     * @return this builder for chaining
     * 
     * @see #shouldNotBeRole(RoleEnum...)
     */
    public FilterBuilder shouldBeRole(final RoleEnum... roles) {
        filterFalseAssert(gotRoleBounds, "You already have restricted the roles");
        this.roleShouldBe = new HashSet<>(Arrays.asList(roles));
        gotRoleBounds = true;
        return this;
    }

    /**
     * Asserts, that the client has none of the given roles
     *
     * @param roles roles to be desired
     * @return this builder for chaining
     * 
     * @see #shouldBeRole(RoleEnum...)
     */
    public FilterBuilder shouldNotBeRole(final RoleEnum... roles) {
        filterFalseAssert(gotRoleBounds, "You already have restricted the roles");
        final ArrayList<RoleEnum> arRoles = new ArrayList<>(Arrays.asList(roles));
        this.roleShouldBe = Arrays.stream(RoleEnum.values()).filter(r -> !arRoles.contains(r))
                .collect(Collectors.toCollection(HashSet::new));
        gotRoleBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur between the two strikes
     *
     * @param inclusiveLowerBound the inclusive lower bound for the strikes
     * @param inclusiveUpperBound the inclusive upper bound for the strikes
     * @return this builder for chaining
     */
    public FilterBuilder betweenStrikeCounts(final int inclusiveLowerBound, final int inclusiveUpperBound) {
        filterFalseAssert(gotStrikeBounds, "You already have restricted the strikes");
        this.lowerStrikeBound = inclusiveLowerBound;
        this.upperStrikeBound = inclusiveUpperBound;
        this.gotStrikeBounds = true;
        return this;
    }

    /**
     * Assert, that the call will occur if the current strike count is contained in
     * the array
     *
     * @param strikes the strike counts desired
     * @return this builder for chaining
     */
    public FilterBuilder shouldBeStrikes(final Integer[] strikes) {
        filterFalseAssert(gotStrikeBounds, "You already have restricted the strikes");
        this.strikeShouldBe = new HashSet<>(Arrays.asList(strikes));
        this.gotStrikeBounds = true;
        return this;
    }

    /**
     * Asserts, that the call will occur only if the player is connected
     *
     * @return this builder for chaining
     */
    public FilterBuilder hasToBeConnected() {
        filterFalseAssert(gotConnectionSpecification, "You already have restricted the connection");
        this.shouldBeConnected = true;
        this.gotConnectionSpecification = true;
        return this;
    }

    /**
     * Asserts, that the call will occur only if the player is connected
     *
     * @return this builder for chaining
     */
    public FilterBuilder hasToBeDisconnected() {
        filterFalseAssert(gotConnectionSpecification, "You already have restricted the connection");
        this.shouldBeConnected = false;
        this.gotConnectionSpecification = true;
        return this;
    }

    /**
     * End the building process and construct the filter.
     * <p>
     * Please note, that this will not create a copy. It will use the data of this
     * filter and therefore may be changed later (if this is desired). If you want a
     * copy, use the copy-constructor {@link #FilterBuilder(FilterBuilder)}.
     *
     * @return the filter triggering according to decisions
     */
    public IMessageFilter build() {
        return this::filterCommand;
    }

    private String filterCommand(final Configuration c, final RoleEnum r, final UUID p, final int s, final boolean i) {

        // Filter 'connection'
        if (gotConnectionSpecification && shouldBeConnected != i) {
            return negate ? TRUE_STRING : "The client has to be connected to send this message.";
            // will be false if not negated, true otherwise
        }

        // Filter 'game phase bounds'
        if (gotGamePhaseBounds) {
            final String report = filterCommandGamePhases(c);
            if (report != null) {
                return report;
            }
        }

        // Filter 'role bounds'
        if (gotRoleBounds && !roleShouldBe.contains(r)) {
            return reportError("The client has to have the one of the role " + r + " to send this message.", negate);
            // will be false if not negated, true otherwise
        }

        // Filter 'strike bounds'
        if (gotStrikeBounds) {
            final String report = filterCommandStrikeBounds(s);
            if (report != null) {
                return report;
            }
        }

        // will be negated here as it should succeed otherwise :D
        return reportError("Negation failed for client. This means, the filter was denied cause of negate.", !negate);
    }

    private String filterCommandGamePhases(final Configuration c) {
        if (phaseShouldBeIn == null) {
            final PhaseComparator before = c.getGamePhase().compareWith(startPhase);
            final PhaseComparator after = c.getGamePhase().compareWith(endPhase);

            if (phaseIsNeitherAfterNorEqual(before)) {
                // the phase is not after the before point
                return reportError(
                        SET_GAME_PHASES + c.getGamePhase() + ") is after starting with + " + startPhase + ".", negate);

            }

            if (phaseIsNeitherBeforeNorEqual(after)) {
                // the phase is not before the after point
                return reportError(SET_GAME_PHASES + c.getGamePhase() + ") is before ending with + " + endPhase + ".",
                        negate);

            }
        } else if (!phaseShouldBeIn.contains(c.getGamePhase())) { // handle that gamePhase is as expected
            return reportError(SET_GAME_PHASES + c.getGamePhase() + ") is in the list of: " + phaseShouldBeIn + ".",
                    negate);

        }

        return null;
    }

    private String filterCommandStrikeBounds(final int s) {
        if (strikeShouldBe == null) {
            // we got upper and lower bounds
            if (notIn(s, lowerStrikeBound, upperStrikeBound)) {
                return reportError("The strike-count of the client has to be in [" + lowerStrikeBound + ", "
                        + upperStrikeBound + "] to send this message, but is: " + s + ".", negate);
            }
        } else if (!strikeShouldBe.contains(s)) {// we got list
            return reportError("The strike-count of the client has to be in {" + strikeShouldBe
                    + "} to send this message, but is: " + s + ".", negate);
        }

        return null;
    }

    private static String reportError(final String errorMessage, final boolean negate) {
        // will be false if not negated, true otherwise
        return negate ? TRUE_STRING : errorMessage;
    }

    private static boolean phaseIsNeitherAfterNorEqual(final PhaseComparator comp) {
        return comp != PhaseComparator.IS_AFTER && comp != PhaseComparator.IS_EQUAL;
    }

    private static boolean phaseIsNeitherBeforeNorEqual(final PhaseComparator comp) {
        return comp != PhaseComparator.IS_BEFORE && comp != PhaseComparator.IS_EQUAL;
    }

    private static boolean notIn(final int i, final int lower, final int upper) {
        return i < lower || i > upper;
    }

    public static IMessageFilter trueFilter() {
        return FilterBuilder::trueFilter;
    }

    /** Will return always true */
    private static String trueFilter(final Object... ignored) {
        return TRUE_STRING;
    }

}
