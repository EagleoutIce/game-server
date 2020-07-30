package de.uulm.team020.server.core;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.server.addons.timer.TimeoutController;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.blueprints.NttsDummyServer;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.validation.GameDataGson;
import de.uulm.team020.validation.ValidationReport;
import de.uulm.team020.validation.Validator;

/**
 * Tests the implementation of the server by using the dummies:
 * <ul>
 * <li>{@link de.uulm.team020.server.core.dummies.DummyNttsController}</li>
 * <li>{@link de.uulm.team020.server.core.dummies.DummyNttsMessageSender}</li>
 * <li>{@link de.uulm.team020.server.core.dummies.DummyClient}</li>
 * </ul>
 *
 * @author Florian Sihler
 * @version 1.0, 03/31/2020
 */
@Tag("Session")
@TestMethodOrder(OrderAnnotation.class)
public class NttsServerBasicSessionTests extends NttsDummyServer {

    public static Stream<Arguments> generate_helloMessageData() {
        return Stream.of(Arguments.arguments("I am a client", RoleEnum.PLAYER),
                Arguments.arguments("I am a nice player", RoleEnum.PLAYER),
                Arguments.arguments("I am Günther", RoleEnum.AI), Arguments.arguments("I am Günther v2.0", RoleEnum.AI),
                Arguments.arguments("I am a client", RoleEnum.PLAYER),
                Arguments.arguments("BystanderDelüxe", RoleEnum.SPECTATOR));
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(1)
    @DisplayName("[Server] Register one client with a hello-message")
    @MethodSource("generate_helloMessageData")
    public void test_helloMessage(final String name, final RoleEnum role) {
        DummyClient aClient = new DummyClient(controller); // has now an 'open connection' to the controller
        HelloMessage hello = new HelloMessage(name, role);

        // controller could simulate this too
        aClient.send(hello.toJson());

        DummySendMessagesBuffer buffer = aClient.getMessages(TIMEOUT_MS);

        // There should be exactly two messages to be send (meta + helloreply)
        Assertions.assertEquals(2, buffer.size());

        // Now we can slowly build the wanted HelloReply-Message
        String message = buffer.get(1);
        MessageTypeEnum targetType = GameDataGson.getType(message);

        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, targetType,
                "The connection should be accepted with a HelloReply");

        // The message should be valid
        ValidationReport report = Validator.validateObject(message, targetType.getLinkedSchema());
        Assertions.assertTrue(report.isValid(),
                "The HelloReply-Message should be valid, but was not for the following reasons: "
                        + report.getReasons());

        HelloReplyMessage helloReplyMessage = GameDataGson.fromJson(message, HelloReplyMessage.class);

        Configuration configuration = controller.getConfiguration();

        // Fields should be as expected
        validateHelloReply(configuration, helloReplyMessage);

        // As the client has registered as a Player, this client should now be
        // registered as playerOne
        NttsClientConnection playerOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection playerTwo = controller.getClientManager().getPlayerTwo();

        // First of all: there should be no second player and the game-phase should be
        // before game-start as we
        // are not ready yet
        Assertions.assertTrue(configuration.getGamePhase().isBefore(GamePhaseEnum.WAIT_FOR_GAME_START),
                "We are not ready, and therefore it shouldn't be: " + configuration.getGamePhase());
        Assertions.assertNull(playerTwo, "Player Two should not have been set.");

        if (role != RoleEnum.SPECTATOR) {
            Assertions.assertNotNull(playerOne, "The first player should be set.");
            Assertions.assertEquals(helloReplyMessage.getClientId(), playerOne.getClientId(),
                    "Client should be player one (id)");
            Assertions.assertEquals(name, playerOne.getClientName(), "Client should be player one (name)");
        } else {
            Assertions.assertNull(playerOne, "Player one should not have been set, as we've registered as Spectator");
        }

        NttsClientConnection connection = controller.getClientManager()
                .getConnectionByUUID(helloReplyMessage.getClientId());
        Assertions.assertNotNull(connection, "This was the connection that did register!");

        // Just to be sure:
        Assertions.assertEquals(helloReplyMessage.getClientId(), connection.getClientId(),
                "The search should have returned the correct client.");

        // it should also be a player:
        Assertions.assertEquals(role, connection.getClientRole(), "Client should be as stated (role)");
        Assertions.assertEquals(name, connection.getClientName(), "Client should be as stated (name)");
    }

    // some are doubled to prevent timer-luckies
    public static Stream<Arguments> generate_registrationData() {
        return Stream.of(
                /* default */
                Arguments.arguments(List.of(ImmutablePair.of("Client P1", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S1", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S2", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S3", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), ImmutablePair.of("Illegal P2", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S4", RoleEnum.SPECTATOR)),
                        new ErrorTypeEnum[] { null, null, null, null, null, ErrorTypeEnum.ALREADY_SERVING, null },
                        new int[] { 0, 4 }, /* zero request ist first, fourth request is second */
                        new int[] { 1, 1, 1, 1, 3, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Client P1", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S1", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S2", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S3", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), ImmutablePair.of("Illegal P2", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S4", RoleEnum.SPECTATOR)),
                        new ErrorTypeEnum[] { null, null, null, null, null, ErrorTypeEnum.ALREADY_SERVING, null },
                        new int[] { 0, 4 }, /* zero request ist first, fourth request is second */
                        new int[] { 1, 1, 1, 1, 3, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Client P1", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S1", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S2", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S3", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), ImmutablePair.of("Illegal P2", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S4", RoleEnum.SPECTATOR)),
                        new ErrorTypeEnum[] { null, null, null, null, null, ErrorTypeEnum.ALREADY_SERVING, null },
                        new int[] { 0, 4 }, /* zero request ist first, fourth request is second */
                        new int[] { 1, 1, 1, 1, 3, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Client P1", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S1", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S2", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Spectator S3", RoleEnum.SPECTATOR),
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), ImmutablePair.of("Illegal P2", RoleEnum.PLAYER),
                        ImmutablePair.of("Spectator S4", RoleEnum.SPECTATOR)),
                        new ErrorTypeEnum[] { null, null, null, null, null, ErrorTypeEnum.ALREADY_SERVING, null },
                        new int[] { 0, 4 }, /* zero request ist first, fourth request is second */
                        new int[] { 1, 1, 1, 1, 3, 1, 2 }),
                /* double name storm */
                Arguments.arguments(List.of(ImmutablePair.of("Jens", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("SuperName", RoleEnum.PLAYER), // 3
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Anothername", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Peter", RoleEnum.SPECTATOR) // 2
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.NAME_NOT_AVAILABLE, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE, null }, new int[] { 1, 4 },
                        new int[] { 1, 1, 1, 1, 3, 1, 1, 1, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Jens", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("SuperName", RoleEnum.PLAYER), // 3
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Anothername", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Peter", RoleEnum.SPECTATOR) // 2
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.NAME_NOT_AVAILABLE, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE, null }, new int[] { 1, 4 },
                        new int[] { 1, 1, 1, 1, 3, 1, 1, 1, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Jens", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("SuperName", RoleEnum.PLAYER), // 3
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Anothername", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Peter", RoleEnum.SPECTATOR) // 2
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.NAME_NOT_AVAILABLE, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE, null }, new int[] { 1, 4 },
                        new int[] { 1, 1, 1, 1, 3, 1, 1, 1, 1, 2 }),
                Arguments.arguments(List.of(ImmutablePair.of("Jens", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("SuperName", RoleEnum.PLAYER), // 3
                        ImmutablePair.of("Client P2", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Dieter", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("Anothername", RoleEnum.AI), // 1
                        ImmutablePair.of("Dieter", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("Peter2", RoleEnum.SPECTATOR) // 2
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.NAME_NOT_AVAILABLE, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE,
                        ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.NAME_NOT_AVAILABLE, null }, new int[] { 1, 4 },
                        new int[] { 1, 1, 1, 1, 3, 1, 1, 1, 1, 2 }),
                /* player storm */
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.AI), // 2
                        ImmutablePair.of("3", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("4", RoleEnum.AI), // 1
                        ImmutablePair.of("5", RoleEnum.PLAYER) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.AI), // 1
                        ImmutablePair.of("2", RoleEnum.AI), // 2
                        ImmutablePair.of("3", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("4", RoleEnum.AI), // 1
                        ImmutablePair.of("5", RoleEnum.PLAYER) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.AI), // 1
                        ImmutablePair.of("2", RoleEnum.AI), // 2
                        ImmutablePair.of("3", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("4", RoleEnum.AI), // 1
                        ImmutablePair.of("5", RoleEnum.PLAYER) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.AI), // 1
                        ImmutablePair.of("2", RoleEnum.AI), // 2
                        ImmutablePair.of("3", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("4", RoleEnum.AI), // 1
                        ImmutablePair.of("5", RoleEnum.PLAYER) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                /* no second player */
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("3", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("4", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("5", RoleEnum.SPECTATOR) // 1
                ), new ErrorTypeEnum[] { null, ErrorTypeEnum.NAME_NOT_AVAILABLE, null, null, null }, new int[] { 0 },
                        new int[] { 1, 1, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                /* no second player */
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("3", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("4", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("5", RoleEnum.SPECTATOR) // 1
                ), new ErrorTypeEnum[] { null, ErrorTypeEnum.NAME_NOT_AVAILABLE, null, null, null }, new int[] { 0 },
                        new int[] { 1, 1, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                /* no second player */
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("3", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("4", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("5", RoleEnum.SPECTATOR) // 1
                ), new ErrorTypeEnum[] { null, ErrorTypeEnum.NAME_NOT_AVAILABLE, null, null, null }, new int[] { 0 },
                        new int[] { 1, 1, 1, 1, 1 }),
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("2", RoleEnum.PLAYER), // 2
                        ImmutablePair.of("3", RoleEnum.AI), // 1
                        ImmutablePair.of("4", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("5", RoleEnum.AI) // 1
                ), new ErrorTypeEnum[] { null, null, ErrorTypeEnum.ALREADY_SERVING, ErrorTypeEnum.ALREADY_SERVING,
                        ErrorTypeEnum.ALREADY_SERVING }, new int[] { 0, 1 }, new int[] { 1, 3, 1, 1, 1 }),
                /* no second player */
                Arguments.arguments(List.of(ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("1", RoleEnum.PLAYER), // 1
                        ImmutablePair.of("3", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("4", RoleEnum.SPECTATOR), // 1
                        ImmutablePair.of("42", RoleEnum.SPECTATOR) // 1
                ), new ErrorTypeEnum[] { null, ErrorTypeEnum.NAME_NOT_AVAILABLE, null, null, null }, new int[] { 0 },
                        new int[] { 1, 1, 1, 1, 1 }));
    }

    @ParameterizedTest
    @Tag("Network")
    @Order(2)
    @DisplayName("[Server] Test client registration")
    @MethodSource("generate_registrationData")
    public void test_clientRegistration(List<ImmutablePair<String, RoleEnum>> clientsData, ErrorTypeEnum[] denialError,
            int[] pNum, int[] expectedMessages) throws InterruptedException {

        DummyClient[] clients = new DummyClient[clientsData.size()];

        for (int i = 0; i < clientsData.size(); i++) {
            ImmutablePair<String, RoleEnum> clientData = clientsData.get(i);
            clients[i] = new DummyClient(controller); // has now an 'open connection' to the controller
            HelloMessage hello = new HelloMessage(clientData.getKey(), clientData.getValue());
            clients[i].send(hello.toJson());
            // clients[i].waitForNewMessages(expectedMessages[i], TIMEOUT_MS)
            // Get the buffer:
            DummySendMessagesBuffer buffer = clients[i].getMessages();

            // Just to be sure:
            Assertions.assertEquals(expectedMessages[i] + 1, buffer.size(),
                    "There should be " + (expectedMessages[i] + 1) + " messages for client " + i + ". But: " + buffer);
        }

        // As the client has registered as a Player, this client should now be
        // registered as playerOne
        NttsClientConnection playerOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection playerTwo = controller.getClientManager().getPlayerTwo();

        switch (pNum.length) {
            case 0:
                Assertions.assertNull(playerOne, "No player did register!");
                Assertions.assertNull(playerTwo, "No player did register!");
                break;
            case 1:
                Assertions.assertNotNull(playerOne, "Only one player did register");
                Assertions.assertNull(playerTwo, "Only one player did register");
                Assertions.assertEquals(playerOne, clients[pNum[0]].getAttachment());
                break;
            case 2:
                Assertions.assertNotNull(playerOne, "Both players did register");
                Assertions.assertNotNull(playerTwo, "Both players did register");
                Assertions.assertEquals(playerOne, clients[pNum[0]].getAttachment());
                Assertions.assertEquals(playerTwo, clients[pNum[1]].getAttachment());
                break;
            default:
                Assertions.fail("Maximum amount of players is 2");
        }

        if (pNum.length == 2) {
            DummySendMessagesBuffer[] buffers = Arrays.stream(clients).map(DummyClient::getMessages)
                    .toArray(DummySendMessagesBuffer[]::new);
            Assertions.assertEquals(clients.length, buffers.length);

            Configuration configuration = controller.getConfiguration();

            for (int i = 0; i < clients.length; i++) {
                if (denialError[i] == null) {
                    NttsClientConnection connection = clients[i].getAttachment();
                    boolean isPlayer = connection.getGameRole().isPlayer();
                    if (isPlayer) {
                        buffers[i].assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE);
                    } else {
                        buffers[i].assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                                MessageTypeEnum.GAME_STARTED);
                    }

                    // First of all we validate Message one to be a HelloReply Message and store
                    // it's uuid, and
                    // furthermore the configurations.
                    // than we check the gamestarted message, validate it to has the same uuid and
                    // than validate
                    // the player-information

                    String firstMessage = buffers[i].get(1);
                    MessageTypeEnum type = GameDataGson.getType(firstMessage);
                    Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, type, "Connection should have been accepted");

                    HelloReplyMessage helloReplyMessage = GameDataGson.fromJson(firstMessage, HelloReplyMessage.class);
                    UUID thisClientsUuid = helloReplyMessage.getClientId();
                    validateHelloReply(configuration, helloReplyMessage);

                    // Now we check the game started message
                    String secondMessage = buffers[i].get(2);
                    type = GameDataGson.getType(secondMessage);
                    Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, type, "Game should have started");

                    GameStartedMessage gameStartedMessage = GameDataGson.fromJson(secondMessage,
                            GameStartedMessage.class);

                    Assertions.assertEquals(thisClientsUuid, gameStartedMessage.getClientId(),
                            "clientIds should match");

                    // match player names
                    Assertions.assertEquals(playerOne.getClientName(), gameStartedMessage.getPlayerOneName(),
                            "player one should be as stated");
                    Assertions.assertEquals(playerOne.getClientId(), gameStartedMessage.getPlayerOneId(),
                            "player one should be as stated");

                    Assertions.assertEquals(playerTwo.getClientName(), gameStartedMessage.getPlayerTwoName(),
                            "player two should be as stated");
                    Assertions.assertEquals(playerTwo.getClientId(), gameStartedMessage.getPlayerTwoId(),
                            "player two should be as stated");

                } else {
                    Assertions.assertEquals(2, buffers[i].size(),
                            "Should have gotten Meta and denial only, but got: " + buffers[i]);

                    // Check that it is a denial
                    String denialMessage = buffers[i].get(1);
                    MessageTypeEnum type = GameDataGson.getType(denialMessage);

                    // It should be an Error Message of "any Type" for now:
                    Assertions.assertEquals(MessageTypeEnum.ERROR, type,
                            "Connection should have been denied for client: " + i);

                    ErrorMessage errorMessage = GameDataGson.fromJson(denialMessage, ErrorMessage.class);

                    // we can toss away the client id, we are just interested in the error-type
                    Assertions.assertEquals(denialError[i], errorMessage.getReason(),
                            "Reason should be as expected for client: " + i);

                    // "done"
                }
            }
        }
    }

    private void checkGameStarted(DummyClient client, String message) {
        MessageTypeEnum type = GameDataGson.getType(message);
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, type,
                "The message '" + message + "' should be a GameStarted message");
        GameStartedMessage gameStartedMessage = GameDataGson.fromJson(message, GameStartedMessage.class);
        NttsClientConnection connection = client.getAttachment();
        Assertions.assertEquals(connection.getClientId(), gameStartedMessage.getClientId(),
                "The clientIds should match.");

        NttsClientConnection playerOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection playerTwo = controller.getClientManager().getPlayerTwo();

        // match player names
        Assertions.assertEquals(playerOne.getClientName(), gameStartedMessage.getPlayerOneName(),
                "player one should be as stated");
        Assertions.assertEquals(playerOne.getClientId(), gameStartedMessage.getPlayerOneId(),
                "player one should be as stated");

        Assertions.assertEquals(playerTwo.getClientName(), gameStartedMessage.getPlayerTwoName(),
                "player two should be as stated");
        Assertions.assertEquals(playerTwo.getClientId(), gameStartedMessage.getPlayerTwoId(),
                "player two should be as stated");
    }

    @RepeatedTest(2)
    @Tag("Network")
    @Order(3)
    @DisplayName("[Server] Test remove player and reset pre game-phase")
    public void test_playerRemoveInPrePhase() throws InterruptedException {

        // we start by registering one player correctly
        DummyClient p1 = new DummyClient(controller);
        setupHelloRite(p1, "P1", RoleEnum.PLAYER);

        // registered as player one
        NttsClientConnection playerOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection playerTwo = controller.getClientManager().getPlayerTwo();
        // Just do a quick validation to be sure
        Assertions.assertNotNull(playerOne, "playerOne should be set");
        Assertions.assertNull(playerTwo, "playerTwo should not be set");
        Assertions.assertEquals(playerOne, p1.getAttachment(), "The player one should be the client");

        // we will remove our connection by a crash
        controller.simulateCrashClose(p1);

        p1.clearMessages();

        // Now the slot has to be free
        playerOne = controller.getClientManager().getPlayerOne();
        playerTwo = controller.getClientManager().getPlayerTwo();
        Assertions.assertNull(playerOne, "no player should be connected");
        Assertions.assertNull(playerTwo, "no player should be connected");

        // so we can connect two players now without any error
        DummyClient p2 = new DummyClient(controller);
        setupHelloRite(p2, "P2", RoleEnum.AI);

        // It should be possible to register with same name too

        DummyClient p3 = new DummyClient(controller);
        setupHelloRite(p3, "P1", RoleEnum.PLAYER);

        playerOne = controller.getClientManager().getPlayerOne();
        playerTwo = controller.getClientManager().getPlayerTwo();

        Assertions.assertNotNull(playerOne, "both players should be connected");
        Assertions.assertNotNull(playerTwo, "both players should be connected");

        Assertions.assertEquals(playerOne, p2.getAttachment(), "The player one should be p2");
        Assertions.assertEquals(playerTwo, p3.getAttachment(), "The player two should be p3");

        Thread.sleep(10);
        // both clients should have a game-started message now, but p1 shouldn't

        DummySendMessagesBuffer bP1 = p1.getMessages();
        DummySendMessagesBuffer bP2 = p2.getMessages();
        DummySendMessagesBuffer bP3 = p3.getMessages();

        Assertions.assertTrue(bP1.isEmpty(), "p1 should not have gotten any (more) messages");

        Assertions.assertEquals(4, bP2.size(), "p2 should have gotten three messages");
        Assertions.assertEquals(4, bP3.size(), "p3 should have gotten three messages");

        // for both: first should be helloReply, second should be gameStarted
        checkHelloReply(p2, bP2.get(1));
        checkHelloReply(p3, bP3.get(1));

        checkGameStarted(p2, bP2.get(2));
        checkGameStarted(p3, bP3.get(2));
    }

    @RepeatedTest(2)
    @Tag("Network")
    @Order(4)
    @DisplayName("[Server] Test check of freshness")
    public void test_freshnessPolicy() {

        // we start by registering one player correctly
        DummyClient p1 = new DummyClient(controller);
        String helloMessage = "{\"name\":\"P1\",\"role\":\"PLAYER\",\"type\":\"HELLO\",\"creationDate\":\"02.04.2020 23:01:09\",\"debugMessage\":\"\"}";
        // As this date is in the past, we do not accept a reply for this message

        p1.send(helloMessage);

        DummySendMessagesBuffer buffer = p1.getMessages(TIMEOUT_MS);

        Assertions.assertEquals(1, buffer.size(), "We should not have gotten any message beside the meta-message.");
    }

    @Test
    @Tag("Network")
    @Order(5)
    @DisplayName("[Server] Test player sending double pause")
    public void test_playerSendsDoublePause() throws InterruptedException, IOException {
        StoryBoard board = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "resumeByBoth", "false"));
        Story story = board.build();
        board.hello("Peter", RoleEnum.PLAYER);
        board.hello("Jens", RoleEnum.PLAYER);
        board.preset(Presets.SKIP_DRAFTING);
        // in game peter wants a pause
        board.pause("Peter");
        Assertions.assertEquals(GamePhaseEnum.GAME_PAUSED, story.getConfiguration().getGamePhase(),
                "Should be paused as not resumed now");

        board.resume("Jens");

        // here there should be no timeout for anyone as pause has resume
        DummyClient peter = story.getClient("Peter");
        DummyClient jens = story.getClient("Jens");
        DummySendMessagesBuffer peterBuffer = peter.getMessages();
        DummySendMessagesBuffer jensBuffer = jens.getMessages();
        peterBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        jensBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        // there should be no pause in the timeout controller
        TimeoutController controller = story.getConfiguration().getTimeoutController();
        Assertions.assertEquals(-1, controller.getShortestRemainingPauseTime(),
                "Should be no pause running as resumed");
        Assertions.assertEquals(GamePhaseEnum.MAIN_GAME_PLAY, story.getConfiguration().getGamePhase(),
                "Should be in main game play as resumed");

        // no resumed, peter wants another pause :)
        // now peter wants another pause
        Assertions.assertDoesNotThrow(() -> board.pause("Peter"),
                "Should not throw as this reschedule is legal by all means");

        // now there should be a pause again which peter may be allowed to resume
        Assertions.assertEquals(GamePhaseEnum.GAME_PAUSED, story.getConfiguration().getGamePhase(),
                "Should be paused as not resumed now");
        Assertions.assertNotEquals(-1, controller.getShortestRemainingPauseTime(), "Should be paused as not resumed");

        board.resume("Peter");
        // Now we should have left again :)
        Assertions.assertEquals(GamePhaseEnum.MAIN_GAME_PLAY, story.getConfiguration().getGamePhase(),
                "Should be in main game play as resumed");
        Assertions.assertEquals(-1, controller.getShortestRemainingPauseTime(),
                "Should be no pause running as resumed");
    }

}