package com.etf.rti.p1.questions;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.Compiler;
import com.etf.rti.p1.compiler.CompilerGenerator;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GrammarChecker {

    private String grammar;
    private Compiler compiler;

    public GrammarChecker(String grammar) throws Exception {
        this.grammar = grammar;

        setupCompiler();
    }

    private void setupCompiler() throws Exception {
        Path tmpDir = Files.createTempDirectory("grammar");
        tmpDir.toFile().deleteOnExit();
        final String name = tmpDir.getFileName().toString();
        Path grammar = tmpDir.resolve(name + ".g4");

        String grammarName = grammar.toAbsolutePath().toString();
        CompilerGenerator compilerGenerator = new CompilerGenerator(grammarName);
        FileOutputStream out = new FileOutputStream(grammarName);

        //TODO: parametrize compiler, it could be maybe Compiler interface instead of BNFCompiler
        BNFCompiler bnfCompiler = new BNFCompiler(name, out);
        bnfCompiler.setInput(new ByteArrayInputStream(this.grammar.getBytes("UTF-8")));
        AParser parser = bnfCompiler.getParser();
        parser.init();
        this.compiler = compilerGenerator.generate();
    }

    public boolean isAnswerGrammaticallyCorrect(String answer) {
        compiler.reset();
        try {
            compiler.setInput(new ByteArrayInputStream(answer.getBytes("UTF-8")));
            compiler.getParser().init();
            return compiler.getParser().getNumberOfSyntaxErrors() <= 0;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

}
