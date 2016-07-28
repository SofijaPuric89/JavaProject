package com.etf.rti.p1.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Vikica on 22.7.2016.
 */
public class MainForm {
    private JPanel mainPanel;
    private JToolBar grammarToolBar;
    private JButton importButton;
    private JButton exportButton;
    private JButton createQuestion;
    private JPanel notationsPanel;
    private JTabbedPane logPanel;
    private JPanel logTabPanel;
    private JTextArea logTextArea;
    private JTextArea BNFNotationTextArea;
    private JTextArea EBNFNotationTextArea;
    private JTextArea syntaxDiagramTextArea;

    public MainForm() {
       importButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {

           }
       });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextArea getBNFNotationTextArea() {
        return BNFNotationTextArea;
    }
}
