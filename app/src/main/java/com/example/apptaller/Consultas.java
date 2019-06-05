package com.example.apptaller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

public class Consultas extends AppCompatActivity {

    private RecyclerView reciclerViewConsulta;
    private TextView tvHeader1, tvHeader2, tvHeader3, tvHeader4, tvHeader5;

    private ArrayList<Consulta1> listaConsulta1;
    private String query;
    private int tipoConsulta;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        setTitle(getIntent().getStringExtra("titulo"));

        bindComponents();
        getBundle();

        if (!conectarBaseDeDatos()) {
            return;
        }

        rellenarHeaders();

        reciclerViewConsulta.setHasFixedSize(true); // ¿Qué hace?

        // Nuestro RecyclerView usará un Linear Layout Manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        reciclerViewConsulta.setLayoutManager(layoutManager);

        // Asociamos un adapter dependiendo el tipo de consulta
        if (tipoConsulta == 1) {
            ArrayList<Consulta1> listaConsulta1 = rellenarListaConsulta1(query);
            AdaptadorConsulta1 adaptadorConsulta1 = new AdaptadorConsulta1(listaConsulta1);
            reciclerViewConsulta.setAdapter(adaptadorConsulta1);
            return;
        }
        if (tipoConsulta == 2) {
            ArrayList<Consulta2> listaConsulta2 = rellenarListaConsulta2(query);
            AdaptadorConsulta2 adaptadorConsulta2 = new AdaptadorConsulta2(listaConsulta2);
            reciclerViewConsulta.setAdapter(adaptadorConsulta2);
            return;
        }
        if (tipoConsulta == 3) {
            ArrayList<Consulta3> listaConsulta3 = rellenarListaConsulta3(query);
            AdaptadorConsulta3 adaptadorConsulta3 = new AdaptadorConsulta3(listaConsulta3);
            reciclerViewConsulta.setAdapter(adaptadorConsulta3);
            return;
        }

    }

    private void bindComponents() {
        reciclerViewConsulta = (RecyclerView) findViewById(R.id.reciclerViewConsulta);

        tvHeader1 = (TextView) findViewById(R.id.tvHeader1);
        tvHeader2 = (TextView) findViewById(R.id.tvHeader2);
        tvHeader3 = (TextView) findViewById(R.id.tvHeader3);
        tvHeader4 = (TextView) findViewById(R.id.tvHeader4);
        tvHeader5 = (TextView) findViewById(R.id.tvHeader5);
    }

    private void getBundle() {
        query = getIntent().getStringExtra("query");
        tipoConsulta = getIntent().getIntExtra("tipoConsulta", 0);
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

    private void rellenarHeaders() {
        if (tipoConsulta == 1) {
            tvHeader1.setText(Consulta1.nombreColumnas[0]);
            tvHeader2.setText(Consulta1.nombreColumnas[1]);
            tvHeader3.setText(Consulta1.nombreColumnas[2]);
            tvHeader4.setText(Consulta1.nombreColumnas[3]);
            tvHeader5.setText(Consulta1.nombreColumnas[4]);
            return;
        }
        if (tipoConsulta == 2) {
            tvHeader1.setText(Consulta2.nombreColumnas[0]);
            tvHeader2.setText(Consulta2.nombreColumnas[1]);
            tvHeader3.setText(Consulta2.nombreColumnas[2]);
            tvHeader4.setText(Consulta2.nombreColumnas[3]);
            tvHeader5.setVisibility(View.GONE); // Ocultar y distribuir width
            return;
        }
        if (tipoConsulta == 3) {
            tvHeader1.setText(Consulta3.nombreColumnas[0]);
            tvHeader2.setText(Consulta3.nombreColumnas[1]);
            tvHeader3.setVisibility(View.GONE);
            tvHeader4.setVisibility(View.GONE);
            tvHeader5.setVisibility(View.GONE);
            return;
        }
    }

    private ArrayList<Consulta1> rellenarListaConsulta1(String query) {
        ArrayList<Consulta1> listaConsulta1Aux = new ArrayList<>();
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        Consulta1 consultaAux;

        while (cursor.moveToNext()) {
            consultaAux = new Consulta1();

            consultaAux.setCiudad(cursor.getString(0));
            consultaAux.setIngresoTotal(cursor.getString(1));
            consultaAux.setIngresoMenor(cursor.getString(2));
            consultaAux.setIngresoMayor(cursor.getString(3));
            consultaAux.setIngresoPromedio(cursor.getString(4));

            listaConsulta1Aux.add(consultaAux);
        }
        bd.close();
        return listaConsulta1Aux;
    }

    private ArrayList<Consulta2> rellenarListaConsulta2(String query) {
        ArrayList<Consulta2> listaConsulta2Aux = new ArrayList<>();
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        Consulta2 consultaAux;

        while (cursor.moveToNext()) {
            consultaAux = new Consulta2();

            consultaAux.setAño(cursor.getString(0));
            consultaAux.setCiudad(cursor.getString(1));
            consultaAux.setMarca(cursor.getString(2));
            consultaAux.setImporteTotal(cursor.getString(3));

            listaConsulta2Aux.add(consultaAux);
        }
        bd.close();
        return listaConsulta2Aux;
    }

    private ArrayList<Consulta3> rellenarListaConsulta3(String query) {
        ArrayList<Consulta3> listaConsulta3Aux = new ArrayList<>();
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        Consulta3 consultaAux;

        while (cursor.moveToNext()) {
            consultaAux = new Consulta3();

            consultaAux.setRfc(cursor.getString(0));
            consultaAux.setNombre(cursor.getString(1));

            listaConsulta3Aux.add(consultaAux);
        }
        bd.close();
        return listaConsulta3Aux;
    }
}
