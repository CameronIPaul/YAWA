package com.example.yawa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String weatherJSON = null;
    String specifiedCity = "Hamilton"; //Hamilton by default
    public static final String JSON_EXTRA = "weatherJSON";
    public static final String CITY_EXTRA = "userSpecifiedCity";
    public static final String DAY_EXTRA = "dayClicked";
    public static final String DAY_INT_EXTRA = "dayInt";

    TextView day1;
    TextView day1Temp;
    TextView day1Desc;

    TextView day2;
    TextView day2Temp;
    TextView day2Desc;

    TextView day3;
    TextView day3Temp;
    TextView day3Desc;

    TextView day4;
    TextView day4Temp;
    TextView day4Desc;

    TextView day5;
    TextView day5Temp;
    TextView day5Desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        day1 = findViewById(R.id.Day1);
        day1Temp = findViewById(R.id.Day1TempVal);
        day1Desc = findViewById(R.id.Day1DescVal);

        day2 = findViewById(R.id.Day2);
        day2Temp = findViewById(R.id.Day2TempVal);
        day2Desc = findViewById(R.id.Day2DescVal);

        day3 = findViewById(R.id.Day3);
        day3Temp = findViewById(R.id.Day3TempVal);
        day3Desc = findViewById(R.id.Day3DescVal);

        day4 = findViewById(R.id.Day4);
        day4Temp = findViewById(R.id.Day4TempVal);
        day4Desc = findViewById(R.id.Day4DescVal);

        day5 = findViewById(R.id.Day5);
        day5Temp = findViewById(R.id.Day5TempVal);
        day5Desc = findViewById(R.id.Day5DescVal);

        EditText City = findViewById(R.id.SearchCity);
        City.setText(specifiedCity);
        City.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    specifiedCity = v.getText().toString();
                    handled = lookupCity(specifiedCity);
                }
                return handled;
            }
        });
    }

    public void Day1Click(View v) {
        Intent intent = new Intent(this, DayInformation.class);
        intent.putExtra(JSON_EXTRA, weatherJSON);
        intent.putExtra(CITY_EXTRA, specifiedCity);
        intent.putExtra(DAY_EXTRA, day1.getText());
        intent.putExtra(DAY_INT_EXTRA, 0);
        startActivity(intent);
    }

    public void Day2Click(View v) {
        if (weatherJSON != null) {
            Intent intent = new Intent(this, DayInformation.class);
            intent.putExtra(JSON_EXTRA, weatherJSON);
            intent.putExtra(CITY_EXTRA, specifiedCity);
            intent.putExtra(DAY_EXTRA, day2.getText());
            intent.putExtra(DAY_INT_EXTRA, 1);
            startActivity(intent);
        }
    }

    public void Day3Click(View v) {
        if (weatherJSON != null) {
            Intent intent = new Intent(this, DayInformation.class);
            intent.putExtra(JSON_EXTRA, weatherJSON);
            intent.putExtra(CITY_EXTRA, specifiedCity);
            intent.putExtra(DAY_EXTRA, day3.getText());
            intent.putExtra(DAY_INT_EXTRA, 2);
            startActivity(intent);
        }
    }

    public void Day4Click(View v) {
        if (weatherJSON != null) {
            Intent intent = new Intent(this, DayInformation.class);
            intent.putExtra(JSON_EXTRA, weatherJSON);
            intent.putExtra(CITY_EXTRA, specifiedCity);
            intent.putExtra(DAY_EXTRA, day4.getText());
            intent.putExtra(DAY_INT_EXTRA, 3);
            startActivity(intent);
        }
    }

    public void Day5Click(View v) {
        if (weatherJSON != null) {
            Intent intent = new Intent(this, DayInformation.class);
            intent.putExtra(JSON_EXTRA, weatherJSON);
            intent.putExtra(CITY_EXTRA, specifiedCity);
            intent.putExtra(DAY_EXTRA, day5.getText());
            intent.putExtra(DAY_INT_EXTRA, 4);
            startActivity(intent);
        }
    }

    public boolean lookupCity (String city) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.openweathermap.org/data/2.5/forecast?q=" + city + ",nz&units=metric&APPID=56691326ed3c81b1c2f06d6e8465fc4d";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        weatherJSON = response;
                        Log.i("200", response);
                        ParseJSON();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Error
                Log.e("400", "Error making API call." + error);
            }
        });

        queue.add(stringRequest);
        return true;
    }

    public void ParseJSON() {
        try {
            JSONObject reader = new JSONObject(weatherJSON);

            JSONArray list = reader.getJSONArray("list");

            for (int i = 0; i < list.length(); i++) {
                if (i % 8 == 0) {
                    JSONObject item = list.getJSONObject(i);

                    int dt = item.getInt("dt");

                    JSONObject main = item.getJSONObject("main");
                    int temp = main.getInt("temp");

                    JSONArray weather = item.getJSONArray("weather");
                    JSONObject w = weather.getJSONObject(0);
                    String description = w.getString("description");

                    switch (i / 8) {
                        case 0:
                            //day1
                            day1Temp.setText(temp + " °C");
                            day1Desc.setText(description);
                            day1.setText(dayOfWeek(dt));
                            break;
                        case 1:
                            //day2
                            day2Temp.setText(temp + " °C");
                            day2Desc.setText(description);
                            day2.setText(dayOfWeek(dt));
                            break;
                        case 2:
                            //day3
                            day3Temp.setText(temp + " °C");
                            day3Desc.setText(description);
                            day3.setText(dayOfWeek(dt));
                            break;
                        case 3:
                            //day4
                            day4Temp.setText(temp + " °C");
                            day4Desc.setText(description);
                            day4.setText(dayOfWeek(dt));
                            break;
                        default:
                            //day5
                            day5Temp.setText(temp + " °C");
                            day5Desc.setText(description);
                            day5.setText(dayOfWeek(dt));
                            break;
                    }
                }
            }
        }
        catch (JSONException e) {
            Log.e("400", "JSON parsing error " + e);
        }
    }

    public String dayOfWeek(int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(i*1000);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK);
        switch (dayNum) {
            case 2:
                return "Monday";
            case 3:
                return "Tuesday";
            case 4:
                return "Wednesday";
            case 5:
                return "Thursday";
            case 6:
                return "Friday";
            case 7:
                return "Saturday";
            default:
                return "Sunday";
        }
    }
}
