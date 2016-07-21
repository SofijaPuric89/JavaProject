package com.etf.rti.p1;

import com.etf.rti.p1.ui.MainForm;
import com.etf.rti.p1.ui.TestForm;

import javax.swing.*;
import java.awt.*;

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
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem = new JMenuItem("File");
        menuItem.add(new Label("Exit"));
        menuBar.add(menuItem);
        frame.setJMenuBar(menuBar);

        frame.add(new MainForm().getMainPanel());

        frame.setVisible(true);
    }
}
