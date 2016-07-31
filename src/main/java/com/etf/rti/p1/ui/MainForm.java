package com.etf.rti.p1.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vikica on 22.7.2016.
 */
public class MainForm implements UIObservable {
    private final MainFrame parent;
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

    //listens for input events on the UI
    private Set<UIListener> listeners = new HashSet<>();

    public MainForm(MainFrame parent) {
        this.parent = parent;

        //define button actions
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String importedGrammar = parent.showImportDialog();

                for(UIListener listener: listeners){
                    listener.importGrammarButtonClicked(importedGrammar);
                }
            }
        });
    }

    @Override
    public void addUIListener(UIListener listener) {
        listeners.add(listener);
    }

    @Override
    public void refreshBNFGrammar(String importedGrammar) {
        BNFNotationTextArea.setText(importedGrammar);
    }

    /**
     * Used for reaching main panel that will be exposed to the MainFrame
     * @return
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
