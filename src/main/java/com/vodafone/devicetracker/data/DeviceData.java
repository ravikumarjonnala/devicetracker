package com.vodafone.devicetracker.data;

import java.util.Map;
import java.util.TreeMap;

import com.vodafone.devicetracker.dataobjects.Tracker;

/**
 * Holds the devices information. Devices from the input dataset are tabulated
 * here.
 */
public class DeviceData {
    private Map<String, Tracker> productIdTrackerMap = new TreeMap<>();

    public void addDevice(String productId, Tracker tracker) {
        productIdTrackerMap.put(productId, tracker);
    }

    public void removeDevice(String productId) {
        productIdTrackerMap.remove(productId);
    }

    public void reset() {
        productIdTrackerMap.clear();
    }

    public Tracker getDevice(String productId) {
        return productIdTrackerMap.get(productId);
    }
}
