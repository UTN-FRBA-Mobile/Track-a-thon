package com.trackathon.utn.track_a_thon.firebase;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.trackathon.utn.track_a_thon.model.Race;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Firebase {

    private static DatabaseReference races() {
        return FirebaseDatabase.getInstance().getReference().child("races");
    }

    private static DatabaseReference trackers(String race, String runner) {
        return FirebaseDatabase.getInstance().getReference()
                .child("trackers")
                .child(race)
                .child(runner);
    }

    public static void allRaces(Consumer<List<Race>> callback) {
        races().addListenerForSingleValueEvent(new RaceEventListener(callback));
    }

    public static void setNewLocation(String race, String runner, Location newLocation) {
        DatabaseReference runnerRef = trackers(race, runner);
        runnerRef.child("lat").setValue(newLocation.getLatitude());
        runnerRef.child("long").setValue(newLocation.getLongitude());
    }

    public static void registerRunner(String race, String runner) {
        races().child(race).child("runners").child(runner).setValue(1);
    }

    public static void unregisterRunner(String race, String runner) {
        races().child(race).child("runners").child(runner).removeValue();
        trackers(race, runner).removeValue();
    }

    private static class RaceEventListener implements ValueEventListener {

        Consumer<List<Race>> callback;

        private RaceEventListener(Consumer<List<Race>> callback) {
            this.callback = callback;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            HashMap<String, Object> racesUpdate = (HashMap<String, Object>) dataSnapshot.getValue();
            ArrayList<Race> races = new ArrayList<>();
            racesUpdate.forEach((name, values) -> {
                HashMap<String, Object> attributes = (HashMap<String, Object>) values;
                Race newRace = new Race();
                newRace.setName(name);
                HashMap<String, Long> runners = (HashMap<String, Long>) attributes.getOrDefault("runners", new HashMap<>());
                HashMap<String, Long> watchers = (HashMap<String, Long>) attributes.getOrDefault("watchers", new HashMap<>());
                newRace.setRunners((long) runners.values().size());
                newRace.setWatchers((long) watchers.values().size());
                races.add(newRace);
            });

            callback.accept(races);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }
}
