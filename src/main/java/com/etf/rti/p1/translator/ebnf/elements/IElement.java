package com.etf.rti.p1.translator.ebnf.elements;

/**
 * Created by sule on 12/12/15.
 */
public interface IElement extends Cloneable {
    boolean isEqual(IElement e);

    String toString();

    Object clone();

    boolean isSpecial();

    boolean hiralMatch(IElement e);
}
