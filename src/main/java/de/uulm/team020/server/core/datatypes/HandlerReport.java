package de.uulm.team020.server.core.datatypes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

import de.uulm.team020.networking.core.MessageContainer;
import de.uulm.team020.server.core.NttsController;

/**
 * Type to be returned by an {@link IMessageHandler} if it is to be used by the
 * {@link NttsController}
 * 
 * @author Florian Sihler
 * @version 1.3, 04/15/2020
 */
public class HandlerReport implements Serializable {

    private static final long serialVersionUID = 7393654878556419024L;

    private boolean successful;
    private List<String> problemList;
    private List<MessageTargetPair> messageRequests;

    private transient BooleanSupplier afterFunctionCall = null;

    private boolean requestsClose;

    private static final HandlerReport DO_NOTHING = new HandlerReport();

    /**
     * A default HandlerReport if the Operation was successful.
     */
    public HandlerReport() {
        this(true, Collections.emptyList(), Collections.emptyList(), false);
    }

    /**
     * A default HandlerReport if the Operation was successful, but there are
     * messages to send.
     * 
     * @param messageRequests Messages to be send by this handler
     */
    public HandlerReport(List<MessageTargetPair> messageRequests) {
        this(true, Collections.emptyList(), messageRequests, false);
    }

    /**
     * A default HandlerReport if the Operation was successful, but there are
     * messages to send.
     * 
     * @param messageRequests Messages to be send by this handler
     * @param requestsClose   Does the handler request a close of the connection?
     */
    public HandlerReport(List<MessageTargetPair> messageRequests, boolean requestsClose) {
        this(true, Collections.emptyList(), messageRequests, requestsClose);
    }

    /**
     * A default HandlerReport if the Operation was successful, but there is one
     * message to send. The target will be determined by the supplied clientId
     * 
     * @param message The Message to send
     * 
     * @see #HandlerReport(MessageContainer, MessageTarget)
     */
    public HandlerReport(MessageContainer message) {
        this(true, Collections.emptyList(),
                List.of(new MessageTargetPair(message, new MessageTarget(message.getClientId()))), false);
    }

    /**
     * A default HandlerReport if the Operation was successful, but there is one
     * message to send.
     * 
     * @param message The Message to send
     * @param target  The target of the Message
     */
    public HandlerReport(MessageContainer message, MessageTarget target) {
        this(true, Collections.emptyList(), List.of(new MessageTargetPair(message, target)), false);
    }

    /**
     * Constructs a new report with "one problem"
     * 
     * @param problem        Problem as String
     * @param messageRequest the message it requests to send
     * @param messageTarget  the target of the Message
     * @param requestsClose  does this problem close the connection?
     */
    public HandlerReport(String problem, MessageContainer messageRequest, MessageTarget messageTarget,
            boolean requestsClose) {
        this(false, List.of(problem), List.of(new MessageTargetPair(messageRequest, messageTarget)), requestsClose);
    }

    /**
     * Creates a new handler-report holding the result of a {@link IMessageHandler}
     * 
     * @param successful      was the handler successful?
     * @param problemList     list of problems if the handler was not successful
     * @param messageRequests messages including their targets the handler wants to
     *                        send
     * @param requestsClose   does the handler request a close of the connection?
     */
    public HandlerReport(boolean successful, List<String> problemList, List<MessageTargetPair> messageRequests,
            boolean requestsClose) {
        this.successful = successful;
        this.problemList = problemList;
        this.messageRequests = messageRequests;
        this.requestsClose = requestsClose;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public List<String> getProblemList() {
        return problemList;
    }

    public List<MessageTargetPair> getMessageRequests() {
        return messageRequests;
    }

    public boolean doesRequestClose() {
        return requestsClose;
    }

    public boolean hasMessagesToSend() {
        return !this.messageRequests.isEmpty();
    }

    public static HandlerReport doNothing() {
        return HandlerReport.DO_NOTHING;
    }

    @Override
    public String toString() {
        return "HandlerReport [messageRequests=" + messageRequests + ", problemList="
                + Arrays.toString(problemList.toArray()) + ", requestsClose=" + requestsClose + ", successful="
                + successful + "]";
    }

    /**
     * Set a Function that should be executed after the other messages have been
     * send. Something like "flush everything and then do this".
     *
     * @param afterFunctionCall the function to be called afterwards
     *
     * @return the modified HandlerReport
     */
    public HandlerReport setAfterFunctionCall(BooleanSupplier afterFunctionCall) {
        this.afterFunctionCall = afterFunctionCall;
        return this;
    }

    public BooleanSupplier getAfterFunctionCall() {
        return afterFunctionCall;
    }
}