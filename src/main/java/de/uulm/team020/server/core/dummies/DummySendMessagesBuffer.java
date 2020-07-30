package de.uulm.team020.server.core.dummies;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.networking.core.MessageTypeEnum;
import de.uulm.team020.server.core.exceptions.ExpectationException;
import de.uulm.team020.validation.GameDataGson;

/**
 * This is used to allow easier naming and to include the lock
 *
 * @author Florian Sihler
 * @version 1.2b, 04/14/2020
 */
public class DummySendMessagesBuffer extends LinkedList<String> {
    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    private static final long serialVersionUID = 3704368728754865108L;

    private final transient DummyClient client;
    private transient Consumer<String> onChange;

    /**
     * Creates a new Buffer, bound to no client.
     *
     * @see #DummySendMessagesBuffer(DummyClient)
     */
    public DummySendMessagesBuffer() {
        this(null);
    }

    /**
     * Creates a new buffer bound to a client
     *
     * @param client The client this one is bound to
     */
    public DummySendMessagesBuffer(final DummyClient client) {
        this(client, null);
    }

    /**
     * Creates a new buffer bound to a client
     *
     * @param client   The client this one is bound to
     * @param onChange Method to be called when value changes
     */
    public DummySendMessagesBuffer(final DummyClient client, final Consumer<String> onChange) {
        this.client = client;
        this.setConsumer(onChange);
    }

    /**
     * Adds the message in a synchronized block to the buffer
     * 
     * @param message the message to add
     * 
     * @return True if the add was successful, false otherwise.
     */
    @Override
    public boolean add(final String message) {
        if (client != null && client.isClosed()) { // won't write if the client is 'closed' for any reason
            magpie.writeDebug("Messages Buffer did toss: " + message + " as the master-client: " + client.getName()
                    + " was closed!", "add");
            return false;
        }
        synchronized (this) {
            final boolean feedback = super.add(message);
            if (onChange != null)
                onChange.accept(message);
            if (feedback)
                this.notifyAll();
            return feedback;
        }
    }

    /**
     * Sets or changes the consumer to be called on change, set this to 'null' to
     * remove any present consumer
     * 
     * @param onChange the consumer to be called
     * 
     * @return This instance of the buffer for chaining, if desired
     */
    public DummySendMessagesBuffer setConsumer(final Consumer<String> onChange) {
        this.onChange = onChange;
        return this;
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the given subsequence of a message buffer is of a given
     * type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types The expected sequence of types
     * 
     * @see #assertTypes(int, MessageTypeEnum...)
     */
    public void assertTypes(final MessageTypeEnum... types) {
        assertTypes(0, types);
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the message buffer is only of a given type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types The expected sequence of types
     * 
     * @see #assertTypes(MessageTypeEnum...)
     * @see #assertTypes(int, MessageTypeEnum...)
     */
    public void assertOnlyTypes(final MessageTypeEnum... types) {
        assertLength(types.length);
        assertTypes(0, types);
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the given subsequence of a message buffer is of a given
     * type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types The expected sequence of types
     * 
     * @see #assertTypes(int, MessageTypeEnum...)
     */
    public void assertTypes(final MessageTypeEnum[]... types) {
        assertTypes(0, Arrays.stream(types).flatMap(Arrays::stream).toArray(MessageTypeEnum[]::new));
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the message buffer is only of a given type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types The expected sequence of types
     * 
     * @see #assertTypes(MessageTypeEnum[]...)
     */
    public void assertOnlyTypes(final MessageTypeEnum[]... types) {
        assertLength(Arrays.stream(types).mapToInt(t -> t.length).sum());
        assertTypes(types);
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the given subsequence of a message buffer is of a given
     * type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types  The expected sequence of types
     * @param types2 The additional expected sequence
     * 
     * @see #assertTypes(int, MessageTypeEnum...)
     */
    public void assertTypes(final MessageTypeEnum[] types, final MessageTypeEnum... types2) {
        synchronized (this) {
            final MessageTypeEnum[][] nestedTypes = new MessageTypeEnum[][] { types, types2 };
            final MessageTypeEnum[] typesMerged = Arrays.stream(nestedTypes).flatMap(Arrays::stream)
                    .toArray(MessageTypeEnum[]::new);
            assertTypes(0, typesMerged);
        }
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the message buffer is only of a given type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * 
     * @param types  The expected sequence of types
     * @param types2 The additional expected sequence
     * 
     * @see #assertTypes(MessageTypeEnum[], MessageTypeEnum...)
     */
    public void assertOnlyTypes(final MessageTypeEnum[] types, final MessageTypeEnum... types2) {
        assertLength(types.length + types2.length);
        assertTypes(types, types2);
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the given subsequence of a message buffer is of a given
     * type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * <p>
     * Unlike {@link #assertTypes(MessageTypeEnum...)} this Method will check for
     * the last messages instead of the beginning, it will do so by modifying the
     * offset based on the length and the size of the current buffer - to ensure the
     * calculation is correct, the method itself will operate in a synchronized
     * manner.
     * 
     * @param types The expected sequence of types
     */
    public synchronized void assertLastTypes(final MessageTypeEnum... types) {
        assertTypes(this.size() - types.length, types);
    }

    /**
     * This is just a simple test method which does <i>not</i> use Assertions but
     * validates, that the given subsequence of a message buffer is of a given
     * type-sequence.
     * <p>
     * If this is the case, this method will perform without a note, otherwise it
     * will throw an {@link ExpectationException}. If the buffer contains not enough
     * messages this will be considered as a problem as well.
     * <p>
     * Example: You have a message buffer containing the messages
     * {@code [A, B, C, D, E, F]}. To validate that they have the given Type
     * {@code T(x)} you would write to validate {@code B, C, D}:
     * 
     * <pre>
     * assertTypes(buffer, 1, T(B), T(C), T(D));
     * </pre>
     * 
     * @param offset The offset/starting index to perform validation on
     * @param types  The expected sequence of types
     */
    public synchronized void assertTypes(final int offset, final MessageTypeEnum... types) {
        synchronized (this) {
            final int upperBound = offset + types.length;
            if (this.size() < upperBound || offset < 0)
                throw new ExpectationException(
                        "Insufficient buffer-size/illegal offset (Buffer: " + this.size() + ", Offset: " + offset
                                + ">=0, Types: " + types.length + ".) For Buffer: " + this.typeTraversal());

            final MessageTypeEnum[] got = this.stream().map(GameDataGson::getType).toArray(MessageTypeEnum[]::new);
            int idx = 0;

            for (final MessageTypeEnum messageTypeEnum : got) {
                // jeah we could skip this more easy to avoid multiple tests,
                // but we expect < 100 messages so this shouldn't matter
                if (idx >= offset && idx < upperBound && (!Objects.equals(messageTypeEnum, types[idx - offset]))) {
                    // This would allow 'null'-checks if they should be invalid
                    throw new ExpectationException(
                            "Mismatch on index: " + idx + " with offset " + offset + " wanted: '" + types[idx - offset]
                                    + "' but got: '" + messageTypeEnum + "' in buffer: " + this.typeTraversal());
                }
                idx += 1;
            }
        }
    }

    /**
     * Works like a length check, but will throw an exception if it does not pass
     * 
     * @param length The length that is wanted
     * 
     * @see #assertLength(int)
     */
    public void assertLength(final int length) {
        synchronized (this) {
            assertLength(this, length);
        }
    }

    /**
     * Works like a length check, but will throw an exception if it does not pass
     * 
     * @param buffer The buffer to check
     * @param length The length that is wanted
     */
    public static synchronized void assertLength(final DummySendMessagesBuffer buffer, final int length) {
        if (buffer.size() != length)
            throw new ExpectationException("Buffer size is: " + buffer.size()
                    + " and therefore not as wanted for desired: " + length + " for buffer: " + buffer.typeTraversal());
    }

    /**
     * Just a simple helper to just print out the Types of the Messages
     * 
     * @return Types of all Messages
     */
    public String typeTraversal() {
        synchronized (this) {
            return Arrays.toString(this.stream().map(GameDataGson::getType).toArray(MessageTypeEnum[]::new));
        }
    }

    /**
     * True if there is at least one message with the given type
     * 
     * @param type The type to search for
     * @return True if the type was found, false otherwise
     */
    public boolean containsType(final MessageTypeEnum type) {
        synchronized (this) {
            return this.stream().map(GameDataGson::getType).anyMatch(t -> t.equals(type));
        }
    }

    /**
     * True if there is at least one message with the given type
     * 
     * @param type   The type to search for
     * @param offset Start searching - this can be used to erase some window
     * @return True if the type was found, false otherwise
     */
    public boolean containsType(final MessageTypeEnum type, final int offset) {
        synchronized (this) {
            return this.stream().sequential().skip(offset).map(GameDataGson::getType).anyMatch(t -> t.equals(type));
        }
    }

    /**
     * This method was written to help another team create a stream of valid
     * messages the client may receive. As they have written their own server (which
     * they may not rely) this provides them with valid json-data.
     * <p>
     * This will name the messages based on: {@code &lt;num&gt;-&lt;name&gt;.json}
     * whereas <i>num</i> is an incrementing number starting with 1
     * 
     * @param path The base path to spawn the messages own
     * 
     * @throws IOException If there is an error generating the data
     */
    public void dumpMessageData(final String path) throws IOException {
        synchronized (this) {
            dumpDataToFile(path);
        }
    }

    private void dumpDataToFile(final String path) throws IOException {
        int counter = 0;
        for (final String message : this) {
            // Cast to
            final MessageContainer container = MessageContainer.getMessage(message);
            final String type = container.getType().toString().toLowerCase();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + counter + "-" + type + ".json"))) {
                writer.write(message);
            } catch (final IOException ex) {
                magpie.writeException(ex, "BufferDump");
                throw ex;
            }
            counter++;
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(client);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        synchronized (this) {
            if (this == obj) {
                return true;
            }
            if (!super.equals(obj)) {
                return false;
            }
            if (!(obj instanceof DummySendMessagesBuffer)) {
                return false;
            }
            final DummySendMessagesBuffer other = (DummySendMessagesBuffer) obj;
            return Objects.equals(client, other.client);
        }
    }
}
