/*
 * Copyright (c) 2012 Jonathan Abourbih
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
