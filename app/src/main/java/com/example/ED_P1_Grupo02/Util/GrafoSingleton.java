package com.example.ED_P1_Grupo02.Util;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;

public class GrafoSingleton {
    private static GrafoSingleton instance;
    private GraphAL<Aeropuerto, Vuelo> grafo;

    private GrafoSingleton() {
        grafo = new GraphAL<>(true, new AeropuertoComparator());
        inicializarGrafoPrueba();
    }

    public void setGrafo(GraphAL<Aeropuerto, Vuelo> grafo) {
        this.grafo = grafo;
    }

    public static void setInstance(GrafoSingleton instance) {
        GrafoSingleton.instance = instance;
    }

    public static GrafoSingleton getInstance() {
        if (instance == null) {
            instance = new GrafoSingleton();
        }
        return instance;
    }

    public GraphAL<Aeropuerto, Vuelo> getGrafo() {
        return grafo;
    }

    private void inicializarGrafoPrueba() {
        Aeropuerto daxing = new Aeropuerto("PKX", "Aeropuerto Daxing", "Beijing", 39.509, 116.410);
        Aeropuerto jfk = new Aeropuerto("JFK", "John F. Kennedy", "New York", 40.641, -73.778);
        Aeropuerto lax = new Aeropuerto("LAX", "Los Angeles Intl", "Los Angeles", 33.941, -118.408);
        Aeropuerto lhr = new Aeropuerto("LHR", "Heathrow", "London", 51.470, -0.454);
        Aeropuerto cdg = new Aeropuerto("CDG", "Charles de Gaulle", "Paris", 49.009, 2.547);
        Aeropuerto hnd = new Aeropuerto("HND", "Haneda", "Tokyo", 35.549, 139.779);
        Aeropuerto dxb = new Aeropuerto("DXB", "Dubai Intl", "Dubai", 25.253, 55.365);
        Aeropuerto fra = new Aeropuerto("FRA", "Frankfurt", "Frankfurt", 50.037, 8.562);

        // Agregar al grafo
        grafo.addVertex(daxing);
        grafo.addVertex(jfk);
        grafo.addVertex(lax);
        grafo.addVertex(lhr);
        grafo.addVertex(cdg);
        grafo.addVertex(hnd);
        grafo.addVertex(dxb);
        grafo.addVertex(fra);

        // Conexiones

        grafo.connect(daxing, lhr, 200000, new Vuelo("Air China", 200000));
        grafo.connect(daxing, dxb, 13000, new Vuelo("Air China", 13000));
        grafo.connect(dxb, fra, 12000, new Vuelo("Air China", 12000));
        grafo.connect(fra, lhr, 1000, new Vuelo("Air China", 1000));
        grafo.connect(daxing, jfk, 1000, new Vuelo("Air China", 1000));
        grafo.connect(jfk, lax, 4000, new Vuelo("American Airlines", 4000));
        grafo.connect(lax , lhr , 5000 ,new Vuelo ("American Airlines" , 5000));
        grafo.connect(fra, lhr, 150000, new Vuelo("France Airlines", 150000));
        grafo.connect(daxing, cdg, 8200, new Vuelo("Air France", 8200));
        grafo.connect(daxing, hnd, 2100, new Vuelo("Japan Airlines", 2100));
        grafo.connect(lhr, jfk, 5500, new Vuelo("British Airways", 5500));
        grafo.connect(cdg, fra, 450, new Vuelo("Air France", 450));
        grafo.connect(fra, cdg, 450, new Vuelo("Lufthansa", 450));
        grafo.connect(hnd, daxing, 2100, new Vuelo("AirChina" ,2100));

    }
}

