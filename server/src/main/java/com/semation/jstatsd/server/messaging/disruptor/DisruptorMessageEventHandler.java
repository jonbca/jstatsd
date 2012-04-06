package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Singleton;
import com.lmax.disruptor.EventHandler;
import com.semation.jstatsd.StatsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 19:06
 */
@Singleton
class DisruptorMessageEventHandler implements EventHandler<StatsMessage> {
    private static final Logger log = LoggerFactory.getLogger(DisruptorMessageEventHandler.class);

    @Override
    public void onEvent(StatsMessage event, long sequence, boolean endOfBatch) throws Exception {
        if (event.getOperation() == StatsMessage.Operation.unknown) {
            log.warn("Skipping unknown event: {} (seq {}, eob={})", new Object[]{event, sequence, endOfBatch});
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Handling event {} (seq {}, eob={})", new Object[]{event, sequence, endOfBatch});
        }
    }
}
