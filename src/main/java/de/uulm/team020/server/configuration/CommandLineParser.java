package de.uulm.team020.server.configuration;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.everit.json.schema.Schema;

import de.uulm.team020.GameData;
import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.parser.commandline.ArgumentParser;
import de.uulm.team020.server.addons.rules.StrictModeController;
import de.uulm.team020.validation.GameDataGson;
import de.uulm.team020.validation.SchemaProvider;
import de.uulm.team020.validation.ValidationReport;
import de.uulm.team020.validation.Validator;

/**
 * Configures and executes cli-arguments based on the ArgumentParser taken from
 * game-data.
 * 
 * @author Florian Sihler
 * @version 1.1b, 03/20/2020
 */
public class CommandLineParser {

    private static final String CLP_TEXT = "Parser";

    /** Logging-Reference, will be shared by all Server-components */
    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    /** Parser to be used */
    private static ArgumentParser parser = null;

    // Hide the default one
    private CommandLineParser() {
    }

    /**
     * Initializes {@link #parser} with all supported Arguments
     */
    private static void init() {
        parser = new ArgumentParser(Configuration.SERVER_NAME, true);

        Option charset = new Option("c", "config-charset", true, "Sets the path to the character-configuration");
        charset.setArgName("FILE");

        Option matchconfig = new Option("m", "config-match", true, "Sets the path to the Match-configuration");
        matchconfig.setArgName("FILE");

        Option scenario = new Option("s", "config-scenario", true, "Sets the path to the scenario-configuration");
        scenario.setArgName("FILE");

        Option port = new Option("p", "port", true,
                "Sets the internal port-number the server is operating on. Will default to 7007. Port should be between 1024 and 65535.");
        port.setArgName("PORT");

        Option verbosity = new Option("v", "verbosity", true,
                "Sets the verbosity of the created logfiles has to be between 0 and 5.");
        verbosity.setArgName("0-5");

        Option freshness = new Option("f", "freshness-threshold", true,
                "Sets the freshness-threshold in seconds (default is disabled). Negative values mean disabled.");
        freshness.setArgName("seconds");

        Option version = new Option("V", "version", false, "Prints the version-information.");

        Option defaults = Option.builder().longOpt("defaults")
                .desc("Automatically sets (internal) defaults for all Configurations.").build();

        Option strict = Option.builder().longOpt("strict")
                .desc("Let all rules by the standard throw an error instead of a warning.").build();

        Option serverConfig = Option.builder().longOpt("server-config").hasArg()
                .desc("Pass another serverConfiguration to be used.").build();
        serverConfig.setArgName("FILE");

        parser.hasX().addOption(charset).addOption(matchconfig).addOption(scenario).addOption(verbosity).addOption(port)
                .addOption(version).addOption(defaults).addOption(strict).addOption(freshness).addOption(serverConfig);
    }

    /**
     * Parse and process Command-Line-Input
     * 
     * @param configuration The config to populate
     * @param args          input from the commandline
     * 
     * @return list of errors, if they occurred
     * 
     * @see #printErrors(List)
     */
    public static List<String> parse(Configuration configuration, String[] args) {
        if (parser == null)
            init();
        List<String> errors = new LinkedList<>();
        try {
            CommandLine clp = parser.parse(args);

            if (clp == null) {
                errors.add("Commandline invalid");
                return errors;
            }

            if (clp.hasOption("V")) {
                printVersion();
                errors.add("Server will only start if not supplied with '-V/--version'.");
                return errors;
            }

            if (clp.hasOption("defaults")) {
                configuration.setLoadDefaults();
            }

            interpretConfigurationOptions(configuration, errors, clp);

        } catch (ParseException ex) {
            magpie.writeException(ex, CLP_TEXT);
            errors.add(ex.getLocalizedMessage());
        }
        return errors;
    }

    private static void interpretConfigurationOptions(Configuration configuration, List<String> errors,
            CommandLine clp) {
        configuration.setStrictMode(clp.hasOption("strict"));

        if (!clp.hasOption("help")) {
            // to assure minvals
            errors.addAll(parseServerConfig(configuration, clp.getOptionValue("server-config")));
            errors.addAll(configuration.getServerConfig().xOptionInject(clp.getOptionValues("x")));

            List<String> cErrors = handleConfiguration(configuration, clp.getOptionValue("c"),
                    "defaults/json/default-characters.json", SchemaProvider.CHARACTERS_SCHEMA,
                    CommandLineParser::handlerCharset, StrictModeController::criticizeCharacterDescriptionPattern);
            if (!cErrors.isEmpty()) {
                errors.add("Please supply valid character-configuration via '-c <PATH>' (or use '--defaults')");
                errors.addAll(cErrors);
            }

            List<String> sErrors = handleConfiguration(configuration, clp.getOptionValue("s"),
                    "defaults/json/default-scenario.scenario", SchemaProvider.SCENARIO_SCHEMA,
                    CommandLineParser::handlerScenario, StrictModeController::criticizeScenarioPattern);
            if (!sErrors.isEmpty()) {
                errors.add("Please supply valid scenario via '-s <PATH>' (or use '--defaults')");
                errors.addAll(sErrors);
            }

            List<String> mErrors = handleConfiguration(configuration, clp.getOptionValue("m"),
                    "defaults/json/default-matchconfig.match", SchemaProvider.MATCHCONFIG_SCHEMA,
                    CommandLineParser::handlerMatchconfig, StrictModeController::criticizeMatchconfigPattern);
            if (!mErrors.isEmpty()) {
                errors.add("Please supply valid matchconfig via '-m <PATH>' (or use '--defaults')");
                errors.addAll(mErrors);
            }

            errors.addAll(parsePort(configuration, clp.getOptionValue("p")));
            errors.addAll(parseVerbosity(clp.getOptionValue("v")));
            errors.addAll(parseFreshness(configuration, clp.getOptionValue("f")));

        } else {
            // Provide additional help
            errors.add("Server will only start if not supplied with '-h/--help'.");
            errors.add("If you are interested, these are all valid x-options.  'True' is considered everything in "
                    + ServerConfiguration.TRUTH_VALUES);

            configuration.getServerConfig().getXConsumers().entrySet().stream() // Stream all x consumers
                    .sorted(Comparator.comparing(Map.Entry::getKey)) // Sort them by key
                    // Foreach of them, add them to the list
                    .forEach(entry -> errors
                            .add(String.format("   - %-17s: %s", entry.getKey(), entry.getValue().getKey())));

        }
    }

    /**
     * Prints all errors generated by {@link #parse(Configuration, String[])},
     * nicely formatted.
     * 
     * @param errors errors from by {@link #parse(Configuration, String[])}
     * @return true if there were errors, false otherwise
     */
    public static boolean printErrors(List<String> errors) {
        if (errors.isEmpty())
            return false;

        errors.forEach(System.err::println);
        return true;
    }

    /**
     * Loads a choosable configuration, as lon as it upholds against the supplied
     * schema
     *
     * @param configuration    the configuration to use
     * @param target           the configured path to load from
     * @param defaultPath      path to load if defaults are allowed
     * @param schema           the Schema to be used to validate
     * @param handler          the Handler to call if Loading was successful
     * @param strictController will connect the strict-controller
     * @return true if loading was successful, false otherwise
     */
    private static List<String> handleConfiguration(Configuration configuration, String target, String defaultPath,
            Schema schema, BiConsumer<Configuration, String> handler, Consumer<String> strictController) {
        List<String> errors = new LinkedList<>();
        InputStream is = null;
        Reader reader = null;

        if (target == null || target.isBlank()) {
            if (configuration.loadDefaults()) {
                magpie.writeInfo("Loading default '" + defaultPath + "'", CLP_TEXT);
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultPath);
                reader = new InputStreamReader(Objects.requireNonNull(is));
            } else {
                errors.add("  - Argument missing");
                return errors;
            }
            strictController.accept(defaultPath);
        }
        // Note: if we have loaded defaults, we will load them from an internal file,
        // therefore we wont bother this anymore
        else if (!new File(target).isFile()) {
            errors.add("  - No or nonexisting File supplied");
            return errors;
        } else {
            strictController.accept(target);
        }

        if (reader == null) {
            try {
                reader = new FileReader(target);
            } catch (FileNotFoundException ex) { // Should have been covered, just to be save
                magpie.writeException(ex, CLP_TEXT);
            }
        }
        // Lets try to get the file
        try (BufferedReader br = new BufferedReader(Objects.requireNonNull(reader))) {
            readAndProcessConfigurationData(configuration, schema, handler, errors, br);
        } catch (Exception ex) {
            magpie.writeException(ex, CLP_TEXT);
            errors.add("  - " + ex.getLocalizedMessage());
        } finally {
            closeStream(reader);
            closeStream(is);
        }
        return errors;
    }

    private static void readAndProcessConfigurationData(Configuration configuration, Schema schema,
            BiConsumer<Configuration, String> handler, List<String> errors, BufferedReader br) {
        String json = br.lines().parallel().collect(Collectors.joining(" "));

        // Validate the json-String:
        ValidationReport report = Validator.validateObject(json, schema);
        if (report.isValid()) {
            handler.accept(configuration, json);
        } else {
            magpie.writeError("Document is not valid!", CLP_TEXT);
            errors.add("  - The supplied Document seems to be invalid!");
            List<String> reasons = report.getReasons();
            for (String reason : reasons) {
                errors.add("    > " + reason);
            }
        }
    }

    private static void closeStream(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ex) {
            magpie.writeException(ex, CLP_TEXT);
        }
    }

    /**
     * Handles Character-Description Data
     *
     * @param configuration the configuration to use
     * @param json          the JSON-Data
     * 
     * @see #handleConfiguration(Configuration, String, String, Schema, BiConsumer,
     *      Consumer)
     */
    private static void handlerCharset(Configuration configuration, String json) {
        CharacterDescription[] characters = GameDataGson.fromJson(json, CharacterDescription[].class);

        // Populate the Configuration.characters
        try {
            configuration.populateCharacters(characters);
        } catch (ConfigurationException ex) {
            magpie.writeException(ex, CLP_TEXT);
        }
    }

    /**
     * Handles Scenario Data
     *
     * @param configuration the configuration to use
     * @param json          the JSON-Data
     *
     * @see #handleConfiguration(Configuration, String, String, Schema, BiConsumer,
     *      Consumer)
     */
    private static void handlerScenario(Configuration configuration, String json) {
        Scenario scenario = GameDataGson.fromJson(json, Scenario.class);

        try {
            configuration.setScenario(scenario);
        } catch (ConfigurationException ex) {
            magpie.writeException(ex, CLP_TEXT);
        }
    }

    /**
     * Handles Matchconfig Data
     *
     * @param configuration the configuration to use
     * @param json          the JSON-Data
     *
     * @see #handleConfiguration(Configuration, String, String, Schema, BiConsumer,
     *      Consumer)
     */
    private static void handlerMatchconfig(Configuration configuration, String json) {
        Matchconfig matchconfig = GameDataGson.fromJson(json, Matchconfig.class);

        try {
            configuration.setMatchconfig(matchconfig);
        } catch (ConfigurationException ex) {
            magpie.writeException(ex, CLP_TEXT);
        }
    }

    /**
     * Parses a Port and validates it before assignment
     *
     * @param configuration the configuration to use
     * @param port          the desired port number
     * 
     * @return errors, if they occurred
     */
    private static List<String> parsePort(Configuration configuration, String port) {
        List<String> errors = new LinkedList<>();
        if (port == null || port.isBlank()) {
            magpie.writeInfo("Using default (internal) port: " + configuration.getPort(), CLP_TEXT);
            configuration.setPort(7007);
            return errors;
        }

        try {
            int iPort = Integer.parseInt(port);
            if (iPort < 0) {
                errors.add("Port numbers must be positive (" + iPort + ")");
                return errors;
            } else if (iPort == 0) {
                errors.add(
                        "Random port numbers are not supported, please select one greater than zero (" + iPort + ").");
                return errors;
            } else if (iPort < 256) {
                errors.add("Port numbers below 256 are reserved for well-known service and can not be used (" + iPort
                        + ").");
                return errors;
            } else if (iPort > 65535) {
                errors.add("Port numbers above 65535 are not supported to preserve compatibility (" + iPort + ").");
                return errors;
            }
            if (iPort < 1024)
                magpie.writeWarning("The desired port number (" + iPort
                        + ") is below 1024 and may require root-rights. As this is may be the desired behavior, it will not be forbidden.",
                        CLP_TEXT);
            configuration.setPort(iPort);
            magpie.writeInfo("Using Port: '" + iPort + "'", "Config");
        } catch (NumberFormatException ex) {
            magpie.writeExceptionShort(ex, CLP_TEXT);
            errors.add("The port number '" + port + "' isn't a number.");
        }

        return errors;
    }

    /**
     * Parses a Verbosity and validates it before assignment
     * 
     * @param verbosity the desired verbosity
     * 
     * @return errors, if they occurred
     */
    private static List<String> parseVerbosity(String verbosity) {
        List<String> errors = new LinkedList<>();
        if (verbosity == null || verbosity.isBlank()) {
            magpie.writeInfo("Using default verbosity.", CLP_TEXT);
            return errors;
        }

        try {
            int iVerbosity = Integer.parseInt(verbosity);
            switch (iVerbosity) {
                case 1:
                    Magpie.setDefaultLevel(Level.SEVERE);
                    break;
                case 2:
                    Magpie.setDefaultLevel(Level.WARNING);
                    break;
                case 3:
                    Magpie.setDefaultLevel(Level.INFO);
                    break;
                case 4:
                    Magpie.setDefaultLevel(Level.FINE);
                    break;
                case 0:
                case 5:
                    Magpie.setDefaultLevel(Level.ALL);
                    break;
                default:
                    errors.add("Verbosity has to be between '0' and '5'.");
            }
        } catch (NumberFormatException ex) {
            magpie.writeExceptionShort(ex, CLP_TEXT);
            errors.add("The verbosity'" + verbosity + "' is not a valid number.");
        }

        return errors;
    }

    /**
     * Parses the freshness
     *
     * @param freshness the desired freshness
     *
     * @return errors, if they occurred
     */
    private static List<String> parseFreshness(Configuration configuration, String freshness) {
        List<String> errors = new LinkedList<>();
        if (freshness == null || freshness.isBlank()) {
            magpie.writeInfo("Using default freshness.", CLP_TEXT);
            return errors;
        }

        try {
            int iFreshness = Integer.parseInt(freshness);
            configuration.setFreshnessThresholdInS(iFreshness);
        } catch (NumberFormatException ex) {
            magpie.writeExceptionShort(ex, CLP_TEXT);
            errors.add("The freshness'" + freshness + "' is not a valid number.");
        }

        return errors;
    }

    /**
     * Parses the server configuration. Fields in the server configuration!
     *
     * @param path the path to load from
     *
     * @return errors, if they occurred
     */
    private static List<String> parseServerConfig(Configuration configuration, String path) {
        List<String> errors = new LinkedList<>();
        if (path == null || path.isBlank()) {
            magpie.writeInfo("Using default server-config.", CLP_TEXT);
            return errors;
        } else if (!new File(path).isFile()) {
            errors.add("There is an error with the server-config you supplied.");
            errors.add("  - No or nonexisting File supplied");
            return errors;
        }
        FileReader reader;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException ex) { // Should have been covered, just to be save
            magpie.writeException(ex, CLP_TEXT);
            errors.add("  - " + ex.getMessage());
            return errors;
        }
        // Lets try to get the file
        try (BufferedReader br = new BufferedReader(Objects.requireNonNull(reader))) {
            String json = br.lines().parallel().collect(Collectors.joining(" "));
            ServerConfiguration serverConfiguration = GameDataGson.fromJson(json, ServerConfiguration.class);
            configuration.setServerConfiguration(serverConfiguration);
        } catch (Exception ex) {
            magpie.writeException(ex, CLP_TEXT);
            errors.add("  - " + ex.getLocalizedMessage());
        } finally {
            closeStream(reader);
        }
        return errors;
    }

    /**
     * Prints Version-Information
     */
    private static void printVersion() {
        System.out.format("game-server v.%s (%s)%n", Configuration.VERSION / 1000D,
                Configuration.IS_STABLE ? "stable" : "unstable");
        System.out.format("  - game-data v.%s (%s)%n", GameData.VERSION / 1000D,
                GameData.IS_STABLE ? "stable" : "unstable");
        System.out.println("This is the server written by Team020.");
    }
}