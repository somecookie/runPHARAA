package ch.epfl.sweng.runpharaa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleMap mMap;
    private LatLng INM_coord = new LatLng(46.518510, 6.563199);
    private User fake_user = new User("Toto", INM_coord, 2000);
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean permission = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        enableMyLocation();

    }

    /**
     * Send request to enable location if it is not already enable
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (!(mMap == null)) {
                mMap.setMyLocationEnabled(true);
            }

            Log.i(TAG, "Permission granted.");

            if (location == null) {
                Log.i(TAG, "Location null.");
                if (permission) {
                    Log.i(TAG, "Location request update.");
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                }
            } else {
                Log.i(TAG, " new Location.");
                handleNewLocation(location);
            }

        }else {
            enableMyLocation();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();

        if(!(mMap == null)) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Permission granted");

                    setMarkers();

                    createLocationRequest();

                    permission = true;

                } else {
                    Log.i(TAG, "Permission not granted");

                }

                if (mMap != null & permission) {
                    // Access to the location has been granted to the app.
                    mMap.setMyLocationEnabled(true);
                }

                return;

            }
        }
    }

    /**
     * Clear the actual map from all its markers and set the new ones
     */
    private void setMarkers(){
        mMap.clear();

        int transparentBlue = 0x2f0000ff;
        int transBlueBorder = 0x000000ff;

        //add a circle of 2km around the current location
        mMap.addCircle(new CircleOptions()
                .center(fake_user.getLocation())
                .radius(2000)
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fake_user.getLocation()));

        //add a marker for each starting point inside the preferred radius
        for(Track tr : fake_user.tracksNearMe()){
            mMap.addMarker(new MarkerOptions()
                    .position(tr.getStartingPoint())
                    .title(tr.getLocation()));
        }
    }


    /**
     * Update the user location when receiving a new location and update the markers
     * @param location the new location
     */
    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        fake_user.setLocation( new LatLng(currentLatitude, currentLongitude));

        setMarkers();
    }

    /**
     * Set up the location request object
     */
    private void createLocationRequest(){
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

}
