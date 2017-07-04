package erikterwiel.consecutivealarmsv20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Erik on 6/22/2017.
 */

public class EditAlarmActivity extends AppCompatActivity{

    // Creating unique extras
    private static final String EXTRA_ALARM_TO = "erikterwiel.consecutivealarmsv20.extra_alarm_to";
    private static final String EXTRA_ALARM_FROM = "erikterwiel.consecutivealarmsv20.extra_alarm_from";

    // Lists functionality variables
    private Alarm mAlarm;
//    private AlarmAdapter mAlarmAdapter;

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

        // Displays and wires from time

    }

    // Simplifies sending alarm objects through intents
    public static Intent newIntent(Context packageContext, Alarm recievedAlarm) {
        Intent i = new Intent(packageContext, EditAlarmActivity.class);
        i.putExtra(EXTRA_ALARM_TO, recievedAlarm);
        return i;
    }
}
