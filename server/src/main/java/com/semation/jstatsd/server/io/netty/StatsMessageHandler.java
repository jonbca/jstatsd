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
