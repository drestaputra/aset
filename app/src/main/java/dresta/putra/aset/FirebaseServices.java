package dresta.putra.aset;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

import dresta.putra.aset.berita.DetailBeritaActivity;

public class FirebaseServices extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int count = 0;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        KeyManager.setSharedPreferenceString(getApplicationContext(), "fcm_token", s);
        Log.e(TAG, "onNewToken: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//Here notification is recieved from server
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        String news_permalink = "";
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> data = remoteMessage.getData();
            news_permalink = data.get("news_permalink").toString();
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        try {
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(),news_permalink);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendNotification(String title, String messageBody,String news_permalink) {
        Intent intent = new Intent(getApplicationContext(), DetailBeritaActivity.class);
        intent.putExtra("pushnotification", "yes");
        intent.putExtra("id_informasi_program", news_permalink);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Artakita");
        mBuilder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_logo))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(Color.parseColor("#FFD600"))
                .setContentIntent(pendingIntent)
                .setContentText(messageBody)
                .setChannelId("Artakita")
                .setPriority(NotificationCompat.PRIORITY_HIGH);
        mNotifyManager.notify(count, mBuilder.build());
        count++;
    }
}