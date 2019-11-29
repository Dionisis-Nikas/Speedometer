package unipi.dionisis98.speedometer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RecordsActivity extends AppCompatActivity {


    private SQLiteDatabase db;
    private ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter adapter;
    Spinner filter;
    Spinner orderFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        listView = findViewById(R.id.listview);
        filter = findViewById(R.id.spinner1);
        orderFilter = findViewById(R.id.spinner2);
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String selected = parentView.getItemAtPosition(position).toString();
                String ordered = orderFilter.getSelectedItem().toString();
                selectRecords(selected,ordered);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        orderFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                String ordered = parentView.getItemAtPosition(position).toString();
                String selected = filter.getSelectedItem().toString();
                selectRecords(selected,ordered);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        arrayList = new ArrayList<>();
        db = openOrCreateDatabase("Records", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Records(longtitude DOUBLE, latitude DOUBLE, speed FLOAT, timestamp DATETIME);");
        selectRecords("Show All","Latest");
    }


    private void selectRecords(String selected,String ordered) {
        String query_all = "SELECT * FROM Records";
        Map<String, String> map = new HashMap<>();


        String filter_today = " WHERE timestamp >= datetime('now', 'start of day')";
        String filter_last_week = " WHERE timestamp >= datetime('now', '-6 days')";
        String filter_last_month = " WHERE timestamp >= datetime('now', 'start of month')";

        String order_by_speed = " ORDER BY speed DESC";
        String order_by_date_ASC = " ORDER BY timestamp ASC";
        String order_by_date_DESC = " ORDER BY timestamp DESC";

        map.put("Show All","");
        map.put("Today",filter_today);
        map.put("This Week",filter_last_week);
        map.put("This Month",filter_last_month);

        map.put("Fastest",order_by_speed);
        map.put("Oldest",order_by_date_ASC);
        map.put("Latest",order_by_date_DESC);
        String got = (map.get(selected)+map.get(ordered));

        Cursor cursor = db.rawQuery(query_all+map.get(selected)+map.get(ordered) , null);
        //Cursor All = db.rawQuery("SELECT * FROM Records", null);

        if (cursor.getCount() == 0)
            Toast.makeText(this, "No records found", Toast.LENGTH_LONG).show();
        else {
            arrayList.clear();
            while (cursor.moveToNext()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("Date: "+cursor.getString(3));
                buffer.append("\n");
                buffer.append("Speed: "+cursor.getString(2)+"km/h");
                buffer.append("\n");
                buffer.append("Longtitude: "+cursor.getString(0));
                buffer.append("\n");
                buffer.append("Latitude: "+cursor.getString(1));
                buffer.append("\n");

                arrayList.add(buffer.toString());
            }
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(null);
            listView.setAdapter(adapter);
            listView.refreshDrawableState();
            Toast.makeText(this,selected+ordered,Toast.LENGTH_SHORT).show();

        }
    }
}
