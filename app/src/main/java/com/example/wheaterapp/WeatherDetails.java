package com.example.wheaterapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.wheaterapp.dao.CityDAO;
import com.example.wheaterapp.model.City;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class WeatherDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView txvHumidity;
    private TextView txvVisibility;
    private TextView txvTempMIn;
    private TextView txvTempMax;
    private TextView txvCity;
    private TextView txvTempDetails;
    private TextView txvDescriptionDetails;
    private TextView txvFeelsLike;
    private TextView txvPressure;
    private ImageView imgList;
    private ImageView imgAdd;
    private String cityJson;
    private String url;
    private String nameCity;
    private String Key = "f432694f017474d6e9f45f3c22a010f9";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_weather_details);

        txvCity = findViewById(R.id.txvCity);
        txvTempDetails = findViewById(R.id.txvTempDetails);
        txvDescriptionDetails = findViewById(R.id.txvDescriptionDetails);
        txvFeelsLike = findViewById(R.id.txvfeelsLike);
        txvHumidity = findViewById(R.id.txvHumidity);
        txvPressure = findViewById(R.id.txvPressure);
        txvTempMIn = findViewById(R.id.txvTempMin);
        txvTempMax = findViewById(R.id.txvTempMax);
        txvVisibility = findViewById(R.id.txvVisibility);

        imgList = findViewById(R.id.imgList);
        imgList.setOnClickListener(this);

        imgAdd = findViewById(R.id.imgAdd);
        imgAdd.setOnClickListener(this);

        Bundle data = getIntent().getExtras();
        nameCity = data.getString("city");
        url = "https://api.openweathermap.org/data/2.5/weather?q=" +nameCity+ "&units=metric&appid=" + Key;



        DowloadJSON dowloadJSON = new DowloadJSON();

        try {
            String result = dowloadJSON.execute(url).get();
            JSONObject jsonObject = new JSONObject(result);
            cityJson = jsonObject.getString("name");
            String tempDetails = jsonObject.getJSONObject("main").getString("temp");
            String humidity = jsonObject.getJSONObject("main").getString("humidity");
            String feelsLike = jsonObject.getJSONObject("main").getString("feels_like");
            String tempMin= jsonObject.getJSONObject("main").getString("temp_min");
            String tempMax = jsonObject.getJSONObject("main").getString("temp_max");
            String visibility = jsonObject.getString("visibility");
            String descriptionDetails = jsonObject.getJSONObject("clouds").getString("all");
            String pressure = jsonObject.getJSONObject("main").getString("pressure");

            txvCity.setText(cityJson);
            txvHumidity.setText(humidity + getString(R.string.percentage));
            txvPressure.setText(pressure);


            Double visibilityDouble = Double.parseDouble(visibility);
            visibility = String.valueOf((visibilityDouble / 1000));

            txvVisibility.setText(visibility + getString(R.string.kilometers));

            int intDescriptionDetails = Integer.parseInt(descriptionDetails);
            if (intDescriptionDetails <= 10){
                txvDescriptionDetails.setText(R.string.clear);
            } else if ( intDescriptionDetails >10 & intDescriptionDetails <=30 ){
                txvDescriptionDetails.setText(R.string.little_cloudy);
            } else if (intDescriptionDetails>30 & intDescriptionDetails <=70 ){
                txvDescriptionDetails.setText(R.string.partly_cloudy);
            } else {
                txvDescriptionDetails.setText(R.string.most_cloudy);
            }


            double feelsLikeFormated = Double.parseDouble(feelsLike);
            double feelslikeDouble;
            feelslikeDouble = (double) Math.round(feelsLikeFormated);
            feelsLike = Double.toString(feelslikeDouble);
            txvFeelsLike.setText(feelsLike + getString(R.string.graus_celsius));

            double tempDetailsFormated = Double.parseDouble(tempDetails);
            double tempDetailsDouble ;
            tempDetailsDouble = (double) Math.round(tempDetailsFormated);
            tempDetails = Double.toString(tempDetailsDouble);
            txvTempDetails.setText(tempDetails + getString(R.string.graus_celsius));


            double tempMinFormated = Double.parseDouble(tempMin);
            double tempMinDouble;
            tempMinDouble = (double) Math.round(tempMinFormated);
            tempMin = tempMinDouble + getString(R.string.graus_celsius);
            txvTempMIn.setText(tempMin);

            double tempMaxFormated = Double.parseDouble(tempMax);
            double tempMaxDouble;
            tempMaxDouble = (double) Math.round(tempMaxFormated);
            tempMax = tempMaxDouble + getString(R.string.graus_celsius);
            txvTempMax.setText(tempMax);


        } catch (ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(v==imgList){
            Intent it = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(it);
        }

        if (v==imgAdd ){
            CityDAO cityDAO = new CityDAO(getApplicationContext());
            City city = new City();
            city.setCityName(cityJson);
            if(cityDAO.save(city)){
                finish();
                Toast.makeText(this, R.string.toast_sucesso,
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public static class DowloadJSON extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while(data != -1){
                    result += (char) data;

                    data =  inputStreamReader.read();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}