package de.uulm.team020.server.core;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.uulm.team020.helper.DateHelper;
import de.uulm.team020.networking.messages.ReplayMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;

/**
 * This class was written with the {@link DummySendMessagesBuffer} in mind. It's
 * goal is to store all outgoing messages to a connection if it is a player
 * connection.
 * <p>
 * The record keeper can then be used to offer replay data for a player. It may
 * be extended for a replay-client to be hooked to the server which then
 * performs full information-cycles by using
 * {@link RequestMetaInformationMessage}-calls.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/06/2020
 */
public class ReplayRecordKeeper {

    private final NttsClientManager manager;

    private List<String> playerOneMessages;
    private List<String> playerTwoMessages;

    private Date gameStart = null;
    private Date gameEnd = null;

    /**
     * Construct a new record keeper
     * 
     * @param manager The operating client manager, which will be used to identify
     *                important message-targets.
     */
    public ReplayRecordKeeper(NttsClientManager manager) {
        this.manager = manager;

        resetPlayerOneRecord();
        resetPlayerTwoRecord();
    }

    /**
     * Reset all records kept for player one. This may be used if the player
     * disconnects before the main game start and looses it's slot.
     * <p>
     * <i>Note:</i>
     * <ul>
     * <li>There will be no backup of this data outside of test-mode.</li>
     * <li>There will be no waits recorded.</li>
     * </ul>
     * 
     * @see #resetPlayerTwoRecord()
     */
    public void resetPlayerOneRecord() {
        this.playerOneMessages = new LinkedList<>();
    }

    /**
     * Reset all records kept for player two. This may be used if the player
     * disconnects before the main game start and looses it's slot.
     * <p>
     * <i>Note:</i>
     * <ul>
     * <li>There will be no backup of this data outside of test-mode.</li>
     * <li>There will be no waits recorded.</li>
     * </ul>
     * 
     * @see #resetPlayerOneRecord()
     */
    public void resetPlayerTwoRecord() {
        this.playerTwoMessages = new LinkedList<>();
    }

    /**
     * Try to record a message
     * 
     * @param target  Target of the message, will be decoded to any possible
     *                connection using the
     *                {@link NttsClientManager#getConnectionByWebSocket(WebSocket)}-feature.
     * @param message The message to record.
     * 
     * @return True, via {@link List#add(Object)} if the message was recorded, false
     *         otherwise.
     */
    public boolean keepRecord(WebSocket target, String message) {

        NttsClientConnection targetConnection = manager.getConnectionByWebSocket(target);
        if (targetConnection == null) { // may be meta on init won't be recorded ;)
            return false;
        }
        switch (targetConnection.getGameRole()) {
            case PLAYER_ONE:
                return playerOneMessages.add(message);
            case PLAYER_TWO:
                return playerTwoMessages.add(message);
            default:
                return false;
        }
    }

    public void gameStarted() {
        this.gameStart = DateHelper.now();
    }

    public void gameEnded() {
        this.gameEnd = DateHelper.now();
    }

    /**
     * Build the replay message for a given role, might be extended in the future
     * 
     * @param clientId      The clientId requesting the message
     * @param configuration The configuration to be used
     * @param target        Desired target role
     * @param rounds        Number of rounds played
     * 
     * @return The built replay message
     */
    public ReplayMessage constructFor(UUID clientId, Configuration configuration, GameRoleEnum target, int rounds) {
        switch (target) {
            case PLAYER_ONE:
                return constructForBuffer(clientId, configuration, playerOneMessages, rounds);
            default:
            case PLAYER_TWO:
                return constructForBuffer(clientId, configuration, playerTwoMessages, rounds);
        }
    }

    private ReplayMessage constructForBuffer(UUID clientId, Configuration configuration, List<String> messageBuffer,
            int rounds) {
        NttsClientConnection playerOne = manager.getPlayerOne();
        NttsClientConnection playerTwo = manager.getPlayerTwo();
        return new ReplayMessage(clientId, //
                configuration.getSessionId(), //
                gameStart, gameEnd, //
                playerOne == null ? null : playerOne.getClientId(), //
                playerTwo == null ? null : playerTwo.getClientId(), //
                playerOne == null ? null : playerOne.getClientName(), //
                playerTwo == null ? null : playerTwo.getClientName(), //
                rounds, //
                configuration.getScenario(), //
                configuration.getMatchconfig(), //
                configuration.getCharacterDescriptions(), messageBuffer.toArray(String[]::new));
    }

}