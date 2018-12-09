package ch.epfl.sweng.runpharaa.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import ch.epfl.sweng.runpharaa.MainActivity;
import ch.epfl.sweng.runpharaa.R;
import ch.epfl.sweng.runpharaa.database.UserDatabaseManagement;

/**
 * Class that handle the received notification
 */
public class FirebaseNotification extends FirebaseMessagingService {

    private static final int idRequest = 1410;
    /**
     * The method receive a remoteMessage that will be converted into a android notification
     * @param remoteMessage
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.i("WESHHHHHHHHHHHHHHHHHH", "Received Message");

        // Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, idRequest,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this, "notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(idRequest, notificationBuilder.build());
    }


    /**
     * Overriding the method that updates the token creation to put it in the firebase as well
     * @param token
     */
    @Override
    public void onNewToken(String token){
        UserDatabaseManagement.writeNotificationKey(token);
    }
}


