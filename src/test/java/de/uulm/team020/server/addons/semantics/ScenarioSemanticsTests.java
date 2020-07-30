package de.uulm.team020.server.addons.semantics;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.datatypes.Scenario;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.validation.GameDataGson;

public class ScenarioSemanticsTests {

    @Test
    @Tag("Addon")
    @Order(1)
    @DisplayName("[Semantics] Check for the default try to be valid")
    public void test_someFunnyStory() throws IOException {

        ServerConfiguration configuration = GameDataGson.fromJson(
                GameDataGson.loadInternalJson("configs/semantics-server-config.json"), ServerConfiguration.class);
        ScenarioSemantics semantics = new ScenarioSemantics(configuration);

        Scenario scenario = GameDataGson.fromJson(GameDataGson.loadInternalJson("scenarios/semanticsTry.scenario"),
                Scenario.class);

        SemanticsReport report = semantics.check(scenario);

        Assertions.assertTrue(report.isValid(), "Should be valid");
        Assertions.assertEquals(ISemanticsType.SUFFICES, report.getIsValid(), "Should suffice");
        Assertions.assertEquals(1, report.getReasons().size(),
                "As it is valid, there shouldn't be any reasons (one empty)");
        Assertions.assertTrue(report.getReason().isEmpty(),
                "As it is valid, there shouldn't be any reasons (one empty => empty!)");

    }

}