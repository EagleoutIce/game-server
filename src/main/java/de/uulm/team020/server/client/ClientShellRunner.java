package de.uulm.team020.server.client;

import java.io.IOException;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.client.controller.ServerClientController;

/**
 * This class may be used to run a new client instance. There is only one
 * allowed (per console)
 * 
 * @author Florian Sihler
 * @version 1.0, 05/23/2020
 * 
 * @since 1.1
 */
public class ClientShellRunner {
    /** Logging-Reference, will be shared by all Server-components */
    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    /**
     * Start a new client session for the current terminal
     * 
     * @throws IOException If something goes wrong obtaining the terminal
     */
    public static void run() throws IOException {
        final ServerClientController controller = new ServerClientController();
        magpie.writeInfo("Starting client shell runner...", "Start");
        controller.start();
    }

    /**
     * Only call from outside -- basically calls {@link #run()}
     * 
     * @param args ignored
     * 
     * @throws IOException If something goes wrong obtaining the terminal
     */
    public static void main(final String[] args) throws IOException {
        ClientShellRunner.run();
    }
}
