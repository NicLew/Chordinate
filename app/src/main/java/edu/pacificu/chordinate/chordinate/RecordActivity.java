package edu.pacificu.chordinate.chordinate;

import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.media.MediaPlayer;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class RecordActivity extends AppCompatActivity {
    private Button recordButton;
    private Button playButton;
    private Button saveButton;
    private Button deleteButton;
    private boolean bIsRecording;
    private MediaRecorder mRecorder = null;

    private MediaPlayer mPlayer = null;
    private boolean bIsPlaying;

    private ArrayList<SavedRecording> mSavedFiles = new ArrayList<SavedRecording>();
    SavedRecordingsAdapter mAdapter;


    //private File mSavedRecFile = new File("saved_rec_file");
    private String FILENAME = "saved_rec_file";
    private String mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mAdapter = new SavedRecordingsAdapter(this, mSavedFiles);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.savedRecordingsList);
        listView.setAdapter(mAdapter);

//        FileInputStream fin = openFileInput(FILENAME);
//        //fin.read();
//        fin.close();

        bIsPlaying = false;
        bIsRecording = false;

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onRecordButtonClick(v);
            }
        });

        playButton = (Button) findViewById(R.id.playbackButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onPlayButtonClick(v);
            }
        });

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onSaveButtonClick(v);
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onDeleteButtonClick(v);
            }
        });
    }

    public void onRecordButtonClick(View view) { // change to use java class methods!!!!!

        if (!bIsRecording)
        {
            mSavedFiles.add(new SavedRecording(mSavedFiles.size()));

            bIsRecording = true;

            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mSavedFiles.get(mSavedFiles.size() - 1).getFileName());
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }

            mRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_SHORT).show();
            recordButton.setText(R.string.record_rec_button_stop);
        }
        else
        {
            bIsRecording = false;
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_SHORT).show();
            recordButton.setText(R.string.record_rec_button_rec);
        }
    }

    public void onPlayButtonClick(View view) {

        if (!bIsPlaying)
        {
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(mSavedFiles.get(mSavedFiles.size() - 1).getFileName());
                mPlayer.prepare();
                mPlayer.start();
                bIsPlaying = true;
                Toast.makeText(getApplicationContext(), "Playback started.", Toast.LENGTH_SHORT).show();
                playButton.setText(R.string.record_playback_button_stop);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        }
        else
        {
            bIsPlaying = false;
            mPlayer.release();
            mPlayer = null;
            Toast.makeText(getApplicationContext(), "Playback stopped.", Toast.LENGTH_SHORT).show();
            playButton.setText(R.string.record_playback_button_play);
        }
    }

    public void onSaveButtonClick(View view) {
        mData = mSavedFiles.toString(); //ed1.getText().toString();

        try {
            FileOutputStream fileOutput = openFileOutput(FILENAME, MODE_APPEND);
            fileOutput.write(mData.getBytes());
            fileOutput.close();
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(),"Recording Saved",Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onDeleteButtonClick(View view) {

        mSavedFiles.remove(mSavedFiles.size() - 1);
        Toast.makeText(getBaseContext(),"Recording Deleted",Toast.LENGTH_SHORT).show();
    }
//
//    protected void onPause() {
//        super.onPause();
//
//        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//        //fos.write(mSavedFiles.toString().getBytes());
//        fos.close();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();  // Always call the superclass method first
//
//        String FILENAME = "hello_file";
//        String string = "hello world!";
//
//        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
//        fos.write(string.getBytes());
//        fos.close();
//
//        // Save the note's current draft, because the activity is stopping
//        // and we want to be sure the current note progress isn't lost.
//       // ContentValues values = new ContentValues();
//        //values.put("NumSavedFiles", mSavedFiles.size());
//        //values.put(NotePad.Notes.COLUMN_NAME_TITLE, getCurrentNoteTitle());
//
//
////        getContentResolver().update(
////                mUri,    // The URI for the note to update.
////                values,  // The map of column names and new values to apply to them.
////                null,    // No SELECT criteria are used.
////                null     // No WHERE columns are used.
////        );
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();  // Always call the superclass method first
//
//        // The activity is either being restarted or started for the first time
//        // so this is where we should make sure that GPS is enabled
////            LocationManager locationManager =
////                    (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////            boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
////
////            if (!gpsEnabled) {
////                // Create a dialog here that requests the user to enable GPS, and use an intent
////                // with the android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS action
////                // to take the user to the Settings screen to enable GPS when they click "OK"
////            }
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();  // Always call the superclass method first
//
//        // Activity being restarted from stopped state
//    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//        // Save UI state changes to the savedInstanceState.
//        // This bundle will be passed to onCreate if the process is
//        // killed and restarted.
//        savedInstanceState.putBoolean("MyBoolean", true);
//        savedInstanceState.putDouble("myDouble", 1.9);
//        savedInstanceState.putInt("MyInt", 1);
//        savedInstanceState.putString("MyString", "Welcome back to Android");
//        // etc.
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        // Restore UI state from the savedInstanceState.
//        // This bundle has also been passed to onCreate.
//        boolean myBoolean = savedInstanceState.getBoolean("MyBoolean");
//        double myDouble = savedInstanceState.getDouble("myDouble");
//        int myInt = savedInstanceState.getInt("MyInt");
//        String myString = savedInstanceState.getString("MyString");
//    }

}