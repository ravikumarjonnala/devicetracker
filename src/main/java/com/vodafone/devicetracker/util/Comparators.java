package com.vodafone.devicetracker.util;

import java.util.Comparator;
import java.util.Date;

/**
 * Reusable comparators.
 */
public class Comparators {
    /**
     * Useful for sorting {@link java.util.Date Date} objects in reverse
     * chronological order.
     */
    public static final Comparator<Date> REVERSE_DATE_TIME_ORDERING = (d1, d2) -> {
        if (d1.after(d2)) {
            return -1;
        }

        if (d2.after(d1)) {
            return 1;

        }

        return 0;
    };
}
