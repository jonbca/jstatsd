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

package com.semation.jstatsd.server.management;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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
