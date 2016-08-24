package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialog;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogController;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogListener;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFrameIcon();

        mainForm = new MainForm(this);
        mainFormListener = new MainFormController(mainForm);

        add(mainForm.getMainPanel());
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }

    //TODO: move this part of code to Form
    public String showImportDialog() {
        ImportDialog importDialog = new ImportDialog(600, 400);
        importDialog.setVisible(true);
        return importDialog.getDialogValue();
    }

    //TODO: move this part of code to Form
    public String showGenerateQuestionDialog() {
        GenerateQuestionDialog questionDialog = new GenerateQuestionDialog(1000, 600);
        generateQuestionDialogListener = new GenerateQuestionDialogController(questionDialog, SinGenContext.getGrammarBNF());
        questionDialog.setVisible(true);
        return questionDialog.getDialogValue();
    }
}
