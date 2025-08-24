package com.example.ED_P1_Grupo02.Vista;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Tdas.Edge;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;

import java.util.LinkedList;
import java.util.stream.Collectors;

public class RutaCortaActivity extends AppCompatActivity {

    private EditText etSalida, etDestino;
    private TextView tvRuta, tvDistancia;
    private Button btnCalcular;

    private GraphAL<Aeropuerto, Integer> grafo; // Grafo con Aeropuerto como vértice

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_corta);

        etSalida = findViewById(R.id.etAeropuertoSalida);
        etDestino = findViewById(R.id.etAeropuertoDestino);
        tvRuta = findViewById(R.id.tvRuta);
        tvDistancia = findViewById(R.id.tvDistancia);
        btnCalcular = findViewById(R.id.btnCalcularRuta);

        // Recibir el grafo desde el Intent
        grafo = (GraphAL<Aeropuerto, Integer>) getIntent().getSerializableExtra("grafo");
        if (grafo == null || grafo.getVertices() == null || grafo.getVertices().isEmpty()) {
            Toast.makeText(this, "Error: Grafo no recibido o vacío", Toast.LENGTH_LONG).show();
            btnCalcular.setEnabled(false);
            return;
        }

        btnCalcular.setOnClickListener(v -> {
            try {
                String codigoSalida = etSalida.getText().toString().trim().toUpperCase();
                String codigoDestino = etDestino.getText().toString().trim().toUpperCase();

                if (codigoSalida.isEmpty() || codigoDestino.isEmpty()) {
                    Toast.makeText(this, "Ingrese ambos aeropuertos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (codigoSalida.equals(codigoDestino)) {
                    Toast.makeText(this, "Los aeropuertos no pueden ser iguales", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Buscar objetos Aeropuerto en el grafo
                Aeropuerto salida = grafo.getVertices().stream()
                        .map(Vertex::getContent)
                        .filter(a -> a.getCodigoIATA().equals(codigoSalida))
                        .findFirst()
                        .orElse(null);

                Aeropuerto destino = grafo.getVertices().stream()
                        .map(Vertex::getContent)
                        .filter(a -> a.getCodigoIATA().equals(codigoDestino))
                        .findFirst()
                        .orElse(null);

                if (salida == null || destino == null) {
                    Toast.makeText(this, "Aeropuerto no encontrado", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Calcular ruta usando Dijkstra
                LinkedList<Aeropuerto> ruta = grafo.dijkstra(salida, destino);

                if (ruta != null && !ruta.isEmpty()) {
                    tvRuta.setText("Ruta: " + ruta.stream()
                            .map(Aeropuerto::getCodigoIATA)
                            .collect(Collectors.joining(" → ")));

                    // Sumar distancia total
                    int distanciaTotal = 0;
                    for (int i = 0; i < ruta.size() - 1; i++) {
                        distanciaTotal += getPesoEntre(ruta.get(i), ruta.get(i + 1));
                    }
                    tvDistancia.setText("Distancia total: " + distanciaTotal + " km");
                } else {
                    tvRuta.setText("Ruta: No se encontró ruta.");
                    tvDistancia.setText("Distancia total: 0 km");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al calcular la ruta: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Método auxiliar para obtener el peso entre dos vértices
    private int getPesoEntre(Aeropuerto from, Aeropuerto to) {
        if (grafo == null) return 0;
        Vertex<Aeropuerto, Integer> vFrom = grafo.getVertexByContent(from);
        if (vFrom != null && vFrom.getEdges() != null) {
            for (Edge<Integer, Aeropuerto> edge : vFrom.getEdges()) {
                if (edge.getTarget() != null && edge.getTarget().getContent() != null
                        && edge.getTarget().getContent().equals(to)) {
                    return edge.getWeight();
                }
            }
        }
        return 0;
    }
}

