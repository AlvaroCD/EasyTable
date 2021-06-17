package com.example.easytable;

import java.util.ArrayList;

public class ListadoPlatillosOrdenadosPojo {
    private int cantidad;
    String nombrePlatillo;
    private ArrayList<String> matrizPlatillo;

    public ListadoPlatillosOrdenadosPojo(){

    }

    public ListadoPlatillosOrdenadosPojo(int cantidad, String nombrePlatillo, ArrayList<String> matrizPlatillo){
        this.cantidad = cantidad;
        this.nombrePlatillo = nombrePlatillo;
        this.matrizPlatillo = matrizPlatillo;
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

    public ArrayList<String> getMatrizPlatillo() {
        return matrizPlatillo;
    }

    public void setMatrizPlatillo(ArrayList<String> matrizPlatillo) {
        this.matrizPlatillo = matrizPlatillo;
    }
}
