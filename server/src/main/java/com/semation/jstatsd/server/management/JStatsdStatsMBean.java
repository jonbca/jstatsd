package com.semation.jstatsd.server.management;

import java.util.Date;

public interface JStatsdStatsMBean {
    public void shutdown();

    public long getMessageCount();

    public int getBadMessageCount();

    public Date getLastMessageReceivedTime();

    public long getLastMessageSeqNo();
}
