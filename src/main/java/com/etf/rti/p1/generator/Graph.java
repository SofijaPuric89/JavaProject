package com.etf.rti.p1.generator;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.util.Util;
import org.antlr.works.Console;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by zika on 15.12.2015..
 */
public class Graph {
    public Path grammarFile;
    private AParser parser;

    private Generator generator;
    private Path tmpDir;

    public void generate(String input) throws Exception {
        translateBnf(input);
        String[] args = {"-f", grammarFile.toAbsolutePath().toString(), "-o", "output","-sd", "eps", "-verbose"};
        Console.main(args);
        Util.deleteFolder(tmpDir.toFile());
    }

    public void translateBnf(String input) throws Exception{
        tmpDir = Files.createTempDirectory("test");
        final String name = "test";
        Path grammar =  tmpDir.resolve(name + ".g4");
        String grammarName = grammar.toAbsolutePath().toString();
        generator = new Generator(grammarName);
        FileOutputStream out = new FileOutputStream(grammarName);

        BNFCompiler compiler = new BNFCompiler(name, "com.etf.rti.p1.bnf", out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        parser = compiler.getParser();
        parser.init();
        grammarFile = grammar;
    }
}
