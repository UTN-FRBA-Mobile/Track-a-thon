package com.trackathon.utn.track_a_thon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.Race;


public class WatchingActivity extends AppCompatActivity {


    TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watching);

        content = (TextView) findViewById(R.id.textView);

        Firebase.allRaces(races -> {
            String t = races
                    .stream()
                    .map(Race::toString)
                    .reduce("", String::concat);
            content.setText(t);
        });
    }
}
