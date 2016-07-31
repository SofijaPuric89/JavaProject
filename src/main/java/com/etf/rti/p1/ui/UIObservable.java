package com.etf.rti.p1.ui;

/**
 * Created by Vikica on 31.7.2016.
 */
public interface UIObservable<T> {

    void addUIListener(UIListener<T> listener);

    void refreshBNFPanel(String bnfGrammar);

    void refreshEBNFPanel(String ebnfGrammar);

    void refreshSyntaxDiagramPanel(String syntaxDiagramGrammar);
}
