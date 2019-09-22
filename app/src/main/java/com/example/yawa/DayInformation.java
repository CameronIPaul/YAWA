package com.example.yawa;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_information);

        Intent intent = getIntent();
        weatherJSON = intent.getStringExtra(MainActivity.JSON_EXTRA);
        specifiedCity = intent.getStringExtra(MainActivity.CITY_EXTRA);
        dayClicked = intent.getStringExtra(MainActivity.DAY_EXTRA);
        dayInt = intent.getIntExtra(MainActivity.DAY_INT_EXTRA, 1);

        TextView day = findViewById(R.id.Day);
        day.setText(dayClicked);

        TextView city = findViewById(R.id.City);
        city.setText(specifiedCity);

        TextView temp = findViewById(R.id.TempVal);
        TextView tempMin = findViewById(R.id.TempMinVal);
        TextView temMax = findViewById(R.id.TempMaxVal);
        TextView desc = findViewById(R.id.DescriptionVal);
        TextView humidity = findViewById(R.id.HumidityVal);

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

            temp.setText(temperature + " °C");
            tempMin.setText(min + " °C");
            temMax.setText(max + " °C");
            humidity.setText(humi + "%");
            desc.setText(description);
        }
        catch (JSONException e) {
            Log.e("400", "JSON parsing error " + e);
        }
    }
}
