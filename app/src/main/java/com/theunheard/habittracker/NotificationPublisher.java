package com.theunheard.habittracker;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

/**
 * Created by ian21 on 4/8/2017.
 */

public class NotificationPublisher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String habitName = intent.getStringExtra("Habit Name");
        String lastPerformed = intent.getStringExtra("Last Performed");
        createNotification(context, "Habit Tracker Reminder: " + habitName, "You last performed this activity on " + lastPerformed, "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        Intent intent = new Intent(context, TabbedActivity.class);
        intent.setAction("Open Data List");
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(com.theunheard.habittracker.R.drawable.statusbaricon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), com.theunheard.habittracker.R.drawable.appicon))
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
