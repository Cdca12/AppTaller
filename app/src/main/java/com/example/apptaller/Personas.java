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

public class Personas extends AppCompatActivity {

    private EditText etNombre, etRFC, etCiudad;
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar, btnNuke;

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
        btnNuke = (Button) findViewById(R.id.btnNuke);
    }

    private void addListeners() {
        etRFC.addTextChangedListener(new TextWatcher() {
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
            añadirPersona();
        });
        btnConsultar.setOnClickListener(view -> {
            consultarPersonaPorRFC();
        });
        btnModificar.setOnClickListener(view -> {
            modificarPersona();
        });
        btnEliminar.setOnClickListener(view -> {
            eliminarPersona();
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
        String query = "INSERT INTO PERSONAS " +
                "VALUES ('" + etRFC.getText().toString().toUpperCase() + "', '"
                + etNombre.getText().toString() + "', '"
                + etCiudad.getText().toString() + "', 1);";

        bd = conexion.getWritableDatabase();

        // ID Repetido, entra excepcion
        try {
            bd.execSQL(query);
        } catch (SQLException e) {
            if (estaDadoBaja()) {
                darDeAlta();
                return;
            }
                Toast toast = Toast.makeText(this, "Ya existe una persona con ese RFC", Toast.LENGTH_SHORT);
                toast.show();
                return;

        } finally {
            bd.close(); // Cerrar conexión a Base de Datos por seguridad
        }
        limpiarCampos();
        Toast toast = Toast.makeText(this, "Persona añadida exitosamente", Toast.LENGTH_SHORT);
        toast.show();
    }

    private boolean estaDadoBaja() {
        String query = "SELECT EstatusPersona FROM PERSONAS " +
                "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);
        cursor.moveToFirst();
        return (Integer.parseInt(cursor.getString(0)) == 0) ? true : false;
    }

    private void darDeAlta() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Persona dada de baja");
        alertDialog.setMessage("Ya existe una persona con ese RFC dada de baja, ¿quisieras darla de alta con los nuevos datos?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE PERSONAS " +
                    "SET EstatusPersona = 1, " +
                    "Nombre = '" + etNombre.getText().toString() + "', " +
                    "Ciudad = '" + etCiudad.getText().toString() + "' " +
                    "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado de alta a la persona", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void consultarPersonaPorRFC() {
        // Valida campo vacío
        if (etRFC.getText().toString().equals("")) {
            Toast toast = Toast.makeText(this, "Favor de ingresar un RFC", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String query = "SELECT * FROM PERSONAS " +
                "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);

        // Validar que haya traido datos
        if (cursor.getCount() == 0) {
            Toast toast = Toast.makeText(this, "No existe una persona con ese RFC", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Valida que no esté dado de baja
        if (estaDadoBaja()) {
            Toast toast = Toast.makeText(this, "La persona está dada de baja", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        // Rellenar datos
        cursor.moveToFirst();
        etRFC.setText(cursor.getString(0));
        etNombre.setText(cursor.getString(1));
        etCiudad.setText(cursor.getString(2));

        // Habilitar botones
        habilitarBotones(true);

        bd.close();
    }

    private void habilitarBotones(boolean b) {
        btnModificar.setEnabled(b);
        btnEliminar.setEnabled(b);
    }

    private void eliminarPersona() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Eliminar " + etNombre.getText().toString());
        alertDialog.setMessage("¿Seguro que desea eliminar a esta persona?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE PERSONAS " +
                    "SET EstatusPersona = 0 " +
                    "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado de baja a la persona", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void modificarPersona() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmar cambios");
        alertDialog.setMessage("¿Desea confirmar los cambios?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE PERSONAS " +
                    "SET RFC = '" + etRFC.getText().toString().toUpperCase() + "', " +
                    "Nombre = '" + etNombre.getText().toString() + "', " +
                    "Ciudad = '" + etCiudad.getText().toString() + "' " +
                    "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado modificado a la persona", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void eliminarTodo() {
        String query = "DELETE FROM PERSONAS;";
        bd = conexion.getWritableDatabase();
        bd.execSQL(query);
        Toast toast = Toast.makeText(this, "Se han eliminado todas las personas", Toast.LENGTH_SHORT);
        toast.show();
        bd.close();
    }
}
