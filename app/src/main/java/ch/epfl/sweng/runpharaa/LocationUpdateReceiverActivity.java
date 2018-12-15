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

    private BroadcastReceiver receiver;

    protected abstract void handleNewLocation();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            if (receiver != null)
                unregisterReceiver(receiver);
        }
    }

    /**
     * Initializes the broadcast receiver to receive updates on the location
     */
    private void initReceiver() {
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    handleNewLocation();
                }
            };
        }
        registerReceiver(receiver, new IntentFilter("location_update"));
    }

}
