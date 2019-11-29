package unipi.dionisis98.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.TubeSpeedometer;

import java.util.Formatter;
import java.util.Locale;


/*
The speedometer activity. The activity where the location tracking and checks for exceeding
the speed limit take place
 */
public class SpeedometerActivity extends AppCompatActivity implements LocationListener {


    TubeSpeedometer tubeSpeedometer;//The gauge for a more clear represantation of our speed
    SQLiteDatabase db;//Our database
    SharedPreferences preferences;//Our shared preferences
    public static final String KEY_PREF_SPEED_LIMIT = "speedlimit";//The key to access the shared preference of our speed limit set by the user
    private TTS myTTS;//Our text to speech object for when the speed limit is exceeded

    private TextView warning;//The on-screen message of our speed limit warning
    private TextView speedText;//Our speed textview
    private String speed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedometer_activity);

        //Get the views of our objects
        tubeSpeedometer = findViewById(R.id.speedView);
        warning = findViewById(R.id.warning);
        speedText = findViewById(R.id.speed);

        //Get our prefernces
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Get our speed limit preference
        speed = preferences.getString(KEY_PREF_SPEED_LIMIT, "");

        //Set our min and max speed limits of the speedometer gauge
        tubeSpeedometer.setMaxSpeed(Integer.parseInt(speed));

        //Gauge effects settings
        tubeSpeedometer.speedTo(0);
        tubeSpeedometer.setWithEffects3D(true);
        tubeSpeedometer.setWithTremble(false);

        //Action bar back button and title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Speedometer");

        //Check for gps permission
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        else {
            //Start the programm if the permission is granted
            begin();
        }
        this.updateSpeed(null);
        tubeSpeedometer.speedTo(0);

        //Open our Records database but if it doesnt exist create it
        db = openOrCreateDatabase("Records",MODE_PRIVATE,null);
        db.delete("Records",null,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Records(longtitude DOUBLE, latitude DOUBLE, speed FLOAT, timestamp DATETIME);");

        //Our text to speech engine
        myTTS = new TTS(this);
    }


    //Back button pressed on actionBar override method
    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    //If location is changed override the method and update speed
    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            this.updateSpeed(location);
        }
    }

    //Required Override methods
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }



    //Once we have permmision initiate begin method which starts the GPS tracking
    @SuppressLint("MissingPermission")
    private void begin(){
        //Initiate our Location manager to get our location services
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            //Get our updates on location every 7 meters and 1.6 seconds for better battery management
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1600,7,this);
        }
    }

    //Our override method for the result of when the permissions dialog appears
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode) == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                begin();
            } else {
                finish();
            }
        }
    }

    //The updateSpeed method that gets our current speed and performs the required checks
    private void updateSpeed(Location location){
        float nCurrentSpeed = 0;
        if(location != null){
            //if location is not null then get the speed and convert it to Km/h
            nCurrentSpeed = location.getSpeed()*3.6f ;
            Float speedf = Float.parseFloat(speed);

            if (speedf < nCurrentSpeed){
                //If current speed is higher than our speed limit then insert the exceeded speed limit record to our database
                insert(location.getLongitude(),location.getLatitude(),nCurrentSpeed);

                //Make our on-screen warning message visible
                warning.setVisibility(View.VISIBLE);

                //Also make our text to speech engine say the warning message
                speakMessage("WARNING !! Speed limit exceeded");
            }
            else{
                //If speed is not higher than our speed limit hide the warning message
                warning.setVisibility(View.INVISIBLE);
            }
        }

        //Convert our current speed to a string and set it as text to our speed textview
        String strCurrentSpeed = String.valueOf(nCurrentSpeed);
        String speedString = strCurrentSpeed+" Km/h";
        speedText.setText(speedString);

        //Set our gauge speed to our current speed with 1 second animation interval
        tubeSpeedometer.speedTo(nCurrentSpeed);

    }

    public void insert(double longtitude,double latitude,float speed){
        //Insert our exceeded speed limit record in our database executing our query
        db.execSQL("INSERT INTO Records values" +
                "('"+longtitude
                +"','"
                +latitude
                +"','"
                + speed
                +"',"
                //+"'2019-11-11 10:00:00');"); // A test date for our dataset if we want to give older dates for testing

                //Our current date in localtime
                +"datetime('now','localtime'));");

    }

    //The method we use to make our TTS engine say a message
    public void speakMessage(String message){
        //Make our text to speech engine say our message
        myTTS.speak(message);
    }
}
