package com.example.ED_P1_Grupo02.Vista;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecEstructura.R;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Tdas.Edge;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;



public class RutaCortaActivity extends AppCompatActivity {

    private ListView lvSalida, lvDestino;
    private TextView tvRuta, tvDistancia;
    private Button btnCalcular;

    private GraphAL<Aeropuerto, Integer> grafo;
    private ArrayList<Aeropuerto> listaAeropuertos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_corta);

        lvSalida = findViewById(R.id.lvAeropuertoSalida);
        lvDestino = findViewById(R.id.lvAeropuertoDestino);
        tvRuta = findViewById(R.id.tvRuta);
        tvDistancia = findViewById(R.id.tvDistancia);
        btnCalcular = findViewById(R.id.btnCalcularRuta);


        tvRuta.setMovementMethod(new ScrollingMovementMethod());
        tvDistancia.setMovementMethod(new ScrollingMovementMethod());


        grafo = (GraphAL<Aeropuerto, Integer>) getIntent().getSerializableExtra("grafo");
        if (grafo == null || grafo.getVertices() == null || grafo.getVertices().isEmpty()) {
            Toast.makeText(this, "Error: Grafo no recibido o vacío", Toast.LENGTH_LONG).show();
            btnCalcular.setEnabled(false);
            return;
        }


        listaAeropuertos = new ArrayList<>();
        ArrayList<String> listaNombres = new ArrayList<>();
        for (Vertex<Aeropuerto, Integer> v : grafo.getVertices()) {
            listaAeropuertos.add(v.getContent());
            listaNombres.add(v.getContent().getCodigoIATA() + " - " + v.getContent().getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice, listaNombres);
        lvSalida.setAdapter(adapter);
        lvDestino.setAdapter(adapter);

        btnCalcular.setOnClickListener(v -> {
            int posSalida = lvSalida.getCheckedItemPosition();
            int posDestino = lvDestino.getCheckedItemPosition();

            if (posSalida == AdapterView.INVALID_POSITION || posDestino == AdapterView.INVALID_POSITION) {
                Toast.makeText(this, "Seleccione ambos aeropuertos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (posSalida == posDestino) {
                Toast.makeText(this, "El aeropuerto de salida y destino no pueden ser iguales", Toast.LENGTH_SHORT).show();
                return;
            }

            Aeropuerto salida = listaAeropuertos.get(posSalida);
            Aeropuerto destino = listaAeropuertos.get(posDestino);

            try {

                LinkedList<Aeropuerto> ruta = grafo.dijkstra(salida, destino);

                if (ruta != null && !ruta.isEmpty()) {
                    tvRuta.setText("Ruta: " + ruta.stream()
                            .map(Aeropuerto::getCodigoIATA)
                            .collect(Collectors.joining(" → ")));


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

