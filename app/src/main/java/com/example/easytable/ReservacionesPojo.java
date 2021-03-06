package com.example.easytable;

import com.google.firebase.Timestamp;
import com.google.type.DateTime;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ReservacionesPojo {

    private String Usuario;
    private String fecha;
    private String hora;

    private String idUsuario;
    private int cantidadPersonas, statusReservacion;

    public ReservacionesPojo() {

    }

    public ReservacionesPojo(String Usuario, int cantidadPersonas, int statusReservacion, String fecha, String hora, String idUsuario){
        this.Usuario = Usuario;
        this.cantidadPersonas = cantidadPersonas;
        this.statusReservacion = statusReservacion;
        this.fecha = fecha;
        this.hora = hora;
        this.idUsuario = idUsuario;

    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public int getStatusReservacion() {
        return statusReservacion;
    }

    public void setStatusReservacion(int statusReservacion) {
        this.statusReservacion = statusReservacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String  getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
