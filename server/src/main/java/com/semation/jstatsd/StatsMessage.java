package com.semation.jstatsd;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: jabourbih
 * Date: 19/03/2012
 * Time: 20:47
 */
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
