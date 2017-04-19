package com.theunheard.habitking;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by ian21 on 4/8/2017.
 */

public class NotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String habitName = intent.getStringExtra("Habit Name");
        createNotification(context, "Habit King Reminder", "I am reminding you of doing this! -> " + habitName, "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, TabbedActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setContentTitle(msg)
                .setTicker((msgAlert))
                .setContentText(msgText);

        mBuilder.setContentIntent(notificationIntent);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(1, mBuilder.build());
    }
}
