package de.uulm.team020.server.core;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.FilterBuilder;
import de.uulm.team020.server.core.datatypes.FilterHandlerPair;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.IMessageFilter;
import de.uulm.team020.server.core.datatypes.IMessageHandler;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.core.handlers.GenericHandlers;
import de.uulm.team020.server.core.handlers.InformationHandlers;
import de.uulm.team020.server.core.handlers.SessionHandlers;

/**
 * This class is used by the {@link NttsController} and handles
 * message-receiving. It is only here to out-source the logic for closing and is
 * used for further structure - it will perform the filtering as well.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class CallController {

    private static final String SERVER_TEXT = "Server";
    private static final String HANDLER_TEXT = "Handler";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private NttsController controller;

    private final Map<MessageTypeEnum, List<FilterHandlerPair<HandlerReport>>> handlers;

    /**
     * Create a new call controller for the server
     * 
     * @param controller The main controller this one shall serve
     */
    public CallController(NttsController controller) {
        this.controller = controller;
        this.handlers = new EnumMap<>(MessageTypeEnum.class);
    }

    /**
     * Loads default handler set
     */
    public void initDefaultHandlers() {
        final ServerConfiguration config = controller.getConfiguration().getServerConfig();
        registerHelloHandlers();
        registerGenericLeaveAndPauseSessionManagement();
        registerMetaInformationHandlers();
        registerDraftingPhaseHandlers(config);
        registerMainGamePhaseHandlers();
        registerReplayHandlers();
    }

    private void registerReplayHandlers() {
        registerHandler(MessageTypeEnum.REQUEST_REPLAY, InformationHandlers::handleReplayRequest,
                new FilterBuilder().hasToBeConnected().shouldBeInGamePhases(GamePhaseEnum.END).build());
        registerIllegalHandler(MessageTypeEnum.REPLAY);
    }

    private void registerHelloHandlers() {
        registerHandler(MessageTypeEnum.HELLO, SessionHandlers::handleHelloAndCreateNttsClientConnection);
        registerIllegalHandler(MessageTypeEnum.HELLO_REPLY);
    }

    private void registerMetaInformationHandlers() {
        // TODO: maybe allow without being connected and assume spectator then?
        registerHandler(MessageTypeEnum.REQUEST_META_INFORMATION, InformationHandlers::handleMetaInformationRequest,
                new FilterBuilder().build());
        registerIllegalHandler(MessageTypeEnum.META_INFORMATION);
    }

    private void registerGenericLeaveAndPauseSessionManagement() {
        registerHandler(MessageTypeEnum.GAME_LEAVE, SessionHandlers::handleGameLeave,
                new FilterBuilder().hasToBeConnected().build());
        registerIllegalHandler(MessageTypeEnum.GAME_LEFT);

        registerHandler(MessageTypeEnum.REQUEST_GAME_PAUSE, SessionHandlers::handlePauseRequest,
                new FilterBuilder().hasToBeConnected().build());
        registerIllegalHandler(MessageTypeEnum.GAME_PAUSE);
        registerHandler(MessageTypeEnum.RECONNECT, SessionHandlers::handleReconnectRequest);
    }

    private void registerMainGamePhaseHandlers() {
        FilterBuilder gameOperationBuilder = new FilterBuilder().hasToBeConnected()
                .shouldBeInGamePhase(GamePhaseEnum.MAIN_GAME_PLAY);
        registerHandler(MessageTypeEnum.GAME_OPERATION, controller.getMainGamePhaseController()::handleOperation,
                gameOperationBuilder.build());
        registerIllegalHandler(MessageTypeEnum.REQUEST_GAME_OPERATION);
    }

    private void registerDraftingPhaseHandlers(final ServerConfiguration config) {
        FilterBuilder itemAssignmentBuilder = new FilterBuilder().hasToBeConnected()
                .shouldBeInGamePhase(GamePhaseEnum.ITEM_ASSIGNMENT);
        if (config.receiveInPause()) {
            itemAssignmentBuilder.orInPause();
        }
        registerHandler(MessageTypeEnum.ITEM_CHOICE, controller.getDraftingPhaseController()::handleItemChoice,
                itemAssignmentBuilder.build());

        registerHandler(MessageTypeEnum.ITEM_CHOICE,
                (ctrl, c, m) -> GenericHandlers.handleNotAllowedToSend(ctrl, c, m,
                        "Item-Assignment is only allowed if connected in assignment phase. Phase: "
                                + controller.getConfiguration().getGamePhase() + "."),
                new FilterBuilder(itemAssignmentBuilder).negatePhases().build());

        FilterBuilder equipmentAssignmentBuilder = new FilterBuilder().hasToBeConnected()
                .shouldBeInGamePhase(GamePhaseEnum.ITEM_ASSIGNMENT);
        if (config.receiveInPause()) {
            equipmentAssignmentBuilder.orInPause();
        }

        registerIllegalHandler(MessageTypeEnum.REQUEST_EQUIPMENT_CHOICE);

        registerHandler(MessageTypeEnum.EQUIPMENT_CHOICE,
                controller.getDraftingPhaseController()::handleEquipmentChoice, equipmentAssignmentBuilder.build());

        registerHandler(MessageTypeEnum.EQUIPMENT_CHOICE,
                (ctrl, c, m) -> GenericHandlers.handleNotAllowedToSend(ctrl, c, m,
                        "Item-Assignment is only allowed if connected in assignment/equip phase. Phase: "
                                + controller.getConfiguration().getGamePhase() + "."),
                new FilterBuilder(equipmentAssignmentBuilder).negatePhases().build());
    }

    /**
     * Emplaces a new handler into {@link #handlers} which will been called
     * regardless of metadata (no/always true Filter) and deny the Message. This can
     * be used to block Messages with this type whilst being able to provide some
     * additional handlers for them.
     * <p>
     * Note: the current implementation does not check for any duplicates, as it was
     * not deemed necessary.
     * 
     * @param type the message-type to register for
     * 
     * @return true if emplacing the handler was successful. Note: the current
     *         implementation will never fail and therefore will not return false;
     * 
     * @see #registerHandler(MessageTypeEnum, IMessageHandler)
     */
    public boolean registerIllegalHandler(final MessageTypeEnum type) {
        return registerHandler(type, (final NttsController ctrl, final WebSocket conn,
                final String message) -> GenericHandlers.handleNotAllowedToSend(ctrl, conn, message, type),
                FilterBuilder.trueFilter());
    }

    /**
     * Emplaces a new handler into {@link #handlers} which will been called
     * regardless of metadata (no/always true Filter)
     * <p>
     * Note: the current implementation does not check for any duplicates, as it was
     * not deemed necessary.
     * 
     * @param type    the message-type to register for
     * @param handler the handler to be executed in case of event
     * @return true if emplacing the handler was successful. Note: the current
     *         implementation will never fail and therefore will not return false;
     * 
     * @see #registerHandler(MessageTypeEnum, IMessageHandler, IMessageFilter)
     */
    public boolean registerHandler(final MessageTypeEnum type, final IMessageHandler<HandlerReport> handler) {
        return registerHandler(type, handler, FilterBuilder.trueFilter());
    }

    /**
     * Emplaces a new handler into {@link #handlers}.
     * <p>
     * Note: the current implementation does not check for any duplicates, as it was
     * not deemed necessary.
     * 
     * @param type    the message-type to register for
     * @param handler the handler to be executed in case of event
     * @param filter  the filter to specify execution
     * @return true if emplacing the handler was successful. Note: the current
     *         implementation will never fail and therefore will not return false;
     */
    public boolean registerHandler(final MessageTypeEnum type, final IMessageHandler<HandlerReport> handler,
            final IMessageFilter filter) {
        magpie.writeDebug("Registering Handler " + handler + " with filter: " + filter, "Register");
        final FilterHandlerPair<HandlerReport> pair = new FilterHandlerPair<>(filter, handler);
        if (!handlers.containsKey(type))
            handlers.put(type, new LinkedList<>());
        final List<FilterHandlerPair<HandlerReport>> list = handlers.get(type);
        // maybe list contains to avoid doubles?
        list.add(pair);
        return true;
    }

    /**
     * Handle the message-receive
     * 
     * @param conn    The connection the message was received from
     * @param message The message received
     */
    public void handleMessageCall(final WebSocket conn, final String message) {
        // Do we have an NttsClientConnection attached?
        NttsClientConnection nttsConn = conn.getAttachment();
        try {
            final MessageContainer container = GeneralHelper.getContainerWithUuidCheck(message, nttsConn);
            final MessageTypeEnum type = container == null ? null : container.getType();

            if (type == null) {
                handleNullTypeOnContainer(conn, nttsConn, message);
                return;
            }

            // Check for freshness, maybe register for meta-information?
            if (this.controller.freshnessPolicy.isTooOld(container)) {
                return;
            }

            // check if we have callbacks for this type:
            final List<FilterHandlerPair<HandlerReport>> handlerPairs = handlers.get(type);
            if (handlerPairs == null || handlerPairs.isEmpty()) {// no handlers
                magpie.writeError("There are no handlers for a message of type " + type, HANDLER_TEXT);
                throw new IllegalMessageException("The message-type you sent (" + type
                        + ") is (at least currently) not supported by this server.");
            }

            // Reports to be returned by the handlers.
            final ArrayList<HandlerReport> reports = new ArrayList<>(2);
            if (nttsConn == null) {
                nttsConn = NttsClientConnection.VOID_CONNECTION;
            }

            final ArrayList<String> errors = new ArrayList<>(handlerPairs.size());
            boolean found = false;

            for (final FilterHandlerPair<HandlerReport> handlerPair : handlerPairs) {
                found = processHandlerReport(conn, message, nttsConn, reports, errors, found, handlerPair);
            }
            if (!found) {
                throw new IllegalMessageException("You are not allowed to send the message of type " + type
                        + ", as you fail to satisfy any of the following constraints: " + errors);
            }

            this.controller.messageSender.handleReports(conn, reports.toArray(HandlerReport[]::new));
        } catch (final HandlerException ex) {
            sendErrorMessageToDealWithHandlerException(conn, ex);
        }
    }

    private boolean processHandlerReport(final WebSocket conn, final String message, NttsClientConnection nttsConn,
            final ArrayList<HandlerReport> reports, final ArrayList<String> errors, boolean found,
            final FilterHandlerPair<HandlerReport> handlerPair) throws HandlerException {
        final String error = handlerPair.surviveFilter(controller.getConfiguration(), nttsConn.getClientRole(),
                nttsConn.getClientId(), nttsConn.getStrikesOwned(), nttsConn.isCurrentlyConnected());
        if (error.isEmpty()) {
            found = true;
            reports.add(handlerPair.getHandler().apply(this.controller, conn, message));
        } else {
            errors.add(error);
        }
        return found;
    }

    private void sendErrorMessageToDealWithHandlerException(final WebSocket conn, final HandlerException ex) {
        magpie.writeError("Error while handling: " + ex.getError() + " (" + ex.getMessage() + ")", HANDLER_TEXT);
        final NttsClientConnection connection = conn.getAttachment();
        UUID servedByClientId;
        if (connection == null) { // Bounce-Back to sender!
            servedByClientId = null;
        } else {
            servedByClientId = connection.getClientId();
        }
        final ErrorMessage error = new ErrorMessage(servedByClientId, ex.getError(), ex.getMessage());
        controller.getAuthor().error("Ending Connection with: " + servedByClientId + " by Exception (" + ex + ")");
        controller.getAuthor().error("Message: " + error);
        this.controller.messageSender.sendMessage(conn, error);
        conn.close(CloseFrame.REFUSE);
    }

    private void handleNullTypeOnContainer(WebSocket conn, NttsClientConnection nttsConn, String message) {
        magpie.writeWarning("The Message: '" + message + "' was not in a valid Containerformat!", HANDLER_TEXT);
        UUID servedByClientId;
        if (nttsConn == null) {
            servedByClientId = null;
        } else {
            servedByClientId = nttsConn.getClientId();
        }

        final ErrorMessage error = new ErrorMessage(servedByClientId, ErrorTypeEnum.ILLEGAL_MESSAGE,
                "The Message you send was not in a valid containerformat!");
        controller.messageSender.sendMessage(conn, error);
        conn.close(CloseFrame.REFUSE);
    }
}