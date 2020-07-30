package de.uulm.team020.server.core.datatypes;

import java.io.Serializable;

import de.uulm.team020.datatypes.util.AbstractPair;
import de.uulm.team020.networking.core.MessageContainer;

/**
 * Implements a Message-Target Pair based on
 * {@link de.uulm.team020.datatypes.util.ImmutablePair ImmutablePair}
 * 
 * @author Florian Sihler
 * @version 1.1, 03/31/2020
 */
public class MessageTargetPair extends AbstractPair<MessageContainer, MessageTarget> implements Serializable {

    private static final long serialVersionUID = -8777902357279051998L;

    /**
     * Initialize a new Pair
     * 
     * @param message The message that should be send
     * @param targets the targets this message should be send to
     */
    public MessageTargetPair(MessageContainer message, MessageTarget targets) {
        super(message, targets);
    }

    public MessageContainer getMessage() {
        return this.getLeft();
    }

    /**
     * Not supported for an immutable Variant
     * 
     * @param ignored -ignored-
     */
    public void setKey(Object ignored) {
        throw new UnsupportedOperationException("This pair is immutable");
    }

    public MessageTarget getTargets() {
        return this.getRight();
    }

    /**
     * Not supported for an immutable Variant
     * 
     * @param ignored -ignored-
     */
    public void setValue(Object ignored) {
        throw new UnsupportedOperationException("This pair is immutable");
    }

    // Some operations for convenience

    /**
     * @return Is the Message a Unicast? Then true, false otherwise.
     */
    public boolean isUnicast() {
        return this.getTargets().isUnicast();
    }

    /**
     * @return Is the Message a Broadcast? Then true, false otherwise.
     */
    public boolean isBroadcast() {
        return this.getTargets().isBroadcast();
    }
}