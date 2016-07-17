package com.etf.rti.p1.ui;

import javax.swing.*;

/**
 * Created by Vikica on 17.7.2016.
 */
public class MainForm {
    private JButton button1;
    private JPanel panel1;
    private JLabel testLabel;

    public MainForm() {
        button1.addActionListener(e -> {
            testLabel.setText("Test message");
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
