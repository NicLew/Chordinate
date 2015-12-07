package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lewe4441 on 12/1/2015.
 */
public class SavedComposition {

    private String mFileName; // to store name of saved file in internal memory
    private String mCompName;
    private String mNotesStr;
    private Date mDate;
    /*private Date mLength;*/

    /**
     * Saved recording constructor. Determines filename and recording name and date.
     *
     * @param fileNum   The file number to assign.
     */
    SavedComposition (int fileNum) {
        mFileName = "comp_file_" + Integer.toString(fileNum);
        mCompName = "Composition #" + Integer.toString(fileNum);
        mNotesStr = "";
        mDate = new Date ();
    }

    SavedComposition (int fileNum, String compName, String notes) {
        mFileName = "comp_file_" + Integer.toString(fileNum);
        mCompName = compName;
        mNotesStr = notes;
        mDate = new Date ();
    }

    SavedComposition(String compName, String dateStr, String notesStr, String fileName) {
        mFileName = fileName;
        mCompName = compName;
        mNotesStr = notesStr;

        DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
        try {
            mDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    /*SavedRecording (int fileNum) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recording_file_" + Integer.toString(fileNum) + ".3gp";
        mFileNameBody = "recording_file_" + Integer.toString(fileNum);

        mRecName = "Recording #" + Integer.toString(fileNum);

        mDate = new Date ();
        mLength = new Date(0);
    }*/

    /**
     * Constructor for an existing saved recording.
     *
     * @param fileName      The file name for the recording.
     * @param fileNameBody  The file name for the recording without the path or extension.
     * @param recName       The name of the recording.
     * @param dateStr       The date the recording was made.
     * @param lengthStr     The length of the recording.
     */
    /*SavedRecording (String fileName, String fileNameBody, String recName, String dateStr, String lengthStr) {
        mFileName = fileName;
        mFileNameBody = fileNameBody;
        mRecName = recName;

        DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.ENGLISH);
        try {
            mDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat lengthFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        try {
            mLength = lengthFormat.parse(lengthStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    /**
     * TODO

     * @return  The file name.
     */
    public String getFileName () {

        return mFileName;
    }

    /**
     * TODO

     * @return  The file name.
     */
    public String getNotes () {

        return mNotesStr;
    }

    /**
     * Returns the file name of the saved recording without the directory path or .3gp extension.

     * @return  The file name.
     */
    /*public String getFileNameBody () {

        return mFileNameBody;
    }*/

    /**
     * Returns the date string formatted mm/dd/yy.
     *
     * @return  The formatted date string.
     */
    public String getDateStr () {
        String dateString = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        dateString = dateFormat.format(mDate);

        return dateString;
    }

    /**
     * Returns the composition name.
     *
     * @return  The composition name.
     */
    public String getCompName () {

        return mCompName;
    }

    /**
     * Returns the composition name. TODO Check all docs in this file
     *
     * @return  The composition name.
     */
    public void setCompName (String name) {

        mCompName = name;
    }


    /**
     * Returns the length string formatted like mm:ss.
     *
     * @return  The formatted length string.
     */
    /*public String getLengthStr () {
        String lenString = null;
        SimpleDateFormat lenFormat = new SimpleDateFormat("mm:ss");

        lenString = lenFormat.format(mLength);

        return lenString;
    }*/

    /**
     * Sets the recording name.
     *
     * @param newName   The new recording name to be assigned.
     */
    /*public void setRecName (String newName) {

        mRecName = newName;
    }*/

    /**
     * Sets the length of the recording.
     *
     * @param length   The length of the recording to be assigned.
     */
    /*public void setLength (long length) {

        mLength = new Date(length);
    }*/

    /**
     * Returns a string representation of a saved recording in the form:
     *      Composition Name
     *      Date (MM/dd/yy)
     *      The string of notes
     *      Name of the file
     *
     * @return a string representation of a saved recording
     */
    public String toString () {

        return mCompName + "\n" + this.getDateStr() + "\n" + mNotesStr + "\n" + mFileName;
    }

    /**
     * Writes a saved recording item to a file for internal storage.
     *
     * @param cw The context wrapper.
     */
    public void writeItemToFile (ContextWrapper cw) {
        OutputStreamWriter fOutput;

        try {
            fOutput = new OutputStreamWriter(cw.openFileOutput(mFileName + ".chd", cw.MODE_PRIVATE));
            fOutput.write(this.toString());
            fOutput.close();
        } catch (Exception e) {
            Log.e("Save exception", e.toString());
        }
    }
}
