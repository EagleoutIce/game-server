package de.uulm.team020.server.core.dummies.story.helper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.GambleAction;
import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.PropertyAction;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.exceptions.PointParseException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryArgumentException;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.exceptions.StorySillyException;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationException;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationExceptionType;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;

/**
 * May be used by the {@link Story} to construct operation messages in the game.
 * <p>
 * This class may gain access to the full story if this is to be desired.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/13/2020
 * 
 * @since 1.1
 */
public class StoryGameOperationGenerator {

    private static final String IN_TXT = " in: ";
    private static final String STORY_EVENT_TXT = "Story-Event, sent by: ";
    private static final String ARGUMENTS_TXT = "The arguments: ";
    private Story story;

    /**
     * Produce a new Story Operation Generate which is used to produce
     * operation-messages during the main game phase
     * 
     * @param story The story that controls this story-operation generator
     */
    public StoryGameOperationGenerator(Story story) {
        this.story = story;
    }

    /**
     * This will find the client going by this name <i>or</i> the one that is
     * currently active if you provide {@link Story#RANDOM}.
     * 
     * @param name The name of the client you want
     * 
     * @return The client requested, if it is the currently active one. An empty
     *         otherwise
     */
    public Optional<DummyClient> decodeActiveClient(final String name) {
        final MainGamePhaseController mainGamePhaseController = story.getController().getMainGamePhaseController();
        if (name.equalsIgnoreCase(Story.RANDOM)) { // random handling
            UUID waitingFor = mainGamePhaseController.getPlayerWaiting();
            if (mainGamePhaseController.getGameFieldController().isGameOver()) {
                // Game already ended
                return Optional.empty();
            }
            if (waitingFor == null) {
                throw new StorySillyException(
                        "You used random to get the operation target - there was none to be found in a given amount of time - currently this features isn't really implemented!");
            }
            return Optional.of(story.getClient(waitingFor));
        }
        return Optional.of(story.getClient(name));
    }

    /**
     * Produce a game operation message
     * 
     * @param client    The client to produce the operation for
     * @param operation The operation desired
     * @param key       The key that was given and should be used for further
     *                  parsing
     * @param arguments The arguments of the key -- may be ignored
     * 
     * @return The produced operation message
     */
    public GameOperationMessage buildGameOperation(final DummyClient client, final OperationEnum operation,
            final String key, final String arguments) {

        // acquire Data
        GameFieldController gfController = story.getGameFieldController();
        NttsClientConnection connection = client.getConnection();
        final String name = client.getName();

        GameOperationMessage gameOperationMessage = null;

        // await for active character
        // TODO: we have to restrict the sendable types
        switch (operation) {
            case MOVEMENT:
                gameOperationMessage = createMovementOperation(gfController, connection, name, key, arguments);
                break;
            case GAMBLE_ACTION:
                gameOperationMessage = createGambleOperation(gfController, connection, name, key, arguments);
                break;
            case SPY_ACTION:
                gameOperationMessage = createSpyOperation(gfController, connection, name, key, arguments);
                break;
            case GADGET_ACTION:
                gameOperationMessage = createGadgetAction(gfController, connection, name, key, arguments);
                break;
            case PROPERTY_ACTION:
                gameOperationMessage = createPropertyAction(gfController, connection, name, key, arguments);
                break;
            case RETIRE:
                // Just drop the argument
                gameOperationMessage = new GameOperationMessage(connection.getClientId(),
                        new Operation(OperationEnum.valueOf(key.toUpperCase()),
                                gfController.getActiveCharacter().getCharacterId(),
                                gfController.getActiveCharacter().getCoordinates()));
                break;
            default:
                throw new StoryException("The operation: " + operation + " is currently not supported.");
        }

        return gameOperationMessage;
    }

    private GameOperationMessage createMovementOperation(GameFieldController gfController,
            NttsClientConnection connection, String name, String key, String arguments) {

        final Character character = gfController.getActiveCharacter();
        final FieldMap map = gfController.getMap();

        // random movement
        if (arguments.equalsIgnoreCase(Story.RANDOM)) {

            return createRandomMovementOperationWithRandomValidTarget(connection, name, character, map);
        }
        GameOperationMessage gameOperationMessage = null;

        try {
            final Point goal = Point.fromString(arguments, character.getCoordinates());
            gameOperationMessage = new GameOperationMessage(connection.getClientId(), new Movement(character, goal),
                    STORY_EVENT_TXT + name);
        } catch (PointParseException ex) {
            throw new StoryArgumentException(ex.getMessage() + IN_TXT + arguments);
        }
        return gameOperationMessage;
    }

    private GameOperationMessage createRandomMovementOperationWithRandomValidTarget(NttsClientConnection connection,
            String name, final Character character, final FieldMap map) {
        // get a random jet possible field for the character to move to
        List<Point> possibleNeighbours = Arrays.stream(character.getCoordinates().getNeighbours())
                .filter(p -> map.getSpecificField(p).isWalkable()).collect(Collectors.toList());
        if (possibleNeighbours.isEmpty()) {
            throw new StoryOperationException(StoryOperationExceptionType.MOVEMENT_NO_TARGET,
                    "There is no target field the character can move to, as none of them are walkable");
        }
        return new GameOperationMessage(connection.getClientId(),
                new Movement(character, RandomHelper.rndPick(possibleNeighbours)), STORY_EVENT_TXT + name + " for: "
                        + character.getName() + ". The target was chosen by the random-token.");
    }

    private GameOperationMessage createGambleOperation(GameFieldController gfController,
            NttsClientConnection connection, String name, String key, String arguments) {

        // expected argument-format: pointX/pointY,stake
        final String[] args = arguments.split(",\\s*stake:\\s*");
        if (args.length != 2) {
            throw new StoryArgumentException(ARGUMENTS_TXT + key
                    + " is faulty, as it is not valid format '<point>, stake: <stake>': " + arguments);
        }
        final Character character = gfController.getActiveCharacter();

        GameOperationMessage gameOperationMessage = null;

        try {
            final Point goal = Point.fromString(args[0], character.getCoordinates());
            gameOperationMessage = new GameOperationMessage(connection.getClientId(),
                    new GambleAction(character.getCharacterId(), goal, Integer.parseInt(args[1])),
                    STORY_EVENT_TXT + name);
        } catch (PointParseException exPoint) {
            throw new StoryArgumentException(exPoint.getMessage() + IN_TXT + arguments);
        } catch (NumberFormatException exNumber) {
            throw new StoryArgumentException("The stake data is faulty in: " + arguments + " as not valid (integer).'");
        }
        return gameOperationMessage;
    }

    private GameOperationMessage createSpyOperation(GameFieldController gfController, NttsClientConnection connection,
            String name, String key, String arguments) {

        final Character character = gfController.getActiveCharacter();

        GameOperationMessage gameOperationMessage = null;

        try {
            final Point goal = Point.fromString(arguments, character.getCoordinates());
            gameOperationMessage = new GameOperationMessage(connection.getClientId(),
                    new Operation(OperationEnum.SPY_ACTION, character.getCharacterId(), goal), STORY_EVENT_TXT + name);
        } catch (PointParseException ex) {
            throw new StoryArgumentException(ex.getMessage() + IN_TXT + arguments);
        }
        return gameOperationMessage;
    }

    private GameOperationMessage createGadgetAction(GameFieldController gfController, NttsClientConnection connection,
            String name, String key, String arguments) {

        // expected argument-format: pointX/pointY,stake
        final String[] args = arguments.split(",\\s*gadget:\\s*");
        if (args.length != 2) {
            throw new StoryArgumentException(ARGUMENTS_TXT + key
                    + " is faulty, as it is not valid format '<point>, gadget: <gadget>': " + arguments);
        }
        final Character character = gfController.getActiveCharacter();

        GameOperationMessage gameOperationMessage = null;

        try {
            final Point goal = Point.fromString(args[0], character.getCoordinates());
            // decode the gadget
            final GadgetEnum gadget = GadgetEnum.valueOf(args[1]);
            gameOperationMessage = new GameOperationMessage(connection.getClientId(),
                    new GadgetAction(character.getCharacterId(), goal, gadget), STORY_EVENT_TXT + name);
        } catch (PointParseException exPoint) {
            throw new StoryArgumentException(exPoint.getMessage() + IN_TXT + arguments);
        } catch (IllegalArgumentException exArg) {
            throw new StoryArgumentException(
                    exArg.getMessage() + ". Picked no valid gadget for: " + args[1] + IN_TXT + arguments);
        }
        return gameOperationMessage;
    }

    private GameOperationMessage createPropertyAction(GameFieldController gfController, NttsClientConnection connection,
            String name, String key, String arguments) {

        // expected argument-format: pointX/pointY,stake
        final String[] args = arguments.split(",\\s*property:\\s*");
        if (args.length != 2) {
            throw new StoryArgumentException(ARGUMENTS_TXT + key
                    + " is faulty, as it is not valid format '<point>, property: <property>': " + arguments);
        }
        final Character character = gfController.getActiveCharacter();

        GameOperationMessage gameOperationMessage = null;

        try {
            final Point goal = Point.fromString(args[0], character.getCoordinates());
            // decode the gadget
            final PropertyEnum property = PropertyEnum.valueOf(args[1]);
            gameOperationMessage = new GameOperationMessage(connection.getClientId(),
                    new PropertyAction(character.getCharacterId(), goal, property), STORY_EVENT_TXT + name);
        } catch (PointParseException exPoint) {
            throw new StoryArgumentException(exPoint.getMessage() + IN_TXT + arguments);
        } catch (IllegalArgumentException exArg) {
            throw new StoryArgumentException(
                    exArg.getMessage() + ". Picked no valid property for: " + args[1] + IN_TXT + arguments);
        }
        return gameOperationMessage;
    }
}