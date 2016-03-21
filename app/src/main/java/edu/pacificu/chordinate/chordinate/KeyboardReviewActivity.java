package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.pacificu.chordinate.chordinate.algorithm.Algorithm;
import edu.pacificu.chordinate.chordinate.algorithm.Scale;

public class KeyboardReviewActivity extends KeyboardActivity implements View.OnTouchListener {

    private Button mRedoButton;
    private Button mSaveAsButton;
    private Button mChordinateButton;
    private Button mPlayButton;
    private EditText mCompName;
    private String mRecordedSong = "";

    public static final String MY_PREFS_NAME = "MyKeyReviewPrefs";
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

    /**
     * Saves the total number of compositions in a shared preferences variable.
     */
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
     * @param view      The view being pressed
     * @param event     The touch event last sensed
     */
    public boolean onTouch (View view, MotionEvent event)
    {
        int id = view.getId();

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

                    String composition = Algorithm.compose(mRecordedSong, Scale.ScaleType.MAJOR);// TODO: allow user to choose scale type
                    Log.d("Composition", composition);
                    this.getTheKPlayback().playComposition (composition);// For testing purposes

                    break;
                case R.id.redoButton:

                    break;
                case R.id.reviewPlay:
                    //this.getTheKPlayback().playComposition (mRecordedSong, 0);
                    Intent reviewViewIntent = new Intent(KeyboardReviewActivity.this,
                            CompositionViewerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString ("recordedSong", mRecordedSong);
                    reviewViewIntent.putExtras(bundle);
                    startActivity (reviewViewIntent);
                    break;
            }
        }
        return true;
    }
}
