package de.uulm.team020.server.core;

import java.io.IOException;
import java.util.List;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;

/**
 * This class is used by the {@link NttsController} and handles the
 * connection-close of a client. It is only here to out-source the logic for
 * closing and is used for further structure.
 * 
 * @author Florian Sihler
 * @version 1.0b, 07/7/2020
 */
public class CloseController {

    private static final String SERVER_TEXT = "Server";
    private static final String HANDLER_TEXT = "Handler";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private final NttsController controller;

    /**
     * Create a new close controller to server
     * 
     * @param controller The main controller this one shall serve
     */
    public CloseController(final NttsController controller) {
        this.controller = controller;
    }

    public void closeHandlerSchedule(final WebSocket conn, final int code, final String reason, final boolean remote) {
        // If we are before the game has started, any close will remove the client from
        // the current session. There has to be no game-pause or timeout-register
        // whatsoever....
        // OR:
        // If we are after the main part, there again, is no reason to keep anyone
        // connected, as there is no need to 'reconnect' for getting the status
        // information. Maybe this should be changed - in this case there is no need to
        // send a GamePause message.
        if (controller.getConfiguration().getGamePhase().isBefore(GamePhaseEnum.GAME_START)
                || controller.getConfiguration().getGamePhase().isAfterOrEqual(GamePhaseEnum.MAIN_GAME_END)) {
            controller.getAuthor().close(conn);
            controller.getClientManager().removeConnection(conn);
        }
        // If we are in the main game phase we have to do the following, if the client
        // is a player
        // otherwise, we just perform the default removal and defend the close
        // 1. Send a pause message to the other player and all spectators
        // 2. Register a Timeout to remove the connection if it triggers.
        // 3. Wait for reconnect (maybe another state?)
        // 4. If the other player leaves, we let the dc player win
        else {
            closeInMainGamePhase(conn, code);
        }
        // maybe distinguish further for 'ping-pong'-reasons?
        // Test with: sudo tc qdisc change dev lo root netem loss 100%
        magpie.writeInfo("Close was defended by Reason: " + code + " (" + reason + ":"
                + NttsServer.closeStatusDecode(code) + "), from: " + (remote ? "remote" : "server"), SERVER_TEXT);
        authorDumpClose(conn);
    }

    private void closeInMainGamePhase(final WebSocket conn, final int code) {
        final NttsClientConnection connection = conn.getAttachment();
        // If there is no connection the client never registered with successful hello
        // and we can forget him
        if (connection == null) {
            controller.getClientManager().removeConnection(conn);
        }
        // If it was a spectator there is never a pause to be scheduled
        else if (connection.getClientRole() == RoleEnum.SPECTATOR) {
            controller.getAuthor().close(conn);
            controller.getClientManager().removeConnection(conn); // just toss for now
        }
        // A player shall get a pause
        else {
            closeInMainGamePhaseAsPlayerSwitchToPause(conn, code, connection);
        }
    }

    private void closeInMainGamePhaseAsPlayerSwitchToPause(final WebSocket conn, final int code,
            final NttsClientConnection connection) {
        // If the client has a connection but was never a player -- e.g. if he tried to
        // connect as another, yet illegal player we will simply remove him too
        if (isNoPlayer(connection)) {
            controller.getAuthor().close(conn);
            controller.getClientManager().removeConnection(conn);
        }
        // In the other case a player (ai or human) disconnected while the
        // main-game-phase we have to check if he "wanted" to leave, or if thee was
        // another reason
        else if (code == CloseFrame.ABNORMAL_CLOSE) {
            controller.getAuthor().close(conn); // crash maybe?
            closeWantsReconnect(connection);
            // magpie.writeError("Handling for timeout is currently WIP", SERVER_TEXT)
        } else {
            controller.gameOver(connection, VictoryEnum.VICTORY_BY_KICK);
            controller.getClientManager().removeConnection(conn); // just toss for now
        }
    }

    private boolean isNoPlayer(final NttsClientConnection connection) {
        return connection.getGameRole() == null || !connection.getGameRole().isPlayer();
    }

    private void closeWantsReconnect(final NttsClientConnection connection) {
        // Should a previous resume-request be forgotten?
        if (controller.getConfiguration().getServerConfig().forgetResumesOnCrash()) {
            controller.getPauseController().setResume(connection, false);
        }
        // are we already in an enforced gamePause?
        if (controller.getConfiguration().getGamePhase() != GamePhaseEnum.GAME_FORCE_PAUSED) {
            enforcePauseAsNotAlreadyWaiting(connection);

        } else {
            keepSpaceAsAlreadyInPause(connection);
        }
    }

    private void keepSpaceAsAlreadyInPause(final NttsClientConnection connection) {
        magpie.writeInfo("Now both players should be disconnected - ghost game", "Close");
        // In case the other one disconnects too, and the other one disconnects, both
        // should have their
        // old timeout
        controller.getConfiguration().getTimeoutController().schedulePlayerReconnect(connection,
                () -> controller.gameOver(connection, VictoryEnum.VICTORY_BY_LEAVE));

        connection.setCurrentlyConnected(false);
    }

    private void enforcePauseAsNotAlreadyWaiting(final NttsClientConnection connection) {
        controller.bufferGamePhase();

        // step zero, pause currently running phase handlers
        if (controller.getConfiguration().getGamePhase() == GamePhaseEnum.ITEM_ASSIGNMENT) {
            // In this case we are holding our belly laughing - and pause the current game
            // turn timers instantly as i do not want to wait to the end
            magpie.writeInfo("Drafting Phase paused for: " + controller.getDraftingPhaseController().pause(),
                    HANDLER_TEXT);
        }

        // step one, enforce a gamePause
        final GamePauseMessage gamePauseMessage = new GamePauseMessage(null, true, true);
        controller.broadcast(gamePauseMessage, List.of(connection));
        controller.getConfiguration().shiftPhase(GamePhaseEnum.GAME_FORCE_PAUSED);

        controller.getConfiguration().getTimeoutController().schedulePlayerReconnect(connection,
                () -> controller.gameOver(connection, VictoryEnum.VICTORY_BY_LEAVE));

        connection.setCurrentlyConnected(false);
    }

    private void authorDumpClose(final WebSocket conn) {
        try {
            if (!controller.getUseDummy()) {
                controller.getAuthor().dump("loss-" + (conn.getAttachment() == null ? "unknown"
                        : ((NttsClientConnection) conn.getAttachment()).getClientName()));
            }
        } catch (final IOException ex) {
            magpie.writeException(ex, "Author-Write");
        }
    }

}