package de.uulm.team020.server.core.dummies.story;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import de.uulm.team020.datatypes.CharacterInformation;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.ImmutablePair;
import de.uulm.team020.datatypes.util.Pair;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.EquipmentChoiceMessage;
import de.uulm.team020.networking.messages.GameLeaveMessage;
import de.uulm.team020.networking.messages.GameOperationMessage;
import de.uulm.team020.networking.messages.HelloMessage;
import de.uulm.team020.networking.messages.ItemChoiceMessage;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.networking.messages.ReconnectMessage;
import de.uulm.team020.networking.messages.RequestGamePauseMessage;
import de.uulm.team020.networking.messages.RequestMetaInformationMessage;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.DummyClient;
import de.uulm.team020.server.core.dummies.DummyNttsController;
import de.uulm.team020.server.core.dummies.DummySendMessagesBuffer;
import de.uulm.team020.server.core.dummies.story.exceptions.StoryException;
import de.uulm.team020.server.core.dummies.story.helper.StoryGameOperationGenerator;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.game.phases.choice.DraftingChoice;
import de.uulm.team020.server.game.phases.choice.DraftingEquipment;
import de.uulm.team020.server.game.phases.choice.DraftingProposal;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.MainGamePhaseController;
import de.uulm.team020.validation.GameDataGson;

/**
 * Created by a {@link StoryBoard} to contain all data about the server-story.
 * <p>
 * Currently the list of clients contains 'all' clients, even if you made them
 * leave, if you want to toss a client away or rename it, so you can let another
 * one with the same name rejoin, use the option "DELETE" or "RENAME"
 *
 * @author Florian Sihler
 * @version 1.1, 05/08/2020
 */
public class Story {

    public static final String RANDOM = "random";

    private static final String DOS_TXT = "DosAttack";
    private static final String UNEXPECTED_TXT = " was not expected (";

    private static IMagpie magpie = Magpie.createMagpieSafe("Server-Story");

    // We keep a list to keep the order of their registration
    private List<DummyClient> clients;

    private final DummyNttsController controller;
    private final Configuration configuration;
    private final ServerConfiguration serverConfig;

    private final StoryGameOperationGenerator operationGenerator;

    private static final int INITIAL_TIMEOUT_MS = 1500;
    private static final int TIMEOUT_MS = 300;

    private boolean escalate = true;

    /**
     * Construct a new Story with defaults
     * 
     * @throws IOException If the loading of the internal configuration defaults
     *                     fails.
     */
    public Story() throws IOException {
        this(DummyNttsController.setConfigDefaults(new Configuration()));
    }

    /**
     * Construct a new Story using set configurations
     *
     * @param configuration The setting you want to be used
     */
    public Story(Configuration configuration) {
        this(new DummyNttsController(configuration), new LinkedList<>());
        this.controller.start();
    }

    /**
     * Base the story on an existing controller-cl
     *
     * @param controller The controller that holds the configuration and "append"
     *                   some parts to the host of the story
     * @param clients    the clients that should take part
     */
    public Story(DummyNttsController controller, List<DummyClient> clients) {
        this.controller = controller;
        this.clients = clients;
        this.configuration = controller.getConfiguration();
        this.serverConfig = this.configuration.getServerConfig();
        this.operationGenerator = new StoryGameOperationGenerator(this);
    }

    private synchronized void assertWanted(DummySendMessagesBuffer buffer, int index, MessageTypeEnum type) {
        if (index >= buffer.size()) {
            throw new StoryException("There was an assertion request for buffer at index: " + index
                    + " but the buffer only holds: " + buffer.size() + " elements, consisting of: " + buffer);
        }
        final String message = buffer.get(index);
        final MessageTypeEnum messageType = GameDataGson.getType(message);
        if (messageType != type) {
            if (escalate) {
                throw new StoryException("The message type: " + message + UNEXPECTED_TXT + type + "). Buffer: "
                        + buffer.typeTraversal());
            } else {
                magpie.writeError("The message type: " + message + UNEXPECTED_TXT + type + "). Escalate was silenced",
                        "Assert");
            }
        }
    }

    private synchronized void assertContains(DummySendMessagesBuffer buffer, int offset, MessageTypeEnum type) {
        if (offset >= buffer.size()) {
            throw new StoryException("There was an assertion request for buffer starting at offset: " + offset
                    + " but the buffer only holds: " + buffer.size() + " elements, consisting of: " + buffer
                    + " called by: " + Magpie.getNameOfCaller(3));
        }

        if (!buffer.containsType(type, offset)) {
            if (escalate) {
                throw new StoryException("The message type at offset " + offset + UNEXPECTED_TXT + type + "). Buffer: "
                        + buffer.typeTraversal() + " called by: " + Magpie.getNameOfCaller(3));
            } else {
                magpie.writeError(
                        "The message type at offset " + offset + UNEXPECTED_TXT + type + "). Escalate was silenced",
                        "Assert");
            }
        }
    }

    private String getName(DummyClient client) {
        final String name = client.getName();
        return name != null ? name : "";
    }

    private UUID getId(DummyClient client) {
        final UUID id = client.getClientId();
        return id != null ? id : UUID.randomUUID();
    }

    /**
     * Returns a client by its name - know your protagonists. Will throw a
     * {@link StoryException} if not found.
     *
     * @param name The name of the wanted character/client
     *
     * @return the client
     */
    public DummyClient getClient(String name) {
        DummyClient client = getClientSave(name);
        if (client == null)
            throw new StoryException("You requested the client " + name + " which does not exist.");
        return client;
    }

    /**
     * Returns a client by its id - know your protagonists. Will throw a
     * {@link StoryException} if not found.
     *
     * @param id The id of the client
     *
     * @return the client
     */
    public DummyClient getClient(UUID id) {
        DummyClient client = getClientSave(id);
        if (client == null)
            throw new StoryException("You requested the client by " + id + " which does not exist.");
        return client;
    }

    // Will throw now exception
    private DummyClient getClientSave(final String name) {
        return clients.stream().filter(x -> Objects.equals(getName(x), name)).findFirst().orElse(null);
    }

    private DummyClient getClientSave(final UUID id) {
        // we prefer open ones
        return clients.stream().filter(x -> getId(x).equals(id) && x.isOpen()).findFirst()
                .orElse(clients.stream().filter(x -> getId(x).equals(id)).findFirst().orElse(null));
    }

    /**
     * Add a client to the mix
     *
     * @param name name of the client
     * @param role role of the client
     * @return The story for chaining
     */
    protected Story newClientHello(String name, RoleEnum role) {
        DummyClient client = new DummyClient(this.controller);
        HelloMessage hello = new HelloMessage(name, role);
        client.send(hello.toJson());
        // important: meta information is first :D
        client.assureMessages(serverConfig.sendMetaOnConnectionOpen() ? 2 : 1, INITIAL_TIMEOUT_MS);
        // multiple waits are important to ensure the registration of the status to
        // check the next ones
        // maybe we wait for gameStarted and request item choice?
        if (Objects.nonNull(controller.getClientManager().getPlayerTwo())) {
            // should get gameStarted
            client.assureMessages(serverConfig.sendMetaOnConnectionOpen() ? 3 : 2, INITIAL_TIMEOUT_MS);

            if (Objects.equals(controller.getMainGamePhaseController().getPlayerWaiting(), client.getClientId())) {
                // should get gameStarted & requestItemChoice
                client.assureMessages(serverConfig.sendMetaOnConnectionOpen() ? 4 : 3, INITIAL_TIMEOUT_MS);
            }
        }
        // add the new client if it wasn't a denial
        assertWanted(client.getMessages(), serverConfig.sendMetaOnConnectionOpen() ? 1 : 0,
                MessageTypeEnum.HELLO_REPLY);
        clients.add(client);
        return this;
    }

    /**
     * Simulate a reconnect for the client
     *
     * @param oldName Name of the client to reconnect to
     * @param newName Name of the new dummy-client name
     * @return The story for chaining
     */
    protected Story simulateReconnect(String oldName, String newName) {
        assureThatNoneOfThemIsNullOrSame(oldName, newName);

        DummyClient old = getClient(oldName);
        NttsClientConnection connection = old.getConnection();
        if (escalate && connection.isCurrentlyConnected()) { // not crashed?
            throw new StoryException(
                    "The client you want to reconnect (" + oldName + ") is still connected (" + connection + ").");
        }
        DummyClient newClient = new DummyClient(controller);
        newClient.fakeName(newName); // Will be named with oldName on server-side
        ReconnectMessage reconnectMessage = new ReconnectMessage(old.getClientId(), configuration.getSessionId());

        int cMsgBuffer = newClient.getMessages().size();
        newClient.send(reconnectMessage.toJson());
        newClient.awaitForMessageType(MessageTypeEnum.GAME_STARTED, 5, TIMEOUT_MS, cMsgBuffer);
        assertContains(newClient.getMessages(), cMsgBuffer, MessageTypeEnum.GAME_STARTED);
        this.clients.add(newClient); // add it
        return this;
    }

    private void assureThatNoneOfThemIsNullOrSame(String oldName, String newName) {
        if (Objects.isNull(oldName) || Objects.isNull(newName)) { // null?
            throw new StoryException("Neither oldName (" + oldName + ") nor newName (" + newName + ") should be null.");
        } else if (Objects.equals(oldName, newName)) { // same?
            throw new StoryException("The oldName (" + oldName + ") and newName (" + newName + ") shouldn't be equal.");
        } else if (Objects.nonNull(getClientSave(newName))) { // already taken?
            throw new StoryException(
                    "There is already a client registered with '" + newName + "' please choose another newName");
        }
    }

    /**
     * Request Meta-Information. Denoted by one comma-separated Token like "KeyA,
     * KeyB, KeyC, ..."
     *
     * @param name                 name of the client
     * @param requestedInformation requested Information
     * @return The story for chaining
     */
    protected Story requestMetaInformation(String name, String[] requestedInformation) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();

        RequestMetaInformationMessage requestMetaInformationMessage = new RequestMetaInformationMessage(
                connection.getClientId(), requestedInformation);
        int cMsgBuffer = client.getMessages().size();
        client.send(requestMetaInformationMessage.toJson());
        client.awaitForMessageType(MessageTypeEnum.META_INFORMATION, 5, TIMEOUT_MS, cMsgBuffer);
        assertContains(client.getMessages(), cMsgBuffer, MessageTypeEnum.META_INFORMATION);
        return this;
    }

    /**
     * Request a GamePause
     *
     * @param name name of the client
     * @return The story for chaining
     */
    protected Story requestGamePause(String name) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();
        RequestGamePauseMessage requestGamePauseMessage = new RequestGamePauseMessage(connection.getClientId(), true);
        int cMsgBuffer = client.getMessages().size();
        client.send(requestGamePauseMessage.toJson());
        client.awaitForMessageType(MessageTypeEnum.GAME_PAUSE, 5, TIMEOUT_MS, cMsgBuffer);
        assertContains(client.getMessages(), cMsgBuffer, MessageTypeEnum.GAME_PAUSE);
        return this;
    }

    /**
     * Request the end of a GamePause
     *
     * @param name name of the client
     * @return The story for chaining
     */
    protected Story resumeGamePause(String name) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();
        RequestGamePauseMessage requestGamePauseMessage = new RequestGamePauseMessage(connection.getClientId(), false);
        int cMsgBuffer = client.getMessages().size();
        client.send(requestGamePauseMessage.toJson());
        // if both have to accept only check if both have accepted
        if (!serverConfig.resumeByBoth() || (controller.getPauseController().playerOneResume()
                && controller.getPauseController().playerTwoResume())) {
            client.awaitForMessageType(MessageTypeEnum.GAME_PAUSE, 5, TIMEOUT_MS, cMsgBuffer);
            assertContains(client.getMessages(), cMsgBuffer, MessageTypeEnum.GAME_PAUSE);
        }
        return this;
    }

    /**
     * Send a GameLeave
     *
     * @param name name of the client
     * @return The story for chaining
     */
    protected Story leaveTheGame(String name) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();
        GameLeaveMessage leaveMessage = new GameLeaveMessage(connection.getClientId());
        int cMsgBuffer = client.getMessages().size();
        client.send(leaveMessage.toJson());
        client.awaitForMessageType(MessageTypeEnum.GAME_LEFT, 5, TIMEOUT_MS, cMsgBuffer);
        assertContains(client.getMessages(), cMsgBuffer, MessageTypeEnum.GAME_LEFT);
        // We want the gameLeft-approval, as there should be only one message in this
        // case, we will
        // assure the last one
        /* NOTE: the client will remain in the list to assure that he is fine */
        return this;
    }

    /**
     * Send an ItemChoice Message
     *
     * @param name      name of the client
     * @param gadget    the gadget to choose
     * @param character the character to choose
     * 
     * @return The story for chaining
     */
    protected Story sendItemChoice(String name, GadgetEnum gadget, UUID character) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();
        ItemChoiceMessage itemChoiceMessage;

        itemChoiceMessage = new ItemChoiceMessage(connection.getClientId(), character, gadget);
        int cMsgBuffer = client.getMessages().size();
        client.send(itemChoiceMessage.toJson());
        client.assureMessages(cMsgBuffer + 1, TIMEOUT_MS);

        /* NOTE: the client will remain in the list to assure that he is fine */
        return this;
    }

    /**
     * Little helper method will parse every element and decode it to character or
     * gadget and then perform the correct translation
     * 
     * @param tokens The tokens present int the last argument of the EQUIP-call
     * @return The translation to be used with
     *         {@link #sendEquipmentChoice(String, List)}
     */
    protected List<Pair<UUID, List<GadgetEnum>>> translateTokens(String[] tokens) {
        List<Pair<UUID, List<GadgetEnum>>> retList = new LinkedList<>();
        Pair<UUID, List<GadgetEnum>> current = null; // = new Pair<UUID,List<GadgetEnum>>(null, new LinkedList<>())
        for (String token : tokens) {
            // check if it is character
            ImmutablePair<UUID, GadgetEnum> pickCode = decodeGadgetCharacter(token);
            if (pickCode.getKey() != null) { // is character
                if (current != null) {// push old
                    retList.add(current); // will not change
                }
                // create new
                current = Pair.of(pickCode.getKey(), new LinkedList<>());
            } else if (pickCode.getValue() != null) { // is gadget
                if (current == null) {
                    throw new StoryException("The gadget: '" + pickCode.getValue() + "' is not bound to a character");
                } else { // add
                    current.getValue().add(pickCode.getValue());
                }
            } else {
                throw new StoryException("The sub-token: '" + token + "' is neither a character nor a gadget.");
            }
        }
        if (current != null)// Don't forget the last one
            retList.add(current);
        return retList;
    }

    /**
     * Send an EquipmentChoice Message
     *
     * @param name    name of the client
     * @param choices the choices to make
     * 
     * @return The story for chaining
     */
    protected Story sendEquipmentChoice(String name, List<Pair<UUID, List<GadgetEnum>>> choices) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();
        DraftingEquipment mapChoice = new DraftingEquipment();
        choices.forEach(c -> mapChoice.put(c.getKey(), c.getValue()));
        EquipmentChoiceMessage equipmentChoiceMessage = new EquipmentChoiceMessage(connection.getClientId(), mapChoice);

        client.send(equipmentChoiceMessage.toJson());

        return this;
    }

    /**
     * Send an randomized but valid EquipmentChoice Message
     *
     * @param name name of the client
     * 
     * @return The story for chaining
     */
    protected Story sendEquipmentChoice(String name) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();

        DraftingChoice choice = controller.getDraftingPhaseController().assertCorrectChoice(connection);

        DraftingEquipment mapChoice = new DraftingEquipment();

        for (UUID character : choice.getCharacters()) {
            mapChoice.put(character, new LinkedList<>());
        }

        // append random, i do not care about efficiency :P
        for (GadgetEnum gadget : choice.getGadgets()) {
            int idx = ThreadLocalRandom.current().nextInt(choice.getCharacters().size());
            mapChoice.get(choice.getCharacters().get(idx)).add(gadget);
        }

        EquipmentChoiceMessage equipmentChoiceMessage = new EquipmentChoiceMessage(connection.getClientId(), mapChoice);

        client.send(equipmentChoiceMessage.toJson());

        return this;
    }

    /**
     * Send an Operation Message, should be extended for all operations which exist
     * (in the future)
     *
     * @param name      name of the client, might be random to assure pause
     * @param key       The key - operation name to be handled
     * @param arguments The arguments passed
     * 
     * @return The story for chaining
     * 
     * @throws InterruptedException If there is a problem with the underlying sleep
     */
    protected Story sendOperation(String name, String key, String arguments) throws InterruptedException {

        final Optional<DummyClient> mayClient = operationGenerator.decodeActiveClient(name);
        if (!mayClient.isPresent()) {
            return this;
        }
        final DummyClient client = mayClient.get();

        // wait for game-start
        client.awaitForMessageType(MessageTypeEnum.GAME_STATUS, 6, TIMEOUT_MS);

        // identify operation
        OperationEnum operation = OperationEnum.valueOf(key.toUpperCase());

        GameOperationMessage gameOperationMessage = operationGenerator.buildGameOperation(client, operation, key,
                arguments);

        int cMsgBuffer = client.getMessages().size();
        client.send(gameOperationMessage.toJson());

        // wait for gameStatus
        client.awaitForMessageType(MessageTypeEnum.GAME_STATUS, 4, TIMEOUT_MS, cMsgBuffer);
        return this;
    }

    /**
     * This will cause a client to issue a rapid-fire of messages to test how the
     * server will respond. This operation will be none-blocking, so you can issue
     * multiple DOS attacks from multiple clients.
     * 
     * @param name        Name of the client to use. If you select 'random' this
     *                    will create a new connection for every attack. New
     *                    connections will register regularly with a hello-routine
     * @param messageType The type of the messages to send. If it is random, it will
     *                    choose a random type <i>each</i> attack.
     * @param times       How many times should the attack be performed?
     * @param delay       How many ms are there to be waited between two attacks
     *                    (0ms doesn't mean exactly 0ms, it means, don't wait extra)
     *
     * @return The story for chaining
     */
    protected Story sendDosAttack(String name, String messageType, int times, int delay) {
        if (name.equalsIgnoreCase("RANDOM")) {
            randomDosAttack(messageType, times, delay);
        } else {
            namedDosAttack(name, messageType, times, delay);
        }

        return this;
    }

    private void namedDosAttack(final String name, final String messageType, final int times, final int delay) {
        for (int i = 0; i < times; i++) {
            dosAttackStep(name, RandomHelper.rndPick(RoleEnum.values()), messageType, false);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                magpie.writeException(ex, DOS_TXT);
            }
        }
    }

    private void randomDosAttack(final String messageType, final int times, final int delay) {
        for (int i = 0; i < times; i++) {
            final String name = RandomHelper.randomString(5, 12);
            if (controller.getClientManager().getPlayerTwo() == null) { // could register as player
                dosAttackStep(name, RandomHelper.rndPick(RoleEnum.values()), messageType, true);
            } else { // cannot register as player
                dosAttackStep(name, RoleEnum.SPECTATOR, messageType, true);
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                magpie.writeException(ex, DOS_TXT);
            }
        }
    }

    private void dosAttackStep(final String name, final RoleEnum role, final String messageType, boolean create) {
        Thread attacker = new Thread(() -> {
            if (create) {
                newClientHello(name, role);
            }
            DummyClient target = getClient(name);
            dosAttack(target, messageType);
        }, "Attacker: " + name);
        attacker.start();
    }

    private void dosAttack(DummyClient attacker, String messageType) {
        MessageContainer outContainer = null;
        MessageTypeEnum type;
        NttsClientConnection connection = attacker.getConnection();

        if (messageType.equalsIgnoreCase("RANDOM")) {
            // Select a random but valid Type, jeah this ignores the ai-rules for pauses,
            // but hey
            switch (connection.getClientRole()) {
                case PLAYER:
                    type = MessageTypeEnum.getRandomPlayer();
                    break;
                case AI:
                    type = MessageTypeEnum.getRandomAi();
                    break;
                default:
                case SPECTATOR:
                    type = MessageTypeEnum.getRandomSpectator();
                    break;
            }

        } else {
            type = MessageTypeEnum.valueOf(messageType.toUpperCase());
        }
        try {
            switch (type) {
                case HELLO:
                    outContainer = new HelloMessage(connection.getClientName(), connection.getClientRole());
                    break;
                case REQUEST_META_INFORMATION:
                    outContainer = new RequestMetaInformationMessage(connection.getClientId(),
                            MetaKeyEnum.SPECTATOR_COUNT, MetaKeyEnum.SPECTATOR_NAMES);
                    break;
                default: // for now
                case ITEM_CHOICE:
                    ImmutablePair<UUID, GadgetEnum> pickCode = populateRndButValidItems(connection, true, true);
                    outContainer = new ItemChoiceMessage(connection.getClientId(), pickCode.getKey(),
                            pickCode.getValue());
                    break;
            }
        } catch (HandlerException ex) {
            magpie.writeException(ex, DOS_TXT);
            if (escalate) {
                throw new StoryException("Dos-Attacker failed for reason: " + ex.getMessage() + " : " + ex);
            }
        }
        if (outContainer != null) {
            attacker.send(outContainer.toJson());
        }
    }

    /**
     * Send a random ItemChoice Message which is valid. Or at least it should be.
     * <p>
     * It operates by using the proposal the server has prepared for this client and
     * choose gadget or character by random - as long as it is valid to choose one
     * of those. So e.g. if it has picked a random character four times by luck it
     * will only pick gadgets from there on.
     * <p>
     * If there is now proposal for the client, this will throw an exception if
     * there is no proposal for this client.
     *
     * @param name      name of the client
     * @param gadget    consider gadgets
     * @param character consider characters
     * 
     * @return The story for chaining
     */
    protected Story sendItemChoice(String name, boolean gadget, boolean character) {
        DummyClient client = getClient(name);
        NttsClientConnection connection = client.getAttachment();

        try {
            ImmutablePair<UUID, GadgetEnum> pickCode = populateRndButValidItems(connection, gadget, character);
            return sendItemChoice(name, pickCode.getValue(), pickCode.getKey());
        } catch (HandlerException | NullPointerException ex) {
            throw new StoryException("Wanted to send a item choice for: " + name + " but got exception: "
                    + ex.getClass() + ": " + ex.getMessage());
        }
    }

    private ImmutablePair<UUID, GadgetEnum> populateRndButValidItems(NttsClientConnection connection, boolean gadget,
            boolean character) throws HandlerException {
        // Get the proposal
        DraftingProposal proposal = controller.getDraftingPhaseController().assertCorrectProposal(connection);
        DraftingChoice choice = controller.getDraftingPhaseController().assertCorrectChoice(connection);
        List<String> pool = new ArrayList<>(6);

        if (character && choice.charactersLeft() > 0) {
            // We can still choose a character
            proposal.getCharacters().stream().map(UUID::toString).forEach(pool::add);
        }
        if (gadget && choice.gadgetsLeft() > 0) {
            // We can still choose a Gadget
            proposal.getGadgets().stream().map(GadgetEnum::toString).forEach(pool::add);
        }
        // can we choose something?
        if (pool.isEmpty())
            throw new StoryException("There is currently no proposal for '" + connection.getClientName()
                    + "' or it does not offer: char:" + character + " gadget: " + gadget + " as proposal is: "
                    + proposal);
        // let's choose a random possibility
        int idx = ThreadLocalRandom.current().nextInt(pool.size());
        String pick = pool.get(idx);
        return decodeGadgetCharacterUuid(pick);
    }

    /**
     * Simulate a timeout for the client
     *
     * @param name name of the client
     * @return The story for chaining
     */
    protected Story simulateTimeoutFor(String name) {
        controller.simulateTimeoutClose(getClient(name));
        return this;
    }

    /**
     * Simulate a crash for the client
     *
     * @param name name of the client
     * @return The story for chaining
     */
    protected Story simulateCrashFor(String name) {
        controller.simulateCrashClose(getClient(name));
        return this;
    }

    /**
     * Exceptions will be silenced
     * 
     * @return The story for chaining
     */
    protected Story allowExceptions() {
        escalate = false;
        return this;
    }

    /**
     * Exceptions wont be silenced (default)
     * 
     * @return The story for chaining
     */
    protected Story forbidExceptions() {
        escalate = true;
        return this;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public DummyNttsController getController() {
        return this.controller;
    }

    /**
     * This is just a convenience method which depends on the
     * {@link MainGamePhaseController}. If you are before the main game phase this
     * will not work.
     * 
     * @return The GameFieldController operated by the main game phase controller
     */
    public GameFieldController getGameFieldController() {
        final GameFieldController gfController = Objects
                .requireNonNull(controller.getMainGamePhaseController(),
                        "Main GamePhase controller shouldn't be null  -- there should be nothing.")
                .getGameFieldController();
        return Objects.requireNonNull(gfController,
                "The GameField controller is null, which means that the game-start probably has not occurred");
    }

    /**
     * Will just call {@link DummyClient#clearMessages()} for all Clients.
     */
    public void eraseAllMemories() {
        this.clients.forEach(DummyClient::clearMessages);
    }

    public List<DummyClient> getClients() {
        return this.clients;
    }

    protected Story deleteClient(String name) {
        clients.remove(getClient(name));
        return this;
    }

    /**
     * Change the name of an underlying client. This is a "fake" change as the
     * server does not typically support any changed.
     * 
     * @param oldName The old name to identify the client via
     *                {@link #getClient(String)}.
     * @param newName The name this client shall have after the operation is
     *                completed.
     * @return This story for being used in chaining
     */
    protected Story renameClient(String oldName, String newName) {
        DummyClient oldClient = getClient(oldName);
        oldClient.fakeName(newName);
        return this;
    }

    /**
     * Add a client to the list
     * 
     * @param client The client to add.
     * 
     * @return The story for chaining
     */
    protected Story addClient(DummyClient client) {
        this.clients.add(client);
        return this;
    }

    @Override
    public String toString() {
        return "Story [clients=" + clients + ", configuration=" + configuration + ", controller=" + controller + "]";
    }

    /**
     * Will commit a {@link DummySendMessagesBuffer#typeTraversal()} on every
     * Client.
     * 
     * @return String including newlines for every client
     */
    public String typeDump() {
        StringBuilder builder = new StringBuilder();
        for (DummyClient dummyClient : clients) {
            builder.append(dummyClient.getName()).append(": ").append(dummyClient.getMessages().typeTraversal())
                    .append("\n");
        }
        return builder.toString();
    }

    /**
     * Character decoder - returns the uuid of a character by name, if present
     * 
     * @param name Name of the desired character
     * @return The uuid, if present
     */
    public Optional<UUID> validCharacter(String name) {
        CharacterInformation[] informations = getConfiguration().getCharacters();
        for (CharacterInformation characterInformation : informations) {
            if (characterInformation.getName().equals(name))
                return Optional.of(characterInformation.getId());
        }
        return Optional.empty();
    }

    /**
     * Decode a choice from the user
     * 
     * @param id gadget or character-name
     * @return The choice as 'either'
     */
    public ImmutablePair<UUID, GadgetEnum> decodeGadgetCharacter(String id) {
        try {
            // is it a Gadget?
            GadgetEnum gadgetEnum = GadgetEnum.valueOf(id.toUpperCase());
            return ImmutablePair.of(null, gadgetEnum);
        } catch (IllegalArgumentException ignored) {
            // is it a Character?
            Optional<UUID> character = validCharacter(id);
            if (character.isPresent()) {
                return ImmutablePair.of(character.get(), null);
            } else {
                throw new StoryException("The id: '" + id + "' is neither a gadget nor a character-name");
            }
        }
    }

    /**
     * Decode a choice from the user
     * 
     * @param id gadget-name or character-uuid
     * @return The choice as 'either'
     */
    public ImmutablePair<UUID, GadgetEnum> decodeGadgetCharacterUuid(String id) {
        try {
            // is it a Gadget?
            GadgetEnum gadgetEnum = GadgetEnum.valueOf(id.toUpperCase());
            return ImmutablePair.of(null, gadgetEnum);
        } catch (IllegalArgumentException ignored) {
            // is it a Character?
            try {
                UUID character = UUID.fromString(id);
                return ImmutablePair.of(character, null);
            } catch (IllegalArgumentException ignored2) {
                throw new StoryException("The id: '" + id + "' is neither a gadget nor a character-name.");
            }
        }
    }

    /**
     * @return True if the story will escalate errors, false otherwise
     */
    public boolean canEscalate() {
        return this.escalate;
    }
}
