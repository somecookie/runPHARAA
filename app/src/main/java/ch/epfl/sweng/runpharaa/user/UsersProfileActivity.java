package ch.epfl.sweng.runpharaa.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.User;

public class UsersProfileActivity extends AppCompatActivity {

    private User actualUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        actualUser = User.instance;

        TextView v = findViewById(R.id.user_name);
        v.setText(actualUser.getName());

        TextView v1 = findViewById(R.id.nbTracks);
        int nbTracks = actualUser.getCreatedTracks().size();
        v1.setText(Integer.toString(nbTracks));

        TextView v2 = findViewById(R.id.nbFav);
        int nbFav = actualUser.getFavoriteTracks().size();
        v2.setText(Integer.toString(nbFav));

        new DownloadImageTask((ImageView) findViewById(R.id.profile_picture))
                .execute(actualUser.getPicture().toString());
    }

    /**
     * Private class to download Uri images and set the ImageView to the image downloaded
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = BitmapFactory.decodeResource(getResources(), R.drawable.default_photo);
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
            }
            return mIcon11;
        }

        /**
         * Set the ImageView to the bitmap result
         * @param result
         */
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}