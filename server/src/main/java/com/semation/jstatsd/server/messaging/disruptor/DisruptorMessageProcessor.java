/*
 * Copyright (c) 2012 Jonathan Abourbih
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
    DisruptorMessageProcessor(Disruptor<StatsMessage> disruptor,
                                     EventHandler<StatsMessage> eventHandler,
                                     Provider<StatsMessageParserTranslator> eventTranslatorProvider,
                                     @NumberOfConsumers int numberOfConsumers) {
        this.disruptor = disruptor;
        this.eventHandler = eventHandler;
        this.eventTranslatorProvider = eventTranslatorProvider;
        this.numberOfConsumers = numberOfConsumers;
    }

    @Inject(optional = true)
    void setExceptionHandler(ExceptionHandler exceptionHandler) {
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
