package erikterwiel.consecutivealarmsv20;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;

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

        SeekBar seekBar = (SeekBar) findViewById(R.id.alert_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 3) seekBar.setProgress(3);
                if (progress > 97) seekBar.setProgress(97);
                if (progress == 3 || progress == 97) setThumbOff(seekBar);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (seekBar.getProgress() == 3 || seekBar.getProgress() == 97) {
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
}
