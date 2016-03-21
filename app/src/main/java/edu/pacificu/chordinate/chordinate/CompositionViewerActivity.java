package edu.pacificu.chordinate.chordinate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.lang.Math;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        ArrayList compNotes = new ArrayList();
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.parentLayout);
        LayoutInflater layoutInflater = getLayoutInflater();
        View view;

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
                view = layoutInflater.inflate(R.layout.comp_view_item, parentLayout, false);
                RelativeLayout child = (RelativeLayout) view.findViewById(R.id.childLayout);
                Button button = new Button(child.getContext());
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                        (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                layoutParams.width = 20;
                layoutParams.height = 20;
                child.setPadding(0, 0, 0, 48 + Math.abs(18 *(int) compNotes.get(i)));
                if ((int) compNotes.get(i) < 0)
                {
                    //button.setText("S");
                    button.setBackgroundResource(R.drawable.note_s);
                }
                else {
                    //button.setText("N");
                    button.setBackgroundResource(R.drawable.note);
                }
                child.setTag("Note" + chordNum);
                button.setTag("Note" + chordNum);
                child.setOnClickListener(this);
                button.setOnClickListener(this);
                child.addView(button, layoutParams);
                parentLayout.addView(child);
            }
            else
            {
                chordNum = i + 1;
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
