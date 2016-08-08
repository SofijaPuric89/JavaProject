package com.etf.rti.p1;

import com.etf.rti.p1.app.SinGenContext;
import com.etf.rti.p1.ui.MainFrame;

import javax.swing.*;

/**
 * Main application class
 */
public class SinGen {

    public SinGenContext context = new SinGenContext();

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        MainFrame mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
}
