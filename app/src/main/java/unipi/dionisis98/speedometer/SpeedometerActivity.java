package unipi.dionisis98.speedometer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.anastr.speedviewlib.TubeSpeedometer;

import java.util.Formatter;
import java.util.Locale;

public class SpeedometerActivity extends AppCompatActivity implements LocationListener {


    TubeSpeedometer tubeSpeedometer;
    SQLiteDatabase db;
    SharedPreferences preferences;
    public static final String KEY_PREF_SPEED_LIMIT = "speedlimit";
    public static final String KEY_PREF_METRIC = "metrics";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speedometer_activity);

        tubeSpeedometer = findViewById(R.id.speedView);

        tubeSpeedometer.setMinSpeed(0);
        tubeSpeedometer.setMaxSpeed(300);
        tubeSpeedometer.setWithEffects3D(true);
        tubeSpeedometer.setWithTremble(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);



        //check for gps permission
        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        else {
            //start the programm if the permission is granted
            doStuff();
        }
        this.updateSpeed(null);


        db = openOrCreateDatabase("Records",MODE_PRIVATE,null);
        //db.delete("Records",null,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Records(longtitude DOUBLE, latitude DOUBLE, speed FLOAT, timestamp DATETIME);");
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            CLocation myLocation = new CLocation(location, false);
            this.updateSpeed(myLocation);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @SuppressLint("MissingPermission")
    private void doStuff(){
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
        }
        Toast.makeText(this, "Waiting for GPS", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((requestCode) == 1000){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                doStuff();
            } else {
                finish();
            }
        }
    }

    private void updateSpeed(CLocation location){
        float nCurrentSpeed = 0;
        float nCurrentAccuracy = 0;
        String speed = preferences.getString(KEY_PREF_SPEED_LIMIT, "");
        //Toast.makeText(this,speed,Toast.LENGTH_LONG).show();
        Long ts = System.currentTimeMillis();


        if(location != null){
            location.setUserMetricUnits(false);
            nCurrentSpeed = location.getSpeed();
            Float speedf = Float.parseFloat(speed);
            if (speedf <= nCurrentSpeed){
                insert(location.getLongitude(),location.getLatitude(),nCurrentSpeed,ts);
            }
            nCurrentAccuracy = location.getAccuracy();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US,"%5.1f",nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ","0");


        tubeSpeedometer.speedTo(nCurrentSpeed);

    }

    public void insert(double longtitude,double latitude,float speed,long ts){
        db.execSQL("INSERT INTO Records values" +
                "('"+longtitude
                +"','"
                +latitude
                +"','"
                + speed
                +"',"
                +"'2019-11-11 10:00:00');");
                //+"datetime('now','-6days','localtime'));");
        Toast.makeText(this,"Done!",Toast.LENGTH_SHORT).show();
    }


}
