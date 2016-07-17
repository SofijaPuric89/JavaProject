package com.etf.rti.p1.translator.graph;

import com.etf.rti.p1.translator.BNFGrammarToGraphTranslator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Korisnik on 20.1.2016.
 */
public class CombinationGenerator {

    private BNFGrammarToGraphTranslator BNFGrammarToGraphTranslator;

    public CombinationGenerator(BNFGrammarToGraphTranslator grammar) {
        BNFGrammarToGraphTranslator = grammar;
    }

    public List<Integer> calculateWidthOfCompositeNode(Node<Symbol> parent, boolean widhts) {
        int sumTerminals = 0;
        int numOfChildrenNonterminals = 0;
        List<String> nameNonterminals = new ArrayList<String>();
        for (Node<Symbol> nod : parent.getChildren()) {
            if (nod.getData().isNonterminal()) {
                numOfChildrenNonterminals++;
                nameNonterminals.add(nod.getData().getName());
            } else {
                sumTerminals += nod.getData().getName().length();
            }
        }
        int[][] array = createMatrixOfChildrenWidths(numOfChildrenNonterminals, nameNonterminals);
        //    if (array.length == numOfChildrenNonterminals) { PROMENJENO U SLEDECE:
        if (widhts) {
            if (!ifMatrixHasEmptyArray(array)) {
                int s = 0;
                List<Integer> all = callGetAllCombinations(sumTerminals, array, s);
                BNFGrammarToGraphTranslator.getGrammarGraph().updateWidths(parent, all);

            }
            return null;
        } else {
            int s = 0;
            List<Integer> all = callGetAllCombinations(sumTerminals, array, s);
            return all;
        }
    }

    public boolean ifMatrixHasEmptyArray(int[][] matrix) {
        boolean b = false;
        for (int[] array : matrix) {
            if (array.length == 0) {
                b = true;
            }
        }
        return b;
    }

    public List<Integer> callGetAllCombinations(int sumTerminals, int[][] array, int s) {
        List<Integer> partial = new ArrayList<Integer>();
        List<Integer> all = new ArrayList<Integer>();
        getAllCombinations(array, partial, all, s);
        for (int i = 0; i < all.size(); i++) {
            all.set(i, all.get(i) + sumTerminals);
        }
        return all;
    }

    public int[][] createMatrixOfChildrenWidths(int numOfChildrenNonterminals, List<String> nameNonterminals) {
        int[][] array = new int[numOfChildrenNonterminals][];
        for (int i = 0; i < numOfChildrenNonterminals; i++) {
            Node<Symbol> nonterminal = BNFGrammarToGraphTranslator.getGrammarGraph().find(nameNonterminals.get(i), BNFGrammarToGraphTranslator.getGrammarGraph().getRoot());
            int[] lens = new int[nonterminal.getData().getWidths().size()];
            int j = 0;
            for (int width : nonterminal.getData().getWidths()) {
                lens[j++] = width;
            }
            array[i] = lens;
        }
        return array;
    }


    private void getAllCombinations(int[][] data, List<Integer> partial, List<Integer> all, int s) {
        if (partial.size() == data.length) {
            for (int i : partial) {
                if (i != -1) {
                    s += i;
                }
            }
            all.add(s);
            s = 0;
            return;
        }
        if (data[partial.size()].length == 0) {
            partial.add(-1);
            getAllCombinations(data, partial, all, s);
            partial.remove(partial.size() - 1);
            return;
        }
        for (int i = 0; i != data[partial.size()].length; i++) {
            partial.add(data[partial.size()][i]);
            getAllCombinations(data, partial, all, s);
            partial.remove(partial.size() - 1);
        }
    }

    public void catchAllCombinations(int[][] data, List<Integer> partial, List<List<Integer>> all) {
        if (partial.size() == data.length) {
            List<Integer> list = new ArrayList<Integer>();
            for (int i : partial) {
                if (i != -1) {
                    list.add(i);
                }
            }
            all.add(list);
            return;
        }
        if (data[partial.size()].length == 0) {
            partial.add(-1);
            catchAllCombinations(data, partial, all);
            partial.remove(partial.size() - 1);
            return;
        }
        for (int i = 0; i != data[partial.size()].length; i++) {
            partial.add(data[partial.size()][i]);
            catchAllCombinations(data, partial, all);
            partial.remove(partial.size() - 1);
        }
    }
}
