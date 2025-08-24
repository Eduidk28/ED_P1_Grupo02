package com.example.ED_P1_Grupo02.Vista;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Controlador.ControladorAeropuerto;
import com.example.ED_P1_Grupo02.Controlador.PersistenciaGrafo;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;

import com.example.ED_P1_Grupo02.Util.GeocodingHelper;


public class AgregarAeropuertoActivity extends AppCompatActivity {

    private EditText etCodigo, etNombre, etCiudad;
    private Button btnGuardar, btnCancelar;
    private ControladorAeropuerto controladorAeropuerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_aeropuerto);
        etCodigo = findViewById(R.id.etCodigo);
        etNombre = findViewById(R.id.etNombre);
        etCiudad = findViewById(R.id.etCiudad);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);

        controladorAeropuerto = new ControladorAeropuerto(this);
        btnGuardar.setOnClickListener(v -> {
            String codigo = etCodigo.getText().toString().trim();
            String nombre = etNombre.getText().toString().trim();
            String ciudad = etCiudad.getText().toString().trim();

            String direccion = "Aeropuerto " + codigo + ", " + ciudad;
            String apiKey = "AIzaSyDi0dXjTu7H6Px90sqgbNBbE-1m0Ymi3Gc";

            GeocodingHelper.obtenerCoordenadas(direccion, apiKey, new GeocodingHelper.GeocodingCallback() {
                @Override
                public void onSuccess(double lat, double lng) {
                    Aeropuerto aeropuerto = new Aeropuerto(codigo, nombre, ciudad, lat, lng);
                    if (controladorAeropuerto.agregarAeropuerto(aeropuerto)) {
                        PersistenciaGrafo.guardar(AgregarAeropuertoActivity.this);
                    }
                    setResult(RESULT_OK);
                    finish();
                }

                @Override
                public void onFailure(String error) {
                    Toast.makeText(AgregarAeropuertoActivity.this,
                            "Error al obtener coordenadas: " + error, Toast.LENGTH_LONG).show();
                }
            });
        });

        btnCancelar.setOnClickListener(v -> finish());
    }

}
