package com.semation.jstatsd.server.management;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 20/03/2012
 * Time: 19:06
 */
public enum InternalStats {
    INSTANCE;

    private final AtomicInteger badMessageCount = new AtomicInteger(0);
    private final AtomicLong receivedMessageCount = new AtomicLong(0L);
    
    private final long startTime = System.currentTimeMillis();
    
    private volatile long lastMessageReceivedTime = 0L;

    private volatile long lastMessageSeqNo = -1L;

    public int addBadMessage() {
        return badMessageCount.incrementAndGet();        
    }
    
    public int getBadMessageCount() {
        return badMessageCount.get();
    }
    
    public long addReceivedMessage(long seq) {
        lastMessageReceivedTime = System.currentTimeMillis();
        lastMessageSeqNo = seq;
        return receivedMessageCount.incrementAndGet();
    }
    
    public long getReceivedMessageCount() {
        return receivedMessageCount.get();
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getLastMessageReceivedTime() {
        return lastMessageReceivedTime;
    }

    public long getLastMessageSeqNo() {
        return lastMessageSeqNo;
    }
}
