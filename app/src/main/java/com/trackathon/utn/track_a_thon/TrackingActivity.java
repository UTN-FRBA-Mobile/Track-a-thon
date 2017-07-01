package com.trackathon.utn.track_a_thon;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class TrackingActivity extends AppCompatActivity {

    private final int REQUEST_CODE_PERMISSION = 5;
    private final String LOCATION_PERMISSION = android.Manifest.permission.ACCESS_FINE_LOCATION;


    Button trackButton;
    LocationReporterService trackingService;
    private String raceName;
    private String raceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);
        trackButton = (Button) findViewById(R.id.trackButton);
        Intent intent = this.getIntent();

        raceName = intent.getExtras().getString(TrackatonConstant.RACE_NAME);
        raceId = intent.getExtras().getString(TrackatonConstant.RACE_ID);

        if (hasLocationPermission()) {
            bindTrackingService();
        } else {
            requestLocationPermission();
        }
    }

    @Override
    public void onBackPressed() {
        unbindTrackingService();
        super.onBackPressed();
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{LOCATION_PERMISSION}, REQUEST_CODE_PERMISSION);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                bindTrackingService();
            }
        }
    }

    private void changeTracking() {
        if (trackingService.isTracking) {
            trackingService.start(raceId);
            trackButton.setText(R.string.stop);
        } else {
            trackingService.stop();
            trackButton.setText(R.string.start);
        }
    }

    private void bindTrackingService() {
        Intent bindIntent = new Intent(this, LocationReporterService.class);
        bindService(bindIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindTrackingService() {
        trackingService.stop();
        unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationReporterService.ServiceBinder binder = (LocationReporterService.ServiceBinder) service;
            trackingService = binder.getService();
            trackButton.setOnClickListener((view) -> changeTracking());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}
