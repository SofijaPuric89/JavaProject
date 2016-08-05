package com.etf.rti.p1.ui;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.BNFGrammarToSyntaxDiagramTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

/**
 * Main controller class that observes and manages UI events. Uses translator layer to provide data for UI.
 * Invokes re-rendering of UI if needed with providing necessary data.
 */
public class UIController implements UIListener {

    private UIObservable myObservable;
    private BNFGrammarToEBNFRuleTranslator toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
    private BNFGrammarToSyntaxDiagramTranslator toSyntaxDiagramTranslator = new BNFGrammarToSyntaxDiagramTranslator();

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

            File syntaxDiagramTranslatorFile = startSyntaxDiagramTranslator(bnfGrammar);
            myObservable.refreshSyntaxDiagramPanel(syntaxDiagramTranslatorFile);
        } catch (Exception e) {
            //TODO: add message to Log panel!
            e.printStackTrace();
        }
    }

    private File startSyntaxDiagramTranslator(String bnfGrammar) throws Exception {
        return toSyntaxDiagramTranslator.transformToSyntaxDiagram(bnfGrammar);
    }

    private String startEBNFTranslator(String bnfGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(bnfGrammar.getBytes("UTF-8")));
        compiler.getParser().init();
        toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
        List<IRule> rules = toEBNFRuleTranslator.transformToEBNF(compiler.getParser().getRules());
        String ebnfGrammar = "";
        for(IRule rule: rules){
            ebnfGrammar = ebnfGrammar.concat(rule.toString() + "\n").replace(" ", "");
        }
        return ebnfGrammar;
    }
}
