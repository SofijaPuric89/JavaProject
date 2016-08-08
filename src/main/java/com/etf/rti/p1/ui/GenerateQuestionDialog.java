package com.etf.rti.p1.ui;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.questions.AnswerGenerator;
import com.etf.rti.p1.translator.BNFGrammarToGraphTranslator;
import com.etf.rti.p1.translator.graph.Graph;
import com.etf.rti.p1.util.GrammarSamples;

import javax.swing.*;
import java.awt.event.*;

public class GenerateQuestionDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;

    public GenerateQuestionDialog(int width, int height) {
        setContentPane(contentPane);
        setModal(true);
        setSize(width, height);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(buttonOK);

        generateAnswers();

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
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

    //TODO: remove or refactor this method, only for testing purposes!
    private void generateAnswers() {
        BNFGrammarToGraphTranslator BNFGrammarToGraphTranslator = new BNFGrammarToGraphTranslator(SinGenContext.getGrammarBNF());
        Graph grammarGraph = BNFGrammarToGraphTranslator.parse();
        //TODO: check what these grammarGraph methods are used for
        grammarGraph.setCompositeNodesToRecursive();
        grammarGraph.setNodesToRecursive();
        grammarGraph.setNodesToInfinite();
        grammarGraph.setWidthToAllNodes();
        grammarGraph.setDifferenceLenToRecursiveNodes();
        int answerLength = 10;
        AnswerGenerator answerGenerator = new AnswerGenerator(BNFGrammarToGraphTranslator.getGrammarGraph(), answerLength, false);
        String generatedAnswer = answerGenerator.generateAnswer(BNFGrammarToGraphTranslator.getGrammarGraph().getRoot(), answerLength);
        String corruptedAnswer = answerGenerator.corruptCorrectAnswer(generatedAnswer);

        textArea1.append(SinGenContext.getGrammarBNF() + "\n\n");
        textArea1.append("generated answer: " + generatedAnswer + "\n\n");
        textArea1.append("corrupted answer: " + corruptedAnswer + "\n\n");
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
