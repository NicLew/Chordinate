package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class KeyboardReviewActivity extends KeyboardActivity implements View.OnTouchListener {

    Button redoButton;
    Button saveAsButton;
    Button chordinateButton;
    Button playButton;
    EditText compName;
    String recordedSong = "";

    // TODO: Test and refactor
    private ArrayList<SavedComposition> mSavedFiles = new ArrayList<SavedComposition>();

    /**
     * Creates the view for the Keyboard Review Activity
     *
     * @param savedInstanceState    The instance to be created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_review);
        Intent reviewIntent = getIntent();
        Bundle extras = reviewIntent.getExtras();
        recordedSong = extras.getString("recordedSong");

        redoButton = (Button) findViewById(R.id.redoButton);
        redoButton.setOnTouchListener(this);

        saveAsButton = (Button) findViewById(R.id.saveAsIsButton);
        saveAsButton.setOnTouchListener(this);

        playButton = (Button) findViewById(R.id.reviewPlay);
        playButton.setOnTouchListener(this);

        chordinateButton = (Button) findViewById(R.id.chordinateButton);
        chordinateButton.setOnTouchListener(this);

        compName = (EditText) findViewById(R.id.newCompName);

    }

    /**
     * Performs the proper actions when a button is pressed
     *
     * @param v         The view being pressed
     * @param event     The touch event last sensed
     */
    public boolean onTouch (View v, MotionEvent event)
    {
        int id = v.getId();
        String filename = "newComposition";
        String contents = "";
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            switch (id)
            {
                case R.id.saveAsIsButton:
                    SavedComposition current = new SavedComposition(mSavedFiles.size() - 1, compName.getText().toString(), recordedSong);
                    mSavedFiles.add(current);
                    //filename = compName.getText().toString();
                    //contents = recordedSong;
                    OutputStreamWriter fOut;

                    try {
                        fOut = new OutputStreamWriter(openFileOutput(current.getFileName() + ".chd", MODE_PRIVATE));
                        fOut.write(current.toString());
                        fOut.close();
                        Toast.makeText(getApplicationContext(), ".chd saved",
                                Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), ".chd not saved",
                                Toast.LENGTH_SHORT).show();
                    }

                    finish ();
                    break;

                case R.id.chordinateButton:
                    /*
                    filename = compName.getText().toString();
                    String line = " ";
                    try {
                        InputStreamReader isr = new InputStreamReader(openFileInput(filename + ".txt"));
                        BufferedReader bReader = new BufferedReader(isr);
                        line = bReader.readLine();
                        Toast.makeText(getApplicationContext(), line, Toast.LENGTH_SHORT).show();
                    }catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                    */

                    break;
                case R.id.redoButton:

                    break;
                case R.id.reviewPlay:
                    KPlayback.playComposition (recordedSong);
                    break;
            }
        }
        return true;
    }
    /*
    /**
     * Parses a string of notes and plays them
     *
     * @param comp      the composition to be played
     *
    public void playComposition (String comp)
    {
        char current;
        int index = 0, keyNum = 0, octNum = 0;

        if (comp.length() > 1)
        {
            do {
                current = comp.charAt(index);

                switch (current)
                {
                    case 'C':
                        keyNum = 0;
                        break;
                    case 'D':
                        keyNum = 2;
                        break;
                    case 'E':
                        keyNum = 4;
                        break;
                    case 'F':
                        keyNum = 5;
                        break;
                    case 'G':
                        keyNum = 7;
                        break;
                    case 'A':
                        keyNum = 9;
                        break;
                    case 'B':
                        keyNum = 11;
                        break;
                    case '#':
                        keyNum ++;
                        break;
                    case ';':
                        try {
                            Thread.sleep (500);
                        } catch (Exception E){}

                        KeyboardSoundPool.play(SoundID[keyNum + (octNum * 12)], 1, 1, 0, 0, 1);
                        break;
                }

                if (Character.isDigit(current))
                {
                    octNum = current - '1';
                }

                index ++;
            } while (current != '$');
        }
    }*/
}
