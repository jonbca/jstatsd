package com.semation.jstatsd.server.messaging;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 01/04/2012
 * Time: 18:46
 */
public interface MessageProcessor {
    public void publish(String message);
    public void start();
    public void shutdown();
}
