package com.semation.jstatsd.server.io.netty;

import com.google.inject.Inject;
import com.semation.jstatsd.server.io.EventListenPort;
import com.semation.jstatsd.server.io.IOExecutor;
import com.semation.jstatsd.server.io.IOHandler;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

class NettyIOHandler implements IOHandler {
    private final ChannelPipelineFactory pipelineFactory;
    private final int port;
    private final ChannelGroup allChannels = new DefaultChannelGroup("jstatsd-server");
    private final DatagramChannelFactory factory;
    private static final Logger log = LoggerFactory.getLogger(NettyIOHandler.class);

    @Inject
    NettyIOHandler(ChannelPipelineFactory pipelineFactory, @IOExecutor ExecutorService ioExecutor,
                          @EventListenPort Integer port) {
        this.pipelineFactory = pipelineFactory;
        factory = new NioDatagramChannelFactory(ioExecutor);
        this.port = port;
    }

    @Override
    public void shutdown() {
        log.warn("Shutdown command received");
        ChannelGroupFuture future = allChannels.close();
        log.info("Close command issued");
        future.awaitUninterruptibly();
        log.info("Channels closed");
        factory.releaseExternalResources();
        log.info("Released external IO resources");
    }

    @Override
    public void start() {
        ConnectionlessBootstrap bootstrap = new ConnectionlessBootstrap(factory);
        bootstrap.setPipelineFactory(pipelineFactory);

        Channel channel = bootstrap.bind(new InetSocketAddress(port));
        allChannels.add(channel);
        log.info("Bound to port {}", port);
    }
}
