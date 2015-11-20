package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;

import android.util.Log;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
    private long mStartTime;
    private long mEndTime;
    private Context context = this;

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
        final ListView listView = (ListView) findViewById(R.id.savedRecordingsList);
        listView.setAdapter(mAdapter);

//        try{
//            FileInputStream fileInput = openFileInput(FILENAME);
//            int c;
//            String temp="";
//
//            while( (c = fileInput.read()) != -1){
//                temp = temp + Character.toString((char)c);
//            }
//            //tv.setText(temp);
//
//            Toast.makeText(getBaseContext(),temp,Toast.LENGTH_LONG).show();
//        }
//        catch(Exception e){
//        }

        bIsPlaying = false;
        bIsRecording = false;

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onRecordButtonClick(view);
            }
        });

        playButton = (Button) findViewById(R.id.playbackButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onPlayButtonClick(view, playButton, mSavedFiles.get(mSavedFiles.size() - 1).getFileName());
            }
        });

        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onSaveButtonClick(view);
            }
        });

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onDeleteButtonClick(view, mSavedFiles.size() - 1);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                //RecordActivity.this.onRecordingListItemClick(view); // move functionality to this once working...
                view.setSelected(true);
                final SavedRecording listItem = (SavedRecording) listView.getItemAtPosition(position);

                final int index = position;

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.item_saved_recording_selected);

                final EditText editRecName = (EditText) dialog.findViewById(R.id.recNameEdit);
                editRecName.setText(listItem.getRecName());

                final Button playSelRec = (Button) dialog.findViewById(R.id.selPlaybackButton);

                playSelRec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecordActivity.this.onPlayButtonClick(view, playSelRec, listItem.getFileName());
                    }
                });

                final Button delSelRec = (Button) dialog.findViewById(R.id.selDeleteButton);

                delSelRec.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        RecordActivity.this.onDeleteButtonClick(view, index);
                        dialog.dismiss();
                    }
                });

                Button exitDialog = (Button) dialog.findViewById(R.id.selExitButton);

                exitDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Button saveExitDialog = (Button) dialog.findViewById(R.id.selSaveButton);

                saveExitDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listItem.setRecName(editRecName.getText().toString());
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
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

            mStartTime = System.currentTimeMillis();
            mRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_SHORT).show();
            recordButton.setText(R.string.record_rec_button_stop);
        }
        else
        {
            bIsRecording = false;
            mRecorder.stop();
            mEndTime = System.currentTimeMillis();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_SHORT).show();
            recordButton.setText(R.string.record_rec_button_rec);
            mSavedFiles.get(mSavedFiles.size() - 1).setLength(mEndTime - mStartTime);
        }
    }

    public void onPlayButtonClick(View view, Button playButton, String fileName) {

        if (!bIsPlaying)
        {
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(fileName);//mSavedFiles.get(mSavedFiles.size() - 1).getFileName()
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
        //mData = mSavedFiles.get(mSavedFiles.size() - 1).toString(); //ed1.getText().toString();

        try {
//            FileOutputStream fileOutput = openFileOutput(FILENAME, MODE_APPEND);
//            fileOutput.write(mData.getBytes());
//            fileOutput.close();
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(),"Recording Saved",Toast.LENGTH_SHORT).show();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void onDeleteButtonClick(View view, int index) {

        //mSavedFiles.remove(mSavedFiles.size() - 1);
        mSavedFiles.remove(index);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getBaseContext(),"Recording Deleted",Toast.LENGTH_SHORT).show();
    }

//    public void onRecordingListItemClick(View view) {
//        //view.setSelected(true);
//        Saved
//    }
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