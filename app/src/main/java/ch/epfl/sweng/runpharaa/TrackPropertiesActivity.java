package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class TrackPropertiesActivity extends AppCompatActivity {

    //TODO: Check if ScrollView is working!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        Intent intent = getIntent();

        final int trackID = intent.getIntExtra("TrackID", 0);
        final Track track = getTrackByID(Track.allTracks(), trackID);

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
        toggleLike.setChecked(User.instance.alreadyLiked(trackID));

        toggleFavorite.setOnCheckedChangeListener(getListener(track, trackID, User.instance.alreadyLiked(trackID), true));

        // Check if the track already in favorites and toggle the button accordingly
        toggleFavorite.setChecked(User.instance.alreadyInFavorites(trackID));

        toggleFavorite.setOnCheckedChangeListener(getListener(track, trackID, User.instance.alreadyInFavorites(trackID), false));
        /*
        TextView Tags = findViewById(R.id.trackTagsID);
        Tags.setText();
        */
    }

    private CompoundButton.OnCheckedChangeListener getListener(Track t, int tID, boolean cond, boolean l) {
        final boolean condition = cond;
        final Track track = t;
        final int trackID = tID;
        final boolean like = l;
        final String s = like ? "Likes: " + track.getLikes() : "Favorites: " + track.getFavourites();
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked && !condition) {
                    if(like) {
                        track.addLike();
                        User.instance.like(trackID);
                    } else {
                        track.addFavourite();
                        User.instance.addToFavorites(trackID);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView trackFavouriteUpdated = findViewById(R.id.trackFavouritesID);
                            trackFavouriteUpdated.setText(s);
                        }
                    });
                } else if (!isChecked && condition) {
                    if(like) {
                        track.removeLike();
                        User.instance.unlike(trackID);
                    } else {
                        track.removeFavourite();
                        User.instance.removeFromFavorites(trackID);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            TextView trackFavouriteUpdated = findViewById(R.id.trackFavouritesID);
                            trackFavouriteUpdated.setText(s);
                        }
                    });

                }
            }
        };
    }

    private Track getTrackByID(ArrayList<Track> tracks, int trackID) {
        for (Track t : tracks) {
            if (t.getUid() == trackID) {
                return t;
            }
        }
        return null;
    }
}
