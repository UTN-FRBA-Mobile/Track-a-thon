package com.trackathon.utn.track_a_thon.model;

import android.location.Location;

public class Runner extends User {

    static final long serialVersionUID = 1L;
    private Float accumulatedDistance;
    private Float currentSpeed;
    private Float maxSpeed;

    private GPSLocation location;

    public static Runner from(Location location, Float accumulatedDistance, Float currentSpeed, Float maxSpeed) {
        User user = User.getCurrentUser();
        Runner runner = new Runner();
        runner.setLocation(GPSLocation.from(location));
        runner.setAccumulatedDistance(accumulatedDistance);
        runner.setCurrentSpeed(currentSpeed);
        runner.setMaxSpeed(maxSpeed);
        runner.setName(user.getName());
        runner.setEmail(user.getEmail());
        runner.setImageUrl(user.getImageUrl());
        return runner;
    }

    public Runner() {
    }

    public GPSLocation getLocation() {
        return location;
    }

    public void setLocation(GPSLocation location) {
        this.location = location;
    }

    public Float getAccumulatedDistance() {
        return accumulatedDistance;
    }

    public void setAccumulatedDistance(Float accumulatedDistance) {
        this.accumulatedDistance = accumulatedDistance;
    }

    public Float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(Float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

}
