package com.trackathon.utn.track_a_thon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trackathon.utn.track_a_thon.model.GPSLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.trackathon.utn.track_a_thon.R.id.mapView;

/**
 * Created by Julian on 2/7/2017.
 */

public class MapFragment extends Fragment {
    MapView mapView;
    List<GPSLocation> points = new ArrayList<>();
    Double lat;
    Double lng;
    MapFragment fragm = this;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_race_map, container, false);

        Button btnAccept = (Button) v.findViewById(R.id.ButtonAccept);
        Button btnCancel = (Button) v.findViewById(R.id.ButtonCancel);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync((googleMap) -> {
            UiSettings uiSettings = googleMap.getUiSettings();

            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setCompassEnabled(true);
            PolylineOptions polylineOptions = new PolylineOptions();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            polylineOptions.width(10).color(Color.RED);
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng arg0)
                {
                    lat = arg0.latitude;
                    lng = arg0.longitude;
                    points.add(new GPSLocation(lat, lng));
                    android.util.Log.i("onMapClick", arg0.toString());

                    /*googleMap.addMarker(new MarkerOptions()
                            .position(arg0)
                            .title("Hello world"));*/



                    polylineOptions.add(arg0);
                    builder.include(arg0);
                    googleMap.addPolyline(polylineOptions);
                    /*if (updateMapPosition) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 18));
                        updateMapPosition = !updateMapPosition;
                    }*/

                }
            });
        });

        //Listener on Submit button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.i("test", getActivity().getClass().toString());
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragm).commit();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NewRaceActivity)getActivity()).setPoints(points);
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragm).commit();
            }
        });

        return v;

    }
}
