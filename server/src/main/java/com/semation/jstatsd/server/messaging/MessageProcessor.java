package com.semation.jstatsd.server.messaging;

public interface MessageProcessor {
    public void publish(String message);
    public void start();
    public void shutdown();
}
