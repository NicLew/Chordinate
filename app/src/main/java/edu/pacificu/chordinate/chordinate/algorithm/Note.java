package edu.pacificu.chordinate.chordinate.algorithm;

/**
 * Represents a note with a note value, an accidental, an octave, and an isMelodyNote flag.
 */
public class Note {

    public static final char NATURAL = 'n';
    public static final char SHARP = '#';
    public static final char FLAT = 'b';
    public static final char DOUBLE_SHARP = 'X';
    public static final char DOUBLE_FLAT = '%';

    private static final int VALUE_INDEX = 0;
    private static final int OCTAVE_INDEX = 1;
    private static final int ACCIDENTAL_INDEX = 1;
    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 3;

    private Character mValue;
    private Character mAccidental;
    private Character mOctave;
    private boolean mbIsMelodyNote;

    /**
     * Create a new note from a string.
     *
     * @param note the string representation of a note to create a note object from
     * @param bIsMelody the isMelodyNote flag value
     */
    Note (String note, boolean bIsMelody) {
        mValue = note.charAt(VALUE_INDEX);

        if (MIN_LENGTH == note.length()) {
            mOctave = note.charAt(OCTAVE_INDEX);
            mAccidental = NATURAL;
        }
        else if (MAX_LENGTH == note.length())
        {
            mAccidental = note.charAt(ACCIDENTAL_INDEX);
            mOctave = note.charAt(MIN_LENGTH);
        }

        mbIsMelodyNote = bIsMelody;
    }

    /**
     * Create a new note with the values passed in.
     *
     * @param value the note value
     * @param accidental the accidental value
     * @param octave the octave value
     * @param bIsMelody the isMelodyNote flag value
     */
    Note (Character value, Character accidental, Character octave, boolean bIsMelody) {
        mValue = value;
        mAccidental = accidental;
        mOctave = octave;
        mbIsMelodyNote = bIsMelody;
    }

    /**
     * Create a new note with the information from an existing note, and set the isMelodyNote flag.
     *
     * @param note the note value to create
     * @param bIsMelody the value to set the isMelodyNote flag to
     */
    Note (Note note, boolean bIsMelody) {
        mValue = note.mValue;
        mAccidental = note.mAccidental;
        mOctave = note.mOctave;
        mbIsMelodyNote = bIsMelody;
    }

    /**
     * Create a new note object with the information from an existing note.
     *
     * @param note the note value to create
     */
    Note (Note note) {
        mValue = note.mValue;
        mAccidental = note.mAccidental;
        mOctave = note.mOctave;
        mbIsMelodyNote = note.mbIsMelodyNote;
    }

    /**
     * Returns true if the note is a melody note, and false if it is not.
     *
     * @return true if the note is a melody note, otherwise false
     */
    public boolean getbIsMelodyNote() {
        return mbIsMelodyNote;
    }

    /**
     * Sets the octave of a note to the passed in value.
     *
     * @param octave the octave value to be assigned
     */
    public void assignOctave (Character octave) {
        mOctave = octave;
    }

    /**
     * Returns the octave of a note.
     *
     * @return the octave of a note
     */
    public Character getOctave () {
        return mOctave;
    }

    /**
     * Determines if two notes are of equal value.
     *
     * @param note the note to compare to the calling notes
     * @return true if the notes are equal, otherwise false
     */
    public Boolean bAreNotesEqual (Note note) {
        Boolean bAreEqual = false;

        if (this.mValue == note.mValue && this.mAccidental == note.mAccidental) {
            bAreEqual = true;
        }

        return bAreEqual;
    }

    /**
     * Determines if the index of one note is less than the index of another in the chromatic scale.
     *
     * @param left the note to compare to the calling note
     * @return true if the index of the calling note is less than the passed in note, otherwise false
     */
    public Boolean bIsNoteLessThen(Note left) {
        if (Scale.findNoteInList(this) < Scale.findNoteInList(left)) {
            return true;
        }

        return false;
    }

    /**
     * Returns a string that represents a note object.
     *
     * @return a string representation of a note
     */
    public String toString () {
        return Character.toString(mValue) + Character.toString(mAccidental) + Character.toString(mOctave);
    }
}
