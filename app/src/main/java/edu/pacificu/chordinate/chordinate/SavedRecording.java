package edu.pacificu.chordinate.chordinate;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedRecording {
    private File mFile;
    private String mFileName;
    private String mRecName;
    private Date mDate;
    private long mLength;

    /**
     * Saved recording constructor. Determines filename and recording name and date.
     *
     * @param fileNum   The file number to assign.
     */
    SavedRecording (int fileNum) {
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/recording_file_"+Integer.toString(fileNum)+".3gp";
        mFile = new File(mFileName);

        mRecName = "Recording #"+Integer.toString(fileNum);

        mDate = new Date ();
        mLength = 0;
    }

    /**
     * Returns the file name of the saved recording.

     * @return  The file name.
     */
    public String getFileName () {

        return mFileName;
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

        lenString = lenFormat.format(new Date(mLength));

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

        mLength = length;
    }
}
