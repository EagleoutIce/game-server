package de.uulm.team020.server.core.datatypes;

import java.util.UUID;

/**
 * Target of a Network-Message
 *
 * @author Florian Sihler
 * @version 1.1, 03/26/2020
 */
public class MessageTarget {

    private boolean allPlayers;
    private boolean allHumans;
    private boolean allAIs;
    private boolean allSpectators;

    private UUID unicastTarget;

    /**
     * Creates a new broadcast-Target
     */
    public MessageTarget() {
        this(true, true, true, true);
    }

    /**
     * Creates a new Target to be a Unicast Message identified by the UUID.
     * 
     * @param unicastTarget the UUID of the unicast target
     */
    public MessageTarget(UUID unicastTarget) {
        this(false, false, false, false);
        this.unicastTarget = unicastTarget;
    }

    /**
     * Specifies a list of targets for the connected message
     * 
     * @param allPlayers    the message should be send to all players (AI and
     *                      Humans)
     * @param allHumans     the message should be send to all human players. This
     *                      Message won't be send if there are no humans.
     * @param allAIs        the message should be send to all AIs
     * @param allSpectators the message should be send to all spectators
     */
    public MessageTarget(boolean allPlayers, boolean allHumans, boolean allAIs, boolean allSpectators) {
        this.allPlayers = allPlayers;
        this.allHumans = allHumans;
        this.allAIs = allAIs;
        this.allSpectators = allSpectators;
    }

    public boolean isAllPlayers() {
        return allPlayers;
    }

    public boolean isAllHumans() {
        return allHumans;
    }

    public boolean isAllAIs() {
        return allAIs;
    }

    public boolean isAllSpectators() {
        return allSpectators;
    }

    public boolean isBroadcast() {
        return (allPlayers || (allAIs && allHumans)) && allSpectators;
    }

    public boolean isUnicast() {
        return !(allPlayers || allSpectators || allHumans || allAIs);
    }

    public UUID getUnicastTarget() {
        return unicastTarget;
    }

}