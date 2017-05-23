package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.trackathon.utn.track_a_thon.firebase.Firebase;


public class RacesActivity extends AppCompatActivity {

    RecyclerView racesWatchers;
    Class<? extends FragmentActivity> nextActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);
        setComponentsBinding();
        nextActivity = getIntent().getExtras().getBoolean(TrackatonConstant.IS_TRACKER) ? TrackingActivity.class : MapsActivity.class;
    }

    private void setComponentsBinding() {
        LinearLayoutManager racesLayout = new LinearLayoutManager(getApplicationContext());

        racesWatchers = (RecyclerView)findViewById(R.id.races);
        racesWatchers.setHasFixedSize(true);
        racesWatchers.setLayoutManager(racesLayout);

        Firebase.allRaces(races -> {
            RecycleViewRaceAdapter adapter = new RecycleViewRaceAdapter(races, (raceId, race) -> {
                Intent intent = new Intent(RacesActivity.this, nextActivity);
                intent.putExtra(TrackatonConstant.RACE_ID, raceId);
                intent.putExtra(TrackatonConstant.RACE_NAME, race.getName());
                startActivity(intent);
            });
            racesWatchers.setAdapter(adapter);
        });
    }

}
