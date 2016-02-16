package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by lund3481 on 2/13/2016.
 */
public class ChordinateActivity extends AppCompatActivity {
    private static KeyPlayback theKPlayback = new KeyPlayback();
    public static KeyPlayback getTheKPlayback()
    {
        return theKPlayback;
    }

    public static void setTheKPlayback(KeyPlayback newKP)
    {
        theKPlayback = newKP;
    }
}
