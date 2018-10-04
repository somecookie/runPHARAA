package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private BroadcastReceiver receiver;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            stopGeoLocalisation();
            if (receiver != null)
                unregisterReceiver(receiver);
        }
    }

    public void stopGeoLocalisation() {
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        stopService(i);
    }

    /**
     * Clear the actual map from all its markers and set the new ones
     */
    private void setMarkers() {
        mMap.clear();

        int transparentBlue = 0x2f0000ff;
        int transBlueBorder = 0x000000ff;

        //add a circle of 2km around the current location
        mMap.addCircle(new CircleOptions()
                .center(FAKE_USER.getLocation())
                .radius(FAKE_USER.getPreferredRadius())
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(FAKE_USER.getLocation()));

        //add a marker for each starting point inside the preferred radius
        for (Track tr : FAKE_USER.tracksNearMe()) {
            mMap.addMarker(new MarkerOptions()
                    .position(tr.getStartingPoint())
                    .title(tr.getLocation()));
        }

    }

    /**
     * Update the user location when receiving a new location and update the markers
     */
    private void handleNewLocation() {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        FAKE_USER.setLocation(new LatLng(currentLatitude, currentLongitude));

        setMarkers();
    }

    private void initReceiver() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Receive new location
                    location = (Location) intent.getExtras().get("new_location");
                    if (location != null)
                        handleNewLocation();
                }
            };
        }
        registerReceiver(receiver, new IntentFilter("location_update"));
    }
}
