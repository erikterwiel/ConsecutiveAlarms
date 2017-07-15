package erikterwiel.consecutivealarmsv20;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * Created by Erik on 6/22/2017.
 */

public class Alarm implements Serializable {

    // Declares object variables
    private String mMasterID;
    private String mLabel;
    private int mFromHour;
    private int mFromMinute;
    private int mToHour;
    private int mToMinute;
    private int mNumAlarms;
    private List<Integer> mAlarmIDs;
    private List<String> mAlarmNames;
    private List<String> mAlarmUris;
    private List<Boolean> mAlarmVibrate;
    private boolean mOn;
    private boolean mSunday;
    private boolean mMonday;
    private boolean mTuesday;
    private boolean mWednesday;
    private boolean mThursday;
    private boolean mFriday;
    private boolean mSaturday;

    // Initializes object variables
    public Alarm() {
        Log.i("Info", "New Alarm object created");
        mFromHour = 7;
        mFromMinute = 20;
        mToHour = 7;
        mToMinute = 30;
        mNumAlarms = 3;
        mAlarmIDs = new ArrayList<Integer>();
        mAlarmNames = new ArrayList<String>();
        mAlarmUris = new ArrayList<String>();
        mAlarmVibrate = new ArrayList<Boolean>();
        mOn = false;
        mSunday = false;
        mMonday = false;
        mTuesday = false;
        mWednesday = false;
        mThursday = false;
        mFriday = false;
        mSaturday = false;
    }

    // Sets alarm
    public void setAlarm(Context context) {

        // Calculates the interval in milliseconds
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, mFromHour);
        calendar.set(Calendar.MINUTE, mFromMinute);
        if (System.currentTimeMillis() > calendar.getTimeInMillis())
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        long fromMillis = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, mToHour);
        calendar.set(Calendar.MINUTE, mToMinute);
        long toMillis = calendar.getTimeInMillis();
        long interval = 0;
        if (mNumAlarms != 1) interval = (toMillis - fromMillis) / (mNumAlarms - 1);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (int i = 0; i < mNumAlarms; i++) {

            // Creates intent that displays alarm info
            Intent appIntent = new Intent(context, AlarmListActivity.class);
            PendingIntent pendingAppIntent = PendingIntent.getActivity(context, 0, appIntent, 0);
            AlarmManager.AlarmClockInfo alarmInfo =
                    new AlarmManager.AlarmClockInfo(fromMillis + (i * interval), pendingAppIntent);

            // Creates intent that actually sets the alarm
            Intent receiverIntent = new Intent(context, AlarmReceiver.class);
            if (i == mNumAlarms - 1) receiverIntent.putExtra("masterID", getMasterID());
            receiverIntent.putExtra("alarmLabel", getLabel());
            receiverIntent.putExtra("alarmTone", getAlarmUri(i));
            receiverIntent.putExtra("alarmVibrate", getAlarmVibrate(i));
            PendingIntent pendingServiceIntent = PendingIntent.getBroadcast(
                    context, getAlarmID(i), receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.setAlarmClock(alarmInfo, pendingServiceIntent);
        }
        setOn(true);
    }

    // Cancels alarm
    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent appIntent = new Intent(context, AlarmReceiver.class);
        for (int i = 0; i < mNumAlarms; i++)
            alarmManager.cancel(PendingIntent.getBroadcast(
                    context, getAlarmID(i), appIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        setOn(false);
    }

    // Changes array to whatever size provided
    public void resize(int newSize, Context context) {
        ArrayList<String> tempAlarmNames = new ArrayList<String>();
        ArrayList<String> tempAlarmUris = new ArrayList<String>();
        ArrayList<Boolean> tempAlarmVibrate = new ArrayList<Boolean>();
        for (int i = 0;  i < newSize; i++) {
            if (i < mAlarmNames.size()) {
                tempAlarmNames.add(mAlarmNames.get(i));
                tempAlarmUris.add(mAlarmUris.get(i));
                tempAlarmVibrate.add(mAlarmVibrate.get(i));
            } else {
                tempAlarmNames.add(Alarm.getDefaultName(context));
                tempAlarmUris.add(Alarm.getDefaultUri());
                tempAlarmVibrate.add(false);
            }
        }
        mAlarmNames = tempAlarmNames;
        mAlarmUris = tempAlarmUris;
        mAlarmVibrate = tempAlarmVibrate;
    }

    // Returns default Uri Name
    public static String getDefaultName(Context context) {
        Ringtone ringtone = RingtoneManager.getRingtone(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        return ringtone.getTitle(context);
    }

    // Returns default Uri
    public static String getDefaultUri() {
        return Settings.System.DEFAULT_ALARM_ALERT_URI.toString();
    }

    // Converts time data to usable strings
    public static Time getTime(int hour, int minute) {
        Time time = new Time();
        if (hour == 0 || hour == 24) {
            time.setMorning("AM");
            if (minute < 10) {
                time.setTime("12:0" + Integer.toString(minute));
                Log.i("Info", "12:0" + Integer.toString(minute) + "AM converted");
            } else {
                time.setTime("12:" + Integer.toString(minute));
                Log.i("Info", "12:" + Integer.toString(minute) + "AM converted");
            }
        } else if (hour ==  12) {
            time.setMorning("PM");
            if (minute < 10) {
                time.setTime("12:0" + Integer.toString(minute));
                Log.i("Info", "12:0" + Integer.toString(minute) + "PM converted");
            } else {
                time.setTime("12:" + Integer.toString(minute));
                Log.i("Info", "12:" + Integer.toString(minute) + "PM converted");
            }
        } else if (hour < 0) {
            time.setMorning("PM");
            if (minute < 10) {
                time.setTime(Integer.toString(hour + 12) + ":0" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour + 12) + ":0" + Integer.toString(minute) +
                        "AM converted");
            } else {
                time.setTime(Integer.toString(hour + 12) + ":" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour + 12) + ":" + Integer.toString(minute) +
                        "AM converted");
            }
        } else if (hour < 13) {
            time.setMorning("AM");
            if (minute < 10) {
                time.setTime(Integer.toString(hour) + ":0" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour) + ":0" + Integer.toString(minute) +
                        "AM converted");
            } else {
                time.setTime(Integer.toString(hour) + ":" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour) + ":" + Integer.toString(minute) +
                        "AM converted");
            }
        } else {
            time.setMorning("PM");
            if (minute < 10) {
                time.setTime(Integer.toString(hour - 12) + ":0" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour - 12) + ":0" + Integer.toString(minute) +
                        "PM converted");
            } else {
                time.setTime(Integer.toString(hour - 12) + ":" + Integer.toString(minute));
                Log.i("Info", Integer.toString(hour - 12) + ":" + Integer.toString(minute) +
                        "PM converted");
            }
        }
        return time;
    }

    // Custom getters and setters
    public void addAlarmName(String toAdd) {
        mAlarmNames.add(toAdd);
    }

    public void addAlarmUri(String toAdd) {
        mAlarmUris.add(toAdd);
    }

    public void addAlarmVibrate(boolean toAdd) {
        mAlarmVibrate.add(toAdd);
    }

    public void addAlarmID(int toAdd) {
        mAlarmIDs.add(toAdd);
        Log.i("Info", "UUID " + Integer.toString(toAdd) + " added");
    }

    public void addNewAlarmID() {
        int uuid = UUID.randomUUID().hashCode();
        Log.i("Info", "UUID " + Integer.toString(uuid) + " created");
        mAlarmIDs.add(uuid);
    }

    public List<String> getAlarmNames() {
        return mAlarmNames;
    }

    public int getAlarmID(int position) {
        return mAlarmIDs.get(position);
    }

    public String getAlarmName(int position) {
        return mAlarmNames.get(position);
    }

    public String getAlarmUri(int position) {
        return mAlarmUris.get(position);
    }

    public boolean getAlarmVibrate(int position) {
        return mAlarmVibrate.get(position);
    }

    public void setAlarmName(int position, String toSet) {
        mAlarmNames.set(position, toSet);
        Log.i("Info", "Alarm name at position " + Integer.toString(position) + " set to "
                + toSet);
    }

    public void setAlarmUri(int position, String toSet) {
        mAlarmUris.set(position, toSet);
        Log.i("Info", "Uri at position " + Integer.toString(position) + " set to "
                + toSet);
    }

    public void setAlarmVibrate(int position, boolean vibrate) {
        mAlarmVibrate.set(position, vibrate);
        Log.i("Info", "Vibrate at position " + Integer.toString(position) + " set to "
                + vibrate);
    }

    // Generated getters and setters

    public String getMasterID() {
        return mMasterID;
    }

    public void setMasterID(String masterID) {
        mMasterID = masterID;
        Log.i("Info", "Master ID set to: " + masterID);
    }

    public void setNewMasterID() {
        mMasterID = UUID.randomUUID().toString();
        Log.i("Info", "Master ID set to: " + mMasterID);
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
        Log.i("Info", "Label set to " + label);
    }

    public int getFromHour() {
        return mFromHour;
    }

    public void setFromHour(int fromHour) {
        mFromHour = fromHour;
        Log.i("Info", "From hour set to " + Integer.toString(fromHour));
    }

    public int getFromMinute() {
        return mFromMinute;
    }

    public void setFromMinute(int fromMinute) {
        mFromMinute = fromMinute;
        Log.i("Info", "From minute set to " + Integer.toString(fromMinute));
    }

    public int getToHour() {
        return mToHour;
    }

    public void setToHour(int toHour) {
        mToHour = toHour;
        Log.i("Info", "To hour set to " + Integer.toString(toHour));
    }

    public int getToMinute() {
        return mToMinute;
    }

    public void setToMinute(int toMinute) {
        mToMinute = toMinute;
        Log.i("Info", "To minute set to " + Integer.toString(toMinute));
    }

    public int getNumAlarms() {
        return mNumAlarms;
    }

    public void setNumAlarms(int numAlarms) {
        mNumAlarms = numAlarms;
        Log.i("Info", "Number of alarms set to " + Integer.toString(numAlarms));
    }

    public boolean isSunday() {
        return mSunday;
    }

    public void setSunday(boolean sunday) {
        mSunday = sunday;
        Log.i("Info", "Repeating Sunday set to: " + sunday);
    }

    public boolean isMonday() {
        return mMonday;
    }

    public void setMonday(boolean monday) {
        mMonday = monday;
        Log.i("Info", "Repeating Monday set to: " + monday);
    }

    public boolean isTuesday() {
        return mTuesday;
    }

    public void setTuesday(boolean tuesday) {
        mTuesday = tuesday;
        Log.i("Info", "Repeating Tuesday set to: " + tuesday);
    }

    public boolean isWednesday() {
        return mWednesday;
    }

    public void setWednesday(boolean wednesday) {
        mWednesday = wednesday;
        Log.i("Info", "Repeating Wednesday set to: " + wednesday);
    }

    public boolean isThursday() {
        return mThursday;
    }

    public void setThursday(boolean thursday) {
        mThursday = thursday;
        Log.i("Info", "Repeating Thursday set to: " + thursday);
    }

    public boolean isFriday() {
        return mFriday;
    }

    public void setFriday(boolean friday) {
        mFriday = friday;
        Log.i("Info", "Repeating Friday set to: " + friday);
    }

    public boolean isSaturday() {
        return mSaturday;
    }

    public void setSaturday(boolean saturday) {
        mSaturday = saturday;
        Log.i("Info", "Repeating Saturday set to: " + saturday);
    }

    public boolean isOn() {
        return mOn;
    }

    public void setOn(boolean on) {
        mOn = on;
        Log.i("Info", "Alarm set on: " + on);
    }
}
