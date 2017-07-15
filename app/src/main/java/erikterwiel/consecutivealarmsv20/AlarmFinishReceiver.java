package erikterwiel.consecutivealarmsv20;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by Erik on 7/14/2017.
 */

public class AlarmFinishReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmAlertService.class);
        context.stopService(serviceIntent);

    }
}
