package ch.epfl.sweng.runpharaa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class CreateTrackActivity2 extends FragmentActivity implements OnMapReadyCallback {

    public static final int IMAGE_GALLERY_REQUEST = 20;

    private GoogleMap map;
    private TextView totalDistanceText, totalAltitudeText;
    private EditText nameText;
    private ImageView trackImage;

    private EditText mTime;
    private SeekBar mSeekBar;
    private TextView mDiffText;

    private double minAltitude = Double.POSITIVE_INFINITY;
    private double maxAltitude = Double.NEGATIVE_INFINITY;
    private Location[] locations;
    private LatLng[] points;
    private Bitmap trackPhoto;
    private int difficulty = 3;
    private double time;

    private boolean propertiesSet = false;
    private boolean typesSet = false;
    private TrackProperties trackProperties;

    private double totalDistance, totalAltitudeChange;

    private String[] listTypes;
    private boolean[] checkedTypes;
    private Set<TrackType> types = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track_2);

        listTypes = getResources().getStringArray(R.array.track_types);
        checkedTypes = new boolean[listTypes.length];

        totalDistanceText = findViewById(R.id.create_text_total_distance);
        totalAltitudeText = findViewById(R.id.create_text_total_altitude);
        nameText = findViewById(R.id.create_text_name);
        Button createButton = findViewById(R.id.create_track_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create track
                if (trackPhoto == null) {
                    trackPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
                }

                if (!propertiesSet) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.properties_not_set), Toast.LENGTH_LONG).show();
                } else if (!typesSet) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.types_not_set), Toast.LENGTH_LONG).show();
                } else if (nameText.getText().toString().isEmpty()) {
                    Toast.makeText(getBaseContext(), getResources().getString(R.string.need_name), Toast.LENGTH_LONG).show();
                } else {
                    // TODO: add track to created tracks
                    trackProperties = new TrackProperties(totalDistance, totalAltitudeChange, time, difficulty, types);
                    Track track = new Track(User.instance.getID(), trackPhoto, nameText.getText().toString(), CustLatLng.LatLngToCustLatLng(Arrays.asList(points)), trackProperties);
                    Track.allTracks.add(track);
                    finish();
                }
            }
        });

        //Open Gallery view when we click on the button
        Button addPhotoFromGallery = findViewById(R.id.add_photo_from_gallery);
        addPhotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //invoke the image gallery
                Intent photoPickIntent = new Intent(Intent.ACTION_PICK);
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureDirectoryPath = pictureDirectory.getPath();

                //get URI representation
                Uri data = Uri.parse(pictureDirectoryPath);

                //set the data and type (all images types)
                photoPickIntent.setDataAndType(data, "image/*");

                //invoke the activity and get something back
                startActivityForResult(photoPickIntent, IMAGE_GALLERY_REQUEST);
            }
        });

        Button propButton = findViewById(R.id.set_properties);
        propButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateTrackActivity2.this);
                final View mView = getLayoutInflater().inflate(R.layout.dialog_properties, null);

                mTime = mView.findViewById(R.id.time);

                mDiffText = mView.findViewById(R.id.diff_text);
                mDiffText.setText(getResources().getString(R.string.difficulty_is) + difficulty);

                mSeekBar = mView.findViewById(R.id.difficulty_bar);
                mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        difficulty = progress;
                        mDiffText.setText(getResources().getString(R.string.difficulty_is) + difficulty);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                mBuilder.setCancelable(false);

                mBuilder.setPositiveButton(getResources().getText(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!mTime.getText().toString().isEmpty()) {
                            time = Double.parseDouble(mTime.getText().toString());
                        } else {
                            Toast.makeText(getBaseContext(), getResources().getString(R.string.default_time), Toast.LENGTH_LONG).show();
                            time = totalDistance / 133;
                        }
                        propertiesSet = true;
                    }
                });

                mBuilder.setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        difficulty = 3;
                        dialog.dismiss();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        });

        Button typeButton = findViewById(R.id.types);
        typeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateTrackActivity2.this);
                mBuilder.setTitle(getResources().getString(R.string.choose_types));
                mBuilder.setMultiChoiceItems(listTypes, checkedTypes, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        switch (listTypes[which]) {
                            case "City":
                                if (isChecked) types.add(TrackType.CITY);
                                else types.remove(TrackType.CITY);
                                break;
                            case "Forest":
                                if (isChecked) types.add(TrackType.FOREST);
                                else types.remove(TrackType.FOREST);
                                break;
                            case "Mountain":
                                if (isChecked) types.add(TrackType.SEASIDE);
                                else types.remove(TrackType.SEASIDE);
                                break;
                            case "Beach":
                                if (isChecked) types.add(TrackType.BEACH);
                                else types.remove(TrackType.BEACH);
                                break;
                            case "Countryside":
                                if (isChecked) types.add(TrackType.COUNTRYSIDE);
                                else types.remove(TrackType.COUNTRYSIDE);
                                break;
                            case "Seaside":
                                if (isChecked) types.add(TrackType.SEASIDE);
                                else types.remove(TrackType.SEASIDE);
                                break;
                        }
                    }
                });

                mBuilder.setCancelable(false);

                mBuilder.setPositiveButton(getResources().getText(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!types.isEmpty()) {
                            typesSet = true;
                        }
                    }
                });

                mBuilder.setNegativeButton(getResources().getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        types.clear();
                        for (int i = 0; i < checkedTypes.length; i++) {
                            checkedTypes[i] = false;
                        }
                        dialog.dismiss();
                    }
                });

                mBuilder.create().show();
            }
        });

        trackImage = findViewById(R.id.track_photo);

        // Get map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_map_view);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == IMAGE_GALLERY_REQUEST) {
            //get the address of the image on the SD card
            Uri imageUri = data.getData();

            //stream to read the image data
            InputStream inputStream;

            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                //get a bitmap from the stream
                trackPhoto = BitmapFactory.decodeStream(inputStream);

                //Add a preview of the photo
                trackImage.setVisibility(View.VISIBLE);
                trackImage.setImageBitmap(trackPhoto);

            } catch (FileNotFoundException e) {
                Toast.makeText(getBaseContext(), "Unable to open image", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    /**
     * Extracts information from the bundle
     */
    private void handleExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Parcelable[] a = bundle.getParcelableArray("locations");
            locations = Arrays.copyOf(a, a.length, Location[].class);
            a = bundle.getParcelableArray("points");
            points = Arrays.copyOf(a, a.length, LatLng[].class);

            computeValues();

            // Show extracted info
            totalDistanceText.setText(String.format("Total distance: %.2f m", totalDistance));
            totalAltitudeText.setText(String.format("Total altitude difference: %.2f m", totalAltitudeChange));
        }
    }

    /**
     * Computes the total distance and total altitude difference of the track
     */
    private void computeValues() {
        // TODO: will we store this info somewhere ? What additional info do we want to show ?
        for (int i = 0; i < locations.length; ++i) {
            Location l = locations[i];
            updateMinAndMaxAltitude(l.getAltitude());
            if (i != 0)
                totalDistance += l.distanceTo(locations[i - 1]);
        }
        // TODO: the altitudes completely off right now, try to fix
        totalAltitudeChange = maxAltitude - minAltitude;
    }

    /**
     * Updates the max and min altitudes according to a new altitude
     *
     * @param a the new altitude
     */
    private void updateMinAndMaxAltitude(double a) {
        if (a < minAltitude)
            minAltitude = a;
        if (a > maxAltitude)
            maxAltitude = a;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Make map static
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return true;
            }
        });
        // Adapt padding to fit markers
        map.setPadding(50, 150, 50, 50);
        handleExtras();
        drawTrackOnMap();
    }

    /**
     * Draws the full track and markers on the map
     */
    private void drawTrackOnMap() {
        if (map != null && points != null) {
            // Get correct zoom
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng point : points)
                boundsBuilder.include(point);
            LatLngBounds bounds = boundsBuilder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int)(getResources().getDisplayMetrics().heightPixels * 0.65);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
            // Add lines
            map.addPolyline(new PolylineOptions().addAll(Arrays.asList(points)));
            // Add markers (start = green, finish = red)
            map.addMarker(new MarkerOptions().position(points[0]).icon(defaultMarker(150)).alpha(0.8f));
            map.addMarker(new MarkerOptions().position(points[points.length - 1]).icon(defaultMarker(20)).alpha(0.8f));
        }
    }
}
