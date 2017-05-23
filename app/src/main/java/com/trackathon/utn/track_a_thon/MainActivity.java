package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btnWatchers;
    private Button btnTrackers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createElements();
        createListeners();
    }

    private void createElements() {
        btnWatchers = (Button) findViewById(R.id.btnWatchers);
        btnTrackers = (Button) findViewById(R.id.btnTrackers);
    }

    private void createListeners() {
        btnTrackers.setOnClickListener((View v) -> openActivity(true));
        btnWatchers.setOnClickListener((View v) -> openActivity(false));
    }

    private void openActivity (Boolean isTracker) {
        Intent intent = new Intent(MainActivity.this, RacesActivity.class);
        intent.putExtra(TrackatonConstant.IS_TRACKER, isTracker);
        startActivity(intent);
    }
}
