package com.trackathon.utn.track_a_thon.firebase;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class RunnerEventListener implements ChildEventListener {
    private BiConsumer<String, Runner> callback;

    RunnerEventListener(BiConsumer<String, Runner> callback) {
        this.callback = callback;
    }

    private void update (DataSnapshot dataSnapshot) {
        Runner runner = dataSnapshot.getValue(Runner.class);
        callback.accept(dataSnapshot.getKey(), runner);
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
