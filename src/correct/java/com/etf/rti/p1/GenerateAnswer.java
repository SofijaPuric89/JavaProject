package src.correct.java.com.etf.rti.p1;


import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Sofija on 1/29/2016.
 */
public class GenerateAnswer {
    private Graph graph;
    private int length;
    private boolean correct;
    private static final double MIN_LENGTH = 0.9;
    private static final double MAX_LENGTH = 1.1;
    private static final String TERMINALSNONTERMINALS = "(<(.+?)>)|([^<>]+)";
    private static final String NONTERMINAL = "<(.+?)>";
    private static Random randGenerator = new Random();
    int correctLen;
    int generateLen = 0;

    static {
        Calendar cal = Calendar.getInstance();
        randGenerator.setSeed(cal.getTimeInMillis());
    }
    public GenerateAnswer(Graph g, boolean c) {
        graph = g; correct = c;
        //randGenerator.setSeed(seed);
    }
    public void generate(int l) {
        length = l;
    }

    public void generateIncorrectAnswer(Node<Symbol> node, int len) {
        ExponentialDistribution eg = new ExponentialDistribution(3.*len/4);
        correctLen =  (int) Math.round(eg.sample());
    }


    public String generateAnswerForNode(Node<Symbol> node, int len) {
        // faza 1 i faza 2: slucajno odabiramo dete koje ima duzinu najpriblizniju len iz liste dece koji imaju pribliznu duzinu len
        if (len < 1) len = 1;
        List<Node<Symbol>> children = getChildren(node, len);
        // ispisivanje dece sa odgovarajucom duzinom
        Node<Symbol> randNode = getRandomNode(children);
        if (randNode.getData().isComposite()) {
            // faza 3: od duzine oduzeti duzinu terminala
            int terminalsLength = getLenOfChildrenTerminals(randNode);
            int left = len - terminalsLength;
            // faza 4:
            List<Node<Symbol>> nodes = getListOfNodes(randNode);
            List<Node<Symbol>> nonterminals = getListOfNonterminals(nodes);
            List<Pair<Node<Symbol>, Integer>> pairs = new ArrayList<Pair<Node<Symbol>, Integer>>();
            for (int i=0;i<nodes.size();i++) {
                pairs.add(null);
            }
            while (!nonterminals.isEmpty()) {
                boolean nonRecursive = false; int i = 0;    // ako u listi imamo jedan nerekurzivni, prvo treba odabrati njega - NE RESAVAMO PROBLEM AKO IMAMO VISE OD JEDNOG NEREKURZIVNOG - TO TREBA RESITI!
                for (Node<Symbol> nod : nonterminals) {
                    if (!nod.isRecursive()) {
                        nonRecursive = true;
                        break;
                    }
                    i++;
                }
                int randNum;
                if (nonRecursive) {
                    randNum = i;
                }
                else {
                    randNum = randomNumber(nonterminals.size());
                }
                Node<Symbol> randomNode = nonterminals.get(randNum);
                nonterminals.remove(randNum);
                int ordNum = 0;
                ordNum = getOrdNum(nodes, randNum, ordNum);
                int sum = getMinimumOfList(nonterminals);
                PermutationGenerator pg = new PermutationGenerator(left-sum);
                HashMap<String, Lists> mapOfNode = randomNode.getData().getDifferenceLen();
                HashMap<String, Lists> cloneMap = (HashMap<String, Lists>) mapOfNode.clone();
                List<Node<Symbol>> childrenList = randomNode.getChildren();
                for (Node<Symbol> child : childrenList) {
                    if (child.getData().getDifferenceLen().isEmpty()) {
                        List<Integer> minimus = child.getData().getWidths();
                        List<Integer> diffs = new ArrayList<Integer>();
                        Lists twoList = new Lists(minimus, diffs);
                        cloneMap.put(child.getData().getName(), twoList);
                    }
                }
                int randNumInSet = 0;
                if(len == 3) {
                    len = 3;
                }
                if (!cloneMap.isEmpty()) {
                    randNumInSet = getRandNumInPermutationsSet(pg, cloneMap);
                }
                else {   // ako je mapOfNode prazna, onda dohvatamo minimalne duzine, a diffs su 0
                    randNumInSet = getRandNumInMinimumsSet(left, randomNode, sum);
                }

                if (nonterminals.isEmpty()) {
                    randNumInSet = left;
                }
                pairs.set(ordNum, new MutablePair<Node<Symbol>, Integer>(randomNode, randNumInSet));
                left -= randNumInSet;
            }
            String answer = generateAnswerForCompositeNode(nodes, pairs);
            return answer;
        }
        else {
            if (!randNode.getData().isNonterminal()) {
                generateLen += randNode.getData().getName().length();
                return randNode.getData().getName();
            }
            else {
                return generateAnswerForNode(randNode, len);
            }
        }
    }


    private String generateAnswerForCompositeNode(List<Node<Symbol>> nodes, List<Pair<Node<Symbol>, Integer>> pairs) {
        if (generateLen >= correctLen && !correct) {
            List<Node<Symbol>> cloneNodes = new ArrayList<Node<Symbol>>();
            List<Pair<Node<Symbol>, Integer>> clonePairs = new ArrayList<Pair<Node<Symbol>, Integer>>();
            List<Integer> ordNumbers = new ArrayList<Integer>();
            Set<Integer> setOfOrds = new TreeSet<Integer>();
            boolean same = true;
            while (same) {
                for (int i=0;i<nodes.size();i++) {
                    ordNumbers.add(i);
                    setOfOrds.add(i);
                }
                int i = 0;
                cloneNodes.clear(); clonePairs.clear();
                while (!setOfOrds.isEmpty()) {
                    int randNum = randomNumber(setOfOrds.size());
                    int num = (Integer) setOfOrds.toArray()[randNum];
                    setOfOrds.remove(num);
                    cloneNodes.add(nodes.get(num));
                    clonePairs.add(pairs.get(num));
                    if (i != num) {
                        same = false;
                    }
                    i++;
                }
            }
            nodes = cloneNodes; pairs = clonePairs;
        }
        String answer = "";
        for (int i=0;i<nodes.size();i++) {
            if (nodes.get(i) == null) {
                answer  += generateAnswerForNode(pairs.get(i).getLeft(), pairs.get(i).getRight());
            }
            else {
                answer += nodes.get(i).getData().getName();
            }
        }
        return answer;
    }

    private int getRandNumInMinimumsSet(int left, Node<Symbol> randomNode, int sum) {
        int randNumInSet;List<Integer> minimums = randomNode.getData().getWidths();
        List<Integer> okMinimus = new ArrayList<Integer>();
        for (int min : minimums) {
            if (min <= (left-sum))
                okMinimus.add(min);
        }
        if (okMinimus.size()==0) {
            Collections.sort(minimums);
            okMinimus.add(minimums.get(0));
        }
        randNumInSet = okMinimus.get(randomNumber(okMinimus.size()));
        return randNumInSet;
    }

    private int getRandNumInPermutationsSet(PermutationGenerator pg, HashMap<String, Lists> mapOfNode) {
        int randNumInSet;Iterator it = mapOfNode.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Lists lists = (Lists) pair.getValue();
            pg.addToSet(lists.getMinimums(), lists.getDifferences());
        }
        Set<Integer> setOfPermutations = pg.getSetOfLengths();
        int randOrdInSet = randomNumber(setOfPermutations.size());
        randNumInSet = (Integer) setOfPermutations.toArray()[randOrdInSet];
        return randNumInSet;
    }

    private int getOrdNum(List<Node<Symbol>> nodes, int randNum, int ordNum) {
        for (int i=0;i<nodes.size(); i++) {
            if (nodes.get(i) != null) {
                if (ordNum == randNum && nodes.get(i).getData().isNonterminal()) {
                    nodes.set(i, null); ordNum = i;
                    break;
                }
                if (nodes.get(i).getData().isNonterminal())
                    ordNum++;
            }
        }
        return ordNum;
    }

    private void printList(List<Node<Symbol>> list) {
        for(Node<Symbol> node : list) {
            if (node != null) System.out.println(node.getData().getName());
        }
    }

    public List<Node<Symbol>> getListOfNodes(Node<Symbol> node) {
        List<Node<Symbol>> nonTerminalsChildren = new ArrayList<Node<Symbol>>();
        Pattern patt = Pattern.compile(TERMINALSNONTERMINALS);
        Matcher match = patt.matcher(node.getData().getName());
        while (match.find()) {
            String nonterminalName = "";
            String part = match.group(0);
            Pattern pattern = Pattern.compile(NONTERMINAL);
            Matcher matcher = pattern.matcher(part);
            if (matcher.find()) {
                nonterminalName = matcher.group(1);
            }
            else
                nonterminalName = part;
            Node<Symbol> child = graph.find(nonterminalName, graph.root);
            nonTerminalsChildren.add(child);
        }
        return nonTerminalsChildren;
    }

    private List<Node<Symbol>> getListOfNonterminals(List<Node<Symbol>> list) {
        List<Node<Symbol>> listNonterminals = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> child : list) {
            if (child.getData().isNonterminal()) {
                listNonterminals.add(child);
            }
        }
        return listNonterminals;
    }

    private int getMinimumOfList(List<Node<Symbol>> list) {
        int sum = 0;
        for (Node<Symbol> child : list) {
            if (child != null) {
                Collections.sort(child.getData().getWidths());
                sum += child.getData().getWidths().get(0);
            }
        }
        return sum;
    }

    private int getLenOfChildrenTerminals(Node<Symbol> node) {
        int sum = 0;
        for (Node<Symbol> child : node.getChildren()) {
            if (!child.getData().isNonterminal() && !child.getData().isComposite()) {
                sum += child.getData().getName().length();
            }
        }
        return sum;
    }

    private List<Node<Symbol>> getChildren(Node<Symbol> node, int len) {
        List<Node<Symbol>> list = new ArrayList<Node<Symbol>>();
        for (Node<Symbol> nod : node.getChildren()) {
            if (nod.isRecursive()) {
                for (int width : nod.getData().getWidths()) {
                    if (width <= len) {  // TREBA <=
                        list.add(nod);
                        break;
                    }
                }
            }
            else {
                for (int width : nod.getData().getWidths()) {
                    if ((width >= MIN_LENGTH*len) && (width <= MAX_LENGTH*len)) {
                        list.add(nod);
                        break;
                    }
                }
            }
        }
        if (list.isEmpty()) {
            int minDiff = Math.abs(len), currDiff;
            Node<Symbol> minChild = null;
            for (Node<Symbol> child : node.getChildren()) {
                for (int width : child.getData().getWidths()) {
                    currDiff = Math.abs(width - len);
                    if (currDiff < minDiff) {
                        minDiff = currDiff;
                    }
                }
            }
            for (Node<Symbol> child : node.getChildren()) {
                for (int width : child.getData().getWidths()) {
                    if (Math.abs(width - len) == Math.abs(minDiff)) {
                        list.add(child);
                        break;
                    }
                }
            }
        }
        if (list.isEmpty()) {
            int minDiff = 1000, currDiff;
            Node<Symbol> minChild = null;
            for (Node<Symbol> child : node.getChildren()) {
                for (int width : child.getData().getWidths()) {
                    currDiff = Math.abs(width - len);
                    if (currDiff < minDiff) {
                        minDiff = currDiff;
                    }
                }
            }
            for (Node<Symbol> child : node.getChildren()) {
                for (int width : child.getData().getWidths()) {
                    if (Math.abs(width - len) == Math.abs(minDiff)) {
                        list.add(child);
                        break;
                    }
                }
            }
        }
        if (list.isEmpty()) {
            int num = randomNumber(node.getChildren().size());
            list.add(node.getChildren().get(num));
        }
        return list;
    }

    private int randomNumber(int len) {
        return randGenerator.nextInt(len);
    }

    private Node<Symbol> getRandomNode(List<Node<Symbol>> list) {
        return list.get(randomNumber(list.size()));
    }

}
