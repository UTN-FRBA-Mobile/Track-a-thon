package com.trackathon.utn.track_a_thon;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Button;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.GPSLocation;
import com.trackathon.utn.track_a_thon.model.Race;

import java.util.ArrayList;
import java.util.List;


public class NewRaceActivity extends AppCompatActivity {

    MapFragment mapFragment = new MapFragment();
    List<GPSLocation> points = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_race);

        Button btnCreate = (Button) findViewById(R.id.ButtonCreate);
        Button btnMap = (Button) findViewById(R.id.ButtonMap);

        EditText nameField = (EditText) findViewById(R.id.EditTextName);
        EditText distanceField = (EditText) findViewById(R.id.EditTextDistance);
        EditText locationField = (EditText) findViewById(R.id.EditTextLocation);
        EditText startTimeField = (EditText) findViewById(R.id.EditTextStartTime);


        //Listener on Submit button
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.overlay_map_container, mapFragment);
                fragmentTransaction.commit();
            }
        });

        //Listener on Submit button
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String distance = distanceField.getText().toString() + "km";
                String location = locationField.getText().toString();
                String startTime = startTimeField.getText().toString();
                Race race = new Race(name, location, distance, startTime, points);

                Firebase.newRace(race);
            }
        });
    }

    public void setPoints(List<GPSLocation> points) {
        this.points = points;
    }

    public void removeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(mapFragment);
        fragmentTransaction.commit();
    }


}
