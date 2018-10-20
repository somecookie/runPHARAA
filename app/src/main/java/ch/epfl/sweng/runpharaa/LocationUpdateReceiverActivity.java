package ch.epfl.sweng.runpharaa;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

public abstract class LocationUpdateReceiverActivity extends FragmentActivity {

    protected BroadcastReceiver receiver;
    protected Location location;

    protected abstract void handleNewLocation();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGeoLocalisation();
        initReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGeoLocalisation();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            stopGeoLocalisation();
            if (receiver != null)
                unregisterReceiver(receiver);
        }
    }

    /**
     * Initializes the broadcast receiver to receive updates on the location
     */
    protected void initReceiver() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Receive new location
                    location = (Location) intent.getExtras().get("new_location");
                    if (location != null)
                        handleNewLocation();
                }
            };
        }
        registerReceiver(receiver, new IntentFilter("location_update"));
    }

    /**
     * Update the user location when receiving a new location and update the markers
     */
    protected void startGeoLocalisation() {
        if(!isServiceRunning(GpsService.class)) {
            Intent i = new Intent(getApplicationContext(), GpsService.class);
            startService(i);
        }
    }

    /**
     * Update the user location when receiving a new location and update the markers
     */
    protected void stopGeoLocalisation() {
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        stopService(i);
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
