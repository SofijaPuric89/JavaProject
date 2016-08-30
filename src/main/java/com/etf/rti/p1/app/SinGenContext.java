package com.etf.rti.p1.app;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.util.SinGenLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Context class that keeps information about current state of application
 */
public final class SinGenContext {

    private static String grammarBNF;
    private static String grammarEBNF;
    private static String firstNonTerminalSymbol;
    private static File loadGrammarRootDir;
    private static AParser parser;

    public static String getGrammarBNF() {
        return grammarBNF;
    }

    public static AParser setGrammarBNF(String grammarBNF) {
        if (SinGenContext.grammarBNF != null && SinGenContext.grammarBNF.equals(grammarBNF)) {
            return SinGenContext.parser;
        }

        SinGenContext.grammarBNF = grammarBNF;
        try {
            SinGenContext.parser = createParser(grammarBNF);
        } catch (Exception e) {
            SinGenLogger.error("Couldn't create BNF parser for grammar " + grammarBNF, e);
        }
        return SinGenContext.parser;
    }

    private static AParser createParser(String bnfGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(bnfGrammar.getBytes("UTF-8")));
        compiler.getParser().init();
        return compiler.getParser();
    }

    public static String getGrammarEBNF() {
        return grammarEBNF;
    }

    public static void setGrammarEBNF(String grammarEBNF) {
        SinGenContext.grammarEBNF = grammarEBNF;
    }

    public static String getFirstNonTerminalSymbol() {
        return firstNonTerminalSymbol;
    }

    public static void setFirstNonterminalSymbol(String firstNonTerminalSymbol) {
        SinGenContext.firstNonTerminalSymbol = firstNonTerminalSymbol;
    }

    public static void setLoadGrammarRootDir(File loadGrammarRootDir) {
        SinGenContext.loadGrammarRootDir = loadGrammarRootDir;
    }

    public static File getLoadGrammarRootDir() {
        return loadGrammarRootDir;
    }

    public static AParser getParser() {
        return parser;
    }
}
