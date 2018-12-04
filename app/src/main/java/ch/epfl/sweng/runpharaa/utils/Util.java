package ch.epfl.sweng.runpharaa.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.runpharaa.tracks.TrackType;

public interface Util {

    static Location locationFromLatLng(LatLng p) {
        Location l = new Location(LocationManager.GPS_PROVIDER);
        l.setLatitude(p.latitude);
        l.setLongitude(p.longitude);
        l.setAltitude(0);
        l.setAccuracy(1);
        l.setTime(System.currentTimeMillis());
        return l;
    }

    static void copyStream(InputStream is, OutputStream os) {
        final int bufferSize = 1024;
        try {
            byte[] bytes = new byte[bufferSize];
            while (true) {
                int count = is.read(bytes, 0, bufferSize);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A one color image.
     *
     * @param width
     * @param height
     * @param color
     * @return A one color image with the given width and height.
     */
    static Bitmap createImage(int width, int height, int color) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(0F, 0F, (float) width, (float) height, paint);
        return bitmap;
    }

    static double[] computeDistanceAndElevationChange(Location[] locations) {

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

    static Bitmap decodeSampledBitmap(byte[] data, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new ByteArrayInputStream(data), null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(new ByteArrayInputStream(data), null, options);
    }

    static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /*static Intent getTwitterIntent(Context ctx, String shareText) {
        Intent shareIntent;

        PackageManager pm = ctx.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo("com.twitter.android", PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        if (installed) {
            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setClassName("com.twitter.android",
                    "com.twitter.android.PostActivity");
            shareIntent.setType("text/*");
            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
            return shareIntent;
        } else {
            String tweetUrl = "https://twitter.com/intent/tweet?text=" + shareText;
            Uri uri = Uri.parse(tweetUrl);
            shareIntent = new Intent(Intent.ACTION_VIEW, uri);
            return shareIntent;
        }
    }*/

    /**
     * Remove the accents of the string and transform it to lower case
     *
     * @param s the string we want to format
     * @return the formatted string
     */
    static String formatString(String s) {
        Required.nonNull(s, "Cannot format null string");
        if (s.isEmpty()) return "";

        s = s.toLowerCase();
        s = s.replaceAll("[èéêë]", "e");
        s = s.replaceAll("[ûù]", "u");
        s = s.replaceAll("[ïî]", "i");
        s = s.replaceAll("[àâ]", "a");
        s = s.replaceAll("Ô", "o");
        return s;
    }

    static Set<TrackType> addTypes(boolean[] checkedTypes) {
        Set<TrackType> types = new HashSet<>();
        for (int i = 0; i < checkedTypes.length; i++) {
            if (checkedTypes[i]) types.add(TrackType.values()[i]);
        }
        return types;
    }
}
