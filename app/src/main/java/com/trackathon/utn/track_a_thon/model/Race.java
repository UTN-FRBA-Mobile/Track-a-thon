package com.trackathon.utn.track_a_thon.model;

import java.util.List;

public class Race {

    private String name;
    private List<String> watchers;
    private List<Runner> runners;

    public Race() {}

    public String getName() {
        return name;
    }
    public List<Runner> getRunners() {
        return runners;
    }
    public List<String> getWatchers() {
        return watchers;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setRunners(List<Runner> runners) {
        this.runners = runners;
    }
    public void setWatchers(List<String> watchers) {
        this.watchers = watchers;
    }

}
