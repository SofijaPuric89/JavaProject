package com.etf.rti.p1.translator;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import nl.bigo.rrdantlr4.DiagramGenerator;
//import org.antlr.works.Console;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

/**
 * TODO: implement class
 */
public class BNFGrammarToSyntaxDiagramTranslator {

    /**
     * TODO: try to refactor multiple usages of same code as in translateBnf method
     * TODO: check if this is needed, and what this method should have as input/output
     *
     * @param grammar
     * @throws Exception
     */
    public void transformToSyntaxDiagramOld(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
//        String[] args = {"-f", pathToGrammarFile.toString(), "-o", "output", "-sd", "eps", "-verbose"};
//        Console.main(args);
    }

    public File transformToSyntaxDiagram(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
        
        DiagramGenerator generator = new DiagramGenerator(pathToGrammarFile.toString());
        return generator.createDiagram();
    }

    public String transformToSyntaxDiagramHTML(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
        DiagramGenerator generator = new DiagramGenerator(pathToGrammarFile.toString());
        Set<String> rules = generator.getRules().keySet();
        String ruleName = rules.toArray(new String[rules.size()])[0];
        generator.createHtml(ruleName);
        return generator.getHtml(ruleName);
//        return new File("output/test/" + ruleName + ".html");
    }

    //TODO: extract these code and reuse instead of coping the same concept
    private Path translateBnf(String input) throws Exception {
        Path tmpDir = Files.createTempDirectory("test");
        tmpDir.toFile().deleteOnExit();
        final String name = "test";
        Path grammarDirPath = tmpDir.resolve(name + ".g4");
        String grammarName = grammarDirPath.toAbsolutePath().toString();
        FileOutputStream out = new FileOutputStream(grammarName);

        BNFCompiler compiler = new BNFCompiler(name, out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        AParser parser = compiler.getParser();
        parser.init();
        return grammarDirPath.toAbsolutePath();
    }
}
