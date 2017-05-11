package com.trackathon.utn.track_a_thon.model;

public class Race {

    String name;
    Long watchers;
    Long runners;

    public Race() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWatchers(Long watchers) {
        this.watchers = watchers;
    }

    public void setRunners(Long runners) {
        this.runners = runners;
    }

    @Override
    public String toString() {
        return "Race{" +
                "name='" + name + '\'' +
                ", watchers=" + watchers +
                ", runners=" + runners +
                '}';
    }
}
