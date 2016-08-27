package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.util.GrammarSamples;

import javax.swing.*;
import java.awt.event.*;
import java.net.URL;

public class NewGrammarDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea grammarTextArea;
    private JComboBox grammarSamplesComboBox;
    private String dialogValue;

    public NewGrammarDialog(int width, int height) {
        setTitle("SinGen - New Grammar");
        setContentPane(contentPane);
        setSize(width, height);
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setFrameIcon();

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

        //set grammar sample text
        refreshGrammarTextArea(GrammarSamples.readGrammarSample(0));

        //set sample grammar combo box
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(GrammarSamples.getGrammarSampleFileNames().toArray());
        grammarSamplesComboBox.setModel(comboBoxModel);
        grammarSamplesComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String selectedFileName = e.getItem().toString();
                String grammarSample = GrammarSamples.readGrammarSample(selectedFileName);
                refreshGrammarTextArea(grammarSample);
            }
        });
    }

    private void refreshGrammarTextArea(String grammarSample) {
        grammarTextArea.setText(grammarSample);
        grammarTextArea.grabFocus();
        pack();
    }

    private void onOK() {
        dialogValue = grammarTextArea.getText();

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public String getDialogValue() {
        return dialogValue;
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }
}
