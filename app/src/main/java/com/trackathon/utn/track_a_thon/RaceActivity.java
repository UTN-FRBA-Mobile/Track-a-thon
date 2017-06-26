package com.trackathon.utn.track_a_thon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.Runner;

import java.util.HashMap;

public class RaceActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private HashMap<String, Marker> runners = new HashMap<>();

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String raceId = intent.getStringExtra(TrackatonConstant.RACE_ID);
        String raceName = intent.getStringExtra(TrackatonConstant.RACE_NAME);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), raceId);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setTitle(getString(R.string.title_activity_map, raceName));

    }

    public void findInMap(String runnerId, Runner runner) {
        Marker marker = runners.get(runner.getName());
        if (marker != null) {
            marker.showInfoWindow();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        mViewPager.setCurrentItem(0, true);
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.mMap = googleMap;
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
                Firebase.raceUpdates(raceId, (runnerId, runner) -> {
                    activity.update(runnerId, runner);
                });
            });
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
