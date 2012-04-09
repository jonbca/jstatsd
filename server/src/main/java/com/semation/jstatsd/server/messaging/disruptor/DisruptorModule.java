package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.MessagingExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class DisruptorModule extends AbstractModule {
    private final int ringSize;
    private static final Logger log = LoggerFactory.getLogger(DisruptorModule.class);

    public DisruptorModule(Integer ringSize) {
        this.ringSize = ringSize;
    }

    @Override
    protected void configure() {
        requestStaticInjection(StatsMessageTranslatorProvider.class);

        bind(new TypeLiteral<EventHandler<StatsMessage>>() {})
                .to(DisruptorMessageEventHandler.class).in(Scopes.SINGLETON);

        bind(Integer.class).annotatedWith(Names.named("Ring Size")).toInstance(ringSize);

        bind(StatsMessageParserTranslator.class).toProvider(StatsMessageTranslatorProvider.class);
        bind(MessageProcessor.class).to(DisruptorMessageProcessor.class);
        bind(WaitStrategy.class).to(BlockingWaitStrategy.class);
    }

    @Provides
    Disruptor<StatsMessage> getDisruptor(@MessagingExecutor ExecutorService executor,
                                         ClaimStrategy claimStrategy, WaitStrategy waitStrategy) {
        EventFactory<StatsMessage> factory = new EventFactory<StatsMessage>() {
            @Override
            public StatsMessage newInstance() {
                return new StatsMessage();
            }
        };

        log.info("Spawning new disruptor");

        return new Disruptor<StatsMessage>(factory, executor, claimStrategy, waitStrategy);
    }

    @Provides
    ClaimStrategy getClaimStrategy(@Named("Ring Size") int ringSize) {
        return new MultiThreadedClaimStrategy(ringSize);
    }
}
