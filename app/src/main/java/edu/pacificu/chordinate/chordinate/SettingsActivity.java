package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends ChordinateActivity {

    private final static int DEFAULT_SPEED = 50;
    private final static int MAX_SPEED = 99;
    private final static int TEST_START_INDEX = 0;
    private final static String DISPLAY_TXT = "Speed: ";
    private final static String SPEED_TAG = "speed";
    private final static String TEST_COMP = "F3A3C4;G3C4E4;B3E4G4;A3D4F4;A3C4E4;G3B3D4;E3G3C4;$";

    private SharedPreferences mSharedPrefs;
    SharedPreferences.Editor mEditor;
    private SeekBar mSpeedSeek;
    private TextView mSpeedText;
    private Button mTestButton;

    /**
     * Create the display for the settings.
     *
     * @param savedInstanceState the instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mSpeedSeek = (SeekBar) findViewById(R.id.changeSpeedSeekBar);
        mSpeedText = (TextView) findViewById(R.id.speedText);
        mTestButton = (Button) findViewById(R.id.testSpeedBtn);

        mSharedPrefs = this.getPreferences(Context.MODE_PRIVATE);
        mEditor = mSharedPrefs.edit();

        mSpeedSeek.setMax(MAX_SPEED);
        mSpeedSeek.setProgress(mSharedPrefs.getInt(SPEED_TAG, DEFAULT_SPEED));
        mSpeedText.setText(DISPLAY_TXT + (mSpeedSeek.getProgress() + 1)); /* will display 1 to 100 rather than 0 to 99 */

        this.getTheKPlayback().setPlaybackSpeed(getPlaybackSpeed(mSpeedSeek.getProgress() + 1));

        /**
         * Capture information from the seek bar and set the playback speed.
         */
        mSpeedSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                mSpeedText.setText(DISPLAY_TXT + (progressValue + 1));
                ChordinateActivity.getTheKPlayback().setPlaybackSpeed(getPlaybackSpeed(progressValue + 1));
                mEditor.putInt(SPEED_TAG, progressValue);
                mEditor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ChordinateActivity.getTheKPlayback().stopPlayback();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        /**
         * Playback a sample composition when the test button is pressed.
         */
        mTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChordinateActivity.getTheKPlayback().playComposition(TEST_COMP, TEST_START_INDEX);
            }
        });
    }

    private int getPlaybackSpeed (int userSpeed) {
        int realSpeed;

        // Flip scale
        int flipped = DEFAULT_SPEED - (userSpeed - DEFAULT_SPEED) + 1;

        // Scale numbers into correct range
        int oldRange = 99;
        int newRange = 800;
        realSpeed = (((flipped - 1) * newRange) / oldRange) + 200;

        return realSpeed;
    }
}































