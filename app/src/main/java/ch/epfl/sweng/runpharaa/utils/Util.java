package ch.epfl.sweng.runpharaa.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Util {

    public static Location locationFromLatLng(LatLng p) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(p.latitude);
        l.setLongitude(p.longitude);
        l.setAltitude(0);
        l.setAccuracy(1);
        l.setTime(System.currentTimeMillis());
        return l;
    }

    public static void copyStream(InputStream is, OutputStream os) {
        final int bufferSize = 1024;
        try {
            byte[] bytes = new byte[bufferSize];
            while(true) {
                int count = is.read(bytes, 0, bufferSize);
                if(count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public static double[] computeDistanceAndElevationChange(Location[] locations) {
        double maxAltitude = Double.NEGATIVE_INFINITY;
        double minAltitude = Double.POSITIVE_INFINITY;
        double[] res = new double[2];
        for (int i = 0; i < locations.length; ++i) {
            Location l = locations[i];
            double a = l.getAltitude();
            if (a < minAltitude)
                minAltitude = a;
            if (a > maxAltitude)
                maxAltitude = a;
            if (i != 0)
                res[0] += l.distanceTo(locations[i - 1]);
        }
        res[1] = maxAltitude - minAltitude;
        return res;
    }


    public static Bitmap InputStreamToBitmap(InputStream inputStream){
        //Resize and compress image
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        final int REQUIRED_SIZE = 100;
        Bitmap trackPhotoTemp = BitmapFactory.decodeStream(inputStream, null, options);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        trackPhotoTemp.compress(Bitmap.CompressFormat.PNG, 75, out);
        return BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
    }
}
