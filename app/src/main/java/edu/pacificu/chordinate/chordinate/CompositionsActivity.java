package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CompositionsActivity extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);
        String[] files;
        files = this.getFilesDir().list();
        String line = "nope";
        try {
            InputStreamReader isr = new InputStreamReader(openFileInput("NewRecording.chd"));
            BufferedReader bReader = new BufferedReader(isr);
            line = bReader.readLine();
            tv = (TextView) findViewById(R.id.textView2);
            tv.setText(line);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
