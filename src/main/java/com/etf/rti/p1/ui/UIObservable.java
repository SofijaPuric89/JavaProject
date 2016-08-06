package com.etf.rti.p1.ui;

import java.io.File;

/**
 * Interface that enables listeners to register on UIObservable and then do proper
 * UI managing using the interface methods.
 */
public interface UIObservable<T> {

    /**
     * Register listener to the UIObservable elemnt
     * @param listener register listener for UI event changes
     */
    void addUIListener(UIListener<T> listener);

    /**
     * Refreshing BNF panel with new bnf grammar
     * @param bnfGrammar new grammar in bnf
     */
    void refreshBNFPanel(String bnfGrammar);

    /**
     * Refreshing BNF panel with new bnf grammar
     * @param ebnfGrammar new grammar in ebnf
     */
    void refreshEBNFPanel(String ebnfGrammar);

    /**
     * Refreshing BNF panel with new bnf grammar
     * @param syntaxDiagramGrammarFile new syntax diagram file for displaying
     */
    void refreshSyntaxDiagramPanel(File syntaxDiagramGrammarFile);

    void appendInfoLog(String log);

    void appendContentLog(String log);

    void appendErrorLog(String log);
}
