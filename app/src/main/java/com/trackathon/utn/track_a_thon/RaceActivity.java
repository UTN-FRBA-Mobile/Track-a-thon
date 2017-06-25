package com.trackathon.utn.track_a_thon;

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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_race);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_race, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public static class MapViewFragment extends Fragment {

        private static final String RACE_ID = TrackatonConstant.RACE_ID;

        private GoogleMap mMap;
        private String raceId;
        private HashMap<String, Marker> runners = new HashMap<>();

        public MapViewFragment() {
        }

        public static MapViewFragment newInstance(String raceId) {
            MapViewFragment fragment = new MapViewFragment();
            Bundle args = new Bundle();
            args.putString(RACE_ID, raceId);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_race_map, container, false);
            raceId = getArguments().getString(RACE_ID);
            setMapView(rootView);
            return rootView;
        }

        private void setMapView(View rootView) {
            MapView mapView = (MapView) rootView.findViewById(R.id.map);
            mapView.getMapAsync((googleMap) -> {
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
            Drawable drawable = getResources().getDrawable(R.drawable.ic_runner, getActivity().getTheme());
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

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0
                    ? MapViewFragment.newInstance("Hello")
                    : PlaceholderFragment.newInstance(position + 1);
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
