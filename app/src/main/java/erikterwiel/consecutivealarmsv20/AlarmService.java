package erikterwiel.consecutivealarmsv20;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Erik on 7/10/2017.
 */

public class AlarmService extends Service {
    private NotificationManager mNotificationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent fullscreenIntent = new Intent(this, AlarmListActivity.class);
        fullscreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingFullscreenIntent = PendingIntent.getActivity(
                this, 777, fullscreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification alarmNotification = new Notification.Builder(this)
                .setCategory(Notification.CATEGORY_ALARM)
                .setSmallIcon(R.drawable.ic_notifications_white_48dp)
                .setContentIntent(pendingFullscreenIntent)
                .setFullScreenIntent(pendingFullscreenIntent, true)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        alarmNotification.fullScreenIntent = pendingFullscreenIntent;
        mNotificationManager.notify(777, alarmNotification);
        return super.onStartCommand(intent, flags, startId);
    }
}
