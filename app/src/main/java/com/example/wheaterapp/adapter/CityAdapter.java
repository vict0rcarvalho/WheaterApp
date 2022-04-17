package com.example.wheaterapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wheaterapp.R;
import com.example.wheaterapp.WeatherDetails;
import com.example.wheaterapp.model.City;
import java.util.List;



public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder>{
    Context context;
    List<City> listaCities;
    List<City> listaNameCities;
    private String nameCity;


    public CityAdapter(List<City> listaCities, List<City> listaNameCities, Context context) {
        this.listaCities = listaCities;
        this.listaNameCities = listaNameCities;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rcv_cities_details, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.ViewHolder holder, int position) {
        City city= listaCities.get(position);
        City cityName = listaNameCities.get(position);
        holder.cityName.setText(cityName.getCityName());
        holder.tempValue.setText(city.getTempValue());
        holder.tempValueMin.setText(city.getTempmin());
        holder.tempValueMax.setText(city.getTempMax());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameCity = listaNameCities.get(position).getCityName();
                Intent it = new Intent(context, WeatherDetails.class);
                it.putExtra("city", nameCity);
                context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return  listaNameCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView cityName;
        TextView tempValueMin;
        TextView tempValueMax;
        TextView tempValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityName = itemView.findViewById(R.id.txvCityName);
            tempValue = itemView.findViewById(R.id.txvTempValue);
            tempValueMin = itemView.findViewById(R.id.txvValueMin);
            tempValueMax = itemView.findViewById(R.id.txvValueMax);
        }
    }
}