package de.uulm.team020.server.configuration;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.CharacterDescription;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.datatypes.enumerations.FieldStateEnum;
import de.uulm.team020.validation.GameDataGson;

/**
 * Validates the functionality of the {@link Configuration}-Class
 */
public class ConfigurationTest {

    private Configuration configuration;

    private static final Matchconfig DEFAULT_MATCHCONFIG = new Matchconfig(1, 1, 0.25, 4, 0.125, 2, 6, 2, 1, 2, 3, 0.35,
            0.64, 0.35, 0.25, 6, 0.65, 0.25, 0.35, 0.12, 12, 3, 3, 5, 15, 6, 8, 4, 320, 20);

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

    @Test
    @Tag("Core")
    @Order(1)
    @DisplayName("[Defaults] Validate the default settings")
    public void test_defaultSettingsForReset() {
        every_test_reset();
    }

    // It should be re-allowed to populate the characters after reset
    @RepeatedTest(3)
    @Tag("Core")
    @Order(2)
    @DisplayName("[Configuration] Validate character population")
    public void test_characterPopulation() throws IOException {
        every_test_reset();
        // load
        String charactersJson = GameDataGson.loadInternalJson("defaults/json/default-characters.json");
        CharacterDescription[] characters = GameDataGson.fromJson(charactersJson, CharacterDescription[].class);

        // populate 1
        configuration.populateCharacters(characters);

        // first check -> we will do the description equals to prevent the UUID that was
        // generated from causing errors
        CharacterInformation[] setCharacters = configuration.getCharacters();

        for (int i = 0; i < setCharacters.length; i++) {
            Assertions.assertEquals(characters[i], setCharacters[i], "Characters should be as set (in: " + i + ")");
            Assertions.assertNotNull(setCharacters[i].getId(), "Every char should now have a UUID (in: " + i + ")");
        }

        // populate 2
        Assertions.assertThrows(ConfigurationException.class,
                () -> configuration.populateCharacters(new CharacterDescription[] {}),
                "It should not be allowed to change the characters");

        // second check, should be the same as before
        CharacterInformation[] nextSetCharacters = configuration.getCharacters();

        for (int i = 0; i < setCharacters.length; i++) {
            Assertions.assertEquals(characters[i], nextSetCharacters[i], "Characters should be as set (in: " + i + ")");
            Assertions.assertEquals(setCharacters[i].getId(), nextSetCharacters[i].getId(),
                    "UUIDs should have staid the same (in: " + i + ")");
        }
    }

    // It should be re-allowed to set the scenario after reset
    @RepeatedTest(3)
    @Tag("Core")
    @Order(3)
    @DisplayName("[Configuration] Validate scenario set")
    public void test_scenarioSet() throws IOException {
        every_test_reset();
        // load
        String scenarioJson = GameDataGson.loadInternalJson("defaults/json/default-scenario.scenario");
        Scenario scenario = GameDataGson.fromJson(scenarioJson, Scenario.class);

        // populate 1
        configuration.setScenario(scenario);

        // first check
        Scenario getScenario = configuration.getScenario();

        Assertions.assertEquals(scenario, getScenario, "Scenario should be as set.");

        // populate 2
        Assertions.assertThrows(ConfigurationException.class,
                () -> configuration.setScenario(new Scenario(new FieldStateEnum[][] {})),
                "It should not be allowed to change the scenario");

        // second check, should be the same as before
        Assertions.assertEquals(scenario, getScenario, "Scenario should be as set before.");
    }

    // It should be re-allowed to set the matchconfig after reset
    @RepeatedTest(3)
    @Tag("Core")
    @Order(4)
    @DisplayName("[Configuration] Validate matchconfig set")
    public void test_matchconfigSet() throws IOException {
        every_test_reset();
        // load
        String matchconfigJson = GameDataGson.loadInternalJson("defaults/json/default-matchconfig.match");
        Matchconfig matchconfig = GameDataGson.fromJson(matchconfigJson, Matchconfig.class);

        // populate 1
        configuration.setMatchconfig(matchconfig);

        // first check
        Matchconfig getMatchconfig = configuration.getMatchconfig();

        Assertions.assertEquals(matchconfig, getMatchconfig, "Matchconfig should be as set.");

        // populate 2
        Assertions.assertThrows(ConfigurationException.class, () -> configuration.setMatchconfig(DEFAULT_MATCHCONFIG),
                "It should not be allowed to change the Matchconfig");

        // second check, should be the same as before
        Assertions.assertEquals(matchconfig, getMatchconfig, "Matchconfig should be as set before.");
    }
}