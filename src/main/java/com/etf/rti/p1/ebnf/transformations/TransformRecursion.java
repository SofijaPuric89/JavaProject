package com.etf.rti.p1.ebnf.transformations;

import com.etf.rti.p1.ebnf.arrays.IElementArray;
import com.etf.rti.p1.ebnf.arrays.SimpleArray;
import com.etf.rti.p1.ebnf.elements.Special;
import com.etf.rti.p1.ebnf.rules.IRule;
import com.etf.rti.p1.ebnf.rules.SimpleRule;

import java.util.Iterator;

/**
 * Created by sule on 12/13/15.
 */
public class TransformRecursion implements ITransform {

    public IRule transform(IRule t) {
        SimpleArray rule = new SimpleArray();
        rule.addElementSuffix(t.getRule());

        t = (IRule) t.clone();

        IRule prefixes, suffixes;
        Iterator<IElementArray> iter = t.getNonRecursiveIterator();
        IElementArray nonrecursive = iter.next();
        iter.remove();

        prefixes = new SimpleRule();
        suffixes = new SimpleRule();
        prefixes.setRule(t.getRule());
        suffixes.setRule(t.getRule());
        iter = t.getRecursiveIterator();
        while (iter.hasNext()) {
            IElementArray recursive = iter.next();
            if (recursive.isPrefix(rule)) {
                iter.remove();
                suffixes.insertOption(recursive.getWithoutPrefix(rule));
            } else if (recursive.isSuffix(rule)) {
                iter.remove();
                prefixes.insertOption(recursive.getWithoutSuffix(rule));
            }
        }

        IElementArray result = new SimpleArray();
        createRepetition(prefixes, result);
        result.addSuffix(nonrecursive);
        createRepetition(suffixes, result);
        t.insertOption(result);


        merge(t);

        return t;
    }

    private void createRepetition(IRule prefixes, IElementArray result) {
        if (prefixes.size() > 0) {
            result.addElementSuffix(new Special("{"));
            result.addSuffix(TransformOption.createOneChoice(TransformOption.ruleToArray(prefixes)));
            result.addElementSuffix(new Special("}"));
        }
    }

    private void merge(IRule t) {
        if (t.size() > 1) {
            IElementArray merged = TransformOption.createOneChoice(TransformOption.ruleToArray(t));
            Iterator<IElementArray> iter = t.iterator();
            while(iter.hasNext()) {
                iter.next();
                iter.remove();
            }
            t.insertOption(merged);
        }
    }
}
