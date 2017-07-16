package erikterwiel.consecutivealarms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Erik on 7/14/2017.
 */

public class AlarmFinishReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // Stops alarm notification, ringing, and vibrating
        Intent serviceIntent = new Intent(context, AlarmAlertService.class);
        context.stopService(serviceIntent);

        String uuid = intent.getStringExtra("masterID");
        Log.i("Info", "UUID " + uuid + " made it to AlarmFinishReceiver");
        if (uuid != null) {

            // Locates the finished alarm
            int alarmLocation = 0;
            SharedPreferences alarmDatabase = context.getSharedPreferences(
                    "AlarmDatabase", Context.MODE_PRIVATE);
            for (int i = 0; i < alarmDatabase.getInt("arraySize", 0); i++) {
                if (alarmDatabase.getString(i + "masterID", null).equals(uuid)) {
                    alarmLocation = i;
                    break;
                }
            }

            // Loads the finished alarm
            Alarm toCheck = new Alarm();
            toCheck.setMasterID(alarmDatabase.getString(alarmLocation + "masterID", null));
            toCheck.setLabel(alarmDatabase.getString(alarmLocation + "label", null));
            toCheck.setFromHour(alarmDatabase.getInt(alarmLocation + "fromHour", 7));
            toCheck.setFromMinute(alarmDatabase.getInt(alarmLocation + "fromMinute", 20));
            toCheck.setToHour(alarmDatabase.getInt(alarmLocation + "toHour", 7));
            toCheck.setToMinute(alarmDatabase.getInt(alarmLocation + "toMinute", 30));
            toCheck.setNumAlarms(alarmDatabase.getInt(alarmLocation + "numAlarms", 3));
            toCheck.setSunday(alarmDatabase.getBoolean(alarmLocation + "sunday", false));
            toCheck.setMonday(alarmDatabase.getBoolean(alarmLocation + "monday", false));
            toCheck.setTuesday(alarmDatabase.getBoolean(alarmLocation + "tuesday", false));
            toCheck.setWednesday(alarmDatabase.getBoolean(alarmLocation + "wednesday", false));
            toCheck.setThursday(alarmDatabase.getBoolean(alarmLocation + "thursday", false));
            toCheck.setFriday(alarmDatabase.getBoolean(alarmLocation + "friday", false));
            toCheck.setSaturday(alarmDatabase.getBoolean(alarmLocation + "saturday", false));
            toCheck.setOn(alarmDatabase.getBoolean(alarmLocation + "on", false));
            for (int i = 0; i < alarmDatabase.getInt(alarmLocation + "numAlarms", 3); i++) {
                toCheck.addAlarmID(alarmDatabase.getInt(alarmLocation + "alarmID" + i, 0));
                toCheck.addAlarmName(alarmDatabase.getString(
                        alarmLocation + "alarmName" + i, null));
                toCheck.addAlarmUri(alarmDatabase.getString(alarmLocation + "alarmUri" + i, null));
                toCheck.addAlarmVibrate(alarmDatabase.getBoolean(
                        alarmLocation + "alarmVibrate" + i, false));
            }

            // Turns off finished alarm
            toCheck.cancelAlarm(context);

            // Checks if alarm is a repeating alarm and sets it if there is
            if (toCheck.isSunday() || toCheck.isMonday()
                    || toCheck.isTuesday() || toCheck.isWednesday()
                    || toCheck.isThursday() || toCheck.isFriday()
                    || toCheck.isSaturday())
                toCheck.setAlarm(context, true);

            // Saves the edited alarm
            SharedPreferences.Editor databaseEditor = alarmDatabase.edit();
            databaseEditor.putBoolean(alarmLocation + "on", toCheck.isOn());
            databaseEditor.apply();
        }
    }
}
