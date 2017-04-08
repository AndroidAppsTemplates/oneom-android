package com.iam.oneom.core.util;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by iam on 08.04.17.
 */

public class DateDescendingOrderComparator implements Comparator<Date> {


    @Override
    public int compare(Date o1, Date o2) {
        return o2.compareTo(o1);
    }
}
