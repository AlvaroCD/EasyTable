package com.example.easytable;

public class RecursosPojo {

    private String nombreDeIngrediente;
    private Boolean disponibilidad;

    public RecursosPojo() {

    }

    public RecursosPojo(String nombreDeIngrediente, Boolean disponibilidad){
        this.nombreDeIngrediente = nombreDeIngrediente;
        this.disponibilidad = disponibilidad;
    }

    public String getNombreDeIngrediente() {
        return nombreDeIngrediente;
    }

    public void setNombreDeIngrediente(String nombreDeIngrediente) {
        this.nombreDeIngrediente = nombreDeIngrediente;
    }

    public Boolean getDisponibilidad() {
        return disponibilidad;
    }

    public void setDisponibilidad(Boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}
