package com.example.easytable;

public class RestaurantePojo {

    private String descripcionRestaurante, direccion, nombreLocal, tipoRestaurante, rfc;
    private int cantidadMesas;

    public RestaurantePojo(){

    }

    public RestaurantePojo(String descripcionRestaurante, String direccion, String nombreLocal, String tipoRestaurante, String rfc, int cantidadMesas) {
        this.descripcionRestaurante = descripcionRestaurante;
        this.direccion = direccion;
        this.nombreLocal = nombreLocal;
        this.tipoRestaurante = tipoRestaurante;
        this.cantidadMesas = cantidadMesas;
        this.rfc = rfc;
    }

    public String getDescripcionRestaurante() {
        return descripcionRestaurante;
    }

    public void setDescripcionRestaurante(String descripcionRestaurante) {
        this.descripcionRestaurante = descripcionRestaurante;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getTipoRestaurante() {
        return tipoRestaurante;
    }

    public void setTipoRestaurante(String tipoRestaurante) {
        this.tipoRestaurante = tipoRestaurante;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getCantidadMesas() {
        return cantidadMesas;
    }

    public void setCantidadMesas(int cantidadMesas) {
        this.cantidadMesas = cantidadMesas;
    }
}
