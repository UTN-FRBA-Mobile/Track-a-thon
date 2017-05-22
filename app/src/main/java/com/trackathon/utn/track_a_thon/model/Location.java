package com.trackathon.utn.track_a_thon.model;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private Double latitude;

    private Double longitude;

    public Location() {
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
}
