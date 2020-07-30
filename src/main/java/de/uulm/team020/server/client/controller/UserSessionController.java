package de.uulm.team020.server.client.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.client.data.UserSessionData;
import de.uulm.team020.validation.GameDataGson;

/**
 * The session controller handles {@link UserSessionData} so it may be used to
 * reconnect after a crash.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class UserSessionController {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    private static final String RECOVERY_PATH = "./";
    private static final String RECOVERY_POST = "-recovery-data.json";

    private final String name;

    /**
     * This initializes the user session controller to search for any data-files
     * 
     * @param name Name of the client to use
     */
    public UserSessionController(String name) {
        this.name = name;
    }

    /**
     * Checks if there is recovery data for the given player. This will not read the
     * recovery data it just checks if there is a file going by the defined pattern
     * for the given name.
     * 
     * @return True if there is a recovery file, false otherwise
     */
    public boolean hasRecoveryData() {
        return buildFilePath(name).isFile();
    }

    /**
     * Deletes the data if it is present
     * 
     * @return True if the file was deleted, false otherwise
     * 
     * @throws IOException If there was an error on deletion
     */
    public boolean eraseRecoveryData() throws IOException {
        return Files.deleteIfExists(buildFilePath(name).toPath());
    }

    /**
     * Just a little helper to build the file path for recovery data
     * 
     * @param name Te name do build the data for
     * 
     * @return The possible target file constructed
     */
    public static File buildFilePath(String name) {
        return new File(RECOVERY_PATH + name + RECOVERY_POST);
    }

    /**
     * Retrieves a user-session data object for the session
     * 
     * @return The SessionData object from the file -- some fields may be null
     * 
     * @throws IOException In case the closing of the stream fails
     */
    public UserSessionData retrieveData() throws IOException {
        final File file = buildFilePath(name);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            final String jsonFileData = reader.lines().collect(Collectors.joining(" "));
            return GameDataGson.fromJson(jsonFileData, UserSessionData.class);
        } catch (IOException ex) {
            magpie.writeException(ex, "load");
            throw ex;
        }
    }

    /**
     * Stores a user-session data object for the session
     * 
     * @param sessionData The SessionData object from the file -- some fields may be
     *                    null
     * 
     * @throws IOException In case the closing of the stream fails
     */
    public void storeData(final UserSessionData sessionData) throws IOException {
        final File file = buildFilePath(name);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(sessionData.toJson());
        } catch (IOException ex) {
            magpie.writeException(ex, "load");
            throw ex;
        }
    }

    public String getName() {
        return this.name;
    }
}