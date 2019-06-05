package com.example.apptaller;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Plaza Nisson");

        if (!conectarBaseDeDatos()) {
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.itemAutos) {
            Intent intent = new Intent(this, Autos.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.itemPersonas) {
            Intent intent = new Intent(this, Personas.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.itemServicios) {
            Intent intent = new Intent(this, Servicios.class);
            startActivity(intent);
            return true;
        }
        // Menu Consultas
        String query;
        if (id == R.id.itemPrimeraConsulta) {
            Intent intent = new Intent(this, Consultas.class);
            query = "SELECT Ciudad, SUM(Precio) IngresoTotal, MIN(Precio) IngresoMenor,\n" +
                    "MAX(Precio) IngresoMayor, AVG(Precio) IngresoPromedio FROM SERVICIOS serv \n" +
                    "INNER JOIN PERSONAS pers ON serv.RFC = pers.RFC\n" +
                    "GROUP BY Ciudad;";
            intent.putExtra("query", query);
            intent.putExtra("tipoConsulta", 1);
            intent.putExtra("titulo", "Ingresos por Ciudad");
            startActivity(intent);
            return true;
        }
        if (id == R.id.itemSegundaConsulta) {
            Intent intent = new Intent(this, Consultas.class);
            query = "SELECT substr(Fecha,  1, 4) Año, Ciudad, Marca, \n" +
                    "SUM(Precio) ImporteTotal FROM SERVICIOS serv\n" +
                    "INNER JOIN PERSONAS pers ON serv.RFC = pers.RFC\n" +
                    "INNER JOIN AUTOS autos ON serv.Placa = autos.Placa\n" +
                    "GROUP BY substr(Fecha,  1, 4), Ciudad, Marca;";
            intent.putExtra("query", query);
            intent.putExtra("tipoConsulta", 2);
            intent.putExtra("titulo", "Servicios Año-Ciudad-Marca");
            startActivity(intent);
            return true;
        }
        if (id == R.id.itemTerceraConsulta) {
            Intent intent = new Intent(this, Consultas.class);
            query = "SELECT RFC, Nombre FROM PERSONAS WHERE RFC NOT IN(\n" +
                    "\tSELECT RFC FROM SERVICIOS\n" +
                    ")\n" +
                    "GROUP BY RFC, Nombre";
            intent.putExtra("query", query);
            intent.putExtra("tipoConsulta", 3);
            intent.putExtra("titulo", "Personas sin Autos en Servicio");
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
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
        Toast toast = Toast.makeText(this, "Conexión a la bd establecida", Toast.LENGTH_SHORT);
        toast.show();
        return true;
    }


}
