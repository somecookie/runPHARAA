package ch.epfl.sweng.runpharaa.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;

public class Util {

    /**
     * A one color image.
     * @param width
     * @param height
     * @param color
     * @return A one color image with the given width and height.
     */
    public static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    @SuppressLint("MissingPermission")
    public static Location getCurrLocation(Activity a){
        LocationManager locationManager = (LocationManager) a.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
}
