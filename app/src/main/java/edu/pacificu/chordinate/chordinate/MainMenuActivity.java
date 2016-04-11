package edu.pacificu.chordinate.chordinate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class MainMenuActivity extends AppCompatActivity {
    Button mKeyboardButton;
    Button mRecordButton;
    Button mCompsButton;
    Button mInstrButton;
    Button mSettingsButton;
    Button mAboutButton;

    /**
     * Sets the content view and creates the main menu buttons.
     *
     * @param   savedInstanceState  The instance state to be created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        initMenuBtns();
    }

    /**
     * Starts the keyboard activity.
     *
     * @param view  The view to be used.
     */
    public void onKeyboardButtonClick(View view) {
        view.setBackgroundResource(R.drawable.top_button_press);
        Intent myIntent = new Intent(MainMenuActivity.this, KeyboardActivity.class);
        startActivity(myIntent);
    }


    /**
     * Starts the record activity.
     *
     * @param view  The view to be used.
     */
    public void onRecordButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button1_press);
        Intent myIntent = new Intent(MainMenuActivity.this, RecordActivity.class);
        startActivity(myIntent);
    }


    /**
     * Starts the saved compositions activity.
     *
     * @param view  The view to be used.
     */
    public void onCompsButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button2_press);
        Intent myIntent = new Intent(MainMenuActivity.this, CompositionsActivity.class);
        startActivity(myIntent);
    }


    /**
     * Starts the instructions activity.
     *
     * @param view  The view to be used.
     */
    public void onInstrButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button3_press);
        Intent myIntent = new Intent(MainMenuActivity.this, InstructionsActivity.class);
        startActivity(myIntent);
    }


    /**
     * Starts the settings activity.
     *
     * @param view  The view to be used.
     */
    public void onSettingsButtonClick(View view) {
        view.setBackgroundResource(R.drawable.middle_button4_press);
        Intent myIntent = new Intent(MainMenuActivity.this, SettingsActivity.class);
        startActivity(myIntent);
    }

    /**
     * Starts the about activity.
     *
     * @param view  The view to be used.
     */
    public void onAboutButtonClick(View view) {
        view.setBackgroundResource(R.drawable.bottom_button_press);
        Intent myIntent = new Intent(MainMenuActivity.this, AboutActivity.class);
        startActivity(myIntent);
    }

    /**
     * Resets all of the button backgrounds back to their original images.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        mKeyboardButton.setBackgroundResource(R.drawable.top_button);
        mRecordButton.setBackgroundResource(R.drawable.middle_button1);
        mCompsButton.setBackgroundResource(R.drawable.middle_button2);
        mInstrButton.setBackgroundResource(R.drawable.middle_button3);
        mSettingsButton.setBackgroundResource(R.drawable.middle_button4);
        mAboutButton.setBackgroundResource(R.drawable.bottom_button);
    }

    /**
     * Initializes the menu's keyboard button and sets it's onClick behavior.
     */
    private void initKeyboardBtn () {
        mKeyboardButton = (Button) findViewById(R.id.keyboardButton);
        mKeyboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onKeyboardButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's record button and sets it's onClick behavior.
     */
    private void initRecordBtn () {
        mRecordButton = (Button) findViewById(R.id.recordMenuButton);
        mRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onRecordButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's saved compositions button and sets it's onClick behavior.
     */
    private void initCompsBtn () {
        mCompsButton = (Button) findViewById(R.id.compsButton);
        mCompsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onCompsButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's instructions button and sets it's onClick behavior.
     */
    private void initInstrBtn () {
        mInstrButton = (Button) findViewById(R.id.instrButton);
        mInstrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onInstrButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's settings button and sets it's onClick behavior.
     */
    private void initSettingsBtn () {
        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onSettingsButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's about button and sets it's onClick behavior.
     */
    private void initAboutBtn () {
        mAboutButton = (Button) findViewById(R.id.aboutButton);
        mAboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainMenuActivity.this.onAboutButtonClick(v);
            }
        });
    }

    /**
     * Initializes the menu's buttons.
     */
    private void initMenuBtns () {
        initKeyboardBtn();
        initRecordBtn();
        initCompsBtn();
        initInstrBtn();
        initSettingsBtn();
        initAboutBtn();
    }

    /**
     * Overrides onBackPressed to close the app when the back button is pressed, instead of
     * returning to the load screen activity
     */
    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent (Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
