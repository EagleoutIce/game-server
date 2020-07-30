package de.uulm.team020.server.core.dummies.story;

import de.uulm.team020.parser.expander.Expander;
import de.uulm.team020.server.core.dummies.story.helper.StoryLine;

/**
 * This class holds 'current data' for a line that shall be used with an
 * {@link IStoryPerformer}-hook. This is not to be confused by {@link StoryLine}
 * which will serve internally.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/13/2020
 */
public class StoryLineData {

    private final int lineCount;
    private final String fileName;
    private final String line;
    private final Expander expander;

    /**
     * Produce a new line data
     * 
     * @param lineCount Current line counter
     * @param fileName  Current file name
     * @param line      The full line loaded
     * @param expander  The expander used by the story Board
     */
    public StoryLineData(int lineCount, String fileName, String line, Expander expander) {
        this.lineCount = lineCount;
        this.fileName = fileName;
        this.line = line;
        this.expander = expander;
    }

    public int getLineCount() {
        return lineCount;
    }

    public String getFileName() {
        return fileName;
    }

    public String getLine() {
        return line;
    }

    public Expander getExpander() {
        return expander;
    }

    @Override
    public String toString() {
        return "StoryLineData [expander=" + expander + ", fileName=" + fileName + ", line=" + line + ", lineCount="
                + lineCount + "]";
    }

}