package com.semation.jstatsd.server.io.netty;

import com.google.inject.Inject;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.util.CharsetUtil;

class StatsMessagePipelineFactory implements ChannelPipelineFactory {
    private final ChannelUpstreamHandler handler;

    @Inject
    StatsMessagePipelineFactory(ChannelUpstreamHandler handler) {
        this.handler = handler;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        return Channels.pipeline(new StringDecoder(CharsetUtil.ISO_8859_1), handler);
    }
}
