package s.ahmadsq.shhi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static android.support.v4.app.NotificationCompat.*;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.


       // Log.d(TAG, "From: " + remoteMessage.getFrom());
       // Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        String message = remoteMessage.getNotification().getBody();
        sendNotification(remoteMessage.getFrom(),message);

    }


    private void sendNotification(String from ,String message) {
        Intent intent = new Intent(this, notificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Builder notificationBuilder = new Builder(this)
                .setSmallIcon(R.drawable.house)
                .setContentTitle(from)
                .setContentText(message)
                .setStyle(new BigTextStyle())
                .setAutoCancel(true)
                .setPriority(PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[] { 50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 ,50 , 300 });

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

}
