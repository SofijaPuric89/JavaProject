package com.etf.rti.p1.ui;

import java.io.File;

/**
 * Listener interface that will be registered on UIObservable object
 * and then react properly on its UI events.
 */
public interface UIListener<T> {

    void grammarImported(String importedGrammar);

    void exportFileSelected(File selectedFile, String bnfGrammar, String ebnfGrammar, File syntaxDiagramGrammarFile);
}
