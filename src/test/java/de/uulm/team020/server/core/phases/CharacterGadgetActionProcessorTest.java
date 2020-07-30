package de.uulm.team020.server.core.phases;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Cocktail;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.WiretapWithEarplugs;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.helper.GameFieldPositioner;
import de.uulm.team020.server.game.phases.main.statistics.IntegerStatisticsBuilder;
import de.uulm.team020.validation.GameDataGson;

/**
 * Tests for gadget actions.
 *
 * @author Lennart Altenhof
 * @author Florian Sihler
 * 
 * @version 1.1, 06/19/2020
 */
class CharacterGadgetActionProcessorTest {

    /**
     * Timeout for waiting for messages.
     */
    final int TIMEOUT_MESSAGES = 1500;

    /**
     * Arguments used in {@link #test_laserCompact(Point, boolean, boolean)}.
     *
     * @return The arguments
     * @see #test_laserCompact(Point, boolean, boolean)
     */
    private static Stream<Arguments> gen_laserCompact() {
        return Stream.of(Arguments.arguments(new Point(5, 11), true, true),
                Arguments.arguments(new Point(3, 5), false, true), Arguments.arguments(new Point(6, 8), false, false),
                Arguments.arguments(new Point(2, 11), false, false),
                Arguments.arguments(new Point(3, 9), false, false));
    }

    /**
     * Arguments used in {@link #test_grapple_barTable(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_grapple_barTable(Point, boolean)
     */
    private static Stream<Arguments> gen_grapple_barTable() {
        return Stream.of(Arguments.arguments(new Point(3, 1), true), Arguments.arguments(new Point(1, 3), false),
                Arguments.arguments(new Point(3, 5), true), Arguments.arguments(new Point(6, 8), false),
                Arguments.arguments(new Point(3, 9), false), Arguments.arguments(new Point(7, 3), false));
    }

    /**
     * Arguments used in {@link #test_rocketPen(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_rocketPen(Point, boolean)
     */
    private static Stream<Arguments> gen_rocketPen() {
        return Stream.of(Arguments.arguments(new Point(5, 2), true), Arguments.arguments(new Point(7, 2), false),
                Arguments.arguments(new Point(2, 7), true), Arguments.arguments(new Point(2, 9), false));
    }

    /**
     * Arguments used in {@link #test_mothballPouch(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_mothballPouch(Point, boolean)
     */
    private static Stream<Arguments> gen_mothballPouch() {
        return Stream.of(Arguments.arguments(new Point(5, 2), true), Arguments.arguments(new Point(4, 6), false),
                Arguments.arguments(new Point(1, 1), true), Arguments.arguments(new Point(2, 7), false));
    }

    /**
     * Arguments used in {@link #test_moledie(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_moledie(Point, boolean)
     */
    private static Stream<Arguments> gen_moledie() {
        return Stream.of(Arguments.arguments(new Point(3, 3), true), Arguments.arguments(new Point(3, 5), true),
                Arguments.arguments(new Point(0, 0), false), // should fail bc of range
                Arguments.arguments(new Point(7, 7), false), // should fail bc of wall
                Arguments.arguments(new Point(8, 8), false), // should fail bc of los
                Arguments.arguments(new Point(5, 7), false), // should fail bc of cat
                Arguments.arguments(new Point(5, 3), true));
    }

    /**
     * Arguments used in {@link #test_jetpack(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_jetpack(Point, boolean)
     */
    private static Stream<Arguments> gen_jetpack() {
        return Stream.of(Arguments.arguments(new Point(7, 10), true), Arguments.arguments(new Point(7, 7), false), // wall
                Arguments.arguments(new Point(7, 8), false), // fireplace
                Arguments.arguments(new Point(5, 5), true), // self
                Arguments.arguments(new Point(7, 9), false) // character
        );
    }

    /**
     * Arguments used in {@link #test_fogTin(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_jetpack(Point, boolean)
     */
    private static Stream<Arguments> gen_fogTin() {
        return Stream.of(Arguments.arguments(new Point(3, 3), true), Arguments.arguments(new Point(0, 0), false), // out
                                                                                                                  // of
                                                                                                                  // range
                Arguments.arguments(new Point(7, 7), false), // wall not allowed
                Arguments.arguments(new Point(7, 5), true), Arguments.arguments(new Point(-1, 5), false) // out of field
        );
    }

    /**
     * Arguments used in {@link #test_cocktail_pickup(Point, boolean)}.
     *
     * @return The arguments
     * @see #gen_cocktail_pickup()
     */
    private static Stream<Arguments> gen_cocktail_pickup() {
        return Stream.of(Arguments.arguments(new Point(5, 6), true), //
                Arguments.arguments(new Point(7, 5), false), // out of range
                Arguments.arguments(new Point(5, 7), false), // out of range
                Arguments.arguments(new Point(4, 5), false), // fireplace
                Arguments.arguments(new Point(6, 5), false) // free
        );
    }

    /**
     * Arguments used in {@link #test_cocktail_pour(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_cocktail_pour(Point, boolean)
     */
    private static Stream<Arguments> gen_cocktail_pour() {
        return Stream.of(Arguments.arguments(new Point(4, 5), true), Arguments.arguments(new Point(4, 5), false),
                Arguments.arguments(new Point(4, 4), false));
    }

    /**
     * Arguments used in {@link #test_cocktail_drink(boolean, boolean)}.
     *
     * @return The arguments
     * @see #test_cocktail_drink(boolean, boolean)
     */
    private static Stream<Arguments> gen_cocktail_drink() {
        return Stream.of(Arguments.arguments(true, true), Arguments.arguments(true, false),
                Arguments.arguments(false, true));
    }

    /**
     * Arguments used in {@link #test_chickenFeed(Point, int, boolean)}.
     *
     * @return The arguments
     * @see #test_chickenFeed(Point, int, boolean)
     */
    private static Stream<Arguments> gen_chickenFeed() {
        final List<Arguments> args = new LinkedList<>();
        final Map<Point, Boolean> points = Map.of(new Point(6, 5), true, new Point(5, 3), false, // out of range
                new Point(5, 6), false // own faction
        );
        points.forEach((p, b) -> {
            for (int i = -1; i < 2; i++) {
                args.add(Arguments.arguments(p, i, b));
            }
        });
        args.add(Arguments.arguments(new Point(4, 5), 0, true)); // npc
        return args.stream();
    }

    /**
     * Arguments used in {@link #test_nugget(Point, boolean, boolean)}.
     *
     * @return The Arguments
     * @see #test_nugget(Point, boolean, boolean)
     */
    private static Stream<Arguments> gen_nugget() {
        return Stream.of(Arguments.arguments(new Point(5, 4), false, false), // opponent
                Arguments.arguments(new Point(5, 0), true, false), // opponent, out of range
                Arguments.arguments(new Point(4, 5), true, false), // own faction
                Arguments.arguments(new Point(6, 5), false, true) // npc
        );
    }

    /**
     * Arguments used in {@link #test_mirrorOfWilderness(Point, boolean, boolean)}
     * (Point, boolean, boolean)}.
     *
     * @return The Arguments
     * @see #test_nugget(Point, boolean, boolean)
     */
    private static Stream<Arguments> gen_mirrorOfWilderness() {
        return Stream.of(Arguments.arguments(new Point(5, 4), false, false), // opponent
                Arguments.arguments(new Point(5, 0), true, false), // opponent, out of range
                Arguments.arguments(new Point(4, 5), false, true), // own faction
                Arguments.arguments(new Point(6, 5), false, false) // npc
        );
    }

    /**
     * Arguments for {@link #test_bowlerBlade(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_bowlerBlade(Point, boolean)
     */
    private static Stream<Arguments> gen_bowlerBlade() {
        return Stream.of(Arguments.arguments(new Point(5, 3), true), // valid
                Arguments.arguments(new Point(15, 15), false), // out of range
                Arguments.arguments(new Point(8, 5), false), // character in los
                Arguments.arguments(new Point(5, 8), false), // wall in los
                Arguments.arguments(new Point(2, 2), true) // magnetic field watch
        );
    }

    /**
     * Arguments used in {@link #test_hairdryer(Point, boolean)}.
     *
     * @return The arguments
     * @see #test_hairdryer(Point, boolean)
     */
    private static Stream<Arguments> gen_hairdryer() {
        return Stream.of(Arguments.arguments(new Point(5, 4), true), // CC
                Arguments.arguments(new Point(6, 5), true), // CCC
                Arguments.arguments(new Point(5, 6), true), // NCC
                Arguments.arguments(new Point(3, 5), false) // OOR
        );
    }

    /**
     * Tests the technicolour prism gadget action.
     *
     * @throws IOException If stuff goes wrong
     */
    @Test
    @DisplayName("[Action] Technicolour prism")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_technicolourPrism() throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/technicolor-prism-story.story");
        Story tale = taleBoard.build();

        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayHansOtto = controller.decodeCharacterByName("Hans Peter Otto");
        Assertions.assertTrue(mayHansOtto.isPresent(), "Character Hans Peter Otto should be present.");
        final Character hansOtto = mayHansOtto.get();
        final int preAP = hansOtto.getAp();
        // assert character holds the technicolour prism
        Assertions.assertTrue(hansOtto.getGadgetType(GadgetEnum.TECHNICOLOUR_PRISM).isPresent(),
                "Character should hold technicolour prism gadget.");
        final Point target = new Point(3, 3);
        Field rouletteTable = controller.getMap().getSpecificField(target);
        Assertions.assertNotNull(rouletteTable, "Target field does not exist.");
        // assert roulette table is not inverted
        Assertions.assertFalse(rouletteTable.isInverted(), "Roulette table should not be inverted at the beginning.");
        // action
        DummyClient client = tale.getClient("saphira");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(hansOtto.getCharacterId(), target, GadgetEnum.TECHNICOLOUR_PRISM));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        // assure technicolour prism has been removed
        Assertions.assertTrue(hansOtto.getGadgetType(GadgetEnum.TECHNICOLOUR_PRISM).isEmpty(),
                "Character should not still have technicolour prism after having used it.");
        // assure aps have been decremented
        Assertions.assertEquals(preAP - 1, hansOtto.getAp(), "AP should have been decremented.");
        // assure target field has been set to inverted
        Assertions.assertTrue(rouletteTable.isInverted(), "Roulette table should be inverted.");
        // assure client received GAME_STATUS message
        Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS));
    }

    /**
     * Tests the poison pills gadget action.
     *
     * @throws IOException If stuff goes wrong
     */
    @Test
    @DisplayName("[Action] Poison pills")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_poisonPills() throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/poison-pills-story.story");
        Story tale = taleBoard.build();

        // target point
        final Point target = new Point(3, 1);
        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayHansOtto = controller.decodeCharacterByName("Hans Peter Otto");
        Assertions.assertTrue(mayHansOtto.isPresent(), "Character Hans Peter Otto should be present.");
        final Character hansOtto = mayHansOtto.get();
        // save ap for later comparison
        final int preAp = hansOtto.getAp();
        // assert character holds the poison pills gadget
        final Optional<Gadget> mayPoisonPills = hansOtto.getGadgetType(GadgetEnum.POISON_PILLS);
        Assertions.assertTrue(mayPoisonPills.isPresent(), "Character should hold the poison pills gadget.");
        final Gadget poisonPills = mayPoisonPills.get();
        // save usages for later comparison
        final int preUsages = poisonPills.getUsages();
        // assert target cocktail is not poisoned
        final Field barField = controller.getMap().getSpecificField(target);
        Assertions.assertTrue(barField.getGadget() != null && barField.getGadget().getGadget() == GadgetEnum.COCKTAIL,
                "Target bar field does not hold any cocktail.");
        final Cocktail cocktail = (Cocktail) barField.getGadget();
        Assertions.assertFalse(cocktail.getPoisoned(), "Cocktail should not be poisoned at the beginning.");
        // action
        DummyClient client = tale.getClient("saphira");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(hansOtto.getCharacterId(), target, GadgetEnum.POISON_PILLS));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS));
        // assure poison pills usages have been decremented
        Assertions.assertEquals(preUsages - 1, poisonPills.getUsages(),
                "Remaining usages should have been decremented.");
        // assure ap have been decremented
        Assertions.assertEquals(preAp - 1, hansOtto.getAp(), "AP should have been decremented.");
        // assure target cocktail is poisoned
        Assertions.assertTrue(cocktail.getPoisoned(), "Cocktail should be poisoned.");
    }

    /**
     * Tests the gas gloss gadget action
     *
     * @throws IOException If stuff goes wrong
     */
    @Test
    @DisplayName("[Action] Gas gloss")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_gasGloss() throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/gas-gloss-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayAgentDieter = controller.decodeCharacterByName("Austauschbarer Agent Dieter 42");
        Assertions.assertTrue(mayAgentDieter.isPresent(),
                "Character Austauschbarer Agent Dieter 42 should be present.");
        final Character agentDieter = mayAgentDieter.get();
        // save ap for later comparison
        final int preAp = agentDieter.getAp();
        // assert character holds the gas gloss gadget
        final Optional<Gadget> mayGasGloss = agentDieter.getGadgetType(GadgetEnum.GAS_GLOSS);
        Assertions.assertTrue(mayGasGloss.isPresent(), "Character should hold the gas gloss gadget.");
        // target setup
        final Point targetPoint = new Point(5, 3);
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(targetPoint);
        Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should be present.");
        final Character targetCharacter = mayTargetCharacter.get();
        final int preTargetHp = targetCharacter.getHp();
        Assertions.assertTrue(targetCharacter.getHp() > 0, "Target character should be alive.");
        // action
        DummyClient client = tale.getClient("dieter");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(agentDieter.getCharacterId(), targetPoint, GadgetEnum.GAS_GLOSS));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS));
        // assure ap has been decremented
        Assertions.assertEquals(preAp - 1, agentDieter.getAp(), "AP should have been decremented.");
        // assure gas gloss has been removed from inventory
        Assertions.assertTrue(agentDieter.getGadgetType(GadgetEnum.GAS_GLOSS).isEmpty(),
                "Gas gloss should have been removed from characters inventory after usage.");
        // assure target characters hp has been reduced
        final int damage = controller.getConfiguration().getMatchconfig().getGasGlossDamage();
        Assertions.assertEquals(preTargetHp - damage, targetCharacter.getHp(),
                "Target character's hp should have been reduced by gas gloss damage specified in " + "match config.");
        // assure statistics have been set accordingly
        IntegerStatisticsBuilder statisticsBuilder = controller.getStatisticsProvider().getDamageReceived();
        final Optional<GameRoleEnum> playerRole = controller.decodePlayerGameRoleByCharacter(agentDieter);
        if (playerRole.isEmpty() || playerRole.get() == GameRoleEnum.SPECTATOR) {
            Assertions.fail("Ehw according to gas-gloss-story agent dieter should be a player.");
        } else {
            // check statistics for victim
            controller.decodePlayerGameRoleByCharacter(targetCharacter)
                    .ifPresent(role -> Assertions.assertEquals(damage,
                            role == GameRoleEnum.PLAYER_ONE ? statisticsBuilder.getPlayerOneValue()
                                    : statisticsBuilder.getPlayerTwoValue(),
                            "Target player should have received " + damage + " damage."));
        }
    }

    /**
     * Tests the gas gloss gadget with assured exfiltration.
     *
     * @throws IOException If stuff goes wrong
     */
    @Test
    @DisplayName("[Action] Gas gloss with exfiltration")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_gasGlossWithExfiltration() throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/gas-gloss-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayAgentDieter = controller.decodeCharacterByName("Austauschbarer Agent Dieter 42");
        Assertions.assertTrue(mayAgentDieter.isPresent(),
                "Character Austauschbarer Agent Dieter 42 should be present.");
        final Character agentDieter = mayAgentDieter.get();
        // target setup
        final Point targetPoint = new Point(5, 3);
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(targetPoint);
        Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should be present.");
        final Character targetCharacter = mayTargetCharacter.get();
        final int damage = controller.getConfiguration().getMatchconfig().getGasGlossDamage();
        // set targets hp to damage so that he will definitely die (should I feel
        // guilty?)
        targetCharacter.setHp(damage);
        Assertions.assertTrue(targetCharacter.getHp() > 0, "Target character should be alive.");
        Assertions.assertEquals(damage, targetCharacter.getHp(),
                "Target should have " + damage + " hp in order to die by gas gloss action.");
        // action
        DummyClient client = tale.getClient("dieter");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(agentDieter.getCharacterId(), targetPoint, GadgetEnum.GAS_GLOSS));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS));
        final GameStatusMessage gameStatusMessage = MessageContainer.getMessage(client.getMessages().getFirst());
        Assertions.assertNotNull(gameStatusMessage, "First message should be game status message.");
        Assertions.assertFalse(gameStatusMessage.getOperations().isEmpty(), "There should be at least one operation.");
        // check for exfiltration operation
        Assertions.assertTrue(
                gameStatusMessage.getOperations().stream().anyMatch(o -> o.getType() == OperationEnum.EXFILTRATION),
                "There should be an exfiltration operation.");
        // check upon our dead target
        Assertions.assertEquals(1, targetCharacter.getHp(), "Target character should have 1 hp (by exfiltration).");
        Assertions.assertTrue(targetCharacter.isExfiltrated(), "Target should be exfiltrated.");
        // check if teleportation succeeded
        final Field barSeat = controller.getMap().getSpecificField(targetCharacter.getCoordinates());
        Assertions.assertNotNull(barSeat, "Target character should be on a valid field. How did he get there?");
        Assertions.assertEquals(FieldStateEnum.BAR_SEAT, barSeat.getState(), "Target should be on a BAR_SEAT field.");
    }

    /**
     * Tests the laser compact gadget action.
     *
     * @param targetPoint         The target
     * @param targetIsCharacter   Whether the target is character (would be equipped
     *                            with a cocktail)
     * @param validTargetExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_laserCompact()
     */
    @ParameterizedTest
    @MethodSource("gen_laserCompact")
    @DisplayName("[Action] Laser compact")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_laserCompact(Point targetPoint, boolean targetIsCharacter, boolean validTargetExpected)
            throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/laser-compact-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayWischmob = controller.decodeCharacterByName("Ein Wischmob");
        Assertions.assertTrue(mayWischmob.isPresent(), "Character Ein Wischmob should be present.");
        final Character wischmob = mayWischmob.get();
        final int preAp = wischmob.getAp();
        // setup character victim
        Character targetCharacter = null;
        // if test targets a character, give him a cocktail
        if (targetIsCharacter) {
            Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(targetPoint);
            Assertions.assertTrue(mayTargetCharacter.isPresent(),
                    "There should be a target character on the target point.");
            targetCharacter = mayTargetCharacter.get();
            targetCharacter.addGadget(new Cocktail(false));
        }
        // assert character holds the gadget
        Assertions.assertTrue(wischmob.getGadgetType(GadgetEnum.LASER_COMPACT).isPresent(),
                "Character should hold the laser-compact gadget.");
        Assertions.assertTrue(preAp > 0, "Character should have AP left.");
        // action
        DummyClient client = tale.getClient("dieter");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(wischmob.getCharacterId(), targetPoint, GadgetEnum.LASER_COMPACT));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (validTargetExpected) {
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS));
            // get success result of operation
            final GameStatusMessage gameStatusMessage = GameDataGson.fromJson(client.getMessages().getFirst(),
                    GameStatusMessage.class);
            Assertions.assertEquals(1, gameStatusMessage.getOperations().size(),
                    "Only one operation should have happened.");
            final boolean operationSuccess = gameStatusMessage.getOperations().get(0).getSuccessful();
            if (targetIsCharacter) {
                final Optional<Gadget> mayCocktail = targetCharacter.getGadgetType(GadgetEnum.COCKTAIL);
                if (operationSuccess) {
                    // cocktail should not exist
                    Assertions.assertTrue(mayCocktail.isEmpty());
                } else {
                    // cocktail should still exist
                    Assertions.assertTrue(mayCocktail.isPresent());
                }
            } else {
                Assertions.assertEquals(operationSuccess,
                        controller.getMap().getSpecificField(targetPoint).getGadget() == null,
                        "Should be null if success and not null otherwise.");
            }
            // assert ap decremented
            Assertions.assertEquals(preAp - 1, wischmob.getAp(), "AP should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR));
        }
    }

    /**
     * Tests the rocket pen gadget action.
     *
     * @param targetPoint                 The target
     * @param targetInLineOfSightExpected Whether the target is expected to be in
     *                                    line of sight
     * @throws IOException If stuff goes wrong
     * @see #gen_rocketPen()
     */
    @ParameterizedTest
    @MethodSource("gen_rocketPen")
    @DisplayName("[Action] Rocket pen")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_rocketPen(Point targetPoint, boolean targetInLineOfSightExpected) throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/rocket-pen-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayAgentDieter = controller.decodeCharacterByName("Austauschbarer Agent Dieter 42");
        Assertions.assertTrue(mayAgentDieter.isPresent(),
                "Character Austauschbarer Agent Dieter 42 should be present.");
        final Character agentDieter = mayAgentDieter.get();
        final int preAp = agentDieter.getAp();

        // assure existing target and get impact area
        Assertions.assertNotNull(controller.getMap().getSpecificField(targetPoint), "Target should exist on map.");
        final Optional<Set<Point>> mayImpactArea = controller.getMap().getNeighboursOfSpecificField(targetPoint);
        Assertions.assertTrue(mayImpactArea.isPresent(), "Target should exist on map.");
        final Set<Point> impactArea = mayImpactArea.get();
        impactArea.add(targetPoint);
        final Optional<List<Field>> mayImpactFields = controller.getMap()
                .getNeighbourFieldsOfSpecificField(targetPoint);
        Assertions.assertTrue(mayImpactFields.isPresent(), "Field should exist on map.");
        final List<Field> impactFields = mayImpactFields.get();
        impactFields.add(controller.getMap().getSpecificField(targetPoint));
        // save wall fields
        final Set<Field> preImpactWalls = impactFields.stream().filter(field -> field.getState() == FieldStateEnum.WALL)
                .collect(Collectors.toSet());
        // save characters
        final Map<Character, Integer> preImpactCharacters = new HashMap<>();
        impactArea.forEach(point -> controller.decodeCharacterByPosition(point).ifPresent(c -> {
            if (!c.getName().equals("<cat>") && !c.getName().equals("<janitor>")) {
                preImpactCharacters.put(c, c.getHp());
            }
        }));
        // action
        DummyClient client = tale.getClient("dieter");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(agentDieter.getCharacterId(), targetPoint, GadgetEnum.ROCKET_PEN));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (targetInLineOfSightExpected) {
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message.");
            // assert walls have been destroyed
            preImpactWalls.forEach(wall -> Assertions.assertEquals(FieldStateEnum.FREE, wall.getState(),
                    "State of wall in impact area should now be FREE. Not fulfilled for: " + wall));
            // assert characters have been damaged (hp lower than before launch)
            preImpactCharacters.forEach((c, preHp) -> Assertions.assertTrue(c.getHp() < preHp,
                    "Character should have less hp than before launch."));
            // assert ap decremented
            Assertions.assertEquals(preAp - 1, agentDieter.getAp(), "AP should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR));
        }
    }

    /**
     * Tests the mothball pouch gadget action.
     *
     * @param targetPoint         The target
     * @param validTargetExpected Whether the target is expected to be valid (in
     *                            terms of range and line of sight)
     * @throws IOException If stuff goes wrong
     * @see #gen_mothballPouch()
     */
    @ParameterizedTest
    @MethodSource("gen_mothballPouch")
    @DisplayName("[Action] Mothball pouch")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_mothballPouch(Point targetPoint, boolean validTargetExpected) throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/mothball-pouch-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> mayAgentDieter = controller.decodeCharacterByName("Austauschbarer Agent Dieter 42");
        Assertions.assertTrue(mayAgentDieter.isPresent(),
                "Character Austauschbarer Agent Dieter 42 should be present.");
        final Character agentDieter = mayAgentDieter.get();
        final int preAp = agentDieter.getAp();
        final Optional<Gadget> mayMothballPouch = agentDieter.getGadgetType(GadgetEnum.MOTHBALL_POUCH);
        Assertions.assertTrue(mayMothballPouch.isPresent(), "Character should own mothball-pouch gadget");
        final Gadget mothballPouch = mayMothballPouch.get();
        final int preUsages = mothballPouch.getUsages();

        // assure existing target and get impact area
        Assertions.assertNotNull(controller.getMap().getSpecificField(targetPoint), "Target should exist on map.");
        final Optional<Set<Point>> mayImpactArea = controller.getMap().getNeighboursOfSpecificField(targetPoint);
        Assertions.assertTrue(mayImpactArea.isPresent(), "Target should exist on map.");
        final Set<Point> impactArea = mayImpactArea.get();
        impactArea.add(targetPoint);
        // save characters
        final Map<Character, Integer> preImpactCharacters = new HashMap<>();
        impactArea.forEach(point -> controller.decodeCharacterByPosition(point).ifPresent(c -> {
            if (!c.getName().equals("<cat>") && !c.getName().equals("<janitor>")) {
                preImpactCharacters.put(c, c.getHp());
            }
        }));
        // action
        DummyClient client = tale.getClient("dieter");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(agentDieter.getCharacterId(), targetPoint, GadgetEnum.MOTHBALL_POUCH));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (validTargetExpected) {
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message.");
            // assert characters have been damaged (hp lower than before launch)
            preImpactCharacters.forEach((c, preHp) -> Assertions.assertTrue(c.getHp() < preHp,
                    "Character should have less hp than before launch."));
            // assert ap decremented
            Assertions.assertEquals(preAp - 1, agentDieter.getAp(), "AP should have been decremented by one.");
            // assert gadget usages decremented
            Assertions.assertEquals(preUsages - 1, mothballPouch.getUsages(),
                    "Usages of gadget should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR));
        }
    }

    /**
     * Tests the grapple action on bar tables.
     *
     * @param targetPoint         The target
     * @param validTargetExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_grapple_barTable()
     */
    @ParameterizedTest
    @MethodSource("gen_grapple_barTable")
    @DisplayName("[Action] Grapple")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_grapple_barTable(Point targetPoint, boolean validTargetExpected) throws IOException {
        StoryBoard taleBoard = new StoryBoard().dontExpand()
                .executeInternalFile("stories/main-phase/gadget-stories/grapple-story.story");
        Story tale = taleBoard.build();

        // get character
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        final Optional<Character> maySaphira = controller.decodeCharacterByName("Saphira");
        Assertions.assertTrue(maySaphira.isPresent(), "Character Saphira should be present.");
        final Character saphira = maySaphira.get();
        final int preAp = saphira.getAp();
        // assert character holds the gadget
        Assertions.assertTrue(saphira.getGadgetType(GadgetEnum.GRAPPLE).isPresent(),
                "Character should hold the grapple gadget.");
        Assertions.assertTrue(preAp > 0, "Character should have AP left.");
        // get field and gadget
        final Field targetField = controller.getMap().getSpecificField(targetPoint);
        if (validTargetExpected) {
            // assert bar table
            Assertions.assertSame(FieldStateEnum.BAR_TABLE, targetField.getState(),
                    "This test only tests using grapple on bar tables.");
        }
        final Gadget targetGadget = targetField.getGadget();
        // action
        DummyClient client = tale.getClient("saphira");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(saphira.getCharacterId(), targetPoint, GadgetEnum.GRAPPLE));
        client.getMessages().clear();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (validTargetExpected) {
            synchronized (client.getMessages()) {
                Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                        "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            }
            // get success result of operation
            final GameStatusMessage gameStatusMessage = GameDataGson.fromJson(client.getMessages().getFirst(),
                    GameStatusMessage.class);
            Assertions.assertEquals(1, gameStatusMessage.getOperations().size(),
                    "Only one operation should have happened.");
            final boolean operationSuccess = gameStatusMessage.getOperations().get(0).getSuccessful();

            Assertions.assertEquals(operationSuccess, targetField.getGadget() == null,
                    "Gadget should have been removed from the target field if success and not removed otherwise.");
            Assertions.assertEquals(operationSuccess, saphira.getGadgetType(targetGadget.getGadget()).isPresent(),
                    "Character should now own the gadget that was previously on target field and not otherwise.");

            // assert ap decremented
            Assertions.assertEquals(preAp - 1, saphira.getAp(), "AP should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR));
        }
    }

    /**
     * Tests the moledie action.
     *
     * @param target          The target for the action
     * @param successExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_moledie()
     */
    @ParameterizedTest
    @MethodSource("gen_moledie")
    @DisplayName("[Action] Moledie")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_moledie(Point target, boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER_INVALID = "Meister Yoda";
        final String TARGET_CHARACTER_VALID = "Tante Gertrude";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER_INVALID, new Point(5, 4),
                TARGET_CHARACTER_VALID, new Point(5, 3));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("dieter", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(Map.of(CHARACTER, List.of(GadgetEnum.MOLEDIE)), 4,
                Map.of(TARGET_CHARACTER_INVALID, List.of(), TARGET_CHARACTER_VALID, List.of()), 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(7, 7), new Field(FieldStateEnum.WALL));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((n, p) -> guidedCharacterPlacement(builder, n, p));
        guidedCharacterPlacement(builder, "<cat>", new Point(5, 7));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        final Story tale = storyBoard.build();

        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free fields
        builder.unlock();
        final Set<Point> fieldsFree = new HashSet<>();
        targetCharacters.forEach(
                (name, point) -> controller.getMap().getNeighboursOfSpecificField(point).ifPresent(fieldsFree::addAll));
        fieldsFree.removeIf(targetCharacters::containsValue);
        fieldsFree.remove(new Point(5, 7)); // cat
        fieldsFree.removeIf(p -> {
            final Optional<Character> oc = controller.decodeCharacterByPosition(p);
            return (oc.isPresent() && (oc.get().getName().equals("<cat>") || oc.get().getName().equals("James Bond")));
        });
        builder.assureFieldsFree(fieldsFree);
        builder.lock();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // get target character
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);

        // action
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.MOLEDIE));
        tale.eraseAllMemories();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            // assure target character now owns moledie (if was direct target)
            if (mayTargetCharacter.isPresent()) {
                Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.MOLEDIE).isEmpty(),
                        "Character should not own moledie anymore because target was a character.");
                Assertions.assertTrue(mayTargetCharacter.get().getGadgetType(GadgetEnum.MOLEDIE).isPresent(),
                        "Target should own moledie now because of being target.");
            }
            // assure ap have been decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Expected error message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the jetpack gadget action.
     *
     * @param target          The target
     * @param successExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_jetpack()
     */
    @ParameterizedTest
    @MethodSource("gen_jetpack")
    @DisplayName("[Action] Jetpack")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_jetpack(Point target, boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(CHARACTER, List.of(GadgetEnum.JETPACK), "Meister Yoda", List.of(), 4, 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(7, 7), new Field(FieldStateEnum.WALL));
        guidedFieldChange(builder, new Point(7, 8), new Field(FieldStateEnum.FIREPLACE));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        guidedCharacterPlacement(builder, "Meister Yoda", new Point(7, 9));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        builder.assureFieldsFree(new Point(7, 10));
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free point 7,10
        controller.decodeCharacterByPosition(new Point(7, 10)).ifPresent(c -> {
            final Optional<Point> mayClosestFreeField = GameFieldPositioner.getClosestFreeField(
                    controller.getAllCharacters(), controller.getMap(), c.getCoordinates(),
                    controller.getRandomController(), true);
            Assertions.assertTrue(mayClosestFreeField.isPresent(), "No free field available.");
            c.setCoordinates(mayClosestFreeField.get());
        });
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // action
        tale.eraseAllMemories();
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.JETPACK));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message.");
            // assure new position
            Assertions.assertEquals(target, jamesBond.getCoordinates(), "Character should now be on target position.");
            // assure gadget has been removed
            Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.JETPACK).isEmpty(),
                    "Character should not own jetpack gadget anymore.");
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message.");
        }
    }

    /**
     * Tests the fog tin gadget action.
     *
     * @param target          The target
     * @param successExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_fogTin()
     */
    @ParameterizedTest
    @MethodSource("gen_fogTin")
    @DisplayName("[Action] Fog tin")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_fogTin(final Point target, boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(CHARACTER, List.of(GadgetEnum.FOG_TIN), 4, 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(7, 7), new Field(FieldStateEnum.WALL));
        guidedFieldChange(builder, new Point(7, 5), new Field(FieldStateEnum.FIREPLACE));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // get impact area
        final Optional<List<Field>> mayImpactArea = controller.getMap().getNeighbourFieldsOfSpecificField(target);
        List<Field> impactArea;
        if (successExpected) {
            Assertions.assertTrue(mayImpactArea.isPresent(), "There should have been a valid impact area.");
            impactArea = mayImpactArea.get();
            impactArea.add(controller.getMap().getSpecificField(target));
        } else {
            impactArea = Collections.emptyList();
        }
        // action
        tale.eraseAllMemories();
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.FOG_TIN));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message because of valid target but got " + client.getMessages()
                            + ".");
            // check impact area
            impactArea.forEach(f -> Assertions.assertTrue(f.isFoggy(),
                    "Field " + f + " located in impact area should now be foggy."));
            // gadget removed
            Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.FOG_TIN).isEmpty(),
                    "Fog tin gadget should have been removed from character's inventory.");
            // aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented.");
            // 3 rounds later
            for (int i = 0; i < 3; i++) {
                controller.nextRound();
            }
            // check impact area (no foggy fields expected)
            impactArea.forEach(f -> Assertions.assertFalse(f.isFoggy(),
                    "Field " + f + " located in impact area should no longer be foggy."));
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message because of no valid target but got " + client.getMessages()
                            + ".");
        }
    }

    /**
     * Tests the cocktail pickup action.
     *
     * @param target          The target
     * @param successExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_cocktail_pickup()
     */
    @ParameterizedTest
    @MethodSource("gen_cocktail_pickup")
    @DisplayName("[Action] Cocktail pickup")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_cocktail_pickup(final Point target, final boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(Map.of(CHARACTER, List.of()), 4, Map.of(), 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(5, 6), new Field(FieldStateEnum.BAR_TABLE));
        guidedFieldChange(builder, new Point(7, 5), new Field(FieldStateEnum.BAR_TABLE));
        guidedFieldChange(builder, new Point(5, 7), new Field(FieldStateEnum.BAR_TABLE));
        guidedFieldChange(builder, new Point(4, 5), new Field(FieldStateEnum.FIREPLACE));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        builder.assureFieldFree(new Point(6, 5));
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.COCKTAIL));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message because of valid target but got " + client.getMessages()
                            + ".");
            // assure target field no longer holds cocktail
            Assertions.assertNull(controller.getMap().getSpecificField(target).getGadget(),
                    "Target field should no longer hold a gadget.");
            // assure character now holds cocktail
            Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.COCKTAIL).isPresent(),
                    "Character should now hold cocktail gadget.");
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message because of no valid target but got " + client.getMessages()
                            + ".");
        }
    }

    /**
     * Tests pouring a cocktail. The case where the target is not a character is
     * covered by {@link #test_cocktail_pickup(Point, boolean)}.
     *
     * @param holdsCocktail Whether the character should hold a cocktail when
     *                      performing the action (will fail if he does not)
     * @throws IOException If stuff goes wrong
     * @see #gen_cocktail_pour()
     */
    @ParameterizedTest
    @MethodSource("gen_cocktail_pour")
    @DisplayName("[Action] Cocktail pour")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_cocktail_pour(Point target, boolean holdsCocktail) throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER = "Tante Gertrude";
        final String TARGET_CHARACTER_WILL_BE_SAVED = "Meister Yoda";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER, new Point(4, 5),
                TARGET_CHARACTER_WILL_BE_SAVED, new Point(4, 4));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(Map.of(CHARACTER, holdsCocktail ? List.of(GadgetEnum.COCKTAIL) : List.of()), 4,
                Map.of(TARGET_CHARACTER, List.of(), TARGET_CHARACTER_WILL_BE_SAVED, List.of()), 4);
        builder.prepareMainPhase();
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((s, point) -> guidedCharacterPlacement(builder, s, point));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // get fields to be free
        final Set<Point> fieldsFree = new HashSet<>();
        controller.getMap().getNeighboursOfSpecificField(targetCharacters.get(TARGET_CHARACTER))
                .ifPresent(fieldsFree::addAll);
        fieldsFree.remove(targetCharacters.get(TARGET_CHARACTER_WILL_BE_SAVED));
        fieldsFree.remove(jamesBond.getCoordinates());
        builder.unlock();
        builder.assureFieldsFree(fieldsFree);
        builder.lock();
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.COCKTAIL));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (holdsCocktail) {
            // holds cocktail and valid target
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message because target was valid but got " + client.getMessages()
                            + ".");
            // assert target character has now CLAMMY_CLOTHES property
            // (gadget-action-matchconfig.match holds a
            // cocktailDodgeChance of 0.0)
            final Optional<Character> mayTargetCharacter = controller.decodeCharacterByName(TARGET_CHARACTER);
            Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should be present.");
            Assertions.assertTrue(mayTargetCharacter.get().getProperties().contains(PropertyEnum.CLAMMY_CLOTHES),
                    "Target character " + mayTargetCharacter.get() + " should now have CLAMMY_CLOTHES property.");
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // does not hold cocktail
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests pouring a cocktail. The case where the target is not a character is
     * covered by {@link #test_cocktail_pickup(Point, boolean)}.
     *
     * @param holdsCocktail Whether the character should hold a cocktail when
     *                      performing the action (will fail if he does not)
     * @throws IOException If stuff goes wrong
     * @see #gen_cocktail_drink()
     */
    @ParameterizedTest
    @MethodSource("gen_cocktail_drink")
    @DisplayName("[Action] Cocktail drink")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_cocktail_drink(final boolean holdsCocktail, final boolean cocktailPoisoned) throws IOException {
        final String CHARACTER = "The legendary Gustav";
        final Point pos = new Point(5, 5);
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(CHARACTER, holdsCocktail ? List.of(GadgetEnum.COCKTAIL) : List.of(), 4, 4);
        builder.prepareMainPhase();
        guidedCharacterPlacement(builder, CHARACTER, pos);
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        // get character
        final Optional<Character> mayTheLegendaryGustav = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayTheLegendaryGustav.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character theLegendaryGustav = mayTheLegendaryGustav.get();
        final int preAp = theLegendaryGustav.getAp();
        final int preHp = theLegendaryGustav.getHp();
        // poison cocktail if needed
        if (holdsCocktail && cocktailPoisoned) {
            final Optional<Gadget> mayCocktail = theLegendaryGustav.getGadgetType(GadgetEnum.COCKTAIL);
            Assertions.assertTrue(mayCocktail.isPresent(), "Character should hold cocktail gadget.");
            ((Cocktail) mayCocktail.get()).setPoisoned(true);
        }
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(theLegendaryGustav.getCharacterId(), pos, GadgetEnum.COCKTAIL));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (holdsCocktail) {
            // holds cocktail --> success expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message because of holding cocktail and valid target but got "
                            + client.getMessages() + ".");
            // assure not holding cocktail anymore
            Assertions.assertTrue(theLegendaryGustav.getGadgetType(GadgetEnum.COCKTAIL).isEmpty(),
                    "Character should not hold cocktail gadget anymore.");
            // assure heal/damage
            final int cocktailHp = tale.getConfiguration().getMatchconfig().getCocktailHp();
            if (cocktailPoisoned) {
                Assertions.assertEquals(Math.max(preHp - cocktailHp, 1), theLegendaryGustav.getHp(),
                        "Should have taken damage because cocktail was poisoned.");
            } else {
                Assertions.assertEquals(Math.min(preHp + cocktailHp, 100), theLegendaryGustav.getHp(),
                        "Should have received hp because cocktail was not poisoned.");
            }
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, theLegendaryGustav.getAp(), "Aps should have been decremented by one.");
        } else {
            // does not hold cocktail --> error expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message because of not holding any cocktail and using self as target "
                            + "but to " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the chicken feed gadget action.
     *
     * @param target          The target
     * @param ipDiffSign      The ip diff sign for the target character
     * @param successExpected Whether the action is expected to be successful
     * @throws IOException If stuff goes wrong
     * @see #gen_chickenFeed()
     */
    @ParameterizedTest
    @MethodSource("gen_chickenFeed")
    @DisplayName("[Action] Chicken feed")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_chickenFeed(Point target, int ipDiffSign, boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER = "Tante Gertrude";
        final String TARGET_CHARACTER_OUF_OF_RANGE = "Meister Yoda";
        final String TARGET_CHARACTER_OWN_FACTION = "Saphira";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER, new Point(6, 5),
                TARGET_CHARACTER_OUF_OF_RANGE, new Point(5, 3), TARGET_CHARACTER_OWN_FACTION, new Point(5, 6));
        final Point NPC_POS = new Point(4, 5);
        final int BASE_IP = 5;
        final int TARGET_CHARACTER_IP_OFFSET = 4;
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(
                Map.of(CHARACTER, List.of(GadgetEnum.CHICKEN_FEED), TARGET_CHARACTER_OWN_FACTION, List.of()), 4,
                Map.of(TARGET_CHARACTER, List.of(), TARGET_CHARACTER_OUF_OF_RANGE, List.of()), 4);
        builder.prepareMainPhase();
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((s, point) -> guidedCharacterPlacement(builder, s, point));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        builder.assureFieldFree(NPC_POS);
        storyBoard = builder.build();
        Story tale = storyBoard.build();
        GameFieldController controller = tale.getController().getMainGamePhaseController().getGameFieldController();
        // collect fields free
        final Set<Point> fieldsFreePre = new HashSet<>();
        targetCharacters.values().forEach(
                point -> controller.getMap().getNeighboursOfSpecificField(point).ifPresent(fieldsFreePre::addAll));
        final Set<Point> fieldsFree = fieldsFreePre.stream().filter(p -> {
            final Optional<Character> oc = controller.decodeCharacterByPosition(p);
            return (oc.isEmpty()
                    || (!(targetCharacters.keySet().stream().anyMatch(name -> name.equals(oc.get().getName()))
                            || CHARACTER.equals(oc.get().getName()))));
        }).collect(Collectors.toSet());
        builder.unlock();
        builder.assureFieldsFree(fieldsFree);
        builder.lock();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // place npc
        final Optional<Character> mayNpc = controller.getNeutralFaction().stream().findAny();
        Assertions.assertTrue(mayNpc.isPresent(), "At least one npc character should be present.");
        final Character npc = mayNpc.get();
        npc.setCoordinates(NPC_POS);
        // set ips
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should be present.");
        final Character targetCharacter = mayTargetCharacter.get();
        final int IP_DIFF = ipDiffSign * TARGET_CHARACTER_IP_OFFSET;
        targetCharacter.setIp(BASE_IP + IP_DIFF);
        jamesBond.setIp(BASE_IP);
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.CHICKEN_FEED));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message because target was valid but got " + client.getMessages()
                            + ".");
            // assert correct ips
            switch (ipDiffSign) {
                case -1:
                    Assertions.assertEquals(BASE_IP + Math.abs(IP_DIFF), jamesBond.getIp(),
                            "Character's ips should have been increased by ip diff.");
                    break;
                case 0:
                    Assertions.assertEquals(BASE_IP, jamesBond.getIp(), "Character's ips should not have changed.");
                    break;
                case 1:
                    Assertions.assertEquals(Math.max(BASE_IP - Math.abs(IP_DIFF), 0), jamesBond.getIp(),
                            "Character's ips should have been reduced by ip diff.");
                    break;
                default:
                    Assertions.fail("Invalid ip diff sign here.");
            }
            // assure gadget removed from inventory
            Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.CHICKEN_FEED).isEmpty(),
                    "Gadget should have been removed from character's inventory.");
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // does not hold cocktail
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the nugget gadget action.
     *
     * @param target          The target of the action
     * @param successExpected Whether the action is expected to be successful
     * @throws IOException If stuff goes wrong
     * @see #gen_nugget()
     */
    @ParameterizedTest
    @MethodSource("gen_nugget")
    @DisplayName("[Action] Nugget")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_nugget(final Point target, boolean errorExpected, boolean successExpected) throws IOException {
        final Point NPC_POS = new Point(6, 5);
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER_OPPONENT = "The legendary Gustav";
        final String TARGET_CHARACTER_OUT_OF_RANGE = "Meister Yoda";
        final String TARGET_CHARACTER_OWN_FACTION = "Hans Peter Otto";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER_OPPONENT, new Point(5, 4),
                TARGET_CHARACTER_OUT_OF_RANGE, new Point(5, 0), TARGET_CHARACTER_OWN_FACTION, new Point(4, 5));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(
                Map.of(CHARACTER, List.of(GadgetEnum.NUGGET), TARGET_CHARACTER_OWN_FACTION, List.of()), 4,
                Map.of(TARGET_CHARACTER_OPPONENT, List.of(), TARGET_CHARACTER_OUT_OF_RANGE, List.of()), 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(7, 7), new Field(FieldStateEnum.WALL));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((n, p) -> guidedCharacterPlacement(builder, n, p));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();

        storyBoard = builder.build();
        final Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free fields
        builder.unlock();
        final Set<Point> fieldsFree = new HashSet<>();
        targetCharacters.forEach(
                (name, point) -> controller.getMap().getNeighboursOfSpecificField(point).ifPresent(fieldsFree::addAll));
        fieldsFree.removeIf(targetCharacters::containsValue);
        fieldsFree.remove(new Point(5, 7)); // cat
        fieldsFree.removeIf(p -> {
            final Optional<Character> oc = controller.decodeCharacterByPosition(p);
            return (oc.isPresent() && (oc.get().getName().equals("<cat>") || oc.get().getName().equals("James Bond")));
        });
        builder.assureFieldsFree(fieldsFree);
        // place npc
        final Optional<Character> mayNpc = controller.getNeutralFaction().stream().findAny();
        Assertions.assertTrue(mayNpc.isPresent(), "At least one npc character should be present.");
        final Character npc = mayNpc.get();
        npc.setCoordinates(NPC_POS);
        builder.lock();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // get target character

        // action
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.NUGGET));
        tale.eraseAllMemories();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (!errorExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            if (successExpected) {
                // check if target belongs to own faction
                controller.decodeCharacterByPosition(target)
                        .ifPresent(c -> Assertions.assertTrue(controller.getPlayerOneFaction().contains(c),
                                "Own faction should have contained target character."));
                final DummyClient client2 = tale.getClient("werner");
                Assertions.assertTrue(client2.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                        "Other client should have received GAME_STATUS as well but got " + client2.getMessages() + ".");
                final GameStatusMessage gsm = MessageContainer.getMessage(client2.getMessages().getFirst());
                Assertions.assertNotNull(gsm);
                Assertions.assertTrue(gsm.getOperations().get(0).getSuccessful(),
                        "Operation should have been successful.");
            } else {
                // check if enemy
                final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
                Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should have been present.");
                final Character targetCharacter = mayTargetCharacter.get();
                if (controller.getActionProcessor().getGuard().isEnemy(client.getConnection(), targetCharacter)) {
                    // check if owning nugget
                    Assertions.assertTrue(targetCharacter.getGadgetType(GadgetEnum.NUGGET).isPresent(),
                            "Target character is of opponent faction and should own NUGGET gadget.");
                }
            }
            // assure ap have been decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Expected error message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the nugget gadget action.
     *
     * @param target          The target of the action
     * @param successExpected Whether the action is expected to be successful
     * @throws IOException If stuff goes wrong
     * @see #gen_mirrorOfWilderness()
     */
    @ParameterizedTest
    @MethodSource("gen_mirrorOfWilderness")
    @DisplayName("[Action] Mirror of wilderness")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_mirrorOfWilderness(final Point target, boolean errorExpected, boolean successExpected)
            throws IOException {
        final Point NPC_POS = new Point(6, 5);
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER_OPPONENT = "The legendary Gustav";
        final String TARGET_CHARACTER_OUT_OF_RANGE = "Meister Yoda";
        final String TARGET_CHARACTER_OWN_FACTION = "Hans Peter Otto";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER_OPPONENT, new Point(5, 4),
                TARGET_CHARACTER_OUT_OF_RANGE, new Point(5, 0), TARGET_CHARACTER_OWN_FACTION, new Point(4, 5));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(
                Map.of(CHARACTER, List.of(GadgetEnum.MIRROR_OF_WILDERNESS), TARGET_CHARACTER_OWN_FACTION, List.of()), 4,
                Map.of(TARGET_CHARACTER_OPPONENT, List.of(), TARGET_CHARACTER_OUT_OF_RANGE, List.of()), 4);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(7, 7), new Field(FieldStateEnum.WALL));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((s, point) -> guidedCharacterPlacement(builder, s, point));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();

        storyBoard = builder.build();
        final Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free fields
        builder.unlock();
        final Set<Point> fieldsFree = new HashSet<>();
        targetCharacters.forEach(
                (name, point) -> controller.getMap().getNeighboursOfSpecificField(point).ifPresent(fieldsFree::addAll));
        fieldsFree.removeIf(targetCharacters::containsValue);
        fieldsFree.remove(new Point(5, 7)); // cat
        fieldsFree.removeIf(p -> {
            final Optional<Character> oc = controller.decodeCharacterByPosition(p);
            return (oc.isPresent() && (oc.get().getName().equals("<cat>") || oc.get().getName().equals("James Bond")));
        });
        builder.assureFieldsFree(fieldsFree);
        // place npc
        final Optional<Character> mayNpc = controller.getNeutralFaction().stream().findAny();
        Assertions.assertTrue(mayNpc.isPresent(), "At least one npc character should be present.");
        final Character npc = mayNpc.get();
        npc.setCoordinates(NPC_POS);
        builder.lock();
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // action
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.MIRROR_OF_WILDERNESS));
        tale.eraseAllMemories();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (!errorExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            if (successExpected) {
                // check if target belongs to own faction
                controller.decodeCharacterByPosition(target)
                        .ifPresent(c -> Assertions.assertTrue(controller.getPlayerOneFaction().contains(c),
                                "Own faction should have contained target character " + c + "."));
                final DummyClient client2 = tale.getClient("werner");
                Assertions.assertTrue(client2.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                        "Other client should have received GAME_STATUS as well but got " + client2.getMessages() + ".");
                final GameStatusMessage gsm = MessageContainer.getMessage(client2.getMessages().getFirst());
                Assertions.assertNotNull(gsm);
                Assertions.assertTrue(gsm.getOperations().get(0).getSuccessful(),
                        "Operation should have been successful.");
            }
            // assure ap have been decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Expected error message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the bowler blade gadget action.
     *
     * @param target          The target
     * @param successExpected Whether the target is expected to be valid
     * @throws IOException If stuff goes wrong
     * @see #gen_bowlerBlade()
     */
    @ParameterizedTest
    @MethodSource("gen_bowlerBlade")
    @DisplayName("[Action] Bowler blade")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_bowlerBlade(final Point target, final boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER_VALID = "The legendary Gustav";
        final String OBSTACLE_CHARACTER = "Meister Yoda";
        final String TARGET_CHARACTER_OUT_OF_RANGE = "Mister Y";
        final String TARGET_CHARACTER_NOT_IN_LOS_CHARACTER = "Hans Peter Otto";
        final String TARGET_CHARACTER_NOT_IN_LOS_WALL = "Tante Gertrude";
        final String TARGET_CHARACTER_MAGNETIC_WATCH = "Misses Y";
        final Map<String, Point> targetCharacters = Map.of(TARGET_CHARACTER_VALID, new Point(5, 3), OBSTACLE_CHARACTER,
                new Point(7, 5), TARGET_CHARACTER_OUT_OF_RANGE, new Point(15, 15),
                TARGET_CHARACTER_NOT_IN_LOS_CHARACTER, new Point(8, 5), TARGET_CHARACTER_NOT_IN_LOS_WALL,
                new Point(5, 8), TARGET_CHARACTER_MAGNETIC_WATCH, new Point(2, 2));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard);
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(Map.of(CHARACTER, List.of(GadgetEnum.BOWLER_BLADE), OBSTACLE_CHARACTER, List.of()),
                2,
                Map.of(TARGET_CHARACTER_VALID, List.of(), TARGET_CHARACTER_OUT_OF_RANGE, List.of(),
                        TARGET_CHARACTER_NOT_IN_LOS_CHARACTER, List.of(), TARGET_CHARACTER_NOT_IN_LOS_WALL, List.of(),
                        TARGET_CHARACTER_MAGNETIC_WATCH, List.of()),
                6);
        builder.prepareMainPhase();
        guidedFieldChange(builder, new Point(5, 7), new Field(FieldStateEnum.WALL));
        guidedCharacterPlacement(builder, CHARACTER, new Point(5, 5));
        targetCharacters.forEach((s, point) -> guidedCharacterPlacement(builder, s, point));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();

        storyBoard = builder.build();
        final Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // remove watch for every target character and only add to one
        targetCharacters.forEach((name, point) -> controller.decodeCharacterByPosition(point).ifPresent(c -> {
            final Optional<Gadget> mayMagneticWatch = c.getGadgetType(GadgetEnum.MAGNETIC_WATCH);
            if (name.equals(TARGET_CHARACTER_MAGNETIC_WATCH)) {
                if (mayMagneticWatch.isEmpty()) {
                    c.addGadget(Gadget.constructGadget(GadgetEnum.MAGNETIC_WATCH));
                }
            } else {
                mayMagneticWatch.ifPresent(c::removeGadget);
            }
        }));
        // free fields
        builder.unlock();
        final Set<Point> fieldsFree = new HashSet<>();
        targetCharacters.forEach(
                (name, point) -> controller.getMap().getNeighboursOfSpecificField(point).ifPresent(fieldsFree::addAll));
        fieldsFree.removeIf(targetCharacters::containsValue);
        builder.assureFieldsFree(fieldsFree);
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // get target character
        final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
        Assertions.assertTrue(mayTargetCharacter.isPresent(), "Target character should be present.");
        final Character targetCharacter = mayTargetCharacter.get();
        final int preHp = targetCharacter.getHp();
        // action
        DummyClient client = tale.getClient("gustav");
        GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.BOWLER_BLADE));
        tale.eraseAllMemories();
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            // valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            // assure one of neighbours has bowler blade gadget
            final Optional<List<Field>> mayNeighbours = controller.getMap().getNeighbourFieldsOfSpecificField(target);
            Assertions.assertTrue(mayNeighbours.isPresent(), "Target field should have neighbours.");
            boolean found = mayNeighbours.get().stream().anyMatch(neighbour -> {
                final Gadget neighbourGadget = neighbour.getGadget();
                return neighbourGadget != null && neighbourGadget.getGadget() == GadgetEnum.BOWLER_BLADE;
            });
            Assertions.assertTrue(found, "One of target's neighbours should contain bowler_blade gadget.");
            // assure damage taken for non-magnetic-watch-owners
            if (targetCharacter.getName().equals(TARGET_CHARACTER_MAGNETIC_WATCH)) {
                // should not have taken damage
                Assertions.assertEquals(preHp, targetCharacter.getHp(),
                        "Hp should not have changed because target character owns magnetic_watch gadget.");
            } else {
                // should have taken damage
                final int expectedNewHp = Math
                        .max(preHp - controller.getConfiguration().getMatchconfig().getBowlerBladeDamage(), 0);
                Assertions.assertEquals(expectedNewHp, targetCharacter.getHp(), "Target character " + targetCharacter
                        + " should now have " + expectedNewHp + " hp but had " + targetCharacter.getHp() + ".");
            }
            // assure ap have been decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        } else {
            // no valid target expected
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Expected error message but got " + client.getMessages() + ".");
        }
    }

    /**
     * Tests the wiretap with earplugs gadget action.
     *
     * @throws IOException If stuff goes wrong
     */
    @Test
    @DisplayName("[Action] Wiretap with earplugs")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_wiretapWithEarplugs() throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER = "Meister Yoda";
        final Point CHARACTER_POS = new Point(5, 5);
        final Point TARGET_CHARACTER_POS = new Point(6, 5);
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard).unlock();
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(CHARACTER, List.of(GadgetEnum.WIRETAP_WITH_EARPLUGS), TARGET_CHARACTER, List.of(),
                4, 4);
        builder.prepareMainPhase();
        guidedCharacterPlacement(builder, CHARACTER, CHARACTER_POS);
        guidedCharacterPlacement(builder, TARGET_CHARACTER, TARGET_CHARACTER_POS);
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        final Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free fields
        Set<Point> fieldsFree = new HashSet<>();
        controller.getMap().getNeighboursOfSpecificField(TARGET_CHARACTER_POS).ifPresent(fieldsFree::addAll);
        fieldsFree.remove(CHARACTER_POS);
        builder.assureFieldsFree(fieldsFree);
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        final int preAp = jamesBond.getAp();
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), TARGET_CHARACTER_POS, GadgetEnum.WIRETAP_WITH_EARPLUGS));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
        // get target
        final Optional<Character> mayMeisterYoda = controller.decodeCharacterByName(TARGET_CHARACTER);
        Assertions.assertTrue(mayMeisterYoda.isPresent(),
                "Target character " + TARGET_CHARACTER + " should be present.");
        final Character meisterYoda = mayMeisterYoda.get();
        // assure aps decremented
        Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
        // check gadget
        final Optional<Gadget> mayWiretapWithEarplugs = jamesBond.getGadgetType(GadgetEnum.WIRETAP_WITH_EARPLUGS);
        Assertions.assertTrue(mayWiretapWithEarplugs.isPresent(),
                "Character " + jamesBond + " does not own any wiretap with earplugs.");
        final WiretapWithEarplugs wiretapWithEarplugs = (WiretapWithEarplugs) mayWiretapWithEarplugs.get();
        Assertions.assertTrue(wiretapWithEarplugs.getWorking(), "Wiretap with earplugs should now be working.");
        Assertions.assertEquals(meisterYoda.getCharacterId(), wiretapWithEarplugs.getActiveOn(),
                "Wiretap with earplugs " + wiretapWithEarplugs + " should now be active on " + meisterYoda + ".");
        // add ip
        final int IP_ADD = 5;
        final int TARGET_CHARACTER_PRE_IP = meisterYoda.getIp();
        final int CHARACTER_PRE_IP = jamesBond.getIp();
        controller.getActionProcessor().getGuard().addIpToCharacter(meisterYoda, IP_ADD);
        // assure yoda got ips
        Assertions.assertEquals(TARGET_CHARACTER_PRE_IP + IP_ADD, meisterYoda.getIp(),
                "Target character " + meisterYoda + " should have received " + IP_ADD + " ips.");
        Assertions.assertEquals(CHARACTER_PRE_IP + IP_ADD, jamesBond.getIp(),
                "Character " + jamesBond + " should have received " + IP_ADD + " ips.");
        // next round --> wiretap will fail because of match config
        builder.enforceNextRound();
        // assure that gadget is no longer working
        Assertions.assertFalse(wiretapWithEarplugs.getWorking(),
                "Wiretap with earplugs should no longer be working because of being broken.");
        Assertions.assertNotNull(wiretapWithEarplugs.getActiveOn(),
                "Target should still be locked on wiretap with earplugs although broken.");
    }

    /**
     * Tests the hairdryer gadget action.
     *
     * @param target          The target
     * @param successExpected Whether the target is expected to be valid
     * @see #gen_hairdryer()
     */
    @ParameterizedTest
    @MethodSource("gen_hairdryer")
    @DisplayName("[Action] Hairdryer")
    @Tag("Action")
    @Tag("Gadget-Action")
    void test_hairdryer(final Point target, boolean successExpected) throws IOException {
        final String CHARACTER = "James Bond";
        final String TARGET_CHARACTER_CC = "Meister Yoda"; // clammy clothes have to be added
        final String TARGET_CHARACTER_CCC = "Spröder Senf";
        final String TARGET_CHARACTER_NCC = "The legendary Gustav";
        final String TARGET_CHARACTER_OOR = "Hans Peter Otto";
        final Map<String, Point> positioning = Map.of(CHARACTER, new Point(5, 5), TARGET_CHARACTER_CC, new Point(5, 4),
                TARGET_CHARACTER_CCC, new Point(6, 5), TARGET_CHARACTER_NCC, new Point(5, 6), TARGET_CHARACTER_OOR,
                new Point(3, 5));
        StoryBoard storyBoard = new StoryBoard().dontExpand();
        storyBoard.inject(Injects.MATCHCONFIG, StoryBoard.INTERNAL, "configs/gadget-action-matchconfig.match");
        storyBoard.inject(Injects.SCENARIO, StoryBoard.INTERNAL, "scenarios/giantFree.scenario");
        BackstoryBuilder builder = new BackstoryBuilder(storyBoard).unlock();
        builder.addClient("gustav", RoleEnum.PLAYER, GameRoleEnum.PLAYER_ONE);
        builder.addClient("werner", RoleEnum.PLAYER, GameRoleEnum.PLAYER_TWO);
        builder.startDraftingPhase();
        builder.setPlayerEquipments(Map.of(CHARACTER, List.of(GadgetEnum.HAIRDRYER)), 4,
                Map.of(TARGET_CHARACTER_CC, List.of(), TARGET_CHARACTER_CCC, List.of(), TARGET_CHARACTER_NCC, List.of(),
                        TARGET_CHARACTER_OOR, List.of()),
                4);
        builder.prepareMainPhase();
        // place characters
        positioning.forEach((s, point) -> guidedCharacterPlacement(builder, s, point));
        builder.injectNextRoundOrder(CHARACTER);
        builder.startMainPhase();
        storyBoard = builder.build();
        final Story tale = storyBoard.build();
        final GameFieldController controller = tale.getController().getMainGamePhaseController()
                .getGameFieldController();
        // free fields
        Set<Point> fieldsFree = new HashSet<>();
        positioning.values()
                .forEach(p -> controller.getMap().getNeighboursOfSpecificField(p).ifPresent(fieldsFree::addAll));
        fieldsFree.removeAll(positioning.values());
        builder.assureFieldsFree(fieldsFree);
        // populate missing properties
        controller.decodeCharacterByName(TARGET_CHARACTER_CC)
                .ifPresent(c -> c.addProperty(PropertyEnum.CLAMMY_CLOTHES));
        // assure properties
        positioning.forEach((name, point) -> {
            final Optional<Character> oc = controller.decodeCharacterByName(name);
            Assertions.assertTrue(oc.isPresent(), "Character " + name + " should be present.");
            final Character c = oc.get();
            switch (name) {
                case TARGET_CHARACTER_CC:
                    Assertions.assertTrue(c.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES),
                            TARGET_CHARACTER_CC + " should have CLAMMY_CLOTHES property.");
                    break;
                case TARGET_CHARACTER_CCC:
                    Assertions.assertTrue(c.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES),
                            TARGET_CHARACTER_CCC + " should have CONSTANT_CLAMMY_CLOTHES property.");
                    break;
                case TARGET_CHARACTER_NCC:
                    Assertions.assertFalse(c.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES),
                            TARGET_CHARACTER_NCC + " should not have CLAMMY_CLOTHES property.");
                    Assertions.assertFalse(c.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES),
                            TARGET_CHARACTER_NCC + " should not have CONSTANT_CLAMMY_CLOTHES property.");
                    break;
            }
        });
        // get character
        final Optional<Character> mayJamesBond = controller.decodeCharacterByName(CHARACTER);
        Assertions.assertTrue(mayJamesBond.isPresent(), "Character " + CHARACTER + " should be present.");
        final Character jamesBond = mayJamesBond.get();
        Assertions.assertTrue(jamesBond.getGadgetType(GadgetEnum.HAIRDRYER).isPresent(),
                "Character " + jamesBond + " should own hairdryer gadget.");
        final int preAp = jamesBond.getAp();
        // get target
        final Optional<Character> mayDryCharacter = controller.decodeCharacterByPosition(target);
        Assertions.assertTrue(mayDryCharacter.isPresent(), "Target character should be present.");
        final Character dryCharacter = mayDryCharacter.get();
        // action
        tale.eraseAllMemories();
        final DummyClient client = tale.getClient("gustav");
        final GameOperationMessage msg = new GameOperationMessage(client.getClientId(),
                new GadgetAction(jamesBond.getCharacterId(), target, GadgetEnum.HAIRDRYER));
        client.send(msg.toJson());
        client.assureMessages(1, TIMEOUT_MESSAGES);
        if (successExpected) {
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.GAME_STATUS),
                    "Should have received GAME_STATUS message but got " + client.getMessages() + ".");
            Assertions.assertFalse(dryCharacter.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES),
                    "Target character should now have the CLAMMY_CLOTHES property anymore.");
            // assure aps decremented
            Assertions.assertEquals(preAp - 1, jamesBond.getAp(), "Aps should have been decremented by one.");
            // check gadget
            final Optional<Gadget> mayHairdryer = jamesBond.getGadgetType(GadgetEnum.HAIRDRYER);
            Assertions.assertTrue(mayHairdryer.isPresent(),
                    "Character " + jamesBond + " does not own hairdryer gadget, alg.");
            final Gadget hairdryer = mayHairdryer.get();
            Assertions.assertTrue(hairdryer.hasUsagesLeft(), "Hairdryer should still have unlimited usages left.");
        } else {
            // no valid target
            Assertions.assertTrue(client.getMessages().containsType(MessageTypeEnum.ERROR),
                    "Should have received ERROR message but got " + client.getMessages() + ".");
        }
    }

    /**
     * This will place the given character on the given position with assurance that
     * the position is free.
     *
     * @param builder The BackstoryBuilder
     * @param name    The character's name
     * @param pos     The target position
     */
    private void guidedCharacterPlacement(BackstoryBuilder builder, String name, Point pos) {
        builder.assureFieldsFree(pos);
        builder.placeCharacter(name, pos);
    }

    /**
     * This will replace the field on the given position with assurance that the
     * position is free.
     *
     * @param builder  The BackstoryBuilder
     * @param pos      The target position
     * @param newField The new field
     */
    private void guidedFieldChange(BackstoryBuilder builder, Point pos, Field newField) {
        builder.assureFieldsFree(pos);
        builder.changeField(pos, newField);
    }

}