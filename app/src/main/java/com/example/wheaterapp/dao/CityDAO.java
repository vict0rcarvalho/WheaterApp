package com.example.wheaterapp.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.wheaterapp.helper.Dbhelper;
import com.example.wheaterapp.helper.ICityDAO;
import com.example.wheaterapp.model.City;
import java.util.ArrayList;
import java.util.List;

public class CityDAO implements ICityDAO {
    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public CityDAO(Context context) {
        Dbhelper dbHelper = new Dbhelper(context);
        escreve = dbHelper.getWritableDatabase();
        le = dbHelper.getReadableDatabase();

    }

    @Override
    public boolean save (City city) {

        ContentValues cv = new ContentValues();
        cv.put("name", city.getCityName());

        try{
            escreve.insert(Dbhelper.TABELA_CITY,null, cv);
        } catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public boolean update(City city) {
        return false;
    }

    @Override
    public boolean delete(City city) {
        return false;
    }

    @Override
    public List<City> listar() {

        List<City> listaCities = new ArrayList<>();

        String sql = "SELECT name FROM " + Dbhelper.TABELA_CITY + ";";
        Cursor cursor = le.rawQuery(sql, null);

        while(cursor.moveToNext()){

            City city = new City();

            int columnIndexname = cursor.getColumnIndex("name");
            String name = cursor.getString(columnIndexname);
            city.setCityName(name);

            listaCities.add(city);
        }
        return listaCities;


    }
}