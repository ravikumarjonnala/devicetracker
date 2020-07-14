package com.vodafone.devicetracker.dataobjects;

import java.util.Date;
import java.util.Iterator;

import com.vodafone.devicetracker.util.BusinessRules;
import com.vodafone.devicetracker.util.StringConstants;

/**
 * A tracker device specialized for cycles. This cycle plus tracker has its' own
 * activeness indicator based on the past events.
 *
 */
public class CyclePlusTracker extends GeneralTracker {

    public CyclePlusTracker(String productId) {
        super(productId);
    }

    @Override
    public String getName() {
        return BusinessRules.isCycleTracker(getProductId()) ? StringConstants.CYCLE_PLUS_TRACKER : super.getName();
    }

    @Override
    public String getActiveIndicator(Integer eventId) {
        if (eventIdDateMap.size() < 3) {
            return StringConstants.STATUS_NA;
        }

        Date date1 = eventIdDateMap.get(eventId);
        Date date2 = null;
        Date date3 = null;

        Iterator<Date> it = timeStatusMap.keySet().iterator();
        while (it.hasNext()) {
            Date date = it.next();
            if (date1.equals(date)) {
                break;
            }
        }
        if (it.hasNext()) {
            date2 = it.next();
        }
        if (it.hasNext()) {
            date3 = it.next();
        }

        if (date2 != null && date3 != null) {
            if (timeStatusMap.get(date1).getDeviceLocation().equals(timeStatusMap.get(date2).getDeviceLocation())
                    && timeStatusMap.get(date1).getDeviceLocation()
                            .equals(timeStatusMap.get(date3).getDeviceLocation())) {
                return StringConstants.STATUS_INACTIVE;
            }
        }

        return super.getActiveIndicator(eventId);
    }
}
