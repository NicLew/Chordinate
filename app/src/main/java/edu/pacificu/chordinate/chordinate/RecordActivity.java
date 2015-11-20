package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class RecordActivity extends AppCompatActivity {
    private Button mRecordButton;
    private Button mPlayButton;
    private MicInput mInput;
    private Context mContext = this;
    private ArrayList<SavedRecording> mSavedFiles = new ArrayList<SavedRecording>();
    private SavedRecordingsAdapter mAdapter;


    //private File mSavedRecFile = new File("saved_rec_file");
    //private String FILENAME = "saved_rec_file";
    //private String mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mInput = new MicInput();
        mAdapter = new SavedRecordingsAdapter(this, mSavedFiles);

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

        initButtons();
        initListView();
    }

    private void onRecordButtonClick() {

        mInput.record(mSavedFiles, mRecordButton);
    }

    private void onPlayButtonClick(Button playButton, String fileName) {

        mInput.playback(playButton, fileName);
    }

    private void onSaveButtonClick() {
        //mData = mSavedFiles.get(mSavedFiles.size() - 1).toString(); //ed1.getText().toString();

        try {
//            FileOutputStream fileOutput = openFileOutput(FILENAME, MODE_APPEND);
//            fileOutput.write(mData.getBytes());
//            fileOutput.close();
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getBaseContext(),"Recording Saved",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void onDeleteButtonClick(Dialog dialog, int index) {

        mSavedFiles.remove(index);
        mAdapter.notifyDataSetChanged();

        if (null != dialog) {
            dialog.dismiss();
        }
    }

    private void initPlayButton (final Button playButton, final String fileName) {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != fileName) {
                    RecordActivity.this.onPlayButtonClick(playButton, fileName);
                } else {
                    RecordActivity.this.onPlayButtonClick(playButton, mSavedFiles.get(mSavedFiles.size() - 1).getFileName());
                }
            }
        });
    }

    private void initDeleteButton (Button deleteButton, final Dialog dialog, final int index) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index >= 0) {
                    RecordActivity.this.onDeleteButtonClick(dialog, index);
                }
                else
                {
                    RecordActivity.this.onDeleteButtonClick(dialog, mSavedFiles.size() - 1);
                }
            }
        });
    }

    private void initRecordButton () {
        mRecordButton = (Button) findViewById(R.id.recordButton);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onRecordButtonClick();
            }
        });
    }

    private void initSaveButton () {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onSaveButtonClick();
            }
        });
    }

    private void initButtons () {
        initRecordButton();

        mPlayButton = (Button) findViewById(R.id.playbackButton);
        initPlayButton(mPlayButton, null);

        initSaveButton();

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        initDeleteButton(deleteButton, null, -1);
    }

    private void initExitButton (final Dialog dialog) {
        Button exitDialog = (Button) dialog.findViewById(R.id.selExitButton);
        exitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void initSaveExitButton (final Dialog dialog, final SavedRecording listItem,
                                     final EditText editRecName) {
        Button saveExitDialog = (Button) dialog.findViewById(R.id.selSaveButton);
        saveExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItem.setRecName(editRecName.getText().toString());
                mAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    private void initListView () {
        final ListView listView = (ListView) findViewById(R.id.savedRecordingsList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long unused) {
                view.setSelected(true);

                final SavedRecording listItem;
                listItem = (SavedRecording) listView.getItemAtPosition(position);

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.item_saved_recording_selected);

                final EditText editRecName = (EditText) dialog.findViewById(R.id.recNameEdit);
                editRecName.setText(listItem.getRecName());

                final Button playSelRec = (Button) dialog.findViewById(R.id.selPlaybackButton);
                initPlayButton(playSelRec, listItem.getFileName());

                final Button delSelRec = (Button) dialog.findViewById(R.id.selDeleteButton);
                initDeleteButton(delSelRec, dialog, position);

                initExitButton(dialog);
                initSaveExitButton (dialog, listItem, editRecName);

                dialog.show();
            }
        });
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