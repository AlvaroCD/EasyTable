package com.example.easytable;

public class ListadoPlatillosOrdenadosPojo {
    private int cantidad;
    private String nombrePlatillo;

    public ListadoPlatillosOrdenadosPojo(){

    }

    public ListadoPlatillosOrdenadosPojo(int cantidad, String nombrePlatillo){
        this.cantidad = cantidad;
        this.nombrePlatillo = nombrePlatillo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
        this.nombrePlatillo = nombrePlatillo;
    }
}
