package de.uulm.team020.server.client.helper;

/**
 * Interface for all classes the digger may search in for further data. The
 * classes are responsible for the representation of the data -- the only thing
 * digger does is search where to find them.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public interface IAmDigable {

    /**
     * Dig for the information in this Object/Class-Architecture.
     * 
     * @param key The key to search for
     * 
     * @return True if the dig was successful, false otherwise.
     */
    boolean dig(DiggerKeyEnum key);

}