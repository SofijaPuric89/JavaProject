package src.correct.java.com.etf.rti.p1;


import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.distribution.ExponentialDistribution;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.*;

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
    boolean once = false;

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

    public void generateIncorrectAnswer(Node<Symbol> node, int len, int sumLen) {
        ExponentialDistribution eg = new ExponentialDistribution(len/2.);
        boolean ok = false;
        while (!ok) {
            correctLen = (int) Math.round(eg.sample());
            if (!(correctLen == 0 || correctLen == sumLen))  {
                ok = true;
            }
        }
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
        if (generateLen >= correctLen && !correct && !once) {  // nekad i ne udje ovde, a kad udje izgenerise pogresan sto je ok...
            once = true;
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
            sort(minimums);
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
                sort(child.getData().getWidths());
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

    public String corruptCorrectAnswer(String correctAnswer) {
        String corruptAnswer = ""; int minDiff = 1000;
        if (once == false) {
            int answerLen = correctAnswer.length(); // zameniti nesto u odgovoru na nekoj poziciji od correctLen do answerLen
            if (correctLen > 0 && correctLen <= answerLen) {
                List<Node<Symbol>> terminals = graph.findAllTerminals();
                List<Pair<Node<Symbol>, Node<Symbol>>> pairs = new ArrayList<Pair<Node<Symbol>, Node<Symbol>>>();   // parovi cvorova terminala koji nemaju sve pretke iste
                for (int i=0; i<terminals.size();i++) {
                    for (int j=i+1; j<terminals.size();j++) {
                       List<Node<Symbol>> parents1 = new ArrayList<Node<Symbol>>();
                        getParentsOfNode(terminals.get(i), parents1);
                        List<Node<Symbol>> parents2 = new ArrayList<Node<Symbol>>();
                        getParentsOfNode(terminals.get(j), parents2);
                        boolean b = areListsTheSame(parents1, parents2);
                        if (numOfDiffParentes(parents1, parents2) < minDiff) {
                            minDiff = numOfDiffParentes(parents1, parents2);
                        }
                        //if (!b) {
                           // pairs.add(new MutablePair<Node<Symbol>, Node<Symbol>>(terminals.get(i), terminals.get(j)));  // ovde nesto pobrljavi...
                        //}
                    }
                }
                for (int i=0; i<terminals.size();i++) {
                    for (int j=i+1; j<terminals.size();j++) {
                        List<Node<Symbol>> parents1 = new ArrayList<Node<Symbol>>();
                        getParentsOfNode(terminals.get(i), parents1);
                        List<Node<Symbol>> parents2 = new ArrayList<Node<Symbol>>();
                        getParentsOfNode(terminals.get(j), parents2);
                        if (numOfDiffParentes(parents1, parents2) == minDiff) {
                            pairs.add(new MutablePair<Node<Symbol>, Node<Symbol>>(terminals.get(i), terminals.get(j)));
                        }
                        //if (!b) {
                        // pairs.add(new MutablePair<Node<Symbol>, Node<Symbol>>(terminals.get(i), terminals.get(j)));  // ovde nesto pobrljavi...
                        //}
                    }
                }
                //int num = randomNumber(pairs.size());   // na pozciji num menjamo ta dva para!
               // List<String> terminals1 = new ArrayList<String>();   // treba proslediti sve, pa koji se nadje orginial (levi deo para) on da se zameni!!!
               // List<String> terminals2 = new ArrayList<String>();
               // getChildrenTerminals(pairs.get(num).getLeft(), terminals1);
               // getChildrenTerminals(pairs.get(num).getRight(), terminals2);
                corruptAnswer = swapTerminals(correctAnswer, pairs, correctLen, answerLen);
            }
        }
        return corruptAnswer;
    }

     String swapTerminals(String correctAnswer, List<Pair<Node<Symbol>, Node<Symbol>>> pairs, int correctLen, int answerLen) {
        String corruptAnswer = correctAnswer;
        for (int i=correctLen; i<=answerLen; i++) {
            for (int num = 0; num < pairs.size(); num++) {
                List<String> terminals1 = new ArrayList<String>();
                List<String> terminals2 = new ArrayList<String>();
                getChildrenTerminals(pairs.get(num).getLeft(), terminals1);
                getChildrenTerminals(pairs.get(num).getRight(), terminals2);
                for (String terminal : terminals1) {
                    if (correctAnswer.substring(i).contains(terminal)) {  // nadjen terminal
                        int index = correctAnswer.indexOf(terminal, i);   // pocinje od i-te pozicije
                        int randNum = randomNumber(terminals2.size());
                        corruptAnswer = correctAnswer.substring(0, i - 1) + terminals2.get(randNum) + correctAnswer.substring(i + terminal.length());
                        break;
                    }
                }
                if (!corruptAnswer.equals(correctAnswer)) {
                    break;
                }
            }
            if (!corruptAnswer.equals(correctAnswer)) {
                break;
            }
        }
        return corruptAnswer;
    }

    private boolean areListsTheSame(List<Node<Symbol>> one, List<Node<Symbol>> two) {
        return one.containsAll(two) && two.containsAll(one);
    }

    private int numOfDiffParentes(List<Node<Symbol>> one, List<Node<Symbol>> two) {
        int num = 0;
        for (int i=0;i<one.size();i++) {
            for (int j=i+1; j<two.size();j++) {
                if (!one.get(i).getData().getName().equals(two.get(j).getData().getName())) {
                    num++;
                }
            }
        }
        return num;
    }

    public void getParentsOfNode(Node<Symbol> node, List<Node<Symbol>> parents) {
        if (node == null)
            return;
        for (Node<Symbol> parent : node.getParents()) {
            if (!(parents.contains(parent))) {
                parents.add(parent);
            }
            getParentsOfNode(parent, parents);
        }
    }

    public void getChildrenTerminals(Node<Symbol> node, List<String> terminals) {
        if (node == null)
            return;
        if (node.getData().isNonterminal() || (node.getData().isComposite())) {
            for (Node<Symbol> child : node.getChildren()) {

                getChildrenTerminals(child, terminals);
            }
        }
        else {
            if (!(terminals.contains(node.getData().getName())) && !node.getData().isNonterminal() && node.getData().isNode()) {
                terminals.add(node.getData().getName());
            }
        }
    }

    private int randomNumber(int len) {
        return randGenerator.nextInt(len);
    }

    private Node<Symbol> getRandomNode(List<Node<Symbol>> list) {
        return list.get(randomNumber(list.size()));
    }

}
