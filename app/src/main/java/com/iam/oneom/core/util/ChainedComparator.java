package com.iam.oneom.core.util;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by iam on 20.05.17.
 */

public class ChainedComparator<T> implements Comparator<T> {

    private List<Comparator<T>> listComparators;

    @SafeVarargs
    public ChainedComparator(Comparator<T>... comparators) {
        this.listComparators = Arrays.asList(comparators);
    }

    @Override
    public int compare(T emp1, T emp2) {
        for (Comparator<T> comparator : listComparators) {
            int result = comparator.compare(emp1, emp2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }
}