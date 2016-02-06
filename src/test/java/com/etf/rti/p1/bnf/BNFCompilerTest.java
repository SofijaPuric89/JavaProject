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
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

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
        c.setInput(new ByteArrayInputStream("f8v_!tucrc449.j.com".getBytes("UTF-8")));
        c.getParser().init();

    }
}