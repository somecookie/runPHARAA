package ch.epfl.sweng.runpharaa.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import ch.epfl.sweng.runpharaa.R;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private Resources res;

    public DownloadImageTask(ImageView bmImage, Resources res) {
        this.bmImage = bmImage;
        this.res = res;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = BitmapFactory.decodeResource(res, R.drawable.default_photo);
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
     *
     * @param result
     */
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}