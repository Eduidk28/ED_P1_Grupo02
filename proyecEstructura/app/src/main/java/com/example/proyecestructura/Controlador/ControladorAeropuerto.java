package com.example.ED_P1_Grupo02.Controlador;

import android.content.Context;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Tdas.GrafoSingleton;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Tdas.Vertex;

public class ControladorAeropuerto {
    private GraphAL<Aeropuerto, Vuelo> grafo;
    private Context context;


    public ControladorAeropuerto(Context context) {
        this.context = context;
        this.grafo = GrafoSingleton.getInstance().getGrafo();
    }


    public boolean agregarAeropuerto(Aeropuerto aeropuerto) {
        if (grafo.getVertexByContent(aeropuerto) != null) {
            return false; // Ya existe
        }
        grafo.addVertex(aeropuerto);
        return true;
    }

    public boolean eliminarAeropuertoPorNombre(String nombreAeropuerto, Context context) {
        GraphAL<Aeropuerto, Vuelo> grafo = GrafoSingleton.getInstance().getGrafo();

        if (nombreAeropuerto == null) return false;

        // Buscar aeropuerto por nombre
        Aeropuerto encontrado = null;
        for (Vertex<Aeropuerto, Vuelo> v : grafo.getVertices()) {
            Aeropuerto a = v.getContent();
            if (a != null && nombreAeropuerto.equals(a.getNombre())) {
                encontrado = a;
                break;
            }
        }

        if (encontrado != null) {
            grafo.removeVertex(encontrado); // elimina el vértice
            PersistenciaGrafo.guardar(context); // guarda los cambios
            return true; // eliminado correctamente
        }

        return false; // no se encontró
    }

    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }
}