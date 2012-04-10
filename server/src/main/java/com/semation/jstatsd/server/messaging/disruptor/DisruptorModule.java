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

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.MessagingExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

public class DisruptorModule extends AbstractModule {
    private static final Logger log = LoggerFactory.getLogger(DisruptorModule.class);

    @Override
    protected void configure() {
        requestStaticInjection(StatsMessageTranslatorProvider.class);

        bind(new TypeLiteral<EventHandler<StatsMessage>>() {})
                .to(DisruptorMessageEventHandler.class).in(Scopes.SINGLETON);

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
