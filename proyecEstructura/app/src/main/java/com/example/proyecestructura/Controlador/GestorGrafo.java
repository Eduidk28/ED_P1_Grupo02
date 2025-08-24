package com.example.ED_P1_Grupo02.Controlador;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;
import com.example.ED_P1_Grupo02.Util.AeropuertoComparator;

public class GestorGrafo {
    private static GestorGrafo instancia;
    private GraphAL<Aeropuerto, Vuelo> grafo;

    private GestorGrafo() {
        grafo = new GraphAL<>(true, new AeropuertoComparator());
    }
    public static GestorGrafo getInstancia() {
        if (instancia == null) {
            instancia = new GestorGrafo();
        }
        return instancia;
    }
    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }
}
