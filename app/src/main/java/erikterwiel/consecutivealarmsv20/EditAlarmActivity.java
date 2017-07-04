package erikterwiel.consecutivealarmsv20;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
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

/**
 * Created by Erik on 6/22/2017.
 */

public class EditAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // Creating unique extras
    private static final String EXTRA_ALARM_TO = "erikterwiel.consecutivealarmsv20.extra_alarm_to";
    private static final String EXTRA_ALARM_FROM = "erikterwiel.consecutivealarmsv20.extra_alarm_from";
    private static final int EXTRA_DIALOG_ID = 00000001;

    // Lists functionality variables
    private Alarm mAlarm;
    //private AlarmAdapter mAlarmAdapter;
    private boolean mIsFrom;

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

    public void updateUI() {

    }

    // Simplifies sending alarm objects through intents
    public static Intent newIntent(Context packageContext, Alarm recievedAlarm) {
        Intent i = new Intent(packageContext, EditAlarmActivity.class);
        i.putExtra(EXTRA_ALARM_TO, recievedAlarm);
        return i;
    }
}
