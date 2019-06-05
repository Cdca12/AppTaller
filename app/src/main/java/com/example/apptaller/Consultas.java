package com.example.apptaller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
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
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        if (!conectarBaseDeDatos()) {
            return;
        }

        mRecyclerView.setHasFixedSize(true);

        // Nuestro RecyclerView usará un linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        // Asociamos un adapter (ver más adelante cómo definirlo)



        query = getIntent().getStringExtra("query");




        if(tipo==1){
            ArrayList<Consulta1> jajaj= rellenarListaConsulta1(query);
             AdaptadorConsulta1 ad= new AdaptadorConsulta1(jajaj);
            mRecyclerView.setAdapter(ad);
        }

        // bindComponents();
        listaConsulta = (ListView) findViewById(R.id.listaConsulta);
        infoConsulta = new ArrayList<String>();

        rellenarLista();
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

    private List<Consulta1> rellenarLista() {
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        Consulta1 consulta;

        while(cursor.moveToNext()) {
            consulta = new Consulta1(cursor.getString(0), );
            consulta.setPrimeraColumna();
            consulta.setSegundaColumna(cursor.getString(1));
            consulta.setTerceraColumna(cursor.getString(2));
            consulta.setCuartaColumna(cursor.getString(3));
            consulta.setQuintaColumna(cursor.getString(4));

            listaConsulta.add(consulta);

        }

        LinearLayout headers = (LinearLayout) findViewById(R.id.tupla);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT,
                1.0f
        );

        TextView encabezado1 = new TextView(this);
        encabezado1.setText("RFC");
        // encabezado1.setLayoutParams(params);
        headers.addView(encabezado1);



        while (cursor.moveToNext()) {

            infoConsulta.add(cursor.getString(0));
        }



        bd.close();
    }
}
