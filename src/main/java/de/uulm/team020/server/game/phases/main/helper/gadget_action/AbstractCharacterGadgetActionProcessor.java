package de.uulm.team020.server.game.phases.main.helper.gadget_action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Cocktail;
import de.uulm.team020.datatypes.Exfiltration;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.main.CharacterActionProcessor;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;
import de.uulm.team020.server.game.phases.main.helper.AbstractActionProcessor;
import de.uulm.team020.server.game.phases.main.helper.CharacterActionGuard;
import de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner;

/**
 * Child class for the {@link CharacterActionProcessor} which is deemed to
 * perform gadget actions. It may use the {@link CharacterActionGuard} to shield
 * the operations. All the operations will only check gadget-specific
 * constraints - it is up to the {@link CharacterActionProcessor} to check if
 * the character has enough ap, can perform an action, ...
 *
 * @author Florian Sihler
 * @version 1.0, 07/07/2020
 */
public abstract class AbstractCharacterGadgetActionProcessor extends AbstractActionProcessor {

    protected static final String ERROR_MSG_NO_TARGET_CHARACTER_SELECTED = "No target character selected.";

    protected static final String NO_TARGET_TXT = "No target character selected.";
    protected static final String NOT_IN_LINE_OF_SIGHT_TXT = "Target field is not in line of sight.";
    protected static final String CHARACTER_TXT = "Character ";

    /**
     * Create the processor, it will operate on the given controller
     *
     * @param gameFieldController The gameFieldController to use
     */
    public AbstractCharacterGadgetActionProcessor(final GameFieldController gameFieldController) {
        super(gameFieldController);
    }

    /**
     * Process a gadget action for a specific character. Will not do anything if
     * there is an exception escalated upwards.
     *
     * @param connection The connection that requested the operation
     * @param action     The action that was requested
     * @param character  The character that requested the operation
     * @return The operations to be used in the game-status report
     * @throws HandlerException If there is an error on performing the operation.
     */
    public List<BaseOperation> processGadgetAction(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {

        switch (action.getGadget()) {
            case HAIRDRYER:
                return hairdryer(action, character);
            case MOLEDIE:
                return moledie(connection, action, character);
            case TECHNICOLOUR_PRISM:
                return technicolourPrism(action, character);
            case BOWLER_BLADE:
                return bowlerBlade(connection, action, character);
            case POISON_PILLS:
                return poisonPills(action, character);
            case LASER_COMPACT:
                return laserCompact(connection, action, character);
            case ROCKET_PEN:
                return rocketPen(action, character);
            case GAS_GLOSS:
                return gasGloss(connection, action, character);
            case MOTHBALL_POUCH:
                return mothballPouch(action, character);
            case FOG_TIN:
                return fogTin(action, character);
            case GRAPPLE:
                return grapple(action, character);
            case WIRETAP_WITH_EARPLUGS:
                return wiretapWithEarplugs(connection, action, character);
            case JETPACK:
                return jetpack(action, character);
            case CHICKEN_FEED:
                return chickenFeed(connection, action, character);
            case NUGGET:
                return nugget(connection, action, character);
            case MIRROR_OF_WILDERNESS:
                return mirrorOfWilderness(connection, action, character);
            case COCKTAIL:
                return cocktail(connection, action, character);
            case DIAMOND_COLLAR:
            default:
                throw new IllegalMessageException("The gadget: " + action.getGadget() + " cannot be used.");
        }
    }

    protected abstract List<BaseOperation> hairdryer(GadgetAction action, Character character) throws HandlerException;

    protected abstract List<BaseOperation> moledie(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> technicolourPrism(GadgetAction action, Character character)
            throws HandlerException;

    protected abstract List<BaseOperation> bowlerBlade(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> poisonPills(GadgetAction action, Character character)
            throws HandlerException;

    protected abstract List<BaseOperation> laserCompact(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> rocketPen(GadgetAction action, Character character) throws HandlerException;

    protected abstract List<BaseOperation> gasGloss(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> mothballPouch(GadgetAction action, Character character)
            throws HandlerException;

    protected abstract List<BaseOperation> fogTin(GadgetAction action, Character character) throws HandlerException;

    protected abstract List<BaseOperation> grapple(GadgetAction action, Character character) throws HandlerException;

    protected abstract List<BaseOperation> wiretapWithEarplugs(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> jetpack(GadgetAction action, Character character) throws HandlerException;

    protected abstract List<BaseOperation> chickenFeed(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> nugget(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> mirrorOfWilderness(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected abstract List<BaseOperation> cocktail(NttsClientConnection connection, GadgetAction action,
            Character character) throws HandlerException;

    protected Optional<Character> cocktailOnCharacter(final NttsClientConnection connection, Character targetCharacter,
            final Character character, final Point target, final List<BaseOperation> res) throws HandlerException {
        boolean success;
        // check if holding cocktail
        final Optional<Gadget> mayCocktail = character.getGadgetType(GadgetEnum.COCKTAIL);
        if (mayCocktail.isEmpty()) {
            throw new IllegalMessageException("Character is not holding any cocktail.");
        }
        final Cocktail cocktail = (Cocktail) mayCocktail.get();
        if (targetCharacter == character) {
            // cast on self --> drink
            success = drinkCocktail(character, res, cocktail, connection);
        } else {
            // cast on neighbour --> pour
            // validate target field
            guard.assureValidNeighbourField(target, character.getCoordinates(), controller.getMap(),
                    "gadget-cocktail pour action");
            // aim
            final Optional<Character> newAim = aimAtTargetWithNeighbours(targetCharacter, character.getCoordinates(),
                    connection, character);
            if (newAim.isPresent()) {
                targetCharacter = newAim.get();
                success = spillCocktailOnCharacter(targetCharacter, character, connection);
            } else {
                success = false;
            }
        }
        // remove from inventory
        character.removeGadget(cocktail);
        if (success) {
            return Optional.of(targetCharacter);
        } else {
            return Optional.empty();
        }
    }

    private boolean spillCocktailOnCharacter(final Character targetCharacter, final Character character,
            final NttsClientConnection connection) {
        boolean success;
        if (guard.flip(character, matchconfig.getCocktailDodgeChance())) {
            // dodged
            magpie.writeDebug(CHARACTER_TXT + targetCharacter + " dodged the cocktail pour from " + character,
                    TURN_TXT);
            success = false;
        } else {
            // not dodged --> target character receives clammy clothes property
            magpie.writeDebug(CHARACTER_TXT + character + " pours cocktail over " + targetCharacter, TURN_TXT);
            targetCharacter.addProperty(PropertyEnum.CLAMMY_CLOTHES);
            success = true;
            controller.getStatisticsProvider().castedCocktail(connection);
        }
        return success;
    }

    private boolean drinkCocktail(final Character character, final List<BaseOperation> res, final Cocktail cocktail,
            final NttsClientConnection connection) {
        boolean success;
        final int damageOrHp = matchconfig.getCocktailHp();
        final boolean hasRobustStomachProperty = character.getProperties().contains(PropertyEnum.ROBUST_STOMACH);
        if (cocktail.getPoisoned()) {
            drinkPoisonedCocktail(character, res, damageOrHp, hasRobustStomachProperty);
            controller.getStatisticsProvider().sippedPoisonedCocktail(connection);
        } else {
            drinHealingCocktail(character, damageOrHp, hasRobustStomachProperty);
        }
        // independent if heal or damage
        controller.getStatisticsProvider().sippedCocktail(connection);
        success = true;
        return success;
    }

    private void drinHealingCocktail(final Character character, int damageOrHp,
            final boolean hasRobustStomachProperty) {
        if (hasRobustStomachProperty) {
            damageOrHp *= 2;
        }
        // heal
        magpie.writeDebug(CHARACTER_TXT + character + " drinks cocktail and receives " + damageOrHp + " hp.", TURN_TXT);
        character.setHp(character.getHp() + damageOrHp);
    }

    private void drinkPoisonedCocktail(final Character character, final List<BaseOperation> res, int damageOrHp,
            final boolean hasRobustStomachProperty) {
        if (hasRobustStomachProperty) {
            damageOrHp /= 2;
        }
        // damage
        magpie.writeDebug(
                CHARACTER_TXT + character + " drinks poisoned cocktail and receives " + damageOrHp + " damage.",
                TURN_TXT);
        res.addAll(damageCharacter(character, damageOrHp, true));
    }

    protected boolean cocktailOnField(final Character character, final Point target) throws HandlerException {
        // pick up cocktail
        // validate target field
        final Field targetField = guard.assureValidNeighbourField(target, character.getCoordinates(),
                controller.getMap(), "gadget-cocktail pick up action");
        // assure bar table
        if (targetField.getState() != FieldStateEnum.BAR_TABLE) {
            throw new IllegalMessageException("Target field is no bar table.");
        }
        // change if being able to place a cocktail is needed) assure existing cocktail
        final Gadget cocktail = targetField.getGadget();
        if (fieldHasNoGadget(cocktail)) {
            throw new IllegalMessageException("Target bar table does not hold any cocktail.");
        }
        // check if already holding cocktail
        if (character.getGadgetType(GadgetEnum.COCKTAIL).isPresent()) {
            throw new IllegalMessageException("Character is already holding a cocktail.");
        }
        // pick up cocktail
        magpie.writeDebug(CHARACTER_TXT + character + " picks up cocktail.", TURN_TXT);
        character.addGadget(cocktail);
        targetField.setGadget(null);
        return true;

    }

    private boolean fieldHasNoGadget(final Gadget cocktail) {
        return cocktail == null || cocktail.getGadget() != GadgetEnum.COCKTAIL;
    }

    /**
     * This will toss the moledie if there is no character on the target field --
     * which will not be checked for validity so it is up to the caller to validate
     * the target field is in moledie-throwing range! -- it will find the closest
     * one using
     * {@link GameFieldController#getClosestCharacterByWalk(Point, boolean)} and, if
     * this finds none, using:
     * {@link GameFieldController#getClosestCharacterByEuclidean(Point)}.
     * <p>
     * This method will call {@link Character#removeMoledie()} on the tosser and the
     * {@link Character#getMoledie(Gadget)} method for the target character.
     *
     * @param thrower The character throwing the moledie
     * @param target  The target the moledie shall be thrown to, check separately
     *                for being valid.
     * @return The character that gained the moledie - this might be the one that
     *         has thrown the moledie.
     */
    public Character tossMoledie(final Character thrower, final Point target) {
        // May Test using: !target.isOnField(controller.getMap()) ||
        // !controller.getMap().getSpecificField(target).isWalkable()

        // remove it:
        final Gadget moledie = thrower.removeMoledie();
        // find closest by walk:
        final Optional<Character> mayTossTarget = controller.getClosestCharacter(target, false);
        if (mayTossTarget.isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "Moledie-Tossing wasn't able to find a shortest character via euclidean for target: " + target
                            + " and thrower: " + thrower);
        }
        final Character tossTarget = mayTossTarget.get();
        tossTarget.getMoledie(moledie);
        return tossTarget;
    }

    /**
     * This moves a given character to a random free
     * {@link FieldStateEnum#BAR_SEAT}. If all ones are occupied a random sitting
     * character will be moved to the next free position and his former seat will be
     * used by the passed character which finally {@link Character#exfiltrate()}
     * will be called on. This applies the given {@code damage} to the given
     * {@code targetCharacter}. This includes possible exfiltration and adding
     * received damage to statistics.
     *
     * @param targetCharacter  The character to received damage
     * @param damage           The damage to deal
     * @param isCocktailDamage Whether the damage is cocktail damage
     * @return Set of possible operations that have occurred during damaging.
     */
    protected List<BaseOperation> damageCharacter(final Character targetCharacter, final int damage,
            final boolean isCocktailDamage) {
        final List<BaseOperation> res = new LinkedList<>();
        if (guard.isCatOrJanitor(targetCharacter)) {
            magpie.writeDebug("Skipping damaging " + targetCharacter.getName(), TURN_TXT);
            return res;
        }
        final int finalDamage;
        // check for toughness
        if (hasToughnessWhichApplies(targetCharacter, isCocktailDamage)) {
            // reduce damage by half
            finalDamage = damage / 2;
        } else {
            finalDamage = damage;
        }
        // deal damage and check if target character has died
        if (targetCharacter.removeHp(finalDamage)) {
            // perform exfiltration
            res.addAll(exfiltration(targetCharacter));
        }
        // add received damage to statistics if target character was no npc
        controller.decodePlayerGameRoleByCharacter(targetCharacter).ifPresent(
                gameRoleEnum -> controller.getStatisticsProvider().receivedDamage(gameRoleEnum, finalDamage));
        return res;
    }

    private boolean hasToughnessWhichApplies(final Character targetCharacter, final boolean isCocktailDamage) {
        return !isCocktailDamage && targetCharacter.getProperties().contains(PropertyEnum.TOUGHNESS);
    }

    /**
     * This moves a given character to a random free
     * {@link FieldStateEnum#BAR_SEAT}. If all ones are occupied a random sitting
     * character will be moved to the next free position and his former seat will be
     * used by the passed character which finally {@link Character#exfiltrate()}
     * will be called on. This applies the given {@code damage} to the given
     * {@code targetCharacter}. This includes possible exfiltration and adding
     * received damage to statistics.
     *
     * @param targetCharacter The character to received damage
     * @param damage          The damage to deal
     * @return Set of possible operations that have occurred during damaging.
     */
    protected List<BaseOperation> damageCharacter(final Character targetCharacter, final int damage) {
        return damageCharacter(targetCharacter, damage, false);
    }

    /**
     * This assures that the passed {@code character} is not a cat or janitor.
     *
     * @param character The character to be checked
     * @throws HandlerException If the passed character is a cat or janitor
     */
    protected void assureNotCatOrJanitor(final Character character) throws HandlerException {
        if (guard.isCatOrJanitor(character)) {
            throw new IllegalMessageException("Target character cannot be cat or janitor.");
        }
    }

    /**
     * Checks whether the target character has the {@link PropertyEnum#HONEY_TRAP}
     * or if any neighbour character of own faction has
     * {@link PropertyEnum#BABYSITTER} property. If it does, there is a chance
     * (honey trap success chance as specified in {@link Matchconfig}) that the aim
     * moves to another character in passed {@code possibleTargetPoints}. After that
     * the baby sitter property is checked.
     *
     * @param targetCharacter      The initial target character
     * @param possibleTargetPoints The possible target points to be chosen from if
     *                             target character owns the
     *                             {@link PropertyEnum#HONEY_TRAP} property
     * @param connection           The connection of the attacker (used for
     *                             determining faction)
     * @param attacker             The attacker (used for flipping
     *                             {@link Matchconfig#getBabysitterSuccessChance()}).
     *                             If {@code null} then the babysitter property for
     *                             neighbours will not be checked.
     * @return The final character to be aimed at if possible (successful babysitter
     *         will return empty)
     */
    protected Optional<Character> aimAt(final Character targetCharacter, final Set<Point> possibleTargetPoints,
            final NttsClientConnection connection, final Character attacker) {
        Optional<Character> res = Optional.of(targetCharacter);
        // honey trap check
        if (honeyTrapChooseAnotherTarget(targetCharacter)) {
            // choose another targetCharacter
            res = findAnotherTargetForHoneyTrap(targetCharacter, possibleTargetPoints, res, attacker);
        }

        // babysitter check
        final Optional<Set<Point>> mayNeighbours = controller.getMap()
                .getNeighboursOfSpecificField(targetCharacter.getCoordinates());

        return checkForBabysitter(connection, attacker, res, mayNeighbours);
    }

    private Optional<Character> checkForBabysitter(final NttsClientConnection connection, final Character attacker,
            final Optional<Character> res, final Optional<Set<Point>> mayNeighbours) {
        // babysitter cannot trigger
        if (attacker == null || connection == null || mayNeighbours.isEmpty()) {
            return res;
        }
        // babysitter can trigger, find targets
        for (final Point neighbourPos : mayNeighbours.get()) {
            final Optional<Character> mayNeighbourCharacter = controller.decodeCharacterByPosition(neighbourPos);
            try {
                if (characterMayBabysit(connection, mayNeighbourCharacter, attacker) && res.isPresent()) {
                    magpie.writeDebug(
                            "Attack for character " + res.get() + " got canceled because of near babysitter property.",
                            TURN_TXT);
                    return Optional.empty();
                }
            } catch (final GameFieldOperationException e) {
                // dont know what happened here
                magpie.writeExceptionShort(e, TURN_TXT);
            }
        }
        return res;
    }

    private Optional<Character> findAnotherTargetForHoneyTrap(final Character targetCharacter,
            final Set<Point> possibleTargetPoints, Optional<Character> res, final Character attacker) {
        magpie.writeDebug("Changing target character because of owned honey_trap property.", TURN_TXT);
        // only choose characters from targetCharacter points that dont equals the
        // current character
        final Set<Character> possibleCharacters = new HashSet<>();
        // get all characters on possible targets
        possibleTargetPoints.stream().map(controller::decodeCharacterByPosition) //
                .filter(Optional::isPresent) // get only the ones present
                .map(Optional::get) // get those present
                // only get those who are not _identical_ to target character and not the
                // janitor; Add to valid targets
                .filter(c -> c != targetCharacter && !guard.isCatOrJanitor(c))
                // filter out the attacker as he is not allowed to be a possible target
                .filter(c -> !c.getCharacterId().equals(attacker.getCharacterId()))
                // add all remaining to possible characters to choose from
                .forEach(possibleCharacters::add);
        // is valid => choose one
        if (!possibleCharacters.isEmpty()) { // if there is another target, choose it
            // this makes it easier to achieve it with the current capabilities of the
            // random helper:
            final Character chosenCharacter = controller.getRandomController().requestCharacter(
                    targetCharacter.getName(), possibleCharacters, RandomOperation.HONEY_TRAP_NEW_TARGET);
            res = Optional.of(chosenCharacter);
        }
        return res;
    }

    protected boolean honeyTrapChooseAnotherTarget(final Character targetCharacter) {
        return targetCharacter.getProperties().contains(PropertyEnum.HONEY_TRAP)
                && controller.getRandomController().requestFlip(targetCharacter.getName(),
                        matchconfig.getHoneyTrapSuccessChance(), RandomOperation.HONEY_TRAP_TRIGGERS);
    }

    protected boolean characterMayBabysit(final NttsClientConnection connection,
            final Optional<Character> mayNeighbourCharacter, final Character attacker)
            throws GameFieldOperationException {
        return mayNeighbourCharacter.isPresent() && !guard.isEnemyOrNpc(connection, mayNeighbourCharacter.get())
                && !guard.isCatOrJanitor(mayNeighbourCharacter.get())
                && mayNeighbourCharacter.get().getProperties().contains(PropertyEnum.BABYSITTER)
                && guard.flip(attacker, matchconfig.getBabysitterSuccessChance());
    }

    /**
     * This returns {@link #aimAt(Character, Set, NttsClientConnection, Character)}
     * with passed {@code
     * targetCharacter} and the neighbours of passed {@code caster}.
     *
     * @param targetCharacter The initial target character
     * @param caster          The coordinates of the casting character (center of
     *                        the neighbour fields that will be used)
     * @param attacker        The attacker (used for flipping
     *                        {@link Matchconfig#getBabysitterSuccessChance()}). If
     *                        {@code null} then the babysitter property for
     *                        neighbours will not be checked.
     * @return The final character to be aimed at if possible (successful babysitter
     *         will return empty)
     * @throws IllegalMessageException If the passed {@code caster} coordinates are
     *                                 invalid
     */
    protected Optional<Character> aimAtTargetWithNeighbours(final Character targetCharacter, final Point caster,
            final NttsClientConnection connection, final Character attacker) throws IllegalMessageException {
        final Optional<Set<Point>> mayNeighbours = controller.getMap().getNeighboursOfSpecificField(caster);
        if (mayNeighbours.isEmpty()) {
            throw new IllegalMessageException(NO_TARGET_TXT);
        }
        return aimAt(targetCharacter, mayNeighbours.get(), connection, attacker);
    }

    /**
     * This moves a given character to a random free
     * {@link FieldStateEnum#BAR_SEAT}. If all ones are occupied a random sitting
     * character will be moved to the next free position and his former seat will be
     * used by the passed character which finally {@link Character#exfiltrate()}
     * will be called on.
     *
     * @param victim The character to be moved to a bar seat.
     * @return The BaseOperations for the exfiltration and possible movement
     *         messages
     */
    public List<BaseOperation> exfiltration(final Character victim) {
        final List<BaseOperation> res = new LinkedList<>();
        // create pool of possible seats
        final List<Point> barSeats = controller.getBarSeatPositions();
        final List<Point> tmpBarSeatPool = new ArrayList<>(barSeats);
        Point targetPoint = null;
        // check in random order for a free one
        while (!tmpBarSeatPool.isEmpty()) {
            final int i = controller.getRandomController().requestRange(victim.getName(), 0, tmpBarSeatPool.size(),
                    RandomOperation.BAR_SEAT_ON_EXFILTRATION);
            if (controller.decodeCharacterByPosition(tmpBarSeatPool.get(i)).isEmpty()) {
                // free
                targetPoint = tmpBarSeatPool.get(i);
                break;
            } else {
                // occupied
                tmpBarSeatPool.remove(i);
            }
        }
        // all seats occupied --> choose random one and move sitting character to next
        // free field
        if (targetPoint == null) {
            targetPoint = thereIsNoFreeSeatSoMakeOneFree(victim, res, barSeats, tmpBarSeatPool);
        }
        // drop diamond necklace
        final Optional<Gadget> diamondNecklace = victim.getGadgetType(GadgetEnum.DIAMOND_COLLAR);
        if (diamondNecklace.isPresent()) {
            controller.getMap().getSpecificField(victim.getCoordinates()).setGadget(diamondNecklace.get());
            // remove from character
            victim.removeGadget(diamondNecklace.get());
            magpie.writeDebug(
                    "Victim hold diamond_necklace gadget. Dropping on field and removing from it's inventory.",
                    TURN_TXT);
        }
        // create operation before setting coordinates, because from-Point in
        // Exfiltration is retrieved from character
        res.add(new Exfiltration(victim, targetPoint));
        // teleport victim to free bar seat
        victim.setCoordinates(targetPoint);
        victim.exfiltrate();
        return res;
    }

    private Point thereIsNoFreeSeatSoMakeOneFree(final Character victim, final List<BaseOperation> res,
            final List<Point> barSeats, final List<Point> tmpBarSeatPool) {
        // this way it is more compatible with the rnd controller
        Point targetPoint;
        final int i = controller.getRandomController().requestRange(victim.getName(), 0, tmpBarSeatPool.size(),
                RandomOperation.BAR_SEAT_ON_EXFILTRATION);
        targetPoint = barSeats.get(i);
        final Optional<Character> mayCharacter = controller.decodeCharacterByPosition(targetPoint);
        if (mayCharacter.isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "We though all seats were occupied. Why is there no character on this position?");
        }
        final Optional<Point> mayFreeTarget = GameFieldPositioner.getClosestFreeField(controller, targetPoint);
        if (mayFreeTarget.isEmpty()) {
            throw new ThisShouldNotHappenException("Well. We dont have any free fields in this whole map. How.");
        }
        // movement operation
        res.add(new Movement(mayCharacter.get(), mayFreeTarget.get()));
        // teleport sitting character
        mayCharacter.get().setCoordinates(mayFreeTarget.get());
        return targetPoint;
    }

    /**
     * Assures the Character does posses the gadget and returns it if it is there.
     * It will Throw an exception otherwise
     * 
     * @param character  The character to ask the gadget from
     * @param gadgetEnum The gadget to ask for
     * 
     * @return The gadget if it is there, will throw an exception otherwise.
     */
    protected Gadget assureGadgetIsThere(final Character character, final GadgetEnum gadgetEnum) {
        final Optional<Gadget> mayGadget = character.getGadgetType(gadgetEnum);
        if (mayGadget.isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "Character does not own " + gadgetEnum + ". This should have been checked by the guard.");
        }
        return mayGadget.get();
    }

    /**
     * Assure that there is a character on the given position will throw an
     * exception otherwise.
     * 
     * @param target The target Field to search a character on
     * 
     * @return The character on the asked field
     * @throws IllegalMessageException If the field is invalid -- this means the
     *                                 writer did an error
     */
    protected Character assureTargetCharacterOnPosition(final Point target) throws IllegalMessageException {
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isEmpty()) {
            throw new IllegalMessageException(ERROR_MSG_NO_TARGET_CHARACTER_SELECTED);
        }
        return mayTargetCharacter.get();
    }
}