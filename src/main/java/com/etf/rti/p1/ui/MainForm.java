package com.etf.rti.p1.ui;

import javax.swing.*;

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
        logPanel.setName("Log");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextArea getBNFNotationTextArea() {
        return BNFNotationTextArea;
    }
}
