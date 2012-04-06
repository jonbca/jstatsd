package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.IllegalMessageFormatException;
import com.semation.jstatsd.server.messaging.StatsMessageParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 02/04/2012
 * Time: 01:00
 */
class StatsMessageEventTranslator implements StatsMessageParserTranslator {
    private String message;
    private final StatsMessageParser parser;
    private static final Logger log = LoggerFactory.getLogger(StatsMessageEventTranslator.class);

    @Inject
    public StatsMessageEventTranslator(StatsMessageParser parser) {
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

    public void setMessage(String message) {
        this.message = message;
    }
}