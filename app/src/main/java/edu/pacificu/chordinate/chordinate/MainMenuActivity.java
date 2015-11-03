package edu.pacificu.chordinate.chordinate;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainMenuActivity extends AppCompatActivity {
    Button keyboardButton;
    Button recordButton;
    Button compsButton;
    Button instrButton;
    Button settingsButton;
    Button aboutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        keyboardButton = (Button) findViewById(R.id.keyboardButton);
        keyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onKeyboardButtonClick(v);
            }
        });

        recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onRecordButtonClick(v);
            }
        });

        compsButton = (Button) findViewById(R.id.compsButton);
        compsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onCompsButtonClick(v);
            }
        });

        instrButton = (Button) findViewById(R.id.instrButton);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onInstrButtonClick(v);
            }
        });

        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onSettingsButtonClick(v);
            }
        });

        aboutButton = (Button) findViewById(R.id.aboutButton);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onAboutButtonClick(v);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onKeyboardButtonClick(View view) {
        view.setBackgroundResource(R.drawable.top_button_press);
        Intent myIntent = new Intent(MainMenuActivity.this, KeyboardActivity.class);
        startActivity(myIntent);
    }

    public void onRecordButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button1_press);
        Intent myIntent = new Intent(MainMenuActivity.this, RecordActivity.class);
        startActivity(myIntent);
    }

    public void onCompsButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button2_press);
        //Intent myIntent = new Intent(MainMenuActivity.this, NewActivity.class);
        //startActivity(myIntent);
    }

    public void onInstrButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button3_press);
        //Intent myIntent = new Intent(MainMenuActivity.this, NewActivity.class);
        //startActivity(myIntent);
    }

    public void onSettingsButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button4_press);
        //Intent myIntent = new Intent(MainMenuActivity.this, NewActivity.class);
        //startActivity(myIntent);
    }

    public void onAboutButtonClick(View view) {
        view.setBackgroundResource(R.drawable.bottom_button_press);
        Intent myIntent = new Intent(MainMenuActivity.this, AboutActivity.class);
        startActivity(myIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        keyboardButton.setBackgroundResource(R.drawable.top_button);
        recordButton.setBackgroundResource(R.drawable.middle_button1);
        compsButton.setBackgroundResource(R.drawable.middle_button2);
        instrButton.setBackgroundResource(R.drawable.middle_button3);
        settingsButton.setBackgroundResource(R.drawable.middle_button4);
        aboutButton.setBackgroundResource(R.drawable.bottom_button);
    }
}
