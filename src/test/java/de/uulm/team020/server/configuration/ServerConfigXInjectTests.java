package de.uulm.team020.server.configuration;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import de.uulm.team020.validation.GameDataGson;

/**
 * Testing the x-inject capabilities of the server-config
 */
public class ServerConfigXInjectTests {

    @Test
    @Tag("Core")
    @Order(0)
    @DisplayName("[XInject] Copy Test")
    public void test_copyTest() throws IOException {
        // Load the server configuration
        ServerConfiguration config = GameDataGson.fromJson(
                GameDataGson.loadInternalJson("configs/xoption-inject-server-config.json"), ServerConfiguration.class);
        // Copy it:
        ServerConfiguration copy = new ServerConfiguration(config);
        Assertions.assertNotSame(config, copy, "Should not be the same");
        Assertions.assertEquals(config, copy, "Should be equal to copied");

        // Change something in the copy:
        config.xOptionInject("interactive", "true");
        Assertions.assertNotEquals(config.dropToCommandLine(), copy.dropToCommandLine(),
                "Drop to command line should differ");
    }

    @Test
    @Tag("Core")
    @Order(0)
    @DisplayName("[XInject] Basic cases nothing")
    public void test_baseCases() throws IOException {
        // Load the server configuration
        ServerConfiguration config = GameDataGson.fromJson(
                GameDataGson.loadInternalJson("configs/xoption-inject-server-config.json"), ServerConfiguration.class);
        List<String> feedback = config.xOptionInject();
        Assertions.assertTrue(feedback.isEmpty(), "List should be empty, but:" + feedback);
    }

    @Test
    @Tag("Core")
    @Order(1)
    @DisplayName("[XInject] Interactive inject")
    public void test_injectInteractive() throws IOException {
        // Load the server configuration
        ServerConfiguration config = GameDataGson.fromJson(
                GameDataGson.loadInternalJson("configs/xoption-inject-server-config.json"), ServerConfiguration.class);
        config.xOptionInject("interactive", "true");
        Assertions.assertTrue(config.dropToCommandLine(), "Should drop as set");
        config.xOptionInject("interactive", "false");
        Assertions.assertFalse(config.dropToCommandLine(), "Should drop as set");
    }

    @Test
    @Tag("Core")
    @Order(2)
    @DisplayName("[XInject] UseLive inject")
    public void test_injectIUseLive() throws IOException {
        // Load the server configuration
        ServerConfiguration config = GameDataGson.fromJson(
                GameDataGson.loadInternalJson("configs/xoption-inject-server-config.json"), ServerConfiguration.class);
        config.xOptionInject("interactive", "true");
        Assertions.assertTrue(config.dropToCommandLine(), "Should drop as set");
        config.xOptionInject("interactive", "false");
        Assertions.assertFalse(config.dropToCommandLine(), "Should drop as set");
    }
}