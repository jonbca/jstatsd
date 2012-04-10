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

package com.semation.jstatsd.server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.semation.jstatsd.server.io.EventListenPort;
import com.semation.jstatsd.server.io.IOExecutor;
import com.semation.jstatsd.server.io.IOHandler;
import com.semation.jstatsd.server.io.netty.NettyIOModule;
import com.semation.jstatsd.server.management.InternalStats;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.MessagingExecutor;
import com.semation.jstatsd.server.messaging.NumberOfConsumers;
import com.semation.jstatsd.server.messaging.disruptor.DisruptorModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JStatsdServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(JStatsdServer.class);
    private static final int RING_SIZE = 1 << 14;
    private MessageProcessor processor;
    private IOHandler ioHandler;

    public static void main(String[] args) {
        JStatsdServer server = new JStatsdServer();
        server.run();
    }

    public void run() {
        InternalStats.INSTANCE.getStartTime();
        log.info("Starting JStatsD Server");

        Injector injector = Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Integer.class).annotatedWith(EventListenPort.class).toInstance(9999);
                bind(Integer.class).annotatedWith(NumberOfConsumers.class).toInstance(3);
                bind(Integer.class).annotatedWith(Names.named("Ring Size")).toInstance(RING_SIZE);
                bind(ExecutorService.class).annotatedWith(IOExecutor.class).toInstance(Executors.newCachedThreadPool());
                bind(ExecutorService.class).annotatedWith(MessagingExecutor.class).toInstance(Executors.newCachedThreadPool());
            }
        }, new DisruptorModule(), new NettyIOModule());

        processor = injector.getInstance(MessageProcessor.class);
        ioHandler = injector.getInstance(IOHandler.class);

        processor.start();
        ioHandler.start();

        log.info("Ready");
    }

    public void shutdown() {
        ioHandler.shutdown();
        processor.shutdown();
    }
}
