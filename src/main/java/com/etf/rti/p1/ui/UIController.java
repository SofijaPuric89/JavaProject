package com.etf.rti.p1.ui;

import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.BNFGrammarToSyntaxDiagramTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.util.SinGenLogger;
import com.etf.rti.p1.util.SinGenLoggerListener;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

/**
 * Main controller class that observes and manages UI events. Uses translator layer to provide data for UI.
 * Invokes re-rendering of UI if needed with providing necessary data.
 */
public class UIController implements UIListener, SinGenLoggerListener {

    private UIObservable myObservable;
    private BNFGrammarToEBNFRuleTranslator toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
    private BNFGrammarToSyntaxDiagramTranslator toSyntaxDiagramTranslator = new BNFGrammarToSyntaxDiagramTranslator();

    public UIController(UIObservable myObservable) {
        this.myObservable = myObservable;

        myObservable.addUIListener(this);

        SinGenLogger.addListener(this);
    }

    @Override
    public void grammarImported(String bnfGrammar) {
        SinGenLogger.info("Imported grammar");
        SinGenLogger.content(bnfGrammar);
        try {
            myObservable.refreshBNFPanel(bnfGrammar);

            String ebnfGrammar = startEBNFTranslator(bnfGrammar);
            SinGenLogger.info("Translated to EBNF");
            SinGenLogger.content(ebnfGrammar);
            myObservable.refreshEBNFPanel(ebnfGrammar);

            File syntaxDiagramTranslatorFile = startSyntaxDiagramTranslator(bnfGrammar);
            SinGenLogger.info("Created syntax diagrams");
            myObservable.refreshSyntaxDiagramPanel(syntaxDiagramTranslatorFile);
        } catch (Exception e) {
            SinGenLogger.error("Error importing and processing grammar", e);
        }
    }

    @Override
    public void exportFileSelected(File selectedFile, String bnfGrammar, String ebnfGrammar,
                                   File syntaxDiagramGrammarFile) {
        try {
            String encodedSyntaxDiagram = Base64.getEncoder().encodeToString(Files.readAllBytes(syntaxDiagramGrammarFile.toPath()));
            String templateFile = IOUtils.toString(UIController.class.getClassLoader().getResourceAsStream("templates/grammar_export.html"));
            String rendered = templateFile.replace("{$file_name$}", FilenameUtils.getBaseName(selectedFile.getName()))
                    .replace("{$bnf_grammar$}", StringEscapeUtils.escapeHtml4(bnfGrammar).replaceAll("\\r\\n", "<br/>"))
                    .replace("{$ebnf_grammar$}", StringEscapeUtils.escapeHtml4(ebnfGrammar).replaceAll("\\r\\n", "<br/>"))
                    .replace("{$syntax_diagrams$}", encodedSyntaxDiagram);

            Files.write(selectedFile.toPath(), rendered.getBytes());
        } catch (IOException e) {
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
            ebnfGrammar = ebnfGrammar.concat(rule.toString() + "\r\n").replace(" ", "");
        }
        return ebnfGrammar;
    }

    @Override
    public void onInfo(String log) {
        myObservable.appendInfoLog(log);
    }

    @Override
    public void onContent(String log) {
        myObservable.appendContentLog(log);
    }

    @Override
    public void onError(String log) {
        myObservable.appendErrorLog(log);
    }
}
