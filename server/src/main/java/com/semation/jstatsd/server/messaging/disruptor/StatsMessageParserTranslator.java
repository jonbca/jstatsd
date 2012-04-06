package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.ImplementedBy;
import com.lmax.disruptor.EventTranslator;
import com.semation.jstatsd.StatsMessage;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 02/04/2012
 * Time: 01:27
 */
@ImplementedBy(StatsMessageEventTranslator.class)
interface StatsMessageParserTranslator extends EventTranslator<StatsMessage> {
    public void setMessage(String message);
}
