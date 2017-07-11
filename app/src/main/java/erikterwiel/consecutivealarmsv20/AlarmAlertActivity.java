package erikterwiel.consecutivealarmsv20;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        SeekBar seekBar = (SeekBar) findViewById(R.id.alert_seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress <= 15) seekBar.setProgress(15);
        //        if (progress >= 85) seekBar.setProgress(85);
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
    }
}
