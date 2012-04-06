package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.lmax.disruptor.EventHandler;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.NumberOfConsumers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

class DisruptorMessageEventHandler implements EventHandler<StatsMessage> {
    private static final Logger log = LoggerFactory.getLogger(DisruptorMessageEventHandler.class);
    private static final AtomicInteger serialNumberGenerator = new AtomicInteger();

    private static final ThreadLocal<Integer> serialNumberThreadLocal = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return serialNumberGenerator.getAndIncrement();
        }
    };

    private final int numberOfConsumers;

    @Inject
    DisruptorMessageEventHandler(@NumberOfConsumers int numberOfConsumers) {
        this.numberOfConsumers = numberOfConsumers;
    }

    @Override
    public void onEvent(StatsMessage event, long sequence, boolean endOfBatch) throws Exception {
        if (!shouldHandleThisEvent(sequence)) return;

        if (event.getOperation() == StatsMessage.Operation.unknown) {
            log.warn("Skipping unknown event: {} (seq {}, eob={})", new Object[]{event, sequence, endOfBatch});
            return;
        }

        if (log.isDebugEnabled()) {
            log.debug("Handling event {} (seq {}, eob={})", new Object[]{event, sequence, endOfBatch});
        }
    }

    private boolean shouldHandleThisEvent(long sequence) {
        return (sequence % numberOfConsumers) == serialNumberThreadLocal.get();
    }
}
