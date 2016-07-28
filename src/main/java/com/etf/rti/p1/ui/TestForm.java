package com.etf.rti.p1.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Vikica on 17.7.2016.
 */
public class TestForm {
    private JButton button1;
    private JPanel panel1;
    private JLabel testLabel;

    public TestForm() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
