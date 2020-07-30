package de.uulm.team020.server.addons.random;

import java.util.EnumMap;
import java.util.Queue;

import de.uulm.team020.server.addons.random.enums.RandomOperation;

/**
 * This class holds all the configuration for one client by mapping operations
 * to their resolution
 * 
 * @author Florian Sihler
 * @version 1.0, 06/07/2020
 * 
 * @since 1.1
 */
public class RandomControllerSettings extends EnumMap<RandomOperation, Queue<String>> {

    private static final long serialVersionUID = 645728373925472666L;

    /**
     * Create a new Settings object
     */
    public RandomControllerSettings() {
        super(RandomOperation.class);
    }

}