package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;

public final class MapsActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        testText = findViewById(R.id.maps_test_text);
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
        mMap.setOnInfoWindowClickListener(this);
        InfoWindowGoogleMap customInfoWindow = new InfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        testText.setText("ready");
    }

    @Override
    protected void handleNewLocation() {
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
                .center(User.instance.getLocation())
                .radius(User.instance.getPreferredRadius())
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));
        //follow the user
        mMap.moveCamera(CameraUpdateFactory.newLatLng(User.instance.getLocation()));

        //add a marker for each starting point inside the preferred radius
        /*
        for (Track tr : User.instance.tracksNearMe()) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(tr.getStartingPoint().ToLatLng())
                    .title(tr.getName()));
            m.setTag(tr.getTrackUid());
        }
        */
        DatabaseManagement.mReadDataOnce(DatabaseManagement.TRACKS_PATH, new DatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                List<Track> tracks = DatabaseManagement.initTracksNearMe(data);
                for (Track t : tracks) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(t.getStartingPoint().ToLatLng())
                            .title(t.getName()));
                    m.setTag(t.getTrackUid());
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in MapsActivity.");
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent i = new Intent(this, TrackPropertiesActivity.class);
        i.putExtra("TrackID", (int) marker.getTag());
        startActivity(i);
    }

    /**
     * Private class that applies the customized info window layout
     */
    private class InfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

        private Context context;

        InfoWindowGoogleMap(Context context) {
            this.context = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.marker_info_window, null);

            TextView title = view.findViewById(R.id.marker_window_title);

            TextView lenText = view.findViewById(R.id.marker_window_text_len);
            TextView diffText = view.findViewById(R.id.marker_window_text_diff);
            TextView likeText = view.findViewById(R.id.marker_window_text_like);

            // Set title
            title.setText(marker.getTitle());

            // Get the correct track by it's id
            Track track = null;
            for (Track tr : Track.allTracks)
                if (track.getTrackUid() ==  marker.getTag())
                    track = tr;

            // Get other info from the track (should never be null be we check just in case)
            if (track != null) {

                TrackProperties tp = track.getProperties();

                lenText.setText(tp.getLength() + " m");
                diffText.setText(tp.getHeightDifference() + " m");
                likeText.setText(tp.getLikes() + "");
            }
            return view;
        }
    }
}
