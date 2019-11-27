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

        speedometerButton = (Button) findViewById(R.id.speedometer_button);
        settingsButton = (Button) findViewById(R.id.settings_button);
        recordsButton = (Button) findViewById(R.id.records_button);
        speechButton = (Button) findViewById(R.id.speech_button);

        speedometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpeedometer();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRecords();
            }
        });

        speechButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSpeech();
            }
        });
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
        Intent intent = new Intent(this,SpeedometerActivity.class);
        startActivity(intent);
    }
}
