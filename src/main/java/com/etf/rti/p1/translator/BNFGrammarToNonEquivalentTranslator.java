package com.etf.rti.p1.translator;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.util.ArrayList;
import java.util.List;

public class BNFGrammarToNonEquivalentTranslator {

    private final String sequence;      // correct in bnfGrammar, should be incorrect in new
    private final AParser bnfParser;

    public BNFGrammarToNonEquivalentTranslator(AParser bnfParser, String sequence) {
        this.bnfParser = bnfParser;
        this.sequence = sequence;
    }

    public String translateToNonEquivalentBNF() {
        return null;
    }

    /**
     * Finds indexes of potential rules that makes sense to corrupt.
     *
     * @return indexes of rules
     */
    List<Integer> getRuleCandidatesToCorrupt(List<IRule> rules) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i < rules.size(); i++) {
            if (rules.get(i).containsTerminal()) {
                result.add(i);
            }
        }

        return result;
    }

    public AParser getBnfParser() {
        return bnfParser;
    }
}
