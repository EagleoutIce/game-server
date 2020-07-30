package de.uulm.team020.server.addons.strikes;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.networking.core.ErrorTypeEnum;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.messages.ErrorMessage;
import de.uulm.team020.networking.messages.StrikeMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.HandlerReport;
import de.uulm.team020.server.core.datatypes.MessageTargetPair;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.ThisShouldNotHappenException;

/**
 * Test the behaviour of the Strike controller
 */
public class StrikeControllerTest {

    @Test
    @DisplayName("[Strikes] Check handling of illegal targets")
    public void test_illegalTargets() {
        final String REASON = "Test reason";
        final Configuration configuration = Configuration.buildFromArgs("--defaults");

        // Get a strike controller and set it up
        StrikeController striker = new StrikeController(configuration);

        NttsClientConnection playerOne = new NttsClientConnection(null, UUID.randomUUID(), "PlayerOne",
                RoleEnum.PLAYER);
        playerOne.setGameRole(GameRoleEnum.PLAYER_ONE);
        NttsClientConnection playerTwo = new NttsClientConnection(null, UUID.randomUUID(), "PlayerTwo",
                RoleEnum.PLAYER);
        playerTwo.setGameRole(GameRoleEnum.PLAYER_TWO);

        NttsClientConnection illegalNull = new NttsClientConnection(null, UUID.randomUUID(), "IllegalPlayer",
                RoleEnum.PLAYER);

        // strike/reset an illegal (null) player
        Assertions.assertThrows(ThisShouldNotHappenException.class, () -> striker.strike(illegalNull, REASON),
                "Exception on illegal null strike");
        Assertions.assertThrows(ThisShouldNotHappenException.class, () -> striker.reset(illegalNull),
                "Exception on illegal null reset");

        // Just as an alias
        NttsClientConnection illegalSpectator = illegalNull;
        illegalSpectator.setGameRole(GameRoleEnum.SPECTATOR);
        Assertions.assertThrows(ThisShouldNotHappenException.class, () -> striker.strike(illegalSpectator, REASON),
                "Exception on illegal spectator strike");
        Assertions.assertThrows(ThisShouldNotHappenException.class, () -> striker.reset(illegalSpectator),
                "Exception on illegal spectator reset");

    }

    @ParameterizedTest
    @DisplayName("[Strikes] Single Strike (for p1/p2)")
    @ValueSource(strings = { "true", "false" }) // true if p1 false otherwise
    public void test_singleStrike(boolean p1) {
        final String REASON = "Test reason";
        final Configuration configuration = Configuration.buildFromArgs("--defaults");

        // Get a strike controller and set it up
        StrikeController striker = new StrikeController(configuration);

        NttsClientConnection playerOne = new NttsClientConnection(null, UUID.randomUUID(), "PlayerOne",
                RoleEnum.PLAYER);
        playerOne.setGameRole(GameRoleEnum.PLAYER_ONE);
        NttsClientConnection playerTwo = new NttsClientConnection(null, UUID.randomUUID(), "PlayerTwo",
                RoleEnum.PLAYER);
        playerTwo.setGameRole(GameRoleEnum.PLAYER_TWO);

        // strike player
        NttsClientConnection playerToStrike = p1 ? playerOne : playerTwo;
        HandlerReport strikePlayer = striker.strike(playerToStrike, REASON);

        // validate strike reason
        List<MessageTargetPair> targets = strikePlayer.getMessageRequests();
        Assertions.assertTrue(targets.size() == 1, "Should be one result");

        MessageContainer shouldBeStrikeMessage = targets.get(0).getMessage();
        Assertions.assertTrue(shouldBeStrikeMessage instanceof StrikeMessage, "Message should be a strike");
        StrikeMessage strikeMessage = (StrikeMessage) shouldBeStrikeMessage;

        // check data for validity
        Assertions.assertEquals(playerToStrike.getClientId(), strikeMessage.getClientId(), "Id should match");
        Assertions.assertEquals(REASON, strikeMessage.getReason(), "Reason should match");
        Assertions.assertEquals(1, strikeMessage.getStrikeNr(), "Strike-Number should match");
        Assertions.assertEquals(configuration.getMatchconfig().getStrikeMaximum(), strikeMessage.getStrikeMax(),
                "Strike-Max should match");
    }

    @Test
    @DisplayName("[Strikes] Increment p1 to max, than p2, check strikes and error on final (includes reset)")
    public void test_strikesP1P2() {
        final Configuration configuration = Configuration.buildFromArgs("--defaults");

        // Get a strike controller and set it up
        StrikeController striker = new StrikeController(configuration);

        NttsClientConnection playerOne = new NttsClientConnection(null, UUID.randomUUID(), "PlayerOne",
                RoleEnum.PLAYER);
        playerOne.setGameRole(GameRoleEnum.PLAYER_ONE);
        NttsClientConnection playerTwo = new NttsClientConnection(null, UUID.randomUUID(), "PlayerTwo",
                RoleEnum.PLAYER);
        playerTwo.setGameRole(GameRoleEnum.PLAYER_TWO);

        final int strikeMax = configuration.getMatchconfig().getStrikeMaximum();

        Assertions.assertTrue(striker.playerOne(), "Player one should be allowed to play.");
        Assertions.assertTrue(striker.playerTwo(), "Player two should be allowed to play.");

        // strike p1 to the ground
        for (int i = 1; i <= strikeMax; i++) {
            testStrike(i, strikeMax, playerOne, striker);
        }

        Assertions.assertFalse(striker.playerOne(), "Player one should not be allowed to play anymore.");
        Assertions.assertTrue(striker.playerTwo(), "Player two should be allowed to play.");

        // strike p2 to the ground
        for (int i = 1; i <= strikeMax; i++) {
            testStrike(i, strikeMax, playerTwo, striker);
        }

        Assertions.assertFalse(striker.playerOne(), "Player one should not be allowed to play anymore.");
        Assertions.assertFalse(striker.playerTwo(), "Player two should not be allowed to play anymore.");

        striker.reset(playerOne);
        Assertions.assertTrue(striker.playerOne(), "Player one should be allowed to play.");
        Assertions.assertFalse(striker.playerTwo(), "Player two should not be allowed to play anymore.");

        striker.reset(playerTwo);

        Assertions.assertTrue(striker.playerOne(), "Player one should be allowed to play.");
        Assertions.assertTrue(striker.playerTwo(), "Player two should be allowed to play.");

        // Maybe test with other orders?
    }

    /**
     * Strikes the given target for given strike number and tests
     * 
     * @param iterNum     Should be number
     * @param expectedMax Should be max
     * @param target      Strike target
     * @param striker     The stricke controller to issue the strike
     */
    private void testStrike(int iterNum, int expectedMax, NttsClientConnection target, StrikeController striker) {
        final String REASON = RandomHelper.randomString(10);
        HandlerReport strikePlayer = striker.strike(target, REASON);

        boolean errorStrike = iterNum >= expectedMax;

        // validate strike reason
        List<MessageTargetPair> targets = strikePlayer.getMessageRequests();
        Assertions.assertEquals((errorStrike ? 2 : 1), targets.size(),
                "Should be " + (errorStrike ? "two" : "one") + " result(s). In: " + iterNum + " for: " + target);

        // Should always be strike
        MessageContainer shouldBeStrikeMessage = targets.get(0).getMessage();
        Assertions.assertTrue(shouldBeStrikeMessage instanceof StrikeMessage,
                "Message should be a strike, but:" + shouldBeStrikeMessage + ". In: " + iterNum + " for: " + target);
        StrikeMessage strikeMessage = (StrikeMessage) shouldBeStrikeMessage;

        // check data for validity
        Assertions.assertEquals(target.getClientId(), strikeMessage.getClientId(),
                "Id should match. In: " + iterNum + " for: " + target);
        Assertions.assertEquals(REASON, strikeMessage.getReason(),
                "Reason should match. In: " + iterNum + " for: " + target);
        Assertions.assertEquals(iterNum, strikeMessage.getStrikeNr(),
                "Strike-Number should match. In: " + iterNum + " for: " + target);
        Assertions.assertEquals(expectedMax, strikeMessage.getStrikeMax(),
                "Strike-Max should match. In: " + iterNum + " for: " + target);

        // it may be an error Message too
        if (!errorStrike) {
            return;
        }

        // check for Error Message kick by Strike :D
        MessageContainer shouldBeErrorMessage = targets.get(1).getMessage();
        Assertions.assertTrue(shouldBeErrorMessage instanceof ErrorMessage,
                "Message should be a error, but: " + shouldBeErrorMessage + ". In: " + iterNum + " for: " + target);
        ErrorMessage errorMessage = (ErrorMessage) shouldBeErrorMessage;

        Assertions.assertEquals(target.getClientId(), errorMessage.getClientId(),
                "Id should match for error. In: " + iterNum + " for: " + target);
        Assertions.assertEquals(ErrorTypeEnum.TOO_MANY_STRIKES, errorMessage.getReason(),
                "Should be error as too many strikes, but: " + errorMessage + ". In: " + iterNum + " for: " + target);
    }

}