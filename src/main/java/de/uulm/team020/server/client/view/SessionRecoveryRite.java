package de.uulm.team020.server.client.view;

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import de.uulm.team020.server.client.console.ConsoleBooleanDialog;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.controller.UserSessionController;
import de.uulm.team020.server.client.data.UserSessionData;
import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;

/**
 * This rite may initiate the recovery process if the there is recovery
 * information for that. If there is no recovery information this rite will
 * simply pass.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class SessionRecoveryRite extends AbstractScreenOwner {

    private static final String SESSION_RITE = "SessionRite";

    private final UserSessionController sessionController;
    private boolean doneRecovery;

    /**
     * Construct the session rite for may-be recoveries
     * 
     * @param controller        The main controller hosting this client session
     * @param sessionController The session controller to use for may recovery
     */
    public SessionRecoveryRite(final ServerClientController controller, final UserSessionController sessionController) {
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
        // is there recovery information?
        doneRecovery = mayRecover();
        tryFree();
    }

    private boolean mayRecover() {
        // first: get em:
        try {
            UserSessionData sessionData = sessionController.retrieveData();
            if (sessionData == null || sessionData.getClientId() == null || sessionData.getSessionId() == null) {
                magpie.writeWarning("The recovery-data for: " + sessionController.getName()
                        + " was tossed for being not valid: " + sessionData, SESSION_RITE);
                return false; //
            }
            if (!sessionData.getUserName().equals(sessionController.getName())) {
                magpie.writeWarning("The recovery-data for: " + sessionController.getName()
                        + " was tossed for different names: " + sessionData, SESSION_RITE);
                return false;
            }
            // got recovery data - ask for recovery
            ConsoleWriter writer = getWriter();
            writer.renderln("Found recovery-data for the client: @|italic " + sessionData + "|@");
            if (mayBeOld(sessionData.getCreationDate())) {
                writer.renderln(
                        "The data seems to be old, probably too old to reconnect. Should it be recovered anyways?");
            } else {
                writer.renderln("Should it be recovered?");
            }
            ConsoleBooleanDialog recoverDialog = new ConsoleBooleanDialog();
            if (recoverDialog.prompt("Recover?")) {
                writer.renderln("Recovery...");
                controller.updateSessionData(sessionData);
            } else {
                writer.renderln("Ok. Do you want the recovery-data to be erased?");
                if (recoverDialog.prompt("Erase?")) {
                    if (sessionController.eraseRecoveryData()) {
                        writer.renderln("Recovery-Data erased!");
                    } else {
                        writer.renderln("Recovery failed, probably it already has been erased or got lost.");
                    }
                } else {
                    writer.renderln("Will not erase recovery-data");
                }
                return false;
            }

            // check if the recovery data is too old

        } catch (IOException ex) {
            magpie.writeException(ex, "recovery");
        }
        return true;
    }

    private boolean mayBeOld(Date recoveryDate) {
        // We use date here, as this date will never get poisoned by different timezones
        // (love ._.)
        return ChronoUnit.MINUTES.between(recoveryDate.toInstant(), new Date().toInstant()) > 5;
    }

    /**
     * Checks if recovery was preformed
     * 
     * @return True if recovered, false otherwise
     */
    public boolean performedRecovery() {
        return doneRecovery;
    }

}