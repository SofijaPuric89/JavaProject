package com.etf.rti.p1.generator;

import com.etf.rti.p1.util.Util;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by zika on 15.12.2015..
 */
public class GraphTest {
    private static final String input =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";
            /*"<start> ::= 11<a>|<b>1\n" +
                    "<a> ::= 1|<a><b>|<a><c><b>\n" +
                    "<b> ::= 101|<b>01\n" +
                    "<c> ::= 1100|<c>11|<c>00\n";*/
    @Test
    public void testGenerate() throws Exception {
        String resourcePath = Util.getInstance().getResourcePath().substring(3);
        Graph graph = new Graph();

        graph.generate(input);
    }
}