package ch.epfl.sweng.runpharaa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;
import ch.epfl.sweng.runpharaa.utils.Config;

public final class MapsActivity extends LocationUpdateReceiverActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private static final int transparentBlue = 0x2f0000ff;
    private static final int transBlueBorder = 0x000000ff;
    private GoogleMap mMap;
    private TextView testText;
    private List<Marker> markers; // used to check if a windowInfo is opened
    private Map<String, TrackProperties> markerToTP = new HashMap<>();
    private boolean userFocused = true;
    private LatLng longClickLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        testText = findViewById(R.id.maps_test_text);

        // Obtain the SupportMapFragment and get notified when the fakeMap is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        markers = new ArrayList<>();
        if(Config.isTest){
            onMapReady(Config.getFakeMap());
        }
    }

    /**
     * Manipulates the fakeMap once available.
     * This callback is triggered when the fakeMap is ready to be used.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapLongClickListener((GoogleMap.OnMapLongClickListener) (pos) -> {
            longClickLocation = pos;
            userFocused = false;

            mMap.addMarker(new MarkerOptions()
                    .position(longClickLocation)
                    .title("selected Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            handleNewLocation();

        });
        InfoWindowGoogleMap customInfoWindow = new InfoWindowGoogleMap(this);
        mMap.setInfoWindowAdapter(customInfoWindow);
        testText.setText("ready");
        handleNewLocation();
    }

    @Override
    protected void handleNewLocation() {
        final LatLng position = (userFocused) ? User.instance.getLocation() : longClickLocation;

        Context context = this;

        final String trackUidInfoWindow = trackUidMarkerWithInfoWindowOpen();

        mMap.clear();
        markers.clear();

        if (!userFocused) {
            mMap.addMarker(new MarkerOptions()
                    .position(longClickLocation)
                    .title("selected Position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        }

        // Add a circle around the current location
        mMap.addCircle(new CircleOptions()
                .center(position)
                .radius(User.instance.getPreferredRadius())
                .fillColor(transparentBlue)
                .strokeColor(transBlueBorder));

        // Follow the user (only if no infoWindow is opened)
        if (trackUidInfoWindow == null)
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

        // Add a marker for each starting point inside the preferred radius
        TrackDatabaseManagement.mReadDataOnce(TrackDatabaseManagement.TRACKS_PATH, new Callback<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot data) {
                List<Track> tracks = TrackDatabaseManagement.initTracksNearLocation(data, position);
                for (Track t : tracks) {
                    Marker m = mMap.addMarker(new MarkerOptions()
                            .position(t.getStartingPoint().ToLatLng())
                            .title(t.getName()));
                    m.setTag(t.getTrackUid());

                    markerToTP.put((Config.isTest) ? "0" : m.getTag().toString(), t.getProperties());

                    // If a marker had its infoWindow opened, reopen it
                    if (trackUidInfoWindow != null && trackUidInfoWindow.equals(t.getTrackUid())) {
                        m.showInfoWindow();
                    }

                    markers.add(m);
                }
            }

            @Override
            public void onError(Exception databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in MapsActivity.");
            }
        });
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        if(marker.getTitle().equals("selected Position")) {
            longClickLocation = null;
            userFocused = true;
            handleNewLocation();
        } else {
            Intent i = new Intent(this, TrackPropertiesActivity.class);
            i.putExtra("TrackID", (String) marker.getTag());
            startActivity(i);
        }
    }

    /**
     * Check if any present marker on the google fakeMap has its infoWindow opened and return the
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
    public class InfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

        private Context context;

        InfoWindowGoogleMap(Context context) {
            this.context = context;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getInfoContents(final Marker marker) {
            if(!marker.getTitle().equals("selected Position")) {
                return getInfoTracks(marker);
            } else {
                return getReturnToLocation();
            }
        }

        /**
         * Handle the windows that display the properties of the track in the fakeMap
         * @param marker
         * @return
         */
        private View getInfoTracks(final Marker marker){
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.marker_info_window, null);

            // Set title
            TextView title = view.findViewById(R.id.marker_window_title);
            title.setText(marker.getTitle());

            TrackProperties tp = markerToTP.get(marker.getTag().toString());

            final TextView lenText = view.findViewById(R.id.marker_window_text_len);
            final TextView diffText = view.findViewById(R.id.marker_window_text_diff);
            final TextView likeText = view.findViewById(R.id.marker_window_text_like);

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);

            lenText.setText(df.format(tp.getLength()) + " m");
            diffText.setText(df.format(tp.getHeightDifference()) + " m");
            likeText.setText(df.format(tp.getLikes()) + "");

            return view;
        }


        /**
         * Handle the window that allow the user to go back from a certain position to its location
         * @return
         */
        private View getReturnToLocation(){
            View view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.marker_other_location, null);

            Button b = view.findViewById(R.id.return_to_location);
            b.setOnClickListener(v -> {
                longClickLocation = null;
                userFocused = true;
                handleNewLocation();
            });

            return view;
        }
    }
}