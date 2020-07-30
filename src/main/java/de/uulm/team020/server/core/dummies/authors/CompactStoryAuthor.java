package de.uulm.team020.server.core.dummies.authors;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import de.uulm.team020.GameData;
import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.server.addons.random.RandomControllerSettings;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;
import de.uulm.team020.server.core.dummies.story.helper.StoryLine;
import de.uulm.team020.server.core.dummies.story.helper.StoryLineProducer;

/**
 * This is the compact story author implementation that is used. It will omit
 * any unnecessary comments and omit any sleeps. The goal is to get small story
 * files for replays
 * 
 * @author Florian Sihler
 * @version 1.0, 14/07/2020
 */
public class CompactStoryAuthor extends StoryAuthor {

    /**
     * Construct a new Author to be at your service
     * 
     * @param name          Name of the story the author should write
     * @param configuration The configuration to be used
     */
    public CompactStoryAuthor(final String name, final Configuration configuration) {
        super(name, configuration);
    }

    /**
     * This will dump the current state of the story
     * 
     * @param writer   The target PrintWriter
     * @param filename The name of the file you love
     * @throws IOException If the writing fails
     */
    public void writeStory(final PrintWriter writer, final String filename) throws IOException {
        writeMetadata(writer, filename);
        writeDefaultConfigPart(writer);
        writeConfigPart(writer);
        writeMainPart(writer);
    }

    @Override
    protected void insertSleepDifferenceBetweenCommands() {
        // Do nothing as this one will not record sleeps
    }

    @Override
    public void comment(String comment) {
        // Do not record comments
    }

    @Override
    public void nextRound(int roundNumber) {
        this.mainPart.add(buildComment("Round: " + roundNumber));
    }

    protected void writeConfigPart(final PrintWriter writer) {
        if (configPart.isEmpty()) {
            return; // No configs => no writing
        }
        for (String line : configPart) {
            writer.println(line);
        }
        writer.println();
        // inject the random results as described in the random controller
        for (Map.Entry<String, RandomControllerSettings> randomTarget : this.randomControllerSettings.entrySet()) {
            for (Map.Entry<RandomOperation, Queue<String>> randomTargetOperation : randomTarget.getValue().entrySet()) {
                writer.println(StoryLine.buildCommand(StoryChapterType.CONFIG_INJECT, // Inject
                        Injects.RANDOM_RESULT.getKey(), // A Random result
                        randomTargetOperation.getKey().toString(), // For the given request operation
                        randomTarget.getKey() + ":" + // list all results
                                randomTargetOperation.getValue().stream() // by mapping them to their label
                                        .collect(Collectors.joining(";"))
                // name:result1;result2;result3
                ));
            }
        }
    }

    protected void writeMainPart(final PrintWriter writer) {
        writer.println();
        for (String line : mainPart) {
            writer.println(line);
        }
    }

    // Writes stuff like used scenario, ....
    protected void writeDefaultConfigPart(final PrintWriter writer) {
        // Server Config
        writer.println(StoryLineProducer.inject(Injects.SERVER_CONFIG, StoryBoard.RAW_JSON,
                configuration.getServerConfig().toJson()));
        // Scenario:
        writer.println(
                StoryLineProducer.inject(Injects.SCENARIO, StoryBoard.RAW_JSON, configuration.getScenario().toJson()));
        // Matchconfig:
        // GameDataGson.getPrettyGson().toJson
        writer.println(StoryLineProducer.inject(Injects.MATCHCONFIG, StoryBoard.RAW_JSON,
                configuration.getMatchconfig().toJson()));
        // Characters:
        List<String> charConversion = new ArrayList<>(configuration.getCharacters().length);
        for (CharacterInformation character : configuration.getCharacters()) {
            charConversion.add(character.toJson());
        }

        writer.println(StoryLineProducer.inject(Injects.CHARACTERS, StoryBoard.RAW_JSON,
                "[" + String.join(",", charConversion) + "]"));
        // i just want to be sure and therefore construct it myself

        writer.println();
    }

    protected void writeMetadata(final PrintWriter writer, final String filename) {
        final Date now = new Date();
        writer.println(buildComment("Date: " + now));
        writer.println(buildComment("Server-Version: " + (Configuration.VERSION / 1000D) + " (using Game-Data v"
                + (GameData.VERSION / 1000D) + ")"));
        writer.println();
    }

}