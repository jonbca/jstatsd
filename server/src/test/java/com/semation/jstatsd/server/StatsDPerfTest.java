package com.semation.jstatsd.server;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.DatagramChannel;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 24/03/2012
 * Time: 19:19
 */
public class StatsDPerfTest {
    RandomStringGenerator rs;
    Random random = new Random();
    DatagramChannel c;
    DatagramChannelFactory factory;
    private JStatsdServer server;

    @Before
    public void setUp() {
        rs = new RandomStringGenerator(random);

        factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());
        ConnectionlessBootstrap b = new ConnectionlessBootstrap(factory);

        b.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new StringEncoder(CharsetUtil.ISO_8859_1),
                        new PerfHandler());
            }
        });

        c = (DatagramChannel) b.bind(new InetSocketAddress("localhost", 9991));

        server = new JStatsdServer();
        server.run();
    }

    @After
    public void teardown() {
        server.shutdown();
    }
    
    @Test
    public void runPerformanceTest() throws InterruptedException {
        int times = 1 << 13;
        
        while (times-- > 0) {
            String key = rs.next() + "." + rs.next();
            String value = Double.toString(random.nextDouble());
            String op = "ms";
            
            String message = key + ":" + value + "|" + op;

            c.write(message, new InetSocketAddress("127.0.0.1", 9999)).awaitUninterruptibly();
            Thread.sleep(0,500);
        }
        
        c.close().awaitUninterruptibly();
        factory.releaseExternalResources();
    }
}

class RandomStringGenerator  {
    private final Random random;

    RandomStringGenerator(Random random) {
        this.random = random;
    }

    public String next() {
        return new BigInteger(130, random).toString(32);
    }
}

class PerfHandler extends SimpleChannelHandler {

}
