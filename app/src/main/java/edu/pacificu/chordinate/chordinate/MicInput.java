package edu.pacificu.chordinate.chordinate;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class MicInput {
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private boolean mbIsRecording;
    private long mStartTime;
    private long mEndTime;

    /**
     * Constructor for MicInput. Initializes boolean for recording to false.
     */
    MicInput() {
        mbIsRecording = false;
    }

    /**
     * Starts recording or stops recording if a recording is already in process.
     *
     * @param savedFiles    The list of saved recordings.
     * @param recordButton  The record button that was pressed.
     */
    public void record (ArrayList<SavedRecording> savedFiles, Button recordButton) {
        if (!mbIsRecording)
        {
            startRecording(savedFiles);
            recordButton.setText(R.string.record_rec_button_stop);
        }
        else
        {
            stopRecording(savedFiles);
            recordButton.setText(R.string.record_rec_button_rec);
        }
    }

    /**
     * Starts playback or stops playback if a playback is already in process.
     *
     * @param playButton    The play button that was pressed.
     * @param fileName      The name of the file to be played from.
     */
    public void playback (Button playButton, String fileName) {

        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }

        mPlayer.start();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                stopPlayback();
            }
        });
    }

    /**
     * Sets up the media recorder and starts recording.
     *
     * @param savedFiles    The list of saved recordings.
     */
    private void startRecording (ArrayList<SavedRecording> savedFiles) {
        savedFiles.add(new SavedRecording(savedFiles.size()));

        mbIsRecording = true;

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(savedFiles.get(savedFiles.size() - 1).getFileName());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("MediaRecorder", "prepare() failed");
        }

        mStartTime = System.currentTimeMillis();
        mRecorder.start();
    }

    /**
     * Stops recording and resets the media recorder.
     *
     * @param savedFiles    The list of saved recordings.
     */
    private void stopRecording (ArrayList<SavedRecording> savedFiles) {
        mbIsRecording = false;
        mRecorder.stop();
        mEndTime = System.currentTimeMillis();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        savedFiles.get(savedFiles.size() - 1).setLength(mEndTime - mStartTime);
    }

    /**
     * Stops playback and resets the media player.
     */
    private void stopPlayback () {
        mPlayer.release();
        mPlayer = null;
    }
}