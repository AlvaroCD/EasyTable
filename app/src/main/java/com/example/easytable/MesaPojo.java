package com.example.easytable;

public class MesaPojo {

    private String idLocal;
    private String nombreLocal;
    private String idMesa;
    private int numeroMesa;
    private Boolean statusMesa;

    public MesaPojo() {
    }

    public MesaPojo(String idLocal, String nombreLocal, String idMesa, int numeroMesa, Boolean statusMesa) {
        this.idLocal = idLocal;
        this.nombreLocal = nombreLocal;
        this.numeroMesa = numeroMesa;
        this.statusMesa = statusMesa;
        this.idMesa = idMesa;
    }


    public String getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(String idLocal) {
        this.idLocal = idLocal;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public Boolean getStatusMesa() {
        return statusMesa;
    }

    public void setStatusMesa(Boolean statusMesa) {
        this.statusMesa = statusMesa;
    }

}
