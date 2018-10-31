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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.user.User;

public final class MapsActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private TextView testText;
    private List<Marker> markers; // used to check if a windowInfo is opened

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        testText = findViewById(R.id.maps_test_text);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
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
        handleNewLocation();
    }

    @Override
    protected void handleNewLocation() {

        final String trackUidInfoWindow = trackUidMarkerWithInfoWindowOpen();

        mMap.clear();
        markers.clear();

        int transparentBlue = 0x2f0000ff;
        int transBlueBorder = 0x000000ff;

        // Add a circle around the current location
        mMap.addCircle(new CircleOptions()
                .center(User.instance.getLocation())
                .radius(User.instance.getPreferredRadius())
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));

        // Follow the user (only if no infoWindow is opened)
        if (trackUidInfoWindow == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(User.instance.getLocation()));

        // Add a marker for each starting point inside the preferred radius
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new TrackDatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                List<Track> tracks = TrackDatabaseManagement.initTracksNearMe(data);
                for (Track t : tracks) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(t.getStartingPoint().ToLatLng())
                            .title(t.getName()));
                    m.setTag(t.getTrackUid());

                    // If a marker had its infoWindow opened, reopen it
                    if (trackUidInfoWindow != null && trackUidInfoWindow.equals(t.getTrackUid())) {
                        m.showInfoWindow();
                    }

                    markers.add(m);
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
        i.putExtra("TrackID", (String) marker.getTag());
        startActivity(i);
    }

    /**
     * Check if any present marker on the google map has its infoWindow opened and return the
     * track ID associated to it
     *
     * @return a String, the trackUid associated to the marker
     */
    private String trackUidMarkerWithInfoWindowOpen() {
        for (Marker m : markers) {
            if (m.isInfoWindowShown() && m.getTag() != null) {
                return m.getTag().toString();
            }
        }
        return null;
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
        public View getInfoContents(final Marker marker) {
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.marker_info_window, null);

            TextView title = view.findViewById(R.id.marker_window_title);

            final TextView lenText = view.findViewById(R.id.marker_window_text_len);
            final TextView diffText = view.findViewById(R.id.marker_window_text_diff);
            final TextView likeText = view.findViewById(R.id.marker_window_text_like);

            // Set title
            title.setText(marker.getTitle());

            TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new TrackDatabaseManagement.OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot data) {
                    // Get the correct track by it's id
                    List<Track> tracks = TrackDatabaseManagement.initTracksNearMe(data);
                    Track track = null;
                    for (Track t : tracks) {
                        if (t.getTrackUid() == marker.getTag())
                            track = t;
                    }

                    // Get other info from the track (should never be null be we check just in case)
                    if (track != null) {
                        TrackProperties tp = track.getProperties();
                        lenText.setText(tp.getLength() + " m");
                        diffText.setText(tp.getHeightDifference() + " m");
                        likeText.setText(tp.getLikes() + "");
                    }
                }

                @Override
                public void onFailed(DatabaseError databaseError) {
                    Log.d("DB Read: ", "Failed to read data from DB in InfoWindowGoogleMap.");
                }
            });
            return view;
        }
    }
}
