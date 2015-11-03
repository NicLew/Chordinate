package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class RecordActivity extends AppCompatActivity {
    Button recordButton;
    Button playButton;

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

    public void onRecordButtonClick(View view) {
        MicInput input = new MicInput();
        input.startRecording();
    }

    public void onPlayButtonClick(View view) {
        // call java classes
    }
}