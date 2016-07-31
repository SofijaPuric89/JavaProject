package com.etf.rti.p1.ui;

import com.etf.rti.p1.SinGen;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Main frame class that contains provided form with main panel
 */
public class MainFrame extends JFrame {

    public MainFrame(MainForm mainForm) throws HeadlessException {
        super("SinGen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(mainForm.getMainPanel());

        URL resource = SinGen.class.getResource("/images/etf_favicon.png");
        ImageIcon imageIcon = new ImageIcon(resource);
        setIconImage(imageIcon.getImage());
    }
}
