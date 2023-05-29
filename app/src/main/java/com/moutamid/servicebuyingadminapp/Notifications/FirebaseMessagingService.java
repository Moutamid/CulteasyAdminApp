package com.moutamid.servicebuyingadminapp.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.fxn.stash.Stash;
import com.google.firebase.messaging.RemoteMessage;
import com.moutamid.servicebuyingadminapp.MainActivity;
import com.moutamid.servicebuyingadminapp.R;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    NotificationManager mNotificationManager;

    public void onNewToken(String s) {
        super.onNewToken(s);
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, 0).edit();
        editor.putString("name", s);
        editor.apply();
        Log.d("ContentValues", "onNewToken: " + s);
    }

    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // if (Stash.getBoolean(Constants.PAUSE_STATUS, false))
        //   return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            sendOreoNotification(remoteMessage);
        } else {
            sendNotification(remoteMessage);
        }

        /*Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), RingtoneManager.getDefaultUri(2));
        r.play();
        if (Build.VERSION.SDK_INT >= 28) {
            r.setLooping(false);
        }

        ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{100, 300, 300, 300}, -1);

//        int resourceImage = getResources().getIdentifier(remoteMessage.getNotification().getIcon(), "drawable", getPackageName());
        NotificationCompat.Builder builder = new NotificationCompat.Builder((Context) this, "CHANNEL_ID");
//        if (Build.VERSION.SDK_INT >= 21) {
//            builder.setSmallIcon(resourceImage);
//        } else {
//            builder.setSmallIcon(resourceImage);
//        }

        builder.setSmallIcon(R.drawable.logo);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this,
                        MainScreen.class),
                PendingIntent.FLAG_UPDATE_CURRENT);//134217728
        builder.setContentTitle(remoteMessage.getNotification().getTitle());
        builder.setContentText(remoteMessage.getNotification().getBody());
        builder.setContentIntent(pendingIntent);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()));
        builder.setAutoCancel(true);
        builder.setPriority(2);
        this.mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);//"notification"
        if (Build.VERSION.SDK_INT >= 26) {
            this.mNotificationManager.createNotificationChannel(new NotificationChannel("Your_channel_id", "Channel human readable title", NotificationManager.IMPORTANCE_HIGH));//4
            builder.setChannelId("Your_channel_idd");
        }
        this.mNotificationManager.notify(100, builder.build());*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendOreoNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_IMMUTABLE);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = null;
        //   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound);
        // }


        oreoNotification.getManager().notify(1, builder.build());
    }

    @SuppressWarnings("deprecation")
    private void sendNotification(RemoteMessage remoteMessage) {

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        NotificationCompat.Builder builder = null;
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 1, intent,
                    PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getActivity(this, 1, intent,
                    PendingIntent.FLAG_ONE_SHOT);
        }
//                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getNotification().getBody()))
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        noti.notify(100, builder.build());
    }
}

