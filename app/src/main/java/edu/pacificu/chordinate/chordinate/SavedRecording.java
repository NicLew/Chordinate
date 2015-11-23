package edu.pacificu.chordinate.chordinate;

import android.os.Environment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SavedRecording {

    private String mFileName;
    private String mFileNameBody;
    private String mRecName;
    private Date mDate;
    private Date mLength;

    /**
     * Saved recording constructor. Determines filename and recording name and date.
     *
     * @param fileNum   The file number to assign.
     */
    SavedRecording (int fileNum) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recording_file_" + Integer.toString(fileNum) + ".3gp";
        mFileNameBody = "recording_file_" + Integer.toString(fileNum);

        mRecName = "Recording #" + Integer.toString(fileNum);

        mDate = new Date ();
        mLength = new Date(0);
    }

    /**
     * TODO: Doc
     */
    SavedRecording (String fileName, String recName, String dateStr, String lengthStr) {
        mFileName = fileName;
        mFileNameBody = "temp_str"; // TODO: Fix!!
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
    }

    /**
     * Returns the file name of the saved recording with the .3gp extension.

     * @return  The file name.
     */
    public String getFileName () {

        return mFileName;
    }

    /**
     * Returns the file name of the saved recording without the directory path or .3gp extension.

     * @return  The file name.
     */
    public String getFileNameBody () {

        return mFileNameBody;
    }

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
     * Returns the recording name.
     *
     * @return  The recording name.
     */
    public String getRecName () {

        return mRecName;
    }

    /**
     * Returns the length string formatted like mm:ss.
     *
     * @return  The formatted length string.
     */
    public String getLengthStr () {
        String lenString = null;
        SimpleDateFormat lenFormat = new SimpleDateFormat("mm:ss");

        lenString = lenFormat.format(mLength);

        return lenString;
    }

    /**
     * Sets the recording name.
     *
     * @param newName   The new recording name to be assigned.
     */
    public void setRecName (String newName) {

        mRecName = newName;
    }

    /**
     * Sets the length of the recording.
     *
     * @param length   The length of the recording to be assigned.
     */
    public void setLength (long length) {

        mLength = new Date(length);
    }

    /**
     * Returns a string representation of a saved recording in the form:
     *      Recording Name
     *      Date (MM/dd/yy)
     *      Length (mm:ss)
     *      Name of associated .3gp file
     *
     * @return a string representation of a saved recording
     */
    public String toString () {

        return mRecName + "\n" + this.getDateStr() + "\n" + this.getLengthStr() + "\n" + mFileName;
    }
}
