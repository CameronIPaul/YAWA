package com.example.yawa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    static View.OnClickListener onClickMain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildAdapterPosition(v);
            if (weatherJSON != null) {
                Intent intent = new Intent(v.getContext(), DayInformation.class);
                intent.putExtra(JSON_EXTRA, weatherJSON);
                intent.putExtra(CITY_EXTRA, specifiedCity);
                intent.putExtra(DAY_EXTRA, dataset[itemPosition - itemPosition%3]); //i.e. get the name of the day
                intent.putExtra(DAY_INT_EXTRA, itemPosition/3);
                v.getContext().startActivity(intent);
            }
        }
    };

    static String weatherJSON = null;
    static String specifiedCity = "Hamilton"; //Hamilton by default
    static String[] dataset = new String[15]; //5 days, each with 3 rows of data (day title, temperature, description)

    static RecyclerView recyclerView;
    RecyclerView.Adapter rAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static final String JSON_EXTRA = "weatherJSON";
    public static final String CITY_EXTRA = "userSpecifiedCity";
    public static final String DAY_EXTRA = "dayClicked";
    public static final String DAY_INT_EXTRA = "dayInt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        dataset[0] = "Day 1";
        dataset[1] = "\t\tTemperature: -";
        dataset[2] = "\t\tDescription: -";
        dataset[3] = "Day 2";
        dataset[4] = "\t\tTemperature: -";
        dataset[5] = "\t\tDescription: -";
        dataset[6] = "Day 3";
        dataset[7] = "\t\tTemperature: -";
        dataset[8] = "\t\tDescription: -";
        dataset[9] = "Day 4";
        dataset[10] = "\t\tTemperature: -";
        dataset[11] = "\t\tDescription: -";
        dataset[12] = "Day 5";
        dataset[13] = "\t\tTemperature: -";
        dataset[14] = "\t\tDescription: -";

        recyclerView = findViewById(R.id.RecyclerViewMain);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rAdapter = new RecyclerAdapter(dataset, true);
        recyclerView.setAdapter(rAdapter);
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
                            dataset[0] = dayOfWeek(dt);
                            dataset[1] = "\t\tTemperature: " + temp + " °C";
                            dataset[2] = "\t\tDescription: " + description;
                            break;
                        case 1:
                            //day2
                            dataset[3] = dayOfWeek(dt);
                            dataset[4] = "\t\tTemperature: " + temp + " °C";
                            dataset[5] = "\t\tDescription: " + description;
                            break;
                        case 2:
                            //day3
                            dataset[6] = dayOfWeek(dt);
                            dataset[7] = "\t\tTemperature: " + temp + " °C";
                            dataset[8] = "\t\tDescription: " + description;
                            break;
                        case 3:
                            //day4
                            dataset[9] = dayOfWeek(dt);
                            dataset[10] = "\t\tTemperature: " + temp + " °C";
                            dataset[11] = "\t\tDescription: " + description;
                            break;
                        default:
                            //day5
                            dataset[12] = dayOfWeek(dt);
                            dataset[13] = "\t\tTemperature: " + temp + " °C";
                            dataset[14] = "\t\tDescription: " + description;
                            break;
                    }

                }
            }
            rAdapter.notifyDataSetChanged();
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
