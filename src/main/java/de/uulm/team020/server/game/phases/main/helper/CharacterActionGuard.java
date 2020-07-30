package de.uulm.team020.server.game.phases.main.helper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import de.uulm.team020.datatypes.Character;
import de.uulm.team020.datatypes.Field;
import de.uulm.team020.datatypes.FieldMap;
import de.uulm.team020.datatypes.Gadget;
import de.uulm.team020.datatypes.WiretapWithEarplugs;
import de.uulm.team020.datatypes.enumerations.GadgetEnum;
import de.uulm.team020.datatypes.enumerations.PropertyEnum;
import de.uulm.team020.datatypes.util.Point;
import de.uulm.team020.helper.RandomHelper;
import de.uulm.team020.helper.pathfinding.Path;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.addons.random.enums.RandomOperation;
import de.uulm.team020.server.configuration.ServerConfiguration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;
import de.uulm.team020.server.core.exceptions.handler.IllegalMessageException;
import de.uulm.team020.server.game.phases.main.GameFieldController;
import de.uulm.team020.server.game.phases.main.GameFieldOperationException;

/**
 * This class contains a set of guarding routines to avoid redundancy in
 * possibility checking. E.g. This class will perform tests, validating if a
 * target field would be selectable or if the Character is even able to do an
 * action in it's current state.
 * <p>
 * This class will use the {@link HandlerException}-Hierarchy to escape any
 * errors - this means it most likely will construct an
 * {@link IllegalMessageException} informing the user about it's mistakes - it
 * would be possible and easy to change this behaviour. Please note, that if the
 * errors affect the game-field or the controller (mismatch with database, ...)
 * this guard is allowed to escalate with a {@link GameFieldOperationException}
 * which will be re-thrown as a {@link HandlerException} most of the time, but
 * can be handled differently.
 *
 * @author Florian Sihler
 * @version 1.0, 05/01/2020
 */
public class CharacterActionGuard {

    private static final String ROUND_TXT = "Round";
    private static final String TURN_TXT = "Turn";
    private static final String TARGET_TXT = "The desired target: ";
    private static final String CHARACTER_TXT = "Character: ";
    private static final IMagpie magpie = Magpie.createMagpieSafe("Server-Main-Game");
    private final GameFieldController controller;

    /**
     * Build a new guard.
     *
     * @param controller The controller that shall be used
     */
    public CharacterActionGuard(GameFieldController controller) {
        this.controller = controller;
    }

    /**
     * Checks if a is a neighbour or a neighbour of a neighbour of b
     *
     * @param a Point a to check
     * @param b Point b to check
     * @return True if the constraint is fulfilled
     */
    public static boolean isNeighbourOfNeighbour(Point a, Point b) {
        return b.isNeighbour(a) || Arrays.stream(a.getNeighbours()).anyMatch(p -> p.isNeighbour(b));
    }

    /**
     * Checks if a is a neighbour or a neighbour of a neighbour of b, but also
     * validates, that the intermediate neighbour (in case there is one) is walkable
     *
     * @param a   Point a to check
     * @param b   Point b to check
     * @param map The map to check the walkable constraint for
     * @return True if the constraint is fulfilled
     */
    public static boolean isNeighbourOfValidNeighbour(Point a, Point b, FieldMap map) {
        return b.isNeighbour(a) || Arrays.stream(a.getNeighbours()).filter(p -> p.isOnField(map))
                .anyMatch(p -> map.getSpecificField(p).isWalkable() && p.isNeighbour(b));
    }

    /**
     * Checks if a character is the cat or the janitor
     *
     * @param character The character to check
     * @return True if and only if the character is the cat or the janitor
     */
    public boolean isCatOrJanitor(Character character) {
        return isCat(character) || isJanitor(character);
    }

    /**
     * Checks if a character is the cat
     *
     * @param character The character to check
     * @return True if and only if the character is the cat
     */
    public boolean isCat(Character character) {
        return Objects.equals(controller.getCat(), character);
    }

    /**
     * Checks if a character is the janitor
     *
     * @param character The character to check
     * @return True if and only if the character is the janitor
     */
    public boolean isJanitor(Character character) {
        return Objects.equals(controller.getJanitor(), character);
    }

    /**
     * This method will use {@link RandomHelper#flip(double)} but will re do the
     * flip if it fails and the user has working 'Tradecraft'-skill.
     *
     * @param character The character that wants to perform the flip
     * @param chance    The chance the flip works (between 0 and 1)
     * @return True if the flip worked, false otherwise
     */
    public boolean flip(Character character, double chance) {
        // If the character has clammy clothes - chances will be cut
        if (hasClammyOrConstantClammyClothes(character)) {
            chance = chance / 2;
        }

        if (controller.getRandomController().requestFlip(character.getName(), chance,
                RandomOperation.OPERATION_SUCCESS)) {
            return true;
        }
        // can the character perform a second clip?
        // Note: the moledie-check is redundant as it shall get removed on constructed
        if (character.getProperties().contains(PropertyEnum.TRADECRAFT)
                && !character.getGadgetType(GadgetEnum.MOLEDIE).isPresent()) {
            magpie.writeDebug("Flipping again for: " + character + " as active Tradecraft-Property triggered",
                    ROUND_TXT);
            return controller.getRandomController().requestFlip(character.getName(), chance,
                    RandomOperation.OPERATION_SUCCESS);
        }
        return false;
    }

    private boolean hasClammyOrConstantClammyClothes(Character character) {
        return character.getProperties().contains(PropertyEnum.CLAMMY_CLOTHES)
                || character.getProperties().contains(PropertyEnum.CONSTANT_CLAMMY_CLOTHES);
    }

    /**
     * This method will match the uuid against the characters present in the current
     * list - if there is a character with this id it will return the character
     * otherwise this method will fail with an exception.
     *
     * @param characterId The id of the character that you search for
     * @return The character if found - will never be null as this will result in
     *         the exception.
     * @throws GameFieldOperationException Thrown if the character was not to be
     *                                     found.
     */
    public Character assureValidCharacter(final UUID characterId) throws GameFieldOperationException {
        return assureValidCharacter(characterId, "");
    }

    /**
     * This method will match the uuid against the characters present in the current
     * list - if there is a character with this id it will return the character
     * otherwise this method will fail with an exception.
     *
     * @param characterId The id of the character that you search for
     * @param tried       Some text describing the operation this access was
     *                    desired. It will be included in the Exception if the
     *                    request fails.
     * @return The character if found - will never be null as this will result in
     *         the exception.
     * @throws GameFieldOperationException Thrown if the character was not to be
     *                                     found.
     */
    public Character assureValidCharacter(final UUID characterId, String tried) throws GameFieldOperationException {
        Optional<Character> mayCharacter = controller.decodeCharacterById(characterId);
        if (mayCharacter.isPresent()) {
            return Objects.requireNonNull(mayCharacter.get());
        }
        throw new GameFieldOperationException("You requested character with id: " + characterId
                + (tried == null || tried.isBlank() ? "" : " for operation: " + tried) + " which does not exist");
    }

    /**
     * This method will match the position against the characters present in the
     * current list - if there is a character with this position it will return the
     * character otherwise this method will fail with an exception.
     *
     * @param characterPos The position of the character that you search for
     * @return The character if found - will never be null as this will result in
     *         the exception.
     * @throws GameFieldOperationException Thrown if the character was not to be
     *                                     found.
     */
    public Character assureValidCharacter(final Point characterPos) throws GameFieldOperationException {
        return assureValidCharacter(characterPos, "");
    }

    /**
     * This method will match the position against the characters present in the
     * current list - if there is a character with this position it will return the
     * character otherwise this method will fail with an exception.
     *
     * @param characterPos The position of the character that you search for
     * @param tried        Some text describing the operation this access was
     *                     desired. It will be included in the Exception if the
     *                     request fails.
     * @return The character if found - will never be null as this will result in
     *         the exception.
     * @throws GameFieldOperationException Thrown if the character was not to be
     *                                     found.
     */
    public Character assureValidCharacter(final Point characterPos, String tried) throws GameFieldOperationException {
        Optional<Character> mayCharacter = controller.decodeCharacterByPosition(characterPos);
        if (mayCharacter.isPresent()) {
            return Objects.requireNonNull(mayCharacter.get());
        }
        throw new GameFieldOperationException("You requested character with the position: " + characterPos
                + (tried == null || tried.isBlank() ? "" : " for operation: " + tried) + " which does not exist");
    }

    /**
     * This will validate, that the character has enough ap to perform an action and
     * that the field the character stands on - or any other constraint - does not
     * block him from doing so.
     *
     * @param character The character that shall perform the action
     * @param map       The map to identify the current field of the character
     * @throws HandlerException If there is any reason the character cannot perform
     *                          this operation.
     */
    public void assureCanPerformAction(Character character, FieldMap map) throws HandlerException {
        assureCharacterIsNotExfiltratedAndHasAP(character);
        final Field field = map.getSpecificField(character.getCoordinates());
        if (field == null) {
            throw new IllegalMessageException(CHARACTER_TXT + character.getName()
                    + " is standing on an invalid field and is not allowed to do anything.");
        }
        if (field.isFoggy()) {
            throw new IllegalMessageException(CHARACTER_TXT + character.getName()
                    + " is standing on a foggy field and cannot perform any operation.");
        }
    }

    private void assureCharacterIsNotExfiltratedAndHasAP(Character character) throws IllegalMessageException {
        if (character.isExfiltrated()) {
            throw new IllegalMessageException(CHARACTER_TXT + character.getName()
                    + " is exfiltrated and therefore not allowed to do any actions.");
        }
        if (character.getAp() <= 0) {
            throw new IllegalMessageException(
                    CHARACTER_TXT + character.getName() + " cannot perform any action as it has no AP left.");
        }
    }

    /**
     * This will validate, that the character has enough mp to perform a movement
     *
     * @param character The character that shall perform the movement
     * @throws HandlerException If there is any reason the character cannot perform
     *                          this operation.
     */
    public void assureCanPerformMovement(Character character) throws HandlerException {
        if (character.isExfiltrated()) {
            throw new IllegalMessageException(CHARACTER_TXT + character.getName()
                    + " is exfiltrated and therefore not allowed to do any movements.");
        }
        if (character.getMp() <= 0) {
            throw new IllegalMessageException(
                    CHARACTER_TXT + character.getName() + " cannot perform any movement as it has no MP left.");
        }
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation.
     *
     * @param target The target point to validate
     * @param map    The map to retrieve the target field from
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidField(Point target, FieldMap map) throws HandlerException {
        return assureValidField(target, map, "");
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation.
     *
     * @param target The target point to validate
     * @param map    The map to retrieve the target field from
     * @param tried  A text which can explain the operation. This will be packed
     *               with the exception if there is the need to throw one.
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidField(Point target, FieldMap map, String tried) throws HandlerException {
        final String op = tried == null || tried.isBlank() ? "" : " for Operation: " + tried;

        if (target == null) {
            throw new IllegalMessageException("The desired target field was null" + op);
        }

        // check: target field is on the map
        if (!target.isOnField(map)) {
            throw new IllegalMessageException(TARGET_TXT + target + op + " is not on the field!");
        }

        return map.getSpecificField(target);
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation. Furthermore this will validate,
     * that the target-field is a neighbour of the current position
     *
     * @param target  The target point to validate
     * @param current The current field to check the neighbour-constraint
     * @param map     The map to retrieve the target field from
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidNeighbourField(Point target, Point current, FieldMap map) throws HandlerException {
        return assureValidNeighbourField(target, current, map, "");
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation.
     *
     * @param target  The target point to validate
     * @param current The current field to check the neighbour-constraint
     * @param map     The map to retrieve the target field from
     * @param tried   A text which can explain the operation. This will be packed
     *                with the exception if there is the need to throw one.
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidNeighbourField(Point target, Point current, FieldMap map, String tried)
            throws HandlerException {
        final Field field = assureValidField(target, map, tried);
        if (!target.isNeighbour(current)) {
            throw new IllegalMessageException(TARGET_TXT + target + " is no neighbour of the current field: " + current
                    + (tried == null || tried.isEmpty() ? "" : ". For: " + tried));
        }
        return field;
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is in line of sight.
     *
     * @param target  The target point to validate
     * @param current The current field to check the neighbour-constraint
     * @param map     The map to retrieve the target field from
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidFieldInLineOfSight(Point target, Point current, FieldMap map) throws HandlerException {
        return assureValidFieldInLineOfSight(target, current, map, "");
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is in line of sight.
     *
     * @param target  The target point to validate
     * @param current The current field to check the neighbour-constraint
     * @param map     The map to retrieve the target field from
     * @param tried   A text which can explain the operation. This will be packed
     *                with the exception if there is the need to throw one.
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     */
    public Field assureValidFieldInLineOfSight(Point target, Point current, FieldMap map, String tried)
            throws HandlerException {
        final Field field = assureValidField(target, map, tried);
        Path path = current.getLine(target);
        if (!path.isLineOfSight(map)) {
            throw new IllegalMessageException(TARGET_TXT + target + " is not in line of sight of the current field: "
                    + current + ". The created path was: " + path
                    + (tried == null || tried.isEmpty() ? "" : ". For: " + tried));
        }
        return field;
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation. In contrast to
     * {@link #assureValidNeighbourField(Point, Point, FieldMap, String)} this
     * allows a point being a neighbour of a neighbour if the character has the
     * Flaps and Seals-Trait. Therefore this guard is mainly used for safe-checks.
     *
     * @param target    The target point to validate
     * @param character The character to check the constraint for.
     * @param map       The map to retrieve the target field from
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     * @see #assureValidReachableField(Point, Character, FieldMap, String)
     * @see #isNeighbourOfNeighbour(Point, Point)
     */
    public Field assureValidReachableField(Point target, Character character, FieldMap map) throws HandlerException {
        return assureValidReachableField(target, character, map, "");
    }

    /**
     * Will assert that the point itself is valid, that it is on the field and that
     * the target field is valid for the operation. In contrast to
     * {@link #assureValidNeighbourField(Point, Point, FieldMap, String)} this
     * allows a point being a neighbour of a neighbour if the character has the
     * Flaps and Seals-Trait. Therefore this guard is mainly used for safe-checks.
     *
     * @param target    The target point to validate
     * @param character The character to check the constraint for.
     * @param map       The map to retrieve the target field from
     * @param tried     A text which can explain the operation. This will be packed
     *                  with the exception if there is the need to throw one.
     * @return The field, the target-point points to.
     * @throws HandlerException Thrown if the point or the field violated the
     *                          constraints.
     * @see #isNeighbourOfNeighbour(Point, Point)
     */
    public Field assureValidReachableField(Point target, Character character, FieldMap map, String tried)
            throws HandlerException {
        final Point current = character.getCoordinates();
        // if flaps and seals is disabled
        // Note: the moledie-check is redundant as it shall get removed on constructed
        if (!character.getProperties().contains(PropertyEnum.FLAPS_AND_SEALS)
                || character.getGadgetType(GadgetEnum.MOLEDIE).isPresent()) {
            return assureValidNeighbourField(target, current, map, tried);
        }
        // valid field itself:
        final Field field = assureValidField(target, map, tried);
        // check is neighbour or neighbour of neighbour
        if (controller.getConfiguration().getServerConfig().flapsAndSealsThroughWall()) {
            if (!isNeighbourOfNeighbour(current, target)) {
                throw new IllegalMessageException(TARGET_TXT + target
                        + " is no neighbour (or neighbour of neighbour) of the current field: " + current);
            }
        } else {
            // cannot fade through walls:
            if (!isNeighbourOfValidNeighbour(current, target, map)) {
                throw new IllegalMessageException(TARGET_TXT + target
                        + " is no neighbour (or neighbour of neighbour) of the current field by a walkable partner : "
                        + current);
            }
        }
        return field;
    }

    /**
     * Will check that the given character has the gadget in its inventory, and that
     * the gadget itself is useable.
     *
     * @param request   The requested gadget
     * @param character The character that wants to make the operation
     * @return The gadget the character has
     * @throws HandlerException If there was any exception (character does not have
     *                          the gadget...)
     */
    public Gadget assureValidGadget(GadgetEnum request, Character character) throws HandlerException {
        Optional<Gadget> mayGadget = character.getGadgetType(request);
        if (!mayGadget.isPresent()) {
            throw new IllegalMessageException(
                    "The character: " + character + " does not posses the requested gadget: " + request);
        }
        Gadget gadget = mayGadget.get();
        if (!gadget.hasUsagesLeft()) {
            throw new IllegalMessageException(
                    "The Gadget: " + gadget + " hold by the character: " + character + " cannot be used.");
        }
        return gadget;
    }

    /**
     * Will check that the given character has the property and that it is active.
     *
     * @param request   The requested property
     * @param character The character that wants to make the operation
     * @return The property - as given
     * @throws HandlerException If there was any exception (character does not have
     *                          the property...)
     */
    public PropertyEnum assureValidProperty(PropertyEnum request, Character character) throws HandlerException {
        if (!character.getProperties().contains(request)) {
            throw new IllegalMessageException(
                    "The character: " + character + " does not posses the requested property: " + request);
        }
        return request;
    }

    /**
     * Checks if the connection owns the given Character
     *
     * @param connection      The connection that the ownership should be checked
     * @param targetCharacter The character to check for
     * @return True if the connection (-role) owns the character, false otherwise
     * @throws GameFieldOperationException If the connection is not allowed to own
     *                                     characters (no player)
     */
    public boolean isOneOfMine(final NttsClientConnection connection, Character targetCharacter)
            throws GameFieldOperationException {
        switch (connection.getGameRole()) {
            case PLAYER_ONE:
                return controller.getPlayerOneFaction().contains(targetCharacter);
            case PLAYER_TWO:
                return controller.getPlayerTwoFaction().contains(targetCharacter);
            default:
                throw new GameFieldOperationException("Connection: " + connection + " wanted to check for: "
                        + targetCharacter + " but is no player.");
        }
    }

    /**
     * Checks if the character is owned by the enemy or is an npc. This is done by
     * checking {@link #isOneOfMine(NttsClientConnection, Character)} for being
     * negative and validating that the character is neither a cat nor a janitor by
     * {@link #isCatOrJanitor(Character)}.
     *
     * @param connection      The connection that the ownership should be checked
     * @param targetCharacter The character to check for
     * @return True if the connection (-role) owns the character, false otherwise
     * 
     * @throws GameFieldOperationException If the
     *                                     {@link #isOneOfMine(NttsClientConnection, Character)}-Request
     *                                     fails!
     */
    public boolean isEnemyOrNpc(final NttsClientConnection connection, Character targetCharacter)
            throws GameFieldOperationException {
        return !isOneOfMine(connection, targetCharacter) && !isCatOrJanitor(targetCharacter);
    }

    /**
     * Checks if the character is owned by the enemy. This is done by checking the
     * opponent's faction for the character and if it is neither cat or janitor.
     *
     * @param connection      The connection (used for determining faction)
     * @param targetCharacter The character to check for
     * 
     * @return True if the character is an enemy, false otherwise
     */
    public boolean isEnemy(final NttsClientConnection connection, Character targetCharacter) {
        return !isCatOrJanitor(targetCharacter)
                && ((connection.getGameRole() == GameRoleEnum.PLAYER_ONE ? controller.getPlayerTwoFaction()
                        : controller.getPlayerOneFaction()).contains(targetCharacter));
    }

    /**
     * This calculates the distance between given Points {@code a} and {@code b}.
     * The way this calculation is done is set via
     * {@link ServerConfiguration#matchconfigDistanceEuclidean()}.
     *
     * @param a The first point
     * @param b The second point
     * @return The distance between both points
     */
    public double getDistance(Point a, Point b) {
        if (controller.getConfiguration().getServerConfig().matchconfigDistanceEuclidean()) {
            return Point.getDistance(a, b);
        } else {
            // line
            return Point.getKingDistance(a, b);
        }
    }

    /**
     * This adds the given {@code ip}s to the passed {@code character}s ips. If any
     * wiretap with earplugs is active on it, the amount of added ips is added to
     * it's owners ips. Ips are added to statistics as well.
     *
     * @param character The character that receives the ips
     * @param ip        The ips to be added
     */
    public void addIpToCharacter(Character character, int ip) {
        addIpToCharacter(character, ip, true);
    }

    /**
     * This adds the given {@code ip}s to the passed {@code character}s ips. If any
     * wiretap with earplugs is active on it, the amount of added ips is added to
     * it's owners ips. Ips are added to statistics as well.
     *
     * @param character                   The character that receives the ips
     * @param ip                          The ips to be added
     * @param checkForWiretapWithEarplugs Whether it should be checked if any
     *                                    wiretap with earplugs gadget is active on
     *                                    the character which receives ips
     */
    public void addIpToCharacter(Character character, int ip, boolean checkForWiretapWithEarplugs) {
        // add ip
        magpie.writeDebug(CHARACTER_TXT + character + " receives " + ip + " ip.", TURN_TXT);
        character.addIp(Math.max(ip, 0));
        if (checkForWiretapWithEarplugs) {
            searchWiretapOwnerAndMayGiftHimIP(character, ip);
        }
        // add ip to statistics
        controller.decodePlayerGameRoleByCharacter(character)
                .ifPresent(gameRoleEnum -> controller.getStatisticsProvider().receivedIp(gameRoleEnum, ip));
    }

    private void searchWiretapOwnerAndMayGiftHimIP(Character character, int ip) {
        // search character with wiretap gadget
        for (Character mayOwnerCharacter : controller.getAllCharacters()) {
            final Optional<Gadget> mayWiretapWithEarplugs = mayOwnerCharacter
                    .getGadgetType(GadgetEnum.WIRETAP_WITH_EARPLUGS);
            if (mayWiretapWithEarplugs.isEmpty()) {
                // other character does not own the wiretap with earplugs gadget
                continue;
            }
            final WiretapWithEarplugs wiretapWithEarplugs = (WiretapWithEarplugs) mayWiretapWithEarplugs.get();
            if (checkIfCharacterHasCorrectEarplugs(character, wiretapWithEarplugs)) {
                // add ip to owner character
                magpie.writeDebug(
                        CHARACTER_TXT + mayOwnerCharacter + " is receiving " + ip + " ip because target character "
                                + character + " is active on wiretap with earplugs gadget and received ip...",
                        TURN_TXT);
                addIpToCharacter(mayOwnerCharacter, ip, false);
            }
        }
    }

    private boolean checkIfCharacterHasCorrectEarplugs(Character character,
            final WiretapWithEarplugs wiretapWithEarplugs) {
        return wiretapWithEarplugs.getWorking()
                && Objects.equals(wiretapWithEarplugs.getActiveOn(), character.getCharacterId());
    }

    /**
     * Gets all available characters in line of sight and given {@code range}.
     *
     * @param center The center point where line of sight is checked from
     * @param range  The allowed range
     * @return Set of points (character locations)
     */
    public Set<Point> getAllCharactersInLineOfSightWithRange(Point center, int range) {
        final Set<Point> res = getAllCharactersInLineOfSight(center);
        return res.stream().filter(p -> getDistance(center, p) <= range).collect(Collectors.toSet());
    }

    /**
     * Gets all available characters in line of sight.
     *
     * @param center The center point where line of sight is checked from
     * @return Set of points (character locations)
     */
    public Set<Point> getAllCharactersInLineOfSight(Point center) {
        final Set<Point> res = new HashSet<>();
        controller.getAllCharacters().stream().filter(
                c -> !isCatOrJanitor(c) && Point.getLine(center, c.getCoordinates()).isLineOfSight(controller.getMap()))
                .collect(Collectors.toSet()).forEach(c -> res.add(c.getCoordinates()));
        return res;
    }

}