package edu.pacificu.chordinate.chordinate;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by lewe4441 on 11/3/2015.
 */
public class InputMelody {
    private MediaRecorder mRecorder;
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
                //Log.e(LOG_TAG, "prepare() failed");
            }

            mStartTime = System.currentTimeMillis();
            mRecorder.start();
            recordButton.setText(R.string.record_rec_button_stop);
        }
        else
        {
            mbIsRecording = false;
            mRecorder.stop();
            mEndTime = System.currentTimeMillis();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            recordButton.setText(R.string.record_rec_button_rec);
            savedFiles.get(savedFiles.size() - 1).setLength(mEndTime - mStartTime);
        }
    }

    public void playback (Button playButton, String fileName) {
        if (!mbIsPlaying)
        {
            try {
                mPlayer = new MediaPlayer();
                mPlayer.setDataSource(fileName);
                mPlayer.prepare();
                mPlayer.start();
                mbIsPlaying = true;
                playButton.setText(R.string.record_playback_button_stop);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        }
        else
        {
            mbIsPlaying = false;
            mPlayer.release();
            mPlayer = null;
            playButton.setText(R.string.record_playback_button_play);
        }
    }
}
