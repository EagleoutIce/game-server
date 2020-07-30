package de.uulm.team020.server.game.phases.main.helper.gadget_action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Cocktail;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.WiretapWithEarplugs;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.main.CharacterActionProcessor;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;
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
 * @author Lennart Altenhof
 * 
 * @version 1.1, 07/07/2020
 */
public class ConcreteCharacterGadgetActionProcessor extends AbstractCharacterGadgetActionProcessor {

    /**
     * Create the processor, it will operate on the given controller
     *
     * @param gameFieldController The gameFieldController to use
     */
    public ConcreteCharacterGadgetActionProcessor(final GameFieldController gameFieldController) {
        super(gameFieldController);
    }

    /**
     * Can be used on the character or on a neighbour character to remove the
     * {@code CLAMMY_CLOTHES}-Property.
     *
     * @param action    The hairdryer-action will not be guarded extra
     * @param character The character that executes the operation
     * @return The operations that were executed
     * @throws HandlerException If there is an error on performing the operation.
     */
    @Override
    protected List<BaseOperation> hairdryer(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        boolean successful;

        // is the cast on the character itself?
        if (character.getCoordinates().equals(target)) {
            magpie.writeDebug("Hairdryer used on itself for: " + character, TURN_TXT);
            // if not present we do not care
            successful = character.getProperties().remove(PropertyEnum.CLAMMY_CLOTHES);
        } else {
            // validate the target is a neighbour
            guard.assureValidNeighbourField(target, character.getCoordinates(), map, "gadget-hairdryer action");
            // get the character on the neighbour field - if there is one
            try {
                final Character targetCharacter = guard.assureValidCharacter(target);
                // if it is cat or janitor, we will just ignore and
                successful = checkValidHairdryerTarget(targetCharacter);
            } catch (final GameFieldOperationException ex) {
                throw new IllegalMessageException("No character here. " + ex.getMessage());
            }
        }
        magpie.writeDebug("Removing CLAMMY_CLOTHES property did " + (successful ? "" : "not ") + "succeed.", TURN_TXT);
        // decrement ap
        character.removeAp();
        return List.of(new GadgetAction(action.getCharacterId(), true, target, action.getGadget()));
    }

    private boolean checkValidHairdryerTarget(final Character targetCharacter) {
        boolean successful;
        if (guard.isCatOrJanitor(targetCharacter)) {
            magpie.writeDebug("Tried the hairdryer on creature: " + targetCharacter
                    + " we will ignore that - yet consume the ap!", TURN_TXT);
            successful = false;
        } else {
            magpie.writeDebug("Using hairdryer on: " + targetCharacter, TURN_TXT);
            successful = targetCharacter.getProperties().remove(PropertyEnum.CLAMMY_CLOTHES);
        }
        return successful;
    }

    /**
     * Can be used on any non-wall field that is in
     * {@link Matchconfig#getMoledieRange()}. This will move the
     * {@link GadgetEnum#MOLEDIE} gadget in the closest character's inventory.
     *
     * @param connection The connection (used for determining faction)
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> moledie(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        Point target = action.getTarget();
        boolean success = true;

        // validate target field
        final Field targetField = guard.assureValidField(target, controller.getMap(), "gadget-moledie action");
        if (fieldIsWall(targetField)) {
            throw new IllegalMessageException("Target field must not be a wall.");
        }
        assureIsLineOfSight(character, target);
        // assure range
        final int range = matchconfig.getMoledieRange();
        if (guard.getDistance(character.getCoordinates(), target) > range) {
            throw new IllegalMessageException("Target is out of moledie range (" + range + ").");
        }
        // check if direct attack --> aim
        if (serverConfig.moledieIsDirectAttack()) {
            // aim if character
            final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
            if (mayTargetCharacter.isPresent()) {
                Character targetCharacter = mayTargetCharacter.get();
                final Optional<Character> newAim = findTargetCharacterForToss(connection, character, target,
                        targetCharacter);
                if (newAim.isPresent()) {
                    targetCharacter = newAim.get();
                    target = targetCharacter.getCoordinates();
                } else {
                    success = false;
                }
            }
        }
        Character newMoledieOwner = null;
        if (success) {
            // toss
            newMoledieOwner = tossMoledie(character, target);
        }
        // decrement ap
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), success,
                success ? newMoledieOwner.getCoordinates() : target, GadgetEnum.MOLEDIE));
    }

    private Optional<Character> findTargetCharacterForToss(final NttsClientConnection connection,
            final Character character, final Point target, final Character targetCharacter) throws HandlerException {
        final Character preAimTargetCharacter = targetCharacter;
        // assure not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // get possible targets for aim
        final Optional<List<Point>> mayPossibleTargets = controller.getPossibleMoledieTargets(target);
        if (mayPossibleTargets.isEmpty()) {
            throw new ThisShouldNotHappenException("No possible targets found for tossing moledie.");
        }
        final List<Point> possibleTargets = mayPossibleTargets.get();
        possibleTargets.removeIf(p -> {
            final Optional<Character> maySameCharacter = controller.decodeCharacterByPosition(p);
            return maySameCharacter.isPresent() && maySameCharacter.get() == preAimTargetCharacter;
        });
        return aimAt(targetCharacter, new HashSet<>(possibleTargets), connection, character);
    }

    /**
     * Can be used on any target in line of sight, with no character in line of
     * sight and in {@link Matchconfig#getBowlerBladeRange()}. If flip with
     * {@link Matchconfig#getBowlerBladeHitChance()} is successfully, the target
     * character receives {@link Matchconfig#getBowlerBladeDamage()} damage. The
     * bowler blade then drops on the closest next free field from the target
     * character no matter whether attacking succeeded or not.
     *
     * @param connection The connection used for determining character faction
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> bowlerBlade(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();
        final List<BaseOperation> res = new ArrayList<>();

        final Gadget bowlerBlade = assureGadgetIsThere(character, GadgetEnum.BOWLER_BLADE);

        // validate target field
        guard.assureValidField(target, controller.getMap(), "gadget-bowler-blade action");
        // validate line of sight (without characters)
        final Path line = assureIsLineOfSight(character, target);
        // assure no character in line of sight
        final Set<Point> blockingFields = getAllBlockingFields(line);
        if (!blockingFields.isEmpty()) {
            throwNotInLineOfSightException(target, line, blockingFields);
        }
        // assure existing target
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isEmpty()) {
            throw new IllegalMessageException(NO_TARGET_TXT);
        }
        Character targetCharacter = mayTargetCharacter.get();
        // assure not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // assure range
        assureRangeIsValid(character, target);
        //
        Point targetBeforePossibleExfiltration = new Point(targetCharacter.getCoordinates());
        // flip
        boolean success = false;
        final List<BaseOperation> damageOperations = new ArrayList<>();
        if (guard.flip(character, matchconfig.getBowlerBladeHitChance())) {
            // still aim because of babysitter
            final Optional<Character> newAim = aimAt(targetCharacter, guard.getAllCharactersInLineOfSightWithRange(
                    character.getCoordinates(), matchconfig.getBowlerBladeRange()), connection, character);

            if (newAim.isPresent()) {
                targetBeforePossibleExfiltration = new Point(newAim.get().getCoordinates());
            }

            // if babysitter saved or character owns magnetic watch
            if (noTargetOrMagneticWatch(newAim)) {
                success = false;
            } else {
                targetCharacter = getTargetCharacterAndDealDamage(damageOperations, newAim.get());
                success = true;
            }
        }
        // drop on neighbour of target (no matter if damage dealen or not)
        character.removeGadget(bowlerBlade);
        final Optional<Point> mayClosestFreeField = GameFieldPositioner.getClosestFreeField(controller,
                targetBeforePossibleExfiltration);
        if (mayClosestFreeField.isEmpty()) {
            throw new IllegalMessageException("No free position found to drop bowler blade. Y'all thick bois eh.");
        }
        dropBowlerBladeOnClosestFreeField(bowlerBlade, mayClosestFreeField.get());
        // decrement ap
        character.removeAp();
        res.add(new GadgetAction(character.getCharacterId(), success, targetCharacter.getCoordinates(),
                GadgetEnum.BOWLER_BLADE));
        res.addAll(damageOperations);
        return res;
    }

    private void dropBowlerBladeOnClosestFreeField(final Gadget bowlerBlade, final Point closestFreeField) {
        magpie.writeDebug("Dropping bowler hat on " + closestFreeField, TURN_TXT);
        controller.getMap().getSpecificField(closestFreeField).setGadget(bowlerBlade);
    }

    private Character getTargetCharacterAndDealDamage(final List<BaseOperation> damageOperations,
            final Character targetCharacter) {
        // deal damage
        final int damage = matchconfig.getBowlerBladeDamage();
        magpie.writeDebug("Dealing " + damage + " damage to " + targetCharacter + " with bowler blade.", TURN_TXT);
        damageOperations.addAll(damageCharacter(targetCharacter, damage));
        return targetCharacter;
    }

    private boolean noTargetOrMagneticWatch(final Optional<Character> newAim) {
        return newAim.isEmpty() || newAim.get().getGadgetType(GadgetEnum.MAGNETIC_WATCH).isPresent();
    }

    private void assureRangeIsValid(final Character character, final Point target) throws IllegalMessageException {
        final int range = matchconfig.getBowlerBladeRange();
        if (guard.getDistance(character.getCoordinates(), target) > range) {
            throw new IllegalMessageException("Target is out of bowler blade range (" + range + ").");
        }
    }

    private void throwNotInLineOfSightException(final Point target, final Path line, final Set<Point> blockingFields)
            throws IllegalMessageException {
        final StringBuilder strBlockingCharacters = new StringBuilder();
        blockingFields.forEach(p -> controller.decodeCharacterByPosition(p).ifPresent(strBlockingCharacters::append));
        throw new IllegalMessageException(
                "Target not in line of sight because character is in line of sight. " + "Line: " + line + ", target: "
                        + target + ", blockingCharacters: " + strBlockingCharacters.toString());
    }

    private Path assureIsLineOfSight(final Character character, final Point target) throws IllegalMessageException {
        final Path line = Point.getLine(character.getCoordinates(), target);
        if (!line.isLineOfSight(controller.getMap())) {
            throw new IllegalMessageException(NOT_IN_LINE_OF_SIGHT_TXT + " Found: " + line);
        }
        return line;
    }

    private Set<Point> getAllBlockingFields(final Path line) {
        // remove start and end point because of
        return line.stream().filter(p -> !p.equals(line.getStart())) //
                .filter(p -> !p.equals(line.getEnd())) // caster and target being in path
                .filter(p -> {
                    final Optional<Character> c = controller.decodeCharacterByPosition(p);
                    return c.isPresent() && (serverConfig.bowlerBladeLineOfSightInterruptedByCatOrJanitor()
                            || !guard.isCatOrJanitor(c.get()));
                }).collect(Collectors.toSet());
    }

    /**
     * Can be used on a neighbour roulette table to change the inverted state. This
     * will negate all results of gambling.
     *
     * @param action    The technicolor-prism action
     * @param character The character that executes the operation
     * @return The operations that were executed
     * @throws HandlerException If there is an error on performing the operation.
     */
    @Override
    protected List<BaseOperation> technicolourPrism(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // validate the target is a neighbour and get field
        final Field rouletteField = guard.assureValidNeighbourField(target, character.getCoordinates(), map,
                "gadget-technicolor-prism action");
        // assure that the target field is a roulette table
        if (rouletteField.getState() != FieldStateEnum.ROULETTE_TABLE) {
            throw new IllegalMessageException("Target field is not a roulette table.");
        }
        // invert table
        magpie.writeDebug("Placing technicolor prism on roulette table.", TURN_TXT);
        rouletteField.setInverted(true);
        // remove from inventory
        final Optional<Gadget> gadget = character.getGadgetType(action.getGadget());
        if (gadget.isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "Gadget should exist because of being validated by the character action guard.");
        }
        character.removeGadget(gadget.get());
        // decrement ap
        character.removeAp();
        // placing a technicolor prism cannot fail
        return List.of(new GadgetAction(action.getCharacterId(), true, target, action.getGadget()));
    }

    /**
     * Can be used on a neighbour field containing a cocktail in order to poison it.
     * This will damage the character who drinks the cocktail instead of healing it.
     *
     * @param action    The poison-pills action
     * @param character The character that executes the operation
     * @return The operations that were executed
     * @throws HandlerException If there is an error on performing the operation.
     */
    @Override
    protected List<BaseOperation> poisonPills(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // get gadget
        final Gadget poisonPills = assureGadgetIsThere(character, GadgetEnum.POISON_PILLS);

        // validate target is a neighbour and get field
        final Field targetField = guard.assureValidNeighbourField(target, character.getCoordinates(), map,
                "gadget-poison-pills action");
        final Cocktail cocktail;
        // check if field or character
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isPresent()) {
            // character
            cocktail = (Cocktail) getValidCocktailFromCharacter(mayTargetCharacter.get());
        } else {
            // field
            // assure target field holds a cocktail
            if (targetFieldDoesNotHaveACocktail(targetField)) {
                throw new IllegalMessageException("Target field does not hold any cocktail.");
            }
            cocktail = (Cocktail) targetField.getGadget();
        }
        poisonCocktail(cocktail);
        reduceGadgetUsagesAndRemoveIfNoneLeft(character, poisonPills);
        // decrement ap
        character.removeAp();
        // poisoning a cocktail cannot fail
        return List.of(new GadgetAction(action.getCharacterId(), true, target, action.getGadget()));
    }

    private Gadget getValidCocktailFromCharacter(final Character targetCharacter) throws IllegalMessageException {
        final Optional<Gadget> mayCocktail = targetCharacter.getGadgetType(GadgetEnum.COCKTAIL);
        // assure character holds cocktail
        if (mayCocktail.isEmpty()) {
            throw new IllegalMessageException("Target character does not hold any cocktail.");
        }
        return mayCocktail.get();
    }

    private void poisonCocktail(final Cocktail cocktail) {
        magpie.writeDebug(
                "Poisoning cocktail" + (cocktail.getPoisoned() ? " (was already poisoned but never mind)." : "."),
                TURN_TXT);
        cocktail.setPoisoned(true);
    }

    private boolean targetFieldDoesNotHaveACocktail(final Field targetField) {
        return targetField.getGadget() == null || targetField.getGadget().getGadget() != GadgetEnum.COCKTAIL
                || !(targetField.getGadget() instanceof Cocktail);
    }

    /**
     * Can be used on a cocktail in line of sight (hold by a character or simply
     * existing on field) which gets removed by laser compact hit chance specified
     * in {@link Matchconfig}.
     *
     * @param connection The connection that is used for determining faction
     * @param action     The action to be executed
     * @param character  The character that performs the action
     * @return The resulting operations
     * @throws HandlerException If any error happens during checks or execution
     */
    @Override
    protected List<BaseOperation> laserCompact(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // assure existing target
        final Field targetField = guard.assureValidField(target, map);
        // validate line of sight
        final Path line = Point.getLine(character.getCoordinates(), target);
        if (!Path.isLineOfSight(line, map)) {
            throw new IllegalMessageException("Target is not in line of sight. Line was: " + line);
        }
        // assure target holds a cocktail and remove
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        final boolean success = guard.flip(character, matchconfig.getLaserCompactHitChance());

        target = shootLasterCompactAtTargetField(connection, character, target, targetField, mayTargetCharacter,
                success);

        // reduce ap
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), success, target, GadgetEnum.LASER_COMPACT));
    }

    private Point shootLasterCompactAtTargetField(final NttsClientConnection connection, final Character character,
            Point target, final Field targetField, final Optional<Character> mayTargetCharacter, boolean success)
            throws IllegalMessageException {
        if (isShootingOnCharacterWithCocktail(mayTargetCharacter)) {
            // character holding a cocktail --> remove cocktail gadget
            if (success) {
                magpie.writeDebug("Successfully using laser compact on target character.", TURN_TXT);
                Character targetCharacter = mayTargetCharacter.get();
                if (serverConfig.laserCompactIsDirectAttack()) {
                    // get possible targets and only use targets with cocktails
                    final Set<Point> possibleTargets = getPossibleLaserCompactTarget(character);
                    // aim
                    final Optional<Character> newAim = aimAt(mayTargetCharacter.get(), new HashSet<>(possibleTargets),
                            connection, character);
                    if (newAim.isPresent()) {
                        // remove cocktail from target character
                        targetCharacter = newAim.get();
                    } else {
                        success = false;
                    }
                }
                targetCharacter.getGadgetType(GadgetEnum.COCKTAIL).ifPresent(targetCharacter::removeGadget);
                // set new target
                target = targetCharacter.getCoordinates();
            }
        } else if (isShootingOnFieldWithCocktail(targetField)) {
            // field holding a gadget --> remove cocktail from field
            if (success) {
                magpie.writeDebug("Successfully using laser compact on target field.", TURN_TXT);
                targetField.setGadget(null);
            }
        } else {
            throw new IllegalMessageException("Target " + target + " does not hold a cocktail.");
        }
        debugSuccessOfCocktailOperation(success);
        return target;
    }

    private void debugSuccessOfCocktailOperation(final boolean success) {
        if (!success) {
            magpie.writeDebug("Removing a cocktail via laser compact did not succeed.", TURN_TXT);
        }
    }

    private boolean isShootingOnFieldWithCocktail(final Field targetField) {
        return targetField.getGadget() != null && targetField.getGadget().getGadget() == GadgetEnum.COCKTAIL;
    }

    private boolean isShootingOnCharacterWithCocktail(final Optional<Character> mayTargetCharacter) {
        return mayTargetCharacter.isPresent()
                && mayTargetCharacter.get().getGadgetType(GadgetEnum.COCKTAIL).isPresent();
    }

    private Set<Point> getPossibleLaserCompactTarget(final Character character) {
        return guard.getAllCharactersInLineOfSight(character.getCoordinates()).stream().filter(p -> {
            final Optional<Character> oc = controller.decodeCharacterByPosition(p);
            return isShootingOnCharacterWithCocktail(oc);
        }).collect(Collectors.toSet());
    }

    /**
     * Can be used on a target in line of sight. All walls on target field and it's
     * neighbours will be converted to fields of state {@link FieldStateEnum#FREE}
     * and characters currently positioned on one of them will receive rocket pen
     * damage as set in {@link Matchconfig}.
     *
     * @param action    The action to be executed
     * @param character The character that performs the action
     * @return The resulting operations
     * @throws HandlerException If stuff goes wrong
     */
    @Override
    protected List<BaseOperation> rocketPen(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        final List<BaseOperation> res = new ArrayList<>();

        // validate character
        if (character.getGadgetType(GadgetEnum.ROCKET_PEN).isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "Character is not holding ROCKET_PEN. This should be caught by guard.");
        }
        // validate target field
        guard.assureValidField(target, map, "rocket-pen-gadget action");
        // validate line of sight
        final Path line = Point.getLine(character.getCoordinates(), target);
        if (!line.isLineOfSight(map)) {
            throw new IllegalMessageException(
                    "Target field " + target + " is not in line of sight for your current position: "
                            + character.getCoordinates() + ". Line was: " + line);
        }
        // launch yoo
        final int damage = matchconfig.getRocketPenDamage();
        // get impact area
        final Optional<Set<Point>> mayImpactArea = map.getNeighboursOfSpecificField(target);
        if (mayImpactArea.isEmpty()) {
            throw new ThisShouldNotHappenException(
                    "Target used for neighbour discovery was not valid. This should have been checked before.");
        }
        final Set<Point> impactArea = mayImpactArea.get();
        // add the field itself to the neighbour-crew, it is the least area to be
        // impacted
        impactArea.add(target);
        final List<Field> impactAreaFields = impactArea.stream().map(map::getSpecificField)
                .collect(Collectors.toList());
        // destroy walls (replace with free fields)
        boolean changedField = false;
        for (final Field field : impactAreaFields) {
            // replace walls:
            changedField = updatedFieldIfItIsWall(changedField, field);
        }

        // If there was a field altered we will reset the pathfinder!
        if (changedField) {
            controller.invalidatePathfinder();
        }

        final List<BaseOperation> damageOperations = new ArrayList<>();
        // damage characters
        for (final Point position : impactArea) {
            damageCharacterOnGivenPosition(damage, damageOperations, position);
        }

        // reduce ap
        character.removeAp();
        // remove from inventory
        character.getGadgetType(GadgetEnum.ROCKET_PEN).ifPresent(character::removeGadget);
        // using rocket pen cannot fail
        res.add(new GadgetAction(character.getCharacterId(), true, target, GadgetEnum.ROCKET_PEN));
        res.addAll(damageOperations);
        return res;
    }

    private void damageCharacterOnGivenPosition(final int damage, final List<BaseOperation> damageOperations,
            final Point position) {
        // if there is a character - at its damage-reports (maybe exfiltration?) to the
        // operations
        controller.decodeCharacterByPosition(position)
                .ifPresent(victim -> damageOperations.addAll(damageCharacter(victim, damage)));
    }

    private boolean updatedFieldIfItIsWall(boolean changedField, final Field field) {
        if (fieldIsWall(field)) {
            field.setState(FieldStateEnum.FREE);
            changedField = true;
        }
        return changedField;
    }

    private boolean fieldIsWall(final Field field) {
        return field.getState() == FieldStateEnum.WALL;
    }

    /**
     * Can be used on a neighbour character and deals the gas gloss damage set in
     * {@link Matchconfig} to the target character. If the character dies
     * {@link #exfiltration(Character)} is being called.
     *
     * @param connection The connection used for determining faction
     * @param action     The gas-gloss action
     * @param character  The character that executes the operation
     * @return The operations that were executed
     * @throws HandlerException If there is an error on performing the operation.
     */
    @Override
    protected List<BaseOperation> gasGloss(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        // declared here because of additional operation caused by exfiltration
        final List<BaseOperation> res = new ArrayList<>();

        // get gadget
        final Gadget gasGloss = assureGadgetIsThere(character, GadgetEnum.GAS_GLOSS);

        // validate target is a neighbour
        guard.assureValidNeighbourField(target, character.getCoordinates(), map, "gadget-gas-gloss action");
        // get target character
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isEmpty()) {
            throw new IllegalMessageException("No character on target field" + target + ".");
        }
        Character targetCharacter = mayTargetCharacter.get();
        // assure not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // get neighbour fields for aiming
        // aim
        final Optional<Character> newAim = aimAtTargetWithNeighbours(targetCharacter, character.getCoordinates(),
                connection, character);
        boolean success = false;
        final List<BaseOperation> damageOperations = new ArrayList<>();
        if (newAim.isPresent()) {
            targetCharacter = newAim.get();
            // apply damage
            final int damage = matchconfig.getGasGlossDamage();
            magpie.writeDebug("Dealing " + damage + " gas gloss damage to target player.", TURN_TXT);
            // deal damage
            damageOperations.addAll(damageCharacter(targetCharacter, damage));
            success = true;
        }
        // remove gas gloss from inventory
        character.removeGadget(gasGloss);
        // decrement ap
        character.removeAp();
        res.add(new GadgetAction(action.getCharacterId(), success, targetCharacter.getCoordinates(),
                action.getGadget()));
        res.addAll(damageOperations);
        return res;
    }

    /**
     * Can be used on a fireplace in line of sight and in mothball pouch range as
     * specified in {@link Matchconfig}. This will damage all characters that are
     * neighbours of the fireplace with mothball pouch damage as specified in
     * {@link Matchconfig}, too. Usages will be decremented (5 usages in total
     * available) and if none are left the gadget will be removed from the
     * characters inventory.
     *
     * @param action    The mothball-pouch action
     * @param character The character that executes the operation
     * @return The operations that were executed
     * @throws HandlerException If there is an error on performing the operation.
     */
    @Override
    protected List<BaseOperation> mothballPouch(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        final List<BaseOperation> res = new ArrayList<>();

        // get gadget
        final Gadget mothballPouch = assureGadgetIsThere(character, GadgetEnum.MOTHBALL_POUCH);

        // validate target field
        final Field targetField = guard.assureValidField(target, map, "gadget-mothball-pouch action");
        if (fieldIsFireplace(targetField)) {
            throw new IllegalMessageException("Target field is not a fireplace.");
        }
        // assure line of sight
        final Path line = Point.getLine(character.getCoordinates(), target);
        assureIsLineOfSight(map, line);
        // assure range
        assureIsInRange(character, target, matchconfig.getMothballPouchRange(), "mothball pouch");
        // deal damage
        // get characters in impact area
        final int damage = matchconfig.getMothballPouchDamage();
        final Set<Character> charactersInImpactArea = computeImpactedCharacters(target, map);
        // remove hp and perform exfiltration if required
        final List<BaseOperation> damageOperations = new ArrayList<>();
        charactersInImpactArea.forEach(c -> damageOperations.addAll(damageCharacter(c, damage)));

        // remove ap
        character.removeAp();
        // reduce gadget usages and remove from inventory if required
        reduceGadgetUsagesAndRemoveIfNoneLeft(character, mothballPouch);

        res.add(new GadgetAction(character.getCharacterId(), true, target, GadgetEnum.MOTHBALL_POUCH));
        res.addAll(damageOperations);
        return res;
    }

    private void reduceGadgetUsagesAndRemoveIfNoneLeft(final Character character, final Gadget gadget) {
        gadget.decrementUsages();
        if (!gadget.hasUsagesLeft()) {
            character.removeGadget(gadget);
        }
    }

    private Set<Character> computeImpactedCharacters(final Point target, final FieldMap map) {
        final Set<Character> charactersInImpactArea = new HashSet<>();
        map.getNeighboursOfSpecificField(target).ifPresent(neighbours -> neighbours
                .forEach(p -> controller.decodeCharacterByPosition(p).ifPresent(charactersInImpactArea::add)));
        return charactersInImpactArea;
    }

    private boolean fieldIsFireplace(final Field targetField) {
        return targetField.getState() != FieldStateEnum.FIREPLACE;
    }

    /**
     * Can be used on any non-wall field in line of sight and in fog tin range as
     * specified in {@link Matchconfig}. This will set the {@link Field#isFoggy()}
     * to true and the {@link Field#getFoggyRoundsRemaining()} to 2. Any character
     * that stands on a foggy field cannot perform any action. The fog lasts the
     * active round and the following two rounds and will be removed during
     * {@link GameFieldController#nextRound()}.
     *
     * @param action    The gadget action to be performed
     * @param character The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> fogTin(final GadgetAction action, final Character character) throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // get gadget
        final Gadget fogTin = assureGadgetIsThere(character, GadgetEnum.FOG_TIN);

        // validate target field
        final Field targetField = guard.assureValidField(target, map, "gadget-fog-tin action");
        // assure line of sight
        final Path line = Point.getLine(character.getCoordinates(), target);
        assureIsLineOfSight(map, line);
        // assure range
        assureIsInRange(character, target, matchconfig.getFogTinRange(), "fog tin");
        // validate not wall
        if (fieldIsWall(targetField)) {
            throw new IllegalMessageException("Target field cannot be wall.");
        }
        // fog
        final Optional<List<Field>> mayImpactArea = controller.getMap().getNeighbourFieldsOfSpecificField(target);
        if (mayImpactArea.isEmpty()) {
            throw new IllegalMessageException(NO_TARGET_TXT);
        }
        final List<Field> impactArea = mayImpactArea.get();
        impactArea.add(targetField);

        for (final Field impactField : impactArea) {
            makeFieldFoggyIfItShouldBeInImpactArea(impactField);
        }

        // decrement ap
        character.removeAp();
        // remove gadget from inventory
        character.removeGadget(fogTin);
        // using fog tin gadget cannot fail
        return List.of(new GadgetAction(character.getCharacterId(), true, target, GadgetEnum.FOG_TIN));
    }

    private void makeFieldFoggyIfItShouldBeInImpactArea(final Field impactField) {
        if (serverConfig.fogHitsWalledFields() || impactField.getState() != FieldStateEnum.WALL) {
            impactField.setFoggy(true);
            impactField.setFoggyRoundsRemaining(2);
        }
    }

    private void assureIsInRange(final Character character, final Point target, final int range, final String name)
            throws IllegalMessageException {
        if (guard.getDistance(character.getCoordinates(), target) > range) {
            throw new IllegalMessageException("Target field is out of " + name + " range (" + range + ").");
        }
    }

    private void assureIsLineOfSight(final FieldMap map, final Path line) throws IllegalMessageException {
        if (!line.isLineOfSight(map)) {
            throw new IllegalMessageException(NOT_IN_LINE_OF_SIGHT_TXT + line);
        }
    }

    /**
     * Can be used on any field in line of sight and grapple range specified in
     * {@link Matchconfig}. With a success chance (grapple hit change specified in
     * {@link Matchconfig}) the gadget hold by the target field will be moved to the
     * characters inventory.
     *
     * @param action    The grapple action
     * @param character The character that performs the action
     * @return The operations that were executed
     * @throws HandlerException If any error happens while performing
     */
    @Override
    protected List<BaseOperation> grapple(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // get gadget
        assureGadgetIsThere(character, GadgetEnum.GRAPPLE);

        // validate target field and assure existing gadget to be grappled
        final Field targetField = guard.assureValidField(target, map, "gadget-grapple action");
        if (targetField.getGadget() == null) {
            throw new IllegalMessageException("Target field does not hold any gadget.");
        }
        // assure line of sight
        final Path line = Point.getLine(character.getCoordinates(), target);
        assureIsLineOfSight(map, line);
        // assure range
        assureIsInRange(character, target, matchconfig.getGrappleRange(), "grapple");
        // flip
        final boolean success = guard.flip(character, matchconfig.getGrappleHitChance());
        if (success) {
            // grab with grapple (grabbledigrab)
            character.addGadget(targetField.getGadget());
            targetField.setGadget(null);
        }
        // reduce ap
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), success, target, GadgetEnum.GRAPPLE));
    }

    /**
     * Can be used on any neighbour character which will set the
     * {@link WiretapWithEarplugs} gadget's activeOn to the target character. If the
     * target character receives any ip these will also be added to the gadget's
     * owner.
     *
     * @param connection The connection used for game role determination
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> wiretapWithEarplugs(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();

        // assure gadget
        final WiretapWithEarplugs wiretapWithEarplugs = (WiretapWithEarplugs) assureGadgetIsThere(character,
                GadgetEnum.WIRETAP_WITH_EARPLUGS);

        // assure not already active
        if (wiretapWithEarplugs.getActiveOn() != null) {
            throw new IllegalMessageException("Wiretap with earplugs already active on character.");
        }
        // assure target character
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isEmpty()) {
            throw new IllegalMessageException(ERROR_MSG_NO_TARGET_CHARACTER_SELECTED);
        }
        final Character targetCharacter = mayTargetCharacter.get();
        // assure opponent or npc character
        final Optional<GameRoleEnum> mayTargetCharacterGameRole = controller
                .decodePlayerGameRoleByCharacter(targetCharacter);
        if (mayTargetCharacterGameRole.isPresent() && mayTargetCharacterGameRole.get() == connection.getGameRole()) {
            // target character belongs to own faction
            throw new IllegalMessageException("Target character is not of opponent faction or npc.");
        }
        // check cat or janitor
        assureNotCatOrJanitor(character);
        // assure neighbour
        guard.assureValidNeighbourField(target, character.getCoordinates(), controller.getMap(),
                "gadget-wiretap-with-earplugs action");
        // add wiretap to target character
        wiretapWithEarplugs.setActiveOn(targetCharacter.getCharacterId());
        wiretapWithEarplugs.setWorking(true);
        // decrement ap
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), true, targetCharacter.getCoordinates(),
                GadgetEnum.WIRETAP_WITH_EARPLUGS));
    }

    /**
     * Can be used on any free field not being occupied by another character. This
     * will teleport the casting character to the target position. The gadget will
     * then be removed from the character's inventory.
     *
     * @param action    The action to be performed
     * @param character The character that performs the action
     * @return List of operations that were executed
     * @throws HandlerException If any error happens during execution
     */
    @Override
    protected List<BaseOperation> jetpack(final GadgetAction action, final Character character)
            throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();

        // get gadget
        final Gadget gadget = assureGadgetIsThere(character, GadgetEnum.JETPACK);
        // validate target field
        final Field targetField = guard.assureValidField(target, map, "gadget-jetpack action");
        if (serverConfig.jetpackAllowedOnIsWalkable()) {
            // check if walkable
            assureTargetFieldIsWalkable(targetField);
        } else if (targetField.getState() != FieldStateEnum.FREE) {
            throw new IllegalMessageException("Target field is not FREE.");
        }
        // target field, too. Assuming
        // that no one is allowed to be standing on target field.
        final Optional<Character> mayCharacterObstacle = controller.decodeCharacterByPosition(target);
        assureNoOneIsInTheWay(character, mayCharacterObstacle);
        // teleport
        character.setCoordinates(target);
        // decrement ap
        character.removeAp();
        // remove from inventory
        character.removeGadget(gadget);
        return List.of(new GadgetAction(character.getCharacterId(), true, target, GadgetEnum.JETPACK));
    }

    private void assureNoOneIsInTheWay(final Character character, final Optional<Character> mayCharacterObstacle)
            throws IllegalMessageException {
        if (targetIsObstacleWhichIsNotSelf(character, mayCharacterObstacle)) {
            throw new IllegalMessageException("Target field is occupied by another character.");
        }
    }

    private boolean targetIsObstacleWhichIsNotSelf(final Character character,
            final Optional<Character> mayCharacterObstacle) {
        return mayCharacterObstacle.isPresent() && mayCharacterObstacle.get() != character;
    }

    private void assureTargetFieldIsWalkable(final Field targetField) throws IllegalMessageException {
        if (!targetField.isWalkable()) {
            throw new IllegalMessageException("Target field " + targetField + " is not walkable.");
        }
    }

    /**
     * Can be used on any neighbour character. If the target character is not of the
     * opposite faction (npc or you selected a character of your own one), nothing
     * happens. Otherwise the difference of the target character's ip and the
     * performing character's ones will be calculated. If the difference is
     * <em>lower</em> than the performing characters ones, it will be
     * <em>subtracted</em>. If it is <em>greater</em>, it will be <em>added</em>.
     * Otherwise nothin The gadget will be removed from the players inventory after
     * being used.
     *
     * @param connection The connection that is also used for retrieving game role
     * @param action     The action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws IllegalMessageException If any error happens during execution
     */
    @Override
    protected List<BaseOperation> chickenFeed(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();

        // get gadget
        final Gadget chickenFeed = assureGadgetIsThere(character, GadgetEnum.CHICKEN_FEED);

        final Character targetCharacter = assureTargetCharacterOnPosition(target);
        // check if neighbour
        if (!Point.isNeighbour(character.getCoordinates(), target)) {
            throw new IllegalMessageException("Target character is no neighbour of performing character.");
        }
        // assure not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // check if target character belongs to enemy player
        try {
            if (guard.isEnemy(connection, targetCharacter)) {
                // calc ip
                calculateAndPerformIpSwap(connection, character, targetCharacter);
            } else if (guard.isOneOfMine(connection, targetCharacter)) {
                throw new IllegalMessageException("Cannot be used on character of own faction.");
            } // else npc --> nothing happens
        } catch (final GameFieldOperationException e) {
            throw new ThisShouldNotHappenException("Got " + e + " while checking if target character is enemy or npc.");
        }
        // remove ap
        character.removeAp();
        // remove gadget from inventory
        character.removeGadget(chickenFeed);
        // using chicken feed cannot fail
        return List.of(new GadgetAction(character.getCharacterId(), true, targetCharacter.getCoordinates(),
                GadgetEnum.CHICKEN_FEED));
    }

    private void calculateAndPerformIpSwap(final NttsClientConnection connection, final Character character,
            final Character targetCharacter) {
        final int characterIp = character.getIp();
        final int targetCharacterIp = targetCharacter.getIp();
        final int diffIp = Math.abs(characterIp - targetCharacterIp);
        final int newCharacterIp;
        if (characterIp < targetCharacterIp) {
            // subtract
            newCharacterIp = Math.max(characterIp - diffIp, 0);
        } else if (characterIp > targetCharacterIp) {
            // add
            newCharacterIp = characterIp + diffIp;
        } else {
            // have a nice day
            newCharacterIp = characterIp;
        }
        // set ip (must be >= 0)
        final int newCharacterIpDiff = newCharacterIp - characterIp;
        if (newCharacterIpDiff > 0) {
            // use add ip because of wiretap with earplugs
            guard.addIpToCharacter(character, newCharacterIpDiff);
        } else {
            // set directly because of subtraction
            controller.getStatisticsProvider().removeIP(connection, Math.abs(newCharacterIpDiff));
            character.setIp(newCharacterIp);
        }
    }

    /**
     * Can be used on a neighbour character. If the character is an npc, he will be
     * added to the performing character's faction. If he belongs to the opposite
     * faction the opponent will be notified and the target character gets the
     * nugget. If the target character belongs to the performing character's
     * faction, nothing happens. After performing the action the nugget will be
     * removed from the character's inventory.
     *
     * @param connection The connection that is used for determining the game role
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any errors occur during execution
     */
    @Override
    protected List<BaseOperation> nugget(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();

        // get gadget
        final Gadget nugget = assureGadgetIsThere(character, GadgetEnum.NUGGET);

        // validate character
        final Character targetCharacter = assureTargetCharacterOnPosition(target);
        // assure target character is not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // check if neighbour
        guard.assureValidNeighbourField(character.getCoordinates(), target, controller.getMap());
        // check if target character belongs to enemy player
        final Optional<GameRoleEnum> mayTargetGameRole = controller.decodePlayerGameRoleByCharacter(targetCharacter);
        boolean success = false;
        if (mayTargetGameRole.isEmpty()) {
            // npc --> add to faction
            final Faction faction = getCorrectFactionByConnection(connection);
            faction.add(targetCharacter);
            controller.getNeutralFaction().remove(targetCharacter);
            success = true;
        } else if (mayTargetGameRole.get() == connection.getGameRole()) {
            // character of own faction
            throw new IllegalMessageException("Target character cannot be of own faction.");
        } else {
            // opposite faction --> add nugget to target character
            targetCharacter.addGadget(nugget);
        }
        // decrement ap
        character.removeAp();
        // remove gadget from inventory
        character.removeGadget(nugget);
        return List.of(new GadgetAction(character.getCharacterId(), success, targetCharacter.getCoordinates(),
                GadgetEnum.NUGGET));
    }

    private Faction getCorrectFactionByConnection(final NttsClientConnection connection) {
        return connection.getGameRole() == GameRoleEnum.PLAYER_ONE ? controller.getPlayerOneFaction()
                : controller.getPlayerTwoFaction();
    }

    /**
     * Can be used on any neighbour character. If the character belongs to own
     * faction swap the character's ips. If it belongs to the opposite faction then
     * with the mirror swap chance as specified in {@link Matchconfig} the ips are
     * swapped and the mirror is getting destroyed. Otherwise it remains usable and
     * in the performing character's inventory. Npc do not own any ip so nothing
     * happens then.
     *
     * @param connection The connection for retrieving game role
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that have been executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> mirrorOfWilderness(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        final Point target = action.getTarget();

        // validate gadget
        final Gadget mirrorOfWilderness = assureGadgetIsThere(character, GadgetEnum.MIRROR_OF_WILDERNESS);

        final Character targetCharacter = assureTargetCharacterOnPosition(target);
        // assure target character is not cat or janitor
        assureNotCatOrJanitor(targetCharacter);
        // validate neighbour
        guard.assureValidNeighbourField(character.getCoordinates(), target, controller.getMap(),
                "gadget-mirror-of-wilderness action");
        // get target character game role
        final Optional<GameRoleEnum> mayGameRole = controller.decodePlayerGameRoleByCharacter(targetCharacter);
        final boolean success;
        if (mayGameRole.isEmpty()) {
            // npc --> nothing happens
            // condition)
            success = false;
        } else if (mayGameRole.get() == connection.getGameRole()) {
            // own faction --> swap ip
            success = true;
        } else {
            // opposite faction
            success = guard.flip(character, matchconfig.getMirrorSwapChance());
            // if success remove gadget from inventory
            removeGadgetOnSuccess(character, mirrorOfWilderness, success);
        }
        // if success then swap ip
        if (success) {
            swapIpOfCharactersAndNotifyStatistics(character, targetCharacter);
        }
        // decrement ap
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), success, targetCharacter.getCoordinates(),
                GadgetEnum.MIRROR_OF_WILDERNESS));
    }

    private void removeGadgetOnSuccess(final Character character, final Gadget mirrorOfWilderness,
            final boolean success) {
        if (success) {
            character.removeGadget(mirrorOfWilderness);
        }
    }

    private void swapIpOfCharactersAndNotifyStatistics(final Character character, final Character targetCharacter) {
        magpie.writeDebug("Swapping ips for characters " + character + " and " + targetCharacter + ".", TURN_TXT);
        final int oldCharacterIp = character.getIp();
        final int oldTargetCharacterIp = targetCharacter.getIp();
        // how much more ip the current character has than the target one
        final int diff = oldCharacterIp - oldTargetCharacterIp;
        // first process the swap for the current character
        final Optional<GameRoleEnum> mayCharacterRole = controller.decodePlayerGameRoleByCharacter(character);
        if (mayCharacterRole.isPresent()) {
            updateIpSoThatTheyAreBalanced(character, oldTargetCharacterIp, diff, mayCharacterRole.get());
        }
        // then for the target character
        final Optional<GameRoleEnum> mayTargetCharacterRole = controller
                .decodePlayerGameRoleByCharacter(targetCharacter);
        if (mayTargetCharacterRole.isPresent()) {
            if (diff >= 0) {
                // target character has less than the current character --> add (again because
                // of wiretap)
                controller.getStatisticsProvider().addIP(mayTargetCharacterRole.get(), diff);
                targetCharacter.addIp(diff);
            } else {
                // target character has more than the current character --> remove
                controller.getStatisticsProvider().removeIP(mayTargetCharacterRole.get(), Math.abs(diff));
                targetCharacter.setIp(oldCharacterIp);
            }
        }
    }

    private void updateIpSoThatTheyAreBalanced(final Character character, final int oldTargetCharacterIp,
            final int diff, final GameRoleEnum characterRole) {
        if (diff >= 0) {
            // has more than the target character --> remove
            controller.getStatisticsProvider().removeIP(characterRole, diff);
            character.setIp(oldTargetCharacterIp);
        } else {
            // has less --> add (especially important because of the wiretap stuff)
            controller.getStatisticsProvider().addIP(characterRole, Math.abs(diff));
            character.addIp(diff);
        }
    }

    /**
     * Can be used on
     * <ul>
     * <li>a bar table with cocktail: character will pick up the cocktail if not
     * already holding one</li>
     * <li>a neighbour character: will pour the cocktail over a character which adds
     * the {@link PropertyEnum#CLAMMY_CLOTHES} property to the target character.
     * This can be avoided by cocktail dodge chance as specified in
     * {@link Matchconfig}.</li>
     * <li>self: will drink the cocktail. If the cocktail is poisoned, the cocktail
     * hp as specified in {@link Matchconfig} will be dealen as damage, otherwise
     * added as hp.</li>
     * </ul>
     * Other uses are forbidden.
     * 
     * @param connection The connection (used for determining faction)
     * @param action     The gadget action to be performed
     * @param character  The character that performs the action
     * @return List of operations that were executed
     * @throws HandlerException If any error occurs during execution
     */
    @Override
    protected List<BaseOperation> cocktail(final NttsClientConnection connection, final GadgetAction action,
            final Character character) throws HandlerException {
        Point target = action.getTarget();

        final List<BaseOperation> res = new LinkedList<>();
        final boolean success;
        // get target
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (mayTargetCharacter.isPresent()) {
            // check if holding cocktail
            guard.assureValidGadget(GadgetEnum.COCKTAIL, character);
            // pour or drink
            final Character targetCharacter = mayTargetCharacter.get();
            final Optional<Character> resTargetCharacter = cocktailOnCharacter(connection, targetCharacter, character,
                    target, res);
            if (resTargetCharacter.isPresent()) {
                success = true;
                target = resTargetCharacter.get().getCoordinates();
            } else {
                success = false;
            }

        } else {
            success = cocktailOnField(character, target);
        }
        // decrement ap
        character.removeAp();
        res.add(new GadgetAction(character.getCharacterId(), success, target, GadgetEnum.COCKTAIL));
        return res;
    }

}