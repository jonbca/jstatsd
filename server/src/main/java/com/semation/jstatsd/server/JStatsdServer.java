package com.semation.jstatsd.server;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.semation.jstatsd.server.io.EventListenPort;
import com.semation.jstatsd.server.io.IOExecutor;
import com.semation.jstatsd.server.io.IOHandler;
import com.semation.jstatsd.server.io.netty.NettyIOModule;
import com.semation.jstatsd.server.management.InternalStats;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import com.semation.jstatsd.server.messaging.NumberOfConsumers;
import com.semation.jstatsd.server.messaging.disruptor.DisruptorModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JStatsdServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(JStatsdServer.class);
    private static final int RING_SIZE = 1 << 14;

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
                bind(ExecutorService.class).annotatedWith(IOExecutor.class).toInstance(Executors.newCachedThreadPool());
            }
        }, new DisruptorModule(RING_SIZE), new NettyIOModule());

        injector.getInstance(MessageProcessor.class).start();
        injector.getInstance(IOHandler.class).start();
        
        log.info("Ready");
    }
}
