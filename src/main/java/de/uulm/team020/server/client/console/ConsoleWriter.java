package de.uulm.team020.server.client.console;

import java.io.PrintStream;
import java.util.Objects;

import org.fusesource.jansi.Ansi;

import de.uulm.team020.server.client.view.blueprints.AbstractScreenOwner;

/**
 * This is the base class to be used on writing to the terminal -- it may be
 * acquired via the {@link AbstractScreenOwner}-Mechanism and maybe by the debug
 * utilities -- you must accept, that you are responsible for multi-threaded
 * accesses as this one barely performs as a wrapper for the JAnsi-toolset.
 * <p>
 * This class operates as a singleton and as a singleton only -- there is no
 * support for feeding multiple terminals at once.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/19/2020
 * 
 * @since 1.1
 */
public class ConsoleWriter {

    private static final ConsoleWriter singleton = new ConsoleWriter();
    private final PrintStream target;
    private final Ansi ansi;

    private ConsoleWriter() {
        // AnsiConsole.systemInstall()
        target = System.out;
        ansi = Ansi.ansi();
        Ansi.setEnabled(true);
        print(ansi.reset());
    }

    /**
     * Get the console writer :D
     * 
     * @return The console writer operating on this terminal
     */
    public static ConsoleWriter getWriter() {
        return singleton;
    }

    /**
     * Print data like {@code System.out.print}
     * 
     * @param data The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter print(final Object data) {
        target.print(data);
        return this;
    }

    /**
     * Print text like {@code System.out.print}
     * 
     * @param text The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter print(final String text) {
        target.print(text);
        return this;
    }

    /**
     * Print text like {@code System.out.print} but allows color render by using:
     * {@code @|<color> <text>|@}. This will reset the sgr-options afterwards
     * 
     * @param text The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter render(final String text) {
        target.print(ansi.render(text));
        return this;
    }

    /**
     * Print data like {@code System.out.println}
     * 
     * @param data The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter println(final Object data) {
        target.print(Objects.requireNonNullElse(data, "null").toString() + ansi.newline());
        return this;
    }

    /**
     * Print a line like {@code System.out.println}
     * 
     * @param line The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter println(final String line) {
        target.print(line + ansi.newline());
        return this;
    }

    /**
     * Print a line like {@code System.out.println} but allows color render by
     * using: {@code @|<color> <text>|@}. This will reset the sgr-options afterwards
     * 
     * @param line The data you want to print
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter renderln(final String line) {
        target.println(ansi.render(line).newline());
        return this;
    }

    /**
     * Clears the whole screen, will set the curser back to start as well
     * <p>
     * This operation will call {@link #flush()} afterwards
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter clearScreen() {
        return print(ansi.eraseScreen(Ansi.Erase.ALL).cursor(1, 1).reset()).flush();
    }

    /**
     * Clear the whole line will set the curser back to start as well.
     * <p>
     * This operation will call {@link #flush()} afterwards
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter clearLine() {
        return print(ansi.eraseLine().cursorToColumn(1).reset());
    }

    /**
     * Reset all SGR-Styles and flush
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter reset() {
        return print(ansi.reset());
    }

    /**
     * Flushes the current buffer -- use with care (/do not overuse)
     * 
     * @return This writer for chaining
     */
    public ConsoleWriter flush() {
        target.flush();
        return this;
    }

    public Ansi buildFrom() {
        return ansi;
    }

}