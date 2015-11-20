/*
package edu.pacificu.chordinate.chordinate;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;

public class InputMelody {
   */
/* private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private boolean mbIsRecording;
    private boolean mbIsPlaying;
    private long mStartTime;
    private long mEndTime;

    InputMelody() {
        mbIsPlaying = false;
        mbIsRecording = false;
    }

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

    public void playback (Button playButton, String fileName) {
        if (!mbIsPlaying)
        {
            startPlayback(fileName);
            playButton.setText(R.string.record_playback_button_stop);
        }
        else
        {
            stopPlayback();
            playButton.setText(R.string.record_playback_button_play);
        }
    }

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

    private void stopRecording (ArrayList<SavedRecording> savedFiles) {
        mbIsRecording = false;
        mRecorder.stop();
        mEndTime = System.currentTimeMillis();
        mRecorder.reset();
        mRecorder.release();
        mRecorder = null;
        savedFiles.get(savedFiles.size() - 1).setLength(mEndTime - mStartTime);
    }

    private void startPlayback (String fileName) {
        mPlayer = new MediaPlayer();

        try {
            mPlayer.setDataSource(fileName);
            mPlayer.prepare();
        } catch (IOException e) {
            Log.e("MediaPlayer", "prepare() failed");
        }

        mPlayer.start();
        mbIsPlaying = true;
    }

    private void stopPlayback () {
        mbIsPlaying = false;
        mPlayer.release();
        mPlayer = null;
    }*//*

}
*/
