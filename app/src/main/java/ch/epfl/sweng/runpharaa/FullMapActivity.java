package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;

import ch.epfl.sweng.runpharaa.utils.Config;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class FullMapActivity  extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private LatLng[] points;
    private TextView testText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_map);
        testText = findViewById(R.id.maps_test_text3);

        Bundle bundle = getIntent().getExtras();
        Parcelable[] a = bundle.getParcelableArray("points");
        points = Arrays.copyOf(a, a.length, LatLng[].class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_map_view3);
        mapFragment.getMapAsync(this);
        if(Config.isTest){
            onMapReady(Config.getFakeMap());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.zoomTo(18));
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(marker -> true);
        map.setPadding(50, 150, 50, 50);
        drawTrackOnMap();
        testText.setText("ready");
    }

    private void drawTrackOnMap() {
        if (map != null && points != null) {
            Log.i("Create Map : ", "Drawing on fakeMap in FullMapActivity.");
            // Get correct zoom
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng point : points)
                boundsBuilder.include(point);
            LatLngBounds bounds = boundsBuilder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = 250;
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
            // Add lines
            map.addPolyline(new PolylineOptions().addAll(Arrays.asList(points)));
            // Add markers (start = green, finish = red)
            map.addMarker(new MarkerOptions().position(points[0]).icon(defaultMarker(150)).alpha(0.8f));
            map.addMarker(new MarkerOptions().position(points[points.length - 1]).icon(defaultMarker(20)).alpha(0.8f));
        }
    }
}
