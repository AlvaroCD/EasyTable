package com.example.easytable;

import java.util.List;

public class ListadoOrdenesPojo {

    private int statusPreparacion;
    private String idPrincipal, mesa;
    private List<String> matrizPlatillos;

    public ListadoOrdenesPojo(){

    }

    public ListadoOrdenesPojo(String mesa, int statusPreparacion, String idPrincipal, List<String> matrizPlatillos){
        this.mesa = mesa;
        this.statusPreparacion = statusPreparacion;
        this.idPrincipal = idPrincipal;
        this.matrizPlatillos = matrizPlatillos;

    }

    public int getStatusPreparacion() {
        return statusPreparacion;
    }

    public void setStatusPreparacion(int statusPreparacion) {
        this.statusPreparacion = statusPreparacion;
    }

    public String getIdPrincipal() {
        return idPrincipal;
    }

    public void setIdPrincipal(String idPrincipal) {
        this.idPrincipal = idPrincipal;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public List<String> getMatrizPlatillos() {
        return matrizPlatillos;
    }

    public void setMatrizPlatillos(List<String> matrizPlatillos) {
        this.matrizPlatillos = matrizPlatillos;
    }
}
