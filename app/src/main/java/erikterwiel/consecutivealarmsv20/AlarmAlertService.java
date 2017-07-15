package erikterwiel.consecutivealarmsv20;

import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

import java.util.Calendar;

/**
 * Created by Erik on 7/14/2017.
 */

public class AlarmAlertService extends Service {
    private NotificationManager mNotificationManager;
    private MediaPlayer mTonePlayer;
    private Vibrator mVibrator;
    private long[] mPattern = {0, 1000, 1000};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // PendingIntent for AlarmAlertActivity
        Intent activityIntent = new Intent(this, AlarmAlertActivity.class);
        activityIntent.putExtra("alarmLabel", intent.getStringExtra("alarmLabel"));
        PendingIntent pendingActivityIntent = PendingIntent.getActivity(
                this, 777, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // PendingIntent for AlarmFinishReceiver
        Intent receiverIntent = new Intent(this, AlarmFinishReceiver.class);
        activityIntent.putExtra("masterID", intent.getStringExtra("masterID"));
        PendingIntent pendingReceiverIntent = PendingIntent.getBroadcast(
                this, 777, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Gets alarm label
        Calendar calendar = Calendar.getInstance();
        Time currentTime = Alarm.getTime(
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        String label = intent.getStringExtra("alarmLabel");
        if (label == null) label = "Alarm";

        // Sets up notification
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_alarm_black_48dp)
                .setContentTitle(label)
                .setContentText(currentTime.getTime() + currentTime.getMorning())
                .setCategory(Notification.CATEGORY_ALARM)
                .setPriority(Notification.PRIORITY_MAX)
                .setAutoCancel(true)
                .addAction(new Notification.Action.Builder(
                        R.drawable.ic_alarm_off_black_48dp, "Dismiss", pendingReceiverIntent)
                        .build());

        // Sets notification to heads up if device is unlocked
        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            notificationBuilder.setContentIntent(pendingActivityIntent);
        } else notificationBuilder.setFullScreenIntent(pendingActivityIntent, true);

        // Displays notification
        Notification alarmNotification = notificationBuilder.build();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(888, alarmNotification);

        // Plays selected alarm tone
        try {
            mTonePlayer = new MediaPlayer();
            mTonePlayer.setDataSource(this, Uri.parse(intent.getStringExtra("alarmTone")));
            mTonePlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mTonePlayer.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build());
            mTonePlayer.prepare();
            mTonePlayer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Vibrates if user selected vibrate
        if (intent.getBooleanExtra("alarmVibrate", false)) {
            mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            mVibrator.vibrate(mPattern, 0);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancel(888);
        mTonePlayer.stop();
        mTonePlayer.release();
        mVibrator.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
