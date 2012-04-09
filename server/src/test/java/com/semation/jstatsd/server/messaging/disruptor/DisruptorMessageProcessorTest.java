package com.semation.jstatsd.server.messaging.disruptor;

import com.google.inject.Provider;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.semation.jstatsd.StatsMessage;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 09/04/2012
 * Time: 15:48
 */
public class DisruptorMessageProcessorTest {
    DisruptorMessageProcessor processor;
    Disruptor<StatsMessage> disruptor;
    EventHandler<StatsMessage> handler;
    Provider<StatsMessageParserTranslator> translatorProvider;
    StatsMessageParserTranslator translator;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        handler = createMock(EventHandler.class);
        disruptor = createMock(Disruptor.class);
        translator = createMock(StatsMessageParserTranslator.class);
        translatorProvider = createMock(Provider.class);

        expect(translatorProvider.get()).andReturn(translator).anyTimes();
        replay(translatorProvider);

        processor = new DisruptorMessageProcessor(disruptor, handler, translatorProvider, 3);
    }

    @Test
    public void testPublish() throws Exception {
        String message = "Foo bar";
        translator.setMessage(message);
        disruptor.publishEvent(translator);

        replay(translator);
        replay(disruptor);
        processor.publish(message);
        verify(translator);
        verify(disruptor);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testStart() throws Exception {
        expect(disruptor.handleEventsWith(handler)).andReturn(null).times(3);
        disruptor.handleExceptionsWith(anyObject(ExceptionHandler.class));

        EventFactory factory = createMock(EventFactory.class);
        expect(factory.newInstance()).andReturn(new StatsMessage()).times(32);
        replay(factory);

        expect(disruptor.start()).andReturn(new RingBuffer<StatsMessage>(factory, 32));
        replay(disruptor);

        processor.start();
    }

    @Test
    public void testShutdown() throws Exception {
        disruptor.shutdown();
        replay(disruptor);
        processor.shutdown();
    }
}
