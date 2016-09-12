package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.util.List;

public class ExcludeFirstChildNodeStrategy extends CorruptBNFRuleStrategy {
    @Override
    public List<IRule> tryToCorrupt(List<IRule> bnfRules, Integer indexToCorrupt) {
        IRule ruleToCorrupt = bnfRules.get(indexToCorrupt).deepCopy();
        int numberOfOptions = ruleToCorrupt.size();
        if (numberOfOptions <= 1) {
            return null; //strategy will not work if there is not at least 2 options in rule
        }
        System.out.println("Found rule with proper number of child nodes");

        ruleToCorrupt.removeOption(0); //removing first child

        return safeCopyOfBnfRules(bnfRules, indexToCorrupt, ruleToCorrupt);
    }
}
