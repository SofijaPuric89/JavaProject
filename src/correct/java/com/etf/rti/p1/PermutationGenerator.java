package src.correct.java.com.etf.rti.p1;

import java.util.*;

/**
 * Created by Sofija on 1/29/2016.
 */
public class PermutationGenerator {
    private Set<Integer> setOfLengths = new TreeSet<Integer>();
    private int length;

    public PermutationGenerator(int l) {
        length = l;
    }
    public Set<Integer> getSetOfLengths() {
        return setOfLengths;
    }
    public void addToSet(List<Integer> minimums, List<Integer> differences) {
        int[] diffsCount = new int[differences.size()];
        int[] diffsMaxCount = new int[differences.size()];
        for (int i=0;i<diffsCount.length; i++) {
            diffsMaxCount[i] = length/differences.get(i);
            if (i != 0 && i != diffsCount.length-1)
                diffsMaxCount[i]++;
        }
        for (int min : minimums) {
            setOfLengths.add(min);
            for (int i=0;i<diffsCount.length; i++) {
                diffsCount[i] = 0;
            }
            while (diffsCount.length > 0 && diffsCount[diffsCount.length-1] < diffsMaxCount[diffsCount.length-1]) {
                int j = 0;
                while (j < diffsCount.length) {
                    if (diffsCount[j] > diffsMaxCount[j])
                        break;
                    int sum = min;
                    /*
                    for (int i=0;i<diffsCount.length;i++) {
                        System.out.print(diffsCount[i] + " ");
                    }
                    System.out.println();
                    */
                    for (int k = 0; k <diffsCount.length; k++) {
                        sum += diffsCount[k]*differences.get(k);
                    }
                    if (sum <= length) {
                        setOfLengths.add(sum);
                    }
                    //diffsCount[j]++;
                    if (diffsCount[j] == diffsMaxCount[j]) {
                        if (j == diffsCount.length-1) {
                            j = 0;
                            continue;
                        }
                        diffsCount[j] = 0;
                        j++;
                        diffsCount[j]++;

                    }
                    else {
                        if (j == 0)
                            diffsCount[j]++;
                        else
                            j = 0;
                    }

                }
            }
        }
    }

    public static void main(String[] args) {
        PermutationGenerator pg = new PermutationGenerator(30);
        List<Integer> mins = new ArrayList<Integer>();
        mins.add(1); //mins.add(3); mins.add(5);
        List<Integer> diffs = new ArrayList<Integer>();
      //  diffs.add(0); diffs.add(5);
        pg.addToSet(mins, diffs);
        System.out.println(pg.getSetOfLengths());
    }
}