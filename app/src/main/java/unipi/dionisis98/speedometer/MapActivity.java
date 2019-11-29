package unipi.dionisis98.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private GoogleMap gmap;
    private SQLiteDatabase db;
    private ArrayList<Coordinates> coordinatesArrayList;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        db = openOrCreateDatabase("Records", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Records(longtitude DOUBLE, latitude DOUBLE, speed FLOAT, timestamp DATETIME);");
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
        coordinatesArrayList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        gmap.setMinZoomPreference(14);
        getAllCoordinates();
        showAllCoordinates(coordinatesArrayList);


    }

    public void addMarker(LatLng position,String speed){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        gmap.addMarker(new MarkerOptions().position(position)
                .title("Speed: "+speed));
    }

    private void getAllCoordinates() {
        String query_all = "SELECT * FROM Records";
        Map<String, String> map = new HashMap<>();

        Cursor cursor = db.rawQuery(query_all , null);
        //Cursor All = db.rawQuery("SELECT * FROM Records", null);

        if (cursor.getCount() == 0)
            Toast.makeText(this, "No records found", Toast.LENGTH_LONG).show();
        else {
            coordinatesArrayList.clear();
            while (cursor.moveToNext()) {
                Coordinates coordinates = new Coordinates(cursor.getString(1),cursor.getString(0),cursor.getString(2));
                coordinatesArrayList.add(coordinates);
            }
        }
    }
    private void showAllCoordinates(ArrayList<Coordinates> coordinates){
        coordinates.forEach(coordinates1 -> {
            LatLng position = new LatLng(Double.parseDouble(coordinates1.getLatitude()),Double.parseDouble(coordinates1.getLongtitude()));
            addMarker(position,coordinates1.getSpeed());
            gmap.moveCamera(CameraUpdateFactory.newLatLng(position));
        });
    }
}

class Coordinates{

    private String longtitude;
    private String latitude;
    private String speed;

    public Coordinates(String latitude,String longtitude,String speed){
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.speed = speed;
    }
    public String getLongtitude(){
        return longtitude;
    }
    public String getLatitude(){
        return latitude;
    }
    public String getSpeed(){
        return speed;
    }
}
