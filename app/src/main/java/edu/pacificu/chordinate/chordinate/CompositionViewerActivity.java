package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.lang.Math;
import java.util.ArrayList;

import edu.pacificu.chordinate.chordinate.algorithm.Algorithm;

public class CompositionViewerActivity extends ChordinateActivity implements View.OnClickListener {
    private static final char SEMICOLON = ';';
    private static final char END_OF_COMP = '$';
    private static final String CHORD_TAG = "Chord";
    private static final String NOTE_TAG = "Note";
    private static final String UNDO_BTN_TAG = "undo";
    private static final String MODE_BTN_TAG = "editMode";
    private static final String START_BTN_TAG = "start";
    private static final String STOP_BTN_TAG = "stop";
    private static final String DONE_BTN_TAG = "done";
    private static final String COMP_SAVED_MSG = "Composition Saved";
    private static final String BTN_TXT_EDIT = "EDIT MODE";
    private static final String BTN_TXT_PLAY = "EXIT\nEDIT MODE";
    private static final int CHORD_NUM_INDEX = 5;

    private String mRecordedSong = "";
    private SavedComposition mComposition;
    private Button mEditModeBtn;
    private Button mUndoBtn;
    private Button mStartBtn;
    private Button mStopBtn;
    private Button mDoneBtn;
    private boolean mbIsEditMode;
    private boolean mbEnableEditMode;
    private ContextWrapper mContextWrapper;
    private LinearLayout mParentLayout;
    private ArrayList<String> mPrevComps; /* will save all of the composition strings from this session */
    private static final int NUM_OF_NOTE_POS = 48;
    private static final int NUM_OF_LINE_POS = 24;
    private static final int END_CHORD = -1000;
    private static final int NOTE_HEIGHT = 20;
    private static final int NOTE_WIDTH = 20;
    private static final int SHARP_HEIGHT = 40;
    private static final int SHARP_WIDTH = 30;
    private static final int NOTE_LEFT_MARGIN = 30;
    private static final int SHARP_LEFT_MARGIN = 0;

    /**
     * Sets the content view, creates buttons, and initiates creation of composition view
     *
     * @param   savedInstanceState  The instance state to be created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition_viewer);

        Intent reviewIntent = getIntent();
        Bundle extras = reviewIntent.getExtras();
        mRecordedSong = extras.getString("recordedSong"); // TODO: Fix magic constants (make public so can use in the other places too)
        mComposition = new SavedComposition(extras.getString("compName"),
                                            extras.getString("dateStr"),
                                            mRecordedSong, extras.getString("fileName"));

        mPrevComps = new ArrayList<String>();

        mbEnableEditMode = extras.getBoolean("enableEditMode");
        mContextWrapper = this;

        mParentLayout = (LinearLayout) findViewById(R.id.parentLayout);

        mbIsEditMode = false;
        mEditModeBtn = (Button) findViewById(R.id.editModeButton);
        if (!mbEnableEditMode) {
            mEditModeBtn.setEnabled(false);
            mEditModeBtn.setVisibility(View.GONE);
        }
        else {
            mEditModeBtn.setOnClickListener(this);
            mEditModeBtn.setTag(MODE_BTN_TAG);
        }

        mUndoBtn = (Button) findViewById(R.id.undoButton);
        if (!mbEnableEditMode)
        {
            mUndoBtn.setEnabled(false);
            mUndoBtn.setVisibility(View.GONE);
        }

        else {
            mUndoBtn.setOnClickListener(this);
            mUndoBtn.setTag(UNDO_BTN_TAG);
            if (mPrevComps.size() <= 0) {
                mUndoBtn.setEnabled(false);
            }
        }
        mStartBtn = (Button) findViewById(R.id.start_comp);
        mStartBtn.setOnClickListener(this);
        mStartBtn.setTag(START_BTN_TAG);

        mStopBtn = (Button) findViewById(R.id.stop_comp);
        mStopBtn.setOnClickListener(this);
        mStopBtn.setTag(STOP_BTN_TAG);

        mDoneBtn = (Button) findViewById (R.id.doneButton);
        mDoneBtn.setOnClickListener(this);
        mDoneBtn.setTag(DONE_BTN_TAG);

        displayNotes();
    }
    /**
     * Determines position of each line and note on the grand scale. Creates each chord in its
     * own layout, adds each chord to the parent layout, and sets all the onClickListeners
     */
    private void displayNotes () { // TODO: Fix magic constants in this function
        char current;
        int index = 0, keyNum = 0, octNum, chordNum, noteNum, noteGap, lineGap;
        boolean bNextChord = true;
        ArrayList compNotes = new ArrayList();
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;
        RelativeLayout chord = (RelativeLayout) this.findViewById(R.id.childLayout);

        mParentLayout.removeViews(1, mParentLayout.getChildCount() - 1);// TODO: fix magic const

        // Determine size of gaps between notes, in pixels
        Point screenSize = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        noteGap = (screenSize.y - NOTE_HEIGHT) / NUM_OF_NOTE_POS;
        lineGap = (screenSize.y - NOTE_HEIGHT) / NUM_OF_LINE_POS;

        // Parse the composition, and determine  of gaps between a given note and the top
        // of the screen. Assigns them to an array for the display
        if (mRecordedSong.length() > 1)
        {
            do {
                current = mRecordedSong.charAt(index);

                switch (current)
                {
                    case 'C':
                        keyNum = 7;
                        break;
                    case 'D':
                        keyNum = 6;
                        break;
                    case 'E':
                        keyNum = 5;
                        break;
                    case 'F':
                        keyNum = 4;
                        break;
                    case 'G':
                        keyNum = 3;
                        break;
                    case 'A':
                        keyNum = 2;
                        break;
                    case 'B':
                        keyNum = 1;
                        break;
                    case '#':
                        keyNum = -keyNum;
                        break;
                    case SEMICOLON:
                        compNotes.add(END_CHORD);
                        break;
                }

                if (Character.isDigit(current))
                {
                    octNum = 6 - (current - '1');
                    if (keyNum > 0) {
                        compNotes.add((keyNum + (octNum * 7)) - 3);
                    }
                    else
                    {
                        compNotes.add (-((Math.abs(keyNum) + (octNum * 7)) - 3));
                    }
                }

                index ++;
            } while (current != END_OF_COMP);
        }
        chordNum = 0;
        noteNum = 0;

        view = layoutInflater.inflate(R.layout.comp_view_item, mParentLayout, false);
        chord = (RelativeLayout) view.findViewById((R.id.childLayout));
        RelativeLayout.LayoutParams trebleParams = new RelativeLayout.LayoutParams(100, lineGap * 5);
        trebleParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        trebleParams.setMargins(0, (lineGap * ((NUM_OF_LINE_POS / 2) - 5)), 0, 0);
        RelativeLayout.LayoutParams bassParams = new RelativeLayout.LayoutParams(150, lineGap * 4);
        bassParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        bassParams.setMargins(0, (lineGap * ((NUM_OF_LINE_POS / 2) + 1)), 0, 0);
        ImageView trebleClef = new ImageView(chord.getContext()), bassClef = new ImageView(chord.getContext());
        trebleClef.setBackgroundResource(R.drawable.treble_clef);
        bassClef.setBackgroundResource(R.drawable.bass_clef);
        chord.addView(trebleClef, trebleParams);
        chord.addView(bassClef, bassParams);

        for (int i = 0; i < NUM_OF_LINE_POS; i ++)
        {
            RelativeLayout.LayoutParams lineLayoutParams = new RelativeLayout.LayoutParams
                    (150, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageView singleLine = new ImageView(chord.getContext());
            lineLayoutParams.setMargins(0,(lineGap * i) + 7, 0, 0);
            if (i == (NUM_OF_LINE_POS / 2) || i < ((NUM_OF_LINE_POS / 2) - 5)
                    || i > ((NUM_OF_LINE_POS / 2) + 5)) {
                singleLine.setBackgroundResource(R.drawable.music_staff_line_mid_c);
            }
            else
            {

                singleLine.setBackgroundResource(R.drawable.music_staff_line);
            }
            chord.addView(singleLine, lineLayoutParams);
        }
        mParentLayout.addView(chord);


        // With the array of distances for each notes, create each view with each chord and set
        // each chord's onClickListener
        for (int i = 0; i < compNotes.size(); i ++)
        {
            if ((int) compNotes.get(i) != END_CHORD) {

                if (bNextChord == true) {
                    view = layoutInflater.inflate(R.layout.comp_view_item, mParentLayout, false);
                    chord = (RelativeLayout) view.findViewById(R.id.childLayout);

                    for (int j = 0; j < NUM_OF_LINE_POS; j ++)
                    {
                        RelativeLayout.LayoutParams lineLayoutParams = new RelativeLayout.LayoutParams
                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        ImageView singleLine = new ImageView(chord.getContext());
                        lineLayoutParams.setMargins(0,(lineGap * j) + 7, 0, 0);
                        if (j == (NUM_OF_LINE_POS / 2) || j < ((NUM_OF_LINE_POS / 2) - 5)
                                || j > ((NUM_OF_LINE_POS / 2) + 5)) {
                            singleLine.setBackgroundResource(R.drawable.music_staff_line_mid_c);
                        }
                        else
                        {

                            singleLine.setBackgroundResource(R.drawable.music_staff_line);
                        }
                        chord.addView(singleLine, lineLayoutParams);
                    }
                }
                ImageView singleNote = new ImageView(chord.getContext());
                RelativeLayout.LayoutParams noteLayoutParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                noteLayoutParams.width = NOTE_WIDTH;
                noteLayoutParams.height = NOTE_HEIGHT;
                noteLayoutParams.setMargins(NOTE_LEFT_MARGIN,
                        (noteGap * (Math.abs((int) compNotes.get(i)) - 1)), 0, 0);

                if ((int) compNotes.get(i) < 0)
                {
                    ImageView sharp = new ImageView(chord.getContext());
                    RelativeLayout.LayoutParams sharpLayoutParams = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    sharpLayoutParams.width = SHARP_WIDTH;
                    sharpLayoutParams.height = SHARP_HEIGHT;
                    sharpLayoutParams.setMargins(SHARP_LEFT_MARGIN,
                            (noteGap * (Math.abs((int) compNotes.get(i)) - 1)) - 10, 0, 0);
                    sharp.setTag("Note" + chordNum);
                    sharp.setOnClickListener(this);
                    sharp.setBackgroundResource(R.drawable.sharp);
                    chord.addView(sharp, sharpLayoutParams);

                    singleNote.setBackgroundResource(R.drawable.note_s);

                }
                else {
                    singleNote.setBackgroundResource(R.drawable.note);
                }

                singleNote.setTag(NOTE_TAG + noteNum);
                chord.addView(singleNote, noteLayoutParams);

                if (bNextChord == true) {
                    chord.setTag(CHORD_TAG + chordNum);
                    ++chordNum;
                    chord.setOnClickListener(this);
                    mParentLayout.addView(chord);
                    bNextChord = false;
                }
            }
            else
            {
                noteNum = i + 1;
                bNextChord = true;
            }
        }
    }

    /**
     * Overrides onDestroy to stop KeyPlayback
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        this.getTheKPlayback().stopPlayback();
    }

    /**
     * Overrides onPause to stop KeyPlayback
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        this.getTheKPlayback().stopPlayback();
    }

    /**
     * Overrides onStop to stop KeyPlayback
     */
    @Override
    protected void onStop()
    {
        super.onStop();

        this.getTheKPlayback().stopPlayback();
    }

    /**
     * Sets functions for all buttons' onClick
     *
     * @param v the view being clicked
     */
    @Override
    public void onClick (View v)
    {
        // onClick for a Chord in the viewer
        if (((String) v.getTag()).contains(CHORD_TAG))
        {
            final int startIndex = getStringIndex(mRecordedSong, Integer.parseInt(((String)v.getTag()).substring(CHORD_NUM_INDEX)));

            // Brings up editor dialog box, if in Edit Mode
            if (mbIsEditMode) {
                final Dialog chooseOpts = new Dialog(this);
                chooseOpts.setContentView(R.layout.choose_comp_options);
                chooseOpts.show();

                String[] keys = {"Let us decide", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
                ArrayAdapter<String> keysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, keys);
                final Spinner keysSpin = (Spinner) chooseOpts.findViewById(R.id.spin_choose_key);
                keysSpin.setAdapter(keysAdapter);

                String[] scaleTypes = {"Major", "Natural Minor", "Harmonic Minor", "Melodic Minor"};
                ArrayAdapter<String> scaleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, scaleTypes);
                final Spinner scaleSpin = (Spinner) chooseOpts.findViewById(R.id.spin_choose_scale);
                scaleSpin.setAdapter(scaleAdapter);

                Button doneBtn = (Button) chooseOpts.findViewById(R.id.btn_done);
                doneBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String key = (String) keysSpin.getSelectedItem();
                        String scaleType = (String) scaleSpin.getSelectedItem();

                        chooseOpts.dismiss();
                        mbIsEditMode = false;
                        mEditModeBtn.setText(BTN_TXT_EDIT);

                        String composition = Algorithm.compose(mRecordedSong, key, scaleType, startIndex);

                        mComposition.setNotesString(mRecordedSong.substring(0, startIndex) + composition);
                        mPrevComps.add(mRecordedSong);

                        mUndoBtn.setEnabled(true);

                        mRecordedSong = mComposition.getNotes();
                        mComposition.writeItemToFile(mContextWrapper);

                        displayNotes();
                        Toast.makeText(getApplicationContext(), COMP_SAVED_MSG, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // Otherwise, start KeyPlayback from selected Chord
            else {
                this.getTheKPlayback().stopPlayback();
                this.getTheKPlayback().playComposition (mRecordedSong, startIndex);
            }
        }
        // onClick for Edit Mode Button
        else if (((String) v.getTag()).contains(MODE_BTN_TAG))
        {
            if (!mbIsEditMode) {
                mbIsEditMode = true;
                mEditModeBtn.setText(BTN_TXT_PLAY);
            }
            else {
                mbIsEditMode = false;
                mEditModeBtn.setText(BTN_TXT_EDIT);
            }
        }
        // onClick for Undo Button
        else if (((String) v.getTag()).contains(UNDO_BTN_TAG)) {
            if (mPrevComps.size() > 0) {
                mRecordedSong = mPrevComps.get(mPrevComps.size() - 1);
                mPrevComps.remove(mPrevComps.size() - 1);

                mComposition.setNotesString(mRecordedSong);
                mComposition.writeItemToFile(mContextWrapper);

                displayNotes();
                Toast.makeText(getApplicationContext(), COMP_SAVED_MSG, Toast.LENGTH_SHORT).show();
            }

            if (mPrevComps.size() <= 0) {
                mUndoBtn.setEnabled(false);
            }
        }

        // onClick for Start Button
        else if (((String) v.getTag()).contains(START_BTN_TAG))
        {
            this.getTheKPlayback().stopPlayback();
            this.getTheKPlayback().playComposition (mRecordedSong, 0);
        }

        // onClick for Stop Button
        else if (((String) v.getTag()).contains(STOP_BTN_TAG))
        {
            this.getTheKPlayback().stopPlayback();
        }

        // onClick for Done Button
        else if (((String) v.getTag()).contains(DONE_BTN_TAG))
        {
            finish();
        }
    }

    /**
     * Finds the index of the string to start reading from.
     *
     * @param startingIndex the array index of the chord to begin with
     * @return the index in the melody string to start at
     */
    private static int getStringIndex (String comp, int startingIndex) {
        int strIndex = 0;
        int semicolonCount = 0;

        while (startingIndex != semicolonCount && END_OF_COMP != comp.charAt(strIndex) && strIndex < comp.length()) {
            if (SEMICOLON == comp.charAt(strIndex)) {
                ++semicolonCount;
            }
            ++strIndex;
        }

        return strIndex;
    }
}
