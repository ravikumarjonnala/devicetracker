package com.vodafone.devicetracker.dataobjects;

import java.util.Objects;

import com.vodafone.devicetracker.util.Utils;

public class DeviceLocation {
    private Double latitude = Double.NaN;
    private Double longitude = Double.NaN;

    public DeviceLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public boolean isLocationAvailable() {
        return !latitude.isNaN() && !longitude.isNaN();
    }

    @Override
    public boolean equals(Object another) {
        if (another == null) {
            return false;
        }

        if (!(another instanceof DeviceLocation)) {
            return false;
        }

        DeviceLocation other = (DeviceLocation) another;
        return areEquivalentDoubles(this.latitude, other.latitude)
                && areEquivalentDoubles(this.longitude, other.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    private boolean areEquivalentDoubles(Double d1, Double d2) {
        if (d1 == null && d2 == null) {
            return true;
        }

        if (Utils.isOnlyOneObjectNull(d1, d2)) {
            return false;
        }

        if (d1.isNaN() && d2.isNaN()) {
            return true;
        }

        if ((d1.isNaN() && !d2.isNaN()) || (!d1.isNaN() && d2.isNaN())) {
            return false;
        }

        return d1.equals(d2);
    }
}
