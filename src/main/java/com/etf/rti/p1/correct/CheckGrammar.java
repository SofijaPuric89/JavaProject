package com.etf.rti.p1.correct;

import com.etf.rti.p1.bnf.BNFCompiler;
import com.etf.rti.p1.exceptions.EParseError;
import com.etf.rti.p1.generator.AParser;
import com.etf.rti.p1.generator.Compiler;
import com.etf.rti.p1.generator.Generator;
import com.etf.rti.p1.util.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

/**
 * Created by Korisnik on 18.1.2016.
 */
public class CheckGrammar {

        private static String input;
        private static String answer;
        private boolean isCorrect;
        private AParser parser;
        private Generator generator;
        private Path tmpDir;
        private int correct = 0;
        private int incorrect = 0;
        private Compiler c;

        public CheckGrammar(String i) {
            input = i;
        }

        public void setAnswer(String a) {
            answer = a;
        }

    public void setUp() throws Exception {
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
            c = generator.generate();
        }

        public void tearDown() throws Exception {
            Util.deleteFolder(tmpDir.toFile());
        }

        public void testGetParser() throws Exception {
            c.reset();
            c.setInput(new ByteArrayInputStream(answer.getBytes("UTF-8")));
            c.getParser().init();
            if (c.getParser().getNumberOfSyntaxErrors() > 0) {
                throw new EParseError("Input stream syntax error");
            }


        }

        public boolean testGrammar() {
            try {
                testGetParser();
                System.out.println("Grammar is ok!");
                correct++;
                isCorrect = true;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                isCorrect = false;
                incorrect++;
            }
            return isCorrect;
        }

    public void print() {
        System.out.println("Tacni: " + correct + " Netacni: " + incorrect);
    }


}
