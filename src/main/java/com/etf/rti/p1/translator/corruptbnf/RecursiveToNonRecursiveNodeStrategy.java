package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.translator.ebnf.arrays.IElementArray;
import com.etf.rti.p1.translator.ebnf.elements.IElement;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.util.Utils;

import java.util.List;


public class RecursiveToNonRecursiveNodeStrategy implements CorruptBNFRuleStrategy {

    @Override
    public List<IRule> tryToCorrupt(List<IRule> bnfRules, Integer indexToCorrupt) {
        if (bnfRules.size() < 3) {
            // with this strategy we need more than one rule after initial rule
            return null;
        }
        IRule ruleToCorrupt = bnfRules.get(indexToCorrupt);
        IElementArray node = ruleToCorrupt.getCompositeDirectRecursiveNode();
        if (node == null) {
            return null;
        }
        System.out.println("Found composite direct recursive: " + node);
        IElement nonTerminalToExchangeWith;
        if (indexToCorrupt < bnfRules.size() - 1) {
            // if we are not at the last rule
            nonTerminalToExchangeWith = bnfRules.get(indexToCorrupt + 1).getRule();
        } else {
            // we are the last rule
            nonTerminalToExchangeWith = bnfRules.get(indexToCorrupt - 1).getRule();
        }
        node.changeElement(ruleToCorrupt.getRule(), nonTerminalToExchangeWith);
        return bnfRules;
    }
}
