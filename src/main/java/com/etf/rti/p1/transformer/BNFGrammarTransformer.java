package com.etf.rti.p1.transformer;

import com.etf.rti.p1.transformer.rules.IRule;
import com.etf.rti.p1.transformer.transformations.ITransform;
import com.etf.rti.p1.transformer.transformations.TransformInnerOption;
import com.etf.rti.p1.transformer.transformations.TransformRecursion;

import java.util.LinkedList;
import java.util.List;

/**
 * Transforming grammar rules to EBNF notation for provided rules in BNF notation.
 * <p>
 * +---------------------+      |-----------------------|      +----------------------+
 * |Rules in BNF notation+ +--> ||BNFGrammarTransformer|| +--> |Rules in EBNF notation|
 * +---------------------+      |-----------------------|      +----------------------+
 */
public class BNFGrammarTransformer {
    private List<ITransform> bnfToEbnfTransformations = new LinkedList<>();

    public BNFGrammarTransformer() {
        bnfToEbnfTransformations.add(new TransformInnerOption());
        bnfToEbnfTransformations.add(new TransformRecursion());
    }

    /**
     * Transforming parser rules to EBNF notation by performing two types of transformations:
     * inner option transformation and recursion transformation.
     */
    public List<IRule> transformToEBNF(List<IRule> rulesInBnf) throws Exception {
        List<IRule> transformedRules = new LinkedList<>();
        for (IRule rule : rulesInBnf) {
            IRule transformedRule = rule;
            for (ITransform transformation : bnfToEbnfTransformations) {
                transformedRule = transformation.transform(transformedRule);
            }
            transformedRules.add(transformedRule);
        }
        return transformedRules;
    }
}
