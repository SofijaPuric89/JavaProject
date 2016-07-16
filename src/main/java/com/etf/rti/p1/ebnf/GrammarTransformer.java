package com.etf.rti.p1.ebnf;

import com.etf.rti.p1.ebnf.rules.IRule;
import com.etf.rti.p1.ebnf.transformations.ITransform;
import com.etf.rti.p1.ebnf.transformations.TransformInnerOption;
import com.etf.rti.p1.ebnf.transformations.TransformRecursion;
import com.etf.rti.p1.exceptions.Exception;
import com.etf.rti.p1.generator.Compiler;

import java.util.LinkedList;

/**
 * Transforming grammar rules to EBNF notation for provided compiler
 */
public class GrammarTransformer {
    private final Compiler compiler;
    private LinkedList<ITransform> transformations = new LinkedList<>();

    public GrammarTransformer(Compiler compiler) {
        this.compiler = compiler;
    }

    /**
     * Transforming parser rules to EBNF notation by performing two types of transformations:
     * inner option transformation and recursion transformation.
     */
    public LinkedList<IRule> transformToEBNF() throws Exception {
        transformations.add(new TransformInnerOption());
        transformations.add(new TransformRecursion());
        return transformRules();
    }

    /**
     * Transforming all rules from parser with set transformations
     *
     * @return list of transformed rules
     */
    public LinkedList<IRule> transformRules() throws Exception {
        LinkedList<IRule> transformedRules = new LinkedList<>();
        for (IRule rule : compiler.getParser().getRules()) {
            IRule transformedRule = rule;
            for (ITransform transformation : transformations) {
                transformedRule = transformation.transform(transformedRule);
            }
            transformedRules.add(transformedRule);
        }
        return transformedRules;
    }
}
