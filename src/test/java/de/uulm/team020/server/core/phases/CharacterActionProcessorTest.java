package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;

import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;

class CharacterActionProcessorTest {

    @RepeatedTest(15)
    @Order(1)
    @DisplayName("[Action] Spy")
    void test_spy() throws IOException {
        // Magpie.setDefaultLevel(Level.ALL);
        BackstoryBuilder builder = spyBackstory(1.0, 1).unlock();
        StoryBoard board = builder.build();
        board.eraseAllClientMemories();
        Story story = board.build();
        // now, lets make a move, mstr bond, observe on saphira!
        DummyClient walter = story.getClient("walter");

        // saphira shall be npc
        builder.transferOwnershipToNeutral("Saphira");

        // spy her
        GameOperationMessage operation = new GameOperationMessage(walter.getClientId(),
                new Operation(OperationEnum.SPY_ACTION, builder.getCharacter("James Bond").getCharacterId(),
                        builder.getCharacter("Saphira").getCoordinates()));
        walter.send(operation.toJson());

        // Now we want to check, that we have gotton exactly one new key
        final String msg = walter.getMessages().getFirst();
        GameStatusMessage status = MessageContainer.getMessage(msg);
        Assertions.assertEquals(1, status.getState().getMySafeCombinations().size(),
                "Should have gotten one new safe key, the one, but: " + status.getState().getMySafeCombinations());
        // TODO: there should be more tests here
    }

    private BackstoryBuilder spyBackstory(double chance, int maySkip) throws IOException {
        StoryBoard board = new StoryBoard();
        board.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/classifier/cross.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(board, "Waffel");
        builder.matchconfigUpdate("spySuccessChance", chance);
        builder.serverConfigUpdate("npcHasRightKeyOnSpyChance", chance);
        builder.serverConfigUpdate("npcHasAtLeastOneKey", true);
        builder.serverConfigUpdate("npcSecretMaySkip", maySkip);
        builder.addClient("walter", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("saphira", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments("James Bond", List.of(), "Saphira", List.of(), 4, 4);
        builder.prepareMainPhase();
        builder.removeAllPropertiesFor("James Bond");
        builder.addPropertyFor("James Bond", PropertyEnum.OBSERVATION);
        builder.placeCharacter("James Bond", new Point(1, 2));
        builder.placeCharacter("<cat>", new Point(2, 2));
        builder.injectNextRoundOrder("James Bond", "Saphira");
        builder.startMainPhase();
        builder.assureFieldFree(new Point(1, 3));
        builder.placeCharacter("Saphira", new Point(1, 3));

        board.sleep(5);
        return builder;
    }
}