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
    Node<Symbol> root = null;
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
            localRoot = parseLeftSideOfRule(localRoot, terminals[1], nonterminal, matcher);
            String[] parts = terminals[1].split(OR); // parts on right side of rule
            for (int j = 0; j < parts.length; j++) {
                //System.out.println(parts[j]);
                Node<Symbol> rroot = addCompositeNode(localRoot, parts[j]);
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
                            node = find(nonterminalName, root);
                            sym = new Symbol(nonterminalName, true, 0, b, false);
                        }
                    } else
                        sym = new Symbol(part, true, 0, b, false);
                    Node<Symbol> nod = new Node<Symbol>(sym);
                    addNode(localRoot, rroot, node, nod);
                }
            }

        }
    }

    private void addNode(Node<Symbol> localRoot, Node<Symbol> rroot, Node<Symbol> node, Node<Symbol> nod) {
        if (node != null) {
            //dodavanje novog roditelja
            if (rroot != null) {
                setParentToExistingNode(localRoot, rroot, node);
            } else {
                setParentToExistingNode(rroot, localRoot, node);
            }
        } else {
            if (rroot != null)
                rroot.addChild(nod);
            else if (localRoot != null)
                localRoot.addChild(nod);
        }
    }

    private void setParentToExistingNode(Node<Symbol> localRoot, Node<Symbol> rroot, Node<Symbol> node) {
        if (node.equals(rroot) || node.equals(localRoot))
            node.setRecursive(true);
        else {
            boolean bb = searchAllTree(node);
            if (bb) node.setRecursive(true);
            else
                node.setParent(rroot);
        }
    }

    private boolean isNonterminal(String part) {
        return part.startsWith("<") ? true : false;
    }

    private Node<Symbol> addCompositeNode(Node<Symbol> localRoot, String part) {
        String s = part.substring(part.indexOf("<") + 1);
        if (!(s.equals(part)))
            s = s.substring(0, s.indexOf(">"));
        int count = StringUtils.countMatches(part, "<"); // how many times contains character <
        Node<Symbol> rroot = null;
        if ((count > 1) || ((s.length() + 2) < part.length())) {
            Symbol ss = new Symbol(part, false, 0, false, true);
            rroot = new Node<Symbol>(ss);
            if (localRoot != null)
                localRoot.addChild(rroot);
        }// adding composite node (composite node - node that contains more then one symbol)
        return rroot;
    }

    private Node<Symbol> parseLeftSideOfRule(Node<Symbol> localRoot, String terminal, String nonterminal, Matcher matcher) {
        if (matcher.find()) {
            nonterminal = matcher.group(1);
        }
        if (root == null) {
            Symbol s = new Symbol(nonterminal, true, 0, true, false);
            root = localRoot = new Node<Symbol>(s);
        } else {
            Node<Symbol> sym = find(nonterminal, root);
            if (sym != null)
                localRoot = sym;
        }
        localRoot.setRecursive(isRecursive(nonterminal, terminal));
        return localRoot;
    }

    private void parseAllRules() {
        array = input.split(NEWLINE);
        for (int i = 0; i < array.length; i++) {
            array[i] = array[i].replaceAll(SPACE, EMPTY);
        }
    }

    public Node<Symbol> find(String name, Node<Symbol> node) {
        Node<Symbol> foundNode = null;
        if (node.getData().getName().equals(name)) {
            foundNode = node;
            return node;
        } else {
            if (!node.isLeaf()) {
                for (int i = 0; i < node.getChildren().size(); i++) {
                    if (node.getChildren().get(i) != null) {
                        foundNode = find(name, node.getChildren().get(i));
                        if (foundNode != null)
                            return foundNode;
                    }
                }
            }
        }
        return foundNode;
    }

    public boolean searchAllTree(Node<Symbol> node) {
        boolean b = false;
        List<Node<Symbol>> parents = node.getParents();
        for (int i = 0; i < parents.size(); i++) {
            if (parents.get(i).equals(node))
                b = true;
            searchAllTree(parents.get(i));
        }
        return b;
    }

    public void updateDepth(Node<Symbol> node, int depth) {
        if (node != null) {
            node.getData().setDepth(depth);
            for (int i = 0; i < node.getChildren().size(); i++) {
                if (node.getChildren().get(i) != null) {
                    updateDepth(node.getChildren().get(i), depth + 1);
                }
            }
        }
    }

    public void updateDepthInverse(Node<Symbol> node, int depth) {
        if (node != null) {
            node.getData().setInverseDepth(depth);
            for (int i = 0; i < node.getParents().size(); i++) {
                if (node.getParents().get(i) != null) {
                    updateDepthInverse(node.getParents().get(i), depth + 1);
                }
            }
        }
    }

    public void updateWidth(Node<Symbol> node, int width) {
        if (node != null) {
            if (!node.getData().isComposite()) {
                if (!node.getData().getWidths().contains(width))  // ako nije kompozitni, dodajemo sirinu samo ako ista vec ne postoji u listi
                    node.getData().addWidth(width);
            } else {
                node.getData().setWidth(width);
            }

            for (Node<Symbol> parent : node.getParents()) {
                if (parent != null) {
                    if (parent.getData().isComposite()) {
                        int sum = 0;
                        //updateWidth(parent, parent.getData().getWidth() + width);
                        for (Node<Symbol> nod : parent.getChildren())
                            sum += nod.getData().getWidth();
                        updateWidth(parent, sum);

                    } else
                        updateWidth(parent, width);
                }
            }
        }
    }

    public void updateWidths(Node<Symbol> node, List<Integer> widths) {
        if (node != null) {
            setWidthsToNode(node, widths);
            for (Node<Symbol> parent : node.getParents()) {
                if (parent != null) {
                    if (parent.getData().isComposite()) {
                        gen.calculateWidthOfCompositeNode(parent, true);
                    } else
                        updateWidths(parent, widths);
                }
            }
        }
    }


    private void setWidthsToNode(Node<Symbol> node, List<Integer> widths) {
        if (!node.getData().isComposite()) {
            for (int w : widths)
                if (!node.getData().getWidths().contains(w)) { // ako nije kompozitni, dodajemo sirinu samo ako ista vec ne postoji u listi
                    node.getData().addWidth(w);
                    break;
                }
        } else {
            //node.getData().getWidths().clear();   // dodato
            node.getData().setWidths(widths);
            //updateParentsWidths(node);
        }
    }

    private void updateParentsWidths(Node<Symbol> node) {
        if (node != null) {
            for (Node<Symbol> parent : node.getParents()) {
                if (parent != null && areAllChildrenComposite(parent)) {
                    //parent.getData().getWidths().clear();
                    parent.getData().getWidths().addAll(node.getData().getWidths());
                }
            }
        }
    }

    private boolean areAllChildrenComposite(Node<Symbol> node) {
        boolean b = true;
        for (Node<Symbol> child : node.getChildren()) {
            if (!child.getData().isComposite()) {
                b = false;
            }
        }
        return b;
    }


    public void updateDepthToAllNodes() {
        List<Node<Symbol>> list = returnAllNodes(root);
        List<Node<Symbol>> leafs = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> sym : list)
            if (sym.isLeaf())
                leafs.add(sym);
        Node<Symbol> max = findNodeMaxDepth();
        updateDepthInverse(max, 0);
    }

    public void setWidthToAllNodes() {
        List<Node<Symbol>> list = returnAllNodes(root);
        List<Node<Symbol>> leafs = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> sym : list)
            if (sym.isLeaf())
                leafs.add(sym);
        for (Node<Symbol> leaf : leafs) {                      // width of terminals is their length (length of their name)
            List<Integer> l = new ArrayList<Integer>();
            l.add(leaf.getData().getName().length());
            updateWidths(leaf, l);
        }
        for (Node<Symbol> l:list) {
            if (l.getData().isInfinite())
                System.out.println(l.getData().getName() + " infinite!" + l.getData().getWidths());
            else
                System.out.println(l.getData().getName() + " " + l.getData().getWidths());
        }

    }

    private Node<Symbol> findNodeMaxDepth() {
        updateDepth(root, 0);
        int maxDepth = 0;
        Node<Symbol> maxNode = null;
        List<Node<Symbol>> list = returnAllNodes(root);
        for (Node<Symbol> sym : list) {
            if (sym.getData().getDepth() > maxDepth) {
                maxDepth = sym.getData().getDepth();
                maxNode = sym;
            }
        }
        return maxNode;
    }

    public static List<Node<Symbol>> returnAllNodes(Node node) {
        List<Node<Symbol>> listOfNodes = new ArrayList<Node<Symbol>>();
        addAllNodes(node, listOfNodes);
        return listOfNodes;
    }

    private static void addAllNodes(Node node, List<Node<Symbol>> listOfNodes) {
        if (node != null && !listOfNodes.contains(node)) {
            listOfNodes.add(node);
            List<Node> children = node.getChildren();
            if (children != null) {
                for (Node child : children) {
                    addAllNodes(child, listOfNodes);
                }
            }
        }
    }

    public boolean isRecursive(String nonterminal, String rightSide) {
        String nonTerm = "<" + nonterminal + ">";
        if (rightSide.contains(nonTerm))
            return true;
        else return false;
    }

    public String print(Node<Symbol> node) {
        String result = node.getData().getName() + "\n";
        if (!node.isLeaf()) {
            for (int i = 0; i < node.getChildren().size(); i++) {
                if (node.getChildren().get(i) != null)
                    result += print(node.getChildren().get(i));
            }
        }
        return result;
    }

    public void setCompositeNodesToRecursive() {
        List<Node<Symbol>> list = returnAllNodes(root);
        List<Node<Symbol>> recursiveNodes = new ArrayList<Node<Symbol>>();
        List<Node<Symbol>> compositeNodes = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> node : list) {
            if (node.isRecursive()) {
                recursiveNodes.add(node);
            }
            if (node.getData().isComposite()) {
                compositeNodes.add(node);
            }
        }
        for (Node<Symbol> compNode : compositeNodes) {
            for (Node<Symbol> recNode : recursiveNodes) {
                String recNodeName = "<" + recNode.getData().getName() + ">";
                if (compNode.getData().getName().contains(recNodeName)) {
                    compNode.setRecursive(true);
                    compNode.addOrdNumOfRecursiveChildren(recNode);
                }
            }
        }
    }

    public List<Node<Symbol>> getListOfRecursiveNodes() {
        List<Node<Symbol>> list = returnAllNodes(root);
        List<Node<Symbol>> recursiveNodes = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> node : list) {
            if (node.isRecursive()) {
                recursiveNodes.add(node);
            }
        }
        return recursiveNodes;
    }

    public List<Node<Symbol>> getListOfRecursiveNonCompositeNodes() {
        List<Node<Symbol>> list = getListOfRecursiveNodes();
        List<Node<Symbol>> directRecursiveNodes = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> node : list) {
            if (!node.getData().isComposite()) {
                List<Node<Symbol>> children = node.getChildren();
                boolean direct = false;
                for (Node<Symbol> child : children) {
                    if (child.getData().isComposite()) {
                        if (child.getData().getName().contains("<" + node.getData().getName() + ">"))
                            direct = true;
                    }
                }
                if (direct)
                    directRecursiveNodes.add(node);
            }
        }
        return directRecursiveNodes;
    }

    public void setParentsToRecursive(Node<Symbol> node, boolean recursive) {
        if (node != null) {
            node.setRecursive(recursive);
            for (int i = 0; i < node.getParents().size(); i++) {
                if (node.getParents().get(i) != null) {
                    setParentsToRecursive(node.getParents().get(i), recursive);
                }
            }
        }
    }

    public void setNodesToRecursive() {
        List<Node<Symbol>> recNodes = getListOfRecursiveNodes();
        for (Node<Symbol> node : recNodes) {
            setParentsToRecursive(node, true);
        }
    }

    public void setNodesToInfinite() {
        List<Node<Symbol>> recNodes = getListOfRecursiveNodes();
        for (Node<Symbol> node : recNodes) {
            node.getData().setInfinite(true);
        }
    }

    public List<Node<Symbol>> getChildrenComposite(Node<Symbol> node) {
        List<Node<Symbol>> list = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> child : node.getChildren()) {
            if (child.getData().isComposite())
                list.add(child);
        }
        return list;
    }

    public void setDifferenceLenToRecursiveNodes() {
        List<Node<Symbol>> recNonCompNodes = getListOfRecursiveNonCompositeNodes();
        for (Node<Symbol> node : recNonCompNodes) {
            List<Node<Symbol>> childrenComposite = getChildrenComposite(node);
            for (Node<Symbol> child : childrenComposite) {
                List<Integer> differencesOfCompositeNode = gen.calculateWidthOfCompositeNode(child, false);
                node.getData().setDifferenceLenArray(child.getData().getName(), differencesOfCompositeNode);
            }
        }
        for (Node<Symbol> node : recNonCompNodes) {
            updateDifferences(node, node.getData().getDifferenceLen(), recNonCompNodes);
        }
        for (Node<Symbol> node : returnAllNodes(root)) {
            System.out.print(node.getData().getName());
            System.out.println(node.getData().getDifferenceLen());
        }
    }

    public void updateDifferences(Node<Symbol> node, HashMap<String, List> differences, List<Node<Symbol>> list) {
        if (node != null) {
            node.getData().setDifferencesLengths(differences);
            for (Node<Symbol> parent : node.getParents()) {
                if (parent != null) {
                    if (!list.contains(parent)) {           // ako parent nije direktno rekurzivni
                        if (!parent.getData().isComposite()) {
                            updateDifferencesOfNonCompositeNode(differences, list, parent);
                        } else {
                            updateDifferencesOfCompositeNode(node, list, parent);
                        }
                    }
                }
            }
        }
    }

    private void updateDifferencesOfCompositeNode(Node<Symbol> node, List<Node<Symbol>> list, Node<Symbol> parent) {
        int numOfChildrenHasDifferences = 1;                   // ako je kompozitni spajaju se differenceLen od svih potomaka
        List<Node<Symbol>> childrenHasDifferences = new ArrayList<Node<Symbol>>();  // node mu je sigurno dete - uzima se njegova mapa i od svih ostalih
        childrenHasDifferences.add(node);
        for (Node<Symbol> child : parent.getChildren()) {
            if (child.getData().isNonterminal() && !child.getData().getDifferenceLen().isEmpty()) {

                if (!childrenHasDifferences.contains(child)) {
                    numOfChildrenHasDifferences++;
                    childrenHasDifferences.add(child);
                }
            }
        }
        int[][] ordNumbersOfChildrenMaps = getOrdNumbersOfChildrenMaps(numOfChildrenHasDifferences, childrenHasDifferences);
        List<Integer> partial = new ArrayList<Integer>();
        List<List<Integer>> all = new ArrayList<List<Integer>>();
        HashMap<String, List> map = new HashMap<String, List>();
        gen.catchAllCombinations(ordNumbersOfChildrenMaps, partial, all);  // all - sve kombinacije iz mapa dece
        for (List<Integer> combination : all) {
            putOneCombinationToMap(childrenHasDifferences, map, combination);
        }
        parent.getData().setDifferenceLen(map);
        updateDifferences(parent, map, list);
    }

    private void putOneCombinationToMap(List<Node<Symbol>> childrenHasDifferences, HashMap<String, List> map, List<Integer> combination) {
        List<Integer> diffs = new ArrayList<Integer>();
        String name = "";
        int ordNumOfChild = 0;
        int ordNumOfCombination = 1;
        for (int ordNum : combination) {
            Node<Symbol> child = childrenHasDifferences.get(ordNumOfChild++);
            name += child.getData().getName();
            for (List<Integer> value : child.getData().getDifferenceLen().values()) {
                if (ordNum == ordNumOfCombination) {
                    diffs.addAll(value);
                    break;
                }
                ordNumOfCombination++;
            }
        }
        if (map.get(name) != null)
            map.get(name).addAll(diffs);
        else
            map.put(name, diffs);
    }

    private int[][] getOrdNumbersOfChildrenMaps(int numOfChildrenHasDifferences, List<Node<Symbol>> childrenHasDifferences) {
        int[][] ordNumbersOfChildrenMaps = new int[numOfChildrenHasDifferences][];
        int ind = 0;
        //System.out.println(numOfChildrenHasDifferences + " " + childrenHasDifferences.size());
        for (Node<Symbol> child : childrenHasDifferences) {
            int sizeOfChildMap = child.getData().getDifferenceLen().size();
            int[] ords = new int[sizeOfChildMap];
            for (int i=1;i<=sizeOfChildMap;i++) {
                ords[i-1] = i;
            }
            ordNumbersOfChildrenMaps[ind++] = ords;
        }
        return ordNumbersOfChildrenMaps;
    }

    private void updateDifferencesOfNonCompositeNode(HashMap<String, List> differences, List<Node<Symbol>> list, Node<Symbol> parent) {
        if (parent.getChildren().size() == 1) {
            updateDifferences(parent, differences, list);  // ako nije kompozitni i ima jedno dete samo se propagira dalje
        }
        else {
            HashMap<String, List> map = new HashMap<String, List>();
            for (Node<Symbol> child : parent.getChildren()) {
                if (!child.getData().getDifferenceLen().isEmpty()) {
                    map.putAll(child.getData().getDifferenceLen());
                }
            }
            parent.getData().setDifferenceLen(map);
            updateDifferences(parent, map, list);
        }
    }

    private int getSumOfTerminals(Node<Symbol> parent) {
        int sum = 0;
        for (Node<Symbol> child : parent.getChildren()) {
            if (!child.getData().isNonterminal())
                sum += child.getData().getName().length();
        }
        return sum;
    }

    public void genereteCorrectAnswer(int length) {   // URADITI!
        int minLen = (int) MINFACTOR * length;
        int maxLen = (int) MAXFACTOR * length;
        int currLen = 0;
        Node<Symbol> currRoot = root;
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
        ParseGrammar pg = new ParseGrammar();
        pg.parse();
        pg.setCompositeNodesToRecursive();
        pg.setNodesToRecursive();
        pg.setNodesToInfinite();
        pg.setWidthToAllNodes();
        pg.setDifferenceLenToRecursiveNodes();
        /*
        int[][] matrix = {{1,2,3}, {1,2}, {1,2,3,4}};
        List<Integer> p = new ArrayList<Integer>();
        List<List<Integer>> all = new ArrayList<List<Integer>>();
        pg.catchAllCombinations(matrix, p, all);
        for (List<Integer> list : all) {
            for (int num : list) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        */
    }
}

