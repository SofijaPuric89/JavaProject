package com.etf.rti.p1.compiler.bnf;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.Compiler;
import com.etf.rti.p1.compiler.CompilerGenerator;
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

    private CompilerGenerator compilerGenerator;

    @Before
    public void setup() throws Exception {
        Path tmpDir = Files.createTempDirectory("test");
        tmpDir.toFile().deleteOnExit();
        final String name = "test";
        Path grammar = tmpDir.resolve(name + ".g4");
        String grammarName = grammar.toAbsolutePath().toString();
        compilerGenerator = new CompilerGenerator(grammarName);
        FileOutputStream out = new FileOutputStream(grammarName);

        BNFCompiler compiler = new BNFCompiler(name, out);
        compiler.setInput(new ByteArrayInputStream(input.getBytes("UTF-8")));
        parser = compiler.getParser();
    }

    @Test
    public void testGetParser() throws Exception {
        parser.init();
        Compiler c = compilerGenerator.generate();
        //c.setInput(new ByteArrayInputStream("f:\\\"d_a4f\"\\\"abc8ab\"\\pe5a_r\\pa8f1k".getBytes("UTF-8")));
        c.setInput(new ByteArrayInputStream("cx52!xwv6.com.icyo82".getBytes("UTF-8")));
        c.getParser().init();

    }
}