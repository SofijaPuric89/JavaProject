package com.etf.rti.p1;

import com.etf.rti.p1.ui.MainForm;
import com.etf.rti.p1.ui.MainFrame;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.util.List;

/**
 * Main application class
 */
public class SinGen {

    private static final String input =
            "<p> ::= <korisnik>!<domen>\n" +
                    "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                    "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                    "<kraj_domena> ::= com | co.rs\n" +
                    "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                    "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

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
