package de.uulm.team020.server.client.view.blueprints;

import java.io.IOException;

import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.server.client.console.ConsoleReader;
import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.controller.ServerClientController;
import de.uulm.team020.server.client.helper.IllegalOwnException;

/**
 * This class is to be implemented by any class that wants to access the screen.
 * To be able to perform any operations on the given Terminal this one must
 * first acquire the terminal view. As it does not make ANY sense to provide
 * multiple screen holders this owner will take advantage of a static mechanism.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public abstract class AbstractScreenOwner {
    /** Logging-Reference, will be shared by all Server(+Client)-components */
    protected static IMagpie magpie = Magpie.createMagpieSafe("Server-Client");

    // there may only be one
    private static AbstractScreenOwner currentOwner = null;

    protected final ServerClientController controller;
    protected final String name;

    private static final ConsoleReader reader = ConsoleReader.getReader();
    private static final ConsoleWriter writer = ConsoleWriter.getWriter();

    /**
     * Default constructor delegates the controller
     * 
     * @param controller The controller this one shall be linked to
     * @param name       The name of this owner to refer to it while debugging
     */
    public AbstractScreenOwner(final ServerClientController controller, final String name) {
        super();
        this.controller = controller;
        this.name = name;
        init();
    }

    /**
     * To be called on construction to setup data in a unified slot
     */
    public final void init() {
        onInit();
    }

    /**
     * To be called on construction to setup data in a unified slot
     */
    public abstract void onInit();

    /**
     * Start the screen
     * 
     * @throws IOException if there is any problem reading gand writing
     */
    public abstract void run() throws IOException;

    /**
     * Performs a check to own-operation
     * 
     * @param newOwner the one that wats to own
     * @return new/own owner
     */
    private static final AbstractScreenOwner own(final AbstractScreenOwner newOwner) {
        if (currentOwner == null) {
            // acquire
            currentOwner = newOwner;
        }
        return currentOwner;
    }

    /**
     * Performs a free-operation to yield the ownership
     * 
     * @param oldOwner the one that wats to free
     * @return true if you hae been the owner and the ownership was freed, false
     *         otherwise
     */
    private static final boolean free(final AbstractScreenOwner oldOwner) {
        if (currentOwner == oldOwner) {
            // acquire
            currentOwner = null;
            return true;
        }
        return false;
    }

    /**
     * Checks if you are able to Perform {@link #tryOwn()} but won't
     * 
     * @return True if you may {@link #tryOwn() mayOwn} the Terminal. This will
     *         return true if you already are the owner.
     */
    public final boolean mayOwn() {
        return currentOwner == null || currentOwner == this;
    }

    /**
     * Tries to get the right to access the current screen
     * 
     * @return True if the operation has worked, false if you are <i>not</i> the new
     *         owner. This will return true if you already are the owner.
     * 
     * @see #own()
     */
    public final boolean tryOwn() {
        return own(this) == this;
    }

    /**
     * Tries to get the right to access the current screen
     * <p>
     * In contrast to {@link #tryOwn()} this will throw an
     * {@link IllegalOwnException} if the acquisition fails.
     * 
     * @see #tryOwn()
     */
    public final void own() {
        if (own(this) != this) {
            throw new IllegalOwnException(
                    "The screen: " + this.name + " is not able to own as the screen is owned by: " + currentOwner.name);
        }
    }

    /**
     * Tries to yield the screen access
     * 
     * @return True if the operation has worked, false if you are <i>not</i> the
     *         owner of the screen and are unable to free it..
     */
    public final boolean tryFree() {
        return free(this);
    }

    /**
     * Checks if you own the screen
     * 
     * @return True if this instance owns the terminal
     */
    public final boolean isOwner() {
        return currentOwner == this;
    }

    protected ConsoleReader getReader() {
        if (currentOwner != this) {
            throw new IllegalStateException("You (" + this.name + ") do not own the console  which is owned by: "
                    + (currentOwner == null ? "null" : currentOwner.name));
        }
        return reader;
    }

    protected ConsoleWriter getWriter() {
        if (currentOwner != this) {
            throw new IllegalStateException("You (" + this.name + ") do not own the console  which is owned by: "
                    + (currentOwner == null ? "null" : currentOwner.name));
        }
        return writer;
    }

}