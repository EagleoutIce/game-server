package de.uulm.team020.server.core.handlers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.PhaseComparator;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.ReplayMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.networking.messages.RequestReplayMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.Faction;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Holds all information Handlers, to handle
 * {@link RequestMetaInformationMessage} and {@link MetaInformationMessage}.
 *
 * @author Florian Sihler
 * @version 1.0, 04/02/2020
 */
public class InformationHandlers {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    /** Hide default constructor */
    private InformationHandlers() {
    }

    private static List<String> messageBuffer = new ArrayList<>(3);

    // Making the code more readable
    private static final UUID[] NO_KEY_UUID = null;
    private static final UUID[] INVALID_UUID = null;

    private static final GadgetEnum[] INVALID_GADGET = null;

    private static final Object NO_KEY = null;
    private static final Object INVALID_KEY = null;

    public static Object populateInformation(final NttsController controller, final NttsClientConnection connection,
            final String key) {
        magpie.writeDebug("Populating: " + key + " for: "
                + (connection == null ? "unknown[new]" : connection.getClientName()) + ".", "Meta");
        final MetaKeyEnum keyType = MetaKeyEnum.getMetaKey(key);
        if (keyType == null) {
            messageBuffer.add("The key '" + key + "' is unknown.");
            return INVALID_KEY;
        }

        final Configuration configuration = controller.getConfiguration();
        switch (keyType) {
            case GAME_REMAINING_PAUSE_TIME:
                if (configuration.getGamePhase().compareWith(GamePhaseEnum.GAME_PAUSED).equals(PhaseComparator.IS_EQUAL)
                        || configuration.getGamePhase().compareWith(GamePhaseEnum.GAME_FORCE_PAUSED)
                                .equals(PhaseComparator.IS_EQUAL)) {
                    messageBuffer.add("There is no pause registered on server side.");
                    return -1;
                } else {
                    return controller.getConfiguration().getTimeoutController().getShortestRemainingPauseTime();
                }
            case SPECTATOR_COUNT:
                return controller.getClientManager().getSpectatorConnections().size();
            case SPECTATOR_NAMES:
                return controller.getClientManager().getSpectatorConnections().stream()
                        .map(NttsClientConnection::getClientName).toArray(String[]::new);
            case PLAYER_COUNT:
                return controller.getClientManager().getPlayerCount();
            case PLAYER_NAMES:
                return controller.getClientManager().getPlayerNames();
            case CONFIGURATION_SCENARIO:
                return configuration.getScenario();
            case CONFIGURATION_MATCHCONFIG:
                return configuration.getMatchconfig();
            case CONFIGURATION_CHARACTER_INFORMATION:
                return configuration.getCharacters();
            case FACTION_PLAYER1:
            case FACTION_PLAYER2:
            case FACTION_NEUTRAL:
                return getFactionMembers(controller, Objects.requireNonNull(connection), keyType);
            case GADGETS_PLAYER1:
            case GADGETS_PLAYER2:
                return getGadgets(controller, Objects.requireNonNull(connection), keyType);
            case AUTHOR_DUMP:
                final StringWriter target = new StringWriter();
                try {
                    controller.getAuthor().writeStory(new PrintWriter(target), "String-Dump");
                    if (!controller.getUseDummy()) {
                        controller.getAuthor()
                                .dump("request-" + (connection == null ? "unknown[new]" : connection.getClientName()));
                    }
                } catch (final IOException ex) {
                    throw new ThisShouldNotHappenException("Author-Dump: " + ex.getMessage());
                }
                return target.toString();
            default:
                messageBuffer.add("There is no handler for this key.");
                return NO_KEY;
        }

    }

    /**
     * Ensures the correct attachment
     * 
     * @param conn the connection to check
     * @return the attachment, exception otherwise
     */
    private static NttsClientConnection assertNttsClientConnection(final WebSocket conn) throws HandlerException {
        final NttsClientConnection connection = conn.getAttachment();
        if (connection == null) {
            throw new IllegalMessageException("The first message has to be HELLO, there is no connection attached...");
        }
        return connection;
    }

    /**
     * This is just an outsourcing for the Faction Handlers 'FACTION_'
     * 
     * @param key the key that should be handled
     * @return null if the access is invalid, the requested uuid's otherwise
     */
    private static UUID[] getFactionMembers(final NttsController controller, final NttsClientConnection connection,
            final MetaKeyEnum key) {

        // Check if the game-phase allows such calculations
        final GamePhaseEnum currentPhase = controller.getConfiguration().getGamePhase();
        if (currentPhase.isBefore(GamePhaseEnum.ITEM_ASSIGNMENT)) {
            messageBuffer.add("You cannot get any Faction-Information before the drafting phase.");
            return INVALID_UUID;
        }

        // check if allowed to ask this information
        final GameRoleEnum role = connection.getGameRole();
        if (role == GameRoleEnum.PLAYER_ONE && key != MetaKeyEnum.FACTION_PLAYER1 // P1 asks for not him
                || role == GameRoleEnum.PLAYER_TWO && key != MetaKeyEnum.FACTION_PLAYER2) {// p2 asks not for him
            messageBuffer.add("You, as " + role + " are not allowed to ask for '" + key.getKey() + "'.");
            return INVALID_UUID;
        }

        // if we are in assignment - consult the drafting-phase-controller
        if (currentPhase == GamePhaseEnum.ITEM_ASSIGNMENT) {
            final DraftingPhaseController dpController = controller.getDraftingPhaseController();
            switch (key) {
                case FACTION_PLAYER1:
                    return dpController.getPlayerOneChoice().getCharacters().toArray(UUID[]::new);
                case FACTION_PLAYER2:
                    return dpController.getPlayerTwoChoice().getCharacters().toArray(UUID[]::new);
                case FACTION_NEUTRAL:
                    messageBuffer.add("There is no neutral faction in the item assignment phase.");
                    return NO_KEY_UUID;
                default:
                    throw new ThisShouldNotHappenException(keyShouldNotHappen(key));
            }
        } else {
            // is to be in main game phase
            final GameFieldController gfController = controller.getMainGamePhaseController().getGameFieldController();
            switch (key) {
                case FACTION_PLAYER1:
                    return gfController.getPlayerOneFaction().stream().map(Character::getCharacterId)
                            .toArray(UUID[]::new);
                case FACTION_PLAYER2:
                    return gfController.getPlayerTwoFaction().stream().map(Character::getCharacterId)
                            .toArray(UUID[]::new);
                case FACTION_NEUTRAL:
                    return gfController.getNeutralFaction().stream().map(Character::getCharacterId)
                            .toArray(UUID[]::new);
                default:
                    throw new ThisShouldNotHappenException(keyShouldNotHappen(key));
            }
        }
    }

    /**
     * This is just an outsourcing for the Faction Handlers 'GADGETS_'
     * 
     * @param key the key that should be handled
     * @return null if the access is invalid, the requested gadgets otherwise
     */
    private static GadgetEnum[] getGadgets(final NttsController controller, final NttsClientConnection connection,
            final MetaKeyEnum key) {

        // Check if the game-phase allows such calculations
        final GamePhaseEnum currentPhase = controller.getConfiguration().getGamePhase();
        if (currentPhase.isBefore(GamePhaseEnum.ITEM_ASSIGNMENT)) {
            messageBuffer.add("You cannot get any Gadget-Information before the drafting phase.");
            return INVALID_GADGET;
        }

        // check if allowed to ask this information
        final GameRoleEnum role = connection.getGameRole();
        if (role == GameRoleEnum.PLAYER_ONE && key != MetaKeyEnum.GADGETS_PLAYER1 // P1 asks for not him
                || role == GameRoleEnum.PLAYER_TWO && key != MetaKeyEnum.GADGETS_PLAYER2) {// p2 asks not for him
            messageBuffer.add("You, as " + role + " are not allowed to ask for '" + key.getKey() + "'.");
            return INVALID_GADGET;
        }

        // if we are in assignment - consult the drafting-phase-controller
        if (currentPhase == GamePhaseEnum.ITEM_ASSIGNMENT) {

            final DraftingPhaseController dpController = controller.getDraftingPhaseController();
            switch (key) {
                case GADGETS_PLAYER1:
                    return dpController.getPlayerOneChoice().getGadgets().toArray(GadgetEnum[]::new);
                case GADGETS_PLAYER2:
                    return dpController.getPlayerTwoChoice().getGadgets().toArray(GadgetEnum[]::new);
                default:
                    throw new ThisShouldNotHappenException(keyShouldNotHappen(key));
            }

        } else {
            final GameFieldController gfController = controller.getMainGamePhaseController().getGameFieldController();

            switch (key) {
                case GADGETS_PLAYER1:
                    return collectGadgetsFromFaction(gfController.getPlayerOneFaction());
                case GADGETS_PLAYER2:
                    return collectGadgetsFromFaction(gfController.getPlayerTwoFaction());
                default:
                    throw new ThisShouldNotHappenException(keyShouldNotHappen(key));
            }
        }

    }

    private static GadgetEnum[] collectGadgetsFromFaction(final Faction faction) {
        return faction.stream().flatMap(c -> c.getGadgets().stream()).map(Gadget::getGadget).toArray(GadgetEnum[]::new);
    }

    public static HandlerReport handleMetaInformationRequest(final NttsController controller, final WebSocket conn,
            final String message) throws HandlerException {

        // Lets' cast the message
        final RequestMetaInformationMessage requestMetaInformationMessage = GameDataGson.fromJson(message,
                RequestMetaInformationMessage.class);

        final NttsClientConnection connection = assertNttsClientConnection(conn);

        controller.getAuthor().meta(connection.getClientName(), requestMetaInformationMessage.getKeys());

        messageBuffer.clear();

        // Now we iterate over all requests and build the answer from them,
        // but first we will filter duplicates.

        final String[] requests = Arrays.stream(requestMetaInformationMessage.getKeys()).distinct()
                .toArray(String[]::new);
        final Map<String, Object> values = Arrays.stream(requests).map(r -> populateSingle(controller, connection, r))
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue()), HashMap::putAll);
        final MetaInformationMessage metaInformationMessage = new MetaInformationMessage(connection.getClientId(),
                values, messageBuffer.toString());

        return new HandlerReport(metaInformationMessage, new MessageTarget(connection.getClientId()));
    }

    /**
     * Populates a specific entry on population
     * 
     * @param controller The controller the callback was called for
     * @param connection The connection to server
     * @param request    The specific request to handle
     * @return Pair of dat key to object. object may be null
     */
    private static ImmutablePair<String, Object> populateSingle(final NttsController controller,
            final NttsClientConnection connection, final String request) {
        try {
            return ImmutablePair.of(request, populateInformation(controller, connection, request));
        } catch (final Exception ex) {
            magpie.writeError("Covered critical exception when populating metadata for: " + connection + " on request: "
                    + request, "Pop");
            magpie.writeException(ex, "Pop");
        }
        return ImmutablePair.of(request, null);
    }

    public static HandlerReport handleReplayRequest(final NttsController controller, final WebSocket conn,
            final String message) throws HandlerException {

        // There is not really any need to (re-)cast the message, yet still:
        final RequestReplayMessage requestReplayMessage = GameDataGson.fromJson(message, RequestReplayMessage.class);
        magpie.writeDebug("Received replay request: " + requestReplayMessage, "Handler");

        final NttsClientConnection connection = assertNttsClientConnection(conn);

        final Configuration configuration = controller.getConfiguration();

        if (!configuration.getServerConfig().offerReplay()) {
            throw new IllegalMessageException("This server has no replay - it was disabled with the server-config!");
        }

        if (configuration.getGamePhase().isBefore(GamePhaseEnum.END)) {
            throw new IllegalMessageException("You cannot ask for a replay before the game has ended. Current phase: "
                    + configuration.getGamePhase());
        }

        int rounds = 0;
        final GameFieldController gameFieldController = controller.getMainGamePhaseController()
                .getGameFieldController();
        if (Objects.nonNull(gameFieldController)) {
            rounds = gameFieldController.getCurrentRoundNumber();
        }

        final ReplayMessage replayMessage = controller.getClientManager().getRecordKeeper()
                .constructFor(connection.getClientId(), configuration, connection.getGameRole(), rounds);

        return new HandlerReport(replayMessage, new MessageTarget(connection.getClientId()));
    }

    /**
     * Produce the greeting meta-information-message to be bounced back on 'hello'
     * 
     * @param controller The controller requesting the greeting-generation
     * 
     * @return The meta-information-message to bounce back
     */
    public static MetaInformationMessage generateGreeting(final NttsController controller) {

        final Map<String, Object> values = new HashMap<>();

        for (final MetaKeyEnum metaKeyEnum : new MetaKeyEnum[] { MetaKeyEnum.PLAYER_COUNT, MetaKeyEnum.PLAYER_NAMES,
                MetaKeyEnum.SPECTATOR_COUNT, MetaKeyEnum.SPECTATOR_NAMES }) {
            try {
                values.put(metaKeyEnum.getKey(), populateInformation(controller, null, metaKeyEnum.getKey()));
            } catch (final Exception ex) {
                // actually we really do not care about the type of information, if it
                // is not working it does not work
                values.put(metaKeyEnum.getKey(), null);
                magpie.writeError("Covered critical exception when populating metadata for greeting ", "Greet");
                magpie.writeException(ex, "Greet");
            }
        }

        return new MetaInformationMessage(null, values, "The one you get on creation. The clientId can be null.");
    }

    private static String keyShouldNotHappen(final MetaKeyEnum key) {
        return "You should not call this method using the key " + key + ". This should be guarded anyways.";
    }
}