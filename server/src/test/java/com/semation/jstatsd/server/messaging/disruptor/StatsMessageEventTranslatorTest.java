package com.semation.jstatsd.server.messaging.disruptor;

import com.semation.jstatsd.StatsMessage;
import com.semation.jstatsd.server.messaging.StatsMessageParser;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertSame;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 09/04/2012
 * Time: 15:22
 */
public class StatsMessageEventTranslatorTest {
    StatsMessageEventTranslator translator;
    StatsMessageParser parserMock;

    @Before
    public void setUp() throws Exception {
        parserMock = createMock(StatsMessageParser.class);
        translator = new StatsMessageEventTranslator(parserMock);
    }

    @Test
    public void testTranslateTo() throws Exception {
        StatsMessage message = new StatsMessage();
        String messageString = "Foo Bar";

        translator.setMessage(messageString);

        expect(parserMock.parse(messageString, message)).andReturn(message);
        replay(parserMock);
        StatsMessage outMessage = translator.translateTo(message, 0L);
        verify(parserMock);
        assertSame(message, outMessage);
    }
}
