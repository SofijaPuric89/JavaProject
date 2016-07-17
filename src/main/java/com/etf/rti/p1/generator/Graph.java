package com.etf.rti.p1.generator;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.util.Utils;
import org.antlr.works.Console;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by zika on 15.12.2015..
 * TODO: change class name! Not fitting to logic!
 * TODO: Code duplicate in translateBnf as in GrammarChecker.setupCompiler() !!!
 */
public class Graph {
    private Path tmpDir;

    public void generate(String grammar) throws Exception {
        Path pathToGrammarFile = translateBnf(grammar);
        String[] args = {"-f", pathToGrammarFile.toString(), "-o", "output", "-sd", "eps", "-verbose"};
        Console.main(args);
    }

    private Path translateBnf(String input) throws Exception {
        tmpDir = Files.createTempDirectory("test");
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
