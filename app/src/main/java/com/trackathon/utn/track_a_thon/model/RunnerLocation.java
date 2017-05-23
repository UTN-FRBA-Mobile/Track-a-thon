package com.trackathon.utn.track_a_thon.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class RunnerLocation {
    private Double latitude;

    private Double longitude;

    public RunnerLocation() {
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

    public static RunnerLocation from(Location location) {
        RunnerLocation runnerLocation = new RunnerLocation();
        runnerLocation.setLatitude(location.getLatitude());
        runnerLocation.setLongitude(location.getLongitude());
        return runnerLocation;
    }
}
