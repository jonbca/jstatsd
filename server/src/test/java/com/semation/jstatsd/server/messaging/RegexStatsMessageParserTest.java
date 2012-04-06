package com.semation.jstatsd.server.messaging;

import com.semation.jstatsd.StatsMessage;
import org.junit.Before;
import org.junit.Test;

import java.util.regex.Matcher;

import static org.junit.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 20/03/2012
 * Time: 18:27
 */
public class RegexStatsMessageParserTest {
    RegexStatsMessageParser parser;
    
    @Before
    public void setUp() throws Exception {
        this.parser = new RegexStatsMessageParser();
    }
    
    @Test(expected = IllegalMessageFormatException.class)
    public void testBadMatch() throws Exception {
        parser.matchMessage("Foo bar zip");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testBadOpcode() throws Exception {
        parser.matchMessage("foo.bar:0.2|xxx");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testBadFrequency() throws Exception {
        parser.matchMessage("foo.bar:0.2|ms|sss");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testBadFrequency2() throws Exception {
        parser.matchMessage("foo.bar:0.3|ms|@-0.5");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testPartMissing() throws Exception {
        parser.matchMessage("foo.bar:|ms");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testPartMissing2() throws Exception {
        parser.matchMessage(":0.4|ms");
    }

    @Test(expected = IllegalMessageFormatException.class)
    public void testBadNumber() throws Exception {
        parser.matchMessage("foo.bar:0.4e3|ms");
    }

    @Test
    public void testGoodMatchNoFrequency() throws Exception {
        Matcher match = parser.matchMessage("foo.bar:0.3|ms");
        
        assertEquals("foo.bar", match.group(1));
        assertEquals("0.3", match.group(2));
        assertEquals("ms", match.group(3));

        match = parser.matchMessage("foo:5|ms");
        assertEquals("foo", match.group(1));
        assertEquals("5", match.group(2));
        assertEquals("ms", match.group(3));
        
        match = parser.matchMessage("foo-bar_zip_99.goo_32_33:-02.95244|c");
        assertEquals("foo-bar_zip_99.goo_32_33", match.group(1));
        assertEquals("-02.95244", match.group(2));
        assertEquals("c", match.group(3));
    }

    @Test
    public void testGoodFrequency() throws Exception {
        Matcher match = parser.matchMessage("foo.bar:0.3|ms|@0.4");
        
        assertEquals("0.4", match.group(4));
    }

    @Test
    public void testKeyProcessing() {
        assertEquals("key-value", parser.processKey("key/value"));
        assertEquals("badteacher", parser.processKey("bad$teacher"));
        assertEquals("badteacher", parser.processKey("badteacher"));
        assertEquals("bad_teacher_foo_bar", parser.processKey("bad teacher\tfoo\nbar"));
    }

    @Test
    public void testParseGoodMessage() throws Exception {
        StatsMessage statsMessage = parser.parse("foo.bar:0.3|ms", new StatsMessage());
        assertEquals("foo.bar", statsMessage.getKey());
        assertEquals(0.3, statsMessage.getValue(), 0.001);
        assertEquals(StatsMessage.Operation.ms, statsMessage.getOperation());
        
        statsMessage = parser.parse("foo.bar:0.2|ms|@0.4", new StatsMessage());
        assertEquals("foo.bar", statsMessage.getKey());
        assertEquals(0.2, statsMessage.getValue(), 0.001);
        assertEquals(StatsMessage.Operation.ms, statsMessage.getOperation());
        assertEquals(0.4, statsMessage.getFrequency(), 0.001);
    }
}
