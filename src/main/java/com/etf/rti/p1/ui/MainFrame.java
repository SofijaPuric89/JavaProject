package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialog;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogController;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogListener;
import org.fife.ui.rsyntaxtextarea.CodeTemplateManager;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.templates.CodeTemplate;
import org.fife.ui.rsyntaxtextarea.templates.StaticCodeTemplate;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Main frame class that contains provided form with main panel
 */
public class MainFrame extends JFrame {

    private final MainForm mainForm;

    MainFormListener mainFormListener;
    GenerateQuestionDialogListener generateQuestionDialogListener;

    public MainFrame() throws HeadlessException {
        super("SinGen");
        enableCodeTemplates();

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFrameIcon();

        mainForm = new MainForm(this);
        mainFormListener = new MainFormController(mainForm);

        add(mainForm.getMainPanel());

    }

    private void enableCodeTemplates() {
        RSyntaxTextArea.setTemplatesEnabled(true);

        CodeTemplateManager codeTemplateManager = RSyntaxTextArea.getCodeTemplateManager();

        CodeTemplate ruleCodeTemplate = new StaticCodeTemplate("rule", "<", "> ::= ");
        codeTemplateManager.addTemplate(ruleCodeTemplate);

        CodeTemplate nonterminalCodeTemplate = new StaticCodeTemplate("nt", "<", ">");
        codeTemplateManager.addTemplate(nonterminalCodeTemplate);

        CodeTemplate orCodeTemplate = new StaticCodeTemplate("or", "|", null);
        codeTemplateManager.addTemplate(orCodeTemplate);
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }

    //TODO: move this part of code to Form
    public String showNewGrammarDialog() {
        // TODO: Based on actual screen size
        NewGrammarDialog newGrammarDialog = new NewGrammarDialog(1000, 600);
        newGrammarDialog.setVisible(true);
        return newGrammarDialog.getDialogValue();
    }

    //TODO: move this part of code to Form
    public String showGenerateQuestionDialog() {
        GenerateQuestionDialog questionDialog = new GenerateQuestionDialog(1000, 600);
        generateQuestionDialogListener = new GenerateQuestionDialogController(questionDialog, SinGenContext.getGrammarBNF());
        questionDialog.setVisible(true);
        return questionDialog.getDialogValue();
    }
}
