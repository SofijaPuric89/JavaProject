package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.util.GrammarSamples;

import javax.swing.*;
import java.awt.event.*;
import java.net.URL;

public class ImportExportDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea grammarTextArea;
    private String dialogValue;

    public ImportExportDialog(int width, int height) {
        setTitle("SinGen - Import/Export Grammar");
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
        String grammarSample = GrammarSamples.readGrammarSample(0);
        grammarTextArea.setText(grammarSample);
        grammarTextArea.selectAll();
        grammarTextArea.grabFocus();
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
