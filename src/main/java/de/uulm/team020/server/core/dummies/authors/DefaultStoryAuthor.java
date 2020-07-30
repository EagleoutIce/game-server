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
import de.uulm.team020.validation.GameDataGson;

/**
 * This is the default story author implementation that is used. It is verbose
 * and tries to include multiple comments. Actually it will include any comment
 * and does no filtering at all.
 * 
 * @author Florian Sihler
 * @version 1.6, 07/07/2020
 */
public class DefaultStoryAuthor extends StoryAuthor {

    /**
     * Construct a new Author to be at your service
     * 
     * @param name          Name of the story the author should write
     * @param configuration The configuration to be used
     */
    public DefaultStoryAuthor(final String name, final Configuration configuration) {
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
        writer.println(StoryLine.buildCommand(StoryChapterType.FORBID_ERRORS));
        writeDefaultConfigPart(writer);
        writeConfigPart(writer);
        writeMainPart(writer);
        writer.println(buildComment("End of File"));
    }

    protected void writeConfigPart(final PrintWriter writer) {
        if (configPart.isEmpty())
            return; // No configs => no writing
        writer.println(COMMENT_LINE);
        writer.println(buildComment("Now the server will write config-injects to assure"));
        writer.println(buildComment("deterministic behaviour."));
        writer.println(COMMENT_LINE);
        for (String line : configPart) {
            writer.println(line);
        }
        writer.println(DIVIDER_COMMENT_LINE);
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
        writer.println(COMMENT_LINE);
        writer.println(buildComment("This is the main part"));
        writer.println(COMMENT_LINE);
        for (String line : mainPart) {
            writer.println(line);
        }
    }

    // Writes stuff like used scenario, ....
    protected void writeDefaultConfigPart(final PrintWriter writer) {
        writer.println(COMMENT_LINE);
        writer.println(buildComment("This is default-configs part, which will set the used scenario, ..."));
        writer.println(COMMENT_LINE);
        // Server Config
        writer.println(StoryLineProducer.collectionStart("", "@server_config"));
        writer.println(GameDataGson.getPrettyGson().toJson(configuration.getServerConfig()));
        writer.println(StoryLineProducer.collectionEnd());
        writer.println(StoryLineProducer.inject(Injects.SERVER_CONFIG, StoryBoard.RAW_JSON, "${@server_config}"));
        // Scenario:
        writer.println(StoryLineProducer.collectionStart("", "@scenario"));
        writer.println("  " + configuration.getScenario().toJson());
        writer.println(StoryLineProducer.collectionEnd());
        writer.println(StoryLineProducer.inject(Injects.SCENARIO, StoryBoard.RAW_JSON, "${@scenario}"));
        // Matchconfig:
        // GameDataGson.getPrettyGson().toJson
        writer.println(StoryLineProducer.collectionStart("", "@matchconfig"));
        writer.println(GameDataGson.getPrettyGson().toJson(configuration.getMatchconfig()));
        writer.println(StoryLineProducer.collectionEnd());
        writer.println(StoryLineProducer.inject(Injects.MATCHCONFIG, StoryBoard.RAW_JSON, "${@matchconfig}"));
        // Characters:
        List<String> charConversion = new ArrayList<>(configuration.getCharacters().length);
        for (CharacterInformation character : configuration.getCharacters()) {
            charConversion.add(character.toJson());
        }

        writer.println(StoryLineProducer.collectionStart("", "@characters"));
        final int charSize = charConversion.size();
        writer.println("[");
        for (int i = 0; i < charSize; i++) {
            writer.println("  " + charConversion.get(i) + (i < charSize - 1 ? ", " : ""));
        }
        writer.println("]");
        writer.println(StoryLineProducer.collectionEnd());
        writer.println(StoryLineProducer.inject(Injects.CHARACTERS, StoryBoard.RAW_JSON, "${@characters}"));
        // i just want to be sure and therefore construct it myself
    }

    protected void writeMetadata(final PrintWriter writer, final String filename) {
        final Date now = new Date();
        writer.println(buildComment("This story was constructed by the StoryAuthor"));
        writer.println(COMMENT_LINE);
        writer.println(buildComment("Filename: " + filename));
        writer.println(buildComment("Date: " + now));
        writer.println(buildComment("Server-Version: " + (Configuration.VERSION / 1000D) + " (using Game-Data v"
                + (GameData.VERSION / 1000D) + ")"));
        writer.println(COMMENT_LINE);
        writer.println(buildSet("story-name", this.name));
        writer.println(buildSet("story-date", now.toString()));
    }

}