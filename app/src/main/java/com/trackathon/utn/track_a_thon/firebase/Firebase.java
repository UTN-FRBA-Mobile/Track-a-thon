package com.trackathon.utn.track_a_thon.firebase;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class Firebase {


    public static void raceUpdates(String race, Consumer<Runner> callback) {
        runnersRef(race).addChildEventListener(new RunnerEventListener(callback));
    }

    public static void allRaces(Consumer<HashMap<String, Race>> callback) {
        racesRef().addListenerForSingleValueEvent(new RaceEventListener(callback));
    }

    public static void setNewLocation(String race, String runner, Location newLocation) {
        DatabaseReference runnerRef = runnerRef(race, runner);
        runnerRef.child("latitude").setValue(newLocation.getLatitude());
        runnerRef.child("longitude").setValue(newLocation.getLongitude());
    }

    public static void registerRunner(String race, String runner) {
        racesRef().child(race).child("runners").child(runner).setValue(1);
    }

    public static void unregisterRunner(String race, String runner) {
        racesRef().child(race).child("runners").child(runner).removeValue();
        runnerRef(race, runner).removeValue();
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

    private static class RaceEventListener implements ValueEventListener {

        Consumer<HashMap<String, Race>> callback;

        private RaceEventListener(Consumer<HashMap<String, Race>> callback) {
            this.callback = callback;
        }


        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<HashMap<String, Race>> type = new GenericTypeIndicator<HashMap<String, Race>>() {};
            HashMap<String, Race> races = dataSnapshot.getValue(type);
            callback.accept(races);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }

    private static class RunnerEventListener implements ChildEventListener{

        private Consumer<Runner> callback;

        private RunnerEventListener(Consumer<Runner> callback) {
            this.callback = callback;
        }

        private void update (DataSnapshot dataSnapshot) {
            try {
                String name = dataSnapshot.getKey();
                HashMap<String, Double> hash = (HashMap<String, Double>) dataSnapshot.getValue();
                LatLng loc = new LatLng(hash.get("latitude"), hash.get("longitude"));
                callback.accept(new Runner());
            } catch (NullPointerException e) {
                // FIXME
            }
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String name) {
            update(dataSnapshot);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String name) {
            update(dataSnapshot);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    }


}
