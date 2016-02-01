package com.etf.rti.p1.bnf;

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

/**
 * Created by zika on 10.12.2015..
 */
public class BNFCompilerTest {
    private static final String input =
            "<start> ::= 11<a>|<b>1\n" +
                    "<a> ::= 1|<a><b>|<a><c><b>\n" +
                    "<b> ::= 101|<b>01\n" +
                    "<c> ::= 1100|<c>11|<c>00\n";

    private AParser parser;

    private Generator generator;
    private Path tmpDir;

    @Before
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
    }

    @After
    public void tearDown() throws Exception {
        Util.deleteFolder(tmpDir.toFile());
    }

    @Test
    public void testGetParser() throws Exception {
        parser.init();
        Compiler c = generator.generate();
        //c.setInput(new ByteArrayInputStream("f:\\\"d_a4f\"\\\"abc8ab\"\\pe5a_r\\pa8f1k".getBytes("UTF-8")));
        c.setInput(new ByteArrayInputStream("111101".getBytes("UTF-8")));
        c.getParser().init();

    }
}