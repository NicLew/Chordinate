package edu.pacificu.chordinate.chordinate;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.lang.Math;

import java.util.ArrayList;

public class CompositionViewerActivity extends ChordinateActivity implements View.OnClickListener {
    private String mRecordedSong = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition_viewer);
        Intent reviewIntent = getIntent();
        Bundle extras = reviewIntent.getExtras();
        mRecordedSong = extras.getString("recordedSong");
        char current;
        int index = 0, keyNum = 0, octNum = 0, chordNum = 0;
        boolean bNextChord = true;
        ArrayList compNotes = new ArrayList();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;
        RelativeLayout chord = (RelativeLayout) this.findViewById(R.id.childLayout);

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

                singleNote.setTag("Note" + chordNum);
                singleNote.setOnClickListener(this);
                chord.addView(singleNote, noteLayoutParams);

                if (bNextChord == true) {
                    chord.setTag("Note" + chordNum);
                    chord.setOnClickListener(this);
                    parentLayout.addView(chord);
                    bNextChord = false;
                }
            }
            else
            {
                chordNum = i + 1;
                bNextChord = true;
            }
        }
    }

    @Override
    public void onClick (View v)
    {
        int startNote = 0;
        if (((String) v.getTag()).contains ("Note"))
        {
            startNote = Integer.parseInt (((String)v.getTag()).substring(4));
            this.getTheKPlayback().playComposition (mRecordedSong, startNote);
        }
    }
}
