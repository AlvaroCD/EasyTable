package com.example.easytable;

public class PlatilloPojo {

    private String nombreLocal, nombrePlatillo, descripcion;
    private int precio;
    private boolean disponibilidad;

    public PlatilloPojo() {
    }

    public PlatilloPojo(String nombreLocal, String nombrePlatillo, int precio, boolean disponibilidad, String descripcion) {
        this.nombreLocal = nombreLocal;
        this.nombrePlatillo = nombrePlatillo;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
        this.descripcion = descripcion;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNombrePlatillo() {
        return nombrePlatillo;
    }

    public void setNombrePlatillo(String nombrePlatillo) {
        this.nombrePlatillo = nombrePlatillo;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public boolean isDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}
