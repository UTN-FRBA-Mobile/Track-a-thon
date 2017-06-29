package com.trackathon.utn.track_a_thon.firebase;

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

    public static void allRunners(String raceId, Consumer<HashMap<String, Runner>> callback) {
        runnersRef(raceId).addValueEventListener(new RunnerValueEventListener(callback));
    }

    public static void setRunner(String raceId, String runnerId, Runner runner) {
        runnerRef(raceId, runnerId).setValue(runner);
    }

    public static String registerRunner(String raceId) {
        return runnersRef(raceId).push().getKey();
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
