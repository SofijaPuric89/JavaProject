package src.correct.java.com.etf.rti.p1;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.StringUtils;
import src.correct.java.com.etf.rti.p1.exceptions.ErrorNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
    private static final double MINFACTOR = 0.8;
    private static final double MAXFACTOR = 1.2;
    private String input =
            "<p> ::= <malo_slovo>:\\<put>\n" +
                    "<put> ::= <dir> | <put>\\<dir> | \"<dir>\"\\<put>\n" +
                    "<dir> ::= <malo_slovo> | <dir><pom>\n" +
                    "<pom> ::= <malo_slovo> | _ | <cifra>\n" +
                    "<malo_slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                    "<cifra> ::= 0|1|2|3|4|5|6|7|8|9";

    private String[] array = null;
    Graph g = new Graph(this);
    CombinationGenerator gen = new CombinationGenerator(this);

    public ParseGrammar(String grammar) {
        input = grammar;
    }

    public ParseGrammar() {
    }

    public String getGrammar() {
        return input;
    }

    public void parse() {
        Node<Symbol> localRoot = null;
        parseAllRules();
        for (int i = 0; i < array.length; i++) {  // parse every rule
            String[] terminals = array[i].split(EQUAL); // terminals[0] - left side; terminals[1] - right side
            String nonterminal = "";
            Pattern pattern = Pattern.compile(NONTERMINAL);
            Matcher matcher = pattern.matcher(terminals[0]);
            localRoot = g.parseLeftSideOfRule(localRoot, terminals[1], nonterminal, matcher);
            String[] parts = terminals[1].split(OR); // parts on right side of rule
            for (int j = 0; j < parts.length; j++) {
                //System.out.println(parts[j]);
                Node<Symbol> rroot = g.addCompositeNode(localRoot, parts[j]);
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

    private boolean isNonterminal(String part) {
        return part.startsWith("<") ? true : false;
    }

    private void parseAllRules() {
        array = input.split(NEWLINE);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replaceAll(SPACE, EMPTY);
        }
    }

    public void genereteCorrectAnswer(int length) {   // URADITI!
        int minLen = (int) MINFACTOR * length;
        int maxLen = (int) MAXFACTOR * length;
        int currLen = 0;
        Node<Symbol> currRoot = g.root;
        if (currRoot.getData().isInfinite()) {  // ako se ponavlja beskonacno
            List<Node<Symbol>> children = currRoot.getChildren();
            int numOfChildren = currRoot.getChildren().size();
            if (numOfChildren > 1) {
                int numOfInfiniteChildren = 0;
                int numOfNonInfiniteChildren = 0;
                List<Node<Symbol>> infiniteChildren = new ArrayList<Node<Symbol>>();
                List<Node<Symbol>> nonInfiniteChildren = new ArrayList<Node<Symbol>>();
                List<List<Integer>> childrenInfiniteWidths = new ArrayList<List<Integer>>();
                List<List<Integer>> childrenNonInfiniteWidths = new ArrayList<List<Integer>>();
                for (Node<Symbol> child : children) {
                    if (child.getData().isInfinite()) {
                        numOfInfiniteChildren++;
                        infiniteChildren.add(child);
                        childrenInfiniteWidths.add(child.getData().getWidths());
                    }
                    else {
                        numOfNonInfiniteChildren++;
                        nonInfiniteChildren.add(child);
                        childrenNonInfiniteWidths.add(child.getData().getWidths());
                    }
                }
            }
            else currRoot = children.get(0);
        }
    }
    public static void main(String[] args) {
       /* ParseGrammar pg = new ParseGrammar("<p> ::= <p><pom> | <pom>\n" +
                "<pom> ::= _ | <cifra>\n" +
                "<cifra> ::= 0|1|2|3|4|5|6|7|8|9"); */
        /*ParseGrammar pg = new ParseGrammar("<start> ::= 11<a>|<b>1\n" +
                "<a> ::= 1|<a><b>|<a><c><b>\n" +
                "<b> ::= 101|<b>01\n" +
                "<c> ::= 1100|<c>11|<c>00\n");*/
        ParseGrammar pg = new ParseGrammar("<p> ::= <korisnik>!<domen>\n" +
                "<korisnik> ::= <rec> | <korisnik>_<rec><rec> | <korisnik>_<kraj_domena>\n" +
                "<domen> ::= <kraj_domena> | <rec>.<domen>\n" +
                "<kraj_domena> ::= com | co.rs\n" +
                "<rec> ::= <slovo> | <slovo><rec> | <rec><cifra>\n" +
                "<slovo> ::= a|b|c|d|e|f|g|h|i|j|k|l|m|n|o|p|q|r|s|t|u|v|w|x|y|z\n" +
                "<cifra> ::= 0|1|2|3|4|5|6|7|8|9");
        //ParseGrammar pg = new ParseGrammar();
        pg.parse();
        pg.g.setCompositeNodesToRecursive();
        pg.g.setNodesToRecursive();
        pg.g.setNodesToInfinite();
        pg.g.setWidthToAllNodes();
        pg.g.setDifferenceLenToRecursiveNodes();
        Node<Symbol> node = pg.g.find("<korisnik>_<rec><rec>", pg.g.root);
        List<Node<Symbol>> list = node.getOrdNumOfRecursiveChildren();
        System.out.println("***");
        for (Node<Symbol> child : list) {
            System.out.println(child.getData().getName());
        }
        System.out.println("***");
        System.out.println("***");
        GenerateAnswer ga = new GenerateAnswer(pg.g, true);
        Node<Symbol> cc = pg.g.find("<korisnik>_<rec><rec>", pg.g.root);
        List<Node<Symbol>> l = ga.getListOfNonterminals(cc);
        for (Node<Symbol> c : l) {
            System.out.println(c.getData().getName());
        }
        System.out.println("***");
    }
}

