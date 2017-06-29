package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.GPSLocation;
import com.trackathon.utn.track_a_thon.model.Race;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;

public class RaceActivity extends AppCompatActivity {

    private Race race;
    private GoogleMap mMap;
    private HashMap<String, Marker> runners = new HashMap<>();
    private Polyline raceTrack;
    private Boolean updateMapPosition = true;

    private ViewPager mViewPager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        MenuItem item = menu.findItem(R.id.show_track);
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_track:
                Boolean newState = !item.isChecked();
                item.setChecked(newState);
                show_track_changed(newState);
        }
        return super.onOptionsItemSelected(item);
    }

    private void show_track_changed(Boolean show) {
        if (show) {
            renderRace();
        } else {
            raceTrack.remove();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        race = (Race) intent.getSerializableExtra(TrackatonConstant.RACE);
        String raceId = intent.getStringExtra(TrackatonConstant.RACE_ID);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), raceId);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setTitle(getString(R.string.title_activity_map, race.getName()));

    }

    public void findInMap(String runnerId, Runner runner) {
        Marker marker = runners.get(runner.getName());
        if (marker != null) {
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        }
        mViewPager.setCurrentItem(0, true);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.mMap = googleMap;
    }

    private void renderRace() {
        PolylineOptions polylineOptions = new PolylineOptions();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        polylineOptions.width(10).color(Color.RED);
        race.getPoints().forEach((point) -> {
            LatLng latLng = point.toLatLng();
            polylineOptions.add(latLng);
            builder.include(latLng);
        });
        raceTrack = mMap.addPolyline(polylineOptions);
        if (updateMapPosition) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 18));
            updateMapPosition = !updateMapPosition;
        }
    }

    private void update(String runnerId, Runner runner) {
        if (runners.containsKey(runner.getName())) {
            updateRunnerMarker(runner);
        } else {
            createRunnerMarker(runner);
        }
    }

    private void createRunnerMarker(Runner runner) {
        LatLng location = runner.getLocation().toLatLng();
        MarkerOptions markerOption = new MarkerOptions().position(location).title(runner.getName()).icon(getBitmapDescriptor());
        Marker marker = mMap.addMarker(markerOption);
        marker.showInfoWindow();
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
        marker.setTitle(runner.getName());
        animateMarker(marker, location);
    }

    public void animateMarker(final Marker marker, final LatLng toPosition) {
        Handler handler = new Handler();
        Long start = SystemClock.uptimeMillis();
        Projection projection = mMap.getProjection();
        Point startPoint = projection.toScreenLocation(marker.getPosition());
        LatLng startLatLng = projection.fromScreenLocation(startPoint);
        Long duration = 2000L;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                } else {
                    marker.setVisible(true);
                }
            }
        });
    }

    public static class RunnersFragment extends Fragment {

        private String raceId;
        private RecyclerView runnersRV;

        public RunnersFragment() {
        }

        public static RunnersFragment newInstance(String raceId) {
            RunnersFragment fragment = new RunnersFragment();
            Bundle args = new Bundle();
            args.putString(TrackatonConstant.RACE_ID, raceId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_race_runners, container, false);
            raceId = getArguments().getString(TrackatonConstant.RACE_ID);
            setRecyclerView(rootView);
            return rootView;
        }

        public void setRecyclerView(View rootView) {
            LinearLayoutManager racesLayout = new LinearLayoutManager(getContext());

            runnersRV = (RecyclerView) rootView.findViewById(R.id.runners_rv);
            runnersRV.setHasFixedSize(true);
            runnersRV.setLayoutManager(racesLayout);

            Firebase.allRunners(raceId, trackers -> {
                RecycleViewRunnerAdapter adapter = new RecycleViewRunnerAdapter(trackers, (runnerId, runner) -> {
                    RaceActivity activity = (RaceActivity) getActivity();
                    activity.findInMap(runnerId, runner);
                });
                runnersRV.setAdapter(adapter);
            });
        }

    }

    public static class MapViewFragment extends Fragment {

        private String raceId;

        public MapViewFragment() {
        }

        public static MapViewFragment newInstance(String raceId) {
            MapViewFragment fragment = new MapViewFragment();
            Bundle args = new Bundle();
            args.putString(TrackatonConstant.RACE_ID, raceId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_race_map, container, false);
            raceId = getArguments().getString(TrackatonConstant.RACE_ID);
            setMapView(rootView, savedInstanceState);
            return rootView;
        }

        private void setMapView(View rootView, Bundle savedInstanceState) {
            MapView mapView = (MapView) rootView.findViewById(R.id.map);
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync((googleMap) -> {
                UiSettings uiSettings = googleMap.getUiSettings();

                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setCompassEnabled(true);
                RaceActivity activity = (RaceActivity) getActivity();
                activity.setGoogleMap(googleMap);
                activity.renderRace();
                renderRunners(activity);
            });
        }

        private void renderRunners(RaceActivity activity) {
            Firebase.raceUpdates(raceId, activity::update);
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String raceId;

        SectionsPagerAdapter(FragmentManager fm, String raceId) {
            super(fm);
            this.raceId = raceId;
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0
                    ? MapViewFragment.newInstance(raceId)
                    : RunnersFragment.newInstance(raceId);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.map);
                case 1:
                    return getString(R.string.runners);
            }
            return null;
        }
    }
}
