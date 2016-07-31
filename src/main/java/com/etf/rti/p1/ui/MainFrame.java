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
    UIListener mainFormListener;

    public MainFrame() throws HeadlessException {
        super("SinGen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setFrameIcon();

        mainForm = new MainForm(this);
        mainFormListener = new UIController(mainForm);
        add(mainForm.getMainPanel());
    }

    private void setFrameIcon() {
        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }

    //TODO: move this part of code to Form
    public String showImportDialog() {
        ImportExportDialog importExportDialog = new ImportExportDialog(600, 400);
        importExportDialog.setVisible(true);
        return importExportDialog.getDialogValue();
    }
}
