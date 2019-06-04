package com.example.apptaller;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static BaseDeDatos instanciaBD;
    private static int version = 1;
    private String queryPersonas, queryAutos, queryServicios;

    private BaseDeDatos(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
        queryPersonas = "CREATE TABLE PERSONAS(" +
                "RFC text PRIMARY KEY," +
                "Nombre text NOT NULL," +
                "Ciudad text NOT NULL," +
                "EstatusPersona integer DEFAULT 1 NOT NULL);";
        queryAutos = "CREATE TABLE ";
        queryServicios = "CREATE TABLE SERVICIOS(" +
                "Orden int PRIMARY KEY, " +
                "Placa text NOT NULL, " +
                "RFC text NOT NULL, " +
                "KM int NOT NULL, " +
                "Precio float NOT NULL," +
                "Fecha text NOT NULL);";
    }

    public static BaseDeDatos getInstance(Context context) {
        if(instanciaBD == null) {
            instanciaBD = new BaseDeDatos(context, "BD_TALLER", null, version);
        }
        return instanciaBD;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryPersonas);
        version++;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS queryUsuarios;");
        db.execSQL(queryUsuarios);
        queryAutos = "CREATE TABLE AUTOS(" +
                "   Placa text PRIMARY KEY," +
                "   Marca text NOT NULL," +
                "   Modelo text NOT NULL," +
                "   Año integer NOT NULL," +
                "   EstatusAuto integer DEFAULT 1 NOT NULL);";
        db.execSQL(queryAutos);
        db.execSQL("DROP TABLE IF EXISTS queryPersonas;");
        db.execSQL(queryPersonas);
    }

}
