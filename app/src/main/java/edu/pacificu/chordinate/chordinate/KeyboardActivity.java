package edu.pacificu.chordinate.chordinate;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class KeyboardActivity extends ChordinateActivity implements View.OnTouchListener {

    // Button, Spinner, and TextView objects
    Button C1Button;
    Button CS1Button;
    Button D1Button;
    Button DS1Button;
    Button E1Button;
    Button F1Button;
    Button FS1Button;
    Button G1Button;
    Button GS1Button;
    Button A1Button;
    Button AS1Button;
    Button B1Button;
    Button C2Button;
    Button CS2Button;
    Button D2Button;
    Button DS2Button;
    Button E2Button;
    Button startRecord;
    Button stopRecord;
    Spinner OctSpinner;
    TextView PlayedNote;
    private boolean bIsRecording;
    private String recording;

    /**
     * Sets the content view, initializes buttons and spinner, and loads the sounds for the
     * keyboard.
     *
     * @param savedInstanceState    The instance state to create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        String[] octaves = {"C1", "C2", "C3", "C4", "C5", "C6"};
        ArrayAdapter<String> stringArrayAdapter =
                new ArrayAdapter<String>
                        (this,android.R.layout.simple_spinner_dropdown_item, octaves);
        C1Button = (Button) findViewById(R.id.C1_button);
        C1Button.setOnTouchListener(this);

        CS1Button = (Button) findViewById(R.id.CS1_button);
        CS1Button.setOnTouchListener(this);

        D1Button = (Button) findViewById(R.id.D1_button);
        D1Button.setOnTouchListener(this);

        DS1Button = (Button) findViewById(R.id.DS1_button);
        DS1Button.setOnTouchListener(this);

        E1Button = (Button) findViewById(R.id.E1_button);
        E1Button.setOnTouchListener(this);

        F1Button = (Button) findViewById(R.id.F1_button);
        F1Button.setOnTouchListener(this);

        FS1Button = (Button) findViewById(R.id.FS1_button);
        FS1Button.setOnTouchListener(this);

        G1Button = (Button) findViewById(R.id.G1_button);
        G1Button.setOnTouchListener(this);

        GS1Button = (Button) findViewById(R.id.GS1_button);
        GS1Button.setOnTouchListener(this);

        A1Button = (Button) findViewById(R.id.A1_button);
        A1Button.setOnTouchListener(this);

        AS1Button = (Button) findViewById(R.id.AS1_button);
        AS1Button.setOnTouchListener(this);

        B1Button = (Button) findViewById(R.id.B1_button);
        B1Button.setOnTouchListener(this);

        C2Button = (Button) findViewById(R.id.C2_button);
        C2Button.setOnTouchListener(this);

        CS2Button = (Button) findViewById(R.id.CS2_button);
        CS2Button.setOnTouchListener(this);

        D2Button = (Button) findViewById(R.id.D2_button);
        D2Button.setOnTouchListener(this);

        DS2Button = (Button) findViewById(R.id.DS2_button);
        DS2Button.setOnTouchListener(this);

        E2Button = (Button) findViewById(R.id.E2_button);
        E2Button.setOnTouchListener(this);

        startRecord = (Button) findViewById (R.id.start_record);
        startRecord.setOnTouchListener(this);

        stopRecord = (Button) findViewById(R.id.stop_record);
        stopRecord.setOnTouchListener(this);
        stopRecord.setEnabled(false);

        OctSpinner = (Spinner) findViewById(R.id.spinner);
        OctSpinner.setAdapter(stringArrayAdapter);
        OctSpinner.setSelection(3);

        PlayedNote = (TextView) findViewById(R.id.textView);

        recording = "";
        bIsRecording = false;
    }

    /**
     * Resets the start and stop record buttons on resume
     */
    @Override
    protected void onResume() {
        super.onResume();

        startRecord.setEnabled(true);
        stopRecord.setEnabled(false);
    }

    /**
     * Performs the proper actions when an on touch event occurs based on the button and
     * the action performed
     *
     * @param v         The view being pressed
     * @param event     The touch event last sensed
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        int id = v.getId(), keyNum = 0;
        int octNum;
        String sId = (String) OctSpinner.getSelectedItem();
        String newNote = "";

        // Set octave
        octNum = sId.charAt(1) - '0';

        // Actions when C, F, or the second C key is pressed
        if (id == R.id.C1_button ||
                id == R.id.F1_button ||
                id == R.id.C2_button) {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                    event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                v.setBackgroundResource(R.drawable.white_left_key_press);
                switch (id) {
                    case R.id.C1_button:
                        newNote = "C" + octNum;
                        keyNum = 0;
                        break;
                    case R.id.F1_button:
                        newNote = "F" + octNum;
                        keyNum = 5;
                        break;
                    case R.id.C2_button:
                        newNote = "C" + (octNum + 1);
                        keyNum = 12;
                        break;
                }
                PlayedNote.setText(newNote);
                this.getTheKPlayback().play (this.getTheKPlayback().SoundID[keyNum + ((octNum - 1) * 12)], 1, 1, 0, 0, 1);
                if (bIsRecording)
                {
                    recording = recording.concat(newNote + ";");
                }

            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_left_key);
            }
        }

        // Actions when D, G, A, or the second D key is pressed
        else if (id == R.id.D1_button ||
                 id == R.id.G1_button ||
                 id == R.id.A1_button ||
                 id == R.id.D2_button) {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                    event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                v.setBackgroundResource(R.drawable.white_middle_key_press);
                switch (id)
                {
                    case R.id.D1_button:
                        newNote = "D" + octNum;
                        keyNum = 2; break;
                    case R.id.G1_button:
                        newNote = "G" + octNum;
                        keyNum = 7; break;
                    case R.id.A1_button:
                        newNote = "A" + octNum;
                        keyNum = 9; break;
                    case R.id.D2_button:
                        newNote = "D" + (octNum + 1);
                        keyNum = 14; break;
                }
                PlayedNote.setText(newNote);
                this.getTheKPlayback().play(this.getTheKPlayback().SoundID[keyNum + ((octNum - 1) * 12)], 1, 1, 0, 0, 1);
                if (bIsRecording)
                {
                    recording = recording.concat(newNote + ";");
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_middle_key);
            }
        }

        // Actions when E, B, or the second E key is pressed
        else if (id == R.id.E1_button ||
                 id == R.id.B1_button ||
                 id == R.id.E2_button) {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                    event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                v.setBackgroundResource(R.drawable.white_right_key_press);
                switch (id)
                {
                    case R.id.E1_button:
                        newNote = "E" + octNum;
                        keyNum = 4; break;
                    case R.id.B1_button:
                        newNote = "B" + octNum;
                        keyNum = 11; break;
                    case R.id.E2_button:
                        newNote = "E" + (octNum + 1);
                        keyNum = 16; break;
                }
                PlayedNote.setText(newNote);
                this.getTheKPlayback().play(this.getTheKPlayback().SoundID[keyNum + ((octNum - 1) * 12)], 1, 1, 0, 0, 1);

                if (bIsRecording)
                {
                    recording = recording.concat(newNote + ";");
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_right_key);
            }
        }

        // Actions when a black key is pressed
        else if (id == R.id.CS1_button ||
                 id == R.id.DS1_button ||
                 id == R.id.FS1_button ||
                 id == R.id.GS1_button ||
                 id == R.id.AS1_button ||
                 id == R.id.CS2_button ||
                 id == R.id.DS2_button) {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                    event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                v.setBackgroundResource(R.drawable.black_key_press);
                switch (id)
                {
                    case R.id.CS1_button:
                        newNote = "C#" + octNum;
                        keyNum = 1; break;
                    case R.id.DS1_button:
                        newNote = "D#" + octNum;
                        keyNum = 3; break;
                    case R.id.FS1_button:
                        newNote = "F#" + octNum;
                        keyNum = 6; break;
                    case R.id.GS1_button:
                        newNote = "G#" + octNum;
                        keyNum = 8; break;
                    case R.id.AS1_button:
                        newNote = "A#" + octNum;
                        keyNum = 10; break;
                    case R.id.CS2_button:
                        newNote = "C#" + (octNum + 1);
                        keyNum = 13; break;
                    case R.id.DS2_button:
                        newNote = "D#" + (octNum + 1);
                        keyNum = 15; break;
                }
                PlayedNote.setText(newNote);
                this.getTheKPlayback().play (this.getTheKPlayback().SoundID[keyNum + ((octNum - 1) * 12)], 1, 1, 0, 0, 1);

                if (bIsRecording)
                {
                    recording = recording.concat(newNote + ";");
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.black_key);
            }
        }

        // Action when Record button is pressed
        else if (id == R.id.start_record &&
                (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                 event.getAction() == MotionEvent.ACTION_DOWN))
        {
            bIsRecording = true;
            startRecord.setEnabled(false);
            stopRecord.setEnabled(true);
        }

        // Action when Stop button is pressed
        else if (id == R.id.stop_record &&
                (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                        event.getAction() == MotionEvent.ACTION_DOWN))
        {
            bIsRecording = false;
            recording = recording.concat("$");

            Intent reviewIntent = new Intent(KeyboardActivity.this, KeyboardReviewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("recordedSong", recording);
            reviewIntent.putExtras(bundle);
            startActivity(reviewIntent);
            recording = "";
        }
        return false;
    }
}


