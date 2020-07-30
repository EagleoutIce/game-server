package de.uulm.team020.server.core.datatypes;

import java.util.UUID;

import de.uulm.team020.datatypes.enumerations.RoleEnum;
import de.uulm.team020.datatypes.util.AbstractPair;
import de.uulm.team020.server.configuration.Configuration;

/**
 * Implements a Filter-Handler Pair based on
 * {@link de.uulm.team020.datatypes.util.ImmutablePair ImmutablePair}
 * 
 * @param <R> The expected Return Type for the {@link IMessageHandler}
 * 
 * @author Florian Sihler
 * @version 1.0, 03/23/2020
 */
public class FilterHandlerPair<R> extends AbstractPair<IMessageFilter, IMessageHandler<R>> {

    /**
     * Initialize a new Pair
     * 
     * @param filter  the filter to be linked to this handler
     * @param handler the handler to be executed if filter applies
     */
    public FilterHandlerPair(IMessageFilter filter, IMessageHandler<R> handler) {
        super(filter, handler);
    }

    public IMessageFilter getFilter() {
        return this.getLeft();
    }

    /**
     * Direct forward to
     * {@link IMessageFilter#filter(Configuration, RoleEnum, UUID, int, boolean)}
     *
     * @param configuration The configuration used by the running controller
     * @param role          The role
     * @param playerId      The id
     * @param strikeCount   The current strike count
     * @param isConnected   is the player currently connected?
     *
     * @return Empty String, if the filter applies, the reason why it doesn't apply
     *         otherwise
     */
    public String surviveFilter(Configuration configuration, RoleEnum role, UUID playerId, int strikeCount,
            boolean isConnected) {
        return this.getLeft().filter(configuration, role, playerId, strikeCount, isConnected);
    }

    /**
     * Not supported for an immutable Variant
     * 
     * @param ignored -ignored-
     */
    public void setKey(Object ignored) {
        throw new UnsupportedOperationException("This pair is immutable");
    }

    public IMessageHandler<R> getHandler() {
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

}