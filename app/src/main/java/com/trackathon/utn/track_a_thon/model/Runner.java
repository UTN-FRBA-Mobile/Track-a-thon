package com.trackathon.utn.track_a_thon.model;

/**
 * Created by federico on 5/15/17.
 */

public class Runner {

    private String name;
    private Location location;

    public Runner() {}

    public String getName() {
        return name;
    }
    public Location getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setocation(Location location) {
        this.location = location;
    }

}
