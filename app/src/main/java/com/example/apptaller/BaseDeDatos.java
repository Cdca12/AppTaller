package com.example.apptaller;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BaseDeDatos extends SQLiteOpenHelper {
    private static BaseDeDatos instanciaBD;
    private static int version = 1;
    private String queryUsuarios;

    private BaseDeDatos(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
        queryUsuarios = "CREATE TABLE PERSONAS(" +
                "   RFC text PRIMARY KEY," +
                "   Nombre text NOT NULL," +
                "   Ciudad text NOT NULL," +
                "   EstatusPersona integer DEFAULT 1 NOT NULL);";
    }

    public static BaseDeDatos getInstance(Context context) {
        if(instanciaBD == null) {
            instanciaBD = new BaseDeDatos(context, "BD_TALLER", null, version);
        }
        return instanciaBD;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(queryUsuarios);
        version++;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS queryUsuarios;");
        db.execSQL(queryUsuarios);
    }

}
