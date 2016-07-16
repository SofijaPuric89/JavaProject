package com.etf.rti.p1.correct;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.exceptions.Exception;
import com.etf.rti.p1.generator.AParser;
import com.etf.rti.p1.generator.Compiler;
import com.etf.rti.p1.generator.CompilerGenerator;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Korisnik on 18.1.2016.
 */
public class GrammarChecker {

    private String grammar;
    private Compiler compiler;

    public GrammarChecker(String grammar) throws java.lang.Exception {
        this.grammar = grammar;

        setupCompiler();
    }

    private void setupCompiler() throws java.lang.Exception {
        Path tmpDir = Files.createTempDirectory("test");
        tmpDir.toFile().deleteOnExit();
        final String name = "test";
        Path grammar = tmpDir.resolve(name + ".g4");

        String grammarName = grammar.toAbsolutePath().toString();
        CompilerGenerator compilerGenerator = new CompilerGenerator(grammarName);
        FileOutputStream out = new FileOutputStream(grammarName);

        //TODO: parametrize compiler, it could be maybe Compiler interface instead of BNFCompiler
        BNFCompiler bnfCompiler = new BNFCompiler(name, "com.etf.rti.p1.bnf", out);
        bnfCompiler.setInput(new ByteArrayInputStream(this.grammar.getBytes("UTF-8")));
        AParser parser = bnfCompiler.getParser();
        parser.init();
        this.compiler = compilerGenerator.generate();
    }

    public boolean isAnswerGramaticallyCorrect(String answer) {
        compiler.reset();
        try {
            compiler.setInput(new ByteArrayInputStream(answer.getBytes("UTF-8")));
            compiler.getParser().init();
            return compiler.getParser().getNumberOfSyntaxErrors() <= 0;
        } catch (Exception | IOException e) {
            System.err.println(e);
            return false;
        }
    }

}
