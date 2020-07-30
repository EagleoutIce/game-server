package de.uulm.team020.server.client.helper;

import java.util.EnumMap;
import java.util.Map;

import de.uulm.team020.server.client.console.ConsoleWriter;
import de.uulm.team020.server.client.console.story.ConsoleStoryChapterType;
import de.uulm.team020.server.client.console.story.ConsoleStoryProcessor;

/**
 * This class is used by the {@link ConsoleStoryProcessor} to serve the
 * {@link ConsoleStoryChapterType#INFO}-Routine it want's all diggable objects
 * to be registered.
 * 
 * @author Florian Sihler
 * @version 1.0, 05/24/2020
 * 
 * @since 1.1
 */
public class InformationDigger {

    private final Map<DiggerKeyEnum, IAmDigable> diggers;

    /**
     * Build up the digger
     */
    public InformationDigger() {
        super();
        diggers = new EnumMap<>(DiggerKeyEnum.class);
    }

    /**
     * Will overwrite previous definitions by placing a new servant for this digger.
     * This allows to give more detailed information later in the game (if more data
     * is present).
     * 
     * @param key    The key to listen for
     * @param digger The digger to call, use null if you want to clear
     * 
     * @return The old digger according to {@link EnumMap#put(Enum, Object)}
     */
    public IAmDigable insertDigger(final DiggerKeyEnum key, final IAmDigable digger) {
        return diggers.put(key, digger);
    }

    /**
     * Will dig for the given key -- will not use the {@link IAmDigable}-Interface
     * to avoid accidental recursions.
     * 
     * @param key The key you want to dig for
     * 
     * @return true if there was a digger that worked, false otherwise.
     */
    public boolean dig(final DiggerKeyEnum key) {
        IAmDigable digable = diggers.get(key);
        if (digable == null || !digable.dig(key)) {
            ConsoleWriter.getWriter().renderln(
                    "@|yellow No detailed Information found for: " + key + " maybe it is not implemented yet?|@");
            return false;
        }
        return true;
    }

    /**
     * Prints help for the given key.
     * 
     * @param key The key to print help for
     */
    public void printHelp(final DiggerKeyEnum key) {
        if (key == null) {
            return;
        }
        ConsoleWriter.getWriter().renderln("Information for: " + key);
        ConsoleWriter.getWriter().renderln(key.getDescription());
    }

    public static void printFullHelp() {
        final ConsoleWriter writer = ConsoleWriter.getWriter();
        writer.renderln("@|blue digger|@ is the tool used by "
                + ConsoleStoryChapterType.renderChapterType(ConsoleStoryChapterType.INFO)
                + " to display further information");
        writer.renderln("You may pick any of the following targets for "
                + ConsoleStoryChapterType.renderChapterType(ConsoleStoryChapterType.INFO));
        for (DiggerKeyEnum key : DiggerKeyEnum.values()) {
            writer.renderln(" - @|italic " + key + "|@: " + key.getDescription());
        }
    }

}