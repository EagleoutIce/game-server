package de.uulm.team020.server.client.view;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.regex.Matcher;

import de.uulm.team020.server.client.console.ConsoleRegexDialog;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.controller.UserSessionController;
import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;

/**
 * This rite will be only established if there is a server connection to be made
 * it will start initializing the web socket connection. Never call connection
 * rite <i>after</i> a successful recovery rite!
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class ConnectionRite extends AbstractScreenOwner {

    private static final String SESSION_RITE = "SessionRite";

    private final UserSessionController sessionController;

    /**
     * Construct the connection rite for may-be recoveries
     * 
     * @param controller        The main controller hosting this client session
     * @param sessionController The session controller to use for may recovery
     */
    public ConnectionRite(final ServerClientController controller, final UserSessionController sessionController) {
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
        final ConsoleWriter writer = getWriter();
        // connect to
        writer.renderln(
                "You are now able connect to a server. Enter the wanted target.\nWrite: @|italic localhost|@ to connect to a server running on this machine.");
        ConsoleRegexDialog serverDialog = new ConsoleRegexDialog(
                "^(wss?:\\/\\/)?(?<adress>[^:]+)(:(?<port>\\d{1,5}))?$");
        URI maybeUri = null;
        do { // this is funny isn't it?
            Matcher entered = serverDialog.prompt("Server");
            maybeUri = validateURI(writer, entered.group("adress"),
                    Objects.requireNonNullElse(entered.group("port"), "7007"));
        } while (maybeUri == null);

        controller.getSessionData().setServerTarget(maybeUri);
        // The main controller will start the real connection afterwards
        tryFree();
    }

    private URI validateURI(ConsoleWriter writer, String hostname, String port) {
        final String uri = "ws://" + hostname + ":" + port;
        magpie.writeInfo("Trying to validate: '" + uri + "'", "validate");

        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            magpie.writeException(e, "valid");
            writer.renderln("@|red The tried uri: '" + uri + "' is not valid!|@");
            return null;
        }
    }
}