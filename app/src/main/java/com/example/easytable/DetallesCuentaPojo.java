package com.example.easytable;

public class DetallesCuentaPojo {

    private String nombrePersona;


    public DetallesCuentaPojo(){

    }

    public DetallesCuentaPojo(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }

    public String getNombrePersona() {
        return nombrePersona;
    }

    public void setNombrePersona(String nombrePersona) {
        this.nombrePersona = nombrePersona;
    }
}
