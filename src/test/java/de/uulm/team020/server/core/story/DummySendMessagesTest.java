package de.uulm.team020.server.core.story;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.GameLeaveMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.StrikeMessage;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.exceptions.ExpectationException;

public class DummySendMessagesTest {

    @Test
    @DisplayName("[Assertions] Test simple assertions with failure.")
    public void test_assertionsWithFailure() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        buffer.add(new HelloMessage("Walter", RoleEnum.AI).toJson());
        // Trigger an exception
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertTypes(MessageTypeEnum.GAME_STATUS));
    }

    private static Stream<Arguments> generate_typeAssertions() {
        return Stream.of(Arguments
                .arguments(new MessageContainer[] { new HelloMessage("X", RoleEnum.PLAYER),
                        new GameLeaveMessage(UUID.randomUUID()), new ItemChoiceMessage(UUID.randomUUID(), null,
                                GadgetEnum.MOLEDIE) },
                        MessageTypeEnum.HELLO, true, false, 3),
                Arguments.arguments(
                        new MessageContainer[] { new HelloMessage("X", RoleEnum.PLAYER),
                                new GameLeaveMessage(UUID.randomUUID()),
                                new ItemChoiceMessage(UUID.randomUUID(), null, GadgetEnum.NUGGET) },
                        MessageTypeEnum.GAME_LEAVE, true, true, 3),
                Arguments
                        .arguments(
                                new MessageContainer[] { new GameLeaveMessage(UUID.randomUUID()),
                                        new ItemChoiceMessage(UUID.randomUUID(), null, GadgetEnum.NUGGET) },
                                MessageTypeEnum.GAME_LEAVE, true, false, 2),
                Arguments.arguments(
                        new MessageContainer[] { new HelloMessage("X", RoleEnum.PLAYER),
                                new GameLeaveMessage(UUID.randomUUID()),
                                new ItemChoiceMessage(UUID.randomUUID(), null, GadgetEnum.BOWLER_BLADE) },
                        MessageTypeEnum.ITEM_CHOICE, true, true, 3),
                Arguments.arguments(
                        new MessageContainer[] { new HelloMessage("X", RoleEnum.PLAYER),
                                new GameLeaveMessage(UUID.randomUUID()),
                                new ItemChoiceMessage(UUID.randomUUID(), null, GadgetEnum.CHICKEN_FEED) },
                        MessageTypeEnum.GAME_STATUS, false, false, 3),
                Arguments.arguments(
                        new MessageContainer[] { new HelloMessage("X", RoleEnum.PLAYER),
                                new GameLeaveMessage(UUID.randomUUID()),
                                new ItemChoiceMessage(UUID.randomUUID(), null, GadgetEnum.JETPACK) },
                        MessageTypeEnum.GAME_OPERATION, false, false, 3),
                Arguments.arguments(new MessageContainer[0], MessageTypeEnum.ERROR, false, false, 0));
    }

    @ParameterizedTest
    @DisplayName("[Assertions] Test simple contains with failure.")
    @MethodSource("generate_typeAssertions")
    public void test_containsAssertions(MessageContainer[] data, MessageTypeEnum type, boolean expected,
            boolean expectedOnOffset, int expectedLength) {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        for (MessageContainer message : data) {
            buffer.add(message.toJson());
        }

        Assertions.assertEquals(expected, buffer.containsType(type),
                "Should be as desired for type: " + type + " and buffer: " + buffer);
        Assertions.assertEquals(expectedOnOffset, buffer.containsType(type, 1),
                "Should be as desired for type: " + type + " and buffer: " + buffer + " for offset: 1.");

        // Assert does throw nothing :D
        Assertions.assertDoesNotThrow(() -> buffer.assertLength(expectedLength),
                "Should be: " + expectedLength + " for buffer and does not throw anything with: " + buffer);
    }

    private static void dummyConsumer(String ignored) {
        // just to mock the signature for the next two tests
    }

    @Test
    @DisplayName("[Assertions] Test Length failure.")
    public void test_lengthFailure() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(3),
                "Should be empty and not three");
        Assertions.assertDoesNotThrow(() -> buffer.assertLength(0), "Should be empty for: " + buffer);

        buffer.add(new HelloMessage("Fred", RoleEnum.AI).toJson());
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(-1),
                "Should be 1 and not negative");
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(2), "Should be 1 and not 2");
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(3), "Should be 1 and not 3");

        Assertions.assertDoesNotThrow(() -> buffer.assertLength(1), "Should be 1 for: " + buffer);

        buffer.add(new GameLeaveMessage(UUID.randomUUID()).toJson());
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(-1),
                "Should be 2 and not negative");
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(1), "Should be 2 and not 1");
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLength(3), "Should be 2 and not 3");

        Assertions.assertDoesNotThrow(() -> buffer.assertLength(2), "Should be 2 for: " + buffer);
    }

    @Test
    @DisplayName("[Equals] Test equals contract (simple and on same).")
    public void test_equalsContract() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        DummySendMessagesBuffer buffer2 = new DummySendMessagesBuffer();

        // Equals on empty:
        Assertions.assertNotEquals(buffer, List.of(), "Buffers should be not equal if differ in type and empty");

        Assertions.assertEquals(buffer, buffer, "Buffers should be equal if identical");
        Assertions.assertEquals(buffer.hashCode(), buffer.hashCode(), "Buffers hashcode should be equal if identical");
        Assertions.assertEquals(buffer2, buffer2, "Buffers should be equal if identical");

        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if empty");
        Assertions.assertEquals(buffer.hashCode(), buffer2.hashCode(), "Buffers hashcode should be equal if empty");

        // simple add and equals checks
        HelloMessage message1 = new HelloMessage("Walter", RoleEnum.AI);
        HelloMessage message2 = new HelloMessage("Walter", RoleEnum.SPECTATOR);
        buffer.add(message1.toJson());
        buffer2.add(message1.toJson());

        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if same message");
        Assertions.assertEquals(buffer, buffer, "Buffers should be equal if identical");
        Assertions.assertEquals(buffer2, buffer2, "Buffers should be equal if identical");

        buffer2.add(message2.toJson());

        Assertions.assertNotEquals(buffer, buffer2, "Buffers should be not equal if second got another message");
        Assertions.assertNotEquals(buffer.hashCode(), buffer2.hashCode(),
                "Buffers hashcode should be not equal if differ");

        Assertions.assertEquals(buffer, buffer, "Buffers should be equal if identical");
        Assertions.assertEquals(buffer2, buffer2, "Buffers should be equal if identical");

        buffer.add(message2.toJson());

        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if same 2 messages");
        Assertions.assertEquals(buffer.hashCode(), buffer2.hashCode(), "Buffers hashcode should be equal if equals");

        Assertions.assertEquals(buffer, buffer, "Buffers should be equal if identical");
        Assertions.assertEquals(buffer2, buffer2, "Buffers should be equal if identical");

        buffer.add(message2.toJson());
        Assertions.assertNotEquals(buffer, buffer2, "Buffers should be not equal if second got doubled message");
        Assertions.assertEquals(buffer, buffer, "Buffers should be equal if identical");
        Assertions.assertEquals(buffer2, buffer2, "Buffers should be equal if identical");

        Assertions.assertNotEquals(buffer, List.of(), "Buffers should be not equal if differ in type");

        buffer2.add(message2.toJson());
        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if same 3 messages");

        // now we will add a consumer
        buffer.setConsumer(DummySendMessagesTest::dummyConsumer);

        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if differ in consumer");

        buffer2.add(message1.toJson());

        Assertions.assertNotEquals(buffer, buffer2, "Buffers should be not equal if differ in consumer and messages");

        buffer2.setConsumer(DummySendMessagesTest::dummyConsumer);
        Assertions.assertNotEquals(buffer, buffer2,
                "Buffers should be not equal if differ in content but same consumer");

        buffer.add(message1.toJson());
        Assertions.assertEquals(buffer, buffer2, "Buffers should be equal if same messages and consumer");
    }

    @Test
    @DisplayName("[Assertions] Test simple assert types and assert only types.")
    public void test_simpleTypeAssertion() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertTypes(MessageTypeEnum.HELLO),
                "buffer is empty: " + buffer);
        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer is empty: " + buffer);

        // we just add messages and check for the expected outcome

        buffer.add(new HelloMessage("Fred-Again", RoleEnum.PLAYER).toJson());

        Assertions.assertDoesNotThrow(() -> buffer.assertTypes(MessageTypeEnum.HELLO),
                "Should work as buffer has only hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertDoesNotThrow(() -> buffer.assertOnlyTypes(MessageTypeEnum.HELLO),
                "Should work as buffer has only hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer has only hello: " + buffer);

        buffer.add(new GameLeaveMessage(UUID.randomUUID()).toJson());

        Assertions.assertDoesNotThrow(() -> buffer.assertTypes(MessageTypeEnum.HELLO),
                "Should work as buffer has hello and gl: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertOnlyTypes(MessageTypeEnum.HELLO),
                "Should throw as buffer has gl and hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertDoesNotThrow(() -> buffer.assertTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer has hello and gl: " + buffer);

        Assertions.assertDoesNotThrow(() -> buffer.assertOnlyTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer has hello and gl: " + buffer);

        // use illegal offset

        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertTypes(-1, MessageTypeEnum.HELLO),
                "Should throw as illegal offset: " + buffer + " (" + buffer.typeTraversal() + ")");

    }

    @Test
    @DisplayName("[Assertions] Test simple assert types and assert only types using array notation.")
    public void test_arrayTypeAssertion() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }),
                "buffer is empty: " + buffer);
        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE },
                        new MessageTypeEnum[0], new MessageTypeEnum[0]),
                "buffer is empty: " + buffer);

        // we just add messages and check for the expected outcome

        buffer.add(new HelloMessage("Fred-Again", RoleEnum.PLAYER).toJson());

        Assertions.assertDoesNotThrow(
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }, new MessageTypeEnum[0]),
                "Should work as buffer has only hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertDoesNotThrow(
                () -> buffer.assertOnlyTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }, new MessageTypeEnum[0],
                        new MessageTypeEnum[0]),
                "Should work as buffer has only hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }, MessageTypeEnum.GAME_LEAVE),
                "buffer has only hello: " + buffer);
        Assertions.assertThrows(
                ExpectationException.class, () -> buffer
                        .assertOnlyTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }, MessageTypeEnum.GAME_LEAVE),
                "buffer has only hello: " + buffer);

        buffer.add(new GameLeaveMessage(UUID.randomUUID()).toJson());

        Assertions.assertDoesNotThrow(
                () -> buffer.assertTypes(new MessageTypeEnum[0], new MessageTypeEnum[] { MessageTypeEnum.HELLO }),
                "Should work as buffer has hello and gl: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertOnlyTypes(new MessageTypeEnum[0], new MessageTypeEnum[] { MessageTypeEnum.HELLO },
                        new MessageTypeEnum[0]),
                "Should throw as buffer has gl and hello: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertDoesNotThrow(
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE }),
                "buffer has hello and gl: " + buffer);
        Assertions.assertDoesNotThrow(
                () -> buffer.assertTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO }, MessageTypeEnum.GAME_LEAVE),
                "buffer has hello and gl: " + buffer);
        Assertions.assertDoesNotThrow(() -> buffer.assertOnlyTypes(new MessageTypeEnum[] { MessageTypeEnum.HELLO },
                MessageTypeEnum.GAME_LEAVE), "buffer has hello and gl: " + buffer);

        Assertions.assertDoesNotThrow(
                () -> buffer.assertOnlyTypes(new MessageTypeEnum[0], new MessageTypeEnum[] { MessageTypeEnum.HELLO },
                        new MessageTypeEnum[] { MessageTypeEnum.GAME_LEAVE }, new MessageTypeEnum[0]),
                "buffer has hello and gl: " + buffer);

        // use illegal offset

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertTypes(-1, new MessageTypeEnum[] { MessageTypeEnum.HELLO }),
                "Should throw as illegal offset: " + buffer + " (" + buffer.typeTraversal() + ")");
    }

    @Test
    @DisplayName("[Assertions] Test last assert types.")
    public void test_lastTypeAssertion() {
        DummySendMessagesBuffer buffer = new DummySendMessagesBuffer();
        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLastTypes(MessageTypeEnum.HELLO),
                "buffer is empty: " + buffer);
        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertLastTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer is empty: " + buffer);

        // we just add messages and check for the expected outcome

        buffer.add(new StrikeMessage(UUID.randomUUID(), 2, 4).toJson());

        Assertions.assertDoesNotThrow(() -> buffer.assertLastTypes(MessageTypeEnum.STRIKE),
                "Should work as buffer has only strike: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertLastTypes(MessageTypeEnum.HELLO, MessageTypeEnum.GAME_LEAVE),
                "buffer has only strike: " + buffer);

        Assertions.assertThrows(ExpectationException.class,
                () -> buffer.assertLastTypes(MessageTypeEnum.STRIKE, MessageTypeEnum.GAME_LEAVE),
                "buffer has only strike: " + buffer);

        buffer.add(new GameLeaveMessage(UUID.randomUUID()).toJson());

        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLastTypes(MessageTypeEnum.HELLO),
                "Should not work as buffer has strike and gl: " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertThrows(ExpectationException.class, () -> buffer.assertLastTypes(MessageTypeEnum.STRIKE),
                "Should not work as buffer has strike AND gl (last): " + buffer + " (" + buffer.typeTraversal() + ")");

        Assertions.assertDoesNotThrow(() -> buffer.assertLastTypes(MessageTypeEnum.GAME_LEAVE),
                "buffer has Strike and gl (last): " + buffer);

        Assertions.assertDoesNotThrow(() -> buffer.assertLastTypes(MessageTypeEnum.STRIKE, MessageTypeEnum.GAME_LEAVE),
                "buffer has Strike and gl: " + buffer);
    }

    @Test
    @DisplayName("[Assertions] With client if closed and on changed.")
    public void test_withClient() throws IOException {
        StoryBoard board = new StoryBoard();
        board.hello("walter", RoleEnum.PLAYER);
        Story story = board.build();

        DummyClient client = story.getClient("walter");
        AtomicBoolean changedFlag = new AtomicBoolean();
        AtomicBoolean closedFlag = new AtomicBoolean();

        client.setConsumer((s) -> changedFlag.set(true));
        client.setCloseConsumer((c, m) -> closedFlag.set(true));

        Assertions.assertFalse(changedFlag.get(), "Changed should not be set");
        Assertions.assertFalse(closedFlag.get(), "Closed should not be set");
        Assertions.assertFalse(client.isClosed(), "Client should not be closed");

        board.meta("walter", MetaKeyEnum.PLAYER_NAMES);

        Assertions.assertTrue(changedFlag.get(), "Changed should be set");
        Assertions.assertFalse(closedFlag.get(), "Closed should not be set");
        Assertions.assertFalse(client.isClosed(), "Client should not be closed");

        // lets close the client :D
        client.close();

        Assertions.assertTrue(closedFlag.get(), "Closed should be set");
        Assertions.assertTrue(client.isClosed(), "Client should be closed");

        Assertions.assertThrows(StoryException.class, () -> board.meta("walter", MetaKeyEnum.PLAYER_NAMES),
                "Should fail as already closed");

        changedFlag.set(false); // reset changed
        // should not be able to add:
        Assertions.assertFalse(client.getMessages().add(new HelloMessage("Jens", RoleEnum.PLAYER).toJson()),
                "Should not be allowed to add as already closed");
        Assertions.assertFalse(changedFlag.get(), "Changed should not be set as it should have been denied");
    }

}