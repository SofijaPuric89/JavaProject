package com.etf.rti.p1.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Main form that is responsible for all UI elements. Class implements UIObservable that makes the
 * listeners can register on UI events and then do the proper controller action. *
 */
public class MainForm implements UIObservable {
    private final MainFrame parent;
    private JPanel mainPanel;
    private JToolBar grammarToolBar;
    private JButton importButton;
    private JButton generateQuestion;
    private JPanel notationsPanel;
    private JTabbedPane logPanel;
    private JPanel logTabPanel;
    private JTextPane logTextPane;
    private JTextArea BNFNotationTextArea;
    private JTextArea EBNFNotationTextArea;
    private JLabel syntaxDiagramImageLabel;
    private JButton exportButton;

    //listens for input events on the UI
    private Set<UIListener> listeners = new HashSet<>();

    // TODO: move to context
    private File syntaxDiagramGrammarFile;

    public MainForm(MainFrame parent) {
        this.parent = parent;

        //define button actions
        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String importedGrammar = parent.showImportDialog();
                if (importedGrammar == null || importedGrammar.isEmpty()) {
                    return;
                }

                for (UIListener listener : listeners) {
                    listener.grammarImported(importedGrammar);
                }
            }
        });
        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setAcceptAllFileFilterUsed(false);
                jFileChooser.setFileFilter(new FileNameExtensionFilter("HTML file (*.html)", "html"));
                int saveDialogValue = jFileChooser.showSaveDialog(mainPanel);
                if(saveDialogValue == JFileChooser.APPROVE_OPTION){
                    File selectedFile = jFileChooser.getSelectedFile();

                    for (UIListener listener : listeners) {
                        listener.exportFileSelected(selectedFile, BNFNotationTextArea.getText(), EBNFNotationTextArea.getText(), syntaxDiagramGrammarFile);
                    }
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
        this.syntaxDiagramGrammarFile = syntaxDiagramGrammarFile;
        try {
            BufferedImage syntaxDiagramImage = ImageIO.read(syntaxDiagramGrammarFile);
            syntaxDiagramImageLabel.setIcon(new ImageIcon(syntaxDiagramImage));
        } catch (IOException e) {
            //TODO: add to log panel, set default display image?!
            e.printStackTrace();
        }
    }

    @Override
    public void appendInfoLog(String log) {
        appendToLogTextArea(log, Color.BLUE);
    }

    @Override
    public void appendContentLog(String log) {
        appendToLogTextArea(log, Color.BLACK);
    }

    @Override
    public void appendErrorLog(String log) {
        appendToLogTextArea(log, Color.RED);
    }

    private void appendToLogTextArea(final String log, Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                StyledDocument doc = logTextPane.getStyledDocument();
                SimpleAttributeSet coloredLog = new SimpleAttributeSet();
                StyleConstants.setForeground(coloredLog, color);
                try {
                    doc.insertString(doc.getLength(), log + "\n\n", coloredLog);
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                logTextPane.setCaretPosition(logTextPane.getDocument().getLength());
            }
        });
    }

    /**
     * Used for reaching main panel that will be exposed to the MainFrame
     *
     * @return
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
