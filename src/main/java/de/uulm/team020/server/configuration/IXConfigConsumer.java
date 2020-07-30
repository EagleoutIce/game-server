package de.uulm.team020.server.configuration;

import java.util.List;

/**
 * Just the functional interface identifying a x-consumer
 * 
 * @author Florian Sihler
 * @version 1.0, 04/21/2020
 */
@FunctionalInterface
public interface IXConfigConsumer {

    /**
     * To be called on consumption
     * 
     * @param key The key passed, may be redundant
     * @param val The value passed
     * 
     * @return Errors if they've occurred
     */
    List<String> apply(String key, String val);

}