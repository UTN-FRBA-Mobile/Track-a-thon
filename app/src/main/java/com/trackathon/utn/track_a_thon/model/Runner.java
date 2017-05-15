package com.trackathon.utn.track_a_thon.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by federico on 5/15/17.
 */

public class Runner {

    private final String name;
    private final LatLng location;

    public Runner(String name, LatLng location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "Runner{" +
                "name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
