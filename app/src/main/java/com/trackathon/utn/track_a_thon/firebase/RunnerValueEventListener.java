package com.trackathon.utn.track_a_thon.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;
import java.util.function.Consumer;

class RunnerValueEventListener implements ValueEventListener {
    private Consumer<HashMap<String, Runner>> callback;

    RunnerValueEventListener(Consumer<HashMap<String, Runner>> callback) {
        this.callback = callback;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        GenericTypeIndicator<HashMap<String, Runner>> type = new GenericTypeIndicator<HashMap<String, Runner>>() {};
        HashMap<String, Runner> runners = dataSnapshot.getValue(type);
        callback.accept(runners);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}