package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;

public class TrackPropertiesActivity extends AppCompatActivity {


    //TODO: Check if ScrollView is working!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        Intent intent = getIntent();

        final int trackID = intent.getIntExtra("TrackID", 0);
        final Track track = getTrackByID(Track.allTracks, trackID);

        TrackProperties tp = track.getProperties();

        ImageView trackBackground = findViewById(R.id.trackBackgroundID);
        trackBackground.setImageBitmap(track.getImage());

        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getName());

        TextView trackCreator = findViewById(R.id.trackCreatorID);

        //trackCreator.setText("By "+track.getCreatorName());
        trackCreator.setText(String.format(getResources().getString(R.string.by), track.getCreatorName()));

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText(String.format(getResources().getString(R.string.duration), tp.getAvgDuration()));

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText(String.format(getResources().getString(R.string.distance), tp.getLength()));

        TextView trackDifficulty = findViewById(R.id.track_difficulty);
        trackDifficulty.setText(String.format(getResources().getString(R.string.difficulty), tp.getAvgDifficulty()));

        TextView trackTags = findViewById(R.id.trackTagsID);
        trackTags.setText(createTagString(track));


        /*
        TextView trackHeightDifference = findViewById(R.id.trackHeightDiffID);
        trackHeightDifference.setText("Height Difference: " + Double.toString(track.getHeight_diff())); //TODO: Figure out height difference.
        */

        TextView trackLikes = findViewById(R.id.trackLikesID);
        trackLikes.setText(""+tp.getLikes());

        TextView trackFavourites = findViewById(R.id.trackFavouritesID);
        trackFavourites.setText(""+tp.getFavorites());

        ToggleButton toggleLike = findViewById(R.id.buttonLikeID);
        ToggleButton toggleFavorite = findViewById(R.id.buttonFavoriteID);

        // Check if the user already liked this track and toggle the button accordingly
        toggleLike.setChecked(User.instance.alreadyLiked(trackID));

        toggleLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    updateLikes(track, trackID);
                } else {
                    updateLikes(track, trackID);
                }
            }
        });

        // Check if the track already in favorites and toggle the button accordingly
        toggleFavorite.setChecked(User.instance.alreadyInFavorites(trackID));

        toggleFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    updateNbFavorites(track, trackID);
                } else {
                    updateNbFavorites(track, trackID);
                }
            }
        });
        /*
        TextView Tags = findViewById(R.id.trackTagsID);
        Tags.setText();
        */
    }

    private String createTagString(Track track) {
        Set<TrackType> typeSet = track.getProperties().getType();
        int nbrTypes = typeSet.size();
        String[] trackType = getResources().getStringArray(R.array.track_types);

        String start = (nbrTypes > 1)?"Tags: ":"Tag: ";

        StringBuilder sb = new StringBuilder();
        sb.append(start);

        int i = 0;

        for(TrackType tt : typeSet){

            sb.append(trackType[TrackType.valueOf(tt.name()).ordinal()]);
            if(i < nbrTypes - 1) sb.append(", ");

            i++;
        }

        return sb.toString();

    }


    private void updateLikes(Track track1, int trackID) {
        final Track track = track1;
        if (User.instance.alreadyLiked(trackID)) {
            track.getProperties().removeLike();
            User.instance.unlike(trackID);
        } else {
            track.getProperties().addLike();
            User.instance.like(trackID);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                TextView trackLikesUpdated = findViewById(R.id.trackLikesID);
                trackLikesUpdated.setText(""+track.getProperties().getLikes());
            }
        });

    }

    private void updateNbFavorites(Track track1, int trackID) {
        final Track track = track1;
        if (User.instance.alreadyInFavorites(trackID)) {
            track.getProperties().removeFavorite();
            User.instance.removeFromFavorites(trackID);
        } else {
            track.getProperties().addFavorite();
            User.instance.addToFavorites(trackID);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                TextView trackFavoritesUpdated = findViewById(R.id.trackFavouritesID);
                trackFavoritesUpdated.setText(""+track.getProperties().getFavorites());
            }
        });

    }


    private Track getTrackByID(ArrayList<Track> tracks, int trackID) {
        for (Track t : tracks) {
            if (t.getTID() == trackID) {
                return t;
            }
        }
        return null;
    }
}
