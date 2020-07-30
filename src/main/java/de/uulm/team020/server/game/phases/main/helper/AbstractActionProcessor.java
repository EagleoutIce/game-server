package de.uulm.team020.server.game.phases.main.helper;

import de.uulm.team020.datatypes.Matchconfig;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.helper.gadget_action.AbstractCharacterGadgetActionProcessor;

/**
 * This class is just a base to build of the
 * {@link AbstractCharacterGadgetActionProcessor} and the
 * {@link CharacterPropertyActionProcessor}.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/04/2020
 */
public class AbstractActionProcessor {
    protected static IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");
    protected static final String TURN_TXT = "Turn";

    protected GameFieldController controller;
    protected final Matchconfig matchconfig;
    protected final ServerConfiguration serverConfig;
    protected CharacterActionGuard guard;

    /**
     * Create the processor, it will operate on the given controller
     * 
     * @param gameFieldController The gameFieldController to use
     */
    public AbstractActionProcessor(GameFieldController gameFieldController) {
        this.controller = gameFieldController;
        this.matchconfig = controller.getConfiguration().getMatchconfig();
        this.serverConfig = controller.getConfiguration().getServerConfig();
        this.guard = new CharacterActionGuard(gameFieldController);
    }

}