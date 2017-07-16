package erikterwiel.consecutivealarms;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Erik on 7/11/2017.
 */

public class AlarmAlertActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Shows activity when locked and hides navigation bar
        final Window window = getWindow();
        View decorView = window.getDecorView();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_alarm_alert);

        // Displays alarm time and name
        TextView alarmTime = (TextView) findViewById(R.id.alert_time);
        TextView alarmAM = (TextView) findViewById(R.id.alert_am);
        TextView alarmLabel = (TextView) findViewById(R.id.alert_label);
        Calendar calendar = Calendar.getInstance();
        Time currentTime = Alarm.getTime(
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        alarmTime.setText(currentTime.getTime());
        alarmAM.setText(currentTime.getMorning());
        String label = getIntent().getStringExtra("alarmLabel");
        if (label == null) label = "Alarm";
        alarmLabel.setText(label);

        // Sets gap between alarm name and swipe bar based on screen height
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        int pxHeight = outMetrics.heightPixels / 3;
        LinearLayout seekLayout = (LinearLayout) findViewById(R.id.alert_seek_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, pxHeight);
        seekLayout.setLayoutParams(layoutParams);

        // Sets up SeekBar and listeners
        SeekBar seekBar = (SeekBar) findViewById(R.id.alert_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            Vibrator gravityVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            boolean gravitated = false;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 3) seekBar.setProgress(3);
                if (progress > 97) seekBar.setProgress(97);
                if (seekBar.getProgress() == 3 || seekBar.getProgress() == 97) {
                    setThumbOff(seekBar);
                    if (!gravitated) {
                        gravityVibrator.vibrate(50);
                        gravitated = true;
                    }
                } else {
                    setThumbOn(seekBar);
                    if (gravitated) {
                        gravitated = false;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                Log.i("Info", "Progress is: " + seekBar.getProgress());
                if (seekBar.getProgress() < 20) {
                    animate(seekBar, 3, 175);
                    if (!gravitated) {
                        gravityVibrator.vibrate(50);
                        gravitated = true;
                    }
                    turnOffAlarm();
                } else if (seekBar.getProgress() > 80) {
                    animate(seekBar, 97, 175);
                    if (!gravitated) {
                        gravityVibrator.vibrate(50);
                        gravitated = true;
                    }
                    turnOffAlarm();
                } else animate(seekBar, 50, 250);
            }
        });
        setThumbOn(seekBar);
    }

    // Sets size of alarm on thumb slider
    private void setThumbOn(SeekBar seekBar) {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        BitmapDrawable offThumb = (BitmapDrawable)  ContextCompat.getDrawable(
                this, R.drawable.ic_alarm_white_circle_160px);
        Bitmap bitmap = offThumb.getBitmap();
        Drawable offThumbScaled = new BitmapDrawable(
                getResources(), Bitmap.createScaledBitmap(bitmap, px, px, true));
        seekBar.setThumb(offThumbScaled);
    }

    // Sets size of alarm off thumb slider
    private void setThumbOff(SeekBar seekBar) {
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        BitmapDrawable offThumb = (BitmapDrawable)  ContextCompat.getDrawable(
                this, R.drawable.ic_alarm_off_white_circle_160px);
        Bitmap bitmap = offThumb.getBitmap();
        Drawable offThumbScaled = new BitmapDrawable(
                getResources(), Bitmap.createScaledBitmap(bitmap, px, px, true));
        seekBar.setThumb(offThumbScaled);
    }

    // Animates the movement of SeekBar slider
    private void animate(SeekBar seekBar, int progress, int speed) {
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", progress);
        animation.setDuration(speed);
        animation.setInterpolator(new OvershootInterpolator());
        animation.start();
    }

    // Called when alarm is turned off
    private void turnOffAlarm() {
        Intent receiverIntent = new Intent(this, AlarmFinishReceiver.class);
        receiverIntent.putExtra("masterID", getIntent().getStringExtra("masterID"));
        sendBroadcast(receiverIntent);
        if (getIntent().getBooleanExtra("killModify", true)) {
            SharedPreferences alarmDatabase =
                    getSharedPreferences("AlarmDatabase", Context.MODE_PRIVATE);
            SharedPreferences.Editor databaseEditor = alarmDatabase.edit();
            databaseEditor.putBoolean("killYourself", true);
            databaseEditor.apply();
        }
        finish();
    }
}
