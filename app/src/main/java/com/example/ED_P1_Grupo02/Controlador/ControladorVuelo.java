package com.example.ED_P1_Grupo02.Controlador;

import android.content.Context;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Tdas.BSTree;
import com.example.ED_P1_Grupo02.Tdas.Edge;
import com.example.ED_P1_Grupo02.Util.GrafoSingleton;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;

import java.util.LinkedList;

public class ControladorVuelo {

    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Context context;

    public ControladorVuelo() {
        this.grafo = GrafoSingleton.getInstance().getGrafo();
    }

    public boolean agregarVuelo(Aeropuerto origen, Aeropuerto destino, int distancia, Vuelo vuelo, Context context) {
        if (origen == null || destino == null || vuelo == null) return false;
        boolean conectado = grafo.connect(origen, destino, distancia, vuelo);
        if (conectado) {
            PersistenciaGrafo.guardar(context);
        }
        return conectado;
    }
    public LinkedList<Aeropuerto> obtenerAeropuertos() {
        LinkedList<Aeropuerto> lista = new LinkedList<>();
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            lista.add(v.getContent());
        }
        return lista;
    }

    public LinkedList<String> obtenerAerolineasOrdenadas(Aeropuerto aeropuerto) {

        BSTree<String, String> arbol = new BSTree<>();

        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            if (v.getContent().equals(aeropuerto)) {
                for (int i = 0; i < v.getEdges().size(); i++) {
                    String aerolinea = v.getEdges().get(i).getData().getAerolinea();

                    arbol.insertNode(aerolinea, aerolinea);
                }
                break;
            }
        }
        LinkedList<String> listaOrdenada = new LinkedList<>();
        arbol.inOrden(arbol.getRoot(), listaOrdenada);

        return listaOrdenada;
    }

    public boolean eliminarVuelo(Aeropuerto origen, Aeropuerto destino, Vuelo vuelo, Context context) {
        boolean removed = grafo.disconnect(origen, destino, vuelo);

        if (removed) {
            PersistenciaGrafo.guardar(context);
        }

        return removed;
    }


    public LinkedList<Aeropuerto> obtenerDestinosDesdeAeropuerto(Aeropuerto origen) {
        LinkedList<Aeropuerto> destinos = new LinkedList<>();
        Vertex<Aeropuerto, Vuelo> v = grafo.getVertexByContent(origen);
        if (v != null) {
            for (Edge<Vuelo, Aeropuerto> e : v.getEdges()) {
                destinos.add(e.getTarget().getContent());
            }
        }
        return destinos;
    }
}
