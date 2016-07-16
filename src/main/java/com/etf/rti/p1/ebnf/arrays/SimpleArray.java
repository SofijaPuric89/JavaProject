package com.etf.rti.p1.ebnf.arrays;

import com.etf.rti.p1.ebnf.elements.IElement;
import com.etf.rti.p1.ebnf.elements.Special;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by sule on 12/12/15.
 */
public class SimpleArray implements IElementArray {
    private LinkedList<IElement> list = new LinkedList<IElement>();

    public boolean samePrefix(IElementArray a) {
        return getSamePrefix(a).size() > 0;
    }

    public boolean sameSuffix(IElementArray a) {
        return getSameSuffix(a).size() > 0;
    }

    public boolean hasElement(IElement e) {
        for (IElement x : list) {
            if (x.isEqual(e))
                return true;
        }
        return false;
    }

    public int compareTo(IElementArray o) {
        String s1 = this.toString();
        String s2 = o.toString();
        return s1.compareTo(s2);
    }

    private static class ElementStack {
        private LinkedList<IElement> stack = new LinkedList<IElement>();
        private Special.Direction direction = null;

        public void add(IElement e) {
            if (!e.isSpecial()) {
                return;
            }

            Special.Direction elemDir = Special.getDirection(e);

            if (direction == null) {
                direction = elemDir.oposite();
            }

            if (direction == elemDir) {
                ListIterator<IElement> iter = stack.listIterator(stack.size());
                while (iter.hasPrevious()) {
                    if (e.hiralMatch(iter.previous())) {
                        iter.remove();
                        break;
                    }
                    iter.remove();
                }
            } else {
                stack.addLast(e);
            }
        }

        public boolean isComplete() {
            return stack.size() == 0;
        }

    }

    public IElementArray getSamePrefix(IElementArray a) {
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(0);
        iter2 = a.iterator(0);
        IElementArray array = new SimpleArray();
        ElementStack stack = new ElementStack();
        while (iter1.hasNext() && iter2.hasNext()) {
            IElement elem1, elem2;
            elem1 = iter1.next();
            elem2 = iter2.next();
            if (elem1.isEqual(elem2)) {
                array.addElementSuffix((IElement) elem1.clone());
                stack.add((IElement) elem1.clone());
            } else {
                break;
            }
        }

        if (stack.isComplete())
            return array;

        return new SimpleArray();
    }

    public IElementArray getSameSuffix(IElementArray a) {
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(size());
        iter2 = a.iterator(a.size());
        IElementArray array = new SimpleArray();
        ElementStack stack = new ElementStack();
        while (iter1.hasPrevious() && iter2.hasPrevious()) {
            IElement elem1, elem2;
            elem1 = iter1.previous();
            elem2 = iter2.previous();
            if (elem1.isEqual(elem2)) {
                array.addElementPrefix((IElement) elem1.clone());
                stack.add((IElement) elem1.clone());
            } else {
                break;
            }
        }

        if (stack.isComplete())
            return array;

        return new SimpleArray();
    }

    public IElementArray getWithoutPrefix(IElementArray a) {
        if (!this.isPrefix(a))
            return null;
        SimpleArray ret = new SimpleArray();
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(0);
        iter2 = a.iterator(0);
        while (iter1.hasNext() && iter2.hasNext()) {
            iter1.next();
            iter2.next();
        }

        while (iter1.hasNext()) {
            ret.addElementSuffix(iter1.next());
        }

        return ret;
    }

    public IElementArray getWithoutSuffix(IElementArray a) {
        if (!this.isSuffix(a))
            return null;
        SimpleArray ret = new SimpleArray();
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(size());
        iter2 = a.iterator(a.size());

        while (iter1.hasPrevious() && iter2.hasPrevious()) {
            iter1.previous();
            iter2.previous();
        }

        while (iter1.hasPrevious()) {
            ret.addElementPrefix(iter1.previous());
        }
        return ret;
    }

    public boolean isPrefix(IElementArray a) {
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(0);
        iter2 = a.iterator(0);
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!iter1.next().isEqual(iter2.next())) {
                return false;
            }
        }

        return !iter2.hasNext();

    }

    public boolean isSuffix(IElementArray a) {
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(size());
        iter2 = a.iterator(a.size());

        while (iter1.hasPrevious() && iter2.hasPrevious()) {
            if (!iter1.previous().isEqual(iter2.previous())) {
                return false;
            }
        }

        return !iter2.hasPrevious();

    }

    public IElementArray removeSuffix(IElementArray a) {
        if (!this.isSuffix(a))
            return this;
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(size());
        iter2 = a.iterator(a.size());

        while (iter1.hasPrevious() && iter2.hasPrevious()) {
            iter1.previous();
            iter2.previous();
            iter1.remove();
        }
        return this;
    }

    public IElementArray removePrefix(IElementArray a) {
        if (!this.isPrefix(a))
            return this;
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(0);
        iter2 = a.iterator(0);
        while (iter1.hasNext() && iter2.hasNext()) {
            iter1.next();
            iter2.next();
            iter1.remove();
        }

        return this;
    }

    public IElementArray addElementPrefix(IElement e) {
        list.addFirst(e);
        return this;
    }

    public IElementArray addElementSuffix(IElement e) {
        list.addLast(e);
        return this;
    }

    public boolean isEqual(IElementArray a) {
        ListIterator<IElement> iter1, iter2;
        iter1 = iterator(0);
        iter2 = a.iterator(0);
        while (iter1.hasNext() && iter2.hasNext()) {
            if (!iter1.next().isEqual(iter2.next())) {
                return false;
            }
        }
        return !(iter1.hasNext() || iter2.hasNext());
    }

    public IElementArray addPrefix(IElementArray prefix) {
        ListIterator<IElement> iter1;
        iter1 = prefix.iterator(prefix.size());
        while (iter1.hasPrevious()) {
            list.addFirst(iter1.previous());
        }
        return this;
    }

    public IElementArray addSuffix(IElementArray suffix) {
        ListIterator<IElement> iter1;
        iter1 = suffix.iterator(0);
        while (iter1.hasNext()) {
            list.addLast(iter1.next());
        }
        return this;
    }

    public ListIterator<IElement> iterator(int pos) {
        return list.listIterator(pos);
    }

    public int size() {
        return list.size();
    }

    public Object clone() {
        SimpleArray ret = null;
        try {
            ret = (SimpleArray) super.clone();
            ret.list = (LinkedList<IElement>) list.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < list.size(); i++) {
            if (i != 0) {
                sb.append(' ');
            }
            sb.append(list.get(i).toString());
        }

        return sb.toString();
    }

}
