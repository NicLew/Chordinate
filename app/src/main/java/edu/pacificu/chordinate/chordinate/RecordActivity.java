package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecordActivity extends AppCompatActivity {
    private static final int DFLT_INDEX = -1;
    private static final String SAVED_REC_EXT = ".sr";

    private Button mRecordButton;
    private Button mPlayButton;
    private Button mSaveButton;
    private Button mDeleteButton;
    private MicInput mInput;
    private Context mContext = this;
    private ArrayList<SavedRecording> mSavedFiles = new ArrayList<SavedRecording>();
    private SavedRecordingsAdapter mAdapter;
    private SavedRecording mCurrent;
    private boolean mbIsRecording;
    private ContextWrapper mContextWrapper = this;

    /**
     * Sets the content view and initializes the buttons and list view.
     *
     * @param savedInstanceState The instance state to create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mInput = new MicInput();
        mAdapter = new SavedRecordingsAdapter(this, mSavedFiles);

        mCurrent = null;
        mbIsRecording = false;

        initButtons();
        initListView();
        readFilesToArray();
    }

    /**
     * Calls the functions to reset the array with the saved files.
     */
    @Override
    protected void onStart () {
        super.onStart();
        readFilesToArray();
    }

    /**
     * Calls the functions to reset the array with the saved files.
     */
    @Override
    protected void onRestart () {
        super.onRestart();
        readFilesToArray();
    }

    /**
     * Calls the functions to reset the array with the saved files.
     */
    @Override
    protected void onResume () {
        super.onResume();
        readFilesToArray();
    }

    /**
     * Performs the proper actions when the record button is pressed. Calls the start recording
     * and stop recording methods of MicInput.
     */
    private void onRecordButtonClick() {

        if (!mbIsRecording) {
            mbIsRecording = true;
            mCurrent = new SavedRecording(mSavedFiles.size());
            mInput.startRecording(mCurrent);
            mRecordButton.setBackgroundResource(R.drawable.stop_button);
            mRecordButton.setText(R.string.record_rec_button_stop);
        } else {
            mbIsRecording = false;
            mInput.stopRecording(mCurrent);
            mRecordButton.setBackgroundResource(R.drawable.record_button);
            mRecordButton.setText(R.string.record_rec_button_rec);
            mRecordButton.setEnabled(false);
            mPlayButton.setEnabled(true);
            mSaveButton.setEnabled(true);
            mDeleteButton.setEnabled(true);
        }
    }

    /**
     * Performs the proper actions when a playback button is pressed. Calls the playback
     * function of MicInput.
     *
     * @param playButton The playback button that was pressed.
     * @param fileName   The name of the file to be played from.
     */
    private void onPlayButtonClick(Button playButton, String fileName) {

        mInput.playback(playButton, fileName);
    }

    /**
     * Saves the current recording to the array.
     */
    private void onSaveButtonClick() {
        mCurrent.writeItemToFile(mContextWrapper);

        mSavedFiles.add(mCurrent);
        mCurrent = null;
        mAdapter.notifyDataSetChanged();

        mRecordButton.setEnabled(true);
        mPlayButton.setEnabled(false);
        mSaveButton.setEnabled(false);
        mDeleteButton.setEnabled(false);
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

        if (null != dialog) {
            //File infoFile = new File(getFilesDir(), mSavedFiles.get(index).getFileNameBody() + SAVED_REC_EXT);
            File infoFile = new File(getFilesDir(), mSavedFiles.get(index).getFileName() + SAVED_REC_EXT);
            File audioFile = new File(mSavedFiles.get(index).getFileName());

            infoFile.delete();
            audioFile.delete();

            mSavedFiles.remove(index);
            mAdapter.notifyDataSetChanged();
            dialog.dismiss();
        } else {
            mCurrent = null;
            mRecordButton.setEnabled(true);
            mPlayButton.setEnabled(false);
            mSaveButton.setEnabled(false);
            mDeleteButton.setEnabled(false);
        }
    }

    /**
     * Reads in the saved files to the saved recordings array.
     */
    private void readFilesToArray() {

        if (0 == mSavedFiles.size()) {
            String fileName, fileNameBody, recName, dateStr, lengthStr;

            String[] files;
            files = this.getFilesDir().list();

            try {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith(SAVED_REC_EXT)) {
                        InputStreamReader fInput = new InputStreamReader(openFileInput(files[i]));
                        BufferedReader buffReader = new BufferedReader(fInput);

                        recName = buffReader.readLine();
                        dateStr = buffReader.readLine();
                        lengthStr = buffReader.readLine();
                        fileName = buffReader.readLine();
                        fileNameBody = buffReader.readLine();

                        mSavedFiles.add(new SavedRecording(fileName, fileNameBody, recName, dateStr, lengthStr));
                    }
                }
            } catch (Exception e) {
                Log.d("readFilesToArray()", e.toString());
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Initializes a playback button.
     *
     * @param playButton The playback button to be initialized.
     * @param fileName   The file to be played from.
     */
    private void initPlayButton(final Button playButton, final String fileName) {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null != fileName) {
                    RecordActivity.this.onPlayButtonClick(playButton, fileName);
                } else {
                    RecordActivity.this.onPlayButtonClick(playButton, mCurrent.getFileName());
                }
            }
        });
    }

    /**
     * Initializes a delete button.
     *
     * @param deleteButton The button to be initialized.
     * @param dialog       The dialog to be used. Will be null if not applicable.
     * @param index        The position of the recording in the list of recordings. Will be
     *                     set to DFLT_INDEX if not applicable.
     */
    private void initDeleteButton(Button deleteButton, final Dialog dialog, final int index) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index > DFLT_INDEX) {
                    RecordActivity.this.onDeleteButtonClick(dialog, index);
                } else {
                    RecordActivity.this.onDeleteButtonClick(dialog, mSavedFiles.size() - 1);
                }
            }
        });
    }

    /**
     * Initializes the record button for the main Record Activity page.
     */
    private void initRecordButton() {
        mRecordButton = (Button) findViewById(R.id.recordButton);
        mRecordButton.setEnabled(true);

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
    private void initSaveButton() {
        mSaveButton = (Button) findViewById(R.id.saveButton);
        mSaveButton.setEnabled(false);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecordActivity.this.onSaveButtonClick();
            }
        });
    }

    /**
     * Initializes the buttons for the main Record Activity page.
     */
    private void initButtons() {
        initRecordButton();

        mPlayButton = (Button) findViewById(R.id.playbackButton);
        mPlayButton.setEnabled(false);
        initPlayButton(mPlayButton, null);

        initSaveButton();

        mDeleteButton = (Button) findViewById(R.id.deleteButton);
        mDeleteButton.setEnabled(false);
        initDeleteButton(mDeleteButton, null, DFLT_INDEX);
    }

    /**
     * Initializes the exit without saving button in the edit recording dialog view.
     *
     * @param dialog The dialog to be used.
     */
    private void initExitButton(final Dialog dialog) {
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
     * @param dialog      The dialog to be used.
     * @param listItem    The list item the dialog is operating upon.
     * @param editRecName The edit text field to be saved.
     */
    private void initSaveExitButton(final Dialog dialog, final SavedRecording listItem,
                                    final EditText editRecName) {
        Button saveExitDialog = (Button) dialog.findViewById(R.id.selSaveButton);

        saveExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //listItem.setRecName(editRecName.getText().toString());
                listItem.setName(editRecName.getText().toString());
                mAdapter.notifyDataSetChanged();
                listItem.writeItemToFile(mContextWrapper);
                dialog.dismiss();
            }
        });
    }

    /**
     * Initializes the ListView for the list of saved recordings and specifies what should
     * happen when an item from that list is selected.
     */
    private void initListView() {
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
                //editRecName.setText(listItem.getRecName());
                editRecName.setText(listItem.getName());

                final Button playSelRec = (Button) dialog.findViewById(R.id.selPlaybackButton);
                initPlayButton(playSelRec, listItem.getFileName());

                final Button delSelRec = (Button) dialog.findViewById(R.id.selDeleteButton);
                initDeleteButton(delSelRec, dialog, position);

                initExitButton(dialog);
                initSaveExitButton(dialog, listItem, editRecName);

                dialog.show();
            }
        });
    }
}