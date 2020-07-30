package de.uulm.team020.server.client.data;

import java.net.URI;
import java.util.Date;
import java.util.UUID;

import de.uulm.team020.datatypes.IAmJson;

/**
 * Just a simple Data class which may be used in client mode to represent data
 * entered with the client-gui.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class UserSessionData implements IAmJson {

    private static final long serialVersionUID = 6047891713273069545L;

    // This is used for recovery!
    private String userName;

    private UUID clientId;
    private UUID sessionId;

    private Date creationDate;

    private URI serverTarget;

    /**
     * Create a new SessionData-Container
     */
    public UserSessionData() {
        super();
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the clientId
     */
    public UUID getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the sessionId
     */
    public UUID getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the creationDate
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate the creationDate to set
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("UserSessionData [clientId=").append(clientId).append(", creationDate=").append(creationDate)
                .append(", sessionId=").append(sessionId).append(", userName=").append(userName).append("]");
        return builder.toString();
    }

    /**
     * @return the serverTarget
     */
    public URI getServerTarget() {
        return serverTarget;
    }

    /**
     * @param serverTarget the serverTarget to set
     */
    public void setServerTarget(URI serverTarget) {
        this.serverTarget = serverTarget;
    }

}