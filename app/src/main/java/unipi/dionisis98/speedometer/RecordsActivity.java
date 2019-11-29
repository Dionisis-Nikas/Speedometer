package unipi.dionisis98.speedometer;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecordsActivity extends AppCompatActivity {


    private SQLiteDatabase db;//Our database
    private ListView listView;//Our listview
    ArrayList<String> arrayList;//The array list that will have the records from the database
    ArrayAdapter adapter;//the adapter to put the array list data to our listview

    Spinner filter;//The filter drop down
    Spinner orderFilter;//The order by drop down

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        //Get our objects based on their ids
        listView = findViewById(R.id.listview);
        filter = findViewById(R.id.spinner1);
        orderFilter = findViewById(R.id.spinner2);

        /*
        Set the item selected listeners for each of the two spinners objects so when the user selects
        a different filter or order filter the data will displayed based on them
         */
        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {
                //Get the selected filter value
                String selected = parentView.getItemAtPosition(position).toString();
                //Keep the selected order filter value
                String ordered = orderFilter.getSelectedItem().toString();
                //Select the records based on the selected filter
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
                //Get the selected order filter value
                String ordered = parentView.getItemAtPosition(position).toString();
                //Keep the selected filter value
                String selected = filter.getSelectedItem().toString();
                //Select the records based on the selected order filter
                selectRecords(selected,ordered);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });


        //Initiate our array list and our database (if not exists)
        arrayList = new ArrayList<>();
        db = openOrCreateDatabase("Records", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Records(longtitude DOUBLE, latitude DOUBLE, speed FLOAT, timestamp DATETIME);");

        //Show all records as default
        selectRecords("Show All","Latest");
    }

/*
This method is where we run the querys to our database to return the corresponding results.
Based on the filters we chose the querys will be modified
 */
    private void selectRecords(String selected,String ordered) {

        //A query to select everything from our table records
        String query_all = "SELECT * FROM Records";

        //Create a map with our dictionary for our filters and the querys
        Map<String, String> map = new HashMap<>();

        //The filter string querys
        String filter_today = " WHERE timestamp >= datetime('now', 'start of day')";
        String filter_last_week = " WHERE timestamp >= datetime('now', '-6 days')";
        String filter_last_month = " WHERE timestamp >= datetime('now', 'start of month')";

        //The order filter string querys
        String order_by_speed = " ORDER BY speed DESC";
        String order_by_date_ASC = " ORDER BY timestamp ASC";
        String order_by_date_DESC = " ORDER BY timestamp DESC";

        //Put the string querys with the corresponding filters value in the map dictionary
        map.put("Show All","");
        map.put("Today",filter_today);
        map.put("This Week",filter_last_week);
        map.put("This Month",filter_last_month);

        //Put the string querys with the corresponding order filters value in the map dictionary
        map.put("Fastest",order_by_speed);
        map.put("Oldest",order_by_date_ASC);
        map.put("Latest",order_by_date_DESC);

        //Our cursor where we will run our query and get the results from the database
        Cursor cursor = db.rawQuery(query_all+map.get(selected)+map.get(ordered) , null);

        if (cursor.getCount() == 0)
            //If no results found make a toast and show that no records found
            Toast.makeText(this, "No records found", Toast.LENGTH_LONG).show();
        else {
            arrayList.clear();//clear our array list
            while (cursor.moveToNext()) {
                /*Create a stringbuffer to construct a string that contains all
                of the data we got from the database for each record
                 */
                StringBuffer buffer = new StringBuffer();
                buffer.append("Date: "+cursor.getString(3));
                buffer.append("\n");
                buffer.append("Speed: "+cursor.getString(2)+" Km/h");
                buffer.append("\n");
                buffer.append("Longtitude: "+cursor.getString(0));
                buffer.append("\n");
                buffer.append("Latitude: "+cursor.getString(1));
                buffer.append("\n");

                //add the string with the results to our array list
                arrayList.add(buffer.toString());
            }

            //Initialize our adapter as an array adapter with a layout of a simple list item and with the data of our array list
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);


            //Set our adapter to our listview to display the results
            listView.setAdapter(adapter);
            listView.refreshDrawableState();
        }
    }
}
