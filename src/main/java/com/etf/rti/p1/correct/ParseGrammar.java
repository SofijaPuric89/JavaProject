package com.etf.rti.p1.correct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Korisnik on 8.1.2016.
 */
public class ParseGrammar {
    private static final String EQUAL = "::=";
    private static final String NEWLINE = "\n";
    private static final String SPACE = "\\s+";
    private static final String EMPTY = "";
    private static final String OR = "\\|";
    private static final String NONTERMINAL = "<(.+?)>";
    private static final String TERMINALSNONTERMINALS = "(<(.+?)>)|([^<>]+)";  // bilo bez ? u drugom delu
    private String input =
            "<p> ::= <malo_slovo>:\\<put>\n" +
                    "<put> ::= <dir> | <put>\\<dir> | \"<dir>\"\\<put>\n" +
                    "<dir> ::= <malo_slovo> | <dir><pom>\n" +
                    "<pom> ::= <malo_slovo> | _ | <cifra>\n" +
                    "<malo_slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    private String[] array = null;
    private boolean[] compRule;
    Graph g = new Graph(this);
    CombinationGenerator gen = new CombinationGenerator(this);

    public ParseGrammar(String grammar) {
        input = grammar;
    }

    public ParseGrammar() {}

    public String getGrammar() {
        return input;
    }

    public void parse() {
        Node<Symbol> localRoot = null;
        parseAllRules();
        compRule = new boolean[array.length];
        for (int i=0;i<compRule.length;i++) compRule[i] = false;
        for (int i = 0; i < array.length; i++) {  // parse every rule
            String[] terminals = array[i].split(EQUAL); // terminals[0] - left side; terminals[1] - right side
            String nonterminal = "";
            Pattern pattern = Pattern.compile(NONTERMINAL);
            Matcher matcher = pattern.matcher(terminals[0]);
            localRoot = g.parseLeftSideOfRule(localRoot, terminals[1], nonterminal, matcher);
            String[] parts = terminals[1].split(OR); // parts on right side of rule
            for (int j = 0; j < parts.length; j++) {
                //System.out.println(parts[j]);
                Node<Symbol> rroot = g.addCompositeNode(localRoot, parts[j], i);
                String part = "";
                Pattern patt = Pattern.compile(TERMINALSNONTERMINALS);
                Matcher match = patt.matcher(parts[j]);
                //System.out.println(match.groupCount());
                while (match.find()) {
                    part = match.group(0);    // tu treba ispitati da li se simbol vec nalazi u stablu ako je neterminal...
                    //System.out.println(part);
                    Node<Symbol> node = null;
                    boolean b = isNonterminal(part);
                    Symbol sym = null;
                    if (b) {
                        matcher = pattern.matcher(part);
                        if (matcher.find()) {
                            //System.out.println(matcher.group(1));
                            String nonterminalName = matcher.group(1);
                            node = g.find(nonterminalName, g.root);
                            sym = new Symbol(nonterminalName, true, 0, b, false);
                        }
                    } else
                        sym = new Symbol(part, true, 0, b, false);
                    Node<Symbol> nod = new Node<Symbol>(sym);
                    g.addNode(localRoot, rroot, node, nod);
                }
            }

        }
    }

    public void setCompRuleToTrue(int i) {
        compRule[i] = true;
    }

    public int numOfCompRule() {
        int sum = 0;
        for (boolean b : compRule) {
            if (b) {
                sum++;
            }
        }
        return sum;
    }

    private boolean isNonterminal(String part) {
        return part.startsWith("<") ? true : false;
    }

    private void parseAllRules() {
        array = input.split(NEWLINE);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replaceAll(SPACE, EMPTY);
        }
    }


    public static void main(String[] args) {
       /* ParseGrammar pg = new ParseGrammar("<p> ::= <p><pom> | <pom>\n" +
                "<pom> ::= _ | <cifra>\n" +
                "<cifra> ::= 0|1|2|3|4|5|6|7|8|9");*/
      /*  ParseGrammar pg = new ParseGrammar("<start> ::= 11<a>|<b>1\n" +
                "<a> ::= 1|<a><b>|<a><c><b>\n" +
                "<b> ::= 101|<b>01\n" +
                "<c> ::= 1100|<c>11|<c>00\n"); */
       ParseGrammar pg = new ParseGrammar("<p> ::= <korisnik>!<domen>\n" +
                "<korisnik> ::= <rec> | <korisnik>_<rec>\n" +
                "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                "<kraj_domena> ::= com | co.rs\n" +
                "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                "<cifra> ::= 0|1|2|3|4|5|6|7|8|9");
       // ParseGrammar pg = new ParseGrammar();
        pg.parse();
        pg.g.setCompositeNodesToRecursive();
        pg.g.setNodesToRecursive();
        pg.g.setNodesToInfinite();
        pg.g.setWidthToAllNodes();
        pg.g.setDifferenceLenToRecursiveNodes();

        CheckGrammar cg = new CheckGrammar(pg.input);
        try {
            cg.setUp();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GenerateAnswer ga = new GenerateAnswer(pg.g, false);
        int len = 10;
        ga.setDistribution(len);
        for (int i = 0; i < 1000; i++) {

            ga.generateIncorrectAnswer(pg.g.root, len, len);
            boolean correct = false;
            String str = "";
           // while (!correct) {
            str = ga.generateAnswerForNode(pg.g.root, len);
            String corrupt = ga.corruptCorrectAnswer(str);

            //  System.out.print("***GENERISANI STRING*** ");
            //  System.out.println(str + " duzina: " + str.length());
            if (corrupt.length() == 0)
                cg.setAnswer(str);
            else
                cg.setAnswer(corrupt);
            cg.testGrammar();
           //  }
           System.out.print("***GENERISANI STRING*** ");
            if (corrupt.length() == 0)
                System.out.println(str + " duzina: " + str.length());
            else
                System.out.println(corrupt + " duzina: " + str.length());
           //    }
          //  String corrupt = ga.corruptCorrectAnswer(str);
          //  System.out.println(corrupt + " duzina: " + corrupt.length());
        }
        //  System.out.println("Num of comp rules: " + pg.numOfCompRule());
        cg.print();

        try {
            cg.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}

