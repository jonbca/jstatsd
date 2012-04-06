package com.semation.jstatsd.server.io.netty;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.semation.jstatsd.server.io.IOHandler;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 17:49
 */
public class NettyIOModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(IOHandler.class).to(NettyIOHandler.class).in(Scopes.SINGLETON);
        bind(ChannelUpstreamHandler.class).to(StatsMessageHandler.class);
        bind(ChannelPipelineFactory.class).to(StatsMessagePipelineFactory.class);
    }
}
