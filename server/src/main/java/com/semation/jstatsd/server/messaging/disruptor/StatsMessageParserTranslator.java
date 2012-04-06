package com.semation.jstatsd.server.messaging.disruptor;

import com.lmax.disruptor.EventTranslator;
import com.semation.jstatsd.StatsMessage;

interface StatsMessageParserTranslator extends EventTranslator<StatsMessage> {
    public void setMessage(String message);
}
