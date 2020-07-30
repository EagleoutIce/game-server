package de.uulm.team020.server.core.datatypes;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.java_websocket.WebSocket;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.helper.DateHelper;
import de.uulm.team020.networking.messages.HelloMessage;

/**
 * Will be created by an NttsServer-instance, binding it's whole life-cycle to
 * the bound {@link WebSocket}. The binding will be achieved through
 * {@link WebSocket#setAttachment(Object)}.
 * <p>
 * Furthermore this instance will hold meta-information about the connected
 * Player and therefore holding e.g it's Role via the {@link RoleEnum}
 * <p>
 * It therefore can be used as a Filter with {@link IMessageFilter
 * iMessageFilter} to only trigger a Callback in certain circumstances.
 *
 * @author Florian Sihler
 * @version 1.3, 04/04/2020
 */
public class NttsClientConnection implements Serializable {

    private static final long serialVersionUID = -7696855335934225575L;

    /** UUID assigned to this endpoint */
    private final UUID clientId;
    /** Name assigned to this endpoint */
    private String clientName;
    /** Role assigned to this endpoint */
    private final RoleEnum clientRole;
    /** The assigned connection */
    private transient WebSocket connection;
    /** Timestamp, the connection was established */
    private final Date firstContactDate;

    /** Specify the in-game-Role */
    private GameRoleEnum gameRole = null;

    /** Stating if the player is currently connected - or not */
    private boolean currentlyConnected;

    /** How many strikes did the user get? */
    private int strikesOwned;

    /**
     * Create a new MetaData-Set to attach to an existing WebSocket-Connection.
     * <p>
     * <i>Important note:</i> You'll have to attach this connection by yourself. To
     * provide this as an 'addon'-only data type it is necessary to do the
     * attachment yourself:
     * 
     * <pre>
     * <code>
     *NttsClientConnection connection = new NttsClientConnection(conn, message);
     *conn.setAttachment(connection);
     * </code>
     * </pre>
     * 
     * The UUID will be created randomized with {@code UUID.randomUUID()}.
     * 
     * @param conn    the connection this client operations on.
     * @param message the message the client used to start registration.
     */
    public NttsClientConnection(WebSocket conn, HelloMessage message) {
        this(conn, UUID.randomUUID(), message.getName(), message.getRole());
    }

    /**
     * Create a new MetaData-Set to attach to an existing WebSocket-Connection.
     * <p>
     * <i>Important note:</i> You'll have to attach this connection by yourself. To
     * provide this as an 'addon'-only data type it is necessary to do the
     * attachment yourself:
     * 
     * <pre>
     * <code>NttsClientConnection connection = new NttsClientConnection(conn, clientId, name, role);
     *conn.setAttachment(connection);</code>
     * </pre>
     *
     * @param conn     the connection this client operations on.
     * @param clientId the ID of the client
     * @param name     name of the client
     * @param role     role of the client
     */
    public NttsClientConnection(WebSocket conn, UUID clientId, String name, RoleEnum role) {
        this.clientId = clientId;
        this.clientName = name;
        this.clientRole = role;
        this.connection = conn;

        firstContactDate = DateHelper.now();
        this.currentlyConnected = true;

        strikesOwned = 0;
    }

    /**
     * Will build a new Connection from custom data - will not be used by the main
     * controller
     * 
     * @param conn     The connection this one is bound to
     * @param clientId The id of the client
     * @param name     The name of the client
     * @param role     The role of the client
     * 
     * @return The connection constructed
     */
    public static NttsClientConnection buildFromData(WebSocket conn, UUID clientId, String name, RoleEnum role) {
        return new NttsClientConnection(conn, clientId, name, role);
    }

    /**
     * Will build a new Connection from custom data - will not be used by the main
     * controller
     * 
     * @param conn     The connection this one is bound to
     * @param clientId The id of the client
     * @param name     The name of the client
     * @param role     The role of the client
     * @param gameRole The gameRole this client should take
     * 
     * @return The connection constructed
     */
    public static NttsClientConnection buildFromData(WebSocket conn, UUID clientId, String name, RoleEnum role,
            GameRoleEnum gameRole) {
        NttsClientConnection connection = new NttsClientConnection(conn, clientId, name, role);
        connection.setGameRole(gameRole);
        return connection;
    }

    public UUID getClientId() {
        return this.clientId;
    }

    public String getClientName() {
        return this.clientName;
    }

    public RoleEnum getClientRole() {
        return this.clientRole;
    }

    public Date getFirstContactDate() {
        return this.firstContactDate;
    }

    public WebSocket getConnection() {
        return this.connection;
    }

    public boolean isCurrentlyConnected() {
        return this.currentlyConnected;
    }

    public void setCurrentlyConnected(boolean currentlyConnected) {
        this.currentlyConnected = currentlyConnected;
    }

    public void setConnection(WebSocket connection) {
        this.connection = connection;
    }

    public void changeClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return how many strikes did the client already get?
     */
    public synchronized int getStrikesOwned() {
        return strikesOwned;
    }

    /**
     * Change the strike-value
     * 
     * @param strikesOwned new strike-value
     */
    public synchronized void setStrikesOwned(int strikesOwned) {
        this.strikesOwned = strikesOwned;
    }

    public boolean setGameRole(GameRoleEnum gameRole) {
        if (this.gameRole != null && gameRole != null)
            return false;
        this.gameRole = gameRole;
        return true;
    }

    public GameRoleEnum getGameRole() {
        return gameRole;
    }

    /**
     * The probably more readable constant for 'no connection'. <b>DO NOT
     * CHANGE!</b>
     */
    public static final NttsClientConnection VOID_CONNECTION = new NttsClientConnection(null, null, null, null);

    @Override
    public String toString() {
        return "NttsClientConnection (" + connection + ") [clientId=" + clientId + ", clientName=" + clientName
                + ", clientRole=" + clientRole + ", gameRole=" + gameRole + ", firstContactDate=" + firstContactDate
                + ", strikesOwned=" + strikesOwned + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((clientId == null) ? 0 : clientId.hashCode());
        result = prime * result + strikesOwned;
        return result;
    }

    /**
     * Two connections are said to be equal, if their {@link #clientId clientIds}
     * are...
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NttsClientConnection other = (NttsClientConnection) obj;
        if (clientId == null) {
            if (other.clientId != null)
                return false;
        } else if (!clientId.equals(other.clientId))
            return false;
        return strikesOwned == other.strikesOwned;
    }

}