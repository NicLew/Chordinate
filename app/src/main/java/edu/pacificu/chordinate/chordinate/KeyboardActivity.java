package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class KeyboardActivity extends AppCompatActivity implements View.OnTouchListener {

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
    Spinner OctSpinner;
    TextView PlayedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        String[] octaves = {"Low C", "Middle C", "High C"};
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

        OctSpinner = (Spinner) findViewById(R.id.spinner);
        OctSpinner.setAdapter(stringArrayAdapter);

        PlayedNote = (TextView) findViewById(R.id.textView);
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        int id = v.getId();
        if (id == R.id.C1_button ||
                id == R.id.F1_button ||
                id == R.id.C2_button) {
            if (event.getAction() == MotionEvent.ACTION_BUTTON_PRESS ||
                    event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_HOVER_ENTER) {
                v.setBackgroundResource(R.drawable.white_left_key_press);
                switch (id)
                {
                    case R.id.C1_button:
                        PlayedNote.setText("C1"); break;
                    case R.id.F1_button:
                        PlayedNote.setText("F1"); break;
                    case R.id.C2_button:
                        PlayedNote.setText("C2"); break;
                }

            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_left_key);
            }
        }

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
                        PlayedNote.setText("D1"); break;
                    case R.id.G1_button:
                        PlayedNote.setText("G1"); break;
                    case R.id.A1_button:
                        PlayedNote.setText("A1"); break;
                    case R.id.D2_button:
                        PlayedNote.setText("D2"); break;
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_middle_key);
            }
        }

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
                        PlayedNote.setText("E1"); break;
                    case R.id.B1_button:
                        PlayedNote.setText("B1"); break;
                    case R.id.E2_button:
                        PlayedNote.setText("E2"); break;
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.white_right_key);
            }
        }

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
                        PlayedNote.setText("C#1"); break;
                    case R.id.DS1_button:
                        PlayedNote.setText("D#1"); break;
                    case R.id.FS1_button:
                        PlayedNote.setText("F#1"); break;
                    case R.id.GS1_button:
                        PlayedNote.setText("G#1"); break;
                    case R.id.AS1_button:
                        PlayedNote.setText("A#1"); break;
                    case R.id.CS2_button:
                        PlayedNote.setText("C#2"); break;
                    case R.id.DS2_button:
                        PlayedNote.setText("D#2"); break;
                }
            } else if (event.getAction() == MotionEvent.ACTION_BUTTON_RELEASE ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                v.setBackgroundResource(R.drawable.black_key);
            }
        }
        return false;
    }
}


