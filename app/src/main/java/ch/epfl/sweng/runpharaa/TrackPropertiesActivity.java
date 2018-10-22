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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

        final String trackID = intent.getStringExtra("TrackID");
        final Track track = getTrackByID(Track.allTracks, trackID);

        TrackProperties tp = track.getProperties();

        ImageView trackBackground = findViewById(R.id.trackBackgroundID);
        //trackBackground.setImageBitmap(track.getImage()); //TODO: ERASE
        new DownloadImageTask((ImageView) trackBackground)
                .execute(track.getImageStorageUri());

        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getName());

        TextView trackCreator = findViewById(R.id.trackCreatorID);
        //TODO: make method like getNameFromID(uid)
        trackCreator.setText("By "+track.getTrackUid());

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText("Duration: " + tp.getAvgDuration() + " minutes");

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText("Length: " + Double.toString(tp.getLength()) + " m");

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

    private void updateLikes(Track track1, String trackID) {
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

    private void updateNbFavorites(Track track1, String trackID) {
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

    private Track getTrackByID(ArrayList<Track> tracks, String trackID) {
        for (Track t : tracks) {
            if (t.getTrackUid().equals(trackID)) {
                return t;
            }
        }
        return null;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }


        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;

            Bitmap decoded = null;

            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.PNG, 20, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return decoded;
        }

        /**
         ** Set the ImageView to the bitmap result
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
