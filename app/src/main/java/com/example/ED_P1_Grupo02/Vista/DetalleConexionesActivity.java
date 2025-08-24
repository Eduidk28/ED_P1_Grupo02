package com.example.ED_P1_Grupo02.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ED_P1_Grupo02.Controlador.ControladorAeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.proyecEstructura.R;


import java.util.ArrayList;

public class DetalleConexionesActivity extends AppCompatActivity {
    private ControladorAeropuerto controladorAeropuerto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_conexiones);

        controladorAeropuerto = new ControladorAeropuerto(this);

        TextView tvNombreAeropuerto = findViewById(R.id.tvNombreAeropuerto);
        TextView tvConexionesSalida = findViewById(R.id.tvConexionesSalida);
        TextView tvConexionesEntrada = findViewById(R.id.tvConexionesEntrada);
        TextView tvNumConexiones = findViewById(R.id.tvNumConexiones);
        TextView tvListaAerolineas = findViewById(R.id.tvListaAerolineas);
        Button btnEliminar = findViewById(R.id.btnEliminarAeropuerto);
        Button btnRegresar = findViewById(R.id.btnRegresar);


        String codigoIATA = getIntent().getStringExtra("codigoIATA");
        String nombreAeropuerto = getIntent().getStringExtra("nombreAeropuerto");
        int gradoSalida = getIntent().getIntExtra("gradoSalida", 0);
        int gradoEntrada = getIntent().getIntExtra("gradoEntrada", 0);
        int gradoTotal = getIntent().getIntExtra("grado", 0);
        ArrayList<String> aerolineas = getIntent().getStringArrayListExtra("aerolineas");

        tvNombreAeropuerto.setText(nombreAeropuerto);
        tvConexionesSalida.setText("Conexiones de salida: " + gradoSalida);
        tvConexionesEntrada.setText("Conexiones de entrada: " + gradoEntrada);
        tvNumConexiones.setText("Número de conexiones en total: " + gradoTotal);

        if (aerolineas != null && !aerolineas.isEmpty()) {
            tvListaAerolineas.setText("Aerolíneas:\n" + String.join("\n", aerolineas));
        } else {
            tvListaAerolineas.setText("No hay aerolíneas registradas");
        }

        btnEliminar.setOnClickListener(v -> {
            boolean exito = controladorAeropuerto.eliminarAeropuertoPorNombre(nombreAeropuerto, this);
            if (exito) {
                Toast.makeText(this, "Aeropuerto eliminado correctamente", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error: No se pudo eliminar el aeropuerto", Toast.LENGTH_SHORT).show();
            }
        });

        btnRegresar.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleConexionesActivity.this, VisualizarGrafoActivity.class);
            startActivity(intent);
            finish();
        });

    }

}
