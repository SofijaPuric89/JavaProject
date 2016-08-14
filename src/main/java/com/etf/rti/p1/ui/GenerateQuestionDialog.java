package com.etf.rti.p1.ui;

import javax.swing.*;
import java.awt.event.*;

public class GenerateQuestionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonGenerate;
    private JTextArea textArea1;
    private JComboBox comboBox1;
    private JSpinner answerLengthSpinner;
    private JTextField answerTextFieldA;
    private JButton generateCorrectAnswerA;
    private JButton generateIncorrectAnswerA;
    private JLabel answerLabelA;
    private JLabel answerIndicatorIconA;
    private JLabel answerLabelB;
    private JLabel answerIndicatorIconB;
    private JTextField answerTextFieldB;
    private JButton generateCorrectAnswerB;
    private JButton generateIncorrectAnswerB;
    private JTextField answerTextFieldC;
    private JButton generateCorrectAnswerC;
    private JButton generateIncorrectAnswerC;
    private JLabel answerLabelC;
    private JLabel answerIndicatorIconC;

    public GenerateQuestionDialog(int width, int height) {
        setTitle("SinGen - Generate Question");
        setContentPane(contentPane);
        setModal(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonGenerate);

        buttonGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGenerateQuestion();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onGenerateQuestion() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
