package com.example.yawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class DayInformation extends AppCompatActivity {

    String weatherJSON;
    String specifiedCity;
    String dayClicked;
    int dayInt;
    RecyclerView recyclerView;
    RecyclerView.Adapter rAdapter;
    RecyclerView.LayoutManager layoutManager;
    String[] dataset = new String[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_information);

        Intent intent = getIntent();
        weatherJSON = intent.getStringExtra(MainActivity.JSON_EXTRA);
        specifiedCity = intent.getStringExtra(MainActivity.CITY_EXTRA);
        dayClicked = intent.getStringExtra(MainActivity.DAY_EXTRA);
        dayInt = intent.getIntExtra(MainActivity.DAY_INT_EXTRA, 1);

        dataset[0] = dayClicked + " - " + specifiedCity;
        dataset[1] = "Temperature: - ";
        dataset[2] = "Minimum Temperature: - ";
        dataset[3] = "Maximum Temperature: - ";
        dataset[4] = "Humidity: - ";
        dataset[5] = "Description: - ";

        recyclerView = findViewById(R.id.RecyclerViewDay);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rAdapter = new RecyclerAdapter(dataset, false);
        recyclerView.setAdapter(rAdapter);

        try {
            JSONObject reader = new JSONObject(weatherJSON);

            JSONArray list = reader.getJSONArray("list");

            JSONObject item = list.getJSONObject(dayInt*8);

            JSONObject main = item.getJSONObject("main");
            int temperature = main.getInt("temp");
            int min = main.getInt("temp_min");
            int max = main.getInt("temp_max");
            for (int i = dayInt * 8 + 1; i < (dayInt + 1) * 8; i ++) {
                JSONObject j = list.getJSONObject(i);
                JSONObject m = j.getJSONObject("main");
                int l = m.getInt("temp_min");
                int u = m.getInt("temp_max");
                if (l < min) {
                    min = l;
                }
                if (u > max) {
                    max = u;
                }
            }

            int humi = main.getInt("humidity");

            JSONArray weather = item.getJSONArray("weather");
            JSONObject w = weather.getJSONObject(0);
            String description = w.getString("description");

            dataset[1] = "Temperature: " + temperature + " °C";
            dataset[2] = "Minimum Temperature: " + min + " °C";
            dataset[3] = "Maximum Temperature: " + max + "°C";
            dataset[4] = "Humidity: " + humi + "%";
            dataset[5] = "Description: " + description;

            rAdapter.notifyDataSetChanged();
        }
        catch (JSONException e) {
            Log.e("400", "JSON parsing error " + e);
        }
    }


}
