package com.etf.rti.p1.ui;

/**
 * Listener interface that will be registered on UIObservable object
 * and then react properly on its UI events.
 */
public interface UIListener<T> {
    void grammarImported(String importedGrammar);
}
