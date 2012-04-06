package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.semation.jstatsd.server.messaging.StatsMessageParser;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 02/04/2012
 * Time: 01:30
 */
class StatsMessageTranslatorProvider implements Provider<StatsMessageParserTranslator> {
    @Inject
    private static StatsMessageParser parser;

    private static final ThreadLocal<StatsMessageParserTranslator> threadLocal = new ThreadLocal<StatsMessageParserTranslator>() {
        @Override
        protected StatsMessageParserTranslator initialValue() {
            return new StatsMessageEventTranslator(parser);
        }
    };
    
    @Override
    public StatsMessageParserTranslator get() {
        return threadLocal.get();
    }
}
