package com.etf.rti.p1.correct;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Korisnik on 8.1.2016.
 */
//TODO: Think about class rename, it is more like BNFNotationGrammarParser with testing purposes
public class ParseGrammar {
    //TODO: think about moving constants in Util class for ParseGrammar
    private static final String EQUAL = "::=";
    private static final String NEWLINE = "\n";
    private static final String SPACE = "\\s+";
    private static final String EMPTY = "";
    private static final String OR = "\\|";
    private static final String NONTERMINAL = "<(.+?)>";
    private static final String TERMINALS_NONTERMINALS = "(<(.+?)>)|([^<>]+)";  // bilo bez ? u drugom delu
    private static final String DEFAULT_GRAMMAR_INPUT =
            "<p> ::= <malo_slovo>:\\<put>\n" +
                    "<put> ::= <dir> | <put>\\<dir> | \"<dir>\"\\<put>\n" +
                    "<dir> ::= <malo_slovo> | <dir><pom>\n" +
                    "<pom> ::= <malo_slovo> | _ | <cifra>\n" +
                    "<malo_slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    private String grammar = "";

    private String[] rules = null; //TODO: check rules is actually an rules of grammar rules?
    private boolean[] compRule; //TODO: refactor this by renaming, check if necessary: used for tracking compiled rules, maybe Rule class?
    Graph grammarGraph = new Graph(this); //graph for storing parsed symbols of grammar

    //TODO: CombinationGenerator not being used in ParseGrammar, refactor and move to Graph?
    CombinationGenerator gen = new CombinationGenerator(this);

    /**
     * @param grammar input grammar for notation parser
     */
    public ParseGrammar(String grammar) {
        this.grammar = grammar;
    }

    public ParseGrammar() {
        grammar = DEFAULT_GRAMMAR_INPUT;
    }

    public void parse() {
        Node<Symbol> localRoot = null;
        rules = parseRules(grammar);
        compRule = new boolean[rules.length]; // TODO: what if rules is null?
        for (int i = 0; i < compRule.length; i++) compRule[i] = false;
        for (int i = 0; i < rules.length; i++) {  // parse every rule
            String[] terminals = rules[i].split(EQUAL); // terminals[0] - left side; terminals[1] - right side
            String nonterminal = "";
            Pattern nonterminalPattern = Pattern.compile(NONTERMINAL);
            Matcher matcher = nonterminalPattern.matcher(terminals[0]);
            localRoot = grammarGraph.parseLeftSideOfRule(localRoot, terminals[1], nonterminal, matcher);
            String[] parts = terminals[1].split(OR); // parts on right side of rule
            for (String part : parts) {
                Node<Symbol> root = grammarGraph.addCompositeNode(localRoot, part, i); //TODO: compositeNode instead of root?
                String matchPart = "";
                Pattern pattern = Pattern.compile(TERMINALS_NONTERMINALS);
                Matcher match = pattern.matcher(part);
                while (match.find()) {
                    matchPart = match.group(0);    // tu treba ispitati da li se simbol vec nalazi u stablu ako je neterminal...
                    Node<Symbol> node = null;
                    boolean matchPartIsNonterminal = isNonterminal(matchPart);
                    Symbol sym = null;
                    if (matchPartIsNonterminal) {
                        matcher = nonterminalPattern.matcher(matchPart);
                        if (matcher.find()) {
                            String nonterminalName = matcher.group(1);
                            node = grammarGraph.find(nonterminalName, grammarGraph.root);
                            sym = new Symbol(nonterminalName, true, 0, matchPartIsNonterminal, false);
                        }
                    } else
                        sym = new Symbol(matchPart, true, 0, matchPartIsNonterminal, false);
                    Node<Symbol> nod = new Node<>(sym);
                    grammarGraph.addNode(localRoot, root, node, nod);
                }
            }

        }
    }

    public void setCompRuleToTrue(int i) {
        compRule[i] = true;
    }

    private boolean isNonterminal(String part) { //TODO: can be part of some Util class related to ParseGrammar?
        return part.startsWith("<");
    }

    private String[] parseRules(String grammar) {
        List<String> rules = new ArrayList<>();
        String[] grammarLines = grammar.split(NEWLINE);
        for (String line : grammarLines) {
            rules.add(line.replaceAll(SPACE, EMPTY));
        }
        return (String[]) rules.toArray();
    }


    /**
     * Main method used for testing purposes of ParseGrammar glass by parsing input grammar setting generated graph for grammar,
     * then creating CheckGrammar and GenerateAnswer which in 1000 iterations print generated correct and corrupt answers for
     * input grammar.
     * TODO: move this to test classes first, then think about parametrization of ParseGrammar in order to use in GUI
     *
     * @param args
     */
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
                "<slovo> ::= a|b|c|d|e|f|grammarGraph|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                "<cifra> ::= 0|1|2|3|4|5|6|7|8|9");
        // ParseGrammar pg = new ParseGrammar();
        pg.parse();
        //TODO: check what these grammarGraph methods are used for
        pg.grammarGraph.setCompositeNodesToRecursive();
        pg.grammarGraph.setNodesToRecursive();
        pg.grammarGraph.setNodesToInfinite();
        pg.grammarGraph.setWidthToAllNodes();
        pg.grammarGraph.setDifferenceLenToRecursiveNodes();

        CheckGrammar checkGrammar = new CheckGrammar(pg.grammar);
        try {
            //TODO: check what setUp is actually doing and if it could be moved to ChechGrammar constructor
            checkGrammar.setUp();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        GenerateAnswer generateAnswer = new GenerateAnswer(pg.grammarGraph, false);
        int len = 10;
        generateAnswer.setDistribution(len); //TODO: move this logic to constructor
        for (int i = 0; i < 1000; i++) {

            generateAnswer.generateIncorrectAnswer(pg.grammarGraph.root, len, len);

            String generatedAnswer = generateAnswer.generateAnswerForNode(pg.grammarGraph.root, len);
            String corruptedAnswer = generateAnswer.corruptCorrectAnswer(generatedAnswer);

            System.out.print("***GENERISANI STRING*** ");
            //TODO: check what is the difference between if and else branch?
            if (corruptedAnswer.length() == 0) {
                checkGrammar.setAnswer(generatedAnswer);
                System.out.println(generatedAnswer + " duzina: " + generatedAnswer.length());
            } else {
                checkGrammar.setAnswer(corruptedAnswer);
                System.out.println(corruptedAnswer + " duzina: " + generatedAnswer.length());
            }

            checkGrammar.testGrammar();
        }

        checkGrammar.print(); //TODO: prints number of correct and corrupted answers, refactor this!

        try {
            //TODO: user only for delete temp folder, refactor that consumer of CheckGrammar class takes care about provided folder
            checkGrammar.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }
}

