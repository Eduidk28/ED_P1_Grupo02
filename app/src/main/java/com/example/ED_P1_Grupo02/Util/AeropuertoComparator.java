package com.example.ED_P1_Grupo02.Util;

import com.example.ED_P1_Grupo02.Modelo.Aeropuerto;

import java.io.Serializable;
import java.util.Comparator;

public class AeropuertoComparator implements Comparator<Aeropuerto>, Serializable {
    @Override
    public int compare(Aeropuerto a1, Aeropuerto a2) {
        return a1.getCodigoIATA().compareTo(a2.getCodigoIATA());
    }


}
