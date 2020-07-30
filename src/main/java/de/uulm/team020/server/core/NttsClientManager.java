package de.uulm.team020.server.core;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.enumerations.GamePhaseEnum;
import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.configuration.Configuration;
import de.uulm.team020.server.core.datatypes.GameRoleEnum;
import de.uulm.team020.server.core.datatypes.MessageTarget;
import de.uulm.team020.server.core.datatypes.NttsClientConnection;
import de.uulm.team020.server.core.dummies.story.BackstoryBuilder;
import de.uulm.team020.server.core.dummies.story.StoryMethod;
import de.uulm.team020.server.core.exceptions.management.ClientIdAlreadyPresentException;
import de.uulm.team020.server.core.exceptions.management.PlayerSlotsFullException;

/**
 * This class does not just hold which client has which role, it will also
 * maintain a map which links the assigned UUID(s) to their destination
 * connection.
 * 
 * @author Florian Sihler
 * @version 1.1b, 06/12/2020
 */
public class NttsClientManager {

    private static final IMagpie magpie = Magpie.createMagpieSafe("Server");
    private static final String MANAGE_TXT = "Manage";

    private final Configuration configuration;

    private final Set<NttsClientConnection> spectatorConnections;

    private NttsClientConnection playerOne;
    private NttsClientConnection playerTwo;

    private final ReplayRecordKeeper recordKeeper;

    public NttsClientManager(final Configuration configuration) {
        this.configuration = configuration;
        spectatorConnections = new LinkedHashSet<>();
        this.recordKeeper = new ReplayRecordKeeper(this);
    }

    private void registerAsPlayer(final NttsClientConnection connection) throws PlayerSlotsFullException {
        if (this.playerOne == null) {
            registerConnectionAsPlayerOne(connection);
        } else if (this.playerTwo == null) {
            registerConnectionAsPlayerTwo(connection);
        } else {
            throw new PlayerSlotsFullException(playerOne.getClientName(), playerTwo.getClientName());
        }
    }

    private void registerConnectionAsPlayerTwo(final NttsClientConnection connection) {
        this.playerTwo = connection;
        magpie.writeInfo("Registered as Player 2 (" + connection.getClientName() + ")", MANAGE_TXT);
        connection.setGameRole(GameRoleEnum.PLAYER_TWO);
        configuration.shiftPhase(GamePhaseEnum.WAIT_FOR_GAME_START);
    }

    private void registerConnectionAsPlayerOne(final NttsClientConnection connection) {
        this.playerOne = connection;
        magpie.writeInfo("Registered as Player 1 (" + connection.getClientName() + ")", MANAGE_TXT);
        connection.setGameRole(GameRoleEnum.PLAYER_ONE);
    }

    private void removeAsPlayer(final NttsClientConnection connection) {
        if (Objects.equals(this.playerOne, connection)) {
            removeConnectionAsPlayerOne(connection);
        } else if (Objects.equals(this.playerTwo, connection)) {
            removeConnectionAsPlayerTwo(connection);
        }
    }

    private void removeConnectionAsPlayerTwo(final NttsClientConnection connection) {
        this.playerTwo = null;
        connection.setGameRole(null);
        recordKeeper.resetPlayerTwoRecord();
        magpie.writeInfo("Removed as Player 2 (" + connection.getClientName() + ")", MANAGE_TXT);
        ifGameDidNotStartWaitForPlayers();
    }

    private void removeConnectionAsPlayerOne(final NttsClientConnection connection) {
        this.playerOne = null;
        connection.setGameRole(null);
        recordKeeper.resetPlayerOneRecord();
        magpie.writeInfo("Removed as Player 1 (" + connection.getClientName() + ")", MANAGE_TXT);
        ifGameDidNotStartWaitForPlayers();
    }

    private void ifGameDidNotStartWaitForPlayers() {
        if (configuration.getGamePhase().isBefore(GamePhaseEnum.GAME_START)) {
            configuration.shiftPhase(GamePhaseEnum.WAIT_FOR_PLAYERS);
        }
    }

    /**
     * Register a new connection
     * 
     * @param newConnection the connection that should be registered
     * @return True, if the connection was added; False if a connection with this
     *         UUID is already registered, in this case it will not override the
     *         existing one!
     * 
     * @throws PlayerSlotsFullException        if there are already two players
     * @throws ClientIdAlreadyPresentException if there is already a client with
     *                                         this id
     */
    public boolean registerConnection(final NttsClientConnection newConnection)
            throws PlayerSlotsFullException, ClientIdAlreadyPresentException {
        if (getConnectionByUUID(newConnection.getClientId()) != NttsClientConnection.VOID_CONNECTION)
            throw new ClientIdAlreadyPresentException(newConnection.getClientId());
        // Is it a player, assign to the player-core
        if (newConnection.getClientRole() == RoleEnum.PLAYER || newConnection.getClientRole() == RoleEnum.AI) {
            registerAsPlayer(newConnection);
        } else {
            spectatorConnections.add(newConnection);
            newConnection.setGameRole(GameRoleEnum.SPECTATOR);
        }
        magpie.writeInfo("The connection with UUID '" + newConnection.getClientId() + "' ("
                + newConnection.getConnection().getRemoteSocketAddress() + ") registered with metadata: "
                + newConnection, MANAGE_TXT);
        return true;
    }

    /**
     * Inject a connection which will overwrite the pre-existing one. Will reset the
     * {@link ReplayRecordKeeper}-buffer.
     * 
     * @param connection Connection to inject
     * @param wanted     Role it should take.
     * 
     * @StoryMethod Used by the {@link BackstoryBuilder} to enforce connections.
     */
    @StoryMethod
    public void injectConnection(final NttsClientConnection connection, final GameRoleEnum wanted) {
        magpie.writeWarning("Injecting a Connection: " + connection, MANAGE_TXT);
        switch (wanted) {
            case PLAYER_ONE:
                if (this.playerOne != null) {
                    recordKeeper.resetPlayerOneRecord();
                }
                this.playerOne = connection;
                connection.setGameRole(GameRoleEnum.PLAYER_ONE);
                return;
            case PLAYER_TWO:
                if (this.playerTwo != null) {
                    recordKeeper.resetPlayerOneRecord();
                }
                this.playerTwo = connection;
                connection.setGameRole(GameRoleEnum.PLAYER_TWO);
                return;
            default:
            case SPECTATOR:
                spectatorConnections.add(connection);
                connection.setGameRole(GameRoleEnum.SPECTATOR);
                return;
        }
    }

    /**
     * Removes a Connection from {@link #spectatorConnections} or the players
     * 
     * @param oldConnection the connection to remove
     * @return True, if the connection could be removed; False if the connection was
     *         not to be found in {@link #spectatorConnections}/players
     */
    public boolean removeConnection(final NttsClientConnection oldConnection) {
        if (getConnectionByUUID(oldConnection.getClientId()) == NttsClientConnection.VOID_CONNECTION)
            return false;

        if (oldConnection.getClientRole() == RoleEnum.PLAYER || oldConnection.getClientRole() == RoleEnum.AI) {
            removeAsPlayer(oldConnection);
        } else {
            spectatorConnections.remove(oldConnection);
            oldConnection.setGameRole(null);
        }

        magpie.writeInfo("The connection with '" + oldConnection.getClientName() + "' was removed.", MANAGE_TXT);
        return true;
    }

    /**
     * Removes a Connection from {@link #spectatorConnections} or the players, by
     * using the attached WebSocket.
     * 
     * @param oldSocket the connection to remove
     * @return True, if the connection could be removed; False if the connection was
     *         not to be found in {@link #spectatorConnections}/players
     * 
     * @see #removeConnection(NttsClientConnection)
     */
    public boolean removeConnection(final WebSocket oldSocket) {
        final NttsClientConnection connection = oldSocket.getAttachment();
        if (connection == null)
            return false;
        return removeConnection(connection);
    }

    @Override
    public String toString() {
        return "NttsClientManager [playerOne=" + playerOne + ", playerTwo=" + playerTwo + ", spectators="
                + spectatorConnections + "]";
    }

    /**
     * @return Number of spectators
     */
    public int spectatorCount() {
        return spectatorConnections.size();
    }

    /**
     * Searches in {@link #spectatorConnections} for the wanted UUID. Currently it
     * will just perform a linear search, as the maximum number of connection is
     * deemed to be seriously limited.
     * 
     * @param target the target uuid
     * @return {@link NttsClientConnection#VOID_CONNECTION}, if the UUID was not to
     *         be found, the linked connection otherwise
     */
    public NttsClientConnection getConnectionByUUID(final UUID target) {
        if (playerOne != null && playerOne.getClientId().equals(target))
            return playerOne;
        if (playerTwo != null && playerTwo.getClientId().equals(target))
            return playerTwo;
        for (final NttsClientConnection nttsClientConnection : spectatorConnections) {
            if (nttsClientConnection.getClientId().equals(target))
                return nttsClientConnection;
        }
        return NttsClientConnection.VOID_CONNECTION; // Not found
    }

    /**
     * Searches in {@link #spectatorConnections} for the wanted UUID. Uses
     * {@link #getConnectionByUUID(UUID)} and is a shortcut for just getting the
     * connection of the linked socket
     * 
     * @param target the target uuid
     * @return null, if the UUID was not to be found, the linked connection
     *         otherwise
     * 
     * @see #getConnectionByUUID(UUID)
     */
    public WebSocket getWebSocketByUUID(final UUID target) {
        return getConnectionByUUID(target).getConnection();
    }

    /**
     * Searches in {@link #spectatorConnections} for the wanted WebSocket. Currently
     * it will just perform a linear search, as the maximum number of connection is
     * deemed to be seriously limited.
     * 
     * @param target the target web socket
     * @return null, if the web socket was not to be found, the linked connection
     *         otherwise
     */
    public UUID getUUIDByWebSocket(final WebSocket target) {
        final NttsClientConnection got = getConnectionByWebSocket(target);
        return got == null ? null : got.getClientId();
    }

    /**
     * Searches in {@link #spectatorConnections} for the wanted WebSocket. Currently
     * it will just perform a linear search, as the maximum number of connection is
     * deemed to be seriously limited.
     * 
     * @param target the target web socket
     * @return null, if the WebSocket was not to be found, the linked connection
     *         otherwise
     */
    public NttsClientConnection getConnectionByWebSocket(final WebSocket target) {
        if (playerOne != null && playerOne.getConnection().equals(target)) {
            return playerOne;
        }
        if (playerTwo != null && playerTwo.getConnection().equals(target)) {
            return playerTwo;
        }
        for (final NttsClientConnection nttsClientConnection : spectatorConnections) {
            if (nttsClientConnection.getConnection().equals(target))
                return nttsClientConnection;
        }
        return null; // Not found
    }

    /**
     * Search if a name is already taken
     * 
     * @param name the name to test for
     * 
     * @return True if the name is already taken.
     */
    public boolean hasName(final String name) {
        return (playerOne != null && playerOne.getClientName().equals(name))
                || (playerTwo != null && playerTwo.getClientName().equals(name))
                || this.spectatorConnections.stream().anyMatch(n -> n.getClientName().equals(name));
    }

    /**
     * @return all connections in {@link #spectatorConnections}/the players. <b>Do
     *         not alter!</b>
     */
    public Set<NttsClientConnection> getAllConnections() {
        final Set<NttsClientConnection> goal = new LinkedHashSet<>(spectatorConnections);
        if (playerOne != null)
            goal.add(playerOne);
        if (playerTwo != null)
            goal.add(playerTwo);
        return goal;
    }

    /**
     * Returns the set of connections in {@link #spectatorConnections}/the players,
     * applying to the filter.
     * 
     * @param target The target specification which will be decoded
     * 
     * @return All connections in {@link #spectatorConnections}/the player, that
     *         apply to the filter. <b>Do not alter!</b>
     */
    public Set<NttsClientConnection> getConnectionsByFilter(final MessageTarget target) {
        return getConnectionsByFilter(target.isAllPlayers(), target.isAllHumans(), target.isAllAIs(),
                target.isAllSpectators());
    }

    private Set<NttsClientConnection> getConnectionsByFilter(final boolean allPlayers, final boolean allHumans,
            final boolean allAIs, final boolean allSpectators) {
        final Set<NttsClientConnection> goal = new LinkedHashSet<>();
        if (playerOne != null) {
            if (isPlayerOnFilter(playerOne, allPlayers, allHumans))
                goal.add(playerOne);
            if (isAiOnFilter(playerOne, allPlayers, allAIs))
                goal.add(playerOne);
        }
        // I know... duplication, but it would more work to outsource this than it would
        // benefit
        if (playerTwo != null) {
            if (isPlayerOnFilter(playerTwo, allPlayers, allHumans))
                goal.add(playerTwo);
            if (isAiOnFilter(playerTwo, allPlayers, allAIs))
                goal.add(playerTwo);
        }
        if (allSpectators)
            goal.addAll(spectatorConnections);
        return goal;
    }

    private static final boolean isPlayerOnFilter(final NttsClientConnection connection, final boolean allPlayers,
            final boolean allHumans) {
        return connection.getClientRole() == RoleEnum.PLAYER && (allPlayers || allHumans);
    }

    private static final boolean isAiOnFilter(final NttsClientConnection connection, final boolean allPlayers,
            final boolean allAIs) {
        return connection.getClientRole() == RoleEnum.AI && (allPlayers || allAIs);
    }

    public NttsClientConnection getPlayerOne() {
        return this.playerOne;
    }

    public NttsClientConnection getPlayerTwo() {
        return this.playerTwo;
    }

    public Set<NttsClientConnection> getSpectatorConnections() {
        return spectatorConnections;
    }

    /**
     * @return Amount of registered players, will be 2 starting from gametart, this
     *         does not mean that they are currently connected, just that they are
     *         active.
     */
    public int getPlayerCount() {
        if (playerOne == null)
            return 0;
        return playerTwo == null ? 1 : 2;
    }

    /**
     * @return Array containing all names of all registered players. The length of
     *         this array wil equal {@link #getPlayerCount()}.
     */
    public String[] getPlayerNames() {
        if (playerOne == null) {
            return new String[0];
        }
        return playerTwo == null ? new String[] { playerOne.getClientName() }
                : new String[] { playerOne.getClientName(), playerTwo.getClientName() };
    }

    /**
     * Get the underlying record keeper for reply creation
     * 
     * @return The underlying record keeper
     */
    public ReplayRecordKeeper getRecordKeeper() {
        return this.recordKeeper;
    }

}