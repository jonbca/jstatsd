package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.ImplementedBy;
import com.lmax.disruptor.EventTranslator;
import com.semation.jstatsd.StatsMessage;

@ImplementedBy(StatsMessageEventTranslator.class)
interface StatsMessageParserTranslator extends EventTranslator<StatsMessage> {
    public void setMessage(String message);
}
