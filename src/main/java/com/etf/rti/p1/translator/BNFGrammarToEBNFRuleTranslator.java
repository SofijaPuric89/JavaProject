package com.etf.rti.p1.translator;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.translator.ebnf.transformations.ITransform;
import com.etf.rti.p1.translator.ebnf.transformations.TransformInnerOption;
import com.etf.rti.p1.translator.ebnf.transformations.TransformRecursion;
//import org.antlr.works.Console;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 * Transforming grammar rules to EBNF notation for provided rules in BNF notation.
 * <p>
 * +---------------------+      |-----------------------|      +----------------------+
 * |Rules in BNF notation+ +--> ||BNFGrammarToEBNFRuleTranslator|| +--> |Rules in EBNF notation|
 * +---------------------+      |-----------------------|      +----------------------+
 */
public class BNFGrammarToEBNFRuleTranslator {
    private List<ITransform> bnfToEbnfTransformations = new LinkedList<>();

    public BNFGrammarToEBNFRuleTranslator() {
        bnfToEbnfTransformations.add(new TransformInnerOption());
        bnfToEbnfTransformations.add(new TransformRecursion());
    }

    public List<IRule> transformToEBNF(String bnfGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(bnfGrammar.getBytes("UTF-8")));
        compiler.getParser().init();
        return transformToEBNF(compiler.getParser().getRules());
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
