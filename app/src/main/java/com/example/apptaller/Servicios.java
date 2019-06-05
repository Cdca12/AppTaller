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
    private Button btnAñadir, btnConsultar, btnModificar, btnEliminar;

    // Conexión a base de datos
    private BaseDeDatos conexion;
    private SQLiteDatabase bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios);

        setTitle("Servicios");

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

        btnAñadir = (Button) findViewById(R.id.btnPrimeraConsulta);
        btnConsultar = (Button) findViewById(R.id.btnSegundaConsulta);
        btnModificar = (Button) findViewById(R.id.btnModificar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
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
    }

    private void añadirServicio() {
        if (camposIncompletos()) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Error: Campos incompletos");
            alertDialog.setMessage("Favor de rellenar todos los campos de texto");
            alertDialog.show();
            return;
        }

        // Validar que exista en las tablas
        if(!existePersona()) {
            Toast toast = Toast.makeText(this, "No existe una persona con ese RFC", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if(!existeAuto()) {
            Toast toast = Toast.makeText(this, "No existe un Auto con esa Placa", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String query = "INSERT INTO SERVICIOS " +
                "VALUES (" + etOrden.getText().toString() + ", '"
                + etPlaca.getText().toString().toUpperCase() + "', '"
                + etRFC.getText().toString().toUpperCase() + "', "
                + etKM.getText().toString() + ", "
                + etPrecio.getText().toString() + ", '"
                + etFecha.getText().toString() + "', 1);";

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
        Toast toast = Toast.makeText(this, "Servicio añadido exitosamente", Toast.LENGTH_SHORT);
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
                "WHERE Orden = " + etOrden.getText().toString().toUpperCase() + " " +
                "AND EstatusServicio = 1;";

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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirmar cambios");
        alertDialog.setMessage("¿Desea confirmar los cambios?\nNo se modificará la Placa ni el RFC");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE SERVICIOS " +
                    "SET KM = " + etKM.getText().toString() + ", " +
                    "Precio = " + etPrecio.getText().toString() + ", " +
                    "Fecha = '" + etFecha.getText().toString() + "' " +
                    "WHERE Orden = " + etOrden.getText().toString() + ";";

            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado modificado el servicio", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
    }

    private void eliminarServicio() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Eliminar Servicio (" + etOrden.getText().toString() + ") del día " + etFecha.getText().toString());
        alertDialog.setMessage("¿Seguro que desea eliminar este servicio?");
        alertDialog.setNegativeButton("No", ((dialog, which) -> {
        }));
        alertDialog.setPositiveButton("Si", ((dialog, which) -> {
            String query = "UPDATE SERVICIOS " +
                    "SET EstatusServicio = 0 " +
                    "WHERE Orden = " + etOrden.getText().toString() + ";";
            bd = conexion.getWritableDatabase();
            bd.execSQL(query);
            Toast toast = Toast.makeText(this, "Se ha dado de baja el servicio", Toast.LENGTH_SHORT);
            toast.show();
            bd.close();
            limpiarCampos();
        }));
        alertDialog.show();
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

    private boolean existePersona() {
        String query = "SELECT RFC FROM PERSONAS " +
                "WHERE RFC = '" + etRFC.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);
        return (cursor.getCount() == 0) ? false : true;
    }

    private boolean existeAuto() {
        String query = "SELECT Placa FROM AUTOS " +
                "WHERE Placa = '" + etPlaca.getText().toString().toUpperCase() + "';";
        bd = conexion.getWritableDatabase();
        Cursor cursor = bd.rawQuery(query, null);
        return (cursor.getCount() == 0) ? false : true;
    }

}
