package de.uulm.team020.server.core.handlers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.uulm.team020.helper.DateHelper;
import de.uulm.team020.logging.IMagpie;
import de.uulm.team020.logging.Magpie;
import de.uulm.team020.networking.core.MessageContainer;

/**
 * Simple class which is the first to get its hands on a message. It will decide
 * if the incoming message is "fresh" enough to be registered. Furthermore it
 * can be used to toss it away while parsing.
 * <p>
 * The main goal is to provide a threshold for e.g. 1 minute. All messages that
 * are "older" won't be parsed.
 * 
 * @author Florian Sihler
 * @version 1.0, 04/02/2020
 */
public class FreshnessPolicy {

    private static IMagpie magpie = Magpie.createMagpieSafe("Server");

    private long threshold;

    /**
     * Construct a new policy handler for a connection with the default threshold of
     * 90 seconds;
     */
    public FreshnessPolicy() {
        this(TimeUnit.SECONDS.toMillis(90));
    }

    /**
     * Construct a new policy handler for a connection
     *
     * @param thresholdInMs threshold in milliseconds, supply negative value for
     *                      infinity
     */
    public FreshnessPolicy(long thresholdInMs) {
        if (thresholdInMs < 0)
            magpie.writeInfo("The threshold will be disabled.", "init");
        else if (thresholdInMs < 5000)
            magpie.writeWarning("The threshold you selected for [" + this + "] is below five seconds!", "init");
        this.threshold = thresholdInMs;
    }

    public long getThreshold() {
        return this.threshold;
    }

    /**
     * This will validate if the message is too old.
     *
     * @param message the message to check
     *
     * @return True if the message is too old, false otherwise
     */
    public boolean isTooOld(MessageContainer message) {
        if (this.threshold < 0)
            return false;

        SimpleDateFormat dateFormat = new SimpleDateFormat();

        // Get the date:
        Date messageCreationDate = message.getCreationDate();
        Date nowDate = DateHelper.now();

        // Is the from the future?
        if (messageCreationDate.after(nowDate)) {
            magpie.writeError(
                    "Server or client time seems to be faulty. The message received was from the future. " + "Now: "
                            + dateFormat.format(nowDate) + ", message: " + dateFormat.format(messageCreationDate),
                    "Policy");
            return false;
        }

        long diff = nowDate.getTime() - messageCreationDate.getTime();

        if (diff > this.threshold) {
            // Has to be thrown away
            magpie.writeWarning("Ignored message '" + message.getType() + "' because of threshold ("
                    + TimeUnit.MILLISECONDS.toSeconds(diff - threshold) + "s too late). Message was: " + message,
                    "Policy");
            return true;
        } else {
            return false;
        }
    }

}
