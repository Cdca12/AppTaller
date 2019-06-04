package com.example.apptaller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.util.ArrayList;

public class Consultas extends AppCompatActivity {

    private ArrayList<String> infoConsulta;
    private ListView listaConsulta;
    private String query;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        if (!conectarBaseDeDatos()) {
            return;
        }

        query = getIntent().getStringExtra("query");


        // bindComponents();
        listaConsulta = (ListView) findViewById(R.id.listaConsulta);
        infoConsulta = new ArrayList<String>();

        Toast toast = Toast.makeText(this, query, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void bindComponents() {
        listaConsulta = (ListView) findViewById(R.id.listaConsulta);
    }

    private boolean conectarBaseDeDatos() {
        conexion = conexion.getInstance(this);
        if (conexion == null) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error de conexión");
            alertDialog.setMessage("No fue posible conectarse a la base de datos");
            alertDialog.show();
            return false;
        }
        // Conexión a la bd establecida
        return true;
    }

    private void rellenarLista() {
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);
    }
}
