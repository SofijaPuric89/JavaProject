package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class CorruptBNFRuleStrategy {
    public abstract List<IRule> tryToCorrupt(List<IRule> bnfRules, Integer indexToCorrupt);

    protected List<IRule> safeCopyOfBnfRules(List<IRule> bnfRules, int indexToCorrupt, IRule corruptedRule) {
        // prevent side-effects to existing BNF rules
        List<IRule> shallowCopy = new ArrayList<>(bnfRules);
        shallowCopy.set(indexToCorrupt, corruptedRule);
        return Collections.unmodifiableList(shallowCopy);
    }
}
