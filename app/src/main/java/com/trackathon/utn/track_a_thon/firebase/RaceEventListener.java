package com.trackathon.utn.track_a_thon.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.trackathon.utn.track_a_thon.model.Race;

import java.util.HashMap;
import java.util.function.Consumer;

class RaceEventListener implements ValueEventListener {

    private Consumer<HashMap<String, Race>> callback;

    RaceEventListener(Consumer<HashMap<String, Race>> callback) {
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
