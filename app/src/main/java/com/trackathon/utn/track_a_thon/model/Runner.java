package com.trackathon.utn.track_a_thon.model;

public class Runner {

    private String name;
    private RunnerLocation location;

    public Runner() {}

    public String getName() {
        return name;
    }
    public RunnerLocation getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(RunnerLocation location) {
        this.location = location;
    }

}
