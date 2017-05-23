package com.trackathon.utn.track_a_thon.firebase;

import android.location.Location;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Firebase {


    public static void allRaces(Consumer<HashMap<String, Race>> callback) {
        racesRef().addListenerForSingleValueEvent(new RaceEventListener(callback));
    }

    public static void setNewLocation(String race, String runner, Location newLocation) {
        DatabaseReference runnerRef = runnerRef(race, runner);
        runnerRef.child("latitude").setValue(newLocation.getLatitude());
        runnerRef.child("longitude").setValue(newLocation.getLongitude());
    }

    public static void registerRunner(String race, String runner) {
        racesRef().child(race).child("runners").child(runner).push();
    }

    public static void unregisterRunner(String race, String runner) {
        racesRef().child(race).child("runners").child(runner).removeValue();
        runnerRef(race, runner).removeValue();
    }

    public static void raceUpdates(String race, BiConsumer<String, Runner> callback) {
        runnersRef(race).addChildEventListener(new RunnerEventListener(callback));
    }


    private static DatabaseReference racesRef() {
        return FirebaseDatabase.getInstance().getReference().child("races");
    }

    private static DatabaseReference raceRef(String raceId) {
        return racesRef().child(raceId);
    }

    private static DatabaseReference runnersRef(String raceId) {
        return raceRef(raceId).child("runners");
    }

    private static DatabaseReference runnerRef(String raceId, String runner) {
        return runnersRef(raceId).child(runner);
    }

}
