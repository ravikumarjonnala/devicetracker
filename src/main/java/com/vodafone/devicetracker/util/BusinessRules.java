package com.vodafone.devicetracker.util;

import java.util.Objects;

public class BusinessRules {

    /**
     * Identifies if a tracker device identified by the provided productId is a
     * cycle tracker.
     * <p>
     * <b>RULE:</b> If the productId begins with the letters WG, the device is a
     * cycle tracker.
     * <p>
     * Throws NullPointerException if productId is null.
     * 
     * @param productId id of the tracker
     * @return true, if the tracker is a cycle tracker
     */
    public static boolean isCycleTracker(String productId) {
        Objects.requireNonNull(productId);
        return productId.startsWith(StringConstants.WG);
    }

}
