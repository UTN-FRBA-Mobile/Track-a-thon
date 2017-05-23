package com.trackathon.utn.track_a_thon.model;

import java.util.HashMap;
import java.util.List;

public class Race {

    private String name;
    private List<String> watchers;
    private HashMap<String, Runner> runners;

    public Race() {}

    public String getName() {
        return name;
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
    public void setWatchers(List<String> watchers) {
        this.watchers = watchers;
    }
    public void setRunners(HashMap<String, Runner>  runners) {
        this.runners = runners;
    }

}
