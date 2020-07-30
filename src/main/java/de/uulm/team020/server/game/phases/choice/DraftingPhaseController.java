package de.uulm.team020.server.game.phases.choice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.EquipmentChoiceMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.RequestEquipmentChoiceMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.server.addons.strikes.StrikeController;
import de.uulm.team020.server.addons.timer.TimeoutController;
import de.uulm.team020.server.configuration.ConfigurationException;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.AbstractPhaseController;
import de.uulm.team020.validation.GameDataGson;

/**
 * This one will take on the logic and control the handlers when the choice
 * phase has started. It won't be called outside of this phase as it is ensured
 * by the {@link NttsController}.
 * 
 * @author Florian Sihler
 * @version 1.1, 06/13/2020
 */
public class DraftingPhaseController extends AbstractPhaseController {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Drafting");

    private static final String RESUME_TXT = "Resume";

    private List<UUID> characterPool;
    private List<GadgetEnum> gadgetPool;

    private TimeoutController choiceTimeout;

    private DraftingProposal playerOneProposal = null;
    private DraftingProposal playerTwoProposal = null;

    private DraftingChoice playerOneChoice = new DraftingChoice("Player 1");
    private DraftingChoice playerTwoChoice = new DraftingChoice("Player 2");

    // To be assigned with equipment
    private DraftingEquipment playerOneEquipment = null;
    private DraftingEquipment playerTwoEquipment = null;

    // our controller <3
    private final NttsController controller;

    // inBuffer-Collection marker
    private boolean receivedInBufferPlayerOne = false;
    private boolean receivedInBufferPlayerTwo = false;

    /**
     * Used to enforce the next proposal, mainly used for testing, or if
     * reproducible matches are desired-
     */
    private Map<String, Queue<DraftingProposal>> enforcedProposals = new HashMap<>();

    /**
     * Construct a new DraftingPhaseController, handling logic in the drafting
     * phase.
     * <p>
     * The main controller
     * <p>
     * 
     * @param controller       The controller to use
     * @param strikeController The strike controller to use
     * @param service          The service this controller and it's
     *                         timeout(triggers) shall run in
     */
    public DraftingPhaseController(final NttsController controller, final StrikeController strikeController,
            final ExecutorService service) {

        super(controller.getConfiguration(), controller.getMessageSender(), controller.getClientManager(),
                controller.getAuthor(), strikeController);
        this.controller = controller;
        this.choiceTimeout = new TimeoutController(configuration, service);

    }

    /**
     * Start this phase
     */
    @Override
    public void startPhase() {
        startPhase(true);
    }

    /**
     * Start this phase
     * 
     * @param sendProposals Probably always true when you wanna use it
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} as a drafting guard
     */
    public void startPhase(boolean sendProposals) {
        active = true;
        configuration.shiftNextPhase();

        clientManager.getRecordKeeper().gameStarted();

        initPools();
        if (sendProposals) {
            sendItemProposal(clientManager.getPlayerOne());
            sendItemProposal(clientManager.getPlayerTwo());
        }
    }

    /**
     * Initializes the pools to generate the data from
     */
    private void initPools() {
        // no character's given, maybe add others or find more elegant solution
        CharacterInformation[] characterInformations = Objects.requireNonNull(configuration.getCharacters(),
                "There are no character informations located in the Configuration, please load them.");
        Stream<UUID> characterIds = Arrays.stream(characterInformations).map(CharacterInformation::getId);

        // Here we will have to remove random picked characters, maybe with turtle?
        characterPool = characterIds.collect(Collectors.toCollection(LinkedList::new));

        List<GadgetEnum> forbiddenGadgets = configuration.getServerConfig().forbiddenGadgets();
        // Gadgets will always be identified by their type, therefore it's not possible
        // to hold duplicates
        // No one is allowed to pick the gadget
        Stream<GadgetEnum> possibleGadgets = List.of(GadgetEnum.values()).stream().filter(
                g -> g != GadgetEnum.COCKTAIL && g != GadgetEnum.DIAMOND_COLLAR && !forbiddenGadgets.contains(g));
        gadgetPool = possibleGadgets.collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Sets the state to paused so that no further choices will be handled - this
     * affects both players
     * 
     * @return Value determined by {@link TimeoutController#pauseAllRunning()}
     */
    @Override
    public boolean pause() {
        active = false;
        return this.choiceTimeout.pauseAllRunning();
    }

    /**
     * Sets the state to resumed so that further choices will be handled - this
     * affects both players
     * 
     * @return Value determined by {@link TimeoutController#resumeAllRunning()}
     */
    @Override
    public boolean resume() {
        return resume(null);
    }

    /**
     * Sets the state to resumed so that further choices will be handled - this
     * affects both players. Call this variant if there was a reconnect and the
     * resending has to be enforced.
     * 
     * @param connection The connection that crashed
     * 
     * @return Value determined by {@link TimeoutController#resumeAllRunning()}
     */
    public boolean resume(NttsClientConnection connection) {
        active = true;
        if (configuration.getGamePhase() != GamePhaseEnum.ITEM_ASSIGNMENT) {
            throw new ThisShouldNotHappenException(
                    "Drafting phase controller was resumed outside of the drafting phase :D");
        }
        resumeForPlayerOne(connection);

        resumeForPlayerTwo(connection);

        // clear buffers
        receivedInBufferPlayerOne = receivedInBufferPlayerTwo = false;
        magpie.writeDebug("Cleared all player buffer-markers", RESUME_TXT);

        if (playerOneEquipment != null && playerTwoEquipment != null) {
            magpie.writeInfo("DraftingPhaseController triggers late shift to main which was postponed due to a break",
                    "DraftingPhase");
            controller.shiftToMainIfReady();
        }

        return this.choiceTimeout.resumeAllRunning();
    }

    private void resumeForPlayerTwo(NttsClientConnection connection) {
        if (playerTwoChoice.itemsLeft() > 0) {
            // player one did not complete drafting => resend last proposal if he had one
            if (!sendItemProposal(clientManager.getPlayerTwo())
                    && Objects.equals(connection, clientManager.getPlayerTwo())) {
                // he has already a proposal, so we will resend it
                sendItemProposal(clientManager.getPlayerTwo(), playerTwoProposal);
            }
        } else if (Objects.equals(connection, clientManager.getPlayerTwo()) && playerTwoEquipment == null) { // none set
            // resend
            sendEquipmentRequest(connection, playerTwoChoice);
        } else if (receivedInBufferPlayerTwo) {
            magpie.writeDebug("Sending equipment-message for player two as choice was received on pause-delay",
                    RESUME_TXT);
            sendEquipmentRequest(clientManager.getPlayerTwo(), playerTwoChoice);
        }
    }

    private void resumeForPlayerOne(NttsClientConnection connection) {
        if (playerOneChoice.itemsLeft() > 0) {
            // player one did not complete drafting => resend last proposal if he had one
            if (!sendItemProposal(clientManager.getPlayerOne())
                    && Objects.equals(connection, clientManager.getPlayerOne())) {
                // he has already a proposal, so we will resend it if connection is p1
                sendItemProposal(clientManager.getPlayerOne(), playerOneProposal);
            }
        } else if (Objects.equals(connection, clientManager.getPlayerOne()) && playerOneEquipment == null) { // none set
            // resend
            sendEquipmentRequest(connection, playerOneChoice);
        } else if (receivedInBufferPlayerOne) {
            magpie.writeDebug("Sending equipment-message for player one as choice was received on pause-delay",
                    RESUME_TXT);
            sendEquipmentRequest(clientManager.getPlayerOne(), playerOneChoice);
        }
    }

    /**
     * @return Random character, picked from the pool
     */
    private UUID pickRandomCharacter() {
        if (!active)
            return null;// just to be sure, this sucks for testing :D
        if (this.characterPool.isEmpty()) {
            throw new ConfigurationException("There is no character left in the pool to choose from.");
        }
        synchronized (this.characterPool) {
            int idx = ThreadLocalRandom.current().nextInt(this.characterPool.size());
            UUID character = this.characterPool.get(idx);
            // remove the Id if there should be no other in the pool
            this.characterPool.remove(idx);
            return character;
        }
    }

    /**
     * @return Random gadget, picked from the pool
     */
    private GadgetEnum pickRandomGadget() {
        if (this.gadgetPool.isEmpty()) {
            throw new ConfigurationException("There is no gadget left in the pool to choose from.");
        }
        synchronized (this.gadgetPool) {
            int idx = ThreadLocalRandom.current().nextInt(this.gadgetPool.size());
            return this.gadgetPool.remove(idx);
        }
    }

    /**
     * @param target The target client the proposal should be generated for
     * 
     * @return Another generated proposal. If necessary, this will remove the items
     *         from the pool, unpicked items have to be 're-added' manually
     */
    private DraftingProposal generateNextProposal(NttsClientConnection target) {
        DraftingProposal nextProposal;
        final String targetName = target.getClientName();
        DraftingChoice choice = assertCorrectChoice(target);

        // check if there is an enforcement for this key, which has one in queue
        if (targetName != null && enforcedProposals.containsKey(targetName)
                && !enforcedProposals.get(targetName).isEmpty()) {
            nextProposal = enforcedProposals.get(targetName).remove(); // if there is one 'pop it'.
            if (choice.charactersLeft() <= 0) { // no characters? pop em!
                nextProposal.setCharacters(Collections.emptyList());
            }
            if (choice.gadgetsLeft() <= 0) { // no gadgets? pop em!
                nextProposal.setGadgets(Collections.emptyList());
            }
            // remove the gadgets & characters from the pool
            nextProposal.getGadgets().forEach(gadgetPool::remove);
            nextProposal.getCharacters().forEach(characterPool::remove);
            return nextProposal;
        }
        nextProposal = new DraftingProposal();

        // choose three characters at random (maybe assure difference if possible? and
        // not to remove)
        if (choice.charactersLeft() > 0) {
            for (int i = 0; i < 3; i++) {
                nextProposal.addCharacter(pickRandomCharacter());
            }
        }
        // choose three gadgets at random
        if (choice.gadgetsLeft() > 0) {
            for (int i = 0; i < 3; i++) {
                nextProposal.addGadget(pickRandomGadget());
            }
        }
        return nextProposal;
    }

    private void clearProposal(GameRoleEnum gameRole) {
        if (gameRole == GameRoleEnum.PLAYER_ONE) {
            playerOneProposal = null;
        } else if (gameRole == GameRoleEnum.PLAYER_TWO) {
            playerTwoProposal = null;
        }
    }

    private synchronized void sendItemProposal(final NttsClientConnection target, final DraftingProposal proposal) {
        // Assign timeout, as the drafting-proposal is linked to it we will not perform
        // another validation here
        choiceTimeout.cancelTimeout(target); // ensure the old one doesn't trigger if there was one
        choiceTimeout.schedulePlayerTurn(target, () -> this.handleNoChoiceFromPlayer(target));

        RequestItemChoiceMessage requestItemChoiceMessage = new RequestItemChoiceMessage(target.getClientId(),
                proposal.getCharacters(), proposal.getGadgets());

        author.itemProposal(target.getClientName(), proposal);

        messageSender.sendMessage(target.getConnection(), requestItemChoiceMessage);
    }

    private void handleNoChoiceFromPlayer(final NttsClientConnection target) {
        if (!active) {// just to be safe guarding
            return;
        }

        magpie.writeWarning("No choice from: " + target.getClientName(), "Timeout");
        messageSender.handleReport(target, strikeController.strike(target, "No item-choice from the client."));

        if (Objects.isNull(target.getGameRole())) { // was removed by strike
            return;
        }

        // "choose random"
        DraftingChoice choice = assertCorrectChoice(target);
        try {
            UUID character = null;
            GadgetEnum gadget = null;
            DraftingProposal proposal = assertCorrectProposal(target);
            if (choice.charactersLeft() > 0 && choice.gadgetsLeft() > 0) { // both possible
                if (RandomHelper.flip()) { // choose character
                    character = RandomHelper.rndPick(proposal.getCharacters());
                } else { // choose gadget
                    gadget = RandomHelper.rndPick(proposal.getGadgets());
                }
            } else if (choice.charactersLeft() > 0) {
                character = RandomHelper.rndPick(proposal.getCharacters());
            } else if (choice.gadgetsLeft() > 0) {
                gadget = RandomHelper.rndPick(proposal.getGadgets());
            } else {
                throw new ThisShouldNotHappenException("Player has already picked enough items? " + choice);
            }
            author.item(target.getClientName(), character, gadget);
            appendChoice(proposal, choice, character, gadget);

        } catch (HandlerException | DraftingChoiceException ex) {
            throw new ThisShouldNotHappenException(ex.getMessage());
        }

        // clear up
        clearProposal(target.getGameRole());

        // check active and if resend
        if (active && choice.itemsLeft() > 0) {
            sendItemProposal(target);
        }
    }

    private void appendChoice(final DraftingProposal proposal, final DraftingChoice playerChose,
            final UUID chosenCharacter, final GadgetEnum chosenGadget)
            throws IllegalMessageException, DraftingChoiceException {
        if (chosenCharacter != null) {
            // a character was chosen
            if (!proposal.getCharacters().contains(chosenCharacter)) {
                throw new IllegalMessageException("The Character you desired: " + chosenCharacter
                        + " was no part of your pool: " + proposal.getCharacters());
            }
            playerChose.addCharacter(chosenCharacter);
            // re-add the ones not picked, as they have been removed for the time
            proposal.getCharacters().stream().filter(x -> !x.equals(chosenCharacter)).forEach(characterPool::add);
            // Add all gadgets too, as none was chosen
            gadgetPool.addAll(proposal.getGadgets());

        } else if (chosenGadget != null) {
            // Has to contain a gadget
            if (!proposal.getGadgets().contains(chosenGadget)) {
                throw new IllegalMessageException("The Gadget you desired: " + chosenGadget
                        + " was no part of your pool: " + proposal.getGadgets());
            }
            playerChose.addGadget(chosenGadget);

            // re-add all as none has been picked
            characterPool.addAll(proposal.getCharacters());
            // re-add the gadgets that he did not pick
            proposal.getGadgets().stream().filter(x -> !x.equals(chosenGadget)).forEach(gadgetPool::add);
        }
    }

    private void handleNoEquipmentFromPlayer(final NttsClientConnection target) {
        if (!active) {// just to be safe guarding
            return;
        }
        magpie.writeWarning("No equipment-message from: " + target.getClientName(), "Timeout");
        messageSender.handleReport(target, strikeController.strike(target, "No equipment-choice from the client."));
        // Todo: DON'T CONTINUE IF END?
        DraftingChoice choice = assertCorrectChoice(target);
        DraftingEquipment mapChoice = new DraftingEquipment();

        for (UUID character : choice.getCharacters()) {
            mapChoice.put(character, new LinkedList<>());
        }

        // Till now: similar to the story-creation
        for (GadgetEnum gadget : choice.getGadgets()) {
            int idx = ThreadLocalRandom.current().nextInt(choice.getCharacters().size());
            mapChoice.get(choice.getCharacters().get(idx)).add(gadget);
        }

        setEquipment(target, mapChoice);

    }

    /**
     * Send an item proposal to the targetPlayer if there currently is none
     * 
     * @param target The target to propose the request to
     * @return True if the request was issued, false otherwise
     */
    protected boolean sendItemProposal(NttsClientConnection target) {
        GameRoleEnum targetRole = target.getGameRole();
        if (targetRole == GameRoleEnum.PLAYER_ONE) {
            if (playerOneProposal != null) {
                return false; // there is already a current proposal
            }

            playerOneProposal = generateNextProposal(target);
            sendItemProposal(target, playerOneProposal);
            return true;
        } else if (targetRole == GameRoleEnum.PLAYER_TWO) {
            if (playerTwoProposal != null) {
                return false; // there is already a current proposal
            }

            playerTwoProposal = generateNextProposal(target);
            sendItemProposal(target, playerTwoProposal);
            return true;
        } else {
            magpie.writeWarning("The target of a proposal request was no player. Target: " + target, "Proposal");
            // throw new ThisShouldNotHappenException("Target is no player (" + target +
            // ")")
            // maybe kicked?
            return false;
        }
    }

    /**
     * Ensures the correct proposal
     * 
     * @param connection the connection to check
     * @return the proposal
     * 
     * @throws HandlerException If there is an error that should be handled by the
     *                          upper layer
     */
    public DraftingProposal assertCorrectProposal(NttsClientConnection connection) throws HandlerException {
        GameRoleEnum gameRole = connection.getGameRole();
        if (gameRole == null || !gameRole.isPlayer())
            throw new IllegalMessageException("This client is not registered as Player, and cannot send request");
        if (gameRole == GameRoleEnum.PLAYER_ONE) {
            return playerOneProposal;
        } else if (gameRole == GameRoleEnum.PLAYER_TWO) {
            return playerTwoProposal;
        } else {
            throw new ThisShouldNotHappenException("Only player one and player two should be players.");
        }
    }

    public DraftingChoice assertCorrectChoice(final NttsClientConnection connection) {
        GameRoleEnum gameRole = connection.getGameRole();
        if (gameRole == GameRoleEnum.PLAYER_ONE) {
            return playerOneChoice;
        } else if (gameRole == GameRoleEnum.PLAYER_TWO) {
            return playerTwoChoice;
        } else {
            throw new ThisShouldNotHappenException("The client '" + connection.getClientName()
                    + "' is no player, and cannot send an item-choice request");
        }
    }

    /**
     * Will process a proposal request and perform any necessary re-orderings (using
     * funny side-effects).
     * 
     * @param proposal          the proposal the character god
     * @param playerChose       the current choice of the player, which will be
     *                          appended if legal
     * @param itemChoiceMessage the choice the player has made
     * 
     * @return True if there is another proposal to be made, false if the player
     *         completed his proposal and is to switch to the equipment-phase.
     * @throws HandlerException To be thrown if the client violated the rules
     */
    public boolean processRequest(DraftingProposal proposal, DraftingChoice playerChose,
            ItemChoiceMessage itemChoiceMessage) throws HandlerException {
        UUID chosenCharacter = itemChoiceMessage.getChosenCharacterId();
        GadgetEnum chosenGadget = itemChoiceMessage.getChosenGadget();

        if (!itemChoiceMessage.isValid()) {
            throw new IllegalMessageException("You have to choose exactly one of gadget/character. But you set: c: "
                    + chosenCharacter + ", g: " + chosenGadget);
        }

        // try to assign the choice
        try {
            appendChoice(proposal, playerChose, chosenCharacter, chosenGadget);
        } catch (DraftingChoiceException ex) {
            throw new IllegalMessageException(ex.getMessage());
        }
        // Is there a choice left to be done?
        return playerChose.itemsLeft() > 0;
    }

    // the mighty item-choice handler, we will embed both handlers, as there will be
    // only two
    @SuppressWarnings("java:S1172")
    public HandlerReport handleItemChoice(NttsController controller, WebSocket conn, String message)
            throws HandlerException {
        NttsClientConnection connection = assertNttsClientConnection(conn);

        GameRoleEnum gameRole = connection.getGameRole();

        // First: check if there is an request already
        DraftingProposal proposal = assertCorrectProposal(connection);

        if (proposal == null)
            throw new IllegalMessageException(
                    "There is no drafting proposal for you, that you could have chosen from.");

        DraftingChoice playerChose = assertCorrectChoice(connection);

        ItemChoiceMessage itemChoiceMessage = GameDataGson.fromJson(message, ItemChoiceMessage.class);
        author.item(connection.getClientName(), itemChoiceMessage.getChosenCharacterId(),
                itemChoiceMessage.getChosenGadget());

        boolean nextItemRequest = processRequest(proposal, playerChose, itemChoiceMessage);

        clearProposal(gameRole);
        strikeController.reset(connection);

        // new events and rechecking will be done when resumed
        if (!active) {
            // just buffer the request by erasing the old proposal
            switch (gameRole) {
                case PLAYER_ONE:
                    receivedInBufferPlayerOne = true;
                    magpie.writeDebug("Acknowledged buffer of p1", "Buffer");
                    break;
                case PLAYER_TWO:
                    receivedInBufferPlayerTwo = true;
                    magpie.writeDebug("Acknowledged buffer of p2", "Buffer");
                    break;
                default:
                case SPECTATOR: // we do not wanna be here, but still
                    break;
            }
        } else if (nextItemRequest) {
            // send the next request
            sendItemProposal(connection);
        } else {
            // send equipment request
            magpie.writeInfo(
                    "The client " + connection.getClientName() + " completed choosing items and will have to equip now",
                    "Drafting");
            sendEquipmentRequest(connection, playerChose);
        }

        return null;
    }

    /**
     * Send an equipment request to the targetPlayer if there currently is none
     * 
     * @param target   The target to propose the request to
     * @param proposal The proposal the client gets
     */
    protected void sendEquipmentRequest(NttsClientConnection target, DraftingChoice proposal) {
        // Assign timeout, as the drafting-proposal is linked to it we will not perform
        // another validation here
        choiceTimeout.cancelTimeout(target); // ensure the old one doesn't trigger if there was one
        choiceTimeout.schedulePlayerTurn(target, () -> this.handleNoEquipmentFromPlayer(target)); // todo: change
                                                                                                  // handler

        RequestEquipmentChoiceMessage requestEquipmentChoiceMessage = new RequestEquipmentChoiceMessage(
                target.getClientId(), proposal.getCharacters(), proposal.getGadgets());

        messageSender.sendMessage(target.getConnection(), requestEquipmentChoiceMessage);
    }

    // the mighty equipment-choice handler, we will embed both handlers, as there
    // will be only two
    public HandlerReport handleEquipmentChoice(NttsController controller, WebSocket conn, String message)
            throws HandlerException {
        NttsClientConnection connection = assertNttsClientConnection(conn);

        GameRoleEnum gameRole = connection.getGameRole();

        if (!gameRole.isPlayer()) {
            throw new IllegalMessageException("Only players are allowed to send their equipment");
        }

        choiceTimeout.cancelTimeout(connection);
        strikeController.reset(connection);

        DraftingChoice playerChose = assertCorrectChoice(connection);

        if (playerChose.itemsLeft() > 0) {
            throw new IllegalMessageException(
                    "You have " + playerChose.itemsLeft() + " items left to choose. You can not equip them now.");
        }

        // check if equipment was valid...

        // first: check if characters match with picked ones
        final EquipmentChoiceMessage equipmentChoiceMessage = GameDataGson.fromJson(message,
                EquipmentChoiceMessage.class);
        final DraftingEquipment equipment = new DraftingEquipment(equipmentChoiceMessage.getEquipment());
        final List<UUID> characters = playerChose.getCharacters();
        final List<GadgetEnum> gadgets = playerChose.getGadgets();
        List<GadgetEnum> gotGadgets = new ArrayList<>(6);
        if (equipment.size() != characters.size()) {
            throw new IllegalMessageException("You have to specify equipment for every character. Expected: "
                    + characters + ". Got: " + equipment);
        }
        // Assert, that the Map contains all Keys
        for (Entry<UUID, List<GadgetEnum>> entry : equipment.entrySet()) {
            final UUID uuid = entry.getKey();
            if (!characters.contains(uuid)) {
                throw new IllegalMessageException(
                        "You were not allow to equip character: " + uuid + ". But you did...");
            }
            gotGadgets.addAll(entry.getValue());
        }

        if (gadgets.size() != gotGadgets.size()) {
            throw new IllegalMessageException(
                    "You had the following Gadgets to choose: " + gadgets + ". But you chose:" + gotGadgets);
        }

        for (GadgetEnum gadgetEnum : gotGadgets) {
            if (!gadgets.contains(gadgetEnum)) {
                throw new IllegalMessageException("You had the following Gadgets to choose: " + gadgets
                        + ". The Gadget: " + gadgetEnum + " was not one of them.");
            }
        }

        magpie.writeInfo("The client: " + connection.getClientName() + " just got the equipment: " + equipment + ".",
                "Equip");

        setEquipment(connection, equipment);
        return new HandlerReport().setAfterFunctionCall(controller::shiftToMainIfReady);
    }

    private void setEquipment(NttsClientConnection connection, DraftingEquipment equipment) {
        if (connection.getGameRole() == GameRoleEnum.PLAYER_ONE) {
            this.playerOneEquipment = equipment;
        } else if (connection.getGameRole() == GameRoleEnum.PLAYER_TWO) {
            this.playerTwoEquipment = equipment;
        } else {
            throw new ThisShouldNotHappenException("There should be no non-player at this point.");
        }
        author.equipment(connection.getClientName(), equipment);
    }

    /**
     * This method should only be called for testing - otherwise it could be
     * considered cheating. Please note, that to ensure the impracticality in a
     * 'real' game, this will not perform pool-removal operations so it's up to the
     * enforce to ensure the prohibition of duplicates
     * 
     * @param next   The next proposal to be issued
     * @param target Name of the target client
     * 
     * @StoryMethod Used by the {@link StoryBoard} to set the next proposal for a
     *              target.
     * 
     * @see #generateNextProposal(NttsClientConnection)
     */
    @StoryMethod
    public void enforceNextProposal(DraftingProposal next, String target) {
        magpie.writeInfo("Enforced next Proposal! [" + next + "] for a client named  " + target, "Drafting");
        if (!this.enforcedProposals.containsKey(target)) {
            // add as new
            Queue<DraftingProposal> proposals = new LinkedList<>();
            proposals.add(next);
            this.enforcedProposals.put(target, proposals);
        } else {
            // enqueue
            this.enforcedProposals.get(target).add(next);
        }
    }

    public DraftingChoice getPlayerOneChoice() {
        return this.playerOneChoice;
    }

    public DraftingChoice getPlayerTwoChoice() {
        return this.playerTwoChoice;
    }

    public DraftingEquipment getPlayerOneEquipment() {
        return this.playerOneEquipment;
    }

    public DraftingEquipment getPlayerTwoEquipment() {
        return this.playerTwoEquipment;
    }

    /**
     * Inject equipment for player one
     * 
     * @param inject The equipment to use
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to set the equipment for
     *              player one.
     * 
     */
    @StoryMethod
    public void injectPlayerOneEquipment(DraftingEquipment inject) {
        magpie.writeWarning("Injected player one equipment to: " + inject, "Inject");
        this.playerOneEquipment = inject;
    }

    /**
     * Inject equipment for player two
     * 
     * @param inject The equipment to use
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to set the equipment for
     *              player two.
     * 
     */
    @StoryMethod
    public void injectPlayerTwoEquipment(DraftingEquipment inject) {
        magpie.writeWarning("Injected player two equipment to: " + inject, "Inject");
        this.playerTwoEquipment = inject;
    }

    public List<UUID> getCharacterPool() {
        return this.characterPool;
    }

    public List<GadgetEnum> getGadgetPool() {
        return this.gadgetPool;
    }

    /**
     * Called to end the phase
     */
    @Override
    public void endPhase() {
        this.active = false;
        this.choiceTimeout.stop();
    }

}