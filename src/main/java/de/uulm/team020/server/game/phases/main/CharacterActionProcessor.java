package de.uulm.team020.server.game.phases.main;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import de.uulm.team020.datatypes.BaseOperation;
import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.GadgetAction;
import de.uulm.team020.datatypes.GambleAction;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Movement;
import de.uulm.team020.datatypes.Operation;
import de.uulm.team020.datatypes.PropertyAction;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.NumericHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.helper.StoryLineProducer;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.main.helper.CharacterActionGuard;
import de.uulm.team020.server.game.phases.main.helper.CharacterPropertyActionProcessor;
import de.uulm.team020.server.game.phases.main.helper.gadget_action.AbstractCharacterGadgetActionProcessor;
import de.uulm.team020.server.game.phases.main.helper.gadget_action.ConcreteCharacterGadgetActionProcessor;
import de.uulm.team020.server.game.phases.main.statistics.GameFieldStatisticsProvider;
import de.uulm.team020.validation.GameDataGson;

/**
 * This class processes the movements and action of players and npc. It assumes
 * the character is allowed to the movement from a turn-perspective (character
 * was the one who was allowed to act). Actions will be performed in an
 * synchronized way. The Processor will signal the
 * {@link GameFieldStatisticsProvider} as well.
 * 
 * @author Florian Sihler
 * @version 1.1, 05/02/2020
 */
public class CharacterActionProcessor {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");
    private static final String TURN_TXT = "Turn";
    private static final String OPERATION_TXT = "operation";

    private final GameFieldController controller;
    private final Matchconfig matchconfig;
    private final ServerConfiguration serverConfig;
    private final CharacterActionGuard guard;

    // avoids +1 in safeNums to lock
    private boolean playerOneCompletedSafes;
    private boolean playerTwoCompletedSafes;
    private boolean neutralCompletedSafes;
    // Collar was found in safe
    private boolean summonedDiamondCollar;
    // Just to ease access
    private final int maxSafeNum;

    private final Set<Character> playerOneSpied; // npc spied by faction of player one
    private final Set<Character> playerTwoSpied; // npc spied by faction of player two
    private final Set<Character> neutralSpied; // npc spied by neutral faction

    // child processors
    private final AbstractCharacterGadgetActionProcessor gadgetProcessor;
    private final CharacterPropertyActionProcessor propertyProcessor;

    /**
     * Create the processor, it will operate on the given controller
     * 
     * @param gameFieldController The gameFieldController to use
     */
    public CharacterActionProcessor(final GameFieldController gameFieldController) {
        this.controller = gameFieldController;
        this.matchconfig = controller.getConfiguration().getMatchconfig();
        this.serverConfig = controller.getConfiguration().getServerConfig();
        this.guard = new CharacterActionGuard(gameFieldController);
        final int neutralSize = this.controller.getNeutralFaction().size();
        this.maxSafeNum = gameFieldController.getMaxSafeNumber();
        playerOneSpied = new HashSet<>(neutralSize);
        playerTwoSpied = new HashSet<>(neutralSize);
        neutralSpied = new HashSet<>(neutralSize); // theoretically can spy more
        gadgetProcessor = new ConcreteCharacterGadgetActionProcessor(gameFieldController);
        propertyProcessor = new CharacterPropertyActionProcessor(gameFieldController);
    }

    private static ThisShouldNotHappenException noSpyConnection(final NttsClientConnection connection) {
        return new ThisShouldNotHappenException("Non-Player connection: " + connection + " wanted to spy.");
    }

    /**
     * Process a movement operation for the given operation -- this will include the
     * decoding of the acting character. The field will be altered if and only if
     * the operation succeeds. Otherwise it will remain untouched.
     * 
     * @param connection The connection that performed the movement
     * @param operation  The operation the requested the movement
     * @param message    The base message which is used to re-parse the operation
     *                   sub-class
     * 
     * @return The operation to be used in the game-status report
     * 
     * @throws HandlerException If there is a severe error with the operation (not
     *                          allowed target field, no mp left...)
     */
    public BaseOperation processMovement(final NttsClientConnection connection, final Operation operation,
            final String message) throws HandlerException {
        final Movement movement = GameDataGson.fromJson(message, OPERATION_TXT, Movement.class);

        final UUID characterId = movement.getCharacterId();
        final Point target = movement.getTarget();
        final Point from = movement.getFrom();

        try {
            // Process the operation for the gameField
            final Point realFrom = processMoveCheckGameEnd(connection, characterId, from, target);
            // if valid: increment statistic
            controller.getStatisticsProvider().addFieldsMovedOn(connection, 1);
            // return the constructed operation
            return new Movement(characterId, target, true, realFrom);
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(
                    "The move you wanted to perform failed for the following reasons: " + ex.getMessage());
        }
    }

    /**
     * Will process a movement operation for the character, if the move is possible,
     * this method will include the place-swapping if the target field is locked by
     * the character.
     * 
     * @param connection  The connection performing this operation - can be null if
     *                    neutral
     * @param characterId The character that shall be moved
     * @param moveFrom    The point the move started on
     * @param moveTarget  The (absolute) target position of the move
     * 
     * @return The start of the move, may differ from given start
     * 
     * @throws GameFieldOperationException If the character does not exist or any
     *                                     other configuration does not work - this
     *                                     will <i>also</i> be thrown if the move is
     *                                     not allowed as the target field is
     *                                     blocked, ... If the exception gets thrown
     *                                     the underlying field will not be altered
     *                                     in any way.
     */
    public Point processMoveCheckGameEnd(final NttsClientConnection connection, final UUID characterId,
            final Point moveFrom, final Point moveTarget) throws GameFieldOperationException {
        final Character character = guard.assureValidCharacter(characterId,
                "Move from: " + moveFrom + " to: " + moveTarget);
        final Point from = character.getCoordinates();
        if (!Objects.equals(from, moveFrom)) {
            magpie.writeWarning(
                    "From field for character: " + character + " differs from actual: " + character.getCoordinates(),
                    TURN_TXT); // maybe throw error?
        }
        if (processMoveRaw(connection, character, moveTarget)) {
            // game has ended
            controller.setGameOver();
        }
        return from;
    }

    /**
     * Will process a movement operation for the character, if the move is possible,
     * this method will include the place-swapping if the target field is locked by
     * the character.
     * 
     * @param connection The connection performing this operation - can be null if
     *                   neutral
     * @param character  The character that shall be moved
     * @param moveTarget The (absolute) target position of the move
     * 
     * @return true If this move ended the game (by gifting the collar probably),
     *         false otherwise
     * @throws GameFieldOperationException If the character does not exist or any
     *                                     other configuration does not work - this
     *                                     will <i>also</i> be thrown if the move is
     *                                     not allowed as the target field is
     *                                     blocked, ... If the exception gets thrown
     *                                     the underlying field will not be altered
     *                                     in any way.
     */
    public boolean processMoveRaw(final NttsClientConnection connection, final Character character,
            final Point moveTarget) throws GameFieldOperationException {
        final Point currentPoint = character.getCoordinates();

        // check if the target field is allowed, this means it is on the field and is a
        // neighbour field
        final FieldMap map = controller.getMap();
        Field target;
        try {
            // check: can do movement?
            guard.assureCanPerformMovement(character);

            // is the target-field walkable? we have already ensured, that it is on the
            // field
            target = guard.assureValidNeighbourField(moveTarget, currentPoint, map);
        } catch (final HandlerException ex) {
            throw new GameFieldOperationException(ex.getMessage());
        }
        if (!target.isWalkable()) {
            throw new GameFieldOperationException(
                    "The target field is not walkable and therefore blocked -- you cannot move on this field: "
                            + target); // plainly not working for blocking reasons
        }

        // Check if there is something on the target field
        final Optional<Character> mayCollideCharacter = controller.decodeCharacterByPosition(moveTarget);
        boolean endedByGift = false;
        if (mayCollideCharacter.isPresent()) {
            endedByGift = swapCharacterWithMovementAndCheckForCat(connection, character, currentPoint,
                    mayCollideCharacter.get(), endedByGift);
        }

        // pickup item if normal character:
        if (!guard.isCatOrJanitor(character) && target.getGadget() != null) {
            character.addGadget(target.getGadget()); // automatically registers moledie
            target.setGadget(null);
        }

        // reduce on performed
        magpie.writeDebug("Moved: " + character.getName() + " from : " + currentPoint + " to: " + moveTarget,
                "Op-Move");
        character.setMp(character.getMp() - 1);
        character.setCoordinates(moveTarget);

        return endedByGift;
    }

    private boolean swapCharacterWithMovementAndCheckForCat(final NttsClientConnection connection,
            final Character character, final Point currentPoint, final Character collideCharacter, boolean endedByGift)
            throws GameFieldOperationException {
        magpie.writeDebug(
                "There is a character (" + collideCharacter
                        + ") on the target field and therefore it will be swapped with your pick: " + character,
                "Op-Move");
        if (collideCharacter.equals(controller.getCat())) {
            controller.getStatisticsProvider().steppedOnCat(connection);
            // game over if having diamond collar!
            if (character.getGadgetType(GadgetEnum.DIAMOND_COLLAR).isPresent()) {
                controller.getStatisticsProvider().giftedDiamondCollar(connection);
                guard.addIpToCharacter(character, matchconfig.getCatIp());
                endedByGift = true;
            }
        } else if (collideCharacter.equals(controller.getJanitor()) && !serverConfig.swapIfMoveOnJanitor()) {
            // we can configure that this is forbidden
            throw new GameFieldOperationException(
                    "The server configuration of this server forbids moves on the janitor-field. You can enable this with the server-configuration.");
        }
        collideCharacter.setCoordinates(currentPoint);
        return endedByGift;
    }

    /**
     * Process retire operation
     * 
     * @param operation The retirement requested
     * 
     * @return The operation to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public BaseOperation processRetire(final Operation operation) throws HandlerException {
        // register for author
        return processRetire(operation.getCharacterId());
    }

    /**
     * Process retire operation
     * 
     * @param characterId The character that wants to retire
     * 
     * @return The operation to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public BaseOperation processRetire(final UUID characterId) throws HandlerException {
        try {
            final Character character = guard.assureValidCharacter(characterId, "Retire");
            character.retire();
            return new Operation(OperationEnum.RETIRE, characterId, true, character.getCoordinates());
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }
    }

    /**
     * Will process the gamble operation
     * 
     * @param gambleAction The operation-message that requested the gamble
     * @param message      The base message which is used to re-parse the operation
     *                     sub-class
     * @return The operation to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public BaseOperation processGambleAction(final GambleAction gambleAction, final String message)
            throws HandlerException {

        final UUID characterId = gambleAction.getCharacterId();

        // check: character exists
        Character character;
        try {
            character = guard.assureValidCharacter(characterId, "Gamble");
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }

        // check: has ap
        final FieldMap map = controller.getMap();
        guard.assureCanPerformAction(character, map);

        final int stake = gambleAction.getStake();
        // check: character has enough chips
        if (character.getChips() < stake) {
            throw new IllegalMessageException(
                    "You do not have enough chips to set a stake of: " + stake + " with the character: " + character);
        }

        final Point target = gambleAction.getTarget();
        final Field field = guard.assureValidNeighbourField(target, character.getCoordinates(), map, "Gamble");

        // check: target field is a roulette table
        if ((field.getState() != FieldStateEnum.ROULETTE_TABLE)) {
            throw new IllegalMessageException("The target-field for the gamble-operation (" + field + ") on: " + target
                    + " is no Roulette-Table.");
        }

        // check: still working?
        if (field.isDestroyed()) {
            throw new IllegalMessageException("The roulette-table you targeted was destroyed!");
        }

        // check: can the roulette table be played on?
        final int chipsPresent = field.getChipAmount();
        if (chipsPresent <= 0) {
            throw new IllegalMessageException(
                    "The roulette-table you targeted has no chips anymore (cannot be played)!");
        }
        // more chips placed than present?
        if (chipsPresent < stake) {
            throw new IllegalMessageException("The roulette-table you targeted only has: " + chipsPresent
                    + " chips, you wanted to place: " + stake + " this is more, and therefore illegal!");
        }

        final boolean gambleResult = gamble(field, character);

        if (gambleResult) { // won the gamble
            updateGambleWin(character, stake, field);
        } else { // lost the gamble
            updateGambleLoss(character, stake, field);
        }

        // remove ap:
        character.removeAp();
        return new GambleAction(characterId, gambleResult, target, stake);
    }

    private void updateGambleLoss(final Character character, final int stake, final Field field) {
        magpie.writeDebug(character.getName() + " has lost the gamble and did loose: " + stake, TURN_TXT);
        // remove the stake
        character.setChips(character.getChips() - stake);
        // add the stake to the table
        field.setChipAmount(field.getChipAmount() + stake);
    }

    private void updateGambleWin(final Character character, final int stake, final Field field) {
        magpie.writeDebug(character.getName() + " has won the gamble and did win: " + stake, TURN_TXT);
        // double the victory:
        character.setChips(character.getChips() + stake);
        // remove the stake from the table
        field.setChipAmount(field.getChipAmount() - stake);
    }

    /**
     * Performs the actual gamble
     * 
     * @param rouletteTable The field to operate on
     * @param gambler       The gambling character
     * 
     * @return True if won, false if lost.
     */
    private boolean gamble(final Field rouletteTable, final Character gambler) {
        int chance = 18;
        if (gambler.getProperties().contains(PropertyEnum.JINX)) {
            chance = 13;
        } else if (gambler.getProperties().contains(PropertyEnum.LUCKY_DEVIL)) {
            chance = 23;
        }
        return rouletteTable.isInverted() ^ controller.getRandomController().requestFlip(gambler.getName(),
                chance / 37D, RandomOperation.GAMBLE_WIN);
    }

    /**
     * Will process the spy operation
     * 
     * @param connection The connection that requested the operation- can be null on
     *                   npc
     * @param operation  The operation-message that requested the spy
     * @param message    The base message which is used to re-parse the operation
     *                   sub-class
     * @return The operation to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public BaseOperation processSpyAction(final NttsClientConnection connection, final Operation operation,
            final String message) throws HandlerException {
        // There is no need to re-parse, as a spy action does not have children
        final UUID characterId = operation.getCharacterId();

        // check: character exists
        Character character;
        try {
            character = guard.assureValidCharacter(characterId, "Spy");
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }

        // are we able to perform the operation?
        final FieldMap map = controller.getMap();
        guard.assureCanPerformAction(character, map);

        // is it a neighbour field
        final Point target = operation.getTarget();
        final Field field = guard.assureValidField(target, map, "Spy");

        boolean worked;

        if (field.getState() == FieldStateEnum.SAFE) {
            guard.assureValidReachableField(target, character, map, "Spy");
            worked = spyOnSafe(connection, field, character);
        } else {
            // has to be neighbour
            guard.assureValidNeighbourField(target, character.getCoordinates(), map, "Spy");

            // get character on this field:
            final Optional<Character> mayTargetCharacter = controller.decodeCharacterByPosition(target);
            if (!mayTargetCharacter.isPresent()) {
                throw new IllegalMessageException(
                        "Spy action must target a character, but there is none on: " + target);
            }
            final Character targetCharacter = mayTargetCharacter.get();
            worked = spyOnCharacter(connection, character, targetCharacter);
        }

        // spy on neighbour character - if npc wins by chance otherwise and gifts secret
        // otherwise it fails w 100% -> discover
        // costs 1 ap => no fog write general guards to prevent redundancy
        // if multiple persons from same faction spy on same secret they do not re-get
        // the ip points. Furthermore: npc-secrets might contain another safe-key,
        // safes always will contain the next one.

        character.removeAp();
        return new Operation(OperationEnum.SPY_ACTION, characterId, worked, target);
    }

    /**
     * Spy into a safe - this will fail if the character has not obtained the
     * necessary safe key to open. It will work, but not re-gift the ip if the safe
     * was already opened by this faction.
     * <p>
     * Won't perform AP-checks or consumption!
     * 
     * @param connection The connection to identify the faction - can be null on npc
     * @param field      The field the spy was enforced on - shall be a safe
     * @param character  The character that wanted to do the spy
     * 
     * @return True if the operation has worked - means ip has been transferred if
     *         necessary, false otherwise
     */
    private boolean spyOnSafe(final NttsClientConnection connection, final Field field, final Character character) {
        final int safeNum = field.getSafeIndex();
        boolean seenAlready;
        Set<Integer> combinations;
        // Does the faction already know this safe index? => no regain of ip
        if (connection == null) {
            magpie.writeDebug("Spied by NPC, validation will use the npc-buffer", TURN_TXT);
            combinations = controller.getNeutralSafeCombinations();
            seenAlready = neutralCompletedSafes;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_ONE) {
            combinations = controller.getPlayerOneSafeCombinations();
            seenAlready = playerOneCompletedSafes;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_TWO) {
            combinations = controller.getPlayerTwoSafeCombinations();
            seenAlready = playerTwoCompletedSafes;
        } else {
            throw noSpyConnection(connection);
        }

        // if completed - always seen all
        seenAlready = seenAlready || combinations.contains(safeNum + 1); // contains next one or is last?
        if (seenAlready) {
            magpie.writeDebug("Safe was already discovered by the faction of: " + character.getName()
                    + " and therefore will not be re-discovered.", TURN_TXT);
            return true; // we do not care
        }

        // first: check if the character has the last safe number
        if (!combinations.contains(safeNum)) {
            magpie.writeWarning("Player does not know the safe key: " + safeNum + " with: " + combinations, TURN_TXT);
            return false;
        }

        // gift ip
        this.guard.addIpToCharacter(character, matchconfig.getSecretToIpFactor());
        // reveal next safe-number (if there is one)
        if (safeNum < maxSafeNum) {
            combinations.add(safeNum + 1);
        } else {
            playerCompletesSafe(connection, character);
        }

        return true;
    }

    private void playerCompletesSafe(final NttsClientConnection connection, final Character character) {
        if (connection == null) {
            neutralCompletedSafes = true;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_ONE) {
            playerOneCompletedSafes = true;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_TWO) {
            playerTwoCompletedSafes = true;
        }
        // may diamond collar?
        if (!summonedDiamondCollar) {
            magpie.writeInfo("The diamond-collar was presented to: " + character.getName(), TURN_TXT);
            character.addGadget(Gadget.constructGadget(GadgetEnum.DIAMOND_COLLAR));
            summonedDiamondCollar = true;
        }
    }

    /**
     * Spy another character - this will fail if no npc or by chance
     * <p>
     * Won't perform AP-checks or consumption!
     * 
     * @param connection      The connection to identify the faction - can be null
     *                        on npc
     * @param character       The character that wanted to do the spy
     * @param targetCharacter The character spied on
     * 
     * @return True if the spy worked, false otherwise
     */
    private boolean spyOnCharacter(final NttsClientConnection connection, final Character character,
            final Character targetCharacter) {
        boolean worked;
        // is it a neutral character?
        if (controller.getNeutralFaction().contains(targetCharacter)) {
            worked = spyOnNeutralCharacter(connection, character, targetCharacter);
        } else {
            // is not neutral, we don't care if this is a character of the own team
            // it fails too
            magpie.writeDebug("Spy action targeted character", TURN_TXT);
            worked = false;
        }
        return worked;
    }

    private boolean spyOnNeutralCharacter(final NttsClientConnection connection, final Character character,
            final Character targetCharacter) {
        magpie.writeDebug("Spy action targeted npc: " + targetCharacter, TURN_TXT);
        // check by chance if worked:
        final double chance = matchconfig.getSpySuccessChance();
        if (!guard.flip(character, chance)) {
            magpie.writeDebug("Spying failed", TURN_TXT);
            return false;
        }

        magpie.writeDebug("Spying worked", TURN_TXT);
        // did this faction already spy on this field?
        final boolean seenAlready;
        Set<Character> spied;
        if (connection == null) {
            magpie.writeDebug("Spied by NPC, validation will use the npc-buffer", TURN_TXT);
            spied = neutralSpied;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_ONE) {
            spied = playerOneSpied;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_TWO) {
            spied = playerTwoSpied;
        } else {
            throw noSpyConnection(connection);
        }
        seenAlready = spied.contains(targetCharacter);
        if (!seenAlready) {
            // gift ip:
            this.guard.addIpToCharacter(character, matchconfig.getSecretToIpFactor()); // discovered a secret
            spied.add(targetCharacter);
            if (controller.getRandomController().requestFlip(targetCharacter.getName(),
                    serverConfig.npcHasRightKeyOnSpyChance(), RandomOperation.NPC_HAS_RIGHT_KEY)) {
                npcSafeSecret(connection, targetCharacter);
            } else {
                magpie.writeInfo("NPC did not present right safe key on spy", TURN_TXT);
            }
        }
        return true;
    }

    /**
     * Grant a safe secret by an npc
     * 
     * @param connection The connection thats want to spy
     * @param spier      The npc that gets spied
     */
    private void npcSafeSecret(final NttsClientConnection connection, final Character spied) {
        magpie.writeInfo("NPC presented right safe key on spy", TURN_TXT);
        Set<Integer> combinations;
        if (connection == null) {
            combinations = controller.getNeutralSafeCombinations();
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_ONE) {
            combinations = controller.getPlayerOneSafeCombinations();
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_TWO) {
            combinations = controller.getPlayerTwoSafeCombinations();
        } else {
            throw noSpyConnection(connection);
        }

        final int foundAmount = NumericHelper.getInBounds(serverConfig.npcSecretMaySkip(), 1, maxSafeNum);
        int willFind = controller.getRandomController().requestRange(spied.getName(), 0, foundAmount + 1,
                RandomOperation.NPC_AMOUNT_OF_SAFE_KEYS);
        if (serverConfig.npcHasAtLeastOneKey()) {
            willFind = willFind < 1 ? 1 : willFind;
        }
        magpie.writeInfo(
                "Will discover: " + willFind + " new Safe-Keys. Has: " + combinations + " (for: " + connection + ")",
                TURN_TXT);
        for (int i = 0; i < willFind; i++) {
            if (npcSafeCodeWithSkip(combinations, i)) {
                break; // break if all safes found
            }
        }
    }

    private boolean npcSafeCodeWithSkip(final Set<Integer> combinations, final int safeNum) {
        if (combinations.isEmpty()) {
            combinations.add(1); // Add the first safe-number
        } else {
            return addNextSafeKey(combinations, safeNum);
        }

        return false;
    }

    private boolean addNextSafeKey(final Set<Integer> combinations, final int safeNum) {
        // Add the next
        final Optional<Integer> mayMax = combinations.stream().max(Integer::compare);
        if (!mayMax.isPresent()) {
            throw new ThisShouldNotHappenException("No maximum safe number on check: " + combinations);
        }
        final int max = mayMax.get(); // there is one
        if (max < maxSafeNum) { // there is another one to discover
            combinations.add(max + 1);
            magpie.writeDebug("Added safe-num: " + (max + 1), TURN_TXT);
        } else {
            magpie.writeDebug("Tossed the generated new key for: " + safeNum, TURN_TXT);
            return true;
        }
        return false; // Not all safe keys found
    }

    /**
     * Will process a gadget operation
     * 
     * @param connection The connection that requested the operation - can be null
     *                   on npc
     * @param operation  The operation-message that requested the gadget-action
     * @param message    The base message which is used to re-parse the operation
     *                   sub-class
     * @return The operations to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public List<BaseOperation> processGadgetAction(final NttsClientConnection connection, final Operation operation,
            final String message) throws HandlerException {
        // we have to re-parse the object.
        final GadgetAction gadgetAction = GameDataGson.fromJson(message, OPERATION_TXT, GadgetAction.class);
        controller.getAuthor().storyLine(StoryLineProducer.gadget(connection.getClientName(), gadgetAction.getTarget(),
                gadgetAction.getGadget()));

        final UUID characterId = gadgetAction.getCharacterId();

        // check: character exists
        Character character;
        try {
            character = guard.assureValidCharacter(characterId, "Gadget");
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }

        // character can pickup cocktail from bar table
        if (gadgetAction.getGadget() != GadgetEnum.COCKTAIL) {
            guard.assureValidGadget(gadgetAction.getGadget(), character);
        }

        final FieldMap map = controller.getMap();
        guard.assureValidField(gadgetAction.getTarget(), map, "GadgetAction");

        // check: has ap
        guard.assureCanPerformAction(character, map);

        return gadgetProcessor.processGadgetAction(connection, gadgetAction, character);
    }

    /**
     * Will process a property operation
     * 
     * @param connection The connection that requested the operation - can be null
     *                   on npc
     * @param operation  The operation-message that requested the gadget-action
     * @param message    The base message which is used to re-parse the operation
     *                   sub-class
     * @return The operations to be used in the game-status report
     * 
     * @throws HandlerException If there is anything illegal with the operation
     *                          (character does not exist, ...)
     */
    public List<BaseOperation> processPropertyAction(final NttsClientConnection connection, final Operation operation,
            final String message) throws HandlerException {
        // we have to re-parse the object.
        final PropertyAction propertyAction = GameDataGson.fromJson(message, OPERATION_TXT, PropertyAction.class);
        writePropertyActionWithAuthor(connection, propertyAction);
        final UUID characterId = propertyAction.getCharacterId();

        // check: character exists
        Character character;
        try {
            character = guard.assureValidCharacter(characterId, "Property");
        } catch (final GameFieldOperationException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }

        // check: field and property
        guard.assureValidProperty(propertyAction.getProperty(), character);

        final FieldMap map = controller.getMap();
        guard.assureValidField(propertyAction.getTarget(), map, "PropertyAction");

        // check: has ap
        guard.assureCanPerformAction(character, map);

        return propertyProcessor.processPropertyAction(connection, propertyAction, character);
    }

    private void writePropertyActionWithAuthor(final NttsClientConnection connection,
            final PropertyAction propertyAction) {
        controller.getAuthor().storyLine(StoryLineProducer.property(connection.getClientName(),
                propertyAction.getTarget(), propertyAction.getProperty()));
    }

    public AbstractCharacterGadgetActionProcessor getCharacterGadgetActionProcessor() {
        return this.gadgetProcessor;
    }

    public CharacterPropertyActionProcessor getCharacterPropertyActionProcessor() {
        return this.propertyProcessor;
    }

    public CharacterActionGuard getGuard() {
        return this.guard;
    }
}