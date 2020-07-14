package com.vodafone.devicetracker.dataobjects;

import java.util.Date;

/**
 * Basic interface for at racker device.
 */
public interface Tracker {

    /**
     * Returns the product id for this product.
     */
    public String getProductId();

    /**
     * Adds a device status to this device.
     */
    public void addStatus(DeviceStatus deviceStatus);

    /**
     * Returns the name of this device.
     */
    public String getName();

    /**
     * Returns the most recent event Id.
     */
    public Integer getLastEventId();

    /**
     * Returns the device status for the given eventId.
     */
    public DeviceStatus getDeviceStatus(Integer eventId);

    /**
     * Returns the device status closest to the given <code>dateTime</code> in the
     * past.
     */
    public DeviceStatus getLastKnownStatus(Date dateTime);

    /**
     * Returns an indication for 'activeness'.
     */
    public String getActiveIndicator(Integer eventId);

}
