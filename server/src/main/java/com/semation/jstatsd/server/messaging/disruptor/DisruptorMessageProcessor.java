package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.IgnoreExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.NumberOfConsumers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
class DisruptorMessageProcessor implements MessageProcessor {
    private final EventHandler<StatsMessage> eventHandler;
    private final Provider<StatsMessageParserTranslator> eventTranslatorProvider;
    private final int numberOfConsumers;
    private final Disruptor<StatsMessage> disruptor;

    private ExceptionHandler exceptionHandler = new IgnoreExceptionHandler();

    private static final Logger log = LoggerFactory.getLogger(DisruptorMessageProcessor.class);

    @Inject
    public DisruptorMessageProcessor(Disruptor<StatsMessage> disruptor,
                                     EventHandler<StatsMessage> eventHandler,
                                     Provider<StatsMessageParserTranslator> eventTranslatorProvider,
                                     @NumberOfConsumers int numberOfConsumers) {
        this.disruptor = disruptor;
        this.eventHandler = eventHandler;
        this.eventTranslatorProvider = eventTranslatorProvider;
        this.numberOfConsumers = numberOfConsumers;
    }

    @Inject(optional = true)
    public void setExceptionHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
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

        for (int i = 0; i < numberOfConsumers; i++) {
            //noinspection unchecked
            disruptor.handleEventsWith(eventHandler);
        }
        disruptor.handleExceptionsWith(exceptionHandler);
        disruptor.start();
        log.info("Message processor started");
    }

    @Override
    public void shutdown() {
        disruptor.shutdown();
    }
}
