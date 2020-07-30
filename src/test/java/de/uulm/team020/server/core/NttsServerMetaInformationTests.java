package de.uulm.team020.server.core;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.validation.GameDataGson;

/**
 * Tests the MetaInformation-Message Handling for some default keys and
 * permission-control. Won't check if the values (like remaining time) are
 * correct on a semantic-level, this will be checked by the tests, that check
 * the functionality of these routines.
 *
 * @author Florian Sihler
 * @version 1.0, 04/07/2020
 */
@Tag("Messages")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NttsServerMetaInformationTests {

    public static DummyNttsController configurationSlave;

    @BeforeAll
    public static void initConfigSlave() throws IOException {
        Configuration configuration = new Configuration();
        DummyNttsController.setConfigDefaults(configuration);
        configurationSlave = new DummyNttsController(configuration);
    }

    public static Stream<Arguments> generate_illegalKeys() {
        return Stream.of(Arguments.arguments((Object) new String[] { "IchBinKeinSchlüssel" }),
                Arguments.arguments((Object) new String[] { "SchlüsselA", "SchlüsselB", "SchlüsselC" }));
    }

    @ParameterizedTest
    @Tag("MetaInformation")
    @Order(1)
    @DisplayName("[MetaInformation] Handle illegal keys")
    @MethodSource("generate_illegalKeys")
    public void test_illegalKeys(String[] keys) throws IOException, MessageException {
        // Get us a client
        Story story = new StoryBoard().execute("HELLO walter PLAYER")
                .execute("META walter \"" + String.join(", ", keys) + "\"").build();
        DummyClient walter = story.getClient("walter");
        DummySendMessagesBuffer walterBuffer = walter.assureMessages(3, 250);

        Assertions.assertEquals(3, walterBuffer.size(), "Three messages: Meta, HelloReply and MetaInfo");

        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(walterBuffer.get(1)),
                "Should be HelloReply, but was: " + walterBuffer.getFirst());
        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(walterBuffer.get(2)),
                "Should be MetaInformation, but was: " + walterBuffer.get(1));

        MetaInformationMessage metaInformationMessage = GameDataGson.fromJson(walterBuffer.get(2),
                MetaInformationMessage.class);

        Map<String, Object> information = metaInformationMessage.getInformation();
        Assertions.assertEquals(keys.length, information.size(), "No information should get consumed.");

        for (String key : keys) {
            Assertions.assertNull(information.get(key), "The value for key: " + key + " should be given and null");
        }
    }

    public static Stream<Arguments> generate_configKeys() {
        return Stream.of(
                Arguments.arguments(MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.SPECTATOR_NAMES }),
                        Map.of(MetaKeyEnum.SPECTATOR_NAMES.getKey(), new String[] { "josef", "roswita" })),
                Arguments.arguments(
                        MetaKeyEnum.toStringArray(
                                new MetaKeyEnum[] { MetaKeyEnum.SPECTATOR_COUNT, MetaKeyEnum.SPECTATOR_NAMES }),
                        Map.of(MetaKeyEnum.SPECTATOR_COUNT.getKey(), 2, MetaKeyEnum.SPECTATOR_NAMES.getKey(),
                                new String[] { "josef", "roswita" })),
                Arguments.arguments(MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.SPECTATOR_COUNT }),
                        Map.of(MetaKeyEnum.SPECTATOR_COUNT.getKey(), 2)),
                Arguments.arguments(
                        MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.GAME_REMAINING_PAUSE_TIME }),
                        Map.of(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), -1)),
                Arguments.arguments(MetaKeyEnum.toStringArray(
                        new MetaKeyEnum[] { MetaKeyEnum.GAME_REMAINING_PAUSE_TIME, MetaKeyEnum.SPECTATOR_COUNT }),
                        Map.of(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), -1, MetaKeyEnum.SPECTATOR_COUNT.getKey(),
                                2)),
                Arguments.arguments(
                        MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.GAME_REMAINING_PAUSE_TIME,
                                MetaKeyEnum.SPECTATOR_COUNT, MetaKeyEnum.CONFIGURATION_SCENARIO }),
                        Map.of(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), -1, MetaKeyEnum.SPECTATOR_COUNT.getKey(),
                                2, MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(),
                                configurationSlave.getConfiguration().getScenario())),
                Arguments.arguments(MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.CONFIGURATION_SCENARIO }),
                        Map.of(MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(),
                                configurationSlave.getConfiguration().getScenario())),
                Arguments.arguments(
                        MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.CONFIGURATION_MATCHCONFIG }),
                        Map.of(MetaKeyEnum.CONFIGURATION_MATCHCONFIG.getKey(),
                                configurationSlave.getConfiguration().getMatchconfig())),
                Arguments.arguments(
                        MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.CONFIGURATION_MATCHCONFIG,
                                MetaKeyEnum.CONFIGURATION_SCENARIO }),
                        Map.of(MetaKeyEnum.CONFIGURATION_MATCHCONFIG.getKey(),
                                configurationSlave.getConfiguration().getMatchconfig(),
                                MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(),
                                configurationSlave.getConfiguration().getScenario())),
                Arguments.arguments(
                        MetaKeyEnum
                                .toStringArray(new MetaKeyEnum[] { MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION }),
                        Map.of(MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION.getKey(),
                                configurationSlave.getConfiguration().getCharacters())),
                Arguments.arguments(MetaKeyEnum.toStringArray(new MetaKeyEnum[] { MetaKeyEnum.GAME_REMAINING_PAUSE_TIME,
                        MetaKeyEnum.SPECTATOR_COUNT, MetaKeyEnum.SPECTATOR_NAMES, MetaKeyEnum.CONFIGURATION_SCENARIO,
                        MetaKeyEnum.CONFIGURATION_MATCHCONFIG, MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION }),
                        Map.of(MetaKeyEnum.GAME_REMAINING_PAUSE_TIME.getKey(), -1, MetaKeyEnum.SPECTATOR_COUNT.getKey(),
                                2, MetaKeyEnum.SPECTATOR_NAMES.getKey(), new String[] { "josef", "roswita" },
                                MetaKeyEnum.CONFIGURATION_SCENARIO.getKey(),
                                configurationSlave.getConfiguration().getScenario(),
                                MetaKeyEnum.CONFIGURATION_MATCHCONFIG.getKey(),
                                configurationSlave.getConfiguration().getMatchconfig(),
                                MetaKeyEnum.CONFIGURATION_CHARACTER_INFORMATION.getKey(),
                                configurationSlave.getConfiguration().getCharacters()))

        );
    }

    @ParameterizedTest
    @Tag("MetaInformation")
    @Order(2)
    @DisplayName("[MetaInformation] Handle configuration keys")
    @MethodSource("generate_configKeys")
    public void test_configKeys(String[] keys, Map<String, Object> expected) throws IOException, MessageException {
        // Get us a client
        Story story = new StoryBoard().execute("HELLO emerald PLAYER").execute("HELLO josef SPECTATOR")
                .execute("HELLO roswita SPECTATOR").execute("META emerald \"" + String.join(", ", keys) + "\"").build();
        DummyClient emerald = story.getClient("emerald");

        DummySendMessagesBuffer emeraldBuffer = emerald.assureMessages(3, 250);

        Assertions.assertEquals(3, emeraldBuffer.size(), "Three messages: Meta, HelloReply and MetaInfo");

        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, GameDataGson.getType(emeraldBuffer.get(1)),
                "Should be HelloReply, but was: " + emeraldBuffer.getFirst());
        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(emeraldBuffer.get(2)),
                "Should be MetaInformation, but was: " + emeraldBuffer.get(2));

        MetaInformationMessage metaInformationMessage = GameDataGson.fromJson(emeraldBuffer.get(2),
                MetaInformationMessage.class);
        Map<String, Object> information = metaInformationMessage.getInformation();

        Assertions.assertEquals(expected.size(), information.size(), "Should have the same keys");

        for (Entry<String, Object> entry : expected.entrySet()) {
            Object wanted = entry.getValue();
            if (wanted.getClass() == String[].class) {
                Assertions.assertArrayEquals((String[]) wanted, (String[]) information.get(entry.getKey()),
                        "Result should be as expected");
            } else if (wanted.getClass() == CharacterInformation[].class) {
                // jeah, this is lazy i know, but otherwise i cannot check if the uuids are
                // correct :/
                // they would be regenerated
                Assertions.assertArrayEquals(story.getConfiguration().getCharacters(),
                        (CharacterInformation[]) information.get(entry.getKey()), "Result should be as expected");
            } else {
                Assertions.assertEquals(wanted, information.get(entry.getKey()), "Result should be as expected");
            }
        }
    }

    @Test
    @Tag("MetaInformation")
    @Order(3)
    @DisplayName("[MetaInformation] Handle faction keys before game started")
    public void test_factionBeforeGameStart() throws IOException, MessageException {
        // Get one client and spectators so no game-started will be issued
        StoryBoard board = new StoryBoard().execute("HELLO jamina SPECTATOR", "HELLO saphira PLAYER");
        Story story = board.build();

        DummyClient jamina = story.getClient("jamina");
        DummyClient saphira = story.getClient("saphira");

        board.execute(
                "META saphira \"Faction.Player1, Faction.Player2, Faction.Neutral, Gadgets.Player1, Gadgets.Player2\"");
        assertMetaInformation(saphira.getMessages().getLast(), new UUID[][] { null, null, null },
                new GadgetEnum[][] { null, null });

        board.execute(
                "META jamina \"Faction.Player1, Faction.Player2, Faction.Neutral, Gadgets.Player1, Gadgets.Player2\"");
        assertMetaInformation(jamina.getMessages().getLast(), new UUID[][] { null, null, null },
                new GadgetEnum[][] { null, null });

    }

    private void assertMetaInformation(String metaMessage, UUID[][] expectedCharacters, GadgetEnum[][] expectedGadgets)
            throws MessageException {
        MessageContainer container = GameDataGson.getContainer(metaMessage);
        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, container.getType(),
                "Should be MetaInformationMessage, but: " + metaMessage);

        MetaInformationMessage message = GameDataGson.fromJson(metaMessage, MetaInformationMessage.class);

        Map<String, Object> information = message.getInformation();

        final MetaKeyEnum[] keys = new MetaKeyEnum[] { MetaKeyEnum.FACTION_PLAYER1, MetaKeyEnum.FACTION_PLAYER2,
                MetaKeyEnum.FACTION_NEUTRAL };
        for (int i = 0; i < keys.length; i++) {
            Assertions.assertTrue(information.containsKey(keys[i].getKey()),
                    "The information should have the key: " + keys[i].getKey());
            UUID[] got = (UUID[]) message.getInformation().get(keys[i].getKey());
            Assertions.assertArrayEquals(expectedCharacters[i], got, "Keys should be as expected: " + metaMessage);
        }

        final MetaKeyEnum[] ckeys = new MetaKeyEnum[] { MetaKeyEnum.GADGETS_PLAYER1, MetaKeyEnum.GADGETS_PLAYER2 };
        for (int i = 0; i < ckeys.length; i++) {
            Assertions.assertTrue(information.containsKey(ckeys[i].getKey()),
                    "The information should have the key: " + ckeys[i].getKey());
            GadgetEnum[] got = (GadgetEnum[]) message.getInformation().get(ckeys[i].getKey());
            Assertions.assertArrayEquals(expectedGadgets[i], got, "Keys should be as expected: " + metaMessage);
        }
    }

    @Test
    @Tag("MetaInformation")
    @Order(4)
    @DisplayName("[MetaInformation] Handle faction and gadget keys on game started, no choice")
    public void test_factionGadgetAfterGameStartNoChoice() throws IOException, MessageException {
        StoryBoard board = new StoryBoard().execute("HELLO jamina PLAYER", "HELLO saphira PLAYER",
                "HELLO peter SPECTATOR");
        Story story = board.build();

        DummyClient jamina = story.getClient("jamina");
        DummyClient saphira = story.getClient("saphira");
        DummyClient peter = story.getClient("peter");

        board.execute(
                "META saphira \"Faction.Player1, Faction.Player2, Faction.Neutral, Gadgets.Player1, Gadgets.Player2\"");
        assertMetaInformation(saphira.getMessages().getLast(), new UUID[][] { null, new UUID[0], null },
                new GadgetEnum[][] { null, new GadgetEnum[0] });

        board.execute(
                "META jamina \"Faction.Player1, Faction.Player2, Faction.Neutral, Gadgets.Player1, Gadgets.Player2\"");
        assertMetaInformation(jamina.getMessages().getLast(), new UUID[][] { new UUID[0], null, null },
                new GadgetEnum[][] { new GadgetEnum[0], null });

        // peter can ask any information, as he is just an amazing nice guy, but there
        // is no neutral here
        board.execute(
                "META peter \"Faction.Player1, Faction.Player2, Faction.Neutral, Gadgets.Player1, Gadgets.Player2\"");
        assertMetaInformation(peter.getMessages().getLast(), new UUID[][] { new UUID[0], new UUID[0], null },
                new GadgetEnum[][] { new GadgetEnum[0], new GadgetEnum[0] });
    }

    @Test
    @Tag("MetaInformation")
    @Order(5)
    @DisplayName("[MetaInformation] Handle faction and gadget keys on game started,done some choices")
    public void test_factionGadgetChoiceAfterSelect() throws IOException, MessageException, InterruptedException {
        StoryBoard board = new StoryBoard().execute(
                /* We just need four injections, as we do not check everything */
                "CONFIG_INJECT next-proposal saphira \"James Bond, Meister Yoda, Meister Yoda, bowler_blade, fog_tin, rocket_pen\"",
                "CONFIG_INJECT next-proposal saphira \"James Bond, James Bond, Meister Yoda, gas_gloss, mothball_pouch, rocket_pen\"",
                "CONFIG_INJECT next-proposal jamina \"James Bond, Meister Yoda, James Bond,   chicken_feed, nugget, mothball_pouch\"",
                "CONFIG_INJECT next-proposal jamina \"James Bond, Meister Yoda, Meister Yoda, laser_compact, magnetic_watch, poison_pills\"",
                "HELLO jamina PLAYER", "HELLO saphira PLAYER", "HELLO peter SPECTATOR");
        Story story = board.build();

        DummyClient peter = story.getClient("peter");

        // Now jamina selects 'Meister Yoda' and 'magnetic_watch'
        // saphira wants 'James Bond' and 'Meister Yoda'
        board.execute("ITEM jamina \"Meister Yoda\"", "ITEM saphira \"James Bond\"", "ITEM saphira \"Meister Yoda\"",
                "ITEM jamina magnetic_watch");

        // No peter want's to know what both of them
        board.execute(
                "META peter \"Spectator.Count, Faction.Player1, Gadgets.Player1, Faction.Player2, Gadgets.Player2, Faction.Neutral, Spectator.Names\"");

        String message = peter.getMessages().getLast();

        Assertions.assertEquals(MessageTypeEnum.META_INFORMATION, GameDataGson.getType(message),
                "Should be meta-information, but: " + message);

        MetaInformationMessage meta = GameDataGson.fromJson(message, MetaInformationMessage.class);

        Map<String, Object> information = meta.getInformation();

        for (MetaKeyEnum key : new MetaKeyEnum[] { MetaKeyEnum.SPECTATOR_NAMES, MetaKeyEnum.SPECTATOR_COUNT,
                MetaKeyEnum.FACTION_NEUTRAL, MetaKeyEnum.FACTION_PLAYER1, MetaKeyEnum.FACTION_PLAYER2,
                MetaKeyEnum.GADGETS_PLAYER1, MetaKeyEnum.GADGETS_PLAYER2 }) {
            Assertions.assertTrue(information.containsKey(key.getKey()), "Should contain the key: " + key);
        }

        Assertions.assertNull(information.get(MetaKeyEnum.FACTION_NEUTRAL.getKey()),
                "There is no neutral faction, but: " + message);

        Configuration configuration = story.getConfiguration();

        Assertions.assertEquals(1, information.get(MetaKeyEnum.SPECTATOR_COUNT.getKey()),
                "There is only Peter watching, but: " + message);
        Assertions.assertArrayEquals(new String[] { "peter" },
                (String[]) information.get(MetaKeyEnum.SPECTATOR_NAMES.getKey()),
                "There is only Peter watching, but: " + message);

        Assertions.assertArrayEquals(new UUID[] { configuration.getCharacterId("Meister Yoda") },
                (UUID[]) information.get(MetaKeyEnum.FACTION_PLAYER1.getKey()),
                "Player 1 did choose one character, but: " + message);
        Assertions.assertArrayEquals(new GadgetEnum[] { GadgetEnum.MAGNETIC_WATCH },
                (GadgetEnum[]) information.get(MetaKeyEnum.GADGETS_PLAYER1.getKey()),
                "Player 1 did choose one gadget, but: " + message);

        Assertions.assertArrayEquals(
                new UUID[] { configuration.getCharacterId("James Bond"), configuration.getCharacterId("Meister Yoda") },
                (UUID[]) information.get(MetaKeyEnum.FACTION_PLAYER2.getKey()),
                "Player 2 did choose two characters, but: " + message);
        Assertions.assertArrayEquals(new GadgetEnum[0],
                (GadgetEnum[]) information.get(MetaKeyEnum.GADGETS_PLAYER2.getKey()),
                "Player 2 did not choose any gadget, but: " + message);
    }

    @Test
    @Tag("MetaInformation")
    @Order(6)
    @DisplayName("[MetaInformation] Request all Meta in game")
    public void test_requestAllMetaInGame() throws IOException, MessageException, InterruptedException {
        StoryBoard board = new StoryBoard().preset(Presets.COMPLETE_DRAFTING);
        Story story = board.build();
        board.eraseAllClientMemories();

        // someone shall do something :
        board.execute("OPERATION random retire <ignored>");
        // Now p1 and p2 want to request _all_ MetaInformation there is :D
        board.meta(board.expandVariable("p1"), MetaKeyEnum.values());
        board.meta(board.expandVariable("p2"), MetaKeyEnum.values());

        DummyClient p1 = story.getClient(board.expandVariable("p1"));
        DummyClient p2 = story.getClient(board.expandVariable("p2"));
        // TODO: test more, the only thing we are interested in is, that this does not
        // throw an error
        p1.getMessages().assertLastTypes(MessageTypeEnum.META_INFORMATION);
        p2.getMessages().assertLastTypes(MessageTypeEnum.META_INFORMATION);
    }

}
