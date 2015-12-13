package com.etf.rti.p1.ebnf.rules;

import com.etf.rti.p1.ebnf.arrays.IElementArray;
import com.etf.rti.p1.ebnf.elements.IElement;

/**
 * Created by sule on 12/12/15.
 */
public interface IRule extends Iterable<IElementArray>, Cloneable {
    IIterator getRecursiveIterator();

    IIterator getNonRecursiveIterator();

    IRule insertOption(IElementArray a);

    IRule setNewOptionForInsert();

    IRule insertElem(IElement e);

    IElement getRule();

    void setRule(IElement e);

    int size();

    Object clone();

}
