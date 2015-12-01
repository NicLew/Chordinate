package edu.pacificu.chordinate.chordinate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CompositionsActivity extends AppCompatActivity {

    TextView tv;
    private CompositionsAdapter mAdapter;
    private ArrayList<SavedComposition> mSavedFiles = new ArrayList<SavedComposition>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compositions);

        SavedComposition current;
        mAdapter = new CompositionsAdapter(this, mSavedFiles);

        String[] files;
        files = this.getFilesDir().list();
        String line = "";
        try {
            for (int i = 0; i < files.length; i ++)
            {
                if (files[i].endsWith(".chd")) {
                    InputStreamReader isr = new InputStreamReader(openFileInput(files[i]));
                    BufferedReader bReader = new BufferedReader(isr);

                    //TODO: Read info from file and add to array and notify adapter!
                    line = line.concat(files[i] + "\n" + bReader.readLine() + "\n");
                }
            }

            //tv = (TextView) findViewById(R.id.textView2);
            //tv.setText(line);
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
