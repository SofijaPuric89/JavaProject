package com.etf.rti.p1.ui;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Vikica on 31.7.2016.
 */
public class UIController implements UIListener {

    private UIObservable myObservable;
    private BNFGrammarToEBNFRuleTranslator toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();

    public UIController(UIObservable myObservable) {
        this.myObservable = myObservable;

        myObservable.addUIListener(this);

    }

    @Override
    public void grammarImported(String bnfGrammar) {
        try {
            myObservable.refreshBNFPanel(bnfGrammar);

            String ebnfGrammar = startEBNFTranslator(bnfGrammar);
            myObservable.refreshEBNFPanel(ebnfGrammar);

            myObservable.refreshSyntaxDiagramPanel(bnfGrammar);
        } catch (Exception e) {
            //TODO: add message to Log panel!
            e.printStackTrace();
        }
    }

    private String startEBNFTranslator(String importedGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(importedGrammar.getBytes("UTF-8")));
        compiler.getParser().init();
        toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
        List<IRule> rules = toEBNFRuleTranslator.transformToEBNF(compiler.getParser().getRules());
        String ebnfGrammar = "";
        for(IRule rule: rules){
            ebnfGrammar = ebnfGrammar.concat(rule.toString() + "\n");
        }
        return ebnfGrammar;
    }
}
