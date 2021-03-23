package com.example.easytable;

import com.google.firebase.firestore.FirebaseFirestore;

public class RestaurantePojo {

    private String descripcionRestaurante, direccion, nombreLocal, tipoRestaurante, rfc;
    private int cantidadMesas;

    public RestaurantePojo() {
    }

    public String getDescripcionRestaurante() {
        return descripcionRestaurante;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public String getTipoRestaurante() {
        return tipoRestaurante;
    }

    public String getRfc() {
        return rfc;
    }

    public int getCantidadMesas() {
        return cantidadMesas;
    }

}
