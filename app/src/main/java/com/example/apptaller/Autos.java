package com.example.apptaller;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Autos extends AppCompatActivity {
    private EditText etMarca, etModelo, etAño, etPlaca;
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar, nuke;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autos);

        bindComponents();
        addListeners();

        if (!conectarBaseDeDatos()) {
            return;
        }
    }

    private void bindComponents() {
        etMarca = (EditText) findViewById(R.id.etMarca);
        etModelo = (EditText) findViewById(R.id.etModelo);
        etAño = (EditText) findViewById(R.id.etAño);
        etPlaca = (EditText) findViewById(R.id.etPlaca);

        btnAñadir = (Button) findViewById(R.id.btnAñadir);
        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
    }
    private void addListeners() {
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
