package com.semation.jstatsd.server.management;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 21/03/2012
 * Time: 22:06
 */
public interface JStatsdStatsMBean {
    public void shutdown();

    public long getMessageCount();

    public int getBadMessageCount();

    public Date getLastMessageReceivedTime();

    public long getLastMessageSeqNo();
}
