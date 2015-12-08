package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SavedRecording extends SavedFile {

    private String mFilePath;
    private Date mLength;

    /**
     * Saved recording constructor. Determines filename and recording name.
     *
     * @param fileNum   The file number to assign.
     */
    SavedRecording (int fileNum) {
        super(fileNum);
        mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFilePath += "/saved_file_" + Integer.toString(fileNum) + ".3gp";

        mName = "Recording #" + Integer.toString(fileNum);
        mLength = new Date(0);
    }

    /**
     * Constructor for an existing saved recording.
     *
     * @param filePath      The file path for the recording.
     * @param fileName      The file name for the recording without the path or extension.
     * @param recName       The name of the recording.
     * @param dateStr       The date the recording was made.
     * @param lengthStr     The length of the recording.
     */
    SavedRecording (String filePath, String fileName, String recName, String dateStr, String lengthStr) {
        super(recName, dateStr, fileName);
        mFilePath = filePath;

        SimpleDateFormat lengthFormat = new SimpleDateFormat("mm:ss", Locale.ENGLISH);
        try {
            mLength = lengthFormat.parse(lengthStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the full path of the file.
     *
     * @return the full path of the file
     */
    public String getFilePath () {

        return mFilePath;
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
     *      Date (yyMMddHHmmssZ)
     *      Length (mm:ss)
     *      Name of associated .3gp file
     *      Name of associated .3gp file without the extension
     *
     * @return a string representation of a saved recording
     */
    public String toString () {

        return mName + "\n" + this.getWholeDateStr() + "\n" + this.getLengthStr() + "\n" + mFilePath + "\n" + mFileName;
    }

    /**
     * Writes a saved recording item to a file for internal storage.
     *
     * @param cw The context wrapper.
     */
    public void writeItemToFile (ContextWrapper cw) {
        OutputStreamWriter fOutput;

        try {
            fOutput = new OutputStreamWriter(cw.openFileOutput(mFileName + ".sr", cw.MODE_PRIVATE));
            fOutput.write(this.toString());
            fOutput.close();
        } catch (Exception e) {
            Log.e("Save exception", e.toString());
        }
    }
}
