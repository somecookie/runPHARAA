package ch.epfl.sweng.runpharaa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.support.v4.app.FragmentActivity;

public abstract class LocationUpdateReceiverActivity extends FragmentActivity {

    protected BroadcastReceiver receiver;
    protected Location location;

    protected abstract void handleNewLocation();

    /**
     * Initializes the broadcast receiver
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
    protected void stopGeoLocalisation() {
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        stopService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
    }

}
