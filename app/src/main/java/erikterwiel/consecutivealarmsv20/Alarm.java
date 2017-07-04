package erikterwiel.consecutivealarmsv20;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.provider.Settings;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Erik on 6/22/2017.
 */

public class Alarm implements Serializable{

    // Lists object variables
    private String mLabel;
    private int mFromHour;
    private int mFromMinute;
    private int mToHour;
    private int mToMinute;
    private int mNumAlarms;
    private List<String> mAlarmIDs;
    private List<Integer> mAlarmHours;
    private List<Integer> mAlarmMins;
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
        mFromHour = 7;
        mFromMinute = 20;
        mToHour = 7;
        mToMinute = 30;
        mNumAlarms = 3;
        mAlarmIDs = new ArrayList<String>();
        mAlarmHours = new ArrayList<Integer>();
        mAlarmMins =  new ArrayList<Integer>();
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

    // Generates unique ID for each alarm
    public static String getID() {
        String uuid = UUID.randomUUID().toString();
        Log.i("Alarm.java", "UUID " + uuid + " created");
        return uuid;
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
        if (hour < 13) {
            time.setMorning("AM");
            if (minute < 10) {
                time.setTime(Integer.toString(hour) + ":0" + Integer.toString(minute));
                Log.i("Alarm.java", Integer.toString(hour) + ":0" + Integer.toString(minute) +
                        "AM converted");
            } else {
                time.setTime(Integer.toString(hour) + ":" + Integer.toString(minute));
                Log.i("Alarm.java", Integer.toString(hour) + ":" + Integer.toString(minute) +
                        "AM converted");
            }
        } else {
            time.setMorning("PM");
            if (minute < 10) {
                time.setTime(Integer.toString(hour - 12) + ":0" + Integer.toString(minute));
                Log.i("Alarm.java", Integer.toString(hour) + ":0" + Integer.toString(minute) +
                        "PM converted");
            } else {
                time.setTime(Integer.toString(hour - 12) + ":" + Integer.toString(minute));
                Log.i("Alarm.java", Integer.toString(hour) + ":" + Integer.toString(minute) +
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

    // Generated getters and setters
    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public int getFromHour() {
        return mFromHour;
    }

    public void setFromHour(int fromHour) {
        mFromHour = fromHour;
    }

    public int getFromMinute() {
        return mFromMinute;
    }

    public void setFromMinute(int fromMinute) {
        mFromMinute = fromMinute;
    }

    public int getToHour() {
        return mToHour;
    }

    public void setToHour(int toHour) {
        mToHour = toHour;
    }

    public int getToMinute() {
        return mToMinute;
    }

    public void setToMinute(int toMinute) {
        mToMinute = toMinute;
    }

    public int getNumAlarms() {
        return mNumAlarms;
    }

    public void setNumAlarms(int numAlarms) {
        mNumAlarms = numAlarms;
    }

    public boolean isSunday() {
        return mSunday;
    }

    public void setSunday(boolean sunday) {
        mSunday = sunday;
    }

    public boolean isMonday() {
        return mMonday;
    }

    public void setMonday(boolean monday) {
        mMonday = monday;
    }

    public boolean isTuesday() {
        return mTuesday;
    }

    public void setTuesday(boolean tuesday) {
        mTuesday = tuesday;
    }

    public boolean isWednesday() {
        return mWednesday;
    }

    public void setWednesday(boolean wednesday) {
        mWednesday = wednesday;
    }

    public boolean isThursday() {
        return mThursday;
    }

    public void setThursday(boolean thursday) {
        mThursday = thursday;
    }

    public boolean isFriday() {
        return mFriday;
    }

    public void setFriday(boolean friday) {
        mFriday = friday;
    }

    public boolean isSaturday() {
        return mSaturday;
    }

    public void setSaturday(boolean saturday) {
        mSaturday = saturday;
    }

    public boolean isOn() {
        return mOn;
    }

    public void setOn(boolean on) {
        mOn = on;
    }
}
