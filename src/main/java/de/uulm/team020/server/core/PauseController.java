package de.uulm.team020.server.core;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.messages.GamePauseMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.game.phases.choice.DraftingPhaseController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;

/**
 * This class is used by the {@link NttsController} and handles the paused-state
 * of the controller
 * 
 * @author Florian Sihler
 * @version 1.0, 05/13/2020
 * 
 * @since 1.1
 */
public class PauseController {

    private static final String SERVER_TEXT = "Server";
    private static final String HANDLER_TEXT = "Handler";

    private static IMagpie magpie = Magpie.createMagpieSafe(SERVER_TEXT);

    private final NttsController controller;

    private boolean playerOneResume;
    private boolean playerTwoResume;

    /**
     * Create a new pause controller to serve
     * 
     * @param controller The main controller this one shall serve
     */
    public PauseController(NttsController controller) {
        this.controller = controller;
        resetResumes();
    }

    /**
     * This will just reset both resume-markers
     */
    public void resetResumes() {
        playerOneResume = playerTwoResume = false;
    }

    /**
     * This will just set the resume-marker for the given connection to the given
     * value -- will do nothing if this is no player connection.
     * 
     * @param value      The desired value for the marker
     * @param connection The connection that requested the resume
     * 
     * @return True if the marker was cleared, false otherwise
     */
    public boolean setResume(final NttsClientConnection connection, final boolean value) {
        switch (connection.getGameRole()) {
            case PLAYER_ONE:
                playerOneResume = value;
                return true;
            case PLAYER_TWO:
                playerTwoResume = value;
                return true;
            default:
                return false;
        }
    }

    /**
     * May be used to resume after the end of a pause -- this will respect the
     * Server-config
     * 
     * @param connection The connection that requested the resume
     * 
     * @return True if performed, false otherwise
     */
    public boolean resumeByPauseEnd(final NttsClientConnection connection) {
        final Configuration configuration = controller.getConfiguration();
        if (!configuration.getServerConfig().resumeByBoth()) {
            // if not desired to require both to resume -- do direct
            return performResumeByPauseEnd(connection, configuration);
        }
        // set marker to true:
        setResume(connection, true);
        if (playerOneResume && playerTwoResume) {
            return performResumeByPauseEnd(connection, configuration);
        }
        // otherwise:
        return false;
    }

    private boolean performResumeByPauseEnd(final NttsClientConnection connection, final Configuration configuration) {
        final DraftingPhaseController draftingPhaseController = controller.getDraftingPhaseController();
        final MainGamePhaseController mainGamePhaseController = controller.getMainGamePhaseController();

        if (configuration.getGamePhase() != GamePhaseEnum.GAME_PAUSED)
            return false; // Not in fitting phase

        final GamePauseMessage gamePauseMessage = new GamePauseMessage(null, false, false);
        controller.broadcast(gamePauseMessage);

        configuration.shiftPhase(controller.getBufferedGamePhase());

        configuration.getTimeoutController().cancelPauseTimeoutForPauser(connection);

        // If we are back to a handler phase:
        // I know this could be linked to the 'phase change'. But it shouldn't. It helps
        // me catching logic errors
        // if it doesn't resume in some situations.
        if (configuration.getGamePhase() == GamePhaseEnum.ITEM_ASSIGNMENT) {
            magpie.writeInfo("Drafting Phase resumed for: " + draftingPhaseController.resume(), HANDLER_TEXT);
        } else if (configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_PLAY
                || configuration.getGamePhase() == GamePhaseEnum.MAIN_GAME_READY) {
            // Resume main game
            magpie.writeInfo("Main Game Phase resumed for: " + mainGamePhaseController.resume(), HANDLER_TEXT);
        }

        // reset Resume-flags
        resetResumes();
        return true;
    }

    /**
     * @return the playerOneResume
     */
    public boolean playerOneResume() {
        return playerOneResume;
    }

    /**
     * @return the playerTwoResume
     */
    public boolean playerTwoResume() {
        return playerTwoResume;
    }

}