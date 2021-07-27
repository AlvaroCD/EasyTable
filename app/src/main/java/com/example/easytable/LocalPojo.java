package com.example.easytable;

public class LocalPojo {

    private String idMesa, idDelLocal;
    private int numeroMesa;
    private boolean statusMesa;

    public LocalPojo(String idMesa, String idDelLocal, int numeroMesa, boolean statusMesa) {
        this.idMesa = idMesa;
        this.idDelLocal = idDelLocal;
        this.numeroMesa = numeroMesa;
        this.statusMesa = statusMesa;
    }

    public LocalPojo(){

    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
    }

    public String getIdDelLocal() {
        return idDelLocal;
    }

    public void setIdDelLocal(String idDelLocal) {
        this.idDelLocal = idDelLocal;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public boolean isStatusMesa() {
        return statusMesa;
    }

    public void setStatusMesa(boolean statusMesa) {
        this.statusMesa = statusMesa;
    }
}
