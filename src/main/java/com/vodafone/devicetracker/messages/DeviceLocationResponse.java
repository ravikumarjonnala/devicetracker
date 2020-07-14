package com.vodafone.devicetracker.messages;

/**
 * Response object for product retrieval requests.
 */
public class DeviceLocationResponse {
    public static final String DEVICE_COULD_NOT_BE_LOCATED = "ERROR: Device could not be located";
    public static final String TECHNICAL_ERROR = "ERROR: A technical exception occurred";
    public static final String ID_NOT_FOUND = "ERROR: Id %s not found";
    public static final String LOCATION_IDENTIFIED = "SUCCESS: Location identified";
    public static final String AIRPLANE_MODE_ON_LOCATION_UNAVAILABLE = "SUCCESS: Location not available: Plase turn off aiplane mode";
}
