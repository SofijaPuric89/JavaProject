package com.etf.rti.p1.ui.highlight;

import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.*;

public class BNFRenameNonterminalDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nonterminalNameTextField;

    private String dialogValue;

    BNFRenameNonterminalDialog(String originalName) {
        setTitle("SinGen - Rename Nonterminal");
        setContentPane(contentPane);
        setSize(400, 140);
        setLocationRelativeTo(null);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        dialogValue = stripBrackets(originalName);
        nonterminalNameTextField.setText(dialogValue);
        nonterminalNameTextField.selectAll();
    }

    private String stripBrackets(String originalName) {
        if (StringUtils.isNotBlank(originalName) && originalName.length() > 1) {
            return originalName.substring(1, originalName.length() - 1);
        }
        return "";
    }

    String getDialogValue() {
        return "<" + dialogValue + ">";
    }

    private void onOK() {
        dialogValue = nonterminalNameTextField.getText();
        dispose();
    }

    private void onCancel() {
        dispose();
    }
}
