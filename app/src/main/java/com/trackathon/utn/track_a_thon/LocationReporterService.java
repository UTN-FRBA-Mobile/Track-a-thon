package com.trackathon.utn.track_a_thon;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.trackathon.utn.track_a_thon.firebase.Firebase;
import com.trackathon.utn.track_a_thon.model.RunnerLocation;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

public class LocationReporterService extends Service {

    private final IBinder serviceBinder = new ServiceBinder();

    class ServiceBinder extends Binder {

        LocationReporterService getService() {
            return LocationReporterService.this;
        }
    }
    final private Integer PERSISTENT_NOTIFICATION_ID = 0;

    private Handler handler;

    private Runnable updateRunnable;
    private Integer updateInterval;
    private LocationManager locationManager;

    private LocationListener locationListener;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationCompat.BigTextStyle notificationStyle;

    private String raceId;
    private String raceName;
    private String runnerId;
    private String runnerName;
    private DecimalFormat speedFormat;
    private Location lastLocation;
    private Float accumulatedDistance;
    private Long startTime;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler();
        updateInterval = 1500;

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updatePersistentNotificationSpeed();
                handler.postDelayed(this, updateInterval);
            }
        };

        speedFormat = new DecimalFormat("#.## m/s");
        speedFormat.setRoundingMode(RoundingMode.CEILING);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationStyle = new NotificationCompat.BigTextStyle();
        notificationBuilder = new NotificationCompat
                .Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_my_location_black_24dp)
                .setContentTitle("Race ongoing!")
                .setStyle(notificationStyle)
                .setOngoing(true);
    }


    @NonNull
    private String baseNotificationMessage() {
        return "Your location is being tracked.";
    }

    private String speedLabel(Float speed) {
        return baseNotificationMessage() + "\nSpeed: " + speedFormat.format(speed);
    }

    public void start(String raceId, String raceName, String runnerName) {
        this.raceId = raceId;
        this.raceName = raceName;
        this.runnerName = runnerName;
        setUpStats();
        addRaceRunner();
        toastNotification("Service started");
        popUpPersistentNotification();
        registerLocationListener();
        startNotificationUpdater();
    }

    private void addRaceRunner() {
        this.runnerId = Firebase.registerRunner(raceId, runnerName);
    }

    private void setUpStats() {
        startTime = currentTime();
        accumulatedDistance = 0f;
        lastLocation = null;
    }

    private void startNotificationUpdater() {
        handler.postDelayed(updateRunnable, updateInterval);
    }

    private Float averageSpeed() {
        return accumulatedDistance / (currentTime() - startTime);
    }

    private void updatePersistentNotificationSpeed() {
        setNotificationText(speedLabel(averageSpeed()));
        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void registerLocationListener() {
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                Log.d("LocationReporterService", location.toString());
                Firebase.setNewLocation(raceId, runnerId, RunnerLocation.from(location));
                if (lastLocation == null) {
                    lastLocation = location;
                } else {
                    accumulatedDistance += location.distanceTo(lastLocation);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
        }
    }

    private void removeLocationListener() {
        locationManager.removeUpdates(locationListener);
    }

    private Long currentTime() {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
    }

    private void toastNotification(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void stop() {
        toastNotification("Service stopped");
        removeRunnerFromRace();
        removePersistentNotification();
        removeLocationListener();
        stopScheduledUpdates();
        stopSelf();
    }

    private void removeRunnerFromRace() {
        Firebase.unregisterRunner(raceId, runnerId);
    }

    private void stopScheduledUpdates() {
        handler.removeCallbacks(updateRunnable);
    }

    private void popUpPersistentNotification() {
        setNotificationText(baseNotificationMessage());
        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, notificationBuilder.build());
    }

    private void removePersistentNotification() {
        notificationManager.cancel(PERSISTENT_NOTIFICATION_ID);
    }

    private void setNotificationText(String text) {
        notificationStyle.bigText(text);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }
}
