package ch.epfl.sweng.runpharaa;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.database.TrackDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.FirebaseTrackAdapter;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Config;
import ch.epfl.sweng.runpharaa.utils.PropertiesOnClickListener;
import ch.epfl.sweng.runpharaa.utils.Util;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

public class CreateTrackActivity2 extends FragmentActivity implements OnMapReadyCallback {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int REQ_WIDTH = 480;
    public static final int REQ_HEIGHT = 200;

    private GoogleMap map;
    private TextView totalDistanceText, totalAltitudeText;
    private EditText nameText;
    private ImageView trackImage;

    private Location[] locations;
    private LatLng[] points;
    private Bitmap trackPhoto;

    private boolean typesSet = false;
    private TrackProperties trackProperties;

    private double totalAltitudeChange;

    private String[] listTypesStr;
    private boolean[] checkedTypes;
    private Set<TrackType> types = new HashSet<>();

    private PropertiesOnClickListener propListener = new PropertiesOnClickListener(this, true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track_2);

        listTypesStr = getResources().getStringArray(R.array.track_types);
        checkedTypes = new boolean[listTypesStr.length];

        totalDistanceText = findViewById(R.id.create_text_total_distance);
        totalAltitudeText = findViewById(R.id.create_text_total_altitude);
        nameText = findViewById(R.id.create_text_name);
        Button createButton = findViewById(R.id.create_track_button);

        createButton.setOnClickListener(v -> {

            // Create track
            if (trackPhoto == null) {
                trackPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
            }

            if (!propListener.isPropertiesSet()) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.properties_not_set), Toast.LENGTH_SHORT).show();
            } else if (!typesSet) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.types_not_set), Toast.LENGTH_SHORT).show();
            } else if (nameText.getText().toString().isEmpty()) {
                Toast.makeText(getBaseContext(), getResources().getString(R.string.need_name), Toast.LENGTH_SHORT).show();
            } else {
                trackProperties = new TrackProperties(propListener.getTotalDistance(),
                        totalAltitudeChange, propListener.getTime(), propListener.getDifficulty(),
                        types);

                FirebaseTrackAdapter track = new FirebaseTrackAdapter(nameText.getText().toString(),
                        User.instance.getUid(), User.instance.getName(), trackPhoto,
                        CustLatLng.LatLngToCustLatLng(Arrays.asList(points)), trackProperties,
                        new ArrayList<>());

                TrackDatabaseManagement.writeNewTrack(track);

                finish();
            }
        });
        //Open Gallery view when we click on the button
        Button addPhotoFromGallery = findViewById(R.id.add_photo_from_gallery);
        addPhotoFromGallery.setOnClickListener(v -> {
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
        });

        Button propButton = findViewById(R.id.set_properties);
        propButton.setOnClickListener(propListener);

        Button typeButton = findViewById(R.id.types);
        typeButton.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateTrackActivity2.this);
            mBuilder.setTitle(getResources().getString(R.string.choose_types));
            mBuilder.setMultiChoiceItems(listTypesStr, checkedTypes, (dialog, which, isChecked) -> checkedTypes[which] = isChecked);

            mBuilder.setCancelable(false);

            mBuilder.setPositiveButton(getResources().getText(R.string.OK), (dialog, which) -> {

                types.clear();

                for (int i = 0; i < checkedTypes.length; i++) {
                    if (checkedTypes[i]) types.add(TrackType.values()[i]);
                }

                if (!types.isEmpty()) {
                    typesSet = true;
                }
            });

            mBuilder.setNegativeButton(getResources().getString(R.string.dismiss), (dialog, which) -> {
                types.clear();
                for (int i = 0; i < checkedTypes.length; i++) {
                    checkedTypes[i] = false;
                }
                dialog.dismiss();
            });

            mBuilder.create().show();
        });

        trackImage = findViewById(R.id.track_photo);

        // Get fakeMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.create_map_view);
        mapFragment.getMapAsync(this);
        if (Config.isTest) {
            onMapReady(Config.getFakeMap());
        }
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
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Util.copyStream(inputStream, baos);
                byte[] bytes = baos.toByteArray();
                trackPhoto = Util.decodeSampledBitmap(bytes, REQ_WIDTH, REQ_HEIGHT);
                //Add a preview of the photo
                trackImage.setVisibility(View.VISIBLE);
                trackImage.setImageBitmap(trackPhoto);

            } catch (FileNotFoundException e) {
                Toast.makeText(getBaseContext(), "Unable to open image", Toast.LENGTH_SHORT).show();
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

            double[] values = Util.computeDistanceAndElevationChange(locations);
            propListener.setTotalDistance(values[0]);
            totalAltitudeChange = values[1];

            // Show extracted info
            totalDistanceText.setText(String.format("Total distance: %.2f m", propListener.getTotalDistance()));
            totalAltitudeText.setText(String.format("Total altitude difference: %.2f m", totalAltitudeChange));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        // Make fakeMap static
        map.getUiSettings().setAllGesturesEnabled(false);
        map.setOnMarkerClickListener(marker -> true);
        // Adapt padding to fit markers
        map.setPadding(50, 150, 50, 50);
        handleExtras();
        drawTrackOnMap();
    }

    /**
     * Draws the full track and markers on the fakeMap
     */
    private void drawTrackOnMap() {
        if (map != null && points != null) {
            // Get correct zoom
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
            for (LatLng point : points)
                boundsBuilder.include(point);
            LatLngBounds bounds = boundsBuilder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
            // Add lines
            map.addPolyline(new PolylineOptions().addAll(Arrays.asList(points)));
            // Add markers (start = green, finish = red)
            map.addMarker(new MarkerOptions().position(points[0]).icon(defaultMarker(150)).alpha(0.8f));
            map.addMarker(new MarkerOptions().position(points[points.length - 1]).icon(defaultMarker(20)).alpha(0.8f));
        }
    }
}