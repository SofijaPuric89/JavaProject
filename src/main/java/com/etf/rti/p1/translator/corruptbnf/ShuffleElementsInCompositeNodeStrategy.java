package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.translator.ebnf.arrays.IElementArray;
import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.util.List;

public class ShuffleElementsInCompositeNodeStrategy extends CorruptBNFRuleStrategy {
    @Override
    public List<IRule> tryToCorrupt(List<IRule> bnfRules, Integer indexToCorrupt) {
        IRule ruleToCorrupt = bnfRules.get(indexToCorrupt).deepCopy();
        IElementArray compositeNode = ruleToCorrupt.getCompositeNode();
        if (compositeNode == null) {
            return null;
        }
        System.out.println("Found composite node");

        compositeNode.shuffleElements();

        return safeCopyOfBnfRules(bnfRules, indexToCorrupt, ruleToCorrupt);
    }
}
