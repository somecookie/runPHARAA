package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;


public final class MapsActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    protected void handleNewLocation() {

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        FAKE_USER.setLocation(new LatLng(currentLatitude, currentLongitude));

        setMarkers();
    }

    /**
     * Clear the actual map from all its markers and set the new ones
     */
    private void setMarkers() {
        mMap.clear();

        int transparentBlue = 0x2f0000ff;
        int transBlueBorder = 0x000000ff;

        //add a circle around the current location
        mMap.addCircle(new CircleOptions()
                .center(FAKE_USER.getLocation())
                .radius(FAKE_USER.getPreferredRadius())
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));
        //follow the user
        mMap.moveCamera(CameraUpdateFactory.newLatLng(FAKE_USER.getLocation()));

        //add a marker for each starting point inside the preferred radius
        for (Track tr : FAKE_USER.tracksNearMe()) {
            mMap.addMarker(new MarkerOptions()
                    .position(tr.getStartingPoint())
                    .title(tr.getLocation()));
        }

    }
}
