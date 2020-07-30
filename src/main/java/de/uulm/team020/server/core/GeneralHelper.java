package de.uulm.team020.server.core;

import java.util.Objects;

import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.validation.GameDataGson;

/**
 * This class provides static helper for the {@link NttsController} and its
 * slaves
 * 
 * @author Florian Sihler
 * @version 1.0, 04/29/2020
 */
public class GeneralHelper {

    private GeneralHelper() {
    }

    /**
     * Returns the message container from json-data but validates connection-details
     * as well to assure the validity of the received data
     * 
     * @param json       The data to parse
     * @param connection The connection that is guessed to be linked to this message
     * 
     * @return The container, if the data lead to a valid one
     * @throws HandlerException If there was any error with the parsing - or with
     *                          the validity
     */
    public static MessageContainer getContainerWithUuidCheck(final String json, final NttsClientConnection connection)
            throws HandlerException {
        final MessageContainer container = GameDataGson.getContainer(json);
        if (connection != null && container != null
                && (!Objects.equals(container.getClientId(), connection.getClientId()))) {
            throw new IllegalMessageException("The uuid you sent (" + container.getClientId()
                    + ") differs from the one you got with the Hello-Reply (" + connection.getClientId()
                    + "). This is fatal and probably totally your fault.");
        }
        return container;
    }

}