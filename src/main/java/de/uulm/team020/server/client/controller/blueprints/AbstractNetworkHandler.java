package de.uulm.team020.server.client.controller.blueprints;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.EquipmentChoiceMessage;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameLeftMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.GameStartedMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.RequestEquipmentChoiceMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;

/**
 * This class shall be used to 'blow meaning' into the networking of the
 * server-client. It has a bunch of methods that you shall implemented to
 * provide meaning to the messages. It is adapted from the class i have written
 * for the ai.
 *
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public abstract class AbstractNetworkHandler {

    protected static final IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    protected ServerClientController controller;

    protected ConsoleWriter writer = ConsoleWriter.getWriter();

    /**
     * Current game phase
     */
    protected GamePhaseEnum currentPhase;

    /**
     * Temporary storage for game phase before pausing
     */
    protected GamePhaseEnum tempPhaseBuffer;

    private int itemChoiceCounter;

    /**
     * Construct a new NetworkHandler which provides easy trigger-access
     *
     * @param controller The controller this one is connected to
     */
    protected AbstractNetworkHandler(ServerClientController controller) {
        // currently empty (phase) shift it to the correct one
        currentPhase = GamePhaseEnum.CONNECT_TO_SERVER;
        this.controller = controller;
    }

    public ServerClientController getController() {
        return this.controller;
    }

    public GamePhaseEnum getCurrentPhase() {
        return this.currentPhase;
    }

    public final void start() throws IOException {
        currentPhase = currentPhase.getNextClient();
        controller.start();
        onStart();
    }

    /**
     * To be called when the network-handler starts working, will get called BEFORE
     * the init-rite but AFTER the network-connection was established.
     */
    protected void onStart() {
    }

    /**
     * Called on an HelloReply received
     *
     * @param reply The helloReply
     */
    public final void helloReply(HelloReplyMessage reply) {
        if (currentPhase != GamePhaseEnum.CONNECT_TO_SERVER) {
            throw new IllegalStateException(
                    "There was no helloReply expected, as we already have one. In Phase: " + currentPhase);
        }
        controller.setSessionData(reply.getClientId(), reply.getSessionId());
        controller.setScenario(reply.getLevel());
        controller.setCharacterInformation(reply.getCharacterSettings());
        onHelloReply(reply);
        currentPhase = currentPhase.getNextClient();
    }

    /**
     * Called, whenever a helloReply is received
     *
     * @param reply The helloReply
     */
    protected void onHelloReply(HelloReplyMessage reply) {
    }

    /**
     * Called on an GameStarted-Message received
     *
     * @param reply The GameStartedMessage
     */
    public final void gameStarted(GameStartedMessage reply) {
        if (currentPhase != GamePhaseEnum.WAIT_FOR_GAME_START) {
            throw new IllegalStateException("There was no gameStarted expected. We are in phase: " + currentPhase);
        }
        // TODO: set player Data
        onGameStarted(reply);
        currentPhase = currentPhase.getNextClient();
    }

    /**
     * Called, whenever a gameStarted-Message is received
     *
     * @param reply The GameStartedMessage
     */
    protected void onGameStarted(GameStartedMessage reply) {
    }

    /**
     * Called on an itemChoice-Request received
     *
     * @param request The RequestItemChoiceMessages
     */
    // TODO (for most messages): Write phase guards, then shift phases
    // TODO : Implement other needed Messages (final, with abstract onX)
    public final void requestItemChoice(RequestItemChoiceMessage request) {
        if (currentPhase == GamePhaseEnum.GAME_START) {
            currentPhase = currentPhase.getNextClient();
        }
        if (currentPhase != GamePhaseEnum.SELECT_ITEMS) {
            throw new IllegalStateException("RequestItemChoiceMessage not expected in phase:" + currentPhase);
        }
        onRequestItemChoice(request);
        itemChoiceCounter++;
        if (itemChoiceCounter == 8) {
            currentPhase = currentPhase.getNextClient();
        }
    }

    /**
     * Called when a onRequestItemChoice message is received in the correct game
     * phase
     *
     * @param request The RequestItemChoiceMessages
     */
    protected abstract void onRequestItemChoice(RequestItemChoiceMessage request);

    /**
     * Called on a gamePause received Stores current game phase on game pause
     * request before setting current state to game pause Restores game phase before
     * pause after receiving game resumption message
     *
     * @param pauseMessage The GamePauseMessage
     */
    public final void requestGamePause(GamePauseMessage pauseMessage) {
        if (!(currentPhase.isAfter(GamePhaseEnum.GAME_START) && currentPhase.isBefore(GamePhaseEnum.MAIN_GAME_END))) {
            throw new IllegalStateException("Pause cannot be requested in current phase " + currentPhase);
        } else if (currentPhase == GamePhaseEnum.GAME_PAUSED && Boolean.FALSE.equals(pauseMessage.getGamePaused())) { // Game
                                                                                                                      // resumption
                                                                                                                      // after
                                                                                                                      // pause
            currentPhase = tempPhaseBuffer;
            onRequestPause(pauseMessage);
        } else if (Boolean.TRUE.equals(pauseMessage.getGamePaused())) {
            tempPhaseBuffer = currentPhase;
            currentPhase = GamePhaseEnum.GAME_PAUSED;
            onRequestPause(pauseMessage);
        } else {
            magpie.writeError("Pause message invalid or received in wrong state", "requestGamePause");
        }
    }

    /**
     * Called, if a pause is requested in a state where pauses are allowed
     *
     * @param pauseMessage The GamePauseMessage
     */
    protected abstract void onRequestPause(GamePauseMessage pauseMessage);

    /**
     * Called on GameLeftMessage received
     *
     * @param message The GameLeftMessage
     */
    public final void gameLeft(GameLeftMessage message) {
        if (!(currentPhase.isAfter(GamePhaseEnum.GAME_START) && currentPhase.isBefore(GamePhaseEnum.MAIN_GAME_END))) {
            throw new IllegalStateException(
                    "There was no GameLeft message expected as we are in state " + currentPhase);
        }
        onGameLeft(message);
        currentPhase = GamePhaseEnum.MAIN_GAME_END;
    }

    /**
     * Called on GameLeftMessage received in the right phase
     *
     * @param message The GameLeftMessage
     */
    protected abstract void onGameLeft(GameLeftMessage message);

    /**
     * Called upon receiving RequestEquipmentChoice message
     *
     * @param message the RequestEquipmentChoiceMessage
     */
    public final void requestEquipmentChoice(RequestEquipmentChoiceMessage message) {
        if (currentPhase != GamePhaseEnum.EQUIP_ITEMS) {
            throw new IllegalStateException("RequestEquipmentMessage not expected in phase " + currentPhase);
        }
        onRequestEquipmentChoice(message);
        currentPhase = currentPhase.getNextClient();
    }

    /**
     * Called upon receiving RequestEquipmentChoice message in the correct phase
     *
     * @param message the RequestEquipmentChoiceMessage
     */
    protected abstract void onRequestEquipmentChoice(RequestEquipmentChoiceMessage message);

    /**
     * Called upon receiving GameStatus message
     *
     * @param message the GameStatus message
     */
    public final void gameStatus(GameStatusMessage message) {
        if (!(currentPhase.isAfterOrEqual(GamePhaseEnum.MAIN_GAME_READY)
                && currentPhase.isBefore(GamePhaseEnum.MAIN_GAME_END))) {
            throw new IllegalStateException("GameStatusMessage not expected in phase " + currentPhase);
        }
        onGameStatus(message);
    }

    /**
     * Called upon receiving GameStatus message in the correct phase
     *
     * @param message the GameStatus message
     */
    protected abstract void onGameStatus(GameStatusMessage message);

    /**
     * Called upon receiving MetaInformation message
     *
     * @param message the MetaInformation message
     */
    public final void metaInformation(MetaInformationMessage message) {
        onMetaInformationMessage(message);
    }

    /**
     * Called upon receiving MetaInformation message
     *
     * @param message the MetaInformation message
     */
    protected abstract void onMetaInformationMessage(MetaInformationMessage message);

    /**
     * Called on an Error-Message received
     *
     * @param message The ErrorMessage
     */
    public final void error(ErrorMessage message) {
        magpie.writeError("Received an (connection-closing) error: " + message.getReason(), "Handler");
        onError(message);
    }

    /**
     * Called, if there is an error which results in an closing connection
     *
     * @param message The ErrorMessage
     */
    protected abstract void onError(ErrorMessage message);

    /**
     * Send a item choice-message to the server, the clientId will be set correctly.
     *
     * @param chosenCharacter Set this to the chosen character, if a character was
     *                        chosen (null otherwise)
     * @param chosenGadget    Set this to the chosen gadget, if a gadget was chosen
     *                        (null otherwise)
     */
    protected void sendItemChoice(UUID chosenCharacter, GadgetEnum chosenGadget) {
        ItemChoiceMessage message = new ItemChoiceMessage(controller.getClientId(), chosenCharacter, chosenGadget);
        if (!message.isValid()) {
            throw new IllegalArgumentException("Exactly one of character and Gadget must not be null, not: character:"
                    + chosenCharacter + " and gadget: " + chosenGadget);
        }
        controller.send(message);
    }

    /**
     * Sends an equipmentChoiceMessage
     *
     * @param equipment The chosen equipment, a map of characterIds with a list
     *                  assigned items
     */
    protected void sendEquipmentChoice(Map<UUID, List<GadgetEnum>> equipment) {
        EquipmentChoiceMessage message = new EquipmentChoiceMessage(controller.getClientId(), equipment);
        controller.send(message);
    }

    /**
     * Sends metaInformationMessage
     *
     * @param keys the keys indicating the requested information
     */
    protected void sendRequestMetaInformationMessage(String[] keys) {
        RequestMetaInformationMessage message = new RequestMetaInformationMessage(controller.getClientId(), keys);
        controller.send(message);
    }

    public GamePhaseEnum getGamePhase() {
        return currentPhase;
    }

    // TODO: other send commands
}
