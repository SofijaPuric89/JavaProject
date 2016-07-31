package com.etf.rti.p1.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
    private JLabel syntaxDiagramImageLabel;
    private JPanel syntaxDiagramImagePanel;

    //listens for input events on the UI
    private Set<UIListener> listeners = new HashSet<>();

    public MainForm(MainFrame parent) {
        this.parent = parent;

        //define button actions
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String importedGrammar = parent.showImportDialog();
                if(importedGrammar == null || importedGrammar.isEmpty()){
                    return;
                }

                for(UIListener listener: listeners){
                    listener.grammarImported(importedGrammar);
                }
            }
        });
    }

    @Override
    public void addUIListener(UIListener listener) {
        listeners.add(listener);
    }

    @Override
    public void refreshBNFPanel(String bnfGrammar) {
        BNFNotationTextArea.setText(bnfGrammar);
    }

    @Override
    public void refreshEBNFPanel(String ebnfGrammar) {
        EBNFNotationTextArea.setText(ebnfGrammar);
    }

    @Override
    public void refreshSyntaxDiagramPanel(File syntaxDiagramGrammarFile) {
        try {
            BufferedImage syntaxDiagramImage = ImageIO.read(syntaxDiagramGrammarFile);
            syntaxDiagramImageLabel.setIcon(new ImageIcon(syntaxDiagramImage));
        } catch (IOException e) {
            //TODO: add to log panel, set default display image?!
            e.printStackTrace();
        }
    }

    /**
     * Used for reaching main panel that will be exposed to the MainFrame
     * @return
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
