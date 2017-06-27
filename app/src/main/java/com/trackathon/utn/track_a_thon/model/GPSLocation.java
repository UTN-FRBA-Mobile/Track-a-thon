package com.trackathon.utn.track_a_thon.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class GPSLocation implements Serializable {

    static final long serialVersionUID = 3L;

    private Double latitude;

    private Double longitude;

    public GPSLocation() {
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(getLatitude(), getLongitude());
    }

    public static GPSLocation from(Location location) {
        GPSLocation gpsLocation = new GPSLocation();
        gpsLocation.setLatitude(location.getLatitude());
        gpsLocation.setLongitude(location.getLongitude());
        return gpsLocation;
    }
}
