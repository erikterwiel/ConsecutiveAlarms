package erikterwiel.consecutivealarmsv20;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Erik on 6/22/2017.
 */

public class EditAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // Creating unique extras
    private static final String EXTRA_ALARM_TO = "erikterwiel.consecutivealarmsv20.extra_alarm_to";
    private static final String EXTRA_ALARM_FROM = "erikterwiel.consecutivealarmsv20.extra_alarm_from";
    private static final int EXTRA_RINGTONE_PICKER_ID = 999;

    // Lists functionality variables
    private Alarm mAlarm;
    private AlarmAdapter mAlarmAdapter;
    private boolean mIsFrom;
    private int mPosition;


    // Lists GUI variables
    private MenuItem mDoneButton;
    private LinearLayout mLabelLayout;
    private TextView mLabel;
    private TextView mFromTime;
    private TextView mFromAM;
    private Time mFromDisplayTime;
    private TextView mToTime;
    private TextView mToAM;
    private Time mToDisplayTime;
    private NumberPicker mAlarmPicker;
    private CheckBox mSunday;
    private CheckBox mMonday;
    private CheckBox mTuesday;
    private CheckBox mWednesday;
    private CheckBox mThursday;
    private CheckBox mFriday;
    private CheckBox mSaturday;
    private RecyclerView mAlarmList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("EditAlarmActivity.java", "onCreate() called");
        setContentView(R.layout.activity_edit_alarm);

        // Initializes variables
        mAlarm = (Alarm) getIntent().getSerializableExtra(EXTRA_ALARM_TO);
        mLabelLayout = (LinearLayout) findViewById(R.id.alarm_label_layout);
        mLabel = (TextView) findViewById(R.id.alarm_label);
        mFromTime = (TextView) findViewById(R.id.edit_from_time);
        mFromAM = (TextView) findViewById(R.id.edit_from_am);
        mToTime = (TextView) findViewById(R.id.edit_to_time);
        mToAM = (TextView) findViewById(R.id.edit_to_am);
        mAlarmPicker = (NumberPicker) findViewById(R.id.edit_alarm_picker);
        mSunday = (CheckBox) findViewById(R.id.list_item_sunday);
        mMonday = (CheckBox) findViewById(R.id.list_item_monday);
        mTuesday = (CheckBox) findViewById(R.id.list_item_tuesday);
        mWednesday = (CheckBox) findViewById(R.id.list_item_wednesday);
        mThursday = (CheckBox) findViewById(R.id.list_item_thursday);
        mFriday = (CheckBox) findViewById(R.id.list_item_friday);
        mSaturday = (CheckBox) findViewById(R.id.list_item_saturday);
        mAlarmList = (RecyclerView) findViewById(R.id.alarms_recycler_view);

        // Sets up label
        mLabel.setText(mAlarm.getLabel());
        mLabelLayout.setOnClickListener(new View.OnClickListener() {
            AlertDialog alert;
            @Override
            public void onClick(View v) {
                FrameLayout container =  new FrameLayout(EditAlarmActivity.this);
                final EditText input = new EditText(EditAlarmActivity.this);
                input.setSingleLine();
                input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setText(mAlarm.getLabel());
                input.selectAll();
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                input.setLayoutParams(params);
                container.addView(input);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(EditAlarmActivity.this)
                        .setTitle("Label")
                        .setView(container);
                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAlarm.setLabel(input.getText().toString());
                        mLabel.setText(input.getText());
                        alert.cancel();
                    }
                });
                alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.cancel();
                    }
                });
                alert = alertBuilder.create();
                alert.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                alert.show();
            }
        });

        // Sets up to and from times
        updateFromTime();
        updateToTime();
        mFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog fromTimePicker = new TimePickerDialog(EditAlarmActivity.this,
                        EditAlarmActivity.this, mAlarm.getFromHour(), mAlarm.getFromMinute(), false);
                fromTimePicker.show();
                mIsFrom = true;
            }
        });
        mToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog toTimePicker = new TimePickerDialog(EditAlarmActivity.this,
                        EditAlarmActivity.this, mAlarm.getToHour(), mAlarm.getToMinute(), false);
                toTimePicker.show();
                mIsFrom =  false;
            }
        });

        // Sets up alarm picker
        mAlarmPicker.setMinValue(1);
        mAlarmPicker.setMaxValue(10);
        mAlarmPicker.setValue(mAlarm.getNumAlarms());
        mAlarmPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mAlarm.setNumAlarms(newVal);
                updateUI();
            }
        });

        // Sets up repeating days
        if (mAlarm.isSunday()) mSunday.setChecked(true);
        if (mAlarm.isMonday()) mMonday.setChecked(true);
        if (mAlarm.isTuesday()) mTuesday.setChecked(true);
        if (mAlarm.isWednesday()) mWednesday.setChecked(true);
        if (mAlarm.isThursday()) mThursday.setChecked(true);
        if (mAlarm.isFriday()) mFriday.setChecked(true);
        if (mAlarm.isSaturday()) mSaturday.setChecked(true);
        mSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setSunday(isChecked);
            }
        });
        mMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setMonday(isChecked);
            }
        });
        mTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setTuesday(isChecked);
            }
        });
        mWednesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setWednesday(isChecked);
            }
        });
        mThursday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setThursday(isChecked);
            }
        });
        mFriday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setFriday(isChecked);
            }
        });
        mSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlarm.setSaturday(isChecked);
            }
        });

        // Sets up alarm RecyclerView
        mAlarmList.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate();
        return super.onCreateOptionsMenu(menu);
    }

    // Sets times when time is picked
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mIsFrom) {
            mAlarm.setFromHour(hourOfDay);
            mAlarm.setFromMinute(minute);
            updateFromTime();
        } else {
            mAlarm.setToHour(hourOfDay);
            mAlarm.setToMinute(minute);
            updateToTime();
        }
    }

    // Updates the times when called
    public void updateFromTime() {
        mFromDisplayTime = Alarm.getTime(mAlarm.getFromHour(), mAlarm.getFromMinute());
        mFromTime.setText(mFromDisplayTime.getTime());
        mFromAM.setText(mFromDisplayTime.getMorning());
        if (mAlarm.getFromHour() > mAlarm.getToHour() ||
                (mAlarm.getFromHour() == mAlarm.getToHour() &&
                        mAlarm.getFromMinute() >= mAlarm.getToMinute())) {
            if (mAlarm.getFromMinute() != 59) {
                mAlarm.setToHour(mAlarm.getFromHour());
                mAlarm.setToMinute(mAlarm.getFromMinute() + 1);
            } else {
                mAlarm.setToHour(mAlarm.getFromHour() + 1);
                mAlarm.setToMinute(0);
            }
            mToDisplayTime = Alarm.getTime(mAlarm.getToHour(), mAlarm.getToMinute());
            mToTime.setText(mToDisplayTime.getTime());
            mToAM.setText(mToDisplayTime.getMorning());
        }
    }

    public void updateToTime() {
        mToDisplayTime = Alarm.getTime(mAlarm.getToHour(), mAlarm.getToMinute());
        mToTime.setText(mToDisplayTime.getTime());
        mToAM.setText(mToDisplayTime.getMorning());
        if (mAlarm.getFromHour() > mAlarm.getToHour() ||
                (mAlarm.getFromHour() == mAlarm.getToHour() &&
                        mAlarm.getFromMinute() >= mAlarm.getToMinute())) {
            if (mAlarm.getToMinute() != 0) {
                mAlarm.setFromHour(mAlarm.getToHour());
                mAlarm.setFromMinute(mAlarm.getToMinute() - 1);
            } else {
                mAlarm.setFromHour(mAlarm.getToHour() - 1);
                mAlarm.setFromMinute(59);
            }
            mFromDisplayTime = Alarm.getTime(mAlarm.getFromHour(), mAlarm.getFromMinute());
            mFromTime.setText(mFromDisplayTime.getTime());
            mFromAM.setText(mFromDisplayTime.getMorning());
        }
    }

    // Needed for RecyclerView
    public void updateUI() {
        mAlarmAdapter = new AlarmAdapter(mAlarm);
        mAlarmList.setAdapter(mAlarmAdapter);
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {
        private List<String> mAlarmNames;

        public AlarmAdapter(Alarm alarm) {
            mAlarmNames = alarm.getAlarmNames();
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(EditAlarmActivity.this);
            View view = layoutInflater.inflate(R.layout.alarm_list_item_alarm, parent, false);
            return new EditAlarmActivity.AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position)  {
            String alarmName = mAlarmNames.get(position);
            holder.bindAlarm(alarmName);
        }

        @Override
        public int getItemCount() {
            return mAlarmNames.size();
        }
    }

    private class AlarmHolder extends RecyclerView.ViewHolder  {
        private LinearLayout mLayoutButton;
        private CheckBox  mVibrateButton;
        private TextView mAlarmNameView;

        public AlarmHolder(View itemView) {
            super(itemView);
            mLayoutButton = (LinearLayout) itemView.findViewById(R.id.alarm_list_item_click_layout);
            mVibrateButton = (CheckBox) itemView.findViewById(R.id.alarm_list_item_check);
            mAlarmNameView = (TextView) itemView.findViewById(R.id.alarm_list_item_ringname);
        }

        public void bindAlarm(String alarmName) {
            mAlarmNameView.setText(alarmName);
            if (mAlarm.getAlarmVibrate(getLayoutPosition())) {
                mVibrateButton.setChecked(true);
            }
            mVibrateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mAlarm.setAlarmVibrate(getLayoutPosition(), isChecked);
                }
            });
            mLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = getLayoutPosition();
                    Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select alarm tone");
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                    startActivityForResult(intent, EXTRA_RINGTONE_PICKER_ID);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            Ringtone ringtone =  RingtoneManager.getRingtone(this, uri);
            String name = ringtone.getTitle(this);
            mAlarm.setAlarmName(mPosition, name);
            mAlarm.setAlarmUri(mPosition, uri.toString());
            Log.i("EditAlarmActivity.java", "Name of uri added with name " + name);
            Log.i("EditAlarmActivity.java", "Uri added with name " + uri.toString());
            updateUI();
        }
    }

    // Simplifies sending alarm objects through intents
    public static Intent newIntent(Context packageContext, Alarm recievedAlarm) {
        Intent i = new Intent(packageContext, EditAlarmActivity.class);
        i.putExtra(EXTRA_ALARM_TO, recievedAlarm);
        return i;
    }
}
