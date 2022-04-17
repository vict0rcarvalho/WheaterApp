package com.example.wheaterapp.helper;

import com.example.wheaterapp.model.City;

import java.util.List;

public interface ICityDAO {
    public boolean save (City city);
    public boolean update (City city);
    public boolean delete (City city);
    public List<City> listar ();
}