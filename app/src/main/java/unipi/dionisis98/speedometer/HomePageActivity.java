package unipi.dionisis98.speedometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/* This is our home page activity. Basically this is our main menu where we can choose any of the
    other activities of the app
 */
public class HomePageActivity extends AppCompatActivity {

    //Our menu buttons for the other activities
    private Button speedometerButton;
    private Button settingsButton;
    private Button recordsButton;
    private Button speechButton;
    private Button mapButton;

    //The speech recognition intent
    private Intent intentSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Get our buttons based on their id
        speedometerButton = findViewById(R.id.speedometer_button);
        settingsButton = findViewById(R.id.settings_button);
        recordsButton = findViewById(R.id.records_button);
        speechButton = findViewById(R.id.speech_button);
        mapButton = findViewById(R.id.map_button);

        //Initialize our speech recognition intent object
        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //Menu button Click Listeners
        speedometerButton.setOnClickListener(v -> openSpeedometer());
        settingsButton.setOnClickListener(v -> openSettings());
        recordsButton.setOnClickListener(v -> openRecords());
        speechButton.setOnClickListener(v -> openSpeech());
        mapButton.setOnClickListener(v -> openRecordsOnMap());
    }

    /*
    These are the methods that are being called when the user clicks one of the menu buttons
    to create an Intent of the new Activity the user selected so it can start this activity
     */
    public void openSpeedometer(){
        Intent intent = new Intent(this,SpeedometerActivity.class);
        startActivity(intent);
    }
    public void openSettings(){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }
    public void openRecords(){
        Intent intent = new Intent(this,RecordsActivity.class);
        startActivity(intent);
    }
    public void openRecordsOnMap(){
        Intent intent = new Intent(this,MapActivity.class);
        startActivity(intent);
    }

    /*
    This method is being called when the user clicks the Speech Recognition button and
    sets the settings and attributes of our Speech Recogniser before starting it
     */
    public void openSpeech(){
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        //set our request code to 10 so we can identify it later in the onActivityResult method
        startActivityForResult(intentSpeech, 10);
    }

    /*
    This override method is being called when the speech recognizer collects our voice recording data and
    gives the results of what it believes is the best matches
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10 && resultCode==RESULT_OK){
            //Get our results in a string array list
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results!= null) {

                //Only get the best match result (the first result in our array list
                String s = results.get(0);

                //Convert it to upper case for easier comparision of our switch cases
                s = s.toUpperCase();

                //Every case is a different activity
                //When the user says one of them with the word OPEN the corresponding activity will open
                switch (s) {
                    case "OPEN SPEEDOMETER":
                        openSpeedometer();
                        break;
                    case "OPEN SETTINGS":
                        openSettings();
                        break;
                    case "OPEN SPEED RECORDS":
                        openRecords();
                        break;

                        //Added 2 cases on the speed records map because the on word is often not said
                    case "OPEN SPEED RECORDS ON MAP":
                        openRecordsOnMap();
                        break;
                    case "OPEN SPEED RECORDS MAP":
                        openRecordsOnMap();
                        break;


                    case "EXIT":
                        finish();
                        break;

                }
            }
            else {
                //if no speech was recognised then show an alert dialog message
                showMessage("Try Again", "No speech recognized");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }

    //the method to show our alert dialog message
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title)
                .setMessage(message);
        builder.show();
    }
}
