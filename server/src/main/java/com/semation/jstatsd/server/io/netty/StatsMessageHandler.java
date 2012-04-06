package com.semation.jstatsd.server.io.netty;

import com.google.inject.Inject;
import com.semation.jstatsd.server.management.InternalStats;
import com.semation.jstatsd.server.messaging.MessageProcessor;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ChannelHandler.Sharable
class StatsMessageHandler extends SimpleChannelUpstreamHandler {
    private final MessageProcessor messageProcessor;
    private static final Logger log = LoggerFactory.getLogger(StatsMessageHandler.class);

    @Inject
    StatsMessageHandler(MessageProcessor messageProcessor) {
        log.debug("Building new StatsMessageHandler");
        this.messageProcessor = messageProcessor;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        final String message = (String) e.getMessage();
        log.debug("Message received: {}", message);

        messageProcessor.publish(message);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
        InternalStats.INSTANCE.addBadMessage();    
    }
}
