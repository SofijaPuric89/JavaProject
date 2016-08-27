package com.etf.rti.p1.translator.graph;

import com.etf.rti.p1.translator.BNFGrammarToGraphTranslator;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by Korisnik on 20.1.2016.
 */
public class Graph {
    private Node<Symbol> root = null;
    private BNFGrammarToGraphTranslator BNFGrammarToGraphTranslator;
    private CombinationGenerator combinationGenerator;

    public Node<Symbol> getRoot() {
        return root;
    }

    public Graph(BNFGrammarToGraphTranslator grammar) {
        BNFGrammarToGraphTranslator = grammar;
        combinationGenerator = new CombinationGenerator(BNFGrammarToGraphTranslator);
    }

    public void addNode(Node<Symbol> localRoot, Node<Symbol> root, Node<Symbol> node, Node<Symbol> nod) {
        if (node != null) {
            //dodavanje novog roditelja
            if (root != null) {
                setParentToExistingNode(localRoot, root, node);
            } else {
                setParentToExistingNode(root, localRoot, node);
            }
        } else {
            if (root != null)
                root.addChild(nod);
            else if (localRoot != null)
                localRoot.addChild(nod);
        }
    }

    public void setParentToExistingNode(Node<Symbol> localRoot, Node<Symbol> root, Node<Symbol> node) {
        if (node.equals(root) || node.equals(localRoot))
            node.setRecursive(true);
        else {
            boolean bb = searchAllTree(node);
            if (bb) node.setRecursive(true);
            else
                node.setParent(root);
        }
    }

    public Node<Symbol> addCompositeNode(Node<Symbol> localRoot, String part, int index) {
        String s = part.substring(part.indexOf("<") + 1);
        if (!(s.equals(part)))
            s = s.substring(0, s.indexOf(">"));
        int count = StringUtils.countMatches(part, "<"); // how many times contains character <
        Node<Symbol> rroot = null;
        if ((count > 1) || ((s.length() + 2) < part.length())) {
            BNFGrammarToGraphTranslator.setCompRuleToTrue(index);
            Symbol ss = new Symbol(part, false, 0, false, true);
            rroot = new Node<Symbol>(ss);
            if (localRoot != null)
                localRoot.addChild(rroot);
        }// adding composite node (composite node - node that contains more then one symbol)
        return rroot;
    }

    public Node<Symbol> parseLeftSideOfRule(Node<Symbol> localRoot, String terminal, String nonterminal, Matcher matcher) {
        if (matcher.find()) {
            nonterminal = matcher.group(1); //get right side of rule
        }
        if (root == null) {
            Symbol s = new Symbol(nonterminal, true, 0, true, false);
            root = localRoot = new Node<>(s);
        } else {
            Node<Symbol> sym = find(nonterminal, root);
            if (sym != null)
                localRoot = sym;
        }
        localRoot.setRecursive(isRecursive(nonterminal, terminal));
        return localRoot;
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

    public void updateWidths(Node<Symbol> node, List<Integer> widths) {
        if (node != null) {
            setWidthsToSpecificNode(node, widths);
            if (isNodeCompositeDirectRecursive(node)) {
                calculateCompDirRecNodeWidths(node);
            }
            setToComplete(node);
            if (isNodeDirectRecursiveNonComposite(node)) {
                adjustWidthsToDirectRecursiveNonCompositeNode(node);
            }
            for (Node<Symbol> parent : node.getParents()) {
                if (parent != null) {
                    if (parent.getData().isComposite()) {
                        combinationGenerator.calculateWidthOfCompositeNode(parent, true);
                    } else
                        updateWidths(parent, widths);
                }
            }
        }
    }

    private void adjustWidthsToDirectRecursiveNonCompositeNode(Node<Symbol> node) {
        List<Integer> allWidths = new ArrayList<Integer>();
        boolean allChildrenComplete = true;
        for (Node<Symbol> child : node.getChildren()) {
            if (child.isComplete()) {
                allWidths.addAll(child.getData().getWidths());
            } else {
                allChildrenComplete = false;
            }
        }
        if (allChildrenComplete) {
            allWidths = removeDuplicates(allWidths);
            node.getData().getWidths().clear();
            setWidthsToSpecificNode(node, allWidths);
        }
    }

    private void setToComplete(Node<Symbol> node) {
        boolean allChildrenCompl = true;
        for (Node<Symbol> child : node.getChildren()) {
            if (!child.isComplete()) {
                allChildrenCompl = false;
            }
        }
        if (allChildrenCompl) {
            node.setComplete(true);
        }
    }


    private void calculateCompDirRecNodeWidths(Node<Symbol> node) {
        List<Element> elements = getCompositeDirectRecursiveNodes();
        String parentName = "";
        for (Element el : elements) {
            if (el.getCompositeDirectRecursiveNode().equals(node))
                parentName = el.getParentName();
        }
        Node<Symbol> parent = find(parentName, root);
        List<Node<Symbol>> brothers = getBrothersNonDirectRecursiveNodes(node, parent);
        List<Integer> minLengths = getListOfMinimumLenghts(brothers);
        List<Integer> finalList = sumTwoLists(node.getData().getWidths(), minLengths);
        node.getData().getWidths().clear();
        //setWidthsToNode(node, finalList);
        setWidthsToSpecificNode(node, finalList);
    }

    private void setWidthsToSpecificNode(Node<Symbol> node, List<Integer> widths) {
        widths = removeDuplicates(widths);
        if (!(isNodeDirectRecursiveNonComposite(node) || isNodeCompositeDirectRecursive(node))) {
            node.getData().getWidths().addAll(widths);
            removeDuplicates(node.getData().getWidths());
        } else node.getData().setWidths(widths);
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
        for (Node<Symbol> l : list) {
            if (l.getData().isInfinite())
                System.out.println(l.getData().getName() + " infinite!" + l.getData().getWidths());
            else
                System.out.println(l.getData().getName() + " " + l.getData().getWidths());
        }
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

    private boolean isRecursive(String nonterminal, String rightSide) {
        return rightSide.contains("<" + nonterminal + ">");
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
        List<Node<Symbol>> recursiveNodes = new ArrayList<>();
        List<Node<Symbol>> compositeNodes = new ArrayList<>();
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

    private boolean isNodeDirectRecursiveNonComposite(Node<Symbol> node) {
        List<Node<Symbol>> list = getListOfRecursiveNonCompositeNodes();
        if (list.contains(node))
            return true;
        else
            return false;
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
                List<Integer> differencesOfCompositeNode = combinationGenerator.calculateWidthOfCompositeNode(child, false);
                node.getData().setDifferenceLenArray(child.getData().getName(), new Lists(child.getData().getWidths(), differencesOfCompositeNode));
            }
        }
        List<Element> compRecElements = getCompositeDirectRecursiveNodes();
        List<Node<Symbol>> compRecNodes = new ArrayList<Node<Symbol>>();
        for (Element el : compRecElements) {
            compRecNodes.add(el.getCompositeDirectRecursiveNode());
        }
        List<Node<Symbol>> recNodes = new ArrayList<Node<Symbol>>();
        recNodes.addAll(recNonCompNodes);
        recNodes.addAll(compRecNodes);
        for (Node<Symbol> node : recNonCompNodes) {
            updateDifferences(node, node.getData().getDifferenceLen(), recNodes);  // bilo recNonCompNodes
        }
        for (Node<Symbol> node : returnAllNodes(root)) {
            System.out.print(node.getData().getName());
            System.out.println(node.getData().getDifferenceLen());
        }
    }

    public List<Element> getCompositeDirectRecursiveNodes() {
        List<Node<Symbol>> allNodes = returnAllNodes(root);
        List<Node<Symbol>> compositeNodes = new ArrayList<Node<Symbol>>();
        List<Element> compRecNodes = new ArrayList<Element>();
        for (Node<Symbol> node : allNodes) {
            if (node.getData().isComposite())
                compositeNodes.add(node);
        }
        for (Node<Symbol> node : compositeNodes) {
            for (Node<Symbol> parent : node.getParents())
                if (node.getData().getName().contains("<" + parent.getData().getName() + ">")) {
                    Element e = new Element(parent.getData().getName(), node);
                    compRecNodes.add(e);
                }
        }
        return compRecNodes;
    }

    public boolean isNodeCompositeDirectRecursive(Node<Symbol> node) {
        List<Element> elements = getCompositeDirectRecursiveNodes();
        for (Element el : elements) {
            if (el.getCompositeDirectRecursiveNode().equals(node))
                return true;
        }
        return false;
    }

    private List<Node<Symbol>> getBrothersNonDirectRecursiveNodes(Node<Symbol> compNode, Node<Symbol> parent) {
        List<Node<Symbol>> brothers = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> child : parent.getChildren()) {
            if (!child.equals(compNode)) {   // iteriramo kroz bracu
                if (!child.getData().getName().contains("<" + parent.getData().getName() + ">")) {
                    brothers.add(child);
                }
            }
        }
        return brothers;
    }

    private List<Integer> getListOfMinimumLenghts(List<Node<Symbol>> nodes) {
        List<Integer> list = new ArrayList<Integer>();
        for (Node<Symbol> node : nodes) {
            list.addAll(node.getData().getWidths());
        }
        list = removeDuplicates(list);
        return list;
    }

    private List<Integer> sumTwoLists(List<Integer> compWidths, List<Integer> anotherList) {
        List<Integer> returnList = new ArrayList<Integer>();
        for (int num1 : compWidths) {
            for (int num2 : anotherList) {
                returnList.add(num1 + num2);
            }
        }
        returnList = removeDuplicates(returnList);
        return returnList;
    }

    private List<Integer> removeDuplicates(List<Integer> list) {
        Set<Integer> hs = new HashSet<Integer>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);
        return list;
    }

    public void updateDifferences(Node<Symbol> node, HashMap<String, Lists> differences, List<Node<Symbol>> list) {
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
        HashMap<String, Lists> map = new HashMap<String, Lists>();
        combinationGenerator.catchAllCombinations(ordNumbersOfChildrenMaps, partial, all);  // all - sve kombinacije iz mapa dece
        for (List<Integer> combination : all) {
            putOneCombinationToMap(childrenHasDifferences, map, combination, parent.getData().getWidths());
        }
        //parent.getData().setDifferencesLengths(map);
        if (getLastNonterminalWithDiffs(parent).equals(node)) {
            updateDifferences(parent, map, list);
        }

    }

    private Node<Symbol> getLastNonterminalWithDiffs(Node<Symbol> parent) {
        Node<Symbol> node = null;
        for (int i = parent.getChildren().size() - 1; i >= 0; i--) {
            Node<Symbol> child = parent.getChildren().get(i);
            if (child.getData().isNonterminal() && !child.getData().getDifferenceLen().isEmpty()) {
                node = child;
                break;
            }
        }
        return node;
    }


    private void putOneCombinationToMap(List<Node<Symbol>> childrenHasDifferences, HashMap<String, Lists> map, List<Integer> combination, List<Integer> compNodeWidths) {
        List<Integer> diffs = new ArrayList<Integer>();
        String name = "";
        int ordNumOfChild = 0;
        int ordNumOfCombination = 1;
        for (int ordNum : combination) {
            Node<Symbol> child = childrenHasDifferences.get(ordNumOfChild++);
            name += child.getData().getName() + ordNum;
            ordNumOfCombination = 1;
            for (Lists value : child.getData().getDifferenceLen().values()) {
                if (ordNum == ordNumOfCombination) {
                    diffs.addAll(value.getDifferences());
                    break;
                }
                ordNumOfCombination++;
            }
        }
        //if (parent.getChildren().get(parent.getChildren().size()-1).equals(node)) {
        if (map.get(name) != null)
            map.get(name).getDifferences().addAll(diffs);
        else
            map.put(name, new Lists(compNodeWidths, diffs));
        // }
    }

    private int[][] getOrdNumbersOfChildrenMaps(int numOfChildrenHasDifferences, List<Node<Symbol>> childrenHasDifferences) {
        int[][] ordNumbersOfChildrenMaps = new int[numOfChildrenHasDifferences][];
        int ind = 0;
        //System.out.println(numOfChildrenHasDifferences + " " + childrenHasDifferences.size());
        for (Node<Symbol> child : childrenHasDifferences) {
            int sizeOfChildMap = child.getData().getDifferenceLen().size();
            int[] ords = new int[sizeOfChildMap];
            for (int i = 1; i <= sizeOfChildMap; i++) {
                ords[i - 1] = i;
            }
            ordNumbersOfChildrenMaps[ind++] = ords;
        }
        return ordNumbersOfChildrenMaps;
    }

    private void updateDifferencesOfNonCompositeNode(HashMap<String, Lists> differences, List<Node<Symbol>> list, Node<Symbol> parent) {
        if (parent.getChildren().size() == 1) {
            updateDifferences(parent, differences, list);  // ako nije kompozitni i ima jedno dete samo se propagira dalje
        } else {
            HashMap<String, Lists> map = new HashMap<String, Lists>();
            for (Node<Symbol> child : parent.getChildren()) {
                if (!child.getData().getDifferenceLen().isEmpty()) {
                    map.putAll(child.getData().getDifferenceLen());
                }
            }
            //parent.getData().setDifferencesLengths(map);
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

    public List<Node<Symbol>> getAllTerminals() {
        List<Node<Symbol>> all = returnAllNodes(root);
        return all.stream()
                .filter(node -> !node.getData().isNonterminal() && node.getData().isNode())
                .collect(Collectors.toList());
    }
}
