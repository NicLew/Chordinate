package edu.pacificu.chordinate.chordinate;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
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
import java.util.Collections;

public class CompositionsActivity extends ChordinateActivity {
    private static final String SAVED_COMP_EXT = ".chd";

    private Context mContext = this;
    private ContextWrapper mContextWrapper = this;
    private CompositionsAdapter mAdapter;
    private ArrayList<SavedComposition> mSavedFiles = new ArrayList<SavedComposition>();
    private boolean mbDoReload;
    private int mCurrentIndex;

    /**
     * Creates the page, initializes the list view, reads in the saved files to an array,
     * and loads the sound pool needed for playback.
     *
     * @param savedInstanceState    The instance state to be created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);

        mAdapter = new CompositionsAdapter(this, mSavedFiles);
        initListView();
        readFilesToArray();
        mbDoReload = false;
    }

    /**
     * When returning to this activity from editing, reload the current composition from memory.
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (mbDoReload) {
            String fileName, compName, notesStr, dateStr;

            try {
                InputStreamReader fInput =
                        new InputStreamReader(openFileInput(mSavedFiles.get(mCurrentIndex).getFileName() + SAVED_COMP_EXT));
                BufferedReader buffReader = new BufferedReader(fInput);

                compName = buffReader.readLine();
                dateStr = buffReader.readLine();
                notesStr = buffReader.readLine();
                fileName = buffReader.readLine();

                mSavedFiles.set(mCurrentIndex, new SavedComposition(compName, dateStr, notesStr, fileName));

            } catch (Exception e) {
                Log.d("onResume()", e.toString());
            }

            mAdapter.notifyDataSetChanged();
            mbDoReload = false;
        }
    }

    /**
     * Reads in the saved files to the saved compositions array.
     */
    private void readFilesToArray() {

        if (0 == mSavedFiles.size()) {
            String fileName, compName, notesStr, dateStr;

            String[] files;
            files = this.getFilesDir().list();

            try {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].endsWith(SAVED_COMP_EXT)) {
                        InputStreamReader fInput = new InputStreamReader(openFileInput(files[i]));
                        BufferedReader buffReader = new BufferedReader(fInput);

                        compName = buffReader.readLine();
                        dateStr = buffReader.readLine();
                        notesStr = buffReader.readLine();
                        fileName = buffReader.readLine();

                        mSavedFiles.add(new SavedComposition(compName, dateStr, notesStr, fileName));
                    }

                    Collections.sort(mSavedFiles, SavedFile.Comparators.DATE);
                }
            } catch (Exception e) {
                Log.d("readFilesToArray()", e.toString());
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Initializes the ListView for the list of saved compositions and specifies what should
     * happen when an item from that list is selected.
     */
    private void initListView() {
        final ListView listView = (ListView) findViewById(R.id.savedCompList);
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

                final SavedComposition listItem;
                listItem = (SavedComposition) listView.getItemAtPosition(position);

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.item_saved_comp_selected);

                final EditText editCompName = (EditText) dialog.findViewById(R.id.compNameEdit);
                editCompName.setText(listItem.getName());

                final Button playSelRec = (Button) dialog.findViewById(R.id.selPlaybackButton);
                initPlayButton(playSelRec, position);

                final Button delSelRec = (Button) dialog.findViewById(R.id.selDeleteButton);
                initDeleteButton(delSelRec, dialog, position);

                initExitButton(dialog);
                initSaveExitButton(dialog, listItem, editCompName);

                dialog.show();
            }
        });
    }

    /**
     * Initializes a playback button.
     *
     * @param playButton The playback button to be initialized.
     * @param position   The position of the list item to be played.
     */
    private void initPlayButton(final Button playButton, final int position) {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompositionsActivity.this.onPlayButtonClick(position);
            }
        });
    }

    /**
     * Performs the proper actions when a playback button is pressed. Calls the playback
     * function of KeyPlayback.
     *
     * @param position the position of the list item to be played back
     */
    private void onPlayButtonClick(int position) {
        SavedComposition listItem = mSavedFiles.get(position);
        mbDoReload = true;
        mCurrentIndex = position;

        startCompReviewActivity(listItem.getName(), listItem.getWholeDateStr(), listItem.getNotes(),
                listItem.getFileName(), true);
    }

    /**
     * Initializes a delete button.
     *
     * @param deleteButton The button to be initialized.
     * @param dialog       The dialog to be used.
     * @param index        The position of the item in the list of compositions.
     */
    private void initDeleteButton(Button deleteButton, final Dialog dialog, final int index) {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompositionsActivity.this.onDeleteButtonClick(dialog, index);
            }
        });
    }

    /**
     * Performs the proper actions when a delete button is pressed. Removes the composition,
     * notifies the list view adaptor that there has been a change, and the dialog is closed.
     *
     * @param dialog The dialog to be closed.
     * @param index  The position of the item in the saved compositions list to be deleted.
     */
    private void onDeleteButtonClick(Dialog dialog, int index) {

        File infoFile = new File(getFilesDir(),
                mSavedFiles.get(index).getFileName() + SAVED_COMP_EXT);

        infoFile.delete();
        mSavedFiles.remove(index);
        mAdapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    /**
     * Initializes the exit without saving button in the edit composition dialog view.
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
     * Initializes the save and exit button in the edit composition dialog view.
     *
     * @param dialog        The dialog to be used.
     * @param listItem      The list item the dialog is operating upon.
     * @param editCompName  The edit text field to be saved.
     */
    private void initSaveExitButton(final Dialog dialog, final SavedComposition listItem,
                                    final EditText editCompName) {
        Button saveExitDialog = (Button) dialog.findViewById(R.id.selSaveButton);

        saveExitDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItem.setName(editCompName.getText().toString());
                mAdapter.notifyDataSetChanged();
                listItem.writeItemToFile(mContextWrapper);
                dialog.dismiss();
            }
        });
    }

    /**
     * Bundles up necessary variables to pass to the composition review activity and starts that
     * activity.
     *
     * @param compName the name of the composition to be viewed
     * @param dateStr the date of the composition to be viewed
     * @param recordedSong the notes string of the composition to be viewed
     * @param fileName the file name of the composition to be viewed
     * @param bEnableEditMode whether or not edit mode should be enabled in the next activity
     */
    private void startCompReviewActivity(String compName, String dateStr, String recordedSong,
                                         String fileName, boolean bEnableEditMode) {
        Bundle compBundle = new Bundle();
        compBundle.putString("compName", compName);
        compBundle.putString("dateStr", dateStr);
        compBundle.putString("recordedSong", recordedSong);
        compBundle.putString("fileName", fileName);
        compBundle.putBoolean("enableEditMode", bEnableEditMode);

        Intent reviewCompIntent = new Intent(CompositionsActivity.this,
                CompositionViewerActivity.class);
        reviewCompIntent.putExtras(compBundle);
        startActivity(reviewCompIntent);
    }
}
