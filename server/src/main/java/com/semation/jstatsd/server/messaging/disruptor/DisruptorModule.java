package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.lmax.disruptor.*;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;

public class DisruptorModule extends AbstractModule {
    private final int ringSize;

    public DisruptorModule(Integer ringSize) {
        this.ringSize = ringSize;
    }

    @Override
    protected void configure() {
        requestStaticInjection(StatsMessageTranslatorProvider.class);

        bind(new TypeLiteral<EventHandler<StatsMessage>>() {;})
                .to(DisruptorMessageEventHandler.class).in(Scopes.SINGLETON);

        bind(StatsMessageParserTranslator.class).toProvider(StatsMessageTranslatorProvider.class);
        bind(MessageProcessor.class).to(DisruptorMessageProcessor.class);

        bind(WaitStrategy.class).toInstance(new YieldingWaitStrategy());
        bind(ClaimStrategy.class).toInstance(new MultiThreadedClaimStrategy(ringSize));
    }
}
