package com.example.easytable;

public class VentasPojo {
    private int montoPagar;
    private String idPrincipal;

    public VentasPojo() {

    }

    private String mesa;

    public int getMontoPagar() {
        return montoPagar;
    }

    public void setMontoPagar(int montoPagar) {
        this.montoPagar = montoPagar;
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

    public VentasPojo(int montoPagar, String idPrincipal, String mesa) {
        this.montoPagar = montoPagar;
        this.idPrincipal = idPrincipal;
        this.mesa = mesa;
    }
}
