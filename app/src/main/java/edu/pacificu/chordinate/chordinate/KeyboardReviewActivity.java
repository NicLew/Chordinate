package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class KeyboardReviewActivity extends KeyboardActivity implements View.OnTouchListener {

    private Button mRedoButton;
    private Button mSaveAsButton;
    private Button mChordinateButton;
    private Button mPlayButton;
    private EditText mCompName;
    private String mRecordedSong = "";

    // TODO: Test and refactor
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private int mNumComps;
    private ContextWrapper mContextWrapper = this;

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
        mRecordedSong = extras.getString("recordedSong");

        mNumComps = 0;
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        mNumComps = prefs.getInt("numComps", 0); //0 is the default value

        mRedoButton = (Button) findViewById(R.id.redoButton);
        mRedoButton.setOnTouchListener(this);

        mSaveAsButton = (Button) findViewById(R.id.saveAsIsButton);
        mSaveAsButton.setOnTouchListener(this);

        mPlayButton = (Button) findViewById(R.id.reviewPlay);
        mPlayButton.setOnTouchListener(this);

        mChordinateButton = (Button) findViewById(R.id.chordinateButton);
        mChordinateButton.setOnTouchListener(this);

        mCompName = (EditText) findViewById(R.id.newCompName);
        mCompName.setText("Composition #" + Integer.toString(mNumComps));

    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("numComps", mNumComps);
        editor.commit();
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

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            switch (id)
            {
                case R.id.saveAsIsButton:
                    SavedComposition current = new SavedComposition(mNumComps, mCompName.getText().toString(), mRecordedSong);
                    current.writeItemToFile(mContextWrapper);
                    ++mNumComps;
                    Toast.makeText(getApplicationContext(), "Composition Saved", Toast.LENGTH_SHORT).show();

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
                    KPlayback.playComposition (mRecordedSong);
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
