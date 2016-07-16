package com.etf.rti.p1.correct;

import java.util.Calendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sofija on 2/3/2016.
 */
public class CorruptBNF {
    private static final String EQUAL = "::=";
    private static final String TERMINALSNONTERMINALS = "(<(.+?)>)|([^<>]+)";
    private String[] rules;
    private boolean[] compRules;
    private Random randGenerator;
    private int numOfCompRules;
    private Graph graph;

    public CorruptBNF(String[] rules, int numOfCompRules, boolean[] compRules, Graph g) {
        this.rules = rules;
        this.numOfCompRules = numOfCompRules;
        this.compRules = compRules;
        randGenerator = new Random();
        graph = g;
        Calendar cal = Calendar.getInstance();
        randGenerator.setSeed(cal.getTimeInMillis());
    }

    private int randomNumber(int len) {
        return randGenerator.nextInt(len);
    }

    private int randRule() {
        int randNum = randomNumber(numOfCompRules);
        int j = -1;
        for (int i = 0; i<compRules.length; i++) {
            if (compRules[i])
                j++;
            if (j == randNum) {
                break;
            }
        }
        return j;
    }

    private void corruptRule() {
        boolean directRecursive = false;
        String rule = rules[randRule()];
        String[] sides = rule.split(EQUAL);
        Pattern pattern = Pattern.compile(TERMINALSNONTERMINALS);
        Matcher matcher = pattern.matcher(sides[1]);
        while (matcher.find()) {
            String part = matcher.group(0);
            if (part.equals(sides[0])) {
                directRecursive = true;
            }
        }
        boolean corrupt = false;
        while (!corrupt) {
            int num = randomNumber(5);
            switch (num) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
            }
        }

    }

    private boolean changeRecursiveToNonRecursive(boolean directRecursive, String[] sides) {
        boolean success = false;   // dovrsiti...
        if (directRecursive) {
            success = true;
        }
        return success;
    }
}
