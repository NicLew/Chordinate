package edu.pacificu.chordinate.chordinate.algorithm;

import java.util.Vector;

/**
 * Runs the composition algorithm.
 */
public class Algorithm {

    private static String mMelodyStr = "G4;A5;B5;G4;F4;G4;"; /* user input melody */
    private static Scale.ScaleType mScaleType = Scale.ScaleType.MAJOR; /* user input scale type */
    private static Note mKey; // TODO: let user choose key
    private static Vector<Note> mMelody = new Vector<Note>();
    private static Vector<Chord> mChords = new Vector<Chord>();

    /**
     * The main method that runs the composition algorithm.
     * @return the string of notes that make up the composition
     */
    public static String compose (String melodyStr, Scale.ScaleType scaleType /*, Note/String key*/) {
        Chord.AbstractChord abstractChord;
        String composition = "";

        /* Load notes from input string into vector. */
        getNotes();

        /* Determine the key. */
        getKey();
        //System.out.println("Key: " + mKey);


        /* Determine chords based on the melody. */
        for (int i = 0; i < mMelody.size(); ++i) {

            abstractChord = Chord.getAbstractChord(mKey, mMelody.get(i), mScaleType);

            if (Chord.AbstractChord.NONE == abstractChord) {
                mChords.add(Chord.getNonScaleToneChord(mKey, mMelody.get(i), mScaleType));
            }
            else
            {
                mChords.add(Chord.getScaleToneChord(mKey, mMelody.get(i), mScaleType));
            }
        }

        /* Try to make first chord a tonic chord */ // TODO: Should we do this or not???
        mChords.get(0).changeChord(mKey, mScaleType, Chord.AbstractChord.I);

        /* Try to create a proper cadence for a stronger ending. */
        cadenceEnding();

        /* Make sure all notes/chords are within the proper octaves. */
        assignOctaves();

        /* Print chords to the screen TODO: Create string/file? when integrating with app */
        //System.out.println();
        //System.out.println("Chords:");
        for (int i = 0; i < mChords.size(); ++i) {
            //System.out.println(mChords.get(i).toString());
            composition += mChords.get(i).toString();
        }

        return composition;
    }

    /**
     * Attempts to create a cadence at the end of a composition by changing the last two chords to
     * a V-I (Authentic cadence) or IV-I (Plagal cadence). If the chords cannot be changed, they are left
     * as they were.
     */
    private static void cadenceEnding () {
        Chord penultChord = new Chord(mChords.get(mChords.size() - 2));
        Chord lastChord = new Chord(mChords.get(mChords.size() - 1));

        if (Chord.AbstractChord.I != lastChord.getAbstractChord()) {
            lastChord.changeChord(mKey, mScaleType, Chord.AbstractChord.I);
        }

        if (Chord.AbstractChord.V != penultChord.getAbstractChord() && Chord.AbstractChord.IV != penultChord.getAbstractChord()) {
            if (!penultChord.changeChord(mKey, mScaleType, Chord.AbstractChord.V)) {
                penultChord.changeChord(mKey, mScaleType, Chord.AbstractChord.IV);
            }
        }

        System.out.println("Penult: " + penultChord.getAbstractChord());// TODO: Remove after debugging
        System.out.println("Last: " + lastChord.getAbstractChord());

        mChords.set(mChords.size() - 2, penultChord);
        mChords.set(mChords.size() - 1, lastChord);
    }

    private static void assignOctaves () {
        for (int i = 0; i < mChords.size(); ++i) {
            mChords.get(i).assignOctave(mMelody.get(i));
        }
    }

    /**
     * Parses the melody string into note objects and adds those notes to a melody vector.
     */
    private static void getNotes () {
        String note;
        int i = 0;

        while (i < mMelodyStr.length()) {
            note = "";

            while (i < mMelodyStr.length() && ';' != mMelodyStr.charAt(i)) {// fix magic consts
                note += mMelodyStr.charAt(i);
                ++i;
            }

            mMelody.add(new Note(note, true));
            ++i;
        }
        System.out.println("Melody: " + mMelody.toString());// TODO: Remove after debugging
    }

    /**
     * Determines the key of the composition based on the melody notes.
     */
    private static void getKey () {
        //mKey = mMelody.get(mMelody.size() - 1);// get key from last note
        mKey = Scale.getRootFromFifth(mMelody.get(mMelody.size() - 1), mScaleType);// TODO: Should it be a fifth down from the last note????? so last chord is always tonic
    }
}
