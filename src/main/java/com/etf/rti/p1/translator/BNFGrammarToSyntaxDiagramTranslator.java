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
        String[] args = {"-f", pathToGrammarFile.toString(), "-o", "output", "-sd", "eps", "-verbose"};
//        Console.main(args);
    }

    public File transformToSyntaxDiagram(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
//        String grammarFileName = "/path/to/Json.g4";
        DiagramGenerator generator = new DiagramGenerator(pathToGrammarFile.toString());
//
//// Print all parsed rules
        System.out.println("all parsed rules: " + generator.getRules().keySet());
//
//// The name of the rule to create a railroad diagram of
        String ruleName = "a";
//
//// Get the SVG of the rule
        String svg = generator.getSVG(ruleName);
        System.out.println("the svg looks like this: " + svg);
//
//// Create the PNG railroad diagram
        boolean success = generator.createDiagram(ruleName);
        System.out.println("successfully created diagram: " + success);
//
//// Create an html file containing all rules
        success = generator.createDiagram(ruleName);

        System.out.println("successfully created the html file: " + success);
        return new File("output/test/" + ruleName + ".png");
    }

    private Path translateBnf(String input) throws Exception {
        Path tmpDir = Files.createTempDirectory("test");
        tmpDir.toFile().deleteOnExit();
        final String name = "test";
        Path grammarDirPath = tmpDir.resolve(name + ".g4");
        String grammarName = grammarDirPath.toAbsolutePath().toString();
        FileOutputStream out = new FileOutputStream(grammarName);

        BNFCompiler compiler = new BNFCompiler(name, "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        AParser parser = compiler.getParser();
        parser.init();
        return grammarDirPath.toAbsolutePath();
    }
}
