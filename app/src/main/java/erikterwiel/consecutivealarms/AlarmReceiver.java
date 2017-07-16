package erikterwiel.consecutivealarms;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by Erik on 7/10/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Always starts notification and ringing service
        Intent serviceIntent = new Intent(context, AlarmAlertService.class);
        serviceIntent.putExtra("masterID", intent.getStringExtra("masterID"));
        serviceIntent.putExtra("alarmLabel", intent.getStringExtra("alarmLabel"));
        serviceIntent.putExtra("alarmTone", intent.getStringExtra("alarmTone"));
        serviceIntent.putExtra("alarmVibrate", intent.getBooleanExtra("alarmVibrate", false));
        context.startService(serviceIntent);

        // Only starts alert activity if screen is locked
        KeyguardManager keyguardManager =
                (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager.inKeyguardRestrictedInputMode()) {
            Intent activityIntent = new Intent(context, AlarmAlertActivity.class);
            activityIntent.putExtra("masterID", intent.getStringExtra("masterID"));
            activityIntent.putExtra("alarmLabel", intent.getStringExtra("alarmLabel"));
            context.startActivity(activityIntent);
        }
    }
}
