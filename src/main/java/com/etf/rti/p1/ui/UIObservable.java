package com.etf.rti.p1.ui;

/**
 * Created by Vikica on 31.7.2016.
 */
public interface UIObservable<T> {

    void addUIListener(UIListener<T> listener);

    void refreshBNFGrammar(String importedGrammar);
}
