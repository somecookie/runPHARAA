package ch.epfl.sweng.runpharaa;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng INM_coord = new LatLng(46.518510, 6.563199);
    private User fake_user = new User("Toto", INM_coord, 2000);
    private int circle_blue = 0x2f0000ff; //should be in values

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //add a marker for each starting point inside the preferred radius
        for(Track tr : fake_user.tracksNearMe()){
            mMap.addMarker(new MarkerOptions()
            .position(tr.getStartingPoint())
            .title(tr.getLocation()));
        }

        // Add a marker at the user's location
        mMap.addMarker(new MarkerOptions().
                position(fake_user.getLocation())
                .title(fake_user.getName()+"'s location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        //add a circle of 2km around the current location
        mMap.addCircle(new CircleOptions()
                .center(fake_user.getLocation())
                .radius(fake_user.getPreferredRadius())
                .fillColor(circle_blue)
                .strokeWidth(0));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fake_user.getLocation()));
    }
}
