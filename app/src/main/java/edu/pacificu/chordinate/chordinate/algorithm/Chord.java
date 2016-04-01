package edu.pacificu.chordinate.chordinate.algorithm;

import java.util.Random;
import java.util.Vector;

/**
 * Represents a chord.
 */
public class Chord {

    public enum AbstractChord {
        I, II, III, IV, V, VI, VII, NONE
    }

    public enum Inversion {
        ROOT,   /* tonic on bottom */
        FIRST,  /* third on bottom */
        SECOND, /* fifth on bottom */
        THIRD   /* seventh on bottom */
    }

    /* Chord position relative to root position */
    private static final int ROOT = 0;
    private static final int THIRD = 1;
    private static final int FIFTH = 2;
    private static final int SEVENTH = 3;
    private static final int NUM_ATTEMPTS = 2;

    private static final char MIN_OCTAVE = '1';

    private Vector<Note> mChord = new Vector<Note>();
    private AbstractChord mAbstract;
    private Inversion mInversion;
    private int mChordType;

    /**
     * Create a blank chord object in a default root position.
     */
    Chord () {
        mInversion = Inversion.ROOT;
    }

    /**
     * Create a new chord object based on the values of an existing chord.
     *
     * @param chord the chord values to create a new chord object with
     */
    Chord (Chord chord) {
        mChord = chord.mChord;
        mInversion = chord.mInversion;
        mAbstract = chord.mAbstract;
        mChordType = chord.mChordType;
    }

    /**
     * A wrapper to get a note from the chord vector.
     *
     * @param i the index of the note to return
     * @return the note at index i within the chord vector
     */
    private Note get (int i) {
        return mChord.get(i);
    }

    /**
     * A wrapper to set a note within the chord vector to the passed in value.
     *
     * @param i the index of the note to be set within the chord vector
     * @param note the note value to be used
     */
    private void set (int i, Note note) {
        mChord.set(i, note);
    }

    /**
     * A wrapper to add a note to the chord vector.
     *
     * @param note the note to add to the chord vector
     */
    private void add (Note note) {
        mChord.add(note);
    }

    /**
     * A wrapper to return the size of the chord vector within the chord object.
     *
     * @return the size of the chord
     */
    private int size () {
        return mChord.size();
    }

    /**
     * Sets the octave for each note within a chord based on what octave the melody note is in.
     *
     * @param melodyNote the note to base the octave assignment off of
     */
    public void assignOctave (Note melodyNote) {
        for (int i = 0; i < mChord.size(); ++i) {

            /* If less than or equal to in chromatic scale use same octave, otherwise subtract one */
            if (MIN_OCTAVE == melodyNote.getOctave() || mChord.get(i).bIsNoteLessThen(melodyNote) ||
                    mChord.get(i).bAreNotesEqual(melodyNote))
            {
                mChord.get(i).assignOctave(melodyNote.getOctave());
            }
            else {
                mChord.get(i).assignOctave((char) (((int) melodyNote.getOctave()) - 1));
            }
        }
    }

    /**
     * Returns the abstract value of a chord.
     *
     * @return the abstract chord
     */
    public AbstractChord getAbstractChord () {
        return mAbstract;
    }

    /**
     * Returns the inversion of the chord.
     *
     * @return the inversion of the chord
     */
    public Inversion getInversion () {
        return mInversion;
    }

    /**
     * Determines a concrete chord based on note, key, chord, and scale information.
     *
     * @param key the key
     * @param melodyNote the note to base the chord choice on
     * @param scaleType the scale type
     * @param abstractChord the abstract chord to build
     * @param chordType the type of chord to build
     * @return the chord
     */
    public static Chord getConcreteChord (Note key, Note melodyNote, Scale.ScaleType scaleType, AbstractChord abstractChord, int chordType) {
        Chord chord;

        switch (chordType) {
            case ChordType.TRIAD:
                chord = buildTriad(key, scaleType, getScaleDegreeFromAbstract(abstractChord));
                chord.mChordType = ChordType.TRIAD;
                break;

            case ChordType.DOMINANT_SEVENTH:
                chord = buildDominantSeventh(key, scaleType, getScaleDegreeFromAbstract(abstractChord));
                chord.mChordType = ChordType.DOMINANT_SEVENTH;
                break;

            default:
                chord = buildTriad(key, scaleType, getScaleDegreeFromAbstract(abstractChord));
                chord.mChordType = ChordType.TRIAD;
                break;
        }

        chord.mAbstract = abstractChord;
        chord = invertRootPosChord(chord, melodyNote);

        return chord;
    }

    /**
     * Determines a chord to use based on a note within the scale, and inverts that chord properly.
     *
     * @param key the key
     * @param note the note to base the chord choice on
     * @param scaleType the scale type
     * @return the chosen chord
     */
    public static Chord getScaleToneChord(Note key, Note note, Scale.ScaleType scaleType) {
        Vector<Chord> possibleChords;
        Chord chord;

        possibleChords = getAllTriads(key, scaleType);
        chord = chooseChord(possibleChords, note);
        chord = invertRootPosChord(chord, note);

        return chord;
    }

    /**
     * Determines a chord to use based on a non-scale note, and inverts that chord properly.
     *
     * @param key the key
     * @param note the note to base the chord choice on
     * @param scaleType the scale type
     * @return the chosen chord
     */
    public static Chord getNonScaleToneChord(Note key, Note note, Scale.ScaleType scaleType) {

        Vector<Chord> possibleChords = new Vector<Chord>();
        Chord chord;

        possibleChords.addAll(getAllBorrowedTriads(key, scaleType));
        possibleChords.addAll(getAllDomSevenths (key, scaleType));

        chord = chooseChord(possibleChords, note);
        chord = invertRootPosChord(chord, note);

        return chord;
    }

    /**
     * Determines which chord to choose from all of the possible chords.
     *
     * @param possibleChords the vector of possible chords to choose from
     * @param note the note that a valid chord should contain
     * @return the chosen chord
     */
    private static Chord chooseChord (Vector<Chord> possibleChords, Note note) {
        Vector<Chord> validChords = new Vector<Chord>();
        Chord chord;
        Random rand = new Random();
        int i = 0, j = 0;

        /* Searches all possible chords and adds valid chords to a new vector. */
        while (i < possibleChords.size())
        {
            if (searchChord(possibleChords.get(i), note)) {
                validChords.add(possibleChords.get(i));
            }
            ++i;
        }

        /* Try to avoid using melody note as root, it causes the third to
         * be on the bottom when the chord is inverted.
         */
        while (validChords.size() > 1 && j < validChords.size()) {
            if (note.bAreNotesEqual(validChords.get(j).mChord.get(0))) { // TODO: Fix magic const (look for others with (0) too)
                validChords.remove(j);
            }
            else {
                ++j;
            }
        }

        /* From the remaining valid chords, choose one at random. */
        chord = validChords.get(rand.nextInt(validChords.size()));

        return chord;
    }

    /**
     * Inverts a root position chord so that the melody note of the chord is on top.
     *
     * @param chord the chord to invert
     * @param melodyNote the melody note within that chord
     * @return the inverted chord
     */
    public static Chord invertRootPosChord (Chord chord, Note melodyNote) {
        int i = 0;
        int melIndex = -1;
        Chord invertedChord = new Chord();

        if (null != melodyNote) {

            /* Finds and sets the melody note within the given chord. */
            for (int j = 0; j < chord.size(); ++j) {
                if (melodyNote.bAreNotesEqual(chord.get(j))) {
                    chord.set(j, new Note(chord.get(j), true));
                    melIndex = j;
                }
            }

            /* If the melody note is already on top, do nothing. Otherwise continue. */
            if (melIndex != chord.size() - 1) {

                switch (chord.mChordType) {
                    case ChordType.TRIAD:
                        switch (melIndex) {
                            case 0:// TODO: Fix magic consts
                                invertedChord.mInversion = Inversion.FIRST;
                                invertedChord.add(chord.get(THIRD));
                                invertedChord.add(chord.get(FIFTH));
                                invertedChord.add(chord.get(ROOT));
                                break;
                            case 1:
                                invertedChord.mInversion = Inversion.SECOND;
                                invertedChord.add(chord.get(FIFTH));
                                invertedChord.add(chord.get(ROOT));
                                invertedChord.add(chord.get(THIRD));
                                break;
                        }
                        break;

                    case ChordType.DOMINANT_SEVENTH:
                        switch (melIndex) {
                            case 0:
                                invertedChord.mInversion = Inversion.FIRST;
                                invertedChord.add(chord.get(THIRD));
                                invertedChord.add(chord.get(FIFTH));
                                invertedChord.add(chord.get(SEVENTH));
                                invertedChord.add(chord.get(ROOT));
                                break;
                            case 1:
                                invertedChord.mInversion = Inversion.SECOND;
                                invertedChord.add(chord.get(FIFTH));
                                invertedChord.add(chord.get(SEVENTH));
                                invertedChord.add(chord.get(ROOT));
                                invertedChord.add(chord.get(THIRD));
                                break;
                            case 2:
                                invertedChord.mInversion = Inversion.THIRD;
                                invertedChord.add(chord.get(SEVENTH));
                                invertedChord.add(chord.get(ROOT));
                                invertedChord.add(chord.get(THIRD));
                                invertedChord.add(chord.get(FIFTH));
                                break;
                        }
                        break;

                    default:
                        return chord;// TODO: test...
                }
            } else {
                invertedChord = chord;
            }
        }
        else {
            invertedChord = chord;
        }

        invertedChord.mAbstract = chord.mAbstract;
        invertedChord.mChordType = chord.mChordType;

        return invertedChord;
    }

    /**
     * Creates a triad built on a scale tone.
     *
     * @param key the key
     * @param scaleType the scale type
     * @param scaleDegree the scale degree of the root note
     * @return the triad
     */
    private static Chord buildTriad(Note key, Scale.ScaleType scaleType, int scaleDegree) {

        Chord chord = new Chord();
        Note chordRoot = Scale.getNoteInScale(key, scaleType, scaleDegree);

        chord.add(chordRoot);
        chord.add(Scale.getThird(chordRoot, scaleType, scaleDegree));
        chord.add(Scale.getFifth(chordRoot, scaleType, scaleDegree));

        return chord;
    }

    /**
     * Creates a dominant seventh chord. A dominant seventh chord consists of a major triad built on a scale tone
     * with a minor seventh added.
     *
     * @param key the key
     * @param scaleType the scale type
     * @param scaleDegree the scale degree of the root note
     * @return the dominant seventh chord
     */
    private static Chord buildDominantSeventh(Note key, Scale.ScaleType scaleType, int scaleDegree) {

        Note chordRoot = Scale.getNoteInScale(key, scaleType, scaleDegree);

        Chord chord = buildTriad(chordRoot, Scale.ScaleType.MAJOR, ScaleDegree.TONIC);
        Note seventh = Scale.getMinorSeventh(chord.get(0));
        chord.add(seventh);

        return chord;
    }

    /**
     * Creates a string representation of a chord object.
     *
     * @return the string representation of a chord
     */
    public String toString() {
        String str = "";

        for (int i = 0; i < mChord.size(); ++i) {
            str += mChord.get(i).toString();
        }

        return str + ";";
    }

    /**
     * Determines the abstract chord based on the key, note, and scale type.
     *
     * @param key the key
     * @param note the note to find the abstract chord of
     * @param scaleType the scale type
     * @return the abstract chord of the note if found, otherwise NONE
     */
    public static AbstractChord getAbstractChord(Note key, Note note, Scale.ScaleType scaleType) {

        switch (Scale.getScaleDegreeFromNote(key, note, scaleType)) {
            case ScaleDegree.TONIC:
                return AbstractChord.I;

            case ScaleDegree.SUPERTONIC:
                return AbstractChord.II;

            case ScaleDegree.MEDIANT:
                return AbstractChord.III;

            case ScaleDegree.SUBDOMINANT:
                return AbstractChord.IV;

            case ScaleDegree.DOMINANT:
                return AbstractChord.V;

            case ScaleDegree.SUBMEDIANT:
                return AbstractChord.VI;

            case ScaleDegree.SUBTONIC:
                return AbstractChord.VII;

            case ScaleDegree.NON_SCALE_TONE:
                return AbstractChord.NONE;

            default:
                return AbstractChord.I;
        }
    }

    /**
     * Returns the melody note from within a chord.
     *
     * @return the melody note if found, otherwise the top note in the chord
     */
    private Note getMelodyNote () {
        for (int i = 0; i < mChord.size(); ++i) {
            if (mChord.get(i).getbIsMelodyNote())
            {
                return mChord.get(i);
            }
        }

        return mChord.get(mChord.size() - 1);
    }

    /**
     * Attempts to change a chord to the abstract chord passed in. If a triad doesn't fit, borrowed chords and
     * dominant seventh chords are tried. If no chords fit, the chord is left unchanged.
     *
     * @param key the key
     * @param scaleType the scale type
     * @param abstractChord the abstract chord to change the chord into
     * @return true if the chord is successfully changed, otherwise false
     */
    public boolean changeChord (Note key, Scale.ScaleType scaleType, AbstractChord abstractChord) {
        Vector<Chord> possibleChords;
        Vector<Chord> validChords = new Vector<Chord>();
        int i = 0, j = 0, k = 0;

        possibleChords = getAllTriads(key, scaleType);

        for (int n = 0; n < NUM_ATTEMPTS; ++n) {

            while (i < possibleChords.size()) {
                if (searchChord(possibleChords.get(i), this.getMelodyNote())) {
                    validChords.add(possibleChords.get(i));
                }
                ++i;
            }

            while (j < validChords.size()) {
                if (abstractChord != validChords.get(j).getAbstractChord()) {
                    validChords.remove(j);
                }
                else {
                    ++j;
                }
            }

            // Try to avoid using melody note as root, causes third to be on the bottom...
            if (validChords.size() > 1) {
                while (k < validChords.size()) {
                    if (this.getMelodyNote().bAreNotesEqual(validChords.get(k).mChord.get(0))) {
                        validChords.remove(k);
                    }
                    else {
                        ++k;
                    }
                }
            }

            if (validChords.size() > 0) {
                Random rand = new Random();
                int randIndex = rand.nextInt(validChords.size());

                if (Chord.Inversion.ROOT == validChords.get(randIndex).getInversion()) {
                    validChords.set(randIndex, Chord.invertRootPosChord(validChords.get(randIndex), this.getMelodyNote()));
                }

                this.mChord = validChords.get(randIndex).mChord;
                this.mChordType = validChords.get(randIndex).mChordType;
                this.mInversion = validChords.get(randIndex).mInversion;
                this.mAbstract = abstractChord;

                return true;
            }

            // if didn't find, try again with borrowed and dom 7s
            possibleChords.clear();
            possibleChords.addAll(getAllBorrowedTriads(key, scaleType));
            possibleChords.addAll(getAllDomSevenths(key, scaleType));
        }

        return false;
    }

    /**
     * Searches a chord for a particular note.
     *
     * @param chord the chord to search
     * @param note the note to search for
     * @return true if the note is found, otherwise false
     */
    private static boolean searchChord (Chord chord, Note note) {
        for (int i = 0; i < chord.size(); ++i) {
            if (chord.get(i).bAreNotesEqual(note)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Creates all dominant seventh chords for a given key and scale type.
     *
     * @param key the key to create the chords in
     * @param scaleType the scale type to create the dominant seventh chords in
     * @return a Vector of Chords containing all possible dominant seventh chords for the key and scale type
     */
    private static Vector<Chord> getAllDomSevenths (Note key, Scale.ScaleType scaleType) {
        Vector<Chord> domChords = new Vector<Chord>();

        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.I, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.II, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.III, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.IV, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.V, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VI, ChordType.DOMINANT_SEVENTH));
        domChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VII, ChordType.DOMINANT_SEVENTH));

        return domChords;
    }

    /**
     * Creates all borrowed chords for a given key and scale type.
     *
     * @param key the key to create the chords in
     * @param scaleType the scale type to create the borrowed chords in
     * @return a Vector of Chords containing all possible borrowed chords for the key and scale type
     */
    private static Vector<Chord> getAllBorrowedTriads (Note key, Scale.ScaleType scaleType) {
        Vector<Chord> borrowedChords = new Vector<Chord>();

        if (Scale.ScaleType.MAJOR == scaleType) {
            scaleType = Scale.ScaleType.HARMONIC_MINOR;
        }
        else {
            scaleType = Scale.ScaleType.MAJOR;
        }

        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.I, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.II, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.III, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.IV, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.V, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VI, ChordType.TRIAD));
        borrowedChords.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VII, ChordType.TRIAD));

        return borrowedChords;
    }

    /**
     * Creates all triads for a given key and scale type.
     *
     * @param key the key to create the triads in
     * @param scaleType the scale type to create the triads in
     * @return a Vector of Chords containing all possible triads for the key and scale type
     */
    private static Vector<Chord> getAllTriads (Note key, Scale.ScaleType scaleType) {
        Vector<Chord> triads = new Vector<Chord>();

        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.I, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.II, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.III, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.IV, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.V, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VI, ChordType.TRIAD));
        triads.add(Chord.getConcreteChord(key, null, scaleType, Chord.AbstractChord.VII, ChordType.TRIAD));

        return triads;
    }

    /**
     * Returns the scale degree based on an abstract chord.
     *
     * @param abstractChord the abstract chord
     * @return the scale degree
     */
    private static int getScaleDegreeFromAbstract(AbstractChord abstractChord) {

        switch (abstractChord) {
            case I:
                return ScaleDegree.TONIC;

            case II:
                return ScaleDegree.SUPERTONIC;

            case III:
                return ScaleDegree.MEDIANT;

            case IV:
                return ScaleDegree.SUBDOMINANT;

            case V:
                return ScaleDegree.DOMINANT;

            case VI:
                return ScaleDegree.SUBMEDIANT;

            case VII:
                return ScaleDegree.SUBTONIC;

            default:
                return ScaleDegree.TONIC;
        }
    }
}

