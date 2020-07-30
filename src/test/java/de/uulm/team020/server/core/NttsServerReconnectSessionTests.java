package de.uulm.team020.server.core;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.ReconnectMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.validation.GameDataGson;

/**
 * Tests the implementation of reconnects by using the dummies:
 * <ul>
 * <li>{@link de.uulm.team020.server.core.dummies.DummyNttsController}</li>
 * <li>{@link de.uulm.team020.server.core.dummies.DummyNttsMessageSender}</li>
 * <li>{@link DummyClient}</li>
 * </ul>
 *
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
@Tag("Session")
@TestMethodOrder(OrderAnnotation.class)
public class NttsServerReconnectSessionTests {

    public static Stream<Arguments> generate_senselessReconnect() {
        return Stream.of(Arguments.arguments(Stream.of("HELLO jörg SPECTATOR", "HELLO saphira PLAYER"), "saphira"),
                Arguments.arguments(Stream.of("HELLO dieter AI", "HELLO saphira AI",
                        /* GameStarted here */
                        "HELLO wolfgang SPECTATOR", "HELLO udo SPECTATOR"), "dieter"));
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(1)
    @DisplayName("[Server] Reconnect when it doesn't make sense")
    @MethodSource("generate_senselessReconnect")
    public void test_senselessReconnect(Stream<String> tale, String tryClient) throws IOException {
        Story story = new StoryBoard().execute(tale).build();
        DummyClient lemming = story.getClient(tryClient);

        // Lemming wants to reconnect but is not allowed to
        NttsClientConnection lemmingConnection = lemming.getAttachment();
        UUID clientId = lemmingConnection.getClientId();
        UUID sessionId = story.getConfiguration().getSessionId();

        lemming.clearMessages();

        ReconnectMessage reconnectMessage = new ReconnectMessage(clientId, sessionId);
        lemming.send(reconnectMessage.toJson());

        DummySendMessagesBuffer buffer = lemming.assureMessages(1, 800);

        String illegal = buffer.getFirst();
        MessageTypeEnum type = GameDataGson.getType(illegal);
        Assertions.assertEquals(MessageTypeEnum.ERROR, type, "Should be an error");

        ErrorMessage error = GameDataGson.fromJson(illegal, ErrorMessage.class);

        Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, error.getReason(), "Should be illegal, for: " + error);

    }

    public static Stream<Arguments> generate_crashReconnect() {
        return Stream.of(Arguments.arguments(Stream.of("HELLO jörg AI", "HELLO saphira PLAYER", // GameStarted
                "HELLO \"dieter günther\" SPECTATOR", "HELLO affeA SPECTATOR", "CRASH saphira"),
                List.of("jörg", "dieter günther", "affeA"), "saphira"));
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(2)
    @DisplayName("[Server] Reconnect after a crash")
    @MethodSource("generate_crashReconnect")
    public void test_crashReconnect(Stream<String> tale, List<String> others, String tryClient) throws IOException {
        Story story = new StoryBoard().execute(tale).build();

        DummyClient lemming = story.getClient(tryClient);

        // Lemming wants to reconnect but is not allowed to
        NttsClientConnection lemmingConnection = lemming.getAttachment();
        UUID clientId = lemmingConnection.getClientId();
        UUID sessionId = story.getConfiguration().getSessionId();

        // Assert everyone got the pause
        for (String name : others) {
            DummyClient client = story.getClient(name);
            NttsClientConnection connection = client.getAttachment();
            DummySendMessagesBuffer buffer = client.getMessages();
            int idx = 3;
            if (connection.getGameRole().isPlayer()) {
                Assertions.assertEquals(5, buffer.size(),
                        "Should have: Meta, HelloReply, GameStarted, RequestItemChoice, Enforced Pause, but gut: "
                                + buffer);

                buffer.assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                        MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE);

                idx = 4;
            } else {
                Assertions.assertEquals(4, buffer.size(),
                        "Should have: Meta, HelloReply, GameStarted, Enforced Pause, but gut: " + buffer);

                buffer.assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                        MessageTypeEnum.GAME_STARTED);

                idx = 3;
            }

            Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(buffer.get(idx)),
                    "Should be GamePaused");

            GamePauseMessage gamePauseMessage = GameDataGson.fromJson(buffer.get(idx), GamePauseMessage.class);
            Assertions.assertTrue(gamePauseMessage.getGamePaused(), "Should be paused now");
            Assertions.assertTrue(gamePauseMessage.getServerEnforced(), "Pause should be (as crashed) server enforced");

            client.clearMessages();
        }
        DummyClient theNewLemming = new DummyClient(story.getController());

        ReconnectMessage reconnectMessage = new ReconnectMessage(clientId, sessionId);
        theNewLemming.send(reconnectMessage.toJson());

        // Assert everybody but reconnected get pause release

        for (String name : others) {
            DummyClient client = story.getClient(name);
            DummySendMessagesBuffer buffer = client.assureMessages(1, 250);
            Assertions.assertEquals(1, buffer.size(), "Should have gotten only one message, but: " + buffer);

            Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(buffer.getFirst()),
                    "Should be GamePause");

            GamePauseMessage gamePauseMessage = GameDataGson.fromJson(buffer.getFirst(), GamePauseMessage.class);
            Assertions.assertFalse(gamePauseMessage.getGamePaused(), "Pause should be over now");
            Assertions.assertTrue(gamePauseMessage.getServerEnforced(), "Pause should be (as crashed) server enforced");
        }

        // check when messages are ready with gamestarted and theoretical
        // operation request?
        // System.out.println(theNewLemming.getMessages())
    }

    private void assertMessageStack(DummySendMessagesBuffer buffer, boolean player) {
        Assertions.assertEquals(player ? 6 : 5, buffer.size(), (player ? 6 : 5) + " Messages, but: " + buffer);
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(buffer.get(1)),
                "Should be HelloReply");
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, GameDataGson.getType(buffer.get(2)),
                "Should be GameStarted");
        int offset = 0;
        if (player) {
            Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, GameDataGson.getType(buffer.get(3)),
                    "Should be RequestItemChoice");
            offset = 1;
        }
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(buffer.get(3 + offset)),
                "Should be GamePaused");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(buffer.get(4 + offset)),
                "Should be the enforced GamePaused");
        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(buffer.get(3 + offset), GamePauseMessage.class);
        Assertions.assertTrue(gamePauseMessage.getGamePaused(), "Should be pause start.");
        Assertions.assertFalse(gamePauseMessage.getServerEnforced(), "Should not be server enforced.");

        GamePauseMessage enforcedPauseMessage = GameDataGson.fromJson(buffer.get(4 + offset), GamePauseMessage.class);
        Assertions.assertTrue(enforcedPauseMessage.getGamePaused(), "Should be pause start.");
        Assertions.assertTrue(enforcedPauseMessage.getServerEnforced(), "Should be enforced pause.");

    }

    @RepeatedTest(2)
    @Tag("Network")
    @Order(3)
    @DisplayName("[Server] Reconnect after a crash while a pause")
    public void test_crashReconnectWhilePause() throws IOException {
        StoryBoard storyBoard = new StoryBoard()
                .execute(Stream.of("HELLO jörg PLAYER", "HELLO dieter SPECTATOR", "HELLO saphira PLAYER", // GameStarted
                        // Pause the game, jörg!
                        "PAUSE jörg", "CRASH saphira"));
        Story story = storyBoard.build();

        // Everybody should have at least 4 messages:
        // HelloReply, GameStarted, Normal Pause, Enforced Pause

        assertMessageStack(story.getClient("jörg").getMessages(), true);
        assertMessageStack(story.getClient("dieter").getMessages(), false);

        // saphira should have gotten only 3:
        DummyClient saphira = story.getClient("saphira");
        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages(10);
        Assertions.assertEquals(5, saphiraBuffer.size(), "Should be 5 messages, but: " + saphiraBuffer);

        // Shouldn't be written more efficient :D
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(saphiraBuffer.get(1)),
                "Should be HelloReply");
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, GameDataGson.getType(saphiraBuffer.get(2)),
                "Should be GameStarted");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, GameDataGson.getType(saphiraBuffer.get(3)),
                "Should be RequestItemChoice");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(saphiraBuffer.get(4)),
                "Should be GamePaused");

        // Saphira should have gotten the not-enforced pause
        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(saphiraBuffer.get(4), GamePauseMessage.class);
        Assertions.assertTrue(gamePauseMessage.getGamePaused(), "Should be pause start.");
        Assertions.assertFalse(gamePauseMessage.getServerEnforced(), "Should not be server enforced.");

        // Now we want saphira to reconnect!

        NttsClientConnection connection = saphira.getAttachment();
        Configuration configuration = story.getConfiguration();

        ReconnectMessage reconnectMessage = new ReconnectMessage(connection.getClientId(),
                configuration.getSessionId());
        DummyClient newSaphira = new DummyClient(story.getController());
        newSaphira.send(reconnectMessage.toJson());

        saphiraBuffer = newSaphira.assureMessages(4, 150);

        DummySendMessagesBuffer joergBuffer = story.getClient("jörg").assureMessages(7, 150);
        DummySendMessagesBuffer dieterBuffer = story.getClient("dieter").assureMessages(6, 150);

        Assertions.assertEquals(7, joergBuffer.size(), "Should have gotten the pause release for the enforced pause");
        Assertions.assertEquals(6, dieterBuffer.size(), "Should have gotten the pause release for the enforced pause");

        crashReconnectWhilePause_assertGamePause(joergBuffer.getLast());
        crashReconnectWhilePause_assertGamePause(dieterBuffer.getLast());

    }

    private void crashReconnectWhilePause_assertGamePause(String message) {
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(message), "Should be GamePause");
        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(message, GamePauseMessage.class);
        Assertions.assertFalse(gamePauseMessage.getGamePaused(), "Should be pause end.");
        Assertions.assertTrue(gamePauseMessage.getServerEnforced(), "Should be server enforced.");

    }

    @RepeatedTest(4)
    @Tag("Network")
    @Order(4)
    @DisplayName("[Server] Reconnect after a double crash while a pause")
    public void test_doubleCrashReconnectWhilePause() throws IOException {
        StoryBoard storyBoard = new StoryBoard()
                .execute(Stream.of("HELLO jörg PLAYER", "HELLO dieter SPECTATOR", "HELLO saphira PLAYER", // GameStarted
                        // Pause the game, jörg!
                        "PAUSE jörg", "CRASH saphira", "CRASH jörg"));
        Story story = storyBoard.build();

        DummyClient joerg = story.getClient("jörg");
        DummySendMessagesBuffer joergBuffer = joerg.getMessages();
        Assertions.assertEquals(6, joergBuffer.size(), "Should be 6 messages, but: " + joergBuffer);

        // Shouldn't be written more efficient :D
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(joergBuffer.get(1)),
                "Should be HelloReply");
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, GameDataGson.getType(joergBuffer.get(2)),
                "Should be GameStarted");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, GameDataGson.getType(joergBuffer.get(3)),
                "Should be ItemChoice");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(joergBuffer.get(4)),
                "Should be GamePaused");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(joergBuffer.get(5)),
                "Should be (enforced) GamePaused");

        // dieter should have at least 4 messages:
        // HelloReply, GameStarted, Normal Pause, Enforced Pause
        assertMessageStack(story.getClient("dieter").getMessages(), false);

        // saphira should have gotten only 4, but different ones:
        DummyClient saphira = story.getClient("saphira");
        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages(150);
        Assertions.assertEquals(5, saphiraBuffer.size(), "Should be 5 messages, but: " + saphiraBuffer);

        // Shouldn't be written more efficient :D
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(saphiraBuffer.get(1)),
                "Should be HelloReply");
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, GameDataGson.getType(saphiraBuffer.get(2)),
                "Should be GameStarted");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, GameDataGson.getType(saphiraBuffer.get(3)),
                "Should be ItemChoice");
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(saphiraBuffer.get(4)),
                "Should be GamePaused");

        // Saphira should have gotten the not-enforced pause
        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(saphiraBuffer.get(4), GamePauseMessage.class);
        Assertions.assertTrue(gamePauseMessage.getGamePaused(), "Should be pause start.");
        Assertions.assertFalse(gamePauseMessage.getServerEnforced(), "Should not be server enforced.");

        // Now we want saphira to reconnect!

        NttsClientConnection connection = saphira.getAttachment();
        Configuration configuration = story.getConfiguration();

        ReconnectMessage reconnectMessage = new ReconnectMessage(connection.getClientId(),
                configuration.getSessionId());
        DummyClient newSaphira = new DummyClient(story.getController());
        newSaphira.send(reconnectMessage.toJson());

        saphiraBuffer = newSaphira.assureMessages(4, 250);

    }

}