package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;

public class ChordinateActivity extends AppCompatActivity {
    private static KeyPlayback theKPlayback = new KeyPlayback();

    /**
     * Returns the shared KeyPlayback object
     *
     * @return the shared KeyPlayback object
     */
    public static KeyPlayback getTheKPlayback()
    {
        return theKPlayback;
    }

    /**
     * Sets the shared KeyPlayback object
     *
     * @param newKP the KeyPlayback object to be set to
     */
    public static void setTheKPlayback(KeyPlayback newKP)
    {
        theKPlayback = newKP;
    }
}
