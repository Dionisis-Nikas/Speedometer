package unipi.dionisis98.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {
    private Button speedometerButton;
    private Button settingsButton;
    private Button recordsButton;
    private Button speechButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        speedometerButton = findViewById(R.id.speedometer_button);
        settingsButton = findViewById(R.id.settings_button);
        recordsButton = findViewById(R.id.records_button);
        speechButton = findViewById(R.id.speech_button);


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

    }
}
