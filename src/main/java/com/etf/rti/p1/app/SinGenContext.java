package com.etf.rti.p1.app;

import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.util.Utils;

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
    private static File syntaxDiagramFile;
    private static int grammarHash;
    private static String incompleteBNFGrammar;

    public static String getGrammarBNF() {
        return grammarBNF;
    }

    public static AParser setGrammarBNF(String grammarBNF) {
        if (SinGenContext.grammarBNF != null && SinGenContext.grammarHash == Utils.getHashFor(grammarBNF)) {
            return SinGenContext.parser;
        }

        SinGenContext.grammarBNF = grammarBNF;
        SinGenContext.grammarHash = Utils.getHashFor(grammarBNF);
        try {
            SinGenContext.parser = createParser(grammarBNF);
        } catch (Exception e) {
            SinGenLogger.error("Couldn't create BNF parser for grammar " + grammarBNF, e);
        }
        return SinGenContext.parser;
    }

    private static AParser createParser(String bnfGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler(out);
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

    public static void setFirstNonTerminalSymbol(String firstNonTerminalSymbol) {
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

    public static void setSyntaxDiagramFile(File syntaxDiagramFile) {
        SinGenContext.syntaxDiagramFile = syntaxDiagramFile;
    }

    public static File getSyntaxDiagramFile() {
        return syntaxDiagramFile;
    }

    public static int getGrammarHash() {
        return grammarHash;
    }

    public static void setIncompleteBNFGrammar(String incompleteBNFGrammar) {
        SinGenContext.incompleteBNFGrammar = incompleteBNFGrammar;
    }

    public static String getIncompleteBNFGrammar() {
        return incompleteBNFGrammar;
    }
}
