package com.example.ED_P1_Grupo02.Controlador;

import android.content.Context;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;
import com.example.ED_P1_Grupo02.Modelo.Vuelo;
import com.example.ED_P1_Grupo02.Util.GrafoSingleton;
import com.example.ED_P1_Grupo02.Tdas.GraphAL;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenciaGrafo {
    public static void guardar(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("grafo.dat", Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(GrafoSingleton.getInstance().getGrafo());
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cargar(Context context) {
        try {
            FileInputStream fis = context.openFileInput("grafo.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            GraphAL<Aeropuerto, Vuelo> grafo = (GraphAL<Aeropuerto, Vuelo>) ois.readObject();
            GrafoSingleton.getInstance().setGrafo(grafo);
            ois.close();
            fis.close();
        } catch (Exception e) {

        }
    }

}
