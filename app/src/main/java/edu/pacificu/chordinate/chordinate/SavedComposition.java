package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SavedComposition {
    private String mFileName;
    private String mCompName;
    private String mNotesStr;
    private Date mDate;

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

    /**
     * Constructor for a partial saved composition.
     *
     * @param fileNum       The file number to assign.
     * @param compName      The name of the composition.
     * @param notesStr      The string of notes for the composition.
     */
    SavedComposition (int fileNum, String compName, String notesStr) {
        mFileName = "comp_file_" + Integer.toString(fileNum);
        mCompName = compName;
        mNotesStr = notesStr;
        mDate = new Date ();
    }

    /**
     * Constructor for an existing saved composition.
     *
     * @param compName      The name of the composition.
     * @param dateStr       The date the recording was made.
     * @param notesStr      The string of notes for the composition.
     * @param fileName      The file name where information is stored.
     */
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

    /**
     * Returns the file name where this compositions information is stored in memory.
     *
     * @return  The file name.
     */
    public String getFileName () {

        return mFileName;
    }

    /**
     * Returns the notes string for a composition.
     *
     * @return  The string of notes.
     */
    public String getNotes () {

        return mNotesStr;
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
     * Returns the composition name.
     *
     * @return  The composition name.
     */
    public String getCompName () {

        return mCompName;
    }

    /**
     * Sets the composition name.
     *
     * @param name  The name the composition is to be given.
     */
    public void setCompName (String name) {

        mCompName = name;
    }

    /**
     * Returns a string representation of a saved recording in the form:
     *      Composition Name
     *      Date (MM/dd/yy)
     *      The string of notes
     *      Name of the file
     *
     * @return a string representation of a saved composition
     */
    public String toString () {

        return mCompName + "\n" + this.getDateStr() + "\n" + mNotesStr + "\n" + mFileName;
    }

    /**
     * Writes a saved composition item to a file for internal storage.
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
