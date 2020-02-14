package hr.totohost.tinoweather;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {

    private String location;
    private String lat, lng;
    private String api_key = "8118ed6ee68db2debfaaa5a44c832918";

    private TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt;
    private View loader, mainContainer, errorText;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            location = savedInstanceState.getString("location");
            api_key = savedInstanceState.getString("api_key");
            lat = savedInstanceState.getString("lat");
            lng = savedInstanceState.getString("lng");
            new weatherTask().execute();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("location",  location);
        outState.putString("api_key", api_key);
        outState.putString("lat", lat);
        outState.putString("lng", lng);
    }

    // init preko imena
    WeatherFragment(String location) {
       this.location = location;
    }

    // init preko kordinata
    WeatherFragment(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    private String getRequestUrl() {
        if(lat != null && lng != null) {
            return "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng +"&units=metric&appid=" + api_key;
        } else {
            return "https://api.openweathermap.org/data/2.5/weather?q=" + location + "&units=metric&appid=" + api_key;
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addressTxt = view.findViewById(R.id.address);
        updated_atTxt = view.findViewById(R.id.updated_at);
        statusTxt = view.findViewById(R.id.status);
        tempTxt = view.findViewById(R.id.temp);
        temp_minTxt = view.findViewById(R.id.temp_min);
        temp_maxTxt = view.findViewById(R.id.temp_max);
        sunriseTxt = view.findViewById(R.id.sunrise);
        sunsetTxt = view.findViewById(R.id.sunset);
        windTxt = view.findViewById(R.id.wind);
        pressureTxt = view.findViewById(R.id.pressure);
        humidityTxt = view.findViewById(R.id.humidity);
        errorText = view.findViewById(R.id.errorText);
        loader = view.findViewById(R.id.loader);
        mainContainer = view.findViewById(R.id.mainContainer);

        new weatherTask().execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);

    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            loader.setVisibility(View.VISIBLE);
            mainContainer.setVisibility(View.GONE);
            errorText.setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            return HttpRequest.excuteGet(getRequestUrl());
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                long sunrise = sys.getLong("sunrise");
                long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                loader.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                loader.setVisibility(View.GONE);
                errorText.setVisibility(View.VISIBLE);
            }

        }
    }

}
