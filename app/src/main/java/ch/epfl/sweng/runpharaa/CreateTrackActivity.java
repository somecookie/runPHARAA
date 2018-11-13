package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.location.RealGpsService;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

public final class CreateTrackActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback {

    private PolylineOptions lines;
    private ArrayList<LatLng> points = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private boolean creating;
    private GoogleMap googleMap;
    private Button createButton;

    /**
     * The listener used for the main button
     */
    private View.OnClickListener buttonOnClickListener = (v) -> {
        if (!creating) {
            creating = true;
            createButton.setText("STOP");
            handleNewLocation();
        } else {
            if (points.size() < 2) {
                Toast.makeText(getBaseContext(), "You need at least 2 points to create a track !", Toast.LENGTH_LONG).show();
            } else {
                creating = false;
                createButton.setText("PROCESSING");
                launchSecondPart();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        // Setup button
        createButton = findViewById(R.id.start_create_button);
        createButton.setOnClickListener(buttonOnClickListener);
        createButton.setText("START");
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(18));

        handleNewLocation();
    }

    @Override
    protected void handleNewLocation() {
        // Get new location
        Location location = GpsService.getInstance().getCurrentLocation();

        if (location == null)
            return;

        LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

        // Move camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
        if (creating) {
            // Store new location
            locations.add(location);
            // Add new point
            points.add(current);
            // Clear map
            googleMap.clear();
            // Draw path
            lines = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
            lines.addAll(points);
            googleMap.addPolyline(lines);
        }
    }

    /**
     * Starts the second part of creating a track
     */
    private void launchSecondPart() {
        Intent i = new Intent(getApplicationContext(), CreateTrackActivity2.class);
        // Add the extras
        i.putExtra("points", points.toArray(new LatLng[points.size()]));
        i.putExtra("locations", locations.toArray(new Location[locations.size()]));
        startActivity(i);
        finish();
    }
}
