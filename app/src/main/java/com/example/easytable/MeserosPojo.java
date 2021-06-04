package com.example.easytable;

public class MeserosPojo {

    private String Nombre, Apellidos;

    public MeserosPojo(){

    }

    public MeserosPojo(String Nombre, String Apellidos){
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }
}
