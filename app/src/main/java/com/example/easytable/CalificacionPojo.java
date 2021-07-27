package com.example.easytable;

public class CalificacionPojo {

    private long cantidad;
    private String costo, nombrePlatillo;

    public CalificacionPojo(){

    }


    public CalificacionPojo(long cantidad, String costo, String nombrePlatillo) {
        this.cantidad = cantidad;
        this.costo = costo;
        this.nombrePlatillo = nombrePlatillo;
    }

    public long getCantidad() {
        return cantidad;
    }

    public void setCantidad(long cantidad) {
        this.cantidad = cantidad;
    }

    public String getCosto() {
        return costo;
    }

    public void setCosto(String costo) {
        this.costo = costo;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
        this.nombrePlatillo = nombrePlatillo;
    }
}
