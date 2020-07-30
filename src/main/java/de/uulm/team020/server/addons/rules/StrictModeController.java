package de.uulm.team020.server.addons.rules;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.Configuration;

/**
 * There are some standards or rules that are 'recommended' or not necessary for
 * a working communication between systems. Therefore the
 * {@link de.uulm.team020.server.configuration.CommandLineParser} offers the
 * <code>--strict</code> mode which enforces any of these rules to be executed
 * and therefore to throw an Error instead of just displaying them.
 * <p>
 * It uses {@link Configuration#isStrictMode()} to determine if there is an
 * error that should be thrown or not.
 * 
 * @author Florian Sihler
 * @version 1.0, 03/26/2020
 */
public class StrictModeController {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    private static Configuration configuration = null;

    // Hide the default one
    private StrictModeController() {
    }

    public static void setConfiguration(final Configuration configuration) {
        StrictModeController.configuration = configuration;
    }

    /**
     * Will give a problem to the strict-mode-controller
     * 
     * @param type    type of the problem
     * @param message the message, giving (hopefully) more specific information
     */
    public static void mayCriticize(final StrictModeTypeEnum type, final String message) {
        final StrictModeException ex = new StrictModeException(type, message);
        if (strictMode()) {
            throw ex;
        } else {
            magpie.writeExceptionShort(ex, "Strict-Mode disabled, the Error is therefore not considered stop-worthy");
        }
    }

    private static boolean strictMode() {
        return configuration != null && configuration.isStrictMode();
    }

    /**
     * Validates the scenario-name
     * 
     * @param scenarioPath the path to the scenario file, can be internal as well
     */
    public static void criticizeScenarioPattern(final String scenarioPath) {
        if (strictMode() && !scenarioPath.endsWith(".scenario"))
            mayCriticize(StrictModeTypeEnum.UNKNOWN_TYPE_EXTENSION,
                    "The standard agrees on naming scenario-files with the '.scenario'-Suffix (" + scenarioPath + ")");
    }

    /**
     * Validates the matchconfig-name
     * 
     * @param matchconfigPath the path to the matchconfig file, can be internal as
     *                        well
     */
    public static void criticizeMatchconfigPattern(final String matchconfigPath) {
        if (!matchconfigPath.endsWith(".match"))
            mayCriticize(StrictModeTypeEnum.UNKNOWN_TYPE_EXTENSION,
                    "The standard agrees on naming matchconfig-files with the '.match'-Suffix (" + matchconfigPath
                            + ")");
    }

    /**
     * Validates the character description-nam
     * 
     * @param charactersPath the path to the character description file
     */
    public static void criticizeCharacterDescriptionPattern(final String charactersPath) {
        if (!charactersPath.endsWith(".json"))
            mayCriticize(StrictModeTypeEnum.UNKNOWN_TYPE_EXTENSION,
                    "The standard agrees on naming character-files with the '.json'-Suffix (" + charactersPath + ")");
    }

    // Maybe explicit null in messages
}