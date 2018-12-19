package ch.epfl.sweng.runpharaa.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.runpharaa.user.User;
import ch.epfl.sweng.runpharaa.utils.Util;

public final class FakeGpsService extends GpsService {

    public static FakeGpsService GOOGLE = new FakeGpsService(new LatLng(37.422, -122.084));
    public static FakeGpsService SAT = new FakeGpsService(Util.locationFromLatLng(new LatLng(46.520566, 6.567820)));

    public FakeGpsService(Location initialLocation) {
        currentLocation = initialLocation;
    }

    public FakeGpsService(LatLng initialLocation) {
        currentLocation = Util.locationFromLatLng(initialLocation);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        updateAndSendNewLocation(currentLocation);
    }

    @Override
    public Location getCurrentLocation() {
        return currentLocation;
    }

    @Override
    public void setNewLocation(Context context, Location location) {
        if(User.instance != null)
            User.instance.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        currentLocation = location;
        context.sendBroadcast(new Intent("location_update"));
    }

    @Override
    protected void updateAndSendNewLocation(Location location) {
        if(User.instance != null)
            User.instance.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        currentLocation = location;
        sendBroadcast(new Intent("location_update"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
