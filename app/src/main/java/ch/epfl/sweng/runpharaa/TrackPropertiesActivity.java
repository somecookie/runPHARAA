package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;

public class TrackPropertiesActivity extends AppCompatActivity {

    //TODO: Check if ScrollView is working!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        Intent intent = getIntent();
        final int trackID = intent.getIntExtra("TrackID", 0);
        final Track track = getTrackByID(FAKE_USER.tracksNearMe(), trackID);

        ImageView trackBackground = findViewById(R.id.trackBackgroundID);
        trackBackground.setImageResource(track.getImage());

        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getLocation());

        TextView trackCreator = findViewById(R.id.trackCreatorID);
        trackCreator.setText(/*track.getCreator_id()*/"Creator: Test User");

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText("Duration: " + track.getAverage_time_length() + " minutes");

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText("Length: " + Double.toString(track.getTrack_length()) + "m");

        /*
        TextView trackHeightDifference = findViewById(R.id.trackHeightDiffID);
        trackHeightDifference.setText("Height Difference: " + Double.toString(track.getHeight_diff())); //TODO: Figure out height difference.
        */

        TextView trackLikes = findViewById(R.id.trackLikesID);
        trackLikes.setText("Likes: " + track.getLikes());

        TextView trackFavourites = findViewById(R.id.trackFavouritesID);
        trackFavourites.setText("Favourites: " + track.getFavourites());

        ToggleButton toggleLike = findViewById(R.id.buttonLikeID);
        ToggleButton toggleFavorite = findViewById(R.id.buttonFavoriteID);

        // Check if the user already liked this track and toggle the button accordingly
        if (FAKE_USER.alreadyLiked(trackID)) {
            toggleLike.setChecked(true);
        } else {
            toggleLike.setChecked(false);
        }

        toggleLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (!FAKE_USER.alreadyLiked(trackID)) {
                        track.addLike();
                        FAKE_USER.like(trackID);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                TextView trackLikesUpdated = findViewById(R.id.trackLikesID);
                                trackLikesUpdated.setText("Likes: " + track.getLikes());
                            }
                        });
                    }
                } else {
                    if (FAKE_USER.alreadyLiked(trackID)) {
                        track.removeLike();
                        FAKE_USER.unlike(trackID);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                TextView trackLikesUpdated = findViewById(R.id.trackLikesID);
                                trackLikesUpdated.setText("Likes: " + track.getLikes());
                            }
                        });
                    }
                }
            }
        });

        toggleFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            track.addFavourite();
                            TextView trackFavouriteUpdated = findViewById(R.id.trackFavouritesID);
                            trackFavouriteUpdated.setText("Favourites: " + track.getFavourites());
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            track.removeFavourite();
                            TextView trackFavouriteUpdated = findViewById(R.id.trackFavouritesID);
                            trackFavouriteUpdated.setText("Favourites: " + track.getFavourites());
                        }
                    });
                }
            }
        });
        /*
        TextView Tags = findViewById(R.id.trackTagsID);
        Tags.setText();
        */
    }

    private Track getTrackByID(ArrayList<Track> tracks, int trackID) {
        for (Track t: tracks) {
            if (t.getUid() == trackID) {
                return t;
            }
        }
        return null;
    }
}
