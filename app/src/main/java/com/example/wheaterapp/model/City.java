package com.example.wheaterapp.model;

import java.io.Serializable;
import java.util.List;

public class City implements Serializable {
    String cityName;
    String tempValue;
    String tempMax;
    String tempmin;



    public String getTempValue() {
        return tempValue;
    }

    public void setTempValue(String tempValue) {
        this.tempValue = tempValue;
    }

    public String getCityName() {
        return cityName;
    }

    public List<City> setCityName(String cityName) {
        this.cityName = cityName;
        return null;
    }
    public String getTempMax() {
        return tempMax;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public String getTempmin() {
        return tempmin;
    }

    public void setTempmin(String tempmin) {
        this.tempmin = tempmin;
    }
}
