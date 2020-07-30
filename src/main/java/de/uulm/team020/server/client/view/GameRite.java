package de.uulm.team020.server.client.view;

import java.io.IOException;
import java.util.Optional;

import de.uulm.team020.server.client.console.ConsoleStoryDialog;
import de.uulm.team020.server.client.console.story.StoryFeedback;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.controller.UserSessionController;
import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;

/**
 * This rite will handle everything to take the full advantage of the
 * {@link ConsoleStoryDialog}
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class GameRite extends AbstractScreenOwner {

    private static final String SESSION_RITE = "SessionRite";

    private final UserSessionController sessionController;

    /**
     * Construct the connection rite for may-be recoveries
     * 
     * @param controller        The main controller hosting this client session
     * @param sessionController The session controller to use for may recovery
     */
    public GameRite(final ServerClientController controller, final UserSessionController sessionController) {
        super(controller, SESSION_RITE);
        this.sessionController = sessionController;
    }

    @Override
    public void onInit() {
        // nothing to be done
    }

    @Override
    public void run() throws IOException {
        own();

        // Own forever :)
        ConsoleStoryDialog storyDialog = new ConsoleStoryDialog(controller, controller.getSessionData().getUserName());
        do {
            Optional<StoryFeedback> feedback;
            // request a story line until valid
            do {
                feedback = storyDialog.prompt("Storyline");
            } while (!feedback.isPresent());

            StoryFeedback got = feedback.get();
            if (got.hasContainer()) { // Send a message!
                getWriter().renderln("@|italic Sending: " + got.getContainer() + "|@");
                controller.send(got.getContainer());
            }
        } while (true);

        // tryFree()
    }
}