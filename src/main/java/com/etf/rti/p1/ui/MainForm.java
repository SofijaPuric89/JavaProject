package com.etf.rti.p1.ui;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.app.SinGenLogger;
import org.fife.ui.rsyntaxtextarea.AbstractTokenMakerFactory;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
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
import java.util.function.Consumer;

/**
 * Main form that is responsible for all UI elements. Class implements MainFormObservable that makes the
 * listeners can register on UI events and then do the proper controller action. *
 */
public class MainForm implements MainFormObservable {
    private static final Color INFO_TEXT_COLOR = new Color(0, 148, 255);
    private static final Color ERROR_TEXT_COLOR = new Color(255, 73, 53);

    private final MainFrame parent;
    private JPanel mainPanel;
    private JToolBar grammarToolBar;
    private JButton newButton;
    private JButton generateQuestionButton;
    private JPanel notationsPanel;
    private JTabbedPane logPanel;
    private JPanel logTabPanel;
    private JTextPane logTextPane;
    private JTextArea bnfNotationTextArea;
    private JTextArea ebnfNotationTextArea;
    private JLabel syntaxDiagramImageLabel;
    private JButton exportButton;
    private JTextField checkSequenceTextField;
    private JLabel checkSequenceIndicatorIcon;
    private JLabel firstNonterminalLabel;
    private JButton openButton;
    private JButton saveAsButton;
    private JScrollPane bnfNotationScrollPane;
    private JScrollPane ebnfNotationScrollPane;

    //listens for input events on the UI
    private Set<MainFormListener> listeners = new HashSet<>();

    // TODO: move to context
    private File syntaxDiagramGrammarFile;


    private final ImageIcon correctIcon;
    private final ImageIcon incorrectIcon;

    public MainForm(MainFrame parent) {
        this.parent = parent;

        correctIcon = new ImageIcon(MainForm.class.getResource("/images/correct_32_32.png"));
        incorrectIcon = new ImageIcon(MainForm.class.getResource("/images/incorrect_32_32.png"));

        //define button actions
        setButtonListeners(parent);

        checkSequenceTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                onSequenceChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                onSequenceChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                onSequenceChange();
            }
        });
    }

    private void onSequenceChange() {
        for (MainFormListener listener : listeners) {
            listener.checkIfAnswerCorrect(checkSequenceTextField.getText(), new Consumer<Boolean>() {
                @Override
                public void accept(Boolean isCorrect) {
                    checkSequenceIndicatorIcon.setIcon(isCorrect ? correctIcon : incorrectIcon);
                }
            });
        }
    }

    private void setButtonListeners(final MainFrame parent) {
        newButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newGrammar = parent.showNewGrammarDialog();
                if (newGrammar == null || newGrammar.isEmpty()) {
                    return;
                }

                for (MainFormListener listener : listeners) {
                    listener.grammarOpened(newGrammar);
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
                if (saveDialogValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();

                    for (MainFormListener listener : listeners) {
                        listener.exportFileSelected(selectedFile, bnfNotationTextArea.getText(), ebnfNotationTextArea.getText(), syntaxDiagramGrammarFile);
                    }
                }
            }
        });
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(SinGenContext.getLoadGrammarRootDir());
                jFileChooser.setAcceptAllFileFilterUsed(false);
                jFileChooser.setFileFilter(new FileNameExtensionFilter("BNF file (*.bnf)", "bnf"));
                int saveDialogValue = jFileChooser.showOpenDialog(mainPanel);
                if (saveDialogValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();

                    for (MainFormListener listener : listeners) {
                        listener.openFileSelected(selectedFile);
                    }
                }
            }
        });
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser(SinGenContext.getLoadGrammarRootDir());
                jFileChooser.setAcceptAllFileFilterUsed(false);
                jFileChooser.setFileFilter(new FileNameExtensionFilter("BNF file (*.bnf)", "bnf"));
                int saveDialogValue = jFileChooser.showSaveDialog(mainPanel);
                if (saveDialogValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = jFileChooser.getSelectedFile();

                    for (MainFormListener listener : listeners) {
                        listener.saveAsFileSelected(selectedFile, bnfNotationTextArea.getText());
                    }
                }
            }
        });
        generateQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String generatedQuestion = parent.showGenerateQuestionDialog();
                if (generatedQuestion != null) {
                    SinGenLogger.info("Generated question with answers");
                    SinGenLogger.content(generatedQuestion);
                }
            }
        });
    }

    @Override
    public void addUIListener(MainFormListener listener) {
        listeners.add(listener);
    }

    @Override
    public void refreshBNFPanel(String bnfGrammar) {
        bnfNotationTextArea.setText(bnfGrammar);
    }

    @Override
    public void refreshEBNFPanel(String ebnfGrammar) {
        ebnfNotationTextArea.setText(ebnfGrammar);
    }

    @Override
    public void refreshSyntaxDiagramPanel(File syntaxDiagramGrammarFile) {
        this.syntaxDiagramGrammarFile = syntaxDiagramGrammarFile;
        try {
            BufferedImage syntaxDiagramImage = ImageIO.read(syntaxDiagramGrammarFile);
            syntaxDiagramImageLabel.setIcon(new ImageIcon(syntaxDiagramImage));
        } catch (IOException e) {
            SinGenLogger.error("Error while refreshing syntax diagram panel. ", e);
            e.printStackTrace();
        }
    }

    @Override
    public void appendInfoLog(String log) {
        appendToLogTextArea(log, INFO_TEXT_COLOR);
    }

    @Override
    public void appendContentLog(String log) {
        appendToLogTextArea(log, Color.BLACK);
    }

    @Override
    public void appendErrorLog(String log) {
        appendToLogTextArea(log, ERROR_TEXT_COLOR);
    }

    @Override
    public void enableAllComponents() {
        exportButton.setEnabled(true);
        saveAsButton.setEnabled(true);
        generateQuestionButton.setEnabled(true);
        bnfNotationTextArea.setEnabled(true);
        ebnfNotationTextArea.setEnabled(true);
        syntaxDiagramImageLabel.setEnabled(true);
        checkSequenceTextField.setEnabled(true);
        if (!checkSequenceTextField.getText().isEmpty()) {
            onSequenceChange();
        }
    }

    @Override
    public void refreshFirstNonTerminalLabel(String symbol) {
        firstNonterminalLabel.setText(" <" + symbol + "> ");
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

    private void createUIComponents() {
        AbstractTokenMakerFactory tokenMakerFactory = (AbstractTokenMakerFactory) TokenMakerFactory.getDefaultInstance();
        tokenMakerFactory.putMapping("text/bnf", "com.etf.rti.p1.ui.highlight.BNFTokenMaker");
        tokenMakerFactory.putMapping("text/ebnf", "com.etf.rti.p1.ui.highlight.EBNFTokenMaker");

        bnfNotationTextArea = new RSyntaxTextArea();
        enableSyntaxHighlighting(bnfNotationTextArea, "text/bnf");
        bnfNotationScrollPane = new RTextScrollPane(bnfNotationTextArea);

        ebnfNotationTextArea = new RSyntaxTextArea();
        enableSyntaxHighlighting(ebnfNotationTextArea, "text/ebnf");
        ebnfNotationScrollPane = new RTextScrollPane(ebnfNotationTextArea);
    }

    private void enableSyntaxHighlighting(JTextArea textArea, String type) {
        ((RSyntaxTextArea) textArea).setSyntaxEditingStyle(type);
        textArea.setLineWrap(true);
    }
}
