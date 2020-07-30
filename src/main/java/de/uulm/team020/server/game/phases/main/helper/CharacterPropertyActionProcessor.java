package de.uulm.team020.server.game.phases.main.helper;

import java.util.List;
import java.util.Optional;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.PropertyAction;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.main.CharacterActionProcessor;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;

/**
 * Child class for the {@link CharacterActionProcessor} which is deemed to
 * perform property actions. It may use the {@link CharacterActionGuard} to
 * shield the operations. All the operations will only check property-specific
 * constraints - it is up to the {@link CharacterActionProcessor} to check if
 * the character has enough ap, can perform an action, ...
 * 
 * @author Florian Sihler
 * @version 1.0, 05/04/2020
 */
public class CharacterPropertyActionProcessor extends AbstractActionProcessor {

    /**
     * Create the processor, it will operate on the given controller
     * 
     * @param gameFieldController The gameFieldController to use
     */
    public CharacterPropertyActionProcessor(GameFieldController gameFieldController) {
        super(gameFieldController);
    }

    /**
     * Process a property action for a specific character. Will not do anything if
     * there is an exception escalated upwards.
     * 
     * @param connection The connection that requested the operation
     * @param action     The action that was requested
     * @param character  The character that requested the operation
     * 
     * @return The operations to be used in the game-status report
     * 
     * @throws HandlerException If there is an error on performing the operation.
     */
    public List<BaseOperation> processPropertyAction(NttsClientConnection connection, PropertyAction action,
            Character character) throws HandlerException {
        switch (action.getProperty()) {
            case BANG_AND_BURN:
                return bangAndBurn(connection, action, character);
            case OBSERVATION:
                return observation(connection, action, character);
            default:
                throw new IllegalMessageException("The property: " + action.getProperty() + " cannot be used.");
        }
    }

    /**
     * Can be used on a neighbour roulette-table to destroy/disable it.
     * 
     * @param connection Connection to be used for faction identification
     * @param action     The bang-and-burn-action will not be guarded extra
     * @param character  The character that executes the operation
     * @return The operations that were executed
     * 
     * @throws HandlerException If there is an error on performing the operation.
     */
    protected List<BaseOperation> bangAndBurn(NttsClientConnection connection, PropertyAction action,
            Character character) throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        final Field targetField = guard.assureValidNeighbourField(target, character.getCoordinates(), map,
                "BangAndBurn");

        // Has to be Roulette Table
        if (targetField.getState() != FieldStateEnum.ROULETTE_TABLE) {
            throw new IllegalMessageException(
                    "Bang and Burn has to be used on a Roulette Table, but the target Field: " + target + " is none.");
        }

        magpie.writeInfo("Destroying Roulette-Table at: " + targetField + " at: " + target, TURN_TXT);
        targetField.setDestroyed(true);

        character.removeAp();
        // Bang and burn will not fail
        return List.of(new PropertyAction(character.getCharacterId(), true, target, action.getProperty()));
    }

    /**
     * Can be used on a person in line of sight. Can succeed by chance and will
     * reveal the faction membership of the garget.
     * 
     * @param connection Connection to be used for faction identification
     * @param action     The bang-and-burn-action will not be guarded extra
     * @param character  The character that executes the operation
     * @return The operations that were executed
     * 
     * @throws HandlerException If there is an error on performing the operation.
     */
    protected List<BaseOperation> observation(NttsClientConnection connection, PropertyAction action,
            Character character) throws HandlerException {
        final Point target = action.getTarget();
        final FieldMap map = controller.getMap();
        // assure target Field is visible
        guard.assureValidFieldInLineOfSight(target, character.getCoordinates(), map, "Observation");

        // Get Character on target field
        Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        if (!mayTargetCharacter.isPresent()) {
            throw new IllegalMessageException(
                    "You wanted to observe a character on Field: " + target + " but there is none.");
        }
        final Character targetCharacter = mayTargetCharacter.get();
        if (guard.isCatOrJanitor(targetCharacter)) {
            throw new IllegalMessageException(
                    "You cannot use observation on: " + targetCharacter + " as it is janitor/cat");
        }

        // flip on chance:
        // identify if we own the character:
        boolean observationSucceeded = observationCheck(connection, targetCharacter, character);
        boolean isEnemy = false;

        if (observationSucceeded) {
            magpie.writeInfo("Observation-Toss succeeded - identifying for target: " + targetCharacter, TURN_TXT);
            // has the target pocket-litter?
            if (targetCharacter.getGadgetType(GadgetEnum.POCKET_LITTER).isPresent()) {
                magpie.writeDebug("Target has pocket-litter!", TURN_TXT);
            } else {
                // identify correctly
                switch (connection.getGameRole()) {
                    case PLAYER_ONE:
                        isEnemy = controller.getPlayerTwoFaction().contains(targetCharacter);
                        break;
                    case PLAYER_TWO:
                        isEnemy = controller.getPlayerOneFaction().contains(targetCharacter);
                        break;
                    default:
                        magpie.writeError("Non-Player: " + connection + " wanted to observer: " + targetCharacter
                                + " with: " + character, TURN_TXT);
                }
            }
        }

        // decrement character's ap
        character.removeAp();
        return List.of(new PropertyAction(character.getCharacterId(), observationSucceeded, target,
                action.getProperty(), isEnemy));
    }

    private boolean observationCheck(final NttsClientConnection connection, final Character targetCharacter,
            final Character spyingCharacter) {
        // if the character is from us - it should not succeed
        try {
            if (guard.isOneOfMine(connection, targetCharacter)) {
                magpie.writeDebug("Failing for one of mine: " + targetCharacter, TURN_TXT);
                return false;
            }
        } catch (GameFieldOperationException ignored) {
            magpie.writeError("Non-Player: " + connection + " wanted to observer: " + targetCharacter + " with: "
                    + spyingCharacter, TURN_TXT);
        }

        // if it is not from the same team, toss the coin
        return guard.flip(spyingCharacter, matchconfig.getObservationSuccessChance());
    }

}