package erikterwiel.consecutivealarmsv20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Erik on 7/10/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent  = new Intent(context, AlarmService.class);
        serviceIntent.putExtra("alarmTone", intent.getStringExtra("alarmTone"));
        serviceIntent.putExtra("alarmVibrate", intent.getBooleanExtra("alarmVibrate", false));
        context.startService(serviceIntent);
    }
}
