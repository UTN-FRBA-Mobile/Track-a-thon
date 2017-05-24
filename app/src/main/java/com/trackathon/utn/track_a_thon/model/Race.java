package com.trackathon.utn.track_a_thon.model;

import java.util.HashMap;
import java.util.List;

public class Race {

    private String name;
    private String location;
    private String startTime;
    private String distance;
    private List<String> watchers;
    private HashMap<String, Runner> runners;

    public Race() {}

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getDistance() {
        return distance;
    }

    public List<String> getWatchers() {
        return watchers;
    }
    public HashMap<String, Runner>  getRunners() {
        return runners;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLocation(String location) { this.location = location; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public void setDistance(String distance) { this.distance = distance; }
    public void setWatchers(List<String> watchers) {
        this.watchers = watchers;
    }
    public void setRunners(HashMap<String, Runner>  runners) {
        this.runners = runners;
    }

}
