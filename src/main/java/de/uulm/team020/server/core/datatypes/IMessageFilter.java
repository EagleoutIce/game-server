package de.uulm.team020.server.core.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.CallController;
import de.uulm.team020.server.core.NttsController;

/**
 * Provides a Filter for a Message, which can be used in
 * {@link CallController#registerHandler(MessageTypeEnum, IMessageHandler, IMessageFilter)}
 * 
 * @author Florian Sihler
 * @version 1.0, 03/22/2020
 */
@FunctionalInterface
public interface IMessageFilter {

    /**
     * Filter-Method should return <code>true</code> if and only if the connected
     * Method should be executed by the {@link NttsController}.
     *
     * @param configuration The configuration used by the controller
     * @param role          The role of the message-sender
     * @param playerId      The id of the message-sender
     * @param strikeCount   The amount of strikes the sender has
     * @param isConnected   is the player currently connected?
     *
     * @return Empty String, if the filter applies, the reason why it doesn't apply
     *         otherwise
     */
    String filter(Configuration configuration, RoleEnum role, UUID playerId, int strikeCount, boolean isConnected);

}