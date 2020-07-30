package de.uulm.team020.server.client.view;

import java.io.IOException;

import de.uulm.team020.server.client.console.ConsoleSimpleDialog;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;
import de.uulm.team020.server.configuration.Configuration;

/**
 * Let the user log in to provide recovery information based on the name. This
 * allows multiple instances to be run on the same machine without the recovery
 * system getting corrupted.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class LoginRite extends AbstractScreenOwner {

    private static final String LOGIN_RITE = "LoginRite";

    /**
     * Construct the login rite asking for user-name
     * 
     * @param controller The main controller hosting this client session
     */
    public LoginRite(final ServerClientController controller) {
        super(controller, LOGIN_RITE);
    }

    @Override
    public void onInit() {
        // we do not have to do anything
    }

    @Override
    public void run() throws IOException {
        own();

        // present information
        final ConsoleWriter writer = getWriter();
        writer.clearScreen().renderln("Welcome to the client mode of the @|bold team020|@ server.");
        writer.renderln(
                "This mode was written just for fun and may not be completed.\nAuthor: @|bold Florian Sihler|@, Version: "
                        + Configuration.VERSION);
        writer.renderln(
                "@|italic Whenever you get asked something you may type|@ @|magenta,italic :help|@ @|italic to get further information.|@");
        writer.renderln("\nAt first, you are allowed to pick your username");
        // Ask for the Username
        ConsoleSimpleDialog dialog = new ConsoleSimpleDialog(str -> str != null && str.length() > 2,
                "Your Name must be at least two characters long. This is the name you use when connecting to a server.");
        final String enteredName = dialog.prompt("Username");
        controller.getSessionData().setUserName(enteredName);
        tryFree();
    }

}