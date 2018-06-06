package com.etf.rti.p1.ui.questions;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.translator.BNFGrammarToEBNFRuleTranslator;
import com.etf.rti.p1.translator.BNFGrammarToNonEquivalentTranslator;
import com.etf.rti.p1.ui.UIObservable;
import com.etf.rti.p1.util.Utils;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.*;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GenerateQuestionDialog extends JDialog implements UIObservable<GenerateQuestionDialogListener> {
    private JPanel contentPane;
    private JButton generateQuestionBtn;
    private JTextPane generatedQuestionTextArea;
    private JComboBox<QuestionModelElement> questionTypeComboBox;
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
    private JPanel panel1;
    private JPanel panel2;
    private JComboBox answerComboBoxA;
    private JComboBox answerComboBoxB;
    private JComboBox answerComboBoxC;
    private JButton generateSequenceBtn;
    private JTextField sequenceTextField;
    private JButton generateCorrectGrammarABtn;
    private JButton generateCorrectGrammarBBtn;
    private JButton generateCorrectGrammarCBtn;
    private JButton generateIncorrectGrammarABtn;
    private JButton generateIncorrectGrammarBBtn;
    private JButton generateIncorrectGrammarCBtn;
    private JLabel grammarIndicatorIconA;
    private JLabel grammarIndicatorIconB;
    private JLabel grammarIndicatorIconC;

    private final ImageIcon correctIcon;
    private final ImageIcon incorrectIcon;

    private final Set<GenerateQuestionDialogListener> listeners;
    private String dialogValue;
    private String answerA;
    private String answerB;
    private String answerC;

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

        setFrameIcon();
        addButtonListeners();
        setupClosingActions();
        setupQuestionComboBox();
        setupAnswerLengthSpinner();
        setupAnswerTextFieldListeners();
        generatedQuestionTextArea.setContentType("text/html");
    }

    @Override
    public void addUIListener(GenerateQuestionDialogListener listener) {
        listeners.add(listener);
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
        if (answerTextField.getText().isEmpty()) {
            indicatorIcon.setIcon(null);
            return;
        }
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.checkIfAnswerCorrect((QuestionModelElement) questionTypeComboBox.getSelectedItem(), answerTextField.getText(), new Consumer<Boolean>() {
                @Override
                public void accept(Boolean isCorrect) {
                    indicatorIcon.setIcon(isCorrect ? correctIcon : incorrectIcon);
                }
            });
        }
    }

    private void setupAnswerLengthSpinner() {
        SpinnerNumberModel model = new SpinnerNumberModel(10, 5, 30, 1);
        answerLengthSpinner.setModel(model);
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
                generateCorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldA.setText(answer);
                        answerA = answer;
                    }
                });
            }
        });
        generateCorrectAnswerBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldB.setText(answer);
                        answerB = answer;
                    }
                });
            }
        });
        generateCorrectAnswerCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateCorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldC.setText(answer);
                        answerC = answer;
                    }
                });
            }
        });
        generateIncorrectAnswerABtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldA.setText(answer);
                        answerA = answer;
                    }
                });
            }
        });
        generateIncorrectAnswerBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldB.setText(answer);
                        answerB = answer;
                    }
                });
            }
        });
        generateIncorrectAnswerCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateIncorrectAnswer(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        answerTextFieldC.setText(answer);
                        answerC = answer;
                    }
                });
            }
        });
        generateSequenceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanAnswerFields();
                generateCorrectSequence(new Consumer<String>() {
                    @Override
                    public void accept(String answer) {
                        sequenceTextField.setText(answer);
                    }
                });
            }
        });
        generateCorrectGrammarABtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerA = generateCorrectGrammar(grammarIndicatorIconA, answerComboBoxA);
            }
        });
        generateCorrectGrammarBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerB = generateCorrectGrammar(grammarIndicatorIconB, answerComboBoxB);
            }
        });
        generateCorrectGrammarCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerC = generateCorrectGrammar(grammarIndicatorIconC, answerComboBoxC);
            }
        });
        generateIncorrectGrammarABtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerA = generateIncorrectGrammar(grammarIndicatorIconA, answerComboBoxA);
            }
        });
        generateIncorrectGrammarBBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerB = generateIncorrectGrammar(grammarIndicatorIconB, answerComboBoxB);
            }
        });
        generateIncorrectGrammarCBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkForUnpopulatedSequence();
                answerC = generateIncorrectGrammar(grammarIndicatorIconC, answerComboBoxC);
            }
        });
    }

    private void generateCorrectSequence(Consumer<String> callback) {
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.generateCorrectSequence((QuestionModelElement) questionTypeComboBox.getSelectedItem(), (Integer) answerLengthSpinner.getValue(), callback);
        }
    }

    private void generateCorrectAnswer(Consumer<String> callback) {
        checkForUnpopulatedSequence();
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.generateCorrectAnswer((QuestionModelElement) questionTypeComboBox.getSelectedItem(), (Integer) answerLengthSpinner.getValue(), callback);
        }
    }

    private void generateIncorrectAnswer(Consumer<String> callback) {
        checkForUnpopulatedSequence();
        for (GenerateQuestionDialogListener listener : listeners) {
            listener.generateIncorrectAnswer((QuestionModelElement) questionTypeComboBox.getSelectedItem(), (Integer) answerLengthSpinner.getValue(), callback);
        }
    }

    private void checkForUnpopulatedSequence() {
        if ((questionTypeComboBox.getSelectedIndex() > 2) && (sequenceTextField.getText().isEmpty())) {
            generateSequenceBtn.doClick();
        }
    }

    private String generateCorrectGrammar(JLabel icon, JComboBox answerComboBox) {
        icon.setIcon(correctIcon);

        //TODO: refactor this!
        switch (answerComboBox.getSelectedIndex()) {
            case 0: // given grammar in BNF
                return "BNF\r\n" + SinGenContext.getGrammarBNF();
            case 1: // given grammar in EBNF
                return "EBNF\r\n" + SinGenContext.getGrammarEBNF();
            default:
                return "\n";
        }
    }

    private String generateIncorrectGrammar(JLabel icon, JComboBox answerComboBox) {
        icon.setIcon(incorrectIcon);

        try {
            BNFGrammarToNonEquivalentTranslator translator =
                    new BNFGrammarToNonEquivalentTranslator(SinGenContext.getParser(), sequenceTextField.getText());
            String corruptBNFGrammar = translator.translateToNonEquivalentBNF();

            //TODO: refactor this!
            switch (answerComboBox.getSelectedIndex()) {
                case 0: // given grammar in BNF
                    return "BNF\r\n" + corruptBNFGrammar;
                case 1: // given grammar in EBNF
                    BNFGrammarToEBNFRuleTranslator toEBNFTranslator = new BNFGrammarToEBNFRuleTranslator();
                    return "EBNF\r\n"
                            + Utils.listOfRulesToEBNFString(toEBNFTranslator.transformToEBNF(corruptBNFGrammar));
                default:
                    return "\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setupClosingActions() {
        // call onClose() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onClose();
            }
        });

        // call onClose() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onClose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void setupQuestionComboBox() {
        QuestionTypeComboBoxModel questionTypeComboBoxModel = new QuestionTypeComboBoxModel();
        questionTypeComboBoxModel.add(QuestionGrammarGivenType.GRAMMAR_IN_BNF,
                QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL,
                "Given grammar in BNF, ask for correct sequence for first non-terminal");
        questionTypeComboBoxModel.add(QuestionGrammarGivenType.GRAMMAR_IN_EBNF,
                QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL,
                "Given grammar in EBNF, ask for correct sequence for first non-terminal");
        questionTypeComboBoxModel.add(QuestionGrammarGivenType.GRAMMAR_AS_SYNTAX_DIAGRAM,
                QuestionAskedForType.CORRECT_SEQUENCE_FOR_FIRST_NONTERMINAL,
                "Given grammar as sequence diagram, ask for correct sequence for first non-terminal");
        questionTypeComboBoxModel.add(QuestionGrammarGivenType.CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL,
                QuestionAskedForType.CORRECT_GRAMMAR_FOR_FIRST_NONTERMINAL_SEQUENCE,
                "Given first non-terminal sequence, ask for correct grammar");
        questionTypeComboBoxModel.add(QuestionGrammarGivenType.CORRECT_SEQUENCE_FOR_FIRST_NON_TERMINAL,
                QuestionAskedForType.CORRECT_RULE_WHICH_SHOULD_BE_ADDED,
                "Given first non-terminal sequence and grammar in BNF, ask for missing rule so that sequence is correct");
        questionTypeComboBox.setModel(questionTypeComboBoxModel);
        questionTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanAnswerFields();
                sequenceTextField.setText("");
                //TODO: refactor this!
                switch(questionTypeComboBox.getSelectedIndex()) {
                    case 3:
                        panel1.setVisible(false);
                        panel2.setVisible(true);
                        sequenceTextField.setVisible(true);
                        generateSequenceBtn.setVisible(true);
                        break;
                    case 4:
                        panel1.setVisible(true);
                        panel2.setVisible(false);
                        sequenceTextField.setVisible(true);
                        generateSequenceBtn.setVisible(true);
                        break;
                    default:
                        panel1.setVisible(true);
                        panel2.setVisible(false);
                        sequenceTextField.setVisible(false);
                        generateSequenceBtn.setVisible(false);
                        break;
                }
            }
        });
        questionTypeComboBox.setSelectedIndex(0);
    }

    private void cleanAnswerFields() {
        answerA = "";
        answerB = "";
        answerC = "";
        answerTextFieldA.setText("");
        answerTextFieldB.setText("");
        answerTextFieldC.setText("");
        grammarIndicatorIconA.setIcon(null);
        grammarIndicatorIconB.setIcon(null);
        grammarIndicatorIconC.setIcon(null);
        answerComboBoxA.setSelectedIndex(0);
        answerComboBoxB.setSelectedIndex(0);
        answerComboBoxC.setSelectedIndex(0);
    }

    private void onGenerateQuestion() {
        QuestionModelElement element = (QuestionModelElement) questionTypeComboBox.getSelectedItem();

        for (GenerateQuestionDialogListener listener : listeners) {
            listener.buildQuestion(element, sequenceTextField.getText(), answerA, answerB, answerC, new Consumer<String>() {
                @Override
                public void accept(String generatedQuestionString) {
                    setDialogValue(generatedQuestionString);
                    generatedQuestionTextArea.setText(surroundWithHtmlBase(generatedQuestionString));
                }
            });
        }
    }

    private String surroundWithHtmlBase(String basicHtml) {
        return "<html>" +
                "<head>" +
                "    <style>" +
                "        body {" +
                "            font-family: \"Verdana, Geneva, sans-serif\";" +
                "            font-size: 14px;" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                basicHtml +
                "</body>" +
                "</html>";
    }

    private void onClose() {
        dispose();
    }

    public String getDialogValue() {
        return dialogValue;
    }

    public void setDialogValue(String dialogValue) {
        this.dialogValue = StringEscapeUtils.unescapeHtml4(dialogValue
                .replaceAll("\\<img[^>]*>", "// Sintaksni dijagram je izostavljen //")
                .replaceAll("<br/>", "\r\n")
                .replaceAll("\\<[^>]*>",""));
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }
}
