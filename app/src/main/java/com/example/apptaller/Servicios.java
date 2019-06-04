package com.example.apptaller;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;

public class Servicios extends AppCompatActivity {

    private EditText etOrden, etPlaca, etRFC, etKM, etPrecio, etFecha;
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar, btnNuke;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        bindComponents();
        addListeners();

        if (!conectarBaseDeDatos()) {
            return;
        }
    }

    private void bindComponents() {
        etOrden = (EditText) findViewById(R.id.etOrden);
        etPlaca = (EditText) findViewById(R.id.etPlaca);
        etRFC = (EditText) findViewById(R.id.etRFC);
        etKM = (EditText) findViewById(R.id.etKM);
        etPrecio = (EditText) findViewById(R.id.etPrecio);
        etFecha = (EditText) findViewById(R.id.etFecha);

        btnAñadir = (Button) findViewById(R.id.btnAñadir);
        btnConsultar = (Button) findViewById(R.id.btnConsultar);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnNuke = (Button) findViewById(R.id.btnNuke);
    }

    private void addListeners() {
        etOrden.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                habilitarBotones(false);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnAñadir.setOnClickListener(view -> {
            añadirServicio();
        });
        btnConsultar.setOnClickListener(view -> {
            consultarServicioPorOrden();
        });
        btnModificar.setOnClickListener(view -> {
            modificarServicio();
        });
        btnEliminar.setOnClickListener(view -> {
            eliminarServicio();
        });
        btnNuke.setOnClickListener(view -> {
            eliminarTodo();
        });
    }

    private void añadirServicio() {
        if (camposIncompletos()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error: Campos incompletos");
            alertDialog.setMessage("Favor de rellenar todos los campos de texto");
            alertDialog.show();
            return;
        }
        // Validar existe Persona
        // Validar existe Auto

        String query = "INSERT INTO SERVICIOS " +
                "VALUES ('" + etOrden.getText().toString() + "', '"
                + etPlaca.getText().toString().toUpperCase() + "', '"
                + etRFC.getText().toString().toUpperCase() + "', '"
                + etKM.getText().toString() + "', '"
                + etPrecio.getText().toString() + "', '"
                + etFecha.getText().toString() + "', 1);"; // Quitar los /

        bd = conexion.getWritableDatabase();

        // ID Repetido, entra excepcion
        try {
            bd.execSQL(query);
        } catch (SQLException e) {
            Toast toast = Toast.makeText(this, "Ya existe ese número de Orden", Toast.LENGTH_SHORT);
            toast.show();
            return;
        } finally {
            bd.close(); // Cerrar conexión a Base de Datos por seguridad
        }
        limpiarCampos();
        Toast toast = Toast.makeText(this, "Persona añadida exitosamente", Toast.LENGTH_SHORT);
        toast.show();
    }

    private void consultarServicioPorOrden() {
        // Valida campo vacío
        if (etOrden.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Favor de ingresar un número de Orden", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String query = "SELECT * FROM SERVICIOS " +
                "WHERE Orden = '" + etOrden.getText().toString().toUpperCase() + "';";

        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        // Validar que haya traido datos
        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(this, "No existe esa orden de servicio", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        cursor.moveToFirst();
        etOrden.setText(cursor.getString(0));
        etPlaca.setText(cursor.getString(1));
        etRFC.setText(cursor.getString(2));
        etKM.setText(cursor.getString(3));
        etPrecio.setText(cursor.getString(4));
        etFecha.setText(cursor.getString(5));

        // Habilitar botones
        habilitarBotones(true);

        bd.close();

    }

    private void modificarServicio() {

    }

    private void eliminarServicio() {

    }

    private void eliminarTodo() {
        String query = "DELETE FROM PERSONAS;";
        bd = conexion.getWritableDatabase();
        bd.execSQL(query);
        Toast toast = Toast.makeText(this, "Se han eliminado todas las personas", Toast.LENGTH_SHORT);
        toast.show();
        bd.close();
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

    private void habilitarBotones(boolean b) {
        btnModificar.setEnabled(b);
        btnEliminar.setEnabled(b);
    }

    private boolean camposIncompletos() {
        if (etOrden.getText().toString().equals("") ||
                etPlaca.getText().toString().equals("") ||
                etRFC.getText().toString().equals("") ||
                etKM.getText().toString().equals("") ||
                etPrecio.getText().toString().equals("") ||
                etFecha.getText().toString().equals("")) {
            return true;
        }
        return false;
    }

    private void limpiarCampos() {
        etOrden.setText(null);
        etPlaca.setText(null);
        etRFC.setText(null);
        etKM.setText(null);
        etPrecio.setText(null);
        etFecha.setText(null);
        etOrden.requestFocus();
    }

}
