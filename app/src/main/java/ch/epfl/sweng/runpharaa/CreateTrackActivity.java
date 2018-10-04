package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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

public class CreateTrackActivity extends FragmentActivity implements OnMapReadyCallback {

    private BroadcastReceiver receiver;
    private PolylineOptions lines;
    private ArrayList<LatLng> points = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private boolean creating;
    private GoogleMap googleMap;
    private Location location;
    private Button createButton;

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
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!creating) {
                    creating = true;
                    createButton.setText("STOP");
                } else {
                    if (points.size() < 2) {
                        Toast.makeText(getBaseContext(), "You need at least 2 points to create a track !", Toast.LENGTH_LONG).show();
                    } else {
                        creating = false;
                        stopGeoLocalisation();
                        createButton.setText("PROCESSING");
                        launchSecondPart();
                    }
                }
            }
        });
        createButton.setText("START");
        // Setup broadcast receiver
        initReceiver();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(18));
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
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

    private void handleNewLocation() {
        if(creating) {
            // Store new location
            locations.add(location);
            // Add new point
            LatLng current = new LatLng(location.getLatitude(), location.getLongitude());
            points.add(current);
            // Move camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            // Clear map
            googleMap.clear();
            // Draw path
            lines = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
            lines.addAll(points);
            googleMap.addPolyline(lines);
        }
    }

    private void launchSecondPart() {
        Intent i = new Intent(getApplicationContext(), CreateTrackActivity2.class);
        // Add the extras
        i.putExtra("points", points.toArray(new LatLng[points.size()]));
        i.putExtra("locations", locations.toArray(new Location[locations.size()]));
        startActivity(i);
        finish();
    }
}
