package src.correct.java.com.etf.rti.p1;


import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
    static {
        Calendar c = Calendar.getInstance();
        randGenerator.setSeed(c.getTimeInMillis());
    }

    public GenerateAnswer(Graph g, boolean c) {
        graph = g; correct = c;
    }
    public void generate(int l) {
        length = l;

    }

    public String generateAnswerForNode(Node<Symbol> node, int len) {
        // faza 1 i faza 2: slucajno odabiramo dete koje ima duzinu najpriblizniju len iz liste dece koji imaju pribliznu duzinu len
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
                int randNum = randomNumber(nonterminals.size());
                Node<Symbol> randomNode = nonterminals.get(randNum);
                nonterminals.remove(randNum);
                int ordNum = 0;
                ordNum = getOrdNum(nodes, randNum, ordNum);
                int sum = getMinimumOfList(nonterminals);
                PermutationGenerator pg = new PermutationGenerator(left-sum);
                HashMap<String, Lists> mapOfNode = randomNode.getData().getDifferenceLen();
                int randNumInSet = 0;
                if (!mapOfNode.isEmpty()) {
                    randNumInSet = getRandNumInPermutationsSet(pg, mapOfNode);
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
                return randNode.getData().getName();
            }
            else {
                return generateAnswerForNode(randNode, len);
            }
        }
    }

    private String generateAnswerForCompositeNode(List<Node<Symbol>> nodes, List<Pair<Node<Symbol>, Integer>> pairs) {
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
            int minDiff = len, currDiff;
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
                    if (Math.abs(width - len) == minDiff) {
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
