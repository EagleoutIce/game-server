package de.uulm.team020.server.core.story;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.ReconnectMessage;
import de.uulm.team020.networking.messages.RequestGamePauseMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Class to test the story construct
 *
 * @author Florian Sihler
 * @version 1.0, 04/16/2020
 */
public class StoryTests {

    @Test
    @Tag("Util")
    @DisplayName("[Story] Welcome-Tales")
    public void test_welcomeTale() throws IOException {
        Stream<String> tale = Stream.of("HELLO bernd PLAYER", "HELLO jens AI", "HELLO \"gustav josef\" SPECTATOR");

        // Write the tale:
        Story story = new StoryBoard().execute(tale).build();

        // Check that the story is correct
        List<DummyClient> clientList = story.getClients();

        Assertions.assertEquals(3, clientList.size(), "There should be 3 clients registered");

        // require them (we will get a story exception if they are not available so
        // there will be a internal "assert")
        DummyClient bernd = story.getClient("bernd");
        DummyClient jens = story.getClient("jens");
        DummyClient gustavJosef = story.getClient("gustav josef");

        // Just make sure, we cant get everyone:
        Assertions.assertThrows(StoryException.class, () -> story.getClient("TschupaTrupa"));

        // everyone should have a game-started-message now
        DummySendMessagesBuffer bufferBernd = bernd.getMessages();
        DummySendMessagesBuffer bufferJens = jens.getMessages();
        DummySendMessagesBuffer bufferGustavJosef = gustavJosef.getMessages();

        // Everybody should have to messages
        Assertions.assertEquals(4, bufferBernd.size(), "Should have 4 messages, but has: " + bufferBernd);
        Assertions.assertEquals(4, bufferJens.size(), "Should have 4 messages, but has: " + bufferJens);
        Assertions.assertEquals(3, bufferGustavJosef.size(), "Should have 3 messages, but has: " + bufferGustavJosef);

        // we check that everybody got a helloReply
        validateHelloReply(bernd, bufferBernd.get(1));
        validateHelloReply(jens, bufferJens.get(1));
        validateHelloReply(gustavJosef, bufferGustavJosef.get(1));

        // the other one should be a gameStartedMessage
        validateGameStarted(bernd, bufferBernd.get(2));
        validateGameStarted(jens, bufferJens.get(2));
        validateGameStarted(gustavJosef, bufferGustavJosef.get(2));

    }

    private void validateHelloReply(DummyClient client, String message) {
        MessageTypeEnum type = GameDataGson.getType(message);
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, type, "Should be helloReply");
        // maybe more?
    }

    private void validateGameStarted(DummyClient client, String message) {
        MessageTypeEnum type = GameDataGson.getType(message);
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, type, "Should be gameStarted");
        // maybe more?
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Leave-Tale")
    public void test_leaveTale() throws IOException {
        Stream<String> tale = Stream.of("HELLO bernd PLAYER", "HELLO jens AI", "HELLO \"gustav josef\" SPECTATOR",
                /* Gustav wants to leave */
                "LEAVE \"gustav josef\"",
                /* saphira wants to watch */
                "HELLO saphira SPECTATOR",
                /*
                 * Now we want to allow another gustav by renaming the other. This will change
                 * the server name
                 */
                "RENAME \"gustav josef\" old-gustav", "HELLO \"gustav josef\" SPECTATOR"
        /* Now we have done so much, lets take a nap: */
        );

        // Write the tale:
        Story story = new StoryBoard().execute(tale).build();

        // get the protagonists

        DummyClient bernd = story.getClient("bernd");
        DummyClient jens = story.getClient("jens");
        DummyClient oldGustav = story.getClient("old-gustav");
        DummyClient saphira = story.getClient("saphira");
        DummyClient gustavJosef = story.getClient("gustav josef");

        // Everybody needs: hello-reply & same game started message

        DummyNttsController controller = story.getController();

        NttsClientConnection playerOne = controller.getClientManager().getPlayerOne();
        NttsClientConnection playerTwo = controller.getClientManager().getPlayerTwo();

        validateFirstTwoMessagesHelloAndReply(4, bernd, playerOne, playerTwo);
        // requestItemChoice
        validateFirstTwoMessagesHelloAndReply(4, jens, playerOne, playerTwo);
        validateFirstTwoMessagesHelloAndReply(4, oldGustav, playerOne, playerTwo);
        validateFirstTwoMessagesHelloAndReply(3, saphira, playerOne, playerTwo);
        validateFirstTwoMessagesHelloAndReply(3, gustavJosef, playerOne, playerTwo);

        NttsClientConnection connection = gustavJosef.getAttachment();
        Assertions.assertNotNull(connection, "Gustav Josef should have a connection.");

        // Now validate GameLeft for old Gustav
        String oldGustavsThird = oldGustav.getMessages().get(3);
        MessageTypeEnum type = GameDataGson.getType(oldGustavsThird);
        Assertions.assertEquals(MessageTypeEnum.GAME_LEFT, type, "Should be gameLeft as spectator");
    }

    private void validateFirstTwoMessagesHelloAndReply(int n, DummyClient client, NttsClientConnection playerOne,
            NttsClientConnection playerTwo) {
        DummySendMessagesBuffer buffer = client.getMessages();
        Assertions.assertEquals(n, buffer.size(),
                "Needs " + n + " messages for " + client.getName() + " but: " + buffer.typeTraversal());

        // everybody was hello-ed, so no care:
        NttsClientConnection connection = client.getAttachment();

        UUID desiredId = connection.getClientId();
        String firstMessage = buffer.get(1);

        MessageTypeEnum type = GameDataGson.getType(firstMessage);
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, type, "Should be helloReply");

        HelloReplyMessage helloReplyMessage = GameDataGson.fromJson(firstMessage, HelloReplyMessage.class);

        Assertions.assertEquals(desiredId, helloReplyMessage.getClientId(), "Should acknowledge correct client");

        // Validate GameStarted Message
        String secondMessage = buffer.get(2);
        type = GameDataGson.getType(secondMessage);
        Assertions.assertEquals(MessageTypeEnum.GAME_STARTED, type, "Game should have started");

        GameStartedMessage gameStartedMessage = GameDataGson.fromJson(secondMessage, GameStartedMessage.class);

        Assertions.assertEquals(desiredId, gameStartedMessage.getClientId(), "clientIds should match");

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

    @Test
    @Tag("Util")
    @DisplayName("[Story] MetaInformation-Tale")
    public void test_metaInformationTale() throws IOException, MessageException, InterruptedException {
        Stream<String> tale = Stream.of("HELLO saphira PLAYER",
                // , Spectator.Count, Spectator.Names, Configuration.Scenario,
                // Configuration.Matchconfig, Configuration.CharacterInformation
                "META saphira \"Game.RemainingPauseTime\"", "HELLO jefferson AI", "PAUSE saphira",
                "META saphira \"Game.RemainingPauseTime\"");

        // Write the tale:
        Story story = new StoryBoard().execute(tale).build();

        DummyClient saphira = story.getClient("saphira");
        NttsClientConnection connection = saphira.getAttachment();

        DummySendMessagesBuffer messages = saphira.getMessages();

        Assertions.assertEquals(7, messages.size(),
                "Should be: Meta, HelloReply, MetaInfo, GameStarted, RequestItemChoice, GamePause, MetaInfo, but was: "
                        + messages);

        String firstRequest = messages.get(2);
        String secondRequest = messages.get(6);

        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(firstRequest),
                "Should be MetaInformation, but was: " + firstRequest);
        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(secondRequest),
                "Should be MetaInformation, but was: " + secondRequest);

        MetaInformationMessage firstMetaInformation = GameDataGson.fromJson(firstRequest, MetaInformationMessage.class);
        MetaInformationMessage secondMetaInformation = GameDataGson.fromJson(secondRequest,
                MetaInformationMessage.class);

        Integer timeout = (Integer) firstMetaInformation.getInformation()
                .get(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey());

        Assertions.assertEquals(-1, timeout, "There is no pause scheduled on first request.");

        timeout = (Integer) secondMetaInformation.getInformation().get(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey());
        int configTimeout = story.getConfiguration().getMatchconfig().getPauseLimit();
        int diff = configTimeout - timeout;

        Assertions.assertTrue(diff < 3,
                "Timeout (" + timeout + ") for pause should be pretty close to " + configTimeout + " [3].");

        // Send Resume message: TODO: change this if both have to accept
        saphira.clearMessages();

        RequestGamePauseMessage requestGamePauseMessage = new RequestGamePauseMessage(connection.getClientId(), false);
        saphira.send(requestGamePauseMessage.toJson());

        DummySendMessagesBuffer releaseMessage = saphira.assureMessages(1, 250);
        String pauseReleaseMessage = releaseMessage.getFirst();
        Assertions.assertEquals(MessageTypeEnum.GAME_PAUSE, GameDataGson.getType(pauseReleaseMessage),
                "Should be Pause-Release, but was: " + pauseReleaseMessage);

        GamePauseMessage gamePauseMessage = GameDataGson.fromJson(pauseReleaseMessage, GamePauseMessage.class);
        Assertions.assertFalse(gamePauseMessage.getServerEnforced(), "Should not be server enforced");
        Assertions.assertFalse(gamePauseMessage.getGamePaused(), "Should not be paused anymore");

        saphira.clearMessages();

        // Request the Message again
        RequestMetaInformationMessage requestMetaInformationMessage = new RequestMetaInformationMessage(
                connection.getClientId(), new String[] { "Game.RemainingPauseTime" });
        saphira.send(requestMetaInformationMessage.toJson());

        DummySendMessagesBuffer metaMessage = saphira.assureMessages(1, 250);
        String metaInformation = metaMessage.getFirst();

        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(metaInformation),
                "Should be MetaInformation, but was: " + metaInformation);

        MetaInformationMessage metaInformationMessage = GameDataGson.fromJson(metaInformation,
                MetaInformationMessage.class);
        int newTimeout = (int) metaInformationMessage.getInformation()
                .get(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey());

        Assertions.assertEquals(-1, newTimeout, "There should be no pause running in " + metaInformationMessage);
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] No Escalation but reconnect?")
    public void test_noEscalation() throws IOException {
        Stream<String> tale = Stream.of("HELLO saphira PLAYER", "ALLOW_ERRORS", "PAUSE saphira",
                // shouldn't be allowed as no gameStart
                "FORBID_ERRORS");

        // Write the tale:
        StoryBoard storyBoard = new StoryBoard().execute(tale);
        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");
        NttsClientConnection connection = saphira.getAttachment();

        // Lets try a mean reconnect
        DummyClient walter = new DummyClient(story.getController());

        ReconnectMessage reconnectMessage = new ReconnectMessage(connection.getClientId(),
                story.getConfiguration().getSessionId());

        walter.send(reconnectMessage.toJson());

        String shouldBeIllegal = walter.getMessages(250).get(1);
        Assertions.assertEquals(MessageTypeEnum.ERROR, GameDataGson.getType(shouldBeIllegal), "Block as not waiting");

        ErrorMessage errorMessage = GameDataGson.fromJson(shouldBeIllegal, ErrorMessage.class);
        Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, errorMessage.getReason(), "Not waiting.");

    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Multiple Injects")
    public void test_multipleInjects() throws IOException {
        // Write the tale:
        StoryBoard storyBoard = new StoryBoard().execute(
                /* We just need four injections, as we do not check everything */
                "CONFIG_INJECT next-proposal saphira \"James Bond, Meister Yoda, Meister Yoda, bowler_blade, fog_tin, rocket_pen\"",
                "CONFIG_INJECT next-proposal saphira \"James Bond, James Bond, Meister Yoda, gas_gloss, mothball_pouch, rocket_pen\"",
                "CONFIG_INJECT next-proposal walter \"James Bond, Meister Yoda, James Bond,   chicken_feed, nugget, mothball_pouch\"",
                "CONFIG_INJECT next-proposal walter \"James Bond, Meister Yoda, Meister Yoda, laser_compact, magnetic_watch, poison_pills\"",
                /* Register Players */
                "HELLO walter PLAYER", "HELLO saphira AI",
                /* choice */
                "ITEM saphira \"Meister Yoda\"", "ITEM walter \"James Bond\"", "ITEM ${playerOneName} magnetic_watch", // DIS
                                                                                                                       // IS
                                                                                                                       // CREAZY
                "ITEM saphira mothball_pouch");
        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");
        DummyClient walter = story.getClient("walter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer walterBuffer = walter.getMessages();

        Assertions.assertEquals(6, saphiraBuffer.size(),
                "Saphira: Should have gotten 6 messages, but: " + saphiraBuffer);
        Assertions.assertEquals(6, walterBuffer.size(), "Walter: Should have gotten 6 messages, but: " + walterBuffer);

        assertRequestItemChoiceMultiple(story, saphiraBuffer.get(3),
                List.of("James Bond", "Meister Yoda", "Meister Yoda"),
                List.of(GadgetEnum.BOWLER_BLADE, GadgetEnum.FOG_TIN, GadgetEnum.ROCKET_PEN));
        assertRequestItemChoiceMultiple(story, saphiraBuffer.get(4),
                List.of("James Bond", "James Bond", "Meister Yoda"),
                List.of(GadgetEnum.GAS_GLOSS, GadgetEnum.MOTHBALL_POUCH, GadgetEnum.ROCKET_PEN));

        assertRequestItemChoiceMultiple(story, walterBuffer.get(3), List.of("James Bond", "Meister Yoda", "James Bond"),
                List.of(GadgetEnum.CHICKEN_FEED, GadgetEnum.NUGGET, GadgetEnum.MOTHBALL_POUCH));
        assertRequestItemChoiceMultiple(story, walterBuffer.get(4),
                List.of("James Bond", "Meister Yoda", "Meister Yoda"),
                List.of(GadgetEnum.LASER_COMPACT, GadgetEnum.MAGNETIC_WATCH, GadgetEnum.POISON_PILLS));
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Run a preset")
    public void test_runPreset() throws IOException {
        // Write the tale:
        StoryBoard storyBoard = new StoryBoard().execute("SET p1 saphira", "SET p2 dieter").preset("complete-drafting");
        Story story = storyBoard.build();

        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer dieterBuffer = dieter.getMessages();

        Assertions.assertTrue(saphiraBuffer.size() >= 12,
                "Saphira: Should have gotten at least 12 messages: Meta, HelloReply, GameStarted, 8*RequestItemChoice, RequestEquipmentChoice, but: "
                        + saphiraBuffer);

        Assertions.assertTrue(saphiraBuffer.size() >= 12,
                "Dieter: Should have gotten at least 12 messages: Meta, HelloReply, GameStarted, 8*RequestItemChoice, RequestEquipmentChoice, but: "
                        + dieterBuffer);

        // The checks have been deemed obsolete, as there is no way this can be checked
        // without relying on the further controllers - but they will be checked with
        // any main game phase tests anyway
    }

    private void assertRequestItemChoiceMultiple(Story story, String message, List<String> characterNames,
            List<GadgetEnum> expectedGadgets) {
        MessageContainer container = GameDataGson.getContainer(message);
        Assertions.assertNotNull(container, "Message should be in valid containerformat (" + message + ").");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, container.getType(),
                "Message should have the requestItemChoice-type (" + message + ").");

        RequestItemChoiceMessage requestItemChoiceMessage = GameDataGson.fromJson(message,
                RequestItemChoiceMessage.class);

        // Decode characters
        List<UUID> expectedCharacters = characterNames.stream().map(x -> story.validCharacter(x).get())
                .collect(Collectors.toList());

        Assertions.assertEquals(expectedGadgets, requestItemChoiceMessage.getOfferedGadgets(),
                "GagetEnums should be proposed as expected");
        Assertions.assertEquals(expectedCharacters, requestItemChoiceMessage.getOfferedCharacterIds(),
                "Characters should be proposed as expected");

    }

    @RepeatedTest(15)
    @Tag("Util")
    @DisplayName("[Story] should not crash for timer")
    public void test_shouldNotCrashForTimer() throws IOException {
        try {
            StoryBoard taleBoard = new StoryBoard().buffered().executeInternalFile("stories/nocrash.story");
            Story story = taleBoard.build();

            DummyClient walter = story.getClient("walter");
            walter.assureMessages(7, 100);
            Assertions.assertTrue(walter.getMessages().size() >= 7);
        } catch (StoryException ignored) {
            // we do not care, as the timeout is to low for the story to complete...
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] should not crash for Dos-Attack")
    public void test_shouldNotCrashForDos() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().buffered().hello("saphira", RoleEnum.PLAYER).hello("walter",
                RoleEnum.SPECTATOR);
        board.dos("saphira", MessageTypeEnum.REQUEST_META_INFORMATION, 50, 0);
        board.dos("walter", MessageTypeEnum.REQUEST_META_INFORMATION, 45, 1);

        Story tale = board.build();

        Thread.sleep(20);
        Assertions.assertNotNull(tale.getClient("saphira"));
    }

    @Test
    @Tag("Util")
    @DisplayName("[StoryAuthor] Just test if it is read correctly")
    public void test_readOfStoryAuthor() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().buffered().sleepless().executeInternalFile("stories/author/example.story");

        Story story = board.build();
        DummyClient jeff = story.getClient("jeff");
        jeff.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE);
        Assertions.assertEquals(4, jeff.getMessages().size(),
                "Should have gotten only 4 messages, but: " + jeff.getMessages());

        story.getClient("Petterson").getMessages().assertTypes(MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED);

        DummyClient walter = story.getClient("walter");
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.META_INFORMATION);
        Assertions.assertEquals(6, walter.getMessages().size(),
                "Should have gotten only 6 messages, but: " + walter.getMessages());
    }

    @Test
    @Tag("Util")
    @DisplayName("[StoryAuthor] Just test if it is read correctly - a more complex variant")
    public void test_readOfStoryAuthor2() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().buffered().sleepless().executeInternalFile("stories/author/example2.story");

        Story story = board.build();
        DummyClient jeff = story.getClient("jeff");
        jeff.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE);
        Assertions.assertEquals(4, jeff.getMessages().size(),
                "Should have gotten only 5 messages, but: " + jeff.getMessages());

        story.getClient("Petterson").getMessages().assertTypes(MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED);

        DummyClient walter = story.getClient("walter");
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.META_INFORMATION);
        Assertions.assertEquals(64, walter.getMessages().size(),
                "Should have gotten only 64 messages, but: " + walter.getMessages());
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Test equipment command")
    public void test_equipment() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().sleepless().hello("saphira", RoleEnum.PLAYER)
                .hello("walter", RoleEnum.SPECTATOR).hello("dieter", RoleEnum.PLAYER).preset(Presets.SKIP_DRAFTING)
                .awake().sleep(10);

        Story story = board.build();

        // validate that we have a equipment from both
        DraftingPhaseController controller = story.getController().getDraftingPhaseController();
        Assertions.assertNotNull(controller.getPlayerOneEquipment(), "Should be equipped now.");
        Assertions.assertNotNull(controller.getPlayerTwoEquipment(), "Should be equipped now.");
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Test equipment command - with explicit target")
    public void test_equipmentExplicit() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().buffered().sleepless()
                .executeInternalFile("stories/drafting-phase/equipment.story");

        Story story = board.build();

        board.awake().build();

        // validate that we have a equipment from both
        DraftingPhaseController controller = story.getController().getDraftingPhaseController();
        Assertions.assertNotNull(controller.getPlayerOneEquipment(), "Should be equipped now.");
        Assertions.assertNotNull(controller.getPlayerTwoEquipment(), "Should be equipped now.");

        // as we now the equipment (including UUIDs), lets rebuild it:
        DraftingEquipment saphiraEquipment = new DraftingEquipment();
        saphiraEquipment.put(UUID.fromString("bfd5972b-0604-4fb6-a939-df029808318a"),
                List.of(GadgetEnum.GRAPPLE, GadgetEnum.GAS_GLOSS, GadgetEnum.POISON_PILLS)); // Schleim
        saphiraEquipment.put(UUID.fromString("0c228ae2-974b-482e-ae23-9856385a3076"), Collections.emptyList()); // Mister
                                                                                                                // Y
        saphiraEquipment.put(UUID.fromString("7e42a99b-6112-4c65-a164-114e0f85192c"), Collections.emptyList()); // Mister
                                                                                                                // X
        saphiraEquipment.put(UUID.fromString("afafa139-60a1-48f8-aff5-0778b95db261"),
                List.of(GadgetEnum.WIRETAP_WITH_EARPLUGS)); // Dieter

        DraftingEquipment dieterEquipment = new DraftingEquipment();
        dieterEquipment.put(UUID.fromString("7b393e7f-8565-45c5-8380-a27753e3f176"),
                List.of(GadgetEnum.JETPACK, GadgetEnum.MOTHBALL_POUCH, GadgetEnum.POCKET_LITTER,
                        GadgetEnum.DIAMOND_COLLAR, GadgetEnum.MIRROR_OF_WILDERNESS)); // Otto
        dieterEquipment.put(UUID.fromString("008cc018-3ae6-4100-ae6c-62ce36ebf60c"), List.of(GadgetEnum.ROCKET_PEN)); // James
                                                                                                                      // Bond

        DraftingEquipment saphiraGot = controller.getPlayerOneEquipment();
        DraftingEquipment dieterGot = controller.getPlayerTwoEquipment();
        assertEquipment(saphiraEquipment, saphiraGot);
        assertEquipment(dieterEquipment, dieterGot);
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Example-Scenario")
    public void test_usePredefinedScenario() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().buffered()
                .execute("CONFIG_INJECT scenario internal scenarios/thepits.scenario");

        Story story = board.build();
        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("scenarios/thepits.scenario"),
                Scenario.class);
        Assertions.assertEquals(story.getConfiguration().getScenario(), scenario, "Should be as set");
    }

    private void assertEquipment(DraftingEquipment expected, DraftingEquipment got) {
        Assertions.assertEquals(expected.size(), got.size(), "Should have all chars, but: " + got);

        for (Entry<UUID, List<GadgetEnum>> entry : expected.entrySet()) {
            final UUID id = entry.getKey();
            final List<GadgetEnum> value = entry.getValue();
            final List<GadgetEnum> gotValue = got.get(id);
            Assertions.assertTrue(got.containsKey(id), "Should contain: " + id);
            Assertions.assertEquals(value.size(), gotValue.size(), "Same choices");
            Assertions.assertTrue(value.containsAll(gotValue),
                    "Should have all of: " + value + ". But was: " + gotValue);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Math operations")
    public void test_simpleMath() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().executeInternalFile("stories/extras/math-evaluation.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        expected.put("setTest", "hello");
        expected.put("setTest2", "wuhuhello-this is funny: hello");
        expected.put("zero", "0");
        expected.put("one", "1");
        // add
        expected.put("testAddTwo", "2");
        expected.put("testAddOne", "1");
        expected.put("testAddTwo2", "2");
        expected.put("testAddTwo3", "2");
        expected.put("testAddOne2", "1");
        expected.put("testAddZero", "0");
        // sub
        expected.put("testSubZero", "0");
        expected.put("testSubZero2", "0");
        expected.put("testSubZero3", "0");
        expected.put("testSubZero4", "0");
        expected.put("testSubOne", "1");
        expected.put("testSubMinusOne", "-1");
        // mul
        expected.put("testMulZero", "0");
        expected.put("testMulZero2", "0");
        expected.put("testMulOne", "1");
        expected.put("testMulOne2", "1");
        expected.put("testMulTwo", "2");
        expected.put("testMulFortyTwo", "42");
        // div
        expected.put("testDivZero", "0");
        expected.put("testDivOne", "1");
        expected.put("testDivOne2", "1");
        expected.put("testDivTwo", "2");
        expected.put("testDivTwentyOne", "21");
        // mod
        expected.put("testModZero", "0");
        expected.put("testModZero2", "0");
        expected.put("testModZero3", "0");
        expected.put("testModZero4", "0");
        expected.put("testModFour", "4");
        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp), "Should be as set for expandable: " + exp);

        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Iter operation")
    public void test_iterOperation() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().executeInternalFile("stories/extras/iter.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        expected.put("setTest", "hello");
        expected.put("testSimple", "1");
        expected.put("iterI", "5");
        expected.put("shouldBe2", "2");
        expected.put("i0", "0");
        expected.put("i1", "1");
        expected.put("i2", "2");
        expected.put("iterINested", "15");
        expected.put("iterNestedHold", "4.2");
        expected.put("shallBe50", "50");
        expected.put("shallBe50Two", "52");

        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp), "Should be as set for expandable: " + exp);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Set operation(s)")
    public void test_setOperation() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().executeInternalFile("stories/extras/set-evaluation.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        expected.put("setTest", "hello");
        expected.put("shouldBeTrue", "1");
        expected.put("shouldBeTrue2", "1");
        expected.put("shouldBeTrue3", "1");
        expected.put("shouldBeTrue4", "1");
        expected.put("shouldBeTrue5", "1");
        expected.put("shouldBeFalse", "0");
        expected.put("shouldBeFalse2", "0");
        expected.put("shouldBeFalse3", "0");
        expected.put("shouldBeFalse4", "0");
        expected.put("shouldBeFalse5", "0");
        expected.put("shouldBeFalse6", "0");
        expected.put("shouldBeFalse7", "0");
        expected.put("addOne", "ADD 1");
        expected.put("shouldBe42", "42");
        expected.put("walter", "6");
        expected.put("#1", "<unknown>");
        expected.put("inc", "ADD 1 ${eval:#1} ${#1}");
        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp, "<unknown>"),
                    "Should be as set for expandable: " + exp);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] If(-Else) operation(s)")
    public void test_ifOperation() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().executeInternalFile("stories/extras/if-evaluation.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        expected.put("setTest", "hello");
        expected.put("checkTrue", "1");
        expected.put("check", "1");
        expected.put("checkTrue2", "1");
        expected.put("checkFalse", "0");
        expected.put("true", "true");
        expected.put("anotherCheck", "0");
        expected.put("checkTrue3", "1");
        expected.put("checkInc", "5");

        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp), "Should be as set for expandable: " + exp);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Turing-Machine")
    public void test_turingMachine() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().set("doDebug", false).executeInternalFile("stories/extras/turing.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        // Final state should be ze
        expected.put("@z", "ze");
        // wanted state should be ze -- the turing machine might accept this with an
        // set_eq
        expected.put("@e", "ze");
        // One step to the left on end
        expected.put("@p", "10");
        expected.put("finalBoard", "HELLO WORLD");
        // The word should have been detected as valid
        expected.put("validEnd", "1");

        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp), "Should be as set for expandable: " + exp);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Turing-Machine shortened")
    public void test_turingMachine2() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().set("doDebug", true).executeInternalFile("stories/extras/turing2.story");
        // Expand and validate expansion :)
        Map<String, String> expected = new HashMap<>();
        // Final state should be ze
        expected.put("@z", "ze");
        // wanted state should be ze -- the turing machine might accept this with an
        // set_eq
        expected.put("@e", "ze");
        // One step to the left on end
        expected.put("@p", "10");
        expected.put("finalBoard", "HELLO WORLD");
        // The word should have been detected as valid
        expected.put("validEnd", "1");

        // test:
        for (Entry<String, String> expect : expected.entrySet()) {
            final String exp = "${" + expect.getKey() + "}";
            Assertions.assertEquals(expect.getValue(), board.expand(exp), "Should be as set for expandable: " + exp);
        }
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Trigger game Over")
    public void test_gameOverTrigger() throws IOException, InterruptedException {
        AtomicBoolean gameOverFlag = new AtomicBoolean(false);
        StoryBoard board = new StoryBoard().onGameOver((ignored) -> gameOverFlag.set(true));
        board.build();
        Assertions.assertFalse(gameOverFlag.get(), "Flag should be false on start");
        // play one game until game over :D
        board.preset(Presets.FULL_GAME);
        Assertions.assertTrue(gameOverFlag.get(), "Flag should be true on end");
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Buffering")
    public void test_buffering() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().execute("SET x 0", "SET y 42")//
                .buffered()
                .execute("SET x 32", "SET_EQ y 42 yIs42", "IF_NOT ${yIs42}:", "SET y 33", "ELSE", "SET y 21", "FI");

        // x = 0; y = 42
        Assertions.assertEquals("0", board.expandVariable("x", "invalid"), "x shall be zero");
        Assertions.assertEquals("42", board.expandVariable("y", "invalid"), "y shall be 42");

        Assertions.assertEquals("invalid", board.expandVariable("newS", "invalid"), "newS shall be invalid");

        board.execute("SET newS supderduper");

        Assertions.assertEquals("invalid", board.expandVariable("newS", "invalid"), "newS shall be invalid");

        // switch to unbuffered and flush buffer
        board.unbuffered();

        // x = 32; y = 21
        Assertions.assertEquals("32", board.expandVariable("x", "invalid"), "x shall be zero");
        Assertions.assertEquals("33", board.expandVariable("y", "invalid"), "y shall be 33");
        Assertions.assertEquals("supderduper", board.expandVariable("newS", "invalid"), "newS shall be supderduper");

        board.execute("SET newS evenbetter");
        Assertions.assertEquals("evenbetter", board.expandVariable("newS", "invalid"), "newS shall be evenbetter");
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Build on Delay")
    public void test_buildOnDelay() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().execute("SET x 0", "SET y 42")//
                .buffered()
                .execute("SET x 32", "SET_EQ y 42 yIs42", "IF_NOT ${yIs42}:", "SET y 33", "ELSE", "SET y 21", "FI");

        // x = 0; y = 42
        Assertions.assertEquals("0", board.expandVariable("x", "invalid"), "x shall be zero");
        Assertions.assertEquals("42", board.expandVariable("y", "invalid"), "y shall be 42");

        Assertions.assertEquals("invalid", board.expandVariable("newS", "invalid"), "newS shall be invalid");

        board.execute("SET newS supderduper");

        Assertions.assertEquals("invalid", board.expandVariable("newS", "invalid"), "newS shall be invalid");

        // switch to a delayed build mode
        board.build(1);

        // x = 32; y = 21
        Assertions.assertEquals("32", board.expandVariable("x", "invalid"), "x shall be zero");
        Assertions.assertEquals("33", board.expandVariable("y", "invalid"), "y shall be 33");
        Assertions.assertEquals("supderduper", board.expandVariable("newS", "invalid"), "newS shall be supderduper");

        board.execute("SET newS evenbetter");
        Assertions.assertEquals("supderduper", board.expandVariable("newS", "invalid"), "newS shall be supderduper");
    }

    @Test
    @Tag("Util")
    @DisplayName("[Story] Fibonacci recursive")
    public void test_fibonacci() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard().executeInternalFile("stories/extras/fibonacci.story");
        Assertions.assertEquals("144", board.expandVariable("result1", "invalid"), "fib(12) = 144");
        Assertions.assertEquals("8", board.expandVariable("result2", "invalid"), "fib(6) = 8");
        Assertions.assertEquals("233", board.expandVariable("result3", "invalid"), "fib(13) = 233");
    }

}