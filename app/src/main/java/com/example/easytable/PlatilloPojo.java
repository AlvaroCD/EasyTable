package com.example.easytable;

public class PlatilloPojo {

    private String nombreLocal;
    private String nombrePlatillo;
    private String descripcion;
    private String idPlatillo;
    private int precio;
    private boolean disponibilidad;

    public PlatilloPojo() {
    }


    public PlatilloPojo(String nombreLocal, String nombrePlatillo, String descripcion,
                        String idPlatillo, int precio, boolean disponibilidad) {
        this.nombreLocal = nombreLocal;
        this.nombrePlatillo = nombrePlatillo;
        this.descripcion = descripcion;
        this.idPlatillo = idPlatillo;
        this.precio = precio;
        this.disponibilidad = disponibilidad;
    }

    public String getIdPlatillo() {
        return idPlatillo;
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
