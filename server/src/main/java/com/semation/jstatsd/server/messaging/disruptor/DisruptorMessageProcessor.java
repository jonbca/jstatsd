package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.MessagingExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 18:49
 */
@Singleton
class DisruptorMessageProcessor implements MessageProcessor {
    private final EventHandler<StatsMessage> eventHandler;
    private final ExecutorService messagingExecutor;
    private final WaitStrategy waitStrategy;
    private final ClaimStrategy claimStrategy;
    private final Provider<StatsMessageParserTranslator> eventTranslatorProvider;
    private Disruptor<StatsMessage> disruptor;

    private static final Logger log = LoggerFactory.getLogger(DisruptorMessageProcessor.class);
    
    @Inject
    public DisruptorMessageProcessor(EventHandler<StatsMessage> eventHandler,
                                     @MessagingExecutor ExecutorService messagingExecutor,
                                     WaitStrategy waitStrategy,
                                     ClaimStrategy claimStrategy,
                                     Provider<StatsMessageParserTranslator> eventTranslatorProvider) {
        this.eventHandler = eventHandler;
        this.messagingExecutor = messagingExecutor;
        this.waitStrategy = waitStrategy;
        this.claimStrategy = claimStrategy;
        this.eventTranslatorProvider = eventTranslatorProvider;
    }

    @Override
    public void publish(String message) {
        StatsMessageParserTranslator translator = eventTranslatorProvider.get();
        translator.setMessage(message);
        disruptor.publishEvent(translator);
    }

    @Override
    public void start() {
        log.info("Starting message processor");
        final EventFactory<StatsMessage> statsMessageEventFactory = new EventFactory<StatsMessage>() {
            @Override
            public StatsMessage newInstance() {
                return new StatsMessage();
            }
        };

        disruptor = new Disruptor<StatsMessage>(statsMessageEventFactory, messagingExecutor, claimStrategy,
                waitStrategy);

        //noinspection unchecked
        disruptor.handleEventsWith(eventHandler);
        disruptor.handleExceptionsWith(new IgnoreExceptionHandler());
        disruptor.start();
        log.info("Message processor started");
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }
}
