package com.example.wheaterapp.helper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class Dbhelper extends SQLiteOpenHelper {
    public static int VERSION = 1;
    public static String NOME_DB = "DB_CITY";
    public static String TABELA_CITY = "cities";

    public Dbhelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = " CREATE TABLE IF NOT EXISTS " + TABELA_CITY
                    + " (name TEXT PRIMARY KEY NOT NULL );";

        String sql_listaCidades = "INSERT INTO " + TABELA_CITY + " (name) VALUES " +
                "('Bady Bassitt'), " +
                "('Lisboa'), " +
                "('Madrid'), " +
                "('Paris'), " +
                "('Copenhaga'), " +
                "('Roma'), " +
                "('Londres'), " +
                "('Dublin'), " +
                "('Praga'), " +
                "('Viena'); ";

        try{
            db.execSQL(sql);
            db.execSQL(sql_listaCidades);
            Log.i("INFO DB", "sucesso ao criar a tabela");

        } catch (Exception e){
            Log.i("INFO DB", "Erro ao criar a tabela" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
