package com.etf.rti.p1;

import com.etf.rti.p1.ui.MainForm;

import javax.swing.*;

/**
 * Main application class
 */
public class SinGen {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("SinGen");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        frame.add(new MainForm().getPanel1());

        frame.setVisible(true);
    }
}
