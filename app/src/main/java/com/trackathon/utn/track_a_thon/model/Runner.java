package com.trackathon.utn.track_a_thon.model;

import android.location.Location;

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

    public static Runner from(Location location) {
        User user = User.getCurrentUser();
        Runner runner = new Runner();
        runner.setLocation(RunnerLocation.from(location));
        runner.setName(user.getName());
        runner.setEmail(user.getEmail());
        runner.setImageUrl(user.getImageUrl());
        return runner;
    }
}
