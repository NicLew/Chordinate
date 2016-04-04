package edu.pacificu.chordinate.chordinate;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;

import java.util.ArrayList;

/**
 * Created by lund3481 on 11/22/2015.
 */
public class KeyPlayback {
    public SoundPool KeySoundPool; // Stores sounds to be played
    SoundPool.Builder KeySPBuilder;
    public static int[] SoundID = new int[84]; // Soundpool ids
    public static int[] SoundFileID = {R.raw.c1, R.raw.cs1, R.raw.d1, R.raw.ds1, R.raw.e1, R.raw.f1,
            R.raw.fs1, R.raw.g1, R.raw.gs1, R.raw.a1, R.raw.as1, R.raw.b1, R.raw.c2, R.raw.cs2,
            R.raw.d2, R.raw.ds2, R.raw.e2, R.raw.f2, R.raw.fs2, R.raw.g2, R.raw.gs2, R.raw.a2,
            R.raw.as2, R.raw.b2, R.raw.c3, R.raw.cs3, R.raw.d3, R.raw.ds3, R.raw.e3, R.raw.f3,
            R.raw.fs3, R.raw.g3, R.raw.gs3, R.raw.a3, R.raw.as3, R.raw.b3, R.raw.c4, R.raw.cs4,
            R.raw.d4, R.raw.ds4, R.raw.e4, R.raw.f4, R.raw.fs4, R.raw.g4, R.raw.gs4, R.raw.a4,
            R.raw.as4, R.raw.b4, R.raw.c5, R.raw.cs5, R.raw.d5, R.raw.ds5, R.raw.e5, R.raw.f5,
            R.raw.fs5, R.raw.g5, R.raw.gs5, R.raw.a5, R.raw.as5, R.raw.b5, R.raw.c6, R.raw.cs6,
            R.raw.d6, R.raw.ds6, R.raw.e6, R.raw.f6, R.raw.fs6, R.raw.g6, R.raw.gs6, R.raw.a6,
            R.raw.as6, R.raw.b6, R.raw.c7, R.raw.cs7, R.raw.d7, R.raw.ds7, R.raw.e7, R.raw.f7,
            R.raw.fs7, R.raw.g7, R.raw.gs7, R.raw.a7, R.raw.as7, R.raw.b7};
    private static boolean isLoaded;
    private int soundIndex;
    private static int playbackSpeed = 600;
    private ArrayList compNotes = new ArrayList();
    private Handler handler = new Handler ();

    KeyPlayback(){
        KeySPBuilder = new SoundPool.Builder();
        KeySPBuilder.setMaxStreams(4);
        KeySoundPool = KeySPBuilder.build();


    }

    public void loadSounds (Context context){
        for (int i = 0; i < SoundFileID.length; i ++)
        {
            // Loads sounds
            SoundID[i] = KeySoundPool.load (context,SoundFileID[i], 1);
        }
    }
    // TODO: Add ability to stop loop upon closing program, Done?
    private Runnable playLoop = new Runnable()
    {
        public void run()
        {
            int note = -1, chordIndex = 0;
            ArrayList chord = new ArrayList();

            if (soundIndex < compNotes.size())
            {
                note = (int) compNotes.get(soundIndex);

                while (note > -1)
                {
                    chord.add(note);
                    soundIndex ++;
                    note = (int) compNotes.get(soundIndex);
                }
                soundIndex ++;

                for (chordIndex = 0; chordIndex < chord.size(); chordIndex ++) {
                    KeySoundPool.play(SoundID[(int)chord.get(chordIndex)], 1, 1, 0, 0, 1);
                }
                handler.postDelayed(this, playbackSpeed);
            }
        }
    };

    public void play (int id, int lVolume, int rVolume, int priority, int loop, float rate)
    {
        KeySoundPool.play(id, lVolume, rVolume, priority, loop, rate);
    }

    public void stopPlayback ()
    {
        handler.removeCallbacks(playLoop);
    }

    public void playComposition (String comp, int startNote)
    {
        char current;
        int /*index = 0,*/ keyNum = 0, octNum = 0;
        int index = startNote;
        soundIndex = 0;
        compNotes.clear();

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
                        compNotes.add(-1);
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
                    compNotes.add(keyNum + (octNum * 12));
                }

                index ++;
            } while (current != '$');

            playLoop.run();
        }
    }
}
