package com.etf.rti.p1.translator.ebnf.rules;

import com.etf.rti.p1.translator.ebnf.arrays.IElementArray;
import com.etf.rti.p1.translator.ebnf.arrays.SimpleArray;
import com.etf.rti.p1.translator.ebnf.elements.IElement;

import java.util.LinkedList;
import java.util.ListIterator;

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
        SimpleRule ret = null;
        try {
            ret = (SimpleRule) super.clone();
            ret.options = (LinkedList<IElementArray>) options.clone();
            ret.nonterminal = (IElement) nonterminal.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Override
    public boolean containsTerminal() {
        return options.stream().anyMatch(IElementArray::containsNonterminalElement);
    }
}
