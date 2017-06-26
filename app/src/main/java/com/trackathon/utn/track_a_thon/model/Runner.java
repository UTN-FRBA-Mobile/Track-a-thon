package com.trackathon.utn.track_a_thon.model;

public class Runner extends User {

    static final long serialVersionUID = 1L;

    private RunnerLocation location;

    public Runner() {}

    public RunnerLocation getLocation() {
        return location;
    }

    public void setLocation(RunnerLocation location) {
        this.location = location;
    }

}
