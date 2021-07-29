package com.example.easytable;

import java.util.ArrayList;

public class ListadoPlatillosOrdenadosPojo {

    private long cantidad;
    private String nombrePlatillo, especificaciones;

    public ListadoPlatillosOrdenadosPojo(){

    }


    public ListadoPlatillosOrdenadosPojo(long cantidad, String nombrePlatillo, String especificaciones) {
        this.cantidad = cantidad;
        this.nombrePlatillo = nombrePlatillo;
        this.especificaciones = especificaciones;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
        this.nombrePlatillo = nombrePlatillo;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }
}
