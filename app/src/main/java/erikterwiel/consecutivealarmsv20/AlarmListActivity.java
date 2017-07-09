package erikterwiel.consecutivealarmsv20;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AlarmListActivity extends AppCompatActivity {

    // Creating unique extras
    private static final String EXTRA_ALARM_FROM = "996";
    private static final int EXTRA_REQUEST_NEW_ALARM = 999;
    private static final int EXTRA_REQUEST_EDIT_ALARM = 998;

    // List functionality variables
    private List<Alarm> mAlarms = new ArrayList<Alarm>();
    private AlarmAdapter mAlarmAdapter;
    private int mAlarmSetIndex;

    // List GUI variables
    private RecyclerView mAlarmList;
    private FloatingActionButton mNewAlarmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
        Log.i("Info", "onCreate() called");

        // Loads saved alarms
        SharedPreferences alarmDatabase = getSharedPreferences("AlarmDatabase", Context.MODE_PRIVATE);
        if (alarmDatabase.contains("arraySize")) {
            int toLoadSize = alarmDatabase.getInt("arraySize", 0);
            for (int i = 0; i < toLoadSize; i++) {
                Alarm toAdd = new Alarm();
                toAdd.setLabel(alarmDatabase.getString(i + "label", null));
                toAdd.setFromHour(alarmDatabase.getInt(i + "fromHour", 7));
                toAdd.setFromMinute(alarmDatabase.getInt(i + "fromMinute", 20));
                toAdd.setToHour(alarmDatabase.getInt(i + "toHour", 7));
                toAdd.setToMinute(alarmDatabase.getInt(i + "toMinute", 30));
                toAdd.setNumAlarms(alarmDatabase.getInt(i + "numAlarms", 3));
                toAdd.setSunday(alarmDatabase.getBoolean(i + "sunday", false));
                toAdd.setMonday(alarmDatabase.getBoolean(i + "monday", false));
                toAdd.setTuesday(alarmDatabase.getBoolean(i + "tuesday", false));
                toAdd.setWednesday(alarmDatabase.getBoolean(i + "wednesday", false));
                toAdd.setThursday(alarmDatabase.getBoolean(i + "thursday", false));
                toAdd.setFriday(alarmDatabase.getBoolean(i + "friday", false));
                toAdd.setSaturday(alarmDatabase.getBoolean(i + "saturday", false));
                toAdd.setOn(alarmDatabase.getBoolean(i + "on", false));
                for (int j = 0; j < alarmDatabase.getInt(i + "numAlarms", 3); j++) {
                    toAdd.addAlarmID(alarmDatabase.getInt(i + "alarmID" + j, 0));
                    toAdd.addAlarmName(alarmDatabase.getString(i + "alarmName" + j, null));
                    toAdd.addAlarmUri(alarmDatabase.getString(i + "alarmUri" + j, null));
                    toAdd.addAlarmVibrate(alarmDatabase.getBoolean(i + "alarmVibrate" + j, false));
                }
                mAlarms.add(toAdd);
            }
        }

        // Setup for new alarm button
        mNewAlarmButton = (FloatingActionButton) findViewById(R.id.new_alarm_button);
        mNewAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creating and sending new alarm to edit activity
                Alarm toSend = new Alarm();
                for (int i = 0; i < 3; i++) {
                    toSend.addAlarmName(Alarm.getDefaultName(AlarmListActivity.this));
                    toSend.addAlarmUri(Alarm.getDefaultUri());
                    toSend.addAlarmVibrate(false);
                }
                Log.i("Info", "New alarm created");
                Intent i = EditAlarmActivity.newIntent(AlarmListActivity.this, toSend);
                startActivityForResult(i, EXTRA_REQUEST_NEW_ALARM);
            }
        });

        // Setup for alarm list RecyclerView
        mAlarmList = (RecyclerView) findViewById(R.id.alarm_list);
        mAlarmList.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Info", "onStop() called");
        SharedPreferences alarmDatabase = getSharedPreferences("AlarmDatabase", Context.MODE_PRIVATE);
        SharedPreferences.Editor databaseEditor = alarmDatabase.edit();
        databaseEditor.putInt("arraySize", mAlarms.size());
        for (int i = 0; i < mAlarms.size(); i++) {
            Alarm toSave = mAlarms.get(i);
            databaseEditor.putString(i + "label", toSave.getLabel());
            databaseEditor.putInt(i + "fromHour", toSave.getFromHour());
            databaseEditor.putInt(i + "fromMinute", toSave.getFromMinute());
            databaseEditor.putInt(i + "toHour", toSave.getToHour());
            databaseEditor.putInt(i + "toMinute", toSave.getToMinute());
            databaseEditor.putInt(i + "numAlarms", toSave.getNumAlarms());
            databaseEditor.putBoolean(i + "sunday", toSave.isSunday());
            databaseEditor.putBoolean(i + "monday", toSave.isMonday());
            databaseEditor.putBoolean(i + "tuesday", toSave.isTuesday());
            databaseEditor.putBoolean(i + "wednesday", toSave.isWednesday());
            databaseEditor.putBoolean(i + "thursday", toSave.isThursday());
            databaseEditor.putBoolean(i + "friday", toSave.isFriday());
            databaseEditor.putBoolean(i + "saturday", toSave.isSaturday());
            databaseEditor.putBoolean(i + "on", toSave.isOn());
            for (int j = 0; j < toSave.getNumAlarms(); j++) {
                databaseEditor.putInt(i + "alarmID" + j, toSave.getAlarmID(j));
                databaseEditor.putString(i + "alarmName" + j, toSave.getAlarmName(j));
                databaseEditor.putString(i + "alarmUri" + j, toSave.getAlarmUri(j));
                databaseEditor.putBoolean(i + "alarmVibrate" + j, toSave.getAlarmVibrate(j));
            }
        }
        databaseEditor.apply();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == EXTRA_REQUEST_NEW_ALARM) {
                mAlarms.add((Alarm) data.getSerializableExtra(EXTRA_ALARM_FROM));
            } else if (requestCode == EXTRA_REQUEST_EDIT_ALARM) {
                mAlarms.set(mAlarmSetIndex, (Alarm) data.getSerializableExtra(EXTRA_ALARM_FROM));
            }
            notifyAlarmSet((Alarm) data.getSerializableExtra(EXTRA_ALARM_FROM));
        }
        updateUI();
    }

    // Called when alarm is set
    public void notifyAlarmSet(Alarm alarm) {
        Snackbar.make(mAlarmList, "An alarm has been set lol not really",
                Snackbar.LENGTH_LONG).show();
    }

    // Called whenever UI is changed
    public void updateUI() {
        mAlarmAdapter = new AlarmAdapter(mAlarms);
        mAlarmList.setAdapter(mAlarmAdapter);
    }

    // RecyclerView Adapter and Holder
    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {
        private List<Alarm> mAdapterAlarms;

        private AlarmAdapter(List<Alarm> alarms) {
            mAdapterAlarms = alarms;
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(AlarmListActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_alarm, parent, false);
            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            Alarm alarm = mAdapterAlarms.get(position);
            holder.bindAlarm(alarm);
        }

        @Override
        public int getItemCount() {
            return mAdapterAlarms.size();
        }
    }

    private class AlarmHolder extends RecyclerView.ViewHolder {

        // List GUI variables for each alarm
        private Alarm mAlarm;
        private TextView mFromTime;
        private TextView mFromAM;
        private TextView mToTime;
        private TextView mToAM;
        private TextView mNumAlarms;
        private TextView mLabel;
        private Switch mSwitch;
        private ImageButton mSettings;
        private Time mDisplayFromTime;
        private Time mDisplayToTime;

        // Initializes GUI variables
        private AlarmHolder(View itemView) {
            super(itemView);
            mFromTime = (TextView) itemView.findViewById(R.id.list_item_from_time);
            mFromAM = (TextView) itemView.findViewById(R.id.list_item_from_am);
            mToTime = (TextView) itemView.findViewById(R.id.list_item_to_time);
            mToAM = (TextView) itemView.findViewById(R.id.list_item_to_am);
            mNumAlarms = (TextView) itemView.findViewById(R.id.list_item_alarm_count);
            mLabel = (TextView) itemView.findViewById(R.id.list_item_label);
            mSwitch = (Switch) itemView.findViewById(R.id.list_item_switch);
            mSettings = (ImageButton) itemView.findViewById(R.id.list_item_settings);
        }

        // Display alarm information and wire buttons
        private void bindAlarm(Alarm alarm) {
            mAlarm = alarm;

            // Display from alarm time
            mDisplayFromTime = Alarm.getTime(mAlarm.getFromHour(), mAlarm.getFromMinute());
            mFromTime.setText(mDisplayFromTime.getTime());
            mFromAM.setText(mDisplayFromTime.getMorning());

            // Display to alarm time
            mDisplayToTime = Alarm.getTime(mAlarm.getToHour(), mAlarm.getToMinute());
            mToTime.setText(mDisplayToTime.getTime());
            mToAM.setText(mDisplayToTime.getMorning());

            // Display number of alarms
            mNumAlarms.setText(Integer.toString(mAlarm.getNumAlarms()) + " Alarms");

            // Display the label
            mLabel.setText(mAlarm.getLabel());

            // Sets up the alarm switch
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked && !mAlarm.isOn()) {
                        mAlarm.setAlarm();
                        mAlarm.setOn(true);
                        notifyAlarmSet(mAlarm);
                    } else if (!isChecked && mAlarm.isOn()) {
                        mAlarm.cancelAlarm();
                        mAlarm.setOn(false);
                    }
                }
            });
            mSwitch.setChecked(mAlarm.isOn());

            // Sets up settings button
            mSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlarmSetIndex = mAlarms.indexOf(mAlarm);
                    Intent i =  EditAlarmActivity.newIntent(AlarmListActivity.this, mAlarm);
                    startActivityForResult(i, EXTRA_REQUEST_EDIT_ALARM);
                }
            });
        }
    }
}
