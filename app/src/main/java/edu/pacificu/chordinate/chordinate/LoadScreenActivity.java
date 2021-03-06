package edu.pacificu.chordinate.chordinate;

import android.content.Intent;
import android.os.Bundle;

public class LoadScreenActivity extends ChordinateActivity {
    /**
     * Loads the Keyboard SoundPool before starting up the MainMenuActivity
     *
     * @param   savedInstanceState  The instance state to be created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        KeyPlayback newKP = new KeyPlayback();
        newKP.loadSounds(this);
        ChordinateActivity.setTheKPlayback(newKP);

        Intent myIntent = new Intent(LoadScreenActivity.this, MainMenuActivity.class);
        startActivity(myIntent);
    }
}
