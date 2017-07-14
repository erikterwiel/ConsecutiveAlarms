package erikterwiel.consecutivealarmsv20;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

/**
 * Created by Erik on 7/11/2017.
 */

public class AlarmAlertActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(R.layout.activity_alarm_alert);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float dpHeight = outMetrics.heightPixels / density;
        int dpHeightInt = (int) (dpHeight * 1.25);
        Log.i("Info", "The height in dp is: " + dpHeightInt);

        LinearLayout seekLayout = (LinearLayout) findViewById(R.id.alert_seek_layout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, dpHeightInt, 0, 0);
        seekLayout.setLayoutParams(layoutParams);

        SeekBar seekBar = (SeekBar) findViewById(R.id.alert_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 15) seekBar.setProgress(15);
                if (progress >= 85) seekBar.setProgress(85);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (seekBar.getProgress() != 50) {
                    CountDownTimer timer = new CountDownTimer(160, 20) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            seekBar.setProgress(seekBar.getProgress() - 5);
                            Log.i("Info", "Progress is now " + Integer.toString(seekBar.getProgress()));
                        }
                        @Override
                        public void onFinish() {}
                    };
                    timer.start();
                }
            }
        });

        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics());
        Drawable offThumb = getResources().getDrawable(R.drawable.ic_alarm_white_160px);
        Bitmap bitmap = ((BitmapDrawable) offThumb).getBitmap();
        Drawable offThumbScaled = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, px, px, true));
        seekBar.setThumb(offThumbScaled);
    }
}
