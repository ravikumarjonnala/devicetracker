package com.vodafone.devicetracker.dataobjects.util;

import java.text.DecimalFormat;

import com.vodafone.devicetracker.dataobjects.DeviceStatus;
import com.vodafone.devicetracker.dataobjects.SuccessTrackerVO;
import com.vodafone.devicetracker.dataobjects.Tracker;
import com.vodafone.devicetracker.messages.DeviceLocationResponse;
import com.vodafone.devicetracker.util.StringConstants;

public class VOConverter {
    private static final DecimalFormat df = new DecimalFormat("##0.0#####");

    public static SuccessTrackerVO convert(Tracker tracker, DeviceStatus deviceStatus) {
        SuccessTrackerVO vo = new SuccessTrackerVO();
        vo.setId(tracker.getProductId());
        vo.setName(tracker.getName());
        vo.setDatetime(deviceStatus.getDateTime());
        vo.setStatus(tracker.getActiveIndicator(deviceStatus.getEventId()));
        if (deviceStatus.getAirplaneMode().booleanValue()) {
            vo.setLatitude(StringConstants.EMPTY_STRING);
            vo.setLongitude(StringConstants.EMPTY_STRING);
            vo.setDescription(DeviceLocationResponse.AIRPLANE_MODE_ON_LOCATION_UNAVAILABLE);
        } else {
            vo.setLatitude(formatCoordinate(deviceStatus.getDeviceLocation().getLatitude()));
            vo.setLongitude(formatCoordinate(deviceStatus.getDeviceLocation().getLongitude()));
            vo.setDescription(DeviceLocationResponse.LOCATION_IDENTIFIED);
        }
        vo.setBattery(getBatteryDescription(deviceStatus.getBatteryPercentage()));

        return vo;
    }

    private static String getBatteryDescription(Integer batteryPercentage) {
        if (batteryPercentage >= 98)
            return StringConstants.BATTERY_FULL;
        if (batteryPercentage >= 60)
            return StringConstants.BATTERY_HIGH;
        if (batteryPercentage >= 40)
            return StringConstants.BATTERY_MEDIUM;
        if (batteryPercentage >= 10)
            return StringConstants.BATTERY_LOW;
        return StringConstants.BATTERY_CRITICAL;
    }

    private static String formatCoordinate(Double ll) {
        if (ll.isNaN()) {
            return StringConstants.EMPTY_STRING;
        }
        return df.format(ll);
    }
}
