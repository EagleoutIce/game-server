package de.uulm.team020;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Scanner;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.helper.DateHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.client.ClientShellRunner;
import de.uulm.team020.server.configuration.CommandLineParser;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.NttsController;

/**
 * Main class to launch the server.
 * <p>
 * It will use {@link CommandLineParser} to process commandline-arguments which
 * is based on {@link de.uulm.team020.parser.commandline.ArgumentParser}. You
 * can pass '{@code --defaults}' to load the default-configuration files located
 * and shipped with this jar in '{@code resources/defaults/json/}'. These files
 * are expected to be valid and checked by the Tests, so we don't have to do the
 * check at runtime. <i>IF someone violates this, the behaviour is considered to
 * be undefined.</i>
 * 
 * 
 * @author Florian Sihler
 * @version 1.3, 06/21/2020
 */
public class ServerMain {
    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    // i know :P
    public static final Object endServerRoutine = new Object();

    /**
     * Run to Start a new Server - which is quite nice
     * 
     * @param args configuration
     * @throws InterruptedException If the timer gets interrupted
     * @throws IOException          If there is an error reading the socket.
     */
    @SuppressWarnings("java:S2189")
    public static void main(final String[] args) throws InterruptedException, IOException {

        Configuration configuration = new Configuration();

        if (CommandLineParser.printErrors(CommandLineParser.parse(configuration, args)))
            return;

        final ServerConfiguration serverConfiguration = configuration.getServerConfig();
        if (serverConfiguration.runAsClient()) {
            ClientShellRunner.run();
            return;
        }

        Magpie.useRainbowForAll(serverConfiguration.magpieShouldUseRainbow());
        Magpie.mirrorToConsole(serverConfiguration.magpieToConsole());
        DateHelper.setZone(ZoneId.of(serverConfiguration.getTimezone()));
        magpie.writeInfo("Time check. Now is: " + DateHelper.now() + " (Zone: "
                + ZoneId.of(serverConfiguration.getTimezone()) + ")", "Main");

        while (true) { // May leave through exit :D
            runGameOnce(configuration, serverConfiguration);
            configuration = new Configuration(configuration).newSession();
        }

    }

    private static void runGameOnce(final Configuration configuration, final ServerConfiguration serverConfiguration)
            throws IOException, InterruptedException {
        // Dummy-Code to host an example server-connection
        final NttsController controller = new NttsController(configuration);
        controller.start();

        if (serverConfiguration.dropToCommandLine()) {
            runInteractiveInCommandLine(controller);
        } else {
            runNormalWithNoInteraction(controller);
        }

        magpie.writeInfo("Preparing next session... (25s cooldown)", "Main");
        controller.stop();
        Thread.sleep(25_500);
    }

    private static void runNormalWithNoInteraction(final NttsController controller)
            throws InterruptedException, IOException {
        try {
            synchronized (endServerRoutine) {
                while (controller.getConfiguration().getGamePhase().isBefore(GamePhaseEnum.END)) {
                    endServerRoutine.wait();
                    magpie.writeInfo("Notified End in Phase: " + controller.getConfiguration().getGamePhase(), "Main");
                }
                controller.getAuthor().dump("regular-game-over");
            }
        } finally {
            controller.stop();
        }
    }

    private static void runInteractiveInCommandLine(final NttsController controller)
            throws IOException, InterruptedException {
        final Scanner scanner = new Scanner(System.in);
        String line;
        do {
            System.out.println("Enter 'quit' to shut down server.");
            line = scanner.nextLine();
        } while (!line.equals("quit"));
        scanner.close();
        controller.getAuthor().dump("regular-game-over-it");
        controller.stop();
        System.exit(0);
    }
}
