package com.etf.rti.p1.ui;

public interface UIObservable<T> {

    /**
     * Register listener to the element
     * @param listener register listener for UI event changes
     */
    void addUIListener(T listener);
}
