package ch.epfl.sweng.runpharaa;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;

public class CreateTrackActivity2 extends Activity {

    private TextView totalDistanceText, totalElevationText;
    private EditText nameText;
    private double minAltitude = Double.POSITIVE_INFINITY;
    private double maxAltitude = Double.NEGATIVE_INFINITY;
    private Location[] locations;
    private LatLng[] points;
    private double totalDistance, totalElevationChange;
    private Button createButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_track_2);
        totalDistanceText = findViewById(R.id.create_text_total_distance);
        totalElevationText = findViewById(R.id.create_text_total_elevation);
        nameText = findViewById(R.id.create_text_name);
        createButton = findViewById(R.id.create_track_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create track
                Track t = new Track(nameText.getText().toString(), points);
                // TODO: add track to created tracks
                Toast.makeText(getApplicationContext(), "New track was successfully created !", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        // Get the extras
        Bundle b = getIntent().getExtras();
        handleExtras(b);
        // Show extracted info
        totalDistanceText.setText(String.format("Total distance: %.2f m", totalDistance));
        totalElevationText.setText(String.format("Total altitude difference: %.2f m", totalElevationChange));
    }

    /**
     * Extracts information from the bundle
     * @param bundle the bundle to extract the information from
     */
    private void handleExtras(Bundle bundle) {
        if (bundle != null) {
            Parcelable[] a = bundle.getParcelableArray("locations");
            locations = Arrays.copyOf(a, a.length, Location[].class);
            a = bundle.getParcelableArray("points");
            points = Arrays.copyOf(a, a.length, LatLng[].class);

            // TODO: will we store this info somewhere ? What additional info do we want to show ?
            // Get total elevation difference and total distance
            for (int i = 0; i < locations.length; ++i) {
                Location l = locations[i];
                double altitude = l.getAltitude();
                if (altitude < minAltitude)
                    minAltitude = altitude;
                if (altitude > maxAltitude)
                    maxAltitude = altitude;
                if (i != 0)
                    totalDistance += l.distanceTo(locations[i - 1]);
            }
            totalElevationChange = maxAltitude - minAltitude;
        }
    }
}
