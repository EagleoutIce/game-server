package de.uulm.team020.server.addons.timer;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import de.uulm.team020.helper.timer.TimeoutSchedule;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Controls timeouts provided by the {@link TimeoutSchedule}. Will only allow a
 * timeout if it is for a player.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public class TimeoutController {

    private static final IMagpie magpie = Magpie.createMagpieSafe("Server");

    private static final String TIMEOUT_TEXT = "Timeout";
    private static final String P1P2ERROR_TEXT = "Only player one and player two should be players.";
    private static final String THE_CLIENT_TEXT = "The client (";

    private TimeoutSchedule playerOneTimeout;
    private TimeoutSchedule playerTwoTimeout;
    /* used to store a pause if the player disconnects */
    private TimeoutSchedule quarantine;

    private NttsClientConnection currentlyPausingPlayer;

    private final Configuration configuration;
    private ExecutorService service;

    /**
     * Create a new Timeout-Controller
     *
     * @param configuration The configuration that shall be used.
     */
    public TimeoutController(Configuration configuration) {
        this.configuration = configuration;
        playerOneTimeout = new TimeoutSchedule();
        playerTwoTimeout = new TimeoutSchedule();
        quarantine = null;
        currentlyPausingPlayer = null;
    }

    /**
     * Create a new Timeout-Controller
     *
     * @param configuration The configuration that shall be used.
     * @param service       Service to be used to execute the tasks. Please note,
     *                      that if used, this will mean the exact execution-time
     *                      can vary.
     */
    public TimeoutController(Configuration configuration, ExecutorService service) {
        this.configuration = configuration;
        playerOneTimeout = new TimeoutSchedule(service);
        playerTwoTimeout = new TimeoutSchedule(service);
        quarantine = null;
        currentlyPausingPlayer = null;
    }

    private void schedulePlayerPause(TimeoutSchedule schedule, NttsClientConnection connection, Runnable runnable) {
        if (schedule.hasTask()) {
            throw new TimeoutForNonPlayerException(
                    connection.getGameRole() + " is already in-pause. (" + connection.getClientName() + ")");
        }

        schedule.runIn(runnable,
                (long) configuration.getMatchconfig().getPauseLimit() * configuration.getMatchconfigMultiplier());
        magpie.writeInfo("Pause for " + configuration.getMatchconfig().getPauseLimit() + "s", TIMEOUT_TEXT);
    }

    /**
     * Schedule a pause with an event
     * 
     * @param connection The connection to schedule the timeout for
     * @param runnable   the method to be execute after the timeout has elapsed
     */
    public void schedulePlayerPause(NttsClientConnection connection, Runnable runnable) {
        GameRoleEnum gameRoleEnum = connection.getGameRole();
        if (gameRoleEnum == null || !gameRoleEnum.isPlayer()) {
            throw new TimeoutForNonPlayerException(THE_CLIENT_TEXT + connection.getClientName()
                    + ") has no right to register a timeout. It is no player and should be kicked right away");
        }

        // we set the player who has started the pause
        currentlyPausingPlayer = connection;

        if (gameRoleEnum == GameRoleEnum.PLAYER_ONE) {
            schedulePlayerPause(playerOneTimeout, connection, runnable);
        } else if (gameRoleEnum == GameRoleEnum.PLAYER_TWO) {
            schedulePlayerPause(playerTwoTimeout, connection, runnable);
        } else {
            throw new ThisShouldNotHappenException(P1P2ERROR_TEXT);
        }
    }

    /**
     * Schedule a Game-Timeout. This will a pause a currently running pause.
     *
     * @param connection The connection to schedule the timeout for
     * @param runnable   the method to be execute after the timeout has elapsed
     */
    public void schedulePlayerReconnect(NttsClientConnection connection, Runnable runnable) {
        GameRoleEnum gameRoleEnum = connection.getGameRole();
        if (gameRoleEnum == null || !gameRoleEnum.isPlayer())
            throw new TimeoutForNonPlayerException(THE_CLIENT_TEXT + connection.getClientName()
                    + ") has no right to register a reconnect-timeout. It is no player and should be kicked right away");

        if (gameRoleEnum == GameRoleEnum.PLAYER_ONE) {
            playerOneTimeout = schedulePlayerReconnect(playerOneTimeout, connection, runnable);
        } else if (gameRoleEnum == GameRoleEnum.PLAYER_TWO) {
            playerTwoTimeout = schedulePlayerReconnect(playerTwoTimeout, connection, runnable);
        } else {
            throw new ThisShouldNotHappenException(P1P2ERROR_TEXT);
        }

        magpie.writeInfo("Pause for " + connection.getClientName(), TIMEOUT_TEXT);
    }

    private TimeoutSchedule schedulePlayerReconnect(TimeoutSchedule schedule, NttsClientConnection connection,
            Runnable runnable) {
        // quarantine the last one, as you cannot disconnect from a disconnect
        if (schedule.hasTask()) {
            quarantine = schedule;
            quarantine.pause();
            magpie.writeInfo("Quarantined pause because of reconnect-wait, remainingTime: "
                    + (TimeUnit.MILLISECONDS.toSeconds(quarantine.getRemainingTime())) + "s", TIMEOUT_TEXT);
        }
        if (Objects.isNull(this.service)) {
            schedule = new TimeoutSchedule();
        } else {
            schedule = new TimeoutSchedule(this.service);
        }
        schedule.runIn(() -> {
            runnable.run();
            TimeoutController.this.reviveFromQuarantine(connection.getGameRole());
        }, configuration.getMatchconfig().getReconnectLimit() * (long) configuration.getMatchconfigMultiplier());
        magpie.writeInfo("Reconnect-Pause for " + configuration.getMatchconfig().getReconnectLimit() + "s",
                TIMEOUT_TEXT);
        return schedule;
    }

    /**
     * Schedule a Turnphase-Timeout. This is not a pause, but a timeout ensuring the
     * player only has X seconds to perform a task.
     *
     * @param connection The connection to schedule the timeout for
     * @param runnable   the method to be execute after the timeout has elapsed
     */
    public void schedulePlayerTurn(NttsClientConnection connection, Runnable runnable) {
        GameRoleEnum gameRoleEnum = connection.getGameRole();
        if (gameRoleEnum == null || !gameRoleEnum.isPlayer()) {
            throw new TimeoutForNonPlayerException(THE_CLIENT_TEXT + connection.getClientName()
                    + ") has no right to register a turn-timeout. It is no player and should be kicked right away");
        }

        if (gameRoleEnum == GameRoleEnum.PLAYER_ONE) {
            schedulePlayerTurn(playerOneTimeout, connection, runnable);
        } else if (gameRoleEnum == GameRoleEnum.PLAYER_TWO) {
            schedulePlayerTurn(playerTwoTimeout, connection, runnable);
        } else {
            throw new ThisShouldNotHappenException(P1P2ERROR_TEXT);
        }

        magpie.writeInfo("TurnPhase-Limit for " + connection.getClientName(), TIMEOUT_TEXT);
    }

    private void schedulePlayerTurn(TimeoutSchedule schedule, NttsClientConnection connection, Runnable runnable) {
        if (schedule.hasTask()) {
            throw new TimeoutForNonPlayerException(
                    connection.getGameRole() + " is already in-pause. (" + connection.getClientName() + ")");
        }

        schedule.runIn(runnable,
                configuration.getMatchconfig().getTurnPhaseLimit() * (long) configuration.getMatchconfigMultiplier());

        magpie.writeInfo("TurnPhase-Timeout for " + configuration.getMatchconfig().getTurnPhaseLimit() + "s",
                TIMEOUT_TEXT);
    }

    private void reviveFromQuarantine(GameRoleEnum role) {
        if (quarantine == null) {
            return;
        }
        quarantine.resume();
        if (role == GameRoleEnum.PLAYER_ONE)
            playerOneTimeout = quarantine;
        else if (role == GameRoleEnum.PLAYER_TWO)
            playerTwoTimeout = quarantine;
        quarantine = null;
    }

    /**
     * This will cancel a running pause timeout (if there is one) it will rely on
     * {@link #cancelTimeout(NttsClientConnection)} but may use the player who
     * started the pause not the one who ended it.
     * 
     * @param fallback The connection to use if there was no pause registered with
     *                 {@link #schedulePlayerPause(NttsClientConnection, Runnable)}
     * @return The result of {@link #cancelTimeout(NttsClientConnection)}
     */
    public boolean cancelPauseTimeoutForPauser(NttsClientConnection fallback) {
        boolean feedback = cancelTimeout(currentlyPausingPlayer == null ? fallback : currentlyPausingPlayer);
        currentlyPausingPlayer = null;
        return feedback;
    }

    /**
     * Cancels the timeout for the running connection. If there is a timeout in
     * quarantine, as we are resuming from a reconnect, it will replace the pause,
     * so you would have to call this method again if you want to cancel both.
     *
     * @param connection the connection to cancel the timeout for
     * @return False if there was no timeout to stop - this shouldn't occur but can
     *         be caught
     */
    public boolean cancelTimeout(NttsClientConnection connection) {
        GameRoleEnum gameRoleEnum = connection.getGameRole();
        if (gameRoleEnum == null || !gameRoleEnum.isPlayer())
            return false;
        if (gameRoleEnum == GameRoleEnum.PLAYER_ONE) {
            if (!playerOneTimeout.hasTask()) {
                return false;
            }
            cancelPlayerOneTimeout();
        } else if (gameRoleEnum == GameRoleEnum.PLAYER_TWO) {
            if (!playerTwoTimeout.hasTask()) {
                return false;
            }
            cancelPlayerTwoTimeout();
        }
        return true;
    }

    private void cancelPlayerOneTimeout() {
        playerOneTimeout.cancelTimeout();
        magpie.writeInfo("The timeout for player one was stopped", TIMEOUT_TEXT);
        if (quarantine != null) {
            quarantine.resume();
            playerOneTimeout = quarantine;
            magpie.writeInfo("The quarantined timeout (for P1) was revived!", TIMEOUT_TEXT);
        }
    }

    private void cancelPlayerTwoTimeout() {
        playerTwoTimeout.cancelTimeout();
        magpie.writeInfo("The timeout for player two was stopped", TIMEOUT_TEXT);
        if (quarantine != null) {
            quarantine.resume();
            playerTwoTimeout = quarantine;
            magpie.writeInfo("The quarantined timeout (for P2) was revived!", TIMEOUT_TEXT);
        }
    }

    /**
     * @return shortest remaining pause time, -1 if none
     */
    public int getShortestRemainingPauseTime() {
        long timerOne = playerOneTimeout.getRemainingTime();
        long timerTwo = playerTwoTimeout.getRemainingTime();

        int shortest = -1;
        if (timerOne >= 0) // timer for one running
            shortest = (int) TimeUnit.MILLISECONDS.toSeconds(timerOne);
        if (timerTwo >= 0) {// timer for two running
            if (shortest >= 0)
                shortest = Math.min(shortest, (int) TimeUnit.MILLISECONDS.toSeconds(timerTwo));
            else
                shortest = (int) TimeUnit.MILLISECONDS.toSeconds(timerTwo);
        }
        return shortest;
    }

    /**
     * Stops all timers, leaves the Object in an invalid state
     * 
     * @see #cancelAllRunning()
     */
    public void stop() {
        cancelAllRunning();
        // playerOneTimeout = null
        // playerTwoTimeout = null
        quarantine = null;
    }

    /**
     * Cancels all currently running timeouts by calling
     * {@link TimeoutSchedule#cancelTimeout()}
     */
    public void cancelAllRunning() {
        if (playerOneTimeout != null)
            playerOneTimeout.cancelTimeout();
        if (playerTwoTimeout != null)
            playerTwoTimeout.cancelTimeout();
        if (quarantine != null)
            quarantine.cancelTimeout();
    }

    /**
     * Pauses all running timeouts -- this excludes the quarantine
     * 
     * @return True if there were timeouts to pause, false otherwise
     */
    public boolean pauseAllRunning() {
        if (playerOneTimeout == null) {
            return false;
        }
        boolean didPause = playerOneTimeout.pause();
        if (playerTwoTimeout == null) {
            return didPause;
        }
        if (playerTwoTimeout.pause()) {// this is done so that both pauses will be issued if necessary
            didPause = true;
        }
        return didPause;
    }

    /**
     * Resumes all paused timeouts -- this excludes the quarantine
     * 
     * @return True if there were timeouts to resume, false otherwise
     */
    public boolean resumeAllRunning() {
        boolean didResume = playerOneTimeout.resume();
        if (playerTwoTimeout.resume()) {// this is done so that both resumes will be issued if necessary
            didResume = true;
        }
        return didResume;
    }

}