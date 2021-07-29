package com.example.easytable;

public class QuejasPojo {

    private String quejaCliente, mesa;

    public QuejasPojo(){

    }

    public QuejasPojo(String quejaCliente, String mesa) {
        this.quejaCliente = quejaCliente;
        this.mesa = mesa;
    }

    public String getQuejaCliente() {
        return quejaCliente;
    }

    public void setQuejaCliente(String quejaCliente) {
        this.quejaCliente = quejaCliente;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }
}
