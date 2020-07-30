package de.uulm.team020.server.game.phases.main.helper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;

/**
 * This class does nothing magical - it provides some helper that will process
 * the next move for an given npc. It can be customized (and even NPC-Dependent)
 * as desired but as it is not really vital to the game play it tries to be as
 * simple as possible for know - furthermore it is not intended for injection
 * (at the moment).
 * 
 * @author Florian Sihler
 * @version 1.0, 05/04/2020
 */
public class NpcMovementProvider {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");

    private GameFieldController controller;

    /**
     * Construct a new movement provider for npc
     * 
     * @param controller The main game field controller to work on
     */
    public NpcMovementProvider(GameFieldController controller) {
        this.controller = controller;
    }

    // private movement provider for every character?

    /**
     * This method will enforce the npc movement for an given npc it can gain any
     * amount of complexity by handling movement actions via sub-providers.
     * 
     * @param character The character to do the move
     * @param iteration The iteration this call appeared in for this character,
     *                  might be used for further handlings
     * 
     * @return Operations performed - as if there was another client requesting
     *         them.
     */
    public List<BaseOperation> performOperation(Character character, final int iteration) {
        if (character.hasApLeft(controller.getMap())) {
            // if has moledie (and could throw :D) - throw it
            List<BaseOperation> couldThrowMoledie = enforcedActionOnMoledie(character);
            if (!couldThrowMoledie.isEmpty())
                return couldThrowMoledie; // so we did throw
        }

        // here you can specify more complex logic if desired
        if (controller.getConfiguration().getServerConfig().npcShouldMove() && character.hasMpLeft()) {
            List<BaseOperation> couldWalk = randomWalk(character);
            if (!couldWalk.isEmpty()) {
                return couldWalk; // so we did walk
            }
        }

        // if there is nothing left for this character -- retire
        character.retire();
        return List
                .of(new Operation(OperationEnum.RETIRE, character.getCharacterId(), true, character.getCoordinates()));
    }

    private List<BaseOperation> enforcedActionOnMoledie(Character character) {
        // Check fo moledie:
        if (!character.getGadgetType(GadgetEnum.MOLEDIE).isPresent()) {
            // If there is none we are free! Letta do what whe-se likes.
            return Collections.emptyList();
        }
        // If the character has a moledie he has to rethrow it:
        Optional<Point> mayTarget = findRandomMoledieTarget(character.getCoordinates(), character);
        if (!mayTarget.isPresent()) {
            return Collections.emptyList();
        }
        final Point target = mayTarget.get();

        magpie.writeInfo("The npc " + character + " will toss the moledie to: " + target, "NPC");
        // toss the moledie
        Character hit = controller.getActionProcessor().getCharacterGadgetActionProcessor().tossMoledie(character,
                target);
        magpie.writeDebug("Npc-Toss by: " + character + " to Target: " + target + " hit: " + hit, "NPC");
        character.removeAp();
        return List.of(new GadgetAction(character.getCharacterId(), true, target, GadgetEnum.MOLEDIE));
    }

    /**
     * This will find a valid moledie spot in range. The 'seed' field is expected to
     * be walkable.
     * 
     * @param seed Seed to start searching from
     * @return Random but yet valid target, if it exists.
     */
    private Optional<Point> findRandomMoledieTarget(Point seed, Character character) {
        final Optional<List<Point>> mayPossibleTargets = controller.getPossibleMoledieTargets(seed);
        if (mayPossibleTargets.isEmpty()) {
            return Optional.empty();
        } else {
            List<Point> possibleTargets = mayPossibleTargets.get();
            Point target = controller.getRandomController().requestPoint(character.getName(), possibleTargets,
                    character.getCoordinates(), RandomOperation.NPC_MOLEDIE_TARGET);
            return Optional.of(target);
        }
    }

    private boolean isOnFieldAndWalkable(final FieldMap map, final Point p) {
        Field field = map.getSpecificField(p);
        return field != null && field.isWalkable();
    }

    private List<BaseOperation> randomWalk(Character character) {
        final FieldMap map = controller.getMap();
        List<Point> possibleNeighbours = Arrays.stream(character.getCoordinates().getNeighbours()) // get all neighbours
                .filter(p -> isOnFieldAndWalkable(map, p)).collect(Collectors.toList());
        if (possibleNeighbours.isEmpty()) { // we dont care
            return Collections.emptyList();
        }

        Point current = character.getCoordinates();
        Point target = controller.getRandomController().requestPoint(character.getName(), possibleNeighbours, current,
                RandomOperation.NPC_MOVEMENT);
        try {
            controller.getActionProcessor().processMoveCheckGameEnd(null, character.getCharacterId(), current, target);
        } catch (GameFieldOperationException ex) {
            magpie.writeInfo("Movement failed for: " + character + " from: " + current + " to: " + target, "NPC-Move");
            magpie.writeException(ex, "NPC-Move");
            return Collections.emptyList();
        }

        return List.of(new Movement(character.getCharacterId(), target, true, current));
    }

}