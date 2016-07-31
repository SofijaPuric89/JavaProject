package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Main frame class that contains provided form with main panel
 */
public class MainFrame extends JFrame {

    private final MainForm mainForm;
    UIListener mainFormListener = new UIController();

    public MainFrame() throws HeadlessException {
        super("SinGen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFrameIcon();

        mainForm = new MainForm(this);
        mainForm.addUIListener(mainFormListener);
        add(mainForm.getMainPanel());
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }

    public JDialog showImportDialog() {
        JDialog jDialog = new JDialog(this, "SinGen - Import Grammar", true);
        Panel panel = new Panel();
        JTextArea jTextArea = new JTextArea("Type BNF grammar here");
        panel.add(jTextArea);
        jDialog.add(panel);
        jDialog.setSize(600, 400);
        jDialog.setVisible(true);
        return jDialog;
    }
}
