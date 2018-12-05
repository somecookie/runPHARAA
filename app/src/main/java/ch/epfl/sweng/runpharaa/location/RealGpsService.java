package ch.epfl.sweng.runpharaa.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.runpharaa.user.settings.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static ch.epfl.sweng.runpharaa.user.settings.SettingsActivity.getInt;

public class RealGpsService extends GpsService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        initLocationRequest();
        mGoogleApiClient.connect();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
            initLocationCallBack();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GPS_SERVICE", "Location services connection failed with code " + connectionResult.getErrorCode());
    }

    @Override
    protected void updateAndSendNewLocation(Location location) {
        currentLocation = location;
        if(User.instance != null)
            User.instance.setLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        sendBroadcast(new Intent("location_update"));
    }

    private void initLocationRequest() {
        // Create the LocationRequest object
        locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Get pref values or default ones
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int timeInterval = getInt(sp, SettingsActivity.PREF_KEY_TIME_INTERVAL, 5);
        int minTimeInterval = getInt(sp, SettingsActivity.PREF_KEY_MIN_TIME_INTERVAL, 1);
        int minDistance = getInt(sp, SettingsActivity.PREF_KEY_MIN_DISTANCE, 5);
        // Set the values
        setTimeInterval(timeInterval);
        setMinTimeInterval(minTimeInterval);
        setMinDistanceInterval(minDistance);
    }

    private void initLocationCallBack() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateAndSendNewLocation(locationResult.getLastLocation());
            }
        };
    }

    // ---------- SETTERS ------------

    /**
     *
     * @param newTimeInterval in seconds
     */
    @Override
    public void setTimeInterval(int newTimeInterval) {
        if(locationRequest != null) {
            locationRequest.setInterval(newTimeInterval * 1000);
        }
    }

    /**
     *
     * @param newMinTimeInterval in seconds
     */
    @Override
    public void setMinTimeInterval(int newMinTimeInterval) {
        if(locationRequest != null) {
            locationRequest.setFastestInterval(newMinTimeInterval * 1000);
        }
    }

    /**
     *
     * @param newMinDistanceInterval in meters
     */
    @Override
    public void setMinDistanceInterval(int newMinDistanceInterval) {
        if(locationRequest != null) {
            locationRequest.setSmallestDisplacement(newMinDistanceInterval);
        }
    }

    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void setNewLocation(Context context, Location location) {
        Log.i("realGpsService", "Can't set new location on the real service...");
    }
}
