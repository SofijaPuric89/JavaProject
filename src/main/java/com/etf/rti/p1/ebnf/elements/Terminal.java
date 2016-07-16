package com.etf.rti.p1.ebnf.elements;

/**
 * Created by sule on 12/12/15.
 */
public class Terminal implements IElement {
    private String value;

    public Terminal(String value) {
        this.value = value;
    }

    public boolean isEqual(IElement e) {
        boolean ret = e instanceof Terminal;
        if (ret) {
            Terminal t = (Terminal) e;
            ret = value.equals(t.value);
        }
        return ret;
    }

    public String toString() {
        return "\"" + value + "\"";
    }

    public Object clone() {
        Terminal t = null;
        try {
            t = (Terminal) (super.clone());
            t.value = value;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return t;
    }

    public boolean isSpecial() {
        return false;
    }

    public boolean hiralMatch(IElement e) {
        return false;
    }
}
