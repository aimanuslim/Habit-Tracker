package com.theunheard.saigono;


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
        String habitName = intent.getStringExtra(String.valueOf(R.string.alarm_notification_habitname_label));
        String lastPerformed = intent.getStringExtra(String.valueOf(R.string.alarm_notification_last_performed_label));
        createNotification(context, "Saigo No Reminder: " + habitName, "You last performed this activity on " + lastPerformed, "Alert");
    }

    public void createNotification(Context context, String msg, String msgText, String msgAlert){
        PendingIntent notificationIntent = PendingIntent.getActivity(context, 0, new Intent(context, TabbedActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.statusbaricon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.appicon))
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
