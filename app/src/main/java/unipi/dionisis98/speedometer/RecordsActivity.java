package unipi.dionisis98.speedometer;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
        String order_by_date = " ORDER BY timestamp DESC";

        map.put("Show All","");
        map.put("Today",filter_today);
        map.put("This Week",filter_last_week);
        map.put("This Month",filter_last_month);

        map.put("By Speed",order_by_speed);
        map.put("Latest",order_by_date);
        String got = (map.get(selected)+map.get(ordered));

        Cursor cursor = db.rawQuery(query_all+map.get(selected)+map.get(ordered) , null);
        //Cursor All = db.rawQuery("SELECT * FROM Records", null);

        if (cursor.getCount() == 0)
            Toast.makeText(this, "No records found", Toast.LENGTH_LONG).show();
        else {

            while (cursor.moveToNext()) {
                StringBuffer buffer = new StringBuffer();
                buffer.append(cursor.getString(0));
                buffer.append("\n");
                buffer.append(cursor.getString(1));
                buffer.append("\n");
                buffer.append(cursor.getString(2));
                buffer.append("\n");
                //SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss " );
                //String dateString = formatter.format(new Date(Long.parseLong(cursor.getString(3))));
                //Long test = Long.parseLong(cursor.getString(3));
                buffer.append(cursor.getString(3));
                buffer.append("\n");
                arrayList.add(buffer.toString());
            }
            Toast.makeText(this, "DONE", Toast.LENGTH_LONG).show();
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(adapter);

        }
    }
}
