package edu.pacificu.chordinate.chordinate;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
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
    private static final int NUM_OF_NOTE_POS = 48;
    private static final int NUM_OF_LINE_POS = 24;
    private static final int END_CHORD = -1000;
    private static final int NOTE_HEIGHT = 20;
    private static final int NOTE_WIDTH = 20;
    private static final int SHARP_HEIGHT = 40;
    private static final int SHARP_WIDTH = 30;
    private static final int NOTE_LEFT_MARGIN = 10;
    private static final int SHARP_LEFT_MARGIN = 30;
    private static final int NOTE_TOP_MARGIN_OFFSET = 90;
    private static final int SHARP_TOP_MARGIN_OFFSET = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition_viewer);
        Intent reviewIntent = getIntent();
        Bundle extras = reviewIntent.getExtras();
        mRecordedSong = extras.getString("recordedSong");
        char current;
        int index = 0, keyNum = 0, octNum = 0, chordNum = 0, noteGap = 0, lineGap = 0;
        boolean bNextChord = true;
        ArrayList compNotes = new ArrayList();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;
        RelativeLayout chord = (RelativeLayout) this.findViewById(R.id.childLayout);
        Point screenSize = new Point();
        Display display = getWindowManager().getDefaultDisplay();
        display.getSize(screenSize);
        noteGap = (screenSize.y - NOTE_HEIGHT) / NUM_OF_NOTE_POS;
        lineGap = (screenSize.y - NOTE_HEIGHT) / NUM_OF_LINE_POS;

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
                        compNotes.add(END_CHORD);
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
            if ((int) compNotes.get(i) != END_CHORD) { // TODO: Magic constant

                if (bNextChord == true) {
                    view = layoutInflater.inflate(R.layout.comp_view_item, parentLayout, false);
                    chord = (RelativeLayout) view.findViewById(R.id.childLayout);

                    for (int j = 0; j < NUM_OF_LINE_POS; j ++)
                    {
                        RelativeLayout.LayoutParams lineLayoutParams = new RelativeLayout.LayoutParams
                                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        ImageView singleLine = new ImageView(chord.getContext());
                        lineLayoutParams.setMargins(0,(lineGap * j) + 7, 0, 0);
                        if (j != (NUM_OF_LINE_POS / 2)) {
                            singleLine.setBackgroundResource(R.drawable.music_staff_line);
                        }
                        else
                        {
                            singleLine.setBackgroundResource(R.drawable.music_staff_line_mid_c);
                        }
                        chord.addView(singleLine, lineLayoutParams);
                    }
                }
                ImageView singleNote = new ImageView(chord.getContext());
                RelativeLayout.LayoutParams noteLayoutParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                noteLayoutParams.width = NOTE_WIDTH;
                noteLayoutParams.height = NOTE_HEIGHT;
                noteLayoutParams.setMargins(NOTE_LEFT_MARGIN, (screenSize.y - NOTE_TOP_MARGIN_OFFSET) -
                        (noteGap * (Math.abs((int) compNotes.get(i)) - 1)), 0, 0);
                if ((int) compNotes.get(i) < 0)
                {
                    ImageView sharp = new ImageView(chord.getContext());
                    RelativeLayout.LayoutParams sharpLayoutParams = new RelativeLayout.LayoutParams
                            (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    sharpLayoutParams.width = SHARP_WIDTH;
                    sharpLayoutParams.height = SHARP_HEIGHT;
                    sharpLayoutParams.setMargins(SHARP_LEFT_MARGIN, (screenSize.y - SHARP_TOP_MARGIN_OFFSET) -
                            (noteGap * (Math.abs((int) compNotes.get(i)) - 1)), 0, 0);
                    sharp.setTag("Note" + chordNum);
                    sharp.setOnClickListener(this);
                    sharp.setBackgroundResource(R.drawable.sharp);
                    chord.addView(sharp, sharpLayoutParams);
                    singleNote.setBackgroundResource(R.drawable.note_s);

                }
                else {
                    singleNote.setBackgroundResource(R.drawable.note);
                }

                singleNote.setTag("Note" + chordNum);
                singleNote.setOnClickListener(this);
                chord.addView(singleNote, noteLayoutParams);

                if (bNextChord == true) {
                    chord.setTag("Chord" + chordNum);
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
    protected void onPause()
    {
        super.onPause();

        this.getTheKPlayback().stopPlayback();
    }

    @Override
    protected void onStop()
    {
        super.onStop();

        this.getTheKPlayback().stopPlayback();
    }

    @Override
    public void onClick (View v)
    {
        int startNote = 0;
        if (((String) v.getTag()).contains ("Chord"))
        {
            this.getTheKPlayback().stopPlayback();
            startNote = Integer.parseInt (((String)v.getTag()).substring(5));
            this.getTheKPlayback().playComposition (mRecordedSong, startNote);
        }
    }
}
