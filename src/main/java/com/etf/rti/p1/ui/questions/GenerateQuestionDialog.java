package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.ui.UIObservable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GenerateQuestionDialog extends JDialog implements UIObservable<GenerateQuestionDialogListener> {
    private JPanel contentPane;
    private JButton generateQuestionBtn;
    private JTextArea textArea1;
    private JComboBox<QuestionTypeComboBoxModelElement> questionTypeComboBox;
    private JSpinner answerLengthSpinner;
    private JTextField answerTextFieldA;
    private JButton generateCorrectAnswerABtn;
    private JButton generateIncorrectAnswerABtn;
    private JLabel answerLabelA;
    private JLabel answerIndicatorIconA;
    private JLabel answerLabelB;
    private JLabel answerIndicatorIconB;
    private JTextField answerTextFieldB;
    private JButton generateCorrectAnswerBBtn;
    private JButton generateIncorrectAnswerBBtn;
    private JTextField answerTextFieldC;
    private JButton generateCorrectAnswerCBtn;
    private JButton generateIncorrectAnswerCBtn;
    private JLabel answerLabelC;
    private JLabel answerIndicatorIconC;

    private final ImageIcon correctIcon;
    private final ImageIcon incorrectIcon;

    private final Set<GenerateQuestionDialogListener> listeners;
    private String dialogValue;

    public GenerateQuestionDialog(int width, int height) {
        setTitle("SinGen - Generate Question");
        setContentPane(contentPane);
        setModal(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(generateQuestionBtn);

        correctIcon = new ImageIcon(GenerateQuestionDialog.class.getResource("/images/correct_32_32.png"));
        incorrectIcon = new ImageIcon(GenerateQuestionDialog.class.getResource("/images/incorrect_32_32.png"));

        listeners = new HashSet<>();

        addButtonListeners();
        setupClosingActions();
        setupQuestionComboBox();
        setupAnswerLengthSpinner();
        setupAnswerTextFieldListeners();
    }

    private void setupAnswerTextFieldListeners() {
        addAnswerChangeListener(answerTextFieldA, answerIndicatorIconA);
        addAnswerChangeListener(answerTextFieldB, answerIndicatorIconB);
        addAnswerChangeListener(answerTextFieldC, answerIndicatorIconC);
    }

    private void addAnswerChangeListener(JTextField answerTextField, JLabel indicatorIcon) {
        answerTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onAnswerChange(answerTextField, indicatorIcon);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onAnswerChange(answerTextField, indicatorIcon);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onAnswerChange(answerTextField, indicatorIcon);
            }
        });
    }

    private void onAnswerChange(JTextField answerTextField, JLabel indicatorIcon) {
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.checkIfAnswerCorrect(answerTextField.getText(), new Consumer<Boolean>() {
                @Override
                public void accept(Boolean isCorrect) {
                    indicatorIcon.setIcon(isCorrect ? correctIcon : incorrectIcon);
                }
            });
        }
    }

    private void setupAnswerLengthSpinner() {
        answerLengthSpinner.setModel(new SpinnerNumberModel(10, 5, 30, 1));
    }

    private void addButtonListeners() {
        generateQuestionBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onGenerateQuestion();
            }
        });
        generateCorrectAnswerABtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCorrectAnswer(answerTextFieldA);
            }
        });
        generateCorrectAnswerBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCorrectAnswer(answerTextFieldB);
            }
        });
        generateCorrectAnswerCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCorrectAnswer(answerTextFieldC);
            }
        });
        generateIncorrectAnswerABtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(answerTextFieldA);
            }
        });
        generateIncorrectAnswerBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(answerTextFieldB);
            }
        });
        generateIncorrectAnswerCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(answerTextFieldC);
            }
        });
    }

    private void generateCorrectAnswer(final JTextField answerTextField) {
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.generateCorrectAnswer((Integer) answerLengthSpinner.getValue(), new Consumer<String>() {
                @Override
                public void accept(String answer) {
                    answerTextField.setText(answer);
                }
            });
        }
    }


    private void generateIncorrectAnswer(JTextField answerTextField) {
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.generateIncorrectAnswer((Integer) answerLengthSpinner.getValue(), new Consumer<String>() {
                @Override
                public void accept(String answer) {
                    answerTextField.setText(answer);
                }
            });
        }
    }

    private void setupClosingActions() {
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

    private void setupQuestionComboBox() {
        QuestionTypeComboBoxModel questionTypeComboBoxModel = new QuestionTypeComboBoxModel();
        questionTypeComboBoxModel.add(QuestionGivenType.GRAMMAR_IN_BNF,
                QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL,
                "Given grammar in BNF ask for correct sequence for first non-terminal");
        questionTypeComboBoxModel.add(QuestionGivenType.GRAMMAR_IN_EBNF,
                QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL,
                "Given grammar in EBNF ask for correct sequence for first non-terminal");
        questionTypeComboBox.setModel(questionTypeComboBoxModel);
        questionTypeComboBox.setSelectedIndex(0);
    }

    private void onGenerateQuestion() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    @Override
    public void addUIListener(GenerateQuestionDialogListener listener) {
        listeners.add(listener);
    }

    public String getDialogValue() {
        return dialogValue;
    }
}