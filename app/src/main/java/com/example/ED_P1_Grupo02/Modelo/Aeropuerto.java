package com.example.ED_P1_Grupo02.Modelo;

import java.io.Serializable;

public class Aeropuerto implements Serializable {
    private String codigoIATA;
    private String nombre;
    private String ciudad;
    private double latitud;
    private double longitud;


    public Aeropuerto(String codigoIATA, String nombre, String ciudad) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.latitud = 0;
        this.longitud = 0;
    }

    public Aeropuerto(String codigoIATA, String nombre, String ciudad, double latitud, double longitud) {
        this.codigoIATA = codigoIATA;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    // Getters y setters
    public String getCodigoIATA() {
        return codigoIATA; }
    public void setCodigoIATA(String codigoIATA) {
        this.codigoIATA = codigoIATA; }

    public String getNombre() {
        return nombre; }
    public void setNombre(String nombre) {
        this.nombre = nombre; }

    public String getCiudad() {
        return ciudad; }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad; }

    public double getLatitud() {
        return latitud; }
    public void setLatitud(double latitud) {
        this.latitud = latitud; }

    public double getLongitud() {
        return longitud; }
    public void setLongitud(double longitud) {
        this.longitud = longitud; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Aeropuerto)) return false;
        Aeropuerto other = (Aeropuerto) obj;
        return codigoIATA != null && codigoIATA.equals(other.getCodigoIATA());
    }

    @Override
    public int hashCode() {
        return codigoIATA != null ? codigoIATA.hashCode() : 0;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
