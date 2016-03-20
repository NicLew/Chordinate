package edu.pacificu.chordinate.chordinate.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains functionality based on scales and scale types.
 */
public class Scale {

    public enum ScaleType {
        MAJOR, NATURAL_MINOR, HARMONIC_MINOR, MELODIC_MINOR
    }

    public static final char NATURAL = 'n';
    public static final char SHARP = '#';
    public static final int NUM_NOTES = 12;
    public static final int NUM_SCALE_STEPS = 7;
    public static final int NUM_STEPS_MINOR_SEVENTH = 10;

    private static final List<Note> CHROMATIC_SCALE = new ArrayList<Note>() {
        {
            add(new Note('A', NATURAL, '0', false));
            add(new Note('A', SHARP, '0', false));
            add(new Note('B', NATURAL, '0', false));
            add(new Note('C', NATURAL, '0', false));
            add(new Note('C', SHARP, '0', false));
            add(new Note('D', NATURAL, '0', false));
            add(new Note('D', SHARP, '0', false));
            add(new Note('E', NATURAL, '0', false));
            add(new Note('F', NATURAL, '0', false));
            add(new Note('F', SHARP, '0', false));
            add(new Note('G', NATURAL, '0', false));
            add(new Note('G', SHARP, '0', false));
        }
    };

    /* Step indexes. Represent the number of steps between each scale degree within each scale type. */
    private static final int[] MAJOR_INDEX = {Step.WHOLE, Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.WHOLE, Step.HALF};
    private static final int[] NAT_MINOR_INDEX = {Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE};
    private static final int[] HAR_MINOR_INDEX = {Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.HALF, Step.MINOR_THIRD, Step.HALF};
    private static final int[] MEL_MINOR_INDEX = {Step.WHOLE, Step.HALF, Step.WHOLE, Step.WHOLE, Step.WHOLE, Step.WHOLE, Step.HALF};

    /**
     * Finds the index of a note within the chromatic scale.
     *
     * @param note the note to find
     * @return the index of the note within the chromatic scale
     */
    public static int findNoteInList(Note note) {

        for (int i = 0; i < NUM_NOTES; ++i) {
            if (note.bAreNotesEqual(CHROMATIC_SCALE.get(i))) {
                return i;
            }
        }

        return ChromaticScaleDegree.C;
    }

    /**
     * Determines the number of half steps between the two notes passed in.
     *
     * @param srcNote the note to start at
     * @param destNote the note to end at
     * @return the number of half steps (distance) between the two notes
     */
    public static int findDistBtwNotes(Note srcNote, Note destNote) {

        int index1 = -1;
        int index2 = -1;
        int dist;

        for (int i = 0; i < NUM_NOTES; ++i) {
            if (srcNote.bAreNotesEqual(CHROMATIC_SCALE.get(i))) {
                index1 = i;
            }
            if (destNote.bAreNotesEqual(CHROMATIC_SCALE.get(i))) {
                index2 = i;
            }
        }

        if (index1 <= index2) {
            dist = index2 - index1;
        }
        else {
            dist = (NUM_NOTES - index1) + index2;
        }

        return dist;
    }

    /**
     * Calls the addSteps function with the correct step index based on the scale type.
     *
     * @param scaleType the scale type
     * @param noteDestIndex the note index to reach
     * @param chordRootIndex the chord root index
     * @return the number of half steps between the chord root and the destination note
     */
    private static int findStep (ScaleType scaleType, int noteDestIndex, int chordRootIndex) {

        switch (scaleType) {
            case MAJOR:
                return addSteps(MAJOR_INDEX, noteDestIndex, chordRootIndex);
            case NATURAL_MINOR:
                return addSteps(NAT_MINOR_INDEX, noteDestIndex, chordRootIndex);
            case HARMONIC_MINOR:
                return addSteps(HAR_MINOR_INDEX, noteDestIndex, chordRootIndex);
            case MELODIC_MINOR:
                return addSteps(MEL_MINOR_INDEX, noteDestIndex, chordRootIndex);
        }

        return Step.WHOLE;
    }

    /**
     * Adds up the number of half steps between the chord root and the destination note.
     *
     * @param indexScale the index scale (dependant upon scale the scale type)
     * @param noteDestIndex the note index to reach
     * @param chordRootIndex the chord root index
     * @return the number of half steps between the chord root and the destination note
     */
    private static int addSteps (int[] indexScale, int noteDestIndex, int chordRootIndex) {
        int step = Step.ROOT;

        if (0 == chordRootIndex && 0 == noteDestIndex)
        {
            step = 0;
        }
        else
        {
            if (0 == chordRootIndex) {
                ++chordRootIndex;
            }

            if (noteDestIndex > NUM_SCALE_STEPS) {
                for (int i = chordRootIndex - 1; i < NUM_SCALE_STEPS; ++i) {
                    step += indexScale[i];
                }
                noteDestIndex %= NUM_SCALE_STEPS;
                chordRootIndex = 1;
            }

            for (int i = chordRootIndex - 1; i <= noteDestIndex - 1; ++i) {
                step += indexScale[i];
            }
        }

        return step;
    }

    /**
     * Determines the note within the chromatic scale based on the scale degree of the desired note.
     *
     * @param key the key
     * @param scaleType the scale type
     * @param scaleDegree the scale degree of the desired note
     * @return the desired note within the chromatic scale
     */
    public static Note getNoteInScale (Note key, ScaleType scaleType, int scaleDegree) {

        return new Note(CHROMATIC_SCALE.get((findNoteInList(key) + findStep(scaleType, scaleDegree, ScaleDegree.TONIC)) % NUM_NOTES));
    }

    /**
     * Determines the note that is a third up from a root note.
     *
     * @param root the root note
     * @param scaleType the scale type
     * @param scaleDegree the scale degree of the root note
     * @return the note a third up from the root note
     */
    public static Note getThird (Note root, Scale.ScaleType scaleType, int scaleDegree) {

        int steps = findStep(scaleType, scaleDegree + ScaleDegree.MEDIANT, scaleDegree + 1);

        return new Note(CHROMATIC_SCALE.get((findNoteInList(root) + steps) % NUM_NOTES));
    }

    /**
     * Determines the note that is a fifth up from a root note.
     *
     * @param root the root note
     * @param scaleType the scale type
     * @param scaleDegree the scale degree of the root note
     * @return the note a fifth up from the root note
     */
    public static Note getFifth (Note root, Scale.ScaleType scaleType, int scaleDegree) {

        int steps = findStep(scaleType, scaleDegree + ScaleDegree.DOMINANT, scaleDegree + 1);

        return new Note(CHROMATIC_SCALE.get((findNoteInList(root) + steps) % NUM_NOTES));
    }

    /**
     * Determines the note a fifth down from the root note.
     *
     * @param root the root note
     * @param scaleType the scale type
     * @return the note a fifth down from the root note
     */
    public static Note getRootFromFifth (Note root, Scale.ScaleType scaleType) {

        int steps = 0, num;

        for (int i = 3; i >= 0; --i) {
            switch (scaleType) {
                case MAJOR:
                    steps += MAJOR_INDEX[i];
                    break;
                case NATURAL_MINOR:
                    steps += NAT_MINOR_INDEX[i];
                    break;
                case HARMONIC_MINOR:
                    steps += HAR_MINOR_INDEX[i];
                    break;
                case MELODIC_MINOR:
                    steps += MEL_MINOR_INDEX[i];
                    break;
            }
        }

        num = findNoteInList(root) - steps;

        if (num < 0) {
            num = NUM_NOTES + num;
        }

        return new Note(CHROMATIC_SCALE.get(num));
    }

    /**
     * Determines the note a minor seventh up from a root note.
     *
     * @param root the note to base the minor seventh off of
     * @return the note a minor seventh up from the root
     */
    public static Note getMinorSeventh (Note root) {

        return new Note(CHROMATIC_SCALE.get((findNoteInList(root) + NUM_STEPS_MINOR_SEVENTH) % NUM_NOTES));
    }

    /**
     * Determines the scale degree based on the key, the scale type, and the note.
     *
     * @param key the key
     * @param note the note to get the scale degree of
     * @param scaleType the scale type
     * @return the scale degree
     */
    public static int getScaleDegreeFromNote(Note key, Note note, Scale.ScaleType scaleType) {
        int dist = Scale.findDistBtwNotes(key, note);

        switch (scaleType) {
            case MAJOR:
                return getMajScaleDegreeFromDist(dist);

            case NATURAL_MINOR:
                return getNatMinScaleDegreeFromDist(dist);

            case HARMONIC_MINOR:
                return getHarMinScaleDegreeFromDist(dist);

            case MELODIC_MINOR:
                return getMelMinScaleDegreeFromDist(dist);

            default:
                return ScaleDegree.NON_SCALE_TONE;
        }
    }

    /**
     * Determines the scale degree within a major scale based on the distance between the // TODO: fix magic consts in these four functs
     * root note of the scale and another note.
     *
     * @param dist the distance between the root and another note
     * @return the scale degree
     */
    private static int getMajScaleDegreeFromDist (int dist) {
        switch (dist) {
            case 0:
                return ScaleDegree.TONIC;

            case 2:
                return ScaleDegree.SUPERTONIC;

            case 4:
                return ScaleDegree.MEDIANT;

            case 5:
                return ScaleDegree.SUBDOMINANT;

            case 7:
                return ScaleDegree.DOMINANT;

            case 9:
                return ScaleDegree.SUBMEDIANT;

            case 11:
                return ScaleDegree.SUBTONIC;

            default:
                return ScaleDegree.NON_SCALE_TONE;
        }
    }

    /**
     * Determines the scale degree within a natural minor scale based on the distance between the
     * root note of the scale and another note.
     *
     * @param dist the distance between the root and another note
     * @return the scale degree
     */
    private static int getNatMinScaleDegreeFromDist (int dist) {
        switch (dist) {
            case 0:
                return ScaleDegree.TONIC;

            case 2:
                return ScaleDegree.SUPERTONIC;

            case 3:
                return ScaleDegree.MEDIANT;

            case 5:
                return ScaleDegree.SUBDOMINANT;

            case 7:
                return ScaleDegree.DOMINANT;

            case 8:
                return ScaleDegree.SUBMEDIANT;

            case 10:
                return ScaleDegree.SUBTONIC;

            default:
                return ScaleDegree.NON_SCALE_TONE;
        }
    }

    /**
     * Determines the scale degree within a harmonic minor scale based on the distance between the
     * root note of the scale and another note.
     *
     * @param dist the distance between the root and another note
     * @return the scale degree
     */
    private static int getHarMinScaleDegreeFromDist (int dist) {
        switch (dist) {
            case 0:
                return ScaleDegree.TONIC;

            case 2:
                return ScaleDegree.SUPERTONIC;

            case 3:
                return ScaleDegree.MEDIANT;

            case 5:
                return ScaleDegree.SUBDOMINANT;

            case 7:
                return ScaleDegree.DOMINANT;

            case 8:
                return ScaleDegree.SUBMEDIANT;

            case 11:
                return ScaleDegree.SUBTONIC;

            default:
                return ScaleDegree.NON_SCALE_TONE;
        }
    }

    /**
     * Determines the scale degree within a melodic minor scale based on the distance between the
     * root note of the scale and another note.
     *
     * @param dist the distance between the root and another note
     * @return the scale degree
     */
    private static int getMelMinScaleDegreeFromDist (int dist) {
        switch (dist) {
            case 0:
                return ScaleDegree.TONIC;

            case 2:
                return ScaleDegree.SUPERTONIC;

            case 3:
                return ScaleDegree.MEDIANT;

            case 5:
                return ScaleDegree.SUBDOMINANT;

            case 7:
                return ScaleDegree.DOMINANT;

            case 9:
                return ScaleDegree.SUBMEDIANT;

            case 11:
                return ScaleDegree.SUBTONIC;

            default:
                return ScaleDegree.NON_SCALE_TONE;
        }
    }
}

