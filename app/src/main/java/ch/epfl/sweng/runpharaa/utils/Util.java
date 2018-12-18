package ch.epfl.sweng.runpharaa.utils;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.login.LoginActivity;

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

    static void goHome(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.compress_to_top_left);
    }

    static void prepareHomeButton(AppCompatActivity activity) {
        activity.getSupportActionBar().setHomeButtonEnabled(true);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
    }

    /**
     * Sign out the current user of the app, finish the current {@link Activity} and launch the {@link LoginActivity}
     * @param activity the current {@link Activity}
     */
    static void signOut(Activity activity) {
        if (Config.isTest) {
            goToLogin(activity);
            return;
        }
        FirebaseAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(activity.getString(R.string.default_web_client_id)).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(activity, gso);
        mGoogleSignInClient.signOut().addOnCompleteListener(activity, task -> goToLogin(activity));
    }

    /**
     * Launch the {@link LoginActivity} and finish the current one
     * @param activity the current {@link Activity}
     */
    static void goToLogin(Activity activity) {
        Toast.makeText(activity.getBaseContext(), activity.getResources().getString(R.string.loggedOut), Toast.LENGTH_SHORT).show();
        Intent login = new Intent(activity.getBaseContext(), LoginActivity.class);
        login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(login);
        activity.finish();
    }
}
