package ch.epfl.sweng.runpharaa.notification;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Date;

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

        // Create Notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, idRequest,
                intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new
                NotificationCompat.Builder(this, "notification")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("message")))
                .setContentText(remoteMessage.getData().get("message"))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "1";
            @SuppressLint("WrongConstant") NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_MAX);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(channelId);
        }

        //The first parameter is a random number to allow to have multiple different notification
        //Put a fixed number to allow only one notification
        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notificationBuilder.build());
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


