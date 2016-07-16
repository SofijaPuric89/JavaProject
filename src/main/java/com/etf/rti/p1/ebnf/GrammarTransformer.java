package com.etf.rti.p1.ebnf;

import com.etf.rti.p1.ebnf.rules.IRule;
import com.etf.rti.p1.ebnf.transformations.ITransform;
import com.etf.rti.p1.ebnf.transformations.TransformInnerOption;
import com.etf.rti.p1.ebnf.transformations.TransformRecursion;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * Transforming grammar rules and stream transformed rules to provided output stream
 * TODO: make GrammarTransformer more generic by parametrize rules/grammar/notation...
 */
public class GrammarTransformer {
    private LinkedList<IRule> rules;
    private PrintStream output;
    private LinkedList<ITransform> transformations = new LinkedList<>();

    //TODO: instead of list of rules as a parameter, try putting grammar
    public GrammarTransformer(LinkedList<IRule> rules, OutputStream output) {
        this.rules = rules;
        this.output = new PrintStream(output);
        transformations.addLast(new TransformInnerOption());
        transformations.addLast(new TransformRecursion());
    }

    /**
     * Printing transformed list of rules
     */
    public void transform() {
        for (IRule rule : rules) {
            IRule transformedRule = rule;
            for (ITransform transformation : transformations) {
                transformedRule = transformation.transform(transformedRule);
            }
            output.println(transformedRule);
        }
    }
}
