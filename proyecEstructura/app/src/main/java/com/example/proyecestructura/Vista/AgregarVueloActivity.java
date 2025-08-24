package com.example.ED_P1_Grupo02.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Controlador.ControladorVuelo;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;



import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AgregarVueloActivity extends AppCompatActivity {

    private Spinner spOrigen, spDestino;
    private EditText etDistancia, etAerolinea;
    private Button btnAgregar;
    private ControladorVuelo controladorVuelo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vuelo);

        spOrigen = findViewById(R.id.spOrigen);
        spDestino = findViewById(R.id.spDestino);
        etDistancia = findViewById(R.id.etDistancia);
        etAerolinea = findViewById(R.id.etAerolinea);
        btnAgregar = findViewById(R.id.btnAgregarVuelo);

        controladorVuelo = new ControladorVuelo();

        LinkedList<Aeropuerto> aeropuertos = controladorVuelo.obtenerAeropuertos();
        ArrayAdapter<Aeropuerto> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aeropuertos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOrigen.setAdapter(adapter);

        // Cuando se seleccione origen, filtrar destinos
        spOrigen.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                Aeropuerto origen = (Aeropuerto) spOrigen.getSelectedItem();
                List<Aeropuerto> destinos = aeropuertos.stream()
                        .filter(a -> !a.equals(origen))
                        .collect(Collectors.toList());
                ArrayAdapter<Aeropuerto> destAdapter = new ArrayAdapter<>(AgregarVueloActivity.this, android.R.layout.simple_spinner_item, destinos);
                destAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spDestino.setAdapter(destAdapter);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });



        btnAgregar.setOnClickListener(v -> {
            Aeropuerto origen = (Aeropuerto) spOrigen.getSelectedItem();
            Aeropuerto destino = (Aeropuerto) spDestino.getSelectedItem();
            String distanciaStr = etDistancia.getText().toString();
            String aerolinea = etAerolinea.getText().toString();

            if (origen == null || destino == null || distanciaStr.isEmpty() || aerolinea.isEmpty()) {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int distancia;
            try {
                distancia = Integer.parseInt(distanciaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Distancia inválida", Toast.LENGTH_SHORT).show();
                return;
            }

            Vuelo vuelo = new Vuelo(aerolinea,distancia);
            boolean exito = controladorVuelo.agregarVuelo(origen, destino, distancia, vuelo,this);
            if (exito) {
                Toast.makeText(this, "Vuelo agregado correctamente", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al agregar vuelo", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(AgregarVueloActivity.this, VisualizarGrafoActivity.class);
            startActivity(intent);
            setResult(RESULT_OK);
            finish();
        });
    }
}
