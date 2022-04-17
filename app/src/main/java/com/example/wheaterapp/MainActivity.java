package com.example.wheaterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.wheaterapp.adapter.CityAdapter;
import com.example.wheaterapp.dao.CityDAO;
import com.example.wheaterapp.model.City;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText edtSearch;
    private ImageButton imgSearch;
    private RecyclerView recyclerView;
    private String nameCity;
    private  List<City> listaCities = new ArrayList<>();;
    private List<City> listaNameCities = new ArrayList<>();;
    private final String KEY = "f432694f017474d6e9f45f3c22a010f9";
    private final String URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private final String URL_UNITS = "&units=metric&appid=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvCities);
        edtSearch = findViewById(R.id.edtSearch);
        imgSearch = findViewById(R.id.imgSearch);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == imgSearch) {
                    nameCity = String.valueOf(edtSearch.getText());
                    Intent it = new Intent(getApplicationContext(), WeatherDetails.class);
                    it.putExtra("city", nameCity);
                    startActivity(it);
                }
            }
        });

        loadCities();


    }

    private void jsonDataWEBCall(City city) {
        String cityName = String.valueOf(city.getCityName());
        StringBuffer url = new StringBuffer(URL)
                .append(cityName).append(URL_UNITS)
                .append(KEY);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url.toString(), null, future, future);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

        try {
            JSONObject response = future.get(3,TimeUnit.SECONDS);
            processJson(response);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    private void processJson (JSONObject jsonObject) {
        try {
            String tempValue = jsonObject.getJSONObject("main").getString("temp");
            String tempMin = jsonObject.getJSONObject("main").getString("temp_min");
            String tempMax = jsonObject.getJSONObject("main").getString("temp_max");

            double tempValueFormated = Double.parseDouble(tempValue);
            double tempValueDouble;
            tempValueDouble = (double) Math.round(tempValueFormated);
            tempValue = tempValueDouble + getString(R.string.graus_celsius);

            double tempMinFormated = Double.parseDouble(tempMin);
            double tempMinDouble;
            tempMinDouble = (double) Math.round(tempMinFormated);
            tempMin = tempMinDouble + getString(R.string.graus_celsius);

            double tempMaxFormated = Double.parseDouble(tempMax);
            double tempMaxDouble;
            tempMaxDouble = (double) Math.round(tempMaxFormated);
            tempMax = tempMaxDouble + getString(R.string.graus_celsius);


            City city = new City();
            city.setTempValue(tempValue);
            city.setTempmin(tempMin);
            city.setTempMax(tempMax);

            listaCities.add(city);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    class ListaCitiesAsyncTask extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {

            for(int i = 0; i<listaNameCities.size(); i++){
                jsonDataWEBCall(listaNameCities.get(i));
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            inflateRecyclerView();
        }
    }


    private void inflateRecyclerView(){
        CityAdapter cityAdapter = new CityAdapter(listaCities, listaNameCities, this);
       recyclerView.setAdapter(cityAdapter);
   }


   void loadCities(){
       CityDAO cityDAO = new CityDAO(getApplicationContext());
       listaNameCities = cityDAO.listar();
   }

    protected void onResume() {
        loadCities();
        new ListaCitiesAsyncTask().execute();
        edtSearch.setText("");
        super.onResume();
    }
    
}