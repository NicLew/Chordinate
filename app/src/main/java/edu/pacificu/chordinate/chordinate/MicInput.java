package edu.pacificu.chordinate.chordinate;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;

public class MicInput {
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private long mStartTime;
    private long mEndTime;

    /**
     * Constructor for MicInput.
     */
    MicInput() {
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
     * @param current The recording to start.
     */
    public void startRecording (SavedRecording current) {

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(current.getFileName());
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
     * @param current The recording to stop.
     */
    public void stopRecording (SavedRecording current) {
        mRecorder.stop();
        mEndTime = System.currentTimeMillis();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        current.setLength(mEndTime - mStartTime);
    }

    /**
     * Stops playback and resets the media player.
     */
    private void stopPlayback () {
        mPlayer.release();
        mPlayer = null;
    }
}