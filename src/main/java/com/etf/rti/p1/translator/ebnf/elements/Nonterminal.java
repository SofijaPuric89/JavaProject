package com.etf.rti.p1.translator.ebnf.elements;

/**
 * Created by sule on 12/12/15.
 */
public class Nonterminal implements IElement {
    private String value;

    public Nonterminal(String value) {
        this.value = value;
    }

    public boolean isEqual(IElement e) {
        boolean ret = e instanceof Nonterminal;
        if (ret) {
            Nonterminal t = (Nonterminal) e;
            ret = value.equals(t.value);
        }
        return ret;
    }

    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public Object clone() {
        Nonterminal t = null;
        try {
            t = (Nonterminal) (super.clone());
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
