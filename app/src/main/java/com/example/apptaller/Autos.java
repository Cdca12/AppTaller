package com.example.apptaller;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Autos extends AppCompatActivity {
    private EditText etMarca, etModelo, etAño, etPlaca;
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar, btnNuke;

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
        btnNuke = (Button) findViewById(R.id.btnNuke);
    }
    private void addListeners() {
        etPlaca.addTextChangedListener(new TextWatcher() {
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
            añadirAuto();
        });
        btnConsultar.setOnClickListener(view -> {
            consultarPersonaPorPlaca();
        });
        btnModificar.setOnClickListener(view -> {
            modificarAuto();
        });
        btnEliminar.setOnClickListener(view -> {
            eliminarAuto();
        });
        btnNuke.setOnClickListener(view -> {
            eliminarTodo();
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
        // Conexión a la bd establecida
        return true;
    }

    private boolean camposIncompletos() {
        if (etMarca.getText().toString().equals("") ||
                etModelo.getText().toString().equals("") ||
                etAño.getText().toString().equals("") ||
                etPlaca.getText().toString().equals("")) {
            return true;
        }
        return false;
    }
    private void limpiarCampos() {
        etMarca.setText(null);
        etModelo.setText(null);
        etAño.setText(null);
        etPlaca.setText(null);
        etMarca.requestFocus();
    }
    private void añadirAuto() {
        if (camposIncompletos()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error: Campos incompletos");
            alertDialog.setMessage("Favor de rellenar todos los campos de texto");
            alertDialog.show();
            return;
        }
        String query = "INSERT INTO AUTOS " +
                "VALUES ('" + etPlaca.getText().toString().toUpperCase() + "', '"
                + etMarca.getText().toString() + "', '"
                + etModelo.getText().toString() + "', '"
                + etAño.getText().toString() + "', 1);";
        bd = conexion.getWritableDatabase();

        // ID Repetido, entra excepcion
        try {
            bd.execSQL(query);
        } catch (SQLException e) {
            if (estaDadoBaja()) {
                darDeAlta();
                return;
            }
            Toast toast = Toast.makeText(this, "Ya existe una auto con esta Placa", Toast.LENGTH_SHORT);
            toast.show();
            return;

        } finally {
            bd.close(); // Cerrar conexión a Base de Datos por seguridad
        }
        limpiarCampos();
        Toast toast = Toast.makeText(this, "Auto añado exitosamente", Toast.LENGTH_SHORT);
        toast.show();
    }
    private boolean estaDadoBaja() {
        String query = "SELECT EstatusAuto FROM AUTOS " +
                "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);
        cursor.moveToFirst();
        return (Integer.parseInt(cursor.getString(0)) == 0) ? true : false;
    }
    private void darDeAlta() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Auto dado de baja");
        alertDialog.setMessage("Ya existe un Auto con esa Placa dado de baja, ¿quisieras darla de alta con los nuevos datos?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE AUTOS " +
                    "SET EstatusAuto = 1, " +
                    "Marca = '" + etMarca.getText().toString() + "', " +
                    "Modelo = '" + etModelo.getText().toString() + "', " +
                    "Año = '" + etAño.getText().toString() + "' " +
                    "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado de alta el auto", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void consultarPersonaPorPlaca() {
        // Valida campo vacío
        if (etPlaca.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Favor de ingresar una placa", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String query = "SELECT * FROM AUTOS " +
                "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        // Validar que haya traido datos
        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(this, "No existe un Auto con esa Placa", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Valida que no esté dado de baja
        if (estaDadoBaja()) {
            Toast toast = Toast.makeText(this, "El auto está dado de baja", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        limpiarCampos();

        // Rellenar datos
        cursor.moveToFirst();
        etPlaca.setText(cursor.getString(0));
        etMarca.setText(cursor.getString(1));
        etModelo.setText(cursor.getString(2));
        etAño.setText(cursor.getString(3));

        // Habilitar botones
        habilitarBotones(true);

        bd.close();
    }
    private void habilitarBotones(boolean b) {
        btnModificar.setEnabled(b);
        btnEliminar.setEnabled(b);
    }

    private void modificarAuto() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmar cambios");
        alertDialog.setMessage("¿Desea confirmar los cambios?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE AUTOS " +
                    "SET Placa = '" + etPlaca.getText().toString().toUpperCase() + "', " +
                    "Marca = '" + etMarca.getText().toString() + "', " +
                    "Modelo = '" + etModelo.getText().toString() + "', " +
                    "Año = '" + etAño.getText().toString() + "' " +
                    "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado modificado el auto", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void eliminarAuto() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Eliminar " + etMarca.getText().toString() + " " + etModelo.getText().toString());
        alertDialog.setMessage("¿Seguro que desea eliminar este auto?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE AUTOS " +
                    "SET EstatusAuto = 0 " +
                    "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado de baja el auto", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void eliminarTodo() {
        String query = "DELETE FROM AUTOS;";
        bd = conexion.getWritableDatabase();
        bd.execSQL(query);
        Toast toast = Toast.makeText(this, "Se han eliminado todos los autos", Toast.LENGTH_SHORT);
        toast.show();
        bd.close();
    }
}
