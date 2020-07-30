package de.uulm.team020.server.client.controller;

import de.uulm.team020.datatypes.exceptions.MessageException;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.GameLeftMessage;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.networking.messages.GameStatusMessage;
import de.uulm.team020.networking.messages.MetaInformationMessage;
import de.uulm.team020.networking.messages.RequestEquipmentChoiceMessage;
import de.uulm.team020.networking.messages.RequestItemChoiceMessage;
import de.uulm.team020.server.client.controller.blueprints.AbstractNetworkHandler;

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
public class ServerClientNetworkHandler extends AbstractNetworkHandler {

    /**
     * Construct a new Network-Handler
     * 
     * @param controller Handles network data
     */
    protected ServerClientNetworkHandler(ServerClientController controller) {
        super(controller);
    }

    @Override
    protected void onRequestItemChoice(RequestItemChoiceMessage request) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onMetaInformationMessage(MetaInformationMessage message) {
        try {
            magpie.writeInfo("Handling: " + message, "Handler");
            controller.getMetaInformationBuffer().updateMetaInformation(message.getInformation());
        } catch (MessageException ex) {
            magpie.writeException(ex, "handle");
            // currently not to be completed
        }
    }

    @Override
    protected void onRequestPause(GamePauseMessage pauseMessage) {
        // currently not to be completed
    }

    @Override
    protected void onGameLeft(GameLeftMessage message) {
        // currently not to be completed
    }

    @Override
    protected void onRequestEquipmentChoice(RequestEquipmentChoiceMessage message) {
        // currently not to be completed
    }

    @Override
    protected void onGameStatus(GameStatusMessage message) {
        // currently not to be completed
    }

    @Override
    protected void onError(ErrorMessage message) {
        controller.getErrorBuffer().add(message);
    }

}