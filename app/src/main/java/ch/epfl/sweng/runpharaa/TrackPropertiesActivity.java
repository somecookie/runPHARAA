package ch.epfl.sweng.runpharaa;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static ch.epfl.sweng.runpharaa.User.FAKE_USER;

public class TrackPropertiesActivity extends AppCompatActivity {

    //TODO: Check if ScrollView is working!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_properties);
        Intent intent = getIntent();
        String trackID = intent.getStringExtra("TrackID");
        Track track = getTrackByID(FAKE_USER.tracksNearMe(), trackID);

        ImageView trackBackground = findViewById(R.id.trackBackgroundID);
        //trackBackground.setImageResource(track.getImage());
        new DownloadImageTask((ImageView) trackBackground)
                .execute(track.getImageStorageUri());

        TextView trackTitle = findViewById(R.id.trackTitleID);
        trackTitle.setText(track.getLocation());

        TextView trackCreator = findViewById(R.id.trackCreatorID);
        trackCreator.setText(/*track.getCreator_id()*/"Creator: Test User");

        TextView trackDuration = findViewById(R.id.trackDurationID);
        trackDuration.setText("Duration: " + track.getAverageTimeLength() + " minutes");

        TextView trackLength = findViewById(R.id.trackLengthID);
        trackLength.setText("Length: " + Double.toString(track.getTrackLength()) + "m");

        /*
        TextView trackHeightDifference = findViewById(R.id.trackHeightDiffID);
        trackHeightDifference.setText("Height Difference: " + Double.toString(track.getHeight_diff())); //TODO: Figure out height difference.
        */

        //TODO: Add Like and Favourite buttons.

        TextView trackLikes = findViewById(R.id.trackLikesID);
        trackLikes.setText("Likes: " + track.getLikes());

        TextView trackFavourites = findViewById(R.id.trackFavouritesID);
        trackFavourites.setText("Favourites: " + track.getFavourites());

        /*
        TextView Tags = findViewById(R.id.trackTagsID);
        Tags.setText();
        */
    }

    private Track getTrackByID(ArrayList<Track> tracks, String trackID) {
        for (Track t: tracks) {
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

                //TODO Might erase.
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                mIcon11.compress(Bitmap.CompressFormat.PNG, 20, out);
                decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return /*mIcon11*/decoded;
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
