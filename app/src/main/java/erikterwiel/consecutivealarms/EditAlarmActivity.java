package erikterwiel.consecutivealarms;

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

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Erik on 6/22/2017.
 */

public class EditAlarmActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // Creating unique extras
    private static final String EXTRA_ALARM_TO = "1000";
    private static final String EXTRA_ALARM_FROM = "996";
    private static final int EXTRA_RINGTONE_PICKER_ID = 997;
    private static final int EXTRA_MASTER_RINGTONE_PICKER_ID = 995;

    // Declares functionality variables
    private Alarm mAlarm;
    private AlarmAdapter mAlarmAdapter;
    private boolean mIsFrom;
    private int mPosition;


    // Declares GUI variables
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
    private TextView mSunLetter;
    private TextView mMonLetter;
    private TextView mTueLetter;
    private TextView mWedLetter;
    private TextView mThuLetter;
    private TextView mFriLetter;
    private TextView mSatLetter;
    private CheckBox mSunday;
    private CheckBox mMonday;
    private CheckBox mTuesday;
    private CheckBox mWednesday;
    private CheckBox mThursday;
    private CheckBox mFriday;
    private CheckBox mSaturday;
    private LinearLayout mMasterLayoutButton;
    private CheckBox  mMasterVibrateButton;
    private TextView mMasterAlarmNameView;
    private RecyclerView mAlarmList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Info", "onCreate() called");
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
        mSunLetter = (TextView) findViewById(R.id.edit_sun_letter);
        mMonLetter = (TextView) findViewById(R.id.edit_mon_letter);
        mTueLetter = (TextView) findViewById(R.id.edit_tue_letter);
        mWedLetter = (TextView) findViewById(R.id.edit_wed_letter);
        mThuLetter = (TextView) findViewById(R.id.edit_thu_letter);
        mFriLetter = (TextView) findViewById(R.id.edit_fri_letter);
        mSatLetter = (TextView) findViewById(R.id.edit_sat_letter);
        mSunday = (CheckBox) findViewById(R.id.list_item_sunday);
        mMonday = (CheckBox) findViewById(R.id.list_item_monday);
        mTuesday = (CheckBox) findViewById(R.id.list_item_tuesday);
        mWednesday = (CheckBox) findViewById(R.id.list_item_wednesday);
        mThursday = (CheckBox) findViewById(R.id.list_item_thursday);
        mFriday = (CheckBox) findViewById(R.id.list_item_friday);
        mSaturday = (CheckBox) findViewById(R.id.list_item_saturday);
        mMasterLayoutButton = (LinearLayout) findViewById(R.id.alarm_master_click_layout);
        mMasterVibrateButton = (CheckBox) findViewById(R.id.alarm_master_check);
        mMasterAlarmNameView  = (TextView) findViewById(R.id.alarm_master_ringname);
        mAlarmList = (RecyclerView) findViewById(R.id.alarms_recycler_view);

        // Sets old alarm value before changes
        mAlarm.setNumAlarmsOld(mAlarm.getNumAlarms());

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
        updateTime("both");
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
                mAlarm.resize(newVal, EditAlarmActivity.this);
                if (oldVal < newVal) {
                    mAlarmAdapter.itemAdded(mAlarm, oldVal);
                } else
                    updateUI();
                if (oldVal == 1 || newVal == 1) updateTime("from");
                refreshMasterAlarm();
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
        mSunLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSunday.setChecked(!mAlarm.isSunday());
            }
        });
        mMonLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMonday.setChecked(!mAlarm.isMonday());
            }
        });
        mTueLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTuesday.setChecked(!mAlarm.isTuesday());
            }
        });
        mWedLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWednesday.setChecked(!mAlarm.isWednesday());
            }
        });
        mThuLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mThursday.setChecked(!mAlarm.isThursday());
            }
        });
        mFriLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFriday.setChecked(!mAlarm.isFriday());
            }
        });
        mSatLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaturday.setChecked(!mAlarm.isSaturday());
            }
        });
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

        // Sets up master alarm picker
        refreshMasterAlarm();

        // Sets up alarm RecyclerView
        mAlarmList.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
    }

    // Sets up done button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.done_button_menu, menu);
        mDoneButton = menu.findItem(R.id.done_button);
        mDoneButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mAlarm.isOn()) mAlarm.cancelAlarmOld(EditAlarmActivity.this);
                for (int i = 0; i < mAlarm.getNumAlarms(); i++) mAlarm.addNewAlarmID();
                mAlarm.setAlarm(EditAlarmActivity.this, false);
                Intent i = new Intent();
                i.putExtra(EXTRA_ALARM_FROM, mAlarm);
                setResult(RESULT_OK, i);
                finish();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // Sets times when time is picked
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (mIsFrom) {
            mAlarm.setFromHour(hourOfDay);
            mAlarm.setFromMinute(minute);
            updateTime("from");
        } else {
            mAlarm.setToHour(hourOfDay);
            mAlarm.setToMinute(minute);
            updateTime("to");
        }
    }

    // Updates the times when called
    public void updateTime(String which) {
        if (which.equals("from")) {
            if (mAlarm.getNumAlarms() == 1) {
                mAlarm.setToHour(mAlarm.getFromHour());
                mAlarm.setToMinute(mAlarm.getFromMinute());
            } else if (mAlarm.getFromHour() > mAlarm.getToHour() ||
                    (mAlarm.getFromHour() == mAlarm.getToHour()
                            && mAlarm.getFromMinute() ==  mAlarm.getToMinute()) ||
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
        } else if (which.equals("to")) {
            if (mAlarm.getNumAlarms() == 1) {
                mAlarm.setFromHour(mAlarm.getToHour());
                mAlarm.setFromMinute(mAlarm.getToMinute());
            } else if (mAlarm.getFromHour() > mAlarm.getToHour() ||
                    (mAlarm.getFromHour() == mAlarm.getToHour()
                            && mAlarm.getFromMinute() ==  mAlarm.getToMinute()) ||
                    (mAlarm.getFromHour() == mAlarm.getToHour() &&
                            mAlarm.getFromMinute() >= mAlarm.getToMinute())) {
                if (mAlarm.getToMinute() != 0) {
                    mAlarm.setFromHour(mAlarm.getToHour());
                    mAlarm.setFromMinute(mAlarm.getToMinute() - 1);
                } else {
                    mAlarm.setFromHour(mAlarm.getToHour() - 1);
                    mAlarm.setFromMinute(59);
                }
            }
        }
        mFromDisplayTime = Alarm.getTime(mAlarm.getFromHour(), mAlarm.getFromMinute());
        mFromTime.setText(mFromDisplayTime.getTime());
        mFromAM.setText(mFromDisplayTime.getMorning());
        mToDisplayTime = Alarm.getTime(mAlarm.getToHour(), mAlarm.getToMinute());
        mToTime.setText(mToDisplayTime.getTime());
        mToAM.setText(mToDisplayTime.getMorning());
    }

    // Refreshes master alarm picker
    public void refreshMasterAlarm() {
        boolean sameRing = true;
        boolean sameVibrate = true;
        String compareUri = mAlarm.getAlarmUri(0);
        boolean compareVibrate = mAlarm.getAlarmVibrate(0);
        for (int i = 1; i < mAlarm.getNumAlarms(); i++) {
            if (!mAlarm.getAlarmUri(i).equals(compareUri)) sameRing = false;
            if (mAlarm.getAlarmVibrate(i) != compareVibrate) sameVibrate = false;
            if (!sameRing && !sameVibrate) break;
        }
        if (sameRing) {
            mMasterAlarmNameView.setText(mAlarm.getAlarmName(0));
        } else {
            mMasterAlarmNameView.setText(R.string.set_master_alarm);
        }
        mMasterVibrateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {}
        });
        if (sameVibrate && compareVibrate) {
            mMasterVibrateButton.setChecked(true);
        } else {
            mMasterVibrateButton.setChecked(false);
        }
        mMasterVibrateButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < mAlarm.getNumAlarms(); i++) {
                    mAlarm.setAlarmVibrate(i, isChecked);
                }
                updateUI();
            }
        });
        mMasterLayoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlarmPicker(EXTRA_MASTER_RINGTONE_PICKER_ID);
            }
        });
    }

    // Opens interface to pick alarm ringtone
    public void startAlarmPicker(int requestCode) {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select alarm tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        startActivityForResult(intent, requestCode);
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

        public void itemAdded(Alarm alarm, int position) {
            mAlarmNames = alarm.getAlarmNames();
            notifyItemInserted(position);
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
                    refreshMasterAlarm();
                }
            });
            mLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPosition = getLayoutPosition();
                    startAlarmPicker(EXTRA_RINGTONE_PICKER_ID);
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
            if (requestCode == EXTRA_RINGTONE_PICKER_ID)  {
                mAlarm.setAlarmName(mPosition, name);
                mAlarm.setAlarmUri(mPosition, uri.toString());
                refreshMasterAlarm();
            } else if (requestCode == EXTRA_MASTER_RINGTONE_PICKER_ID) {
                for (int i = 0; i < mAlarm.getNumAlarms(); i++) {
                    mMasterAlarmNameView.setText(name);
                    mAlarm.setAlarmName(i, name);
                    mAlarm.setAlarmUri(i, uri.toString());
                }
            }
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
