package com.trackathon.utn.track_a_thon.model;

public class Runner extends User {

    private RunnerLocation location;

    public Runner() {}

    public RunnerLocation getLocation() {
        return location;
    }

    public void setLocation(RunnerLocation location) {
        this.location = location;
    }

}
