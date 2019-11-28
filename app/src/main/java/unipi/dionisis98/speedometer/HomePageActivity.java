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

public class HomePageActivity extends AppCompatActivity {
    private Button speedometerButton;
    private Button settingsButton;
    private Button recordsButton;
    private Button speechButton;
    private Intent intentSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        speedometerButton = findViewById(R.id.speedometer_button);
        settingsButton = findViewById(R.id.settings_button);
        recordsButton = findViewById(R.id.records_button);
        speechButton = findViewById(R.id.speech_button);

        intentSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        speedometerButton.setOnClickListener(v -> openSpeedometer());

        settingsButton.setOnClickListener(v -> openSettings());

        recordsButton.setOnClickListener(v -> openRecords());

        speechButton.setOnClickListener(v -> openSpeech());
    }

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
    public void openSpeech(){
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intentSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        startActivityForResult(intentSpeech, 10);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10 && resultCode==RESULT_OK){
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            if (results!= null) {
                String s = results.get(0);
                s = s.toUpperCase();
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
                    case "EXIT":
                        finish();
                        break;

                }
            }
            else {
                showMessage("Try Again", "No speech recognized");
            }
        } else {
            Toast.makeText(getApplicationContext(), "Failed to recognize speech!", Toast.LENGTH_LONG).show();
        }
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title)
                .setMessage(message);
        builder.show();
    }
}
