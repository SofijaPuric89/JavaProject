package com.etf.rti.p1.ui;

/**
 * Created by Vikica on 31.7.2016.
 */
public class UIController implements UIListener {

    private UIObservable myObservable;

    public UIController(UIObservable myObservable) {
        this.myObservable = myObservable;

        myObservable.addUIListener(this);
    }

    @Override
    public void importGrammarButtonClicked(String importedGrammar) {
        System.out.println("IMPORTED: " + importedGrammar);

        myObservable.refreshBNFGrammar(importedGrammar);
    }
}
