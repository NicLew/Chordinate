package edu.pacificu.chordinate.chordinate;

import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.util.Log;
import android.media.MediaPlayer;
import android.widget.Toast;

import java.io.IOException;
import java.util.Vector;

// button images: https://openclipart.org/detail/76183/playback-buttons


public class RecordActivity extends AppCompatActivity {
    Button recordButton;
    Button playButton;
    boolean bIsRecording = false;
    MediaRecorder mRecorder = new MediaRecorder();
    String mFileName;
    //int numRec = 0;

    private MediaPlayer mPlayer = new MediaPlayer();
    boolean bIsPlaying = false;

    // saved recordings part:
    //Vector<String> mSavedFiles; // Instead of "String" it will be user defined class "SavedRecording" OR "SavedFile"
    int mNumRec = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onRecordButtonClick(v);
            }
        });

        playButton = (Button) findViewById(R.id.playbackButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordActivity.this.onPlayButtonClick(v);
            }
        });
    }

    public void onRecordButtonClick(View view) { // change to use java class methods!!!!!
        //MicInput input = new MicInput();
        //input.startRecording();
        //MediaRecorder recorder = new MediaRecorder();
        String fileName = null;

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest"+mNumRec+".3gp";

//        fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//        fileName += "/audiorecordtest"+mNumRec+".3gp";
//        mSavedFiles.add(fileName);
        ++mNumRec;

        if (!bIsRecording)
        {
            bIsRecording = true;
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            //mRecorder.setOutputFile(mSavedFiles.get(mNumRec - 1));
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }

            mRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_LONG).show();
            recordButton.setText(R.string.record_rec_button_stop);
        }
        else
        {
            bIsRecording = false;
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            mRecorder = null;
            Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_LONG).show();
            recordButton.setText(R.string.record_rec_button_rec);
        }
    }

    public void onPlayButtonClick(View view) {
        //mPlayer = new MediaPlayer();

        if (!bIsPlaying)
        {
            try {
                //mPlayer.setDataSource(mSavedFiles.get(mNumRec - 1));
                mPlayer.setDataSource(mFileName);
                mPlayer.prepare();
                mPlayer.start();
                bIsPlaying = true;
                Toast.makeText(getApplicationContext(), "Playback started.", Toast.LENGTH_LONG).show();
                playButton.setText(R.string.record_playback_button_stop);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }
        }
        else
        {
            bIsPlaying = false;
            mPlayer.release();
            mPlayer = null;
            Toast.makeText(getApplicationContext(), "Playback stopped.", Toast.LENGTH_LONG).show();
            playButton.setText(R.string.record_playback_button_play);
        }
    }
}