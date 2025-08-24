package com.example.ED_P1_Grupo02.Vista;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ED_P1_Grupo02.Controlador.ControladorVuelo;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.proyecEstructura.R;


import java.util.LinkedList;

public class EliminarVueloActivity extends AppCompatActivity {

    private Spinner spAeropuerto, spDestino, spAerolinea;
    private Button btnEliminar, btnRegresar;
    private ControladorVuelo controladorVuelo;
    private LinkedList<Aeropuerto> listaAeropuertos;
    private LinkedList<String> listaAerolineas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_vuelo);

        spAeropuerto = findViewById(R.id.spAeropuerto);
        spDestino = findViewById(R.id.spAeropuertoDestino);
        spAerolinea = findViewById(R.id.spAerolinea);
        Button btnEliminar = findViewById(R.id.btnEliminarVueloConfirm);
        Button btnRegresar = findViewById(R.id.btnRegresar);

        controladorVuelo = new ControladorVuelo();
        listaAeropuertos = controladorVuelo.obtenerAeropuertos();

        // Spinner de origen
        ArrayAdapter<Aeropuerto> adapterOrigen = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaAeropuertos);
        adapterOrigen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAeropuerto.setAdapter(adapterOrigen);

        // Spinner de destino
        ArrayAdapter<Aeropuerto> adapterDestino = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaAeropuertos);
        adapterDestino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDestino.setAdapter(adapterDestino);

        // Cuando se seleccione un aeropuerto origen, llenar aerolíneas disponibles desde ese origen
        spAeropuerto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Aeropuerto aeropuerto = listaAeropuertos.get(position);

                // Llenar aerolineas
                listaAerolineas = controladorVuelo.obtenerAerolineasOrdenadas(aeropuerto);
                ArrayAdapter<String> adapterAerolinea = new ArrayAdapter<>(EliminarVueloActivity.this,
                        android.R.layout.simple_spinner_item, listaAerolineas);
                adapterAerolinea.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spAerolinea.setAdapter(adapterAerolinea);

                // Llenar destinos disponibles desde este aeropuerto
                LinkedList<Aeropuerto> listaDestinos = controladorVuelo.obtenerDestinosDesdeAeropuerto(aeropuerto);
                ArrayAdapter<Aeropuerto> adapterDestino = new ArrayAdapter<>(EliminarVueloActivity.this,
                        android.R.layout.simple_spinner_item, listaDestinos);
                adapterDestino.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDestino.setAdapter(adapterDestino);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Botón eliminar vuelo considerando origen, destino y aerolínea
        btnEliminar.setOnClickListener(v -> {
            Aeropuerto origen = (Aeropuerto) spAeropuerto.getSelectedItem();
            Aeropuerto destino = (Aeropuerto) spDestino.getSelectedItem();
            String aerolinea = (String) spAerolinea.getSelectedItem();

            if (origen != null && destino != null && aerolinea != null) {
                boolean eliminado = controladorVuelo.eliminarVuelo(origen, destino, aerolinea,this);
                if (eliminado) {
                    Toast.makeText(this, "Vuelo eliminado", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "No se pudo eliminar el vuelo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegresar.setOnClickListener(v -> finish());
    }
}