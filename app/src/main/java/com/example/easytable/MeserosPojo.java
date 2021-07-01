package com.example.easytable;

public class MeserosPojo {

    private String Nombre;
    private String Apellidos;
    private String Cuenta;

    private String mesa;

    public MeserosPojo(){

    }

    public MeserosPojo(String mesa) {
        this.mesa = mesa;
    }

    public MeserosPojo(String Nombre, String Apellidos, String Cuenta){
        this.Nombre = Nombre;
        this.Apellidos = Apellidos;
        this.Cuenta = Cuenta;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public String getCuenta() { return Cuenta; }

    public void setCuenta(String cuenta) { Cuenta = cuenta; }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
    }
}
