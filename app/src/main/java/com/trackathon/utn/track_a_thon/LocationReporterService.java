package com.trackathon.utn.track_a_thon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.trackathon.utn.track_a_thon.firebase.Firebase;

public class LocationReporterService extends Service {

    private final IBinder serviceBinder = new ServiceBinder();

    class ServiceBinder extends Binder {
        LocationReporterService getService() {
            return LocationReporterService.this;
        }
    }

    private LocationManager locationManager;
    private LocationListener locationListener;

    public void start() {
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.d("LocationReporterService", location.toString());
                Firebase.setNewLocation("Nike 10k", "Usain Bolt", location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, locationListener);
        }
    }

    public void stop() {
        locationManager.removeUpdates(locationListener);
        stopSelf();
        Toast.makeText(getApplicationContext(), "Service stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }
}
