package com.etf.rti.p1.translator;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.questions.GrammarChecker;
import com.etf.rti.p1.translator.corruptbnf.CorruptBNFRuleStrategy;
import com.etf.rti.p1.translator.corruptbnf.ExcludeFirstChildNodeStrategy;
import com.etf.rti.p1.translator.corruptbnf.RecursiveToNonRecursiveNodeStrategy;
import com.etf.rti.p1.translator.corruptbnf.ShuffleElementsInCompositeNodeStrategy;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BNFGrammarToNonEquivalentTranslator {

    private final String sequence;      // correct in bnfGrammar, should be incorrect in new
    private final AParser bnfParser;

    private final List<CorruptBNFRuleStrategy> strategies;

    public BNFGrammarToNonEquivalentTranslator(AParser bnfParser, String sequence) {
        this.bnfParser = bnfParser;
        this.sequence = sequence;
        this.strategies = defineStrategies();
    }

    private List<CorruptBNFRuleStrategy> defineStrategies() {
        List<CorruptBNFRuleStrategy> strategies = new ArrayList<>();
        strategies.add(new RecursiveToNonRecursiveNodeStrategy());
        strategies.add(new ExcludeFirstChildNodeStrategy());
        strategies.add(new ShuffleElementsInCompositeNodeStrategy());
        return strategies;
    }

    public String translateToNonEquivalentBNF() {
        if (sequence == null || sequence.isEmpty()) {
            return null;
        }
        List<IRule> bnfRules = bnfParser.getRules();
        List<Integer> ruleIndexesToCorrupt = getRuleCandidatesToCorrupt(bnfRules);
        randomize(ruleIndexesToCorrupt);
        randomize(strategies);
        for (Integer indexToCorrupt : ruleIndexesToCorrupt) {
            for (CorruptBNFRuleStrategy strategy : strategies) {
                List<IRule> corruptedGrammar = strategy.tryToCorrupt(bnfRules, indexToCorrupt);
                if (corruptedGrammar != null) {
                    String corruptedGrammarString = Utils.listOfRulesToBNFString(corruptedGrammar);
                    if (!isGrammaticallyCorrectForSequence(corruptedGrammarString)) {
                        return corruptedGrammarString;
                    }
                }
            }
        }
        return null;
    }

    private boolean isGrammaticallyCorrectForSequence(String corruptedGrammar) {
        try {
            GrammarChecker grammarChecker = new GrammarChecker(corruptedGrammar);
            return grammarChecker.isAnswerGrammaticallyCorrect(sequence);
        } catch (Exception e) {
            return false;
        }
    }

    private void randomize(List<?> list) {
        Collections.shuffle(list);
    }

    /**
     * Finds indexes of potential rules that makes sense to corrupt.
     *
     * @return indexes of rules
     */
    List<Integer> getRuleCandidatesToCorrupt(List<IRule> rules) {
        if ((rules == null) || (rules.size() < 2)) {
            return Collections.emptyList();
        }
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i < rules.size(); i++) {
            if (rules.get(i).containsNonterminal()) {
                result.add(i);
            }
        }

        return result;
    }

    public AParser getBnfParser() {
        return bnfParser;
    }
}
