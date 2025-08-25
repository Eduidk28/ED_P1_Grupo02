package com.example.ED_P1_Grupo02.Vista;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ED_P1_Grupo02.Controlador.ControladorAeropuerto;
import com.example.ED_P1_Grupo02.Controlador.ControladorPerfil;
import com.example.ED_P1_Grupo02.Controlador.PersistenciaGrafo;
import com.example.ED_P1_Grupo02.MainActivity;
import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;

import com.example.ED_P1_Grupo02.Tdas.Edge;
import com.example.ED_P1_Grupo02.Util.GrafoSingleton;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;
import com.example.proyecEstructura.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualizarGrafoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ControladorPerfil controladorPerfil;
    private ControladorAeropuerto controladorAeropuerto;
    private ActivityResultLauncher<Intent> agregarAeropuertoLauncher;
    private ActivityResultLauncher<Intent> agregarVueloLauncher;
    private ActivityResultLauncher<Intent> eliminarVueloLauncher;
    private ActivityResultLauncher<Intent> detalleAeropuertoLauncher;
    private Button btnCalcularRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_grafo);

        controladorPerfil = new ControladorPerfil(this);
        controladorAeropuerto = new ControladorAeropuerto(this);
        Button btnCalcularRuta = findViewById(R.id.btnCalcularRuta);
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        Button btnAgregarAeropuerto = findViewById(R.id.btnAgregarAeropuerto);
        btnAgregarAeropuerto.setOnClickListener(v ->
                agregarAeropuertoLauncher.launch(new Intent(this, AgregarAeropuertoActivity.class))
        );

        Button btnAgregarVuelo = findViewById(R.id.btnAgregarVuelo);
        btnAgregarVuelo.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarVueloActivity.class);
            agregarVueloLauncher.launch(intent);
        });

        Button btnEliminarVuelo = findViewById(R.id.btnEliminarVuelo);
        btnEliminarVuelo.setOnClickListener(v -> {
            Intent intent = new Intent(this, EliminarVueloActivity.class);
            eliminarVueloLauncher.launch(intent);
        });
        btnCalcularRuta.setOnClickListener(v -> {
            Intent intent = new Intent(VisualizarGrafoActivity.this, RutaCortaActivity.class);
            intent.putExtra("grafo", controladorAeropuerto.getGrafo());

            startActivity(intent);
        });

        agregarAeropuertoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) dibujarGrafo();
                }
        );

        agregarVueloLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) dibujarGrafo();
                }
        );

        eliminarVueloLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && mMap != null) {
                        mMap.clear();
                        onMapReady(mMap);
                    }
                }
        );

        detalleAeropuertoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && mMap != null) {
                        mMap.clear();
                        onMapReady(mMap);
                    }
                }
        );

        PersistenciaGrafo.cargar(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if (mapFragment != null) mapFragment.getMapAsync(this);
    }

    private void dibujarGrafo() {
        if (mMap == null) return;
        mMap.clear();

        GraphAL<Aeropuerto, Vuelo> grafo = GrafoSingleton.getInstance().getGrafo();
        if (grafo == null || grafo.getVertices().isEmpty()) return;


        Map<String, List<Edge<Vuelo, Aeropuerto>>> aristasPorPar = new HashMap<>();
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            for (Edge<Vuelo, Aeropuerto> e : v.getEdges()) {
                String key = e.getSource().getContent().getCodigoIATA() + "->" + e.getTarget().getContent().getCodigoIATA();
                aristasPorPar.computeIfAbsent(key, k -> new ArrayList<>()).add(e);
            }
        }


        for (Map.Entry<String, List<Edge<Vuelo, Aeropuerto>>> entry : aristasPorPar.entrySet()) {
            List<Edge<Vuelo, Aeropuerto>> edges = entry.getValue();
            int total = edges.size();
            for (int i = 0; i < total; i++) {
                Edge<Vuelo, Aeropuerto> e = edges.get(i);
                LatLng start = new LatLng(e.getSource().getContent().getLatitud(),
                        e.getSource().getContent().getLongitud());
                LatLng end = new LatLng(e.getTarget().getContent().getLatitud(),
                        e.getTarget().getContent().getLongitud());
                int peso = e.getData().getKilometros();
                drawEdgeWithWeightAndArrow(start, end, peso, i, total);
            }

        }

        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            Aeropuerto a = v.getContent();
            LatLng pos = new LatLng(a.getLatitud(), a.getLongitud());

            mMap.addCircle(new com.google.android.gms.maps.model.CircleOptions()
                    .center(pos)
                    .radius(10000)
                    .fillColor(Color.BLACK)
                    .strokeColor(Color.BLACK)
                    .strokeWidth(2));


            mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(a.getNombre() + " (" + a.getCodigoIATA() + ")"));
        }


        LatLng primer = new LatLng(grafo.getVertices().get(0).getContent().getLatitud(),
                grafo.getVertices().get(0).getContent().getLongitud());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(primer, 2.5f));
    }

    private void drawEdgeWithWeightAndArrow(LatLng start, LatLng end, int pesoKm, int index, int totalAristas) {
        double dx = end.longitude - start.longitude;
        double dy = end.latitude - start.latitude;
        double length = Math.sqrt(dx * dx + dy * dy);
        double ux = -dy / length;
        double uy = dx / length;

        double offset = 0.05;
        double factor = (index - (totalAristas - 1) / 2.0) * offset;

        LatLng startOffset = new LatLng(start.latitude + uy * factor, start.longitude + ux * factor);
        LatLng endOffset = new LatLng(end.latitude + uy * factor, end.longitude + ux * factor);

        mMap.addPolyline(new PolylineOptions().add(startOffset, endOffset).width(5).color(Color.BLUE));

        drawArrow(startOffset, endOffset);

        LatLng mid = new LatLng((startOffset.latitude + endOffset.latitude) / 2,
                (startOffset.longitude + endOffset.longitude) / 2);
        mMap.addMarker(new MarkerOptions()
                .position(mid)
                .anchor(0.5f, 0.5f)
                .title(pesoKm + " km")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .flat(true)
                .alpha(0.8f)
        );
    }

    private void drawArrow(LatLng start, LatLng end) {
        double factor = 0.99;
        double arrowLat = start.latitude + factor * (end.latitude - start.latitude);
        double arrowLng = start.longitude + factor * (end.longitude - start.longitude);
        LatLng arrowPos = new LatLng(arrowLat, arrowLng);

        double angle = Math.atan2(end.latitude - start.latitude, end.longitude - start.longitude);
        double arrowLength = 0.3;
        double arrowAngle = Math.PI / 8;

        LatLng p1 = new LatLng(
                arrowPos.latitude - arrowLength * Math.sin(angle - arrowAngle),
                arrowPos.longitude - arrowLength * Math.cos(angle - arrowAngle)
        );
        LatLng p2 = new LatLng(
                arrowPos.latitude - arrowLength * Math.sin(angle + arrowAngle),
                arrowPos.longitude - arrowLength * Math.cos(angle + arrowAngle)
        );

        mMap.addPolyline(new PolylineOptions()
                .add(arrowPos, p1)
                .add(arrowPos, p2)
                .width(5)
                .color(Color.BLUE));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        dibujarGrafo();

        mMap.setOnMarkerClickListener(marker -> {
            String title = marker.getTitle();
            if (title == null || !title.contains("(") || !title.contains(")")) return false;

            String codigo = title.substring(title.indexOf("(") + 1, title.indexOf(")"));
            Vertex<Aeropuerto, Vuelo> vertice = GrafoSingleton.getInstance()
                    .getGrafo()
                    .getVertexByContent(new Aeropuerto(codigo, "", ""));
            if (vertice == null) return false;

            Aeropuerto aeropuerto = vertice.getContent();

            ArrayList<String> aerolineas = new ArrayList<>();
            for (Edge<Vuelo, Aeropuerto> e : vertice.getEdges()) {
                String aerolinea = e.getData().getAerolinea();
                if (!aerolineas.contains(aerolinea)) {
                    aerolineas.add(aerolinea);
                }
            }

            Intent intent = new Intent(this, DetalleConexionesActivity.class);
            intent.putExtra("codigoIATA", aeropuerto.getCodigoIATA());
            intent.putExtra("nombreAeropuerto", aeropuerto.getNombre());
            intent.putExtra("ciudadAeropuerto", aeropuerto.getCiudad());
            intent.putExtra("gradoSalida", GrafoSingleton.getInstance().getGrafo().gradoSalida(aeropuerto));
            intent.putExtra("gradoEntrada", GrafoSingleton.getInstance().getGrafo().gradoEntrada(aeropuerto));
            intent.putExtra("grado", GrafoSingleton.getInstance().getGrafo().gradoTotal(aeropuerto));
            intent.putStringArrayListExtra("aerolineas", aerolineas);

            detalleAeropuertoLauncher.launch(intent);
            return true;
        });
    }
}