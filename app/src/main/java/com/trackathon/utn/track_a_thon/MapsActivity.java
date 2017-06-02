package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.Runner;
import com.trackathon.utn.track_a_thon.model.User;

import java.util.HashMap;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private String raceId;
    private GoogleMap mMap;
    private HashMap<String, Marker> runners;
    private RecyclerView runnersRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = this.getIntent();

        String raceName = intent.getExtras().getString(TrackatonConstant.RACE_NAME);
        raceId = intent.getExtras().getString(TrackatonConstant.RACE_ID);
        runners = new HashMap<>();

        setTitle(getString(R.string.title_activity_map, raceName));

        createToolbar();
        createNavigation();
    }

    private void createToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void createNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNavigationMenu();

        User user = User.getCurrentUser();
        View headerView = navigationView.getHeaderView(0);
        ((TextView)headerView.findViewById(R.id.navigation_drawer_name)).setText(user.getName());
        ((TextView)headerView.findViewById(R.id.navigation_drawer_email)).setText(user.getEmail());
        ImageView imageView = (ImageView) headerView.findViewById(R.id.navigation_drawer_avatar);
        Picasso.with(headerView.getContext()).load(user.getImageUrl()).into(imageView);
    }

    private void setNavigationMenu() {
        LinearLayoutManager racesLayout = new LinearLayoutManager(getApplicationContext());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        runnersRV = (RecyclerView)findViewById(R.id.runners);
        runnersRV.setHasFixedSize(true);
        runnersRV.setLayoutManager(racesLayout);

        Firebase.allRunners(raceId, trackers -> {
            RecycleViewRunnerAdapter adapter = new RecycleViewRunnerAdapter(trackers, (runnerId, runner) -> {
                Marker marker = runners.get(runner.getName());
                marker.showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                drawer.closeDrawer(GravityCompat.START);
            });
            runnersRV.setAdapter(adapter);
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_watchers) {
            doSomething();
        } else if (id == R.id.nav_trackers) {
            doSomething();
        } else if (id == R.id.nav_logout) {
            doSomething();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doSomething() {

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
        MarkerOptions markerOption = new MarkerOptions().position(location).title(runner.getName()).icon(getBitmapDescriptor());
        Marker marker = mMap.addMarker(markerOption);
        marker.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        runners.put(runner.getName(), marker);
    }

    @NonNull
    private BitmapDescriptor getBitmapDescriptor() {
        Drawable drawable = getResources().getDrawable(R.drawable.ic_runner, getTheme());
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void updateRunnerMarker(Runner runner) {
        LatLng location = runner.getLocation().toLatLng();
        Marker marker = runners.get(runner.getName());
        marker.showInfoWindow();
        marker.setPosition(location);
        marker.setTitle(runner.getName());
    }
}
