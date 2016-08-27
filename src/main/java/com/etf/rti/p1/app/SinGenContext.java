package com.etf.rti.p1.app;

/**
 * Context class that keeps information about current state of application
 */
public final class SinGenContext {

    private static String grammarBNF;
    private static String grammarEBNF;
    private static String firstNonTerminalSymbol;

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
}
