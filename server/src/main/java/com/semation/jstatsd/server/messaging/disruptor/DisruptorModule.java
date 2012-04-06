package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.lmax.disruptor.*;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 19:12
 */
public class DisruptorModule extends AbstractModule {
    private final int ringSize;

    public DisruptorModule(Integer ringSize) {
        this.ringSize = ringSize;
    }

    @Override
    protected void configure() {
        requestStaticInjection(StatsMessageTranslatorProvider.class);

        bind(new TypeLiteral<EventHandler<StatsMessage>>() {;}).to(DisruptorMessageEventHandler.class);

        bind(StatsMessageParserTranslator.class).toProvider(StatsMessageTranslatorProvider.class);
        bind(MessageProcessor.class).to(DisruptorMessageProcessor.class);

        bind(WaitStrategy.class).toInstance(new BlockingWaitStrategy());
        bind(ClaimStrategy.class).toInstance(new MultiThreadedClaimStrategy(ringSize));
    }
}
