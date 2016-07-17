package com.etf.rti.p1.transformer.transformations;

import com.etf.rti.p1.transformer.arrays.IElementArray;
import com.etf.rti.p1.transformer.arrays.SimpleArray;
import com.etf.rti.p1.transformer.elements.IElement;
import com.etf.rti.p1.transformer.rules.IRule;
import com.etf.rti.p1.transformer.rules.SimpleRule;

import java.util.Iterator;

/**
 * Created by sule on 12/13/15.
 */
public class TransformInnerOption extends TransformOption {
    public IRule transform(IRule t) {
        IRule retRule;
        t = (IRule) t.clone();
        retRule = removeSomeChoices(t, t.getRecursiveIterator());

        mergeInnerOption(t);

        merge(retRule, null, t, null);

        t = retRule;
        retRule = removeSomeChoices(t, t.getNonRecursiveIterator());

        mergeInnerOption(t);

        addChangedChoices(t, retRule);

        return retRule;
    }

    protected interface IInnerJob extends IJob {
        IElementArray match(IElementArray array1, IElementArray array2, IElement rule);
    }

    protected class InnerPrefixJob extends PrefixJob implements IInnerJob {
        public IElementArray match(IElementArray array1, IElementArray array2, IElement rule) {

            IElementArray samePrefix = array1.getSamePrefix(array2);
            IElementArray resto = array1.getWithoutPrefix(samePrefix);
            if (resto.hasElement(rule))
                return new SimpleArray();
            resto = array2.getWithoutPrefix(samePrefix);
            if (resto.hasElement(rule))
                return new SimpleArray();
            return samePrefix;
        }
    }

    protected class InnerSuffixJob extends SuffixJob implements IInnerJob {
        public IElementArray match(IElementArray array1, IElementArray array2, IElement rule) {
            IElementArray sameSuffix = array1.getSameSuffix(array2);
            IElementArray resto = array1.getWithoutSuffix(sameSuffix);
            if (resto.hasElement(rule))
                return new SimpleArray();
            resto = array2.getWithoutSuffix(sameSuffix);
            if (resto.hasElement(rule))
                return new SimpleArray();
            return sameSuffix;
        }
    }

    private IInnerJob innerPrefixJob = new InnerPrefixJob();

    private IInnerJob innerSuffixJob = new InnerSuffixJob();

    protected void mergeInnerOption(IRule t) {
        mergeWholeOption(t);

        boolean toTry = true;

        while (toTry) {
            toTry = tryMerge(t, innerPrefixJob);
            toTry = toTry || tryMerge(t, innerSuffixJob);
        }
    }

    private boolean tryMerge(IRule t, IInnerJob job) {
        IElementArray match;
        match = findMax(t, job);
        if (match.size() > 0) {
            IRule newT = new SimpleRule();
            newT.setRule((IElement) t.getRule().clone());
            Iterator<IElementArray> iter = t.iterator();
            while (iter.hasNext()) {
                IElementArray array = iter.next();
                if (job.isMatch(match, array, t.getRule())) {
                    newT.insertOption(job.cut(match, array));
                    iter.remove();
                }
            }
            int oldSize = newT.size();
            mergeInnerOption(newT);
            if (oldSize == newT.size()) {
                merge(t, job, newT, match);
                return true;
            }
            iter = newT.iterator();
            while (iter.hasNext()) {
                IElementArray array = iter.next();
                t.insertOption(job.merge(match, array));
                iter.remove();
            }
            return true;
        }
        return false;
    }

    private IElementArray findMax(IRule t, IInnerJob job) {
        Iterator<IElementArray> iter1, iter2;
        int max = 0;
        IElementArray ret = new SimpleArray();
        iter1 = t.iterator();
        while (iter1.hasNext()) {
            iter2 = t.iterator();
            IElementArray array1, array2;
            array1 = iter1.next();
            while (iter2.hasNext()) {
                array2 = iter2.next();
                if (array1.isEqual(array2))
                    continue;
                IElementArray match = job.match(array1, array2, t.getRule());
                if (match.size() > max) {
                    ret = match;
                }
            }
        }

        return ret;
    }


}
