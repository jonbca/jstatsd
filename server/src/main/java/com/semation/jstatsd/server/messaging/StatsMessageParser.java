package com.semation.jstatsd.server.messaging;

import com.google.inject.ImplementedBy;
import com.semation.jstatsd.StatsMessage;

/**
 * Parses the given message into a StatsMessage.
 */
@ImplementedBy(RegexStatsMessageParser.class)
public interface StatsMessageParser {
    /**
     * Parses the received message into a {@link StatsMessage}. This method does not
     * create a new StatsMessage; it just updates the contents of the destination parameter
     * with the new values.
     *
     * @param message the message to parse
     * @param destination the StatsMessage to update
     * @return the same StatsMessage that was passed in the destination parameter
     * @throws IllegalMessageFormatException if the message does not match the format.
     */
    public StatsMessage parse(String message, StatsMessage destination) throws IllegalMessageFormatException;
}
