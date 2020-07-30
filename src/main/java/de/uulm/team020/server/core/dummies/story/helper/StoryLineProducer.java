package de.uulm.team020.server.core.dummies.story.helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.OperationEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.networking.messages.MetaKeyEnum;
import de.uulm.team020.server.core.dummies.story.Injects;
import de.uulm.team020.server.core.dummies.story.Presets;
import de.uulm.team020.server.core.dummies.story.Story;
import de.uulm.team020.server.core.dummies.story.StoryAuthor;
import de.uulm.team020.server.core.dummies.story.StoryBoard;
import de.uulm.team020.server.core.dummies.story.StoryChapterType;

/**
 * This class will provide convenience-Methods to build up Story-Lines that can
 * be consumed by the {@link StoryAuthor} or the {@link StoryBoard}. As this
 * class is meant as a makro-container only, there will be static-methods only.
 * 
 * @author Florian Sihler
 * @version 1.1b, 06/12/2020
 * 
 * @since 1.1
 */
public class StoryLineProducer {

    private StoryLineProducer() {
    }

    /**
     * Just a readable way of register a new client in java.
     * 
     * @param name Name of the client
     * @param role role of the client
     * 
     * @return The produced StoryLine
     */
    public static StoryLine hello(final String name, final RoleEnum role) {
        return new StoryLine(StoryChapterType.HELLO, name, role.toString());
    }

    /**
     * Just a readable way of sending a dos-attack in java.
     * 
     * @param name        Name of the client, can be random
     * @param messageType The type of the Message, for random use
     * @param amount      Number of Attacks
     * @param delay       Number of ms between two attacks
     * 
     * @return The produced StoryLine
     * 
     * @see #dos(String, int, int)
     */
    public static StoryLine dos(final String name, final MessageTypeEnum messageType, final int amount,
            final int delay) {
        return new StoryLine(StoryChapterType.DOS, name, messageType.toString(), String.valueOf(amount),
                String.valueOf(delay));
    }

    /**
     * Just a readable way of sending a random-type dos-attack in java.
     * 
     * @param name   Name of the client, can be random
     * @param amount Number of Attacks
     * @param delay  Number of ms between two attacks
     * 
     * @return The produced StoryLine
     * 
     * @see #dos(String, MessageTypeEnum, int, int)
     */
    public static StoryLine dos(final String name, final int amount, final int delay) {
        return new StoryLine(StoryChapterType.DOS, name, Story.RANDOM, String.valueOf(amount), String.valueOf(delay));
    }

    /**
     * Just a readable way to allow errors (execution time)
     * 
     * @return The produced StoryLine
     */
    public static StoryLine allowErrors() {
        return new StoryLine(StoryChapterType.ALLOW_ERRORS);
    }

    /**
     * Just a readable way to forbid errors (execution time). This is the default
     * for the {@link StoryBoard}
     * 
     * @return The produced StoryLine
     */
    public static StoryLine forbidErrors() {
        return new StoryLine(StoryChapterType.FORBID_ERRORS);
    }

    /**
     * Perform an inject routine
     * 
     * @param injectType Type of the inject
     * @param target     The target for the inject
     * @param value      The value to inject for the given target
     * 
     * @return The produced StoryLine
     */
    public static StoryLine inject(final Injects injectType, final String target, final String value) {
        return new StoryLine(StoryChapterType.CONFIG_INJECT, injectType.getKey(), target, value);
    }

    /**
     * Execute a preset
     *
     * @param name the name of the preset you desire
     * 
     * @return The produced StoryLine
     */
    public static StoryLine preset(final String name) {
        return new StoryLine(StoryChapterType.PRESET, name);
    }

    /**
     * Execute a preset
     *
     * @param name the name of the preset you desire
     * @return The produced StoryLine
     */
    public static StoryLine preset(final Presets name) {
        return new StoryLine(StoryChapterType.PRESET, name.getName());
    }

    /**
     * Let a client crash
     *
     * @param name the name of the client to crash
     * @return The produced StoryLine
     */
    public static StoryLine crash(final String name) {
        return new StoryLine(StoryChapterType.CRASH, name);
    }

    /**
     * Let a client reconnect
     *
     * @param oldName The name of the client that shall reconnect
     * @param newName The name the story shall refer to the new client that
     *                reconnects
     * @return The produced StoryLine
     */
    public static StoryLine reconnect(final String oldName, final String newName) {
        return new StoryLine(StoryChapterType.RECONNECT, oldName, newName);
    }

    /**
     * Rename the fake-name for the story (client-specific)
     *
     * @param oldName The old name of the client
     * @param newName The name the story will have for him now
     * 
     * @return The produced StoryLine
     */
    public static StoryLine rename(final String oldName, final String newName) {
        return new StoryLine(StoryChapterType.RENAME, oldName, newName);
    }

    /**
     * Let a client request a pause
     *
     * @param name The name of the client that wants the pause
     * 
     * @return The produced StoryLine
     */
    public static StoryLine pause(final String name) {
        return new StoryLine(StoryChapterType.PAUSE, name);
    }

    /**
     * Let a client resume
     *
     * @param name The name of the client that wants to resume
     * 
     * @return The produced StoryLine
     */
    public static StoryLine resume(final String name) {
        return new StoryLine(StoryChapterType.RESUME, name);
    }

    /**
     * Let a client send the retire operation
     *
     * @param name The name of the client that wants to retire
     * 
     * @return The produced StoryLine
     */
    public static StoryLine retire(final String name) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.RETIRE.toString(), "<ignored>");
    }

    /**
     * Let a client send an operation
     *
     * @param args The generic arguments to pass to this operation
     * 
     * @return The produced StoryLine
     */
    public static StoryLine operation(final String... args) {
        return new StoryLine(StoryChapterType.OPERATION, args);
    }

    /**
     * Let the currently active client send the retire operation
     * 
     * @return The produced StoryLine
     */
    public static StoryLine retire() {
        return retire(Story.RANDOM);
    }

    /**
     * Let a client move
     *
     * @param name   The name of the client that wants to move
     * @param target The target of the movement operation
     * 
     * @return The produced StoryLine
     */
    public static StoryLine move(final String name, final Point target) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.MOVEMENT.toString(), target.toString());
    }

    /**
     * Let a client spy
     *
     * @param name   The name of the client that wants to move
     * @param target The target of the spy operation
     * 
     * @return The produced StoryLine
     */
    public static StoryLine spy(final String name, final Point target) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.SPY_ACTION.toString(), target.toString());
    }

    /**
     * Let a client use a gadget
     *
     * @param name   The name of the client that wants to move
     * @param target The target of the spy operation
     * @param gadget The gadget to use
     * 
     * @return The produced StoryLine
     */
    public static StoryLine gadget(final String name, final Point target, final GadgetEnum gadget) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.GADGET_ACTION.toString(),
                target.toString() + ",gadget:" + gadget);
    }

    /**
     * Let a client use a property
     *
     * @param name     The name of the client that wants to move
     * @param target   The target of the spy operation
     * @param property The property to use
     * 
     * @return The produced StoryLine
     */
    public static StoryLine property(final String name, final Point target, final PropertyEnum property) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.PROPERTY_ACTION.toString(),
                target.toString() + ",property:" + property);
    }

    /**
     * Let a client move
     *
     * @param name   The name of the client that wants to move
     * @param target The relative target of the movement operation
     * 
     * @return The produced StoryLine
     */
    public static StoryLine moveRelative(final String name, final Point target) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.MOVEMENT.toString(),
                "+" + target.toString());
    }

    /**
     * Let a client gamble
     *
     * @param name   The name of the client that wants to gamble
     * @param target The target of the movement operation
     * @param stake  The amount of chips to place
     * @return The produced StoryLine
     */
    public static StoryLine gamble(final String name, final Point target, int stake) {
        return new StoryLine(StoryChapterType.OPERATION, name, OperationEnum.GAMBLE_ACTION.toString(),
                target.toString() + ",stake:" + stake);
    }

    /**
     * Let a client send a meta-information request
     *
     * @param name The name of the client that wants to request the meta information
     * @param keys The wanted keys
     * 
     * @return The produced StoryLine
     */
    public static StoryLine meta(final String name, final MetaKeyEnum... keys) {
        return new StoryLine(StoryChapterType.META, name,
                Arrays.stream(keys).map(MetaKeyEnum::getKey).collect(Collectors.joining(",")));
    }

    /**
     * Let a client send a meta-information request
     *
     * @param name The name of the client that wants to request the meta information
     * @param keys The wanted keys
     * 
     * @return The produced StoryLine
     */
    public static StoryLine meta(final String name, final String... keys) {
        return new StoryLine(StoryChapterType.META, name, String.join(", ", keys));
    }

    /**
     * Just sleep for a certain time (on execution, if not sleepless)
     *
     * @param lengthInMs Time to sleep in ms
     * @return The produced StoryLine
     */
    public static StoryLine sleep(final int lengthInMs) {
        return new StoryLine(StoryChapterType.SLEEP, String.valueOf(lengthInMs));
    }

    /**
     * Just sleep for a certain time (on execution, if not sleepless)
     *
     * @param lengthInMs Time to sleep in ms
     * @return The produced StoryLine
     */
    public static StoryLine sleep(final long lengthInMs) {
        return new StoryLine(StoryChapterType.SLEEP, String.valueOf(lengthInMs));
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * @return The produced StoryLine
     */
    public static StoryLine set(final String variable, final String value) {
        return new StoryLine(StoryChapterType.SET, variable, value);
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * 
     * @return The produced StoryLine
     */
    public static StoryLine set(final String variable, final int value) {
        return new StoryLine(StoryChapterType.SET, variable, String.valueOf(value));
    }

    /**
     * Execute a set command
     *
     * @param variable The name of the variable
     * @param value    The value it shall be set to
     * 
     * @return The produced StoryLine
     */
    public static StoryLine set(final String variable, final boolean value) {
        return new StoryLine(StoryChapterType.SET, variable, String.valueOf(value));
    }

    /**
     * Will make a client leave the story
     *
     * @param name The name of the client that wants to leave
     * 
     * @return The produced StoryLine
     */
    public static StoryLine leave(String name) {
        return new StoryLine(StoryChapterType.LEAVE, name);
    }

    /**
     * Will make a client pick an item
     *
     * @param name The name of the client that wants to pick the item
     * @param item The item the client wants
     * 
     * @return The produced StoryLine
     */
    public static StoryLine item(String name, String item) {
        return new StoryLine(StoryChapterType.ITEM, name, item);
    }

    /**
     * Will make a client equip
     *
     * @param name      The name of the client that wants to equip
     * @param equipment The equipment the client wants
     * 
     * @return The produced StoryLine
     */
    public static StoryLine equip(String name, String[] equipment) {
        return new StoryLine(StoryChapterType.EQUIP, name, String.join(",", equipment));
    }

    /**
     * Will make a client equip
     *
     * @param name      The name of the client that wants to equip
     * @param equipment The equipment the client wants
     * 
     * @return The produced StoryLine
     */
    public static StoryLine equip(String name, Collection<String> equipment) {
        return new StoryLine(StoryChapterType.EQUIP, name, String.join(",", equipment));
    }

    /**
     * Start collecting
     *
     * @param joiner   The joiner to use
     * @param variable The variable name to store the ml string into
     * 
     * @return The produced StoryLine
     */
    public static StoryLine collectionStart(String joiner, String variable) {
        return new StoryLine(StoryChapterType.COLLECT_START, joiner.isEmpty() ? "\"\"" : joiner, variable);
    }


    /**
     * End collecting
     *
     * @return The produced StoryLine
     */
    public static StoryLine collectionEnd() {
        return new StoryLine(StoryChapterType.COLLECT_END);
    }
}