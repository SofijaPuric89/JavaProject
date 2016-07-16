package com.etf.rti.p1.correct;

import com.etf.rti.p1.correct.graph.Graph;
import com.etf.rti.p1.correct.graph.Node;
import com.etf.rti.p1.correct.graph.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parsing grammar provided as input string in BNF notation. Input string contains \n as an indicator of new line
 * TODO: Think about class rename, it is more like BNFNotationGrammarParser with testing purposes
 */
public class BNFGrammarParser {
    //TODO: think about moving constants in Util class for BNFGrammarParser
    private static final String EQUAL = "::=";
    private static final String NEWLINE = "\n";
    private static final String SPACE = "\\s+";
    private static final String EMPTY = "";
    private static final String OR = "\\|";
    private static final String NONTERMINAL = "<(.+?)>";
    private static final String TERMINALS_NONTERMINALS = "(<(.+?)>)|([^<>]+)";  // bilo bez ? u drugom delu

    private String grammar = "";

    private boolean[] compRule; //TODO: refactor this by renaming, check if necessary: used for tracking compiled rules, maybe Rule class?

    private Graph grammarGraph = new Graph(this); //graph for storing parsed symbols of grammar


    /**
     * @param grammar input grammar for notation parser
     */
    public BNFGrammarParser(String grammar) {
        this.grammar = grammar;
    }

    public String getGrammar() {
        return grammar;
    }

    public Graph parse() {
        Node<Symbol> localRoot = null;
        ArrayList<String> rules = (ArrayList<String>) parseRules(grammar);
        compRule = new boolean[rules.size()]; // TODO: what if rules is null?
        for (int i = 0; i < compRule.length; i++) compRule[i] = false;
        for (int i = 0; i < rules.size(); i++) {  // parse each rule
            String[] terminals = rules.get(i).split(EQUAL); // terminals[0] - left side; terminals[1] - right side
            String nonterminal = "";
            Pattern nonterminalPattern = Pattern.compile(NONTERMINAL);
            Matcher leftSideMatcher = nonterminalPattern.matcher(terminals[0]);
            localRoot = grammarGraph.parseLeftSideOfRule(localRoot, terminals[1], nonterminal, leftSideMatcher);
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
                        leftSideMatcher = nonterminalPattern.matcher(matchPart);
                        if (leftSideMatcher.find()) {
                            String nonterminalName = leftSideMatcher.group(1);
                            node = grammarGraph.find(nonterminalName, grammarGraph.getRoot());
                            sym = new Symbol(nonterminalName, true, 0, matchPartIsNonterminal, false);
                        }
                    } else
                        sym = new Symbol(matchPart, true, 0, matchPartIsNonterminal, false);
                    Node<Symbol> nod = new Node<>(sym);
                    grammarGraph.addNode(localRoot, root, node, nod);
                }
            }

        }
        return grammarGraph;
    }

    public Graph getGrammarGraph() {
        return grammarGraph;
    }

    public void setCompRuleToTrue(int i) {
        compRule[i] = true;
    }

    private boolean isNonterminal(String part) { //TODO: can be part of some Util class related to BNFGrammarParser?
        return part.startsWith("<");
    }

    private List<String> parseRules(String grammar) {
        List<String> rules = new ArrayList<>();
        String[] grammarLines = grammar.split(NEWLINE);
        for (String line : grammarLines) {
            rules.add(line.replaceAll(SPACE, EMPTY));
        }
        return rules;
    }
}

