package de.uulm.team020.server.core.datatypes;

import java.util.Objects;
import java.util.function.Function;

import org.java_websocket.WebSocket;

import de.uulm.team020.server.core.NttsController;
import de.uulm.team020.server.core.exceptions.handler.HandlerException;

/**
 * Defines the signature requirements for a Callback function You can simple
 * register this function with
 * 
 * @author Florian Sihler
 * @version 1.0, 03/22/2020
 */
@FunctionalInterface
public interface IMessageHandler<R> {

    /**
     * Call the Handler for a given function
     *
     * @param controller the controller which called the handler
     * @param conn       the connection this message was received
     * @param message    the message to parse
     * 
     * @return The data to process after the handling
     * 
     * @throws HandlerException If there's an error message to be sent that cancels
     *                          the connection
     */
    R apply(NttsController controller, WebSocket conn, String message) throws HandlerException;

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the {@code after} function to the result. If evaluation of
     * either function throws an exception, it is relayed to the caller of the
     * composed function.
     *
     * @param <V>   the type of output of the {@code after} function, and of the
     *              composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies
     *         the {@code after} function
     * @throws NullPointerException if after is null
     */

    default <V> IMessageHandler<V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        /* Change Signature here */
        return (NttsController controller, WebSocket conn, String message) -> after
                .apply(apply(controller, conn, message));
    }

}