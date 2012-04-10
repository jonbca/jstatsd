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

package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.IllegalMessageFormatException;
import com.semation.jstatsd.server.messaging.StatsMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StatsMessageEventTranslator implements StatsMessageParserTranslator {
    private String message;
    private final StatsMessageParser parser;
    private static final Logger log = LoggerFactory.getLogger(StatsMessageEventTranslator.class);

    @Inject
    StatsMessageEventTranslator(StatsMessageParser parser) {
        this.parser = parser;
    }
    
    @Override
    public StatsMessage translateTo(StatsMessage event, long sequence) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Translating to sequence {} event: ", sequence, event);
            }
            parser.parse(message, event);
        } catch (IllegalMessageFormatException e) {
            log.error("Illegal message format: {}", message);
            throw new RuntimeException(e);
        }
        return event;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
