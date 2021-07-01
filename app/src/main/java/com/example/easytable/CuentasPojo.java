package com.example.easytable;

public class CuentasPojo {

    private String mesa;
    private boolean pagado;

    public CuentasPojo(){

    }


    public CuentasPojo(String mesa, boolean pagado) {
        this.mesa = mesa;
        this.pagado = pagado;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public boolean isPagado() {
        return pagado;
    }

    public void setPagado(boolean pagado) {
        this.pagado = pagado;
    }
}
