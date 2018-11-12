package ch.epfl.sweng.runpharaa.location;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Binder;
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

import ch.epfl.sweng.runpharaa.user.SettingsActivity;
import ch.epfl.sweng.runpharaa.user.User;

import static ch.epfl.sweng.runpharaa.user.SettingsActivity.getInt;

public class RealGpsService extends GpsService implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private IBinder iBinder = new LocalBinder();

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    // Needed for launching service during tests
    public class LocalBinder extends Binder {
        public RealGpsService getService() {
            return RealGpsService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

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

    /*@Override
    public void onCreate() {
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        //Toast.makeText(getApplicationContext(), "Starting GPS Service", Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(getApplicationContext(), "Ending GPS Service", Toast.LENGTH_SHORT).show();
        if (mGoogleApiClient.isConnected()) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
            mGoogleApiClient.disconnect();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
            initLocationCallBack();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("GPS_SERVICE", "Location services connection failed with code " + connectionResult.getErrorCode());
    }

    /*@Override
    public void onLocationChanged(Location location) {
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_SHORT).show();
        sendNewLocation(location);
    }*/

    @Override
    protected void updateAndSendNewLocation(Location location) {
        currentLocation = location;
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