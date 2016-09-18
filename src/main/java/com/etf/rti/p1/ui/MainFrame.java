package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialog;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogController;
import com.etf.rti.p1.ui.questions.GenerateQuestionDialogListener;

import javax.swing.*;
import java.net.URL;

/**
 * Main frame class that contains provided form with main panel
 */
public class MainFrame extends JFrame {

    private static final double GENERATE_QUESTION_DIALOG_SCALE = 0.9;

    private final MainForm mainForm;
    MainFormListener mainFormListener;
    GenerateQuestionDialogListener generateQuestionDialogListener;

    public MainFrame() {
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
    public String showNewGrammarDialog() {
        // TODO: Based on actual screen size
        NewGrammarDialog newGrammarDialog = new NewGrammarDialog(1000, 600);
        newGrammarDialog.setVisible(true);
        return newGrammarDialog.getDialogValue();
    }

    //TODO: move this part of code to Form
    public String showGenerateQuestionDialog() {
        int width = (int) (mainForm.getMainPanel().getWidth() * GENERATE_QUESTION_DIALOG_SCALE);
        int height = (int) (mainForm.getMainPanel().getHeight() * GENERATE_QUESTION_DIALOG_SCALE);
        GenerateQuestionDialog questionDialog = new GenerateQuestionDialog(width, height);
        generateQuestionDialogListener = new GenerateQuestionDialogController(questionDialog, SinGenContext.getGrammarBNF());
        questionDialog.setVisible(true);
        return questionDialog.getDialogValue();
    }
}
