package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.networking.messages.StrikeMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.game.phases.choice.DraftingChoice;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Tests the implementation the Drafting phase control
 *
 * @author Florian Sihler
 * @version 1.0, 04/11/2020
 */
@Tag("Phases")
@Tag("Phase-Drafting")
@TestMethodOrder(OrderAnnotation.class)
public class DraftingPhaseTests {

    private static void assertIllegalMessage(String message) {
        MessageContainer container = GameDataGson.getContainer(message);
        Assertions.assertNotNull(container, "Message should be in valid containerformat (" + message + ").");
        Assertions.assertEquals(MessageTypeEnum.ERROR, container.getType(),
                "Message should have the error-type (" + message + ").");

        ErrorMessage errorMessage = GameDataGson.fromJson(message, ErrorMessage.class);
        Assertions.assertEquals(ErrorTypeEnum.ILLEGAL_MESSAGE, errorMessage.getReason(),
                "Should be an illegal message (" + message + ").");
    }

    private static RequestItemChoiceMessage assertRequestItemChoiceMessage(String message) {
        MessageContainer container = GameDataGson.getContainer(message);
        Assertions.assertNotNull(container, "Message should be in valid containerformat (" + message + ").");
        Assertions.assertEquals(MessageTypeEnum.REQUEST_ITEM_CHOICE, container.getType(),
                "Message should have the requestItemChoice-type (" + message + ").");

        return GameDataGson.fromJson(message, RequestItemChoiceMessage.class);
    }

    @Test
    @Tag("Core")
    @Order(1)
    @DisplayName("[Phase] Draft one invalid item")
    public void test_invalidItemDraft() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered()
                .executeInternalFile("stories/drafting-phase/skip-drafting.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");

        // saphira should select something she doesn't get... she chooses the
        // chicken-feed
        ItemChoiceMessage choiceMessage = new ItemChoiceMessage(saphira.getClientId(), null, GadgetEnum.CHICKEN_FEED);
        saphira.send(choiceMessage.toJson());

        DummySendMessagesBuffer buffer = saphira.assureMessages(5, 250);

        // last should be an illegal message
        assertIllegalMessage(buffer.getLast());
    }

    @RepeatedTest(45)
    @Order(2)
    @DisplayName("[Phase] Never return a chosen item")
    public void test_chosenItemDraft() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered().execute("HELLO dieter PLAYER")
                .execute("HELLO jeremy SPECTATOR").execute("HELLO saphira AI");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");

        // saphira selects a random gadget
        DummySendMessagesBuffer buffer = saphira.assureMessages(4, 250);

        RequestItemChoiceMessage firstChoice = assertRequestItemChoiceMessage(buffer.getLast());

        Assertions.assertFalse(firstChoice.getOfferedGadgets().contains(GadgetEnum.DIAMOND_COLLAR),
                "The diamond collar cannot be in choice, but: " + firstChoice);
        Assertions.assertFalse(firstChoice.getOfferedGadgets().contains(GadgetEnum.COCKTAIL),
                "The cocktail cannot be in choice, but: " + firstChoice);

        GadgetEnum saphiraChoice = firstChoice.getOfferedGadgets().get(0);

        ItemChoiceMessage choiceMessage = new ItemChoiceMessage(saphira.getClientId(), null, saphiraChoice);
        saphira.send(choiceMessage.toJson());

        buffer = saphira.assureMessages(5, 250);
        Assertions.assertEquals(5, buffer.size(),
                "Should have gotten 5 messages, but (" + buffer.size() + "): " + buffer);

        RequestItemChoiceMessage newChoice = assertRequestItemChoiceMessage(buffer.getLast());
        Assertions.assertEquals(3, newChoice.getOfferedGadgets().size(),
                "Should have gotten 3 gadgets, but: " + newChoice);
        Assertions.assertEquals(3, newChoice.getOfferedCharacterIds().size(),
                "Should have gotten 3 characters, but: " + newChoice);

        Assertions.assertFalse(newChoice.getOfferedGadgets().contains(saphiraChoice),
                "Shouldn't contain" + saphiraChoice + ", but: " + newChoice);

        DummySendMessagesBuffer dieterBuffer = dieter.assureMessages(3, 250);
        Assertions.assertEquals(4, dieterBuffer.size(),
                "Should have gotten 4 messages, but (" + dieterBuffer.size() + "): " + dieterBuffer);

        RequestItemChoiceMessage dieterChoice = assertRequestItemChoiceMessage(dieterBuffer.getLast());
        Assertions.assertEquals(3, dieterChoice.getOfferedGadgets().size(),
                "Should have gotten 3 gadgets, but: " + dieterChoice);
        Assertions.assertEquals(3, dieterChoice.getOfferedCharacterIds().size(),
                "Should have gotten 3 characters, but: " + dieterChoice);

        // dieter and saphira should not have gotten the same gadgets
        List<GadgetEnum> saphiraGadgets = newChoice.getOfferedGadgets();
        List<GadgetEnum> dieterGadgets = dieterChoice.getOfferedGadgets();
        for (GadgetEnum gadgetEnum : saphiraGadgets) {
            if (dieterGadgets.contains(gadgetEnum))
                Assertions.fail("Dieter shouldn't have gotten the Gadget: " + gadgetEnum + ". Saphira: " + newChoice
                        + ", Dieter: " + dieterChoice);
        }
    }

    @RepeatedTest(15)
    @Order(2)
    @DisplayName("[Phase] don't show not choosable - gadgets")
    public void test_neverShowNotChoosableGadgets() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().execute("HELLO dieter PLAYER").execute("HELLO jeremy SPECTATOR")
                .execute("HELLO saphira AI");
        Story tale = taleBoard.build();

        // saphira choose 6 gadgets and 1 character:
        // dieter will only choose gadgets
        for (int i = 0; i < 7; i++) {
            if (i == 5) {
                taleBoard.execute("ITEM saphira random-character");
            } else {
                taleBoard.execute("ITEM saphira random-gadget");
                taleBoard.execute("ITEM dieter random-gadget");
            }
        }

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");

        String lastSaphira = saphira.assureMessages(10, 70).getLast();
        String lastDieter = dieter.assureMessages(10, 70).getLast();

        RequestItemChoiceMessage saphiraRequest = MessageContainer.getMessage(lastSaphira);
        RequestItemChoiceMessage dieterRequest = MessageContainer.getMessage(lastDieter);

        Assertions.assertTrue(saphiraRequest.getOfferedGadgets().isEmpty(),
                "There should be no gadgets presented anymore.");
        Assertions.assertTrue(dieterRequest.getOfferedGadgets().isEmpty(),
                "There should be no gadgets presented anymore.");

        Assertions.assertFalse(saphiraRequest.getOfferedCharacterIds().isEmpty(),
                "There should still be characters presented.");
        Assertions.assertFalse(dieterRequest.getOfferedCharacterIds().isEmpty(),
                "There should still be characters presented.");
    }

    @RepeatedTest(15)
    @Order(2)
    @DisplayName("[Phase] don't show not choosable - characters")
    public void test_neverShowNotChoosableCharacter() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().execute("HELLO dieter PLAYER").execute("HELLO jeremy SPECTATOR")
                .execute("HELLO saphira AI");
        Story tale = taleBoard.build();

        // saphira choose 4 characters and 1 gadget:
        // dieter will only choose gadgets
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                taleBoard.execute("ITEM saphira random-gadget");
            } else {
                taleBoard.execute("ITEM saphira random-character");
                taleBoard.execute("ITEM dieter random-gadget");
            }
        }

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");

        String lastSaphira = saphira.assureMessages(8, 70).getLast();
        String lastDieter = dieter.assureMessages(8, 70).getLast();

        RequestItemChoiceMessage saphiraRequest = MessageContainer.getMessage(lastSaphira);
        RequestItemChoiceMessage dieterRequest = MessageContainer.getMessage(lastDieter);

        Assertions.assertFalse(saphiraRequest.getOfferedGadgets().isEmpty(),
                "There should be gadgets presented for saphira.");
        Assertions.assertFalse(dieterRequest.getOfferedGadgets().isEmpty(),
                "There should be gadgets presented for dieter.");

        Assertions.assertTrue(saphiraRequest.getOfferedCharacterIds().isEmpty(),
                "There should be no characters for saphira anymore.");
        Assertions.assertFalse(dieterRequest.getOfferedCharacterIds().isEmpty(),
                "There should still be characters presented.");
    }

    @RepeatedTest(5)
    @Order(2)
    @DisplayName("[Phase] don't show not choosable - characters (altered order)")
    public void test_neverShowNotChoosableCharacter2() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().execute("HELLO dieter PLAYER").execute("HELLO jeremy SPECTATOR")
                .execute("HELLO saphira AI");
        Story tale = taleBoard.build();

        // dieter choose 4 characters and 1 gadget:
        // saphira will only choose gadgets
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                taleBoard.execute("ITEM dieter random-gadget");
            } else {
                taleBoard.execute("ITEM dieter random-character");
                taleBoard.execute("ITEM saphira random-gadget");
            }
        }

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");

        String lastSaphira = saphira.assureMessages(8, 70).getLast();
        String lastDieter = dieter.assureMessages(8, 70).getLast();

        RequestItemChoiceMessage saphiraRequest = MessageContainer.getMessage(lastSaphira);
        RequestItemChoiceMessage dieterRequest = MessageContainer.getMessage(lastDieter);

        Assertions.assertFalse(saphiraRequest.getOfferedGadgets().isEmpty(),
                "There should be gadgets presented for saphira.");
        Assertions.assertFalse(dieterRequest.getOfferedGadgets().isEmpty(),
                "There should be gadgets presented for dieter.");

        Assertions.assertFalse(saphiraRequest.getOfferedCharacterIds().isEmpty(),
                "There should still be characters presented.");
        Assertions.assertTrue(dieterRequest.getOfferedCharacterIds().isEmpty(),
                "There should be no characters for dieter anymore.");
    }

    @RepeatedTest(30)
    @Order(3)
    @DisplayName("[Phase] Run a complete drafting-phase")
    public void test_computeDrafting() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered()
                .executeInternalFile("stories/drafting-phase/complete-drafting.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");

        // get the choices the server has registered
        DraftingPhaseController dpController = tale.getController().getDraftingPhaseController();
        DraftingChoice saphiraChoice = dpController.assertCorrectChoice(saphira.getConnection());
        DraftingChoice dieterChoice = dpController.assertCorrectChoice(dieter.getConnection());

        // Assert, that they have different Gadgets
        for (GadgetEnum chosenGadget : saphiraChoice.getGadgets()) {
            Assertions.assertFalse(dieterChoice.getGadgets().contains(chosenGadget),
                    "should have different Gadgets for: " + chosenGadget + " in " + dieterChoice.getGadgets());
        }
    }

    @Test
    @Order(4)
    @DisplayName("[Phase] Test pause and resume in drafting")
    public void test_pauseAndResume() throws IOException {
        StoryBoard taleBoard = new StoryBoard().buffered().execute("HELLO dieter PLAYER", "HELLO walter PLAYER",
                "ITER 4:", "ITEM dieter random", "ITEM walter random", "RETI");
        taleBoard.execute("CRASH dieter", "RECONNECT dieter next-dieter");
        Story tale = taleBoard.build();

        DummyClient dieter = tale.getClient("dieter");
        DummyClient nextDieter = tale.getClient("next-dieter");
        DummyClient walter = tale.getClient("walter");

        // Game Start rite and 4 choices + the next choice [then crash]
        dieter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE);
        // Same as dieter, but game pause and game resume additionally
        walter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        // Same game-started, next itemchoicerequest
        nextDieter.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.REQUEST_ITEM_CHOICE);

        // Assure everybody has the same game-started
        GameStartedMessage dieterStart = MessageContainer.getMessage(dieter.getMessages().get(2));
        GameStartedMessage walterStart = MessageContainer.getMessage(walter.getMessages().get(2));
        GameStartedMessage nextDieterStart = MessageContainer.getMessage(nextDieter.getMessages().get(1));

        Assertions.assertEquals(dieterStart.getPlayerOneId(), walterStart.getPlayerOneId(), "1) One. Should be equal.");
        Assertions.assertEquals(dieterStart.getPlayerTwoId(), walterStart.getPlayerTwoId(), "1) Two. Should be equal.");
        Assertions.assertEquals(dieterStart.getPlayerOneId(), nextDieterStart.getPlayerOneId(),
                "2) One. Should be equal.");
        Assertions.assertEquals(dieterStart.getPlayerTwoId(), nextDieterStart.getPlayerTwoId(),
                "2) Two. Should be equal.");

        Assertions.assertEquals(dieterStart.getClientId(), nextDieterStart.getClientId(), "Ids should be same");

    }

    @Test
    @Order(5)
    @DisplayName("[Phase] Test moledie init")
    public void test_noMolediePropertiesOnReceive() throws IOException {
        StoryBoard taleBoard = new StoryBoard().executeInternalFile("stories/drafting-phase/moledie-story.story");
        Story tale = taleBoard.build();

        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        Character hansOtto = controller.decodeCharacterByName("Hans Peter Otto").get();
        Assertions.assertEquals(2, hansOtto.getProperties().size());
        Assertions.assertTrue(hansOtto.getProperties().contains(PropertyEnum.ROBUST_STOMACH));
        Assertions.assertTrue(hansOtto.getProperties().contains(PropertyEnum.LUCKY_DEVIL));
        // Moledie buffer checks
        Assertions.assertNotNull(hansOtto.getMoledieBuffer());
        Assertions.assertEquals(1, hansOtto.getMoledieBuffer().size());
        Assertions.assertTrue(hansOtto.getMoledieBuffer().contains(PropertyEnum.FLAPS_AND_SEALS));

        // check with all:
        taleBoard = new StoryBoard()
                .set("hpOttoFeatures", "\"FLAPS_AND_SEALS\",\"LUCKY_DEVIL\",\"OBSERVATION\",\"TRADECRAFT\"")
                .executeInternalFile("stories/drafting-phase/moledie-story.story");
        tale = taleBoard.build();
        controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        hansOtto = controller.decodeCharacterByName("Hans Peter Otto").get();
        Assertions.assertEquals(2, hansOtto.getProperties().size());
        Assertions.assertTrue(hansOtto.getProperties().contains(PropertyEnum.ROBUST_STOMACH));
        Assertions.assertTrue(hansOtto.getProperties().contains(PropertyEnum.LUCKY_DEVIL));

        // Moledie buffer checks
        Assertions.assertNotNull(hansOtto.getMoledieBuffer());
        Assertions.assertEquals(3, hansOtto.getMoledieBuffer().size());
        Assertions.assertTrue(hansOtto.getMoledieBuffer().contains(PropertyEnum.FLAPS_AND_SEALS));
        Assertions.assertTrue(hansOtto.getMoledieBuffer().contains(PropertyEnum.OBSERVATION));
        Assertions.assertTrue(hansOtto.getMoledieBuffer().contains(PropertyEnum.TRADECRAFT));
    }

    @Test
    @Order(6)
    @DisplayName("[Phase] Test item request in pause (entry delayed)")
    public void test_itemRequestWhilePause() throws IOException {
        StoryBoard board = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "resumeByBoth", "true"))
                .set("pauseItem", true).executeInternalFile("stories/drafting-phase/itemOrEquipInPause.story");
        Story story = board.build();
        // saphira wants a pause
        board.pause("saphira");
        // dieter will send his item request delayed :)
        board.execute("ITEM dieter mirror_of_wilderness");
        // resume have to be both
        board.resume("dieter");
        // saphira wants to choose too
        board.execute("ITEM saphira poison_pills");
        // no one should have gotton an equipment choice!

        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer dieterBuffer = dieter.getMessages();

        saphiraBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.GAME_PAUSE);
        dieterBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.GAME_PAUSE);
        board.resume("saphira");

        // we are running again -> now they should have gotten the equipment request
        saphiraBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE);
        dieterBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE);

        // equipment from file should work
        board.execute(
                "EQUIP saphira \"Schleim B. Olzen,GRAPPLE,GAS_GLOSS,POISON_PILLS,Mister Y,Mister X,Austauschbarer Agent Dieter 42,WIRETAP_WITH_EARPLUGS\"");
        board.execute(
                "EQUIP dieter \"Hans Peter Otto,JETPACK,MOTHBALL_POUCH,POCKET_LITTER,DIAMOND_COLLAR,MIRROR_OF_WILDERNESS,James Bond,ROCKET_PEN\"");
        Assertions.assertTrue(saphiraBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have gotten game-status for saphira as game should have started.");
        // may have gotten operation - is not relevant
        Assertions.assertTrue(dieterBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have gotten game-status for dieter< as game should have started.");
        // may have gotten operation - is not relevant
    }

    @Test
    @Order(6)
    @DisplayName("[Phase] Test item request in pause (entry delayed somewhere in the middle)")
    public void test_itemRequestWhilePauseInMid() throws IOException {
        StoryBoard board = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "resumeByBoth", "true"))
                .set("pauseMid", true).executeInternalFile("stories/drafting-phase/itemOrEquipInPause.story");
        Story story = board.build();
        // saphira wants a pause
        board.pause("saphira");
        // dieter will send his item request delayed :)
        board.execute("ITEM dieter mothball_pouch");
        // resume have to be both
        board.resume("dieter");

        // no one should have gotton an equipment choice!
        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer dieterBuffer = dieter.getMessages();

        saphiraBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.GAME_PAUSE);
        dieterBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
                MessageTypeEnum.GAME_PAUSE);
        board.resume("saphira");

        // we are running again -> now they should have gotten the equipment request
        saphiraBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE); // saphira did not choose in pause
        dieterBuffer.assertLastTypes(MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.REQUEST_ITEM_CHOICE);

        board.execute("ITEM dieter \"James Bond\"");
        board.execute("ITEM saphira gas_gloss");

        // how about another pause :) - now you, dieter!

        board.pause("dieter");
        board.execute("ITEM saphira \"Mister X\"");
        board.resume("saphira");
        board.execute("ITEM dieter pocket_litter");

        saphiraBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE); // saphira did not choose in pause
        dieterBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE);

        board.resume("dieter");

        // Running again

        saphiraBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE); // saphira did not choose in pause
        dieterBuffer.assertLastTypes(MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.REQUEST_ITEM_CHOICE);
    }

    @Test
    @Order(7)
    @DisplayName("[Phase] Test item request in pause (entry delayed final equip)")
    public void test_itemRequestWhilePauseFinalEquip() throws IOException {
        StoryBoard board = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "resumeByBoth", "true"))
                .set("pauseItem", true).executeInternalFile("stories/drafting-phase/itemOrEquipInPause.story");
        Story story = board.build();
        // complete item choices
        board.execute("ITEM saphira poison_pills");
        board.execute("ITEM dieter mirror_of_wilderness");
        // first equip:
        board.execute(
                "EQUIP saphira \"Schleim B. Olzen,GRAPPLE,GAS_GLOSS,POISON_PILLS,Mister Y,Mister X,Austauschbarer Agent Dieter 42,WIRETAP_WITH_EARPLUGS\"");

        // saphira wants pause - saphira will crash!
        board.crash("saphira");
        board.execute(
                "EQUIP dieter \"Hans Peter Otto,JETPACK,MOTHBALL_POUCH,POCKET_LITTER,DIAMOND_COLLAR,MIRROR_OF_WILDERNESS,James Bond,ROCKET_PEN\"");

        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer dieterBuffer = dieter.getMessages();

        board.reconnect("saphira", "new-saphira");
        DummyClient newSaphira = story.getClient("new-saphira");
        DummySendMessagesBuffer newSaphiraBuffer = newSaphira.getMessages();

        // now: new saphira and dieter should have gotten the game status :)
        Assertions.assertFalse(saphiraBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have not gotten game-status for saphira as crashed.");
        // may have gotten operation - is not relevant
        Assertions.assertTrue(dieterBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have gotten game-status for dieter as game should have started.");
        Assertions.assertTrue(newSaphiraBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have gotten game-status for new Saphira as game should have started.");
    }

    @Test
    @Order(8)
    @DisplayName("[Phase] Test item request in pause (entry delayed non-final equip)")
    public void test_itemRequestWhilePauseNonFinalEquip() throws IOException {
        StoryBoard board = new StoryBoard(Configuration.buildFromArgs("--defaults", "--x", "resumeByBoth", "true"))
                .set("pauseItem", true).executeInternalFile("stories/drafting-phase/itemOrEquipInPause.story");
        Story story = board.build();
        // complete item choices
        board.execute("ITEM saphira poison_pills");
        board.execute("ITEM dieter mirror_of_wilderness");
        // now no first equip from!

        // saphira wants pause - saphira will crash!
        board.crash("saphira");
        board.execute(
                "EQUIP dieter \"Hans Peter Otto,JETPACK,MOTHBALL_POUCH,POCKET_LITTER,DIAMOND_COLLAR,MIRROR_OF_WILDERNESS,James Bond,ROCKET_PEN\"");

        DummyClient saphira = story.getClient("saphira");
        DummyClient dieter = story.getClient("dieter");

        DummySendMessagesBuffer saphiraBuffer = saphira.getMessages();
        DummySendMessagesBuffer dieterBuffer = dieter.getMessages();

        // game should not start as request of saphira missing
        board.reconnect("saphira", "new-saphira");
        DummyClient newSaphira = story.getClient("new-saphira");
        DummySendMessagesBuffer newSaphiraBuffer = newSaphira.getMessages();

        // now: new saphira and dieter should have gotten the game status :)
        Assertions.assertFalse(saphiraBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should have not gotten game-status for saphira as crashed.");
        // may have gotten operation - is not relevant
        Assertions.assertFalse(dieterBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should not have gotten game-status for dieter as game should not have started.");
        Assertions.assertFalse(newSaphiraBuffer.containsType(MessageTypeEnum.GAME_STATUS),
                "Should not have gotten game-status for new Saphira as game should not have started.");
        Assertions.assertTrue(newSaphiraBuffer.containsType(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE),
                "Saphira should have gotton request equipment choice.");

    }

    @Test
    @Order(9)
    @DisplayName("[Phase] Test timeout handling if no item was requested")
    public void test_timeoutNoItemSelected() throws IOException, MessageException {
        // we will do two cheats here - we set the turn timeout to one second and reduce
        // the multiplier to 50 so that the turn timeout is 50ms
        BackstoryBuilder builder = new BackstoryBuilder(Configuration.buildFromArgs("--defaults")).unlock();
        builder.matchconfigUpdate("turnPhaseLimit", 1);
        builder.matchconfigUpdate("strikeMaximum", 12); // we do not want to get kicked
        StoryBoard board = builder.build().set("pauseItem", true).unbuffered();
        Story story = board.build();
        story.getConfiguration().changeMultiplier(500);

        board.hello("Walter", RoleEnum.PLAYER);
        board.hello("Jenson", RoleEnum.PLAYER);

        DummyClient walter = story.getClient("Walter");
        DummyClient jenson = story.getClient("Jenson");

        // already got 4 messages, wait for 3 more + 3 for the strikes
        // The buffer of one is for lennart :D
        walter.assureMessages(9, 4000); // ~140 for lock
        final int walterMessageSize = walter.getMessageSize();
        Assertions.assertTrue(walterMessageSize >= 8, "Walter should have gotten at least 8 messages, but: "
                + walterMessageSize + " with: " + story.typeDump());

        jenson.assureMessages(9, 4000); // ~140 for lock
        final int jensonMessageSize = jenson.getMessageSize();
        Assertions.assertTrue(jensonMessageSize >= 8, "Jenson should have gotten at least 8 messages, but: "
                + jensonMessageSize + " with: " + story.typeDump());

        // game starts

        // Now there should be up to 3 enforced choices but at least two :
        // First of all we will reset the multiplier
        story.getConfiguration().changeMultiplier(5000);

        // Now we have time :)
        // Let them lock their old choices:
        board.execute("ITEM Walter random");
        board.execute("ITEM Jenson random");

        // validate, that strikes have been set correctly and are reset now for both

        Assertions.assertEquals(0, story.getController().getStrikeController().getPlayerOneStrikes(),
                "No Strikes for player one");
        Assertions.assertEquals(0, story.getController().getStrikeController().getPlayerTwoStrikes(),
                "No Strikes for player two");

        // Filter strike messages of both
        List<StrikeMessage> strikeMessagesP1 = getAllStrikes(walter);
        List<StrikeMessage> strikeMessagesP2 = getAllStrikes(jenson);

        for (int i = 0; i < strikeMessagesP1.size(); i++) {
            Assertions.assertEquals(i + 1, strikeMessagesP1.get(i).getStrikeNr(), "P1: Number should be as stated for: "
                    + i + " in: " + strikeMessagesP1 + " for: " + walter.getMessages());
        }

        for (int i = 0; i < strikeMessagesP2.size(); i++) {
            Assertions.assertEquals(i + 1, strikeMessagesP2.get(i).getStrikeNr(), "P2: Number should be as stated for: "
                    + i + " in: " + strikeMessagesP2 + " for: " + jenson.getMessages());
        }

        // get their current choices on the server side -- there should be 3-4
        // (depending on the trigger of the last timeout)

        DraftingChoice walterChoice = story.getController().getDraftingPhaseController().getPlayerOneChoice();
        DraftingChoice jensonChoice = story.getController().getDraftingPhaseController().getPlayerTwoChoice();

        int walterSize = walterChoice.itemsChosen();
        int jensonSize = jensonChoice.itemsChosen();

        Assertions.assertTrue(walterSize == 3 || walterSize == 4,
                "Walter should have gotten 3 or 4 items (depending on the last timeout) but: " + walterChoice
                        + " Story: " + story.typeDump());

        Assertions.assertTrue(jensonSize == 3 || jensonSize == 4,
                "Jenson should have gotten 3 or 4 items (depending on the last timeout) but: " + jensonChoice
                        + " Story: " + story.typeDump());

        // issue meta information
        board.meta("Walter", MetaKeyEnum.FACTION_PLAYER1, MetaKeyEnum.GADGETS_PLAYER1);
        board.meta("Jenson", MetaKeyEnum.FACTION_PLAYER2, MetaKeyEnum.GADGETS_PLAYER2);

        // Check Meta:
        MetaInformationMessage metaWalter = MessageContainer.getMessage(walter.getMessages().getLast());
        MetaInformationMessage metaJenson = MessageContainer.getMessage(jenson.getMessages().getLast());

        Assertions.assertArrayEquals(walterChoice.getCharacters().toArray(UUID[]::new),
                (UUID[]) metaWalter.getInformation().get(MetaKeyEnum.FACTION_PLAYER1.getKey()),
                "Walter should have gotten the correct characters");

        Assertions.assertArrayEquals(jensonChoice.getCharacters().toArray(UUID[]::new),
                (UUID[]) metaJenson.getInformation().get(MetaKeyEnum.FACTION_PLAYER2.getKey()),
                "Jenson should have gotten the correct characters");

        Assertions.assertArrayEquals(walterChoice.getGadgets().toArray(GadgetEnum[]::new),
                (GadgetEnum[]) metaWalter.getInformation().get(MetaKeyEnum.GADGETS_PLAYER1.getKey()),
                "Walter should have gotten the correct gadgets");

        Assertions.assertArrayEquals(jensonChoice.getGadgets().toArray(GadgetEnum[]::new),
                (GadgetEnum[]) metaJenson.getInformation().get(MetaKeyEnum.GADGETS_PLAYER2.getKey()),
                "Jenson should have gotten the correct gadgets");
        // todo: play to equipment :D
        // URGENT TODO: other tests
    }

    private List<StrikeMessage> getAllStrikes(DummyClient client) {
        return client.getMessages().stream().map(MessageContainer::getMessage)
                .filter(m -> ((MessageContainer) m).getType() == MessageTypeEnum.STRIKE).map(m -> (StrikeMessage) m)
                .collect(Collectors.toList());
    }

    // TODO: weird crash combination
    // TODO: check this in game
}