package erikterwiel.consecutivealarmsv20;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Erik on 6/22/2017.
 */

public class Alarm implements Serializable {

    // Lists object variables
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
        mSunday = false;
        mMonday = false;
        mTuesday = false;
        mWednesday = false;
        mThursday = false;
        mFriday = false;
        mSaturday = false;
        mOn = false;
    }

    public void setAlarm() {
        setOn(true);
    }

    public void cancelAlarm() {
        setOn(false);
    }

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
