package com.example.apptaller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

public class Personas extends AppCompatActivity {

    private EditText etNombre, etRFC, etCiudad;
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personas);

        bindComponents();
        addListeners();

        if (!conectarBaseDeDatos()) {
            return;
        }
    }

    private void bindComponents() {
        etNombre = (EditText) findViewById(R.id.etNombre);
        etRFC = (EditText) findViewById(R.id.etRFC);
        etCiudad = (EditText) findViewById(R.id.etCiudad);
        btnAñadir = (Button) findViewById(R.id.btnAñadir);
        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
    }

    private void addListeners() {
        btnAñadir.setOnClickListener(view -> {
            añadirPersona();
        });
        btnConsultar.setOnClickListener(view -> {
            consultarPersonaPorRFC();
        });
        btnModificar.setOnClickListener(view -> {

        });
        btnEliminar.setOnClickListener(view -> {

        });
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

    private boolean camposVacios() {
        if (etNombre.getText().toString().equals("") &&
                etRFC.getText().toString().equals("") &&
                etCiudad.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private boolean camposIncompletos() {
        if (etNombre.getText().toString().equals("") ||
                etRFC.getText().toString().equals("") ||
                etCiudad.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void limpiarCampos() {
        etNombre.setText(null);
        etRFC.setText(null);
        etCiudad.setText(null);
        etNombre.requestFocus();
    }

    private void añadirPersona() {
        if (camposIncompletos()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error: Campos incompletos");
            alertDialog.setMessage("Favor de rellenar todos los campos de texto");
            alertDialog.show();
            return;
        }
        String query = "INSERT INTO PERSONAS (RFC, Nombre, Ciudad, EstatusPersona) " +
                "VALUES ('" + etRFC.getText().toString() + "', '"
                + etNombre.getText().toString() + "', '"
                + etCiudad.getText().toString() + "', 1);";
        bd = conexion.getWritableDatabase();
        // TODO: Cachar por si tira excepción. ID repetido, otro excepcion general SQL que el mensaje diga error inesperado bd
        bd.execSQL(query);
        bd.close(); // Cerrar conexión a Base de Datos por seguridad
        limpiarCampos();
        Toast toast = Toast.makeText(this, "Persona añadida exitosamente", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void consultarPersonaPorRFC() {
        // Valida campo vacío
        if (etRFC.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Favor de ingresar un RFC", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String query = "SELECT * FROM PERSONAS WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        // Validar que haya traido datos
        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(this, "No existe una persona con ese RFC", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Valida que no esté dado de baja
        cursor.moveToFirst();
        if (Integer.parseInt(cursor.getString(3)) == 0) {
            Toast toast = Toast.makeText(this, "La persona está dada de baja", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        limpiarCampos();

        // Rellenar datos
        cursor.moveToFirst();
        etRFC.setText(cursor.getString(0));
        etNombre.setText(cursor.getString(1));
        etCiudad.setText(cursor.getString(2));

        // Habilitar botones
        btnModificar.setEnabled(true);
        btnEliminar.setEnabled(true);

        bd.close();
    }
}
