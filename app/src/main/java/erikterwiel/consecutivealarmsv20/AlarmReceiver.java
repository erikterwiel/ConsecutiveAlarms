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
        Intent activityIntent = new Intent(context, AlarmAlertActivity.class);
        activityIntent.putExtra("masterID", intent.getStringExtra("masterID"));
        activityIntent.putExtra("alarmLabel", intent.getStringExtra("alarmLabel"));
        activityIntent.putExtra("alarmTone", intent.getStringExtra("alarmTone"));
        activityIntent.putExtra("alarmVibrate", intent.getBooleanExtra("alarmVibrate", false));
        context.startActivity(activityIntent);
    }
}
