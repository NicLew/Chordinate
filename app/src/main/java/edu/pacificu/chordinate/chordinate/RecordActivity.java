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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    private static final int DFLT_INDEX = -1;

    private Button mRecordButton;
    private Button mPlayButton;
    private MicInput mInput;
    private Context mContext = this;
    private ArrayList<SavedRecording> mSavedFiles = new ArrayList<SavedRecording>();
    private SavedRecordingsAdapter mAdapter;


    //private File mSavedRecFile = new File("saved_rec_file");
    //private String FILENAME = "saved_rec_file";
    //private String mData;

    /**
     * Sets the content view and initializes the buttons and list view.
     *
     * @param savedInstanceState    The instance state to create.
     */
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

    /**
     * Performs the proper actions when the record button is pressed. Calls the record
     * function of MicInput.
     */
    private void onRecordButtonClick() {

        mInput.record(mSavedFiles, mRecordButton);
    }

    /**
     * Performs the proper actions when a playback button is pressed. Calls the playback
     * function of MicInput.
     *
     * @param playButton    The playback button that was pressed.
     * @param fileName      The name of the file to be played from.
     */
    private void onPlayButtonClick(Button playButton, String fileName) {

        mInput.playback(playButton, fileName);
    }

    /**
     * TODO: Only add a recording to the array if the user presses "save"
     * TODO Doc onSaveButtonClick
     */
    private void onSaveButtonClick() {
        //mData = mSavedFiles.get(mSavedFiles.size() - 1).toString(); //ed1.getText().toString();

        mAdapter.notifyDataSetChanged();
    /*    try {
            FileOutputStream fileOutput = openFileOutput(FILENAME, MODE_APPEND);
            fileOutput.write(mData.getBytes());
            fileOutput.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    /**
     * Performs the proper actions when a delete button is pressed. Removes the recording,
     * notifies the list view adaptor that there has been a change, and, if dialog is not null,
     * the dialog is closed.
     *
     * @param dialog The dialog to be closed. Will be null if not applicable.
     * @param index  The position of the recording in the saved recording list to be deleted.
     */
    private void onDeleteButtonClick(Dialog dialog, int index) {

        mSavedFiles.remove(index);
        mAdapter.notifyDataSetChanged();

        if (null != dialog) {
            dialog.dismiss();
        }
    }

    /**
     * Initializes a playback button.
     *
     * @param playButton    The playback button to be initialized.
     * @param fileName      The file to be played from.
     */
    private void initPlayButton (final Button playButton, final String fileName) {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != fileName) {
                    RecordActivity.this.onPlayButtonClick(playButton, fileName);
                } else {
                    RecordActivity.this.onPlayButtonClick(playButton,
                            mSavedFiles.get(mSavedFiles.size() - 1).getFileName());
                }
            }
        });
    }

    /**
     * Initializes a delete button.
     *
     * @param deleteButton  The button to be initialized.
     * @param dialog        The dialog to be used. Will be null if not applicable.
     * @param index         The position of the recording in the list of recordings. Will be
     *                      set to DFLT_INDEX if not applicable.
     */
    private void initDeleteButton (Button deleteButton, final Dialog dialog, final int index) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index > DFLT_INDEX) {
                    RecordActivity.this.onDeleteButtonClick(dialog, index);
                }
                else
                {
                    RecordActivity.this.onDeleteButtonClick(dialog, mSavedFiles.size() - 1);
                }
            }
        });
    }

    /**
     * Initializes the record button for the main Record Activity page.
     */
    private void initRecordButton () {
        mRecordButton = (Button) findViewById(R.id.recordButton);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onRecordButtonClick();
            }
        });
    }

    /**
     * Initializes the save recording button for the main Record Activity page.
     */
    private void initSaveButton () {
        Button saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onSaveButtonClick();
            }
        });
    }

    /**
     * Initializes the buttons for the main Record Activity page.
     */
    private void initButtons () {
        initRecordButton();

        mPlayButton = (Button) findViewById(R.id.playbackButton);
        initPlayButton(mPlayButton, null);

        initSaveButton();

        Button deleteButton = (Button) findViewById(R.id.deleteButton);
        initDeleteButton(deleteButton, null, DFLT_INDEX);
    }

    /**
     * Initializes the exit without saving button in the edit recording dialog view.
     *
     * @param dialog    The dialog to be used.
     */
    private void initExitButton (final Dialog dialog) {
        Button exitDialog = (Button) dialog.findViewById(R.id.selExitButton);
        exitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * Initializes the save and exit button in the edit recording dialog view.
     *
     * @param dialog        The dialog to be used.
     * @param listItem      The list item the dialog is operating upon.
     * @param editRecName   The edit text field to be saved.
     */
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

    /**
     * Initializes the ListView for the list of saved recordings and specifies what should
     * happen when an item from that list is selected.
     */
    private void initListView () {
        final ListView listView = (ListView) findViewById(R.id.savedRecordingsList);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /**
             * Specifies what should happen when an item from the list is selected.
             *
             * @param parent    Unused, to match parent class signature.
             * @param view      The view to be used.
             * @param position  The position in the list of the selected item.
             * @param unused    An unused argument parameter, to match parent class signature.
             */
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