package com.etf.rti.p1.util;

import com.etf.rti.p1.translator.ebnf.rules.IRule;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;

/**
 * TODO: think about creating SinGen runner class that will be in charge for running the processes.
 */
public class Utils {

    private Utils() {
    }

    public static String getResourcePath() {
        return Utils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static int runProcess(String command, String[] vars) throws Exception {
        Process pro = Runtime.getRuntime().exec(command, vars);
        readOutput(pro.getErrorStream());
        readOutput(pro.getInputStream());
        pro.waitFor();
        return pro.exitValue();
    }

    private static void readOutput(InputStream ins) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(ins));
        while ((in.readLine()) != null) ;
    }

    public static String listOfRulesToEBNFString(List<IRule> rules) {
        String grammar = "";
        for (IRule rule : rules) {
            grammar = grammar.concat(rule + "\r\n").replace(" ", "");
        }
        return grammar;
    }

    public static String listOfRulesToBNFString(List<IRule> rules) {
        String grammar = "";
        for (IRule rule : rules) {
            grammar = grammar.concat(rule.toBNFString() + "\r\n").replace(" ", "");
        }
        return grammar;
    }

    public static String encodeImage(File imageFile) throws IOException {
        return Base64.getEncoder().encodeToString(Files.readAllBytes(imageFile.toPath()));
    }
}
