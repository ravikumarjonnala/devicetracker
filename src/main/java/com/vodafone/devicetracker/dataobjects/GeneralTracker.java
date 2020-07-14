package com.vodafone.devicetracker.dataobjects;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.vodafone.devicetracker.util.Comparators;
import com.vodafone.devicetracker.util.StringConstants;

/**
 * Represents a device.
 */
public class GeneralTracker implements Tracker {
    protected String productId;
    protected Map<Date, DeviceStatus> timeStatusMap = new TreeMap<>(Comparators.REVERSE_DATE_TIME_ORDERING);
    protected Map<Integer, Date> eventIdDateMap = new TreeMap<>();
    protected Integer lastEventId;

    public GeneralTracker(String productId) {
        this.productId = productId;
    }

    @Override
    public void addStatus(DeviceStatus deviceStatus) {
        timeStatusMap.put(deviceStatus.getDateTime(), deviceStatus);
        eventIdDateMap.put(deviceStatus.getEventId(), deviceStatus.getDateTime());
        lastEventId = deviceStatus.getEventId();
    }

    @Override
    public String getName() {
        return productId.startsWith(StringConstants.SIXTY_NINE) ? StringConstants.GENERAL_TRACKER
                : StringConstants.UNKNOWN;
    }

    @Override
    public String getProductId() {
        return productId;
    }

    @Override
    public Integer getLastEventId() {
        return lastEventId;
    }

    @Override
    public DeviceStatus getLastKnownStatus(Date dateTime) {
        Iterator<Date> it = timeStatusMap.keySet().iterator();
        while (it.hasNext()) {
            Date key = it.next();
            if (key.after(dateTime)) {
                continue;
            }
            return timeStatusMap.get(key);
        }

        return null;
    }

    @Override
    public DeviceStatus getDeviceStatus(Integer eventId) {
        return eventIdDateMap.get(eventId) != null ? timeStatusMap.get(eventIdDateMap.get(eventId)) : null;
    }

    @Override
    public String getActiveIndicator(Integer eventId) {
        DeviceStatus deviceStatus = getDeviceStatus(eventId);
        if (deviceStatus == null) {
            return StringConstants.EMPTY_STRING;
        }

        if (deviceStatus.getAirplaneMode().booleanValue()) {
            return StringConstants.STATUS_INACTIVE;
        }

        return StringConstants.STATUS_ACTIVE;

    }
}
