package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.InternalResources;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.ReplayMessage;
import de.uulm.team020.networking.messages.RequestReplayMessage;
import de.uulm.team020.networking.messages.StatisticsMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.game.phases.main.statistics.BooleanStatisticsBuilder;

/**
 * The goal of this file is to provide one run through a whole game using
 * stories to jump into the main phase which is injected in terms of round order
 * an than having characters do stuff, that seems "ok". Currently it will only
 * check some parts of a full game, it may be extended, as all other behaviour
 * gets checked else where
 */
@Tag("Full")
public class FullGameTest {
    private StoryBoard produceQuickstart() throws IOException {
        return produceQuickstart(new StoryBoard());
    }

    private StoryBoard produceQuickstart(StoryBoard board) throws IOException {
        return board.hello("Josef", RoleEnum.SPECTATOR).executeInternalFile("stories/full-game-quickstart.story")
                .hello("walter", RoleEnum.SPECTATOR);
    }

    private static MessageTypeEnum[] SKIP_DRAFTING_NO_META = { MessageTypeEnum.HELLO_REPLY,
            MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE };

    private static MessageTypeEnum[] SKIP_DRAFTING_PLAYER = { MessageTypeEnum.META_INFORMATION,
            MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE };

    private static MessageTypeEnum[] DRAFTING_ON_HALT_PLAYER = { MessageTypeEnum.META_INFORMATION,
            MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE };

    @RepeatedTest(4)
    @DisplayName("[Full] Drafting Phase")
    public void test_drafting() throws IOException {
        StoryBoard board = produceQuickstart();
        Story story = board.build();
        // If you may want to see the outcome:
        // System.out.println(story.getGameFieldController().getIslandMask())

        DummyClient josef = story.getClient("Josef");
        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");
        DummyClient walter = story.getClient("walter");

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        dieter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        josef.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED); // has none, as joined later! This is as the standard proposed it to be
    }

    @RepeatedTest(4)
    @DisplayName("[Full] Drafting Phase without Meta-Information message on reveal")
    public void test_draftingNoMeta() throws IOException {
        BackstoryBuilder builder = new BackstoryBuilder();
        builder.serverConfigUpdate("sendMetaOnConnectionOpen", false);
        StoryBoard board = produceQuickstart(builder.build());
        Story story = board.build();
        // If you may want to see the outcome:
        // System.out.println(story.getGameFieldController().getIslandMask())

        DummyClient josef = story.getClient("Josef");
        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");
        DummyClient walter = story.getClient("walter");

        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_NO_META, MessageTypeEnum.GAME_STATUS);
        dieter.getMessages().assertOnlyTypes(SKIP_DRAFTING_NO_META, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED);
        // has none, as joined later! This is as the standard proposed it to be
    }

    @RepeatedTest(4)
    @DisplayName("[Full] Drafting Phase with Meta-Information")
    public void test_draftingMetaInformation() throws IOException, MessageException {
        // haltInDrafting is an Expandable i have defined in the specific story
        StoryBoard board = produceQuickstart(new StoryBoard().set("haltInDrafting", true));
        Story story = board.build();
        // If you may want to see the outcome:
        // System.out.println(story.getGameFieldController().getIslandMask())

        DummyClient josef = story.getClient("Josef");
        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");
        DummyClient walter = story.getClient("walter");

        saphira.getMessages().assertTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertTypes(DRAFTING_ON_HALT_PLAYER);
        josef.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY);
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY);

        // josef wants to know the current choices of both players
        board.execute("META Josef Faction.Player1,Faction.Player2,Gadgets.Player1,Gadgets.Player2");

        MetaInformationMessage metaMessage = MessageContainer.getMessage(josef.getMessages().getLast());

        // Now we check the wanted Key-Value pairs as the story has enforced
        // them
        Map<String, Object> data = metaMessage.getInformation();
        // faction of p1:
        UUID[] charactersP1 = (UUID[]) data.get(MetaKeyEnum.FACTION_PLAYER1.getKey());
        assertFaction(story, charactersP1, "Saphira");
        // faction of p2
        UUID[] charactersP2 = (UUID[]) data.get(MetaKeyEnum.FACTION_PLAYER2.getKey());
        assertFaction(story, charactersP2, "Mister X", "Austauschbarer Agent Dieter 42", "Ein Wischmob");
        // gadgets p1
        GadgetEnum[] gadgetsP1 = (GadgetEnum[]) data.get(MetaKeyEnum.GADGETS_PLAYER1.getKey());
        assertGadgets(story, gadgetsP1, GadgetEnum.MOLEDIE, GadgetEnum.GRAPPLE, GadgetEnum.TECHNICOLOUR_PRISM,
                GadgetEnum.MAGNETIC_WATCH);
        // gadgets p2
        GadgetEnum[] gadgetsP2 = (GadgetEnum[]) data.get(MetaKeyEnum.GADGETS_PLAYER2.getKey());
        assertGadgets(story, gadgetsP2, GadgetEnum.NUGGET, GadgetEnum.GAS_GLOSS);
    }

    private void assertFaction(final Story story, UUID[] gotten, String... expected) {
        Assertions.assertEquals(expected.length, gotten.length,
                "Should have gotten as stated, as: " + Arrays.toString(expected));
        for (int i = 0; i < expected.length; i++) {
            final UUID characterId = gotten[i];
            CharacterInformation gotExpected = story.getConfiguration().getCharacter(expected[i]);
            Assertions.assertEquals(gotExpected.getId(), characterId,
                    "Client should have chosen: " + expected + " but chose: " + characterId);
        }
    }

    private void assertGadgets(final Story story, GadgetEnum[] gotten, GadgetEnum... expected) {
        Assertions.assertEquals(expected.length, gotten.length,
                "Should have gotten as stated, as: " + Arrays.toString(expected));
        for (int i = 0; i < expected.length; i++) {
            final GadgetEnum gadget = gotten[i];
            Assertions.assertEquals(expected[i], gadget,
                    "Client should have chosen: " + expected + " but chose: " + gadget);
        }
    }

    @RepeatedTest(4)
    @DisplayName("[Full] Drafting Phase with crash")
    public void test_draftingWithCrash() throws IOException, MessageException {
        // haltInDrafting is an Expandable i have defined in the specific story
        StoryBoard board = produceQuickstart(new StoryBoard().set("haltInDrafting", true));
        Story story = board.build();
        // If you may want to see the outcome:
        // System.out.println(story.getGameFieldController().getIslandMask())

        DummyClient josef = story.getClient("Josef");
        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");
        DummyClient walter = story.getClient("walter");

        saphira.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED);

        // let dieter complete his drafting:
        board.execute("ITEM dieter \"Spröder Senf\"", "ITEM dieter hairdryer", "ITEM dieter mirror_of_wilderness");

        // let saphira crash:
        board.crash("saphira");

        // now, saphira has crashed
        saphira.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_PAUSE);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE);

        // let saphira reconnect
        board.reconnect("saphira", "new-saphira");

        DummyClient newSaphira = story.getClient("new-saphira");

        saphira.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        newSaphira.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.REQUEST_ITEM_CHOICE);

        // now saphira wants to know the picks she has already done
        board.execute("META new-saphira Faction.Player1,Faction.Player2,Gadgets.Player1,Gadgets.Player2");

        MetaInformationMessage metaMessage = MessageContainer.getMessage(newSaphira.getMessages().getLast());
        Map<String, Object> data = metaMessage.getInformation();

        UUID[] charactersP1 = (UUID[]) data.get(MetaKeyEnum.FACTION_PLAYER1.getKey());
        assertFaction(story, charactersP1, "Saphira");
        // faction of p2
        UUID[] charactersP2 = (UUID[]) data.get(MetaKeyEnum.FACTION_PLAYER2.getKey());
        Assertions.assertNull(charactersP2, "We are p1 and not allowed to get them");
        // gadgets p1
        GadgetEnum[] gadgetsP1 = (GadgetEnum[]) data.get(MetaKeyEnum.GADGETS_PLAYER1.getKey());
        assertGadgets(story, gadgetsP1, GadgetEnum.MOLEDIE, GadgetEnum.GRAPPLE, GadgetEnum.TECHNICOLOUR_PRISM,
                GadgetEnum.MAGNETIC_WATCH);
        // gadgets p2
        GadgetEnum[] gadgetsP2 = (GadgetEnum[]) data.get(MetaKeyEnum.GADGETS_PLAYER2.getKey());
        Assertions.assertNull(gadgetsP2, "We are p1 and not allowed to get them");

        // let dieter select his equipment:
        board.execute(
                "EQUIP dieter \"Mister X,Austauschbarer Agent Dieter 42,NUGGET,GAS_GLOSS,MIRROR_OF_WILDERNESS,Ein Wischmob,HAIRDRYER,Spröder Senf\"");
        saphira.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        newSaphira.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.META_INFORMATION);

        // let saphira complete her drafting phase
        board.execute("ITEM new-saphira \"Hans Peter Otto\"", "ITEM new-saphira \"Schleim B. Olzen\"",
                "ITEM new-saphira fog_tin");
        saphira.getMessages().assertOnlyTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        josef.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        newSaphira.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE);

        // Now saphira may equip
        board.execute(
                "EQUIP new-saphira \"Hans Peter Otto,MOLEDIE,TECHNICOLOUR_PRISM,MAGNETIC_WATCH,Schleim B. Olzen,GRAPPLE,Saphira,FOG_TIN\"");

        // Letta game start one of the players may start, we do not care who
        // Therefore we do not assert only
        saphira.getMessages().assertTypes(DRAFTING_ON_HALT_PLAYER);
        dieter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_STATUS);
        josef.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_STATUS);
        newSaphira.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.META_INFORMATION,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE, MessageTypeEnum.GAME_STATUS);
    }

    @RepeatedTest(42)
    @DisplayName("[Full] First round some will move, game will end by collar.")
    public void test_firstRoundMovementsWithEnd() throws IOException, InterruptedException {
        StoryBoard board = produceQuickstart();
        Story story = board.build();
        // If you may want to see the outcome:
        // System.out.println(story.getGameFieldController().getIslandMask())

        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        // at the point of writing this stories are not capable of injecting
        // roulette-table chips but backstories are (of course they are ;D)
        // so we will modify the field for agent42 so he can gamble for sure
        // with an amount we know
        BackstoryBuilder builder = new BackstoryBuilder(board).unlock();
        builder.matchconfigUpdate("chipsToIpFactor", 5);
        builder.matchconfigUpdate("catIp", 200);
        builder.serverConfigUpdate("offerReplay", true);
        builder.serverConfigUpdate("liveView", false);
        Field rouletteTable = new Field(FieldStateEnum.ROULETTE_TABLE);
        rouletteTable.setChipAmount(8);
        builder.changeField(new Point(3, 3), rouletteTable);

        // it is the turn of agent42 so he wants to gamble ;)
        board.gamble("dieter", new Point(3, 3), 6);

        // one step to the right for dieters character
        board.moveRelative("dieter", new Point(1, 0));

        // dieter will now retire
        board.retire("dieter");

        // now it is saphira turn with saphira
        // walk
        board.moveRelative("saphira", new Point(-1, 0));

        // Just to show:
        board.move("saphira", new Point(5, 2));

        // saphira will now retire
        board.retire("saphira");

        // now saphira is allowed to move with hans peter otto
        // he is directly above the cat -- gift it hans otto, gift it.
        builder.giftGadgetToCharacter("Hans Peter Otto", GadgetEnum.DIAMOND_COLLAR);
        // here, you have it, no give it:
        board.moveRelative("saphira", new Point(0, 1));

        // game has ended, as saphira as gifted the collar with Hans Peter Otto

        // get last message of everybody -- as no one crashed this works fine
        for (DummyClient client : story.getClients()) {
            assertGameWonByCollar(client.getMessages().getLast(), saphira.getClientId());
        }

        // assure the collar-win was set
        BooleanStatisticsBuilder booleanStatistics = story.getGameFieldController().getStatisticsProvider()
                .getGiftedDiamondCollar();
        Assertions.assertTrue(booleanStatistics.getPlayerOneValue(), "Saphira has gifted the collar");
        Assertions.assertFalse(booleanStatistics.getPlayerTwoValue(), "Dieter did not");

        // now dieter wants a replay
        RequestReplayMessage request = new RequestReplayMessage(dieter.getClientId());
        dieter.send(request.toJson());
        ReplayMessage replay = MessageContainer.getMessage(dieter.getMessages().getLast());
        Assertions.assertEquals(MessageTypeEnum.REPLAY, replay.getType(), "Should be allowed as replay available.");
        Assertions.assertTrue(replay.getMessages().length > 0,
                "Should have recorded at least one message, but:" + replay);

        // story.getController().getAuthor().dump("Story")
    }

    private void assertGameWonByCollar(String statistics, UUID saphiraId) {
        StatisticsMessage message = MessageContainer.getMessage(statistics);
        Assertions.assertEquals(saphiraId, message.getWinner(),
                "Saphira should have won. But: " + message.getStatistics());
        Assertions.assertEquals(VictoryEnum.VICTORY_BY_IP, message.getReason(),
                "Saphira should have won by ip as gift is higher. But: " + message.getStatistics());
        Assertions.assertTrue(message.hasReplay(), "Should offer replay as set, but: " + message);
    }

    @Test
    @DisplayName("[PlagueMask] Test the execution of the plague-mask.")
    public void test_plagueMask() throws IOException, InterruptedException {

        StoryBoard board = new StoryBoard().hello("Josef", RoleEnum.SPECTATOR)
                .executeInternalFile("stories/main-phase/gadget-stories/anti-plague-mask.story")
                .hello("walter", RoleEnum.SPECTATOR);
        Story story = board.build();
        // The character identified by "Mister X" will own the plague mask
        // he is second in round
        DummyClient saphira = story.getClient("saphira");

        GameStatusMessage saphiraGS = MessageContainer
                .getMessage(saphira.getMessages().get(saphira.getMessageSize() - 2));

        for (Character character : saphiraGS.getState().getCharacters()) {
            if (character.getName().equals("Zackiger Zacharias"))
                Assertions.assertTrue(character.getAp() != 0 || character.getMp() != 0,
                        "Character 'Zackiger Zacharias' should have gotten ap or mp on last status but didn't: "
                                + character);
        }

        // we will damage mister x
        BackstoryBuilder builder = new BackstoryBuilder(board).unlock();
        builder.updateHpFor("Mister X", 50);

        Character misterX = builder.getCharacter("Mister X");

        // should be damaged
        Assertions.assertEquals(50, misterX.getHp(), "Shall be damaged by backstory.");

        // first of all saphira may operate with 'Zackiger Zacharias'
        // saphira will now retire
        board.retire("saphira");

        // This will give the turn to mister x - he may have 60 hp now
        // should be damaged
        Assertions.assertEquals(60, misterX.getHp(), "Should have gotten healed by the gadget.");

        // dieter wants to move one step down
        board.moveRelative("dieter", new Point(0, 1));
        Assertions.assertEquals(60, misterX.getHp(), "Should have not gotten healed twice.");

        // dieter wants to retire
        board.retire("dieter");
        Assertions.assertEquals(60, misterX.getHp(), "Should have not gotten healed twice.");

        // Now it is saphiras turn again with spröder senf
        Assertions.assertEquals(60, misterX.getHp(), "Should have not gotten healed twice.");

        // It will just retire
        board.retire("saphira");

        Assertions.assertEquals(60, misterX.getHp(), "Should have not gotten healed twice.");
        Assertions.assertTrue(misterX.getGadgetType(GadgetEnum.ANTI_PLAGUE_MASK).isPresent(),
                "'Mister X' shall still have the plague mask, but: " + misterX);
    }

    @Test
    @DisplayName("[InvalidOpening] Test an invalid opening.")
    public void test_invalidOpening() throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard();
        board.hello("Walter", RoleEnum.PLAYER);
        board.hello("Jens", RoleEnum.PLAYER);
        Story story = board.build();
        DummyClient guenther = new DummyClient(story.getController());
        guenther.send(new HelloMessage("Günther", RoleEnum.PLAYER).toJson());
        // we expect an already serving exception
        ErrorMessage message = MessageContainer.getMessage(guenther.getMessages().getLast());
        Assertions.assertEquals(ErrorTypeEnum.ALREADY_SERVING, message.getReason(),
                "Reason should be already serving for seat is already taken: " + message);
    }

    @ParameterizedTest
    @Tag("Util")
    @Tag("Full")
    @CsvSource(value = { "stories/re-telling/simple-retirement.story:saphira:0",
            "stories/re-telling/simple-retirement.story:saphira:0", "stories/re-telling/full-game1.story:Frau Holle:0",
            "stories/re-telling/full-game2.story:Ich:0",
            "stories/re-telling/full-game3.story:Nicht das X, das Sie suchen:0",
            "stories/re-telling/full-game4.story:Die siebte Sonate:0", "stories/re-telling/full-game5.story:Nicht-Du:0",
            "stories/re-telling/full-game6.story:Saphira:0", "stories/re-telling/full-game7.story:Brandstifter:0",
            "stories/re-telling/full-game8.story:Nicht-Du:1", "stories/re-telling/full-game9.story:Nicht-Du:1",
            "stories/re-telling/full-game10.story:Frodo:0", "stories/re-telling/full-game11.story:Du:1",
            "stories/re-telling/full-game12.story:Gustav:1", "stories/re-telling/full-game13.story:Saphira:1",
            "stories/re-telling/full-game14.story:Gustav:1", "stories/re-telling/full-game15.story:Udo Hinterberg:1",
            "stories/re-telling/full-game16.story:Der Helge:1", "stories/re-telling/full-game17.story:Der Helge:1",
            "stories/re-telling/full-game18.story:Adler ohne Softgetränk:1",
            "stories/re-telling/full-game19.story:Du:1", "stories/re-telling/full-game20.story:Du:0",
            "stories/re-telling/full-game21.story:Der Helge:0" }, delimiter = ':') //

    @DisplayName("[Story] Tell me the same Story (retirements)")
    public void test_tellMeTheSameStory(String storyPath, String expectedWinner, int storyAuthorVerbosity)
            throws IOException, InterruptedException {
        StoryBoard board = new StoryBoard(
                Configuration.buildFromArgs("--defaults", "--x", "author", Integer.toString(storyAuthorVerbosity)))
                        .sleepless().executeInternalFile(storyPath);
        Story story = board.build();
        StoryAuthor author = story.getController().getAuthor();
        final StringWriter target = new StringWriter();
        author.writeStory(new PrintWriter(target), "String-Dump");
        final String[] lines = meltDownStory(Arrays.stream(target.toString().split("\n")).sequential());// for all lines
        final String[] originalLines = meltDownStory(
                Arrays.stream(InternalResources.getFileLines(storyPath)).sequential());

        Assertions.assertArrayEquals(originalLines, lines, "Sources should match as same story. But: "
                + Arrays.toString(originalLines) + " vs. " + Arrays.toString(lines));

        // Retrieve winner:
        DummyClient winner = story.getClient(expectedWinner);
        MessageContainer shouldBeStatistics = MessageContainer.getMessage(winner.getMessages().getLast());
        Assertions.assertEquals(MessageTypeEnum.STATISTICS, shouldBeStatistics.getType(), "Last should be statistics");
        StatisticsMessage stats = (StatisticsMessage) shouldBeStatistics;
        Assertions.assertEquals(stats.getClientId(), stats.getWinner(), "Winner should be the one that requested");
    }

    private String[] meltDownStory(Stream<String> base) {
        return base.map(String::trim) // cleanse leading whitespace
                .filter(str -> !str.startsWith("#")) // ignore comments (sloppy)
                .filter(str -> !str.startsWith("SLEEP")) // ignore sleeps (Sloppy)
                .skip(2) // skip the two set-lines that hold date-information
                .toArray(String[]::new); // collect as list
    }

    // May Test: crash mid round
    // May Test: double crash mid round
    // May Test: pause on round end
}