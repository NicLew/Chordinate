package edu.pacificu.chordinate.chordinate;

import android.content.ContextWrapper;
import android.util.Log;

import java.io.OutputStreamWriter;

public class SavedComposition extends SavedFile {

    private String mNotesStr;

    /**
     * Constructor for a partial saved composition.
     *
     * @param fileNum       The file number to assign.
     * @param compName      The name of the composition.
     * @param notesStr      The string of notes for the composition.
     */
    SavedComposition (int fileNum, String compName, String notesStr) {
        super(fileNum);
        mName = compName;
        mNotesStr = notesStr;
    }

    /**
     * Constructor for an existing saved composition.
     *
     * @param compName      The name of the composition.
     * @param dateStr       The string representation of the date the composition was made.
     * @param notesStr      The string of notes for the composition.
     * @param fileName      The file name where information is stored.
     */
    SavedComposition(String compName, String dateStr, String notesStr, String fileName) {
        super(compName, dateStr, fileName);
        mNotesStr = notesStr;
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
     * Returns a string representation of a saved recording in the form:
     *      Composition Name
     *      Date (yyMMddHHmmssZ)
     *      The string of notes
     *      Name of the file
     *
     * @return a string representation of a saved composition
     */
    public String toString () {

        return mName + "\n" + this.getWholeDateStr() + "\n" + mNotesStr + "\n" + mFileName;
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
