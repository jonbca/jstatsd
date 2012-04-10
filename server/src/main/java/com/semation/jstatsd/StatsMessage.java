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

package com.semation.jstatsd;

import java.io.Serializable;

public class StatsMessage implements Serializable {

    private static final long serialVersionUID = -2224441356015801894L;

    private String key;
    private double value;
    private Operation op;
    private double frequency;
    
    private transient String stringValue = null;
    
    public StatsMessage() {
        reset();
    }

    @Override
    public String toString() {
        if (stringValue == null) {
            stringValue = String.format("%s:%f|%s", key, value, op.toString());
            
            if (frequency > 0.0d) {
                stringValue = stringValue + String.format("|@%f", frequency);
            }
        }
        return stringValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Operation getOperation() {
        return op;
    }

    public void setOperation(Operation op) {
        this.op = op;
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public void reset() {
        frequency = Double.NaN;
        key = "";
        value = 0.0d;
        op = Operation.unknown;
        stringValue = null;
    }


    @SuppressWarnings("UnusedDeclaration")
    public static enum Operation {
        /** Counter */
        c,
        /** Timer value in milliseconds */
        ms,
        /** Unknown */
        unknown
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatsMessage that = (StatsMessage) o;

        return Double.compare(that.frequency, frequency) == 0 && Double.compare(that.value, value) == 0
                && !(key != null ? !key.equals(that.key) : that.key != null) && op == that.op;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = key != null ? key.hashCode() : 0;
        temp = value != +0.0d ? Double.doubleToLongBits(value) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (op != null ? op.hashCode() : 0);
        temp = frequency != +0.0d ? Double.doubleToLongBits(frequency) : 0L;
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
