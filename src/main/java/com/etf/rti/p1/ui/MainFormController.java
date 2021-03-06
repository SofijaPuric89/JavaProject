package com.etf.rti.p1.ui;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.questions.QuestionGenerator;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.BNFGrammarToSyntaxDiagramTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.app.SinGenLogger;
import com.etf.rti.p1.app.SinGenLoggerListener;
import com.etf.rti.p1.util.Utils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;

/**
 * Main controller class that observes and manages UI events. Uses translator layer to provide data for UI.
 * Invokes re-rendering of UI if needed with providing necessary data.
 */
public class MainFormController implements MainFormListener, SinGenLoggerListener {

    private MainFormObservable myObservable;
    private BNFGrammarToEBNFRuleTranslator toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
    private BNFGrammarToSyntaxDiagramTranslator toSyntaxDiagramTranslator = new BNFGrammarToSyntaxDiagramTranslator();
    private QuestionGenerator questionGenerator;

    public MainFormController(MainFormObservable myObservable) {
        this.myObservable = myObservable;

        myObservable.addUIListener(this);

        SinGenLogger.addListener(this);
    }

    @Override
    public void grammarOpened(String bnfGrammar) {
        SinGenLogger.info("Imported grammar");
        SinGenLogger.content(bnfGrammar);
        try {
            myObservable.refreshBNFPanel(bnfGrammar);

            AParser bnfParser = SinGenContext.setGrammarBNF(bnfGrammar);

            String firstNonterminalSymbol = bnfParser.getFirstNonterminalSymbol();
            myObservable.refreshFirstNonTerminalLabel(firstNonterminalSymbol);
            SinGenContext.setFirstNonTerminalSymbol(firstNonterminalSymbol);

            String ebnfGrammar = translateToEBNF(bnfParser);
            SinGenLogger.info("Translated to EBNF");
            SinGenLogger.content(ebnfGrammar);
            SinGenContext.setGrammarEBNF(ebnfGrammar);
            myObservable.refreshEBNFPanel(ebnfGrammar);

            File syntaxDiagramTranslatorFile = translateToSyntaxDiagram(bnfGrammar);
            SinGenLogger.info("Created syntax diagrams");
            SinGenContext.setSyntaxDiagramFile(syntaxDiagramTranslatorFile);
            myObservable.refreshSyntaxDiagramPanel(syntaxDiagramTranslatorFile);

            myObservable.enableAllComponents();
        } catch (Exception e) {
            SinGenLogger.error("Error importing and processing grammar", e);
        }
    }

    @Override
    public void exportFileSelected(File selectedFile, String bnfGrammar, String ebnfGrammar,
                                   File syntaxDiagramGrammarFile) {
        try {
            String encodedSyntaxDiagram = Utils.encodeImage(syntaxDiagramGrammarFile);
            String templateFile = IOUtils.toString(MainFormController.class.getClassLoader().getResourceAsStream("templates/grammar_export.html"));
            String rendered = templateFile
                    .replace("{$file_name$}", FilenameUtils.getBaseName(selectedFile.getName()))
                    .replace("{$bnf_grammar$}", escapeHtml(bnfGrammar))
                    .replace("{$ebnf_grammar$}", escapeHtml(ebnfGrammar))
                    .replace("{$syntax_diagrams$}", encodedSyntaxDiagram);

            Path exportFilePath = selectedFile.toPath();
            if (!selectedFile.getName().endsWith(".html")) {
                exportFilePath = Paths.get(selectedFile.getAbsolutePath() + ".html");
            }

            Files.write(exportFilePath, rendered.getBytes());
            SinGenLogger.info("Grammar exported to location: " + selectedFile.getAbsolutePath());
        } catch (IOException e) {
            SinGenLogger.error("Error while exporting to file ", e);
        }
    }

    @Override
    public void checkIfAnswerCorrect(String answer, Consumer<Boolean> callback) {
        if ((questionGenerator == null) || (questionGenerator.getGrammarHash() != SinGenContext.getGrammarHash())) {
            questionGenerator = new QuestionGenerator(SinGenContext.getGrammarBNF());
        }
        callback.accept(questionGenerator.isAnswerGrammaticallyCorrect(answer));
    }

    @Override
    public void openFileSelected(File selectedFile) {
        try {
            String bnfGrammar = IOUtils.toString(new FileInputStream(selectedFile));
            grammarOpened(bnfGrammar);

            SinGenContext.setLoadGrammarRootDir(selectedFile.getParentFile());
        } catch (IOException e) {
            SinGenLogger.error("Error opening file " + selectedFile.getName(), e);
        }
    }

    @Override
    public void saveAsFileSelected(File selectedFile, String bnfGrammar) {
        try {
            Path savedAsFilePath = selectedFile.toPath();
            if (!selectedFile.getName().endsWith(".bnf")) {
                savedAsFilePath = Paths.get(selectedFile.getAbsolutePath() + ".bnf");
            }

            Files.write(savedAsFilePath, bnfGrammar.getBytes());
        } catch (IOException e) {
            SinGenLogger.error("Error saving file " + selectedFile.getName(), e);
        }
    }

    private String escapeHtml(String text) {
        return StringEscapeUtils.escapeHtml4(text).replaceAll("\\r\\n", "<br/>");
    }

    private File translateToSyntaxDiagram(String bnfGrammar) throws Exception {
        return toSyntaxDiagramTranslator.transformToSyntaxDiagram(bnfGrammar);
    }

    private String translateToEBNF(AParser bnfParser) throws Exception {
        toEBNFRuleTranslator = new BNFGrammarToEBNFRuleTranslator();
        List<IRule> rules = toEBNFRuleTranslator.transformToEBNF(bnfParser.getRules());
        return Utils.listOfRulesToEBNFString(rules);
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
