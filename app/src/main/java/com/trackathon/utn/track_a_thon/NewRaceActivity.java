package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.*;
import android.widget.EditText;
import android.widget.Button;

import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.Race;


public class NewRaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_race);

        Button btnCreate = (Button) findViewById(R.id.ButtonCreate);

        EditText nameField = (EditText) findViewById(R.id.EditTextName);
        EditText distanceField = (EditText) findViewById(R.id.EditTextDistance);
        EditText locationField = (EditText) findViewById(R.id.EditTextLocation);
        EditText startTimeField = (EditText) findViewById(R.id.EditTextStartTime);

        //Listener on Submit button
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String distance = distanceField.getText().toString() + "km";
                String location = locationField.getText().toString();
                String startTime = startTimeField.getText().toString();
                Race race = new Race(name, location, distance, startTime);

                Firebase.newRace(race);
            }
        });
    }


}
