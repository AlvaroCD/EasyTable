package com.example.easytable;

public class PlatillosCuentasPojo {

    private String especificaciones, nombrePlatillo, costo;
    private long cantidad;

    public PlatillosCuentasPojo(){

    }

    public PlatillosCuentasPojo(String especificaciones, String nombrePlatillo, long cantidad, String costo) {
        this.especificaciones = especificaciones;
        this.nombrePlatillo = nombrePlatillo;
        this.cantidad = cantidad;
        this.costo = costo;
    }

    public String getEspecificaciones() {
        return especificaciones;
    }

    public void setEspecificaciones(String especificaciones) {
        this.especificaciones = especificaciones;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
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
}
