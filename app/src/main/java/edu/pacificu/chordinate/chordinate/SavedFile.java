package edu.pacificu.chordinate.chordinate;


import android.content.ContextWrapper;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class SavedFile {
    protected String mFileName;
    protected String mName;
    protected Date mDate;

    /**
     * Saved file constructor.
     *
     * @param fileNum   The file number to be assigned.
     */
    SavedFile (int fileNum) {
        mFileName = "saved_file_" + Integer.toString(fileNum);
        mName = "Saved Item #" + Integer.toString(fileNum);
        mDate = new Date ();
    }

    /**
     * Saved file constructor for an existing saved item.
     *
     * @param name      The name of the saved file.
     * @param dateStr   The string representation of the date the file was created.
     * @param fileName  The name of the file the info is stored in.
     */
    SavedFile (String name, String dateStr, String fileName) {
        mFileName = fileName;
        mName = name;

        DateFormat format = new SimpleDateFormat("yyMMddHHmmssZ", Locale.ENGLISH);
        try {
            mDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the file name where this compositions information is stored in memory.
     *
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
     * Returns the date string formatted yyMMddHHmmssZ.
     *
     * @return  The date string.
     */
    public String getWholeDateStr () {
        String dateString = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssZ");

        dateString = dateFormat.format(mDate);

        return dateString;
    }

    /**
     * Returns the name.
     *
     * @return  The name.
     */
    public String getName () {

        return mName;
    }

    /**
     * Sets the name.
     *
     * @param name  The name that is to be given.
     */
    public void setName (String name) {

        mName = name;
    }

    /**
     * Writes a saved composition item to a file for internal storage. Must override this
     * function in subclasses so that the file extension is correct.
     *
     * @param cw The context wrapper.
     */
    public void writeItemToFile (ContextWrapper cw) {
        OutputStreamWriter fOutput;

        try {
            fOutput = new OutputStreamWriter(cw.openFileOutput(mFileName + ".xxx", cw.MODE_PRIVATE));
            fOutput.write(this.toString());
            fOutput.close();
        } catch (Exception e) {
            Log.e("Save exception", e.toString());
        }
    }

    public static class Comparators {

        public static Comparator<SavedFile> DATE = new Comparator<SavedFile>() {
            @Override
            public int compare(SavedFile item1, SavedFile item2) {
                return item1.mDate.compareTo(item2.mDate);
            }
        };
    }
}
