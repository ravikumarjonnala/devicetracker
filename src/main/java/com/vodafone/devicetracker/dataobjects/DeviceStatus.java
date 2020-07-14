package com.vodafone.devicetracker.dataobjects;

import java.util.Date;

import com.vodafone.devicetracker.util.StringConstants;

public class DeviceStatus {
    private Integer eventId;
    private Date dateTime;
    private DeviceLocation deviceLocation;
    private Integer batteryPercentage;
    private String light;
    private Boolean airplaneMode;

    public DeviceStatus(Integer eventId, Date dateTime, DeviceLocation deviceLocation, Integer batteryPercentage,
            String light, Boolean airplaneMode) {
        super();
        this.eventId = eventId;
        this.dateTime = dateTime;
        this.deviceLocation = deviceLocation;
        this.batteryPercentage = batteryPercentage;
        this.light = light;
        this.airplaneMode = airplaneMode;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public DeviceLocation getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(DeviceLocation deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public Integer getBatteryPercentage() {
        return batteryPercentage;
    }

    public void setBatteryPercentage(Integer batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public Boolean getAirplaneMode() {
        return airplaneMode;
    }

    public void setAirplaneMode(Boolean airplaneMode) {
        this.airplaneMode = airplaneMode;
    }

    public static Boolean determineAirplaneMode(String airplaneMode) {
        return Boolean.valueOf(airplaneMode.equalsIgnoreCase(StringConstants.ON));
    }
}
