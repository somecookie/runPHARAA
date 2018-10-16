package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;

public class TrackPropertiesActivity extends AppCompatActivity {

    //TODO: Check if ScrollView is working!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        Intent intent = getIntent();
        int trackID = intent.getIntExtra("TrackID", 0);
        Track track = getTrackByID(User.instance.tracksNearMe(), trackID);

        TrackProperties tp = track.getProperties();

        ImageView trackBackground = findViewById(R.id.trackBackgroundID);
        trackBackground.setImageBitmap(track.getImage());

        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getName());

        TextView trackCreator = findViewById(R.id.trackCreatorID);
        trackCreator.setText(/*track.getCreator_id()*/"Creator: Test User");

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText("Duration: " + tp.getAvgDuration() + " minutes");

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText("Length: " + Double.toString(tp.getLength()) + "m");

        /*
        TextView trackHeightDifference = findViewById(R.id.trackHeightDiffID);
        trackHeightDifference.setText("Height Difference: " + Double.toString(track.getHeight_diff())); //TODO: Figure out height difference.
        */

        //TODO: Add Like and Favourite buttons.

        TextView trackLikes = findViewById(R.id.trackLikesID);
        trackLikes.setText("Likes: " + tp.getLikes());

        TextView trackFavourites = findViewById(R.id.trackFavouritesID);
        trackFavourites.setText("Favourites: " + tp.getFavorites());

        /*
        TextView Tags = findViewById(R.id.trackTagsID);
        Tags.setText();
        */
    }

    private Track getTrackByID(ArrayList<Track> tracks, int trackID) {
        for (Track t: tracks) {
            if (t.getTID() == trackID) {
                return t;
            }
        }
        return null;
    }
}
