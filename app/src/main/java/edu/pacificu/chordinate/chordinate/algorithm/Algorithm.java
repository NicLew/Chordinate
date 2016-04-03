package edu.pacificu.chordinate.chordinate.algorithm;

import android.util.Log;

import java.util.Vector;

/**
 * Runs the composition algorithm.
 */
public class Algorithm {

    private static String mMelodyStr;
    private static Scale.ScaleType mScaleType;
    private static Note mKey;
    private static Vector<Note> mMelody = new Vector<Note>();
    private static Vector<Chord> mChords = new Vector<Chord>();

    /**
     * The main method that runs the composition algorithm.
     * @return the string of notes that make up the composition
     */
    public static String compose (String melodyStr, String key, String scaleType, int startingIndex) {
        Chord.AbstractChord abstractChord;
        String composition = "";

        mMelody.clear();
        mChords.clear();

        mMelodyStr = melodyStr;
        mScaleType = getUserScaleType(scaleType);

        /* Load notes from input string into vector. */
        getNotes(startingIndex);

        /* Determine the key. */
        getKey(key);
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
        //mChords.get(0).changeChord(mKey, mScaleType, Chord.AbstractChord.I);

        /* Try to create a proper cadence for a stronger ending. */
        cadenceEnding();

        /* Make sure all notes/chords are within the proper octaves. */
        assignOctaves(0);

        /* Print chords to the screen */
        for (int i = 0; i < mChords.size(); ++i) {
            composition += mChords.get(i).toString();
        }

        return composition + "$";
    }

    /**
     * Attempts to create a cadence at the end of a composition by changing the last two chords to
     * a V-I (Authentic cadence) or IV-I (Plagal cadence). If the chords cannot be changed, they are left
     * as they were.
     */
    private static void cadenceEnding () {
        if (mChords.size() > 1) {
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
        else if (mChords.size() > 0) {
            Chord lastChord = new Chord(mChords.get(mChords.size() - 1));

            if (Chord.AbstractChord.I != lastChord.getAbstractChord()) {
                lastChord.changeChord(mKey, mScaleType, Chord.AbstractChord.I);
            }

            // TODO: Remove after debugging
            System.out.println("Last: " + lastChord.getAbstractChord());

            mChords.set(mChords.size() - 1, lastChord);
        }
    }

    private static void assignOctaves (int startingIndex) {
        for (int i = startingIndex; i < mChords.size(); ++i) {
            mChords.get(i).assignOctave(mMelody.get(i));
        }
    }

    /**
     * Parses the melody string into note objects and adds those notes to a melody vector.
     */
    private static void getNotes (int index) {
        String note;
        int i = index;

        while ('$' != mMelodyStr.charAt(i) && i < mMelodyStr.length()) {
            note = "";

            if (';' == mMelodyStr.charAt(i)) {// TODO: fix magic consts

                if (i - 3 < 0 || !Character.isLetter(mMelodyStr.charAt(i - 3)))
                {
                    note += mMelodyStr.charAt(i - 2); // value
                }
                else {
                    note += mMelodyStr.charAt(i - 3); // value
                    note += mMelodyStr.charAt(i - 2); // accidental (opt)
                }

                note += mMelodyStr.charAt(i - 1); // octave number
                mMelody.add(new Note(note, true));
            }

            ++i;
        }

        System.out.println("Melody: " + mMelody.toString());// TODO: Remove after debugging
    }

    /**
     * Determines the key of the composition based on the melody notes.
     */
    private static void getKey (String userKey) {

        if (0 == userKey.compareTo("Let us decide")) {// TODO Fix magic const
            //mKey = mMelody.get(mMelody.size() - 1);// get key from last note
            mKey = Scale.getRootFromFifth(mMelody.get(mMelody.size() - 1), mScaleType);// TODO: Should it be a fifth down from the last note????? so last chord is always tonic
        }
        else {

            if (1 == userKey.length()) {
                userKey += "0";
            }

            mKey = new Note(userKey, false);
            Log.d ("KeyValue", mKey.toString());
        }

    }

    /**
     * Determines the scale type the user wishes to use.
     */
    private static Scale.ScaleType getUserScaleType (String scaleType) {
        if (0 == scaleType.compareTo("Major")) {
            return Scale.ScaleType.MAJOR;
        }
        else if (0 == scaleType.compareTo("Natural Minor")) {
            return Scale.ScaleType.NATURAL_MINOR;
        }
        else if (0 == scaleType.compareTo("Harmonic Minor")) {
            return Scale.ScaleType.HARMONIC_MINOR;
        }
        else if (0 == scaleType.compareTo("Melodic Minor")) {
            return Scale.ScaleType.MELODIC_MINOR;
        }

        return Scale.ScaleType.MAJOR;
    }
}
