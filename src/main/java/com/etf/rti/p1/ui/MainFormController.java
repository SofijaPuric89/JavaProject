package com.etf.rti.p1.ui;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.compiler.AParser;
import com.etf.rti.p1.compiler.bnf.BNFCompiler;
import com.etf.rti.p1.questions.QuestionGenerator;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.BNFGrammarToSyntaxDiagramTranslator;
import com.etf.rti.p1.translator.ebnf.rules.IRule;
import com.etf.rti.p1.util.SinGenLogger;
import com.etf.rti.p1.util.SinGenLoggerListener;
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
            SinGenContext.setGrammarBNF(bnfGrammar);

            AParser bnfParser = createParser(bnfGrammar);

            String firstNonterminalSymbol = bnfParser.getFirstNonterminalSymbol();
            myObservable.refreshFirstNonTerminalLabel(firstNonterminalSymbol);
            SinGenContext.setFirstNonterminalSymbol(firstNonterminalSymbol);

            String ebnfGrammar = translateToEBNF(bnfParser);
            SinGenLogger.info("Translated to EBNF");
            SinGenLogger.content(ebnfGrammar);
            SinGenContext.setGrammarEBNF(ebnfGrammar);
            myObservable.refreshEBNFPanel(ebnfGrammar);

            File syntaxDiagramTranslatorFile = translateToSyntaxDiagram(bnfGrammar);
            SinGenLogger.info("Created syntax diagrams");
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
            String encodedSyntaxDiagram = Base64.getEncoder().encodeToString(Files.readAllBytes(syntaxDiagramGrammarFile.toPath()));
            String templateFile = IOUtils.toString(MainFormController.class.getClassLoader().getResourceAsStream("templates/grammar_export.html"));
            String rendered = templateFile.replace("{$file_name$}", FilenameUtils.getBaseName(selectedFile.getName()))
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
        if (questionGenerator == null) {
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
        String ebnfGrammar = "";
        for (IRule rule : rules) {
            ebnfGrammar = ebnfGrammar.concat(rule.toString() + "\r\n").replace(" ", "");
        }
        return ebnfGrammar;
    }

    private AParser createParser(String bnfGrammar) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BNFCompiler compiler = new BNFCompiler("test", "com.etf.rti.p1.compiler.bnf", out);
        compiler.setInput(new ByteArrayInputStream(bnfGrammar.getBytes("UTF-8")));
        compiler.getParser().init();
        return compiler.getParser();
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
