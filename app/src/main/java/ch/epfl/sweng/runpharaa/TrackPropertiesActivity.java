package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Set;

import ch.epfl.sweng.runpharaa.cache.ImageLoader;
import ch.epfl.sweng.runpharaa.database.DatabaseManagement;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;
import ch.epfl.sweng.runpharaa.tracks.Track;
import ch.epfl.sweng.runpharaa.tracks.TrackProperties;
import ch.epfl.sweng.runpharaa.tracks.TrackType;
import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Callback;

public class TrackPropertiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        final Intent intent = getIntent();

        DatabaseManagement.mReadDataOnce(DatabaseManagement.TRACKS_PATH, new DatabaseManagement.OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot data) {
                final String trackID = intent.getStringExtra("TrackID");
                final Track track = DatabaseManagement.initTrack(data, trackID);

                TrackProperties tp = track.getProperties();

                ImageView trackBackground = findViewById(R.id.trackBackgroundID);

                ImageLoader.getLoader(getBaseContext()).displayImage(track.getImageStorageUri(), trackBackground, false); // caching

                TextView trackTitle = findViewById(R.id.trackTitleID);
                trackTitle.setText(track.getName());

                TextView trackCreator = findViewById(R.id.trackCreatorID);
                //TODO: make method like getNameFromID(uid) -> once the Users are in DB
                /*
                UserDatabaseManagement.downloadUser(track.getCreatorUid(), new Callback<User>() {
                    @Override
                    public void onSuccess(User value) {
                        trackCreator.setText("By "+ value.getName());
                    }
                });
                */

                TextView trackDuration = findViewById(R.id.trackDurationID);
                trackDuration.setText("Duration: " + tp.getAvgDuration() + " minutes");


                TextView trackLength = findViewById(R.id.trackLengthID);
                trackLength.setText("Length: " + Double.toString(tp.getLength()) + " m");

                TextView trackDifficulty = findViewById(R.id.track_difficulty);
                trackDifficulty.setText("Difficulty: " + Double.toString(tp.getAvgDifficulty()) + " / 5.0");


                TextView trackHeightDifference = findViewById(R.id.trackHeightDiffID);
                trackHeightDifference.setText("Height Difference: " + Double.toString(track.getHeightDifference()) + " m");


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


                TextView trackTags = findViewById(R.id.trackTagsID);
                trackTags.setText(createTagString(track));

            }

            @Override
            public void onFailed(DatabaseError databaseError) {
                Log.d("DB Read: ", "Failed to read data from DB in TrackPropertiesActivity.");
            }
        });
    }

    //TODO: uncomment when u need this, it's f*cking up coverage rn
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

    private void updateLikes(Track track1, String trackID) {
        final Track track = track1;
        if (User.instance.alreadyLiked(trackID)) {
            track.getProperties().removeLike();
            User.instance.unlike(trackID);
            UserDatabaseManagement.removeLikedTrack(trackID);
            //TODO: remove the track from the firebase
        } else {
            track.getProperties().addLike();
            User.instance.like(trackID);
            UserDatabaseManagement.updateLikedTracks(User.instance);
        }
        DatabaseManagement.updateTrack(track);
        runOnUiThread(() -> {
            TextView trackLikesUpdated = findViewById(R.id.trackLikesID);
            trackLikesUpdated.setText(""+track.getProperties().getLikes());
        });

    }

    private void updateNbFavorites(Track track1, String trackID) {
        final Track track = track1;
        if (User.instance.alreadyInFavorites(trackID)) {
            track.getProperties().removeFavorite();
            User.instance.removeFromFavorites(trackID);
            UserDatabaseManagement.removeFavoriteTrack(trackID);

            //TODO: remove the track from the firebase
        } else {
            track.getProperties().addFavorite();
            User.instance.addToFavorites(trackID);
        }

        DatabaseManagement.updateTrack(track);
        UserDatabaseManagement.updateFavoriteTracks(User.instance);
        runOnUiThread(() -> {
            TextView trackFavoritesUpdated = findViewById(R.id.trackFavouritesID);
            trackFavoritesUpdated.setText(""+track.getProperties().getFavorites());
        });

    }

    /*private Track getTrackByID(ArrayList<Track> tracks, String trackID) {
        for (Track t : tracks) {
            if (t.getTrackUid().equals(trackID)) {
                return t;
            }
        }
        return null;
    }*/
}