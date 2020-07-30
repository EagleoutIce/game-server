package de.uulm.team020.server.core.blueprints;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.HelloReplyMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.validation.GameDataGson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.logging.Level;

/**
 * Written to ease the writing of server tests.
 *
 * @author Florian Sihler
 * @version 1.0, 04/04/2020
 */
public abstract class NttsDummyServer {
    protected DummyNttsController controller;

    protected static final int TIMEOUT_MS = 150;

    @BeforeAll
    public static void setupMagpie() {
        Magpie.setDefaultLevel(Level.INFO);
    }

    @BeforeEach
    public void setupController() throws IOException {
        Configuration configuration = new Configuration();

        /* We want to do freshness tests */
        configuration.setFreshnessThresholdInS(90);

        controller = new DummyNttsController(configuration);
        controller.setConfigDefaults();

        controller.start();
    }

    protected void checkHelloReply(DummyClient client, String message) {
        MessageTypeEnum type = GameDataGson.getType(message);
        Assertions.assertEquals(MessageTypeEnum.HELLO_REPLY, type, "The message '" + message
                + "' should be a HelloReply message");
        HelloReplyMessage helloReplyMessage = GameDataGson.fromJson(message, HelloReplyMessage.class);
        Assertions.assertEquals(((NttsClientConnection)client.getAttachment()).getClientId(),
                helloReplyMessage.getClientId(), "clientId should be as targeted");
    }


    protected void setupHelloRite(DummyClient client, String name, RoleEnum role) {
        HelloMessage helloMessage = new HelloMessage(name, role);
        client.send(helloMessage.toJson());
        client.waitForNewMessages(1, TIMEOUT_MS);
    }

    public void validateHelloReply(Configuration configuration,  HelloReplyMessage helloReplyMessage) {
        // Fields should be as expected
        Assertions.assertEquals(configuration.getSessionId(), helloReplyMessage.getSessionId(),
                "sessionId should be as expected");
        Assertions.assertEquals(configuration.getScenario(), helloReplyMessage.getLevel(),
                "scenario should be as expected");
        Assertions.assertArrayEquals(configuration.getCharacters(), helloReplyMessage.getCharacterSettings(),
                "characters should be as expected");
        Assertions.assertEquals(configuration.getMatchconfig(), helloReplyMessage.getSettings(),
                "matchconfig should be as expected");
    }

}
