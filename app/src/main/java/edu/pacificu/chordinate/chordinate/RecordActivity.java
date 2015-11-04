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


public class RecordActivity extends AppCompatActivity {
    Button recordButton;
    Button playButton;
    boolean bIsRecording = false;
    MediaRecorder mRecorder = new MediaRecorder();
    String mFileName;
    int numRec = 0;

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
        //String fileName = "sampleOutput";
        ++numRec;
        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/audiorecordtest"+numRec+".3gp";

        if (!bIsRecording)
        {
            bIsRecording = true;
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                //Log.e(LOG_TAG, "prepare() failed");
            }

            mRecorder.start();
            Toast.makeText(getApplicationContext(), "Recording started.", Toast.LENGTH_LONG).show();
        }
        else
        {
            bIsRecording = false;
            mRecorder.stop();
            mRecorder.reset();
            mRecorder.release();
            Toast.makeText(getApplicationContext(), "Recording stopped.", Toast.LENGTH_LONG).show();
        }
    }

    public void onPlayButtonClick(View view) {
        // call java classes
    }
}