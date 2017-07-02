package com.trackathon.utn.track_a_thon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Race implements Serializable {

    static final long serialVersionUID = 2L;

    private String name;
    private String location;
    private String startTime;
    private String distance;
    private List<GPSLocation> points;
    private List<String> watchers;
    private HashMap<String, Runner> runners;

    public Race() {
    }

    public Race(String name, String location, String distance, String startTime) {
        this.name = name;
        this.location = location;
        this.distance = distance;
        this.startTime = startTime;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getDistance() {
        return distance;
    }

    public String getStartTime() {
        return startTime;
    }

    public List<String> getWatchers() {
        if (watchers == null) {
            watchers = new ArrayList<>();
        }
        return watchers;
    }

    public List<GPSLocation> getPoints() {
        return points;
    }

    public HashMap<String, Runner> getRunners() {
        if (runners == null) {
            runners = new HashMap<>();
        }
        return runners;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setWatchers(List<String> watchers) {
        this.watchers = watchers;
    }

    public void setPoints(List<GPSLocation> points) {
        this.points = points;
    }

    public void setRunners(HashMap<String, Runner> runners) {
        this.runners = runners;
    }
}
