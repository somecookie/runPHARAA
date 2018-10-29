package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.app.Service;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.runpharaa.user.User;

public class GpsService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private IBinder iBinder = new LocalBinder();

    private GoogleApiClient mGoogleApiClient;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private static LocationRequest locationRequest;
    private static Location currentLocation;

    // Needed for launching service during tests
    public class LocalBinder extends Binder {
        public GpsService getService() {
            return GpsService.this;
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

    private void updateAndSendNewLocation(Location location) {
        // Broadcast the new location to other activities
        //Intent i = new Intent("location_update");
        //i.putExtra("new_location", location);
        currentLocation = location;
        User.instance.setLocation(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        sendBroadcast(new Intent("location_update"));
    }

    private void initLocationRequest() {
        // Get pref values or default ones
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String timeIntervalString = sp.getString(getString(R.string.pref_key_time_interval), getString(R.string.pref_default_time_interval));
        String minTimeIntervalString = sp.getString(getString(R.string.pref_key_min_time_interval), getString(R.string.pref_default_min_time_interval));
        String minDistanceString = sp.getString(getString(R.string.pref_key_min_distance_interval), getString(R.string.pref_default_min_distance_interval));
        // Extract the values
        long timeInterval = Long.parseLong(timeIntervalString);
        long minTimeInterval = Long.parseLong(minTimeIntervalString);
        float minDistance = Float.parseFloat(minDistanceString);
        // Create the LocationRequest object
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(timeInterval)
                .setFastestInterval(minTimeInterval)
                .setSmallestDisplacement(minDistance);
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

    public static void setTimeInterval(int newTimeInterval) {
        if(locationRequest != null) {
            locationRequest.setInterval(newTimeInterval);
        }
    }

    public static void setMinTimeInterval(int newMinTimeInterval) {
        if(locationRequest != null) {
            locationRequest.setFastestInterval(newMinTimeInterval);
        }
    }

    public static void setMinDistanceInterval(int newMinDistanceInterval) {
        if(locationRequest != null) {
            locationRequest.setSmallestDisplacement(newMinDistanceInterval);
        }
    }

    public static Location getCurrentLocation() {
        return currentLocation;
    }
}
