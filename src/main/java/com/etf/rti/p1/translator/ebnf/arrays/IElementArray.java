package com.etf.rti.p1.translator.ebnf.arrays;

import com.etf.rti.p1.translator.ebnf.elements.IElement;

import java.util.ListIterator;

/**
 * Created by sule on 12/12/15.
 */
public interface IElementArray extends Cloneable, Comparable<IElementArray> {
    boolean samePrefix(IElementArray a);

    boolean sameSuffix(IElementArray a);

    boolean hasElement(IElement e);

    IElementArray getSamePrefix(IElementArray a);

    IElementArray getSameSuffix(IElementArray a);

    IElementArray getWithoutPrefix(IElementArray a);

    IElementArray getWithoutSuffix(IElementArray a);

    boolean isPrefix(IElementArray a);

    boolean isSuffix(IElementArray a);

    IElementArray removeSuffix(IElementArray a);

    IElementArray removePrefix(IElementArray a);

    IElementArray addElementPrefix(IElement e);

    IElementArray addElementSuffix(IElement e);

    boolean isEqual(IElementArray a);

    IElementArray addPrefix(IElementArray prefix);

    IElementArray addSuffix(IElementArray suffix);

    ListIterator<IElement> iterator(int pos);

    boolean containsNonterminalElement();

    boolean isComposite();

    void changeElement(IElement before, IElement after);

    int size();

    Object clone();

    String toBNFString();

    IElementArray shuffleElements();
}
