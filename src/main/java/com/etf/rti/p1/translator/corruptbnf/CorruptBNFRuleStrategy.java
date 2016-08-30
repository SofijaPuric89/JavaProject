package com.etf.rti.p1.translator.corruptbnf;

import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.util.List;

public interface CorruptBNFRuleStrategy {
    List<IRule> tryToCorrupt(List<IRule> bnfRules, Integer indexToCorrupt);
}
