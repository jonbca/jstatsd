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

package com.semation.jstatsd.server.messaging;

import com.semation.jstatsd.StatsMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class RegexStatsMessageParser implements StatsMessageParser {
    private static final Pattern MESSAGE = Pattern.compile("^([a-zA-Z0-9\\-\\._]+):(-?[0-9]*\\.?[0-9]+)\\|(c|ms)(?:\\|@([0-9]*\\.?[0-9]+))?$");
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern FILTER_CHARS = Pattern.compile("[^a-zA-Z_\\-0-9\\.]");

    private static final Logger log = LoggerFactory.getLogger(RegexStatsMessageParser.class);
    
    @Override
    public StatsMessage parse(String message, final StatsMessage destination) throws IllegalMessageFormatException {
        log.debug("Parsing message {}", message);

        final Matcher matcher = matchMessage(message.trim());
                
        final String key = processKey(matcher.group(1));
        final double value = Double.parseDouble(matcher.group(2));
        final StatsMessage.Operation op = StatsMessage.Operation.valueOf(matcher.group(3));
        final double frequency = matcher.group(4) == null ? Double.NaN : Double.parseDouble(matcher.group(4));

        destination.setKey(key);
        destination.setValue(value);
        destination.setOperation(op);
        destination.setFrequency(frequency);

        return destination;
    }
    
    Matcher matchMessage(String message) throws IllegalMessageFormatException {
        Matcher matcher = MESSAGE.matcher(message);
        if (matcher.matches() && matcher.groupCount() >= 3) {
            return matcher;
        } else {
            if (!matcher.matches()) {
                log.error("Message did not parse: {}", message);
            }
            throw new IllegalMessageFormatException("Message does not match format: " + message);
        }
    }

    /**
     * Munge the key to match the processing that's done in statsd. Replaces all whitespace with underscores,
     * and all slashes with hyphens. Removes all characters that match
     * <code>[^a-zA-Z_\\-0-9\\.]</code>
     *
     * @param key the key to process
     * @return the processed key
     */
    String processKey(String key) {
        key = WHITESPACE.matcher(key).replaceAll("_").replace('/', '-');
        return FILTER_CHARS.matcher(key).replaceAll("");
    }
}
