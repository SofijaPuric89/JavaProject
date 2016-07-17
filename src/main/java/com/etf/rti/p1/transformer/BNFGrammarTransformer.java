package com.etf.rti.p1.transformer;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.generator.AParser;
import com.etf.rti.p1.transformer.rules.IRule;
import com.etf.rti.p1.transformer.transformations.ITransform;
import com.etf.rti.p1.transformer.transformations.TransformInnerOption;
import com.etf.rti.p1.transformer.transformations.TransformRecursion;
import org.antlr.works.Console;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**
     * TODO: try to refactor multiple usages of same code as in translateBnf method
     * TODO: check if this is needed, and what this method should have as input/output
     * @param grammar
     * @throws Exception
     */
    public void transformToSyntaxDiagram(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
        String[] args = {"-f", pathToGrammarFile.toString(), "-o", "output", "-sd", "eps", "-verbose"};
        Console.main(args);
    }

    private Path translateBnf(String input) throws Exception {
        Path tmpDir = Files.createTempDirectory("test");
        tmpDir.toFile().deleteOnExit();
        final String name = "test";
        Path grammarDirPath = tmpDir.resolve(name + ".g4");
        String grammarName = grammarDirPath.toAbsolutePath().toString();
        FileOutputStream out = new FileOutputStream(grammarName);

        BNFCompiler compiler = new BNFCompiler(name, "com.etf.rti.p1.bnf", out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        AParser parser = compiler.getParser();
        parser.init();
        return grammarDirPath.toAbsolutePath();
    }
}
