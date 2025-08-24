package com.example.ED_P1_Grupo02.Modelo;

import java.io.Serializable;

public class Vuelo implements Serializable {
    private String aerolinea;
    private int kilometros;

    public Vuelo(String aerolinea, int kilometros) {
        this.aerolinea = aerolinea;
        this.kilometros = kilometros;
    }

    public String getAerolinea() {

        return aerolinea;
    }

    public void setAerolinea(String aerolinea) {

        this.aerolinea = aerolinea;
    }

    public int getKilometros() {

        return kilometros;
    }

    public void setKilometros(int kilometros) {

        this.kilometros = kilometros;
    }


    @Override
    public String toString() {
        return "Vuelo{" +
                "aerolinea='" + aerolinea + '\'' +
                ", duracionMinutos=" + kilometros +
                '}';
    }


}
