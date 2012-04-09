package com.semation.jstatsd.server.messaging.disruptor;

import com.lmax.disruptor.EventHandler;
import com.semation.jstatsd.StatsMessage;
import org.junit.Test;

import static org.easymock.EasyMock.*;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 09/04/2012
 * Time: 15:37
 */
public class DisruptorMessageEventHandlerTest {
    @Test
    public void testOnEvent() throws Exception {
        EventHandler<StatsMessage> handler = new DisruptorMessageEventHandler(5);

        StatsMessage messageMock = createMock(StatsMessage.class);
        expect(messageMock.getOperation()).andReturn(StatsMessage.Operation.unknown);

        replay(messageMock);
        handler.onEvent(messageMock, 0, true);
        verify(messageMock);

        reset(messageMock);

        replay(messageMock);
        handler.onEvent(messageMock, 1, true);
        verify(messageMock);

        reset(messageMock);
        expect(messageMock.getOperation()).andReturn(StatsMessage.Operation.unknown);
        replay(messageMock);

        handler.onEvent(messageMock, 5, true);

        verify(messageMock);
    }
}
