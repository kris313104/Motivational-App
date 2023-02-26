package com.example.demotivationalquotes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.List;

public class ReminderBroadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        QuoteFinder quoteFinder = new QuoteFinder();

        List<String> quotes = null;
        try {
            quotes = quoteFinder.searchQuote();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long pattern[] = {500, 500, 500};
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notcha1")
                .setSmallIcon(R.drawable.ic_thumb)
                .setContentTitle("Your motivational quote!")
                .setContentText("")
//                .setSound(sound)
                .setSound(null)
//                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
//                .setDefaults(Notification.DEFAULT_SOUND)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(quotes.get(0) + quotes.get(1)))
                .setVibrate(pattern)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();

        notificationManager.notify(100, notification);

        playNotificationSound(context);
    }

    void playNotificationSound(Context context){
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound);
//        Uri defaultdSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(context, sound);
        r.play();
    }
}
