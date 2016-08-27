package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;
import com.etf.rti.p1.util.GrammarSamples;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.net.URL;

public class NewGrammarDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea grammarTextArea;
    private JList grammarSamplesList;
    private JScrollPane grammarScrollPane;
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

        //set sample grammar combo box
        grammarSamplesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        grammarSamplesList.setLayoutOrientation(JList.VERTICAL);
        ListModel listModel = new DefaultComboBoxModel(GrammarSamples.getGrammarSampleFileNames().toArray());
        grammarSamplesList.setModel(listModel);
        grammarSamplesList.setSelectedIndex(-1);
        grammarSamplesList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedGrammarSampleIndex = e.getFirstIndex();
                String grammarSample = GrammarSamples.readGrammarSample(selectedGrammarSampleIndex);
                refreshGrammarTextArea(grammarSample);
            }
        });
    }

    private void refreshGrammarTextArea(String grammarSample) {
        grammarTextArea.setText(grammarSample);
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

    private void createUIComponents() {
        grammarTextArea = new RSyntaxTextArea();
        // TODO: Create BNF syntax style
        ((RSyntaxTextArea)grammarTextArea).setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_HTML);
        ((RSyntaxTextArea)grammarTextArea).setCodeFoldingEnabled(true);
        grammarScrollPane = new RTextScrollPane(grammarTextArea);
    }
}
