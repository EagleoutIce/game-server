package de.uulm.team020.server.game.phases.main.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.helper.pathfinding.Pathfinder;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;
import de.uulm.team020.server.game.phases.main.islands.IslandSpawner;

/**
 * Helps the {@link GameFieldController} by providing the actions for cat,
 * janitor and npc.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class GameFieldActionController {

    private static final String SERVER_TEXT = "Server";
    private static final String ROUND_TXT = "Round";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private final NpcMovementProvider npcMovementProvider;

    private GameFieldController controller;
    private Set<Character> characters;

    private Faction playerOneFaction;
    private Faction playerTwoFaction;

    public GameFieldActionController(GameFieldController controller, Set<Character> characters,
            Faction playerOneFaction, Faction playerTwoFaction) {
        this.controller = controller;
        this.npcMovementProvider = new NpcMovementProvider(controller);
        this.characters = characters;
        this.playerOneFaction = playerOneFaction;
        this.playerTwoFaction = playerTwoFaction;
    }

    // Cat and Janitor actions
    public void catAction() {
        magpie.writeInfo("Performing cat turn", ROUND_TXT);
        // is there any character in the current island?`The cat is allowed to jump if
        // islands are used
        Point target = null;
        if (controller.getConfiguration().getServerConfig().useIslandsForSpawn()) {
            // connected to any player?
            if (!catCanReachPlayer()) {
                magpie.writeInfo("Cat will jump", ROUND_TXT);
                target = catJump();
                magpie.writeDebug("Cat jumped to: " + target, ROUND_TXT);
            } else {
                magpie.writeDebug("Cat cannot reach player and will move normally", ROUND_TXT);
            }
        }
        // perform a random move for the cat
        final Character cat = controller.getCat();
        final FieldMap map = controller.getMap();
        if (target == null) {
            Point[] possibles = cat.getCoordinates().getNeighbours();
            List<Point> freeNeighbours = Arrays.stream(possibles).filter(p -> p.isOnField(map.getField()))
                    .filter(p -> map.getSpecificField(p).isWalkable()).collect(Collectors.toList());
            target = cat.getCoordinates(); // if move fails, keep same point - should not happen
            if (freeNeighbours.isEmpty()) {
                magpie.writeWarning("The cat has no target field to move to! Placed for: " + cat, ROUND_TXT);
            } else {
                target = controller.getRandomController().requestPoint(RandomController.GLOBAL, freeNeighbours,
                        cat.getCoordinates(), RandomOperation.CAT_WALK_TARGET);
                cat.setMp(1); // cat is always allowed to move-it move-it
                try {
                    controller.getActionProcessor().processMoveRaw(null, cat, target);
                } catch (GameFieldOperationException ex) {
                    magpie.writeException(ex, ROUND_TXT);
                    // may rethrow?
                } finally {
                    cat.setMp(-1); // we just want to avoid errors
                }
            }
        }

        // Identify target field, is it holding the diamond_collar
        Gadget targetGadget = map.getSpecificField(target).getGadget();
        if (targetGadget != null && targetGadget.getGadget() == GadgetEnum.DIAMOND_COLLAR) {
            magpie.writeInfo("The cat stepped on the diamond-collar laying on field: " + target, ROUND_TXT);
            controller.gameOver(
                    List.of(getOperationOfCatAndJanitor(OperationEnum.CAT_ACTION, cat.getCharacterId(), true, target)));
            cat.setMp(-1); // we just want to avoid errors
            cat.retire(); // cat won't do something else
            return;
        }

        cat.retire(); // cat won't do something else

        controller.mayPublishStatus(
                List.of(getOperationOfCatAndJanitor(OperationEnum.CAT_ACTION, cat.getCharacterId(), true, target)));
    }

    private boolean catCanReachPlayer() {
        // if cat should not jump, we emualte this as if they are reachable
        if (!controller.getConfiguration().getServerConfig().catMayJump()) {
            return true;
        }
        final Point catPosition = controller.getCat().getCoordinates();
        // search if there is any player reachable by the current cat position
        boolean reachesPlayerOne = controller.getAllCharacters().stream()//
                // every character, that is owned by player one
                .filter(controller.getPlayerOneFaction()::contains)//
                .anyMatch(c -> controller.getPathfinder().connected(catPosition, c.getCoordinates()));
        boolean reachesPlayerTwo = controller.getAllCharacters().stream()//
                // every character, that is owned by player one
                .filter(controller.getPlayerTwoFaction()::contains)//
                .anyMatch(c -> controller.getPathfinder().connected(catPosition, c.getCoordinates()));
        // It is possible to fine tune this further - but currently not desired
        return reachesPlayerOne || reachesPlayerTwo;
    }

    private Point catJump() {
        final Character cat = controller.getCat();
        // acquire a new spawner, as it is not expensive
        IslandSpawner spawner = new IslandSpawner(controller.getIslands(), controller.getSafePositions());
        Set<Character> allPlayers = new HashSet<>(playerOneFaction);
        allPlayers.addAll(playerTwoFaction);
        int maxIsland = spawner.getMaxPlayerIsland(/* faction1 & faction2: */ allPlayers, characters);
        Point catJumpTarget;
        if (maxIsland >= 0) {
            catJumpTarget = spawner.spawnCharacter(maxIsland, cat,
                    characters.stream().map(Character::getCoordinates).collect(Collectors.toSet()));
        } else {
            // just give her a random new position
            spawner.spawnCharacters(Set.of(cat), characters);
            catJumpTarget = cat.getCoordinates();
        }
        // Produce the movement of the cat:
        return catJumpTarget;
    }

    public void janitorAction() {
        magpie.writeDebug("Janitor will move...", ROUND_TXT);
        final FieldMap map = controller.getMap();
        // we do not mind performing a bfs routine to get the closest one, we will
        // perform an a* for all exfiltrated using the flying-pathfinder
        Pathfinder<Field> janitorFinder = new Pathfinder<>(map, true, true);
        int shortest = -1;
        Character target = null;
        final Character cat = controller.getCat();
        final Character janitor = controller.getJanitor();
        for (Character character : this.characters) {
            if (character.equals(janitor) || character.equals(cat))
                // shouldn't remove himself or the cat!
                continue;
            Path targetPath = janitorFinder.findPath(janitor.getCoordinates(), character.getCoordinates(), false);
            if (shortest < 0 || targetPath.size() < shortest) { // update minima
                shortest = targetPath.size();
                target = character;
            }
        }

        magpie.writeInfo("Janitor removes character: " + target, ROUND_TXT);
        this.characters.remove(target);
        // maybe remove from the factions as well:
        // this.playerOneFaction.remove(target)
        // this.playerTwoFaction.remove(target)

        // janitor jumps to free target point

        final Point coordinates = target == null ? janitor.getCoordinates() : target.getCoordinates();

        janitor.setCoordinates(coordinates);
        // no characters left? => game over
        if (this.characters.size() == 2) { // only janitor and cat left
            magpie.writeInfo("No Characters left", ROUND_TXT);
            // game-over by no players left
            controller.gameOver(List.of(getOperationOfCatAndJanitor(OperationEnum.JANITOR_ACTION,
                    janitor.getCharacterId(), true, coordinates)));
        } else {
            // publish normal GameStatus
            controller.mayPublishStatus(List.of(getOperationOfCatAndJanitor(OperationEnum.JANITOR_ACTION,
                    janitor.getCharacterId(), true, coordinates)));
        }
    }

    private BaseOperation getOperationOfCatAndJanitor(OperationEnum op, UUID theoreticalId, boolean successful,
            Point coordinates) {
        if (controller.getConfiguration().getServerConfig().catAndJanitorHaveUuid()) {
            return new Operation(op, theoreticalId, successful, coordinates);
        } else {
            return new BaseOperation(op, successful, coordinates);
        }
    }

    /**
     * Perform an npc action, will not directly perform the publishing of the new
     * status
     * 
     * @param character The character to move
     * @param iteration Current iteration for this character
     * 
     * @return The operation performed -- currently only the retirement
     */
    public List<BaseOperation> npcAction(Character character, int iteration) {
        // Artificial wait:
        ServerConfiguration serverConfiguration = this.controller.getConfiguration().getServerConfig();
        // NPC WAIT CHOICE:
        final int artificialNpcWait = controller.getRandomController().requestRange(character.getName(),
                serverConfiguration.getNpcMinDelay(), serverConfiguration.getNpcMaxDelay() + 1,
                RandomOperation.NPC_WAIT_IN_MS);
        try {
            Thread.sleep(artificialNpcWait);
        } catch (InterruptedException ex) {
            magpie.writeError("On artificial npc movement wait of: " + artificialNpcWait, ROUND_TXT);
            magpie.writeException(ex, ROUND_TXT);
        }

        // we will consult the movement provider if we the server configuration allows
        // us to, and if the character can perform another move
        if (character.hasActionsLeft(controller.getMap())) {
            // consult the provider - he may have special movements for every character
            return npcMovementProvider.performOperation(character, iteration);
        } else {
            // retire and finish the movement
            magpie.writeDebug("NPC character: " + character.getName() + " will end its' turn and retire.", ROUND_TXT);
            // Perform retirement:
            character.retire();
            // Signal it for game status update and hope we do not get called again :D
            return List.of(
                    new Operation(OperationEnum.RETIRE, character.getCharacterId(), true, character.getCoordinates()));
        }
    }
}