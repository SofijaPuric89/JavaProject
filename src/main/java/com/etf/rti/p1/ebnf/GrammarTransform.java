package com.etf.rti.p1.ebnf;

import com.etf.rti.p1.ebnf.rules.IRule;
import com.etf.rti.p1.ebnf.transformations.ITransform;
import com.etf.rti.p1.ebnf.transformations.TransformInnerOption;
import com.etf.rti.p1.ebnf.transformations.TransformRecursion;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * Created by sule on 12/12/15.
 */
public class GrammarTransform {
    private LinkedList<IRule> rules;
    private PrintStream output;
    private LinkedList<ITransform> transformations = new LinkedList<ITransform>();

    public GrammarTransform(LinkedList<IRule> rules, OutputStream output) {
        this.rules = rules;
        this.output = new PrintStream(output);
        transformations.addLast(new TransformInnerOption());
        transformations.addLast(new TransformRecursion());
    }

    public void doJob() {

        for (IRule r : rules) {
            IRule done = r;
            for (ITransform t : transformations) {
                done = t.transform(done);
            }

            output.println(done);
        }

    }
}
