package de.uulm.team020.server.core.dummies.story;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.Statistics;
import de.uulm.team020.datatypes.StatisticsEntry;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.enumerations.VictoryEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.RandomController;
import de.uulm.team020.server.addons.random.RandomControllerSettings;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.helper.StoryLine;
import de.uulm.team020.server.core.dummies.story.helper.StoryLineProducer;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.choice.DraftingProposal;
import de.uulm.team020.server.game.phases.main.Faction;

/**
 * An author writes a story and acts as an easy-to-use API for building
 * arbitrary stories. I see no reason to use this in tests as most test cases
 * will be built better if you write the story yourself.
 * <p>
 * This class is to be used by the server itself and logs any activity done by
 * the server so it can be used in a test case.
 * <p>
 * To put it simple: Author is simply generating the story commands. There is no
 * magic to be expected.
 * <p>
 * Any timespan greater than {@link #setSleepThreshold(long)} will be recorded
 * with its' full length. Furthermore, just using the author does not guarantee
 * anything, it is for the upper level to ensure every action gets logged.
 * <p>
 * Starting with V1.3 this will use the {@link StoryLineProducer}.
 * 
 * @author Florian Sihler
 * @version 1.6, 07/07/2020
 */
public abstract class StoryAuthor {

    protected static final String COMMENT_LINE = "# =============================================================================";
    protected static final String DIVIDER_COMMENT_LINE = "# ---------------------------------------------------------";
    protected static final String VALUE_TXT = "value";

    protected static IMagpie magpie = Magpie.createMagpieSafe("Server-Story");

    protected final String name;
    protected final Configuration configuration;

    protected Map<String, Integer> convenientCrashCounters;

    protected List<String> configPart;
    protected List<String> mainPart;

    // used to buffer injections for the random controller
    protected final Map<String, RandomControllerSettings> randomControllerSettings;

    protected long lastInvokeStamp = -1;
    protected long sleepThreshold = 0;

    /**
     * Construct a new Author to be at your service
     * 
     * @param name          Name of the story the author should write
     * @param configuration The configuration to be used
     */
    public StoryAuthor(final String name, final Configuration configuration) {
        this.name = name;
        this.configPart = new LinkedList<>();
        this.mainPart = new LinkedList<>();
        this.configuration = configuration;
        this.convenientCrashCounters = new HashMap<>();
        this.randomControllerSettings = new HashMap<>();
    }

    /**
     * Set the minimum amount of milliseconds to occur between two statements so
     * that they will be recorded
     * 
     * @param threshold Minimum time in ms to record sleep
     */
    public void setSleepThreshold(long threshold) {
        this.sleepThreshold = TimeUnit.MILLISECONDS.toNanos(threshold);
    }

    /**
     * Log matchconfig
     * 
     * @param name Name of the matchconfig
     */
    public void matchconfig(String name) {
        addToMainPart(StoryLineProducer.set("story-matchconfig", name));
    }

    /**
     * Log scenario
     * 
     * @param name Name of the scenario
     */
    public void scenario(String name) {
        addToMainPart(StoryLineProducer.set("story-scenario", name));
    }

    /**
     * Log characters (excluding uuid(s) they will be retranslated)
     * 
     * @param name Name of the character descriptions
     */
    public void characters(String name) {
        addToMainPart(StoryLineProducer.set("story-characters", name));
    }

    /**
     * Log the registration of a new client
     * 
     * @param clientName The name of the client
     * @param role       The role of the client
     */
    public void hello(String clientName, RoleEnum role) {
        addStoryLine(StoryLineProducer.hello(clientName, role));
    }

    /**
     * Signal, that there was an abnormal close of the connection by a client
     * 
     * @param clientName The name of the client
     */
    public void close(String clientName) {
        addStoryLine(StoryLineProducer.crash(clientName));
    }

    /**
     * Signal, that there was an abnormal close of the connection by a client
     * 
     * @param client The client to leave
     */
    public void close(WebSocket client) {
        NttsClientConnection connection = client.getAttachment();
        if (connection == null || !connection.isCurrentlyConnected()) {
            return;
        }
        addStoryLine(StoryLineProducer.crash(connection.getClientName()));
    }

    /**
     * Signal, that there was a leave by a client
     * 
     * @param clientName The name of the client
     */
    public void leave(String clientName) {
        addStoryLine(StoryLineProducer.leave(clientName));
    }

    /**
     * Signal, that there was a leave by a client
     * 
     * @param client The client to leave
     */
    public void leave(WebSocket client) {
        NttsClientConnection connection = client.getAttachment();
        if (connection == null)
            return;
        addStoryLine(StoryLineProducer.leave(connection.getClientName()));
    }

    /**
     * Signal, that there was a pause request
     * 
     * @param clientName The name of the client
     */
    public void pause(String clientName) {
        addStoryLine(StoryLineProducer.pause(clientName));
        // that there is some time
    }

    /**
     * Signal, that there was a resume-request of the connection by a client
     * 
     * @param clientName The name of the client
     */
    public void resume(String clientName) {
        addStoryLine(StoryLineProducer.resume(clientName));
    }

    /**
     * Signal the start of another round
     */
    public void nextRound(int roundNumber) {
        this.mainPart.add(DIVIDER_COMMENT_LINE);
        this.mainPart.add(buildComment("Round Number: " + roundNumber));
        this.mainPart.add(DIVIDER_COMMENT_LINE);
        // addStoryLine(StoryLineProducer.set("#round", roundNumber))
    }

    /**
     * Add a storyLine. I've implemented this one to not having to write
     * delegate-methods
     * 
     * @param line The line to add
     */
    public void storyLine(StoryLine line) {
        addStoryLine(line);
    }

    /**
     * This will reconnect a client by using an incremental counter for moving old
     * names to stale
     * 
     * @param clientName The clientName to reconnect
     */
    public void reconnect(String clientName) {
        int counter = 1;
        if (this.convenientCrashCounters.containsKey(clientName)) {
            counter = this.convenientCrashCounters.get(clientName);
        }
        this.convenientCrashCounters.put(clientName, counter + 1);
        final String oldName = clientName + "-" + counter;
        addStoryLine(StoryLineProducer.rename(clientName, oldName));
        addToMainPart(StoryLineProducer.reconnect(oldName, clientName));
    }

    /**
     * Signal, that there was a meta-information-request by a client
     * 
     * @param clientName The name of the client
     * @param keys       The desired keys
     */
    public void meta(String clientName, String[] keys) {
        // I like the space more :P
        addStoryLine(StoryLineProducer.meta(clientName, keys));
    }

    /**
     * Log a generated drafting proposal, as shipped out to a client
     * 
     * @param clientName The name of the client
     * @param proposal   the drafting proposal
     */
    public void itemProposal(final String clientName, DraftingProposal proposal) {
        // decode the character-names
        if (proposal == null) {
            magpie.writeWarning(
                    "Request to write an item Proposal without a proposal - ignoring! (for: " + clientName + ")",
                    "Author");
            return;
        }

        String items = proposal.getCharacters().stream().map(configuration::getCharacterName)
                .collect(Collectors.joining(","));
        items += (items.isBlank() ? "" : ",")
                + proposal.getGadgets().stream().map(g -> g.toString().toLowerCase()).collect(Collectors.joining(","));
        addToConfigPart(StoryLineProducer.inject(Injects.NEXT_PROPOSAL, clientName, items));
    }

    /**
     * Log an item choice
     * 
     * @param clientName  The name of the client
     * @param characterId The chosen character
     * @param gadget      The chosen gadget
     */
    public void item(final String clientName, UUID characterId, GadgetEnum gadget) {
        String target = "";
        if (characterId != null) {
            target = configuration.getCharacterName(characterId);
        } else if (gadget != null) {
            target = gadget.toString().toLowerCase();
        } else {
            // chosen nothing? fine than - idiot :P
        }
        addStoryLine(StoryLineProducer.item(clientName, target));
    }

    /**
     * Log an equipment choice
     * 
     * @param clientName The name of the client
     * @param equipment  The equipment
     */
    public void equipment(final String clientName, DraftingEquipment equipment) {
        List<String> data = new ArrayList<>();

        for (Entry<UUID, List<GadgetEnum>> entry : equipment.entrySet()) {
            final UUID uuid = entry.getKey();
            String idName = configuration.getCharacterName(uuid);
            data.add(idName);
            entry.getValue().stream().map(GadgetEnum::toString).forEach(data::add);
        }

        addStoryLine(StoryLineProducer.equip(clientName, data));
    }

    /**
     * Log a movement operation from a certain placer.
     * 
     * @param clientName The client that sent the operation
     * @param target     The target point of the movement.
     */
    public void movement(String clientName, Point target) {
        addStoryLine(StoryLineProducer.move(clientName, target));
    }

    /**
     * Register a retire operation from a character, probably caused by a timeout
     * 
     * @param clientName The client that sent the operation
     */
    public void retire(String clientName) {
        addStoryLine(StoryLineProducer.retire(clientName));
    }

    /**
     * Write the winner as comment to the end of the file
     * 
     * @param winner Winner (name) of the game
     * @param reason Victory (reason)
     */
    public void winner(String winner, VictoryEnum reason) {
        this.mainPart.add(COMMENT_LINE);
        this.mainPart.add(buildComment("Winner: " + winner + " for reason: " + reason));
    }

    /**
     * Add a comment to the main part
     * 
     * @param comment The comment to write
     */
    public void comment(String comment) {
        this.mainPart.add(buildComment(comment));
    }

    /**
     * Log an error
     * 
     * @param comment The error to write
     */
    public void error(String comment) {
        this.mainPart.add(buildComment(comment));
    }

    /**
     * Write the statistics as comments to the end of the file
     * 
     * @param statistics The statistics to write
     */
    public void statistics(Statistics statistics) {
        this.mainPart.add(DIVIDER_COMMENT_LINE);
        for (StatisticsEntry entry : statistics.getStatistics()) {
            decodeStatisticsToMainPart(entry);
        }
        this.mainPart.add(DIVIDER_COMMENT_LINE);
    }

    private void decodeStatisticsToMainPart(StatisticsEntry entry) {
        this.mainPart.add(buildComment(entry.getTitle() + " (" + entry.getDescription() + "):"));
        this.mainPart.add(
                buildComment("  Player one: " + entry.getValuePlayer1() + " Player Two: " + entry.getValuePlayer2()));
    }

    /**
     * This method is to be used by the {@link RandomController} to archive the
     * resolut of flips. Already-injected and newly tossed results are not
     * separated. This is the same for point retrieval etc.
     * 
     * @param name      The corresponding name for the operation
     * @param operation The operation that was queried
     * @param result    The result of the random operation
     */
    public void storeRandomResult(String name, RandomOperation operation, String result) {
        // both creation and checking should happen here, as this is the only time this
        // map should get populated! There is no need in adding another layer.
        if (!this.randomControllerSettings.containsKey(name)) {
            this.randomControllerSettings.put(name, new RandomControllerSettings());
        }
        RandomControllerSettings settings = this.randomControllerSettings.get(name);
        if (!settings.containsKey(operation)) {
            settings.put(operation, new LinkedList<>());
        }
        settings.get(operation).add(result);
    }

    /**
     * Write the safe order to the config stream - this shall be used only when the
     * scenario is known, otherwise there is no real reason, when the number safes
     * is not known - or if the order of safes is not known - this would be the same
     * as randomized, and as it is only possible to validate a certain amount of
     * constraints - which are not to be validated with an inject, this would be
     * senseless.
     * 
     * @param order Safe order in iteration order - the order is determined by the
     *              array iteration of the Field-Controller.
     */
    public void setSafeOrder(int[] order) {
        String strOrder = IntStream.of(order).mapToObj(String::valueOf).collect(Collectors.joining(","));
        addToConfigPart(StoryLineProducer.inject(Injects.SAFE_ORDER, VALUE_TXT, strOrder));
    }

    public void setNextRoundOrder(List<String> names) {
        addToConfigPart(StoryLineProducer.inject(Injects.NEXT_ROUND_ORDER, VALUE_TXT, String.join(",", names)));
    }

    /**
     * Write the npc configuration in pickt order
     * 
     * @param neutralDump neutral characters
     */
    public void setNpcSetup(Faction neutralDump) {
        List<String> data = new ArrayList<>();

        for (Character character : neutralDump) {
            data.add(character.getName());
            character.getGadgets().stream().map(Gadget::getGadget).map(GadgetEnum::toString).forEach(data::add);
        }

        addToConfigPart(StoryLineProducer.inject(Injects.NPC_PICK, VALUE_TXT, String.join(",", data)));
    }

    /**
     * Write the character placements as set
     * 
     * @param charactersToPlace characters with descriptions
     */
    public void setGameFieldSetup(Set<Character> charactersToPlace) {
        List<Character> characters = new LinkedList<>(charactersToPlace);
        // sort by name
        characters.sort((a, b) -> a.getName().compareTo(b.getName()));

        List<String> data = new ArrayList<>();

        for (Character character : characters) {
            data.add(character.getName());
            final Point cP = character.getCoordinates();
            data.add(cP.getX() + "/" + cP.getY());
        }

        addToConfigPart(StoryLineProducer.inject(Injects.START_POSITIONS, VALUE_TXT, String.join(",", data)));
    }

    // Adding
    protected void addToMainPart(StoryLine line) {
        this.mainPart.add(line.toString());
    }

    protected void addToConfigPart(StoryLine line) {
        this.configPart.add(line.toString());
    }
    // Creating:

    /**
     * Performs an addition to the main part, measuring the time as well
     * 
     * @param line The line to add
     */
    protected synchronized void addStoryLine(final StoryLine line) {
        if (lastInvokeStamp >= 0) {
            insertSleepDifferenceBetweenCommands();
        }
        lastInvokeStamp = System.nanoTime();
        addToMainPart(line);
    }

    protected void insertSleepDifferenceBetweenCommands() {
        long diff = System.nanoTime() - lastInvokeStamp;
        if (diff >= sleepThreshold) {
            addToMainPart(StoryLineProducer.sleep(TimeUnit.NANOSECONDS.toMillis(diff)));
        }
    }

    // Writing:

    /**
     * Will dump the current Story to a file in the temp-folder using the
     * temporary-file creation-mechanism of Java
     *
     * @throws IOException If there is an error creating the file
     * 
     * @see #writeStory(String)
     */
    public void dump() throws IOException {
        dump("");
    }

    /**
     * Will dump the current Story to a file in the temp-folder using the
     * temporary-file creation-mechanism of Java
     * 
     * @param tag A tat to identify the dump
     * 
     * @throws IOException If there is an error creating the file
     * 
     * @see #writeStory(String)
     */
    public void dump(String tag) throws IOException {
        File target = Path.of(System.getProperty("java.io.tmpdir"), "Stories").toFile();
        target.mkdirs();
        File file = File.createTempFile(
                name + "-" + tag + "-" + Magpie.getFormatter().folderDate.format(new Date()) + "--", ".story", target);
        writeStory(file.getAbsolutePath());
    }

    /**
     * This will dump the current state of the story into a file of your desire.
     * 
     * @param filename The name of the file you love
     */
    public void writeStory(final String filename) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)))) {
            writeStory(writer, filename);
        } catch (IOException ex) {
            magpie.writeException(ex, "Author");
        }
    }

    /**
     * This will dump the current state of the story
     * 
     * @param writer   The target PrintWriter
     * @param filename The name of the file you love
     * @throws IOException If the writing fails
     */
    public abstract void writeStory(final PrintWriter writer, final String filename) throws IOException;

    protected abstract void writeConfigPart(final PrintWriter writer);

    protected abstract void writeMainPart(final PrintWriter writer);

    // Writes stuff like used scenario, ....
    protected abstract void writeDefaultConfigPart(final PrintWriter writer);

    protected abstract void writeMetadata(final PrintWriter writer, final String filename);

    protected String buildComment(String comment) {
        return "# " + comment;
    }

    protected String buildSet(String key, String value) {
        return StoryLineProducer.set(key, value).buildCommand();
    }

}