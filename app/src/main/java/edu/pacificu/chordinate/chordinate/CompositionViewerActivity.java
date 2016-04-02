package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private String mRecordedSong = "";
    private Button mEditModeBtn;
    private boolean mbIsEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition_viewer);
        Intent reviewIntent = getIntent();
        Bundle extras = reviewIntent.getExtras();
        mRecordedSong = extras.getString("recordedSong");
        char current;
        int index = 0, keyNum = 0, octNum = 0, chordNum = 0, noteNum = 0;
        boolean bNextChord = true;
        ArrayList compNotes = new ArrayList();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;
        RelativeLayout chord = (RelativeLayout) this.findViewById(R.id.childLayout);

        mEditModeBtn = (Button) findViewById(R.id.editModeButton);
        mEditModeBtn.setOnClickListener(this);
        mEditModeBtn.setTag("editMode");
        mbIsEditMode = false;

        if (mRecordedSong.length() > 1)
        {
            do {
                current = mRecordedSong.charAt(index);

                switch (current)
                {
                    case 'C':
                        keyNum = 1;
                        break;
                    case 'D':
                        keyNum = 2;
                        break;
                    case 'E':
                        keyNum = 3;
                        break;
                    case 'F':
                        keyNum = 4;
                        break;
                    case 'G':
                        keyNum = 5;
                        break;
                    case 'A':
                        keyNum = 6;
                        break;
                    case 'B':
                        keyNum = 7;
                        break;
                    case '#':
                        keyNum = -keyNum;
                        break;
                    case ';':
                        compNotes.add(-1000);
                        /*
                        try {
                            Thread.sleep (500);
                        } catch (Exception E){}

                        KeySoundPool.play(SoundID[keyNum + (octNum * 12)], 1, 1, 0, 0, 1);*/
                        break;
                }

                if (Character.isDigit(current))
                {
                    octNum = current - '1';
                    if (keyNum > 0) {
                        compNotes.add(keyNum + (octNum * 7));
                    }
                    else
                    {
                        compNotes.add (-(Math.abs(keyNum) + (octNum * 7)));
                    }
                }

                index ++;
            } while (current != '$');
        }
        chordNum = 0;
        noteNum = 0;
        for (int i = 0; i < compNotes.size(); i ++)
        {
            if ((int) compNotes.get(i) != -1000) { // TODO: Magic constant

                if (bNextChord == true) {
                    view = layoutInflater.inflate(R.layout.comp_view_item, parentLayout, false);
                    chord = (RelativeLayout) view.findViewById(R.id.childLayout);
                }
                ImageView singleNote = new ImageView(chord.getContext());
                RelativeLayout.LayoutParams noteLayoutParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //noteLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                //noteLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                noteLayoutParams.width = 20;
                noteLayoutParams.height = 20;
                noteLayoutParams.setMargins(10,(48 + Math.abs(18 * (int) compNotes.get(i))), 0, 0);
                //buttonLayoutParams.bottomMargin = 48 + Math.abs(18 *(int) compNotes.get(i));
                //child.setPadding(0, 0, 0, 48 + Math.abs(18 *(int) compNotes.get(i)));
                if ((int) compNotes.get(i) < 0)
                {
                    //button.setText("S");
                    singleNote.setBackgroundResource(R.drawable.note_s);
                }
                else {
                    //button.setText("N");
                    singleNote.setBackgroundResource(R.drawable.note);
                }

                singleNote.setTag("Note" + noteNum);
                //singleNote.setOnClickListener(this);
                chord.addView(singleNote, noteLayoutParams);

                if (bNextChord == true) {
                    chord.setTag("Chord" + chordNum);
                    ++chordNum;
                    chord.setOnClickListener(this);
                    parentLayout.addView(chord);
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

    @Override
    public void onClick (View v)
    {
        Log.d("what is the tag?", (String) v.getTag());


        int startNote = 0; // TODO: this is by note, not chord right now, fix
        if (((String) v.getTag()).contains("Chord"))
        {
            startNote = Integer.parseInt (((String)v.getTag()).substring(5));// TODO Fix magic constant

            int strIndex = 0;
            int semicolonCount = 0;

            while (startNote != semicolonCount && '$' != mRecordedSong.charAt(strIndex) && strIndex < mRecordedSong.length()) {
                if (';' == mRecordedSong.charAt(strIndex)) {// TODO: fix magic consts
                    ++semicolonCount;
                }
                ++strIndex;
            }

            final int startIndex = strIndex;

            if (mbIsEditMode) {
                // bring up edit dialog

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

                        String composition = Algorithm.compose(mRecordedSong, key, scaleType, startIndex);
                        Log.d("Composition", composition);

                        // TODO: figure out how to save changes to the string
                        /*SavedComposition compToSave = new SavedComposition(mNumComps, mCompName.getText().toString(), composition);
                        compToSave.writeItemToFile(mContextWrapper);
                        ++mNumComps;
                        Toast.makeText(getApplicationContext(), "Composition Saved", Toast.LENGTH_SHORT).show();
                        finish();*/

                       /* Intent reviewCompIntent = new Intent(KeyboardReviewActivity.this,
                                CompositionViewerActivity.class);
                        Bundle compBundle = new Bundle();
                        compBundle.putString("recordedSong", composition);
                        reviewCompIntent.putExtras(compBundle);
                        startActivity(reviewCompIntent);*/
                    }
                });




            }
            else {
                this.getTheKPlayback().playComposition (mRecordedSong, startNote);
            }
        }
        else if (((String) v.getTag()).contains("editMode"))
        {
            if (!mbIsEditMode) {
                mbIsEditMode = true;
                mEditModeBtn.setText("PLAY MODE");
            }
            else {
                mbIsEditMode = false;
                mEditModeBtn.setText("EDIT MODE");
            }
        }
    }
}
