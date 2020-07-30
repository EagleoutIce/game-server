package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.State;
import de.uulm.team020.datatypes.enumerations.GenderEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.RequestGameOperationMessage;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationException;
import de.uulm.team020.server.core.dummies.story.exceptions.operation.StoryOperationExceptionType;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Will test the main game phase
 */
public class MainGamePhaseTests {

    @RepeatedTest(6)
    @Tag("Core")
    @Order(1)
    @DisplayName("[Phase] Test, if reaching the main-game-phase works with no errors")
    public void test_justJumpToMain() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered().hello("some spectator", RoleEnum.SPECTATOR)
                .preset(Presets.COMPLETE_DRAFTING);
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");
        DummyClient specti = tale.getClient("some spectator");

        // here we just want to see if the types hold up to expected ones:
        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS);
        dieter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS);
    }

    @RepeatedTest(6)
    @Tag("Core")
    @Order(2)
    @DisplayName("[Phase] Test, if startup-configuration seems to be valid (using isles)")
    public void test_setupAppearsToBeValid() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered().hello("some spectator", RoleEnum.SPECTATOR)
                .preset(Presets.COMPLETE_DRAFTING);
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");
        DummyClient specti = tale.getClient("some spectator");

        // get all GameStatus messages => indexed to avoid automated turn-requests
        GameStatusMessage saphiraStatus = MessageContainer.getMessage(saphira.getMessages().get(12));
        GameStatusMessage dieterStatus = MessageContainer.getMessage(dieter.getMessages().get(12));
        GameStatusMessage spectiStatus = MessageContainer.getMessage(specti.getMessages().get(3));

        assertThreeSameGameStatus(tale.getConfiguration().getServerConfig(), saphiraStatus, dieterStatus, spectiStatus);
    }

    @RepeatedTest(6)
    @Tag("Core")
    @Order(2)
    @DisplayName("[Phase] Test, if startup-configuration seems to be valid (using no isles)")
    public void test_setupInCaseOfNoIslands() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered().execute("SERVER_CONFIG useIslands true")
                .hello("some spectator", RoleEnum.SPECTATOR).preset(Presets.COMPLETE_DRAFTING);
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient dieter = tale.getClient("dieter");
        DummyClient specti = tale.getClient("some spectator");

        // get all GameStatus messages => indexed to avoid automated turn-requests
        GameStatusMessage saphiraStatus = MessageContainer.getMessage(saphira.getMessages().get(12));
        GameStatusMessage dieterStatus = MessageContainer.getMessage(dieter.getMessages().get(12));
        GameStatusMessage spectiStatus = MessageContainer.getMessage(specti.getMessages().get(3));

        assertThreeSameGameStatus(tale.getConfiguration().getServerConfig(),saphiraStatus, dieterStatus, spectiStatus);

    }

    private void assertThreeSameGameStatus(ServerConfiguration serverConfig, GameStatusMessage a, GameStatusMessage b,
            GameStatusMessage c) {
        // jeah, could be done more elegant... but who needs for loop ;)
        Assertions.assertFalse(a.getIsGameOver(), "a: Game should not be over");
        Assertions.assertFalse(b.getIsGameOver(), "b: Game should not be over");
        Assertions.assertFalse(c.getIsGameOver(), "c: Game should not be over");

        Assertions.assertNull(a.getActiveCharacterId(), "a: No Character should be there");
        Assertions.assertNull(b.getActiveCharacterId(), "b: No Character should be there");
        Assertions.assertNull(c.getActiveCharacterId(), "c: No Character should be there");

        // NOTE: changed server-config default to empty list...
        if (serverConfig.sendEmptyOperationListOnStart()) {
            Assertions.assertNotNull(a.getOperations(),
                    "a: No Operations should be there, there should be an empty list");
            Assertions.assertNotNull(b.getOperations(),
                    "b: No Operations should be there, there should be an empty list");
            Assertions.assertNotNull(c.getOperations(),
                    "c: No Operations should be there, there should be an empty list");
            Assertions.assertTrue(a.getOperations().isEmpty(),
                    "a: No Operations should be there, there should be an empty list");
            Assertions.assertTrue(b.getOperations().isEmpty(),
                    "b: No Operations should be there, there should be an empty list");
            Assertions.assertTrue(c.getOperations().isEmpty(),
                    "c: No Operations should be there, there should be an empty list");
        } else {
            Assertions.assertNull(a.getOperations(), "a: No Operations should be there");
            Assertions.assertNull(b.getOperations(), "b: No Operations should be there");
            Assertions.assertNull(c.getOperations(), "c: No Operations should be there");
        }
        // State should be same for all
        final State aState = a.getState();
        final State bState = b.getState();
        final State cState = c.getState();

        Assertions.assertEquals(aState.getCurrentRound(), bState.getCurrentRound(),
                "[a:b] current round should be same");
        Assertions.assertEquals(bState.getCurrentRound(), cState.getCurrentRound(),
                "[b:c] current round should be same");

        Assertions.assertTrue(aState.getMap().equals(bState.getMap()),
                "[a:b] map should be same, but: " + aState.getMap() + " and " + bState.getMap());
        Assertions.assertTrue(bState.getMap().equals(cState.getMap()),
                "[b:c] map should be same, but: " + aState.getMap() + " and " + bState.getMap());

        Assertions.assertEquals(aState.getCharacters(), bState.getCharacters(), "[a:b] characters should be same");
        Assertions.assertEquals(bState.getCharacters(), cState.getCharacters(), "[b:c] characters should be same");

        Assertions.assertNotEquals(new Point(-1, -1), aState.getCatCoordinates(), "a: Cat should be on field");
        Assertions.assertNotEquals(new Point(-1, -1), bState.getCatCoordinates(), "b: Cat should be on field");
        Assertions.assertNotEquals(new Point(-1, -1), cState.getCatCoordinates(), "c: Cat should be on field");

        Assertions.assertEquals(new Point(-1, -1), aState.getJanitorCoordinates(), "a: Janitor shouldn't be on field");
        Assertions.assertEquals(new Point(-1, -1), bState.getJanitorCoordinates(), "b: Janitor shouldn't be on field");
        Assertions.assertEquals(new Point(-1, -1), cState.getJanitorCoordinates(), "c: Janitor shouldn't be on field");

        Assertions.assertTrue(aState.getMySafeCombinations().isEmpty(), "a: No safe-combinations at start");
        Assertions.assertTrue(bState.getMySafeCombinations().isEmpty(), "b: No safe-combinations at start");
        Assertions.assertNull(cState.getMySafeCombinations(), "c: Spectator shouldn't have safe combinations");
    }

    @RepeatedTest(3)
    @Tag("Core")
    @Order(3)
    @DisplayName("[Phase] Test, if startup-configuration seems to be valid (using injects)")
    public void test_setupAsInjected() throws IOException {
        // Just checking out buffered mode, it's awesome!
        StoryBoard taleBoard = new StoryBoard().buffered()
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        // get all GameStatus messages => indexed to avoid automated turn-requests
        GameStatusMessage saphiraStatus = MessageContainer.getMessage(saphira.getMessages().get(12));
        GameStatusMessage dieterStatus = MessageContainer.getMessage(walter.getMessages().get(12));
        GameStatusMessage spectiStatus = MessageContainer.getMessage(specti.getMessages().get(3));

        assertThreeSameGameStatus(tale.getConfiguration().getServerConfig(), saphiraStatus, dieterStatus, spectiStatus);

        // check and validate that the generated field is reasonable by the given
        // scenario
        Set<ImmutablePair<String, Point>> expectedNames = Set.of(
                new ImmutablePair<>("Schleim B. Olzen", new Point(5, 4)),
                new ImmutablePair<>("Mister X", new Point(1, 8)),
                new ImmutablePair<>("Zackiger Zacharias", new Point(6, 8)),
                new ImmutablePair<>("The legendary Gustav", new Point(5, 12)),
                new ImmutablePair<>("Ein Wischmob", new Point(3, 2)), new ImmutablePair<>("Petterson", new Point(2, 8)),
                new ImmutablePair<>("James Bond", new Point(4, 8)),
                new ImmutablePair<>("Meister Yoda", new Point(8, 14)),
                new ImmutablePair<>("Austauschbarer Agent Dieter 42", new Point(3, 6)),
                new ImmutablePair<>("Misses Y", new Point(1, 14)));
        // every name has to be in:
        Set<Character> gotCharacters = saphiraStatus.getState().getCharacters();
        Assertions.assertEquals(expectedNames.size(), gotCharacters.size(),
                "Should have gotten the same char length as expected, but: " + gotCharacters + " when expected: "
                        + expectedNames);
        for (Character character : gotCharacters) {
            Optional<ImmutablePair<String, Point>> mayPair = expectedNames.stream()
                    .filter(n -> n.getKey().equals(character.getName())).findAny();
            Assertions.assertTrue(mayPair.isPresent(), "Should have character: " + character + " but pool: "
                    + gotCharacters + " when expected: " + expectedNames);
            Assertions.assertEquals(mayPair.get().getValue(), character.getCoordinates(), "Should be on position: "
                    + mayPair.get().getValue() + " but: " + character + " when expected: " + expectedNames);
        }

        // check if the first player is as injected in the round
        GameFieldController gfController = tale.getController().getMainGamePhaseController().getGameFieldController();
        Character currentlyActiveCharacter = gfController.getActiveCharacter();

        // check that the active character is correct
        Character expectedCharacter = new Character(new CharacterInformation(
                new CharacterDescription("Schleim B. Olzen", "Ignored on construction so whatever", GenderEnum.DIVERSE,
                        List.of(PropertyEnum.LUCKY_DEVIL, PropertyEnum.NIMBLENESS, PropertyEnum.TRADECRAFT)),
                UUID.fromString("5d12ec03-753a-4f6a-a4e5-c1b8bc9e6132")), List.of());
        Assertions.assertEquals(expectedCharacter, currentlyActiveCharacter);

        // Assure that walter got request operation for schleim b. olzen and all others
        // not
        String lastMessage = walter.getMessages().getLast();
        assureRequestOperation(currentlyActiveCharacter.getCharacterId(), lastMessage);

        // no-one else should have gotten the certain types
        Assertions.assertFalse(saphira.getMessages().containsType(MessageTypeEnum.REQUEST_GAME_OPERATION),
                "Saphira should not have gotton an operation request.");
        Assertions.assertFalse(specti.getMessages().containsType(MessageTypeEnum.REQUEST_GAME_OPERATION),
                "Specti should not have gotton an operation request.");
    }

    private void assureRequestOperation(UUID characterId, String message) {

        Assertions.assertEquals(MessageTypeEnum.REQUEST_GAME_OPERATION, GameDataGson.getType(message),
                "should be a request");
        RequestGameOperationMessage requestGameOperationMessage = MessageContainer.getMessage(message);

        Assertions.assertEquals(characterId, requestGameOperationMessage.getCharacterId(),
                "Should be expected Character");

    }

    private static MessageTypeEnum[] SKIP_DRAFTING_PLAYER = { MessageTypeEnum.META_INFORMATION,
            MessageTypeEnum.HELLO_REPLY, MessageTypeEnum.GAME_STARTED, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_ITEM_CHOICE,
            MessageTypeEnum.REQUEST_ITEM_CHOICE, MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE };

    @Test
    @Tag("Core")
    @Order(3)
    @DisplayName("[Phase] Test messages on crash outside of pause")
    public void test_crashOnStartPhaseWithReconnect() throws IOException {
        StoryBoard taleBoard = new StoryBoard().executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("CRASH saphira"); // poor saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT saphira saphira-1");

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");

        saphira1.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS);
    }

    @RepeatedTest(2)
    @Tag("Core")
    @Order(4)
    @DisplayName("[Phase] Test messages on crash inside of a pause")
    public void test_crashOnStartPhaseWithReconnectOnPause() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("PAUSE walter", "CRASH saphira"); // poor saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT saphira saphira-1");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        // Now the pause shall be lifted, go for it walter
        taleBoard.execute("RESUME walter");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
    }

    @RepeatedTest(2)
    @Tag("Core")
    @Order(4)
    @DisplayName("[Phase] Test messages on crash inside of a pause, where the crasher wanted it")
    // the one who crashed
    public void test_crashOnStartPhaseWithReconnectOnPauseSame() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("PAUSE saphira", "CRASH saphira"); // poor saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT saphira saphira-1");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        // Now the pause shall be lifted, go for it walter
        taleBoard.execute("RESUME walter");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
    }

    @RepeatedTest(2)
    @Tag("Core")
    @Order(5)
    @DisplayName("[Phase] Test messages on double crash outside of a pause")
    // the one who crashed
    public void test_crashOnStartPhaseDoubleWithReconnect() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("CRASH walter", "CRASH saphira"); // poor saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT saphira saphira-1");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");
        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        // now walter will reconnect aswell
        taleBoard.execute("RECONNECT walter walter-1");
        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        DummyClient walter1 = tale.getClient("walter-1");
        walter1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.REQUEST_GAME_OPERATION);
    }

    @RepeatedTest(2)
    @Tag("Core")
    @Order(5)
    @DisplayName("[Phase] Test messages on double crash outside of a pause with changed order")
    // the one who crashed
    public void test_crashOnStartPhaseDoubleWithReconnect2() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("CRASH walter", "CRASH saphira"); // poor saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT walter walter-1");
        saphira.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE);

        DummyClient walter1 = tale.getClient("walter-1");
        walter1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE);

        // now walter will reconnect aswell
        taleBoard.execute("RECONNECT saphira saphira-1");
        walter1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertOnlyTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");
        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS);
    }

    // now switch into a pause
    @Test
    @Tag("Core")
    @Order(6)
    @DisplayName("[Phase] Test messages on double crash inside of a pause")
    // the one who crashed
    public void test_crashOnStartPhaseDoubleWithReconnectOnPause() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        taleBoard.execute("PAUSE walter", "CRASH walter", "CRASH saphira"); // poor
                                                                            // saphira

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        taleBoard.execute("RECONNECT saphira saphira-1");

        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);

        DummyClient saphira1 = tale.getClient("saphira-1");
        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        // now walter will reconnect aswell
        taleBoard.execute("RECONNECT walter walter-1");

        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);

        DummyClient walter1 = tale.getClient("walter-1");

        walter1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE);

        // no walter wants to resume
        taleBoard.execute(StoryChapterType.RESUME, "walter-1");
        walter1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.REQUEST_GAME_OPERATION, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        saphira1.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.GAME_STARTED,
                MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
        walter.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS,
                MessageTypeEnum.REQUEST_GAME_OPERATION);
        saphira.getMessages().assertTypes(SKIP_DRAFTING_PLAYER, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertOnlyTypes(MessageTypeEnum.META_INFORMATION, MessageTypeEnum.HELLO_REPLY,
                MessageTypeEnum.GAME_STARTED, MessageTypeEnum.GAME_STATUS, MessageTypeEnum.GAME_PAUSE,
                MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE, MessageTypeEnum.GAME_PAUSE);
    }

    @Test
    @Tag("Core")
    @Order(7)
    @DisplayName("[Phase] Test one Round of movements and retirements")
    // the one who crashed
    public void test_oneRoundOfMovementsAndRetirements() throws IOException, InterruptedException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        // execute commands in order:
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        nRandTurns(controller, taleBoard, 8); // 8 characters that have to be allowed to draw

        Assertions.assertEquals(2, controller.getCurrentRoundNumber(),
                "Should be in round 2 now as there was a new round request");

    }

    private void nRandTurns(GameFieldController controller, StoryBoard taleBoard, int n) {
        for (int j = 0; j < n; j++) {
            Assertions.assertEquals(1, controller.getCurrentRoundNumber(), "Should be in round 1");
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
        }
    }

    @Test
    @Tag("Core")
    @Order(8)
    @DisplayName("[Phase] Test messages on a crash in midst of a current round")
    // the one who crashed
    public void test_crashMidGameNoTurn() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        nRandTurns(controller, taleBoard, 4); // 8 characters that have to be allowed to draw, we stop somewhere
                                              // mid
                                              // where it was NOT saphiras turn

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        saphira.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertLastTypes(MessageTypeEnum.REQUEST_GAME_OPERATION);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);

        taleBoard.crash("saphira"); // poor saphira

        saphira.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);

        taleBoard.reconnect("saphira", "saphira-1");

        saphira.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);
        tale.getClient("saphira-1").getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);
        walter.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);

    }

    @Test
    @Tag("Core")
    @Order(8)
    @DisplayName("[Phase] Test messages on a crash for current player in midst of a current round")
    // the one who crashed
    public void test_crashMidGameHasTurn() throws IOException {
        StoryBoard taleBoard = new StoryBoard().set("p1Role", "PLAYER") // we need him as player to pause
                .executeInternalFile("stories/main-phase/skip-injected.story");
        Story tale = taleBoard.build();

        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        nRandTurns(controller, taleBoard, 6); // 8 characters that have to be allowed to draw, we stop somewhere
                                              // mid
                                              // where it was NOT saphiras turn

        DummyClient saphira = tale.getClient("saphira");
        DummyClient walter = tale.getClient("walter");
        DummyClient specti = tale.getClient("Petterson");

        saphira.getMessages().assertLastTypes(MessageTypeEnum.REQUEST_GAME_OPERATION);
        walter.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_STATUS);

        taleBoard.crash("saphira"); // poor saphira

        saphira.getMessages().assertLastTypes(MessageTypeEnum.REQUEST_GAME_OPERATION);
        walter.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);

        taleBoard.reconnect("saphira", "saphira-1");

        saphira.getMessages().assertLastTypes(MessageTypeEnum.REQUEST_GAME_OPERATION);
        tale.getClient("saphira-1").getMessages().assertLastTypes(MessageTypeEnum.REQUEST_GAME_OPERATION);
        walter.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);
        specti.getMessages().assertLastTypes(MessageTypeEnum.GAME_PAUSE);

    }
}