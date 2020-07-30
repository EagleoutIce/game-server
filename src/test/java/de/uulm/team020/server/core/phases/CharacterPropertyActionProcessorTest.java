package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.PropertyAction;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationException;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationExceptionType;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Testing the property Actions
 *
 * @author Florian Sihler
 * @version 1.1, 05/04/2020
 */
class CharacterPropertyActionProcessorTest {

    private void nRandTurns(GameFieldController controller, StoryBoard taleBoard, int n, DummyClient waiter) {
        for (int j = 0; j < n; j++) {
            try {
                taleBoard.execute(StoryChapterType.OPERATION, "random", "movement", "random");
            } catch (StoryOperationException ex) {
                if (ex.getType() == StoryOperationExceptionType.MOVEMENT_NO_TARGET) {
                    // if this is the case there is no target to move to, we can ignore that
                } else {
                    throw ex; // rethrow
                }
            }
            taleBoard.execute(StoryChapterType.OPERATION, "random", "retire", "<nothing>");
            waiter.awaitForMessageType(MessageTypeEnum.GAME_STATUS, 4, 450, waiter.getMessageSize() + 1);
        }
    }

    @Test
    @Order(1)
    @DisplayName("[Action] Bang and Burn")
    void test_bangAndBurn() throws IOException {
        StoryBoard taleBoard = new StoryBoard().executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        final FieldMap map = tale.getController().getMainGamePhaseController().getGameFieldController().getMap();
        Field target = map.getSpecificField(new Point(6, 12));
        Assertions.assertFalse(target.isDestroyed(), "Should not be destroyed target: " + target);

        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayGustav = controller.decodeCharacterByName("The legendary Gustav");
        Assertions.assertTrue(mayGustav.isPresent(), "The Character 'The legendary Gustav' should be in the story!");
        final Character gustav = mayGustav.get();
        DummyClient walter = tale.getClient("walter");
        // skip until gustav can operate
        nRandTurns(controller, taleBoard, 2, walter);
        // No gustav can perform an operation
        // There is a roulette table on the right - we might engage there will be
        // further checks in the future :D
        GameOperationMessage operationMessage = new GameOperationMessage(walter.getClientId(),
                new PropertyAction(gustav.getCharacterId(), new Point(6, 12), PropertyEnum.BANG_AND_BURN));
        final int size = walter.getMessageSize();
        walter.send(operationMessage.toJson());
        walter.assureMessages(23, 250);
        // as walter is still allowed to do something with günther, we check for the
        // previous message before the next request game operation:
        String message = walter.getMessages().get(size);
        GameStatusMessage status = MessageContainer.getMessage(message);
        Assertions.assertEquals(1, status.getOperations().size(),
                "There shall be one operation to report. But: " + status.getOperations());
        PropertyAction[] actions = GameDataGson.fromJson(message, "operations", PropertyAction[].class);
        Assertions.assertEquals(1, actions.length,
                "There shall still be one operation to report. But: " + Arrays.toString(actions));
        PropertyAction mainAction = actions[0];
        Assertions.assertEquals(PropertyEnum.BANG_AND_BURN, mainAction.getProperty(),
                "Should be as performed: " + mainAction);
        Assertions.assertTrue(mainAction.getSuccessful(), "Successful: " + mainAction);
        // check, that the field is destroyed:
        Assertions.assertTrue(target.isDestroyed(), "Should be destroyed: " + mainAction + " target: " + target);
    }

    private BackstoryBuilder observationBackstory(double chance) throws IOException {
        StoryBoard board = new StoryBoard();
        board.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(board, "Waffel");
        builder.matchconfigUpdate("observationSuccessChance", chance);
        builder.addClient("walter", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("saphira", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments("James Bond", List.of(), "Saphira", List.of(), 4, 4);
        builder.prepareMainPhase();
        builder.removeAllPropertiesFor("James Bond");
        builder.addPropertyFor("James Bond", PropertyEnum.OBSERVATION);
        builder.placeCharacter("James Bond", new Point(1, 1));
        builder.placeCharacter("<cat>", new Point(2, 2));
        builder.injectNextRoundOrder("James Bond", "Saphira");
        builder.startMainPhase();
        builder.assureFieldFree(new Point(1, 3));
        builder.placeCharacter("Saphira", new Point(1, 3));

        board.sleep(5);
        return builder;
    }

    @RepeatedTest(30)
    @Order(2)
    @DisplayName("[Action] Observation valid")
    void test_observation() throws IOException {
        BackstoryBuilder builder = observationBackstory(1.0);
        StoryBoard board = builder.build();
        board.eraseAllClientMemories();
        Story story = board.build();
        // now, lets make a move, mstr bond, observe on saphira!
        DummyClient walter = story.getClient("walter");

        GameOperationMessage operation = new GameOperationMessage(walter.getClientId(),
                new PropertyAction(builder.getCharacter("James Bond").getCharacterId(),
                        builder.getCharacter("Saphira").getCoordinates(), PropertyEnum.OBSERVATION));
        walter.send(operation.toJson());

        // Get Game-status on first position
        final String msg = walter.getMessages().getFirst();
        GameStatusMessage status = MessageContainer.getMessage(msg);
        // get correct operation:
        PropertyAction[] actions = status.getOperations().toArray(PropertyAction[]::new);
        Assertions.assertEquals(1, actions.length,
                "Should have gotten just one property action, but: " + Arrays.toString(actions));
        Assertions.assertTrue(actions[0].getSuccessful(),
                "Should have worked as enforced, but: " + Arrays.toString(actions));
        Assertions.assertTrue(actions[0].isEnemy(),
                "Saphira is an enemy without pocket litter, but: " + Arrays.toString(actions));
    }

    @RepeatedTest(60)
    @Order(2)
    @DisplayName("[Action] Observation fail")
    void test_observationFailChance() throws IOException {
        BackstoryBuilder builder = observationBackstory(0);
        StoryBoard board = builder.build();
        board.eraseAllClientMemories();
        Story story = board.build();
        // now, lets make a move, mstr bond, observe on saphira!
        DummyClient walter = story.getClient("walter");
        GameOperationMessage operation = new GameOperationMessage(walter.getClientId(),
                new PropertyAction(builder.getCharacter("James Bond").getCharacterId(),
                        builder.getCharacter("Saphira").getCoordinates(), PropertyEnum.OBSERVATION));
        walter.send(operation.toJson());

        // Get Game-status on first position
        final String msg = walter.getMessages().getFirst();
        MessageContainer.getMessage(msg); // assure that parsed correctly

        // get correct operation:
        PropertyAction[] actions = GameDataGson.fromJson(msg, "operations", PropertyAction[].class);
        Assertions.assertEquals(1, actions.length,
                "Should have gotten just one property action, but: " + Arrays.toString(actions));
        Assertions.assertFalse(actions[0].getSuccessful(),
                "Should have not worked as enforced, but: " + Arrays.toString(actions));
        Assertions.assertFalse(actions[0].isEnemy(),
                "Saphira is an enemy without pocket litter, but the observation should have failed: "
                        + Arrays.toString(actions));
    }

    @RepeatedTest(20)
    @Order(3)
    @DisplayName("[Action] Observation foggy")
    void test_observationFoggy() throws IOException {
        BackstoryBuilder builder = observationBackstory(1.0);
        Field foggyField = new Field(FieldStateEnum.FREE);
        foggyField.setFoggy(true);
        builder.changeField(new Point(1, 2), foggyField);
        StoryBoard board = builder.build();
        board.eraseAllClientMemories();
        Story story = board.build();
        // now, lets make a move, mstr bond, observe on saphira!
        DummyClient walter = story.getClient("walter");
        GameOperationMessage operation = new GameOperationMessage(walter.getClientId(),
                new PropertyAction(builder.getCharacter("James Bond").getCharacterId(),
                        builder.getCharacter("Saphira").getCoordinates(), PropertyEnum.OBSERVATION));
        walter.send(operation.toJson());

        // Get Game-status on first position
        final String msg = walter.getMessages().getFirst();
        ErrorMessage status = MessageContainer.getMessage(msg);
        // get correct operation:
        Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, status.getReason(),
                "Should be illegal as field was foggy, but: " + status);
        // saphira, please take the crown:
        DummyClient saphira = story.getClient("saphira");
        Assertions.assertEquals(1, saphira.getMessageSize(),
                "Saphira should only have one message, the statistics one as all others should have been erased, but: "
                        + saphira.getMessages().typeTraversal());
        StatisticsMessage statistics = MessageContainer.getMessage(saphira.getMessages().getLast());
        Assertions.assertEquals(saphira.getClientId(), statistics.getWinner(), "Saphira should have won");
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_KICK, statistics.getReason(),
                "Saphira should have won by kick of walter.");
    }

}