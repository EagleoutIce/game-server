package de.uulm.team020.server.addons.strikes;

import java.util.List;

import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.StrikeMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.MessageTargetPair;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Just a simple strike-controller, handling strikes for both players. Mostly
 * triggered, when a message is missing in time
 * 
 * @author Florian Sihler
 * @version 1.0, 04/15/2020
 */
public class StrikeController {

    private int playerOneStrikes;
    private int playerTwoStrikes;

    private Configuration configuration;

    /**
     * Construct a new Strike controller
     * 
     * @param configuration The configuration this controller should use
     */
    public StrikeController(Configuration configuration) {
        playerOneStrikes = 0;
        playerTwoStrikes = 0;
        this.configuration = configuration;
    }

    /**
     * Register a strike for a connection
     * 
     * @param connection The connection the strike was registered for.
     * @param reason     A textual representation defending the strike.
     * 
     * @return A HandlerReport stating all Messages to send
     */
    public HandlerReport strike(NttsClientConnection connection, String reason) {
        final GameRoleEnum gameRole = connection.getGameRole();
        if (gameRole == GameRoleEnum.PLAYER_ONE) {
            playerOneStrikes += 1;
            return strikeCheck(connection, playerOneStrikes, reason);
        } else if (gameRole == GameRoleEnum.PLAYER_TWO) {
            playerTwoStrikes += 1;
            return strikeCheck(connection, playerTwoStrikes, reason);
        } else {
            throw new ThisShouldNotHappenException(
                    "Only Players are allowed to get strikes, but " + connection + " is not a player");
        }
    }

    /**
     * Reset the strike counter for a connection
     * 
     * @param connection The connection to reset the strike counter for
     */
    public void reset(NttsClientConnection connection) {
        final GameRoleEnum gameRole = connection.getGameRole();
        if (gameRole == GameRoleEnum.PLAYER_ONE) {
            playerOneStrikes = 0;
        } else if (gameRole == GameRoleEnum.PLAYER_TWO) {
            playerTwoStrikes = 0;
        } else {
            throw new ThisShouldNotHappenException(
                    "Only Players are allowed to reset strikes, but " + connection + " is not a player");
        }
    }

    private HandlerReport strikeCheck(NttsClientConnection connection, int n, String reason) {
        final int strikeMax = configuration.getMatchconfig().getStrikeMaximum();
        StrikeMessage strikeMessage = new StrikeMessage(connection.getClientId(), n, strikeMax, reason);
        if (n >= strikeMax) {
            // end connection
            ErrorMessage errorMessage = new ErrorMessage(connection.getClientId(), ErrorTypeEnum.TOO_MANY_STRIKES);
            return new HandlerReport(
                    List.of(new MessageTargetPair(strikeMessage, new MessageTarget(connection.getClientId())),
                            new MessageTargetPair(errorMessage, new MessageTarget(connection.getClientId()))),
                    true);
        } else {
            return new HandlerReport(strikeMessage, new MessageTarget(connection.getClientId()));
        }
    }

    /**
     * @return True if p1 is still allowed to play, else false
     */
    public boolean playerOne() {
        return playerOneStrikes < configuration.getMatchconfig().getStrikeMaximum();
    }

    /**
     * @return True if p2 is still allowed to play, else false
     */
    public boolean playerTwo() {
        return playerTwoStrikes < configuration.getMatchconfig().getStrikeMaximum();
    }

    public int getPlayerOneStrikes() {
        return playerOneStrikes;
    }

    public int getPlayerTwoStrikes() {
        return playerTwoStrikes;
    }
}