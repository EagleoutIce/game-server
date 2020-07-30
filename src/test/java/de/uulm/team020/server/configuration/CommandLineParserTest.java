package de.uulm.team020.server.configuration;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.validation.SchemaProvider;
import de.uulm.team020.validation.ValidationReport;
import de.uulm.team020.validation.Validator;
import org.junit.jupiter.api.*;

import java.util.List;


/**
 * Validates the functionality of {@link CommandLineParser}
 * on the base of {@link Configuration#reset()}
 */
public class CommandLineParserTest {
    private Configuration configuration;

    @BeforeEach
    public void reset_config() {
        configuration = new Configuration();
    }

    public void every_test_reset() {
        Assertions.assertNotNull(configuration.getSessionId(), "There always has to be a sessionId");
        Assertions.assertEquals(7007, configuration.getPort(), "The default port is 7007");
        Assertions.assertNull(configuration.getCharacters(), "No characters by init");
        Assertions.assertNull(configuration.getMatchconfig(), "No matchconfig by init");
        Assertions.assertNull(configuration.getScenario(), "No scenario by init");
    }

    @Test @Tag("Core") @Order(1)
    @DisplayName("[Commandline] Validate config for no input")
    public void test_withNoSettings() {
        every_test_reset();
        List<String> feedback = CommandLineParser.parse(configuration, new String[] {});
        Assertions.assertFalse(feedback.isEmpty(), "There should be errors");
        Assertions.assertEquals(6, feedback.size(), "There should be 6 errors (3 mandatory args)");
    }

    @Test @Tag("Core") @Order(2)
    @DisplayName("[Commandline] Validate the use of --defaults")
    public void test_withDefaults() {
        every_test_reset();
        List<String> feedback = CommandLineParser.parse(configuration, new String[] {"--defaults"});
        ValidationReport report;

        Assertions.assertTrue(feedback.isEmpty(), "There should be no errors");
        Assertions.assertEquals(7007, configuration.getPort(), "The default port is 7007");
        Assertions.assertNotNull(configuration.getCharacters(), "There should be characters now...");
        report = Validator.validateObject(CharacterInformation.arrayToJson(configuration.getCharacters()), SchemaProvider.CHARACTERS_SCHEMA);
        Assertions.assertTrue(report.isValid(), "There should be no errors in the validation for the characters");
        Assertions.assertNotNull(configuration.getMatchconfig(), "There should be a valid matchconfig now...");
        report = Validator.validateObject(configuration.getMatchconfig().toJson(), SchemaProvider.MATCHCONFIG_SCHEMA);
        Assertions.assertTrue(report.isValid(), "There should be no errors in the validation for the matchconfig");
        Assertions.assertNotNull(configuration.getScenario(), "There should be a valid scenario now..");
        report = Validator.validateObject(configuration.getScenario().toJson(), SchemaProvider.SCENARIO_SCHEMA);
        Assertions.assertTrue(report.isValid(), "There should be no errors in the validation for the scenario");
    }

    // Other tests may follow

}