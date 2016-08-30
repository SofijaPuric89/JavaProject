package com.etf.rti.p1.translator.ebnf.rules;

import com.etf.rti.p1.translator.ebnf.arrays.IElementArray;
import com.etf.rti.p1.translator.ebnf.arrays.SimpleArray;
import com.etf.rti.p1.translator.ebnf.elements.IElement;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

/**
 * Created by sule on 12/12/15.
 */
public class SimpleRule implements IRule {
    private LinkedList<IElementArray> options = new LinkedList<IElementArray>();
    private IElementArray newOption;
    private IElement nonterminal;

    public SimpleRule() {
        newOption = new SimpleArray();
    }

    private abstract class Iterator implements IIterator {
        protected ListIterator<IElementArray> iter = SimpleRule.this.options.listIterator(0);

        protected abstract void prepareNext();

        public boolean hasNext() {
            prepareNext();
            return iter.hasNext();
        }

        public IElementArray next() {
            return (IElementArray) iter.next().clone();
        }

        public void remove() {
            iter.remove();
        }
    }

    public IIterator getRecursiveIterator() {
        return new SimpleRule.Iterator() {
            @Override
            protected void prepareNext() {
                while (iter.hasNext()) {
                    if (iter.next().hasElement(nonterminal)) {
                        iter.previous();
                        return;
                    }
                }
            }
        };
    }

    public IIterator getNonRecursiveIterator() {
        return new SimpleRule.Iterator() {
            @Override
            protected void prepareNext() {
                while (iter.hasNext()) {
                    if (!iter.next().hasElement(nonterminal)) {
                        iter.previous();
                        return;
                    }
                }
            }
        };
    }

    public IRule insertOption(IElementArray a) {
        options.addLast(a);

        return this;
    }

    public IRule setNewOptionForInsert() {
        if (newOption.size() > 0) {
            options.addLast(newOption);
            newOption = new SimpleArray();
        }
        return this;
    }

    public IRule insertElem(IElement e) {
        newOption.addElementSuffix(e);
        return this;
    }

    public IElement getRule() {
        return nonterminal;
    }

    public void setRule(IElement e) {
        nonterminal = e;
    }

    public int size() {
        return options.size();
    }

    public java.util.Iterator<IElementArray> iterator() {
        return options.iterator();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(nonterminal);
        sb.append(" = ");
        if (options.size() > 2) {
            sb.append("( ");
        }
        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).size() == 0)
                continue;
            if (i > 0) {
                sb.append(" | ");
                sb.append(options.get(i));
            } else {
                sb.append(options.get(i));
            }
        }

        if (options.size() > 2) {
            sb.append(" )");
        }

        sb.append('.');

        return sb.toString();
    }

    public Object clone() {
        return copy((LinkedList<IElementArray>) options.clone());
    }

    @Override
    public IRule deepCopy() {
        LinkedList<IElementArray> optionsCopy = options.stream()
                .map(option -> (IElementArray) option.clone())
                .collect(Collectors.toCollection(LinkedList::new));
        return copy(optionsCopy);
    }

    private IRule copy(LinkedList<IElementArray> options) {
        SimpleRule ret = null;
        try {
            ret = (SimpleRule) super.clone();
            ret.options = options;
            ret.nonterminal = (IElement) nonterminal.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public boolean containsNonterminal() {
        return options.stream().anyMatch(IElementArray::containsNonterminalElement);
    }

    @Override
    public IElementArray getCompositeDirectRecursiveNode() {
        return options.stream()
                .filter(IElementArray::isComposite)
                .filter(this::isDirectRecursive)
                .findAny()
                .orElse(null);
    }

    @Override
    public String toBNFString() {
        StringBuilder sb = new StringBuilder();

        sb.append(nonterminal.toBNFString());
        sb.append(" ::= ");

        for (int i = 0; i < options.size(); i++) {
            if (options.get(i).size() == 0)
                continue;
            if (i > 0) {
                sb.append(" | ");
                sb.append(options.get(i).toBNFString());
            } else {
                sb.append(options.get(i).toBNFString());
            }
        }

        return sb.toString();
    }

    private boolean isDirectRecursive(IElementArray elementArray) {
        return elementArray.hasElement(nonterminal);
    }
}
