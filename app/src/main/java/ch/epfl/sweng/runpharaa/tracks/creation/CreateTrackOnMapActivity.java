package ch.epfl.sweng.runpharaa.tracks.creation;

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

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.location.GpsService;
import ch.epfl.sweng.runpharaa.location.LocationUpdateReceiverActivity;
import ch.epfl.sweng.runpharaa.utils.Config;

public final class CreateTrackOnMapActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback {

    private PolylineOptions lines;
    private ArrayList<LatLng> points = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private boolean creating;
    private GoogleMap map;
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
        // Setup button
        createButton = findViewById(R.id.start_create_button);
        createButton.setOnClickListener(buttonOnClickListener);
        createButton.setText("START");
        // Obtain the SupportMapFragment and get notified when the fakeMap is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (Config.isTest) {
            onMapReady(Config.getFakeMap());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(18));

        lines = new PolylineOptions();

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
        map.moveCamera(CameraUpdateFactory.newLatLng(current));
        if (creating) {
            // Store new location
            locations.add(location);
            // Add new point
            points.add(current);
            // Clear fakeMap
            map.clear();
            // Draw path
            lines = lines.width(10).color(Color.BLUE).geodesic(true);
            lines.addAll(points);
            map.addPolyline(lines);
        }
    }

    /**
     * Starts the second part of creating a track
     */
    private void launchSecondPart() {
        Intent i = new Intent(getApplicationContext(), SetTrackDetailsActivity.class);
        // Add the extras
        i.putExtra("points", points.toArray(new LatLng[points.size()]));
        i.putExtra("locations", locations.toArray(new Location[locations.size()]));
        startActivity(i);
        finish();
    }
}
