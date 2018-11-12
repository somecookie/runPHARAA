package ch.epfl.sweng.runpharaa.location;

import android.app.Service;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public abstract class GpsService extends Service {
    protected static Location currentLocation;

    public abstract Location getCurrentLocation();
    public abstract void setNewLocation(Context context, Location location);

    protected abstract void updateAndSendNewLocation(Location location);

    public void setTimeInterval(int interval) {}
    public void setMinTimeInterval(int interval) {}
    public void setMinDistanceInterval(int interval) {}
}
