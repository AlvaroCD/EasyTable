package com.example.easytable;

public class ComentarioPojo {
    private String comentario, nombreDelLocalComentado;

    public ComentarioPojo(){

    }

    public ComentarioPojo(String comentario, String nombreDelLocalComentado) {
        this.comentario = comentario;
        this.nombreDelLocalComentado = nombreDelLocalComentado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getNombreDelLocalComentado() {
        return nombreDelLocalComentado;
    }

    public void setNombreDelLocalComentado(String nombreDelLocalComentado) {
        this.nombreDelLocalComentado = nombreDelLocalComentado;
    }


}
