package com.etf.rti.p1.transformer.transformations;

import com.etf.rti.p1.transformer.arrays.IElementArray;
import com.etf.rti.p1.transformer.arrays.SimpleArray;
import com.etf.rti.p1.transformer.elements.IElement;
import com.etf.rti.p1.transformer.elements.Special;
import com.etf.rti.p1.transformer.rules.IRule;
import com.etf.rti.p1.transformer.rules.SimpleRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by sule on 12/12/15.
 */
public abstract class TransformOption implements ITransform {
    public IRule transform(IRule t) {
        IRule retRule;
        t = (IRule) t.clone();
        retRule = removeSomeChoices(t, t.getRecursiveIterator());

        mergeWholeOption(t);

        addChangedChoices(t, retRule);

        t = retRule;
        retRule = removeSomeChoices(t, t.getNonRecursiveIterator());

        mergeWholeOption(t);

        addChangedChoices(t, retRule);

        return retRule;
    }

    protected void addChangedChoices(IRule t, IRule retRule) {
        Iterator<IElementArray> iter;
        iter = t.iterator();
        while (iter.hasNext()) {
            retRule.insertOption(iter.next());
            iter.remove();
        }
    }

    protected IRule removeSomeChoices(IRule t, Iterator<IElementArray> iter) {
        IRule retRule;
        retRule = new SimpleRule();
        retRule.setRule(t.getRule());

        while (iter.hasNext()) {
            retRule.insertOption(iter.next());
            iter.remove();
        }
        return retRule;
    }

    protected interface IJob {
        boolean isMatch(IElementArray toFindArray, IElementArray searchArray, IElement rule);

        IElementArray cut(IElementArray toFindArray, IElementArray searchArray);

        IElementArray merge(IElementArray baseArray, IElementArray optionsArray);
    }

    protected class PrefixJob implements IJob {
        public boolean isMatch(IElementArray toFindArray, IElementArray searchArray, IElement rule) {
            boolean ret = searchArray.isPrefix(toFindArray);
            if (ret) {
                ret = !searchArray.getWithoutPrefix(toFindArray).hasElement(rule);
            }
            return ret;
        }

        public IElementArray cut(IElementArray toFindArray, IElementArray searchArray) {
            return searchArray.removePrefix(toFindArray);
        }

        public IElementArray merge(IElementArray baseArray, IElementArray optionsArray) {
            return optionsArray.addPrefix(baseArray);
        }
    }

    protected class SuffixJob implements IJob {
        public boolean isMatch(IElementArray toFindArray, IElementArray searchArray, IElement rule) {
            boolean ret = searchArray.isSuffix(toFindArray);
            if (ret) {
                ret = !searchArray.getWithoutSuffix(toFindArray).hasElement(rule);
            }
            return ret;
        }

        public IElementArray cut(IElementArray toFindArray, IElementArray searchArray) {
            return searchArray.removeSuffix(toFindArray);
        }

        public IElementArray merge(IElementArray baseArray, IElementArray optionsArray) {
            return optionsArray.addSuffix(baseArray);
        }
    }

    protected final void mergeWholeOption(IRule t) {

        IJob prefixJob = new PrefixJob();

        IJob suffixJob = new SuffixJob();

        boolean tryMerge = true;

        while (tryMerge) {
            tryMerge = isTryMerge(t, prefixJob);
            tryMerge = tryMerge || isTryMerge(t, suffixJob);
        }

    }

    private boolean isTryMerge(IRule t, IJob job) {
        boolean tryMerge;
        IElementArray maxArray;
        ArrayList<IElementArray> match;
        tryMerge = false;
        maxArray = getNumMatch(t, job);
        if (maxArray != null) {
            match = getMatch(t, job, maxArray);

            IRule tmp = arrayToRule(t.getRule(), match);
            mergeInnerOption(tmp);
            match = ruleToArray(tmp);

            merge(t, job, match, maxArray);
            tryMerge = true;
        }
        return tryMerge;
    }

    protected abstract void mergeInnerOption(IRule t);

    public static IRule arrayToRule(IElement rule, ArrayList<IElementArray> list) {
        IRule ret = new SimpleRule();
        ret.setRule((IElement) rule.clone());
        for (IElementArray aList : list) {
            ret.insertOption(aList);
        }
        return ret;
    }

    private IElementArray getNumMatch(IRule t, IJob job) {
        IElementArray maxArray = null;
        int max = 0;
        for (IElementArray array1 : t) {
            Iterator<IElementArray> iter2 = t.iterator();
            int localMax = 0;
            while (iter2.hasNext()) {
                IElementArray array2 = iter2.next();
                if (array1.isEqual(array2))
                    continue;
                if (job.isMatch(array1, array2, t.getRule())) {
                    localMax++;
                }
            }

            if (localMax > max) {
                max = localMax;
                maxArray = array1;
            }
        }

        return maxArray;
    }

    private ArrayList<IElementArray> getMatch(IRule t, IJob job, IElementArray array1) {
        ArrayList<IElementArray> ret = new ArrayList<IElementArray>();
        Iterator<IElementArray> iter = t.iterator();
        while (iter.hasNext()) {
            IElementArray array2 = iter.next();
            if (array1.isEqual(array2)) {
                iter.remove();
                continue;
            }

            if (job.isMatch(array1, array2, t.getRule())) {
                iter.remove();
                ret.add(job.cut(array1, array2));
            }
        }

        return ret;
    }

    protected void merge(IRule t, IJob job, IRule options, IElementArray array) {
        IElementArray oneChoice = createOneChoice(ruleToArray(options));
        if (job != null) {
            t.insertOption(job.merge(array, oneChoice));
        } else {
            t.insertOption(oneChoice);
        }

    }

    public static ArrayList<IElementArray> ruleToArray(IRule options) {
        Iterator<IElementArray> iter = options.iterator();
        ArrayList<IElementArray> list;
        list = new ArrayList<IElementArray>();
        while (iter.hasNext()) {
            list.add(iter.next());
        }
        return list;
    }

    private void merge(IRule t, IJob job, ArrayList<IElementArray> list, IElementArray array) {
        IElementArray result = createOneChoice(list);
        IElement elem;


        elem = new Special("[");
        result.addElementPrefix(elem);


        elem = new Special("]");
        result.addElementSuffix(elem);

        t.insertOption(job.merge(array, result));
    }

    public static IElementArray createOneChoice(ArrayList<IElementArray> list) {
        assert (list.size() > 0);
        boolean choices = list.size() > 1;
        IElementArray result = new SimpleArray();
        IElement elem;

        Collections.sort(list);

        if (choices) {

            elem = new Special("(");
            result.addElementSuffix(elem);
        }

        result.addSuffix(list.get(0));
        for (int i = 1; i < list.size(); i++) {

            elem = new Special("|");
            result.addElementSuffix(elem);
            result.addSuffix(list.get(i));
        }

        if (choices) {

            elem = new Special(")");
            result.addElementSuffix(elem);
        }
        return result;
    }
}
