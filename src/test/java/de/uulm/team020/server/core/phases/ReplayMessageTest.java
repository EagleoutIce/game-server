package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.ReplayMessage;
import de.uulm.team020.networking.messages.RequestReplayMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;

/**
 * Test the handling of replay messages after the game has ended
 */
public class ReplayMessageTest {

    public static Stream<Arguments> generate_regularReplay() {
        return Stream.of(Arguments.arguments(true), Arguments.arguments(false));
    }

    @ParameterizedTest
    @Order(1)
    @DisplayName("[Replay] Regular Replay")
    @MethodSource("generate_regularReplay")
    public void test_regularReplay(boolean hasReplay) throws IOException {
        StoryBoard storyBoard = new StoryBoard(
                Configuration.buildFromArgs("--defaults", "--x", "replay", String.valueOf(hasReplay)));
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/short-matchconfig.match");
        storyBoard.set("#charsP1", 4);
        storyBoard.set("#charsP2", 4);
        storyBoard.preset(Presets.COMPLETE_DRAFTING_CHARACTERS).preset(Presets.SKIP_MAIN);
        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");
        saphira.awaitForMessageType(MessageTypeEnum.STATISTICS, 4, 250);

        StatisticsMessage msg = MessageContainer.getMessage(saphira.getMessages().getLast());
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_RANDOMNESS, msg.getReason(),
                "Winner shall be determined by randomness as both got the same amount of characters.");
        Assertions.assertEquals(hasReplay, msg.hasReplay(), "Shall offer replay as stated.");

        // give us this replay
        RequestReplayMessage request = new RequestReplayMessage(saphira.getClientId());
        saphira.send(request.toJson());

        if (hasReplay) {
            saphira.awaitForMessageType(MessageTypeEnum.REPLAY, 4, 250);
            ReplayMessage replay = MessageContainer.getMessage(saphira.getMessages().getLast());
            Assertions.assertEquals(MessageTypeEnum.REPLAY, replay.getType(), "Should be allowed as replay available.");
            Assertions.assertTrue(replay.getMessages().length > 0,
                    "Should have recorded at least one message, but:" + replay);
        } else {
            saphira.awaitForMessageType(MessageTypeEnum.ERROR, 4, 250);
            ErrorMessage error = MessageContainer.getMessage(saphira.getMessages().getLast());
            Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, error.getReason(),
                    "Should be denied as no replay .");
        }
    }

    @ParameterizedTest
    @Order(2)
    @DisplayName("[Replay] Regular Replay with different winner")
    @MethodSource("generate_regularReplay")
    public void test_regularReplayWinner(boolean hasReplay) throws IOException {
        StoryBoard storyBoard = new StoryBoard(
                Configuration.buildFromArgs("--defaults", "--x", "replay", String.valueOf(hasReplay)));
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/short-matchconfig.match");
        storyBoard.set("#charsP1", 4);
        storyBoard.set("#charsP2", 3);
        storyBoard.preset(Presets.COMPLETE_DRAFTING_CHARACTERS).preset(Presets.SKIP_MAIN);
        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");
        saphira.awaitForMessageType(MessageTypeEnum.STATISTICS, 4, 250);

        StatisticsMessage msg = MessageContainer.getMessage(saphira.getMessages().getLast());
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_IP, msg.getReason(),
                "Winner shall be determined by ip as p1 has more characters.");
        Assertions.assertEquals(saphira.getClientId(), msg.getWinner(), "Winner shall be p1 has more characters.");
        Assertions.assertEquals(hasReplay, msg.hasReplay(), "Shall offer replay as stated.");

        // give us this replay
        RequestReplayMessage request = new RequestReplayMessage(saphira.getClientId());
        saphira.send(request.toJson());

        if (hasReplay) {
            saphira.awaitForMessageType(MessageTypeEnum.REPLAY, 4, 250);
            ReplayMessage replay = MessageContainer.getMessage(saphira.getMessages().getLast());
            Assertions.assertEquals(MessageTypeEnum.REPLAY, replay.getType(), "Should be allowed as replay available.");
            Assertions.assertTrue(replay.getMessages().length > 0,
                    "Should have recorded at least one message, but:" + replay);
        } else {
            saphira.awaitForMessageType(MessageTypeEnum.ERROR, 4, 250);
            ErrorMessage error = MessageContainer.getMessage(saphira.getMessages().getLast());
            Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, error.getReason(),
                    "Should be denied as no replay .");
        }
    }

    @RepeatedTest(8)
    @Order(2)
    @DisplayName("[Replay] Regular Replay at the wrong time")
    public void test_regularReplayWrongTime() throws IOException {
        StoryBoard storyBoard = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "replay", "true"));
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/short-matchconfig.match");
        storyBoard.set("#charsP1", 4);
        storyBoard.set("#charsP2", 3);
        storyBoard.preset(Presets.COMPLETE_DRAFTING_CHARACTERS);
        // play some rounds (indentation just for visual)
        storyBoard.execute("ITER 2:");
        storyBoard.execute("    ITER ${#playerCharacters}:");
        storyBoard.execute("        BREAK_EQ ${gameOver} true");
        storyBoard.execute("        OPERATION random retire <none>");
        storyBoard.execute("    RETI");
        storyBoard.execute("RETI");

        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");

        // Game did not end at this point
        // give us this replay
        RequestReplayMessage request = new RequestReplayMessage(saphira.getClientId());
        saphira.send(request.toJson());
        saphira.awaitForMessageType(MessageTypeEnum.ERROR, 4, 250);
        ErrorMessage error = MessageContainer.getMessage(saphira.getMessages().getLast());
        Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, error.getReason(), "Should be denied as no replay .");
    }

}