package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private String raceId;
    private GoogleMap mMap;
    private HashMap<String, Marker> runners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();

        String raceName = intent.getExtras().getString(TrackatonConstant.RACE_NAME);
        raceId = intent.getExtras().getString(TrackatonConstant.RACE_ID);
        runners = new HashMap<>();

        setTitle(getString(R.string.title_activity_map, raceName));
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        UiSettings uiSettings = googleMap.getUiSettings();

        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);

        mMap = googleMap;
        Firebase.raceUpdates(raceId, (runnerId, runner) -> {
            if (runners.containsKey(runner.getName())) {
                updateRunnerMarker(runner);
            } else {
                createRunnerMarker(runner);
            }
        });
    }

    private void createRunnerMarker(Runner runner) {
        LatLng location = runner.getLocation().toLatLng();
        MarkerOptions markerOption = new MarkerOptions().position(location).title(runner.getName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_runner));
        Marker marker = mMap.addMarker(markerOption);
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        runners.put(runner.getName(), marker);
    }

    private void updateRunnerMarker(Runner runner) {
        LatLng location = runner.getLocation().toLatLng();
        Marker marker = runners.get(runner.getName());
        marker.showInfoWindow();
        marker.setPosition(location);
        marker.setTitle(runner.getName());
    }
}
