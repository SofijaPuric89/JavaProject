package com.etf.rti.p1.app;

import java.io.File;

/**
 * Context class that keeps information about current state of application
 */
public final class SinGenContext {

    private static String grammarBNF;
    private static String grammarEBNF;
    private static String firstNonTerminalSymbol;
    private static File loadGrammarRootDir;

    public static String getGrammarBNF() {
        return grammarBNF;
    }

    public static void setGrammarBNF(String grammarBNF) {
        SinGenContext.grammarBNF = grammarBNF;
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
}
